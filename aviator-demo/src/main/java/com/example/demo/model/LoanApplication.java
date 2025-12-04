package com.example.demo.model;

/**
 * 贷款申请模型
 *
 * @author demo
 */
public class LoanApplication {
    
    private Integer age;
    private Double monthlyIncome;
    private Double loanAmount;
    private Integer creditScore;
    private Boolean hasCollateral;
    private Integer employmentYears;

    public LoanApplication() {
    }

    public LoanApplication(Integer age, Double monthlyIncome, Double loanAmount, 
                          Integer creditScore, Boolean hasCollateral, Integer employmentYears) {
        this.age = age;
        this.monthlyIncome = monthlyIncome;
        this.loanAmount = loanAmount;
        this.creditScore = creditScore;
        this.hasCollateral = hasCollateral;
        this.employmentYears = employmentYears;
    }

    public Integer getAge() {
        return age;
    }

    public void setAge(Integer age) {
        this.age = age;
    }

    public Double getMonthlyIncome() {
        return monthlyIncome;
    }

    public void setMonthlyIncome(Double monthlyIncome) {
        this.monthlyIncome = monthlyIncome;
    }

    public Double getLoanAmount() {
        return loanAmount;
    }

    public void setLoanAmount(Double loanAmount) {
        this.loanAmount = loanAmount;
    }

    public Integer getCreditScore() {
        return creditScore;
    }

    public void setCreditScore(Integer creditScore) {
        this.creditScore = creditScore;
    }

    public Boolean getHasCollateral() {
        return hasCollateral;
    }

    public void setHasCollateral(Boolean hasCollateral) {
        this.hasCollateral = hasCollateral;
    }

    public Integer getEmploymentYears() {
        return employmentYears;
    }

    public void setEmploymentYears(Integer employmentYears) {
        this.employmentYears = employmentYears;
    }
}

