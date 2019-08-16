package com.smate.core.base.utils.string;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Field;
import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFDataFormat;
import org.apache.poi.hssf.usermodel.HSSFDateUtil;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

/**
 * 
 * @author zzx
 *
 */
public class ExcelUtils {
  /**
   * 
   * @Title: createExcelFile
   * @Description: 在填充sheet数据的时候,会需要一个空的Excel文件,用于设置Sheet信息的时候用到
   * @return 一个不带有头信息,数据信息的空的excel文件
   * @return: HSSFWorkbook
   */
  public static HSSFWorkbook createExcelFile() {
    HSSFWorkbook wb = new HSSFWorkbook();
    return wb;
  }

  /**
   * 
   * @Title: createExcelFile
   * @Description: 创建一个空的带有头信息的excel
   * @param fileName
   * @param heads
   * @return
   * @return: HSSFWorkbook
   */
  public static HSSFWorkbook createExcelFile(String fileName, List<String> heads) {
    HSSFWorkbook wb = new HSSFWorkbook();
    if (StringUtils.isEmpty(fileName) || null == heads) {
      return null;
    } else {
      HSSFSheet sheet = wb.createSheet(fileName);
      HSSFRow row = sheet.createRow(0);
      // 封装头信息
      for (int index = 0; index < heads.size(); index++) {
        row.createCell(index).setCellValue(heads.get(index));
      }
    }
    return wb;
  }

  public static XSSFWorkbook createExcelFile2(String fileName, List<String> heads) {
    XSSFWorkbook wb = new XSSFWorkbook();
    if (StringUtils.isEmpty(fileName) || null == heads) {
      return null;
    } else {
      XSSFSheet createSheet = wb.createSheet(fileName);

      XSSFRow row = createSheet.createRow(0);
      // 封装头信息
      for (int index = 0; index < heads.size(); index++) {
        row.createCell(index).setCellValue(heads.get(index));
      }
    }
    return wb;
  }

  /**
   * @Title: createExcelFile
   * @Description: 创建excel,带有头信息和数据
   * @param fileName excel表格文件名称
   * @param heads excel表格的头信息
   * @param dataList excel表格要填充的数据
   * @return
   * @throws IOException
   * @return: HSSFWorkbook
   */
  public static HSSFWorkbook createExcelFile(String fileName, List<String> heads, List<List<String>> dataList) {
    HSSFWorkbook wb = new HSSFWorkbook();
    if (StringUtils.isEmpty(fileName) || null == heads || null == dataList) {
      return null;
    } else {
      HSSFSheet sheet = wb.createSheet(fileName);
      HSSFRow row = sheet.createRow(0);
      // 封装头信息
      for (int index = 0; index < heads.size(); index++) {
        row.createCell(index).setCellValue(heads.get(index));
      }
      // 填充数据信息
      for (int i = 0; i < dataList.size(); i++) {
        HSSFRow row_data = sheet.createRow(i + 1);
        for (int j = 0; j < dataList.get(i).size(); j++) {
          row_data.createCell(j).setCellValue(dataList.get(i).get(j));
        }
      }
    }
    return wb;
  }

  /**
   * 
   * @Title: produceCellType
   * @Description: 获取excel单元格里面内容的格式,来获取数据
   * @param cell 单元格
   * @return
   * @return: String 单元格的内容
   */
  private static String produceCellType(Cell cell) {
    String cellStrData = null;
    if (null == cell) {
      return null;
    } else {
      switch (cell.getCellType()) {
        case HSSFCell.CELL_TYPE_NUMERIC: // 日期或者数字
          // 处理日期格式、时间格式
          if (HSSFDateUtil.isCellDateFormatted(cell)) {
            SimpleDateFormat sdf = null;
            // 时间格式的处理
            if (cell.getCellStyle().getDataFormat() == HSSFDataFormat.getBuiltinFormat("h:mm")) {
              sdf = new SimpleDateFormat("HH:mm");
            } else {// 日期格式的处理
              sdf = new SimpleDateFormat("yyyy-MM-dd");
            }
            Date date = cell.getDateCellValue();
            cellStrData = sdf.format(date);
          } else {
            // 数字的处理
            double cellData = cell.getNumericCellValue();
            cellStrData = String.valueOf(cellData);
          }
          break;
        case HSSFCell.CELL_TYPE_STRING: // 字符串
          cellStrData = cell.getStringCellValue();
          break;
        case HSSFCell.CELL_TYPE_BOOLEAN: // Boolean
          cellStrData = String.valueOf(cell.getBooleanCellValue());
          break;
        case HSSFCell.CELL_TYPE_FORMULA: // 公式
          cellStrData = String.valueOf(cell.getCellFormula());
          break;
        case HSSFCell.CELL_TYPE_BLANK: // 空值
          break;
        case HSSFCell.CELL_TYPE_ERROR: // 故障
          break;
        default:
          break;
      }
    }
    return cellStrData;
  }

