package com.smate.web.v8pub.service.poi;

import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFClientAnchor;
import org.apache.poi.hssf.usermodel.HSSFComment;
import org.apache.poi.hssf.usermodel.HSSFFont;
import org.apache.poi.hssf.usermodel.HSSFPatriarch;
import org.apache.poi.hssf.usermodel.HSSFRichTextString;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.web.util.HtmlUtils;

import com.smate.core.base.utils.string.StringHtml;
import com.smate.web.v8pub.poi.FontConstants;

/**
 * 
 * 利用开源组件POI动态导出EXCEL文档.
 * 
 * 
 * @author 陈祥荣
 * @param <T>
 * 
 *        应用泛型，代表任意一个符合javabean风格的类
 * 
 *        注意这里为了简单起见，boolean型的属性xxx的get器方式为getXxx(),而不是isXxx()
 * 
 *        byte[]表jpg格式的图片数据.
 * 
 *        利用反射，根据javabean属性的先后顺序
 * 
 */

public class POIHandler<T> {

  public HSSFWorkbook exportExcel(String title, Collection<T> dataset, int flag, String pattern) throws Exception {
    String[] headers;
    try {
      String locale = LocaleContextHolder.getLocale().toString();
      switch (flag) {
        case 1:// 个人信息
          if ("zh_CN".equals(locale))
            headers =
                new String[] {"姓名", "头衔", "邮件", "电话号码", "手机", "QQ", "MSN", "成果", "项目", " SCI/SSCI引用", "Ｈ指数", "研究领域"};
          else
            headers = new String[] {"Name", "Title", "Email", "Tel", "Mobile", "QQ", "MSN", "Publication", "Project",
                "SCI/SSCI", "H-index", "ResearchArea"};
          break;
        case 2:// 成果
          if ("zh_CN".equals(locale))
            headers = new String[] {"类别", "标题", "外文标题", "作者名", "摘要", "外文摘要", "关键词", "外文关键词", "来源", "ISSN", "状态", "类型",
                "论文类别", "会议组织者", "ISBN", "章节号码", "总字数", "生效日期", "备注", "全文"};
          else
            headers = new String[] {"Category", "Title", "Foreign Title", "Author", "Abstract", "Foreign Abstract",
                "Key", "Abstract Key", "Source", "ISSN", "Status", "Kind", "Paper Type", "Conference organizers",
                "ISBN", "Chapter number", "Total Words", "Effective Date", "Remark", "FullText"};
          break;
        case 3:// 项目
          if ("zh_CN".equals(locale))
            headers = new String[] {"标题", "外文名称", "作者名", "摘要", "外文摘要", "关键词", "外文关键词", "学科代码", "主导单位名称", "批准号（本机构）",
                "批准号（资助机构）", "项目年度", "项目类型", "资助机构", "资助机构(英文)", "资助机构类别", "资助机构类型(英文)", "开始日期", "结束日期", "资金总数", "货币单位",
                "项目状态", "备注", "全文"};
          else
            headers = new String[] {"Title", "Foreign Title", "Author", "Project Abstract",
                "Project Abstract in Other Language", "Keywords", "Keywords in Other Language", "Discipline",
                "Support units", "Internal Approve No.", "External Approve No.", "Year", "Category",
                "Funding organizations", "Funding in ohter language", "Scheme", "Scheme in other language",
                "Start Date", "End Date", "Total funds", "Currency unit", "Status", "Remarks", "Fulltext"};
          break;
        case 4:// 单位列表
          if ("zh_CN".equals(locale))
            headers = new String[] {"单位主键", "单位名称", "人员数", "项目数", "成果数", "单位管理员"};
          else
            headers = new String[] {"Id", "Name", "Personnel number", "Project Number", "Publication Number", "Admins"};
          break;
        default:
          if ("zh_CN".equals(locale))
            headers = new String[] {"姓名", "头衔", "邮件", "电话号码", "手机", "QQ", "MSN"};
          else
            headers = new String[] {"Name", "Title", "Email", "Tel", "Mobile", "QQ", "MSN"};
          break;
      }
      if (pattern == null)
        pattern = "yyyy-MM-dd";
      if (title == null)
        title = "excel";
      return this.exportExcelx(title, headers, dataset, pattern);
    } catch (Exception e) {
      throw new Exception(e);
    }
  }

