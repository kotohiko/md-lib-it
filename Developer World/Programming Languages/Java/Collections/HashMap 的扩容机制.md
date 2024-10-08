在 Java 中，`HashMap` 是基于哈希表实现的键值对存储结构。`HashMap` 的扩容机制是它能够高效存储和查找数据的一个重要环节。下面将从几个关键点详细解释 `HashMap` 的扩容机制。

### 1   基本概念
`HashMap` 使用数组加链表（或树）的方式来存储元素。数组中的每一个位置称为「桶（bucket）」，当多个键的哈希值冲突时，元素会以「链表」或「红黑树」的形式存储在桶内。==随着元素的不断增加，哈希冲突可能会增多，从而影响查询性能。因此需要进行**扩容**。==

### 2   何时触发扩容

`HashMap` 扩容的触发条件与**负载因子（load factor）**有关。默认情况下，`HashMap` 的初始容量是 16，默认的负载因子是 0.75。即当 `HashMap` 中元素的数量超过当前容量的 75% 时，触发扩容操作。例如，在默认配置下，当插入第 13 个元素时（16 * 0.75 = 12），会触发扩容。

### 3   扩容过程
扩容时，`HashMap` 的容量会**翻倍**，并且需要将现有元素重新分布到新的数组中，这个过程称为 **rehash**。具体步骤如下：
1. **容量翻倍**：扩容时，新数组的大小是原数组大小的两倍。
2. **重新计算哈希值**：对于现有的每个键值对，`HashMap` 会根据新数组的容量重新计算哈希值，以确定新的位置。
3. **分配位置**：重新计算哈希值后，需要将元素放入新数组对应的桶中。如果多个元素的哈希值映射到同一个桶，则它们会按照链表或红黑树的结构存储。

### 4   rehash 的优化
在 JDK 8 之后，`HashMap` 进行了 rehash 操作的优化。==在重新分配桶位时，`HashMap` 不再重新计算哈希值，而是通过简单的位运算来判断元素是保留在原位置，还是移动到新数组的 “新桶位”。==这个优化是基于扩容时容量的两倍特性，即旧索引和新索引的差异仅体现在高位的某一位上。

假设某个键的哈希值是 `h`，扩容前的数组长度是 `n`，扩容后的数组长度是 `2n`，则键的存放位置要么在索引 `h % n`，要么在索引 `h % n + n`。这样可以通过一次位运算判断键的存放位置，大大提高了 rehash 的效率。

### 5   树化
当一个桶中的链表长度超过一定阈值（默认是 8）时，`HashMap` 会将链表转换为红黑树结构，以提高查找效率。

### 6   总结
Java 中 `HashMap` 的扩容机制是为了应对哈希冲突和性能下降问题。当元素数量超过负载因子的阈值时，`HashMap` 通过扩容和重新分配元素来保持高效的查找性能。JDK 8 进一步优化了 rehash 的过程，减少了扩容带来的性能损耗。



