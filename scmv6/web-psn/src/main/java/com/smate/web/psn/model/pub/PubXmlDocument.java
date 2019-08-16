package com.smate.web.psn.model.pub;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.math.NumberUtils;
import org.dom4j.Attribute;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.dom4j.Node;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.smate.core.base.pub.enums.PublicationTypeEnum;
import com.smate.core.base.utils.constant.PubXmlConstants;
import com.smate.core.base.utils.data.XmlUtil;
import com.smate.core.base.utils.exception.InvalidXpathException;
import com.smate.core.base.utils.string.IrisNumberUtils;
import com.smate.core.base.utils.string.IrisStringUtils;

/**
 * @author hzr 成果XML数据
 */
public class PubXmlDocument {

  /**
   * 
   */
  private Document xmlDocument = null;
  protected Logger logger = LoggerFactory.getLogger(getClass());

  /**
   * @throws DocumentException DocumentException
   */
  public PubXmlDocument() throws DocumentException {
    try {
      String xml = PubXmlConstants.XML_DECL;
      xml = xml + "<data ><pub_meta schema_version=\"3.0\" /></data>";
      xmlDocument = DocumentHelper.parseText(xml);
    } catch (DocumentException e) {
      throw e;
    }
  }

  /**
   * @param xmlData Xml字符串
   * @throws DocumentException DocumentException
   */
  public PubXmlDocument(String xmlData) throws DocumentException {
    try {
      // 除去特殊字符
      xmlData = IrisStringUtils.filterXmlStr(xmlData);
      xmlDocument = DocumentHelper.parseText(xmlData);
    } catch (DocumentException e) {
      logger.error("生成xml文档报错", e);
      throw e;
    }
  }

  /**
   * @param doc Document
   * @throws DocumentException DocumentException
   */
  public PubXmlDocument(Document doc) throws DocumentException {
    xmlDocument = doc;
  }

  /**
   * 获取成果ID.
   * 
   * @return long
   */
  public long getPubId() {

    String tmp = this.getXmlNodeAttribute(PubXmlConstants.PUB_META_XPATH, "pub_id");
    if ("".equals(tmp)) {
      throw new java.lang.NullPointerException("pub_meta/pub_id can't be null.");
    }
    return Long.parseLong(tmp);
  }

  /**
   * 获取成果ID.无异常
   * 
   * @return long
   */
  public Long getNewPubId() {

    String tmp = this.getXmlNodeAttribute(PubXmlConstants.PUB_META_XPATH, "pub_id");
    if ("".equals(tmp)) {
      return null;
    }
    return Long.parseLong(tmp);
  }

  /**
   * 获取版本号.
   * 
   * @return
   */
  public int getVersionNO() {
    String versionNO = this.getXmlNodeAttribute(PubXmlConstants.PUB_META_XPATH, "version_no");
    if ("".equals(versionNO)) {
      throw new java.lang.NullPointerException("pub_meta/@version_no can't be null.");
    }
    return Integer.parseInt(versionNO);
  }

  /**
   * lqh add 是否存在成果ID.
   * 
   * @return boolean
   */
  public boolean isExistPubId() {

    String tmp = this.getXmlNodeAttribute(PubXmlConstants.PUB_META_XPATH, "pub_id");
    if ("".equals(tmp)) {
      return false;
    }
    return true;
  }

  /**
   * 获取成果类型ID.
   * 
   * @return int
   */
  public int getPubTypeId() {
    String str = this.getXmlNodeAttribute(PubXmlConstants.PUB_TYPE_XPATH, "id");
    if ("".equals(str)) {
      throw new java.lang.NullPointerException("pub_type/id can't be null.");
    }
    return Integer.parseInt(str);
  }

  /**
   * 获取主要类别信息，例如成果、文献.
   * 
   * @return
   */
  public int getArticleTypeId() {

    String str = this.getXmlNodeAttribute(PubXmlConstants.PUB_TYPE_XPATH, "article_type");
    if ("".equals(str)) {
      throw new java.lang.NullPointerException("pub_type/article_type can't be null.");
    }
    return Integer.parseInt(str);
  }

  /**
   * @param pubTypeId 成果类型ID
   */
  public void setPubTypeId(long pubTypeId) {
    Node node = this.getNode(PubXmlConstants.PUB_TYPE_XPATH);
    if (node == null) {
      node = (Node) this.createElement(PubXmlConstants.PUB_TYPE_XPATH);
    }
    if (node != null) {
      ((Element) node).addAttribute("id", String.valueOf(pubTypeId));
    }
  }

  /**
   * 获取人员ID.
   * 
   * @return long
   */
  public long getOwerPsnId() {
    String str = this.getXmlNodeAttribute(PubXmlConstants.PUB_META_XPATH, "record_psn_id");
    if (str == null || "".equals(str)) {
      throw new java.lang.NullPointerException("pub_meta/record_psn_id can't be null.");
    }
    return Long.parseLong(str);
  }