  public HSSFWorkbook exportExcelTable(String title, Collection<T> dataset, String pattern) throws Exception {
    try {
      if (pattern == null)
        pattern = "yyyy-MM-dd";
      if (title == null)
        title = "excel";

      // 声明一个工作薄
      HSSFWorkbook workbook = new HSSFWorkbook();
      // 生成一个表格
      HSSFSheet sheet = workbook.createSheet(title);
      // 设置表格默认列宽度为15个字节
      sheet.setDefaultColumnWidth((short) 15);
      // sheet.setColumnWidth(1, 100);
      // 生成并设置另一个样式
      HSSFCellStyle style = workbook.createCellStyle();
      // 生成另一个字体
      HSSFFont font = workbook.createFont();
      font.setBoldweight(HSSFFont.BOLDWEIGHT_NORMAL);
      // 把字体应用到当前的样式
      style.setFont(font);
      // 声明一个画图的顶级管理器
      HSSFPatriarch patriarch = sheet.createDrawingPatriarch();
      // 定义注释的大小和位置,详见文档
      HSSFComment comment = patriarch.createComment(new HSSFClientAnchor(0, 0, 0, 0, (short) 4, 2, (short) 6, 5));
      // 设置注释内容
      comment.setString(new HSSFRichTextString("可以在POI中添加注释！"));
      // 设置注释作者，当鼠标移动到单元格上是可以在状态栏中看到该内容.
      comment.setAuthor("leno");
      // 产生表格标题行
      HSSFRow row = null;
      // 产生表格列
      HSSFCell cell = null;
      // 遍历集合数据，产生数据行
      Iterator<T> it = dataset.iterator();
      int index = 0;
      StringBuffer sb = null;
      while (it.hasNext()) {
        sb = new StringBuffer();
        row = sheet.createRow(index);
        cell = row.createCell(0);
        cell.setCellStyle(style);
        T t = (T) it.next();
        // 利用反射，根据javabean属性的先后顺序，动态调用getXxx()方法得到属性值
        Field[] fields = t.getClass().getDeclaredFields();
        Field[] temp;

        // 判断是否有Serializable
        Field field = fields[0];
        String fieldName = field.getName();
        String getMethodName = "get" + fieldName.substring(0, 1).toUpperCase() + fieldName.substring(1);
        Class tCls = t.getClass();
        Method getMethod = tCls.getMethod(getMethodName, new Class[] {});
        if ("getSerialVersionUID".equals(getMethod.getName())) {
          temp = new Field[fields.length - 1];
          for (int i = 0; i < temp.length; i++)
            temp[i] = fields[i + 1];
        } else
          temp = fields;

        for (short i = 0; i < temp.length; i++) {
          field = temp[i];
          fieldName = field.getName();
          getMethodName = "get" + fieldName.substring(0, 1).toUpperCase() + fieldName.substring(1);
          tCls = t.getClass();
          getMethod = tCls.getMethod(getMethodName, new Class[] {});
          Object value = getMethod.invoke(t, new Object[] {});

          if (value != null && !"".equals(value)) {
            sb.append(StringHtml.wordHandler(value.toString().replace(FontConstants.FONT_ITALIC_MARK, "")) + ", ");
          }
        }
        sb.deleteCharAt(sb.length() - 2);
        // sb.append("。");
        HSSFRichTextString richString = new HSSFRichTextString(sb.toString());
        HSSFFont font3 = workbook.createFont();
        font3.setColor(HSSFColor.BLACK.index);
        richString.applyFont(font3);
        cell.setCellValue(richString);
        index++;
      }

      return workbook;
    } catch (Exception e) {
      throw new Exception(e);
    }
  }

  /**
   * 
   * 这是一个通用的方法，利用了JAVA的反射机制，可以将放置在JAVA集合中并且符合一定条件的数据以EXCEL 的形式输出到指定IO设备上.
   * 
   * 
   * 
   * @param title
   * 
   *        表格标题名
   * 
   * @param headers
   * 
   *        表格属性列名数组
   * 
   * @param dataset
   * 
   *        需要显示的数据集合,集合中一定要放置符合javabean风格的类的对象。此方法支持的
   * 
   *        javabean属性的数据类型有基本数据类型及String,Date,byte[](图片数据)
   * 
   * @param out
   * 
   *        与输出设备关联的流对象，可以将EXCEL文档导出到本地文件或者网络中
   * 
   * @param pattern
   * 
   *        如果有时间数据，设定输出格式。默认为"yyy-MM-dd"
   * @throws NoSuchMethodException
   * @throws SecurityException
   * @throws InvocationTargetException
   * @throws IllegalAccessException
   * @throws IllegalArgumentException
   * @throws IOException
   * 
   */

