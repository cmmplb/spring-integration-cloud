# 学习 SpringCloud 整合

spring 官网：https://spring.io/

Spring Cloud Alibaba-logo：https://sca.aliyun.com/

kubernetes：https://kubernetes.io/

spring-boot 和 spring-cloud 版本关系图

![版本关系图.png](doc%2Fimages%2F%E7%89%88%E6%9C%AC%E5%85%B3%E7%B3%BB%E5%9B%BE.png)

master 分支为最新版本 2023.0.3.x：

版本从新到旧排列，feature：

| 分支            | spring-boot    | spring-cloud | spring-cloud-alibaba | spring-cloud-kubernetes | 完成度 |
|---------------|----------------|--------------|----------------------|-------------------------|-----|
| 2023.0.3.x    | 3.3.4          | 2023.0.3     | 2023.0.1.2           | 3.1.3                   | ✅   |
| 2022.0.5.x    | 3.1.12         | 2022.0.5     | 2022.0.0.0           | 3.0.5                   | ✅   |
| 2021.0.9.x    | 2.7.18         | 2021.0.9     | 2021.0.6.1           | 2.1.9                   | ✅   |
| 2020.0.6.x    | 2.5.15         | 2020.0.6     | 2021.1               | 2.0.6                   | ✅   |
| Hoxton.SR12.x | 2.3.12.RELEASE | Hoxton.SR12  | 2.2.10-RC2           | 1.1.10.RELEASE          | ❌   |

**其他版本查看spring-parent.pom中配置的properties**

**TODO:**

````
- Higress
- spring-cloud-square(类似feign)
````

### 当前分支：feature/2021.0.9.x，对比上一个版本 2020.0.6.x 的配置变化和问题：**

****

1. maven.compiler.source 和 maven.compiler.target 升级为 17；

****

2. javax.servlet.* 相关包更改为 jakarta.servlet.*；

****

