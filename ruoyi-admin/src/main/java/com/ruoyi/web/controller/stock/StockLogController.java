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
import com.ruoyi.system.domain.StockLog;
import com.ruoyi.system.service.IStockLogService;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.ruoyi.common.core.page.TableDataInfo;

/**
 * 库存Controller
 * 
 * @author ruoyi
 * @date 2023-06-01
 */
@RestController
@RequestMapping("/stock/log")
public class StockLogController extends BaseController
{
    @Autowired
    private IStockLogService stockLogService;

    /**
     * 查询库存列表
     */
    @PreAuthorize("@ss.hasPermi('stock:log:list')")
    @GetMapping("/list")
    public TableDataInfo list(StockLog stockLog)
    {
        startPage();
        List<StockLog> list = stockLogService.selectStockLogList(stockLog);
        return getDataTable(list);
    }

    /**
     * 导出库存列表
     */
    @PreAuthorize("@ss.hasPermi('stock:log:export')")
    @Log(title = "库存", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, StockLog stockLog)
    {
        List<StockLog> list = stockLogService.selectStockLogList(stockLog);
        ExcelUtil<StockLog> util = new ExcelUtil<>(StockLog.class);
        util.exportExcel(response, list, "库存数据");
    }

    /**
     * 获取库存详细信息
     */
    @PreAuthorize("@ss.hasPermi('stock:log:query')")
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") Long id)
    {
        return success(stockLogService.selectStockLogById(id));
    }

    /**
     * 新增库存
     */
    @PreAuthorize("@ss.hasPermi('stock:log:add')")
    @Log(title = "库存", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody StockLog stockLog)
    {
        return toAjax(stockLogService.insertStockLog(stockLog));
    }

    /**
     * 修改库存
     */
    @PreAuthorize("@ss.hasPermi('stock:log:edit')")
    @Log(title = "库存", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody StockLog stockLog)
    {
        return toAjax(stockLogService.updateStockLog(stockLog));
    }

    /**
     * 删除库存
     */
    @PreAuthorize("@ss.hasPermi('stock:log:remove')")
    @Log(title = "库存", businessType = BusinessType.DELETE)
	@DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable Long[] ids)
    {
        return toAjax(stockLogService.deleteStockLogByIds(ids));
    }
}