  @SuppressWarnings({"unchecked", "deprecation"})
  public HSSFWorkbook exportExcelx(String title, String[] headers,

      Collection<T> dataset, String pattern) throws Exception {
    // 声明一个工作薄
    HSSFWorkbook workbook = new HSSFWorkbook();
    // 生成一个表格
    HSSFSheet sheet = workbook.createSheet(title);
    // 设置表格默认列宽度为15个字节
    sheet.setDefaultColumnWidth((short) 15);
    // 生成一个样式
    HSSFCellStyle style = workbook.createCellStyle();
    // 设置这些样式
    style.setFillForegroundColor(HSSFColor.BLACK.index);
    // style.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
    // style.setBorderBottom(HSSFCellStyle.BORDER_THIN);
    // style.setBorderLeft(HSSFCellStyle.BORDER_THIN);
    // style.setBorderRight(HSSFCellStyle.BORDER_THIN);
    // style.setBorderTop(HSSFCellStyle.BORDER_THIN);
    style.setAlignment(HSSFCellStyle.ALIGN_CENTER);
    // 生成一个字体
    HSSFFont font = workbook.createFont();
    // font.setColor(HSSFColor.VIOLET.index);
    font.setFontHeightInPoints((short) 12);
    font.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
    // 把字体应用到当前的样式
    style.setFont(font);
    // 生成并设置另一个样式
    HSSFCellStyle style2 = workbook.createCellStyle();
    // 生成另一个字体
    HSSFFont font2 = workbook.createFont();
    font2.setBoldweight(HSSFFont.BOLDWEIGHT_NORMAL);
    // 把字体应用到当前的样式
    style2.setFont(font2);
    // 声明一个画图的顶级管理器
    HSSFPatriarch patriarch = sheet.createDrawingPatriarch();
    // 定义注释的大小和位置,详见文档
    HSSFComment comment = patriarch.createComment(new HSSFClientAnchor(0, 0, 0, 0, (short) 4, 2, (short) 6, 5));
    // 设置注释内容
    comment.setString(new HSSFRichTextString("可以在POI中添加注释！"));
    // 设置注释作者，当鼠标移动到单元格上是可以在状态栏中看到该内容.
    comment.setAuthor("leno");
    // 产生表格标题行
    HSSFRow row = sheet.createRow(0);
    for (short i = 0; i < headers.length; i++) {
      HSSFCell cell = row.createCell(i);
      cell.setCellStyle(style);
      HSSFRichTextString text = new HSSFRichTextString(headers[i]);
      cell.setCellValue(text);
    }
    // 遍历集合数据，产生数据行
    Iterator<T> it = dataset.iterator();
    int index = 0;
    while (it.hasNext()) {
      index++;
      row = sheet.createRow(index);
      T t = (T) it.next();
      // 利用反射，根据javabean属性的先后顺序，动态调用getXxx()方法得到属性值
      Field[] fields = t.getClass().getDeclaredFields();
      Field[] temp;

      // 判断是否有Serializable
      Field field = fields[0];
      String fieldName = field.getName();
      String getMethodName = "get" + fieldName.substring(0, 1).toUpperCase() + fieldName.substring(1);
      Class tCls = t.getClass();
      Method getMethod = tCls.getMethod(getMethodName, new Class[] {});
      if ("getSerialVersionUID".equals(getMethod.getName())) {
        temp = new Field[fields.length - 1];
        for (int i = 0; i < temp.length; i++)
          temp[i] = fields[i + 1];
      } else
        temp = fields;

      HSSFFont font3 = workbook.createFont();
      font3.setColor(HSSFColor.BLACK.index);
      boolean flag = false;
      for (short i = 0; i < temp.length; i++) {
        field = temp[i];
        fieldName = field.getName();
        getMethodName = "get" + fieldName.substring(0, 1).toUpperCase() + fieldName.substring(1);
        tCls = t.getClass();
        getMethod = tCls.getMethod(getMethodName, new Class[] {});
        if ("getIsPrincipalInsName".equals(getMethod.getName())) {
          flag = true;
          continue;
        }
        HSSFCell cell = null;
        if (!flag) {
          cell = row.createCell(i);
        } else {
          cell = row.createCell(i - 1);
        }
        cell.setCellStyle(style2);
        Object value = getMethod.invoke(t, new Object[] {});
        // 判断值的类型后进行强制类型转换
        String textValue = null;

        if (value == null) {
          textValue = "";
        } else if (value instanceof Boolean) {
          boolean bValue = (Boolean) value;
          textValue = "真";
          if (!bValue) {
            textValue = "假";
          }
        } else if (value instanceof Date) {
          Date date = (Date) value;
          SimpleDateFormat sdf = new SimpleDateFormat(pattern);
          textValue = sdf.format(date);
        } else if (value instanceof byte[]) {
          // 有图片时，设置行高为60px;
          row.setHeightInPoints(60);
          // 设置图片所在列宽度为80px,注意这里单位的一个换算
          sheet.setColumnWidth(i, (short) (35.7 * 80));
          // sheet.autoSizeColumn(i);
          byte[] bsValue = (byte[]) value;
          HSSFClientAnchor anchor = new HSSFClientAnchor(0, 0, 1023, 255, (short) 6, index, (short) 6, index);
          anchor.setAnchorType(2);
          patriarch.createPicture(anchor, workbook.addPicture(bsValue, HSSFWorkbook.PICTURE_TYPE_JPEG));
        } else {
          // 其它数据类型都当作字符串简单处理
          textValue = HtmlUtils.htmlUnescape(StringHtml.removeTagFromText(value.toString() + " "));
        }
        // 如果不是图片数据，就利用正则表达式判断textValue是否全部由数字组成
        if (textValue != null) {
          Pattern p = Pattern.compile("^\\d+(\\.\\d+)?$");
          Matcher matcher = p.matcher(textValue);
          if (matcher.matches()) {
            // 是数字当作double处理
            cell.setCellValue(Double.parseDouble(textValue));
          } else {
            HSSFRichTextString richString = new HSSFRichTextString(textValue);
            richString.applyFont(font3);
            cell.setCellValue(richString);
          }
        }

      }
    }
    return workbook;
  }

