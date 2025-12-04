package com.example.demo.config;

import com.example.demo.service.AviatorService;
import com.googlecode.aviator.runtime.function.AbstractFunction;
import com.googlecode.aviator.runtime.function.FunctionUtils;
import com.googlecode.aviator.runtime.type.AviatorBoolean;
import com.googlecode.aviator.runtime.type.AviatorDouble;
import com.googlecode.aviator.runtime.type.AviatorObject;
import com.googlecode.aviator.runtime.type.AviatorString;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;

import javax.annotation.PostConstruct;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;

/**
 * 自定义 Aviator 函数配置
 * 扩展 Aviator 引擎的功能
 *
 * @author demo
 */
@Configuration
public class CustomAviatorFunctions {

    private static final Logger logger = LoggerFactory.getLogger(CustomAviatorFunctions.class);

    @Autowired
    private AviatorService aviatorService;

    @PostConstruct
    public void registerCustomFunctions() {
        logger.info("注册自定义 Aviator 函数...");

        // 1. 平方函数
        aviatorService.addFunction(new AbstractFunction() {
            @Override
            public String getName() {
                return "square";
            }

            @Override
            public AviatorObject call(Map<String, Object> env, AviatorObject arg1) {
                Number num = FunctionUtils.getNumberValue(arg1, env);
                double value = num.doubleValue();
                return new AviatorDouble(value * value);
            }
        });

        // 2. 立方函数
        aviatorService.addFunction(new AbstractFunction() {
            @Override
            public String getName() {
                return "cube";
            }

            @Override
            public AviatorObject call(Map<String, Object> env, AviatorObject arg1) {
                Number num = FunctionUtils.getNumberValue(arg1, env);
                double value = num.doubleValue();
                return new AviatorDouble(value * value * value);
            }
        });

        // 3. 判断奇偶数
        aviatorService.addFunction(new AbstractFunction() {
            @Override
            public String getName() {
                return "isEven";
            }

            @Override
            public AviatorObject call(Map<String, Object> env, AviatorObject arg1) {
                Number num = FunctionUtils.getNumberValue(arg1, env);
                return AviatorBoolean.valueOf(num.intValue() % 2 == 0);
            }
        });

        // 4. 字符串反转
        aviatorService.addFunction(new AbstractFunction() {
            @Override
            public String getName() {
                return "reverse";
            }

            @Override
            public AviatorObject call(Map<String, Object> env, AviatorObject arg1) {
                String str = FunctionUtils.getStringValue(arg1, env);
                String reversed = new StringBuilder(str).reverse().toString();
                return new AviatorString(reversed);
            }
        });

        // 5. 首字母大写
        aviatorService.addFunction(new AbstractFunction() {
            @Override
            public String getName() {
                return "capitalize";
            }

            @Override
            public AviatorObject call(Map<String, Object> env, AviatorObject arg1) {
                String str = FunctionUtils.getStringValue(arg1, env);
                if (str == null || str.isEmpty()) {
                    return new AviatorString("");
                }
                String capitalized = str.substring(0, 1).toUpperCase() + str.substring(1);
                return new AviatorString(capitalized);
            }
        });

        // 6. 格式化当前日期
        aviatorService.addFunction(new AbstractFunction() {
            @Override
            public String getName() {
                return "formatNow";
            }

            @Override
            public AviatorObject call(Map<String, Object> env, AviatorObject arg1) {
                String format = FunctionUtils.getStringValue(arg1, env);
                SimpleDateFormat sdf = new SimpleDateFormat(format);
                return new AviatorString(sdf.format(new Date()));
            }
        });

        // 7. 范围检查函数
        aviatorService.addFunction(new AbstractFunction() {
            @Override
            public String getName() {
                return "inRange";
            }

            @Override
            public AviatorObject call(Map<String, Object> env, 
                                     AviatorObject arg1, 
                                     AviatorObject arg2, 
                                     AviatorObject arg3) {
                Number value = FunctionUtils.getNumberValue(arg1, env);
                Number min = FunctionUtils.getNumberValue(arg2, env);
                Number max = FunctionUtils.getNumberValue(arg3, env);
                
                double v = value.doubleValue();
                double minVal = min.doubleValue();
                double maxVal = max.doubleValue();
                
                return AviatorBoolean.valueOf(v >= minVal && v <= maxVal);
            }
        });

        // 8. 百分比格式化
        aviatorService.addFunction(new AbstractFunction() {
            @Override
            public String getName() {
                return "toPercent";
            }

            @Override
            public AviatorObject call(Map<String, Object> env, AviatorObject arg1) {
                Number num = FunctionUtils.getNumberValue(arg1, env);
                double value = num.doubleValue();
                String percent = String.format("%.2f%%", value * 100);
                return new AviatorString(percent);
            }
        });

        // 9. 保留小数位
        aviatorService.addFunction(new AbstractFunction() {
            @Override
            public String getName() {
                return "round";
            }

            @Override
            public AviatorObject call(Map<String, Object> env, 
                                     AviatorObject arg1, 
                                     AviatorObject arg2) {
                Number num = FunctionUtils.getNumberValue(arg1, env);
                Number digits = FunctionUtils.getNumberValue(arg2, env);
                
                double value = num.doubleValue();
                int scale = digits.intValue();
                double factor = Math.pow(10, scale);
                double rounded = Math.round(value * factor) / factor;
                
                return new AviatorDouble(rounded);
            }
        });

        // 10. 计算折扣后价格（常用业务函数）
        aviatorService.addFunction(new AbstractFunction() {
            @Override
            public String getName() {
                return "applyDiscount";
            }

            @Override
            public AviatorObject call(Map<String, Object> env, 
                                     AviatorObject arg1, 
                                     AviatorObject arg2) {
                Number price = FunctionUtils.getNumberValue(arg1, env);
                Number discount = FunctionUtils.getNumberValue(arg2, env);
                
                double finalPrice = price.doubleValue() * (1 - discount.doubleValue());
                return new AviatorDouble(finalPrice);
            }
        });

        logger.info("自定义函数注册完成！共注册 10 个函数");
    }
}

