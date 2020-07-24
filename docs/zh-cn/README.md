# 入门

支持JDK：1.8+

## 快速开始

### 第一步：添加依赖

maven:

```
<dependency>
  <groupId>io.github.yedaxia</groupId>
  <artifactId>japidocs</artifactId>
  <version>1.4.2</version>
</dependency>
```

gradle:

```
compile 'io.github.yedaxia:japidocs:1.4.2'
```

### 第二步：配置参数

你可以在任意一个main入口运行下面的代码：

```java
DocsConfig config = new DocsConfig();
config.setProjectPath("your springboot project path"); // 项目根目录
config.setProjectName("ProjectName"); // 项目名称
config.setApiVersion("V1.0");       // 声明该API的版本
config.setDocsPath("your api docs path"); // 生成API 文档所在目录
config.setAutoGenerate(Boolean.TRUE);  // 配置自动生成
Docs.buildHtmlDocs(config); // 执行生成文档
```
如果没有意外，执行完上面的代码后，你就可以在配置的目录中看到生成的文档了。

## 编码规范

JApiDocs是通过解析Java源码来实现的，要使得JApiDocs正确工作，需要你在项目中的`Controller`书写遵循一定的编码规范。

你可以结合源码中 `SpringDemo` 这个模块来对照理解。

### 1. 添加必要的代码注释

其中类注释会对应到一级接口分组，你也可以通过`@description`来指定分组名称；JApiDocs 会通过 `@param` 来寻找接口参数和进一步解析参数的内容。

```java
/**
 * 用户接口
 */
@RequestMapping("/api/user/")
@RestController
public class UserController {


    /**
     * 用户列表
     * @param listForm
     */
    @RequestMapping(path = "list", method = {RequestMethod.GET,  RequestMethod.POST}  )
    public ApiResult<PageResult<UserVO>> list(UserListForm listForm){
        return null;
    }

    /**
     * 保存用户
     * @param userForm
     */
    @PostMapping(path = "save")
    public ApiResult<UserVO> saveUser(@RequestBody UserForm userForm){
        return null;
    }

    /**
     * 删除用户
     * @param userId 用户ID
     */
    @PostMapping("delete")
    public ApiResult deleteUser(@RequestParam Long userId){
        return null;
    }
}
```

如果提交的表单是 `application/x-www-form-urlencoded` 类型的`key/value`格式，你可以在 SpringBoot 端通过在 `@param` 参数后添加字段解释或者在相关的JavaBean对象里面添加解释：

```java
// 直接在java的 @param 注解中
@param userId 用户ID
```

```java
// 在FormBean对象中
public class UserListForm extends PageForm{
    private Integer status; //用户状态
    private String name; //用户名
}
```
这种格式对于到文档中的参数描述将是表格的形式：

参数名|类型|必须|描述
--|--|--|--
status|int|否|用户状态
name|string|否|用户名

如果提交的表单是 `application/json` 类型的`json`数据格式，对应 SpringBoot 中的 `@RequestBody` 注解，在文档中则是 `json` 格式显示：

```java
{
  "id": "long //用户ID",
  "name": "string //用户名",
  "phone": "long //电话",
  "avatar": "string //头像",
  "gender": "byte //性别"
}
```

### 2. 接口声明返回对象

我们知道，如果`Controller`声明了`@RestController`，SpringBoot会把返回的对象直接序列成Json数据格式返回给前端。
JApiDocs也利用了这一特性来解析接口返回的结果，但由于JApiDocs是静态解析源码的，因此你要明确指出返回对象的类型信息，JApiDocs支持继承、泛型、循环嵌套等复杂的类解析。

比如上面的`saveUser`接口：

```java
 /**
 * 保存用户
 * @param userForm
 */
@PostMapping(path = "save")
public ApiResult<UserVO> saveUser(@RequestBody UserForm userForm){
    return null;
}
```

`ApiResult<UserVO>`表明了该接口返回的数据结构，经过JApiDocs处理后是这样的：

