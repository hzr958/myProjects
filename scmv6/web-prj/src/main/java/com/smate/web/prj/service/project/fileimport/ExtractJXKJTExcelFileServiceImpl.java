package com.smate.web.prj.service.project.fileimport;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.lang.reflect.Field;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.transaction.Transactional;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFDateUtil;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.springframework.stereotype.Service;

import com.smate.core.base.utils.common.MoneyFormatterUtils;
import com.smate.web.prj.form.fileimport.PrjFundPlanDTO;
import com.smate.web.prj.form.fileimport.PrjInfoDTO;
import com.smate.web.prj.form.fileimport.PrjMemberDTO;
import com.smate.web.prj.form.fileimport.PrjReportDTO;
import com.smate.web.prj.util.DataValidUtils;

/**
 * 提取江西科技厅Excel 文件服务
 *
 * @author aijiangbin
 * @create 2019-06-13 14:44
 **/
@Service("extractJXKJTExcelFileService")
@Transactional(rollbackOn = Exception.class)
public class ExtractJXKJTExcelFileServiceImpl extends BaseExtractFileService {

  public static List<String> PRJ_TITLE_LIST = Arrays.asList("zhTitle", "enTitle", "leader", "zhkeywords", "enKeywords",
      "zhAbstract", "enAbstract", "projectNo", "fundingYear", "agency", "scheme", "insName", "secInsName", "startDate",
      "endDate", "prjAmount", "applicationCode", "subjectCode", "economicCoode", "cseiCoode");

  public static List<String> PRJ_TITLE_MAXLEN = Arrays.asList("250", "250", "50", "500", "500", "500", "500", "20",
      "20", "20", "50", "50", "50", "20", "20", "20", "20", "20", "20", "20");

  public static List<String> PRJ_REPORT_LIST = Arrays.asList("prjNo", "seqNo", "abortDate", "reportType");

  public static List<String> PRJ_REPORT_MAXLEN = Arrays.asList("20", "4", "20", "50");

  public static List<String> PRJ_MEMBER_LIST =
      Arrays.asList("prjNo", "seqNo", "name", "email", "mobile", "openId", "insName", "isLeader");

  public static List<String> PRJ_MEMBER_MAXLEN = Arrays.asList("20", "4", "100", "100", "20", "10", "100", "100");

  public static List<String> PRJ_FUND_LIST =
      Arrays.asList("prjNo", "seqNo", "itemName", "itemAmout", "ptAmout", "zcAmout", "remark", "pSeq");

  public static List<String> PRJ_FUND_MAXLEN = Arrays.asList("20", "4", "100", "20", "20", "20", "200", "20");

  public static List<String> REPORT_TYPE = Arrays.asList("进展报告", "中期报告", "审计报告", "结题报告", "验收报告");

  public static List<String> PRINC_List = Arrays.asList("是", "否");


  @Override
  public Map<String, Object> checkFile(File file, String sourceFileFileName) {
    Map<String, Object> result = new HashMap<>();
    String suffix = sourceFileFileName.substring(sourceFileFileName.lastIndexOf("."));
    if (!".xls".equalsIgnoreCase(suffix) && !".xlsx".equalsIgnoreCase(suffix)) {
      result.put("warnmsg", "文件格式错误");
      return result;
    }
    return null;
  }

  @Override
  public List<PrjInfoDTO> parseFile(File sourceFile) {
    List<PrjInfoDTO> list = new ArrayList<>();
    try {
      InputStream in = new BufferedInputStream(new FileInputStream(sourceFile), 15 * 1024);
      HSSFWorkbook book = new HSSFWorkbook(in); // 得到Excel工作薄
      for (int index = 0; index < book.getNumberOfSheets(); index++) {
        HSSFSheet sheet = book.getSheetAt(index);
        // excel 计算行数是从0为基数，不是从1开始，所以应该+1，具体原因可见HSSRow.class 的getRowNum方法说明
        int rowsLen = sheet.getLastRowNum() + 1; // 得到工作表的行数
        if (rowsLen < 2) {
          break;
        }
        if (index > 4) {
          break;
        }
        switch (index) {
          case 0:
            buildPrjInfoList(list, index, sheet, rowsLen);
            break;
          case 1:
            buildReportInfoList(list, index, sheet, rowsLen);
            break;
          case 2:
            buildMemberInfoList(list, index, sheet, rowsLen);
            break;
          case 3:
            buildfundInfoList(list, index, sheet, rowsLen);
            break;
        }
      }

    } catch (Exception e) {
      logger.error("将Excel文件拆分成 prjInfo 对象异常", e);
    }
    return list;
  }

