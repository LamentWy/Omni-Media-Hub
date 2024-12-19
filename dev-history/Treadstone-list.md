# Tread stone

## sqlite

sqlite 必须在表中不包含任何一条数据时才可以用函数作为默认值，否则他就会报 ``Cannot add a column with non-constant default``。

比如以下语句，如果 User 表为空则可以添加，不为空则异常：
```sql
alter table User add column last_modified_date datetime not null default (datetime('now', 'localtime'));
```

## r2dbc not support sqlite

## Spring Security in Webflux 

``spring-boot-starter-security `` 的默认行为是，保护所有路径，并且自动生成一个用户，用户名为 user，其密码则是打印在 Console 中的一段随机字符串。

先复习一下 spring sercurity 的基础逻辑，详情去看[Spring Security文档](https://docs.spring.io/spring-security/reference/index.html)。

客户端向服务发起请求，Server 收到请求后 Filter/FilterChain (比如``SecurityWebFilterChain``) 先对请求进行过滤，
servlet/webflux 都是如此，然后请求进入验证(Authentication)环节。

这里主要关注 ``AuthenticationManager/ReactiveAuthenticationManager`` 和 ``AuthenticationProvider``。
AuthenticationManager 这个抽象就是用来定义 Spring Security 如何进行验证的 API，而 ``AuthenticationProvider`` 就是具体实现。当然这里是化简后并不准确的说法。

最后，验证，比如登录验证，你肯定要比对密码，而密码在数据库中，这就涉及到最后一个 Spring Security 的概念``UserDetailsService/ReactiveUserDetailsService``，它负责去访问数据库什么的。

之所以要复习这个是因为在 webflux 中集成 Spring Security 其实跟 servlet 中差不多，只不过其中的几个抽象你需要使用 ``Reactive`` 前缀的版本。