  /**
   * 获取单位ID.
   * 
   * @return long
   */
  public long getOwerInsId() {
    String str = this.getXmlNodeAttribute(PubXmlConstants.PUB_META_XPATH, "record_ins_id");
    if (str == null || "".equals(str)) {
      throw new java.lang.NullPointerException("pub_meta/record_ins_id can't be null.");
    }
    return Long.parseLong(str);
  }

  /**
   * DBID.
   * 
   * @return
   */
  public Long getSourceDbId() {

    String str = this.getXmlNodeAttribute(PubXmlConstants.PUB_META_XPATH, "source_db_id");
    return IrisNumberUtils.createLong(str);
  }

  /**
   * 获取期刊ID.
   * 
   * @return Long
   */
  public Long getPubJournalId() {
    String jid = this.getXmlNodeAttribute(PubXmlConstants.PUB_JOURNAL_XPATH, "jid");
    if ("".equals(jid))
      return null;
    return Long.parseLong(jid);
  }

  /**
   * 获取人员列表节点.
   * 
   * @return List
   */
  @SuppressWarnings("unchecked")
  public List getPubMembers() {
    List list = this.getNodes(PubXmlConstants.PUB_MEMBERS_MEMBER_XPATH);
    return list;
  }

  /**
   * 获取作者名列表.
   * 
   * @return
   */
  @SuppressWarnings("rawtypes")
  public List<String> getPubMemberNames() {
    List<String> mbNames = new ArrayList<String>();
    List mbs = this.getPubMembers();
    if (CollectionUtils.isNotEmpty(mbs)) {
      for (int i = 0; i < mbs.size(); i++) {
        Element e = (Element) mbs.get(i);
        String mbName = e.attributeValue("member_psn_name");
        if (StringUtils.isNotBlank(mbName)) {
          mbNames.add(mbName);
        }
      }
    }
    return mbNames;
  }

  /**
   * 成果检查错误信息.
   * 
   * @return
   */
  @SuppressWarnings("unchecked")
  public List getPubErrorFields() {

    List list = this.getNodes(PubXmlConstants.PUB_ERRORS_ERROR_XPATH);
    return list;
  }

  /**
   * 指派作者节点列表.
   * 
   * @return List
   */
  @SuppressWarnings("unchecked")
  public List getPubAuthorList() {
    List list = this.getNodes(PubXmlConstants.PUB_AUTHOR_XPATH);
    return list;
  }

  /**
   * 指派作者节点.
   * 
   * @return List
   */
  public Element getPubAuthors() {
    return (Element) this.getNode(PubXmlConstants.PUB_AUTHORS_XPATH);
  }

  /**
   * 获取指派作者名称列表.
   * 
   * @return List<String>
   */
  @SuppressWarnings("unchecked")
  public List<String> getPubAuthorNameList() {
    List nodes = this.getPubAuthorList();
    List<String> names = new ArrayList<String>();
    for (int i = 0; i < nodes.size(); i++) {
      Element ele = (Element) nodes.get(i);
      String name = org.apache.commons.lang.StringUtils.stripToEmpty(ele.attributeValue("au"));
      if (!"".equals(name))
        names.add(name);
    }
    return names;
  }

  /**
   * 获取元数据节点.
   * 
   * @return Node
   */
  public Node getPubMeta() {
    return this.getNode(PubXmlConstants.PUB_META_XPATH);
  }

  /**
   * 获取expand节点.
   * 
   * @return Node
   */
  public Node getPubExpand() {
    return this.getNode(PubXmlConstants.PUB_EXPAND_XPATH);
  }

  /**
   * 获取人员节点.
   * 
   * @return Node
   */
  public Node getPubMember() {
    return this.getNode(PubXmlConstants.PUB_MEMBERS_XPATH);
  }

  /**
   * 获取项目节点.
   * 
   * @return Node
   */
  public Node getProject() {
    return this.getNode(PubXmlConstants.PUB_PROJECT_XPATH);
  }

  /**
   * 获取奖励节点.
   * 
   * @return Node
   */
  public Node getAward() {
    return this.getNode(PubXmlConstants.PUB_AWARD_XPATH);
  }

  /**
   * 获取书籍节点.
   * 
   * @return Node
   */
  public Node getBook() {
    return this.getNode(PubXmlConstants.PUB_BOOK_XPATH);
  }

  /**
   * 获取会议论文节点.
   * 
   * @return Node
   */
  public Node getConfPaper() {
    return this.getNode(PubXmlConstants.PUB_CONF_PAPER_XPATH);
  }

  /**
   * 获取期刊节点.
   * 
   * @return Node
   */
  public Node getJournalArtilce() {
    return this.getNode(PubXmlConstants.PUB_JOURNAL_XPATH);
  }

  /**
   * 获取期刊编辑节点.
   * 
   * @return Node
   */
  public Node getJournalEditor() {
    return this.getNode(PubXmlConstants.PUB_JOURNAL_EDITOR_XPATH);
  }