  @SuppressWarnings({"deprecation", "unchecked"})
  public HSSFWorkbook exportExcelByTemp(Collection<T> dataset, String pattern, String fileTempPath) throws Exception {
    String[] headerArray = null;
    URL fileUrl = new URL(fileTempPath);
    HttpURLConnection connection = (HttpURLConnection) fileUrl.openConnection();
    DataInputStream in = new DataInputStream(connection.getInputStream());
    POIFSFileSystem fs = new POIFSFileSystem(in);

    // 声明一个工作薄
    HSSFWorkbook workbook = new HSSFWorkbook(fs);
    HSSFSheet sheet = null;

    // 遍历集合数据，产生数据行
    Iterator<T> it = dataset.iterator();
    int index = 3;
    HSSFRow row = null;
    String getMethodName = "";
    String propertyName = "";
    int sheetIndex = -1;
    String[] sheetNameArray = new String[dataset.size()];
    Map<Integer, Integer> map = new HashMap<Integer, Integer>();
    // map存第几行开始写数据
    map.put(1, 3);
    map.put(2, 3);
    map.put(3, 3);
    map.put(4, 3);
    map.put(5, 3);
    map.put(6, 3);
    map.put(7, 3);
    map.put(8, 3);
    map.put(9, 3);
    map.put(10, 3);
    int nameCount = 0;
    while (it.hasNext()) {
      T t = (T) it.next();
      Class tCls = t.getClass();
      // 获取工作薄
      getMethodName = "getSheetIndex";
      Method getMethod = tCls.getMethod(getMethodName, new Class[] {});
      sheetIndex = Integer.valueOf(getMethod.invoke(t, new Object[] {}).toString());
      index = map.get(sheetIndex);
      sheet = workbook.getSheetAt(sheetIndex);
      sheetNameArray[nameCount] = sheet.getSheetName();
      nameCount++;

      headerArray = readHeader(fileTempPath, sheetIndex);

      row = sheet.createRow(index);

      for (short i = 0; i < headerArray.length; i++) {
        if (headerArray[i] != null && !"".equals(headerArray[i])) {
          HSSFCell cell = row.createCell(i);
          propertyName = headerArray[i].replace("_", "");
          getMethodName = "get" + propertyName.substring(0, 1).toUpperCase() + propertyName.substring(1);
          getMethod = tCls.getMethod(getMethodName, new Class[] {});
          Object value = getMethod.invoke(t, new Object[] {});
          // 判断值的类型后进行强制类型转换
          String textValue = null;

          if (value == null) {
            textValue = "";
          } else if (value instanceof Boolean) {
            boolean bValue = (Boolean) value;
            textValue = "真";
            if (!bValue) {
              textValue = "假";
            }
          } else if (value instanceof Date) {
            Date date = (Date) value;
            SimpleDateFormat sdf = new SimpleDateFormat(pattern);
            textValue = sdf.format(date);
          } else {
            // 其它数据类型都当作字符串简单处理
            textValue = HtmlUtils.htmlUnescape(StringHtml.removeTagFromText(value.toString() + " "));
          }
          // 如果不是图片数据，就利用正则表达式判断textValue是否全部由数字组成
          if (textValue != null) {
            textValue = textValue.trim();
            Pattern p = Pattern.compile("^\\d+(\\.\\d+)?$");
            Matcher matcher = p.matcher(textValue);
            if (matcher.matches()) {
              // 是数字当作double处理
              cell.setCellValue(Double.parseDouble(textValue));
            } else {
              HSSFRichTextString richString = new HSSFRichTextString(textValue);
              cell.setCellValue(richString);
            }
          }

        }
      }
      index++;
      map.put(sheetIndex, index);
    }

    int leftCount = 11;
    int removeFlag = 0;
    for (int i = 0; i < leftCount; i++) {
      if (removeFlag == 1) {
        i--;
      }
      removeFlag = 0;
      String sheetName = workbook.getSheetName(i);
      for (int j = 0; j < sheetNameArray.length; j++) {
        if (sheetName.equals(sheetNameArray[j])) {
          removeFlag = 0;
          break;
        } else {
          removeFlag = 1;
        }
      }
      if (removeFlag == 1) {
        workbook.removeSheetAt(i);
        leftCount--;
      }
    }
    if (removeFlag == 1) {
      String sheetName = workbook.getSheetName(leftCount - 1);
      for (int j = 0; j < sheetNameArray.length; j++) {
        if (sheetName.equals(sheetNameArray[j])) {
          removeFlag = 0;
          break;
        }
      }
      if (removeFlag == 1) {
        workbook.removeSheetAt(leftCount - 1);
      }
    }

    return workbook;
  }

