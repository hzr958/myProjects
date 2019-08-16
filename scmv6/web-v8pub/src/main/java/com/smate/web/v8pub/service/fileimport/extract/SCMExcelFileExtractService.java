package com.smate.web.v8pub.service.fileimport.extract;

import java.io.IOException;
import java.io.InputStream;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import org.apache.commons.lang3.StringUtils;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFDateUtil;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.multipart.MultipartFile;

import com.smate.core.base.pub.enums.PubBookTypeEnum;
import com.smate.core.base.pub.enums.PubConferencePaperTypeEnum;
import com.smate.core.base.pub.enums.PubPatentAreaEnum;
import com.smate.core.base.pub.enums.PubPatentTransitionStatusEnum;
import com.smate.core.base.pub.enums.PubSCAcquisitionTypeEnum;
import com.smate.core.base.pub.enums.PubSCScopeTypeEnum;
import com.smate.core.base.pub.enums.PubStandardTypeEnum;
import com.smate.core.base.pub.enums.PubThesisDegreeEnum;
import com.smate.core.base.pub.util.PubParamUtils;
import com.smate.core.base.utils.number.NumberUtils;

public class SCMExcelFileExtractService extends FileExtractBaseService {
  private Logger logger = LoggerFactory.getLogger(getClass());

  @Override
  public List<Map<String, String>> fileExtractToMap(MultipartFile sourceFile) {
    InputStream in = null;
    List<Map<String, String>> pubList = new ArrayList<>();
    try {
      in = sourceFile.getInputStream();
      HSSFWorkbook book = new HSSFWorkbook(in); // 得到Excel工作薄
      Map<String, String> pubInfoMap = null;
      Integer seqNo = 0;
      for (int index = 0; index < book.getNumberOfSheets(); index++) {
        HSSFSheet sheet = book.getSheetAt(index);
        int rowsLen = sheet.getLastRowNum(); // 得到工作表的行数
        if (rowsLen < 2) {
          continue;
        }

        List<String> listTitle = new ArrayList<String>(); // 取得字段模板
        List<String> listMaxLen = new ArrayList<String>();

        HSSFRow rowTitle = sheet.getRow(0);
        HSSFRow rowLimit = sheet.getRow(1);

        // 如果每页的最左上角没有发现ctitle或etitle，则跳到下一页导入
        if (!"title".equalsIgnoreCase(rowTitle.getCell(0).toString())) {
          continue;
        }
        for (int cellIndex = 0; cellIndex < rowTitle.getLastCellNum(); cellIndex++) {
          String content = "";
          if (null != rowTitle.getCell(cellIndex))
            content = rowTitle.getCell(cellIndex).toString();
          if (content.equalsIgnoreCase(""))
            break;
          listTitle.add(content);
          listMaxLen.add(Objects.toString(rowLimit.getCell(cellIndex), ""));
        }
        int cellLen = listTitle.size();
        String pubType = "";
        String sheetName = book.getSheetName(index);
        logger.info("导入类型：{}", sheetName);
        if ("奖励".equalsIgnoreCase(sheetName) || "Award".equalsIgnoreCase(sheetName)) {
          pubType = "1";
        } else if ("书(著作)".equalsIgnoreCase(sheetName) || "书".equalsIgnoreCase(sheetName)
            || "Book(Monograph)".equalsIgnoreCase(sheetName)) {
          pubType = "2";
        } else if ("会议论文".equalsIgnoreCase(sheetName) || "Conference Paper".equalsIgnoreCase(sheetName)) {
          pubType = "3";
        } else if ("期刊论文".equalsIgnoreCase(sheetName) || "期刊文章".equalsIgnoreCase(sheetName)
            || "Journal Article".equalsIgnoreCase(sheetName)) {
          pubType = "4";
        } else if ("专利 ".equalsIgnoreCase(sheetName) || "Patent".equalsIgnoreCase(sheetName)) {// SCM00005339
          pubType = "5";
        } else if ("学位论文".equalsIgnoreCase(sheetName) || "Thesis".equalsIgnoreCase(sheetName)) {
          pubType = "8";
        } else if ("书籍章节".equalsIgnoreCase(sheetName) || "Book Chapter".equalsIgnoreCase(sheetName)) {
          pubType = "10";
        } else if ("标准".equalsIgnoreCase(sheetName) || "Standard".equalsIgnoreCase(sheetName)) {
          pubType = "12";
        } else if ("软件著作权".equalsIgnoreCase(sheetName) || "Software Copyright".equalsIgnoreCase(sheetName)) {
          pubType = "13";
        } else {
          pubType = "7";
        }

        for (int rowIndex = 3; rowIndex <= rowsLen; rowIndex++) {

          HSSFRow row = sheet.getRow(rowIndex);
          String title = "";
          if (null == row)
            continue;
          if (null != row.getCell(0))
            title = row.getCell(0).toString().trim();
          // 只要发现有一列的ctitle,etitle为空，则此行及之后的行都不再取
          if ("".equals(title)) {
            break;
          }
          pubInfoMap = new HashMap<>();
          pubInfoMap.put("seq_no", seqNo.toString());
          seqNo++;
          pubInfoMap.put("pub_type", pubType);
          pubInfoMap.put("source_db_code", "SCMEXCEL");
          for (int cellIndex = 0; cellIndex < cellLen; cellIndex++) {
            HSSFCell cell = row.getCell(cellIndex);
            if (null != cell) {

              // 日期数据类型处理.
              if (HSSFCell.CELL_TYPE_NUMERIC == cell.getCellType()) {
                if (HSSFDateUtil.isCellDateFormatted(cell)) {
                  Date d = cell.getDateCellValue();
                  SimpleDateFormat formater = new SimpleDateFormat("yyyy-MM-dd");
                  pubInfoMap.put(listTitle.get(cellIndex), getCellValue(formater.format(d), listMaxLen.get(cellIndex)));
                } else {
                  String value = cell.toString();
                  if (StringUtils.isNotBlank(value)) {
                    // excel文件，公开号和专利号，如果输入的是纯数字，导入后小数点后面的内容会丢失
                    // String.valueOf(Double.valueOf(value).intValue());
                    /*
                     * if (value.indexOf(".0") > -1) {// 数字后面不要加上.0 value = value.substring(0, value.indexOf(".0")); }
                     */
                    DecimalFormat df = new DecimalFormat("0");
                    value = df.format(cell.getNumericCellValue());
                    pubInfoMap.put(listTitle.get(cellIndex), getCellValue(value, listMaxLen.get(cellIndex)));
                  }
                }
              } else {
                String value = getCellValue(cell.toString(), listMaxLen.get(cellIndex));
                if ("publish_status".equals(listTitle.get(cellIndex))) {
                  pubInfoMap.put(listTitle.get(cellIndex), value.indexOf("A") < 0 ? "P" : "A");
                  // if(!"patent_status".equals(listTitle.get(cellIndex))&&!"publication_status".equals(listTitle.get(cellIndex))){
                  // 由fzq确认,SCM-16354-文件导入-xls-专利-专利状态由用户选择,专利状态为空时再看开始生效日期,开始生效日期有值专利状态则为授权,否则为申请
                  // 由fzq确认,SCM-16354-文件导入-xls-书(著作)-出版状态-用户选择什么就是什么.与其他无关
                } else if ("patent_status".equals(listTitle.get(cellIndex))) {
                  if (StringUtils.isNotBlank(value)) {
                    pubInfoMap.put(listTitle.get(cellIndex), value.substring(0, 1));
                  }
                } else if ("patent_type".equals(listTitle.get(cellIndex))) {
                  if (StringUtils.isNotBlank(value)) {
                    pubInfoMap.put(listTitle.get(cellIndex), value.substring(0, 2));
                  }
                } else {
                  pubInfoMap.put(listTitle.get(cellIndex), value);
                }
              }

            }
          }

          pubList.add(pubInfoMap);

        }

      }

    } catch (Exception e) {
      logger.error("将Excel文件拆分成xml格式字符串时出错", e);
    } finally {
      if (in != null) {
        try {
          in.close();
        } catch (IOException e) {
          logger.error("关闭文件流异常", e);
        }
      }
    }

    return pubList;
  }

