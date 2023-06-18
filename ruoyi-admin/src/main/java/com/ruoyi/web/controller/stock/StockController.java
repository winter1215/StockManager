package com.ruoyi.web.controller.stock;

import java.util.List;
import javax.servlet.http.HttpServletResponse;

import com.ruoyi.system.domain.dto.StockOutDto;
import io.swagger.annotations.ApiOperation;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.beans.factory.annotation.Autowired;
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
@RestController
@RequestMapping("/stock/stock")
public class StockController extends BaseController
{
    @Autowired
    private IStockService stockService;

    /**
     * 查询库存列表
     */
    @PreAuthorize("@ss.hasPermi('stock:stock:list')")
    @GetMapping("/list")
    public TableDataInfo list(Stock stock)
    {
        startPage();
        List<Stock> list = stockService.selectStockList(stock);
        return getDataTable(list);
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
    @PutMapping
    public AjaxResult edit(@RequestBody Stock stock)
    {
        return toAjax(stockService.updateStock(stock));
    }

    /**
     * 删除库存
     */
    @PreAuthorize("@ss.hasPermi('stock:stock:remove')")
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
    @PostMapping("/out")
    public AjaxResult out(@RequestBody List<StockOutDto> stockOutDtoList)
    {
        // 传入数据里面存了型材编码 和 出货的数量 以及 出货的状态
        // 1. 用型材编码查到对应的stock记录
        // 2.用查到的stock和出货数量和出货状态构造stockLog
        // 3.判断状态决定是否批量更新stock
        // 4.批量更新stockLog
        return toAjax(true);
    }
}