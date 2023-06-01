package com.ruoyi.system.service.impl;

import java.util.List;
import com.ruoyi.common.utils.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ruoyi.system.mapper.StockLogMapper;
import com.ruoyi.system.domain.StockLog;
import com.ruoyi.system.service.IStockLogService;

/**
 * 库存Service业务层处理
 * 
 * @author ruoyi
 * @date 2023-06-01
 */
@Service
public class StockLogServiceImpl implements IStockLogService 
{
    @Autowired
    private StockLogMapper stockLogMapper;

    /**
     * 查询库存
     * 
     * @param id 库存主键
     * @return 库存
     */
    @Override
    public StockLog selectStockLogById(Long id)
    {
        return stockLogMapper.selectStockLogById(id);
    }

    /**
     * 查询库存列表
     * 
     * @param stockLog 库存
     * @return 库存
     */
    @Override
    public List<StockLog> selectStockLogList(StockLog stockLog)
    {
        return stockLogMapper.selectStockLogList(stockLog);
    }

    /**
     * 新增库存
     * 
     * @param stockLog 库存
     * @return 结果
     */
    @Override
    public int insertStockLog(StockLog stockLog)
    {
        stockLog.setCreateTime(DateUtils.getNowDate());
        return stockLogMapper.insertStockLog(stockLog);
    }

    /**
     * 修改库存
     * 
     * @param stockLog 库存
     * @return 结果
     */
    @Override
    public int updateStockLog(StockLog stockLog)
    {
        stockLog.setUpdateTime(DateUtils.getNowDate());
        return stockLogMapper.updateStockLog(stockLog);
    }

    /**
     * 批量删除库存
     * 
     * @param ids 需要删除的库存主键
     * @return 结果
     */
    @Override
    public int deleteStockLogByIds(Long[] ids)
    {
        return stockLogMapper.deleteStockLogByIds(ids);
    }

    /**
     * 删除库存信息
     * 
     * @param id 库存主键
     * @return 结果
     */
    @Override
    public int deleteStockLogById(Long id)
    {
        return stockLogMapper.deleteStockLogById(id);
    }
}
