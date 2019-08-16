package com.smate.core.base.pub.util;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;

import com.smate.core.base.consts.model.ConstRegion;
import com.smate.core.base.pub.enums.PubPatentAreaEnum;
import com.smate.core.base.pub.enums.PubThesisDegreeEnum;
import com.smate.core.base.utils.data.XmlUtil;
import com.smate.core.base.utils.date.DateStringSplitFormateUtil;

public class PubParamUtils {

  /**
   * 构造pageNumber参数
   *
   * @param startPage
   * @param endPage
   * @param articleNo
   * @return
   */
  public static String buildPageNumber(String startPage, String endPage, String articleNo) {
    startPage = StringUtils.trimToEmpty(startPage);
    endPage = StringUtils.trimToEmpty(endPage);
    articleNo = StringUtils.trimToEmpty(articleNo);
    boolean flag = StringUtils.isNotBlank(startPage) && !"0".equals(startPage) && StringUtils.isNotBlank(endPage)
        && !"0".equals(endPage);
    if (flag) {
      return startPage + "-" + endPage;
    }
    if (StringUtils.isNotBlank(startPage) && !"0".equals(startPage)) {
      return startPage;
    }
    if (StringUtils.isNotBlank(endPage) && !"0".equals(endPage)) {
      return endPage;
    }
    return articleNo;
  }

  /**
   * 构造专利类型的描述信息 默认返回为空
   *
   * @param type
   * @return
   */
  public static String buildPatentTypeDesc(String type) {
    if (StringUtils.isEmpty(type)) {
      return "";
    }
    if ("51".equals(type)) {
      return "发明专利";
    }
    if ("52".equals(type)) {
      return "实用新型";
    }
    if ("53".equals(type)) {
      return "外观设计";
    }
    if ("54".equals(type)) {
      return "植物专利";
    }
    return type;
  }

  public static String rebuildAuthorNames(String authorNames) {
    if (StringUtils.isNotEmpty(authorNames)) {
      // authorNames 传入有值的话，则不进行构造，但是要处理一下数据
      List<String> authorList = AuthorNamesUtils.parsePubAuthorNames(authorNames);
      authorNames = buildPubAuthorNames(authorList);
    }
    authorNames = XmlUtil.formateSymbolAuthors(authorNames, authorNames);
    return authorNames;
  }

  /**
   * 重构成果作者
   *
   * @param members
   * @return authorNames
   */
  public static String buildPubAuthorNames(List<String> authors) {
    StringBuffer authorNames = new StringBuffer();
    if (CollectionUtils.isEmpty(authors)) {
      return null;
    }
    for (String name : authors) {
      if (StringUtils.isBlank(name)) {
        continue;
      }
      name = name.replace(" ", "");
      if (authorNames.length() > 0) {
        authorNames.append("; ");
      }
      authorNames.append(name);
    }
    // 作者的长度超过400，则进行截取
    if (authorNames.length() >= 400) {
      authorNames.substring(0, 399);
    }
    return authorNames.toString();
  }

  /**
   * 获取最大的引用次数
   *
   * @param old
   * @param now
   * @return
   */
  public static Integer maxCitations(Integer old, Integer now) {
    if (old == null) {
      return now;
    }
    if (now == null) {
      return old;
    }
    if (now >= old) {
      return now;
    }
    return old;
  }

  /**
   * 获取专利的发证单位
   *
   * @param area
   * @return
   */
  public static String buildPatentIssuingAuthority(PubPatentAreaEnum area) {
    String flag = area.getValue();
    switch (flag) {
      case "CHINA":
        return "国家知识产权局(SIPO)";
      case "USA":
        return "United States Patent and Trademark Office(USPTO)";
      case "EUROPE":
        return "European Patent Office (EPO)";
      case "JAPAN":
        return "Japan Patent Office(JPO)";
      case "WIPO":
        return "World Intellectual property organization";
      case "OTHER":
        return "";
    }
    return "";
  }

  /**
   * 根据年月日重新构造标准日期
   *
   * @param year
   * @param month
   * @param day
   * @return
   */
  public static String buildDate(String year, String month, String day) {
    if (StringUtils.isBlank(year)) {
      return "";
    }
    if (StringUtils.isBlank(month)) {
      return year;
    }
    if (StringUtils.isBlank(day)) {
      return year + "-" + month;
    }

    return year + "-" + month + "-" + day;
  }

  /**
   * 判断是否是ISI的dbCode
   *
   * @param sourceDbCode
   * @return
   */
  public static boolean isISIDbCode(String sourceDbCode) {
    return sourceDbCode.equalsIgnoreCase("ISTP") || sourceDbCode.equalsIgnoreCase("SCI")
        || sourceDbCode.equalsIgnoreCase("SCIE") || sourceDbCode.equalsIgnoreCase("SSCI")
        || sourceDbCode.equalsIgnoreCase("Open Access Library");
  }

