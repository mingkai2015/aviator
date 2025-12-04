package com.github.aviator.autoconfigure;

import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * Aviator 配置属性类
 * 
 * @author Aviator Starter
 */
@ConfigurationProperties(prefix = "aviator")
public class AviatorProperties {

    /**
     * 是否启用 Aviator 自动配置，默认为 false
     */
    private boolean enable = false;

    /**
     * 是否开启表达式编译缓存，默认为 true
     */
    private boolean cacheEnabled = true;

    /**
     * 表达式编译缓存大小，默认 1000
     */
    private int cacheSize = 1000;

    /**
     * 是否优化表达式执行，默认为 true
     */
    private boolean optimizeEnabled = true;

    /**
     * 是否允许追踪求值过程，默认为 false
     */
    private boolean traceEnabled = false;

    public boolean isEnable() {
        return enable;
    }

    public void setEnable(boolean enable) {
        this.enable = enable;
    }

    public boolean isCacheEnabled() {
        return cacheEnabled;
    }

    public void setCacheEnabled(boolean cacheEnabled) {
        this.cacheEnabled = cacheEnabled;
    }

    public int getCacheSize() {
        return cacheSize;
    }

    public void setCacheSize(int cacheSize) {
        this.cacheSize = cacheSize;
    }

    public boolean isOptimizeEnabled() {
        return optimizeEnabled;
    }

    public void setOptimizeEnabled(boolean optimizeEnabled) {
        this.optimizeEnabled = optimizeEnabled;
    }

    public boolean isTraceEnabled() {
        return traceEnabled;
    }

    public void setTraceEnabled(boolean traceEnabled) {
        this.traceEnabled = traceEnabled;
    }
}

