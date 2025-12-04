package com.example.demo.service;

import com.googlecode.aviator.AviatorEvaluator;
import com.googlecode.aviator.Expression;
import com.googlecode.aviator.Options;
import com.googlecode.aviator.runtime.function.AbstractFunction;
import com.googlecode.aviator.runtime.function.FunctionUtils;
import com.googlecode.aviator.runtime.type.AviatorDouble;
import com.googlecode.aviator.runtime.type.AviatorObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.Map;

/**
 * Aviator 表达式引擎服务
 * 封装 Aviator 官方 API，提供统一的表达式编译和执行接口
 *
 * @author demo
 */
@Service
public class AviatorService {

    private static final Logger logger = LoggerFactory.getLogger(AviatorService.class);

    @PostConstruct
    public void init() {
        // 启用优化选项
        AviatorEvaluator.setOption(Options.ALWAYS_PARSE_FLOATING_POINT_NUMBER_INTO_DECIMAL, true);
        AviatorEvaluator.setOption(Options.ALWAYS_PARSE_INTEGRAL_NUMBER_INTO_DECIMAL, false);
        
        // 添加常用 math 函数
        registerMathFunctions();
        
        logger.info("AviatorService 初始化完成");
    }

    /**
     * 注册常用的 math 函数
     */
    private void registerMathFunctions() {
        // math.pow
        AviatorEvaluator.addFunction(new AbstractFunction() {
            @Override
            public String getName() {
                return "math.pow";
            }

            @Override
            public AviatorObject call(Map<String, Object> env, AviatorObject arg1, AviatorObject arg2) {
                Number base = FunctionUtils.getNumberValue(arg1, env);
                Number exponent = FunctionUtils.getNumberValue(arg2, env);
                double result = Math.pow(base.doubleValue(), exponent.doubleValue());
                return new AviatorDouble(result);
            }
        });

        // math.min
        AviatorEvaluator.addFunction(new AbstractFunction() {
            @Override
            public String getName() {
                return "math.min";
            }

            @Override
            public AviatorObject call(Map<String, Object> env, AviatorObject arg1, AviatorObject arg2) {
                Number num1 = FunctionUtils.getNumberValue(arg1, env);
                Number num2 = FunctionUtils.getNumberValue(arg2, env);
                double result = Math.min(num1.doubleValue(), num2.doubleValue());
                return new AviatorDouble(result);
            }
        });

        // min (without math. prefix)
        AviatorEvaluator.addFunction(new AbstractFunction() {
            @Override
            public String getName() {
                return "min";
            }

            @Override
            public AviatorObject call(Map<String, Object> env, AviatorObject arg1, AviatorObject arg2) {
                Number num1 = FunctionUtils.getNumberValue(arg1, env);
                Number num2 = FunctionUtils.getNumberValue(arg2, env);
                double result = Math.min(num1.doubleValue(), num2.doubleValue());
                return new AviatorDouble(result);
            }
        });

        // math.max
        AviatorEvaluator.addFunction(new AbstractFunction() {
            @Override
            public String getName() {
                return "math.max";
            }

            @Override
            public AviatorObject call(Map<String, Object> env, AviatorObject arg1, AviatorObject arg2) {
                Number num1 = FunctionUtils.getNumberValue(arg1, env);
                Number num2 = FunctionUtils.getNumberValue(arg2, env);
                double result = Math.max(num1.doubleValue(), num2.doubleValue());
                return new AviatorDouble(result);
            }
        });

        // math.pi (常量，通过函数形式提供)
        try {
            // 将 math.pi 作为变量添加到环境中
            // 由于这是个常量，我们在表达式中可以直接使用
        } catch (Exception e) {
            logger.warn("无法添加 math.pi", e);
        }

        logger.info("Math 函数注册完成: pow, min, max");
    }

    /**
     * 编译表达式
     */
    public Expression compile(String expression) {
        return AviatorEvaluator.compile(expression);
    }

    /**
     * 编译表达式（带缓存选项）
     */
    public Expression compile(String expression, boolean cached) {
        return AviatorEvaluator.compile(expression, cached);
    }

    /**
     * 执行表达式
     */
    public Object execute(String expression) {
        return AviatorEvaluator.execute(expression);
    }

    /**
     * 执行表达式（带变量）
     */
    public Object execute(String expression, Map<String, Object> env) {
        return AviatorEvaluator.execute(expression, env);
    }

    /**
     * 添加自定义函数
     */
    public void addFunction(AbstractFunction function) {
        AviatorEvaluator.addFunction(function);
    }

    /**
     * 移除自定义函数
     */
    public void removeFunction(String name) {
        AviatorEvaluator.removeFunction(name);
    }

    /**
     * 清除表达式缓存
     */
    public void clearExpressionCache() {
        AviatorEvaluator.clearExpressionCache();
    }
}

