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

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.multipart.MultipartFile;

import com.smate.core.base.pub.enums.PubThesisDegreeEnum;
import com.smate.core.base.pub.util.PubParamUtils;
import com.smate.web.v8pub.utils.FileCodeUtils;
import com.smate.web.v8pub.utils.PubFileParamsUtils;


/**
 * 
 * @author aijiangbin
 * @date 2018年8月1日
 */
public class RefWorksFileExtractService extends FileExtractBaseService {
  private Logger logger = LoggerFactory.getLogger(getClass());


  @Override
  public List<Map<String, String>> fileExtractToMap(MultipartFile sourceFile) {
    List<Map<String, String>> list = new ArrayList<Map<String, String>>();
    Map<String, String> pubMap = null;
    InputStream in = null;
    BufferedReader br = null;
    String contentTag =
        "LN |PS |RT |SR |ID |A1 |T1 |JF |JO |YR |FD |VO |IS |SP |OP |K1 |AB |NO |A2 |T2 |ED |PB |PP |A3 |A4 |A5 |T3 |SN |AV |AD |AN |LA |CL |SF |OT |LK |DO |CN |DB |DS |IP |RD |ST |U1 |U2 |U3 |U4 |U5 |UL |SL |LL |CR |WT |A6 |WV |WP ";
    String prjstartTag = "RT ";
    int tagLen = 2;
    String currentKey = "";
    String[] contentTags = contentTag.split("\\|");
    Boolean isElement = false;
    try {
      Charset fileCode = FileCodeUtils.guessCharset(sourceFile.getInputStream());
      in = sourceFile.getInputStream();
      br = new BufferedReader(new InputStreamReader(in, fileCode));
      String line = br.readLine();
      Boolean isTag = false;

      while (line != null) {
        if (!"".equals(line)) {
          if (line.startsWith(prjstartTag) || line.startsWith(prjstartTag, 1)) {
            pubMap = new HashMap<String, String>();
            list.add(pubMap);
            pubMap.put(prjstartTag.substring(0, tagLen), line.substring(prjstartTag.length()).trim());
            isElement = true;
          } else {
            isTag = false;
            for (int i = 0; i < contentTags.length; i++) {
              if (line.startsWith(contentTags[i])) {

                isElement = true;
                isTag = true;
                String strValue = line.substring(contentTags[i].length() - 1).trim();
                if ("FD ".equals(contentTags[i])) {
                  try {
                    strValue = convertDate(strValue);
                  } catch (Exception e) {
                    strValue = "";
                  }
                }
                if ("A1 ".equals(contentTags[i]) || "A2 ".equals(contentTags[i])) {
                  strValue = strValue.replace(",", " ");
                } else if ("AN ".equals(contentTags[i]) && StringUtils.isNotBlank(strValue)) {
                  strValue = strValue.substring(strValue.lastIndexOf(":") + 1, strValue.length()).trim();
                }
                currentKey = contentTags[i].substring(0, tagLen);
                // 作者 关键词有多个
                if ("A1".equals(currentKey) || "K1".equals(currentKey) || "A2".equals(currentKey)) {
                  if (StringUtils.isNotBlank(pubMap.get(currentKey))) {
                    String oldValue = pubMap.get(currentKey);
                    pubMap.put(currentKey, oldValue + "; " + strValue);
                  } else {
                    pubMap.put(currentKey, strValue);
                  }
                } else {
                  pubMap.put(currentKey, strValue);
                }
                break;
              } else {
                isElement = false;
              }
            }
            if (isElement && !isTag && StringUtils.isNotBlank(currentKey)) {
              pubMap.put(currentKey, line.trim());
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
          logger.error("解析文件，关联输入流异常", e);
        }
      }
      if (in != null) {
        try {
          in.close();
        } catch (IOException e) {
          logger.error("解析文件，关联输入流异常", e);
        }
      }

    }

    return list;
  }

  private String convertDate(String strDate) {
    String[] strDates = strDate.split(" ");
    String month = "";
    String day = "";

    if (strDates.length > 1) {
      if (strDates[0].length() > 2) {
        day = strDates[1];
        month = engMonth2Int(strDates[0]);
        return "-" + month + "-" + day;
      } else {
        day = strDates[0];
        month = engMonth2Int(strDates[1]);
        return "-" + month + "-" + day;
      }

    } else {
      return "-" + engMonth2Int(strDates[0]);

    }
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
        pubInfo.setTitle(map.get("T1"));
        pubInfo.setAuthorNames(map.get("A1"));
        pubInfo.setSourceDbCode("fileRefwork");
        pubInfo.setSrcFulltextUrl(map.get("UL"));
        pubInfo.setCabstract(map.get("AB"));
        if (StringUtils.isEmpty(map.get("YR"))) {// 由于会议成果的FD是表示自由格式的成果发表日期（Publication Data,Free
                                                 // Form）,当YR不存在时，才去进行构建，取FD的值，防止日期格式出现异常
          pubInfo.setPubyear(PubFileParamsUtils.buildPublishDate(map.get("YR"), map.get("FD")));
        } else {
          pubInfo.setPubyear(map.get("YR"));
        }
        pubInfo.setVolumeNo(map.get("VO"));
        pubInfo.setIssue(map.get("IS"));

        String sp = map.get("SP");
        String op = map.get("OP");
        String an = map.get("AN");
        String pageNumber = PubParamUtils.buildPageNumber(sp, op, an);
        pubInfo.setPageNumber(pageNumber);
        pubInfo.setConfName(map.get("T2"));
        pubInfo.setCity(map.get("PP"));
        String rt = map.get("RT");
        rt = StringUtils.trimToEmpty(rt);
        int pubType = 7;
        switch (rt) {
          case "Book, Edited":
            // 书籍著作
            pubType = 2;
            break;
          case "Report":
          case "Conference Paper":
          case "Conference Proceedings":
            pubType = 3;
            break;
          case "Journal":
          case "Journal Article":
            pubType = 4;
            break;
          case "Thesis":
          case "Dissertation":
          case "Dissertation/Thesis":
            // 学位论文
            pubType = 8;
            break;
          case "Book, Section":
            // 书籍章节
            pubType = 10;
            break;
          default:
            pubType = 7;
            break;
        }
        pubInfo.setPubType(pubType);
        pubInfo.setOriginal(StringUtils.isNotBlank(map.get("JF")) ? map.get("JF") : map.get("JO"));
        pubInfo.setKeywords(map.get("K1"));

        pubInfo.setDoi(map.get("DO"));

        pubInfo.setEditors(map.get("A2"));
        // 书籍论文中，PB是出版社
        pubInfo.setPublisher(map.get("PB"));
        // 学位论文中，PB是颁发机构
        pubInfo.setIssuingAuthority(map.get("PB"));
        // 书籍论文中，T2是书名
        pubInfo.setBookTitle(map.get("T2"));
        // 学位论文中，YR是答辩日期
        pubInfo.setDefenseDate(map.get("YR"));
        // 学位
        String degree = map.get("ED");
        pubInfo.setDegree(buildDegree(degree));
      }
    }
    return pubFileInfoList;
  }

  private PubThesisDegreeEnum buildDegree(String degree) {
    if (StringUtils.isEmpty(degree)) {
      return PubThesisDegreeEnum.OTHER;
    }
    if (degree.equalsIgnoreCase("PhD") || degree.equalsIgnoreCase("博士")) {
      return PubThesisDegreeEnum.DOCTOR;
    }
    if (degree.equalsIgnoreCase("硕士")) {
      return PubThesisDegreeEnum.MASTER;
    }
    return PubThesisDegreeEnum.OTHER;
  }

}
