<template>
    <div>
        <el-table :data="dataProp" height="700">
            <el-table-column label="型材编码" align="center" prop="profileCode" />
            <el-table-column label="型材名称" align="center" prop="profileName" />
            <el-table-column label="颜色" align="center" prop="color" />
            <el-table-column label="数量" align="center" prop="quantity">
                <template slot-scope="scope">
                    {{ `${scope.row.quantity} ${scope.row.materialType === 0 || scope.row.materialType === '支'  ? "支" : "件"}` }}
                </template>
            </el-table-column>
            <el-table-column label="长度" align="center" prop="length" />
            <el-table-column label="厚度" align="center" prop="thickness" />
            <el-table-column label="进货单价" align="center" prop="price" />
            <!-- <el-table-column label="出货重量" align="center" prop="weight" /> -->
            <el-table-column label="操作" align="center" class-name="small-padding fixed-width">
                <template slot-scope="scope">
                    <el-button size="mini" type="danger" icon="el-icon-delete"
                        @click="$emit('handleMoveOut', scope.row, 1)">移除</el-button>
                </template>
            </el-table-column>
        </el-table>
    </div>
</template>

<script>
export default {
    data() {
        return {

        }
    },
    props: ['dataProp'],
    methods: {

    },
    computed: {

    }
}
</script>