  /**
   * 获取其他类型节点.
   * 
   * @return Node
   */
  public Node getOther() {
    return this.getNode(PubXmlConstants.PUB_OTHER_XPATH);
  }

  /**
   * 获取专利节点.
   * 
   * @return Node
   */
  public Node getPatent() {
    return this.getNode(PubXmlConstants.PUB_PATENT_XPATH);
  }

  /**
   * 获取收录情况.
   * 
   * @return Node
   */
  public Node getPubList() {
    return this.getNode(PubXmlConstants.PUB_LIST_XPATH);
  }

  /**
   * 获取工作文档节点.
   * 
   * @return Node
   */
  public Node getWorkPaper() {
    return this.getNode(PubXmlConstants.PUB_WORK_PAPER_XPATH);
  }

  /**
   * 获取模板.
   * 
   * @return String
   */
  public String getFormTemplate() {
    return this.getXmlNodeAttribute(PubXmlConstants.PUB_META_XPATH, "tmpl_form");
  }

  public void setFormTemplate(String tmplForm) {
    Element element = (Element) this.getNode(PubXmlConstants.PUB_META_XPATH);
    if (element == null) {
      element = this.createElement(PubXmlConstants.PUB_META_XPATH);
    }
    element.addAttribute("tmpl_form", tmplForm);
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
   * 获取根节点.
   * 
   * @return Node
   */
  public Node getRootNode() {
    return this.xmlDocument.selectSingleNode(PubXmlConstants.ROOT_XPATH);
  }

  /**
   * @param xpath Xml元素路径
   * @return List
   */
  @SuppressWarnings("unchecked")
  public List getNodes(String xpath) {
    if (this.xmlDocument == null) {
      throw new java.lang.NullPointerException("xml doc is null.");
    }
    if (!xpath.startsWith(PubXmlConstants.ROOT_XPATH))
      xpath = PubXmlConstants.ROOT_XPATH + xpath;
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
    if (!xpath.startsWith(PubXmlConstants.ROOT_XPATH))
      xpath = PubXmlConstants.ROOT_XPATH + xpath;
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
      ele = this.createElement(nodepath);
    }
    if (ele != null) {
      if (!"cite_times".equals(attrName))
        ele.addAttribute(attrName, newValue);
    }
  }

