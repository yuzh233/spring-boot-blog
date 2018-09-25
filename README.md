# 基于 Spring Boot 技术栈构建企业级博客系统的开发记录

<!-- TOC -->

- [基于 Spring Boot 技术栈构建企业级博客系统的开发记录](#基于-spring-boot-技术栈构建企业级博客系统的开发记录)
    - [一、 使用 Gradle / Spring Initializer 搭建 Spring Boot 运行环境](#一-使用-gradle--spring-initializer-搭建-spring-boot-运行环境)
        - [1.1 安装 Gradle 环境](#11-安装-gradle-环境)
        - [1.2 使用 Spring Initializer 快速生成 Spring Boot 应用](#12-使用-spring-initializer-快速生成-spring-boot-应用)
        - [1.3 项目结构](#13-项目结构)
        - [1.4 自定义存储仓库](#14-自定义存储仓库)
        - [1.5 编写程序代码及测试用例](#15-编写程序代码及测试用例)
        - [1.6 以 Gradle / Wrapper 编译项目](#16-以-gradle--wrapper-编译项目)
        - [1.7 Gradle 项目运行的三种方式](#17-gradle-项目运行的三种方式)
    - [二、 Thymeleaf 模板引擎](#二-thymeleaf-模板引擎)
        - [2.1 Thymeleaf](#21-thymeleaf)
        - [2.2 标准方言（语法）](#22-标准方言语法)
        - [2.4 集成 Spring Boot](#24-集成-spring-boot)
        - [2.5 Thymeleaf 实战](#25-thymeleaf-实战)
    - [三、 Spring Data JPA 数据持久化](#三-spring-data-jpa-数据持久化)
        - [3.1 JPA 简介](#31-jpa-简介)
        - [3.2 JPA 核心概念](#32-jpa-核心概念)
        - [3.3 Spring Data JPA 使用概括](#33-spring-data-jpa-使用概括)
        - [3.4. Spring Data JPA / Hibernate / Spring Boot 集成](#34-spring-data-jpa--hibernate--spring-boot-集成)
        - [3.5 数据持久化实战](#35-数据持久化实战)
            - [3.5.1 H2 内存数据库](#351-h2-内存数据库)
            - [3.5.2 MySql 物理数据库](#352-mysql-物理数据库)

<!-- /TOC -->

## 一、 使用 Gradle / Spring Initializer 搭建 Spring Boot 运行环境

###  1.1 安装 Gradle 环境

下载 gradle [二进制文件](https://gradle.org/releases/)，解压到指定目录；

配置 `GRADLE_HOME` 环境变量，值为解压路径；

添加 `%GRADLE_HOME%/bin` 到 Path 变量。

命令行 `gradle -v` 查看版本：

![](blog-img/image_5.png)

### 1.2 使用 Spring Initializer 快速生成 Spring Boot 应用

使用 Spring 快速开始[向导](https://start.spring.io/)，创建一个项目并下载到本地。  

![](blog-img\image_6.png)

使用 gradle 编译项目，进入根目录执行：

```shell
gradle build
```

编译完成之后会在目录生成一个 build 文件夹，里面存放着编译后的文件以及安装的jar。

![](blog-img/image_8.png)

运行这个 jar ：

```shell
java -jar spring-boot-blog-0.0.1-SNAPSHOT.jar
```

![](blog-img/image_10.png)

一个简单的 spring  boot 应用启动起来了，接下来可以正常访问。

### 1.3 项目结构

![](blog-img/image_11.png)

- `.gradle`  gradle 运行时相关配置文件，不用多说了。
- `文件夹 build` 项目编译后路径
- `文件夹 gradle` 
  - `文件夹 wrapper` 统一管理 gradle 版本，优点是即使没有装 gradle 环境可以运行里面的 jar 直接构建项目。
- `build.gradle` gradle的用户配置文件（构建脚本），相当于 maven 的 pom.xml.
- `gradlew` gradle 环境搭建的脚本（linux）
- `gradlew.bat` gradle 环境搭建的脚本（windows）
- `settings.gradle` 其他用户配置

### 1.4 自定义存储仓库

更改 build.gradle 配置文件：

```xml
// buildscript 代码块中脚本优先执行
buildscript {

    // ext 用于定义动态属性
    ext {
        springBootVersion = '1.5.17.BUILD-SNAPSHOT'
    }

    // 使用了 Maven 的中央仓库（也可以指定其他仓库）
    repositories {
        mavenCentral()
        maven { url "https://repo.spring.io/snapshot" }
        maven { url "https://repo.spring.io/milestone" }
        // 使用 aliyun 镜像仓库
//        maven { url 'http://maven.aliyun.com/nexus/content/groups/public/' }
    }

    // 依赖关系
    dependencies {
        // classpath 声明说明了在执行其余的脚本时，ClassLoader 可以使用这些依赖项
        classpath("org.springframework.boot:spring-boot-gradle-plugin:${springBootVersion}")
    }
}

// 使用插件
apply plugin: 'java'
apply plugin: 'eclipse'
apply plugin: 'org.springframework.boot'

group = 'xyz.yuzh.spring.boot.blog'

// 打包的类型为 jar，并指定了生成的打包的文件名称和版本
jar {
    baseName = 'hello-world'
    version = '0.0.1-SNAPSHOT'
}

version = '0.0.1-SNAPSHOT'
// 指定编译 .java 文件的 JDK 版本
sourceCompatibility = 1.8

// 默认使用了 Maven 的中央仓库。
repositories {
    mavenCentral()
    maven { url "https://repo.spring.io/snapshot" }
    maven { url "https://repo.spring.io/milestone" }
//    maven { url 'http://maven.aliyun.com/nexus/content/groups/public/' }
}

// 依赖关系
dependencies {
    // 该依赖对于编译发行是必须的
    compile('org.springframework.boot:spring-boot-starter-web')
    // 该依赖对于编译测试是必须的，默认包含编译产品依赖和编译时依
    testCompile('org.springframework.boot:spring-boot-starter-test')
}

```

### 1.5 编写程序代码及测试用例

控制层：

```java
@RestController
public class HelloController {
    @RequestMapping(value = "/hello")
    public String hello(){
        return "hello gradle!";
    }
}
```

用例代码：使用了 Spring Mvc 单元测试类 MockMVC，[详解参考](https://blog.csdn.net/kqzhu/article/details/78836275)

```java
@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
public class SpringBootBlogApplicationTests {
    @Autowired
    private MockMvc mockMvc;

    @Test
    public void testHello() throws Exception {
        ResultActions actions = mockMvc.perform(MockMvcRequestBuilders.get("/hello").
                accept(MediaType.APPLICATION_JSON)); // 指定客户端能够接收的内容类型

        actions.andExpect(status().isOk()); // 添加断言, 添加ResultMatcher验证规则，验证控制器执行完成后结果是否正确.
        actions.andExpect(content().string(equalTo("hello gradle!"))); // 添加断言,返回结果内容是否是指定的.
        actions.andDo(MockMvcResultHandlers.print()); // 添加一个结果处理器，输出整个响应结果信息.
        actions.andReturn(); // 执行完毕返回相应的结果
    }
}
```

### 1.6 以 Gradle / Wrapper 编译项目

当本地没有装 gradle 环境时，可以通过 wrapper 构建项目，只需在根目录执行即可打包：

```shell
gradlew build
```

![](blog-img/image_12.png)

运行：

![](blog-img/image_13.png)

### 1.7 Gradle 项目运行的三种方式

1). 使用 java  -jar 

2). 通过 SpringApplication.run()

3). 使用 Spring Boot Gradle 插件

```shell
gradle bootRun / gradlew bootRun (wrapper 方式)
```

![](blog-img/image_14.png)

## 二、 Thymeleaf 模板引擎

### 2.1 Thymeleaf

[官方文档](https://www.thymeleaf.org/doc/tutorials/3.0/usingthymeleaf.html#introducing-thymeleaf)

Java 模板引擎。能够处理 Html / XML / JavaScript / CSS / 甚至纯文本 。类似于 JSP / FreeMarker

自然模板。原型即页面

语法优雅易懂。支持 OGNL / SpringEL

遵从 Web 标准。支持 HTML5

### 2.2 标准方言（语法）

**名称空间**

    <span th:text="…">
    需要引入命名空间:
    <html xmlns:th="http://www.thymeleaf.org">


或

    <span data-th-text="…">
    符合html5标准，不需要引入命名空间

**变量表达式**

${…}

	<span th:text="${book.author.name}">

**消息表达式（i8n / 国际化）**

#{…}

    <th th:text="#{header.address.city}">…</th>
    <th th:text="#{header.address.country}">…</th>

**选择表达式**

*{…}

    <div th:object="${book}">
        ...
        <span th:text="*{title}">...</span>
        ...
    </div>

与变量表达式的区别：它是在当前选择的对象而不是整个上下文变量映射上执行。`${book}`  取的是整个上下文中的变量，而 `*{title}` 是在当前 `${book}`  里边的变量。因此变量表达式一定程度上提高了效率。

**链接表达式**

@{…}

链接表达式可以是相对的，在这种情况下，应用程序上下文将不会作为 URL 的前缀

	<a th:href="@{../documents/report}">…</a>

也可以是服务器相对的，同样也没有应用程序上下文前缀

	<a th:href="@{~/contents/main}">…</a>

协议相对的（类似绝对 URL，但浏览器将使用在显示的页面中使用的相同的 HTTP 或 HTTPS 协议）

	<a th:href="@{//static.mycompany.com/res/initial}">…</a>

也可以是绝对的

	<a th:href="@{http://static.mycompany.com/main}">…</a>

**模板布局**

`th:insert` 将公共片段整个插入到声明引入的元素中

`th:replace` 将声明引入的元素替换为公共片段

`th:include` 将被引入的片段的内容包含进这个标签


    <footer th:fragment="copy">
    	&copy; 2011 The Good Thymes Virtual Grocery
    </footer>
    
    引入方式
    <div th:insert="footer :: copy"></div>
    <div th:replace="footer :: copy"></div>
    <div th:include="footer :: copy"></div>
    
    效果
    <div>
        <footer>
        	&copy; 2011 The Good Thymes Virtual Grocery
        </footer>
    </div>
    
    <footer>
    	&copy; 2011 The Good Thymes Virtual Grocery
    </footer>
    
    <div>
    	&copy; 2011 The Good Thymes Virtual Grocery
    </div>
也可以使用指定的 id 来替代 `th:fragment="copy"` 引用时指定 `#id`

**字面量**

文本 `<span th:text="'web application'">` 单引号包裹

数字 `<span th:text="2015"> or <span th:text="2980 + 3">`

布尔：`<span th:if="${user.isAdmin()} == false">` or  `<span th:if="${user.name} == null`

算术操作 (+ 、-、*、/、%)：`<span th:with="isEven=(${prodStat.count} % 2 == 0)">`

比较和等价：`> 、< 、>= 、 <= （gt、lt、ge、le）`

等价： `== 、 != (eq、ne)`

**条件运算**

    <span th:class="${row.even} ? ‘even' : ‘odd' "></span>

**无操作**

__

    <span th:text="${user.name} ? : __">no user authenticated</span>

用户名不存在取无操作运算符，保留原始文本值。

**设置属性值**

设置任意属性值 `th:attr`

	th:attr="action=@{/subsctibe}" 设置action属性值

设置指定属性值，比如:  `th:action / th:value  / th:text`

固定布尔属性， `th:checked="${user.active}"` 如果结果为 true 设置为选中状态

**迭代器**

基本迭代 th:each

    <li th:each=“book : ${books}” th:text=“${book.title}”>En las Orillas del Sar</li>

状态变量：`index（0开始）、count（1开始）、size、current、even/odd、first、last`

**条件语句**

th:if / th:unless （成立时 / 不成立时）

th:switch

    <div th:switch="${user.role}">
    	<p th:case="'admin'">user is an administrator</p>
    	<p th:case="${roles.manager}">user is an manager</p>
    	<p th:case="*">user is some other thing</p>
    </div>

**注释**

1). 标准的 html 注释 `<!-- -->`

2). thymeleaf 解析器注释块 `<!--/*  */-->`

    删除 <!--/* 和 */--> 之间的所有内容
    
    <!--/*--> 
      <div>you can see me only before thymeleaf processes me!</div>
    <!--*/-->

3). 原型注释块：当模板静态打开时（比如原型设计），原型注释块所注释的代码将被注释，而在模板执行时，这些注释的代码，会被显示出来。

原型设计代码：

    <span>hello</span>
    <!--/*/ 
        <div th:th:text="${...}">
            ...
        </div>
    /*/-->
    <span>goodbye!</span>

模板渲染执行后：

    <span>hello</span>
    	<div th:th:text="${...}">
    		...
    	</div>
    <span>goodbye!</span>
**内联表达式**

[[…]] 或 [(…)] 分别对应于 th:text（会转译特殊字符） 和 th:utext（不会转译特殊字符）

禁用内联：`th:inline="none"`

Javascript 内联：`th:inline="JavaScript"`

CSS 内联：`th:inline="css"`

### 2.3 表达式基本对象

 #ctx：上下文对象。是 `org.thymeleaf.context.IContext` 或者 `org.thymeleaf.context.IWebContext` 的实现。

#locale： 直接访问与 `java.util.Locale` 关联的当前的请求。

    ${#ctx.locale}
    ${#ctx.bariableNames}
    
    ${#ctx.request}
    ${#ctx.response}
    ${#ctx.session}
    ${#ctx.servletContext}
    
    ${#locale}
**Request/session 等属性**

param：用于检索请求参数

session：用于检索session属性

application：用于检索application/servlet上下文属性

	${#param.foo}
	${#param.size()}
	${#param.isEmpty()}
	${#param.containsKey('foo')}
	
	${#session.foo}
	${#session.size()}
	${#session.isEmpty()}
	${#session.containsKey('foo')}
	
	${#application.foo}
	${#application.size()}
	${#application.isEmpty()}
	${#application.containsKey('foo')}

**Web上下文对象**

#request：直接访问与当前请求关联的 HttpServletRequest 对象

#session：直接访问与当前请求关联的 HttpSession 对象

#servletContext：直接访问与当前请求关联的 servletContext 对象

	${#request.getAttribute('foo')}
	${#request.getParameter('foo')}
	${#request.getContextPath()}
	${#request.getRequestName())}
	
	${#session.getAttribute('foo')}
	${#session.id}
	${#session.lastAccessedTime}
	
	${#servletContext.getAttribute('foo')}
	${#servletContext.contextPath}
### 2.4 集成 Spring Boot

修改 buid.gradle。添加对 Thymeleaf 的依赖，自定义 Thymeleaf 和 Thyme leaf Layout Dialect 的版本。

```xml
buildscript {
    // ext 用于定义动态属性（统一管理版本）
    ext {
        springBootVersion = '1.5.17.BUILD-SNAPSHOT'
    }

    // 指定 Thymeleaf 和 Thymeleaf Layout Dialect 的版本
    ext['thymeleaf.version'] = '3.0.3.RELEASE'
    ext['thymeleaf-layout-dialect.version'] = '2.2.0'
    ......
}
......
// 依赖关系
dependencies {
   ......
    // 添加 Thymeleaf 依赖
    testCompile('org.springframework.boot:spring-boot-starter-thymeleaf')
}
```

修改 Spring Boot 的 application.properties（或 application.yml ）

```yml
spring:
  thymeleaf:
    encoding: UTF-8
    cache: false
    # 使用 HTML5 标准
    mode: HTML5
```

### 2.5 Thymeleaf 实战

**接口设计**

| 接口                   | 描述                                                      |
| ---------------------- | --------------------------------------------------------- |
| GET /users             | 返回用于展示用户列表的 list.html                          |
| GET /users/{id}        | 返回用于展示用户的 view.html                              |
| GET /users/form        | 返回用于新增或者修改用户的 form.html                      |
| POST /users            | 新增或修改用户，成功后重定向到 list.html                  |
| GET /users/delete/{id} | 根据 id 删除相应的用户数据，成功后重定向到 list.html      |
| GET /users/modify/{id} | 根据 id 获取相应的用户数据，并返回 form.html 用来执行修改 |

**后台编码**

```java
@Repository
public class UserRepositoryImpl implements UserRepository {
    /**
     * 累加器
     */
    private static AtomicLong counter = new AtomicLong();

    /**
     * 暂存数据
     */
    ConcurrentMap<Long, User> userMap = new ConcurrentHashMap<>();

    @Override
    public User saveOrUpdateUser(User user) {
        Long id = user.getId();
        if (id <= 0) {
            id = counter.incrementAndGet();
            user.setId(id);
        }
        userMap.put(id, user);
        return null;
    }

    @Override
    public void deleteUser(Long id) {
        userMap.remove(id);
    }

    @Override
    public User getUserById(Long id) {
        return userMap.get(id);
    }

    @Override
    public List<User> listUser() {
        return new ArrayList<>(userMap.values());
    }
}
```

```java
@RestController
@RequestMapping(value = "/users")
public class UserController {

    @Autowired
    private UserRepository userRepository;

    public List<User> getUserList() {
        return userRepository.listUser();
    }

    /**
     * 查询所有用户 [ GET /users ]
     *
     */
    @GetMapping
    public ModelAndView list(ModelMap map) {
        map.addAttribute("users", getUserList());
        map.addAttribute("title", "用户管理");
        return new ModelAndView("users/list", "userModel", map);
    }

    /**
     * 根据 id 查询用户 [ GET /users/{id} ]
     */
    @GetMapping(value = "/{id}")
    public ModelAndView getUserById(@PathVariable("id") Long id, ModelMap map) {
        User user = userRepository.getUserById(id);
        System.out.println(user);
        map.addAttribute("user", user);
        map.addAttribute("title", "查看用户");
        return new ModelAndView("users/view", "userModel", map);
    }

    /**
     * 跳转到新建用户 [ GET /users/form ]
     */
    @GetMapping(value = "/form")
    public ModelAndView createForm(ModelMap map) {
        map.addAttribute("user", new User());
        map.addAttribute("title", "创建用户");
        return new ModelAndView("users/form", "userModel", map);
    }

    /**
     * 新建及修改 [ POST /users ]
     */
    @PostMapping
    public ModelAndView create(User user) {
        userRepository.saveOrUpdateUser(user);
        return new ModelAndView("redirect:/users");
    }

    /**
     * 删除用户 [ GET /users/delete/{id} ]
     */
    @GetMapping(value = "delete/{id}")
    public ModelAndView delete(@PathVariable("id") Long id, ModelMap map) {
        userRepository.deleteUser(id);
        map.addAttribute("users", getUserList());
        return new ModelAndView("redirect:/users");
    }

    /**
     * 跳转修改页面 [ GET /users/modify/{id} ]
     */
    @GetMapping(value = "modify/{id}")
    public ModelAndView update(@PathVariable("id") Long id, ModelMap map) {
        User user = userRepository.getUserById(id);
        map.addAttribute("user", user);
        map.addAttribute("title", "修改用户");
        return new ModelAndView("users/form", "userModel", map);
    }

}
```

**前台设计**

fragment/header.html：共用的头部页面

```html
<!DOCTYPE html>
<html lang="en" xmlns:th="http://www.thymeleaf.org" xmlns:layout="http://www.ultraq.net.nz/thymeleaf/layout">
<head>
    <meta charset="UTF-8">
    <title>Thymeleaf in action</title>
</head>
<body>
<div th:fragment="header">
    <h1>Thymeleaf in action</h1>
    <a th:href="@{~/users}">首页</a>
    <a th:href="@{~/users/form}">新增</a>
</div>
</body>
</html>
```

fragment/footer.html：共用的底部页面

```html
<div th:fragment="footer">
    <a th:href="@{http://blog.yuzh.xyz}">welcome to blog.yuzh.xyz</a>
</div>
```

users/form.html 新增/修改

```html
<!DOCTYPE html>
<html lang="en" xmlns:th="http://www.thymeleaf.org" xmlns:layout="http://www.ultraq.net.nz/thymeleaf/layout">
<head>
    <meta charset="UTF-8">
    <title>Thymeleaf in action</title>
</head>
<body>
<div th:replace="~{fragments/header::header}"></div>
<h3 th:text="${userModel.title}"></h3>
<form th:action="@{~/users}" method="post">
    <input type="text" name="name" th:value="${userModel!=null}?${userModel.user.name}" placeholder="name">
    <input type="email" name="email" th:value="${userModel!=null}?${userModel.user.email}" placeholder="email">
    <input type="text" name="age" th:value="${userModel!=null}?${userModel.user.age}" placeholder="age">
    <input type="submit" th:value="${userModel.title=='创建用户'} ? 'register' : 'modify'">
    <input type="hidden" name="id" th:if="${userModel.title=='修改用户'}" th:value="${userModel.user.id}">
</form>
<div th:replace="~{fragments/footer::footer}"></div>
</body>
</html>
```

users/list.html 列表展示

```html
<!DOCTYPE html>
<html lang="en" xmlns:th="http://www.thymeleaf.org" xmlns:layout="http://www.ultraq.net.nz/thymeleaf/layout">
<head>
    <meta charset="UTF-8">
    <title>Thymeleaf in action</title>
</head>
<body>
<div th:replace="~{fragments/header :: header}"></div>
<h3 th:text="${userModel!=null}?${userModel.title}">yuzh</h3>
<table border="1">
    <thead>
    <tr>
        <th>ID</th>
        <th>Email</th>
        <th>Name</th>
        <th>Age</th>
        <th>操作</th>
    </tr>
    </thead>
    <tbody>
    <tr th:if="${userModel.users.size()}==0">
        <td colspan="5">没有用户信息！</td>
    </tr>
    <tr th:each="user:${userModel.users}">
        <td th:text="${user.id}"></td>
        <td th:text="${user.email}"></td>
        <td th:text="${user.name}"></td>
        <td th:text="${user.age}"></td>
        <td>
            <a href="" th:href="@{~/users/}+${user.id}">修改</a>
            <a href="" th:href="@{~/users/delete/}+${user.id}">删除</a>
        </td>
    </tr>
    </tbody>
</table>
<div th:replace="~{fragments/footer::footer}"></div>
</body>
</html>
```

users/view 查看用户

```html
<!DOCTYPE html>
<html lang="en" xmlns:th="http://www.thymeleaf.org" xmlns:layout="http://www.ultraq.net.nz/thymeleaf/layout">
<head>
    <meta charset="UTF-8">
    <title>Thymeleaf in action</title>
</head>
<body>
<div th:replace="~{fragments/header :: header}"></div>
<h3>[[${userModel.title}]]</h3>
<a th:href="@{~/users/modify/}+${userModel.user.id}">修改信息</a>
<p>ID：[[${userModel.user.id}]]</p>
<p>Name：[[${userModel.user.name}]]</p>
<p>Email：[[${userModel.user.email}]]</p>
<p>Age：[[${userModel.user.age}]]</p>
<div th:replace="~{fragments/footer :: footer}"></div>
</body>
</html>
```

## 三、 Spring Data JPA 数据持久化

### 3.1 JPA 简介

JPA (Java Persistence API) 是用于管理 JavaEE 和 JavaSE环境中的持久化，以及对象/关系映射的 Java API。

JPA 是用于处理数据持久化的接口（规范），基于 JPA 规范的实现有 EclipiseLink、Hibernate、Apache OpenJPA

### 3.2 JPA 核心概念

理解 jpa 的核心概念，才能更好的使用 jpa 持久化。没有符合任意一项都不能成功完成持久化！

**实体**

- 实体表示关系型数据库中表
- 每个实体实例对应于表中的行
- 类必须使用 javax.persistence.Entity 注解
- 类必须有一个 public 或 protected 的无参数构造器
- 实体实例被当作以分离对象方式进行传递（例如通过会话 bean 的远程业务接口），则该类必须实现 Serializable 接口
- 必须要有唯一的对象标识符：简单主键（javax.persistence.Id）、复合主键（javax.persistence.Embeddedld 和 javax.persistence.IdClass）

**关系**

- 一对一：@OneToOne
- 一对多：@OneToMany
- 多对一：@ManyToOne
- 多对多：@ManyToMany

**EntityManager (管理实体的接口**

*管理实体的接口*

- 定义用于与持久化上下文进行交互的方法
- 创建和删除持久实体实例，通过实体的主键查找实体
- 允许在实体上运行查询

*获取 EntityManager 实例*

```java
@PersistenceUnit 
EntityManagerFactory emf; // 用于创建 EntityManager 的工厂类
EntityManager em;
@Resource
UserTransaction utx; // 事务
...
em = emf.createEmtityManager(); // 创建 EntityManager 
try{
    utx.begin(); // 事务开始
    em.persist(SomeEntity); // 通过实体管理器持久化一个实体对象
    em.merge(AnotherEntity); // 通过实体管理器合并一个实体对象
    em.remove(ThirdEntity); // 通过实体管理器移除一个实体对象
    utx.commit(); // 事务提交
} catch (Exception e){
    utx.rollback(); // 回滚
}
```

*查找实体*

```java
@PersistenceContext
EntityManager em;
public void enterOrder(int custID, CustomerOrder newOrder){
    Customer cust = em.find(Customer.class, custID); // 通过实体管理器查找指定类型的实体
    cust.getOrders().add(newOrder);
    newOrder.setCustomer(cust);
}
```

### 3.3 Spring Data JPA 使用概括

**什么是 Spring Data JPA：**

- Spring Data 家族的一部分
- 对基于 JPA 的数据访问层的增强支持
- 更容易构建基于使用 Spring 的数据访问技术栈的应用程序

**常用接口：**

- CrudRepository 定义了一些增删改查的通用接口
- PagingAndSortingRepository 用于分页和排序的接口，扩展于 CrudRepository

- 自定义接口：继承 Repository 及子类

### 3.4. Spring Data JPA / Hibernate / Spring Boot 集成

修改 build.gradle 

```gradle
buildscript {
	......
    // 自定义  Hibernate 的版本
    ext['hibernate.version'] = '5.2.8.Final'
    ......
}
// 依赖关系
dependencies {
	......
    // 添加 Spring Data JPA 的依赖
    compile('org.springframework.boot:spring-boot-starter-data-jpa')

    // 添加 MySQL连接驱动 的依赖
    compile('mysql:mysql-connector-java:6.0.5')
    
     // 添加 H2 的依赖 内存数据库
    runtime('com.h2database:h2:1.4.193')
	......
}
```

### 3.5 数据持久化实战

#### 3.5.1 H2 内存数据库

**后台编码**

- 实体 User
- 资源库 UserRepository
- 控制器 UserController

修改实体：实现 `Serializable` 接口、添加 `@Entity` 、设置主键 `@Id` 和自增策略 `@GeneratedValue`

```java
@Entity // 实体
public class User implements Serializable {

    @Id // 主键
    @GeneratedValue(strategy = GenerationType.IDENTITY) // 自增策略
    private long id;
    private String name;
    private String email;
    private int age;

    protected User() { // JPA 的规范要求无参构造函数；设为 protected 防止直接使用
    }

    public User(long id, String name, String email, int age) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.age = age;
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", email='" + email + '\'' +
                ", age=" + age +
                '}';
    }
}
```

修改资源库：删除自己的实现、继承 JPA 的 `@Repository` 接口

```java
/**
 * @author yu.zh [yuzh233@gmail.com] 2018/09/25 21:57
 * <p>
 * CrudRepository 提供了常用接口，自己无需编写接口。
 */
public interface UserRepository extends CrudRepository<User, Long> {
}
```

修改控制器：将自己的实现方法的调用改为 JPA 的常用 api，如：`findAll()` /  `findOne(id)` /  `save(user)` /  `delete(id)` 

**访问与验证数据**

能正常访问与操作，数据存储在了 `内存数据库 H2` ,使用 h2 的 `控制台` 访问数据。

设置显示 h2 控制台：

```yml
spring:
  # 使用 H2 控制台
  h2.console.enabled: true
```

访问控制台： http://localhost:8080/h2-console/

![](blog-img/image_15.png)

注意：JDBC URL 需要手动更改为 `jdbc:h2:mem:testdb` 才能正常访问，否则看不到保存的表。接着点击 `connect` 

![](blog-img/image_17.png)



可以看到 JPA 在内存型数据中自动保存了一张 `USER` 表，就是 User 实体所映射的表，并且根据主键关系与实体属性映射了添加的数据，我们可以与使用关系型数据库一样的方式对内存型数据库执行 SQL 操作。

#### 3.5.2 MySql 物理数据库

修改配置文件：

```yml
spring:
  # 模板引擎
  thymeleaf:
    encoding: UTF-8
    cache: false
    mode: HTML5

  # 使用 H2 控制台
  h2.console.enabled: true

  # 数据源
  datasource:
    url: jdbc:mysql://localhost/blog?characterEncoding=utf-8&useSSL=false&serverTimezone=UTC
    username: root
    password: admin
    driver-class-name: com.mysql.cj.jdbc.Driver

  # JPA
  jpa:
    show-sql: true
    hibernate:
      # 每次启动都会删除之前的表结构和数据并重新生成新的表结构
      ddl-auto: create-drop
```

启动项目，可以看到 hibernate 自动创建了表结构，字段和实体属性一一对应。

![](blog-img/image_18.png)

浏览器存入了两条数据之后，查看数据库：

![](blog-img/image_19.png)

数据成功存入，并且 h2 数据库没有存入，说明指定了 mysql 作为数据源， h2 的配置可有可无了。

**在实际场景中使用 MySql 这种大型数据库，在开发测试过程中建议使用 H2 这种内存数据库，提高开发效率。**

