package com.github.aviator.service;

import com.googlecode.aviator.AviatorEvaluator;
import com.googlecode.aviator.Expression;
import com.googlecode.aviator.Options;
import com.github.aviator.autoconfigure.AviatorProperties;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.PostConstruct;
import java.util.Map;

/**
 * Aviator 服务类，提供动态公式计算功能
 * 
 * @author Aviator Starter
 */
public class AviatorService {

    private static final Logger logger = LoggerFactory.getLogger(AviatorService.class);

    private final AviatorProperties properties;

    public AviatorService(AviatorProperties properties) {
        this.properties = properties;
    }

    @PostConstruct
    public void init() {
        // 配置 Aviator 引擎
        if (properties.isCacheEnabled()) {
            AviatorEvaluator.getInstance().useLRUExpressionCache(properties.getCacheSize());
            logger.info("Aviator expression cache enabled, cache size: {}", properties.getCacheSize());
        }

        AviatorEvaluator.setOption(Options.OPTIMIZE_LEVEL, 
            properties.isOptimizeEnabled() ? AviatorEvaluator.EVAL : AviatorEvaluator.COMPILE);
        
        AviatorEvaluator.setOption(Options.TRACE_EVAL, properties.isTraceEnabled());

        logger.info("Aviator service initialized successfully");
    }

    /**
     * 执行表达式，使用默认环境
     * 
     * @param expression 表达式字符串
     * @return 执行结果
     */
    public Object execute(String expression) {
        return execute(expression, null);
    }

    /**
     * 执行表达式，使用给定的环境变量
     * 
     * @param expression 表达式字符串
     * @param env 环境变量 Map
     * @return 执行结果
     */
    public Object execute(String expression, Map<String, Object> env) {
        try {
            if (env == null || env.isEmpty()) {
                return AviatorEvaluator.execute(expression);
            } else {
                return AviatorEvaluator.execute(expression, env);
            }
        } catch (Exception e) {
            logger.error("Error executing expression: {}", expression, e);
            throw new RuntimeException("Failed to execute expression: " + expression, e);
        }
    }

    /**
     * 编译表达式
     * 
     * @param expression 表达式字符串
     * @return 编译后的表达式对象
     */
    public Expression compile(String expression) {
        try {
            return AviatorEvaluator.compile(expression);
        } catch (Exception e) {
            logger.error("Error compiling expression: {}", expression, e);
            throw new RuntimeException("Failed to compile expression: " + expression, e);
        }
    }

    /**
     * 编译并缓存表达式
     * 
     * @param expression 表达式字符串
     * @param cached 是否缓存
     * @return 编译后的表达式对象
     */
    public Expression compile(String expression, boolean cached) {
        try {
            return AviatorEvaluator.compile(expression, cached);
        } catch (Exception e) {
            logger.error("Error compiling expression: {}", expression, e);
            throw new RuntimeException("Failed to compile expression: " + expression, e);
        }
    }

    /**
     * 清除表达式缓存
     */
    public void clearExpressionCache() {
        AviatorEvaluator.clearExpressionCache();
        logger.info("Aviator expression cache cleared");
    }

    /**
     * 添加函数到 Aviator 引擎
     * 
     * @param function Aviator 自定义函数
     */
    public void addFunction(com.googlecode.aviator.runtime.function.AbstractFunction function) {
        AviatorEvaluator.addFunction(function);
        logger.info("Added custom function: {}", function.getName());
    }

    /**
     * 移除函数
     * 
     * @param functionName 函数名称
     */
    public void removeFunction(String functionName) {
        AviatorEvaluator.removeFunction(functionName);
        logger.info("Removed custom function: {}", functionName);
    }

    /**
     * 设置 Aviator 选项
     * 
     * @param option 选项
     * @param value 值
     */
    public void setOption(Options option, Object value) {
        AviatorEvaluator.setOption(option, value);
        logger.debug("Set Aviator option: {} = {}", option, value);
    }
}

