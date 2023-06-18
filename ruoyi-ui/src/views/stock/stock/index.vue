<template>
  <div class="app-container">
    <el-form :model="queryParams" ref="queryForm" size="small" :inline="true" v-show="showSearch" label-width="68px">
      <el-form-item label="型材编码" prop="profileCode">
        <el-input
          v-model="queryParams.profileCode"
          placeholder="请输入型材编码"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="颜色" prop="color">
        <el-input
          v-model="queryParams.color"
          placeholder="请输入颜色"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="数量" prop="quantity">
        <el-input
          v-model="queryParams.quantity"
          placeholder="请输入数量"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="重量(kg)" prop="weight">
        <el-input
          v-model="queryParams.weight"
          placeholder="请输入重量(kg)"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="型材名称" prop="profileName">
        <el-input
          v-model="queryParams.profileName"
          placeholder="请输入型材名称"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="长度" prop="length">
        <el-input
          v-model="queryParams.length"
          placeholder="请输入长度"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="厚度" prop="thickness">
        <el-input
          v-model="queryParams.thickness"
          placeholder="请输入厚度"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="单位" prop="materialType">
        <el-select v-model="queryParams.materialType" placeholder="请选择单位">
          <el-option label="支" :value="0"></el-option>
          <el-option label="件" :value="1"></el-option>
        </el-select>
      </el-form-item>
      <el-form-item label="进货单价" prop="price">
        <el-input
          v-model="queryParams.price"
          placeholder="请输入型材进货单价"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item>
        <el-button type="primary" icon="el-icon-search" size="mini" @click="handleQuery">搜索</el-button>
        <el-button icon="el-icon-refresh" size="mini" @click="resetQuery">重置</el-button>
      </el-form-item>
    </el-form>

    <el-row :gutter="10" class="mb8">
      <el-col :span="1.5">
        <el-button
          type="primary"
          plain
          icon="el-icon-plus"
          size="mini"
          @click="handleAdd"
          v-hasPermi="['stock:stock:add']"
        >新增</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
          type="success"
          plain
          icon="el-icon-edit"
          size="mini"
          :disabled="single"
          @click="handleUpdate"
          v-hasPermi="['stock:stock:edit']"
        >修改</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
          type="danger"
          plain
          icon="el-icon-delete"
          size="mini"
          :disabled="multiple"
          @click="handleDelete"
          v-hasPermi="['stock:stock:remove']"
        >删除</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
          type="warning"
          plain
          icon="el-icon-download"
          size="mini"
          @click="handleExport"
          v-hasPermi="['stock:stock:export']"
        >导出</el-button>
      </el-col>
      <right-toolbar :showSearch.sync="showSearch" @queryTable="getList"></right-toolbar>
    </el-row>

    <el-table style="overflow:auto; width: 100%;" v-loading="loading" :data="stockList" @selection-change="handleSelectionChange">
      <el-table-column type="selection" width="55" align="center" />
      <el-table-column label="序号" type="index" width="50"/>
      <el-table-column label="型材编码" align="center" prop="profileCode" />
      <el-table-column label="型材名称" align="center" prop="profileName" />
      <el-table-column label="颜色" align="center" prop="color" />
      <el-table-column label="数量" align="center" prop="quantity">
        <template slot-scope="scope">
          {{ `${scope.row.quantity} ${scope.row.materialType === 0 ? "支" : "件"}` }}
        </template>
      </el-table-column>
      <el-table-column label="长度" align="center" prop="length" />
      <el-table-column label="厚度" align="center" prop="thickness" />
      <el-table-column label="进货单价" align="center" prop="price" />
      <el-table-column label="重量(kg)" align="center" prop="weight" />
      <el-table-column label="总重量(kg)" align="center" prop="totalWeight" />
      <el-table-column label="操作" align="center" class-name="small-padding fixed-width">
        <template slot-scope="scope">
          <el-button
            size="mini"
            type="text"
            icon="el-icon-edit"
            @click="handleUpdate(scope.row)"
            v-hasPermi="['stock:stock:edit']"
          >修改</el-button>
          <el-button
            size="mini"
            type="text"
            icon="el-icon-delete"
            @click="handleDelete(scope.row)"
            v-hasPermi="['stock:stock:remove']"
          >删除</el-button>
        </template>
      </el-table-column>
    </el-table>
    
    <pagination
      v-show="total>0"
      :total="total"
      :page.sync="queryParams.pageNum"
      :limit.sync="queryParams.pageSize"
      @pagination="getList"
    />

    <!-- 添加或修改库存对话框 -->
    <el-dialog :title="title" :visible.sync="open" width="500px" append-to-body :close-on-click-modal="false">
      <el-form ref="form" v-loading="addLoading" :model="form" :rules="rules" label-width="80px">
        <el-form-item label="型材编码" prop="profileCode">
          <el-input v-if="title === '添加库存'" v-model="form.profileCode" @change="fetchStockByCode" placeholder="请输入型材编码" />
          <el-input v-else v-model="form.profileCode" placeholder="请输入型材编码" />
        </el-form-item>
        <el-form-item label="颜色" prop="color">
          <el-input v-model="form.color" placeholder="请输入颜色" />
        </el-form-item>
        <el-form-item label="数量" prop="quantity">
          <el-input v-model.number="form.quantity" placeholder="请输入数量" />
        </el-form-item>
        <el-form-item label="单个重量(kg)" prop="weight">
          <el-input v-model="form.weight" placeholder="请输入重量" />
        </el-form-item>
        <el-form-item label="型材名称" prop="profileName">
          <el-input v-model="form.profileName" placeholder="请输入型材名称" />
        </el-form-item>
        <el-form-item label="长度" prop="length">
          <el-input v-model="form.length" placeholder="请输入长度" />
        </el-form-item>
        <el-form-item label="厚度" prop="thickness">
          <el-input v-model="form.thickness" placeholder="请输入厚度" />
        </el-form-item>
        <!-- todo: 下拉框 -->
        <el-form-item label="单位" prop="materialType">
          <el-select v-model="form.materialType" placeholder="请选择单位">
            <el-option label="支" :value="0"></el-option>
            <el-option label="件" :value="1"></el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="进货单价" prop="price">
          <el-input v-model="form.price" placeholder="请输入型材进货单价" />
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button type="primary" @click="submitForm">确 定</el-button>
        <el-button @click="cancel">取 消</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
