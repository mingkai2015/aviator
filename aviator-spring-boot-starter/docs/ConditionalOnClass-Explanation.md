# @ConditionalOnClass 注解选择说明

## 问题

为什么使用 `@ConditionalOnClass(com.googlecode.aviator.AviatorEvaluator.class)` 而不是 `com.googlecode.aviator.Expression.class`？

## 答案

使用 `AviatorEvaluator` 是最佳实践，原因如下：

### 1. 类的性质不同

#### AviatorEvaluator（具体类）
```java
package com.googlecode.aviator;

public class AviatorEvaluator {
    // Aviator 引擎的核心入口类
    // 提供所有静态方法用于表达式执行
    
    public static Object execute(String expression);
    public static Object execute(String expression, Map<String, Object> env);
    public static Expression compile(String expression);
    public static Expression compile(String expression, boolean cached);
    public static void addFunction(AbstractFunction function);
    // ... 更多核心方法
}
```

**特点**：
- ✅ 具体类，不是接口
- ✅ Aviator 库的唯一核心入口
- ✅ 包含所有主要功能
- ✅ 标志性类，专属于 Aviator

#### Expression（接口）
```java
package com.googlecode.aviator;

public interface Expression extends Serializable {
    // 只是表示编译后表达式的接口
    Object execute(Map<String, Object> env);
    Object execute();
}
```

**特点**：
- ⚠️ 接口，可能被其他实现
- ⚠️ 不是库的入口
- ⚠️ 功能有限
- ⚠️ 不够标志性

### 2. Spring Boot 条件注解的目的

`@ConditionalOnClass` 的作用是：**判断某个库是否在 classpath 中**

#### 应该选择：
- ✅ 库的核心类
- ✅ 具有唯一性的类
- ✅ 标志性的入口类
- ✅ 不太可能与其他库重名的类

#### 不应该选择：
- ❌ 接口（可能被多个库实现）
- ❌ 通用的类名
- ❌ 内部实现类
- ❌ 工具类

### 3. 实际使用场景

在我们的 `AviatorService` 中，我们实际使用的就是 `AviatorEvaluator`：

```java
@Service
public class AviatorService {
    
    @PostConstruct
    public void init() {
        // 直接使用 AviatorEvaluator 的静态方法
        AviatorEvaluator.getInstance().useLRUExpressionCache(cacheSize);
        AviatorEvaluator.setOption(Options.OPTIMIZE_LEVEL, ...);
    }
    
    public Object execute(String expression, Map<String, Object> env) {
        // 直接调用 AviatorEvaluator
        return AviatorEvaluator.execute(expression, env);
    }
    
    public Expression compile(String expression) {
        // 通过 AviatorEvaluator 获取 Expression
        return AviatorEvaluator.compile(expression);
    }
}
```

**可以看到**：
- 我们的服务依赖的是 `AviatorEvaluator`，不是 `Expression`
- `Expression` 只是 `AviatorEvaluator.compile()` 的返回值类型
- 如果没有 `AviatorEvaluator`，我们的服务无法工作

### 4. Spring Boot 生态中的类似例子

#### Redis
```java
@Configuration
@ConditionalOnClass(RedisOperations.class)  // 核心接口，但是标志性的
public class RedisAutoConfiguration {
    // ...
}
```

#### Jackson
```java
@Configuration
@ConditionalOnClass(ObjectMapper.class)  // 核心类
public class JacksonAutoConfiguration {
    // ...
}
```

#### Hibernate
```java
@Configuration
@ConditionalOnClass({ LocalSessionFactoryBean.class, EntityManager.class })
public class HibernateJpaAutoConfiguration {
    // 使用核心类 LocalSessionFactoryBean
}
```

#### Aviator（我们的实现）
```java
@Configuration
@ConditionalOnClass(AviatorEvaluator.class)  // 核心入口类
public class AviatorAutoConfiguration {
    // ...
}
```

### 5. 如果使用 Expression 会怎样？

#### 问题 1：不够准确
```java
@ConditionalOnClass(Expression.class)  // ❌ 不好的选择
```

