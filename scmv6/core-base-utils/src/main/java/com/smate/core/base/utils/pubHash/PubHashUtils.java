package com.smate.core.base.utils.pubHash;

import java.io.Serializable;

import org.apache.commons.lang.StringUtils;
import org.springframework.web.util.HtmlUtils;

import com.smate.core.base.utils.common.HashUtils;
import com.smate.core.base.utils.data.XmlUtil;
import com.smate.core.base.utils.string.ServiceUtil;

/**
 * 成果Hash.
 * 
 * @author liqinghua
 * 
 */
public class PubHashUtils implements Serializable {

  /**
   * 
   */
  private static final long serialVersionUID = 7154508805148985913L;
  private static final String PUNCTUATION = "[\\.。,，;；、!！\\?\\{\\}【】'‘\"\\(\\)（）\\[\\]]{1,}";

  /**
   * 清理标题.
   * 
   * @param title
   * @return String
   */
  public static String cleanTitle(String title) {
    if (StringUtils.isBlank(title)) {
      return null;
    }
    // 截取长度
    title = StringUtils.substring(title, 0, 2000);
    // 清理法语字符
    title = XmlUtil.fAccentToE(title);
    // 先转小写
    title = title.toLowerCase();
    // html解码
    title = HtmlUtils.htmlUnescape(title);
    // 清理法语字符， 先让其解完码，再进行替换法语字符
    title = XmlUtil.fAccentToE(title);
    title = XmlUtil.trimUnSupportHTML(title);
    title = XmlUtil.trimP(title);
    title = XmlUtil.trimAllHtml(title);
    title = XmlUtil.filterForCompare(title);
    // 去除标点符号
    title = title.replaceAll(PUNCTUATION, "");
    title = title.trim();

    if (StringUtils.isBlank(title)) {
      return null;
    }
    return title;
  }

  /**
   * 生成成果某些字段的组合hash.
   * 
   * @param values 字符数组
   * @return String
   */
  public static Long fingerPrint(String[] values) {
    int count = 0;
    for (int i = 0; i < values.length; i++) {
      // 为空，用None补充
      if (StringUtils.isBlank(values[i])) {
        values[i] = "NONE";
        count++;
      }
    }
    // 全部是空字符
    if (count == values.length) {
      return null;
    }
    String str = StringUtils.join(values, "|");
    return HashUtils.getStrHashCode(XmlUtil.getTrimBlankLower(str));
  }

  /**
   * 获取干净的doi hash.
   * 
   * @param doi
   * @return Long
   */
  public static Long cleanDoiHash(String doi) {
    if (StringUtils.isBlank(doi))
      return null;
    // 顺序不能变，一定在这里，移到下面会有问题
    doi = StringUtils.substring(doi, 0, 100);
    doi = HtmlUtils.htmlUnescape(doi);
    doi = doi.replaceAll("\\/", "");
    // 截取长度
    return HashUtils.getStrHashCode(XmlUtil.getTrimBlankLower(doi));
  }

  /**
   * 获取去除标点符号的doi hash.
   * 
   * @param doi
   * @return Long
   */
  public static Long getDoiHashRemotePun(String doi) {
    if (StringUtils.isBlank(doi))
      return null;
    // 顺序不能变，一定在这里，移到下面会有问题
    doi = StringUtils.substring(doi, 0, 100);
    doi = HtmlUtils.htmlUnescape(doi);
    doi = doi.replaceAll("\\/", "");
    // SCM-21253 增加对标点符号的去除
    doi = doi.replaceAll(PUNCTUATION, "");
    return HashUtils.getStrHashCode(XmlUtil.getTrimBlankLower(doi));
  }

  /**
   * 获取干净的IsiId hash.
   * 
   * @param IsiId
   * @return Long
   */
  public static Long cleanSourceIdHash(String sourceId) {
    if (StringUtils.isBlank(sourceId))
      return null;
    return HashUtils.getStrHashCode(XmlUtil.getTrimBlankLower(sourceId));
  }

