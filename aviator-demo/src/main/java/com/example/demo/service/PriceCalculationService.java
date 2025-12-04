package com.example.demo.service;

import com.example.demo.model.PriceCalculation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

/**
 * 价格计算服务
 * 演示如何使用 Aviator 进行复杂的价格计算
 *
 * @author demo
 */
@Service
public class PriceCalculationService {

    private static final Logger logger = LoggerFactory.getLogger(PriceCalculationService.class);

    @Autowired
    private AviatorService aviatorService;

    /**
     * 计算最终价格
     * 公式: (原价 * 数量 * (1 - 折扣率) + 运费) * (1 + 税率)
     */
    public Double calculateFinalPrice(PriceCalculation calculation) {
        Map<String, Object> env = new HashMap<>();
        env.put("price", calculation.getOriginalPrice());
        env.put("quantity", calculation.getQuantity());
        env.put("discount", calculation.getDiscountRate());
        env.put("shipping", calculation.getShippingFee());
        env.put("tax", calculation.getTaxRate());

        String expression = "(price * quantity * (1 - discount) + shipping) * (1 + tax)";
        logger.info("计算价格: expression={}, params={}", expression, env);

        Object result = aviatorService.execute(expression, env);
        return ((Number) result).doubleValue();
    }

    /**
     * 计算折扣后价格
     */
    public Double calculateDiscountPrice(Double originalPrice, Double discountRate) {
        Map<String, Object> env = new HashMap<>();
        env.put("price", originalPrice);
        env.put("discount", discountRate);

        Object result = aviatorService.execute("price * (1 - discount)", env);
        return ((Number) result).doubleValue();
    }

    /**
     * 根据会员等级计算折扣
     * VIP等级越高，折扣越大
     */
    public Double calculateVipDiscount(Double basePrice, Integer vipLevel) {
        Map<String, Object> env = new HashMap<>();
        env.put("price", basePrice);
        env.put("level", vipLevel);

        // VIP 折扣: 基础价格 * (1 - 等级 * 0.05)，最高8折
        String expression = "price * (1 - min(level * 0.05, 0.2))";
        Object result = aviatorService.execute(expression, env);
        return ((Number) result).doubleValue();
    }

    /**
     * 计算满减优惠
     * 满100减10，满200减25，满500减80
     */
    public Double calculateFullReduction(Double totalAmount) {
        Map<String, Object> env = new HashMap<>();
        env.put("amount", totalAmount);

        String expression = "amount >= 500 ? amount - 80 : " +
                          "(amount >= 200 ? amount - 25 : " +
                          "(amount >= 100 ? amount - 10 : amount))";
        
        Object result = aviatorService.execute(expression, env);
        return ((Number) result).doubleValue();
    }

    /**
     * 计算阶梯价格
     * 数量越多，单价越低
     */
    public Double calculateTieredPrice(Integer quantity, Double unitPrice) {
        Map<String, Object> env = new HashMap<>();
        env.put("qty", quantity);
        env.put("price", unitPrice);

        // 1-10件: 原价, 11-50件: 9折, 51-100件: 8折, 100+: 7折
        String expression = "qty <= 10 ? qty * price : " +
                          "(qty <= 50 ? qty * price * 0.9 : " +
                          "(qty <= 100 ? qty * price * 0.8 : " +
                          "qty * price * 0.7))";
        
        Object result = aviatorService.execute(expression, env);
        return ((Number) result).doubleValue();
    }

    /**
     * 计算复利
     * 用于金融计算：本金 * (1 + 利率) ^ 期数
     */
    public Double calculateCompoundInterest(Double principal, Double rate, Integer periods) {
        Map<String, Object> env = new HashMap<>();
        env.put("principal", principal);
        env.put("rate", rate);
        env.put("periods", periods);

        String expression = "principal * math.pow(1 + rate, periods)";
        Object result = aviatorService.execute(expression, env);
        return ((Number) result).doubleValue();
    }
}

