package com.ruoyi.web.controller.stock;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import javax.servlet.http.HttpServletResponse;

import com.ruoyi.common.exception.base.BaseException;
import com.ruoyi.system.domain.StockLog;
import com.ruoyi.system.domain.StockOutInfo;
import com.ruoyi.system.domain.dto.StockListDto;
import com.ruoyi.system.domain.dto.StockOutDto;
import com.ruoyi.system.service.IStockLogService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.BeanUtils;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.CollectionUtils;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import com.ruoyi.common.annotation.Log;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.enums.BusinessType;
import com.ruoyi.system.domain.Stock;
import com.ruoyi.system.service.IStockService;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.ruoyi.common.core.page.TableDataInfo;

/**
 * 库存Controller
 * 
 * @author ruoyi
 * @date 2023-05-31
 */
@Api("库存管理")
@RestController
@RequestMapping("/stock/stock")
public class StockController extends BaseController
{
    @Autowired
    private IStockService stockService;

    @Autowired
    private IStockLogService stockLogService;

    /**
     * 查询库存列表
     */
    @PreAuthorize("@ss.hasPermi('stock:stock:list')")
    @GetMapping("/list")
    public AjaxResult list(Stock stock)
    {
        startPage();
        List<Stock> list = stockService.selectStockList(stock);
        float totalWeight = stockService.selectTotalWeight();
        TableDataInfo dataTable = getDataTable(list);
        StockListDto listDto = new StockListDto();
        listDto.setDataTable(dataTable);
        listDto.setTotalWeight(totalWeight);
        return success(listDto);
    }

    /**
     * 导出库存列表
     */
    @PreAuthorize("@ss.hasPermi('stock:stock:export')")
    @Log(title = "库存", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, Stock stock, Long[] ids)
    {
        // 导出清单中的数据
        List<Stock> list;
        if (ids != null && ids.length != 0) {
            list = stockService.selectStockListByIds(ids);
        } else {
            // 条件
            list = stockService.selectStockList(stock);
        }
        ExcelUtil<Stock> util = new ExcelUtil<>(Stock.class);
        util.exportExcel(response, list, "库存数据");
    }

    /**
     * 获取库存详细信息
     */
    @PreAuthorize("@ss.hasPermi('stock:stock:query')")
    @ApiOperation("获取库存详细详细接口")
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") Long id)
    {
        return success(stockService.selectStockById(id));
    }

    @PreAuthorize("@ss.hasPermi('stock:stock:query')")
    @GetMapping(value = "/getByCode")
    public AjaxResult getInfoByProfileCode(@RequestParam String profileCode)
    {
        return success(stockService.selectStockByProfileCode(profileCode));
    }

    /**
     * 新增库存
     */
    @PreAuthorize("@ss.hasPermi('stock:stock:add')")
    @ApiOperation("进货接口")
    @PostMapping
    public AjaxResult add(@Validated @RequestBody Stock stock)
    {

        return toAjax(stockService.insertStock(stock));
    }

    /**
     * 修改库存
     */
    @PreAuthorize("@ss.hasPermi('stock:stock:edit')")
    @ApiOperation("修改库存接口")
    @PutMapping
    public AjaxResult edit(@RequestBody Stock stock)
    {
        return toAjax(stockService.updateStock(stock));
    }