  /**
   * 获取限制指定长底的字符串.
   * 
   * @param inputText
   * @param limitLen
   * @return
   */
  private String getCellValue(String inputText, String limitLen) {
    try {
      if (StringUtils.isBlank(inputText) || StringUtils.isBlank(limitLen))
        return inputText;
      int maxLen = Double.valueOf(limitLen).intValue();
      if (inputText.length() > maxLen)
        return inputText.substring(0, maxLen);

    } catch (Exception e) {
    }
    return inputText;

  }

  @Override
  public List<PubFileInfo> processData(List<Map<String, String>> list) {
    List<PubFileInfo> pubFileInfoList = new ArrayList<>();
    if (list != null && list.size() > 0) {
      PubFileInfo pubInfo = null;
      int seqNo = 1;
      // StringUtils.isNotBlank(map.get("ctitle"))?map.get("ctitle"):map.get("etitle")
      for (Map<String, String> map : list) {
        pubInfo = new PubFileInfo();
        pubInfo.setSeqNo(seqNo++);
        pubInfo.setSourceDbCode(map.get("source_db_code"));
        pubInfo.setTitle(map.get("title"));
        pubInfo.setAuthorNames(map.get("author_names"));
        pubInfo.setOrganization(map.get("ins_names"));
        pubInfo.setPubyear(map.get("publish_date"));
        pubInfo.setVolumeNo(map.get("volume"));
        pubInfo.setIssue(map.get("issue"));
        pubInfo.setPubType(parserStringToInt(map.get("pub_type")));
        pubInfo.setOriginal(map.get("journal_name"));
        String startPage = map.get("start_page");
        String endPage = map.get("end_page");
        String articleNo = map.get("article_no");
        String pageNumber = PubParamUtils.buildPageNumber(startPage, endPage, articleNo);
        if (pubInfo.getPubType() == 4) {
          pageNumber = map.get("page_number");
        }
        pubInfo.setPageNumber(pageNumber);
        pubInfo.setISBN(map.get("isbn"));
        pubInfo.setIssn(map.get("issn"));

        // pubInfo.setRegNo(regNo);
        // pubInfo.setCity(map.get("city"));
        pubInfo.setCountry(map.get("country"));
        pubInfo.setFundInfo(map.get("fundinfo"));
        pubInfo.setCitations(NumberUtils.parseInt(map.get("citations"), null));
        pubInfo.setDoi(map.get("doi"));
        pubInfo.setCabstract(map.get("summary"));
        pubInfo.setKeywords(map.get("keywords"));
        pubInfo.setSrcFulltextUrl(map.get("fulltext_url"));

        buildSistationInfo(pubInfo, map);

        buildOtherInfo(pubInfo, map);
        pubFileInfoList.add(pubInfo);
      }
    }
    return pubFileInfoList;
  }

