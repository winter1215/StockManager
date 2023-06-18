<template>
  <div class="app-container">
    <el-form :model="queryParams" ref="queryForm" size="small" :inline="true" v-show="showSearch" label-width="68px">
      <el-form-item label="型材编码" prop="profileCode">
        <el-input v-model="queryParams.profileCode" placeholder="请输入型材编码" clearable @keyup.enter.native="handleQuery" />
      </el-form-item>
      <el-form-item label="颜色" prop="color">
        <el-input v-model="queryParams.color" placeholder="请输入颜色" clearable @keyup.enter.native="handleQuery" />
      </el-form-item>
      <el-form-item label="数量" prop="quantity">
        <el-input v-model="queryParams.quantity" placeholder="请输入数量" clearable @keyup.enter.native="handleQuery" />
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
      <el-form-item label="单位" prop="materialType">
        <el-select v-model="queryParams.materialType" placeholder="请选择单位">
          <el-option label="支" :value="0"></el-option>
          <el-option label="件" :value="1"></el-option>
        </el-select>
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
        <el-button type="success" plain icon="el-icon-delete" size="mini" @click="drawerShow = true"
          v-hasPermi="['stock:stock:list']">查看清单</el-button>
      </el-col>
      <right-toolbar :showSearch.sync="showSearch" @queryTable="getList"></right-toolbar>
    </el-row>

    <!-- 出货数量修改库存对话框 -->
    <el-dialog title="出货面板" :visible.sync="open" width="500px" append-to-body>
      <el-form ref="form" :model="form" :rules="rules" label-width="80px">
        <el-form-item label="型材编码" prop="profileCode">
          <el-input v-model="form.profileCode" disabled />
        </el-form-item>
        <el-form-item label="型材名称" prop="profileName">
          <el-input v-model="form.profileName" disabled />
        </el-form-item>
        <el-form-item label="数量" prop="quantity">
          <el-input v-model.number="form.quantity" placeholder="请输入数量" />
        </el-form-item>
        <el-form-item label="进货单价" prop="price">
          <el-input v-model="form.price" placeholder="请输入型材进货单价" />
        </el-form-item>
        <!-- 是否需要出货价格 -->
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button type="primary" @click="handleMoveIn">确 定</el-button>
        <el-button @click="open = false">取 消</el-button>
      </div>
    </el-dialog>

    <!-- 清单抽屉 -->
    <el-drawer title="出货清单" :visible.sync="drawerShow" direction="rtl" size="50%">
      <el-col :span="1.5">
        <el-button type="warning" plain icon="el-icon-download" size="mini" @click="handleExport"
          v-hasPermi="['stock:stock:export']">导出清单</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button type="warning" plain icon="el-icon-download" size="mini" @click="printStockout"
          v-hasPermi="['stock:stock:export']">打印清单</el-button>
      </el-col>
      <el-table :data="stockoutList" height="700">
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
            <el-button size="mini" type="danger" icon="el-icon-delete" @click="handleMoveOut(scope.row, 1)">移除</el-button>
          </template>
        </el-table-column>
      </el-table>
    </el-drawer>

    <!-- 库存表格 -->
    <el-table v-loading="loading" @sort-change="onSortChange" :data="stockList" ref="stockRef">
      <el-table-column label="序号" type="index" width="50" />
      <el-table-column label="型材编码" align="center" prop="profileCode" />
      <el-table-column label="型材名称" align="center" prop="profileName" />
      <el-table-column label="颜色" align="center" prop="color" />
      <el-table-column label="数量" align="center" sortable="custom" prop="quantity">
        <template slot-scope="scope">
          {{ `${scope.row.quantity} ${scope.row.materialType === 0 ? "支" : "件"}` }}
        </template>
      </el-table-column>
      <el-table-column label="长度" align="center" prop="length" />
      <el-table-column label="厚度" align="center" prop="thickness" />
      <el-table-column label="进货单价" align="center" prop="price" />
      <el-table-column label="重量(kg)" align="center" prop="weight" />
      <el-table-column label="总重量(kg)" align="center" sortable="custom" prop="totalWeight" />
      <el-table-column label="操作" align="center" class-name="small-padding fixed-width">
        <template slot-scope="scope">
          <el-button v-if="scope.row.inDrawer" size="mini" type="danger" icon="el-icon-delete"
            @click="handleMoveOut(scope.row, 0)">移出清单</el-button>
          <el-button v-else size="mini" type="primary" icon="el-icon-delete"
            @click="openDialog(scope.row)">移入清单</el-button>

        </template>
      </el-table-column>
    </el-table>

    <pagination v-show="total > 0" :total="total" :page.sync="queryParams.pageNum" :limit.sync="queryParams.pageSize"
      @pagination="getList" />
  </div>
