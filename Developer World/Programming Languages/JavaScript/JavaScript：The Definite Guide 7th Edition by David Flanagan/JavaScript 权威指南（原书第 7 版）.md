# 第 1 章   JavaScript 简介



我对于 JavaScript 的认知，从初识至今（2025 年）的十年以来，一直都是前端、或者脚本语言，殊不知 JavaScript 在这漫长的 10 年间悄然发生了翻天覆地的变化。毕竟身为后端程序员，关心 JavaScript 比较少。

是什么造就了这样的变化？那就不得不提一嘴 Node.js 了。Node.js 是一个基于 Chrome V8 引擎 的 JavaScript 运行时环境。它可以将 JavaScript 从浏览器中 “解放” 出来，让 JavaScript 可以在服务器端、命令行执行。进一步来讲，那便是：在以前，JavaScript 只能在浏览器里跑，主要做网页交互。如今有了 Node.js，JavaScript 也可以像 Python、Java 那样，编写后端服务、命令行工具，甚至操作系统脚本。

虽然名字和 Java 很像，甚至语法风格看起来也有点像，但是 JavaScript 与 Java 有一个非常显著的区别，就是 JavaScript 是动态的编程语言。所谓动态，就是指在运行时（runtime）可以改变程序结构或变量类型。

例如，

1. 变量类型是动态的，不固定

    ```js
    let x = 42;      // x 是 Number
    x = "hello";     // 现在 x 是 String
    ```

    👉 不需要提前声明变量类型，类型会在运行时自动决定。

2. 对象结构可随时改变

    ```js
    let obj = { a: 1 };
    obj.b = 2;          // 运行时新增属性
    delete obj.a;       // 删除属性
    ```

    👉 运行时就能修改对象的结构。

3. 可以在运行时生成和执行代码

    ```js
    eval("console.log('动态执行代码')");
    ```

    👉 `eval`、`Function` 构造器能在程序运行时解析并执行字符串代码。

4. 函数本身也是对象

    可以动态添加属性、修改甚至替换函数。

相对的，静态语言的特点是

- 在编译阶段就需要确定所有变量的类型和结构，运行时不能随意改变；
- 数据结构（类、结构体）的字段固定，不能随便增删；
- 没有或者用友极少运行时代码生成能力。

Java 正是典型的静态语言，举例如下：

```java
int x = 42;
x = "hello";  // ❌ 编译错误，不能把字符串赋给 int
```

```java
class Person {
    int age;
    String name;
}
Person p = new Person();
p.gender = "male"; // ❌ 编译错误，类中没有 gender 字段
```

其他动态语言还包括 Python、Ruby、PHP。静态语言还包括 C/C++、Go、Rust。似乎静态语言的共同点是偏向于做大型项目，动态语言的特点偏向于灵活、运行脚本。



## 1.3   JavaScript 之旅