- `Expression` 是接口，理论上可能存在其他实现
- 不能准确判断 Aviator 库是否存在
- 可能与其他表达式引擎的接口混淆

#### 问题 2：语义不清
```java
// 看到这个条件，你能确定是 Aviator 库吗？
@ConditionalOnClass(Expression.class)  // ❓ 哪个 Expression？

// 而这个很明确
@ConditionalOnClass(AviatorEvaluator.class)  // ✅ 明确是 Aviator
```

#### 问题 3：不符合最佳实践
- Spring Boot 官方推荐使用库的核心类
- 而不是通用接口或返回值类型

### 6. 完整的条件组合

在我们的自动配置中，使用了三个条件：

```java
@Configuration
@ConditionalOnClass(AviatorEvaluator.class)              // 1️⃣ Aviator 库存在
@ConditionalOnProperty(prefix = "aviator",                // 2️⃣ 配置开启
                       name = "enable", 
                       havingValue = "true")
@EnableConfigurationProperties(AviatorProperties.class)   // 3️⃣ 启用配置属性
public class AviatorAutoConfiguration {
    // ...
}
```

**条件解释**：
1. `@ConditionalOnClass(AviatorEvaluator.class)` - 确保 Aviator 库在 classpath 中
2. `@ConditionalOnProperty` - 确保用户显式启用了 `aviator.enable=true`
3. `@EnableConfigurationProperties` - 绑定配置属性

### 7. 验证这个选择

我们可以做一个简单的测试：

```java
// 如果 Aviator 库不存在
// 那么 AviatorEvaluator 类也不存在
// 自动配置就不会生效 ✅

// 如果 Aviator 库存在但 aviator.enable=false
// AviatorEvaluator 类存在，但配置条件不满足
// 自动配置仍然不会生效 ✅

// 只有当两个条件都满足时
// 自动配置才会生效 ✅
```

### 8. 类比说明

这就像：

#### 判断 MySQL 驱动是否存在
```java
// ✅ 好的选择
@ConditionalOnClass(com.mysql.jdbc.Driver.class)

// ❌ 不好的选择
@ConditionalOnClass(java.sql.Connection.class)  // 太通用了
```

#### 判断 Redis 客户端是否存在
```java
// ✅ 好的选择
@ConditionalOnClass(RedisTemplate.class)

// ❌ 不好的选择
@ConditionalOnClass(RedisConnection.class)  // 接口，不够具体
```

#### 判断 Aviator 库是否存在
```java
// ✅ 好的选择
@ConditionalOnClass(AviatorEvaluator.class)  // 核心入口类

// ❌ 不好的选择
@ConditionalOnClass(Expression.class)  // 接口，不够具体
```

## 总结

| 对比项 | AviatorEvaluator ✅ | Expression ❌ |
|--------|-------------------|--------------|
| 类型 | 具体类 | 接口 |
| 唯一性 | 专属于 Aviator | 可能被其他库实现 |
| 标志性 | 强（核心入口类） | 弱（只是返回值类型） |
| 功能性 | 完整（所有核心功能） | 有限（只有执行方法） |
| 实际使用 | 直接使用 | 间接使用 |
| 语义清晰 | 清晰明确 | 不够明确 |
| 最佳实践 | 符合 | 不符合 |

## 推荐做法

当编写 Spring Boot Starter 时，选择 `@ConditionalOnClass` 的类应该：

1. ✅ 选择库的核心入口类
2. ✅ 选择具有唯一性的类
3. ✅ 选择你实际会使用的类
4. ✅ 选择标志性的类名
5. ❌ 避免选择通用接口
6. ❌ 避免选择内部实现类
7. ❌ 避免选择工具类

## 参考

- [Spring Boot Conditional Annotations](https://docs.spring.io/spring-boot/docs/current/reference/html/features.html#features.developing-auto-configuration.condition-annotations)
- [Creating Your Own Auto-configuration](https://docs.spring.io/spring-boot/docs/current/reference/html/features.html#features.developing-auto-configuration)
- [Aviator Documentation](https://github.com/killme2008/aviator)

