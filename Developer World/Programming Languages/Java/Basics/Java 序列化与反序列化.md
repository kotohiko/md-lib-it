## 1   序列化与反序列化

### 1.1   概述

序列化是将 Java 对象转换为==字节流==的过程，这样对象可以通过==网络传输==、==远程调用==、分布式系统中数据交换、==持久化存储==（如保存到数据库或文件）或者==缓存==。Java 提供了 `java.io.Serializable` 接口来支持序列化，只要类实现了这个接口，就可以将该类的对象进行序列化。

而反序列化则是将字节流重新转换为对象的过程，即从存储中读取数据并重新创建对象。

### 1.2   理解

序列化其实就是将对象转化成可传输的字节序列格式，以便于存储和传输。

因为对象在 JVM 中可以认为是「立体」的，会有各种引用，比如在内存地址 `0x1234` 引用了某某对象，那此时这个对象要传输到网络的另一端的时候，就需要把这些引用 “压扁”。

因为网络的另一端内存地址 `0x1234` 可以没有某某对象，所以传输的对象需要包含这些信息，然后接收端讲这些扁平的信息再反序列化得到对象。

### 1.3   `ObjectInputStream` 与 `ObjectOutputStream`

`java.io` 包中的 `ObjectInputStream` 和 `ObjectOutputStream` 用于对象的序列化和反序列化。序列化是将对象的状态转换为字节流的过程，而反序列化则是将字节流恢复为对象的过程。这两个类通常用于在网络传输或持久化存储对象时。

#### 1.3.1   `ObjectOutputStream`

==`ObjectOutputStream` 用于将对象写入输出流==。它提供了 `writeObject(Object obj)` 方法，==可以将实现了 `Serializable` 接口的对象序列化并写入流中。==

#### 1.3.2   `ObjectInputStream`

==`ObjectInputStream` 用于从输入流中读取对象==。它提供了 `readObject()` 方法，可以将==字节流转换回对象。读取的对象必须与写入的对象的类型兼容。==

#### 1.3.3   使用示例

下面是一个简单的示例，演示如何使用 `ObjectOutputStream` 和 `ObjectInputStream` 进行对象的序列化和反序列化。

##### 创建一个可序列化的类

```java
import java.io.Serializable;

public class Person implements Serializable {
    
    // 建议添加此字段以保持版本一致性
    private static final long serialVersionUID = 1L; 
    private String name;
    private int age;

    public Person(String name, int age) {
        this.name = name;
        this.age = age;
    }

    @Override
    public String toString() {
        return "Person{name='" + name + "', age=" + age + '}';
    }
}
```

##### 序列化对象

```java
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;

public class SerializeExample {
    public static void main(String[] args) {
        Person person = new Person("Alice", 30);

        try (FileOutputStream fileOut = new FileOutputStream("person.ser");
            ObjectOutputStream out = new ObjectOutputStream(fileOut)) {
            out.writeObject(person);
            System.out.println("Serialized data is saved in person.ser");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
```

##### 反序列化对象

```java
import java.io.FileInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;

public class DeserializeExample {
    public static void main(String[] args) {
        Person person = null;

        try (FileInputStream fileIn = new FileInputStream("person.ser");
            ObjectInputStream in = new ObjectInputStream(fileIn)) {
            person = (Person) in.readObject();
            System.out.println("Deserialized Person: " + person);
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }
}
```

#### 1.3.4   总结

1. **序列化**：使用 `ObjectOutputStream` 将对象转换为字节流并保存到文件中。
2. **反序列化**：使用 `ObjectInputStream` 从文件中读取字节流并将其转换回对象。



### 1.4   `Serializable` 接口

首先声明 `Serializable` 这个接口没有实际意义，就是起的标记作用。

看一下 `java.io.ObjectOutputStream#writeObject0` 的源码，除了 `String`、数组、枚举，如果实现了这个接口，就会走 `writeOrdinaryObject` 方法，否则序列化就会报错。

```java
// remaining cases
            if (obj instanceof String) {
                writeString((String) obj, unshared);
            } else if (cl.isArray()) {
                writeArray(obj, desc, unshared);
            } else if (obj instanceof Enum) {
                writeEnum((Enum<?>) obj, desc, unshared);
            } else if (obj instanceof Serializable) {
                writeOrdinaryObject(obj, desc, unshared);
            } else {
                if (extendedDebugInfo) {
                    throw new NotSerializableException(
                        cl.getName() + "\n" + debugInfoStack.toString());
                } else {
                    throw new NotSerializableException(cl.getName());
                }
            }
```

### 1.5   `serialVersionUID` 的用处

```java
private static final long serialVersionUID = 1L;
```

在浏览各大开源项目的时候，类似于这样的代码会很常见，这个 ID 其实就是用来验证序列化的对象和反序列化对应的对象的 ID 是否一致。

所以这个 ID 的数字其实并不重要，不论是 `1L` 还是 IDEA 自动生成的，只要序列化的时候，对象的 `serialVersionUID` 和反序列化的时候对象的 `serialVersionUID` 一致就可以。

如果没有显式制定 `serialVersionUID`，那么编译器会根据类的相关信息自动生成一个，可以认为是一个指纹。

如果没有定义一个 `serialVersionUID`，然后序列化一个对象之后，在反序列化之前把对象的类的结构改了，比如增加了一个成员变量，那么此时反序列化会失败。

正是因为类的结构变了，生成的 “指纹” 变了，那么 `serialVersionUID` 也就不一致了。

总而言之，`serialVersionUID` 就是起到的验证的作用。

另外值得一提的是，Java 序列化不包含静态变量。

简而言之就是序列化之后存储的内容不包含静态变量的值。看一下以下代码就很清晰了。

