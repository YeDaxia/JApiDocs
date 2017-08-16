
> [English ReadMe](README-EN.md)

# 简介

一个简单的[示例](https://yedaxia.github.io/play-apidocs/)。 

JApiDocs 是一个符合 Java 编程习惯的 Api 文档生成工具。最大程度地利用 Java 的语法特性，你只管用心设计好接口，添加必要的注释，JApiDocs 会帮你导出一份漂亮的 Html 文档，并生成相关的 Java 和 Object-C 相关数据模型代码，从此，Android 和 IOS 的同学可以少敲很多代码了，你也不需要费力维护接口文档的变化，只需要维护好你的代码就可以了。

# 特性

1. 以一个 Controller 作为一组接口导出到一个 Html 文件中。
2. 支持生成 Java 和 Object-C 语言的 Response 模型代码。
3. 深度支持 [Spring Boot](http://projects.spring.io/spring-boot/)， [PlayFramework](https://www.playframework.com/)，[JFinal](http://www.jfinal.com/)，不需要额外声明路由。
4. 支持一般的 Java Web 工程，需要在相关方法添加额外的路由。
5. 支持接口声明过时(`@Deprecated`)，方便的文档目录等。
6. 支持自定义代码生成模板。
7. [:new: 支持集成发布到 rap](https://github.com/YeDaxia/JApiDocs/wiki/%E9%9B%86%E6%88%90-Rap-%E6%8E%A5%E5%8F%A3%E6%B5%8B%E8%AF%95) (测试阶段)。

# 快速使用

1. 我们以 spring 为例，一张图很容易就明白了 JApidocs 是怎么工作的了，你在设计接口的时候可以顺便就把相关的注释给填好了，这和 Java 程序员的编程习惯是保持一致的。

![spring controller](http://ohb4y25jk.bkt.clouddn.com/spring-controllers.png)

这里你可能会对`@ApiDoc`注解感到迷惑，这也是唯一需要一点额外工作的地方，别急，下面马上就讲到它。

2. `@ApiDoc` 是我们定义的一个注解，除非程序运行起来，否则我们是没办法知道 `response` 里面都包含有哪些内容，但是我们明明有了相关的视图类，为了解决这个问题，我们折衷设计了这个基于`RetentionPolicy.SOURCE`的注解，它不会给现有的代码造成任何的负担。由于是基于 Java 源码进行解析的，所以你不需要依赖我们的 Jar 包，你可以在你自己的工程任意地方添加这个简单的类即可，当然，如果你连这个也不愿意也是没关系的，你只需要直接添加我们的 Jar 包即可，里面已经为你准备好这个类了。

``` java

@Retention(RetentionPolicy.SOURCE)
@Target({ElementType.METHOD})
public @interface ApiDoc {

    /**
     * result class
     * @return
     */
	Class<?> value() default Null.class;

    /**
     * result class
     */
	Class<?> result() default Null.class;

    /**
     * request url
     */
	String url() default "";

    /**
     * request method
     */
	String method() default "get";

    final class Null{

    }
}

```

如果你用的是我们深度支持的 MVC 框架，那么你只需要写好返回的视图模型就可以了。

3. 集成依赖和运行程序

**命令行模式:**

下载`all`包，然后在和这个`jar`包相同目录下创建名称是`docs.config`的配置文件，里面可以配置这几个参数：

```json
projectPath = 工程目录（必须）
docsPath = 文档输出目录（非必须，默认是${projectPath}/apidocs）
codeTplPath = 代码模版目录 (非必须，如果你需要自定义生成的代码才会用到。)
mvcFramework = [spring, play, jfinal, generic](非必须，代码内部有判断，如果出现误判的情况，可以通过这个强制指定)

```
配置好之后，运行该`jar`包就可以了。

```java
java -jar ***-all.jar
```

**代码模式**

如果想做一些持续集成的话，代码模式还是比较方便的，直接添加依赖或者下载相关`jar`包，其中`min`包是不包含第三方依赖的。

```
compile 'io.github.yedaxia:japidocs:1.0'
compile 'com.google.code.gson:gson:2.8.0'
compile 'com.github.javaparser:javaparser-core:3.3.0'
```

只需要调用下面一句代码即可：

```java
Docs.buildHtmlDocs(DocsConfig config);
```

4. 自定义输出 Java 和 IOS 代码：

你可以把工程里面相关的代码模板文件拷贝出来，然后在配置参数声明好该路径即可，具体的模板文件如下：
![code template files](http://ohb4y25jk.bkt.clouddn.com/darcy_blog_apidocs-code-tpls.png)

5. 更多的用法和不同的框架可以参考我们的示例代码。

# 注意的地方

1. 返回视图类不支持循环引用，会导致 stackoverflow。

```java
class UserVO{
    BookVO book;
}

class BookKVO{
    UserVO user;
}
```

2. JFinal 路由配置必须在 configRoute 方法体里，否则会解析失败。

```java
    @Override
    public void configRoute(Routes me) {
        me.add("/api/v1/user", UserController.class);
        me.add("/api/v1/book", BookController.class);
        me.add(new AmdinRoutes());
    }
```

# 支持和反馈

由于每个人写代码的习惯可能都不一样，虽然已经尽可能考虑到了多种不同的情况，但由于作者本人的认知和精力有限，难免会疏忽或者本身就存在有 bug 的情况，如果你在使用的过程中有碰到困难或者疑问，欢迎提`issue`或者加扣扣群进行反馈：70948803。

如果你觉得这个项目对你有用，请猛戳[:star: star](https://github.com/YeDaxia/JApiDocs)；开源不易，如果还不过瘾，你还可以小赏一下表示支持：

![](http://ohb4y25jk.bkt.clouddn.com/darcy_blog_zhifubao_qr.jpg)

你的支持是我前进的动力！

# License

JApiDocs is available under the Apache 2 License. Please see the [Licensing section](http://docs.hazelcast.org/docs/latest-dev/manual/html-single/index.html#licensing) for more information.