import { listStock, getStock, getStockByCode, delStock, addStock, updateStock } from "@/api/stock/stock";

export default {
  name: "Stock",
  data() {
    return {
      // 遮罩层
      loading: true,
      // 添加库存时的查询 loading
      addLoading: false,
      // 选中数组
      ids: [],
      // 非单个禁用
      single: true,
      // 非多个禁用
      multiple: true,
      // 显示搜索条件
      showSearch: true,
      // 总条数
      total: 0,
      // 库存表格数据
      stockList: [],
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
        weight: null,
        profileName: null,
        length: null,
        thickness: null,
        materialType: null,
        price: null,
        isDelete: null
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
          { required: true, message: "数量不能为空", trigger: "blur" },
          { type: 'number', message: "必须为整数" }
        ],
        weight: [
          { required: true, message: "重量不能为空", trigger: "blur" },
          { pattern: /^\d+(\.\d+)?$/, message: '必须为数字类型' }
        ],
        profileName: [
          { required: true, message: "型材名称不能为空", trigger: "blur" }
        ],
        length: [
          { required: true, message: "长度不能为空", trigger: "blur" },
          { pattern: /^\d+(\.\d+)?$/, message: '必须为数字类型' }
        ],
        thickness: [
          { required: true, message: "厚度不能为空", trigger: "blur" },
          { pattern: /^\d+(\.\d+)?$/, message: '必须为数字类型' }
        ],
        price: [
          { required: true, message: "型材进货单价不能为空", trigger: "blur" },
          { pattern: /^\d+(\.\d+)?$/, message: '必须为数字类型' }
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
      listStock(this.queryParams).then(response => {
        this.stockList = response.rows;
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
        weight: null,
        profileName: null,
        length: null,
        thickness: null,
        materialType: null,
        price: null,
        createTime: null,
        updateTime: null,
        isDelete: null
      };
      this.resetForm("form");
    },
    /** 搜索按钮操作 */
    handleQuery() {
      this.queryParams.pageNum = 1;
      this.getList();
    },
    /** 重置按钮操作 */
    resetQuery() {
      this.resetForm("queryForm");
      this.handleQuery();
    },
    // 多选框选中数据
    handleSelectionChange(selection) {
      this.ids = selection.map(item => item.id)
      this.single = selection.length!==1
      this.multiple = !selection.length
    },
    /** 新增按钮操作 */
    handleAdd() {
      this.reset();
      this.open = true;
      this.title = "添加库存";
    },

    async fetchStockByCode() {
      this.addLoading = true;
      const res = await getStockByCode(this.form.profileCode);
      this.addLoading = false;
      res.data.quantity = null;
      res.data.id = null;
      this.form = res.data;

    },

    /** 修改按钮操作 */
    handleUpdate(row) {
      this.reset();
      const id = row.id || this.ids
      getStock(id).then(response => {
        this.form = response.data;
        this.open = true;
        this.title = "修改库存";
      });
    },
    /** 提交按钮 */
    submitForm() {
      this.$refs["form"].validate(valid => {
        if (valid) {
          if (this.form.id != null) {
            updateStock(this.form).then(response => {
              this.$modal.msgSuccess("修改成功");
              this.open = false;
              this.getList();
            });
          } else {
            addStock(this.form).then(response => {
              this.$modal.msgSuccess("新增成功");
              this.open = false;
              this.getList();
            });
          }
        }
      });
    },
    /** 删除按钮操作 */
    handleDelete(row) {
      const ids = row.id || this.ids;
      this.$modal.confirm('是否确认删除库存编号为"' + ids + '"的数据项？').then(function() {
        return delStock(ids);
      }).then(() => {
        this.getList();
        this.$modal.msgSuccess("删除成功");
      }).catch(() => {});
    },
    /** 导出按钮操作 */
    handleExport() {
      this.download('stock/stock/export', {
        ...this.queryParams
      }, `stock_${new Date().getTime()}.xlsx`)
    }
  }
};
</script>
