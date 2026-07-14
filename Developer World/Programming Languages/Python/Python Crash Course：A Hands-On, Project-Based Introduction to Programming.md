# PART I: BASICS



## CHAPTER 1   GETTING STARTED



In this chapter you’ll run your first Python program, *hello_world.py*. First, you’ll need to check whether Python is installed on your computer; if it isn’t, you’ll install it. You’ll also install a text editor to work with your Python programs. Text editors recognize Python code and highlight sections as you write, making it easy to understand the structure of your code.

### Setting Up Your Programming Environment

Python differs slightly on different operating systems, so you’ll need to keep a few considerations in mind. Here, we’ll look at the two major versions of Python currently in use and outline the steps to set up Python on your system. 

#### Python 2 and Python 3

Today, two versions of Python are available: Python 2 and the newer Python 3. Every programming language evolves as new ideas and technologies emerge, and the developers of Python have continually made the language more versatile and powerful. Most changes are incremental and hardly noticeable, but in some cases code written for Python 2 may not run properly on systems with Python 3 installed. Throughout this book I’ll point out areas of significant difference between Python 2 and Python 3, so whichever version you use, you’ll be able to follow the instructions.

If both versions are installed on your system or if you need to install Python, use Python 3. If Python 2 is the only version on your system and you’d rather jump into writing code instead of installing Python, you can start with Python 2. But the sooner you upgrade to using Python 3 the better, so you’ll be working with the most recent version.

#### Running Snippets of Python Code

Python comes with an interpreter that runs in a terminal window, allowing you to try bits of Python without having to save and run an entire program.

Throughout this book, you’ll see snippets that look like this:

```shell
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

```shell
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

```shell
Hello Python world!
```











### Strings

Because most programs define and gather some sort of data, and then do something useful with it, it helps to classify different types of data. The first data type we’ll look at is the string. Strings are quite simple at first glance, but you can use them in many different ways.

A *string* is simply a series of characters. Anything inside quotes is considered a string in Python, and you can use single or double quotes around your strings like this: 

```shell
"This is a string."
'This is also a string.'
```

This flexibility allows you to use quotes and apostrophes within your strings:

```shell
'I told my friend, "Python is my favorite language!"'
"The language 'Python' is named after Monty Python, not the snake."
"One of Python's strengths is its diverse and supportive community."
```

Let’s explore some of the ways you can use strings.

#### Changing Case in a String with Methods

One of the simplest tasks you can do with strings is change the case of the words in a string. Look at the following code, and try to determine what’s happening:

```python
name = "ada lovelace"
print(name.title())
```

Save this file as *name.py*, and then run it. You should see this output:

```shell
Ada Lovelace
```

In this example, the lowercase string "`ada lovelace`" is stored in the variable `name`. The method `title()` appears after the variable in the `print()` statement. A *method* is an action that Python can perform on a piece of data. The dot (`.`) after `name` in `name.title()` tells Python to make the `title()` method act on the variable `name`. Every method is followed by a set of parentheses, because methods often need additional information to do their work. That information is provided inside the parentheses. The `title()` function doesn’t need any additional information, so its parentheses are empty.

`title()` displays each word in titlecase, where each word begins with a capital letter. This is useful because you’ll often want to think of a name as a piece of information. For example, you might want your program to recognize the input values `Ada`, `ADA`, and `ada` as the same name, and display all of them as `Ada`.

Several other useful methods are available for dealing with case as well. For example, you can change a string to all uppercase or all lowercase letters like this:

```python
name = "Ada Lovelace"
print(name.upper())
print(name.lower())
```

This will display the following:

```shell
ADA LOVELACE
ada lovelace
```

The `lower()` method is particularly useful for storing data. Many times you won’t want to trust the capitalization that your users provide, so you’ll convert strings to lowercase before storing them. Then when you want to display the information, you’ll use the case that makes the most sense for each string.

#### Combining or Concatenating Strings

#### Adding Whitespace to Strings with Tabs or Newlines

#### Stripping Whitespace



### Numbers

Numbers are used quite often in programming to keep score in games, represent data in visualizations, store information in web applications, and so on. Python treats numbers in several different ways, depending on how they are being used. Let’s first look at how Python manages integers, because they are the simplest to work with.

#### Integers

You can add (`+`), subtract (`-`), multiply (`*`), and divide (`/`) integers in Python. 

```cmd
>>> 2 + 3
5
>>> 3 - 2
1
>>> 2 * 3
6
>>> 3 / 2
1.5
```

In a terminal session, Python simply returns the result of the operation. Python uses two multiplication symbols to represent exponents:

```cmd
>>> 3 ** 2
9
>>> 3 ** 3
27
>>> 10 ** 6
1000000
```

Python supports the order of operations too, so you can use multiple operations in one expression. You can also use parentheses to modify the order of operations so Python can evaluate your expression in the order you specify. For example:

```cmd
>>> 2 + 3*4
14
>>> (2 + 3) * 4
20
```

The spacing in these examples has no effect on how Python evaluates the expressions; it simply helps you more quickly spot the operations that have priority when you’re reading through the code.

### Comments



















## CHAPTER 3   INTRODUCING LISTS



In this chapter and the next you’ll learn what lists are and how to start working with the elements in a list. Lists allow you to store sets of information in one place, whether you have just a few items or millions of items. Lists are one of Python’s most powerful features readily accessible to new programmers, and they tie together many important concepts in programming.

### What Is a List?

A *list* is a collection of items in a particular order. You can make a list that includes the letters of the alphabet, the digits from 0–9, or the names of all the people in your family. You can put anything you want into a list, and 38  Chapter 3 the items in your list don’t have to be related in any particular way. Because a list usually contains more than one element, it’s a good idea to make the name of your list plural, such as `letters`, `digits`, or `names`.

In Python, square brackets (`[]`) indicate a list, and individual elements in the list are separated by commas. Here’s a simple example of a list that contains a few kinds of bicycles:

```python
bicycles = ['trek', 'cannondale', 'redline', 'specialized']
print(bicycles)
```

If you ask Python to print a list, Python returns its representation of the list, including the square brackets:

```cmd
['trek', 'cannondale', 'redline', 'specialized'] 
```

Because this isn’t the output you want your users to see, let’s learn how to access the individual items in a list. 

#### Accessing Elements in a List

Lists are ordered collections, so you can access any element in a list by telling Python the position, or index, of the item desired. To access an element in a list, write the name of the list followed by the index of the item enclosed in square brackets.

For example, let’s pull out the first bicycle in the list bicycles: 