  /**
   * 构建收录信息
   * 
   * @param pubInfo
   * @param map
   */
  private void buildSistationInfo(PubFileInfo pubInfo, Map<String, String> map) {
    String scie = map.get("list_scie");
    String ssci = map.get("list_ssci");
    String istp = map.get("list_istp");
    String ei = map.get("list_ei");
    /*
     * //这三个以后改 String cssci = map.get("list_cssci"); String pku = map.get("list_pku"); String other =
     * map.get("list_other");
     */
    List<SitationInfo> sitationInfoList = new ArrayList<>();
    pubInfo.setSitationJson(sitationInfoList);

    if (StringUtils.isNotBlank(scie)) {
      SitationInfo sitationInfo = new SitationInfo();
      sitationInfo.setLibraryName("sci");
      if (scie.equals("是") || scie.equalsIgnoreCase("yes")) {
        sitationInfo.setSitStatus(1);
      } else {
        sitationInfo.setSitStatus(0);
      }
      sitationInfoList.add(sitationInfo);
    }
    if (StringUtils.isNotBlank(ssci)) {
      SitationInfo sitationInfo = new SitationInfo();
      sitationInfo.setLibraryName("ssci");
      if (ssci.equals("是") || ssci.equalsIgnoreCase("yes")) {
        sitationInfo.setSitStatus(1);
      } else {
        sitationInfo.setSitStatus(0);
      }
      sitationInfoList.add(sitationInfo);
    }
    if (StringUtils.isNotBlank(istp)) {
      SitationInfo sitationInfo = new SitationInfo();
      sitationInfo.setLibraryName("istp");
      if (istp.equals("是") || istp.equalsIgnoreCase("yes")) {
        sitationInfo.setSitStatus(1);
      } else {
        sitationInfo.setSitStatus(0);
      }
      sitationInfoList.add(sitationInfo);
    }
    if (StringUtils.isNotBlank(ei)) {
      SitationInfo sitationInfo = new SitationInfo();
      sitationInfo.setLibraryName("ei");
      if (ei.equals("是") || ei.equalsIgnoreCase("yes")) {
        sitationInfo.setSitStatus(1);
      } else {
        sitationInfo.setSitStatus(0);
      }
      sitationInfoList.add(sitationInfo);
    }
    /*
     * if (StringUtils.isNotBlank(cssci)) { SitationInfo sitationInfo = new SitationInfo();
     * sitationInfo.setLibraryName("cssci"); if (cssci.equals("是") || cssci.equalsIgnoreCase("yes")) {
     * sitationInfo.setSitStatus(1); } else { sitationInfo.setSitStatus(0); }
     * sitationInfoList.add(sitationInfo); } if (StringUtils.isNotBlank(pku)) { SitationInfo
     * sitationInfo = new SitationInfo(); sitationInfo.setLibraryName("pku"); if (pku.equals("是") ||
     * pku.equalsIgnoreCase("yes")) { sitationInfo.setSitStatus(1); } else {
     * sitationInfo.setSitStatus(0); } sitationInfoList.add(sitationInfo); } if
     * (StringUtils.isNotBlank(other)) { SitationInfo sitationInfo = new SitationInfo();
     * sitationInfo.setLibraryName("other"); if (other.equals("是") || other.equalsIgnoreCase("yes")) {
     * sitationInfo.setSitStatus(1); } else { sitationInfo.setSitStatus(0); }
     * sitationInfoList.add(sitationInfo); }
     */
  }