  /**
   * 判断是否是CNKI的dbCode
   *
   * @param sourceDbCode
   * @return
   */
  public static boolean isCNKIDbCode(String sourceDbCode) {
    return sourceDbCode.equalsIgnoreCase("ChinaJournal");
  }

  /**
   * 判断是否是EI的dbCode
   *
   * @param sourceDbCode
   * @return
   */
  public static boolean isEIDbCode(String sourceDbCode) {
    return sourceDbCode.equalsIgnoreCase("EI");
  }

  /**
   * 判断是否是Cnkipat或者RAINPAT的dbCode
   *
   * @param sourceDbCode
   * @return
   */
  public static boolean isCNIPRDbCode(String sourceDbCode) {
    return sourceDbCode.equalsIgnoreCase("Cnkipat") || sourceDbCode.equalsIgnoreCase("RAINPAT")
        || sourceDbCode.equalsIgnoreCase("CNIPR");
  }

  /**
   * 重新构造dbId
   * 
   * @param dbId
   * @return
   */
  public static Integer combineDbid(Integer dbId) {
    if (dbId == 15 || dbId == 16 || dbId == 17) {
      dbId = 99;
    }
    if (dbId == 11 || dbId == 21 || dbId == 31) {
      dbId = 98;
    }
    return dbId;
  }

  /**
   * 重置引用次数，只有dbId为15，16，17的引用次数才要存起来 所属文献库是：SCI，SSCI，ISTP
   * 
   * @param dbId
   * @param citations
   * @return
   */
  public static Integer resetCitation(Integer dbId, Integer citations) {
    if (dbId == null) {
      return 0;
    }
    if (dbId == 15 || dbId == 16 || dbId == 17) {
      return citations == null ? 0 : citations;
    }
    return 0;
  }

  /**
   * 根据收录机构名获取dbId
   * 
   * @param libraryName
   * @return
   */
  public static Integer buildDbId(String libraryName) {
    if ("SCI".equalsIgnoreCase(libraryName) || "SCIE".equalsIgnoreCase(libraryName)) {
      return 16;
    }
    if ("SSCI".equalsIgnoreCase(libraryName)) {
      return 17;
    }
    if ("ISTP".equalsIgnoreCase(libraryName)) {
      return 15;
    }
    if ("EI".equalsIgnoreCase(libraryName)) {
      return 14;
    }
    if ("CSSCI".equalsIgnoreCase(libraryName)) {
      return 34;
    }
    return null;
  }

  /**
   * 去除html标记
   * 
   * @param text
   * @return
   */
  public static String trimAllHtml(String text) {
    text = org.apache.commons.lang.StringUtils.trimToEmpty(text);
    text = text.replaceAll("\\<.*?>", "").replaceAll("\\s+", " ");
    return text;
  }

  /**
   * 重置引用次数
   * 
   * @param dbId
   * @param citations
   * @return
   */
  public static Integer resetCitations(Integer dbId, Integer citations) {
    if (dbId == null) {
      return 0;
    }
    if (dbId == 15 || dbId == 16 || dbId == 17) {
      return citations == null ? 0 : citations;
    }
    return 0;
  }


  public static String dealWithSpace(String data) {
    if (StringUtils.isEmpty(data)) {
      return "";
    }
    data = data.replace("&lt;br/&gt;", "\n");
    data = data.replace("&lt;br&gt;", "\n");
    data = data.replace("&lt;p&gt;", "\n");
    return data;
  }

  /**
   * 基准库匹配国家或地区Id
   *
   * @param regionList
   * @param countryName
   * @return
   */
  public static Long matchCountryId(List<ConstRegion> regionList, String countryName) {
    if (StringUtils.isEmpty(countryName)) {
      return null;
    }
    List<Long> countryIds = new ArrayList<>();
    if (CollectionUtils.isNotEmpty(regionList)) {
      for (ConstRegion constRegion : regionList) {
        // 先处理地区名，全部转为小写
        String zhRegionName = cleanCountryName(constRegion.getZhName());
        String enRegionName = cleanCountryName(constRegion.getEnName());
        countryName = cleanCountryName(countryName);
        // 匹配
        boolean isMatch = (countryName.contains(zhRegionName) && StringUtils.isNotEmpty(zhRegionName))
            || (countryName.contains(enRegionName) && StringUtils.isNotEmpty(enRegionName));
        if (isMatch) {
          countryIds.add(constRegion.getId());
        }
      }
    }
    if (CollectionUtils.isNotEmpty(countryIds)) {
      // 取最大的地区id
      Long countryId = countryIds.stream().mapToLong(regionId -> regionId).summaryStatistics().getMax();
      return countryId;
    }
    return null;
  }

