package com.example.demo.service;

import com.googlecode.aviator.Expression;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 公式管理服务
 * 演示如何管理和缓存常用公式
 *
 * @author demo
 */
@Service
public class FormulaService {

    private static final Logger logger = LoggerFactory.getLogger(FormulaService.class);

    @Autowired
    private AviatorService aviatorService;

    // 预编译的公式缓存
    private final Map<String, Expression> formulaCache = new ConcurrentHashMap<>();

    // 公式模板库
    private final Map<String, String> formulaTemplates = new HashMap<>();

    @PostConstruct
    public void init() {
        // 初始化常用公式模板
        formulaTemplates.put("discount", "price * (1 - rate)");
        formulaTemplates.put("tax", "amount * (1 + taxRate)");
        formulaTemplates.put("total_price", "price * quantity");
        formulaTemplates.put("final_price", "(price * quantity * (1 - discount) + shipping) * (1 + tax)");
        formulaTemplates.put("compound_interest", "principal * math.pow(1 + rate, periods)");
        formulaTemplates.put("bmi", "weight / math.pow(height / 100, 2)");
        formulaTemplates.put("age_from_year", "2024 - birthYear");
        formulaTemplates.put("circle_area", "3.141592653589793 * math.pow(radius, 2)");
        formulaTemplates.put("rectangle_area", "length * width");
        formulaTemplates.put("triangle_area", "base * height / 2");

        // 预编译常用公式
        logger.info("预编译常用公式...");
        for (Map.Entry<String, String> entry : formulaTemplates.entrySet()) {
            try {
                Expression exp = aviatorService.compile(entry.getValue(), true);
                formulaCache.put(entry.getKey(), exp);
                logger.info("公式编译成功: {} = {}", entry.getKey(), entry.getValue());
            } catch (Exception e) {
                logger.error("公式编译失败: {} = {}", entry.getKey(), entry.getValue(), e);
            }
        }
    }

    /**
     * 使用预编译公式计算
     */
    public Object calculateByFormula(String formulaName, Map<String, Object> params) {
        Expression expression = formulaCache.get(formulaName);
        if (expression == null) {
            throw new IllegalArgumentException("公式不存在: " + formulaName);
        }

        logger.debug("使用预编译公式计算: name={}, params={}", formulaName, params);
        return expression.execute(params);
    }

    /**
     * 获取所有可用的公式
     */
    public Map<String, String> getAllFormulas() {
        return new HashMap<>(formulaTemplates);
    }

    /**
     * 添加自定义公式
     */
    public void addFormula(String name, String expression) {
        formulaTemplates.put(name, expression);
        Expression exp = aviatorService.compile(expression, true);
        formulaCache.put(name, exp);
        logger.info("添加自定义公式: {} = {}", name, expression);
    }

    /**
     * 删除公式
     */
    public void removeFormula(String name) {
        formulaTemplates.remove(name);
        formulaCache.remove(name);
        logger.info("删除公式: {}", name);
    }

    /**
     * 批量计算
     */
    public Map<String, Object> batchCalculate(String formulaName, Map<String, Object>[] paramsList) {
        Map<String, Object> results = new HashMap<>();
        Expression expression = formulaCache.get(formulaName);
        
        if (expression == null) {
            throw new IllegalArgumentException("公式不存在: " + formulaName);
        }

        for (int i = 0; i < paramsList.length; i++) {
            Object result = expression.execute(paramsList[i]);
            results.put("result_" + i, result);
        }

        return results;
    }

    /**
     * 验证公式语法
     */
    public boolean validateFormula(String expression) {
        try {
            aviatorService.compile(expression);
            return true;
        } catch (Exception e) {
            logger.warn("公式语法错误: {}, error: {}", expression, e.getMessage());
            return false;
        }
    }
}

