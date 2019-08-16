package com.smate.core.base.utils.export;

import java.io.IOException;
import java.io.OutputStream;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.Collection;
import java.util.Date;
import java.util.Iterator;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFFont;
import org.apache.poi.hssf.usermodel.HSSFPatriarch;
import org.apache.poi.hssf.usermodel.HSSFRichTextString;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.hssf.util.HSSFColor;

/**
 * @author yxs
 * @since 2018年1月8日
 * @descript excel导出工具类
 */
public class ExportExcelUtil<T> {
  public void exportExcel(String titile, String[] headers, String[] fieldNames, Collection<T> dataset, OutputStream out)
      throws Exception {
    exportExcel(titile, headers, fieldNames, dataset, out, "yyyy-MM-dd");
  }

  /**
   * 这是一个通用的方法，利用了JAVA的反射机制，可以将放置在JAVA集合中并且符号一定条件的数据以EXCEL 的形式输出到指定IO设备上
   *
   * @param title 表格标题名
   * @param headers 表格属性列名数组
   * @param dataset 需要显示的数据集合,集合中一定要放置符合javabean风格的类的对象。此方法支持的 javabean属性的数据类型有基本数据类型及String,Date
   * @param out 与输出设备关联的流对象，可以将EXCEL文档导出到本地文件或者网络中
   * @param pattern 如果有时间数据，设定输出格式。默认为"yyy-MM-dd"
   */
  @SuppressWarnings({"unchecked", "unused"})
  public void exportExcel(String title, String[] headers, String[] fieldNames, Collection<T> dataset, OutputStream out,
      String pattern) throws Exception {
    // 声明一个工作薄
    HSSFWorkbook workbook = new HSSFWorkbook();
    // 生成一个表格
    HSSFSheet sheet = workbook.createSheet(title);
    HSSFCellStyle columnTopStyle = this.getColumnTopStyle(workbook);// 获取列头样式对象
    HSSFCellStyle style = this.getStyle(workbook); // 单元格样式对象
    // 设置表格默认列宽度为15个字节
    sheet.setDefaultColumnWidth((short) 15);
    // 声明一个画图的顶级管理器
    HSSFPatriarch patriarch = sheet.createDrawingPatriarch();
    // 产生表格标题行
    HSSFRow rowRowName = sheet.createRow(0);
    for (short i = 0; i < headers.length; i++) {
      HSSFCell cellRowName = rowRowName.createCell(i);
      cellRowName.setCellType(HSSFCell.CELL_TYPE_STRING); // 设置列头单元格的数据类型
      HSSFRichTextString text = new HSSFRichTextString(headers[i]);
      cellRowName.setCellValue(text); // 设置列头单元格的值
      cellRowName.setCellStyle(columnTopStyle); // 设置列样式
    }
    // 遍历集合数据，产生数据行
    Iterator<T> it = dataset.iterator();
    int rowindex = 1;
    int cellindex = 0;
    while (it.hasNext()) {
      HSSFRow row = sheet.createRow(rowindex);
      rowindex++;
      T t = (T) it.next();
      // 利用反射，根据javabean属性的先后顺序，动态调用getXxx()方法得到属性值
      Field[] fields = t.getClass().getDeclaredFields();
      HSSFCell cell = null;
      for (short i = 0; i < fields.length; i++) {
        Field field = fields[i];
        String fieldName = field.getName();
        if (!matchFileValue(fieldNames, fieldName)) {
          continue;
        } else {
          if (cellindex == headers.length) {
            cellindex = 0;
          }
          cell = row.createCell(cellindex);
          cell.setCellStyle(style); // 设置单元格样式
          cellindex++;
          String getMethodName = "get" + fieldName.substring(0, 1).toUpperCase() + fieldName.substring(1);
          try {
            Class tCls = t.getClass();
            Method getMethod = tCls.getMethod(getMethodName, new Class[] {});
            Class<?> type = field.getType();
            Object value = getMethod.invoke(t, new Object[] {});
            // 判断值的类型后进行强制类型转换
            String textValue = null;
            if (type.equals(Integer.class) || type.equals(Long.class)) {
              if (value == null) {
                textValue = "0";
              } else {
                textValue = value.toString();
              }
            } else if (type.equals(String.class)) {
              if (value == null) {
                textValue = "";
              } else {
                textValue = value.toString();
              }
            } else if (type.equals(Date.class)) {
              if (value == null) {
                textValue = "";
              } else {
                Date date = (Date) value;
                SimpleDateFormat sdf = new SimpleDateFormat(pattern);
                textValue = sdf.format(date);
              }
            }
            if (StringUtils.isNotBlank(textValue)) {
              Pattern p = Pattern.compile("^//d+(//.//d+)?$");
              Matcher matcher = p.matcher(textValue);
              if (matcher.matches()) {
                if (matchFileValue(fieldNames, fieldName)) {
                  cell.setCellValue(Double.parseDouble(textValue));
                }
              } else {
                if (matchFileValue(fieldNames, fieldName)) {
                  cell.setCellValue(textValue);
                }
              }
            }

          } catch (Exception e) {
            e.printStackTrace();
          }

        }
      }
    }
    if (workbook != null) {
      try {
        workbook.write(out);
      } catch (IOException e) {
        e.printStackTrace();
      } finally {
        out.close();

      }
    }
  }