  /**
   * 获取干净的issue hash.
   * 
   * @param issue
   * @return Long
   */
  public static Long cleanIssueHash(String issue) {
    if (StringUtils.isBlank(issue))
      return null;
    return HashUtils.getStrHashCode(XmlUtil.getTrimBlankLower(issue));
  }

  /**
   * 获取干净的patent no hash.
   * 
   * @param IsiId
   * @return Long
   */
  public static Long cleanPatentNoHash(String patentNo) {
    if (StringUtils.isBlank(patentNo))
      return null;
    // 截取长度
    patentNo = StringUtils.substring(patentNo, 0, 40);
    return HashUtils.getStrHashCode(XmlUtil.getTrimBlankLower(patentNo));
  }

  /**
   * 获取干净的成果会议名称hash.
   * 
   * @param title 标题
   * @return Long
   */
  public static Long cleanConfNameHash(String confName) {

    if (StringUtils.isBlank(confName)) {
      return null;
    }
    confName = HtmlUtils.htmlUnescape(confName);
    confName = XmlUtil.trimAllHtml(confName);
    confName = XmlUtil.filterForCompare(confName);
    confName = confName.replaceAll(PUNCTUATION, "");
    if (StringUtils.isBlank(confName)) {
      return null;
    }
    return HashUtils.getStrHashCode(XmlUtil.getTrimBlankLower(confName));
  }

  /**
   * 获取干净的成果地址hash.
   * 
   * @param title
   * @return String
   */
  public static Long cleanPubAddrHash(String pubAddr) {
    if (StringUtils.isBlank(pubAddr)) {
      return null;
    }
    // html解码
    pubAddr = HtmlUtils.htmlUnescape(pubAddr);
    pubAddr = XmlUtil.trimAllHtml(pubAddr);
    pubAddr = XmlUtil.filterForCompare(pubAddr);
    pubAddr = pubAddr.trim().toLowerCase();
    pubAddr = pubAddr.replaceAll(PUNCTUATION, "");
    if (StringUtils.isBlank(pubAddr)) {
      return null;
    }
    return HashUtils.getStrHashCode(XmlUtil.getTrimBlankLower(pubAddr));
  }

  /**
   * 获取干净的成果标题hash.
   * 
   * @param title 标题
   * @return Long
   */
  public static Long cleanTitleHash(String title) {

    if (StringUtils.isBlank(title)) {
      return null;
    }
    title = PubHashUtils.cleanTitle(title);
    if (StringUtils.isBlank(title)) {
      return null;
    }
    return HashUtils.getStrHashCode(XmlUtil.getTrimBlankLower(title));
  }

  /**
   * 中文库成果组合指纹，用于基准库同库查重.
   * 
   * @param year
   * @param original
   * @param author
   * @param title
   * @return
   */
  public static Long cleanTitleHash(String zhTitle, String enTitle) {

    // 标题处理
    zhTitle = PubHashUtils.cleanTitle(zhTitle);
    enTitle = PubHashUtils.cleanTitle(enTitle);
    String[] values = new String[] {zhTitle, enTitle};
    return fingerPrint(values);
  }

  /**
   * 获取作者hashcode.
   * 
   * @param auName
   * @return Long
   */
  public static Long getAuNameHash(String auName) {

    if (StringUtils.isBlank(auName)) {
      return null;
    }
    // html解码，删除所有html文本空格，分隔符
    auName = XmlUtil.filterAuForCompare(auName);
    if (StringUtils.isBlank(auName)) {
      return null;
    }
    return HashUtils.getStrHashCode(XmlUtil.getTrimBlankLower(auName));
  }

  /**
   * 专利号hash.
   * 
   * @param patentNo
   * @return Long
   */
  public static Long getPatentNoHash(String patentNo) {
    if (StringUtils.isBlank(patentNo)) {
      return null;
    }
    return HashUtils.getStrHashCode(XmlUtil.getTrimBlankLower(patentNo));
  }

  /**
   * 关键词Hash.
   * 
   * @param keyword
   * @return
   */
  public static Long getKeywordHash(String keyword) {
    if (StringUtils.isBlank(keyword)) {
      return null;
    }
    return HashUtils.getStrHashCode(XmlUtil.getTrimBlankLower(keyword));
  }

