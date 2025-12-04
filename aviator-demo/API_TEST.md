# API 测试指南

快速测试 Aviator Demo 项目的所有 API 接口。

## 🚀 启动应用

```bash
cd /Users/mac/workspace/projects/aviator-demo
./gradlew bootRun
```

等待启动完成后，应用会在 http://localhost:8080 运行。

## 📊 API 测试用例

### 1. 价格计算 API

#### 1.1 计算最终价格（含折扣、运费、税费）

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

**预期结果**: `{"success":true,"message":"计算成功","data":199.5}`

---

#### 1.2 简单折扣计算

```bash
curl "http://localhost:8080/api/price/discount?price=100&rate=0.2"
```

**预期结果**: `{"success":true,"data":80.0}`

---

#### 1.3 VIP 折扣（等级越高折扣越大）

```bash
# VIP 1级 (5% 折扣)
curl "http://localhost:8080/api/price/vip?price=100&level=1"
# 预期: 95.0

# VIP 3级 (15% 折扣)
curl "http://localhost:8080/api/price/vip?price=100&level=3"
# 预期: 85.0

# VIP 5级 (最高 20% 折扣)
curl "http://localhost:8080/api/price/vip?price=100&level=5"
# 预期: 80.0
```

---

#### 1.4 满减优惠

```bash
# 未满100，无优惠
curl "http://localhost:8080/api/price/reduction?amount=99"
# 预期: 99.0

# 满100减10
curl "http://localhost:8080/api/price/reduction?amount=150"
# 预期: 140.0

# 满200减25
curl "http://localhost:8080/api/price/reduction?amount=250"
# 预期: 225.0

# 满500减80
curl "http://localhost:8080/api/price/reduction?amount=500"
# 预期: 420.0
```

---

#### 1.5 阶梯价格（批量购买折扣）

```bash
# 1-10件，原价
curl "http://localhost:8080/api/price/tiered?quantity=5&unitPrice=10"
# 预期: 50.0

# 11-50件，9折
curl "http://localhost:8080/api/price/tiered?quantity=30&unitPrice=10"
# 预期: 270.0

# 51-100件，8折
curl "http://localhost:8080/api/price/tiered?quantity=60&unitPrice=10"
# 预期: 480.0

# 100+件，7折
curl "http://localhost:8080/api/price/tiered?quantity=150&unitPrice=10"
# 预期: 1050.0
```

---

#### 1.6 复利计算

```bash
# 本金10000，年利率5%，12个月
curl "http://localhost:8080/api/price/compound?principal=10000&rate=0.05&periods=12"
# 预期: 约 1795.86
```

---

### 2. 规则引擎 API

#### 2.1 贷款资格评估

**符合条件的申请**:
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
```
**预期**: `{"success":true,"message":"符合贷款条件","data":true}`

**不符合条件的申请**:
```bash
curl -X POST http://localhost:8080/api/rule/loan/evaluate \
  -H "Content-Type: application/json" \
  -d '{
    "age": 20,
    "monthlyIncome": 3000,
    "loanAmount": 100000,
    "creditScore": 550,
    "hasCollateral": false,
    "employmentYears": 0
  }'
```
**预期**: `{"success":true,"message":"不符合贷款条件","data":false}`

---

#### 2.2 贷款利率计算

```bash
# 信用分750，有抵押物：4% - 1% = 3%
curl "http://localhost:8080/api/rule/loan/rate?credit=750&collateral=true"

# 信用分700，无抵押物：5%
curl "http://localhost:8080/api/rule/loan/rate?credit=700&collateral=false"

# 信用分600，有抵押物：7% - 1% = 6%
curl "http://localhost:8080/api/rule/loan/rate?credit=600&collateral=true"
```

---

#### 2.3 用户等级评估

```bash
# 钻石会员（消费>=10000，订单>=50）
curl "http://localhost:8080/api/rule/user/level?spent=12000&orders=60"
# 预期: "DIAMOND"

# 黄金会员（消费>=5000，订单>=30）
curl "http://localhost:8080/api/rule/user/level?spent=6000&orders=35"
# 预期: "GOLD"

# 银牌会员（消费>=2000，订单>=15）
curl "http://localhost:8080/api/rule/user/level?spent=3000&orders=20"
# 预期: "SILVER"