  /**
   * 
   * @Title: convertExcelDataToMapDataWithPrimaryKey
   * @Description: 将excel文件的每一行数据,转换为HashMap的形式.只转换第一个sheet的数据内容
   * @param convertMap 转换的准则,例如 Map<String, String> headMap = new LinkedHashMap<String, String>();
   *        headMap.put("指标ID", "indicatorId"); headMap.put("指标名称", "indicatorName");
   * @param filePath excel文件
   * @return
   * @throws IOException
   * @return: List<HashMap<String, String>>
   */
  public static List<HashMap<String, String>> convertExcelDataToMapData(Map<String, String> convertMap, String filePath)
      throws IOException {
    List<HashMap<String, String>> dataList = new ArrayList<HashMap<String, String>>();
    Map<String, Integer> headMap = new HashMap<String, Integer>();
    if (null == convertMap || convertMap.size() == 0 || StringUtils.isEmpty(filePath)) {
      return dataList;
    } else {
      InputStream input = new FileInputStream(filePath); // 建立输入流
      Workbook wb = new HSSFWorkbook(input);
      Sheet sheet = wb.getSheetAt(0);
      Row rowIndexs = sheet.getRow(0);
      int cellSize = rowIndexs.getLastCellNum();
      Set<String> keys = convertMap.keySet();
      // 将对应的字段和excel的head的下标对应起来
      for (String key : keys) {
        for (int i = 0; i < cellSize; i++) {
          Cell cell = rowIndexs.getCell(i);
          if (cell != null && cell.getCellType() != Cell.CELL_TYPE_BLANK) {
            if (key.equals(cell.getStringCellValue())) {
              headMap.put(key, rowIndexs.getCell(i).getColumnIndex());
            }
          }
        }
      }
      // 处理数据
      int rowSize = sheet.getLastRowNum();
      for (int i = 1; i < rowSize; i++) { // 第一行默认是表头数据,不算入计算结果
        HashMap<String, String> resultMap = new HashMap<String, String>(); // 用于保存每一行的转换结果
        Row row = sheet.getRow(i);
        for (Entry<String, Integer> entry : headMap.entrySet()) {
          Cell cell = row.getCell(entry.getValue());
          String data = produceCellType(cell);
          resultMap.put(convertMap.get(entry.getKey()), data);
        }
        dataList.add(resultMap);
      }
    }

    return dataList;
  }

