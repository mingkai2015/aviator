# Aviator Spring Boot Starter

[![License](https://img.shields.io/badge/license-Apache%202.0-blue.svg)](LICENSE)
[![Java](https://img.shields.io/badge/Java-1.8+-orange.svg)](https://www.oracle.com/java/)
[![Spring Boot](https://img.shields.io/badge/Spring%20Boot-2.7+-green.svg)](https://spring.io/projects/spring-boot)

一个基于 Google Aviator 表达式引擎的 Spring Boot Starter，提供动态公式计算功能。

## 📋 目录

- [功能特性](#-功能特性)
- [快速开始](#快速开始)
- [配置说明](#配置说明)
- [高级用法](#高级用法)
- [支持的表达式](#支持的表达式)
- [常见表达式示例](#常见表达式示例)
- [应用场景](#应用场景)
- [开发者指南](#️-开发者指南)
- [系统要求](#系统要求)
- [贡献指南](#贡献指南)

## ✨ 功能特性

- 🚀 开箱即用的 Aviator 表达式引擎集成
- ⚙️ 灵活的配置选项
- 📦 完整的 Spring Boot 自动配置支持
- 🔥 表达式编译缓存，提升性能
- 🛠️ 支持自定义函数扩展
- 📝 支持配置文件智能提示

## 快速开始

### 1. 添加依赖

在你的 Spring Boot 项目中添加以下依赖：

**Gradle:**

```gradle
dependencies {
    implementation 'com.github.aviator:aviator-spring-boot-starter:1.0.0-SNAPSHOT'
}
```

**Maven:**

```xml
<dependency>
    <groupId>com.github.aviator</groupId>
    <artifactId>aviator-spring-boot-starter</artifactId>
    <version>1.0.0-SNAPSHOT</version>
</dependency>
```

### 2. 配置启用

在 `application.yml` 或 `application.properties` 中启用 Aviator：

**application.yml:**

```yaml
aviator:
  enable: true
  cache-enabled: true
  cache-size: 1000
  optimize-enabled: true
  trace-enabled: false
```

**application.properties:**

```properties
aviator.enable=true
aviator.cache-enabled=true
aviator.cache-size=1000
aviator.optimize-enabled=true
aviator.trace-enabled=false
```

### 3. 使用示例

```java
import com.github.aviator.service.AviatorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class CalculationService {

    @Autowired
    private AviatorService aviatorService;

    public void calculate() {
        // 简单计算
        Object result1 = aviatorService.execute("1 + 2 + 3");
        System.out.println("1 + 2 + 3 = " + result1); // 输出: 6

        // 使用变量
        Map<String, Object> env = new HashMap<>();
        env.put("a", 10);
        env.put("b", 20);
        Object result2 = aviatorService.execute("a + b * 2", env);
        System.out.println("a + b * 2 = " + result2); // 输出: 50

        // 字符串操作
        env.put("name", "Aviator");
        Object result3 = aviatorService.execute("'Hello, ' + name + '!'", env);
        System.out.println(result3); // 输出: Hello, Aviator!

        // 条件判断
        env.put("score", 85);
        Object result4 = aviatorService.execute("score >= 60 ? 'pass' : 'fail'", env);
        System.out.println(result4); // 输出: pass
    }
}
```

## 配置说明

| 配置项 | 类型 | 默认值 | 说明 |
|-------|------|--------|------|
| `aviator.enable` | Boolean | `false` | 是否启用 Aviator 自动配置 |
| `aviator.cache-enabled` | Boolean | `true` | 是否开启表达式编译缓存 |
| `aviator.cache-size` | Integer | `1000` | 表达式编译缓存大小 |
| `aviator.optimize-enabled` | Boolean | `true` | 是否优化表达式执行 |
| `aviator.trace-enabled` | Boolean | `false` | 是否允许追踪求值过程 |

## 高级用法

### 编译表达式

对于需要多次执行的表达式，可以先编译后重复使用：

```java
import com.googlecode.aviator.Expression;

@Autowired
private AviatorService aviatorService;

public void compileExample() {
    // 编译表达式
    Expression exp = aviatorService.compile("a * b + c");
    
    // 多次执行
    Map<String, Object> env1 = new HashMap<>();
    env1.put("a", 1);
    env1.put("b", 2);
    env1.put("c", 3);
    Object result1 = exp.execute(env1); // 5
    
    Map<String, Object> env2 = new HashMap<>();
    env2.put("a", 10);
    env2.put("b", 20);
    env2.put("c", 30);
    Object result2 = exp.execute(env2); // 230
}
```

### 自定义函数

```java
import com.googlecode.aviator.runtime.function.AbstractFunction;
import com.googlecode.aviator.runtime.type.AviatorDouble;
import com.googlecode.aviator.runtime.type.AviatorObject;

@Component
public class CustomFunctionConfig {

    @Autowired
    private AviatorService aviatorService;

    @PostConstruct
    public void init() {
        // 添加自定义函数
        aviatorService.addFunction(new AbstractFunction() {
            @Override
            public String getName() {
                return "double";
            }

            @Override
            public AviatorObject call(Map<String, Object> env, AviatorObject arg1) {
                Number num = (Number) arg1.getValue(env);
                return new AviatorDouble(num.doubleValue() * 2);
            }
        });
    }
}

// 使用自定义函数
Object result = aviatorService.execute("double(21)"); // 42.0
```

### 清除缓存

```java
@Autowired
private AviatorService aviatorService;

public void clearCache() {
    aviatorService.clearExpressionCache();
}
```

## 支持的表达式

Aviator 支持丰富的表达式语法：

- **算术运算**: `+`, `-`, `*`, `/`, `%`
- **关系运算**: `>`, `>=`, `<`, `<=`, `==`, `!=`
- **逻辑运算**: `&&`, `||`, `!`
- **位运算**: `&`, `|`, `^`, `~`, `<<`, `>>`
- **三元运算**: `condition ? value1 : value2`
- **字符串操作**: 连接、比较等
- **正则表达式**: `=~` 匹配操作符
- **集合操作**: `map`, `filter`, `reduce` 等
- **自定义函数**: 支持扩展自定义函数

## 常见表达式示例

```java
// 数学计算
"3.14 * r * r"                          // 圆面积
"(a + b) / 2"                           // 平均值

// 字符串处理
"string.length(name)"                   // 字符串长度
"string.substring(name, 0, 5)"         // 截取子串
"string.contains(text, 'keyword')"     // 包含判断

// 条件判断
"age >= 18 ? 'adult' : 'minor'"        // 年龄判断
"score >= 90 ? 'A' : (score >= 80 ? 'B' : 'C')" // 等级判断

// 集合操作
"count(list)"                           // 集合大小
"include(list, element)"                // 包含判断

// 日期时间
"sysdate() - birthday > 365 * 18"      // 判断是否成年
```

## 应用场景

### 动态定价
```java
// 根据会员等级、促销活动计算实时价格
"price * quantity * (1 - discount) * (1 + vipLevel * 0.05)"
```

### 业务规则引擎
```java
// 动态配置贷款审批规则
"age >= 18 && age <= 60 && income > 5000 && creditScore >= 600"
```

### 动态报表
```java
// 计算利润率
"(totalRevenue - totalCost) / totalRevenue * 100"
```

## 🛠️ 开发者指南

### 构建项目

```bash
# 克隆项目
git clone <repository-url>
cd aviator-spring-boot-starter

# 构建项目
./gradlew clean build

# 运行测试
./gradlew test
```

### 发布到本地 Maven 仓库

```bash
./gradlew publishToMavenLocal
```

发布后，其他本地项目可以引用这个依赖。发布位置：`~/.m2/repository/com/github/aviator/aviator-spring-boot-starter/`

### 项目结构

```
aviator-spring-boot-starter/
├── src/
│   ├── main/
│   │   ├── java/
│   │   │   └── com/github/aviator/
│   │   │       ├── autoconfigure/      # 自动配置
│   │   │       │   ├── AviatorAutoConfiguration.java
│   │   │       │   └── AviatorProperties.java
│   │   │       └── service/            # 核心服务
│   │   │           └── AviatorService.java
│   │   └── resources/
│   │       ├── META-INF/
│   │       │   └── spring.factories    # 自动配置注册
│   │       └── application.yml.example
│   └── test/                            # 测试用例
└── build.gradle                         # 构建配置
```

## 系统要求

- **JDK**: 1.8 或更高版本
- **Spring Boot**: 2.x
- **Gradle**: 7.x（通过 Wrapper 管理）

## 许可证

本项目采用 [Apache License 2.0](LICENSE) 许可证

## 参考资源

- [Aviator 官方文档](https://github.com/killme2008/aviator)
- [Spring Boot 官方文档](https://spring.io/projects/spring-boot)
- [Spring Boot Starter 开发指南](https://docs.spring.io/spring-boot/docs/current/reference/html/features.html#features.developing-auto-configuration)

## 贡献指南

欢迎贡献代码！请遵循以下步骤：

1. Fork 本项目
2. 创建特性分支 (`git checkout -b feature/AmazingFeature`)
3. 提交更改 (`git commit -m 'Add some AmazingFeature'`)
4. 推送到分支 (`git push origin feature/AmazingFeature`)
5. 开启 Pull Request

## 问题反馈

如有问题或建议，欢迎提交 [Issue](https://github.com/yourusername/aviator-spring-boot-starter/issues)

---

**版本**: 1.0.0-SNAPSHOT  
**最后更新**: 2025-12-04

