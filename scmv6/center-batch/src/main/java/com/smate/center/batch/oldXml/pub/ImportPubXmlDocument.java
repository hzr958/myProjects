package com.smate.center.batch.oldXml.pub;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.math.NumberUtils;
import org.dom4j.Attribute;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.dom4j.Node;

import com.smate.center.batch.constant.ImportPubXmlConstants;
import com.smate.core.base.utils.data.XmlUtil;
import com.smate.core.base.utils.exception.InvalidXpathException;

/**
 * 导入成果document
 * 
 * @author liqinghua
 * 
 */
public class ImportPubXmlDocument {

  /**
   * 
   */
  private Document xmlDocument = null;

  // publication节点中常用attribute
  public String[] commonXml = {"tmpsource_url", "source_url", "fulltext_url", "tmpcite_record_url", "etitle", "ctitle",
      "pub_type", "organization", "organization_spec", "eabstract", "cabstract", "ekeywords", "ckeywords", "issue_date",
      "original", "source", "description", "pubyear", "doc_type", "source_db_code", "sc", "journal_category_no",
      "fundinfo", "author_names_abbr", "source_id", "book_title", "series_name", "effective_start_date",
      "effective_end_date", "start_date", "end_date", "citation_index", "amount", "article_number", "doi",
      "publish_state", "publisher", "organizer", "conf_venue", "proceeding_title", "pub_date_desc", "thesis_ins_name",
      "thesis_dept_name", "thesis_programme", "remark", "keyword_plus", "accept_date", "patent_no", "patent_category",
      "patent_issue_date", "patent_open_no", "country", "city", "patent_main_category_no", "patent_category_no",
      "patent_priority", "patent_agent_org", "patent_agent_person", "legal_status", "patent_validity",
      "application_date", "start_page", "end_page", "issn", "issue", "volume", "isbn"};

  public String[] OtherXml = {"start_page", "end_page", "issn", "issue", "volume", "isbn"};

  // 在构造基准库xml时，publication节点下需要特殊处理的attribute
  public String[] xmlAttrSpec =
      {"cite_record_url", "ei_cite_record_url", "cnki_cite_record_url", "cnipr_cite_record_url", "cite_times",
          "ei_cite_times", "cnki_cite_times", "cnipr_cite_times", "author_names", "authors_names_spec"};

  /**
   * 构造DOCUMENT.
   * 
   * @param xmlData Xml字符串
   * @throws DocumentException DocumentException
   */
  public ImportPubXmlDocument(String xmlData) throws DocumentException {
    try {
      xmlDocument = DocumentHelper.parseText(xmlData);
    } catch (DocumentException e) {
      throw e;
    }
  }

  /**
   * 获取data节点的seq_no.
   * 
   * @return
   */
  public String getSeqNo() {
    return this.getXmlNodeAttribute(ImportPubXmlConstants.ROOT_XPATH, "seq_no");
  }

  /**
   * 获取常量数组
   * 
   * @return
   */
  public String[] getCommonXml() {
    return this.commonXml;
  }

  public List<String> getXmlAttrSpecList() {
    return Arrays.asList(xmlAttrSpec);
  }

  public List<String> getOtherXmlList() {
    return Arrays.asList(OtherXml);
  }

  public List<String> getCommonXmlList() {
    return Arrays.asList(commonXml);
  }

  /**
   * 获取成果年份.
   * 
   * @return
   */
  public Integer getPubYear() {

    String pubyear = this.getXmlNodeAttribute(ImportPubXmlConstants.PUBLICATION_XPATH, "pubyear");
    if (StringUtils.isBlank(pubyear)) {
      pubyear = this.getXmlNodeAttribute(ImportPubXmlConstants.PUBLICATION_XPATH, "start_date");
    }
    if (StringUtils.isBlank(pubyear)) {
      return null;
    }
    pubyear = pubyear.replaceAll("\\s+", "");
    String[] temp = pubyear.split("[-/]");
    String year = "0".equals(temp[0]) || StringUtils.isBlank(temp[0]) ? "" : temp[0];
    year = XmlUtil.changeSBCChar(year);
    // SCM-13338 rainpat xml时间需要截取
    if (year.length() > 4) {
      year = year.substring(0, 4);
    }
    if (NumberUtils.isDigits(year)) {
      return Integer.valueOf(year);
    }
    return null;
  }

