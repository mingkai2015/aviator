package com.github.aviator.autoconfigure;

import com.github.aviator.service.AviatorService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;

import static org.junit.Assert.*;

/**
 * AviatorAutoConfiguration 测试类
 * 
 * @author Aviator Starter
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = AviatorAutoConfigurationTest.TestApplication.class)
@TestPropertySource(properties = {
    "aviator.enable=true",
    "aviator.cache-enabled=true",
    "aviator.cache-size=500"
})
public class AviatorAutoConfigurationTest {

    @Autowired
    private ApplicationContext applicationContext;

    @Test
    public void testAviatorServiceBeanExists() {
        assertTrue(applicationContext.containsBean("aviatorService"));
    }

    @Test
    public void testAviatorServiceAutowired() {
        AviatorService aviatorService = applicationContext.getBean(AviatorService.class);
        assertNotNull(aviatorService);
    }

    @Test
    public void testAviatorPropertiesLoaded() {
        AviatorProperties properties = applicationContext.getBean(AviatorProperties.class);
        assertNotNull(properties);
        assertTrue(properties.isEnable());
        assertTrue(properties.isCacheEnabled());
        assertEquals(500, properties.getCacheSize());
    }

    @Test
    public void testAviatorServiceFunctionality() {
        AviatorService aviatorService = applicationContext.getBean(AviatorService.class);
        Object result = aviatorService.execute("10 + 20");
        assertEquals(30L, result);
    }

    @SpringBootApplication
    static class TestApplication {
    }
}

