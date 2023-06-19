package com.ruoyi.common.enums;

/**
 * 材料类型/单位
 * 
 * @author ruoyi
 */
public enum MaterialType
{
    ZHI(0, "支"),
    JIAN(1, "件");

    private final Integer code;
    private final String info;

    MaterialType(Integer code, String info) {
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
