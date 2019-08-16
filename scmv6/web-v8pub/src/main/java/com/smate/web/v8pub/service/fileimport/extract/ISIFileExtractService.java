package com.smate.web.v8pub.service.fileimport.extract;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.multipart.MultipartFile;

import com.smate.core.base.pub.util.PubParamUtils;
import com.smate.core.base.utils.date.DateUtils;
import com.smate.core.base.utils.number.NumberUtils;
import com.smate.web.v8pub.utils.FileCodeUtils;
import com.smate.web.v8pub.utils.PubFileParamsUtils;


/**
 * 
 * @author aijiangbin
 * @date 2018年8月2日
 */
public class ISIFileExtractService extends FileExtractBaseService {
  private Logger logger = LoggerFactory.getLogger(getClass());



  @Override
  public List<Map<String, String>> fileExtractToMap(MultipartFile sourceFile) {

    List<Map<String, String>> list = new ArrayList<>();
    Map<String, String> pubMap = null;
    InputStream in = null;
    BufferedReader br = null;
    String contentTag =
        "PT |AU |AF |TI |SO |AB |PY |PD |VL |IS |BP |EP |UT |ET |SN |TC |RP |EM |C1 |AR |PU |PA |DE |ID |SE |DT |CT |CL |CY |BN |DI |FU |LA ";
    String prjstartTag = "PT ";
    String prjendTag = "ER";
    int tagLen = 2;

    String[] contentTags = contentTag.split("\\|");
    Boolean isElement = false;

    try {
      Charset fileCode = FileCodeUtils.guessCharset(sourceFile.getInputStream());
      in = sourceFile.getInputStream();
      br = new BufferedReader(new InputStreamReader(in, fileCode));
      String line = br.readLine();
      String currentKey = "";
      Boolean isTag = false;
      Boolean isAuthor = false;

      while (line != null) {
        if (isElement && line.startsWith(prjendTag)) {
          isElement = false;
        }
        if (line.startsWith(prjstartTag)) {
          pubMap = new HashMap<>();
          list.add(pubMap);
          pubMap.put(prjstartTag.substring(0, tagLen), line.substring(prjstartTag.length() - 1).trim());
          isElement = true;
        } else if (isElement) {
          if (line.startsWith("  ")) {
            if (isElement && isTag) {
              if (isAuthor) {
                line = " ;" + line;// 在拼接时添加一个空格,防止在切割c1时名字中出现.;结尾的数据导致作者单位匹配不正常
              }
              if (StringUtils.isNotBlank(pubMap.get(currentKey))) {
                String oldVal = pubMap.get(currentKey);
                String newVal = oldVal + line;
                // 进行多个空格的处理
                newVal = PubFileParamsUtils.replaceMultipleSpace(newVal);
                pubMap.put(currentKey, newVal);
              } else {
                pubMap.put(currentKey, line);
              }
            }
          } else {
            for (int i = 0; i < contentTags.length; i++) {
              if (line.startsWith(contentTags[i])) {
                isTag = true;
                isAuthor = false;
                currentKey = contentTags[i].substring(0, tagLen);
                String strValue = line.substring(contentTags[i].length() - 1).trim();
                if (("AF ").equals(contentTags[i]) || "AU ".equals(contentTags[i]) || "C1 ".equals(contentTags[i])) {
                  isAuthor = true;
                } else if ("PD ".equals(contentTags[i])) {
                  strValue = convertDate(strValue);
                } else if ("SO ".equals(contentTags[i]) && strValue.indexOf(", VOL") > 0) {
                  strValue = strValue.substring(0, strValue.indexOf(", VOL"));
                } else if ("CY ".equals(contentTags[i])) {
                  strValue = convertCYdate(strValue);
                  if (StringUtils.isNotBlank(strValue)) {
                    pubMap.put(currentKey, strValue.substring(0, strValue.indexOf("-")));
                    strValue = strValue.substring(strValue.indexOf("-") + 1, strValue.length());
                    currentKey = "CYD";
                  }
                }
                strValue = PubFileParamsUtils.replaceMultipleSpace(strValue);
                pubMap.put(currentKey, strValue);
                break;
              }
              isTag = false;
            }
          }
        }
        line = br.readLine();
      }

    } catch (Exception e) {
      logger.error("将文件拆分成xml格式字符串时出错", e);
    } finally {
      if (br != null) {
        try {
          br.close();
        } catch (IOException e) {
          logger.error("关闭文件流异常", e);
        }
      }
      if (in != null) {
        try {
          in.close();
        } catch (IOException e) {
          logger.error("关闭文件流异常", e);
        }
      }

    }
    return list;
  }

  /**
   * 转换成日期格式.
   * 
   * @param strDate
   * @return
   */
  private String convertDate(String strDate) {
    try {
      String[] strDates = strDate.split(" ");
      String month = engMonth2Int(strDates[0]);
      if (strDates.length > 1) {
        try {
          month = month + "-" + Integer.parseInt(strDates[1]);
        } catch (Exception e) {
        }
      }

      return month;
    } catch (Exception e) {
      return "";
    }
  }