  /**
   * 获取字符串正序、反序 hashcode.
   * 
   * @param keyword
   * @return
   */
  public static Long[] getKeywordUnitHash(String keyword) {
    if (StringUtils.isBlank(keyword)) {
      return null;
    }
    // 中文去掉所有空格
    if (ServiceUtil.isChineseStr(keyword)) {
      return HashUtils.getSrUnitHashCode(XmlUtil.getTrimBlankLower(keyword));
    } else {
      // 英文保留单词之间的空格
      return HashUtils.getSrUnitHashCode(XmlUtil.getTrimDoubleBlankLower(keyword));
    }
  }

  /**
   * 英文库成果组合指纹，用于基准库同库查重.
   * 
   * @param year
   * @param original
   * @param author
   * @param title
   * @return
   */
  public static Long getEnPubUnitFingerPrint(Integer year, String original, String author) {

    // 作者名称处理
    author = XmlUtil.filterAuForCompare(author);

    // original处理
    original = PubHashUtils.cleanTitle(original);
    String[] values = new String[] {year == null ? null : year.toString(), original, author};
    return fingerPrint(values);
  }

  /**
   * 期刊articleNo指纹. jname + issn + volume + issue + article_no .
   * 
   * @param jname
   * @param issn
   * @param volume
   * @param issue
   * @param articleNo
   * @return
   */
  public static Long getJaFingerPrint(String jname, String issn, String volume, String issue, String articleNo) {
    jname = PubHashUtils.cleanTitle(jname);
    if (StringUtils.isBlank(jname) || StringUtils.isBlank(volume) || StringUtils.isBlank(articleNo)) {
      return null;
    }
    String[] values = new String[] {jname, issn, volume, issue, articleNo};
    return fingerPrint(values);
  }

  /**
   * 期刊pageStart指纹. jname + issn + volume + issue + page_start.
   * 
   * @param jname
   * @param issn
   * @param volume
   * @param issue
   * @param pageStart
   * @return
   */
  public static Long getJpFingerPrint(String jname, String issn, String volume, String issue, String pageStart) {
    jname = PubHashUtils.cleanTitle(jname);
    if (StringUtils.isBlank(jname) || StringUtils.isBlank(volume) || StringUtils.isBlank(pageStart)) {
      return null;
    }
    String[] values = new String[] {jname, issn, volume, issue, pageStart};
    return fingerPrint(values);
  }

  /**
   * 期刊指纹. jname + issn .
   * 
   * @param jname
   * @param issn
   * @param volume
   * @param issue
   * @param pageStart
   * @return
   */
  public static Long getJFingerPrint(String jname, String issn) {
    jname = PubHashUtils.cleanTitle(jname);
    if (StringUtils.isBlank(jname)) {
      return null;
    }
    String[] values = new String[] {jname, issn};
    return fingerPrint(values);
  }

  /**
   * 会议page_start指纹. cpfinger_print：Isbn + page_start.
   * 
   * @param isbn
   * @param pageStart
   * @return
   */
  public static Long getCpFingerPrint(String isbn, String pageStart) {
    if (StringUtils.isBlank(isbn) || StringUtils.isBlank(pageStart)) {
      return null;
    }
    String[] values = new String[] {isbn, pageStart};
    return fingerPrint(values);
  }

  /**
   * 会议article_no指纹. cafinger_print：Isbn + article_no .
   * 
   * @param isbn
   * @param articleNo
   * @return
   */
  public static Long getCaFingerPrint(String isbn, String articleNo) {
    if (StringUtils.isBlank(isbn) || StringUtils.isBlank(articleNo)) {
      return null;
    }
    String[] values = new String[] {isbn, articleNo};
    return fingerPrint(values);
  }

  /**
   * 会议指纹.issn + volume + article_no.
   * 
   * @param issn
   * @param volume
   * @param issue 允许空
   * @param articleNo
   * @return
   */
  public static Long getCvaFingerPrint(String issn, String volume, String issue, String articleNo) {
    if (issn == null || StringUtils.isBlank(volume) || StringUtils.isBlank(articleNo)) {
      return null;
    }
    String[] values = new String[] {issn, volume, issue, articleNo};
    return fingerPrint(values);
  }