3. Spring Boot 3 只支持 OpenAPI3 规范，springfox 不再使用，替换为 springdoc，同时移除 swagger 部分注解：

   ````
   @Api → @Tag
   @ApiIgnore → @Parameter(hidden = true) 或 @Operation(hidden = true) 或 @Hidden
   @ApiImplicitParam → @Parameter
   @ApiImplicitParams → @Parameters
   @ApiModel → @Schema
   @ApiModelProperty(hidden = true) → @Schema(accessMode = READ_ONLY)
   @ApiModelProperty → @Schema
   @Operation(summary = “foo”, notes = “bar”) → @Operation(summary = “foo”, description = “bar”)，其中summary为左侧菜单展示的标题，description为右侧接口描述
   @ApiParam → @Parameter
   @ApiResponse(code = 404, message = “foo”) → @ApiResponse(responseCode = “404”, description = “foo”)
   ````

   knife4j ApiSupport 排序不生效问题：在 @Tag 中添加 extensions = {@Extension(properties = {@ExtensionProperty(name = "
   x-order", value = "3", parseValue = true)})}，并且description不能为空，原来的 ApiSort 和 ApiSupport
   用了没效果，看后续版本更新会不会生效。0.0

****

4. 访问接口打印警告：

   ````
   ocalVariableTableParameterNameDiscoverer : Using deprecated '-debug' fallback for parameter name resolution. Compile the affected code with '-parameters' instead or avoid its introspection: ...
   ````

    - public Result<String> index(String name), 解析不了请求参数name

    - 默认情况下，任何简单值类型（由 BeanUtils#isSimpleProperty 确定）且未由任何其他参数解析器解析的参数都将被视为使用
      @RequestParam 注解。

    - 如果不使用 spring-boot-starter-parent 作为父工程，那么接口中必须显式声明 @RequestParam("name")

    - LocalVariableTableParameterNameDiscoverer已在 Spring 6.1 中删除。

   **解决方案**

    - 方案1，在方法参数上添加注解

      ````
      @PathVariable(name=“id”, required = true)
      @RequestParam(name=“id”)
      ````

    - 方案2，编译时保留方法参数名

      idea设置：File > Settings > Build, Execution, Deployment > Compiler > Java Compiler > Additional command line
      parameters: （输入框中填写 -parameters）

    - 方案３，pom文件设置

      ````xml
      <!--　添加后重新编译代码　-->
      <plugin>
          <groupId>org.apache.maven.plugins</groupId>
          <artifactId>maven-compiler-plugin</artifactId>
          <configuration>
              <compilerArgs>
                  <arg>-parameters</arg>
              </compilerArgs>
          </configuration>
      </plugin>
      ````

****

5. 由于使用的是 JDK 17，源码启动 nacos-server 需要 JDK 8 的环境，这里把 nacos-server 相关模块移除；

****

6. Spring Security 升级到 6.1.9，之前 extends WebSecurityConfigurerAdapter 已弃用；

7. spring-security-oauth2-autoconfigure 弃用，更换为 spring-security-oauth2-authorization-server；

****

8. Mybatis-plus count 查询返回值由 int 改为 long；

****

9. Gateway ResponseStatusException getStatus() 方法移除，使用 getStatusCode()：

   `HttpStatus httpStatus = HttpStatus.valueOf(rse.getStatusCode().value())`

****

9. 使用到 JDK 9 及以上相关功能：

    - class.newInstance()方法被标记为过时，使用 clazz.getDeclaredConstructor().newInstance()；
    - Base64.decode 移除，Base64.getDecoder().decode

****

10. JDK 14 引入 @Serial 注解，用于标识 serialVersionUID、readObject、writeObject、readResolve 和 writeReplace 等序列化特殊方法；

****

11. jdk16添加instanceof模式匹配，在进行类型检查时可以直接进行类型转换：

```
if ((e instanceof BusinessException)) {
   BusinessException c = (BusinessException) e;
   if (c.getStatusCode() != 0) {
       setStatusCode(c.getStatusCode());
   }
   return ResultUtil.custom(c.getCode(), c.getMessage());
}

=>

if ((e instanceof BusinessException c)) {
   if (c.getStatusCode() != 0) {
       setStatusCode(c.getStatusCode());
   }
   return ResultUtil.custom(c.getCode(), c.getMessage());
}
```

****

12. 依赖：

**依赖升级：**

| 依赖                             | 之前版本   | 升级版本   | 
|--------------------------------|--------|--------|
| apollo-client                  | 1.9.2  | 2.3.0  | 
| fastjson                       | 1.2.76 | 2.0.53 |    
| mybatis                        | 3.5.9  | 3.5.16 |    
| mybatis-spring                 | 2.0.7  | 3.5.16 |    
| mybatis-spring-boot-starter    | 2.1.4  | 3.0.3  |    
| pagehelper-spring-boot-starter | 1.3.0  | 2.1.0  |    

****

**增加依赖：**

| 依赖                                           | 版本    |
|----------------------------------------------|-------|
| dynamic-datasource-spring-boot3-starter      | 4.3.1 |     
| mybatis-plus-spring-boot3-starter            | 3.5.8 |    
| knife4j-openapi3-jakarta-spring-boot-starter | 4.5.0 |   
| knife4j-gateway-spring-boot-starter          | 4.5.0 |     

****

**移除依赖：**

| 依赖                        | 版本      |
|---------------------------|---------|
| mybatis-plus-boot-starter | 3.4.3.1 |     

****

## 历史版本变化和问题

### feature/2.7.18.x，对比上一个版本 feature/2.5.15.x 的变化：

1. `META-INF/spring.factories` 中 org.springframework.boot.autoconfigure.EnableAutoConfiguration
   配置项改为在`META-INF/spring/org.springframework.boot.autoconfigure.AutoConfiguration.imports`中声明，不需要使用\，每行一个配置类

***
