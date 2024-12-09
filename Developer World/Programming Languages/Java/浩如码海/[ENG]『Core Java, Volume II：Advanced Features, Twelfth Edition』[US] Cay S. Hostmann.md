# Chapter 1: Streams



-   [1.1 From Iterating to Stream Operations](##1.1 From Iterating to Stream Operations)
-   [1.2 Stream Creation](##1.2 Stream Creation)
-   [1.3 The `filter.map` and `flatMap` Methods](##1.3 The `filter.map` and `flatMap` Methods)
-   [1.4 Extracting Substreams and Combining Streams](##1.4 Extracting Substreams and Combining Streams)
-   [1.5 Other Stream Transformations](##1.5 Other Stream Transformations)
-   [1.6 Simple Reductions](##1.6 Simple Reductions)
-   1.7 The Optional Type
-   1.8 Collection Results
-   1.9 Collecting into Maps
-   1.10 Grouping and Partitioning
-   1.11 Downstream Collectors
-   1.12 Reduction Operations
-   1.13 Primitive Type Streams
-   1.14 Parallel Streams

Compared to collections, streams provide a view of data that lets you specify computations at a higher conceptual level. ==With a stream, you specify what you want to have done, not how to do it.== You leave the scheduling of operations to the implementation. For example, suppose you want to compute the average of a certain property. You specify the source of data and the property, and the stream library can then optimize the computation, for example by using multiple threads for computing sums and counts and combining the results.

In this chapter, you will learn how to use the Java stream library, which was introduced in Java 8, to process collections in a “what, not how” style.

## 1.1 From Iterating to Stream Operations

When you process a collection, you usually iterate over its elements and do some work with each of them. For example, suppose we want to count all long words in a book. First, let’s put them into a list:

```java
var contents = Files.readString(Path.of("alice.txt"))); // Readfile into string
List<String> words = List.of(contents.split("\\PL+"));
	// Split into words; nonletters are delimiters
```

Now we are ready to iterate:

```java
int count = 0;
for (String w : words) {
    if (w.length() > 12) count++;
}
```

With streams, the same operation looks like this:

```java
long count = words.stream()
    .filter(w -> w.length() > 12)
    .count();
```

Now you don’t have to scan the loop for evidence of filtering and counting. The method names tell you right away what the code intends to do. Moreover, where the loop **prescribes**[^1.1-1] the order of operations in complete detail, a stream is able to schedule the operations any way it wants, as long as the result is correct.

Simply changing `stream` to `parallelStream` allows the stream library to do the filtering and counting in parallel.

```java
long count = words.parallelStream()
    .filter(w -> w.length() > 12)
    .count();
```

Streams follow the “what, not how” principle. In our stream example, we describe what needs to be done: get the long words and count them. We don’t specify in which order, or in which thread, this should happen. In contrast, the loop at the beginning of this section specifies exactly how the computation should work, and thereby **forgoes**[^1.1-2] any chances of optimization.

A stream seems **superficially**[^1.1-3] similar to a collection, allowing you to transform and retrieve data. But there are significant differences:

1.  A stream does not store its elements. They may be stored in an **underlying**[^1.1-4] collection or generated on demand.
2.  Stream operations don’t **mutate**[^1.1-5] their source. For example, the filter method does not remove elements from a stream but **yields**[^1.1-6] a new stream in which they are not present.
3.  Stream operations are *lazy* when possible. This means they are not executed until their result is needed. For example, if you only ask for the first five long words instead of all, the filter method will stop filtering after the fifth match. As a consequence, you can even have infinite streams!

Let us have another look at the example. The stream and `parallelStream` methods yield a stream for the words list. The filter method returns another stream that contains only the words of length greater than twelve. The count method reduces that stream to a result. This workflow is typical when you work with streams. You set up a pipeline of operations in three stages:

1.  Create a stream.
2.  Specify **intermediate**[^1.1-7] operations for transforming the initial stream into others, possibly in multiple steps.
3.  Apply a terminal operation to produce a result. This operation forces the execution of the lazy operations that precede it. Afterwards, the stream can no longer be used.

In the example in Listing 1.1, the stream is created with the stream or `parallelStream` method. The filter method transforms it, and
count is the terminal operation.

【Listing 1.1 `streams/CountLongWords.java`】

```java
package streams;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

/**
 * @author Cay Horstmann
 * @version 1.02 2019-08-28
 */
public class CountLongWords {
    public static void main(String[] args) throws IOException {
        var contents = Files.readString(Path.of("gutenberg/alice30.txt"));
        // Split into words; nonletters are delimiters
        List<String> words = List.of(contents.split("\\PL+"));

        long count = 0;
        for (String word : words) {
            if (word.length() > 12) {
                ++count;
            }
        }
        System.out.println(count);

        count = words.stream().filter(word -> word.length() > 12).count();
        System.out.println(count);

        count = words.parallelStream().filter(word -> word.length() > 12).count();
        System.out.println(count);
    }
}
```

In the next section, you will see how to create a stream. The subsequent three sections deal with stream transformations. They are followed by five sections on terminal operations.

- `java.util.stream.Stream`   8

    - `Stream filter(Predicate p)`

        yields a stream containing all elements of this stream fulfilling `p`.

    - `long count()`

        yields the number of elements of this stream. This is a terminal operation.

- `java.util.Collection`   1.2

    - `default Stream stream()`

    - `default Stream parallelStream()`

        yield a sequential or parallel stream of the elements in this collection.



>   [^1.1-1]: (of a person or an organization with authority) to say what should be done or how sth should be done
>   [^1.1-2]:
>
>   
>
>   [^1.1-6]:[transitive] **yield** **sth** to produce or provide sth, for example a profit, result or crop
>
>   



## 1.2 Stream Creation



## 1.3 The `filter.map` and `flatMap` Methods



## 1.4 Extracting Substreams and Combining Streams



## 1.5 Other Stream Transformations



## 1.6 Simple Reductions