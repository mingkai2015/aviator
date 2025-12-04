package com.example.demo.controller;

import com.example.demo.dto.ApiResponse;
import com.example.demo.dto.ExpressionRequest;
import com.example.demo.model.LoanApplication;
import com.example.demo.service.RuleEngineService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * 规则引擎控制器
 *
 * @author demo
 */
@RestController
@RequestMapping("/api/rule")
public class RuleController {

    @Autowired
    private RuleEngineService ruleService;

    /**
     * 评估贷款资格
     * POST /api/rule/loan/evaluate
     */
    @PostMapping("/loan/evaluate")
    public ApiResponse<Boolean> evaluateLoan(@RequestBody LoanApplication application) {
        try {
            boolean eligible = ruleService.evaluateLoanEligibility(application);
            return ApiResponse.success(eligible ? "符合贷款条件" : "不符合贷款条件", eligible);
        } catch (Exception e) {
            return ApiResponse.error(e.getMessage());
        }
    }

    /**
     * 计算贷款利率
     * GET /api/rule/loan/rate?credit=700&collateral=true
     */
    @GetMapping("/loan/rate")
    public ApiResponse<Double> calculateLoanRate(
            @RequestParam Integer credit,
            @RequestParam(defaultValue = "false") Boolean collateral) {
        try {
            Double rate = ruleService.calculateLoanRate(credit, collateral);
            return ApiResponse.success(rate);
        } catch (Exception e) {
            return ApiResponse.error(e.getMessage());
        }
    }

    /**
     * 评估用户等级
     * GET /api/rule/user/level?spent=6000&orders=35
     */
    @GetMapping("/user/level")
    public ApiResponse<String> evaluateUserLevel(
            @RequestParam Double spent,
            @RequestParam Integer orders) {
        try {
            String level = ruleService.evaluateUserLevel(spent, orders);
            return ApiResponse.success(level);
        } catch (Exception e) {
            return ApiResponse.error(e.getMessage());
        }
    }

    /**
     * 计算风险评分
     * GET /api/rule/risk/score?age=35&income=10000&credit=720&debt=0.3&defaulted=false
     */
    @GetMapping("/risk/score")
    public ApiResponse<Integer> calculateRiskScore(
            @RequestParam Integer age,
            @RequestParam Double income,
            @RequestParam Integer credit,
            @RequestParam Double debt,
            @RequestParam(defaultValue = "false") Boolean defaulted) {
        try {
            Integer score = ruleService.calculateRiskScore(age, income, credit, debt, defaulted);
            return ApiResponse.success(score);
        } catch (Exception e) {
            return ApiResponse.error(e.getMessage());
        }
    }

    /**
     * 优惠券发放规则
     * GET /api/rule/coupon?days=25&orders=15&avgValue=180&newUser=false
     */
    @GetMapping("/coupon")
    public ApiResponse<String> determineCoupon(
            @RequestParam Integer days,
            @RequestParam Integer orders,
            @RequestParam Double avgValue,
            @RequestParam(defaultValue = "false") Boolean newUser) {
        try {
            String couponType = ruleService.determineCouponType(days, orders, avgValue, newUser);
            return ApiResponse.success(couponType);
        } catch (Exception e) {
            return ApiResponse.error(e.getMessage());
        }
    }

    /**
     * 成绩等级评定
     * GET /api/rule/grade?score=85
     */
    @GetMapping("/grade")
    public ApiResponse<String> evaluateGrade(@RequestParam Integer score) {
        try {
            String grade = ruleService.evaluateGrade(score);
            return ApiResponse.success(grade);
        } catch (Exception e) {
            return ApiResponse.error(e.getMessage());
        }
    }

    /**
     * 执行自定义规则
     * POST /api/rule/custom
     * Body: {"expression": "age > 18 && score >= 60", "variables": {"age": 20, "score": 75}}
     */
    @PostMapping("/custom")
    public ApiResponse<Object> executeCustomRule(@RequestBody ExpressionRequest request) {
        try {
            Object result = ruleService.executeCustomRule(
                request.getExpression(), 
                request.getVariables()
            );
            return ApiResponse.success(result);
        } catch (Exception e) {
            return ApiResponse.error(e.getMessage());
        }
    }
}

