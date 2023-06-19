package com.ruoyi.system.service.impl;

import java.time.LocalDate;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

import com.ruoyi.common.exception.base.BaseException;
import com.ruoyi.common.utils.DateUtils;
import com.ruoyi.system.domain.Stock;
import com.ruoyi.system.mapper.StockMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ruoyi.system.mapper.StockLogMapper;
import com.ruoyi.system.domain.StockLog;
import com.ruoyi.system.service.IStockLogService;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

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

    public int insertStockLogs(List<StockLog> stockLogs)
    {
        return stockLogMapper.insertStockLogs(stockLogs);
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

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int deleteDraftById(Long id) {
        // 1. 删除 id (id 为 id的 fid 的也为id)
        // 2. 删除 fid = id
        int flag = stockLogMapper.deleteStockLogByFid(id);
        return flag;
    }

    @Override
    public int useDraftById(Long id) {
        // 1. 修改 id state = 2
        StockLog stockLog = new StockLog();
        stockLog.setId(id);
        // todo: 枚举
        stockLog.setState(2);
        stockLogMapper.updateStockLog(stockLog);
        // 2. 对库存做修改
        StockLog stockLog1 = new StockLog();
        stockLog1.setfId(id);
        // 查出待处理的 stockLogList
        List<StockLog> stockLogList = stockLogMapper.selectStockLogList(stockLog1);
        // 更新所有 fid 待处理的日志为已完成 state = 2
        stockLogMapper.updateStockLogs(id);
        if (CollectionUtils.isEmpty(stockLogList)) {
            throw new BaseException("参数错误");
        }
        List<String> profileCodeList = stockLogList.stream().map(StockLog::getProfileCode).collect(Collectors.toList());
        // 查出需要修改的库存 Stock
        List<Stock> stockList = stockMapper.selectStockByProfileCodes(profileCodeList);

        // 组装出 stockLogList Map
        Map<String, Long> profileCodeChangeMap = stockLogList.stream().collect(Collectors.toMap(StockLog::getProfileCode, StockLog::getChangeQuantity));
        stockList.forEach(item -> {
            if (item == null) {
                return;
            }
            Long changeQuantity = profileCodeChangeMap.get(item.getProfileCode());
            item.setQuantity(item.getQuantity() - changeQuantity);
            item.setTotalWeight(item.getQuantity() * item.getWeight());
        });
        // 批量更新
        stockList = stockList.stream().filter(Objects::nonNull).collect(Collectors.toList());
        return stockMapper.batchUpdateStock(stockList);
    }

    /**
     * 获取当前的单号
     * @return 返回一个单号
     */
    @Override
    public String getOrder() {
        // 获取当前日期
        LocalDate currentDate = LocalDate.now();

        // 获取年份后两位
        int yearLastTwoDigits = currentDate.getYear() % 100;

        // 获取月份和日期
        int month = currentDate.getMonthValue();
        int day = currentDate.getDayOfMonth();

        // 将年份后两位、月份和日期拼接为字符串
        String dateString = "GR" + String.format("%02d", yearLastTwoDigits) + String.format("%02d", month) + String.format("%02d", day);

        // 获取是第几单
        Date currentTime = new Date();
        Long num = stockLogMapper.selectOrder(currentTime);
        return dateString + String.format("%03d", num);
    }
}
