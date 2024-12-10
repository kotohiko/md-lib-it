## 1   使用场景

根据「申请人」查询「当前申请人所在部门」所有人员「资产变更」申请数量。



## 2   现场复盘

### 2.1   原 SQL

查询条件使用「子查询」导致「变更申请表」全表扫描未走索引，执行时间超 10s。

```mysql
SELECT
    COUNT( * )
FROM
    ASSET_MODIFY MODIFYS
WHERE
    MODIFYS.is_del = 0
    AND modifys.creator IN (
        SELECT id 
        FROM sys_staff 
        WHERE dept_id IN (
            SELECT ORG_ID 
            FROM PERMISSION_STAFF_ORGANIZATION 
            WHERE STAFF_ID = ''
        )
    )
```

【执行过程分析】

- 依次计算内层子查询的结果（如 `ORG_ID` 列表），然后传递给外层子查询。
- 外层子查询计算出符合条件的 `id` 列表，再传递给主查询的 `IN` 子句。
- 每一层子查询都需要单独的执行和传递，耗时较多。

【性能存在问题分析】

- **嵌套子查询**

    - 内层子查询

        ```mysql
        SELECT ORG_ID 
        FROM PERMISSION_STAFF_ORGANIZATION 
        WHERE STAFF_ID = ''
        ```

        需要先获取 `ORG_ID` 列表，然后将结果传递给外层子查询。

    - 外层子查询

        ```mysql
        SELECT id 
        FROM sys_staff 
        WHERE dept_id IN (...)
        ```

        再基于内层查询的结果查找 `sys_staff` 的 `id`。这种「嵌套式」的执行可能会导致数据库频繁地切换上下文。

- **无法充分利用索引。**子查询中的 `IN` 语句通常无法很好地利用索引，特别是当 `dept_id` 或 `ORG_ID` 没有适当的索引时，容易导致全表扫描。

- **子查询重复计算。**在主查询的执行过程中，数据库可能会多次重新计算子查询的结果，增加了开销。

- **可读性较低。**嵌套的子查询使得语句的逻辑层次复杂，可读性较差，不利于维护和优化。

- **消耗内存**。可能导致大量临时数据存储在内存中，严重影响性能。

### 2.2   优化方案

修改 `IN` 为关联查询

```mysql
SELECT
    COUNT( * )
FROM
    ASSET_MODIFY MODIFYS,
    sys_staff staff,
    PERMISSION_STAFF_ORGANIZATION org
WHERE
    MODIFYS.creator = staff.id
    AND staff.dept_id = org.ORG_ID
    AND org.STAFF_ID = ''
    AND MODIFYS.is_del = 0
```

【优化思路分析】

- **将子查询改为 `JOIN`。**
    - 原来需要通过两层子查询获取 `ORG_ID` 和 `sys_staff.id`，现在通过 `JOIN` 直接将三张表关联起来：
        - `ASSET_MODIFY.MODIFYS.creator = sys_staff.id`
        - `sys_staff.dept_id = PERMISSION_STAFF_ORGANIZATION.ORG_ID`
    - 这种方式让数据库可以直接利用连接条件优化查询路径，避免了多次计算子查询结果。
    
- **减少重复计算。**原查询中，内层和外层子查询可能被重复计算多次，而 `JOIN` 是一次性完成数据筛选，大大减少了重复操作。

- **提高索引利用效率。**
    - 在 `JOIN` 条件中，数据库可以使用 `MODIFYS.creator`、`staff.id`、`staff.dept_id` 和 `org.ORG_ID` 等字段上的索引来加速查询。
    
    - 假设表有以下索引
        - `ASSET_MODIFY.creator` 索引
        - `sys_staff.id` 和 `sys_staff.dept_id` 索引
        - `PERMISSION_STAFF_ORGANIZATION.ORG_ID` 和 `PERMISSION_STAFF_ORGANIZATION.STAFF_ID` 索引
        
        这些索引会使 `JOIN` 查询效率大幅提升。
    
- **简化查询逻辑。**优化后的查询逻辑平铺直叙，没有嵌套的子查询，查询更直观易懂，也便于后续的维护。

【优化后执行过程分析】

- 通过 `JOIN`，一次性关联三张表，数据库优化器可以利用索引直接生成最优执行计划。

- 避免了嵌套子查询带来的上下文切换和重复计算。



# 3   子查询该怎样使用？

尽管 `JOIN` 通常更高效，但子查询也有其适用场景：

- 子查询的结果是一个小数据集，且这个数据集不需要和主查询进行复杂的关联操作。
- 查询逻辑清晰、简单时，使用子查询可以更易读。
- 子查询只需要执行一次，不会被主查询多次使用时。



# 4   总结

优化后的查询通过将子查询替换为 `JOIN`，显著减少了重复计算和嵌套逻辑，充分利用了索引的性能优势。在大部分情况下，这样的优化能够极大地提升查询效率，特别是数据量较大时。