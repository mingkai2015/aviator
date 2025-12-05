# Aviator Demo

一个基于 Aviator Spring Boot Starter 的完整示例项目，展示了如何使用 Aviator 表达式引擎实现各种业务功能。

## 🚀 快速开始

### 方式一：本地运行

```bash
# 构建项目
./gradlew clean build

# 运行项目
./gradlew bootRun
```

应用启动后，访问：http://localhost:8080

### 方式二：Docker 运行（推荐）

```bash
# 1. 构建 Docker 镜像（使用 Jib，无需 Docker daemon）
./gradlew jibDockerBuild

# 2. 运行容器
docker run -d -p 8080:8080 --name aviator-demo aviator-demo:latest

# 3. 查看日志
docker logs -f aviator-demo
```

应用启动后，访问：http://localhost:8080

### 测试 API

使用 curl 或 Postman 测试各个 API 接口。

## 📦 项目结构

```
aviator-demo/
├── src/main/java/com/example/demo/
│   ├── AviatorDemoApplication.java          # 启动类
│   ├── config/
│   │   └── CustomAviatorFunctions.java      # 自定义函数配置
│   ├── controller/
│   │   ├── PriceController.java             # 价格计算接口
│   │   ├── RuleController.java              # 规则引擎接口
│   │   └── FormulaController.java           # 公式管理接口
│   ├── service/
│   │   ├── PriceCalculationService.java     # 价格计算服务
│   │   ├── RuleEngineService.java           # 规则引擎服务
│   │   └── FormulaService.java              # 公式管理服务
│   ├── model/
│   │   ├── PriceCalculation.java            # 价格计算模型
│   │   └── LoanApplication.java             # 贷款申请模型
│   └── dto/
│       ├── ApiResponse.java                 # 统一响应
│       └── ExpressionRequest.java           # 表达式请求
└── src/main/resources/
    └── application.yml                       # 配置文件
```

## 🎯 功能特性

### 1. 价格计算服务

提供多种价格计算功能：

#### 1.1 最终价格计算

```bash
curl -X POST http://localhost:8080/api/price/final \
  -H "Content-Type: application/json" \
  -d '{
    "originalPrice": 100,
    "quantity": 2,
    "discountRate": 0.1,
    "shippingFee": 10,
    "taxRate": 0.05
  }'
```

#### 1.2 折扣计算

```bash
curl "http://localhost:8080/api/price/discount?price=100&rate=0.2"
# 返回: 80.0
```

#### 1.3 VIP 折扣

```bash
curl "http://localhost:8080/api/price/vip?price=100&level=3"
# VIP3 享受 15% 折扣，返回: 85.0
```

#### 1.4 满减优惠

```bash
curl "http://localhost:8080/api/price/reduction?amount=250"
# 满200减25，返回: 225.0
```

#### 1.5 阶梯价格

```bash
curl "http://localhost:8080/api/price/tiered?quantity=60&unitPrice=10"
# 51-100件享受8折，返回: 480.0
```

#### 1.6 复利计算

```bash
curl "http://localhost:8080/api/price/compound?principal=10000&rate=0.05&periods=12"
# 本金10000，年利率5%，12个月，返回: 1795.86
```

### 2. 规则引擎服务

实现复杂的业务规则判断：

#### 2.1 贷款资格评估

```bash
curl -X POST http://localhost:8080/api/rule/loan/evaluate \
  -H "Content-Type: application/json" \
  -d '{
    "age": 30,
    "monthlyIncome": 10000,
    "loanAmount": 50000,
    "creditScore": 720,
    "hasCollateral": true,
    "employmentYears": 3
  }'
# 返回: {"success":true,"message":"符合贷款条件","data":true}
```

#### 2.2 贷款利率计算

```bash
curl "http://localhost:8080/api/rule/loan/rate?credit=700&collateral=true"
# 信用分700，有抵押物，返回: 0.04 (4%)
```

#### 2.3 用户等级评估

```bash
curl "http://localhost:8080/api/rule/user/level?spent=6000&orders=35"
# 累计消费6000元，35笔订单，返回: "GOLD"
```

#### 2.4 风险评分

```bash
curl "http://localhost:8080/api/rule/risk/score?age=35&income=10000&credit=720&debt=0.3&defaulted=false"
# 返回风险评分（0-100，越高越低风险）
```

#### 2.5 优惠券发放

```bash
curl "http://localhost:8080/api/rule/coupon?days=25&orders=15&avgValue=180&newUser=false"
# 返回: "REGULAR_COUPON"
```

#### 2.6 成绩等级

```bash
curl "http://localhost:8080/api/rule/grade?score=85"
# 返回: "B"
```

#### 2.7 自定义规则

```bash
curl -X POST http://localhost:8080/api/rule/custom \
  -H "Content-Type: application/json" \
  -d '{
    "expression": "age >= 18 && score >= 60",
    "variables": {
      "age": 20,
      "score": 75
    }
  }'
# 返回: {"success":true,"data":true}
```

### 3. 公式管理服务

