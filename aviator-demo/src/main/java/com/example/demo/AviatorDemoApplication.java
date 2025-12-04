package com.example.demo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Aviator Demo 应用启动类
 *
 * @author demo
 */
@SpringBootApplication
public class AviatorDemoApplication {

    public static void main(String[] args) {
        SpringApplication.run(AviatorDemoApplication.class, args);
        System.out.println("\n===========================================");
        System.out.println("  Aviator Demo Application Started!");
        System.out.println("  Access: http://localhost:8080");
        System.out.println("  API Docs: See README.md");
        System.out.println("===========================================\n");
    }
}