    /**
     * 删除库存
     */
    @PreAuthorize("@ss.hasPermi('stock:stock:remove')")
    @ApiOperation("删除库存接口")
    @DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable Long[] ids)
    {
        return toAjax(stockService.deleteStockByIds(ids));
    }

    /**
     * 出货库存
     * @param
     * @return
     */
    @PreAuthorize("@ss.hasPermi('stock:stock:out')")
    @ApiOperation("打印出货接口")
    @PostMapping("/out")
    public AjaxResult out(@RequestBody StockOutDto stockOutDto)
    {
        // 传入数据里面存了型材编码 和 出货的数量 以及 出货的状态
        List<StockOutInfo> stockOutInfos = stockOutDto.getStockOutInfoList();
        Integer state = stockOutDto.getState();

        // format
        List<String> profileCodeList = stockOutInfos.stream()
                .map(StockOutInfo::getProfileCode)
                .collect(Collectors.toList());
        List<Long> changeQuantityList = stockOutInfos.stream()
                .map(StockOutInfo::getChangeQuantity)
                .collect(Collectors.toList());
        List<Float> changeWeightList = stockOutInfos.stream()
                .map(StockOutInfo::getChangeWeight)
                .collect(Collectors.toList());
        List<Float> changePriceList = stockOutInfos.stream()
                .map(StockOutInfo::getChangePrice)
                .collect(Collectors.toList());
        // 1. 用型材编码查到对应的stock记录
        List<Stock> stockList = stockService.selectStockByProfileCodes(profileCodeList);

        // 遇到 null 抛出异常，说明该批数据有错
        stockList.forEach(item -> {
            if (item == null) {
                throw new BaseException("参数错误，请刷新重试");
            }
        });

        if (CollectionUtils.isEmpty(stockList) || state == null) {
            throw new BaseException("参数错误");
        }
        // 数量校验
        for (int i = 0; i < stockList.size(); i++) {
            Long quantity = stockList.get(i).getQuantity();
            Long changeQuantity = changeQuantityList.get(i);
            if (changeQuantity < 0 || changeQuantity > quantity) {
                throw new BaseException("参数错误，请刷新重试");
            }
        }

        StockLog head = new StockLog();
        BeanUtils.copyProperties(stockList.get(0), head);
        head.setId(0L);
        head.setfId(0L);
        head.setChangeQuantity(changeQuantityList.get(0));
        head.setWeight(changeWeightList.get(0) == null ? 0 : changeWeightList.get(0));
        head.setPrice(changePriceList.get(0) == null ? 0 : changePriceList.get(0));
        head.setState(1);
        head.setLogType(1);
        stockLogService.insertStockLog(head);
        Long fId = head.getId();
        head.setfId(fId);
        stockLogService.updateStockLog(head);
        // 2.用查到的stock和出货数量和出货状态构造stockLog
        List<StockLog> stockLogList = new ArrayList<>();
        for (int i = 1; i < stockList.size(); i ++  ) {
            StockLog stockLog = new StockLog();
            BeanUtils.copyProperties(stockList.get(i), stockLog);
            stockLog.setfId(fId);
            stockLog.setChangeQuantity(changeQuantityList.get(i));
            stockLog.setWeight(changeWeightList.get(i) == null ? 0 : changeWeightList.get(i));
            stockLog.setPrice(changePriceList.get(i) == null ? 0 : changePriceList.get(i));
            stockLog.setState(0);
            stockLog.setLogType(1);
            stockLogList.add(stockLog);
        }
        // 判空 防止 mapper 报错
        if(!CollectionUtils.isEmpty(stockLogList)) {
            // 3.批量更新stockLog
            stockLogService.insertStockLogs(stockLogList);
        }
        // 4.判断状态决定是否批量更新stock
        // 1 表示待确定，2 表示已确定
        if (state == 2) {
            // 更新stock
            List<Stock> updatedStockList = new ArrayList<>();
            for (int i = 0; i < stockList.size(); i ++ ) {
                Stock stock = stockList.get(i);
                stock.setQuantity(stock.getQuantity() - changeQuantityList.get(i));
                // 商家计算重量误差，若出库后重量为负数，默认将重量置为 0
                float newWeight = (stock.getWeight() - changeWeightList.get(i));
                stock.setWeight(newWeight < 0 ? 0 : newWeight);
                updatedStockList.add(stock);
            }
            return toAjax(stockService.updateStocks(updatedStockList));
        }
        return toAjax(true);
    }
}