  /**
   * 获取数据库Code.
   * 
   * @return
   */
  public String getSourceDbCode() {

    return this.getXmlNodeAttribute(ImportPubXmlConstants.PUBLICATION_XPATH, "source_db_code");
  }

  /**
   * 获取文献库唯一ID.
   * 
   * @return
   */
  public String getSourceId() {

    return this.getXmlNodeAttribute(ImportPubXmlConstants.PUBLICATION_XPATH, "source_id");
  }

  /**
   * 获取DOI.
   * 
   * @return
   */
  public String getDoi() {

    return this.getXmlNodeAttribute(ImportPubXmlConstants.PUBLICATION_XPATH, "doi");
  }

  /**
   * 获取英文标题.
   * 
   * @return
   */
  public String getEnTitle() {

    return this.getXmlNodeAttribute(ImportPubXmlConstants.PUBLICATION_XPATH, "etitle");
  }

  /**
   * 获取中文标题.
   * 
   * @return
   */
  public String getZhTitle() {

    return this.getXmlNodeAttribute(ImportPubXmlConstants.PUBLICATION_XPATH, "ctitle");
  }

  /**
   * 获取成果类别.
   * 
   * @return
   */
  public Integer getPubType() {

    String pubType = this.getXmlNodeAttribute(ImportPubXmlConstants.PUBLICATION_XPATH, "pub_type");
    pubType = pubType.replaceAll("\\s+", "");
    if (NumberUtils.isDigits(pubType)) {
      return Integer.valueOf(pubType);
    }
    return null;
  }

  /**
   * 获取成果类别.
   * 
   * @return
   */
  public Integer getPatentCategory() {

    String pubType = this.getXmlNodeAttribute(ImportPubXmlConstants.PUBLICATION_XPATH, "patent_category");
    pubType = pubType.replaceAll("\\s+", "");
    if (NumberUtils.isDigits(pubType)) {
      return Integer.valueOf(pubType);
    }
    return null;
  }

  /**
   * 获取出版刊物.
   * 
   * @return
   */
  public String getOriginal() {

    return this.getXmlNodeAttribute(ImportPubXmlConstants.PUBLICATION_XPATH, "original");
  }

  /**
   * 获取issn.
   * 
   * @return
   */
  public String getIssn() {

    String issn = this.getXmlNodeAttribute(ImportPubXmlConstants.PUBLICATION_XPATH, "issn");
    issn = XmlUtil.buildStandardIssn(StringUtils.trim(issn));
    return issn;
  }

  /**
   * 获取isbn.
   * 
   * @return
   */
  public String getIsbn() {

    return this.getXmlNodeAttribute(ImportPubXmlConstants.PUBLICATION_XPATH, "isbn");
  }

  /**
   * 获取issue.
   * 
   * @return
   */
  public String getIssue() {

    return this.getXmlNodeAttribute(ImportPubXmlConstants.PUBLICATION_XPATH, "issue");
  }

  /**
   * 获取volume.
   * 
   * @return
   */
  public String getVolume() {

    return this.getXmlNodeAttribute(ImportPubXmlConstants.PUBLICATION_XPATH, "volume");
  }

  /**
   * 起始页.
   * 
   * @return
   */
  public String getStartPage() {

    return this.getXmlNodeAttribute(ImportPubXmlConstants.PUBLICATION_XPATH, "start_page");
  }

  /**
   * 结束页.
   * 
   * @return
   */
  public String getEndPage() {

    return this.getXmlNodeAttribute(ImportPubXmlConstants.PUBLICATION_XPATH, "end_page");
  }

  /**
   * article_number.
   * 
   * @return
   */
  public String getArticleNo() {

    return this.getXmlNodeAttribute(ImportPubXmlConstants.PUBLICATION_XPATH, "article_number");
  }

  /**
   * 作者名称.
   * 
   * @return
   */
  public String getAuthorNames() {

    return this.getXmlNodeAttribute(ImportPubXmlConstants.PUBLICATION_XPATH, "author_names");
  }