  /**
   * 
   * @Title: convertExcelDataToMapDataWithPrimaryKey
   * @Description: excel的转换,带有主键的原则。如果excel的那一行数据的表示的主键为null或者没填写。那么这一行不转换。 例如,下面的
   *               指标ID可以理解为主键.//默认第一行的第一列为主键 将excel文件的每一行数据,转换为HashMap的形式.只转换第一个sheet的数据内容.
   * @param convertMap 转换的准则,例如 Map<String, String> headMap = new LinkedHashMap<String, String>();
   *        headMap.put("指标ID", "indicatorId"); headMap.put("指标名称", "indicatorName");
   * @param filePath excel文件
   * @return
   * @throws IOException
   * @return: List<HashMap<String, String>>
   */
  public static List<HashMap<String, String>> convertExcelDataToMapDataWithPrimaryKey(Map<String, String> convertMap,
      String filePath) throws IOException {
    List<HashMap<String, String>> dataList = new ArrayList<HashMap<String, String>>();
    Map<String, Integer> headMap = new HashMap<String, Integer>();
    if (null == convertMap || convertMap.size() == 0 || StringUtils.isEmpty(filePath)) {
      return dataList;
    } else {
      InputStream input = new FileInputStream(filePath); // 建立输入流
      Workbook wb = new HSSFWorkbook(input);
      Sheet sheet = wb.getSheetAt(0);
      Row rowIndexs = sheet.getRow(0);
      int cellSize = rowIndexs.getLastCellNum();
      Set<String> keys = convertMap.keySet();
      // 将对应的字段和excel的head的下标对应起来
      for (String key : keys) {
        for (int i = 0; i < cellSize; i++) {
          Cell cell = rowIndexs.getCell(i);
          if (cell != null && cell.getCellType() != Cell.CELL_TYPE_BLANK) {
            if (key.equals(cell.getStringCellValue())) {
              headMap.put(key, rowIndexs.getCell(i).getColumnIndex());
            }
          }
        }
      }
      // 处理数据
      int rowSize = sheet.getLastRowNum();
      for (int i = 1; i < rowSize; i++) { // 第一行默认是表头数据,不算入计算结果
        HashMap<String, String> resultMap = new HashMap<String, String>(); // 用于保存每一行的转换结果
        Row row = sheet.getRow(i);
        Cell flagCell = row.getCell(0); // 默认第0列是每一行的主键
        if (null != row && null != flagCell && HSSFCell.CELL_TYPE_BLANK != flagCell.getCellType()) {
          for (Entry<String, Integer> entry : headMap.entrySet()) {
            Cell cell = row.getCell(entry.getValue());
            if (null != cell) {
              String data = produceCellType(cell);
              resultMap.put(convertMap.get(entry.getKey()), data);
            }
          }
        }
        // 将数据加入到,返回数值里面
        dataList.add(resultMap);
      }
    }
    return dataList;
  }

  /**
   * @param <T>
   * @Title: convertExcelDataToClassData
   * @Description: 解析excel已有的数据,以Class的形式返回.
   * @param headNameMap headNameMap.put("CID", "customerId")
   * @param fileName excel文件
   * @param class1 要转换的Class的类型
   * @return
   * @throws InstantiationException
   * @throws IllegalAccessException
   * @throws IOException
   * @throws NoSuchFieldException
   * @throws SecurityException
   * @return: List<T>
   */
  public static <T> List<T> convertExcelDataToClassData(Map<String, String> convertMap, String fileName,
      Class<T> class1)
      throws InstantiationException, IllegalAccessException, IOException, NoSuchFieldException, SecurityException {

    List<T> objects = new ArrayList<T>(); // 返回结果集
    Map<String, Integer> indexHashMap = new HashMap<String, Integer>(); // 定位excel头文件cell位置
    if (null == convertMap || convertMap.size() == 0 || StringUtils.isEmpty(fileName)) {
      return objects;
    } else {
      InputStream input = new FileInputStream(fileName); // 建立输入流
      Workbook wb = new HSSFWorkbook(input);
      Sheet sheet = wb.getSheetAt(0);
      Row rowIndexs = sheet.getRow(0);
      int cellSize = rowIndexs.getLastCellNum();
      // 将对应的字段和excel的head的下标对应起来
      Set<String> keys = convertMap.keySet();
      for (String key : keys) {
        for (int i = 0; i < cellSize; i++) {
          Cell cell = rowIndexs.getCell(i);
          if (cell != null && cell.getCellType() != Cell.CELL_TYPE_BLANK) {
            if (key.equals(produceCellType(cell))) {
              indexHashMap.put(key, rowIndexs.getCell(i).getColumnIndex()); // 头文件push 下标位置
            }
          }
        }
      }
      // 数据的封装
      for (int i = 1; i <= sheet.getLastRowNum(); i++) { // 第一行默认是下标，不算入计算结果
        Row row = sheet.getRow(i);
        T object = class1.newInstance();
        for (Entry<String, Integer> entry : indexHashMap.entrySet()) {
          Cell cell = row.getCell(entry.getValue());
          String data = produceCellType(cell);
          String fieldName = convertMap.get(entry.getKey());
          Field field = object.getClass().getDeclaredField(fieldName);
          field.setAccessible(true);
          // 根据Field的类型,来设置Field的内容
          // 以便于适应除了String外的int,long,double,float等类型的属性
          setFieldValue(object, data, field);
        }
        objects.add(object);
      }

    }
    return objects;
  }

