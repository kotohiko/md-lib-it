# [A tour of the C# language](https://learn.microsoft.com/en-us/dotnet/csharp/tour-of-csharp/)

文档里提到，说 C# 是 ***component-oriented*** 的，我暂译「面向组件」的，这于我而言又是一个全新的概念。根据文档中的解释，大概是说 C# 可以通过整合各种组件来迅速完成项目的交付。

接下来讲的就是 C# 拥有的一些特性，差不多也都是 OOP 编程语言（或者说是 Java）固有的一些特性，如[**垃圾回收（*Garbage collection*）**](https://learn.microsoft.com/en-us/dotnet/standard/garbage-collection/)、**[空类型（*Nullable types*）](https://learn.microsoft.com/en-us/dotnet/csharp/nullable-references)**、[**异常处理（*Exception handling*）**](https://learn.microsoft.com/en-us/dotnet/csharp/fundamentals/exceptions/)、[**lambda 表达式（*Lambda expressions*）**](https://learn.microsoft.com/en-us/dotnet/csharp/language-reference/operators/lambda-expressions)等。而 *[**LINQ（Language Integrated Query）**](https://learn.microsoft.com/en-us/dotnet/csharp/linq/)*倒是一个没听说过的概念，看简述大概是与数据源打交道的一种语法。此外，C# 中对[**异步操作（*asynchronous operations*）**](https://learn.microsoft.com/en-us/dotnet/csharp/asynchronous-programming/)提供的支持可以有效地构建分布式系统。

C# 有一个[**统一类型系统**](https://learn.microsoft.com/en-us/dotnet/csharp/fundamentals/types/)（暂译，英文为：*[**unified type system**](https://learn.microsoft.com/en-us/dotnet/csharp/fundamentals/types/)*）。在 C# 中，所有的类型，如 `int`、`double` 都继承自一个根类型 `object`（又是个似曾相识的东西），这印证了 C# 对数据类型的设计思路上似乎与 Java 有着些许不同。所有类型共用一组通用运算。然后介绍用户自定义[引用类型（*reference types*）](https://learn.microsoft.com/en-us/dotnet/csharp/language-reference/builtin-types/reference-types)／[值类型（*value types*）](https://learn.microsoft.com/en-us/dotnet/csharp/language-reference/builtin-types/value-types)、泛型方法、迭代器等。

C# 强调**版控（*versioning*）**——好神奇，这如何强调Σ(⊙▽⊙"a）？随即又引出了两个修饰符：`virtual` 与 `override`。只看概念很难理解，交给后续实战开发了再理解也不迟。



## [.NET architecture](https://learn.microsoft.com/en-us/dotnet/csharp/tour-of-csharp/#net-architecture)

C# 程序运行在 .NET 上。所谓 .NET 是一种被称为为**公共语言运行库（common language runtime，CLR）**的虚拟运行系统，包含一系列类库。CLR 则是 Microsoft 对公共语言基础结构（CLI，全称 common language infrastructure）国际标准的实现。CLI 是创建执行和开发环境的基础，语言和库可以在其中无缝协同运转。

C# 源代码会被编译成符合 CLI 规范的[中间语言（intermediate language，IL）](https://learn.microsoft.com/en-us/dotnet/standard/managed-code)。IL 代码和资源（如位图、字符串）会被存放在一个程序集里，通常扩展名为 *.dll*。程序集包含介绍自己本身的类型、版本和区域信息。

执行 C# 程序的时候，程序集会被加载进 CLR。CLR 通过 JIT（Just-In-Time）来将 IL 代码转为本地机器指令。CLR 还可以提供自动垃圾回收、异常处理、以及资源管理等其他服务。由 CLR 执行的代码有时会被称为「托管代码」。而「非托管代码」会被编译成面向特定平台的本机语言。

「语言互动性」（英文为 language interoperability。相对正规一些的译名为「语言互操作性」，我觉得不好听，就不想采用这个译名。）是 .NET 的核心特性之一。C# 编译器生成的 IL 代码符合公共类型规范（Common Type Specification，简称 CTS）。C# 产生的 IL 代码可以与由 .NET 版本的 F#、Visual Basic、C++ 生成的代码进行交互。还有超过 20 种与 CTS 兼容的语言。 单个程序集可以包含多个用各种 .NET 语言编写的模块。 这些类型可以相互引用，就像它们是用同一种语言编写的一样。

除去这些运行时服务，.NET 也包括大量的库。这些库支持多种工作。它们被组织进命名空间以完成各种功能——从文件的 I／O 到字符串操作，到 XML 解析，再到 web 应用框架、Windows Forms 控制。通常 C# 应用可以通过 .NET 类库来解决大量繁杂的工作。