  /**
   * 获取项目列表信息
   * 
   * @param list
   * @param index
   * @param sheet
   * @param rowsLen
   * @throws Exception
   */
  private void buildPrjInfoList(List<PrjInfoDTO> list, int index, HSSFSheet sheet, int rowsLen) throws Exception {
    try {
      for (int rowIndex = 1; rowIndex < rowsLen; rowIndex++) {
        HSSFRow row = sheet.getRow(rowIndex);
        if (!checkParam(row, index)) {
          continue;
        }
        int cellLen = getRowLen(index);
        PrjInfoDTO info = new PrjInfoDTO();
        info.setTemplate("JXKJT");
        list.add(info);
        for (int cellIndex = 0; cellIndex < cellLen; cellIndex++) {
          HSSFCell cell = row.getCell(cellIndex);
          if (null != cell) {
            List<String> cellInfo = getCellInfo(index, cellIndex);
            String cellField = cellInfo.get(0);
            String maxLength = cellInfo.get(1);
            String value = "";
            value = getCellValue(cell, cellField, maxLength, value);
            Field f = info.getClass().getDeclaredField(cellField);
            f.setAccessible(true);
            f.set(info, value);
          }
        }
      }
    } catch (Exception e) {
      logger.error("解析构建excel中的项目基本信息数据出错", e);
    }
  }

  /**
   * 获取项目报告信息
   * 
   * @param list
   * @param index
   * @param sheet
   * @param rowsLen
   * @throws Exception
   */
  private void buildReportInfoList(List<PrjInfoDTO> list, int index, HSSFSheet sheet, int rowsLen) throws Exception {
    if (CollectionUtils.isEmpty(list))
      return;
    try {
      for (int rowIndex = 1; rowIndex < rowsLen; rowIndex++) {
        HSSFRow row = sheet.getRow(rowIndex);
        if (!checkParam(row, index)) {
          continue;
        }
        int cellLen = getRowLen(index);
        PrjReportDTO info = new PrjReportDTO();
        for (int cellIndex = 0; cellIndex < cellLen; cellIndex++) {
          HSSFCell cell = row.getCell(cellIndex);
          if (null != cell) {
            List<String> cellInfo = getCellInfo(index, cellIndex);
            String cellField = cellInfo.get(0);
            String maxLength = cellInfo.get(1);
            String value = "";
            value = getCellValue(cell, cellField, maxLength, value);
            Field f = info.getClass().getDeclaredField(cellField);
            f.setAccessible(true);
            f.set(info, value);
          }
        }
        list.forEach(i -> {
          if (StringUtils.isNotBlank(i.getProjectNo()) && i.getProjectNo().equals(info.getPrjNo())) {
            i.getPrjReportDTOS().add(info);
          }
        });
      }
    } catch (Exception e) {
      logger.error("解析构建excel中的项目报告数据出错", e);
    }
  }

  /**
   * 获取项目经费信息
   * 
   * @param list
   * @param index
   * @param sheet
   * @param rowsLen
   * @throws Exception
   */
  private void buildfundInfoList(List<PrjInfoDTO> list, int index, HSSFSheet sheet, int rowsLen) throws Exception {
    if (CollectionUtils.isEmpty(list))
      return;
    try {
      for (int rowIndex = 1; rowIndex < rowsLen; rowIndex++) {
        HSSFRow row = sheet.getRow(rowIndex);
        if (!checkParam(row, index)) {
          continue;
        }
        int cellLen = getRowLen(index);
        PrjFundPlanDTO info = new PrjFundPlanDTO();
        for (int cellIndex = 0; cellIndex < cellLen; cellIndex++) {
          HSSFCell cell = row.getCell(cellIndex);
          if (null != cell) {
            List<String> cellInfo = getCellInfo(index, cellIndex);
            String cellField = cellInfo.get(0);
            String maxLength = cellInfo.get(1);
            String value = "";
            value = getCellValue(cell, cellField, maxLength, value);
            Field f = info.getClass().getDeclaredField(cellField);
            f.setAccessible(true);
            f.set(info, value);
          }
        }
        list.forEach(i -> {
          if (StringUtils.isNotBlank(i.getProjectNo()) && i.getProjectNo().equals(info.getPrjNo())) {
            i.getPrjFundPlanDTOS().add(info);
          }
        });
      }
    } catch (Exception e) {
      logger.error("解析构建excel中的项目经费数据出错", e);
    }
  }