  /**
   * 作者名spec.
   * 
   * @return
   */
  public String getAuthorNameSpec() {
    return this.getXmlNodeAttribute(ImportPubXmlConstants.PUBLICATION_XPATH, "authors_names_spec");
  }

  /**
   * 专利号.
   * 
   * @return
   */
  public String getPatentNo() {

    return this.getXmlNodeAttribute(ImportPubXmlConstants.PUBLICATION_XPATH, "patent_no");
  }

  /**
   * 专利公开号.
   * 
   * @return
   */
  public String getPatentOpenNo() {

    return this.getXmlNodeAttribute(ImportPubXmlConstants.PUBLICATION_XPATH, "patent_open_no");
  }

  /**
   * 会议名称.
   * 
   * @return
   */
  public String getConfName() {

    return this.getXmlNodeAttribute(ImportPubXmlConstants.PUBLICATION_XPATH, "proceeding_title");
  }

  /**
   * 获取isi引用次数.
   * 
   * @return
   */
  public Integer getCiteTimes() {
    String citeTimes = this.getXmlNodeAttribute(ImportPubXmlConstants.PUBLICATION_XPATH, "cite_times");
    citeTimes = citeTimes.replaceAll("\\s+", "");
    if (NumberUtils.isDigits(citeTimes)) {
      return Integer.valueOf(citeTimes);
    }
    return null;
  }

  /**
   * 获取rainpat 引用次数.
   * 
   * @return
   */
  public Integer getCniprCiteTimes() {
    String citeTimes = this.getXmlNodeAttribute(ImportPubXmlConstants.PUBLICATION_XPATH, "cnipr_cite_times");
    citeTimes = citeTimes.replaceAll("\\s+", "");
    if (NumberUtils.isDigits(citeTimes)) {
      return Integer.valueOf(citeTimes);
    }
    return null;
  }

  /**
   * 获取ei引用次数.
   * 
   * @return
   */
  public Integer getEiCiteTimes() {
    String citeTimes = this.getXmlNodeAttribute(ImportPubXmlConstants.PUBLICATION_XPATH, "ei_cite_times");
    citeTimes = citeTimes.replaceAll("\\s+", "");
    if (NumberUtils.isDigits(citeTimes)) {
      return Integer.valueOf(citeTimes);
    }
    return null;
  }

  /**
   * 获取cnki引用次数.
   * 
   * @return
   */
  public Integer getCnkiCiteTimes() {
    String citeTimes = this.getXmlNodeAttribute(ImportPubXmlConstants.PUBLICATION_XPATH, "cnki_cite_times");
    citeTimes = citeTimes.replaceAll("\\s+", "");
    if (NumberUtils.isDigits(citeTimes)) {
      return Integer.valueOf(citeTimes);
    }
    return null;
  }

  /**
   * 获取成果单位.
   * 
   * @return
   */
  public String getOrgnization() {

    String organization = this.getXmlNodeAttribute(ImportPubXmlConstants.PUBLICATION_XPATH, "organization");
    return organization;
  }

  /**
   * 获取成果单位.
   * 
   * @return
   */
  public String getOrgnizationSpec() {

    String organizationSpec = this.getXmlNodeAttribute(ImportPubXmlConstants.PUBLICATION_XPATH, "organization_spec");
    return organizationSpec;
  }

  /**
   * 获取中文关键词.
   * 
   * @return
   */
  public String getZhKeywords() {

    String ckeywords = this.getXmlNodeAttribute(ImportPubXmlConstants.PUBLICATION_XPATH, "ckeywords");
    return ckeywords;
  }

  /**
   * 获取英文关键词.
   * 
   * @return
   */
  public String getEnKeywords() {

    String ckeywords = this.getXmlNodeAttribute(ImportPubXmlConstants.PUBLICATION_XPATH, "ekeywords");
    return ckeywords;
  }

  /**
   * 获取基金资助信息.
   * 
   * @return
   */
  public String getFundInfo() {
    String FundInfo = this.getXmlNodeAttribute(ImportPubXmlConstants.PUBLICATION_XPATH, "fundinfo");
    return FundInfo;
  }