管理和使用预定义公式：

#### 3.1 查看所有公式

```bash
curl "http://localhost:8080/api/formula/list"
```

返回所有可用公式：
```json
{
  "discount": "price * (1 - rate)",
  "tax": "amount * (1 + taxRate)",
  "compound_interest": "principal * math.pow(1 + rate, periods)",
  "bmi": "weight / math.pow(height / 100, 2)",
  "circle_area": "math.pi * math.pow(radius, 2)",
  ...
}
```

#### 3.2 使用公式计算

```bash
# 计算折扣
curl -X POST http://localhost:8080/api/formula/calculate/discount \
  -H "Content-Type: application/json" \
  -d '{"price": 100, "rate": 0.2}'
# 返回: 80.0

# 计算 BMI
curl -X POST http://localhost:8080/api/formula/calculate/bmi \
  -H "Content-Type: application/json" \
  -d '{"weight": 70, "height": 175}'
# 返回: 22.86

# 计算圆面积
curl -X POST http://localhost:8080/api/formula/calculate/circle_area \
  -H "Content-Type: application/json" \
  -d '{"radius": 5}'
# 返回: 78.54
```

#### 3.3 添加自定义公式

```bash
curl -X POST "http://localhost:8080/api/formula/add?name=myFormula&expression=a*2+b"
```

#### 3.4 删除公式

```bash
curl -X DELETE "http://localhost:8080/api/formula/myFormula"
```

#### 3.5 验证公式语法

```bash
curl "http://localhost:8080/api/formula/validate?expression=a+b*2"
# 返回: {"success":true,"message":"公式语法正确","data":true}
```

## 🛠️ 自定义函数

项目扩展了 10 个自定义函数：

| 函数名 | 说明 | 示例 |
|--------|------|------|
| `square(n)` | 平方 | `square(5)` → 25 |
| `cube(n)` | 立方 | `cube(3)` → 27 |
| `isEven(n)` | 判断偶数 | `isEven(4)` → true |
| `reverse(s)` | 字符串反转 | `reverse("hello")` → "olleh" |
| `capitalize(s)` | 首字母大写 | `capitalize("hello")` → "Hello" |
| `formatNow(fmt)` | 格式化当前时间 | `formatNow("yyyy-MM-dd")` → "2024-12-04" |
| `inRange(v,min,max)` | 范围检查 | `inRange(5,1,10)` → true |
| `toPercent(n)` | 百分比格式化 | `toPercent(0.25)` → "25.00%" |
| `round(n,digits)` | 保留小数 | `round(3.14159,2)` → 3.14 |
| `applyDiscount(p,d)` | 应用折扣 | `applyDiscount(100,0.2)` → 80.0 |

### 使用示例

```bash
curl -X POST http://localhost:8080/api/rule/custom \
  -H "Content-Type: application/json" \
  -d '{
    "expression": "square(5) + cube(3)",
    "variables": {}
  }'
# 返回: 52 (25 + 27)

curl -X POST http://localhost:8080/api/rule/custom \
  -H "Content-Type: application/json" \
  -d '{
    "expression": "isEven(num) ? \"偶数\" : \"奇数\"",
    "variables": {"num": 8}
  }'
# 返回: "偶数"
```

## ⚙️ 配置说明

`application.yml` 配置：

```yaml
aviator:
  enable: true              # 启用 Aviator
  cache-enabled: true       # 启用表达式缓存
  cache-size: 1000         # 缓存大小
  optimize-enabled: true    # 启用优化
  trace-enabled: false      # 调试追踪（生产环境应为 false）
```

## 📊 使用场景

### 1. 电商平台
- 动态价格计算
- 优惠券规则
- 会员折扣
- 满减活动
- 阶梯定价

### 2. 金融系统
- 贷款资格评估
- 利率计算
- 风险评分
- 复利计算
- 信用评级

### 3. 积分系统
- 积分计算规则
- 等级评定
- 奖励规则
- 兑换比率

### 4. 报表系统
- 动态公式计算
- 数据汇总
- 指标计算

### 5. 教务系统
- 成绩评定
- GPA 计算
- 奖学金评定

## 🧪 测试

运行测试：

```bash
./gradlew test
```

## 🐳 Docker 部署

本项目支持多种方式打包成 Docker 镜像。

### 方式一：Jib 插件（推荐 ⭐⭐⭐⭐⭐）

**优势：** 构建速度快（~12秒）、镜像小（~106MB）、无需 Docker daemon

#### 1. 构建到本地 Docker

如果你已经安装并启动了 Docker：

```bash
./gradlew jibDockerBuild
```

这会创建镜像：`aviator-demo:latest` 和 `aviator-demo:0.0.1-SNAPSHOT`

#### 2. 构建为 tar 文件（无需 Docker）

如果没有 Docker，可以构建为 tar 文件：

```bash
./gradlew jibBuildTar
```

生成的 tar 文件位于：`build/jib-image.tar`

加载到 Docker：

```bash
docker load < build/jib-image.tar
```

