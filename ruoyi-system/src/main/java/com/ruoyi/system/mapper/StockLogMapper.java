package com.ruoyi.system.mapper;

import java.util.List;
import com.ruoyi.system.domain.StockLog;

/**
 * 库存Mapper接口
 * 
 * @author ruoyi
 * @date 2023-06-01
 */
public interface StockLogMapper 
{
    /**
     * 查询库存
     * 
     * @param id 库存主键
     * @return 库存
     */
    public StockLog selectStockLogById(Long id);

    /**
     * 查询库存列表
     * 
     * @param stockLog 库存
     * @return 库存集合
     */
    public List<StockLog> selectStockLogList(StockLog stockLog);

    /**
     * 新增库存
     * 
     * @param stockLog 库存
     * @return 结果
     */
    public int insertStockLog(StockLog stockLog);

    /**
     * 修改库存
     * 
     * @param stockLog 库存
     * @return 结果
     */
    public int updateStockLog(StockLog stockLog);

    /**
     * 删除库存
     * 
     * @param id 库存主键
     * @return 结果
     */
    public int deleteStockLogById(Long id);

    /**
     * 批量删除库存
     * 
     * @param ids 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteStockLogByIds(Long[] ids);
}
