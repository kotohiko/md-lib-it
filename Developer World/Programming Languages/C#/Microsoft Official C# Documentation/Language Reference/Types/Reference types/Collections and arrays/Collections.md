# Collections



.NET 提供了很多集合类型，可以存储、管理相关对象的组。

## [Indexable collections](https://learn.microsoft.com/en-us/dotnet/csharp/language-reference/builtin-types/collections#indexable-collections)

所谓**可索引集合（*indexable collection*）**是指可以通过索引来访问其中每个元素的集合。某个元素的**索引（*index*）**指的是在序列中排在它前面的元素的数量。因此，索引 `0` 引用的元素就是第一个元素，它前面没有任何元素；索引 `1` 就是第二个元素，以此类推。以下示例将使用 [`List<T>`](https://learn.microsoft.com/en-us/dotnet/api/system.collections.generic.list-1) 类。它是最常见的可索引集合。

以下示例将创建并初始化一个字符串列表、移除元素、队尾添加元素。并且每一次修改都会使用 [foreach](https://learn.microsoft.com/en-us/dotnet/csharp/language-reference/statements/iteration-statements#the-foreach-statement) 语句或 `for` 循环来遍历字符串列表。

```c#
// Create a list of strings by using a
// collection initializer.
List<string> salmons = ["chinook", "coho", "pink", "sockeye"];

// Iterate through the list.
foreach (var salmon in salmons)
{
    Console.Write(salmon + " ");
}
// Output: chinook coho pink sockeye

// Remove an element from the list by specifying
// the object.
salmons.Remove("coho");


// Iterate using the index:
for (var index = 0; index < salmons.Count; index++)
{
    Console.Write(salmons[index] + " ");
}
// Output: chinook pink sockeye

// Add the removed element
salmons.Add("coho");
// Iterate through the list.
foreach (var salmon in salmons)
{
    Console.Write(salmon + " ");
}
// Output: chinook pink sockeye coho
```



## [Key/value pair collections](https://learn.microsoft.com/en-us/dotnet/csharp/language-reference/builtin-types/collections#keyvalue-pair-collections)

C# 的字典集合为 [`Dictionary<TKey,TValue>`](https://learn.microsoft.com/en-us/dotnet/api/system.collections.generic.dictionary-2)。

以下示例创建 `Dictionary` 集合并通过使用 `foreach` 语句循环访问字典。

```c#
private static void IterateThruDictionary()
{
    Dictionary<string, Element> elements = BuildDictionary();

    foreach (KeyValuePair<string, Element> kvp in elements)
    {
        Element theElement = kvp.Value;

        Console.WriteLine("key: " + kvp.Key);
        Console.WriteLine("values: " + theElement.Symbol + " " +
            theElement.Name + " " + theElement.AtomicNumber);
    }
}

public class Element
{
    public required string Symbol { get; init; }
    public required string Name { get; init; }
    public required int AtomicNumber { get; init; }
}

private static Dictionary<string, Element> BuildDictionary() =>
    new ()
    {
        {"K",
            new (){ Symbol="K", Name="Potassium", AtomicNumber=19}},
        {"Ca",
            new (){ Symbol="Ca", Name="Calcium", AtomicNumber=20}},
        {"Sc",
            new (){ Symbol="Sc", Name="Scandium", AtomicNumber=21}},
        {"Ti",
            new (){ Symbol="Ti", Name="Titanium", AtomicNumber=22}}
    };
```



## Iterators



## LINQ and collections

