## 1   使用场景

根据「申请人」查询「转移部门」所有「资产转移单」数量。



## 2   现场复盘

### 2.1   原 SQL

查询条件部门使用「子查询」，「资产转移表」部门字段未建索引，全表扫描未走索引执行时间超 7s。

```mysql
SELECT
  COUNT( * ) 
FROM
  ASSET_SHIFT SHIFT
  JOIN process_hi_backlog backlog ON BUSINESS_KEY_ = shift_uuid 
WHERE
  SHIFT.DELETE_STATUS = 0 
  AND SHIFT.COMPANY = ? 
  AND shift.shift_dept IN ( SELECT ORG_ID FROM PERMISSION_STAFF_ORGANIZATION WHERE STAFF_ID = ? )
```

【表信息整理】

-   `ASSET_SHIFT SHIFT`：资产转移表（包含字段 `shift_uuid`、`DELETE_STATUS`、`COMPANY`、`shift_dept` 等）。
-   `process_hi_backlog backlog`：过程后台表（包含字段 `BUSINESS_KEY_`，用于连接 `ASSET_SHIFT` 表的 `shift_uuid` 字段）。
-   `PERMISSION_STAFF_ORGANIZATION`：权限-员工-组织表，用于存储员工与组织的关系。

【查询目标分析】查询满足条件的资产转移单的数量。条件包括：

-   资产转移单的 `DELETE_STATUS` 为 `0`，表示未被删除。
-   资产转移单属于指定的公司（`SHIFT.COMPANY = ?`）。
-   `shift.shift_dept` 在 `PERMISSION_STAFF_ORGANIZATION` 表中满足员工 ID 为指定值的组织 ID 列表中。

【查询逻辑分析】

1.  先通过 `JOIN` 语句，将 `ASSET_SHIFT` 和 `process_hi_backlog` 表连接，条件是 `ASSET_SHIFT` 的 `shift_uuid` 等于 `process_hi_backlog` 表中的 `BUSINESS_KEY_`。
2.  然后，通过 `WHERE` 条件筛选数据。
3.  `SHIFT.DELETE_STATUS = 0` 和 `SHIFT.COMPANY = ?` 是直接条件。
4.  `shift.shift_dept IN (...)` 是一个子查询，表示只保留 `shift_dept` 存在于指定员工的组织 ID 列表中的数据。

【瓶颈分析】

由于 `shift_dept` 字段上没有索引，`IN` 子查询会导致全表扫描，这样查询效率较低。当数据量较大时，执行时间可能较长。

### 2.2   SQL 优化方案

```mysql
SELECT
  COUNT( * ) 
FROM
  ASSET_SHIFT SHIFT
  JOIN process_hi_backlog backlog ON BUSINESS_KEY_ = shift_uuid
  JOIN PERMISSION_STAFF_ORGANIZATION org ON shift.shift_dept = org.ORG_ID 
WHERE
  SHIFT.COMPANY = '' 
  AND org.staff_id = '' 
  AND SHIFT.DELETE_STATUS = 0
```

【优化思路】

-   将 `shift.shift_dept IN (...)` 子查询改成 `JOIN`。
-   直接通过 `JOIN PERMISSION_STAFF_ORGANIZATION org` 的方式，把 `shift_dept` 字段和 `ORG_ID` 进行匹配。
-   把原来 `WHERE` 中的 `STAFF_ID = ?` 条件放在 `org.staff_id = ?` 的 `JOIN` 连接上。

【查询逻辑分析】

-   第一个 `JOIN` 依旧是 `ASSET_SHIFT` 和 `process_hi_backlog` 表，通过 `shift_uuid` 和 `BUSINESS_KEY_` 字段匹配。
-   新增的 `JOIN` 将 `ASSET_SHIFT` 表与 `PERMISSION_STAFF_ORGANIZATION` 表通过 `shift_dept` 和 `ORG_ID` 字段匹配。
-   最后，通过 `WHERE` 条件保留符合 `SHIFT.COMPANY = ?` 和 `DELETE_STATUS = 0` 的记录。

【性能提升点】

-   `JOIN` 比 `IN` 子查询效率更高，因为 `JOIN` 可以利用索引（假设 `ORG_ID` 和 `STAFF_ID` 有索引）。
-   优化后的查询只在一条查询路径中完成数据匹配，减少了子查询的开销。

### 2.3   对比与总结

-   原查询：通过 `IN` 子查询先得到符合员工 ID 的 `ORG_ID` 列表，再在 `ASSET_SHIFT` 表中查找符合条件的记录。这种方式导致 `shift_dept` 的全表扫描，速度较慢。
-   优化后查询：通过 `JOIN` 直接关联 `ASSET_SHIFT` 和 `PERMISSION_STAFF_ORGANIZATION` 表，将数据匹配转移到 `JOIN` 中，减少子查询和全表扫描的开销。

总体来说，这种优化将会显著提升查询速度，特别是在 `PERMISSION_STAFF_ORGANIZATION` 表有适当索引时，能够最大化利用数据库的连接效率。