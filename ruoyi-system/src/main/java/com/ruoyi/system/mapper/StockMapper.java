package com.ruoyi.system.mapper;

import java.util.List;
import com.ruoyi.system.domain.Stock;

/**
 * 库存Mapper接口
 * 
 * @author ruoyi
 * @date 2023-05-31
 */
public interface StockMapper 
{
    /**
     * 查询库存
     * 
     * @param id 库存主键
     * @return 库存
     */
    public Stock selectStockById(Long id);

    public List<Stock> selectStockByIds(Long[] ids);
    /**
     * 查询库存列表
     * 
     * @param stock 库存
     * @return 库存集合
     */
    public List<Stock> selectStockList(Stock stock);

    /**
     * 新增库存
     * 
     * @param stock 库存
     * @return 结果
     */
    public int insertStock(Stock stock);

    /**
     * 修改库存
     * 
     * @param stock 库存
     * @return 结果
     */
    public int updateStock(Stock stock);

    public int updateStockQuantity(Stock stock);

    /**
     * 删除库存
     * 
     * @param id 库存主键
     * @return 结果
     */
    public int deleteStockById(Long id);

    /**
     * 批量删除库存
     * 
     * @param ids 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteStockByIds(Long[] ids);

    Stock selectStockByProfileCode(String profileCode);

    List<Stock> selectStockByProfileCodes(List<String> profileCodeList);

    int batchUpdateStock(List<Stock> stockList);
}