  private boolean matchFileValue(String[] inFieldNames, String outFieldName) {
    for (int i = 0; i < inFieldNames.length; i++) {
      if (outFieldName.equalsIgnoreCase(inFieldNames[i])) {
        return true;
      }
    }
    return false;
  }

  /**
   * @author yxs
   * @param title 文件名
   * @param headers 表格属性列名
   * @param fieldNames 要导出的字段
   * @param dataset 要导出的集合
   */
  public void Export(String[] headers, String[] fieldNames, Collection<T> dataset, HttpServletRequest request,
      HttpServletResponse response) throws Exception {
    // 导出文件的标题
    String fileName = DateUtil.getDateFormat(new Date(), "yyyyMMdd") + ".xls";
    String _fileName = null;
    // 设置表格标题行
    OutputStream out;
    try {
      if (StringUtils.contains(request.getHeader("User-Agent"), "MSIE")) {// IE浏览器
        _fileName = URLEncoder.encode(fileName, "UTF8");
      } else if (StringUtils.contains(request.getHeader("User-Agent"), "Mozilla")) {// 火狐浏览器，谷歌浏览器
        _fileName = new String(fileName.getBytes(), "ISO8859-1");
      } else {
        _fileName = URLEncoder.encode(fileName, "UTF8");// 其他浏览器
      }
      response.setHeader("Content-Disposition", "attachment; filename=\"" + _fileName + "\"");
      response.setContentType("octets/stream");
      response.setContentType("application/vnd.ms-excel");
      out = response.getOutputStream();
      exportExcel(_fileName, headers, fieldNames, dataset, out);
      out.close();
      System.out.println("excel导出成功！");
    } catch (IOException e) {
      e.printStackTrace();
    }

  }

  /* 列头单元格样式 */
  public HSSFCellStyle getColumnTopStyle(HSSFWorkbook workbook) {
    // 设置字体
    HSSFFont font = workbook.createFont();
    // 设置字体大小
    font.setFontHeightInPoints((short) 11);
    // 字体加粗
    font.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
    // 设置字体名字
    font.setFontName("Courier New");
    // 设置样式;
    HSSFCellStyle style = workbook.createCellStyle();
    // 设置底边框;
    style.setBorderBottom(HSSFCellStyle.BORDER_THIN);
    // 设置底边框颜色;
    style.setBottomBorderColor(HSSFColor.BLACK.index);
    // 设置左边框;
    style.setBorderLeft(HSSFCellStyle.BORDER_THIN);
    // 设置左边框颜色;
    style.setLeftBorderColor(HSSFColor.BLACK.index);
    // 设置右边框;
    style.setBorderRight(HSSFCellStyle.BORDER_THIN);
    // 设置右边框颜色;
    style.setRightBorderColor(HSSFColor.BLACK.index);
    // 设置顶边框;
    style.setBorderTop(HSSFCellStyle.BORDER_THIN);
    // 设置顶边框颜色;
    style.setTopBorderColor(HSSFColor.BLACK.index);
    // 在样式用应用设置的字体;
    style.setFont(font);
    // 设置自动换行;
    style.setWrapText(false);
    // 设置水平对齐的样式为居中对齐;
    style.setAlignment(HSSFCellStyle.ALIGN_CENTER);
    // 设置垂直对齐的样式为居中对齐;
    style.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
    return style;

  }

  /* 列数据信息单元格样式 */
  public HSSFCellStyle getStyle(HSSFWorkbook workbook) {
    // 设置字体
    HSSFFont font = workbook.createFont();
    // 设置字体大小
    // font.setFontHeightInPoints((short)10);
    // 字体加粗
    // font.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
    // 设置字体名字
    font.setFontName("Courier New");
    // 设置样式;
    HSSFCellStyle style = workbook.createCellStyle();
    // 设置底边框;
    style.setBorderBottom(HSSFCellStyle.BORDER_THIN);
    // 设置底边框颜色;
    style.setBottomBorderColor(HSSFColor.BLACK.index);
    // 设置左边框;
    style.setBorderLeft(HSSFCellStyle.BORDER_THIN);
    // 设置左边框颜色;
    style.setLeftBorderColor(HSSFColor.BLACK.index);
    // 设置右边框;
    style.setBorderRight(HSSFCellStyle.BORDER_THIN);
    // 设置右边框颜色;
    style.setRightBorderColor(HSSFColor.BLACK.index);
    // 设置顶边框;
    style.setBorderTop(HSSFCellStyle.BORDER_THIN);
    // 设置顶边框颜色;
    style.setTopBorderColor(HSSFColor.BLACK.index);
    // 在样式用应用设置的字体;
    style.setFont(font);
    // 设置自动换行;
    style.setWrapText(false);
    // 设置水平对齐的样式为居中对齐;
    style.setAlignment(HSSFCellStyle.ALIGN_CENTER);
    // 设置垂直对齐的样式为居中对齐;
    style.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
    return style;

  }

}