  /**
   * 解析基准库字段数据.
   * 
   * @return
   */
  public Map<String, Object> prasePubWhData() {

    Integer pubYear = getPubYear();
    String sourceDbCode = StringUtils.trim(getSourceDbCode());
    String sourceId = StringUtils.trim(getSourceId());
    String doi = StringUtils.trim(getDoi());
    String enTitle = StringUtils.trim(getEnTitle());
    String zhTitle = StringUtils.trim(getZhTitle());
    Integer pubType = getPubType();
    String original = StringUtils.trim(getOriginal());
    String issn = StringUtils.trim(getIssn());
    String isbn = StringUtils.trim(getIsbn());
    String issue = StringUtils.trim(getIssue());
    String volume = StringUtils.trim(getVolume());
    String startPage = StringUtils.trim(getStartPage());
    String endPage = StringUtils.trim(getEndPage());
    String articleNo = StringUtils.trim(getArticleNo());
    String authorNames = StringUtils.trim(getAuthorNames());
    String patentNo = StringUtils.trim(getPatentNo());
    String patentOpenNo = StringUtils.trim(getPatentOpenNo());
    String confName = StringUtils.trim(getConfName());
    String organization = getOrgnization();
    Integer citeTimes = getCiteTimes();
    String zhKeywords = StringUtils.trim(getZhKeywords());
    String enKeywords = StringUtils.trim(getEnKeywords());
    String fundInfo = StringUtils.trim(getFundInfo());
    String categoryNo = StringUtils.trim(getCategoryNo());
    String scienceCategory = StringUtils.trim(getScienceCategory());

    Map<String, Object> map = new HashMap<String, Object>();
    map.put("pubYear", pubYear);
    map.put("sourceDbCode", sourceDbCode);
    map.put("sourceId", sourceId);
    map.put("doi", doi);
    map.put("enTitle", enTitle);
    map.put("zhTitle", zhTitle);
    map.put("pubType", pubType);
    map.put("original", original);
    map.put("issn", issn);
    map.put("isbn", isbn);
    map.put("issue", issue);
    map.put("volume", volume);
    map.put("startPage", startPage);
    map.put("endPage", endPage);
    map.put("articleNo", articleNo);
    map.put("authorNames", authorNames);
    map.put("patentNo", patentNo);
    map.put("patentOpenNo", patentOpenNo);
    map.put("confName", confName);
    map.put("citeTimes", citeTimes);
    map.put("organization", organization);
    map.put("zhKeywords", zhKeywords);
    map.put("enKeywords", enKeywords);
    map.put("fundInfo", fundInfo);
    map.put("categoryNo", categoryNo);
    map.put("sc", scienceCategory);
    return map;
  }

  /**
   * 为解析基准库字段数据，数据比修改前更全面.
   * 
   * @return
   */
  public Map<String, Object> prasePdwhImportPubDataNew() {

    Map<String, Object> map = new HashMap<String, Object>();

    for (String attr : this.commonXml) {
      String attrValue = StringUtils.trim(this.getXmlNodeAttribute(ImportPubXmlConstants.PUBLICATION_XPATH, attr));
      map.put(attr, attrValue);
    }

    for (String attrSpec : this.xmlAttrSpec) {
      String attrSpecValue =
          StringUtils.trim(this.getXmlNodeAttribute(ImportPubXmlConstants.PUBLICATION_XPATH, attrSpec));
      map.put(attrSpec, attrSpecValue);
    }

    // 特殊处理部分
    Integer patentCategory = getPatentCategory();
    map.put("patent_category", patentCategory);
    Integer pubYear = getPubYear();
    map.put("pubyear", pubYear);
    Integer pubType = getPubType();
    map.put("pub_type", pubType);
    Integer citeTimes = getCiteTimes();
    map.put("cite_times", citeTimes);
    Integer eiCiteTimes = getEiCiteTimes();
    map.put("ei_cite_times", eiCiteTimes);
    Integer cnkiCiteTimes = getCnkiCiteTimes();
    map.put("cnki_cite_times", cnkiCiteTimes);

    return map;
  }

