<template>
  <div class="app-container">

    <el-table style="overflow:auto; width: 100%;" v-loading="loading" :data="stockList"
      @selection-change="handleSelectionChange" @row-dblclick="clickRow">
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
          {{ scope.row.materialType === 0 ? "支" : "件" }}
        </template>
      </el-table-column>
      <el-table-column label="出货单价" align="center" prop="price" />
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
      <el-table-column label="处理状态" align="center" prop="state">
        <template slot-scope="scope">
          <el-tag v-if="scope.row.state === 0">默认</el-tag>
          <el-tag v-else-if="scope.row.state === 1" type="info">未处理</el-tag>
          <el-tag v-else-if="scope.row.state === 2">已处理</el-tag>
        </template>
      </el-table-column>
      <el-table-column label="操作" align="center" class-name="small-padding fixed-width">
        <template slot-scope="scope">
          <el-button size="mini" type="text" icon="el-icon-edit" @click="handleYes(scope.row)"
            v-hasPermi="['stock:stock:edit']">确认</el-button>
          <el-button size="mini" type="text" icon="el-icon-delete" @click="handleNo(scope.row)"
            v-hasPermi="['stock:stock:remove']">抛弃</el-button>
        </template>
      </el-table-column>
    </el-table>

    <!-- children list table modal -->
    <el-dialog title="该批次出货列表" :visible.sync="dialogVisible" width="80%">
      <el-table style="overflow:auto; width: 100%;" v-loading="dialogLoading" :data="childList">
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
            {{ scope.row.materialType === 0 ? "支" : "件" }}
          </template>
        </el-table-column>
        <el-table-column label="出货单价" align="center" prop="price" />
        <el-table-column label="重量(kg)" align="center" prop="weight" />
      </el-table>
      <pagination v-show="childTotal > 0" :total="childTotal" :page.sync="childPage.pageNum"
        :limit.sync="childPage.pageSize" @pagination="getChildrenList" />
    </el-dialog>

  </div>
</template>

<script>
import { listDraft, getChildren, removeDraft, useDraft } from "@/api/stock/log";


export default {
  name: "Stock",
  data() {
    return {
      // 遮罩层
      loading: true,
      // 模态框的 loading
      dialogLoading: false,
      single: true,
      // 非多个禁用
      multiple: true,
      // 是否打开模态框
      dialogVisible: false,
      // 显示搜索条件
      showSearch: true,
      // 总条数
      total: 0,
      // 孩子总条数
      childTotal: 0,
      childPage: {
        pageNum: 1,
        pageSize: 10,
        fId: 0
      },
      // 库存表格数据
      stockList: [],
      // 孩子 list
      childList: [],
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
      listDraft(this.queryParams).then(response => {
        this.stockList = response.rows;
        this.total = response.total;
        this.loading = false;
      });
    },
    // 多选框选中数据
    handleSelectionChange(selection) {
      this.ids = selection.map(item => item.id)
      this.single = selection.length !== 1
      this.multiple = !selection.length
    },

    // 处理确认
    handleYes(row) {
      this.$modal.confirm('是否确认该批次出货').then(function () {
        return useDraft(row.id)
      }).then(() => {
        this.getList();
        this.$modal.msgSuccess("操作成功");
      }).catch(() => { });
    },
    // 处理抛弃
    handleNo(row) {
      this.$modal.confirm('是否抛弃该批次出货').then(function () {
        return removeDraft(row.id)
      }).then(() => {
        this.getList();
        this.$modal.msgSuccess("抛弃成功");
      }).catch(() => { });
    },

    async getChildrenList() {
      const res = await getChildren(this.childPage);
      this.childList = res.rows;
      this.childTotal = res.total;
    },

    clickRow(row, column, event) {
      this.childPage.fId = row.id;
      this.dialogVisible = true;
      this.dialogLoading = true;

      this.getChildrenList();
      this.dialogLoading = false;
    },
  },
}
</script>
