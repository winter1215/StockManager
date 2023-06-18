package com.ruoyi.system.domain.dto;

import java.util.List;

public class StockOutDto {
    class StockOutInfo {
        String profileCode;
        Integer changeQuantity;

        public String getProfileCode() {
            return profileCode;
        }

        public void setProfileCode(String profileCode) {
            this.profileCode = profileCode;
        }

        public Integer getChangeQuantity() {
            return changeQuantity;
        }

        public void setChangeQuantity(Integer changeQuantity) {
            this.changeQuantity = changeQuantity;
        }
    }

    /** 封装的出货List*/
    List<StockOutInfo> stockOutInfoList;
    /** 该批次出货的状态*/
    Integer state;


    public List<StockOutInfo> getStockOutInfoList() {
        return stockOutInfoList;
    }

    public void setStockOutInfoList(List<StockOutInfo> stockOutInfoList) {
        this.stockOutInfoList = stockOutInfoList;
    }

    public Integer getState() {
        return state;
    }

    public void setState(Integer state) {
        this.state = state;
    }
}
