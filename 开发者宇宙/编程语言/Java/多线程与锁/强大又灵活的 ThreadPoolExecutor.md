## 1   详解七个参数

Java 中的 `ThreadPoolExecutor` 是一个非常强大的线程池实现，它提供了丰富的配置选项来满足各种不同的需求。在创建 `ThreadPoolExecutor` 实例时，通常需要指定七个主要参数。下面是这些参数的详细说明：

- `corePoolSize`（核心线程数）

    - 描述：线程池的基本大小，即线程池中==始终维持==的最小线程数。

    - 作用：即使没有任务执行，线程池也会保持至少 `corePoolSize` 个线程处于等待状态，==除非设置了 `allowCoreThreadTimeOut` 为 `true`==。

    - 默认值：无默认值，==必须==显式指定。

- `maximumPoolSize`（最大线程数）
    - 描述：线程池允许的最大线程数。
    - 作用：当活动线程数达到 `maximumPoolSize` 时，线程池不会创建新的线程，而是将任务放入队列中等待执行。
    - 默认值：无默认值，必须显式指定。

- `keepAliveTime`（非核心线程的存活时间）
    - 描述：当线程池中的线程数目大于 `corePoolSize` 时，多余的空闲线程最多只能存活的时间。
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

示例代码
以下是一个创建 ThreadPoolExecutor 的示例代码：

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

## 2   工作流程

`ThreadPoolExecutor` 的工作流程是一个典型的**任务分配和执行**模型，它通过一系列内部机制来处理并发任务。下面详细描述工作流程：

### 2.1   创建线程池

首先，通过构造函数创建一个 `ThreadPoolExecutor` 实例，并传入必要的参数，如核心线程数、最大线程数、非核心线程的存活时间、任务队列、线程工厂以及拒绝策略。

### 2.2   提交任务

当通过 `execute()` 方法提交任务时，线程池会根据当前的情况来决定如何处理这个任务。

### 2.3   分配任务

线程池根据当前的状态和配置来决定任务的分配。

#### 2.3.1   核心线程数内的任务分配

- 如果当前活动线程数少于核心线程数，则会创建一个新的线程来执行这个任务，即使当前已经有空闲线程。
- 如果当前活动线程数等于核心线程数，并且任务队列未满，则将任务加入队列中等待执行。

#### 2.3.2   核心线程数外的任务分配

- 如果当前活动线程数已经达到核心线程数，且任务队列已满，此时如果最大线程数尚未达到，则创建一个新的线程来执行这个任务。
- 如果当前活动线程数已经达到了最大线程数，且任务队列已满，此时将根据拒绝策略来处理这个任务。

### 2.4   任务执行

一旦任务被分配给一个线程，它就会被执行。线程执行完任务后，会回到空闲状态，等待下一个任务。

### 2.5   线程回收

#### 2.5.1   核心线程的回收

如果设置了 `allowCoreThreadTimeOut` 为 `true`，那么即使是在核心线程数内的线程，如果超过了 `keepAliveTime` 时间没有任务执行，也会被回收。

#### 2.5.2   非核心线程的回收

非核心线程如果在 `keepAliveTime` 时间内没有任务执行，将会被回收。

### 2.6   线程池关闭

可以通过调用 `shutdown()` 方法来停止接受新的任务提交，并等待已提交的任务执行完毕。调用 `shutdownNow()` 方法则会尝试停止所有正在执行的任务，并立即返回等待执行的任务列表。

### 总结

Java 线程池的工作流程可以概括为以下几个阶段：

- 初始化：创建线程池实例，并设置必要的参数。
- 任务提交：通过 `execute()` 方法提交任务。
- 任务分配：根据当前线程池的状态和配置，决定任务是立即执行、排队等待还是被拒绝。
- 任务执行：线程执行任务，并在完成后回到空闲状态。
- 线程回收：根据线程的存活时间和线程池的状态，决定是否回收线程。
- 线程池关闭：停止接受新的任务，并等待当前任务执行完毕。

通过这种方式，Java 线程池可以有效地管理并发任务，优化资源利用，并提供可靠的执行机制。