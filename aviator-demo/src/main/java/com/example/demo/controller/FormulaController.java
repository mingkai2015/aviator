package com.example.demo.controller;

import com.example.demo.dto.ApiResponse;
import com.example.demo.service.FormulaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

/**
 * 公式管理控制器
 *
 * @author demo
 */
@RestController
@RequestMapping("/api/formula")
public class FormulaController {

    @Autowired
    private FormulaService formulaService;

    /**
     * 获取所有可用公式
     * GET /api/formula/list
     */
    @GetMapping("/list")
    public ApiResponse<Map<String, String>> getAllFormulas() {
        try {
            Map<String, String> formulas = formulaService.getAllFormulas();
            return ApiResponse.success(formulas);
        } catch (Exception e) {
            return ApiResponse.error(e.getMessage());
        }
    }

    /**
     * 使用预定义公式计算
     * POST /api/formula/calculate/{name}
     * Body: {"price": 100, "rate": 0.2}
     */
    @PostMapping("/calculate/{name}")
    public ApiResponse<Object> calculateByFormula(
            @PathVariable String name,
            @RequestBody Map<String, Object> params) {
        try {
            Object result = formulaService.calculateByFormula(name, params);
            return ApiResponse.success(result);
        } catch (Exception e) {
            return ApiResponse.error(e.getMessage());
        }
    }

    /**
     * 添加自定义公式
     * POST /api/formula/add?name=myFormula&expression=a+b*2
     */
    @PostMapping("/add")
    public ApiResponse<String> addFormula(
            @RequestParam String name,
            @RequestParam String expression) {
        try {
            formulaService.addFormula(name, expression);
            return ApiResponse.success("公式添加成功", name);
        } catch (Exception e) {
            return ApiResponse.error(e.getMessage());
        }
    }

    /**
     * 删除公式
     * DELETE /api/formula/{name}
     */
    @DeleteMapping("/{name}")
    public ApiResponse<String> removeFormula(@PathVariable String name) {
        try {
            formulaService.removeFormula(name);
            return ApiResponse.success("公式删除成功", name);
        } catch (Exception e) {
            return ApiResponse.error(e.getMessage());
        }
    }

    /**
     * 验证公式语法
     * GET /api/formula/validate?expression=a+b*2
     */
    @GetMapping("/validate")
    public ApiResponse<Boolean> validateFormula(@RequestParam String expression) {
        try {
            boolean valid = formulaService.validateFormula(expression);
            return ApiResponse.success(valid ? "公式语法正确" : "公式语法错误", valid);
        } catch (Exception e) {
            return ApiResponse.error(e.getMessage());
        }
    }
}