  @SuppressWarnings("deprecation")
  public String[] readHeader(String fileTempPath, int sheetIndex) {
    POIFSFileSystem fs = null;
    HSSFWorkbook wb = null;
    HSSFSheet sheet = null;
    HSSFRow headerRow = null;
    DataInputStream input = null;

    String[] headerArray = null;
    // 读取
    try {
      URL fileUrl = new URL(fileTempPath);
      HttpURLConnection connection = (HttpURLConnection) fileUrl.openConnection();
      input = new DataInputStream(connection.getInputStream());// excelPath,Excel
      // 文件 的绝对路径
      fs = new POIFSFileSystem(input);
      wb = new HSSFWorkbook(fs);
      sheet = wb.getSheetAt(sheetIndex);
      headerRow = sheet.getRow(0);// 得到标题的内容对象。
      int colNum = headerRow.getPhysicalNumberOfCells(); // 得到标题总列数
      headerArray = new String[colNum];

      for (int i = 0; i < colNum; i++) {
        headerArray[i] = headerRow.getCell((short) i).getStringCellValue();
      }
    } catch (FileNotFoundException e) {
      e.printStackTrace();
    } catch (IOException e) {
      e.printStackTrace();
    } finally {
      try {
        if (input != null) {
          input.close();
        }
      } catch (IOException e) {
        e.printStackTrace();
      }
    }
    return headerArray;
  }

  private String copyFile(String fileTempPath) throws IOException {
    String newFileName = UUID.randomUUID().toString().replace("-", "");
    Integer lastIndex = fileTempPath.lastIndexOf(".");
    String outFilePath = fileTempPath.substring(0, lastIndex) + newFileName + fileTempPath.substring(lastIndex + 1);
    FileInputStream fis = new FileInputStream(fileTempPath);
    FileOutputStream fos = new FileOutputStream(outFilePath);
    byte[] buff = new byte[1024];
    int readed = -1;
    while ((readed = fis.read(buff)) > 0)
      fos.write(buff, 0, readed);
    fis.close();
    fos.close();
    return outFilePath;
  }
}