  /**
   * 构建成果类型
   * 
   * @param fileInfo
   * @param map StringUtils.isNotBlank(map.get("ctitle"))?map.get("ctitle"):map.get("etitle")
   */
  private void buildOtherInfo(PubFileInfo fileInfo, Map<String, String> map) {
    int pubType = fileInfo.getPubType();
    switch (pubType) {
      case 1:
        fileInfo.setAwardCategory(map.get("award_category"));
        fileInfo.setAwardGrade(map.get("award_grade"));
        fileInfo.setIssueInsName(map.get("issue_ins_name"));
        fileInfo.setAwardDate(map.get("award_date"));
        fileInfo.setCertificateNo(map.get("certificate_no"));
        break;
      case 2:
      case 10:
        fileInfo.setBookTitle(map.get("book_name"));
        fileInfo.setISBN(map.get("isbn"));
        fileInfo.setLanguage(map.get("language"));
        fileInfo.setPublisher(map.get("publisher"));
        fileInfo.setCategoryValue(PubBookTypeEnum.parse(map.get("book_type")));
        // fileInfo.setPublicationStatus(map.get("publication_status"));
        fileInfo.setTotalPages(parserStringToInt(map.get("total_pages")));
        fileInfo.setTotalWords(parserStringToInt(map.get("total_words")));
        fileInfo.setSeriesName(map.get("series_name"));
        fileInfo.setEditors(map.get("editors"));
        fileInfo.setChapterNo(map.get("chapter_no"));
        break;
      case 3:
        fileInfo.setPaperTypeValue(PubConferencePaperTypeEnum.parse(map.get("paper_type")));
        // fileInfo.setCity(map.get("conf_venue"));
        fileInfo.setConfName(map.get("conference_name"));
        fileInfo.setOrganizer(map.get("organizer"));
        fileInfo.setStartDate(map.get("start_date"));
        fileInfo.setEndDate(map.get("end_date"));
        fileInfo.setPapersName(map.get("papers_name"));
        break;
      case 4:
        fileInfo.setPublicationStatus(map.get("publish_status"));
        break;
      case 5:
        fileInfo.setPatentNo(map.get("patent_no"));
        // fileInfo.setPubyear(map.get("start_date"));
        fileInfo.setPatenType(map.get("patent_type"));
        // fileInfo.setIssuingAuthority(map.get("issue_org"));
        // fileInfo.setCategoryNo(map.get("category_no"));
        fileInfo.setPatentOpenNo(map.get("patent_open_no"));
        fileInfo.setApplicationNo(map.get("application_no"));
        fileInfo.setPatentNo(map.get("application_no"));
        fileInfo.setPatentStatus(map.get("patent_status"));
        fileInfo.setApplier(map.get("patent_applier"));
        fileInfo.setPatentArea(PubPatentAreaEnum.parse(map.get("patent_area")));
        fileInfo.setIpc(map.get("ipc"));
        fileInfo.setCpc(map.get("cpc"));
        fileInfo.setStartDate(map.get("start_date"));
        fileInfo.setEndDate(map.get("end_date"));
        fileInfo.setApplicationDate(map.get("application_date"));
        fileInfo.setPatentChangeStatus(PubPatentTransitionStatusEnum.parse(map.get("patent_change_status")));
        fileInfo.setPatentPrice(map.get("patent_price"));
        break;
      case 8:
        fileInfo.setDegree(PubThesisDegreeEnum.parse(map.get("degree")));
        fileInfo.setIssuingAuthority(map.get("thesis_ins_name"));
        fileInfo.setDepartment(map.get("department"));
        fileInfo.setDefenseDate(map.get("defense_date"));
        break;
      case 12:
        fileInfo.setStandardNo(map.get("standard_no"));
        PubStandardTypeEnum type = PubStandardTypeEnum.parse(map.get("standard_type"));
        fileInfo.setType(type);
        String standardIntitution = map.get("standard_intitution");
        if (type.equals(PubStandardTypeEnum.INTERNATIONAL)) {
          fileInfo.setPublishAgency(standardIntitution);
        } else {
          fileInfo.setTechnicalCommittees(standardIntitution);
        }
        break;
      case 13:
        fileInfo.setRegisterNo(map.get("register_no"));
        fileInfo.setAcquisitionType(PubSCAcquisitionTypeEnum.parse(map.get("acquisition_type")));
        fileInfo.setScopeType(PubSCScopeTypeEnum.parse(map.get("scope_type")));
        break;
      default:
        break;
    }
  }
}
