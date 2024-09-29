（本文整理自通义千问）



`HashMap` 是 Java 中非常常用的集合类之一，它实现了 `Map` 接口，提供了基于哈希表的数据结构。下面详细讲解 `HashMap` 的扩容机制及其内部实现原理。



## 1   基本概念

`HashMap` 使用**哈希表**来存储键值对。每个键值对存储在一个 `Node`（在 Java 8 中是 `Node<K,V>`，在 Java 9 及以后版本中是 `HashMap.Node<K,V>`）对象中。当向 `HashMap` 中添加元素时，`HashMap` 会计算键的哈希码，并根据哈希码确定元素在数组中的索引位置。



## 2   初始容量与负载因子

在创建 `HashMap` 时，可以指定**初始容量（initial capacity）**和**负载因子（load factor）**。如果没有指定，默认情况下初始容量为 16，负载因子为 0.75。

- 初始容量：`HashMap` 的初始大小。
- 负载因子：`HashMap` 能够容忍的最大填充程度，它是哈希表中键值对数量与哈希表大小的比例。负载因子越大，哈希碰撞的可能性越高，性能会下降。



## 3   扩容触发条件

`HashMap` 的扩容主要发生在以下两种情况：

1. 插入元素时：当 `HashMap` 中的键值对数量超过了当前容量与负载因子的乘积时，`HashMap` 会自动进行扩容。
2. 调用 `putAll` 方法时：如果调用 `putAll` 方法，`HashMap` 也会检查是否需要扩容。



## 4   扩容算法

扩容算法的主要步骤如下：

1. 计算新的容量：新的容量通常是当前容量的两倍。
2. 重新分配元素：将旧数组中的元素重新计算哈希值，并放入新数组的相应位置。

### 扩容的具体实现

在 Java 8 中，`HashMap` 的扩容实现如下：

```java
final Node<K, V>[] resize() {
    Node<K, V>[] oldTab = table;
    int oldCap = (oldTab == null) ? 0 : oldTab.length;
    int oldThr = threshold;
    int newCap, newThr = 0;

    // 如果当前容量为 0，则按照默认容量初始化
    if (oldCap > 0) {
        // 如果当前容量大于最大容量 MAXIMUM_CAPACITY，则不再扩容
        if (oldCap >= MAXIMUM_CAPACITY) {
            threshold = Integer.MAX_VALUE; // 用于 putAll 等场景
            return oldTab;
        } else if ((newCap = oldCap << 1) < MAXIMUM_CAPACITY && oldCap >= DEFAULT_INITIAL_CAPACITY) {
            newThr = oldThr << 1; // double threshold
        }
    } else if ((newCap = DEFAULT_INITIAL_CAPACITY) > 0) {
        newThr = loadFactor * DEFAULT_INITIAL_CAPACITY;
    }

    // 如果 newCap 为负数，则意味着计算过程中发生了整数溢出，设置 newCap 为最大容量
    if (newThr == 0) {
        float ft = (float) newCap * loadFactor;
        newThr = (newCap < MAXIMUM_CAPACITY && ft < (float) MAXIMUM_CAPACITY ?
                (int) ft : Integer.MAX_VALUE);
    }

    threshold = newThr;
    @SuppressWarnings({"rawtypes", "unchecked"})
    Node<K, V>[] newTab = (Node<K, V>[]) new Node[newCap];
    table = newTab;
    if (oldTab != null) {
        // 重新分配元素
        for (int j = 0; j < oldCap; ++j) {
            Node<K, V> e;
            if ((e = oldTab[j]) != null) {
                oldTab[j] = null;
                if (e.next == null)
                    newTab[e.hash & (newCap - 1)] = e;
                else if (e instanceof TreeNode)
                    ((TreeNode<K, V>) e).split(this, newTab, j, oldCap);
                else { // preserve order
                    Node<K, V> loHead = null, loTail = null;
                    Node<K, V> hiHead = null, hiTail = null;
                    Node<K, V> next;
                    do {
                        next = e.next;
                        if ((e.hash & oldCap) == 0) {
                            if (loTail == null)
                                loHead = e;
                            else
                                loTail.next = e;
                            loTail = e;
                        } else {
                            if (hiTail == null)
                                hiHead = e;
                            else
                                hiTail.next = e;
                            hiTail = e;
                        }
                    } while ((e = next) != null);
                    if (loTail != null) {
                        loTail.next = null;
                        newTab[j] = loHead;
                    }
                    if (hiTail != null) {
                        hiTail.next = null;
                        newTab[j + oldCap] = hiHead;
                    }
                }
            }
        }
    }
    return oldTab;
}
```



## 5   扩容过程

1. 计算新容量：新的容量通常是当前容量的两倍。
2. 创建新数组：创建一个新的数组，大小为当前容量的两倍。
3. 重新分配元素：将旧数组中的每个元素重新计算哈希值，并放入新数组的相应位置。由于数组大小变化，元素的位置可能发生变化。



## 6   为什么选择 0.75 作为负载因子？

负载因子的选择是为了在空间利用率和查找效率之间找到一个平衡点。负载因子 0.75 表示当哈希表达到 75% 的填充度时就进行扩容。这样做可以减少哈希冲突的概率，从而提高查找效率，同时又不至于浪费太多内存空间。



## 7   为什么容量是 2 的幂次方？

选择容量为 2 的幂次方是因为这可以使得哈希码与数组索引之间的计算更简单高效。使用位运算（如 `hash & (capacity - 1)`）代替模运算（如 `hash % capacity`），可以显著提高性能。



## 总结

`HashMap` 的扩容机制是在达到一定阈值时自动将容量翻倍，并重新分配元素。==合理的初始容量和负载因子的选择对于优化性能非常重要。==扩容过程涉及到重新计算哈希值并将元素重新放置到新的位置，这是一个相对耗时的操作，但在实际应用中，合理的参数设置可以有效减少扩容的频率，从而提高整体性能。