```json
{
  "code": "int",
  "errMsg": "string",
  "data": {
    "userId": "string //用户id",
    "userName": "string //用户名",
    "friends": [
      {
        "userId": "string //用户id",
        "userName": "string //用户名"
      }
    ],
    "readBooks": [
      {
        "bookId": "long //图书id",
        "bookName": "string //图书名称"
      }
    ],
    "isFollow": "boolean //是否关注"
  }
}
```

如果你不是通过返回对象的形式，你也可以通过JApiDocs提供的`@ApiDoc`注解来声明返回类型，你可以参考`@ApiDoc`章节的相关配置内容。

### 3. 接口对象在源码中

我们知道，经过编译后的 class 字节码中是没有注释信息的，如果要通过反射字节码的方式来实现，则不可避免要引入运行时注解，这样会增加使用成本，
考虑到这一点，JApiDocs 从第二个版本之后就改成了使用解析源码的方式，而不是反射字节码的思路来实现了，但这样直接导致的缺陷就是：
所有的 Form Bean (表单)对象和返回对象就必须在项目的源码中，否则就无法解析了，如果你们项目的JavaBean对象是通过jar包的形式提供的，
那很遗憾，JApiDocs将无法支持。

# 高级配置

## @ApiDoc

JApiDocs 默认只导出声明了`@ApiDoc`的接口，我们前面通过设置 `config.setAutoGenerate(Boolean.TRUE)` 来解除了这个限制。

如果你不希望把所有的接口都导出，你可以把`autoGenerate`设置关闭，在相关`Controller`类或者接口方法上通过添加`@ApiDoc`来确定哪些接口需要导出。

当`@ApiDoc`声明在接口方法上的时候，它还拥有一些更灵活的设置，下面我们来看一下：

- result: 这个可以直接声明返回的对象类型，如果你声明了，将会覆盖SpringBoot的返回对象
- url: 请求URL，扩展字段，用于支持非SpringBoot项目
- method: 请求方法，扩展字段，用于支持非SpringBoot项目

例子：

```java
@ApiDoc(result = AdminVO.class, url = "/api/v1/admin/login2", method = "post")
```

## @Ignore

 如果你不想导出对象里面的某个字段，可以给这个字段加上`@Ignore`注解，这样JApiDocs导出文档的时候就会自动忽略掉了：
 
例子:
 
 ```java
public class UserForm{
    @Ignore
    private Byte gender; //性别
}
```

## 导出更多格式

### 导出markdown

```java
config.addPlugin(new MarkdownDocPlugin());
```

### 导出 pdf 或者 word

你可以通过 [pandoc](https://pandoc.org/) 把 markdown 格式转成 pdf 或者 word 格式。

## 自定义代码模板

JApiDocs 除了支持文档导出，目前也支持生成了 Android 和 iOS 的返回对象代码，对应 Java 和 Object-C 语言，
如果你想修改代码模板，可以通过以下的方法：

### 第一步：定义代码模板

把源码中`library`项目`resources`目录下的代码模板拷贝一份，其中，`IOS_`表示 Object-C 代码模板，`JAVA_`开头表示 Java代码，
模板中类似`${CLASS_NAME}`的符号是替换变量，具体含义你可以结合生成的代码进行理解，然后按照你想要的代码模板进行修改即可。

### 第二步：选择新的模板

通过`DocsConfig`配置模板路径替换成新的模板：

```java
docsConfig.setCodeTplPath("模板路径");
```

## 添加更多功能

JApiDocs 提供了插件接口，你可以通过插件接口来实现更多丰富的功能，下面介绍如何添加插件：

### 第一步：实现 IPluginSupport 接口

```java
public class CustomPlugin implements IPluginSupport{
    
    @Override
    public void execute(List<ControllerNode> controllerNodeList){
        // 实现你自己的功能需求
    }
}
```

### 第二步：添加插件

```java
 config.addPlugin(new CustomPlugin());
```