# Getting Started

Supported JDK：1.8+

## Quick Start

### Step One：Add Dependency

maven:

```
<dependency>
  <groupId>io.github.yedaxia</groupId>
  <artifactId>japidocs</artifactId>
  <version>1.4.4</version>
</dependency>
```

gradle:

```
compile 'io.github.yedaxia:japidocs:1.4.4'
```

### Step Two：Configuration

You can run code below at any main():

```java
DocsConfig config = new DocsConfig();
config.setProjectPath("your springboot project path"); // root project path 
config.setProjectName("ProjectName"); // project name
config.setApiVersion("V1.0");       // api version
config.setDocsPath("your api docs path"); // api docs target path
config.setAutoGenerate(Boolean.TRUE);  // auto generate 
Docs.buildHtmlDocs(config); // execute to generate 
```
If there is no accident, after executing the above code, you can see the generated documents in the configured directory.

## Code Style Requirements

JApiDocs is implemented by parsing Java source code. To make JApiDocs work correctly, you need to follow certain coding standards in the writing of `Controller` in the project.

You can refer the `SpringDemo` module in the source code for comparison and understanding.

### 1. Add Necessary Code Comments

The class comment will correspond to the first-level grouping. You can also specify the group name through `@description`; JApiDocs will use `@param` to find parameters for further analyze.

```java
/**
 * User API
 */
@RequestMapping("/api/user/")
@RestController
public class UserController {


    /**
     * Get User List
     * @param listForm
     */
    @RequestMapping(path = "list", method = {RequestMethod.GET,  RequestMethod.POST}  )
    public ApiResult<PageResult<UserVO>> list(UserListForm listForm){
        return null;
    }

    /**
     * Save User
     * @param userForm
     */
    @PostMapping(path = "save")
    public ApiResult<UserVO> saveUser(@RequestBody UserForm userForm){
        return null;
    }

    /**
     * Delete User
     * @param userId user id
     */
    @PostMapping("delete")
    public ApiResult deleteUser(@RequestParam Long userId){
        return null;
    }
}
```

If the submitted form is `application/x-www-form-urlencoded` type, you can add a description after `@param` or add an comment in JavaBean object :

```java
// in @param
@param userId user id
```

```java
// at FormBean
public class UserListForm extends PageForm{
    private Integer status; //user status
    private String name; //user name
}
```

This form type would show as table in the document：

parameter name|parameter type|must|description
--|--|--|--
status|int|N|user status
name|string|N|user name

If the submitted form is `application/json` type, corresponding to the `@RequestBody` annotation in SpringBoot, it will display as `json` format in the document:

```java
{
  "id": "long //user id",
  "name": "string //user name",
  "phone": "long // user phone",
  "avatar": "string // user avatar url",
  "gender": "byte //use gender"
}
```

### 2. Return Specific Class Type

We know that if a Controller Class declares @RestController, SpringBoot will return json data to the front end. JApiDocs also uses this feature to parse the result in the api method, but since JApiDocs parses the source code statically, you must clearly return a specific class type. JApiDocs supports complex class analysis such as inheritance, generics, and loop nesting.

Such as `saveUser` ：

```java
 /**
 * save user
 * @param userForm
 */
@PostMapping(path = "save")
public ApiResult<UserVO> saveUser(@RequestBody UserForm userForm){
    return null;
}
```

`ApiResult<UserVO>` shows the data structure of response，after processing by JApiDocs，it's like this：

```json
{
  "code": "int",
  "errMsg": "string",
  "data": {
    "userId": "string //user id",
    "userName": "string //user name",
    "friends": [
      {
        "userId": "string //user id",
        "userName": "string //user name"
      }
    ],
    "readBooks": [
      {
        "bookId": "long //book id",
        "bookName": "string //book name"
      }
    ],
    "isFollow": "boolean //is follow"
  }
}
```

If you don't like the *return* type, you can also use `@ApiDoc` in JApiDoc to declare the response type，you can refer the `@ApiDoc` chapter below.

### 3. Api Java Beans Should In Source Code 

We know that there is no comment information in the compiled class bytecode. For JApiDcos to work better, 
your Form bean Class and return Class should be in the source code, otherwise the generated document will be missing description information.
Anyway, in version 1.4.2, JApiDocs will try to parse the field information by reflection when can't find the source code (the dependent class is in the jar package).

# Advanced Configuration

## @ApiDoc

By default, JApiDocs only exports the api that declares `@ApiDoc`. We previously removed this restriction by setting `config.setAutoGenerate(Boolean.TRUE)`.

If you don't want to export all apis, you can turn off the `autoGenerate` and add `@ApiDoc` to the `Controller` class or api method to determine which api need to be exported.

Let's see how the `@ApiDoc` works on api method:

- result: the returned object type, it will override the returned object of SpringBoot
- url: request url，extended field, used to support non-SpringBoot projects
- method: request method，extended field, used to support non-SpringBoot projects

ex：

```java
@ApiDoc(result = AdminVO.class, url = "/api/v1/admin/login2", method = "post")
```

## @Ignore

### Ignore Controller Class

Add `@Ignore` at Controller, all of its method would ignore.

```java

@Ignore
public class UserController { 
 
}
```

### Ignore Method

```java

@Ignore
@PostMapping("save")
public ApiResult saveUser(){
  return null;
}

```

### Ignore Field

If you don’t want to export a field in the object, you can add `@Ignore` annotation to this field, so that JApiDocs will automatically ignore it when exporting the document:

ex:
 
 ```java
public class UserForm{
    @Ignore
    private Byte gender;
}
```

## Export More Format

### Export Markdown

```java
config.addPlugin(new MarkdownDocPlugin());
```

### Export Pdf Or Word

You can use [pandoc](https://pandoc.org/) convert markdown to pdf or word.

## Custom Code Templates

In addition to supporting api document export, JApiDocs currently also supports the generation of return object codes for Android and iOS, corresponding to Java and Object-C languages,
If you want to modify the code template, you can use the following steps:

### Step One：Modify Code Templates

Copy the code templates in the `library` project `resources` directory of the source code, where `IOS_` means Object-C code template, and `JAVA_` starts with Java code,
The symbol similar to "${CLASS_NAME}" in the template is a substitution variable. You will understand the meaning compare to the generated code, and try to modify it according to the code template you want.

### Step Two：Use The New Code Templates

Use `DocsConfig` to replace with new template:

```java
docsConfig.setResourcePath("your new tempalte path");
```

## More Custom Features

JApiDocs provides a plug-in interface. You can implement more rich features through the plug-in interface. 
The following describes how to make this:

### Step One：Implements *IPluginSupport* Interface

```java
public class CustomPlugin implements IPluginSupport{
    
    @Override
    public void execute(List<ControllerNode> controllerNodeList){
        // do something you want
    }
}
```

### Step Two：Add Your Plugin

```java
 config.addPlugin(new CustomPlugin());
```