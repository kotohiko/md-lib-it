# PART I: BASICS



## CHAPTER 1   GETTING STARTED



### Setting Up Your Programming Environment



#### Python 2 and Python 3



#### Running Snippets of Python Code

Python comes with an interpreter that runs in a terminal window, allowing you to try bits of Python without having to save and run an entire program.

Throughout this book, you’ll see snippets that look like this:

```python
>>> print("Hello Python interpreter!")
Hello Python interpreter!
>>>
```

The text on 1st line is what you’ll type in and then execute by pressing enter. Most of the examples in the book are small, self-contained programs that you’ll run from your editor, because that’s how you’ll write most of your code. But sometimes basic concepts will be shown in a series of snippets run through a Python terminal session to demonstrate isolated concepts more efficiently. Any time you see the three angle brackets in a code listing u, you’re looking at the output of a terminal session. We’ll try coding in the interpreter for your system in a moment. 

#### Hello World!



### Python on Different Operating Systems





## CHAPTER 2   VARIABLES AND SIMPLE DATA TYPES

### What Really Happens When You Run hello_world.py

Let’s take a closer look at what Python does when you run *hello_world.py*. As it turns out, Python does a fair amount of work, even when it runs a simple program:

```python
print("Hello Python world!")
```

When you run this code, you should see this output:

```cmd
Hello Python world!
```

When you run the file *hello_world.py*, the ending *.py* indicates that the file is a Python program. Your editor then runs the file through the *Python interpreter*, which reads through the program and determines what each word in the program means. For example, when the interpreter sees the word print, it prints to the screen whatever is inside the parentheses.

As you write your programs, your editor highlights different parts of your program in different ways. For example, it recognizes that `print` is the name of a function and displays that word in blue. It recognizes that “Hello Python world!” is not Python code and displays that phrase in orange. This feature is called *syntax highlighting* and is quite useful as you start to write your own programs.

### Variables

Let’s try using a variable in *hello_world.py*. Add a new line at the beginning of the file, and modify the second line:

```python
message = "Hello Python world!"
print(message)
```

Run this program to see what happens. You should see the same output you saw previously:

```cmd
Hello Python world!
```







































