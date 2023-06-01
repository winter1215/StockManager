package com.ruoyi.common.enums;

/**
 * 0 -&gt; 进货, 1 -&gt; 出货, 2 -&gt; 补货, 3 -&gt; 用户操作
 * 
 * @author ruoyi
 */
public enum StockLogType
{

    STOCKING(0, "进货"),
    SHIP(1, "出货"),
    RESTOCK(2, "补货"),
    USER_OPS(3, "用户操作");
    private final Integer code;
    private final String info;

    StockLogType(Integer code, String info) {
        this.code = code;
        this.info = info;
    }

    public Integer getCode() {
        return code;
    }

    public String getInfo() {
        return info;
    }
}