  private String convertCYdate(String cydate) {
    String newDate = "";
    try {
      // MAR 17-23, 2012
      // 2012/03/17-2012/03/23
      if (StringUtils.isNotBlank(cydate)) {
        String year = StringUtils.substring(cydate, cydate.indexOf(",") + 1, cydate.length()).trim();
        String month = cydate.substring(0, cydate.indexOf(" "));
        month = StringUtils.isBlank(month) ? "" : DateUtils.changeStringToNumber(month);
        String startDay = StringUtils.substring(cydate, cydate.indexOf(" ") + 1, cydate.indexOf("-"));
        String endDay = StringUtils.substring(cydate, cydate.indexOf("-") + 1, cydate.indexOf(","));
        newDate = year + "/" + month + "/" + startDay + "-" + year + "/" + month + "/" + endDay;
      }
    } catch (Exception e) {
      logger.error("====convertCYdate error cydate:{}", cydate, e);
    }
    return newDate;
  }

  @Override
  public List<PubFileInfo> processData(List<Map<String, String>> list) {

    List<PubFileInfo> pubFileInfoList = new ArrayList<>();
    PubFileInfo pubInfo = null;
    if (list != null && list.size() > 0) {
      int seqNo = 1;
      for (Map<String, String> map : list) {
        pubInfo = new PubFileInfo();
        pubFileInfoList.add(pubInfo);
        pubInfo.setSeqNo(seqNo++);
        pubInfo.setTitle(map.get("TI"));
        pubInfo.setAuthorNames(StringUtils.isNotBlank(map.get("AF")) ? map.get("AF") : map.get("AU"));
        pubInfo.setSourceDbCode("fileISI");
        pubInfo.setSrcFulltextUrl(map.get("UL"));
        pubInfo.setEmail(map.get("EM"));
        pubInfo.setAuthorNamesAbbr(map.get("AU"));
        pubInfo.setCabstract(map.get("AB"));

        pubInfo.setPubyear(PubFileParamsUtils.buildPublishDate(map.get("PY"), map.get("PD")));
        pubInfo.setOriginal(map.get("SO"));
        String bp = map.get("BP");
        String ep = map.get("EP");
        String ar = map.get("AR");
        String pageNumber = PubParamUtils.buildPageNumber(bp, ep, ar);
        pubInfo.setPageNumber(pageNumber);

        pubInfo.setKeywords(map.get("DE"));
        pubInfo.setKeywordPlus(map.get("ID"));
        pubInfo.setVolumeNo(map.get("VL"));
        pubInfo.setIssue(map.get("IS"));
        pubInfo.setIssn(map.get("SN"));
        pubInfo.setSourceId(substring(map.get("UT"), 5));

        pubInfo.setCitations(NumberUtils.toInt(map.get("TC"), 0));

        pubInfo.setOrganization(
            (map.get("RP") != null ? map.get("RP") : "") + (map.get("C1") != null ? map.get("C1") : ""));
        pubInfo.setUnitInfo(map.get("C1"));

        // PA 是出版商地址，PU是出版商，SCM-24073 只取PU信息
        // + (map.get("PA") != null ? map.get("PA") : "")
        pubInfo.setPublisher(map.get("PU") != null ? map.get("PU") : "");
        pubInfo.setBookTitle(map.get("SO"));
        pubInfo.setSeriesName(map.get("SE"));
        pubInfo.setCity(map.get("CL"));
        pubInfo.setConfName(map.get("CT"));
        pubInfo.setStartDate(map.get("CY"));
        pubInfo.setEndDate(map.get("CYD"));
        pubInfo.setDoi(map.get("DI"));
        pubInfo.setISBN(map.get("BN"));
        pubInfo.setLanguage(map.get("LA"));

        pubInfo.setFundInfo(map.get("FU"));
        Integer pubType = getPubType(map);// 获取成果类型

        pubType = (pubType == null) ? 7 : pubType;
        pubInfo.setPubType(pubType);
      }
    }
    return pubFileInfoList;

  }

  private Integer getPubType(Map<String, String> map) {
    Integer pubType = null;
    String DT = map.get("DT");
    if (StringUtils.isNotBlank(DT)) {
      List<String> typeList = PubFileParamsUtils.parsePubTypes(DT);
      if (CollectionUtils.isNotEmpty(typeList)) {
        for (String pType : typeList) {
          if (pubType == null) {
            pubType = buildPubType(pType);
          }
        }
      }
    } else {
      String PT = map.get("PT");
      if (StringUtils.isNotBlank(PT)) {
        switch (PT.toLowerCase()) {
          case "j":
            pubType = 4;
            break;
          case "s":
            pubType = 10;
            break;
          case "c":
            pubType = 3;
            break;
          case "b":
            pubType = 2;
            break;
          case "p":
            pubType = 5;
            break;
          default:
            pubType = 7;
            break;
        }
      }
    }
    return pubType;
  }

  private Integer buildPubType(String DT) {
    Integer pubType = 7;
    switch (DT) {
      case "Award":
        pubType = 1;
        break;
      case "Book":
      case "Book/Monograph":
        pubType = 2;
        break;
      case "Conference Paper":
      case "Proceedings Paper":
      case "Meeting Abstract":
      case "Meeting Summary":
      case "Meeting-Abstract":
        pubType = 3;
        break;
      case "Conference Proceedings":
      case "Journal Article":
      case "Article":
      case "Book Review":
        pubType = 4;
        break;
      case "Patent":
        pubType = 5;
        break;
      case "Thesis":
        pubType = 8;
        break;
      case "Book Section":
      case "Book Chapter":
      case "Review":
        pubType = 10;
        break;
      case "Journal Editor":
        pubType = 11;
        break;
      default:
        pubType = null;
        break;
    }
    return pubType;
  }

}
