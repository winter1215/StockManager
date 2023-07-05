package com.ruoyi.system.domain;

public class StockOutInfo {
    String profileCode;
    Long changeQuantity;
    /**
    * 更改的重量
    */
    Float changeWeight;
    /**
    * 出货单价
    */
    Float changePrice;


    public Float getChangeWeight() {
        return changeWeight;
    }

    public void setChangeWeight(Float changeWeight) {
        this.changeWeight = changeWeight;
    }

    public String getProfileCode() {
        return profileCode;
    }

    public Float getChangePrice() {
        return changePrice;
    }

    public void setChangePrice(Float changePrice) {
        this.changePrice = changePrice;
    }

    public void setProfileCode(String profileCode) {
        this.profileCode = profileCode;
    }

    public Long getChangeQuantity() {
        return changeQuantity;
    }

    public void setChangeQuantity(Long changeQuantity) {
        this.changeQuantity = changeQuantity;
    }

    @Override
    public String toString() {
        return "StockOutInfo{" +
                "profileCode='" + profileCode + '\'' +
                ", changeQuantity=" + changeQuantity +
                '}';
    }
}