  /**
   * 解析并 获取 cell中值
   * 
   * @param cell
   * @param cellField
   * @param maxLength
   * @param value
   * @return
   * @throws Exception
   */
  private String getCellValue(HSSFCell cell, String cellField, String maxLength, String value) throws Exception {
    if (HSSFCell.CELL_TYPE_NUMERIC == cell.getCellType()) {
      if (HSSFDateUtil.isCellDateFormatted(cell)) {
        Date d = cell.getDateCellValue();
        SimpleDateFormat formater = new SimpleDateFormat("yyyy-MM-dd");
        value = getCellValue(formater.format(d), maxLength);
      } else {
        value = cell.toString();
        if (StringUtils.isNotBlank(value)) {
          if ("amount".equals(cellField.toLowerCase())) {
            value = MoneyFormatterUtils.format(value);
          } else {
            value = String.valueOf(Double.valueOf(value).intValue());
          }
          value = getCellValue(value, maxLength);
        }
      }

    } else {
      value = getCellValue(cell.toString(), maxLength);
    }
    return value;
  }

  /**
   * 获取项目成员信息
   * 
   * @param list
   * @param index
   * @param sheet
   * @param rowsLen
   * @throws Exception
   */
  private void buildMemberInfoList(List<PrjInfoDTO> list, int index, HSSFSheet sheet, int rowsLen) throws Exception {
    if (CollectionUtils.isEmpty(list))
      return;
    try {
      for (int rowIndex = 1; rowIndex < rowsLen; rowIndex++) {
        HSSFRow row = sheet.getRow(rowIndex);
        if (!checkParam(row, index)) {
          continue;
        }
        int cellLen = getRowLen(index);
        PrjMemberDTO info = new PrjMemberDTO();
        for (int cellIndex = 0; cellIndex < cellLen; cellIndex++) {
          HSSFCell cell = row.getCell(cellIndex);
          if (null != cell) {
            List<String> cellInfo = getCellInfo(index, cellIndex);
            String cellField = cellInfo.get(0);
            String maxLength = cellInfo.get(1);
            String value = "";
            value = getCellValue(cell, cellField, maxLength, value);
            Field f = info.getClass().getDeclaredField(cellField);
            f.setAccessible(true);
            f.set(info, value);
          }
        }
        list.forEach(i -> {
          if (StringUtils.isNotBlank(i.getProjectNo()) && i.getProjectNo().equals(info.getPrjNo())) {
            i.getMembers().add(info);
          }
        });
      }
    } catch (Exception e) {
      logger.error("解析构建excel中的项目成员数据出错", e);
    }
  }



  /**
   * list.get(0) = field list.get(1) = field.maxLen
   * 
   * @param sheeetSeq
   * @param rowIndex
   * @return
   */
  public List<String> getCellInfo(int sheeetSeq, int rowIndex) {
    List<String> list = new ArrayList<>();
    switch (sheeetSeq) {
      case 0:
        list.add(PRJ_TITLE_LIST.get(rowIndex));
        list.add(PRJ_TITLE_MAXLEN.get(rowIndex));
        break;
      case 1:;
        list.add(PRJ_REPORT_LIST.get(rowIndex));
        list.add(PRJ_REPORT_MAXLEN.get(rowIndex));
        break;
      case 2:;
        list.add(PRJ_MEMBER_LIST.get(rowIndex));
        list.add(PRJ_MEMBER_MAXLEN.get(rowIndex));
        break;
      case 3:;
        list.add(PRJ_FUND_LIST.get(rowIndex));
        list.add(PRJ_FUND_MAXLEN.get(rowIndex));
        break;
    }
    return list;
  }

  public int getRowLen(int sheeetSeq) {
    switch (sheeetSeq) {
      case 0:
        return PRJ_TITLE_LIST.size();
      case 1:;
        return PRJ_REPORT_LIST.size();
      case 2:;
        return PRJ_MEMBER_LIST.size();
      case 3:;
        return PRJ_FUND_LIST.size();
    }
    return 0;
  }