  /**
   * 设置属性，防止旧的XML文档覆盖 新生成的XML文档中的cite_times属性值.
   * 
   * @param nodepath xml元素路径
   * @param attrName xml属性名
   * @param newValue xml属性新值
   */
  public void setNodeAttribute(String nodepath, String attrName, String newValue) {
    if (this.xmlDocument == null) {
      throw new java.lang.NullPointerException("xml doc is null.");
    }
    if (attrName.startsWith("@")) {
      attrName = attrName.substring(1);
    }
    Element ele = (Element) this.getNode(nodepath);
    if (ele == null) {
      ele = this.createElement(nodepath);
    }
    if (ele != null) {
      ele.addAttribute(attrName, newValue);
    }
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
   * @return Element
   */
  public Element addPubJournalElement() {
    Element ele = (Element) this.getNode(PubXmlConstants.PUB_JOURNAL_XPATH);
    if (ele != null) {
      return ele;
    }
    return this.createElement(PubXmlConstants.PUB_JOURNAL_XPATH);
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
   * @param from Element
   */
  public void copyPubElement(Element from) {
    if (from == null) {
      return;
    }

    Element root = (Element) this.getRootNode();
    Node ele = root.selectSingleNode(from.getName());
    if (ele == null) {
      ele = root.addElement(from.getName());
    }
    copyPubElement((Element) ele, from);
  }

  public void removeAttribute(String xpath, String... attrs) {
    Node node = this.getNode(xpath);
    if (node != null) {
      Element ele = (Element) node;
      for (String attr : attrs) {
        Attribute attribute = ele.attribute(attr);
        if (attribute != null) {
          ele.remove(attribute);
        }
      }
    }
  }

  /**
   * 在同一节点内，拷贝节点的fromAttr属性值到toAttr属性值.
   * 
   * @param xpath xml元素路径
   * @param fromAttr 来源属性名
   * @param toAttr 目的属性名
   */
  public void copyAttributeValue(String xpath, String fromAttr, String toAttr) {
    Node element = this.getNode(xpath);
    if (element != null) {
      String val = this.getXmlNodeAttributeValue(element, fromAttr);
      ((Element) (element)).addAttribute(toAttr, val);
    }
  }

  /**
   * 在不同节点内，拷贝节点的fromAttr属性值到toAttr属性值.
   * 
   * @param fromXpath 来源xml元素路径
   * @param fromAttr 来源xml元素属性
   * @param toXpath 目的xml元素路径
   * @param toAttr 目的xml元素属性
   */
  public void copyAttributeValue(String fromXpath, String fromAttr, String toXpath, String toAttr) {
    Node fromNode = this.getNode(fromXpath);
    Node toNode = this.getNode(toXpath);
    if (fromNode == null || toNode == null) {
      return;
    }

    String val = this.getXmlNodeAttributeValue(fromNode, fromAttr);
    ((Element) (toNode)).addAttribute(toAttr, val);

  }

  /**
   * @param fromXpath 来源xml元素路径
   * @param toXpath 目的xml元素路径
   * @param attrs 目的xml元素属性
   */
  public void copyAttributeValue(String fromXpath, String toXpath, String[] attrs) {
    Node fromNode = this.getNode(fromXpath);
    Node toNode = this.getNode(toXpath);
    if (fromNode == null || toNode == null) {
      return;
    }

    for (String attr : attrs) {
      String val = this.getXmlNodeAttributeValue(fromNode, attr);
      ((Element) (toNode)).addAttribute(attr, val);
    }
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

  /**
   * @param val 文本
   * @return String
   */
  public String escapeGTLT(String val) {
    if (val == null) {
      return "";
    }

    val = val.replace(">", "&gt").replace("<", "&lt;");

    return val;
  }

  /**
   * 是否奖励类型.
   * 
   * @return boolean
   */
  public boolean isAward() {
    if (PublicationTypeEnum.AWARD == this.getPubTypeId()) {
      return true;
    }

    return false;
  }

  /**
   * 是否书籍类型.
   * 
   * @return boolean
   */
  public boolean isBook() {
    if (PublicationTypeEnum.BOOK == this.getPubTypeId()) {
      return true;
    }

    return false;
  }

  /**
   * 是否是会议论文.
   * 
   * @return boolean
   */
  public boolean isConfPaper() {
    if (PublicationTypeEnum.CONFERENCE_PAPER == this.getPubTypeId()) {
      return true;
    }

    return false;
  }

  /**
   * 是否期刊文章.
   * 
   * @return boolean
   */
  public boolean isJournalArticle() {
    if (PublicationTypeEnum.JOURNAL_ARTICLE == this.getPubTypeId()) {
      return true;
    }

    return false;
  }

  /**
   * 是否专利类别.
   * 
   * @return boolean
   */
  public boolean isPatent() {
    if (PublicationTypeEnum.PATENT == this.getPubTypeId()) {
      return true;
    }

    return false;
  }

  /**
   * 是否项目类型.
   * 
   * @return boolean
   */
  public boolean isProject() {
    if (6 == this.getPubTypeId()) {
      return true;
    }

    return false;
  }

  /**
   * 是否其他类型.
   * 
   * @return boolean
   */
  public boolean isOther() {
    if (PublicationTypeEnum.OTHERS == this.getPubTypeId()) {
      return true;
    }

    return false;
  }

  /**
   * 是否学位论文.
   * 
   * @return boolean
   */
  public boolean isThesis() {
    if (PublicationTypeEnum.THESIS == this.getPubTypeId()) {
      return true;
    }

    return false;
  }

  /**
   * 是否工作文档.
   * 
   * @return boolean
   */
  public boolean isWorkingPaper() {
    if (9 == this.getPubTypeId()) {
      return true;
    }

    return false;
  }

  /**
   * 是否工作文档.
   * 
   * @return boolean
   */
  public boolean isWorkingPaper(int pubType) {
    if (9 == pubType) {
      return true;
    }

    return false;
  }

  /**
   * 是否是书籍章节类型.
   * 
   * @return boolean
   */
  public boolean isBookChapter() {
    if (PublicationTypeEnum.BOOK_CHAPTER == this.getPubTypeId()) {
      return true;
    }

    return false;
  }

  /**
   * 是否是期刊编辑.
   * 
   * @return boolean
   */
  public boolean isJournalEditor() {
    if (PublicationTypeEnum.JOURNAL_EDITOR == this.getPubTypeId()) {
      return true;
    }

    return false;
  }

  /**
   * 返回一些列字段的内容.
   * 
   * @param fields 字段xpath(如/publication/@zh_title).
   * @return Map<String,String>
   * @throws InvalidXpathException InvalidXpathException
   */
  public Map<String, String> getFieldsData(List<String> fields) throws InvalidXpathException {
    Map<String, String> data = new HashMap<String, String>();
    for (int index = 0; index < fields.size(); index++) {
      String xpath = fields.get(index);
      String value = this.getXmlNodeAttribute(xpath);
      data.put(xpath, value);
    }
    return data;
  }

  /**
   * 获取更新引用次数的URL.
   * 
   * @return
   */
  public String getCitationUrl() {
    String url = this.getXmlNodeAttribute(PubXmlConstants.PUBLICATION_XPATH, "cited_url");
    return url;
  }

  /**
   * 获取导入的来源URL.
   * 
   * @return
   */
  public String getSourceUrl() {
    String url = this.getXmlNodeAttribute(PubXmlConstants.PUB_META_XPATH, "source_url");
    return url;
  }

  /**
   * 获取成果全文链接或全文附件.
   * 
   * @return
   */

  public PubFulltextExtend getFulltext() {

    String url = this.getXmlNodeAttribute(PubXmlConstants.PUB_FULLTEXT_XPATH, "fulltext_url");
    Long fileId = IrisNumberUtils.createLong(this.getXmlNodeAttribute(PubXmlConstants.PUB_FULLTEXT_XPATH, "file_id"));
    Integer fileNodeId =
        IrisNumberUtils.createInteger(this.getXmlNodeAttribute(PubXmlConstants.PUB_FULLTEXT_XPATH, "node_id"));
    Long insId = IrisNumberUtils.createLong(this.getXmlNodeAttribute(PubXmlConstants.PUB_FULLTEXT_XPATH, "ins_id"));
    Integer dbid =
        IrisNumberUtils.createInteger(this.getXmlNodeAttribute(PubXmlConstants.PUB_META_XPATH, "source_db_id"));

    String fileName = this.getXmlNodeAttribute(PubXmlConstants.PUB_FULLTEXT_XPATH, "file_name");
    String fileExt = this.getXmlNodeAttribute(PubXmlConstants.PUB_FULLTEXT_XPATH, "file_ext");
    if (!"".equals(fileName)) {
      fileName = fileName + fileExt;
    }

    PubFulltextExtend ret = new PubFulltextExtend(url, fileId, fileName, fileExt, dbid, fileNodeId, insId);
    return ret;
  }

  /**
   * 把旧xml中的属性值copy到新的xml.
   * 
   * @param oldPub
   * @param attrs
   * @param attrs2
   */
  public void copyAttributeValue(PubXmlDocument oldDoc, String oldXpath, String newXpath, String[] fromAttrs,
      String[] toAttrs) {
    if (fromAttrs.length < 1 || toAttrs.length < fromAttrs.length) {
      return;
    }
    for (int i = 0; i < fromAttrs.length; i++) {
      String value = oldDoc.getXmlNodeAttribute(oldXpath, fromAttrs[i]);
      this.setXmlNodeAttribute(newXpath, toAttrs[i], value);
    }
  }

  /**
   * 把旧xml中的属性值填充到新的xml，如果新XML存在，则不需要填充.
   * 
   * @param oldPub
   * @param attrs
   * @param attrs2
   */
  public void fillAttributeValue(PubXmlDocument oldDoc, String oldXpath, String newXpath, String[] fromAttrs,
      String[] toAttrs) {
    if (fromAttrs.length < 1 || toAttrs.length < fromAttrs.length) {
      return;
    }
    for (int i = 0; i < fromAttrs.length; i++) {
      String value1 = this.getXmlNodeAttribute(newXpath, toAttrs[i]);
      if (StringUtils.isBlank(value1)) {
        String value = oldDoc.getXmlNodeAttribute(oldXpath, fromAttrs[i]);
        this.setXmlNodeAttribute(newXpath, toAttrs[i], value);
      }
    }
  }

  /**
   * 将map中的键值对合并到节点中.
   * 
   * @param ele
   * @param map
   */
  public void fillAttribute(Element ele, Map<String, String> map) {

    Iterator<String> iter = map.keySet().iterator();
    while (iter.hasNext()) {
      String attrName = iter.next();
      ele.addAttribute(attrName, StringUtils.trimToEmpty(map.get(attrName)));
    }
  }

  /**
   * @param args
   * @throws Exception
   */
  public static void main(String[] args) throws Exception {

  }

  public Node getPublication() {
    return this.getNode(PubXmlConstants.PUBLICATION_XPATH);
  }

  public void mergeList(Element newEle, Element ele) {

    for (String listName : PubXmlConstants.PUB_LIST_SOURCE_ATTRS) {
      String listValue = ele.attributeValue(listName);
      String newListValue = newEle.attributeValue(listName);
      if ((StringUtils.isBlank(newListValue) || "0".equals(newListValue))
          && (StringUtils.isNotBlank(listValue) && "1".equals(listValue))) {
        newEle.addAttribute(listName, "1");
      }
    }
  }

  /**
   * 合并属性.
   * 
   * @param newEle
   * @param ele
   * @param attrs
   */
  public void mergeAttr(Element newEle, Element ele, String[] attrs) {
    if (newEle == null || ele == null || attrs == null) {
      return;
    }
    for (String attr : attrs) {
      String newValue = StringUtils.trimToNull(newEle.attributeValue(attr));
      if (newValue == null) {
        String oldValue = StringUtils.trimToNull(ele.attributeValue(attr));
        if (oldValue != null) {
          newEle.addAttribute(attr, oldValue);
        }
      }
    }
  }

  @SuppressWarnings("unchecked")
  public void cleanPubMembersPmId() {
    List<Element> members = this.getPubMembers();
    if (CollectionUtils.isNotEmpty(members)) {
      for (Element el : members) {
        el.addAttribute("pm_id", "");
      }
    }
  }

  /**
   * 获取导入成果年份.
   * 
   * @return
   */
  public Integer getImportPubYear() {

    String pubyear = this.getXmlNodeAttribute(PubXmlConstants.PUBLICATION_XPATH, "pubyear");
    if (StringUtils.isBlank(pubyear)) {
      pubyear = this.getXmlNodeAttribute(PubXmlConstants.PUBLICATION_XPATH, "start_date");
    }
    if (StringUtils.isBlank(pubyear)) {
      return null;
    }
    pubyear = pubyear.replaceAll("\\s+", "");
    String[] temp = pubyear.split("[-/]");
    String year = "0".equals(temp[0]) || StringUtils.isBlank(temp[0]) ? "" : temp[0];
    year = XmlUtil.changeSBCChar(year);
    if (NumberUtils.isDigits(year)) {
      return Integer.valueOf(year);
    }
    return null;
  }

  public void fillPubListAttribute(Element ele, Map<String, String> pubList) {
    Iterator<String> iter = pubList.keySet().iterator();
    while (iter.hasNext()) {
      String attrName = iter.next();
      String list = StringUtils.trimToEmpty(pubList.get(attrName));
      if ("0".equals(list) || StringUtils.isBlank(list)) {
        ele.addAttribute(attrName, "0");
      } else {
        ele.addAttribute(attrName, "1");
      }
    }
  }

  /**
   * 成果导入时，根据不同的库，完善收录情况.
   */
  public void fillPubListForImport() {

    if (this.xmlDocument == null) {
      throw new java.lang.NullPointerException("xmlDocument can't be null.");
    }
    PubXmlDocument.fillPubListForImport(this);
  }

  /**
   * 成果导入时，根据不同的库，完善收录情况.
   * 
   * @param xmlDocument
   */
  public static void fillPubListForImport(PubXmlDocument xmlDocument) {
    if (xmlDocument == null) {
      throw new java.lang.NullPointerException("xmlDocument can't be null.");
    }

    if (!xmlDocument.existsNode(PubXmlConstants.PUB_LIST_XPATH)) {
      xmlDocument.createElement(PubXmlConstants.PUB_LIST_XPATH);
      String[] attrs = new String[] {"list_ei", "list_sci", "list_ssci", "list_istp", "list_ei_source",
          "list_sci_source", "list_ssci_source", "list_istp_source"};
      for (String attr : attrs) {
        xmlDocument.copyAttributeValue(PubXmlConstants.PUBLICATION_XPATH, attr, PubXmlConstants.PUB_LIST_XPATH, attr);
      }
    }

    String source = xmlDocument.getXmlNodeAttribute(PubXmlConstants.PUBLICATION_XPATH, "source");
    if (StringUtils.isNotBlank(source) && "EI".equalsIgnoreCase(source)) {
      xmlDocument.setXmlNodeAttribute(PubXmlConstants.PUB_LIST_XPATH, "list_ei", "1");
      xmlDocument.setXmlNodeAttribute(PubXmlConstants.PUB_LIST_XPATH, "list_ei_source", "1");
    }
    // 完善isi收录情况.
    String citationIndex = xmlDocument.getXmlNodeAttribute(PubXmlConstants.PUBLICATION_XPATH, "citation_index");
    fillPubListByCitationIndex(xmlDocument, citationIndex);

  }

  /**
   * 成果导入时，合并成果，将新成果的收录情况加入到原始成果中.
   * 
   * @param xmlDocument
   * @param ignoreDocument
   */
  public static void fillPubListForImportMerge(PubXmlDocument xmlDocument, PubXmlDocument ignoreDocument) {
    if (xmlDocument == null || ignoreDocument == null) {
      throw new java.lang.NullPointerException("xmlDocument can't be null.");
    }

    // EI
    String source = ignoreDocument.getXmlNodeAttribute(PubXmlConstants.PUBLICATION_XPATH, "source");
    if (StringUtils.isNotBlank(source) && "EI".equalsIgnoreCase(source)) {
      xmlDocument.setXmlNodeAttribute(PubXmlConstants.PUB_LIST_XPATH, "list_ei", "1");
      xmlDocument.setXmlNodeAttribute(PubXmlConstants.PUB_LIST_XPATH, "list_ei_source", "1");
    }
    // 完善isi收录情况.
    String citationIndex = ignoreDocument.getXmlNodeAttribute(PubXmlConstants.PUBLICATION_XPATH, "citation_index");
    fillPubListByCitationIndex(xmlDocument, citationIndex);

  }

  /**
   * 完善isi收录情况.
   * 
   * @param citationIndex
   */
  public void fillPubListByCitationIndex(String citationIndex) {

    if (this.xmlDocument == null) {
      throw new java.lang.NullPointerException("xmlDocument can't be null.");
    }
    PubXmlDocument.fillPubListByCitationIndex(this, citationIndex);
  }

  /**
   * 完善收录情况.
   * 
   * @param xmlDocument
   * @param citationIndex
   */
  public static void fillPubListByCitationIndex(PubXmlDocument xmlDocument, String citationIndex) {
    if (StringUtils.isNotBlank(citationIndex)) {
      String[] citationIndexs = citationIndex.split(",|;");
      Long sourceDbId = xmlDocument.getSourceDbId();
      for (String tmpCitationIndex : citationIndexs) {
        if ("SCI".equalsIgnoreCase(tmpCitationIndex)) {
          xmlDocument.setXmlNodeAttribute(PubXmlConstants.PUB_LIST_XPATH, "list_sci", "1");
          if (PubXmlDbUtils.isIsiDb(sourceDbId)) {
            xmlDocument.setXmlNodeAttribute(PubXmlConstants.PUB_LIST_XPATH, "list_sci_source", "1");
          }
        }
        if ("ISTP".equalsIgnoreCase(tmpCitationIndex)) {
          xmlDocument.setXmlNodeAttribute(PubXmlConstants.PUB_LIST_XPATH, "list_istp", "1");
          if (PubXmlDbUtils.isIsiDb(sourceDbId)) {
            xmlDocument.setXmlNodeAttribute(PubXmlConstants.PUB_LIST_XPATH, "list_istp_source", "1");
          }
        }
        if ("SSCI".equalsIgnoreCase(tmpCitationIndex)) {
          xmlDocument.setXmlNodeAttribute(PubXmlConstants.PUB_LIST_XPATH, "list_ssci", "1");
          if (PubXmlDbUtils.isIsiDb(sourceDbId)) {
            xmlDocument.setXmlNodeAttribute(PubXmlConstants.PUB_LIST_XPATH, "list_ssci_source", "1");
          }
        }
        if ("EI".equalsIgnoreCase(tmpCitationIndex)) {
          xmlDocument.setXmlNodeAttribute(PubXmlConstants.PUB_LIST_XPATH, "list_ei", "1");
          if (PubXmlDbUtils.isEiDb(sourceDbId)) {
            xmlDocument.setXmlNodeAttribute(PubXmlConstants.PUB_LIST_XPATH, "list_ei_source", "1");
          }
        }
      }
    }
  }

  /**
   * 完善期刊数据.
   * 
   * @param journal
   * @param nodeId
   */
  public void fillPubJournalByJournal(Journal journal, Integer nodeId) {
    Element ele = (Element) this.getJournalArtilce();
    if (ele == null) {
      ele = this.createElement(PubXmlConstants.PUB_JOURNAL_XPATH);
    }
    // 期刊统一放在1节点
    ele.addAttribute("node_id", nodeId.toString());
    ele.addAttribute("jid", String.valueOf(journal.getId()));
    ele.addAttribute("jname", StringUtils.isNotBlank(journal.getZhName()) ? journal.getZhName() : journal.getEnName());
    ele.addAttribute("zh_name", journal.getZhName());
    ele.addAttribute("en_name", journal.getEnName());
    ele.addAttribute("jissn", journal.getIssn());
  }

  /**
   * 解析各个文献库的source_id.
   * 
   * @param doc
   */
  public static void parseDbSourceId(PubXmlDocument doc) {
    Long sourceDbId = doc.getSourceDbId();
    String sourceId = doc.getXmlNodeAttribute(PubXmlConstants.PUBLICATION_XPATH, "source_id");
    if (StringUtils.isBlank(sourceId)) {
      return;
    }
    doc.setNodeAttribute(PubXmlConstants.PUB_META_XPATH, "source_id", sourceId);
    if (PubXmlDbUtils.isScopusDb(sourceDbId)) {
      doc.setNodeAttribute(PubXmlConstants.PUB_META_XPATH, "sps_id", sourceId);
    } else if (PubXmlDbUtils.isEiDb(sourceDbId)) {
      doc.setNodeAttribute(PubXmlConstants.PUB_META_XPATH, "ei_id", sourceId);
    } else if (PubXmlDbUtils.isIsiDb(sourceDbId)) {
      doc.setNodeAttribute(PubXmlConstants.PUB_META_XPATH, "isi_id", sourceId);
    }
  }

  /**
   * 解析各个文献库的source_db_url.
   * 
   * @param doc
   */
  public static void parseDbSourceUrl(PubXmlDocument doc) {

    Long sourceDbId = doc.getSourceDbId();
    String sourceUrl = doc.getXmlNodeAttribute(PubXmlConstants.PUBLICATION_XPATH, "source_url");
    if (StringUtils.isBlank(sourceUrl)) {
      sourceUrl = doc.getXmlNodeAttribute(PubXmlConstants.PUB_META_XPATH, "source_url");
    }
    if (StringUtils.isBlank(sourceUrl) || sourceDbId == null || sourceDbId == -1) {
      return;
    }
    if (PubXmlDbUtils.isScopusDb(sourceDbId)) {

      doc.setNodeAttribute(PubXmlConstants.PUB_META_XPATH, "sps_source_url", sourceUrl);
    } else if (PubXmlDbUtils.isEiDb(sourceDbId)) {

      doc.setNodeAttribute(PubXmlConstants.PUB_META_XPATH, "ei_source_url", sourceUrl);
    } else if (PubXmlDbUtils.isIsiDb(sourceDbId)) {

      doc.setNodeAttribute(PubXmlConstants.PUB_META_XPATH, "isi_source_url", sourceUrl);
    } else if (PubXmlDbUtils.isCNIPRDb(sourceDbId)) {

      doc.setNodeAttribute(PubXmlConstants.PUB_META_XPATH, "cnipr_source_url", sourceUrl);
    } else if (PubXmlDbUtils.isWanFangDb(sourceDbId)) {

      doc.setNodeAttribute(PubXmlConstants.PUB_META_XPATH, "wf_source_url", sourceUrl);
    } else if (PubXmlDbUtils.isCnkiDb(sourceDbId)) {

      doc.setNodeAttribute(PubXmlConstants.PUB_META_XPATH, "cnki_source_url", sourceUrl);
    }
  }

  public Document getXmlDocument() {
    return xmlDocument;
  }

  /**
   * 获取zhTitle.
   * 
   * @return
   */
  public String getZhTitle() {
    String str = this.getXmlNodeAttribute(PubXmlConstants.PUBLICATION_XPATH, "zh_title");
    return str;
  }

  /**
   * 获取enTitle.
   * 
   * @return
   */
  public String getEnTitle() {
    String str = this.getXmlNodeAttribute(PubXmlConstants.PUBLICATION_XPATH, "en_title");
    return str;
  }

  /**
   * 获取publish_year.
   * 
   * @return
   */
  public String getPublishYear() {
    String str = this.getXmlNodeAttribute(PubXmlConstants.PUBLICATION_XPATH, "publish_year");
    return str;
  }

  /**
   * 获取publish_month.
   * 
   * @return
   */
  public String getPublishMonth() {
    String str = this.getXmlNodeAttribute(PubXmlConstants.PUBLICATION_XPATH, "publish_month");
    return str;
  }

  /**
   * 获取publish_day.
   * 
   * @return
   */
  public String getPublishDay() {
    String str = this.getXmlNodeAttribute(PubXmlConstants.PUBLICATION_XPATH, "publish_day");
    return str;
  }

  /**
   * 获取cite_times.
   * 
   * @return
   */
  public Integer getCiteTimes() {
    String str = this.getXmlNodeAttribute(PubXmlConstants.PUBLICATION_XPATH, "cite_times");
    return IrisNumberUtils.createInteger(str);
  }

  /**
   * 获取fulltext_url.
   * 
   * @return
   */
  public String getFullTextUrl() {
    String str = this.getXmlNodeAttribute(PubXmlConstants.PUB_FULLTEXT_XPATH, "fulltext_url");
    return str;
  }

  /**
   * 获取 publication fulltext_url.
   * 
   * @return
   */
  public String getFullTextUrlByPublication() {
    String str = this.getXmlNodeAttribute(PubXmlConstants.PUBLICATION_XPATH, "fulltext_url");
    return str;
  }

  public String getFullTextId() {
    String fileId = this.getXmlNodeAttribute(PubXmlConstants.PUB_FULLTEXT_XPATH, "file_id");
    return fileId;
  }

  /**
   * 获取node_id.
   * 
   * @return
   */
  public Integer getFullTextNodeId() {
    String str = this.getXmlNodeAttribute(PubXmlConstants.PUB_FULLTEXT_XPATH, "node_id");
    return IrisNumberUtils.createInteger(str);
  }

  /**
   * 获取file_ext.
   * 
   * @return
   */
  public String getFullTextFileExt() {
    String str = this.getXmlNodeAttribute(PubXmlConstants.PUB_FULLTEXT_XPATH, "file_ext");
    return str;
  }

  /**
   * 获取影响因子.
   * 
   * @return
   */
  public String getImpactFactors() {
    String str = this.getXmlNodeAttribute(PubXmlConstants.PUBLICATION_XPATH, "impact_factors");
    return str;
  }

  /**
   * 获取expand-PsnId.
   * 
   * @return
   */
  public Long getExpandPsnId() {
    String str = this.getXmlNodeAttribute(PubXmlConstants.PUB_EXPAND_XPATH, "psnId");
    return IrisNumberUtils.createLong(str);
  }

  /**
   * 获取成果全文结点
   * 
   * @return
   */
  public Node getPubFullTextNode() {
    return this.getNode(PubXmlConstants.PUB_FULLTEXT_XPATH);
  }

  public Node getPubAttachmentsNode() {
    return this.getNode(PubXmlConstants.PUB_ATTACHMENTS_XPATH);
  }

}