  /**
   * @param xpath xml元素路径
   * @param attrName xml属性名
   * @return String
   */
  public String getXmlNodeAttribute(String xpath, String attrName) {
    if (this.xmlDocument == null) {
      throw new java.lang.NullPointerException("xml doc is null.");
    }
    Node node = this.getNode(xpath);
    if (node != null) {
      return getXmlNodeAttributeValue(node, attrName);
    }
    return "";
  }

  /**
   * @param node xml节点
   * @param attrName xml属性名
   * @return String
   */
  public String getXmlNodeAttributeValue(Node node, String attrName) {
    if (node == null) {
      return "";
    }
    String val = node.valueOf("@" + attrName);
    if (val == null) {
      val = "";
    }
    return val.trim();
  }

  /**
   * @param xpath xml元素路径
   * @return boolean
   */
  public boolean existsNode(String xpath) {
    if (this.xmlDocument == null) {
      throw new java.lang.NullPointerException("xml doc is null.");
    }
    Node node = this.getNode(xpath);
    return node != null;
  }

  /**
   * @param xpath xml元素路径
   * @param attrName xml属性
   * @return boolean
   */
  public boolean existsNodeAttribute(String xpath, String attrName) {
    if (this.xmlDocument == null) {
      throw new java.lang.NullPointerException("xml doc is null.");
    }
    Node node = this.getNode(xpath);
    if (node == null) {
      return false;
    }
    Element ele = (Element) node;
    Attribute attr = ele.attribute(attrName);
    return attr != null;
  }

  /**
   * @param xpath xml元素路径
   */
  public void removeNode(String xpath) {
    if (this.xmlDocument == null) {
      throw new java.lang.NullPointerException("xml doc is null.");
    }
    Node node = this.getNode(xpath);
    if (node != null) {
      boolean result = this.xmlDocument.remove(node);
      if (!result) {
        node.detach();
      }
    }
  }

  /**
   * 获取根节点.
   * 
   * @return Node
   */
  public Node getRootNode() {
    return this.xmlDocument.selectSingleNode(ImportPubXmlConstants.ROOT_XPATH);
  }

  /**
   * @param xpath Xml元素路径
   * @return List
   */
  @SuppressWarnings("rawtypes")
  public List getNodes(String xpath) {
    if (this.xmlDocument == null) {
      throw new java.lang.NullPointerException("xml doc is null.");
    }
    if (!xpath.startsWith(ImportPubXmlConstants.ROOT_XPATH))
      xpath = ImportPubXmlConstants.ROOT_XPATH + xpath;
    return this.xmlDocument.selectNodes(xpath);
  }

  /**
   * @param xpath xml元素路径
   * @return Node
   */
  public Node getNode(String xpath) {
    if (this.xmlDocument == null) {
      throw new java.lang.NullPointerException("xml doc is null.");
    }
    if (!xpath.startsWith(ImportPubXmlConstants.ROOT_XPATH))
      xpath = ImportPubXmlConstants.ROOT_XPATH + xpath;
    return this.xmlDocument.selectSingleNode(xpath);
  }

  /**
   * @param fullPath xml元素路径
   * @return String
   * @throws InvalidXpathException InvalidXpathException
   */
  public String getXmlNodeAttribute(String fullPath) throws InvalidXpathException {
    if (this.xmlDocument == null) {
      throw new java.lang.NullPointerException("xml doc is null.");
    }
    int pos = fullPath.indexOf("@");
    if (pos == -1) {
      throw new InvalidXpathException(fullPath);
    }
    String attrName = fullPath.substring(pos + 1);
    String xpath = fullPath.substring(0, pos - 1);

    Node node = this.getNode(xpath);
    if (node != null) {
      String val = node.valueOf("@" + attrName);
      val = org.apache.commons.lang.StringUtils.stripToEmpty(val);
      return val;
    }
    return "";
  }

  /**
   * @param nodepath xml元素路径
   * @param attrName xml属性名
   * @param newValue xml属性新值
   */
  public void setXmlNodeAttribute(String nodepath, String attrName, String newValue) {
    if (this.xmlDocument == null) {
      throw new java.lang.NullPointerException("xml doc is null.");
    }
    if (attrName.startsWith("@")) {
      attrName = attrName.substring(1);
    }
    Element ele = (Element) this.getNode(nodepath);
    if (ele == null) {
      return;
    }
    if (ele != null) {
      ele.addAttribute(attrName, newValue);
    }
  }

