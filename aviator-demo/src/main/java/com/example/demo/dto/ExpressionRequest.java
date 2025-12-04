package com.example.demo.dto;

import java.util.Map;

/**
 * 表达式执行请求 DTO
 *
 * @author demo
 */
public class ExpressionRequest {
    
    private String expression;
    private Map<String, Object> variables;

    public ExpressionRequest() {
    }

    public ExpressionRequest(String expression, Map<String, Object> variables) {
        this.expression = expression;
        this.variables = variables;
    }

    public String getExpression() {
        return expression;
    }

    public void setExpression(String expression) {
        this.expression = expression;
    }

    public Map<String, Object> getVariables() {
        return variables;
    }

    public void setVariables(Map<String, Object> variables) {
        this.variables = variables;
    }

    @Override
    public String toString() {
        return "ExpressionRequest{" +
                "expression='" + expression + '\'' +
                ", variables=" + variables +
                '}';
    }
}

