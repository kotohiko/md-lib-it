# What Really Happens When You Run hello_world.py

Simplicity and elegance are Python's most charming traits. This exceptionally low barrier to entry makes it the top choice for countless developers taking their first steps into programming.

Just type this line of code 

```python
print("Hello World!")
```

into your IDE or Shell, and it will run instantly. When saving your code to a file, remember that while Python can execute any file extension, you should always use `.py` so your system and editor can correctly identify it.

> ### 🤣 FUN FACT
>
> Is that statement accurate? Does a Python file *really* have to end in `.py` to execute? Turns out, nope!
>
> Sounds completely bogus, right? But try copying your `hello_world.py` file and renaming it to `hello_world.python`. At this point, PyCharm will throw its hands in the air and go, *"I don't know her."*
>
> However, if you open CMD in that directory and fire off `python hello_world.python`, boom—it prints out your message without skipping a beat! 
>
> ```cmd
> D:\Coding>python hello_world.py
> Hello World!
> 
> D:\Coding>python hello_world.python
> Hello World!
> ```
>
> The truth is, when you run `python {filename}` via the command line, the interpreter couldn't care less about the file extension. Whether your file is named `test.python`, `test.txt`, or literally has no extension at all, typing `python test.python` will run it like a charm.
>
> We use `.py` mostly as a courtesy so operating systems and IDEs (like PyCharm or VS Code) actually know it's a Python script—giving you sweet perks like syntax highlighting, auto-completion, and the ability to double-click to run.
>
> That said, there is one hilarious catch: **the `import` system isn't nearly as chill.** While the command line lets you run any weirdly named file, Python's `import` mechanism is strictly a `.py` purist. If you try to do `import my_module` when the file is named `my_module.txt` or `my_module.python`, Python will immediately panic with a `ModuleNotFoundError`—unless you feel like bending over backward to hack custom loaders with `importlib`!

When you execute the line of code, your computer goes through four main steps to turn code into pixels on your screen.

1. Parsing: The **Python interpreter** reads the line, recognizing `print` as a built-in function and `"Hello World!"` as a text string.
2. Compiling: Python translates this line into intermediate instructions called bytecode, which the Python Virtual Machine (PVM) can understand.
3. System Call: The PVM executes the bytecode and asks the operating system (OS) to write the text to the standard output device (your screen).
4. Rendering: The OS uses the graphics card and drivers to convert the characters into pixels, displaying "Hello World!" in your console or terminal.

# Variables

Variables are fundamental concepts that programming languages can hardly do without. They are also something you will face every day in your future studies, research, and work if you deal with code daily. The basic syntax of variables in Python is almost no different from other programming languages.

```python
message = "Hello Python world!"
print(message)
```

## Naming and Using Variables

When switching from one programming language to a new one, following the new language's development standards is both necessary and wise. Here are several variable naming conventions for Python. You can memorize them naturally as you get more practice, without trying too hard:

- Basic Character Rules
    - Variable names can only contain letters, numbers, and underscores, and they **cannot start with a number**.
    - Spaces and special symbols cannot be used in variable names. To separate words for better readability, use an underscore (e.g., `student_age`).
    - Python 3 allows Chinese characters for naming variables. However, for team collaboration and professional coding standards, using Chinese in production environments is **not recommended**.
- Avoid Name Conflicts
    - Never use Python's [reserved words (keywords)](https://docs.python.org/3/reference/lexical_analysis.html#keywords) (such as `if`, `for`, `class`, etc.) as variable names, or it will cause a syntax error.
    - Avoid using built-in function names (such as `list`, `sum`, `str`, etc.) to name variables. Doing so overrides the built-in feature and leads to unexpected bugs.
- Be Clear and Concise
    - Variable names should be **short and self-explanatory** (e.g., use `age` instead of `a` or `student_age_number_value`).
- Confusing Characters
    - Avoid using the standalone lowercase letter `l` or uppercase letter `O`. They look too similar to the numbers `1` and `0` in most fonts.

## Avoiding Name Errors When Using Variables

The Python interpreter has an interesting error-correction mechanism…

(TBA)

### Variables Are Labels

When programming with variables, the output may sometimes contradict expectations; however, thoroughly understanding the language's underlying mechanisms can help avoid these unexpected situations. If you are coming to Python from another programming language, it is very helpful to understand the tag/box mechanism. If you are a complete beginner, just knowing it exists is enough for now, as we will cover it later.

> ### 🧐 DEEP DIVE
>
> #### Comparing Variable Mechanisms in Python, Java, and C
>
> To understand this clearly, let us imagine variables as either **"Boxes"** or **"Labels"**.
>
> ##### 1. Core Difference: Boxes (C) vs. Labels (Python)
>
> ###### C Language: Variables are "Fixed Boxes"
>
> - C is a **statically-typed** language.
> - When you declare a variable, you must first carve out a fixed-size "memory box" (e.g., `int a = 10;`). 
> - This box can only hold integers. If you change `a` to `20`, you are changing the content inside the box, but the box itself (the memory address) stays the same.
>
> ###### Python: Variables are "Name Labels"
>
> - Python is a **dynamically-typed** language.
> - A variable is not a box, but a **label** (reference) that you stick onto an object.
> - When you write `x = 10`, Python creates an integer object `10` in memory, then sticks the label `x` onto it.
> - If you then write `x = "Hello"`, Python creates a string object `"Hello"`, tears the label `x` off `10`, and sticks it onto `"Hello"`.
>
> ##### 2. Python vs. Java: Are They More Similar?
>
> **Yes, Python's variable mechanism is highly similar to Java's!** Both languages fundamentally use **object references**.
>
> Even though Java requires you to explicitly state types (e.g., `String msg = "Hello";`), their underlying logic for objects is almost identical:
>
> ###### Similarities: Both Use "Labels"
>
> - **Java object variables are labels**. When you write `String msg = new String("Hello");`, `msg` is just a reference pointing to a string object in the heap memory. This matches Python's labeling logic exactly.
> - **Garbage Collection**: When an object has no more labels (references) pointing to it, both Python and Java will automatically activate garbage collection to clean that useless object out of memory.
>
> ###### Differences: Java Has "Exceptions" (Primitive Types)
>
> To run faster, Java keeps a feature from C: **Primitive Types**.
>
> - **Java Primitive Types** (like `int`, `double`, `boolean`): These are **real boxes**. The variable directly stores the actual value, not a label.
> - **Java Reference Types** (like `String`, `List`, Custom Classes): These are **labels**.
> - **Everything in Python**: In Python, **everything is an object** (including the number `1` and the boolean `True`). Python has no primitive types; everything uses labels.

# Strings

## Changing Case in a String with Methods



# Numbers

# Comments

# The Zen of Python

