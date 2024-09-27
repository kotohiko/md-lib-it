`@Bean` 是 Spring 框架中的一个注解，用于声明一个对象作为 Spring 容器管理的 Bean 实例。这个注解通常用于配置类（使用 `@Configuration` 注解的类）中，以便将一个或多个对象定义为 Spring 应用程序上下文的一部分。这样，当其他组件需要这些对象时，它们可以从 Spring 容器中自动获取。

`@Bean` 注解的主要用途包括：

- 实例化对象：通过 `@Bean` 注解，可以告诉 Spring 如何创建和配置一个对象。这允许你在不使用 XML 配置的情况下定义 Bean。
- 配置对象：`@Bean` 注解允许你在创建对象时为其提供自定义的初始化逻辑，例如设置属性值、调用初始化方法等。
- 控制 Bean 的生命周期：通过 `@Bean` 注解，你可以控制 Bean 的生命周期，例如定义 Bean 的作用域（单例、原型等）和销毁方法。
- 依赖注入：`@Bean` 注解允许你在其他Bean中自动注入所需的依赖项。Spring 容器会自动处理依赖关系并返回所需的 Bean 实例。

示例：

```java
@Configuration
public class AppConfig {

    @Bean
    public MyService myService() {
        return new MyServiceImpl();
    }

    @Bean
    public MyRepository myRepository() {
        return new MyRepositoryImpl();
    }
}
```

在这个示例中，`AppConfig` 类使用了 `@Configuration` 注解，表示它是一个配置类。在这个类中，我们声明了两个 Bean：`myService` 和 `myRepository`。==当Spring 容器启动时，它会自动创建这些 Bean 的实例，并在需要时将它们注入到其他组件中。==

总之，`@Bean` 注解在 Spring 框架中用于定义和配置 Bean 实例，使得开发者能够更加灵活地管理和使用对象之间的依赖关系。