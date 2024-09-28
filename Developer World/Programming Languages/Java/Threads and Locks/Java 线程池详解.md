# 1   `Executors`



`Executors` 是一个工具类，提供了一系列静态工厂方法，用于创建不同类型的线程池。

```java
package java.util.concurrent;

import ...

/**
 * ...
 *
 * @since 1.5
 * @author Doug Lea
 */
public class Executors {
    ...
}
```

这些线程池都实现了 `ExecutorService` 接口，提供了管理和执行异步任务的功能。

实现 `ExecutorService` 接口的类都包括：

| 名称                       | 声明                                                         | 简介                                                         | 版本 |
| -------------------------- | ------------------------------------------------------------ | ------------------------------------------------------------ | ---- |
| `AbstractExecutorService`  | `public abstract class AbstractExecutorService implements ExecutorService {...}` | `AbstractExecutorService` 是 Java 并发包 `java.util.concurrent` 中的一个抽象类，它实现了 `ExecutorService` 接口。这个抽象类提供了一些默认实现的方法，使得开发者在创建自定义的 `ExecutorService` 实现时可以更加容易地遵循 `ExecutorService` 的规范。<br>`ExecutorService` 是一个更高级的接口，它扩展了 `Executor` 接口，并添加了管理终止和生成 `Future` 以便跟踪进度的功能。`ExecutorService` 提供了生命周期管理的方法，如关闭线程池、等待所有任务完成等。 | 5    |
| `ForkJoinPool`             | `public class ForkJoinPool extends AbstractExecutorService {...}` | 是 Java 7 引入的一个并行执行框架，主要用于处理大量的小任务，通过将大任务拆分为更小的子任务，并利用多个处理器核心来并行处理这些子任务，从而提高程序的执行效率。 | 7    |
| `ScheduledExecutorService` | `public interface ScheduledExecutorService extends ExecutorService {...}` | `ScheduledExecutorService` 是 JUC 中的一个接口，它继承自 `ExecutorService` 接口。`ScheduledExecutorService` 提供了定时和周期性执行任务的功能，类似于传统的 `Timer` 和 `TimerTask`，但提供了更强大的功能和更好的灵活性。 | 5    |
| `ThreadPoolExecutor`       | `public class ThreadPoolExecutor extends AbstractExecutorService {...}` |                                                              |      |
|                            |                                                              |                                                              |      |



## 1.1   为什么使用 `Executors`？

- **简化线程池创建：**`Executors` 提供了方便的方法，无需手动配置线程池的各种参数。
- **提供常用线程池类型：**`Executors` 提供了多种线程池类型，满足不同场景的需求。
- **隐藏底层复杂性：**`Executors` 封装了线程池的创建和管理细节，让开发者可以更专注于业务逻辑。



## 1.2   `Executors` 常用方法及对应线程池

| 方法                                       | 返回类型                   | 描述                                                         | 特点                                                         |
| ------------------------------------------ | -------------------------- | ------------------------------------------------------------ | ------------------------------------------------------------ |
| `newFixedThreadPool(int nThreads)`         | `ExecutorService`          | 创建一个固定大小的线程池。核心线程数和最大线程数相等，超出部分的任务会在队列中等待。 | 线程数固定，适合处理稳定的任务负载。<br>任务队列==无界==，可以无限排队。<br/>适用于==负载均衡==的场景。 |
| `newCachedThreadPool()`                    | `ExecutorService`          | 创建一个可缓存的线程池。如果线程池中的线程数量超过了处理需求，会回收空闲线程，如果任务数量增加，会创建新的线程。 | 线程数动态调整，适合处理==突发性==的任务。<br/>适用于执行很多短期异步任务的场景 |
| `newSingleThreadExecutor()`                | `ExecutorService`          | 创建一个只有一个线程的线程池。保证任务按照顺序执行。         | 只有一个线程，适合按顺序执行任务。<br/>适用于==串行执行==任务的场景。 |
| `newScheduledThreadPool(int corePoolSize)` | `ScheduledExecutorService` | 创建一个支持定时及周期性任务执行的线程池。                   | 支持定时和周期性任务。<br/>适用于需要定时执行任务的场景，如定时备份、定时清理等。 |

使用示例

```java
import java.util.concurrent.*;

public class ExecutorExample {
    public static void main(String[] args) {
        // 创建固定大小的线程池
        ExecutorService fixedThreadPool = Executors.newFixedThreadPool(5);
        for (int i = 0; i < 10; i++) {
            fixedThreadPool.execute(() -> {
                System.out.println(Thread.currentThread().getName() + " is running");
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            });
        }
        fixedThreadPool.shutdown();
    }
}
```



