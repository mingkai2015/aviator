package com.example.demo.service;

import com.example.demo.model.LoanApplication;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

/**
 * 规则引擎服务
 * 演示如何使用 Aviator 实现业务规则引擎
 *
 * @author demo
 */
@Service
public class RuleEngineService {

    private static final Logger logger = LoggerFactory.getLogger(RuleEngineService.class);

    @Autowired
    private AviatorService aviatorService;

    /**
     * 评估贷款申请资格
     * 规则：
     * 1. 年龄在18-65之间
     * 2. 月收入 > 贷款金额的10%
     * 3. 信用分 >= 600
     * 4. 如果有抵押物，信用分要求降低到550
     * 5. 工作年限 >= 1年
     */
    public boolean evaluateLoanEligibility(LoanApplication application) {
        Map<String, Object> env = new HashMap<>();
        env.put("age", application.getAge());
        env.put("income", application.getMonthlyIncome());
        env.put("amount", application.getLoanAmount());
        env.put("credit", application.getCreditScore());
        env.put("collateral", application.getHasCollateral());
        env.put("years", application.getEmploymentYears());

        String rule = "age >= 18 && age <= 65 && " +
                     "income >= amount * 0.1 && " +
                     "(collateral ? credit >= 550 : credit >= 600) && " +
                     "years >= 1";

        logger.info("评估贷款资格: rule={}, params={}", rule, env);

        Object result = aviatorService.execute(rule, env);
        return (Boolean) result;
    }

    /**
     * 计算贷款利率
     * 根据信用分和是否有抵押物动态计算利率
     */
    public Double calculateLoanRate(Integer creditScore, Boolean hasCollateral) {
        Map<String, Object> env = new HashMap<>();
        env.put("credit", creditScore);
        env.put("collateral", hasCollateral);

        // 基础利率根据信用分计算，有抵押物减少1%
        String expression = "(credit >= 750 ? 0.04 : " +
                          "(credit >= 700 ? 0.05 : " +
                          "(credit >= 650 ? 0.06 : " +
                          "(credit >= 600 ? 0.07 : 0.08)))) " +
                          "- (collateral ? 0.01 : 0)";

        Object result = aviatorService.execute(expression, env);
        return ((Number) result).doubleValue();
    }

    /**
     * 用户等级评估
     * 根据消费金额和购买次数评估用户等级
     */
    public String evaluateUserLevel(Double totalSpent, Integer orderCount) {
        Map<String, Object> env = new HashMap<>();
        env.put("spent", totalSpent);
        env.put("orders", orderCount);

        String expression = "spent >= 10000 && orders >= 50 ? 'DIAMOND' : " +
                          "(spent >= 5000 && orders >= 30 ? 'GOLD' : " +
                          "(spent >= 2000 && orders >= 15 ? 'SILVER' : " +
                          "(spent >= 500 && orders >= 5 ? 'BRONZE' : 'NORMAL')))";

        Object result = aviatorService.execute(expression, env);
        return (String) result;
    }

    /**
     * 风险评分
     * 综合多个维度计算风险评分 (0-100)
     */
    public Integer calculateRiskScore(Integer age, Double income, Integer creditScore, 
                                     Double debtRatio, Boolean hasDefault) {
        Map<String, Object> env = new HashMap<>();
        env.put("age", age);
        env.put("income", income);
        env.put("credit", creditScore);
        env.put("debt", debtRatio);
        env.put("defaulted", hasDefault);

        // 风险评分公式（数值越高风险越低）
        String expression = "math.max(0, math.min(100, " +
                          "(credit / 10) + " +                    // 信用分占比
                          "(income / 1000) + " +                  // 收入占比
                          "(age >= 25 && age <= 55 ? 20 : 10) - " + // 年龄加分
                          "(debt * 30) - " +                      // 负债率扣分
                          "(defaulted ? 30 : 0)))";              // 违约记录扣分

        Object result = aviatorService.execute(expression, env);
        return ((Number) result).intValue();
    }

    /**
     * 优惠券发放规则
     * 根据用户行为决定发放什么优惠券
     */
    public String determineCouponType(Integer loginDays, Integer orderCount, 
                                     Double averageOrderValue, Boolean isNewUser) {
        Map<String, Object> env = new HashMap<>();
        env.put("days", loginDays);
        env.put("orders", orderCount);
        env.put("avgValue", averageOrderValue);
        env.put("newUser", isNewUser);

        String expression = "newUser ? 'WELCOME_COUPON' : " +
                          "(days >= 30 && orders >= 20 && avgValue >= 200 ? 'VIP_COUPON' : " +
                          "(days >= 15 && orders >= 10 ? 'REGULAR_COUPON' : " +
                          "(days >= 7 ? 'TRIAL_COUPON' : 'NO_COUPON')))";

        Object result = aviatorService.execute(expression, env);
        return (String) result;
    }

    /**
     * 考试成绩等级评定
     */
    public String evaluateGrade(Integer score) {
        Map<String, Object> env = new HashMap<>();
        env.put("score", score);

        String expression = "score >= 90 ? 'A' : " +
                          "(score >= 80 ? 'B' : " +
                          "(score >= 70 ? 'C' : " +
                          "(score >= 60 ? 'D' : 'F')))";

        Object result = aviatorService.execute(expression, env);
        return (String) result;
    }

    /**
     * 动态表达式执行
     * 允许前端传入自定义表达式
     */
    public Object executeCustomRule(String expression, Map<String, Object> variables) {
        logger.info("执行自定义规则: expression={}, variables={}", expression, variables);
        return aviatorService.execute(expression, variables);
    }
}

