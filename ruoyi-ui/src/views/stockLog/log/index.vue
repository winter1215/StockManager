<template>
  <div class="app-container">
    <el-form :model="queryParams" ref="queryForm" size="small" :inline="true" v-show="showSearch" label-width="auto">
      <el-form-item label="型材编码" prop="profileCode">
        <el-input v-model="queryParams.profileCode" placeholder="请输入型材编码" clearable @keyup.enter.native="handleQuery" />
      </el-form-item>
      <el-form-item label="日期">
            <el-date-picker
              v-model="dateRange"
              style="width: 240px"
              value-format="yyyy-MM-dd"
              type="daterange"
              range-separator="-"
              start-placeholder="开始日期"
              end-placeholder="结束日期"
            ></el-date-picker>
          </el-form-item>
      <el-form-item label="颜色" prop="color">
        <el-input v-model="queryParams.color" placeholder="请输入颜色" clearable @keyup.enter.native="handleQuery" />
      </el-form-item>
      <el-form-item label="操作前数量" prop="quantity">
        <el-input v-model="queryParams.quantity" placeholder="请输入操作前数量" clearable @keyup.enter.native="handleQuery" />
      </el-form-item>
      <el-form-item label="变更数量" prop="changeQuantity">
        <el-input v-model="queryParams.changeQuantity" placeholder="请输入变更数量" clearable
          @keyup.enter.native="handleQuery" />
      </el-form-item>
      <el-form-item label="重量(kg)" prop="weight">
        <el-input v-model="queryParams.weight" placeholder="请输入重量" clearable @keyup.enter.native="handleQuery" />
      </el-form-item>
      <el-form-item label="型材名称" prop="profileName">
        <el-input v-model="queryParams.profileName" placeholder="请输入型材名称" clearable @keyup.enter.native="handleQuery" />
      </el-form-item>
      <el-form-item label="长度" prop="length">
        <el-input v-model="queryParams.length" placeholder="请输入长度" clearable @keyup.enter.native="handleQuery" />
      </el-form-item>
      <el-form-item label="厚度" prop="thickness">
        <el-input v-model="queryParams.thickness" placeholder="请输入厚度" clearable @keyup.enter.native="handleQuery" />
      </el-form-item>
      <el-form-item label="进货单价" prop="price">
        <el-input v-model="queryParams.price" placeholder="请输入型材进货单价" clearable @keyup.enter.native="handleQuery" />
      </el-form-item>
      <el-form-item>
        <el-button type="primary" icon="el-icon-search" size="mini" @click="handleQuery">搜索</el-button>
        <el-button icon="el-icon-refresh" size="mini" @click="resetQuery">重置</el-button>
      </el-form-item>
    </el-form>

    <el-row :gutter="10" class="mb8">
      <el-col :span="1.5">
        <el-button size="mini" type="success" @click="handleLogType(0)">进货日志</el-button>
        <el-button size="mini" type="success" @click="handleLogType(1)">出货日志</el-button>
        <el-button size="mini" type="success" @click="handleLogType(3)">操作日志</el-button>
        <el-button size="mini" type="success" @click="handleLogType(4)">删除日志</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button type="warning" plain icon="el-icon-download" size="mini" @click="handleExport"
          v-hasPermi="['stock:log:export']">导出</el-button>
      </el-col>
      <right-toolbar :showSearch.sync="showSearch" @queryTable="getList"></right-toolbar>
    </el-row>

    <el-table v-loading="loading" :data="logList" @selection-change="handleSelectionChange">
      <el-table-column type="selection" width="55" align="center" />
      <el-table-column label="序号" type="index" width="50" />
      <el-table-column label="日期" align="center" prop="createTime" />
      <el-table-column label="型材编码" align="center" prop="profileCode" />
      <el-table-column label="型材名称" align="center" prop="profileName" />
      <el-table-column label="颜色" align="center" prop="color" />
      <el-table-column label="操作前数量" align="center" prop="quantity" />
      <el-table-column label="变更数量" align="center" prop="changeQuantity" />
      <el-table-column label="长度" align="center" prop="length" />
      <el-table-column label="厚度" align="center" prop="thickness" />
      <el-table-column label="单位" align="center" prop="materialType">
        <template slot-scope="scope">
          {{ scope.row.materialType === 0 ? "支" : "件"}}
        </template>
      </el-table-column>
      <el-table-column label="进货单价" align="center" prop="price" />
      <el-table-column label="重量(kg)" align="center" prop="weight" />
      <el-table-column label="日志类型" align="center" prop="logType">
        <template slot-scope="scope">
          <el-tag v-if="scope.row.logType === 0">进货</el-tag>
          <el-tag v-else-if="scope.row.logType === 1" type="success">出货</el-tag>
          <el-tag v-else-if="scope.row.logType === 2" type="info">补货</el-tag>
          <el-tag v-else-if="scope.row.logType === 3" type="warning">用户操作</el-tag>
          <el-tag v-else-if="scope.row.logType === 4" type="danger">删除操作</el-tag>
        </template>
      </el-table-column>
      <el-table-column v-if="logType === 1" label="处理状态" align="center" prop="state">
        <template slot-scope="scope">
          <el-tag v-if="scope.row.state === 0" type="info">未处理</el-tag>
          <el-tag v-else-if="scope.row.state === 1" type="info">未处理</el-tag>
          <el-tag v-else-if="scope.row.state === 2" >已处理</el-tag>
        </template>
      </el-table-column>
    </el-table>

    <pagination v-show="total > 0" :total="total" :page.sync="queryParams.pageNum" :limit.sync="queryParams.pageSize"
      @pagination="getList" />
  </div>
