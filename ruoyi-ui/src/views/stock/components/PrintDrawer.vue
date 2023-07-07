<template>
    <div>
        <el-row :gutter="20">
            <el-col :span="6" :offset="18">
                <el-button :loading="waitShowPrinter" type="primary" icon="printer" @click.stop="print">打印</el-button>
                <el-button type="primary" icon="printer" @click.stop="toPdf">pdf</el-button>
            </el-col>
        </el-row>
        <div style="min-height: 100px; margin-left: 5%;">
            <div id="preview_content_custom"></div>
        </div>

        <el-dialog title="出货面板" :visible.sync="dialogFormVisible" width="500px" append-to-body>
            <el-form ref="form" :model="form" label-width="80px">
                <el-form-item label="客户名称" prop="name">
                    <el-input v-model.number="form.name" placeholder="请输入客户名称" />
                </el-form-item>
                <el-form-item label="价格" prop="price">
                    <el-input v-model="form.price" placeholder="请输入出货价格" />
                </el-form-item>
                <el-form-item label="重量" prop="weight">
                    <el-input v-model="form.weight" placeholder="请输入出货总重" />
                </el-form-item>
            </el-form>
            <div slot="footer" class="dialog-footer">
                <el-button type="primary" @click="doprint">确 定</el-button>
                <el-button @click="dialogFormVisible = false">取 消</el-button>
            </div>
        </el-dialog>
    </div>
</template>
  
<script>
import template from './template'
import { outStock } from "@/api/stock/stock";
import { getOrder } from "@/api/stock/log";
import { hiprint } from "vue-plugin-hiprint";