# 铜牌会员（消费>=500，订单>=5）
curl "http://localhost:8080/api/rule/user/level?spent=800&orders=10"
# 预期: "BRONZE"

# 普通会员
curl "http://localhost:8080/api/rule/user/level?spent=200&orders=2"
# 预期: "NORMAL"
```

---

#### 2.4 风险评分

```bash
# 低风险用户（高评分）
curl "http://localhost:8080/api/rule/risk/score?age=35&income=15000&credit=750&debt=0.2&defaulted=false"
# 预期: 约 90+

# 高风险用户（低评分）
curl "http://localhost:8080/api/rule/risk/score?age=22&income=3000&credit=600&debt=0.7&defaulted=true"
# 预期: 约 30-
```

---

#### 2.5 优惠券发放规则

```bash
# 新用户优惠券
curl "http://localhost:8080/api/rule/coupon?days=1&orders=0&avgValue=0&newUser=true"
# 预期: "WELCOME_COUPON"

# VIP 优惠券
curl "http://localhost:8080/api/rule/coupon?days=35&orders=25&avgValue=250&newUser=false"
# 预期: "VIP_COUPON"

# 普通优惠券
curl "http://localhost:8080/api/rule/coupon?days=20&orders=12&avgValue=150&newUser=false"
# 预期: "REGULAR_COUPON"

# 试用优惠券
curl "http://localhost:8080/api/rule/coupon?days=8&orders=2&avgValue=50&newUser=false"
# 预期: "TRIAL_COUPON"

# 无优惠券
curl "http://localhost:8080/api/rule/coupon?days=3&orders=0&avgValue=0&newUser=false"
# 预期: "NO_COUPON"
```

---

#### 2.6 成绩等级评定

```bash
curl "http://localhost:8080/api/rule/grade?score=95"  # A
curl "http://localhost:8080/api/rule/grade?score=85"  # B
curl "http://localhost:8080/api/rule/grade?score=75"  # C
curl "http://localhost:8080/api/rule/grade?score=65"  # D
curl "http://localhost:8080/api/rule/grade?score=55"  # F
```

---

#### 2.7 自定义规则（动态表达式）

**示例 1: 简单条件判断**
```bash
curl -X POST http://localhost:8080/api/rule/custom \
  -H "Content-Type: application/json" \
  -d '{
    "expression": "age >= 18 && score >= 60",
    "variables": {"age": 20, "score": 75}
  }'
# 预期: {"success":true,"data":true}
```

**示例 2: 复杂计算**
```bash
curl -X POST http://localhost:8080/api/rule/custom \
  -H "Content-Type: application/json" \
  -d '{
    "expression": "(price * quantity * (1 - discount) + shipping) * (1 + tax)",
    "variables": {
      "price": 50,
      "quantity": 3,
      "discount": 0.1,
      "shipping": 10,
      "tax": 0.05
    }
  }'
# 预期: 151.725
```

**示例 3: 使用自定义函数**
```bash
curl -X POST http://localhost:8080/api/rule/custom \
  -H "Content-Type: application/json" \
  -d '{
    "expression": "square(5) + cube(3)",
    "variables": {}
  }'
# 预期: 52 (25 + 27)

curl -X POST http://localhost:8080/api/rule/custom \
  -H "Content-Type: application/json" \
  -d '{
    "expression": "isEven(num) ? \"偶数\" : \"奇数\"",
    "variables": {"num": 8}
  }'
# 预期: "偶数"

curl -X POST http://localhost:8080/api/rule/custom \
  -H "Content-Type: application/json" \
  -d '{
    "expression": "capitalize(reverse(text))",
    "variables": {"text": "hello"}
  }'
# 预期: "Olleh"
```

---

### 3. 公式管理 API

#### 3.1 查看所有可用公式

```bash
curl "http://localhost:8080/api/formula/list"
```

**预期返回所有预定义公式列表**

---

#### 3.2 使用预定义公式计算

**折扣计算**:
```bash
curl -X POST http://localhost:8080/api/formula/calculate/discount \
  -H "Content-Type: application/json" \
  -d '{"price": 100, "rate": 0.2}'
