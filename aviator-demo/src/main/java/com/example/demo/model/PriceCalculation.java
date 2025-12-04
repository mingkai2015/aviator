package com.example.demo.model;

/**
 * 价格计算模型
 *
 * @author demo
 */
public class PriceCalculation {
    
    private Double originalPrice;
    private Integer quantity;
    private Double discountRate;
    private Double shippingFee;
    private Double taxRate;

    public PriceCalculation() {
    }

    public PriceCalculation(Double originalPrice, Integer quantity, Double discountRate, 
                           Double shippingFee, Double taxRate) {
        this.originalPrice = originalPrice;
        this.quantity = quantity;
        this.discountRate = discountRate;
        this.shippingFee = shippingFee;
        this.taxRate = taxRate;
    }

    public Double getOriginalPrice() {
        return originalPrice;
    }

    public void setOriginalPrice(Double originalPrice) {
        this.originalPrice = originalPrice;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    public Double getDiscountRate() {
        return discountRate;
    }

    public void setDiscountRate(Double discountRate) {
        this.discountRate = discountRate;
    }

    public Double getShippingFee() {
        return shippingFee;
    }

    public void setShippingFee(Double shippingFee) {
        this.shippingFee = shippingFee;
    }

    public Double getTaxRate() {
        return taxRate;
    }

    public void setTaxRate(Double taxRate) {
        this.taxRate = taxRate;
    }
}