</template>

<script>
import { listLog, listByLogType, delLog } from "@/api/stock/log";

export default {
  name: "Log",
  data() {
    return {
      dateRange: [],
      // 遮罩层
      loading: true,
      // 选中数组
      ids: [],
      // 非单个禁用
      single: true,
      // 非多个禁用
      multiple: true,
      // 显示搜索条件
      showSearch: true,
      // logTye
      logType: undefined,
      // 总条数
      total: 0,
      // 库存表格数据
      logList: [],
      // 弹出层标题
      title: "",
      // 是否显示弹出层
      open: false,
      // 查询参数
      queryParams: {
        pageNum: 1,
        pageSize: 10,
        profileCode: null,
        color: null,
        quantity: null,
        changeQuantity: null,
        weight: null,
        profileName: null,
        length: null,
        thickness: null,
        materialType: null,
        price: null,
        logType: null,
      },
      // 表单参数
      form: {},
      // 表单校验
      rules: {
        profileCode: [
          { required: true, message: "型材编码不能为空", trigger: "blur" }
        ],
        color: [
          { required: true, message: "颜色不能为空", trigger: "blur" }
        ],
        quantity: [
          { required: true, message: "操作前数量不能为空", trigger: "blur" }
        ],
        changeQuantity: [
          { required: true, message: "变更数量不能为空", trigger: "blur" }
        ],
        weight: [
          { required: true, message: "重量不能为空", trigger: "blur" }
        ],
        profileName: [
          { required: true, message: "型材名称不能为空", trigger: "blur" }
        ],
        length: [
          { required: true, message: "长度不能为空", trigger: "blur" }
        ],
        thickness: [
          { required: true, message: "厚度不能为空", trigger: "blur" }
        ],
        materialType: [
          { required: true, message: "材料类型：0 -&gt; 铝材(支), 1 -&gt; 配件(件)不能为空", trigger: "change" }
        ],
        price: [
          { required: true, message: "型材进货单价不能为空", trigger: "blur" }
        ],
        logType: [
          { required: true, message: "日志类型: 0 -&gt; 进货, 1 -&gt; 出货, 2 -&gt; 补货, 3 -&gt; 用户操作不能为空", trigger: "change" }
        ],
      }
    };
  },
  created() {
    this.getList();
  },
  methods: {
    /** 查询库存列表 */
    getList() {
      this.loading = true;
      listLog(this.addDateRange(this.queryParams, this.dateRange)).then(response => {
        this.logList = response.rows;
        this.total = response.total;
        this.loading = false;
      });
    },
    // 取消按钮
    cancel() {
      this.open = false;
      this.reset();
    },
    // 表单重置
    reset() {
      this.form = {
        id: null,
        profileCode: null,
        color: null,
        quantity: null,
        changeQuantity: null,
        weight: null,
        profileName: null,
        length: null,
        thickness: null,
        materialType: null,
        price: null,
        logType: null,
        createTime: null,
        updateTime: null,
        isDelete: null
      };
      this.resetForm("form");
    },
    /** 搜索按钮操作 */
    handleQuery() {
      this.queryParams.pageNum = 1;
      this.getList()
    },
    /** 重置按钮操作 */
    resetQuery() {
      this.resetForm("queryForm");
      // 清空时间选择(dataRange 不在 paramsQuery 中)
      this.dateRange = [];
      this.handleQuery();
    },
    // 多选框选中数据
    handleSelectionChange(selection) {
      this.ids = selection.map(item => item.id)
      this.single = selection.length !== 1
      this.multiple = !selection.length
    },
    /** 删除按钮操作 */
    handleDelete(row) {
      const ids = row.id || this.ids;
      this.$modal.confirm('是否确认删除库存编号为"' + ids + '"的数据项？').then(function () {
        return delLog(ids);
      }).then(() => {
        this.getList();
        this.$modal.msgSuccess("删除成功");
      }).catch(() => { });
    },
    /** 查出不同日志类型的日志 */
    async handleLogType(logType = 0) {
      this.logType = logType
      this.queryParams.logType = logType
      this.loading = true;
      const res = await listByLogType(logType);
      this.logList = res.rows;
      this.total = res.total;
      this.loading = false;
    },


    /** 导出按钮操作 */
    handleExport() {
      this.download('stock/log/export', {
        ...this.queryParams
      }, `log_${new Date().getTime()}.xlsx`)
    }
  }
};
</script>
