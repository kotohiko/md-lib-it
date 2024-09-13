## 第 1 章   简介（Introduction）



> 【精彩原文】
>
> Threads are an inescapable feature of the Java language, and they can simplify the development of complex systems by turning complicated ==asynchronous code== into simpler straight-line code. In addition, threads are the easiest way to tap the computing power of multiprocessor systems.

### 1.1   并发简史（A (very) brief history of concurrency）

操作系统（英文：*operating system*，后面简称 OS）的出现解决了早期计算机很多问题，如：OS 可以让计算机运行多个程序，每个程序都运行在单独的「进程（*process*）」中。具体做法包括，OS 给每个进程分配资源，包括内存、文件句柄以及安全证书等。当然，进程之间也并非是绝对隔离的，在有需要的前提下可以通过一些==粗粒度==的通信机制来交换数据，包括套接字、信号处理器、共享内存、信号量、文件等。

计算机引入 OS 主要是解决了以下几类问题：

- 资源利用率。比如等待外部操作的完成。
- 公平性。比如不同的用户或程序对于计算机资源应该享有同等的使用权。
- 便利性。在计算多个任务时，编写多个程序并进行必要的通信，比只编写一个程序要容易实现得多。

> 【精彩原文】
>
> The manufacturers of teakettles and toasters know their products are often used in an ==asynchronous== manner, so they raise an audible signal when they complete their task. Finding the right balance of sequentiality and asynchrony is often a characteristic of efficient people—and the same is true of programs.

无独有偶，上述问题在今天看来似乎更像是考虑用「线程」来解决。没错，正是这些促使进程出现的需求也同样催生着线程的出现。线程可以理解为一个进程范围内的多个控制流。线程可以共享进程内的资源，如内存句柄、文件句柄。但是，每个线程都有各自的程序计数器（*program conuter*）、栈以及局部变量等。

> 【精彩原文】
>
> Threads are sometimes called *lightweight processes*, and most modern operating systems treat threads, not processes, as the basic units of scheduling. 

关于到底线程和进程孰为 OS 的基本调度单位？原文答案也比较清楚了，半个多世纪前，线程尚未普及甚至还未诞生之时，自然不可能是调度的基本单位；多线程的概念普及并且流行以后，才慢慢成为了基本调度单位。

### 1.2   线程的优势（Benifits of threads）

本节阐述了使用线程能带来什么好处，几乎没有核心知识点，重在理解和培养意识。

#### 1.2.1   发挥多处理器的强大功能（Exploiting multiple processors）

#### 1.2.2   建模的简单性（Simplicity of modeling）

> 【精彩原文】
>
> The same is true for software: a program that processes one type of task sequentially is simpler to write, less error-prone, and easier to test than one managing multiple different types of tasks at once.

#### 1.2.3   异步事件的简化处理

#### 1.2.4   响应更灵敏的用户界面

### 1.3   线程带来的风险（Risks of threads）

#### 1.3.1   安全性问题

### 1.4   线程无处不在（Threads are everywhere）





# 第一部分   基础知识



## 第 2 章   线程安全性

初学的时候可能会不太容易理解「线程安全」到底是个什么玩意儿。根据我的学习经验来看，至少可以先给自己建立一个基本的认知，那便是：当极短时间内有很多请求要修改某一个公共变量的时候，这时候就非常容易出现线程安全问题。

> 【精彩原文】
>
> Writing thread-safe code is, at its core, about managing access to *state*, and in particular to *shared*, *mutable state*.

### 2.1   什么是线程安全性（What is thread safety?）

> 【精彩原文】
>
> A class is *thread-safe* if it behaves correctly when accessed from multiple threads, regardless of the scheduling or interleaving of the execution of those threads by the runtime environment, and with no additional synchronization or other coordination on the part of the calling code.

接下来是死磕环节。

> 【问问 AI】（阿里通义）“当多个线程访问某个类时，不管运行时环境采用何种调度方式或者这些线程将如何交替执行。”具体都有什么调度方式？线程又具体如何交替执行呢？
>
> 在讨论多线程编程时，提到的“不管运行时环境采用何种调度方式或者这些线程将如何交替执行”主要是指在并发环境中，线程的执行顺序是不确定的，因此编写并发程序时需要考虑线程安全问题。下面是关于线程调度的一些常见方式以及线程如何交替执行的简要说明：
>
> ### 线程调度方式
>
> - **先来先服务（FCFS, First-Come, First-Served）**：
>     - 这种简单的调度策略按照线程到达的顺序来调度线程。但是这种策略在多核系统中不太常用，因为它不支持并发执行。
> - **轮询（Round Robin）**：
>     - 在轮询调度中，每个线程都会获得一个固定的时间片（time slice）。一旦一个线程的时间片用尽，它就会被挂起，CPU 会调度下一个线程执行。这种方式确保了所有线程都能公平地获得 CPU 时间。
> - **优先级调度（Priority Scheduling）**：
>     - 每个线程都被赋予一个优先级，高优先级的线程会被优先调度。如果当前运行的线程被更高优先级的线程抢占，则会暂停当前线程，转而执行更高优先级的线程。
> - **多级反馈队列（Multilevel Feedback Queue）**：
>     - 这是一种综合性的调度方法，它将线程分为不同的优先级队列，并为每个队列分配不同的时间片。当一个线程的时间片用尽时，它会被移到较低优先级的队列中，从而让出 CPU 给其他线程。
>
> ### 线程如何交替执行
>
> 线程的交替执行依赖于操作系统调度器的策略。以下是几种常见的线程交替执行的情况：
>
> 1. **时间片轮转**：
>     - 当一个线程的时间片耗尽时，调度器会选择另一个线程来执行。这种方式保证了所有线程都有机会执行。
> 2. **自愿放弃**：
>     - 线程可以选择在某些点上自愿放弃CPU时间，例如在调用 `yield()` 函数时，它会请求调度器将 CPU 时间分配给其他线程。
> 3. **阻塞操作**：
>     - 如果一个线程正在进行 I/O 操作或等待锁释放，它会被挂起，调度器会选择其他就绪线程执行。
> 4. **抢占式调度**：
>     - 在抢占式调度中，如果一个更高优先级的线程变为可运行状态，它可以立即抢占当前正在执行的线程的 CPU 时间。
> 5. **响应中断**：
>     - 当系统接收到硬件中断时，当前正在执行的线程会被挂起，以便处理中断。
>
> 总之，线程的交替执行是由操作系统调度器根据一定的算法来决定的，目的是为了最大化系统的吞吐量、响应时间和公平性。在线程设计时，开发者需要考虑到这些不确定性，确保线程之间的协作和数据一致性。