  /**
   * 获取Dom串.
   * 
   * @return String
   */
  public String getXmlString() {
    if (this.xmlDocument == null) {
      throw new java.lang.NullPointerException("xml doc is null.");
    }
    String xml = this.xmlDocument.asXML();
    // 删除word特殊字符
    xml = XmlUtil.trimWordChar(xml);
    return xml;
  }

  /**
   * 作者节点列表.
   * 
   * @return List
   */
  @SuppressWarnings("rawtypes")
  public List getPubAuthorList() {
    List list = this.getNodes(ImportPubXmlConstants.AUTHOR_XPATH);
    return list;
  }

  /**
   * 作者名简称.
   * 
   * @return
   */
  public String getAuthorNamesAbbr() {
    return this.getXmlNodeAttribute(ImportPubXmlConstants.PUBLICATION_XPATH, "author_names_abbr");
  }

  /**
   * 获取journal_category_no分类号（国内cnki分类号）.
   * 
   * @return
   */
  public String getCategoryNo() {
    String categoryNo = this.getXmlNodeAttribute(ImportPubXmlConstants.PUBLICATION_XPATH, "journal_category_no");
    return categoryNo;
  }

  /**
   * 获取cs分类号（isi研究方向）.
   * 
   * @return
   */
  public String getScienceCategory() {
    String sc = this.getXmlNodeAttribute(ImportPubXmlConstants.PUBLICATION_XPATH, "sc");
    return sc;
  }

  /**
   * @param elePath xml元素路径 (e.g. /publication, /pub_members/pub_member[01], /pub_members, )
   * @return Element
   */
  public Element createElement(String elePath) {
    if (XmlUtil.isEmpty(elePath)) {
      throw new java.lang.NullPointerException("can't create Element with NULL (elePath).");
    }

    elePath = elePath.toLowerCase();
    String[] paths = XmlUtil.splitXPath(elePath); // 分割元素和元素的属性
    String[] segs = paths[0].split("/"); // will return ['/','publication']
    // or
    // ['/','pub_members','pub_member[1]']
    Element root = (Element) this.getRootNode();
    Element parentEle = (Element) this.getNode("/" + segs[1]);
    if (parentEle == null) {
      parentEle = root.addElement(segs[1]);
    }
    // 只支持2级节点
    if (segs.length > 2) {
      String name = segs[2];
      int pos = name.indexOf("[");
      Integer seqNo = null;
      if (pos > 0) {// 获取序号
        String tmp = name.substring(pos + 1, name.length() - 1);
        seqNo = Integer.parseInt(tmp);
      }
      // 删除序号
      name = name.replaceAll("\\[.*\\]", "");
      String selectedPath = segs[2];
      Element ele = null;

      if (seqNo != null) {
        // 页面提交的数据顺序是不确定的，因此如果前面序号的节点后到，则提前创建，防止页面获取XML数据乱序
        for (int i = 1; i <= seqNo; i++) {
          selectedPath = name + "[@seq_no=" + String.valueOf(i) + "]";
          ele = (Element) parentEle.selectSingleNode(selectedPath); // segs[2]如:pub_member[01]
          if (ele == null) {
            ele = parentEle.addElement(name);
            ele.addAttribute("seq_no", String.valueOf(i));
          }
        }
      } else {
        ele = (Element) parentEle.selectSingleNode(selectedPath);
        if (ele == null) {
          ele = parentEle.addElement(name);
        }
      }
      return ele;
    }
    return parentEle;
  }

  /**
   * @param to 目的元素
   * @param from 来源元素
   */
  @SuppressWarnings("unchecked")
  public void copyPubElement(Element to, Element from) {
    if (from == null || to == null) {
      return;
    }
    List list = from.attributes();
    for (int i = 0; i < list.size(); i++) {
      Attribute attr = (Attribute) list.get(i);
      to.addAttribute(attr.getName(), attr.getValue());
    }
  }
}