## 1.3   注意事项

- **线程池大小**：线程池大小的设置需要根据具体应用场景来调整，过大或过小都会影响性能。
- **任务类型**：不同的任务类型适合不同的线程池类型。
- **线程池关闭**：使用 `shutdown()` 方法来关闭线程池，并使用 `awaitTermination()` 方法等待所有任务执行完毕。



## 1.4   深入理解

- `ThreadPoolExecutor`：==`Executors` 中创建的线程池底层都是基于 `ThreadPoolExecutor` 实现的。==
- **核心参数**：`corePoolSize`、`maximumPoolSize`、`keepAliveTime`、`workQueue` 等参数对线程池的行为有重要影响。
- **拒绝策略**：当任务队列满了且线程池中的线程数达到最大时，会触发拒绝策略。





# 2   `ThreadPoolExecutor`

Java 中的 `ThreadPoolExecutor` 是一个非常强大的线程池实现，它提供了丰富的配置选项来满足各种不同的需求。



## 2.1   详解七个参数

在创建 `ThreadPoolExecutor` 实例时，通常需要指定七个主要参数。下面是这些参数的详细说明：

- `corePoolSize`（核心线程数）
    - 描述：线程池的基本大小，即线程池中==始终维持==的最小线程数。

    - 作用：即使没有任务执行，线程池也会保持至少 `corePoolSize` 个线程处于等待状态，==除非设置了 `allowCoreThreadTimeOut` 为 `true`==。

    - 默认值：==无默认值，必须显式指定。==

- `maximumPoolSize`（最大线程数）
    - 描述：线程池允许的最大线程数。
    - 作用：当活动线程数达到 `maximumPoolSize` 时，线程池不会创建新的线程，而是将任务放入队列中等待执行。
    - 默认值：==无默认值，必须显式指定。==

- `keepAliveTime`（非核心线程的存活时间）
    - 描述：当线程池中的线程数目大于 `corePoolSize` 时，多余的空闲线程最多存活的时间。
    - 作用：设置非核心线程在没有任务执行时等待新任务的最长时间，超时后会终止多余的线程。
    - 单位：需要指定时间单位，如秒 (`TimeUnit.SECONDS`) 或毫秒 (`TimeUnit.MILLISECONDS`)。

- `timeUnit`（时间单位）
    - 描述：与 `keepAliveTime` 相关的时间单位。
    - 作用：指定 `keepAliveTime` 参数的时间单位，如 `TimeUnit.SECONDS` 表示 `keepAliveTime` 的单位为秒。
- `workQueue`（任务队列）
    - 描述：用来存放等待执行的任务的阻塞队列。
    - 作用：当提交的任务数量超过 `corePoolSize` 时，超出的线程会被放入此队列等待执行。
    - 类型：`BlockingQueue<Runnable>`，常见的实现有 `ArrayBlockingQueue`、`LinkedBlockingQueue`、`SynchronousQueue` 等。
- `threadFactory`（线程工厂）
    - 描述：用于创建新线程的工厂。
    - 作用：可以自定义线程属性，如线程名称、优先级等。
    - 默认值：如果没有指定，则使用默认的 `threadFactory` 创建线程。
- `handler`（拒绝策略）
    - 描述：当任务队列已满且线程数达到 `maximumPoolSize` 时，如何处理新提交的任务。
    - 作用：定义了当无法接收更多任务时的行为。
    - 策略：常见的策略包括：
        - `AbortPolicy`：抛出 `RejectedExecutionException` 异常。
        - `CallerRunsPolicy`：由调用者所在的线程来运行任务。
        - `DiscardOldestPolicy`：抛弃队列中最老的一个任务，并重新尝试执行任务（只有当队列还有空间的情况下才适用）。
        - `DiscardPolicy`：不处理，丢弃掉任务。

【示例代码】以下是一个创建 `ThreadPoolExecutor` 的示例代码：

```java
import java.util.concurrent.*;

public class ThreadPoolExample {
    public static void main(String[] args) {
        ThreadPoolExecutor executor = new ThreadPoolExecutor(
            2, // corePoolSize
            5, // maximumPoolSize
            60L, // keepAliveTime
            TimeUnit.SECONDS, // timeUnit
            new ArrayBlockingQueue<>(3), // workQueue
            Executors.defaultThreadFactory(), // threadFactory
            new ThreadPoolExecutor.CallerRunsPolicy() // handler
        );

        // 提交任务
        for (int i = 0; i < 10; i++) {
            executor.execute(() -> {
                System.out.println(Thread.currentThread().getName() + " is processing");
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            });
        }

        // 关闭线程池
        executor.shutdown();
    }
}
```