“主调代码不需要任何额外的同步或协同”。这句话不难理解，既然被调用方（供应商）声称自己的「线程安全的」，那么调用它的那一方（客户）就完全没有必要、也没有义务去担心甚至解决被调用方的线程安全问题（可以试想一下 `HashMap` 与 `ConcurrentHashMap`）。

“这个类都能表现出正确的行为”。换句话说，就是达到预期的效果，也就是符合需求。

回想基础薄弱事后的自己反复读这句话，无论如何都难以理解。在有了更多工作经历以及 AIGC 如此强大的今天，回头再看这句话，就觉得好理解的多了。

### 2.2   原子性（Atomicity）



### 2.3   加锁机制（Locking）

#### 2.3.1   内置锁（Intrinsic locks）



#### 2.3.2   重入（Reentrancy）





## 第 3 章   对象的共享



## 第 4 章   对象的组合



## 第 5 章   基础构建模块

### 5.3   阻塞队列和生产者 - 消费者模式（Blocking queues and the producer-consumer pattern）



# 第二部分   结构化并发应用程序





## 第 6 章   任务执行





### 6.2   `Executor` 框架





#### 6.2.3   线程池



### 6.3   找出可利用的并行性





## 第 7 章   取消与关闭



## 第 8 章   线程池的使用

### 8.1   在任务与执行策略之间的隐性耦合

### 8.3   配置 `ThreadPoolExecutor`

#### 8.3.1   线程的创建与销毁

**基本大小／核心线程数（*core pool size*）**表示的是没有任务执行时线程池的大小。此外，只有在==工作队列满了以后==才会创建超过这个数量的线程。

**最大大小／最大线程数（*maximum pool size*）**表示的是可同时活动的线程数量的上限。

如果一个线程的空闲时间超过了存活时间，那么将被标记为「可回收的」，并且当线程池的大小超过了基本大小时，这个线程将会被终止。



## 第 9 章   图形用户界面应用程序



# 第三部分   活跃性、性能与测试

## 第 10 章   避免活跃性危险





# 第四部分   高级主题（Advanced Topics）

## 第 13 章   显式锁（Explicit Locks）



## 第 14 章   构建自定义的同步工具



## 第 15 章   原子变量与非阻塞同步机制

本章将探讨原子变量（如 `AtomicInteger`）与非阻塞的同步机制。

近年来，在并发算法领域侧重研究的所谓「非阻塞算法（*nonblocking algorithms*）」，是指采用底层的原子机器指令（如 Compare and Swap，简称 CAS 指令）代替锁来确保数据在并发访问中的一致性。

相比锁，非阻塞算法在设计和实现上复杂的多，但是具有更好的活跃性和可伸缩性。非阻塞算法的优势是：

- 多线程竞争同一数据时不会发生阻塞，故非阻塞算法可以在粒度更细的层次上进行协调，并且极大地减少调度开销。

- 不存在「死锁」和其他活跃性问题。

- 基于锁的算法会存在这样的问题：当一个线程在休眠或自旋的同时持有锁，那么其他线程都无法执行下去。非阻塞算法不会受到单个线程失败的影响。

    > 【问问 AI】（阿里通义）为什么非阻塞算法不受单个线程失败的影响？
    >
    > - 线程独立性：
    >     - 在非阻塞算法中，每个线程都可以独立地尝试更新共享数据，即使某个线程失败或暂停，其他线程仍然可以继续尝试更新。
    >     - 这种独立性使得系统整体的健壮性更强。
    > - 避免阻塞：
    >     - 由于没有锁的存在，即使某个线程在某个时刻失败，其他线程也不会被阻塞，可以继续执行它们的任务。
    > - 重试机制：
    >     - 非阻塞算法中的重试机制允许线程在发现数据已发生变化时重新尝试更新，这进一步增强了系统的鲁棒性。

自 Java 5.0 起，可以使用原子类来构建高效的非阻塞算法。



## 第 16 章   Java 内存模型（The Java Memory Model）
