package com.github.aviator.service;

import com.github.aviator.autoconfigure.AviatorProperties;
import org.junit.Before;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.*;

/**
 * AviatorService 测试类
 * 
 * @author Aviator Starter
 */
public class AviatorServiceTest {

    private AviatorService aviatorService;

    @Before
    public void setUp() {
        AviatorProperties properties = new AviatorProperties();
        properties.setEnable(true);
        properties.setCacheEnabled(true);
        properties.setCacheSize(100);
        properties.setOptimizeEnabled(true);
        
        aviatorService = new AviatorService(properties);
        aviatorService.init();
    }

    @Test
    public void testSimpleCalculation() {
        Object result = aviatorService.execute("1 + 2 + 3");
        assertEquals(6L, result);
    }

    @Test
    public void testCalculationWithVariables() {
        Map<String, Object> env = new HashMap<>();
        env.put("a", 10);
        env.put("b", 20);
        
        Object result = aviatorService.execute("a + b", env);
        assertEquals(30L, result);
    }

    @Test
    public void testMultiplication() {
        Map<String, Object> env = new HashMap<>();
        env.put("x", 5);
        env.put("y", 3);
        
        Object result = aviatorService.execute("x * y", env);
        assertEquals(15L, result);
    }

    @Test
    public void testStringConcatenation() {
        Map<String, Object> env = new HashMap<>();
        env.put("name", "Aviator");
        
        Object result = aviatorService.execute("'Hello, ' + name + '!'", env);
        assertEquals("Hello, Aviator!", result);
    }

    @Test
    public void testConditionalExpression() {
        Map<String, Object> env = new HashMap<>();
        env.put("score", 85);
        
        Object result = aviatorService.execute("score >= 60 ? 'pass' : 'fail'", env);
        assertEquals("pass", result);
    }

    @Test
    public void testComparisonOperators() {
        Map<String, Object> env = new HashMap<>();
        env.put("age", 25);
        
        Object result1 = aviatorService.execute("age > 18", env);
        assertTrue((Boolean) result1);
        
        Object result2 = aviatorService.execute("age < 30", env);
        assertTrue((Boolean) result2);
        
        Object result3 = aviatorService.execute("age == 25", env);
        assertTrue((Boolean) result3);
    }

    @Test
    public void testLogicalOperators() {
        Map<String, Object> env = new HashMap<>();
        env.put("x", true);
        env.put("y", false);
        
        Object result1 = aviatorService.execute("x && y", env);
        assertFalse((Boolean) result1);
        
        Object result2 = aviatorService.execute("x || y", env);
        assertTrue((Boolean) result2);
        
        Object result3 = aviatorService.execute("!y", env);
        assertTrue((Boolean) result3);
    }

    @Test
    public void testCompileExpression() {
        com.googlecode.aviator.Expression exp = aviatorService.compile("a * b + c");
        assertNotNull(exp);
        
        Map<String, Object> env = new HashMap<>();
        env.put("a", 2);
        env.put("b", 3);
        env.put("c", 4);
        
        Object result = exp.execute(env);
        assertEquals(10L, result);
    }

    @Test
    public void testComplexExpression() {
        Map<String, Object> env = new HashMap<>();
        env.put("price", 100.0);
        env.put("quantity", 5);
        env.put("discount", 0.1);
        
        Object result = aviatorService.execute("price * quantity * (1 - discount)", env);
        assertEquals(450.0, result);
    }

    @Test(expected = RuntimeException.class)
    public void testInvalidExpression() {
        aviatorService.execute("invalid expression @@#$");
    }
}

