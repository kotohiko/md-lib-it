在 Spring MVC 中，**拦截器（Interceptor）**是一种用于在请求处理流程中的特定点执行任务的技术，类似于 Servlet 中的**过滤器（Filter）**。

### 自定义拦截器

1.  定义一个类，实现 `org.springframework.web.servlet.HandlerInterceptor` 接口
2.  创建拦截方法
    -   实现 `preHandle` 方法，在控制器调用之前执行。如果返回 `false`，则中断请求处理。
    -   实现 `preHandle` 方法，在控制器调用之后执行，但在视图渲染之前。
    -   实现 `afterCompletion` 方法，在请求完成后执行，用于清理资源。
3.  注册拦截器。在 Spring MVC 的配置类中注册拦截器，可以使用 `WebMvcConfigurer` 接口的 `addInterceptors` 方法。
4.  配置拦截器的路径匹配。通过 `addPathPatterns` 方法指定拦截器应该应用于哪些请求路径
5.  配置拦截器的排除路径。使用 `excludePathPatterns` 方法指定哪些请求路径应该被拦截器忽略。
6.  配置拦截器的顺序。使用 `order` 方法设置拦截器的顺序，数字越小，优先级越高。
7.  启用 MVC 配置。确保配置类上使用了 `@EnableWebMvc` 注解，以启用 Web MVC 配置。

```java
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

@Component
public class MyInterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        // 在控制器调用之前执行
        System.out.println("Before controller...");
        return true; // 返回 true 继续流程，返回 false 则中断请求
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) {
        // 在控制器调用之后，视图渲染之前执行
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) {
        // 请求完成后执行
    }
}

```



```java
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new MyInterceptor())
                .addPathPatterns("/**") // 拦截所有请求
                .excludePathPatterns("/ignore") // 排除路径
                .order(1); // 设置拦截器顺序
    }
}

```



### 与拦截器的区别

-   过滤器主要用于对请求和响应进行预处理和后处理，拦截器主要用于在方法调用前后进行处理。
-   过滤器适用于整个 Web 应用程序，可以对所有请求进行处理，拦截器适用于特定的控制器或方法，可以针对特定的业务逻辑进行处理
-   过滤器的生命周期由 Sevlet 容器管理，通常在应用启动时初始化，并在应用关闭时销毁；拦截器的生命周期由 Spring 容器管理，通常在 Spring 上下文初始化时创建，并在上下文关闭时销毁