.NET 的更多详情请参阅 [Overview of .NET](https://learn.microsoft.com/en-us/dotnet/core/introduction)。



## [Hello world](https://learn.microsoft.com/en-us/dotnet/csharp/tour-of-csharp/#hello-world)

当介绍一门编程语言的时候，“Hello World” 程序通常是一个经典又司空见惯的选择。以下便是 C# 中的 “Hello World”。

```c#
using System;

class Hello
{
    static void Main()
    {
        // This line prints "Hello, World" 
        Console.WriteLine("Hello, World");
    }
}
```

`using` 指令表示引用了一个叫 `System` 的**命名空间（namespace）**。所谓「命名空间」提供了一种将 C# 程序和库整合起来的分层方法。命名空间包含了类型和其他命名空间——举个例子，`System` 命名空间包含了一些类型，比如程序中引用到的 `Console` 类，以及其他命名空间如 `IO`、`Collections`。

程序第一行使用了 `using` 指令，意味着 `Console.WriteLine()` 就毋须写成 `System.Console.WriteLine()`。

本程序中，`Hello` 类只有一个「成员」，也就是 `Main` 方法。与 Java 一样，`Main` 方法也是使用 `static` 来修饰。实例方法可以通过使用关键字 `this` 引用特定的封闭对象实例，而静态方法则可以在不引用特定对象的情况下运行。按照约定，`Main` 静态方法是 C# 程序的入口点。

（有关注释的内容，不想写了）



## [Types and variables](https://learn.microsoft.com/en-us/dotnet/csharp/tour-of-csharp/#types-and-variables)

**类型（*type*）**可以定义 C# 中的任何数据的结构和行为。类型的声明可以包含其成员、基类、它实现的接口、以及该类型允许的操作。一个变量也就意味着对一个特定类型的实例的引用。

C# 中有两种类型：值类型（*value types*）与引用类型（*reference types*）。值类型的变量是实打实地携带自己的数据。而引用类型存储的是对数据（也就是通常所讲的「对象」）的引用。对于「引用类型」，两个变量可以引用同一个对象；对一个变量执行的运算可能会影响另一个变量引用的对象。对于「值类型」，

**标识符（*identifier*）**是变量名称。标识符是一串不带空格的 unicode 序列。

- 值类型

    - 简单类型
        - [有符号整型](https://learn.microsoft.com/en-us/dotnet/csharp/language-reference/builtin-types/integral-numeric-types)：`sbyte`、`short`、`int`、`long`
        - [无符号类型](https://learn.microsoft.com/en-us/dotnet/csharp/language-reference/builtin-types/integral-numeric-types)：`byte`、`ushort`、`uint`、`ulong`
        - [Unicode 字符](https://learn.microsoft.com/en-us/dotnet/standard/base-types/character-encoding-introduction)：`char`，表示 UTF-16 代码单元
        - [IEEE 二进制浮点](https://learn.microsoft.com/en-us/dotnet/csharp/language-reference/builtin-types/floating-point-numeric-types)：`float`、`double`
        - [高精度十进制浮点数](https://learn.microsoft.com/zh-cn/dotnet/csharp/language-reference/builtin-types/floating-point-numeric-types)：`decimal`
        - 布尔值：`bool`，表示布尔值（`true` 或 `false`）
    - [枚举类型](https://learn.microsoft.com/zh-cn/dotnet/csharp/language-reference/builtin-types/enum)
        - `enum E {...}` 格式的用户定义类型。`enum` 类型是一种包含已命名常量的独特类型。 每个 `enum` 类型都有一个基础类型（必须是八种整型类型之一）。 `enum` 类型的值集与基础类型的值集相同。
    - [结构类型](https://learn.microsoft.com/zh-cn/dotnet/csharp/language-reference/builtin-types/struct)
        - 格式为 `struct S {...}` 的用户定义类型
    - [可以为 null 的值类型](https://learn.microsoft.com/zh-cn/dotnet/csharp/language-reference/builtin-types/nullable-value-types)
        - 值为 `null` 的其他所有值类型的扩展
    - [元组值类型](https://learn.microsoft.com/zh-cn/dotnet/csharp/language-reference/builtin-types/value-tuples)
        - 格式为 `(T1, T2, ...)` 的用户定义类型

- [引用类型](https://learn.microsoft.com/zh-cn/dotnet/csharp/language-reference/keywords/reference-types)
    - [类类型](https://learn.microsoft.com/zh-cn/dotnet/csharp/language-reference/keywords/class)
        - 其他所有类型的最终基类：`object`
        - [Unicode 字符串](https://learn.microsoft.com/en-us/dotnet/standard/base-types/character-encoding-introduction)：`string`，表示 UTF-16 代码单元序列
        - 格式为 `class C {...}` 的用户定义类型

    - [接口类型](https://learn.microsoft.com/zh-cn/dotnet/csharp/language-reference/keywords/interface)
        - 格式为 `interface I {...}` 的用户定义类型
    - [数组类型](https://learn.microsoft.com/zh-cn/dotnet/csharp/programming-guide/arrays/)
        - 一维、多维与交错。例如：`int[]`、`int[,]` 和 `int[][]`
    - [委托类型](https://learn.microsoft.com/zh-cn/dotnet/csharp/language-reference/builtin-types/reference-types#the-delegate-type)
        - 格式为 `delegate int D(...)` 的用户定义类型



- `class` 类型可以定义数据结构，包含数据成员（域）和函数成员（方法、属性等等）。类类型支持单一继承和多态，也就是派生类可以延展和具体化基类的机制。
- `struct` 类型与类类型相似，也包含数据成员及函数成员。然而，与类不同的是，结构是值类型，通常不需要堆分配。结构不支持用户定义的继承，所有结构均隐式继承自 `object` 类型。
- `interface` 类型表示
- `delegate` 类型



C# 的类型系统是统一的，故其任意类型都可以被当做 `object`



## [Program structure](https://learn.microsoft.com/en-us/dotnet/csharp/tour-of-csharp/#program-structure)

C# 中核心结构概念包括：[程序（*programs*）](https://learn.microsoft.com/en-us/dotnet/csharp/fundamentals/program-structure/)、[命名空间（*namespaces*）](https://learn.microsoft.com/en-us/dotnet/csharp/fundamentals/types/namespaces)、[类型（*types*）](https://learn.microsoft.com/en-us/dotnet/csharp/fundamentals/types/)、[成员（*members*）](https://learn.microsoft.com/zh-cn/dotnet/csharp/programming-guide/classes-and-structs/members)与[程序集（*assemblies*）](https://learn.microsoft.com/zh-cn/dotnet/standard/assembly/)。



