package com.github.aviator.autoconfigure;

import com.github.aviator.service.AviatorService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Aviator 自动配置类
 * 
 * @author Aviator Starter
 */
@Configuration
@ConditionalOnClass(com.googlecode.aviator.AviatorEvaluator.class)
@ConditionalOnProperty(prefix = "aviator", name = "enable", havingValue = "true")
@EnableConfigurationProperties(AviatorProperties.class)
public class AviatorAutoConfiguration {

    private static final Logger logger = LoggerFactory.getLogger(AviatorAutoConfiguration.class);

    public AviatorAutoConfiguration() {
        logger.info("Aviator auto-configuration is being initialized...");
    }

    /**
     * 创建 AviatorService Bean
     * 
     * @param properties Aviator 配置属性
     * @return AviatorService 实例
     */
    @Bean
    @ConditionalOnMissingBean
    public AviatorService aviatorService(AviatorProperties properties) {
        logger.info("Creating AviatorService bean with properties: enable={}, cacheEnabled={}, cacheSize={}", 
            properties.isEnable(), properties.isCacheEnabled(), properties.getCacheSize());
        return new AviatorService(properties);
    }
}