  /**
   * clean国家或地区名
   *
   * @param countryName
   * @return
   */
  private static String cleanCountryName(String countryName) {
    if (StringUtils.isEmpty(countryName)) {
      return "";
    }
    countryName = countryName.replace("市", "");
    countryName = countryName.replace("省", "");
    countryName = countryName.replace("自治区", "");
    countryName = countryName.replace("自治州", "");
    countryName = countryName.replace("自治县", "");
    countryName = countryName.replace("地区", "");
    countryName = countryName.replace("盟", "");
    //countryName = countryName.replace("林区", "");  山西太原  会匹配 西林区  2019-04-25ajb
    countryName = countryName.replace("的岛礁及其海域", "");
    countryName = countryName.replace("土家族", "");
    countryName = countryName.replace("苗族", "");
    countryName = countryName.replace("布依族", "");
    countryName = countryName.replace("侗族", "");
    countryName = countryName.replace("壮族", "");
    countryName = countryName.replace("黎族", "");
    countryName = countryName.replace("藏族", "");
    countryName = countryName.replace("羌族", "");
    countryName = countryName.replace("蒙古族", "");
    countryName = countryName.replace("彝族", "");
    countryName = countryName.replace("朝鲜族", "");
    countryName = countryName.replace("哈尼族", "");
    countryName = countryName.replace("傣族", "");
    countryName = countryName.replace("白族", "");
    countryName = countryName.replace("景颇族", "");
    countryName = countryName.replace("僳族", "");
    countryName = countryName.toLowerCase();
    return countryName;
  }

  public static Integer countStr(String str, String falg) {
    Integer count = 0;
    if (StringUtils.isNotBlank(str) && StringUtils.isNotBlank(falg)) {
      char[] chars = str.toCharArray();
      for (char c : chars) {
        if (falg.equalsIgnoreCase(c + "")) {
          count++;
        }
      }
    }
    return count;
  }

  public static PubPatentAreaEnum buildArea(String patent_open_no) {
    if (StringUtils.isBlank(patent_open_no)) {
      return PubPatentAreaEnum.OTHER;
    }
    patent_open_no = patent_open_no.toUpperCase();
    if (patent_open_no.contains("US")) {
      return PubPatentAreaEnum.USA;
    }
    if (patent_open_no.contains("WO")) {
      return PubPatentAreaEnum.WIPO;
    }
    if (patent_open_no.contains("EP")) {
      return PubPatentAreaEnum.EUROPE;
    }
    if (patent_open_no.contains("JP")) {
      return PubPatentAreaEnum.JAPAN;
    }
    if (patent_open_no.contains("CN")) {
      return PubPatentAreaEnum.CHINA;
    }
    return PubPatentAreaEnum.OTHER;
  }

  public static String buildPatentType(String string) {
    if (StringUtils.isBlank(string)) {
      return "";
    }
    if ("发明专利".equals(string)) {
      return "51";
    }
    if ("实用新型".equals(string)) {
      return "52";
    }
    if ("外观设计".equals(string)) {
      return "53";
    }
    if ("植物专利".equals(string)) {
      return "54";
    }
    return "";
  }

  public static PubThesisDegreeEnum buildDegree(String degree) {
    if (StringUtils.isBlank(degree)) {
      return PubThesisDegreeEnum.OTHER;
    }
    degree = StringUtils.trimToEmpty(degree);
    if ("硕士".equalsIgnoreCase(degree)) {
      return PubThesisDegreeEnum.MASTER;
    }
    if ("博士".equalsIgnoreCase(degree)) {
      return PubThesisDegreeEnum.DOCTOR;
    }
    if ("其他".equalsIgnoreCase(degree)) {
      return PubThesisDegreeEnum.OTHER;
    }
    return PubThesisDegreeEnum.OTHER;
  }

  /**
   * content是否含有中文
   * 
   * @param content
   */
  public static boolean isChinese(String content) {
    if (StringUtils.isBlank(content)) {
      return false;
    }
    Pattern p = Pattern.compile("[\u4e00-\u9fa5]");
    if (p.matcher(content).find()) {
      return true;
    }
    return false;
  }


  /**
   * 格式化日期
   * 
   * @param date
   * @return
   */
  public static String formatDate(String date) {
    if (StringUtils.isBlank(date)) {
      return "";
    }
    Map<String, String> publishDateMap = DateStringSplitFormateUtil.split(date);
    date = publishDateMap.get("fomate_date");
    return date;
  }

}