  /**
   * @param <T>
   * @Title: convertExcelDataToClassData
   * @Description: 解析excel已有的数据,以Class的形式返回.
   * @param headNameMap headNameMap.put("CID", "customerId")
   * @param fileName excel文件
   * @param class1 要转换的Class的类型
   * @return
   * @throws InstantiationException
   * @throws IllegalAccessException
   * @throws IOException
   * @throws NoSuchFieldException
   * @throws SecurityException
   * @return: List<T>
   */
  public static <T> List<T> convertExcelDataToClassData(Map<String, String> convertMap, InputStream input,
      Class<T> class1)
      throws InstantiationException, IllegalAccessException, IOException, NoSuchFieldException, SecurityException {

    List<T> objects = new ArrayList<T>(); // 返回结果集
    Map<String, Integer> indexHashMap = new HashMap<String, Integer>(); // 定位excel头文件cell位置
    if (null == convertMap || convertMap.size() == 0 || null == input) {
      return objects;
    } else {
      Workbook wb = new HSSFWorkbook(input);
      Sheet sheet = wb.getSheetAt(0);
      Row rowIndexs = sheet.getRow(0);
      int cellSize = rowIndexs.getLastCellNum();
      // 将对应的字段和excel的head的下标对应起来
      Set<String> keys = convertMap.keySet();
      for (String key : keys) {
        for (int i = 0; i < cellSize; i++) {
          Cell cell = rowIndexs.getCell(i);
          if (cell != null && cell.getCellType() != Cell.CELL_TYPE_BLANK) {
            if (key.equals(produceCellType(cell))) {
              indexHashMap.put(key, rowIndexs.getCell(i).getColumnIndex()); // 头文件push 下标位置
            }
          }
        }
      }
      // 数据的封装
      for (int i = 1; i <= sheet.getLastRowNum(); i++) { // 第一行默认是下标，不算入计算结果
        Row row = sheet.getRow(i);
        T object = class1.newInstance();
        for (Entry<String, Integer> entry : indexHashMap.entrySet()) {
          Cell cell = row.getCell(entry.getValue());
          String data = produceCellType(cell);
          String fieldName = convertMap.get(entry.getKey());
          Field field = object.getClass().getDeclaredField(fieldName);
          field.setAccessible(true);
          // 根据Field的类型,来设置Field的内容
          // 以便于适应除了String外的int,long,double,float等类型的属性
          setFieldValue(object, data, field);
        }
        objects.add(object);
      }

    }
    return objects;
  }

  /**
   * @Title: matcheExcelIndexToDataForm
   * @Description: 解析excel已数组的形式返回
   * @param headNameMap headNameMap.put("CID", "customerId")
   * @param fileName
   * @param class1
   * @return
   * @throws InstantiationException
   * @throws IllegalAccessException
   * @throws IOException
   * @throws NoSuchFieldException
   * @throws SecurityException
   * @return: List<Object>
   */
  public static <T> List<T> convertExcelDataToClassDataWithPrimaryKey(Map<String, String> headNameMap, InputStream ips,
      Class<T> class1)
      throws InstantiationException, IllegalAccessException, IOException, NoSuchFieldException, SecurityException {

    List<T> objects = new ArrayList<T>(); // 返回结果集
    Map<String, Integer> indexHashMap = new HashMap<String, Integer>(); // 定位excel头文件cell位置
    if (null == headNameMap || headNameMap.size() == 0 || null == ips) {
      return objects;
    } else {
      Workbook wb = null;
      wb = new HSSFWorkbook(ips);
      Sheet sheet = wb.getSheetAt(0);
      Row rowIndexs = sheet.getRow(0);
      int cellSize = rowIndexs.getLastCellNum();
      Set<String> keys = headNameMap.keySet();
      for (String key : keys) {
        for (int i = 0; i < cellSize; i++) {
          Cell cell = rowIndexs.getCell(i);
          if (cell != null && cell.getCellType() != Cell.CELL_TYPE_BLANK) {
            if (key.equals(cell.getStringCellValue())) {
              indexHashMap.put(key, rowIndexs.getCell(i).getColumnIndex()); // 头文件push 下标位置
            }

          }

        }
      }

      for (int i = 1; i <= sheet.getLastRowNum(); i++) { // 第一行默认是下标，不算入计算结果
        Row row = sheet.getRow(i);
        T object = class1.newInstance();
        Cell flagCell = row.getCell(0);
        if (null != row && null != flagCell && HSSFCell.CELL_TYPE_BLANK != flagCell.getCellType()) {
          for (Entry<String, Integer> entry : indexHashMap.entrySet()) {
            Cell cell = row.getCell(entry.getValue());
            if (null != cell) {
              String data = produceCellType(cell);
              String fieldName = headNameMap.get(entry.getKey());
              Field field = object.getClass().getDeclaredField(fieldName);
              field.setAccessible(true);
              // 根据Field的类型,来设置Field的内容
              // 以便于适应除了String外的int,long,double,float等类型的属性
              setFieldValue(object, data, field);
            } else {
              continue;
            }
          }
          objects.add(object);
        } else {
          break;
        }
      }

    }
    return objects;
  }