  /**
   * 会议指纹.issn +  volume + page_start.
   * 
   * @param issn
   * @param volume
   * @param issue 允许空
   * @param pageStart
   * @return
   */
  public static Long getCvpFingerPrint(String issn, String volume, String issue, String pageStart) {
    if (issn == null || StringUtils.isBlank(volume) || StringUtils.isBlank(pageStart)) {
      return null;
    }
    String[] values = new String[] {issn, volume, issue, pageStart};
    return fingerPrint(values);
  }

  public static void main(String[] args) throws Exception {
    Long[] hs = PubHashUtils.getKeywordUnitHash("dna polymerase".toLowerCase().replaceAll("\\s+", " ").trim());
    System.out.println(hs[0]);
    System.out.println(hs[1]);
  }

  /**
   * 获取成果hash
   * 
   * @param title
   * @param pubType
   * @param year
   * @param byFunction
   * @return
   */
  public static String getTitleInfoHash(String title, Integer pubType, Integer year) {
    if (title == null) {
      title = "";
    }
    String pubTypeStr = "";
    if (pubType != null && pubType > 0) {
      pubTypeStr = pubType.toString();
    }
    String pubYearStr = "";
    if (year != null && year > 0) {
      pubYearStr = year.toString();
    }
    title = cleanTitle(title);
    pubTypeStr = pubTypeStr.trim();
    pubYearStr = pubYearStr.trim();
    return getTitleInfoHash(title, pubTypeStr, pubYearStr);
  }

  /**
   * 获取成果hash
   * 
   * @param title
   * @param pubType
   * @param year
   * @param byFunction
   * @return
   */
  public static String getTitleInfoHash(String title, String pubType, String year) {
    if (title == null) {
      title = "";
    }
    if (pubType == null) {
      pubType = "";
    }
    if (year == null) {
      year = "";
    }
    title = dealTitle(title);
    pubType = pubType.trim();
    year = year.trim();
    return HashUtils.getStrHashCode(title + pubType + year) + "";
  }

  /**
   * 获取由 标题+类型 生成的哈希值
   * 
   * @param title
   * @param pubType
   * @return
   */
  public static String getTpHash(String title, String pubType) {
    if (title == null) {
      title = "";
    }
    if (pubType == null) {
      pubType = "";
    }
    title = cleanTitle(title);
    pubType = pubType.trim();
    return HashUtils.getStrHashCode(title + pubType) + "";
  }

  /**
   * 对标题进行处理:去掉 1.统一小写 2.空格+and+空格 3.&amp; 4.html标签过滤 5.去掉标点符号以及空格
   * 
   * @param title
   * @return
   */
  @Deprecated
  private static String dealTitle(String title) {
    if (org.apache.commons.lang3.StringUtils.isNotBlank(title)) {
      title = title.toLowerCase();
      title = title.replaceAll(" and ", "");
      title = title.replaceAll("&amp;", "");
      title = com.smate.core.base.utils.common.HtmlUtils.Html2Text(title);
      title = title.replaceAll("[\\pP\\p{Punct}\\pZ\\pS]", "");
    }
    return title;
  }

  /**
   * 著作中文标题加isbn.
   * 
   * @param isbn
   * @param zhTitle
   * @return
   */
  public static Long getBzFingerPrint(String isbn, String zhTitle) {
    if (StringUtils.isBlank(isbn)) {
      return null;
    }
    String[] values = new String[] {isbn, zhTitle};
    return fingerPrint(values);
  }

  /**
   * 著作英文标题加isbn.
   * 
   * @param isbn
   * @param enTitle
   * @return
   */
  public static Long getBeFingerPrint(String isbn, String enTitle) {
    if (StringUtils.isBlank(isbn)) {
      return null;
    }
    String[] values = new String[] {isbn, enTitle};
    return fingerPrint(values);
  }

}
