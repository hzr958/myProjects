package com.smate.sie.center.task.model;

import com.smate.core.base.utils.constant.PubXmlConstants;
import com.smate.core.base.utils.data.XmlUtil;
import com.smate.core.base.utils.exception.InvalidXpathException;
import com.smate.core.base.utils.string.IrisStringUtils;
import org.dom4j.*;

import java.util.List;

/**
 * @author ajb 成果XML数据
 */
public class PubXmlDocument {

  /**
   * 
   */
  private Document xmlDocument = null;

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
   * 获取成果全文链接或全文附件.
   * 
   * @return
   */
  /*
   * public com.smate.center.open.model.nsfc.PubFulltext getFulltext() {
   * 
   * String url = this.getXmlNodeAttribute(PubXmlConstants.PUB_FULLTEXT_XPATH, "fulltext_url"); Long
   * fileId = IrisNumberUtils.createLong(this .getXmlNodeAttribute(PubXmlConstants.PUB_FULLTEXT_XPATH,
   * "file_id")); Integer fileNodeId =
   * IrisNumberUtils.createInteger(this.getXmlNodeAttribute(PubXmlConstants.PUB_FULLTEXT_XPATH,
   * "node_id")); Long insId =
   * IrisNumberUtils.createLong(this.getXmlNodeAttribute(PubXmlConstants.PUB_FULLTEXT_XPATH,
   * "ins_id")); Integer dbid =
   * IrisNumberUtils.createInteger(this.getXmlNodeAttribute(PubXmlConstants.PUB_META_XPATH,
   * "source_db_id"));
   * 
   * String fileName = this.getXmlNodeAttribute(PubXmlConstants.PUB_FULLTEXT_XPATH, "file_name");
   * String fileExt = this.getXmlNodeAttribute(PubXmlConstants.PUB_FULLTEXT_XPATH, "file_ext"); if
   * (!"".equals(fileName)) { fileName = fileName + fileExt; }
   * 
   * com.smate.center.open.model.nsfc.PubFulltext ret = new
   * com.smate.center.open.model.nsfc.PubFulltext(url, fileId, fileName, fileExt, dbid, fileNodeId,
   * insId); return ret; }
   */

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
   * 获取根节点.
   * 
   * @return Node
   */
  public Node getRootNode() {
    return this.xmlDocument.selectSingleNode(PubXmlConstants.ROOT_XPATH);
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

}