  /**
   * 
   * @Title: setFieldValue 设置JavaBean属性的数据,以便于支持除了String类型外的其他数据类型
   * @Description: setFieldValue 设置JavaBean属性的数据,以便于支持除了String类型外的其他数据类型,例如int, long,double,date
   * @param object JavaBean
   * @param data 要设置的数据
   * @param field JavaBean的Field字段
   * @throws IllegalAccessException
   * @return: void
   */
  private static <T> void setFieldValue(T object, String data, Field field) throws IllegalAccessException {
    // 对field的类型进行判断,以便于支持String外的其它类型
    String fieldType = field.getType().getName();
    if (fieldType.equals("java.lang.Double") || fieldType.equals("double")) {
      // Double类型的处理
      double doubleValue = Double.parseDouble(data);
      field.set(object, doubleValue);
    } else if (fieldType.equals("java.lang.Float") || fieldType.equals("float")) {
      // Float类型的处理
      float folatValue = Float.parseFloat(data);
      field.set(object, folatValue);
    } else if (fieldType.equals("java.lang.Integer") || fieldType.equals("int")) {
      // Integer类型的处理
      int intValue = Integer.parseInt(data);
      field.set(object, intValue);
    } else if (fieldType.equals("java.lang.Long") || fieldType.equals("long")) {
      // Long类型的处理
      long longValue = Long.parseLong(data);
      field.set(object, longValue);
    } else if (field.getType().getName().equals("java.util.Date")) {
      // Date类型的处理
      // "yyyy-MM-dd HH:mm:ss",根据具体的格式来处理
      Date dateValue = strToDate(data);
      field.set(object, dateValue);
    } else {
      // String的处理
      field.set(object, data);
    }
  }

  /**
   * @Title: matcheExcelIndexToDataForm
   * @Description: 解析excel已数组的形式返回
   * @param headNameMap headNameMap.put("CID", "customerId")
   * @param fileName
   * @param class1
   * @return
   * @throws InstantiationException
   * @throws IllegalAccessException
   * @throws IOException
   * @throws NoSuchFieldException
   * @throws SecurityException
   * @return: List<Object>
   */
  public static <T> List<T> convertExcelDataToClassDataWithPrimaryKey(Map<String, String> headNameMap, String fileName,
      Class<T> class1)
      throws InstantiationException, IllegalAccessException, IOException, NoSuchFieldException, SecurityException {

    List<T> objects = new ArrayList<T>(); // 返回结果集
    Map<String, Integer> indexHashMap = new HashMap<String, Integer>(); // 定位excel头文件cell位置
    if (null == headNameMap || headNameMap.size() == 0 || null == fileName) {
      return objects;
    } else {
      Workbook wb = null;
      InputStream ips = new FileInputStream(fileName);
      wb = new HSSFWorkbook(ips);
      Sheet sheet = wb.getSheetAt(0);
      Row rowIndexs = sheet.getRow(0);
      int cellSize = rowIndexs.getLastCellNum();
      Set<String> keys = headNameMap.keySet();
      for (String key : keys) {
        for (int i = 0; i < cellSize; i++) {
          Cell cell = rowIndexs.getCell(i);
          if (cell != null && cell.getCellType() != Cell.CELL_TYPE_BLANK) {
            if (key.equals(cell.getStringCellValue())) {
              indexHashMap.put(key, rowIndexs.getCell(i).getColumnIndex()); // 头文件push 下标位置
            }

          }

        }
      }

      for (int i = 1; i <= sheet.getLastRowNum(); i++) { // 第一行默认是下标，不算入计算结果
        Row row = sheet.getRow(i);
        T object = class1.newInstance();
        Cell flagCell = row.getCell(0);
        if (null != row && null != flagCell && HSSFCell.CELL_TYPE_BLANK != flagCell.getCellType()) {
          for (Entry<String, Integer> entry : indexHashMap.entrySet()) {
            Cell cell = row.getCell(entry.getValue());
            if (null != cell) {
              String data = produceCellType(cell);
              String fieldName = headNameMap.get(entry.getKey());
              Field field = object.getClass().getDeclaredField(fieldName);
              field.setAccessible(true);
              // 根据Field的类型,来设置Field的内容
              // 以便于适应除了String外的int,long,double,float等类型的属性
              setFieldValue(object, data, field);
            } else {
              continue;
            }
          }
          objects.add(object);
        } else {
          break;
        }
      }

    }
    return objects;
  }

