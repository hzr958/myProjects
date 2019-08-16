package com.smate.web.management.service.grp;

import com.smate.web.management.model.grp.GrpItemInfo;
import org.apache.commons.lang.StringUtils;
import org.apache.poi.hssf.usermodel.*;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.lang.reflect.Field;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * 提取Excel 文件服务
 *
 * @author aijiangbin
 * @create 2019-06-13 14:44
 **/
@Service("extractExcelFileService")
@Transactional (rollbackOn = Exception.class)
public class ExtractExcelFileServiceImpl  extends  BaseExtractFileService{

  public static List<String> GRP_TITLE_LIST = Arrays.asList("groupName", "groupType", "breif", "smateFirstCategoryId",
      "smateSecondCategoryId", "nsfcCategoryId", "keyword", "openType", "openModule", "ownerPsnId");//
  public static List<String> GRP_MAXLEN_LIST = Arrays.asList("200", "20", "2000", "20",
      "20", "20", "650", "20", "20","20");
  @Override
  public Map<String, Object> checkFile(File file ,String sourceFileFileName) {
    Map result = new HashMap();
    if(StringUtils.isBlank(sourceFileFileName)){
      result.put("warnmsg","文件错误") ;
      return  result;
    }
    String suffix =  sourceFileFileName.substring(sourceFileFileName.lastIndexOf(".")) ;
    if(!".xls".equalsIgnoreCase(suffix) && !".xlsx".equalsIgnoreCase(suffix)){
      result.put("warnmsg","文件格式错误") ;
      return result;
    }
    return null;
  }

  @Override
  public List<GrpItemInfo> parseFile(File sourceFile) {
    List<GrpItemInfo>  list = new ArrayList<>();
    try {
      InputStream in = new BufferedInputStream(new FileInputStream(sourceFile), 15 * 1024);
      HSSFWorkbook book = new HSSFWorkbook(in); // 得到Excel工作薄
      Integer seqNo = 0;
      for (int index = 0; index < book.getNumberOfSheets(); index++) {
        HSSFSheet sheet = book.getSheetAt(index);
        int rowsLen = sheet.getLastRowNum()+10; // 得到工作表的行数
        if (rowsLen < 2) {
          continue;
        }
        if (index > 1) {
          break;
        }
        int cellLen = GRP_TITLE_LIST.size();
        for (int rowIndex = 1; rowIndex < rowsLen; rowIndex++) {
          HSSFRow row = sheet.getRow(rowIndex);
          String title = ""; // 标题必填
          String groupType = ""; // 群组类型
          String  openType= ""; // 公开类型
          if (row != null && null != row.getCell(0))
              title = row.getCell(0).toString().trim();
          if (StringUtils.isBlank(title)) {
             continue;
          }
          if (null != row.getCell(1)) {
            groupType = row.getCell(1).toString().trim();
          }
          if ("".equals(groupType)) {
            continue;
          }
          if (null != row.getCell(7)) {
            openType = row.getCell(7).toString().trim();
          }
          if ("".equals(openType)) {
            continue;
          }
          GrpItemInfo info = new GrpItemInfo();
          list.add(info);
          //info.setSourceDbCode("SCMEXCEL") ;
          for (int cellIndex = 0; cellIndex < cellLen; cellIndex++) {
            HSSFCell cell = row.getCell(cellIndex);
            if (null != cell) {
              // 日期数据类型处理.
              String value = "";
              if (HSSFCell.CELL_TYPE_NUMERIC == cell.getCellType()) {
                if (HSSFCell.CELL_TYPE_NUMERIC == cell.getCellType()) {
                  if (HSSFDateUtil.isCellDateFormatted(cell)) {
                    Date d = cell.getDateCellValue();
                    SimpleDateFormat formater = new SimpleDateFormat("yyyy-MM-dd");
                    value = getCellValue(formater.format(d), GRP_MAXLEN_LIST.get(cellIndex));
                  } else {
                    value = cell.toString();
                    if (StringUtils.isNotBlank(value)) {
                      DecimalFormat df = new DecimalFormat("0");
                      value = df.format(cell.getNumericCellValue());
                    }
                  }
                }

              }else{
                 value = getCellValue(cell.toString(), GRP_MAXLEN_LIST.get(cellIndex));
              }
              Field f = info.getClass().getDeclaredField(GRP_TITLE_LIST.get(cellIndex));
              f.setAccessible(true);
              f.set(info, value);
            }
          }
        }
      }

    } catch (Exception e) {
      logger.error("将Excel文件拆分成  Grp对象异常", e);
    }
    return list;
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
}
