# Chapter 1. Introduction to JavaScript

JavaScript is the programming language of the web. The overwhelming majority of websites use JavaScript, and all modern web browsers—on desktops, **tablets**[^1-1], and phones—include JavaScript interpreters, making JavaScript the most-deployed programming language in history. Over the last decade, Node.js has enabled JavaScript programming outside of web browsers, and the **dramatic**[^1-2] success of Node means that JavaScript is now also the most-used programming language among
software developers. Whether you’re starting **from scratch**[^1-3] or are already using JavaScript professionally, this book will help you master the language.

If you are already familiar with other programming languages, it may help you to know that JavaScript is a **high-level**, **dynamic**, **interpreted** programming language that is well-suited to object-oriented and functional programming styles. JavaScript’s variables are untyped. Its syntax is **loosely**[^1-4] based on Java, but the languages are otherwise unrelated. JavaScript **derives**[^1-5] its first-class functions from Scheme and its prototype-based inheritance from the little-known language Self. But you do not need to know any of those languages, or be familiar with those terms, to use this book and learn JavaScript.

The name “JavaScript” is quite misleading. Except for a superficial syntactic resemblance, JavaScript is completely different from the Java programming language. And JavaScript has **long since**[^1-6] outgrown its scripting-language roots to become a robust and efficient general-purpose language suitable for serious software engineering and projects with huge codebases.

>   ### JAVASCRIPT: NAMES, VERSIONS, AND MODES
>
>   JavaScript was created at Netscape in the early days of the web, and technically, “JavaScript” is a trademark licensed from Sun Microsystems (now Oracle) used to describe Netscape’s (now Mozilla’s) implementation of the language. Netscape submitted the language for standardization to **ECMA—the European Computer Manufacturer’s Association**[^1-7]—and because of trademark issues, the standardized version of the language was stuck with the awkward name “ECMAScript.” In practice, everyone just calls the language JavaScript. This book uses the name “ECMAScript” and the abbreviation “ES” to refer to the language standard and to versions of that standard.
>
>   For most of the 2010s, version 5 of the ECMAScript standard has been supported by all web browsers. This book treats ES5 as the compatibility baseline and no longer discusses earlier versions of the language. ES6 was released in 2015 and added major new features—including class and module syntax—that changed JavaScript from a scripting language into a serious, general-purpose language suitable for large-scale software engineering. Since ES6, the ECMAScript specification has
>   moved to a yearly release cadence, and versions of the language—ES2016, ES2017, ES2018, ES2019, and ES2020—are now identified by year of release.
>
>   As JavaScript evolved, the language designers attempted to correct **flaws**[^1-8] in the early (pre-ES5) versions. In order to maintain backward compatibility, it is not possible to remove **legacy**[^1-9] features, no matter how flawed. But in ES5 and later, programs can **opt in**[^1-10] to JavaScript’s strict mode in which a number of early language mistakes have been corrected. The mechanism for opting in is the `use strict` directive described in §5.6.3. That section also summarizes the differences between legacy JavaScript and strict JavaScript. In ES6 and later, the use of new language features often implicitly invokes strict mode. For example, if you use the ES6 class keyword or create an ES6 module, then all the code within the class or module is automatically strict, and the old, flawed features are not available in those contexts. This book will cover the legacy features of JavaScript but is careful to point out that they are not available in strict mode.





>   [^1-1]: (*also* **Tablet PC<sup>TM</sup>**) a small computer that is easy to carry, with a large TOUCH SCREEN and usually without a physical keyboard
>   [^1-2]: (of a change, an event, etc.) sudden, very great and ofter surprising
>   [^1-3]: **from scratch**: without any previous preparation or knowledge
>   [^1-4]: in a way that is not exact
>   [^1-5]: (*formal*) to get sth from sth
>   [^1-6]: adverb of the distant or comparatively distant past
>   [^1-7]: is an industry association dedicated to the standardization of information and communication systems
>   [^1-8]: a mistake in sth that means that it is not correct or does not work correctly
>   [^1-9]: a situation that exists now because of events, actions, etc. that took place in the past
>
>   



## 1.1   Exploring JavaScript