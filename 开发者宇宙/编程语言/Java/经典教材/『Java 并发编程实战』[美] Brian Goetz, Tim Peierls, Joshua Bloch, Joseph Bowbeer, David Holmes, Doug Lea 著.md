## 第 1 章   简介（Introduction）



> Threads are an inescapable feature of the Java language, and they can simplify the development of complex systems by turning complicated ==asynchronous code== into simpler straight-line code. In addition, threads are the easiest way to tap the computing power of multiprocessor systems.

### 1.1   并发简史（A (very) brief history of concurrency）

操作系统（英文：*operating system*，后面简称 OS）的出现解决了早期计算机很多问题，如：OS 可以让计算机运行多个程序，每个程序都运行在单独的「进程（*process*）」中。具体做法包括，OS 给每个进程分配资源，包括内存、文件句柄以及安全证书等。当然，进程之间也并非是绝对隔离的，在有需要的前提下可以通过一些==粗粒度==的通信机制来交换数据，包括套接字、信号处理器、共享内存、信号量、文件等。

计算机引入 OS 主要是解决了以下几类问题：

- 资源利用率。比如等待外部操作的完成。
- 公平性。比如不同的用户或程序对于计算机资源应该享有同等的使用权。
- 便利性。在计算多个任务时，编写多个程序并进行必要的通信，比只编写一个程序要容易实现得多。

> The manufacturers of teakettles and toasters know their products are often used in an ==asynchronous== manner, so they raise an audible signal when they complete their task. Finding the right balance of sequentiality and asynchrony is often a characteristic of efficient people—and the same is true of programs.

无独有偶，上述问题在今天看来似乎更像是考虑用「线程」来解决。没错，正是这些促使进程出现的需求也同样催生着线程的出现。线程可以理解为一个进程范围内的多个控制流。线程可以共享进程内的资源，如内存句柄、文件句柄。但是，每个线程都有各自的程序计数器（*program conuter*）、栈以及局部变量等。

> Threads are sometimes called *lightweight processes*, and most modern operating systems treat threads, not processes, as the basic units of scheduling. 

关于到底线程和进程孰为基本调度单位？原文答案也比较清楚了，半个多世纪前，线程尚未普及甚至还未诞生之时，自然不可能是调度的基本单位；多线程的概念普及并且流行以后，才慢慢成为了基本调度单位。

### 1.2   线程的优势（Benifits of threads）



#### 1.2.1   发挥多处理器的强大功能（Exploiting multiple processors）



#### 1.2.2   建模的简单性（Simplicity of modeling）

> The same is true for software: a program that processes one type of task sequentially is simpler to write, less error-prone, and easier to test than one managing multiple different types of tasks at once.







# 第一部分   基础知识



## 第 2 章   线程安全性



## 第 3 章   对象的共享



## 第 4 章   对象的组合





# 第二部分   结构化并发应用程序





## 第 6 章   任务执行





### 6.2   `Executor` 框架





#### 6.2.3   线程池



### 6.3   找出可利用的并行性









## 第 8 章   线程池的使用

### 8.1   在任务与执行策略之间的隐性耦合

### 8.3   配置 `ThreadPoolExecutor`

#### 8.3.1   线程的创建与销毁

**基本大小／核心线程数（*core pool size*）**表示的是没有任务执行时线程池的大小。此外，只有在==工作队列满了以后==才会创建超过这个数量的线程。

**最大大小／最大线程数（*maximum pool size*）**表示的是可同时活动的线程数量的上限。

如果一个线程的空闲时间超过了存活时间，那么将被标记为「可回收的」，并且当线程池的大小超过了基本大小时，这个线程将会被终止。



# 第四部分   高级主题

## 第 15 章   原子变量与非阻塞同步机制

本章将探讨原子变量（如 `AtomicInteger`）与非阻塞的同步机制。

近年来，在并发算法领域侧重研究的所谓「非阻塞算法（*nonblocking algorithms*）」，是指采用底层的原子机器指令（如 Compare and Swap，简称 CAS 指令）代替锁来确保数据在并发访问中的一致性。

相比锁，非阻塞算法在设计和实现上复杂的多，但是具有更好的活跃性和可伸缩性。
