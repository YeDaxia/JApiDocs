# Introduction

Click [hear](https://yedaxia.github.io/play-apidocs/) to check the example.

JApiDocs is a Java programming tool that complies with the Java programming conventions. JApiDocs will help you to export a beautiful Html document and generate relevant Java and Object-C related data model code, this will reduce Android And IOS Client Developers's work, and you do not need to work hard to maintain changes in the api documents, just need to maintain your code.

# Features

1. A Controller as a set of api export to a html file.
2. Support Java And Object-C Model Code generation.
3. Depth support Spring Boot, Play Framework, JFinal, no need to declare additional routing.
4. Support the general Java Web project, need to add additional routing in the relevant annotation.
5. Support interface statement outdated (@Deprecated), convenient document directory and so on.
6. Support for custom code generation templates.
7. Support Post to Rap[RAP](http://rapapi.org/org/index.do).
8. Support multi module and generic type (List<User>).
9.  :new: Support custom plugins to do what ever you want.

# Quick Start

1. In spring, for example, the image below will help you easilly to understand how JApidocs works.
![](http://ohb4y25jk.bkt.clouddn.com/spring-controllers.png)
As you can see, you can done your work so naturally. the only extra part is the `@ApiDoc`, we will discuss this below.

2. `@ApiDoc` is a custom annatation, as we known, unless we  run the program , otherwise we can not know what the response really contains. To solve this problem, we bring into `View Class`, we can know what will return in the response by the `View Class`, and `JApiDocs` will read every member of it and its parent class except the `static` member. this is why `@ApiDoc` exists.

you can just simplly include our `jar` or create a class naming `ApiDoc` in your project, it doesn't care where package it belongs.

```java
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
3. run to generate docs

(1) add dependencies below:

```
compile 'io.github.yedaxia:japidocs:1.2.4'
```

(2) one line simple java code:

```
Docs.buildHtmlDocs(DocsConfig config);
```

config attributes:

```
projectPath: project directory Windows users should use double slash '\\' or backslash '/'
docsPath: Document output directory (not required, default is ${projectPath}/apidocs)
codeTplPath: Code template directory (not required, if you need custom generated code will be used.)
mvcFramework:[spring, play, jfinal, generic] (not required, the code has internal judgment, if there is a misjudgment, you can use this to force the specified)
javaSrcPaths: (not required) Multi-module projects currently support maven and gradle. If not resolved, the source directory can be added via the `addJavaSrcPath` method.
Plugins: (not required) custom implementation plugin
```


4. custom java or object-c code:

copy the template files in the project, and make the template you like:

![](http://ohb4y25jk.bkt.clouddn.com/darcy_blog_apidocs-code-tpls.png)

# Notes

1. not support cycle class:

```java
class UserVO{
    BookVO book;
}

class BookKVO{
    UserVO user;
}
```

2. Jfinal Users should put the routes in the method `configRoute`:

```java
@Override
public void configRoute(Routes me) {
    me.add("/api/v1/user", UserController.class);
    me.add("/api/v1/book", BookController.class);
    me.add(new AmdinRoutes());
}
```

# License

JApiDocs is available under the Apache 2 License. Please see the [Licensing section](http://docs.hazelcast.org/docs/latest-dev/manual/html-single/index.html#licensing) for more information.

