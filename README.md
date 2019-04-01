
> [English ReadMe](README-EN.md)

# 简介

一个简单的[示例](https://yedaxia.github.io/play-apidocs/)。 

JApiDocs 是一个符合 Java 编程习惯的 Api 文档生成工具。最大程度地利用 Java 的语法特性，你只管用心设计好接口，添加必要的注释，JApiDocs 会帮你导出一份漂亮的 Html 文档，并生成相关的 Java 和 Object-C 相关数据模型代码，从此，Android 和 IOS 的同学可以少敲很多代码了，你也不需要费力维护接口文档的变化，只需要维护好你的代码就可以了。

反馈交流Q群：70948803

# 特性

1. 以一个 Controller 作为一组接口导出到一个 Html 文件中。
2. 支持生成 Java 和 Object-C 语言的 Response 模型代码。
3. 深度支持 [Spring Boot](http://projects.spring.io/spring-boot/)， [PlayFramework](https://www.playframework.com/)，[JFinal](http://www.jfinal.com/)，不需要额外声明路由。
4. 支持一般的 Java Web 工程，需要在相关方法添加额外的路由。
5. 支持接口声明过时(`@Deprecated`)，方便的文档目录等。
6. 支持自定义代码生成模板。
7. 支持集成发布到 [RAP](http://rapapi.org/org/index.do)。
8. 支持多模块、泛型。
9. :new: 支持实现自己的插件。

# Maven 和 Gradle

```
<dependency>
  <groupId>io.github.yedaxia</groupId>
  <artifactId>japidocs</artifactId>
  <version>1.2.4</version>
</dependency>
```

```
compile 'io.github.yedaxia:japidocs:1.2.4'
```

# 快速使用

## 原理简介：

我们以 spring 为例，一张图很容易就明白了 JApidocs 是怎么工作的了，你在设计接口的时候可以顺便就把相关的注释给填好了，这和 Java 程序员的编程习惯是保持一致的。

![spring controller](http://ohb4y25jk.bkt.clouddn.com/spring-controllers.png)

## @ApiDoc 注解：

**只有声明了 @ApiDoc 的接口才会导出文档。**

`@ApiDoc` 是我们定义的一个 `RetentionPolicy.SOURCE` 注解，它不会给现有的代码造成任何的负担。另外 JApiDocs 是基于 Java 源码进行解析的，你可以不需要依赖我们的 Jar 包，在你自己的工程里面新建一个同名的类即可；但我推荐你还是引入 Jar 包，这样省事一些。

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

详细的使用请参考 **`SpringDemo`** 这个例子。

## 快速集成



1）添加依赖，或者下载 jar 包到你的 libs 目录中。

```
compile 'io.github.yedaxia:japidocs:1.2.4'
```

2）设置好相关参数。

```java
main:
    DocsConfig config = new DocsConfig();
    config.setProjectPath(projectPath);
    Docs.buildHtmlDocs(DocsConfig config);
```

参数说明

```
projectPath： 工程目录 windows用户注意用双斜杠'\\'或者反斜杠'/'
docsPath： 文档输出目录（非必须，默认是${projectPath}/apidocs）
codeTplPath：代码模版目录 (非必须，如果你需要自定义生成的代码才会用到。)
mvcFramework：[spring, play, jfinal, generic](非必须，代码内部有判断，如果出现误判的情况，可以通过这个强制指定)
javaSrcPaths： (非必须)多模块项目目前是支持 maven 和 gradle 的，如果没有解析出来的话，可以通过 `addJavaSrcPath` 方法来添加源码目录。
plugins：（非必须）自定义实现插件
```


## 自定义输出 Java 和 IOS 代码：

你可以把工程里面相关的代码模板文件拷贝出来，然后在配置参数声明好该路径即可，具体的模板文件如下：
![code template files](http://ohb4y25jk.bkt.clouddn.com/darcy_blog_apidocs-code-tpls.png)


## 如何实现实现自定义插件

第一步：实现 `IPluginSupport` 接口

```java

public class CustomPlugin implements IPluginSupport{
    
    @Override
    public void execute(List<ControllerNode> controllerNodeList){
        // 实现你自己的功能需求
    }
}

```

第二步：添加插件:

```
 config.addPlugin(IPluginSupport plugin);
```

## 如何集成到 RAP 进行接口测试：

具体的集成请查看 [Wiki](https://github.com/YeDaxia/JApiDocs/wiki/%E9%9B%86%E6%88%90-Rap-%E6%8E%A5%E5%8F%A3%E6%B5%8B%E8%AF%95)

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

tips：可以通过 `@Ignore` 注解忽略字段来解决这个问题。

2. JFinal 路由配置必须在 configRoute 方法体里，否则会解析失败。

```java
    @Override
    public void configRoute(Routes me) {
        me.add("/api/v1/user", UserController.class);
        me.add("/api/v1/book", BookController.class);
        me.add(new AmdinRoutes());
    }
```

3. JApiDocs 是通过静态解析源码来工作的，所有的 JavaBean 类源码必须在项目中，**不支持在 jar 包里面的对象类**。

4. 更多问题请参考：[常见问题记录](https://github.com/YeDaxia/JApiDocs/wiki/%E5%B8%B8%E8%A7%81%E9%97%AE%E9%A2%98%E8%AE%B0%E5%BD%95)。

# 支持和反馈

如果你在使用的过程中有碰到困难或者疑问，欢迎提 issue 和 PR。

如果你觉得这个项目有用，可以推荐给你的朋友。你的支持是我前进的动力！

我的个人博客：[叶大侠的主页](http://yedaxia.me/)

# License

JApiDocs is available under the Apache 2 License. Please see the [Licensing section](http://docs.hazelcast.org/docs/latest-dev/manual/html-single/index.html#licensing) for more information.


