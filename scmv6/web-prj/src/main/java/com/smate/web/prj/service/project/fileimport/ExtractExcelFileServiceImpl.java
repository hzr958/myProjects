package com.smate.web.prj.service.project.fileimport;

import com.smate.core.base.utils.common.MoneyFormatterUtils;
import com.smate.web.prj.form.fileimport.PrjInfoDTO;
import com.smate.web.prj.form.fileimport.PrjMemberDTO;
import org.apache.commons.lang.StringUtils;
import org.apache.poi.hssf.usermodel.*;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.lang.reflect.Field;
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

  public static List<String> PRJ_TITLE_LIST = Arrays.asList("zhTitle", "leader", "agency", "scheme",
      "projectNo", "insName", "fundingYear", "prjType", "prjStatus", "prjAmount", "startDate",
      "endDate", "zhAbstract", "scienceArea", "zhkeywords", "fullLink","remark","prjMembers");// 8 - PRJ_TYPE
  public static List<String> PRJ_MAXLEN_LIST = Arrays.asList("500", "20", "100", "100",
      "20", "100", "20", "20", "20","20","20",
      "20", "2000", "100", "200", "200", "200", "200");
  @Override
  public Map<String, Object> checkFile(File file ,String sourceFileFileName) {
    Map result = new HashMap();
    String suffix =  sourceFileFileName.substring(sourceFileFileName.lastIndexOf(".")) ;
    if(!".xls".equalsIgnoreCase(suffix) && !".xlsx".equalsIgnoreCase(suffix)){
      result.put("warnmsg","文件格式错误") ;
      return result;
    }
    return null;
  }

  @Override
  public List<PrjInfoDTO> parseFile(File sourceFile) {
    List<PrjInfoDTO>  list = new ArrayList<>();
    try {
      InputStream in = new BufferedInputStream(new FileInputStream(sourceFile), 15 * 1024);
      HSSFWorkbook book = new HSSFWorkbook(in); // 得到Excel工作薄
      Integer seqNo = 0;
      for (int index = 0; index < book.getNumberOfSheets(); index++) {
        HSSFSheet sheet = book.getSheetAt(index);
        int rowsLen = sheet.getLastRowNum(); // 得到工作表的行数
        if (rowsLen < 2) {
          continue;
        }
        if (index > 1) {
          break;
        }
        int cellLen = PRJ_TITLE_LIST.size();
        for (int rowIndex = 1; rowIndex < rowsLen; rowIndex++) {
          HSSFRow row = sheet.getRow(rowIndex);
          String title = ""; // 标题必填
          String insName = ""; // 标题必填
          if (null != row.getCell(0))
              title = row.getCell(0).toString().trim();
          if (StringUtils.isBlank(title)) {
             continue;
          }
          if (null != row.getCell(5)) {
            insName = row.getCell(5).toString().trim();
          }
          if ("".equals(insName)) {
            continue;
          }
          PrjInfoDTO info = new PrjInfoDTO();
          list.add(info);
          info.setSourceDbCode("SCMEXCEL") ;
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
                    value = getCellValue(formater.format(d), PRJ_MAXLEN_LIST.get(cellIndex));
                  } else {
                    value = cell.toString();
                    if (StringUtils.isNotBlank(value)) {
                      if ("amount".equals(PRJ_TITLE_LIST.get(cellIndex).toLowerCase())) {
                        value = MoneyFormatterUtils.format(value);
                      } else {
                        value = String.valueOf(Double.valueOf(value).intValue());
                      }
                      value = getCellValue(value, PRJ_MAXLEN_LIST.get(cellIndex));
                    }
                  }
                }

              }else{
                 value = getCellValue(cell.toString(), PRJ_MAXLEN_LIST.get(cellIndex));
              }
              Field f = info.getClass().getDeclaredField(PRJ_TITLE_LIST.get(cellIndex));
              f.setAccessible(true);
              f.set(info, value);
            }
          }
          // 处理作者
          buildAuthors(info);
        }
      }

    } catch (Exception e) {
      logger.error("将Excel文件拆分成 prjInfo 对象异常", e);
    }
    return list;
  }


  /**
   * 处理作者
   * @param prjInfo
   */
  private void buildAuthors(PrjInfoDTO prjInfo) {

    PrjMemberDTO  leader =  null ;
    int seq = 1 ;
    if(StringUtils.isNotBlank(prjInfo.getLeader())){
      leader = new PrjMemberDTO();
      leader.setName(prjInfo.getLeader());
      leader.setSeqNo(seq+"");
      seq ++ ;
      leader.setIsLeader("是");
      prjInfo.getMembers().add(leader);
    }
    if(StringUtils.isNotBlank( prjInfo.getPrjMembers())){
      String[] split = prjInfo.getPrjMembers().split(splitNameReg);
      boolean flag = false ;
      for(String name : split){
        if(leader != null && prjInfo.getLeader().equals(name)){
          break;
        }
        PrjMemberDTO member = new PrjMemberDTO();
        member.setName(name);
        member.setSeqNo(seq+"");
        seq ++ ;
        prjInfo.getMembers().add(member);
      }
    }
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
