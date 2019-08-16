package com.smate.sie.core.base.utils.xml;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.dom4j.Attribute;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.dom4j.Node;

import com.smate.core.base.utils.data.XmlUtil;
import com.smate.core.base.utils.exception.InvalidXpathException;

/**
 * @author yamingd 项目XML数据
 */
public class SiePrjXmlDocument {

  /**
   * 
   */
  private Document xmlDocument = null;

  /**
   * @throws DocumentException DocumentException
   */
  public SiePrjXmlDocument() throws DocumentException {
    try {
      String xml = SiePrjXmlConstants.XML_DECL;
      xml = xml + "<data ><prj_meta schema_version=\"3.0\" /></data>";
      xmlDocument = DocumentHelper.parseText(xml);
    } catch (DocumentException e) {
      throw e;
    }
  }

  /**
   * @param xmlData Xml字符串
   * @throws DocumentException DocumentException
   */
  public SiePrjXmlDocument(String xmlData) throws DocumentException {
    try {
      xmlDocument = DocumentHelper.parseText(xmlData);
    } catch (DocumentException e) {
      throw e;
    }
  }

  /**
   * 导入作者节点.
   * 
   * @return List
   */
  @SuppressWarnings("unchecked")
  public List getPubAuthorList() {
    List list = this.getNodes(SiePrjXmlConstants.PUB_AUTHOR_XPATH);
    return list;
  }

  /**
   * @param doc Document
   * @throws DocumentException DocumentException
   */
  public SiePrjXmlDocument(Document doc) throws DocumentException {
    xmlDocument = doc;
  }

  /**
   * 获取项目ID.
   * 
   * @return long
   */
  public long getPrjId() {

    String tmp = this.getXmlNodeAttribute(SiePrjXmlConstants.PRJ_META_XPATH, "prj_id");
    if ("".equals(tmp)) {
      throw new java.lang.NullPointerException("prj_meta/prj_id can't be null.");
    }
    return Long.parseLong(tmp);
  }

  /**
   * 获取版本号.
   * 
   * @return
   */
  public int getVersionNO() {
    String versionNO = this.getXmlNodeAttribute(SiePrjXmlConstants.PRJ_META_XPATH, "version_no");
    if ("".equals(versionNO)) {
      throw new java.lang.NullPointerException("prj_meta/@version_no can't be null.");
    }
    return Integer.parseInt(versionNO);
  }

  /**
   * lqh add 是否存在项目ID.
   * 
   * @return boolean
   */
  public boolean isExistPrjId() {

    String tmp = this.getXmlNodeAttribute(SiePrjXmlConstants.PRJ_META_XPATH, "prj_id");
    if (StringUtils.isBlank(tmp)) {
      return false;
    }
    return true;
  }

  /**
   * 获取人员ID.
   * 
   * @return long
   */
  public long getOwerPsnId() {
    String str = this.getXmlNodeAttribute(SiePrjXmlConstants.PRJ_META_XPATH, "record_psn_id");
    if (StringUtils.isBlank(str)) {
      throw new java.lang.NullPointerException("prj_meta/record_psn_id can't be null.");
    }
    return Long.parseLong(str);
  }

  /**
   * 获取人员列表节点.
   * 
   * @return List
   */
  @SuppressWarnings("unchecked")
  public List getPrjMembers() {
    List list = this.getNodes(SiePrjXmlConstants.PRJ_MEMBERS_MEMBER_XPATH);
    return list;
  }

  /**
   * 项目检查错误信息.
   * 
   * @return
   */
  @SuppressWarnings("unchecked")
  public List getPrjErrorFields() {

    List list = this.getNodes(SiePrjXmlConstants.PRJ_ERRORS_ERROR_XPATH);
    return list;
  }

  /**
   * 获取元数据节点.
   * 
   * @return Node
   */
  public Node getPrjMeta() {
    return this.getNode(SiePrjXmlConstants.PRJ_META_XPATH);
  }

  /**
   * 获取人员节点.
   * 
   * @return Node
   */
  public Node getPrjMember() {
    return this.getNode(SiePrjXmlConstants.PRJ_MEMBERS_XPATH);
  }

  /**
   * 获取模板.
   * 
   * @return String
   */
  public String getFormTemplate() {
    return this.getXmlNodeAttribute(SiePrjXmlConstants.PRJ_META_XPATH, "tmpl_form");
  }

  public void setFormTemplate(String tmplForm) {
    Element element = (Element) this.getNode(SiePrjXmlConstants.PRJ_META_XPATH);
    if (element == null) {
      element = this.createElement(SiePrjXmlConstants.PRJ_META_XPATH);
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
    return this.xmlDocument.selectSingleNode(SiePrjXmlConstants.ROOT_XPATH);
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
    if (!xpath.startsWith(SiePrjXmlConstants.ROOT_XPATH))
      xpath = SiePrjXmlConstants.ROOT_XPATH + xpath;
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
    if (!xpath.startsWith(SiePrjXmlConstants.ROOT_XPATH))
      xpath = SiePrjXmlConstants.ROOT_XPATH + xpath;
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
      val = StringUtils.stripToEmpty(val);
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
   * @param elePath xml元素路径 (e.g. /project, /prj_members/prj_member[01], /prj_members, )
   * @return Element
   */
  public Element createElement(String elePath) {
    if (XmlUtil.isEmpty(elePath)) {
      throw new java.lang.NullPointerException("can't create Element with NULL (elePath).");
    }

    elePath = elePath.toLowerCase();
    String[] paths = XmlUtil.splitXPath(elePath); // 分割元素和元素的属性
    String[] segs = paths[0].split("/"); // will return ['/','project']
    // or
    // ['/','prj_members','prj_member[1]']
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
          ele = (Element) parentEle.selectSingleNode(selectedPath); // segs[2]如:prj_member[01]
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
  public void copyPrjElement(Element from) {
    if (from == null) {
      return;
    }

    Element root = (Element) this.getRootNode();
    Node ele = root.selectSingleNode(from.getName());
    if (ele == null) {
      ele = root.addElement(from.getName());
    }
    copyPrjElement((Element) ele, from);
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
  public void copyPrjElement(Element to, Element from) {
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
   * 返回一些列字段的内容.
   * 
   * @param fields 字段xpath(如/project/@zh_title).
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
   * 获取导入的来源URL.
   * 
   * @return
   */
  public String getSourceUrl() {
    String url = this.getXmlNodeAttribute(SiePrjXmlConstants.PRJ_META_XPATH, "source_url");
    return url;
  }

  /**
   * 把旧xml中的属性值填充到新的xml，如果新XML存在，则不需要填充.
   * 
   * @param oldPub
   * @param attrs
   * @param attrs2
   */
  public void fillAttributeValue(SiePrjXmlDocument oldDoc, String oldXpath, String newXpath, String[] fromAttrs,
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
   * 获取expand节点.
   * 
   * @return Node
   */
  public Node getPrjExpand() {
    return this.getNode(SiePrjXmlConstants.PRJ_EXPAND_XPATH);
  }

}
