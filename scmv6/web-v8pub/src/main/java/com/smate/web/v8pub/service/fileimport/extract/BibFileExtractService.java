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

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.multipart.MultipartFile;

import com.smate.core.base.pub.enums.PubThesisDegreeEnum;
import com.smate.core.base.utils.number.NumberUtils;
import com.smate.web.v8pub.utils.FileCodeUtils;

public class BibFileExtractService extends FileExtractBaseService {
  private Logger logger = LoggerFactory.getLogger(getClass());
  String type1 = "@article";// 期刊论文
  String type2 = "@book";// book
  String type3 = "@inproceedings";// 会议论文
  String type4 = "@inbook";// 书籍章节
  String type5 = "@thesis";// 学位论文(其他)
  String type5_1 = "@phdthesis";// 学位论文(博士)
  String type5_2 = "@mastersthesis";// 学位论文(硕士)
  String type6 = "@misc";// 其他
  String type6_1 = "@booklet";// 其他
  String type6_2 = "@conference";// 其他
  String type6_3 = "@incollection";// 其他
  String type6_4 = "@manual";// 其他
  String type6_5 = "@proceedings";// 其他
  String type6_6 = "@techreport";// 其他
  String type6_7 = "@unpublished";// 其他

  @Override
  public List<Map<String, String>> fileExtractToMap(MultipartFile sourceFile) {
    InputStream in = null;
    BufferedReader br = null;
    List<Map<String, String>> pubList = new ArrayList<>();
    String type1Tag =
        "author=|title=|journal=|year=|volume=|ISSN=|number=|pages=|month=|note=|Publisher=|DOI=|url=|abstract=|keywords=";
    String type2Tag =
        "author=|editor=|title=|publisher=|year=|volume=|number=|series=|address=|month=|note=|ISBN=|pages=|abstract=|keywords=|url=";// edition=|
    int tagLen = 3;

    String[] type1Tags = type1Tag.split("\\|");
    String[] type2Tags = type2Tag.split("\\|");
    String[] type3Tags = {"author=", "title=", "booktitle=", "year=", "editor=", "volume=", "series=", "pages=",
        "address=", "month=", "organization=", "publisher=", "note=", "abstract=", "keywords="};
    String[] type4Tags = {"author=", "editor=", "title=", "booktitle=", "chapter=", "pages=", "publisher", "year=",
        "volume=", "series=", "type", "address=", "month=", "note=", "ISBN=", "abstract=", "keywords=", "url="};// edition=
    String[] type5Tags = {"author=", "title=", "school=", "university", "year=", "address=", "month=", "note=",
        "abstract=", "keywords=", "type="};
    String[] type6Tags = {"author=", "title=", "year=", "abstract=", "keywords="};
    try {
      Charset fileCode = FileCodeUtils.guessCharset(sourceFile.getInputStream());
      in = sourceFile.getInputStream();
      br = new BufferedReader(new InputStreamReader(in, fileCode));
      String line = br.readLine();
      if (StringUtils.isNotBlank(line)) {
        line = line.trim();
        StringBuilder sb = new StringBuilder();
        char[] chars = line.toCharArray();
        for (int i = 0; i < chars.length; i++) {
          // 如果不是正规的utf-8格式,则跳过前面乱七八糟的字符
          if ((byte) chars[i] > 0) {
            sb.append(chars[i]);
          }
        }
        line = sb.toString();
      }
      Map<String, String> pubInfoMap = null;
      Integer seqNo = 0;
      while (line != null) {
        // @article--4
        if (line.startsWith(type1)) {
          pubInfoMap = new HashMap<>();
          pubList.add(pubInfoMap);
          line = readNextLine(br);
          pubInfoMap.put("Ref", "Journal Article");
          buildMapData(type1, type1Tags, line, pubInfoMap, tagLen, br);
        }
        // @book--2
        if (line.startsWith(type2)) {
          pubInfoMap = new HashMap<>();
          pubList.add(pubInfoMap);
          line = readNextLine(br);
          pubInfoMap.put("Ref", "Book");
          buildMapData(type2, type2Tags, line, pubInfoMap, tagLen, br);
        }
        // @inproceedings--3
        if (line.startsWith(type3)) {
          line = readNextLine(br);
          pubInfoMap = new HashMap<>();
          pubList.add(pubInfoMap);
          pubInfoMap.put("Ref", "Conference Paper");
          buildMapData(type3, type3Tags, line, pubInfoMap, tagLen, br);
        }
        // @inbook--10
        if (line.startsWith(type4)) {
          pubInfoMap = new HashMap<>();
          pubList.add(pubInfoMap);
          line = readNextLine(br);
          pubInfoMap.put("Ref", "Book Section");
          buildMapData(type4, type4Tags, line, pubInfoMap, tagLen, br);
        }
        // @thesis--8
        if (line.startsWith(type5)) {
          pubInfoMap = new HashMap<>();
          pubList.add(pubInfoMap);
          line = readNextLine(br);
          pubInfoMap.put("Ref", "Thesis");
          // 若出现type字段,以type标识论文属于哪种学位(硕士/博士/其他)论文,在buildMapData中构造,同理,以下三者的pro均遵从该规则
          pubInfoMap.put("pro", PubThesisDegreeEnum.OTHER.getValue());
          buildMapData(type5, type5Tags, line, pubInfoMap, tagLen, br);
        }
        // @phdthesis--8
        if (line.startsWith(type5_1)) {
          pubInfoMap = new HashMap<>();
          pubList.add(pubInfoMap);
          line = readNextLine(br);
          pubInfoMap.put("Ref", "Thesis");
          pubInfoMap.put("pro", PubThesisDegreeEnum.DOCTOR.getValue());
          buildMapData(type5_1, type5Tags, line, pubInfoMap, tagLen, br);
        }
        // @mastersthesis--8
        if (line.startsWith(type5_2)) {
          pubInfoMap = new HashMap<>();
          pubList.add(pubInfoMap);
          line = readNextLine(br);
          pubInfoMap.put("Ref", "Thesis");
          pubInfoMap.put("pro", PubThesisDegreeEnum.MASTER.getValue());
          buildMapData(type5_2, type5Tags, line, pubInfoMap, tagLen, br);
        }
        // @misc--7
        if (line.startsWith(type6) || line.startsWith(type6_1) || line.startsWith(type6_2) || line.startsWith(type6_3)
            || line.startsWith(type6_4) || line.startsWith(type6_5) || line.startsWith(type6_6)
            || line.startsWith(type6_7)) {
          line = readNextLine(br);
          pubInfoMap = new HashMap<>();
          pubList.add(pubInfoMap);
          pubInfoMap.put("Ref", "Others");
          buildMapData(type6, type6Tags, line, pubInfoMap, tagLen, br);
        }
        line = readNextLine(br);
      }
    } catch (Exception e) {
      logger.error("将文件拆分成map格式字符串时出错", e);
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

    return pubList;
  }

  private String readNextLine(BufferedReader br) throws IOException {
    String line = br.readLine();
    if (StringUtils.isNotBlank(line)) {
      line = line.trim();
    }
    return line;
  }


  private void buildMapData(String type, String[] contentTags, String line, Map<String, String> pubInfoMap, int tagLen,
      BufferedReader br) {
    try {
      while (line != null) {
        for (int i = 0; i < contentTags.length; i++) {
          String strLine = line.replaceAll("\\s+", "");
          if (strLine.startsWith(contentTags[i])) {
            String temp = line;
            int start = temp.indexOf("{") + 1;
            int end = temp.lastIndexOf("},");
            if (end < 0) {
              end = temp.lastIndexOf("}");
            }
            if (start == 0 || end < 0 || end < start) {
              temp = "";
            } else {
              temp = temp.substring(start, end);
            }
            // 如果发现有"pages="，则分拆成strat_page,end_page两个结点
            if ("pages=".equals(contentTags[i])) {
              pubInfoMap.put("page_number", temp);
            } else if ("editor=".equals(contentTags[i]) && type.equals(type4)) {
              pubInfoMap.put("edi4", temp);
            } else if ("author=".equals(contentTags[i])) {
              temp = temp.replaceAll(" and ", "; ");
              pubInfoMap.put("aut", temp);
            } else if ("editor=".equals(contentTags[i])) {
              temp = temp.replaceAll(" and ", "; ");
              pubInfoMap.put("edi", temp);
            } else if (("volume=".equals(contentTags[i]) || "number=".equals(contentTags[i])) && type.equals(type2)) {
              pubInfoMap.put("b_vol", temp);
            } else if (("volume=".equals(contentTags[i]) || "number=".equals(contentTags[i])) && type.equals(type3)) {
              pubInfoMap.put("i_vol", temp);
            } else if (("volume=".equals(contentTags[i]) || "number=".equals(contentTags[i])) && type.equals(type4)) {
              pubInfoMap.put("in_vol", temp);
            } else if ("booktitle=".equals(contentTags[i]) && type.equals(type3)) {
              pubInfoMap.put("conf_name", temp);
            } else if ("type=".equals(contentTags[i]) && !"thesis".equalsIgnoreCase(temp)) {
              // 由于从EndNote或者万方导出的数据会包含type字段,该字段依然标识了当前论文是哪种学位论文, 而且该字段是与论文所属类型是一致的,所以当该字段出现时以该字段作为标识该论文是哪种学位论文
              pubInfoMap.put("pro", PubThesisDegreeEnum.parse(temp).getValue());
            } else {
              pubInfoMap.put(contentTags[i].substring(0, tagLen), temp);
            }
          }
        }
        line = br.readLine();
        if (StringUtils.isNotBlank(line)) {
          line = line.trim();
          if ("}".equals(line)) {
            // line = br.readLine();
            break;
          }
        }
      }
    } catch (Exception e) {
      logger.error("", e);
    }
  }


  @Override
  public List<PubFileInfo> processData(List<Map<String, String>> list) {
    List<PubFileInfo> pubFileInfoList = new ArrayList<>();
    PubFileInfo pubFileInfo = null;
    if (list != null && list.size() > 0) {
      int seqNo = 1;
      for (Map<String, String> map : list) {
        pubFileInfo = new PubFileInfo();
        pubFileInfoList.add(pubFileInfo);
        pubFileInfo.setSeqNo(seqNo++);
        pubFileInfo.setTitle(map.get("tit"));
        pubFileInfo.setSourceDbCode("fileBibTex");

        if (StringUtils.isNotBlank(map.get("aut")) && StringUtils.isNotBlank(map.get("edi"))) {
          pubFileInfo.setAuthorNames(map.get("aut") + "; " + map.get("edi"));
        } else if (StringUtils.isNotBlank(map.get("aut"))) {
          pubFileInfo.setAuthorNames(map.get("aut"));
        } else {
          pubFileInfo.setAuthorNames(map.get("edi"));
        }
        pubFileInfo.setCabstract(map.get("abs"));

        if (StringUtils.isNotBlank(map.get("yea")) && StringUtils.isNotBlank(map.get("mon"))) {
          pubFileInfo.setPubyear(map.get("yea") + map.get("mon"));
        } else if (StringUtils.isNotBlank(map.get("yea"))) {
          pubFileInfo.setPubyear(map.get("yea"));
        } else {
          pubFileInfo.setPubyear(map.get("mon"));
        }
        pubFileInfo.setVolumeNo(substring(map.get("vol"), 20) + substring(map.get("b_vol"), 20)
            + substring(map.get("i_vol"), 20) + substring(map.get("in_vol"), 20));
        pubFileInfo.setIssue(substring(map.get("num"), 20));
        buildPubType(pubFileInfo, map);

        pubFileInfo.setOriginal(map.get("jou"));
        pubFileInfo.setPageNumber(map.get("page_number"));

        pubFileInfo.setKeywords(StringUtils.isNotBlank(map.get("key")) ? map.get("key") : map.get("aK"));

        pubFileInfo.setISBN(StringUtils.isNotBlank(map.get("ISB")) ? map.get("ISB") : map.get("At"));
        pubFileInfo.setIssn(map.get("ISS"));
        pubFileInfo.setDoi(StringUtils.isNotBlank(map.get("DOI")) ? map.get("DOI") : map.get("aR"));
        pubFileInfo.setSrcFulltextUrl(StringUtils.isNotBlank(map.get("url")) ? map.get("url") : map.get("aU"));
        pubFileInfo.setRegNo(map.get("Acc"));
        if (StringUtils.isNotBlank(map.get("Cit"))) {
          pubFileInfo.setCity(map.get("Cit"));
        } else {
          pubFileInfo.setCity(StringUtils.isNotBlank(map.get("conf_city")) ? map.get("conf_city") : map.get("aC"));
        }

        pubFileInfo.setFundInfo(map.get("FU"));
        pubFileInfo.setRemarks(map.get("not"));
        pubFileInfo.setCitations(NumberUtils.toInt(map.get("Ctd"), 0));
        buildExtraInfo(pubFileInfo, map);

        pubFileInfo.setCountry(map.get("Cou"));
        pubFileInfo.setApplier(map.get("aL"));


      }
    }
    return pubFileInfoList;
  }

  private void buildExtraInfo(PubFileInfo pubFileInfo, Map<String, String> map) {
    String ref = map.get("Ref");
    switch (ref) {
      case "Book":
        pubFileInfo.setPublisher(map.get("pub"));
        pubFileInfo.setSeriesName(map.get("ser"));
        pubFileInfo.setLanguage(StringUtils.isNotBlank(map.get("Lan")) ? map.get("Lan") : map.get("aG"));
        break;
      case "Book Section":
        pubFileInfo.setBookTitle(map.get("boo"));
        pubFileInfo.setChapterNo(map.get("cha"));
        pubFileInfo.setSeriesName(map.get("ser"));
        pubFileInfo.setPublisher(map.get("pub"));
        pubFileInfo.setEditors(map.get("edi4"));
        break;
      case "Thesis":

        pubFileInfo.setDegree(PubThesisDegreeEnum.parse(map.get("pro")));
        pubFileInfo.setIssuingAuthority(StringUtils.isNotBlank(map.get("sch")) ? map.get("sch") : map.get("uni"));
        pubFileInfo.setDepartment(map.get("aB"));
        break;
      case "Conference Paper":
      case "Conference Proceedings":
      case "Conference Proceeding":
        pubFileInfo.setConfName(StringUtils.isNotBlank(map.get("conf_name")) ? map.get("conf_name") : map.get("aB"));
        pubFileInfo.setStartDate(map.get("conf_year"));
        break;
    }
  }

  private void buildPubType(PubFileInfo pubFileInfo, Map<String, String> map) {
    String ref = map.get("Ref");
    int pubType = 7;
    switch (ref) {
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
    pubFileInfo.setPubType(pubType);
  }



  public static String substring(String str, int length) {
    if (StringUtils.isBlank(str)) {
      return "";
    }
    if (str.length() > length) {
      str = str.substring(0, length);
    }
    return str;
  }

}
