package com.ruoyi.system.service.impl;

import java.util.List;
import com.ruoyi.common.enums.StockLogType;
import com.ruoyi.common.utils.DateUtils;
import com.ruoyi.system.domain.StockLog;
import com.ruoyi.system.mapper.StockLogMapper;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ruoyi.system.mapper.StockMapper;
import com.ruoyi.system.domain.Stock;
import com.ruoyi.system.service.IStockService;
import org.springframework.transaction.annotation.Transactional;

/**
 * 库存Service业务层处理
 * 
 * @author ruoyi
 * @date 2023-05-31
 */
@Service
public class StockServiceImpl implements IStockService 
{
    @Autowired
    private StockMapper stockMapper;

    @Autowired
    private StockLogMapper stockLogMapper;

    /**
     * 查询库存
     * 
     * @param id 库存主键
     * @return 库存
     */
    @Override
    public Stock selectStockById(Long id)
    {
        return stockMapper.selectStockById(id);
    }

    /**
     * 查询库存列表
     * 
     * @param stock 库存
     * @return 库存
     */
    @Override
    public List<Stock> selectStockList(Stock stock)
    {
        return stockMapper.selectStockList(stock);
    }

    /**
     * 新增库存
     * 
     * @param stock 库存
     * @return 结果
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public int insertStock(Stock stock)
    {
        // 1. 数据的校验

        // 2. 插入到 stock 表
        String profileCode = stock.getProfileCode();
        Stock preStock = stockMapper.selectStockByProfileCode(profileCode);

        // 不存在则新增,存在则合并
        Long changeQuantity = stock.getQuantity();
        Long preStockQuantity = 0L;
        if (preStock != null) {
            preStockQuantity = preStock.getQuantity();
            Long newQuantity = preStockQuantity + changeQuantity;
            stock.setQuantity(newQuantity);
            stockMapper.updateStockQuantity(stock);
        } else {
            stockMapper.insertStock(stock);
        }

        // 插入到 stockLog
        StockLog stockLog = new StockLog();
        stockLog.setQuantity(preStockQuantity);
        stockLog.setChangeQuantity(changeQuantity);
        stockLog.setLogType(StockLogType.STOCKING.getCode());
        BeanUtils.copyProperties(stock, stockLog);
        return stockLogMapper.insertStockLog(stockLog);
    }

    /**
     * 修改库存
     * 
     * @param stock 库存
     * @return 结果
     */
    @Override
    public int updateStock(Stock stock)
    {
        stock.setUpdateTime(DateUtils.getNowDate());
        return stockMapper.updateStock(stock);
    }

    /**
     * 批量删除库存
     * 
     * @param ids 需要删除的库存主键
     * @return 结果
     */
    @Override
    public int deleteStockByIds(Long[] ids)
    {
        return stockMapper.deleteStockByIds(ids);
    }

    /**
     * 删除库存信息
     * 
     * @param id 库存主键
     * @return 结果
     */
    @Override
    public int deleteStockById(Long id)
    {
        return stockMapper.deleteStockById(id);
    }

    @Override
    public Stock selectStockByProfileCode(String profileCode) {
        return stockMapper.selectStockByProfileCode(profileCode);
    }
}
