package com.smate.web.management.service.journal;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.slf4j.Logger;
import org.springframework.util.Assert;

import com.smate.web.management.model.journal.BaseJournalTempBatch;
import com.smate.web.management.model.journal.BaseJournalTempIsiIf;

/**
 * 基础期刊工具类.
 * 
 * @author cwli
 */
@SuppressWarnings("unchecked")
public class BaseJournalUtils {
  // 每次导入的条数！
  public static int IMPORT_NUM = 5;

  public static String[] getPropertyNames(Object entity) {
    try {
      Assert.notNull(entity, "entity不能为空");
      Field[] f = entity.getClass().getDeclaredFields();
      String[] strs = new String[f.length];
      for (int i = 0; i < f.length; i++) {
        strs[i] = f[i].getName();
      }
      return strs;
    } catch (Exception e) {
      e.printStackTrace();
      return null;
    }
  }

  public static void setObjectProperty(Object entity, String propertyName, Object value) {
    try {
      Assert.notNull(entity, "entity不能为空");
      Assert.hasText(propertyName, "propertyName不能为空");
      Method[] methods = entity.getClass().getDeclaredMethods();
      String methodName;
      String methodNameFix;
      for (int i = 0; i < methods.length; i++) {
        methodName = methods[i].getName();
        methodNameFix = methodName.substring(3, methodName.length());
        methodNameFix = methodNameFix.toLowerCase();
        if (methodName.startsWith("set")) {
          if (methodNameFix.equals(propertyName.toLowerCase())) {
            try {
              methods[i].invoke(entity, new Object[] {value});
            } catch (IllegalArgumentException e) {
              e.printStackTrace();
            } catch (IllegalAccessException e) {
              e.printStackTrace();
            } catch (InvocationTargetException e) {
              e.printStackTrace();
            }
            continue;
          }
        }
      }
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  public static String[] comprableStr(String[] params1, String[] params2) {
    try {
      if (params1 == null || params2 == null) {
        return null;
      }
      String[] result = new String[params1.length];
      for (int i = 0; i < params1.length; i++) {
        for (String string : params2) {
          if (params1[i].trim().equals(string)) {
            result[i] = string;
          }
        }
      }
      return result;
    } catch (Exception e) {
      e.printStackTrace();
      return null;
    }
  }

  @SuppressWarnings({"unchecked", "deprecation"})
  public static List excelToEntity(String temp, File xlsFile, List entityList, Logger logger) {
    Assert.notNull(xlsFile, "excelFile不能为空");
    try {
      new HSSFWorkbook(new FileInputStream(xlsFile));
      return readXlsFile(temp, xlsFile, entityList, logger);
    } catch (Exception e) {
      logger.error("读取xls文件失败,开始以xlsx方式读取文件");
      try {
        new XSSFWorkbook(new FileInputStream(xlsFile));
        return readXlsxFile(temp, xlsFile, entityList, logger);
      } catch (Exception e1) {
        logger.error("读取xlsx文件出错");
        return null;
      }
    }
  }

  static List readXlsFile(String temp, File xlsFile, List entityList, Logger logger) {
    try {
      InputStream in = new BufferedInputStream(new FileInputStream(xlsFile), 16 * 1024);
      HSSFWorkbook wb = new HSSFWorkbook(in);
      HSSFSheet sheet = wb.getSheetAt(0);
      int lastRow = sheet.getLastRowNum();
      logger.debug("读取Excel数据,共{}行", lastRow);
      String[] entityProperty = null;
      int titleRowCount = 0;
      for (int i = 0; i <= lastRow; i++) {
        Object entity = null;
        if ("baseJournal".equals(temp))
          entity = new BaseJournalTempBatch();
        if ("isiIf".equals(temp))
          entity = new BaseJournalTempIsiIf();
        HSSFRow row = sheet.getRow(i);
        if (row == null) {// 读取到空白行，就直接跳过，读取下一行的数据
          continue;
        }
        int lastCell = row.getLastCellNum();
        if (i == 0) {
          logger.debug("导入的Excel数据，第{}行共有{}列", i, lastCell);
          titleRowCount = lastCell;
          String[] entityPropertyNames = BaseJournalUtils.getPropertyNames(entity);
          String[] xlsTitles = new String[lastCell];
          for (int j = 0; j < lastCell; j++) {
            HSSFCell cell = row.getCell(j);
            if (null != cell)
              xlsTitles[j] = cell.toString().trim();
          }
          entityProperty = BaseJournalUtils.comprableStr(xlsTitles, entityPropertyNames);
        } else if (i > 0) {
          for (int j = 0; j < titleRowCount; j++) {
            HSSFCell cell = row.getCell(j);
            String cellValue = cell == null ? "" : cell.toString().trim();
            if (entityProperty != null && entityProperty.length > 0) {
              if (StringUtils.equalsIgnoreCase(entityProperty[j], "ifYear")) {
                if (cellValue.indexOf(".") > -1) {
                  BaseJournalUtils.setObjectProperty(entity, entityProperty[j],
                      cellValue.substring(0, cellValue.indexOf(".")));
                } else {
                  BaseJournalUtils.setObjectProperty(entity, entityProperty[j], cellValue);
                }
              } else {
                BaseJournalUtils.setObjectProperty(entity, entityProperty[j], cellValue);
              }

            }
          }
          if (i <= 5)
            logger.debug("=====前5行的第{}:行:{}", i, entity.toString());
          entityList.add(entity);
        }
      }
    } catch (Exception e) {
      e.printStackTrace();
    }
    return entityList;
  }

  static List readXlsxFile(String temp, File file, List entityList, Logger logger) throws Exception {
    try {
      InputStream in = new BufferedInputStream(new FileInputStream(file), 16 * 1024);
      XSSFWorkbook wb = new XSSFWorkbook(in);
      XSSFSheet sheet = wb.getSheetAt(0);
      int lastRow = sheet.getLastRowNum();
      logger.debug("读取Excel数据,共{}行", lastRow);
      String[] entityProperty = null;
      int titleRowCount = 0;
      for (int i = 0; i <= lastRow; i++) {
        Object entity = null;
        if (JournalManageService.BASE_JOURNAL_TYPE.equals(temp))
          entity = new BaseJournalTempBatch();
        if (JournalManageService.ISIIF_TYPE.equals(temp))
          entity = new BaseJournalTempIsiIf();
        XSSFRow row = sheet.getRow(i);
        if (row == null) {// 读取到空白行，就直接跳过，读取下一行的数据
          continue;
        }
        int lastCell = row.getLastCellNum();
        if (i == 0) {
          logger.debug("导入的Excel数据，第{}行共有{}列", i, lastCell);
          titleRowCount = lastCell;
          String[] entityPropertyNames = BaseJournalUtils.getPropertyNames(entity);
          String[] xlsTitles = new String[lastCell];
          for (int j = 0; j < lastCell; j++) {
            XSSFCell cell = row.getCell(j);
            if (null != cell)
              xlsTitles[j] = cell.toString().trim();
          }
          entityProperty = BaseJournalUtils.comprableStr(xlsTitles, entityPropertyNames);
        } else if (i > 0) {
          for (int j = 0; j < titleRowCount; j++) {
            XSSFCell cell = row.getCell(j);
            String cellValue = cell == null ? "" : cell.toString().trim();

            if (entityProperty != null && entityProperty.length > 0) {
              if (StringUtils.equalsIgnoreCase(entityProperty[j], "ifYear")) {
                if (cellValue.indexOf(".") > -1) {
                  BaseJournalUtils.setObjectProperty(entity, entityProperty[j],
                      cellValue.substring(0, cellValue.indexOf(".")));
                } else {
                  BaseJournalUtils.setObjectProperty(entity, entityProperty[j], cellValue);
                }
              } else if (StringUtils.isNotBlank(entityProperty[j])) {
                BaseJournalUtils.setObjectProperty(entity, entityProperty[j], cellValue);
              }
            }
          }
          if (i <= 5)
            logger.debug("=====前5行的第{}:行:{}", i, entity.toString());
          entityList.add(entity);
        }
      }
    } catch (Exception e) {
      e.printStackTrace();
    }
    return entityList;
  }

  public static Integer addInteger(Integer one, Integer two) {
    return one == null ? (two == null ? 0 : two) : (two == null ? one : one + two);
  }

  public static void main(String[] a) {

    System.out.println(addInteger(new Integer(1), null));
  }

}
