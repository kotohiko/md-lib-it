在 Java 中，`ThreadLocal` 是一种用于存储线程局部变量的机制。每个线程都可以独立地改变其副本，不会影响其他线程所对应的副本。它提供了一种将数据与特定线程关联起来的方式，可以用来为每个线程创建一个单独的数据副本。



## `ThreadLocal` 原理

### 内部结构

`ThreadLocal` 内部维护了一个 `ThreadLocalMap` 类型的成员变量，这个 `ThreadLocalMap` 本质上是一个自定义的哈希表。

每个 `Thread` 都有一个 `ThreadLocal.ThreadLocalMap threadLocals` 的实例变量

```java
package java.lang;

import ...
    
public class Thread implements Runnable {
    
    ...
    
	/*
     * ThreadLocal values pertaining to this thread. This map is maintained
     * by the ThreadLocal class.
     */
    ThreadLocal.ThreadLocalMap threadLocals;
    
    ...
        
}
```

用来存储属于该线程的 `ThreadLocal` 变量。

### 键值对存储

`ThreadLocalMap` 使用 `WeakReference` 来保存 `ThreadLocal` 对象作为键（key），而实际存储的值则是与之相关的线程局部变量。

使用 `WeakReference` 的好处是，当 `ThreadLocal` 实例不再被外部引用时，垃圾收集器可以回收这些对象，避免内存泄漏。

### 存取操作

- 当调用 `ThreadLocal` 的 `set(T value)` 方法时，实际上是在当前线程的 `ThreadLocalMap` 中以当前 `ThreadLocal` 实例为键来设置值。
- 调用 `get()` 方法时，则会从当前线程的 `ThreadLocalMap` 中查找对应的值；如果找不到（即第一次访问），则会先初始化一个值再返回。

### 线程隔离

不同线程之间的 `ThreadLocal` 数据是互相隔离的。每个线程只能访问到自己的 `ThreadLocalMap` 中的数据，这样就实现了线程间的局部变量隔离。

### 内存泄漏问题

- 如果 `ThreadLocal` 不再被使用但线程仍然存活，那么 `ThreadLocalMap` 中的强引用会导致无法回收与 `ThreadLocal` 关联的值，造成内存泄漏。
- Java 6 后引入了弱引用和清理机制来缓解这个问题，但是开发者仍需注意适时清理无用的 `ThreadLocal` 变量。

## 示例代码

```java
public class ThreadLocalExample {
    public static void main(String[] args) {
        ThreadLocal<String> localVariable = new ThreadLocal<>();
        
        // 设置当前线程的局部变量
        localVariable.set("main thread value");
        
        System.out.println(localVariable.get()); // 输出: main thread value
        
        // 新建一个线程并设置不同的局部变量
        new Thread(() -> {
            localVariable.set("new thread value");
            System.out.println(localVariable.get()); // 输出: new thread value
        }).start();
        
        // 主线程输出自己的局部变量
        System.out.println(localVariable.get()); // 输出: main thread value
    }
}
```

通过这段代码可以看到，主线程和新线程各自拥有自己的一份 `localVariable` 的值，互不影响。

在面试中提到 `ThreadLocal` 时，展示你对其内部工作原理的理解以及如何正确使用它是非常重要的。同时也要提及关于内存泄漏的潜在风险及如何预防这些问题。