这段代码创建了一个线程池，核心线程数为 2，最大线程数为 5，非核心线程的存活时间为 60 秒，任务队列使用的是 `ArrayBlockingQueue`，大小为 3。当线程池无法接受更多任务时，将由调用者线程执行任务。

通过合理配置这些参数，你可以创建出适合特定应用场景的线程池。不同的参数组合可以适应不同的负载和性能要求。



## 2.2   工作流程

`ThreadPoolExecutor` 的工作流程是一个典型的**任务分配和执行**模型，它通过一系列内部机制来处理并发任务。下面详细描述工作流程：

1. **创建线程池**

    首先，通过构造函数创建一个 `ThreadPoolExecutor` 实例，并传入必要的参数，如核心线程数、最大线程数、非核心线程的存活时间、任务队列、线程工厂以及拒绝策略。

    请注意在线程池启动后，==核心线程是否立刻创建取决于是否调用了 `prestartAllCoreThreads()` 方法，默认情况下是不会调用==。如果需要启动线程池后便立刻创建核心线程，请调用这个方法。

    ```java
        public int prestartAllCoreThreads() {
            int n = 0;
            while (addWorker(null, true))
                ++n;
            return n;
        }
    ```

    另外，使用 `prestartAllCoreThreads()` 可能不是最优的做法，因为这会导致资源的提前占用，如果这些线程长时间不执行任务，则会造成资源浪费。通常情况下，我们是不需要这样做的，除非有特别的需求。

2. **提交任务**

    当通过 `execute()` 方法提交任务时，线程池会根据当前的情况来决定如何处理这个任务。

3. **分配任务**

    线程池根据当前的状态和配置来决定任务的分配。

    - 核心线程数内的任务分配
        - 如果当前活动线程数少于核心线程数，则会创建一个新的线程来执行这个任务。==如果已有空闲线程的存在，那么将直接使用空闲线程而非创建新线程。==
        - 如果当前活动线程数等于核心线程数，并且任务队列未满，则将任务加入队列中等待执行。
    - 核心线程数外的任务分配
        - 如果当前活动线程数已经达到核心线程数，且任务队列已满，此时如果最大线程数尚未达到，则创建一个新的线程来执行这个任务。
        - 如果当前活动线程数已经达到了最大线程数，且任务队列已满，此时将根据拒绝策略来处理这个任务。

4. **任务执行**

    一旦任务被分配给一个线程，它就会被执行。线程执行完任务后，会回到空闲状态，等待下一个任务。

5. **线程回收**

    - 核心线程的回收

        如果设置了 `allowCoreThreadTimeOut` 为 `true`，那么==即使是在核心线程数内的线程，如果超过了 `keepAliveTime` 时间没有任务执行，也会被回收。==这种配置通常用于希望减少资源消耗的情况，比如在系统负载降低时减少不必要的线程开销。

        ```java
            public void allowCoreThreadTimeOut(boolean value) {
                if (value && keepAliveTime <= 0)
                    throw new IllegalArgumentException("Core threads must have nonzero keep alive times");
                if (value != allowCoreThreadTimeOut) {
                    allowCoreThreadTimeOut = value;
                    if (value)
                        interruptIdleWorkers();
                }
            }
        ```

    - 非核心线程的回收

        非核心线程如果在 `keepAliveTime` 时间内没有任务执行，将会被回收。

6. **线程池关闭**

    可以通过调用 `shutdown()` 方法来停止接受新的任务提交，并等待已提交的任务执行完毕。调用 `shutdownNow()` 方法则会尝试停止所有正在执行的任务，并立即返回等待执行的任务列表。



## 2.3   总结

Java 线程池的工作流程可以概括为以下几个阶段：

- 初始化：创建线程池实例，并设置必要的参数。
- 任务提交：通过 `execute()` 方法提交任务。
- 任务分配：根据当前线程池的状态和配置，决定任务是立即执行、排队等待还是被拒绝。
- 任务执行：线程执行任务，并在完成后回到空闲状态。
- 线程回收：根据线程的存活时间和线程池的状态，决定是否回收线程。
- 线程池关闭：停止接受新的任务，并等待当前任务执行完毕。

通过这种方式，Java 线程池可以有效地管理并发任务，优化资源利用，并提供可靠的执行机制。