</template>

<script>
import { listStock } from "@/api/stock/stock";
import { genExcel } from '@/utils/excel'

export default {
  name: "Stock",
  data() {
    return {
      // 遮罩层
      loading: true,
      // 抽屉弹出
      drawerShow: false,
      // 选中数组
      ids: new Set(),
      // 显示搜索条件
      showSearch: true,
      // 总条数
      total: 0,
      // 库存表格数据
      stockList: [],
      // 出货列表
      stockoutList: [],
      // 出货 form
      form: {},
      // 表单校验
      rules: {
        profileCode: [
          { required: true, message: "型材编码不能为空", trigger: "blur" }
        ],
        quantity: [
          { required: true, message: "操作前数量不能为空", trigger: "blur" },
          { type: 'number', message: "必须为整数" }
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
        price: [
          { required: true, message: "型材进货单价不能为空", trigger: "blur" }
        ],
      },
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
        response.rows.map(item => {
          if (this.ids.has(item.id)) {
            item.inDrawer = true;
          } else {
            item.inDrawer = false;
          }
        })
        this.stockList = response.rows;
        this.total = response.total;
        this.loading = false;
      });
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
    // 当前列是否存在出货清单中
    inDrawer(id) {
      return this.ids.has(id)
    },
    openDialog(row) {
      this.form = { ...row }
      this.form.quantity = null;
      this.open = true
    },
    // 移入抽屉中的元素
    handleMoveIn() {
      this.$refs["form"].validate(valid => {
        if (valid) {
          const id = this.form.id;
          const flag = this.ids.has(id);

          if (!flag) {
            const stock = this.stockList.filter(item => item.id === this.form.id)[0]

            if (!this.form.quantity || this.form.quantity <= 0) {
              this.$message.error("出货数量有误");
              return;
            }

            this.stockoutList.push(this.form);
            this.ids.add(id);
            // 移入成功后将 list 中的对应 id 的对象 inDrawer 改变
            if (stock) {
              stock.inDrawer = true
            }
            this.$message.success("移入成功")
          } else {
            this.$message.warning("请勿重复添加")
          }
          this.open = false;
        }
      });

    },
    // 移除抽屉中的元素: type: 0 库存页面移除, type = 1 抽屉中移除
    handleMoveOut(row, type = 0) {
      // 该场景中 stockoutList 不适合用 set,因为 has，delete 等是通过引用的，在这里引用可能由于分页的存在不相同，我们需要的是通过 id 来比较
      this.stockoutList = this.stockoutList.filter(item => item.id != row.id);

      this.ids.delete(row.id);
      if (type === 0) {
        row.inDrawer = false;
      } else {
        let item = this.stockList.filter(item => item.id === row.id)[0];
        if (item) {
          item.inDrawer = false;
        }
      }
      this.$message.warning("移出成功")
    },

    // 排序监听事件
    onSortChange(event) {
      if (event.order) {
        this.queryParams.orderByColumn = event.prop;
        this.queryParams.isAsc = event.order;
      } else {
        this.queryParams.orderByColumn = null;
        this.queryParams.isAsc = null;
      }
      this.getList();
    },

    /** 导出按钮操作 */
    handleExport() {
      genExcel([...this.stockoutList]);
    },

    printStockout() {
      console.log([...this.ids]);
    }
  }
};
</script>