let hiprintTemplate;
export default {
    name: "printPreview",
    // stock out list : 需要初始化组装成 printData
    props: {
        dataProp: Array
    },
    data() {
        return {
            dialogFormVisible: false,
            waitShowPrinter: false,
            formLabelWidth: '120px',
            // 纸张宽 mm
            width: 0,
            form: {},
            // 模板
            hiprintTemplate: {},
            // 数据
            printData: {
                zhi: 0,
                ge: 0,
                totalWeight: 0.0,
                totalPrice: 0,
                totalCap: ""
            }
        }
    },
    created() {
        console.log(template)
    },
    mounted() {
        this.init();
        // 初始化数据
        this.initData();
        this.show(hiprintTemplate);
    },
    watch: {
        dataProp(newVal) {
            this.printData.zhi = 0;
            this.printData.ge = 0;
            this.printData.totalWeight = 0.0;
            this.printData.totalPrice = 0.0;
            this.printData.totalCap = "";
            this.initData();
            this.show(hiprintTemplate);
        },
        deep: true
    },
    methods: {
        init() {
            hiprintTemplate = new hiprint.PrintTemplate({
                template: template
            });
        },
        initData() {
            // 拷贝 防止数据出错

            this.printData.makeDate = this.parseTime(new Date(), '{y}年{m}月{d}日')
            let tmp = [...this.dataProp] //浅拷贝，可能会修改上游数据
            tmp.forEach(item => {
                // item.totalPrice = item.weight * item.price; // 单件的总重
                // item.totalPrice = parseFloat(item.totalPrice.toFixed(2));
                // this.printData.totalPrice = this.printData.totalPrice + item.totalPrice; // 出货单的总重

                if (item.materialType === 0 || item.materialType === '支') {
                    this.printData.zhi = this.printData.zhi + item.quantity;
                } else {
                    this.printData.ge = this.printData.ge + item.quantity;
                }
                item.materialType = item.materialType === 0 || item.materialType === '支' ? '支' : '件';
                // this.printData.totalWeight = this.printData.totalWeight + item.weight;
            })
            // 打印预览将 总价与总重保留两位小数
            // this.printData.totalPrice = parseFloat(this.printData.totalPrice.toFixed(2));
            // this.printData.totalWeight = parseFloat(this.printData.totalWeight.toFixed(2));

            this.printData.totalCap = this.toChinese(this.printData.totalPrice);
            this.printData.table = tmp;

        },

        show(hiprintTemplate, width = '210') {
            this.width = hiprintTemplate.editingPanel ? hiprintTemplate.editingPanel.width : width;
            this.hiprintTemplate = hiprintTemplate
            setTimeout(() => {
                // eslint-disable-next-line no-undef
                $('#preview_content_custom').html(hiprintTemplate.getHtml(this.printData))
                this.spinning = false
            }, 500)
        },


        print() {
            this.dialogFormVisible = true;
            this.form = {};
            // this.$prompt('请输入客户姓名', '提示', {
            //     confirmButtonText: '确定',
            //     cancelButtonText: '取消',
            //     inputErrorMessage: '请输入客户姓名'
            // }).then(async ({ value }) => {

            //     const res = await getOrder();
            //     let param = {};
            //     param.name = value;
            //     // 不是 data
            //     param.orderNum = res.msg;
            //     console.log(res);

            //     // 初始化数据
            //     this.printData.zhi = 0;
            //     this.printData.ge = 0;
            //     this.printData.totalWeight = 0.0;
            //     this.printData.totalPrice = 0.0;
            //     this.printData.totalCap = "";
            //     this.initData(param);
            //     this.waitShowPrinter = true
            //     this.hiprintTemplate.print(this.printData, {}, {
            //         callback: () => {
            //             this.waitShowPrinter = false
            //         }
            //     })

            //     let data = {};
            //     data.stockOutInfoList = [...this.dataProp].map(item => {
            //         return {
            //             profileCode: item.profileCode,
            //             changeQuantity: item.quantity,
            //             changeWeight: item.weight,
            //             changePrice: item.price
            //         }
            //     });
            //     data.state = 1; // 待确定状态
            //     const res1 = outStock(data);
            // }).catch(() => {
            //     this.$message({
            //         type: 'info',
            //         message: '取消输入'
            //     });
            // });
        },

        async doprint() {
            const res = await getOrder();
            // 不是 data
            this.printData.name = this.form.name;
            this.printData.totalWeight = this.form.weight;
            this.printData.totalPrice = parseFloat((this.form.weight * this.form.price).toFixed(2));
            this.printData.orderNum = res.msg;

            // 初始化数据
            this.printData.zhi = 0;
            this.printData.ge = 0;
            this.printData.totalCap = "";
            this.printData.name 
            this.initData();
            this.waitShowPrinter = true
            this.hiprintTemplate.print(this.printData, {}, {
                callback: () => {
                    this.waitShowPrinter = false
                }
            })

            let data = {};
            data.stockOutInfoList = [...this.dataProp].map(item => {
                return {
                    profileCode: item.profileCode,
                    changeQuantity: item.quantity,
                    changeWeight: item.weight,
                    changePrice: item.price
                }
            });
            data.state = 1; // 待确定状态
            const res1 = outStock(data);
        },

        toPdf() {
            this.hiprintTemplate.toPdf(this.printData, '打印预览pdf');
        },
        toChinese(n) {
            if (n === 0)
                return "零";
            if (!/^(0|[1-9]\d*)(\.\d+)?$/.test(n))
                return "";
            var unit = "仟佰拾亿仟佰拾万仟佰拾元角分", str = "";
            n += "00";
            var p = n.indexOf('.');
            if (p >= 0)
                n = n.substring(0, p) + n.substr(p + 1, 2);
            unit = unit.substr(unit.length - n.length);
            for (var i = 0; i < n.length; i++)
                str += '零壹贰叁肆伍陆柒捌玖'.charAt(n.charAt(i)) + unit.charAt(i);
            return str.replace(/零(仟|佰|拾|角)/g, "零").replace(/(零)+/g, "零").replace(/零(万|亿|元)/g, "$1").replace(/(亿)万/g, "$1$2").replace(/^元零?|零分/g, "").replace(/元$/g, "元整");
        }


    }
}

</script>
<style scoped>
.ant-modal-body {
    padding: 0px;
}

.ant-modal-content {
    margin-bottom: 24px;
}
</style>