  private String getCellValue(String inputText, String limitLen) throws Exception {
    try {
      if (StringUtils.isBlank(inputText) || StringUtils.isBlank(limitLen)) {
        return inputText;
      }
      int maxLen = Double.valueOf(limitLen).intValue();
      if (inputText.length() > maxLen) {
        return inputText.substring(0, maxLen);
      }
    } catch (Exception e) {
      e.printStackTrace();
      throw e;
    }
    return inputText;

  }

  public boolean checkParam(HSSFRow row, int index) {
    switch (index) {
      case 0:
        return checkPrjInfo(row);
      case 1:
        return checkPrjReport(row);
      case 2:
        return checkPrjmember(row);
      case 3:
        return checkPrjFund(row);
    }
    return false;
  }

  /**
   * 批准号0、序号1、提交截止时间2、报告类型3均有值
   * 
   * @param row
   * @return
   */
  private boolean checkPrjReport(HSSFRow row) {
    SimpleDateFormat formater = new SimpleDateFormat("yyyy-MM-dd");
    if (row == null) {
      return false;
    }
    // 需要校验的行号
    int[] mustSet = new int[] {0, 1, 2, 3};
    for (int i = 0; i < mustSet.length; i++) {
      int index = mustSet[i];
      String data = "";
      if (null != row.getCell(index)) {
        data = row.getCell(index).toString().trim();
      }
      if (StringUtils.isBlank(data))
        return false;
      // 检验日期
      if (index == 2) {
        data = formater.format(row.getCell(index).getDateCellValue());
        if (!DataValidUtils.validDate(data))
          return false;
      }
      if (index == 3 && !REPORT_TYPE.contains(data))
        return false;
    }

    return true;
  }

  /**
   * 批准号0，序号1、姓名2、是否负责人有值7则此行有效
   * 
   * @param row
   * @return
   */
  private boolean checkPrjmember(HSSFRow row) {
    if (row == null) {
      return false;
    }
    // 需要校验的行号
    int[] mustSet = new int[] {0, 1, 2, 7};
    for (int i = 0; i < mustSet.length; i++) {
      int index = mustSet[i];
      String data = "";
      if (null != row.getCell(index)) {
        data = row.getCell(index).toString().trim();
      }
      if (StringUtils.isBlank(data))
        return false;
      if (index == 7 && !PRINC_List.contains(data))
        return false;
    }
    return true;
  }

  /**
   * 批准号0、序号1、科目名2有值则此行有效
   * 
   * @param row
   * @return
   */
  private boolean checkPrjFund(HSSFRow row) {
    if (row == null) {
      return false;
    }
    // 需要校验的行号
    int[] mustSet = new int[] {0, 1, 2, 3, 4, 5};
    for (int i = 0; i < mustSet.length; i++) {
      int index = mustSet[i];
      String data = "";
      if (null != row.getCell(index)) {
        data = row.getCell(index).toString().trim();
      }
      // 经费金额不合法的数据不导入 项目经费表3-5列都是金额（可以为空）
      if (index == 3 || index == 4 || index == 5) {
        // 不为空才验证
        if (StringUtils.isNotBlank(data) && !DataValidUtils.validAmountF(data))
          return false;
        continue;
      }
      if (StringUtils.isBlank(data)) {
        return false;
      }

    }
    return true;
  }

  /**
   * 必填：中文项目名称0、批准号7、立项年份8、资助机构名称9、开始时间13、结束时间14。资助金额15立项年份为4位数字
   * 
   * @param row
   * @return
   */
  private boolean checkPrjInfo(HSSFRow row) {
    SimpleDateFormat formater = new SimpleDateFormat("yyyy-MM-dd");
    // 需要校验的行号
    int[] mustSet = new int[] {0, 7, 8, 9, 13, 14, 15};
    for (int i = 0; i < mustSet.length; i++) {
      int index = mustSet[i];
      String data = "";
      if (null != row.getCell(index))
        data = row.getCell(index).toString().trim();
      if (index == 15) {
        // 不为空资助金额才校验
        if (StringUtils.isNotBlank(data) && !DataValidUtils.validAmountI(data))
          return false;
        continue;
      }

      if (StringUtils.isBlank(data)) {
        return false;
      }
      // 立项年份保证4位
      if (mustSet[i] == 8 && !DataValidUtils.validYear(data)) {
        return false;
      }
      // 检验日期
      if (index == 13 || index == 14) {
        data = formater.format(row.getCell(index).getDateCellValue());
        if (!DataValidUtils.validDate(data))
          return false;
      }


    }
    return true;
  }


}
