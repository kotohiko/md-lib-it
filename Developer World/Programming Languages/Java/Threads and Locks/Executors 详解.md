`Executors` 是一个工具类，提供了一系列静态工厂方法，用于创建不同类型的线程池。这些线程池都实现了 `ExecutorService` 接口，提供了管理和执行异步任务的功能。

### 1   为什么使用 `Executors`？

- **简化线程池创建：**`Executors` 提供了方便的方法，无需手动配置线程池的各种参数。
- **提供常用线程池类型：**`Executors` 提供了多种线程池类型，满足不同场景的需求。
- **隐藏底层复杂性：**`Executors` 封装了线程池的创建和管理细节，让开发者可以更专注于业务逻辑。

### 2   `Executors` 常用方法及对应线程池

| 方法                                       | 返回类型                   | 描述                                                         | 特点                                                         |
| ------------------------------------------ | -------------------------- | ------------------------------------------------------------ | ------------------------------------------------------------ |
| `newFixedThreadPool(int nThreads)`         | `ExecutorService`          | 创建一个固定大小的线程池。核心线程数和最大线程数相等，超出部分的任务会在队列中等待。 | 线程数固定，适合处理稳定的任务负载。<br>任务队列无界，可以无限排队。<br/>适用于==负载均衡==的场景。 |
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

### 3   注意事项

- **线程池大小**：线程池大小的设置需要根据具体应用场景来调整，过大或过小都会影响性能。
- **任务类型**：不同的任务类型适合不同的线程池类型。
- **线程池关闭**：使用 `shutdown()` 方法来关闭线程池，并使用 `awaitTermination()` 方法等待所有任务执行完毕。

### 4   深入理解

- `ThreadPoolExecutor`：`Executors` 中创建的线程池==底层都是基于 `ThreadPoolExecutor` 实现的。==
- **核心参数**：`corePoolSize`、`maximumPoolSize`、`keepAliveTime`、`workQueue` 等参数对线程池的行为有重要影响。
- **拒绝策略**：当任务队列满了且线程池中的线程数达到最大时，会触发拒绝策略。