```java
package org.jacob.serial.statictest;

import org.jacob.serial.constants.CpjSerialConstants;

import java.io.*;

/**
 * Demonstrates the serialization and deserialization of a class with a static variable.
 * The class is serialized to a file, and after modifying the static variable,
 * it is deserialized to observe if the static variable retains its modified value.
 * <p>
 * Note: Static variables are not part of an object's state and thus are not serialized.
 *
 * @author Kotohiko
 * @since 13:09 Oct 10, 2024
 */
public class StaticVarSerialTest implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * A static integer variable initialized to 1.
     */
    public static int num = 1;

    /**
     * Main method that demonstrates serialization and deserialization of the StaticVarSerialTest instance.
     * It writes the current instance to a file, modifies the static variable {@code num},
     * reads back the object from the file, and prints the value of {@code num} after deserialization.
     *
     * @param args Command line arguments (not used).
     */
    @SuppressWarnings("all")
    public static void main(String[] args) {
        try (
                FileOutputStream fileOutputStream = new FileOutputStream(CpjSerialConstants.CPJ_STATIC_VAR_SERIAL_PATH);
                ObjectOutputStream objectOutputStream = new ObjectOutputStream(fileOutputStream)
        ) {
            objectOutputStream.writeObject(new StaticVarSerialTest());

            StaticVarSerialTest.num = 666;

            ObjectInputStream objectInputStream
                    = new ObjectInputStream(new FileInputStream(CpjSerialConstants.CPJ_STATIC_VAR_SERIAL_PATH));
            StaticVarSerialTest t = (StaticVarSerialTest) objectInputStream.readObject();

        } catch (IOException | ClassNotFoundException e) {
            throw new RuntimeException(e);
        }

        System.out.println(StaticVarSerialTest.num);
    }
}
```

```
666
```





## 2   `transient` 修饰符

在 Java 中，`transient` 关键字用于标记一个类的字段，以指示该字段在序列化过程中不应被序列化。换句话说，==`transient` 修饰的字段不会被写入对象的序列化流中==。==因此在反序列化时，该字段的值将会被初始化为其类型的默认值。==

### 2.1   使用场景

1. **敏感信息**：如果对象中包含敏感数据（如密码），不希望将其序列化到文件或网络中，可以使用 `transient`。
2. **不必要的数据**：某些字段可能在序列化时是不必要的，或者在重新创建对象时可以重新计算或恢复，因此可以将这些字段标记为 `transient`。
3. **避免序列化的循环引用**：在某些复杂对象中，某些字段可能导致序列化时的循环引用或不必要的复杂性，可以通过标记为 `transient` 来避免这些问题。

### 2.2   示例代码

以下是一个使用 `transient` 的示例。

#### 2.2.1   创建一个包含 `transient` 字段的可序列化类

```java
package org.jacob.serial.entity;

import java.io.Serial;
import java.io.Serializable;

/**
 * @author Kotohiko
 * @since 00:54 Oct 10, 2024
 */
public class User implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;
    private final String username;
    // 不希望序列化的字段
    private final transient String password;

    public User(String username, String password) {
        this.username = username;
        this.password = password;
    }

    @Override
    public String toString() {
        return "User{username='" + username + "', password='" + password + "'}";
    }
}

```

#### 2.2.2 序列化对象

```java
package org.jacob.serial;

import org.jacob.serial.constants.CpjSerialConstants;
import org.jacob.serial.entity.User;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;

/**
 * @author Kotohiko
 * @since 00:53 Oct 10, 2024
 */
public class SerializeUser {
    public static void main(String[] args) {

        User user = new User("Alice", "secretPassword");

        try (
                FileOutputStream fileOut = new FileOutputStream(CpjSerialConstants.CPJ_USER_SERIAL_PATH);
                ObjectOutputStream out = new ObjectOutputStream(fileOut)
        ) {
            out.writeObject(user);
            System.out.println("Serialized data is saved in user.ser");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

```

#### 2.3.3   反序列化对象

```java
package org.jacob.serial;

import org.jacob.serial.constants.CpjSerialConstants;
import org.jacob.serial.entity.User;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;

/**
 * @author Kotohiko
 * @since 00:59 Oct 10, 2024
 */
public class DeserializeUser {
    public static void main(String[] args) {
        try (
                FileInputStream fileIn = new FileInputStream(CpjSerialConstants.CPJ_USER_SERIAL_PATH);
                ObjectInputStream in = new ObjectInputStream(fileIn)
        ) {
            User user = (User) in.readObject();
            System.out.println("Deserialized User: " + user);

        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }
}

```

当你运行 `SerializeUser` 类时，输出会类似于：

```
Serialized data is saved in user.ser
```

然后运行 `DeserializeUser` 类时，输出将是：

```
Deserialized User: User{username='Alice', password='null'}
```

### 2.3   总结

- 使用 `transient` 修饰符可以防止字段在序列化过程中被保存。
- 反序列化时，`transient` 字段的值将被初始化为默认值（如 `null`，对于对象类型，或 0，针对基本数据类型）。



## 3   序列化存在的问题

### 3.1   性能问题

Java 的默认序列化机制可能比较慢，尤其是对于大规模分布式系统。通常会采用更加高效的序列化框架（如 Protobuf、Kryo）。

### 3.2   安全性问题

反序列化是一个潜在的安全风险，因为通过恶意构造的字节流，可能会加载不安全的类或执行不期望的代码。因此，反序列化过程需要进行输入验证，避免反序列化漏洞。

---

本节相关内容也可以翻阅《Effective Java》[美] Joshua Bloch 著（原书第 3 版），第 12 章有更多关于序列化的讨论。