# 预期: 80.0
```

**税费计算**:
```bash
curl -X POST http://localhost:8080/api/formula/calculate/tax \
  -H "Content-Type: application/json" \
  -d '{"amount": 100, "taxRate": 0.1}'
# 预期: 110.0
```

**BMI 计算**:
```bash
curl -X POST http://localhost:8080/api/formula/calculate/bmi \
  -H "Content-Type: application/json" \
  -d '{"weight": 70, "height": 175}'
# 预期: 22.86
```

**圆面积计算**:
```bash
curl -X POST http://localhost:8080/api/formula/calculate/circle_area \
  -H "Content-Type: application/json" \
  -d '{"radius": 5}'
# 预期: 78.54
```

**复利计算**:
```bash
curl -X POST http://localhost:8080/api/formula/calculate/compound_interest \
  -H "Content-Type: application/json" \
  -d '{"principal": 1000, "rate": 0.05, "periods": 12}'
# 预期: 1795.86
```

---

#### 3.3 添加自定义公式

```bash
curl -X POST "http://localhost:8080/api/formula/add?name=profit&expression=(revenue-cost)/cost*100"
# 预期: {"success":true,"message":"公式添加成功","data":"profit"}
```

**使用新添加的公式**:
```bash
curl -X POST http://localhost:8080/api/formula/calculate/profit \
  -H "Content-Type: application/json" \
  -d '{"revenue": 150, "cost": 100}'
# 预期: 50.0 (利润率50%)
```

---

#### 3.4 验证公式语法

```bash
# 正确的公式
curl "http://localhost:8080/api/formula/validate?expression=a+b*2"
# 预期: {"success":true,"message":"公式语法正确","data":true}

# 错误的公式
curl "http://localhost:8080/api/formula/validate?expression=a++b**2"
# 预期: {"success":true,"message":"公式语法错误","data":false}
```

---

#### 3.5 删除公式

```bash
curl -X DELETE "http://localhost:8080/api/formula/profit"
# 预期: {"success":true,"message":"公式删除成功","data":"profit"}
```

---

## 🧪 批量测试脚本

创建一个测试脚本 `test_all.sh`:

```bash
#!/bin/bash

echo "=== 测试价格计算 API ==="
curl -s "http://localhost:8080/api/price/discount?price=100&rate=0.2" | jq
sleep 1

echo -e "\n=== 测试规则引擎 API ==="
curl -s "http://localhost:8080/api/rule/grade?score=85" | jq
sleep 1

echo -e "\n=== 测试公式管理 API ==="
curl -s "http://localhost:8080/api/formula/list" | jq
sleep 1

echo -e "\n=== 测试自定义规则 ==="
curl -s -X POST http://localhost:8080/api/rule/custom \
  -H "Content-Type: application/json" \
  -d '{"expression": "square(5)", "variables": {}}' | jq

echo -e "\n=== 所有测试完成 ==="
```

运行：
```bash
chmod +x test_all.sh
./test_all.sh
```

## 📝 测试清单

- [ ] 价格计算 - 最终价格
- [ ] 价格计算 - 折扣
- [ ] 价格计算 - VIP 折扣
- [ ] 价格计算 - 满减
- [ ] 价格计算 - 阶梯价格
- [ ] 价格计算 - 复利
- [ ] 规则引擎 - 贷款评估
- [ ] 规则引擎 - 贷款利率
- [ ] 规则引擎 - 用户等级
- [ ] 规则引擎 - 风险评分
- [ ] 规则引擎 - 优惠券
- [ ] 规则引擎 - 成绩等级
- [ ] 规则引擎 - 自定义规则
- [ ] 公式管理 - 列表
- [ ] 公式管理 - 计算
- [ ] 公式管理 - 添加
- [ ] 公式管理 - 删除
- [ ] 公式管理 - 验证
- [ ] 自定义函数 - square
- [ ] 自定义函数 - cube
- [ ] 自定义函数 - isEven
- [ ] 自定义函数 - reverse
- [ ] 自定义函数 - capitalize

## 🔧 使用 Postman

可以将以上 curl 命令导入 Postman 或创建 Postman Collection 进行测试。

## 📊 预期性能

- 简单表达式计算: < 1ms
- 复杂表达式计算: < 5ms
- 预编译公式执行: < 0.5ms

---

**提示**: 如果遇到错误，检查应用日志获取详细信息。

