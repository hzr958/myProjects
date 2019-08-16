package com.smate.web.v8pub.service.fileimport.extract;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.multipart.MultipartFile;

import com.smate.core.base.pub.enums.PubConferencePaperTypeEnum;
import com.smate.core.base.pub.enums.PubPatentAreaEnum;
import com.smate.core.base.pub.util.AuthorNamesUtils;
import com.smate.core.base.pub.util.PubParamUtils;
import com.smate.core.base.utils.number.NumberUtils;
import com.smate.web.v8pub.utils.FileCodeUtils;
import com.smate.web.v8pub.utils.PubBuildAuthorNamesUtils;

public class EndNoteFileExtractService extends FileExtractBaseService {
  private Logger logger = LoggerFactory.getLogger(getClass());

  @Override
  public List<Map<String, String>> fileExtractToMap(MultipartFile sourceFile) {
    String contentTag =
        "Reference Type: |Record Number: |Conference Name: |Conference Location: |Year of Conference: |Author: |Title: |Place Published: |Year: |Journal: |Volume: |Issue: |Pages: |Short Title: |Keywords: |Abstract: |URL: |Last Modified Date: |Name of Database: |Language: |Pages: |Date: |Type of Medium: |City: |Publisher: |Notes: |Times Cited: |Number of Pages: |ISBN: |Published Source: |Country: |Assignee: |Issue Date: |Patent Number: |Accession Number: |Issue Date: |ISSN: |Art. No.:|DOI: |Inventor: |International Patent Classification: |Epub Date: | Date: |start_page: |end_page: |Author Address: |Cited References Count: |Series Title: |Book Title: |Patent Type: |University: |Thesis Type: ";
    String prjstartTag = "Reference Type: ";
    String prjendTag = "Reference Type: ";
    String contTag =
        "%0 |%B |%C |%A |%T |%D |%J |%V |%N |%& |%P |%! |%K |%X |%U |%G |%8 |%I |%Z |%@ |%R |%S |%E |%L |%+ |%9 ";
    String prjStarTag = "%0 ";
    String prjEndTag = "%0 ";
    int tagLen = 3;

    List<Map<String, String>> list = new ArrayList<>();
    String[] contentTags = contentTag.split("\\|");
    String[] contTags = contTag.split("\\|");
    Boolean isElement = false;
    InputStream in = null;
    BufferedReader br = null;
    try {
      Charset fileCode = FileCodeUtils.guessCharset(sourceFile.getInputStream());
      in = sourceFile.getInputStream();
      br = new BufferedReader(new InputStreamReader(in, fileCode));
      String line = br.readLine();
      String lineOne = line;

      Boolean isKeywords = false;
      boolean isKeywordsCurrLine = true; // 当前行的数据也是关键词（关键词首行除外）
      boolean isKeywordsEnd = false; // 关键词结束的标识
      boolean isKeywordsSave = false; // 是否已写入XML

      String keywordsTag = "Keywords: ";
      String keywordTagPct = "%K ";
      String keywordsElement = "";
      String keywords = "";
      Map<String, String> pubInfoMap = null;
      while (line != null) {
        if (!"".equals(line)) {
          line = line.replace((char) 0x1, (char) 32);
          if (isElement && (line.startsWith(prjendTag) || line.startsWith(prjEndTag))) {
            if (isKeywords && !isKeywordsSave) {// 关键词在成果的最后一行出现的情形
              pubInfoMap.put(keywordsElement, keywords);
              isKeywords = false;
              isKeywordsCurrLine = true;
              isKeywordsEnd = false;
              keywords = "";
            }
            isKeywordsSave = false;
            isElement = false;
          }
          if (line.startsWith(prjstartTag) || line.startsWith(prjstartTag, 1)) {
            // 成果开始的地方，new 一个 成果map信息
            pubInfoMap = new HashMap<>();
            list.add(pubInfoMap);
            String key = prjstartTag.substring(0, tagLen);
            String value = line.substring(prjstartTag.length()).trim();
            pubInfoMap.put(key, value);
            isElement = true;
          } else if (line.startsWith(prjStarTag) || line.startsWith(prjStarTag, 1)) {
            // 成果开始的地方，new 一个 成果map信息
            pubInfoMap = new HashMap<>();
            list.add(pubInfoMap);
            String key = prjStarTag.substring(0, tagLen - 1).replace("%", "a");
            String value = line.substring(prjStarTag.length()).trim();
            pubInfoMap.put(key, value);
            isElement = true;
          } else if (pubInfoMap != null) {
            if (lineOne.startsWith(contentTags[0], 1) || lineOne.startsWith(contentTags[0])) {

              if (isKeywords) {// 上一行是关键词
                for (int i = 0; i < contentTags.length; i++) {
                  if (line.startsWith(contentTags[i])) {
                    isKeywordsCurrLine = false;
                    isKeywordsEnd = true;
                    break;
                  }
                }
                if (isKeywordsCurrLine) {// 当前行的数据也是关键词（关键词首行除外）
                  keywords += line + ";";
                }
                if (isKeywordsEnd) {// 关键词结束的标识，即当前行的数据不再是关键词
                  isElement = true;
                  pubInfoMap.put(keywordsElement, keywords);
                  isKeywords = false;
                  isKeywordsSave = true;
                  isKeywordsCurrLine = true;
                  isKeywordsEnd = false;
                  keywords = "";
                }
              }

              if (line.startsWith(keywordsTag)) {// 关键词首行
                keywordsElement = keywordsTag.substring(0, tagLen);
                if (line.length() > keywordsTag.length()) {
                  keywords = line.substring(keywordsTag.length() - 1).trim() + ";";
                }
                isKeywords = true;
              }

              if (!isKeywords) {
                isElement = contentTagsHandle(tagLen, contentTags, isElement, pubInfoMap, line);
              }
            } else if (lineOne.startsWith(contTags[0], 1) || lineOne.startsWith(contTags[0])) {

              if (isKeywords) {// 上一行是关键词
                for (int i = 0; i < contTags.length; i++) {
                  if (line.startsWith(contTags[i])) {
                    isKeywordsCurrLine = false;
                    isKeywordsEnd = true;
                    break;
                  }
                }
                if (isKeywordsCurrLine) {// 当前行的数据也是关键词（关键词首行除外）
                  keywords += line + ";";
                }
                if (isKeywordsEnd) {// 关键词结束的标识，即当前行的数据不再是关键词
                  isElement = true;
                  pubInfoMap.put(keywordsElement, keywords);
                  isKeywords = false;
                  isKeywordsSave = true;
                  isKeywordsCurrLine = true;
                  isKeywordsEnd = false;
                  keywords = "";
                }
              }

              if (line.startsWith(keywordTagPct)) {// 关键词首行
                keywordsElement = "aK";
                if (line.length() > keywordTagPct.length()) {
                  keywords = line.substring(keywordTagPct.length() - 1).trim() + ";";
                }
                isKeywords = true;
              }

              if (!isKeywords) {
                isElement = contTagsHandle(tagLen, contTags, isElement, pubInfoMap, line);
              }
            }

          }
        }
        line = br.readLine();

      }

    } catch (FileNotFoundException e) {
      logger.error("将文件拆分成Map,未找到文件", e);
    } catch (Exception e) {
      logger.error("将文件拆分成Map格式字符串时出错", e);
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


  private Boolean contentTagsHandle(int tagLen, String[] contentTags, Boolean isElement, Map<String, String> pubInfoMap,
      String line) {
    for (int i = 0; i < contentTags.length; i++) {
      if (line.startsWith(contentTags[i])) {
        isElement = true;
        // 这里针对endnote中的pages
        // 特殊处理,pages中包括了startpage和end page并以"-"连起来,
        if ("Pages: ".equals(contentTags[i]) || "Number of Pages: ".equals(contentTags[i])) { // 如果发现有"Pages:"，则分拆成strat_page,end_page两个结点
          String page = line.substring(contentTags[i].length() - 1).trim();
          pubInfoMap.put("page_number", page);
        } else if ("Issue Date: ".equals(contentTags[i])) {
          pubInfoMap.put("ISD", convertDate(line.substring(contentTags[i].length() - 1)));
        } else if ("Times Cited:".equals(contentTags[i])) {
          pubInfoMap.put("Ctd", line.substring(contentTags[i].length()).trim());
        } else if ("Conference Name:".equalsIgnoreCase(contentTags[i].trim())) {
          pubInfoMap.put("conf_name", line.substring(contentTags[i].length()).trim());
        } else if ("Series Title:".equalsIgnoreCase(contentTags[i].trim())) {
          pubInfoMap.put("ST", line.substring(contentTags[i].length()).trim());
        } else if ("Book Title:".equalsIgnoreCase(contentTags[i].trim())) {
          pubInfoMap.put("BT", line.substring(contentTags[i].length()).trim());
        } else if ("Conference Location:".equalsIgnoreCase(contentTags[i].trim())) {
          pubInfoMap.put("conf_city", line.substring(contentTags[i].length()).trim());
        } else if ("Year of Conference:".equalsIgnoreCase(contentTags[i].trim())) {
          pubInfoMap.put("conf_year", line.substring(contentTags[i].length()).trim());
        } else if ("Cited References Count:".equals(contentTags[i])) {
          pubInfoMap.put("Ctd", line.substring(contentTags[i].length()).trim());
        } else if ("Patent Type: ".equals(contentTags[i])) {
          pubInfoMap.put("paType", line.substring(contentTags[i].length()).trim());
        } else if ("Art. No.:".equals(contentTags[i])) {
          pubInfoMap.put("Art_No", line.substring(contentTags[i].length()).trim());
        } else if ("Author: ".equals(contentTags[i]) || "Inventor: ".equals(contentTags[i])) {
          line = line.substring(contentTags[i].length()).trim();
          if (StringUtils.isNotBlank(line) && !line.contains(";")) {
            if (!PubParamUtils.isChinese(line) && line.contains(".,")) {
              line = line.replaceAll(" and ", ",");
              line = line.replace(".,", ";");
            } else if (PubParamUtils.isChinese(line) && line.contains(",")) {
              line = line.replaceAll(" and ", ",");
              line = line.replaceAll(",", ";");
            } else {
              line = line.replaceAll(" and ", ";");
            }
            // 只有一个逗号，不进行拆分
            if (line.indexOf(",") == line.lastIndexOf(",")) {
              line = line.replaceAll(",", " ");
            }
          }
          // 含有分号，也含有 and 字符，进行替换成分号
          if (line.contains(";") && line.contains(" and ")) {
            line = line.replaceAll(" and ", ";");
          }
          // authorNames 传入有值的话，则不进行构造，但是要处理一下数据
          List<String> authorList = AuthorNamesUtils.parsePubAuthorNames(line);
          line = PubBuildAuthorNamesUtils.buildPubAuthorNames(authorList);
          pubInfoMap.put("Aut", line);
        } else if ("Date: ".equals(contentTags[i])) {
          String year = pubInfoMap.get("Yea");
          line = line.substring(contentTags[i].length()).trim();
          String date = year;
          if (line.matches("\\d{1,2}(\\s\\d{1,2})?")) {
            date = year + "-" + line.replaceAll(" ", "-");
          } else {
            String[] dates = line.split(" ");
            String mon = FileExtractBaseService.engMonth2Int(dates[0]);
            if (dates.length == 2) {
              date = year + "-" + mon + "-" + dates[1];
            } else {
              date = year + "-" + mon;
            }
            if (StringUtils.isBlank(year)) {
              date = line;
            }
          }
          pubInfoMap.put("Yea", date);
        } else {
          if (pubInfoMap.get(contentTags[i].substring(0, tagLen)) == null) {
            pubInfoMap.put(contentTags[i].substring(0, tagLen), line.substring(contentTags[i].length() - 1).trim());
          }
        }
      }
    }
    return isElement;
  }

  private Boolean contTagsHandle(int tagLen, String[] contTags, Boolean isElement, Map<String, String> pubInfoMap,
      String line) {
    for (int i = 0; i < contTags.length; i++) {
      if (line.startsWith(contTags[i])) {
        isElement = true;
        // 这里针对endnote中的pages
        // 特殊处理,pages中包括了startpage和end page并以"-"连起来,
        if ("%P ".equals(contTags[i])) { // 如果发现有"Pages:"，则分拆成strat_page,end_page两个结点
          String page = line.substring(contTags[i].length() - 1).trim();
          pubInfoMap.put("page_number", page);
        } else if ("%! ".equals(contTags[i])) {
          pubInfoMap.put("Ll", convertDate(line.substring(contTags[i].length() - 1)));
        } else if ("%8 ".equals(contTags[i])) {
          pubInfoMap.put("aPub_date", line.substring(contTags[i].length()).trim());
        } else if ("%9 ".equals(contTags[i])) {
          pubInfoMap.put("aPat_type", line.substring(contTags[i].length()).trim());
        } else if ("%+ ".equals(contTags[i])) {
          pubInfoMap.put("Plu", line.substring(contTags[i].length()).trim());
        } else if ("%@ ".equals(contTags[i])) {
          pubInfoMap.put("At", line.substring(contTags[i].length()).trim());
        } else if ("%V ".equals(contTags[i])) {
          pubInfoMap.put("The", line.substring(contTags[i].length()).trim());
        } else if ("%& ".equals(contTags[i])) {
          pubInfoMap.put("An", line.substring(contTags[i].length()).trim());
        } else if ("%A ".equals(contTags[i])) {// 作者
          String author = pubInfoMap.get("aA");
          String nextAuthor = line.substring(contTags[i].length()).trim();
          if (StringUtils.isNotBlank(author)) {
            pubInfoMap.put("aA", author + "; " + nextAuthor);
          } else {
            pubInfoMap.put("aA", nextAuthor);
          }
        } else if ("%H ".equals(contTags[i])) {// 英文作者
          String author = pubInfoMap.get("aH");
          String nextAuthor = line.substring(contTags[i].length()).trim();
          if (StringUtils.isNotBlank(author)) {
            pubInfoMap.put("aH", author + "; " + nextAuthor);
          } else {
            pubInfoMap.put("aH", nextAuthor);
          }
        } else if ("%J ".equals(contTags[i])) {// 期刊名称取第一个
          if (StringUtils.isBlank(pubInfoMap.get("aJ"))) {
            pubInfoMap.put("aJ", line.substring(contTags[i].length()).trim());
          }
        } else {
          String tag = contTags[i].substring(0, tagLen - 1);
          tag = tag.replace("%", "a");
          pubInfoMap.put(tag, line.substring(contTags[i].length() - 1).trim());
        }
      }
    }
    return isElement;
  }

  private String convertDate(String strDate) {
    try {
      String[] strDates = strDate.split(" ");
      String month = engMonth2Int(strDates[1]);
      return strDates[2] + "-" + month + "-" + strDates[0];
    } catch (Exception e) {
      return strDate;
    }
  }

  @Override
  public List<PubFileInfo> processData(List<Map<String, String>> list) {
    List<PubFileInfo> pubFileInfoList = new ArrayList<>();
    PubFileInfo fileInfo = null;
    if (list == null || list.size() == 0) {
      return pubFileInfoList;
    }
    int seqNo = 1;
    for (Map<String, String> map : list) {
      fileInfo = new PubFileInfo();
      pubFileInfoList.add(fileInfo);
      // 系列号
      fileInfo.setSeqNo(seqNo++);
      buildBaseInfo(fileInfo, map);
      buildPubType(fileInfo, map);
      fileInfo.setOriginal(StringUtils.isNotBlank(map.get("Jou")) ? map.get("Jou") : map.get("aJ"));
      fileInfo.setPageNumber(map.get("page_number"));
      fileInfo.setKeywords(StringUtils.isNotBlank(map.get("Key")) ? map.get("Key") : map.get("aK"));
      fileInfo.setISBN(StringUtils.isNotBlank(map.get("ISB")) ? map.get("ISB") : map.get("At"));
      fileInfo.setIssn(StringUtils.isNotBlank(map.get("ISS")) ? map.get("ISS") : map.get("At"));
      fileInfo.setDoi(StringUtils.isNotBlank(map.get("DOI")) ? map.get("DOI") : map.get("aR"));
      fileInfo.setSrcFulltextUrl(StringUtils.isNotBlank(map.get("URL")) ? map.get("URL") : map.get("aU"));
      fileInfo.setRegNo(map.get("Acc"));
      if (StringUtils.isNotBlank(map.get("Cit"))) {
        fileInfo.setCity(map.get("Cit"));
      } else {
        fileInfo.setCity(StringUtils.isNotBlank(map.get("conf_city")) ? map.get("conf_city") : map.get("aC"));
      }
      fileInfo.setFundInfo(map.get("FU"));
      fileInfo.setRemarks(StringUtils.isNotBlank(map.get("Not")) ? map.get("Not") : map.get("aZ"));

      fileInfo.setCitations(NumberUtils.toInt(map.get("Ctd"), 0));
      buildExtraInfo(fileInfo, map);
      fileInfo.setCountry(map.get("Cou"));
      String applier = StringUtils.isNotBlank(map.get("aL")) ? map.get("aL") : map.get("Ass");
      fileInfo.setApplier(applier);

    }
    return pubFileInfoList;
  }



  /**
   * 构建基本信息
   * 
   * @param fileInfo
   * @param map
   */
  private void buildBaseInfo(PubFileInfo fileInfo, Map<String, String> map) {

    fileInfo.setTitle(StringUtils.isNotBlank(map.get("Tit")) ? map.get("Tit") : map.get("aT"));
    fileInfo.setSourceDbCode("fileEndNote");
    // 作者
    if (StringUtils.isNotBlank(map.get("Aut"))) {
      fileInfo.setAuthorNames(map.get("Aut"));
    } else if (StringUtils.isNotBlank(map.get("Inv"))) {
      fileInfo.setAuthorNames(map.get("Inv"));
    } else {
      fileInfo.setAuthorNames(StringUtils.isNoneBlank(map.get("aA")) ? map.get("aA") : map.get("aH"));
    }

    fileInfo.setCabstract(StringUtils.isNotBlank(map.get("Abs")) ? map.get("Abs") : map.get("aX"));
    // 出版年月
    if (StringUtils.isNotBlank(map.get("Yea"))) {
      fileInfo.setPubyear(map.get("Yea"));
      fileInfo.setPatentStatus("授权");
    } else if (StringUtils.isNotBlank(map.get("aPub_date"))) {
      fileInfo.setPubyear(map.get("aPub_date"));
    } else {
      fileInfo.setPubyear(map.get("aD"));
    }

    if (StringUtils.isNotBlank(map.get("Vol"))) {
      if (map.get("Vol").length() > 20) {
        fileInfo.setVolumeNo(map.get("Vol").substring(0, 20));
      } else {
        fileInfo.setVolumeNo(map.get("Vol"));
      }
    } else if (StringUtils.isNotBlank(map.get("aV"))) {
      if (map.get("aV").length() > 20) {
        fileInfo.setVolumeNo(map.get("aV").substring(0, 20));
      } else {
        fileInfo.setVolumeNo(map.get("aV"));
      }
    } else if (StringUtils.isNotBlank(map.get("The"))) {
      if (map.get("The").length() > 20) {
        fileInfo.setVolumeNo(map.get("The").substring(0, 20));
      } else {
        fileInfo.setVolumeNo(map.get("The"));
      }
    }

    if (StringUtils.isNotBlank(map.get("Iss"))) {
      if (map.get("Iss").length() > 20) {
        fileInfo.setIssue(map.get("Iss").substring(0, 20));
      } else {
        fileInfo.setIssue(map.get("Iss"));
      }
    } else if (StringUtils.isNotBlank(map.get("aN"))) {
      if (map.get("aN").length() > 20) {
        fileInfo.setIssue(map.get("aN").substring(0, 20));
      } else {
        fileInfo.setIssue(map.get("aN"));
      }
    }
  }


  /**
   * 构建额外的信息
   * 
   * @param fileInfo
   * @param map
   */
  private void buildExtraInfo(PubFileInfo fileInfo, Map<String, String> map) {
    String type = StringUtils.isNotBlank(map.get("Ref")) ? map.get("Ref") : map.get("a0");
    switch (type) {
      case "Book":
        fileInfo.setBookTitle(StringUtils.isNotBlank(map.get("BT")) ? map.get("BT") : map.get("aB"));
        fileInfo.setSeriesName(StringUtils.isNotBlank(map.get("ST")) ? map.get("ST") : map.get("aS"));
        fileInfo.setPublisher(StringUtils.isNotBlank(map.get("Pub")) ? map.get("Pub") : map.get("aI"));
        fileInfo.setLanguage(StringUtils.isNotBlank(map.get("Lan")) ? map.get("Lan") : map.get("aG"));
        fileInfo.setTotalPages(parserStringToInt(map.get("An")));
        break;
      case "Book Section":
        fileInfo.setBookTitle(StringUtils.isNotBlank(map.get("BT")) ? map.get("BT") : map.get("aB"));
        fileInfo.setSeriesName(StringUtils.isNotBlank(map.get("ST")) ? map.get("ST") : map.get("aS"));
        fileInfo.setEditors(map.get("aE"));
        fileInfo.setChapterNo(map.get("An"));
        fileInfo.setPublisher(StringUtils.isNotBlank(map.get("Pub")) ? map.get("Pub") : map.get("aI"));
        break;
      case "Patent":
        String patentNumber = StringUtils.isNotBlank(map.get("Pat")) ? map.get("Pat") : map.get("At");
        if ("1".equals(fileInfo.getPatentStatus()) || "授权".equals(fileInfo.getPatentStatus())) {
          fileInfo.setPatentOpenNo(patentNumber);

        } else {
          fileInfo.setPatentNo(patentNumber);
          fileInfo.setApplicationNo(patentNumber);
        }
        fileInfo.setStartDate(map.get("ISD"));
        fileInfo.setApplicationNo(map.get("Acc"));
        fileInfo.setIssuingAuthority(StringUtils.isNotBlank(map.get("Pub")) ? map.get("Pub") : map.get("aI"));
        fileInfo.setCategoryNo(map.get("aPat_type"));
        // 专利地区
        PubPatentAreaEnum patentArea = PubParamUtils.buildArea(patentNumber);
        fileInfo.setPatentArea(patentArea);
        // 发证单位
        fileInfo.setIssuingAuthority(PubParamUtils.buildPatentIssuingAuthority(patentArea));

        fileInfo.setPatenType(PubParamUtils.buildPatentType(map.get("paType")));
        break;
      case "Thesis":
        String degree = StringUtils.isNotBlank(map.get("aPat_type")) ? map.get("aPat_type") : map.get("The");
        fileInfo.setDegree(PubParamUtils.buildDegree(degree));
        String issuingAuthority = StringUtils.isNotBlank(map.get("aI")) ? map.get("aI") : map.get("Uni");
        fileInfo.setIssuingAuthority(issuingAuthority);
        fileInfo.setDepartment(map.get("aB"));
        fileInfo.setDefenseDate(fileInfo.getPubyear());
        break;
      case "Conference Paper":
      case "Conference Proceedings":
      case "Conference Proceeding":
        fileInfo.setPaperTypeValue(PubConferencePaperTypeEnum.parse(map.get("aPat_type")));
        fileInfo.setStartDate(map.get("conf_year"));
        fileInfo.setConfName(StringUtils.isNotBlank(map.get("conf_name")) ? map.get("conf_name") : map.get("aB"));
        break;
      default:
        break;
    }
  }


  /**
   * 构建成果类型
   * 
   * @param fileInfo
   * @param map
   */
  private void buildPubType(PubFileInfo fileInfo, Map<String, String> map) {
    String type = StringUtils.isNotBlank(map.get("Ref")) ? map.get("Ref") : map.get("a0");
    int pubType = 7;
    switch (type) {
      case "Award":
        pubType = 1;
        break;
      case "Book":
      case "Book/Monograph":
        pubType = 2;
        break;
      case "Conference Paper":
      case "Conference Proceedings":
      case "Conference Proceeding":
        pubType = 3;
        break;
      case "Journal Article":
        pubType = 4;
        break;
      case "Patent":
        pubType = 5;
        break;
      case "Others":
        pubType = 7;
        break;
      case "Thesis":
        pubType = 8;
        break;
      case "Book Section":
        pubType = 10;
        break;
      case "Journal Editor":
        pubType = 11;
        break;
      default:
        pubType = 7;
        break;
    }
    fileInfo.setPubType(pubType);
  }



}
