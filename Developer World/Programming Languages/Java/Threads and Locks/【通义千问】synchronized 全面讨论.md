## 1   如何使用 `synchronized`



## 2   `synchronized` 的原理

Java 中的 `synchronized` 关键字是基于「对象监视器（Monitor）」机制来实现线程同步的。监视器是一个同步工具，==它允许线程在进入一个代码块或方法之前获取锁，并在退出时释放锁==。下面详细介绍 `synchronized` 的工作原理：

### 2.1   对象监视器（Monitor）

==每个 Java 对象都有一个与之关联的监视器==，这个监视器本质上是一个「内置锁」（也称为内部锁或互斥锁）。当一个线程试图访问由 `synchronized` 保护的代码块或方法时，==它必须首先获得该对象的监视器锁。==

- **对于实例方法**：监视器是该实例对象本身。
- **对于静态方法**：监视器是类的 `Class` 对象。
- **对于同步代码块**：监视器可以是任意对象，通过 `synchronized (obj)` 指定。

### 2.2   锁的状态

在 JVM 中，对象头包含了一个指向锁信息的指针。根据不同的状态，锁可以分为以下几种类型：

- **无锁状态（Unlocked）**：
   - 对象未被任何线程锁定。

- **偏向锁（Biased Locking）**：
   - 偏向锁是一种优化手段，用于减少无竞争情况下的同步开销。如果一个线程多次获取同一个锁，JVM 可以将这个锁标记为偏向于该线程，从而在后续获取锁时不需要进行 CAS 操作。
   - 如果其他线程尝试获取这个锁，则会撤销偏向锁，升级为轻量级锁。

- **轻量级锁（Lightweight Locking）**：
   - 轻量级锁使用 CAS 操作来避免重量级锁带来的性能损耗。当线程尝试获取锁时，JVM 会在当前线程的栈帧中创建一个 Lock Record（锁记录），然后尝试用 CAS 将对象头中的 Mark Word 替换为指向这个锁记录的指针。
   - 如果 CAS 成功，表示没有其他线程竞争，线程获取了锁；如果 CAS 失败，表示有竞争，需要升级为重量级锁。

- **重量级锁（Heavyweight Locking）**：
   - 当多个线程竞争同一把锁时，轻量级锁会膨胀为重量级锁。这时，线程会被阻塞并放入操作系统的等待队列中，直到持有锁的线程释放锁后，其他线程才会被唤醒并重新竞争锁。

- **自旋锁（Spin Locking）**：
   - 在某些情况下，JVM 可能会让线程自旋而不是立即阻塞，即不断尝试获取锁，直到成功。自旋锁适用于预期锁很快就会被释放的情况。

### 2.3   获取和释放锁的过程

- **获取锁**：
  - 线程尝试获取锁时，会检查对象的 Mark Word。
  - 如果是无锁状态，线程会尝试使用 CAS 将 Mark Word 更改为指向自己栈帧中的锁记录。
  - 如果 CAS 成功，线程获取了轻量级锁；如果 CAS 失败，表示有竞争，可能升级为重量级锁。
  - 如果已经是重量级锁，线程会被阻塞并加入等待队列。

- **释放锁**：
  - 当线程执行完同步代码块或方法时，或者抛出异常时，会自动释放锁。
  - 释放锁时，JVM 会将对象的 Mark Word 恢复到原始状态，如果是重量级锁，还会唤醒等待队列中的一个线程。

### 2.4   性能考虑

- **偏向锁**和**轻量级锁**是为了减少多线程环境下的同步开销而设计的，它们可以显著提高程序的性能。
- **重量级锁** 会导致线程阻塞，涉及操作系统级别的上下文切换，开销较大。

### 2.5   示例

```java
public class SynchronizedExample {
    private int count = 0;

    public synchronized void increment() {
        count++;
    }

    public static void main(String[] args) {
        SynchronizedExample example = new SynchronizedExample();
        
        // 创建两个线程同时调用increment方法
        Thread t1 = new Thread(() -> {
            for (int i = 0; i < 1000; i++) {
                example.increment();
            }
        });

        Thread t2 = new Thread(() -> {
            for (int i = 0; i < 1000; i++) {
                example.increment();
            }
        });

        t1.start();
        t2.start();

        try {
            t1.join();
            t2.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        System.out.println("Count: " + example.count); // 应该输出2000
    }
}
```

在这个示例中，`increment` 方法被 `synchronized` 修饰，确保每次只有一个线程可以修改 `count` 变量，从而保证了线程安全。



## 3   `synchronized` 的实际应用

