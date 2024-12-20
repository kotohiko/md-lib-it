



# Chapter 1. An Introduction to Java

In this chapter

- [1.1 Java as a Programming Platform](##1.1 Java as a Programming Platform)
- 1.2 The Java “White Paper” Buzzwords
- 1.3 Java Applets and the Internet
- 1.4 A Short History of Java
- 1.5 Common Misconceptions about Java



The first release of Java in 1996 generated an incredible amount of excitement, not just in the computer press, but in mainstream media such as the *New York Times*, the *Washington Post*, and *BusinessWeek*. Java has the distinction of being the first and only programming language that had a tenminute story on **National Public Radio**[^1-1]. A $100,000,000 **venture**[^1-2] capital fund was set up solely for products using a *specific* computer language. I hope you will enjoy a brief history of Java that you will find in this chapter.

> [^1-1]:an American [public broadcasting](https://en.wikipedia.org/wiki/Public_broadcasting) organization headquartered in [Washington, D.C.](https://en.wikipedia.org/wiki/Washington,_D.C.), with its NPR West headquarters in [Culver City, California](https://en.wikipedia.org/wiki/Culver_City,_California).
> [^1-2]:a business project or activity, especially one that involves taking risks

## 1.1 Java as a Programming Platform

In the first edition of this book, my coauthor Gary Cornell and I had this to write about Java:

“As a computer language, Java’s **hype**[^1.1-1] is overdone: Java is certainly a *good* programming language. There is no doubt that it is one of the better languages available to serious programmers. We think it could *potentially* have been a great programming language, but it is probably too late for that. Once a language is out[^1.1-2] in the field, the ugly reality of compatibility[^1.1-3] with existing code sets in.”

Our editor got a lot of flack for this paragraph from someone very high up at Sun Microsystems, the company that originally developed Java. The Java language has a lot of nice features that we will examine in detail later in this chapter. It has its share of warts, and some of the newer additions to the language are not as elegant as the original features because of compatibility requirements.



> [^1.1-1]:advertisements and discussion in the media telling the public about a product and about how good or important it is
> [^1.1-2]:available to everyone; known to everyone
> [^1.1-3]:the ability of machines, especially computers, and computer programs to be used together
>
> 











# Chapter 6. Interfaces, Lambda Expressions, and Inner Classes

In this chapter

- [6.1 Interfaces](##6.1 Interfaces)
- 6.2 Lambda Expressions
- 6.3 Inner Classes
- 6.4 Service Loaders
- 6.5 Proxies



You have now learned about classes and inheritance, the key concepts of ==object-oriented programming== in Java. This chapter shows you several advanced techniques that are commonly used. Despite their less obvious **nature**[^6-1], you will need to master them to complete your Java tool **chest**[^6-2].

The first technique, called *interfaces*, is a way of describing *what* classes should do, without specifying *how* they should do it. A class can *implement* one or more interfaces. You can then use objects of these implementing classes whenever **conformance**[^6-3] to the interface is required. After discussing interfaces, we move on to *lambda expressions*, a **concise**[^6-4] way to create blocks of code that can be executed at a later point in time. Using lambda expressions, you can express code that uses callbacks or variable behavior in an elegant and concise fashion.

We then discuss the mechanism of *inner classes*. Inner classes are technically somewhat complex—they are defined inside other classes, and their methods can access the fields of the surrounding class. Inner classes are useful when you design collections of cooperating classes.

This chapter **concludes**[^6-5] with a discussion of *proxies*, ==objects that implement **arbitrary**[^6-6] interfaces.== A proxy is a very **specialized**[^6-7] construct that is useful for building system-level tools. You can safely skip that section on first reading.

> [^6-1]:[sing.] the basic qualities of a thing
> [^6-2]:a large strong box, usually made of wood, used for storing things in and/or moving them from one place to another
> [^6-3]:**conformance (to/with sth)** (formal) the fact of following the rules or standards of sth
> [^6-4]:giving only the information that is necessary and important, using few words
> [^6-5]:[intransitive, transitive] (*formal*) to come to an end; to bring sth to an end
> [^6-6]:(of an action, a decision, a rule, etc.) not seeming to be based on a reason, system or plan and sometimes seeming unfair
> [^6-7]:designed or developed for a particular purpose or area of knowledge



## 6.1 Interfaces





# Chapter 12. Concurrency

In this chapter

- [12.1 What Are Threads?](##12.1 What Are Threads?)
- 12.2 Thread States
- 12.3 Thread Properties
- 12.4 Synchronization
- 12.5 Thread-Safe Collections
- 12.6 Tasks and Thread Pools
- 12.7 Asynchronous Computations
- 12.8 Processes

You are probably familiar with *multitasking*—your operating system’s ability to have more than one program working at what seems like the same time. For example, you can print while editing or downloading your email. Nowadays, you are likely to have a computer with more than one CPU, but the number of concurrently executing processes is not limited by the number of CPUs. The operating system assigns CPU time slices to each process, giving the impression of parallel activity.

Multithreaded programs extend the idea of multitasking by taking it one level lower: Individual programs will appear to do multiple tasks at the same time. Each task is executed in a *thread*, which is **short**[^12-1] for thread of control. Programs that can run more than one thread at once are said to be *multithreaded*.

So, what is the difference between multiple *processes* and multiple *threads*? ==The essential difference is that while each process has a complete set of its own variables, threads share the same data.== This sounds somewhat risky, and indeed it can be, as you will see later in this chapter. However, shared variables make communication between threads more efficient and easier to program than interprocess communication. Moreover, on some operating systems, threads are more “lightweight” than processes—it takes less **overhead**[^12-2] to create and destroy individual threads than it does to launch new processes.

Multithreading is extremely useful in practice. For example, a browser should be able to **simultaneously**[^12-3] download multiple images. A web server needs to be able to serve concurrent requests. Graphical user interface (GUI) programs have a **separate**[^12-4] thread for gathering user interface events from the host operating environment. This chapter shows you how to add multithreading capability to your Java applications.

Fair warning: Concurrent programming can get very complex. In this chapter, I **cover**[^12-5] all the tools that an application programmer is likely to need. However, for more **intricate**[^12-6] system-level programming, we suggest that you turn to a more advanced reference, such as *Java Concurrency in Practice* by Brian Goetz et al. (Addison-Wesley Professional, 2006).

> [^12-1]:**short for sth** being a shorter form of a name or word
> [^12-2]:(*especially BrE*) (*also* ov**erhead [**uncountable] *especially in NAmE*) regular costs that you have when you are running a business or an organization, such as rent, electricity, wages, etc.
> [^12-3]:at the same time as sth else
> [^12-4]:forming a unit by itself; not joined to sth else
> [^12-5]:[transitive] **cover** **sth** to include sth; to deal with sth
> [^12-6]:having a lot of different parts and small details that fit together



## 12.1 What Are Threads?

Let us start by looking at a simple program that uses two threads. This program moves money between bank accounts. We make use of a `Bank` class that stores the balances of a given number of accounts. The `transfer` method transfers an amount from one account to another. See Listing 12.2 for the implementation.

【**Listing 12.1** `threads/ThreadTest.java`】

```java
package threads;

/**
 * @author Cay Horstmann
 * @version 1.30 2004-08-01
 */
public class ThreadTest {
    
    public static final int DELAY = 10;
    public static final int STEPS = 100;
    public static final double MAX_AMOUNT = 1000;

    public static void main(String[] args) {
        var bank = new Bank(4, 100000);
        Runnable task1 = () ->
        {
            try {
                for (int i = 0; i < STEPS; i++) {
                    double amount = MAX_AMOUNT * Math.random();
                    bank.transfer(0, 1, amount);
                    Thread.sleep((int) (DELAY * Math.random()));
                }
            } catch (InterruptedException e) {
            }
        };

        Runnable task2 = () ->
        {
            try {
                for (int i = 0; i < STEPS; i++) {
                    double amount = MAX_AMOUNT * Math.random();
                    bank.transfer(2, 3, amount);
                    Thread.sleep((int) (DELAY * Math.random()));
                }
            } catch (InterruptedException e) {
            }
        };

        new Thread(task1).start();
        new Thread(task2).start();
    }
}

```





## 12.6 Tasks and Thread Pools

Constructing a new thread is somewhat expensive because it involves interaction with the operating system. If your program creates a large number of short-lived threads, you should not map each task to a separate thread, but use a *thread pool* instead. A thread pool contains a number of threads that are ready to run. You give a `Runnable` to the pool, and one of the threads calls the `run` method. When the `run` method exits, the thread doesn’t die but stays around to serve the next request.

In the following sections, you will see the tools that the Java concurrency framework provides for coordinating concurrent tasks.

### 12.6.1 `Callables` and `Futures`

A `Runnable` encapsulates a task that runs asynchronously; you can think of it as an asynchronous method with ==no parameters and no return value.== A `Callable` is similar to a `Runnable`, ==but it returns a value.== The `Callable` interface is a parameterized type, with a single method `call`.

```java
public interface Callable<V> {
	V call() throws Exception;
}
```

The type parameter is the type of the returned value. For example, =a `Callable<Integer>` represents an asynchronous computation that eventually returns an `Integer` object.

==A `Future` holds the *result* of an asynchronous computation.== You start a computation, give someone the `Future` object, and forget about it. The owner of the `Future` object can obtain the result when it is ready.

The `Future<V>` interface has the following methods:

```java
V get()
V get(long timeout, TimeUnit unit)
void cancel(boolean mayInterrupt)
boolean isCancelled()
boolean isDone()
```



## 12.7 Asynchronous Computations

So far, our approach to concurrent computation has been to break up a task, and then ==wait until all pieces have completed.== But waiting is not always a good idea. In the following sections, you will see how to implement waitfree, or *asynchronous*, computations.

### 12.7.1 Competable Futures

