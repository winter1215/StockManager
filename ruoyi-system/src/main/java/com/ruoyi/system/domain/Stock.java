package com.ruoyi.system.domain;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.ruoyi.common.annotation.Excel;
import com.ruoyi.common.core.domain.BaseEntity;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;

/**
 * 库存对象 stock
 * 
 * @author ruoyi
 * @date 2023-05-31
 */
public class Stock extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 主键 id */
    private Long id;

    /** 型材编码 */
    @Excel(name = "型材编码")
    @NotBlank(message = "型材编码不能为空")
    private String profileCode;

    /** 颜色 */
    @Excel(name = "颜色")
    private String color;

    /** 数量 */
    @Excel(name = "数量")
    @Min(value = 0, message = "数量不合法")
    private Long quantity;

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

    /** 材料类型：0 -> 铝材(支), 1 -> 配件(件) */
    @Excel(name = "单位", readConverterExp = "0=支,1=件")
    private Integer materialType;

    /** 型材进货单价 */
    @Excel(name = "型材进货单价")
    private Long price;

    /** 逻辑删除: 0 -> 未删除, 1 -> 删除 */
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
            .append("weight", getWeight())
            .append("profileName", getProfileName())
            .append("length", getLength())
            .append("thickness", getThickness())
            .append("MaterialType", getMaterialType())
            .append("price", getPrice())
            .append("createTime", getCreateTime())
            .append("updateTime", getUpdateTime())
            .append("isDelete", getIsDelete())
            .toString();
    }
}
