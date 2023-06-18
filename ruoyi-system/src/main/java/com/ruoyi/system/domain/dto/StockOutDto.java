package com.ruoyi.system.domain.dto;

import com.ruoyi.system.domain.StockOutInfo;

import java.util.List;

public class StockOutDto {

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

    @Override
    public String toString() {
        return "StockOutDto{" +
                "stockOutInfoList=" + stockOutInfoList +
                ", state=" + state +
                '}';
    }
}