  /**
   * 
   * @Title: exportExcel
   * @Description: 需要先创建好excel文件,调用一次添加一次sheet信息
   * @param workbook 要添加sheet信息的excel
   * @param sheetNum sheet的编号位置,从0开始
   * @param sheetTitle 要添加sheet信息
   * @param heads 头信息
   * @param dataList 要填充的数据
   * @return
   * @throws Exception
   * @return: HSSFWorkbook 返回天填充数据后的excel
   */
  public static HSSFWorkbook fillExcelWithSheetInfo(HSSFWorkbook workbook, int sheetNum, String sheetTitle,
      List<String> heads, List<List<String>> dataList) throws IOException {
    if (StringUtils.isEmpty(sheetTitle) || null == heads || null == dataList || workbook == null) {
      return null;
    } else {
      // create sheet
      HSSFSheet sheet = workbook.createSheet();
      workbook.setSheetName(sheetNum, sheetTitle);
      // 头信息
      HSSFRow row = sheet.createRow(0);
      for (int index = 0; index < heads.size(); index++) {
        row.createCell(index).setCellValue(heads.get(index));
      }
      // 填充信息
      for (int i = 0; i < dataList.size(); i++) {
        HSSFRow row_data = sheet.createRow(i + 1);
        for (int j = 0; j < dataList.get(i).size(); j++) {
          row_data.createCell(j).setCellValue(dataList.get(i).get(j));
        }
      }
    }
    return workbook;
  }

  /**
   * 使用一个List数组,来填充要显示的excel数据
   * 
   * @param userList headMap.put("顾问ID", "userId");
   * @return
   * @throws NoSuchFieldException
   * @throws SecurityException
   * @throws IllegalArgumentException
   * @throws IllegalAccessException
   */
  public static Map<String, Object> fillExcelData(Map<String, String> headMap, List<?> objects)
      throws NoSuchFieldException, SecurityException, IllegalArgumentException, IllegalAccessException {
    Map<String, Object> resMap = new HashMap<String, Object>();
    List<String> heads = new ArrayList<String>();
    Set<String> keySet = headMap.keySet();
    List<List<String>> dataList = new ArrayList<List<String>>();
    boolean flag = true;
    if (null == objects || objects.size() < 1) {
      for (String key : keySet) {
        heads.add(key);
      }
    }
    for (Object object : objects) {
      List<String> data = new ArrayList<String>();
      for (String key : keySet) {
        if (flag == true) {
          heads.add(key);
        }
        Field userField = object.getClass().getDeclaredField(headMap.get(key));
        userField.setAccessible(true);
        String userData = String.valueOf(userField.get(object));
        // 一些特殊的判断
        if ("状态".equals(key)) {
          if ("1".equals(userData)) {
            userData = "有效";
          } else {
            userData = "无效";
          }
        }
        data.add(userData);
      }
      flag = false;
      dataList.add(data);
    }
    resMap.put("heads", heads);
    resMap.put("dataList", dataList);
    return resMap;
  }

  /**
   * 将长时间格式字符串转换为时间 yyyy-MM-dd HH:mm:ss
   * 
   * @param strDate
   * @return
   */
  private static Date strToDate(String strDate) {
    SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
    ParsePosition pos = new ParsePosition(0);
    Date strtodate = formatter.parse(strDate, pos);
    return strtodate;
  }

}
