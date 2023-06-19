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
    </div>
</template>
  
<script>
import template from './template'
import { outStock } from "@/api/stock/stock";
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
            waitShowPrinter: false,
            // 纸张宽 mm
            width: 0,
            // 模板
            hiprintTemplate: {},
            // 数据
            printData: {
                zhi: 0,
                ge: 0,
                totalWeight: 0.0,
                totalPrice: 0.0,
                totalCap: ""
            }
        }
    },
    created() {
    },
    mounted() {
        this.init();
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
        }
    },
    methods: {
        init() {
            hiprintTemplate = new hiprint.PrintTemplate({
                template: template
            });
            // 初始化数据
            this.initData();
            this.show(hiprintTemplate);
        },
        initData() {
            // 拷贝 防止数据出错
            let tmp = [...this.dataProp]
            tmp.forEach(item => {
                this.printData.totalPrice = this.printData.totalPrice + item.quantity * item.price;
                item.totalPrice = item.quantity * item.price;
                if (item.materialType == 0) {
                    this.printData.zhi = this.printData.zhi + item.quantity;
                } else {
                    this.printData.ge = this.printData.ge + item.quantity;
                }
                item.materialType = item.materialType === 0 ? '支' : '件';
                this.printData.totalWeight = this.printData.totalWeight + item.totalWeight;
            })
            this.printData.totalCap = this.toChinese(this.printData.totalPrice);
            this.printData.table = this.dataProp;

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
            this.$confirm('打印后请于待确认处处理', '提示', {
                confirmButtonText: '确定',
                cancelButtonText: '取消',
                type: 'warning'
            }).then(async () => {
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
                        changeQuantity: item.quantity
                    }
                });
                data.state = 1; // 待确定状态
                const res = await outStock(data);
                console.log(res);
            }).catch(() => { });



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