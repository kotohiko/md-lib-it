## 第 1 章   简介



> Threads are an inescapable feature of the Java language, and they can simplify the develop- ment of complex systems by turning complicated ==asynchronous code== into simpler straight-line code. In addition, threads are the easiest way to tap the computing power of multiprocessor systems.

### 1.1   并发简史

操作系统（英文：*operating system*，后面简称 OS）的出现解决了早期计算机很多问题，如：OS 可以让计算机运行多个程序，每个程序都运行在单独的「进程（*process*）」中。具体做法包括，OS 给每个进程分配资源，包括内存、文件句柄以及安全证书等。当然，进程之间也并非是绝对隔离的，在有需要的前提下可以通过一些==粗粒度==的通信机制来交换数据，包括套接字、信号处理器、共享内存、信号量、文件等。

计算机引入 OS 主要是解决了以下几类问题：

- 资源利用率。比如等待外部操作的完成。
- 公平性。比如不同的用户或程序对于计算机资源应该享有同等的使用权。
- 便利性。在计算多个任务时，编写多个程序并进行必要的通信，比只编写一个程序要容易实现得多。

> The manufacturers of teakettles and toasters know their products are often used in an ==asynchronous== manner, so they raise an audible signal when they complete their task. Finding the right balance of sequentiality and asynchrony is often a characteristic of efficient people—and the same is true of programs.



### 1.2   线程的优势



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
