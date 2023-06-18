const ExcelJS = require("exceljs");
import FileSaver from "file-saver";
import { Message } from 'element-ui'

export function genExcel(data) {
  if (data.length === 0) {
    Message({ message: "清单为空", type: 'error', duration: 1 * 1000 })
  } else {
    doGenExcel(data)
  }
}

function doGenExcel(data) {
  // 创建一个新的工作簿
  const workbook = new ExcelJS.Workbook();

  // 添加一个工作表
  const worksheet = workbook.addWorksheet("Sheet1");

  // 定义表头
  const headers = [
    { header: "型材编码", key: "profileCode" },
    { header: "型材名称", key: "profileName" },
    { header: "颜色", key: "color" },
    { header: "长度", key: "length" },
    { header: "数量", key: "quantity" },
    { header: "厚度", key: "thickness" },
    { header: "重量", key: "weight" },
    { header: "单价", key: "price" },
  ];

  const _titleCell = worksheet.getRow(1);
  // 设置第一行的高度
  _titleCell.height = 30;
  // 设置第一行的字体样式
  _titleCell.font = {
    name: "黑体",
    bold: true,
    size: 14,
    color: {
      argb: "FF999999",
    },
  };
  // 设置第一行的对齐方式（水平垂直）
  _titleCell.alignment = {
    vertical: "middle",
    horizontal: "center",
  };
  // 设置边框线的样式
  _titleCell.border = {
    top: {
      style: "medium",
      color: {
        argb: "00000000",
      },
    },
    left: {
      style: "medium",
      color: {
        argb: "00000000",
      },
    },
    bottom: {
      style: "medium",
      color: {
        argb: "00000000",
      },
    },
    right: {
      style: "medium",
      color: {
        argb: "00000000",
      },
    },
  };
  // 设置单元的样式
  _titleCell.fill = {
    type: "pattern",
    pattern: "solid",
    fgColor: {
      argb: "FFF5F7FA",
    },
  };

  // 添加表头行
  worksheet.columns = headers;
  // 添加数据到工作表
  data.forEach((rowData) => {
    const row = worksheet.addRow(rowData);
  });
  const date = new Date();
  const formattedDateTime = `${date.getFullYear()}-${(date.getMonth() + 1)
    .toString()
    .padStart(2, "0")}-${date.getDate().toString().padStart(2, "0")} ${date
    .getHours()
    .toString()
    .padStart(2, "0")}:${date.getMinutes().toString().padStart(2, "0")}:${date
    .getSeconds()
    .toString()
    .padStart(2, "0")}`;
  workbook.xlsx.writeBuffer().then((buffer) => {
    let _file = new Blob([buffer], {
      type: "application/octet-stream",
    });
    FileSaver.saveAs(_file, `补货单-${formattedDateTime}.xlsx`);
  });
}