#### 3. 推送到 Docker Registry

```bash
# 推送到 Docker Hub
./gradlew jib \
  -Djib.to.image=your-username/aviator-demo:latest \
  -Djib.to.auth.username=YOUR_USERNAME \
  -Djib.to.auth.password=YOUR_PASSWORD

# 推送到私有 Registry
./gradlew jib \
  -Djib.to.image=registry.example.com/aviator-demo:latest
```

### 方式二：传统 Dockerfile

使用项目根目录的 `Dockerfile`：

```bash
# 构建镜像
docker build -t aviator-demo:latest .

# 不使用缓存
docker build --no-cache -t aviator-demo:latest .
```

### 方式三：Spring Boot bootBuildImage

需要 Docker daemon 运行：

```bash
# 确保 Docker 已启动
open -a Docker  # macOS

# 构建镜像
./gradlew bootBuildImage --imageName=aviator-demo:latest
```

### 运行 Docker 容器

```bash
# 基本运行
docker run -p 8080:8080 aviator-demo:latest

# 后台运行（推荐）
docker run -d -p 8080:8080 --name aviator-demo aviator-demo:latest

# 带环境变量和资源限制
docker run -d \
  --name aviator-demo \
  -p 8080:8080 \
  -e SPRING_PROFILES_ACTIVE=prod \
  -e JAVA_OPTS="-Xms512m -Xmx1024m" \
  --memory="1g" \
  --cpus="1.0" \
  --restart=unless-stopped \
  aviator-demo:latest

# 查看日志
docker logs -f aviator-demo

# 停止和删除容器
docker stop aviator-demo
docker rm aviator-demo
```

### Docker Compose

创建 `docker-compose.yml`：

```yaml
version: '3.8'

services:
  aviator-demo:
    image: aviator-demo:latest
    ports:
      - "8080:8080"
    environment:
      - SPRING_PROFILES_ACTIVE=prod
      - JAVA_OPTS=-Xms256m -Xmx512m
    restart: unless-stopped
```

使用 Docker Compose：

```bash
# 启动服务
docker-compose up -d

# 查看日志
docker-compose logs -f

# 停止服务
docker-compose down
```

### 方案对比

| 方案 | 构建时间 | 镜像大小 | 需要 Docker | 推荐度 |
|-----|---------|---------|------------|--------|
| **Jib** | ~12秒 | ~106MB | ❌ | ⭐⭐⭐⭐⭐ |
| **Dockerfile** | ~2-3分钟 | ~180MB | ✅ | ⭐⭐⭐ |
| **bootBuildImage** | ~1-2分钟 | ~250MB | ✅ | ⭐⭐⭐ |

**推荐使用 Jib 方案**，构建速度最快，镜像最小，且不需要 Docker daemon。

详细的 Docker 配置说明请参考 [DOCKER.md](./DOCKER.md)

## 📝 开发建议

### 1. 添加新的计算功能

在 `service` 包下创建新的服务类，注入 `AviatorService`：

```java
@Service
public class MyCalculationService {
    @Autowired
    private AviatorService aviatorService;
    
    public Object calculate(Map<String, Object> params) {
        return aviatorService.execute("your_expression", params);
    }
}
```

### 2. 添加新的自定义函数

在 `CustomAviatorFunctions.java` 中添加：

```java
aviatorService.addFunction(new AbstractFunction() {
    @Override
    public String getName() {
        return "myFunction";
    }
    
    @Override
    public AviatorObject call(Map<String, Object> env, AviatorObject arg1) {
        // 实现逻辑
    }
});
```

### 3. 创建新的 REST API

在 `controller` 包下创建新的控制器。

## 🔍 性能优化

1. **使用公式预编译**: 对于频繁使用的表达式，使用 `FormulaService` 预编译
2. **启用缓存**: 配置文件中已启用表达式缓存
3. **避免复杂表达式**: 将复杂逻辑拆分为多个简单表达式

## 🐛 故障排除

### 问题 1: 依赖找不到

确保 aviator-spring-boot-starter 已发布到本地 Maven 仓库：

```bash
cd ../aviator-spring-boot-starter
./gradlew publishToMavenLocal
```

### 问题 2: 自动配置不生效

检查 `application.yml` 中 `aviator.enable` 是否为 `true`。

### 问题 3: 表达式语法错误

使用验证接口检查表达式语法：

```bash
curl "http://localhost:8080/api/formula/validate?expression=your_expression"
```

## 📚 参考资源

- [Aviator 官方文档](https://github.com/killme2008/aviator)
- [Spring Boot 官方文档](https://spring.io/projects/spring-boot)
- [aviator-spring-boot-starter 项目](../aviator-spring-boot-starter)
- [Jib 官方文档](https://github.com/GoogleContainerTools/jib)
- [Docker 部署详细文档](./DOCKER.md)

## 📧 联系方式

如有问题或建议，欢迎提交 Issue。

---

**版本**: 0.0.1-SNAPSHOT  
**更新日期**: 2024-12-04

