package com.ruoyi.system.domain.dto;

import com.ruoyi.common.core.page.TableDataInfo;

public class StockListDto {
    TableDataInfo dataTable;

    Float totalWeight;

    public TableDataInfo getDataTable() {
        return dataTable;
    }

    public void setDataTable(TableDataInfo dataTable) {
        this.dataTable = dataTable;
    }

    public Float getTotalWeight() {
        return totalWeight;
    }

    public void setTotalWeight(Float totalWeight) {
        this.totalWeight = totalWeight;
    }
}
