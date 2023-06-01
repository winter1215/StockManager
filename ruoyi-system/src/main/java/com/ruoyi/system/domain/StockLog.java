package com.ruoyi.system.domain;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.ruoyi.common.annotation.Excel;
import com.ruoyi.common.core.domain.BaseEntity;

/**
 * 库存对象 stock_log
 * 
 * @author ruoyi
 * @date 2023-06-01
 */
public class StockLog extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 主键 id */
    private Long id;

    /** 型材编码 */
    @Excel(name = "型材编码")
    private String profileCode;

    /** 颜色 */
    @Excel(name = "颜色")
    private String color;

    /** 操作前数量 */
    @Excel(name = "操作前数量")
    private Long quantity;

    /** 变更数量 */
    @Excel(name = "变更数量")
    private Long changeQuantity;

    /** 重量 */
    @Excel(name = "重量")
    private Long weight;

    /** 型材名称 */
    @Excel(name = "型材名称")
    private String profileName;

    /** 长度 */
    @Excel(name = "长度")
    private Long length;

    /** 厚度 */
    @Excel(name = "厚度")
    private Long thickness;

    /** 材料类型：0 -&gt; 铝材(支), 1 -&gt; 配件(件) */
    @Excel(name = "单位", readConverterExp = "0=支,1=件")
    private Integer materialType;

    /** 型材进货单价 */
    @Excel(name = "型材进货单价")
    private Long price;

    /** 日志类型: 0 -&gt; 进货, 1 -&gt; 出货, 2 -&gt; 补货, 3 -&gt; 用户操作 */
    @Excel(name = "日志类型", readConverterExp = "0=进货,1=出货,2=补货,3=用户操作")
    private Integer logType;

    /** 逻辑删除: 0 -&gt; 未删除, 1 -&gt; 删除 */
    private Integer isDelete;

    public void setId(Long id)
    {
        this.id = id;
    }

    public Long getId()
    {
        return id;
    }
    public void setProfileCode(String profileCode)
    {
        this.profileCode = profileCode;
    }

    public String getProfileCode()
    {
        return profileCode;
    }
    public void setColor(String color)
    {
        this.color = color;
    }

    public String getColor()
    {
        return color;
    }
    public void setQuantity(Long quantity)
    {
        this.quantity = quantity;
    }

    public Long getQuantity()
    {
        return quantity;
    }
    public void setChangeQuantity(Long changeQuantity)
    {
        this.changeQuantity = changeQuantity;
    }

    public Long getChangeQuantity()
    {
        return changeQuantity;
    }
    public void setWeight(Long weight)
    {
        this.weight = weight;
    }

    public Long getWeight()
    {
        return weight;
    }
    public void setProfileName(String profileName)
    {
        this.profileName = profileName;
    }

    public String getProfileName()
    {
        return profileName;
    }
    public void setLength(Long length)
    {
        this.length = length;
    }

    public Long getLength()
    {
        return length;
    }
    public void setThickness(Long thickness)
    {
        this.thickness = thickness;
    }

    public Long getThickness()
    {
        return thickness;
    }
    public void setMaterialType(Integer materialType)
    {
        this.materialType = materialType;
    }

    public Integer getMaterialType()
    {
        return materialType;
    }
    public void setPrice(Long price)
    {
        this.price = price;
    }

    public Long getPrice()
    {
        return price;
    }
    public void setLogType(Integer logType)
    {
        this.logType = logType;
    }

    public Integer getLogType()
    {
        return logType;
    }
    public void setIsDelete(Integer isDelete)
    {
        this.isDelete = isDelete;
    }

    public Integer getIsDelete()
    {
        return isDelete;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
            .append("id", getId())
            .append("profileCode", getProfileCode())
            .append("color", getColor())
            .append("quantity", getQuantity())
            .append("changeQuantity", getChangeQuantity())
            .append("weight", getWeight())
            .append("profileName", getProfileName())
            .append("length", getLength())
            .append("thickness", getThickness())
            .append("MaterialType", getMaterialType())
            .append("price", getPrice())
            .append("logType", getLogType())
            .append("createTime", getCreateTime())
            .append("updateTime", getUpdateTime())
            .append("isDelete", getIsDelete())
            .toString();
    }
}
