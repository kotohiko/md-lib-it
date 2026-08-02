# What Really Happens When You Run hello_world.py

After learning Java, I tend to wonder how a new programming language works just by printing 'Hello World'. Unlike Java, C, and C++, Python's 'Hello World' seems much simpler—just one line of code. So, what is the interpreter actually doing under the hood when it runs this?

Once you've learned Java, you naturally stop looking at `print("Hello World")` as "just printing text" and start asking, **"What machinery is hidden behind this single line?"**

The short answer is:

> **Python's `print("Hello World")` looks simple because the interpreter hides a tremendous amount of work that Java makes you write explicitly.**

Let's see what happens step by step.

```python
print("Hello World")
```

Although it's one line, the interpreter performs many stages before the words appear on your screen.

## Step 1. Read the source file

The Python interpreter (`python.exe`) first opens your `.py` file and reads it as plain text. At this point it simply sees characters like

```
p r i n t ( " H e l l o ...
```

Nothing has been executed yet.

## Step 2. Lexical Analysis (Tokenization)

The interpreter breaks the text into tokens. Instead of characters, it now understands something like

```
NAME      print
LPAREN    (
STRING    "Hello World"
RPAREN    )
```

This is similar to what Java's compiler does.

## Step 3. Parsing

The parser checks whether the grammar is valid. It constructs an **Abstract Syntax Tree (AST)**. Conceptually it becomes

```
FunctionCall
    |
    +-- Function: print
    |
    +-- Argument:
          "Hello World"
```

Now Python knows

> "This is a function call."

## Step 4. Compile to Bytecode

This surprises many beginners.

Python is **not** purely interpreted. Before execution, CPython compiles the AST into **Python bytecode**.

The bytecode is something like

```
LOAD_NAME      print
LOAD_CONST     "Hello World"
CALL_FUNCTION
RETURN_VALUE
```

This is not machine code. It is an instruction set designed specifically for the **Python Virtual Machine (PVM)**. You can even inspect it:

```python
import dis

def hello():
    print("Hello World")

dis.dis(hello)
```

You'll see bytecode instructions such as `LOAD_GLOBAL`, `LOAD_CONST`, `CALL`, and `RETURN_VALUE` (the exact names vary slightly between Python versions).

## Step 5. Execute Bytecode

Now the Python Virtual Machine begins executing.

It works much like a CPU, but instead of hardware instructions, it executes Python bytecode.

Conceptually:

```
LOAD_GLOBAL print
```

↓

Find the built-in function named `print`.

```
LOAD_CONST "Hello World"
```

↓

Push the string object onto the evaluation stack.

```
CALL
```

↓

Invoke the `print` function with that argument.

## Step 6. What does `print()` actually do?

Here's where things become interesting.

`print` is **not** a language keyword. It is an ordinary built-in function implemented in C inside CPython. When the VM calls it, the function:

1. Converts each argument to a string if necessary.
2. Joins multiple arguments using the separator (`sep`, default `" "`).
3. Appends the ending (`end`, default `"\n"`).
4. Writes the result to `sys.stdout`.

So internally it behaves roughly like

```python
sys.stdout.write("Hello World\n")
```

## Step 7. Operating System Call

`sys.stdout` eventually reaches the operating system.

On Windows, CPython ultimately calls Windows APIs to write to the console.

On Linux or macOS, it eventually invokes system calls such as `write()`.

Only now do the characters finally appear in your terminal.

## Why is Java so much longer?

Java asks you to write things that Python supplies automatically.

Java:

```java
public class Hello {
    public static void main(String[] args) {
        System.out.println("Hello World");
    }
}
```

The JVM still performs almost the same internal work:

- Read source (or class file)
- Parse
- Verify
- Load classes
- Locate `main()`
- Invoke `System.out.println()`
- Eventually call the operating system

The difference is that Java exposes much more of the program structure.

Python hides it.

## An interesting comparison

Think of Java as driving a manual transmission. Before moving, you explicitly:

- start the engine
- engage the clutch
- choose first gear
- release the clutch
- accelerate

Python is like an automatic transmission. You simply press the accelerator. The gearbox still changes gears—it just happens automatically.

## What makes Python feel "magical"?

Several things are happening implicitly:

- `print` is already available without importing anything.
- There is no required `main()` function.
- There is no class declaration.
- Strings are created automatically.
- Memory allocation is handled automatically.
- The bytecode compiler runs automatically.
- The Python Virtual Machine starts automatically.
- The runtime environment is initialized automatically.

All of these steps still happen—they're just hidden from you. A simplified execution pipeline:

```
hello.py
    │
    ▼
Read source code
    │
    ▼
Tokenizer
    │
    ▼
Parser
    │
    ▼
Abstract Syntax Tree (AST)
    │
    ▼
Bytecode Compiler
    │
    ▼
Python Bytecode
    │
    ▼
Python Virtual Machine (PVM)
    │
    ▼
Built-in C function: print()
    │
    ▼
Operating System API
    │
    ▼
Terminal
```

If you're coming from Java, one of the most fascinating things you'll discover is that **Python is neither "just interpreted" nor "just compiled."** In CPython, it uses a hybrid execution model: your source code is first compiled into Python bytecode, and then that bytecode is interpreted by the Python Virtual Machine. This layered design is one reason Python remains flexible while still being portable across different operating systems and hardware architectures.