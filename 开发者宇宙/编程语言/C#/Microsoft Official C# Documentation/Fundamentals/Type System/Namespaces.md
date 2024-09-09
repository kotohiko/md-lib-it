在 C# 中，「命名空间」这个概念频繁出现，体现在两个方面。其一，.NET 通过命名空间来把很多类组织起来，如下：

```c#
System.Console.WriteLine("Hello World!");
```

[`System`](https://learn.microsoft.com/en-us/dotnet/api/system) 就是一个命名空间，而 [`Console`](https://learn.microsoft.com/en-us/dotnet/api/system.console) 是里面的一个类。通过 `using` 关键词可以省去书写类全名，如下所示：

```c#
using System;
```

```c#
Console.WriteLine("Hello World!");
```



其二，在做大型项目的时候，声明命名空间可以帮你限定类和方法的名称的有效范围。使用 [`namespace`](https://learn.microsoft.com/en-us/dotnet/csharp/language-reference/keywords/namespace) 关键词声明一个命名空间，如下所示：

```c#
namespace SampleNamespace
{
    class SampleClass
    {
        public void SampleMethod()
        {
            System.Console.WriteLine(
                "SampleMethod inside SampleNamespace");
        }
    }
}
```

需注意的是命名空间的名称必须符合 [C# 标识符命名](https://learn.microsoft.com/en-us/dotnet/csharp/fundamentals/coding-style/identifier-names)规范。

