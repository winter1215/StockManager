package com.ruoyi.web.controller.stock;

import java.util.List;
import javax.servlet.http.HttpServletResponse;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
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
    public void export(HttpServletResponse response, Stock stock)
    {
        List<Stock> list = stockService.selectStockList(stock);
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

    /**
     * 新增库存
     */
    @PreAuthorize("@ss.hasPermi('stock:stock:add')")
    @Log(title = "库存", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody Stock stock)
    {
        return toAjax(stockService.insertStock(stock));
    }

    /**
     * 修改库存
     */
    @PreAuthorize("@ss.hasPermi('stock:stock:edit')")
    @Log(title = "库存", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody Stock stock)
    {
        return toAjax(stockService.updateStock(stock));
    }

    /**
     * 删除库存
     */
    @PreAuthorize("@ss.hasPermi('stock:stock:remove')")
    @Log(title = "库存", businessType = BusinessType.DELETE)
    @DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable Long[] ids)
    {
        return toAjax(stockService.deleteStockByIds(ids));
    }
}