package com.ruoyi.system.domain;

public class StockOutInfo {
    String profileCode;
    Long changeQuantity;

    public String getProfileCode() {
        return profileCode;
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