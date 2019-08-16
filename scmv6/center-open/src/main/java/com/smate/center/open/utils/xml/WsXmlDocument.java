package com.smate.center.open.utils.xml;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections4.MapUtils;
import org.apache.commons.lang.ObjectUtils;
import org.dom4j.Attribute;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.dom4j.Node;

import com.smate.core.base.utils.exception.InvalidXpathException;


/**
 * scm对外webservice接口document. <data> <interface> <method> <arg0></arg0> <arg1></arg1> ......
 * </method> </interface> </data>
 * 
 * @author pwl
 */
public class WsXmlDocument {

  public static final String XML_DECL = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>";

  public static final String WS_RESULT_PATH = "/result";

  public static final String WS_DATA_PATH = "/data";

  public static final String WS_INTERFACE_PATH = "/interface";

  public static final String WS_INTERFACE_METHOD_PATH = "/interface/method";

  public static final String WS_ERROR_MSG_NODE = "/error_msg";

  /**
   * 
   */
  private Document xmlDocument = null;

  /**
   * 1、构造返回类型doc；2、构造参数doc.
   * 
   * @param type
   * @throws DocumentException
   */
  public WsXmlDocument(int type) throws DocumentException {

    String xml = WsXmlDocument.XML_DECL;
    if (type == 2) {
      xml = xml + "<data><interface><method></method></interface></data>";
    } else {
      xml = xml + "<result></result>";
    }

    xmlDocument = DocumentHelper.parseText(xml);
  }

  public WsXmlDocument(String xmlData) throws DocumentException {

    xmlDocument = DocumentHelper.parseText(xmlData);
  }

  public WsXmlDocument(Document doc) throws DocumentException {

    xmlDocument = doc;
  }

  /**
   * 获取接口节点.
   * 
   * @return Node
   */
  public Node getWsInterface() {

    return this.getNode(WsXmlDocument.WS_INTERFACE_PATH);
  }

  /**
   * 获取接口方法节点.
   * 
   * @return Node
   */
  public Node getWsInterfaceMethod() {

    return this.getNode(WsXmlDocument.WS_INTERFACE_METHOD_PATH);
  }

  @SuppressWarnings("unchecked")
  public List<Element> getMethodElementChildren() {

    Element methodElement = (Element) this.getWsInterfaceMethod();
    if (methodElement == null) {
      throw new java.lang.NullPointerException("/data/interface/method is null.");
    }
    return methodElement.elements();
  }

  @SuppressWarnings("unchecked")
  public List<String> getMethodParams() throws Exception {

    List<String> valList = new ArrayList<String>();
    Element methodElement = (Element) this.getWsInterfaceMethod();
    List<Element> list = methodElement.elements();
    if (CollectionUtils.isNotEmpty(list)) {
      for (Element element : list) {
        valList.add(element.getStringValue());
      }
    }
    return valList;
  }

  /**
   * 获取根节点.
   * 
   * @return Node
   */
  public Node getRootNode(String rootPath) {

    return this.xmlDocument.selectSingleNode(rootPath);
  }

  /**
   * @param xpath Xml元素路径
   * @return List
   */
  @SuppressWarnings("rawtypes")
  public List getNodes(String rootPath, String xpath) {
    if (this.xmlDocument == null) {
      throw new java.lang.NullPointerException("xml doc is null.");
    }
    if (!xpath.startsWith(rootPath)) {
      xpath = rootPath + xpath;
    }
    return this.xmlDocument.selectNodes(xpath);
  }

  /**
   * @param fullPath xml元素路径
   * @return String
   * @throws InvalidXpathException InvalidXpathException
   */
  public String getXmlNodeAttribute(String fullPath) throws Exception {
    if (this.xmlDocument == null) {
      throw new java.lang.NullPointerException("xml doc is null.");
    }
    int pos = fullPath.indexOf("@");
    if (pos == -1) {
      throw new Exception(fullPath);
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
   * @param xpath xml元素路径
   * @return Node
   */
  public Node getNode(String xpath) {
    if (this.xmlDocument == null) {
      throw new java.lang.NullPointerException("xml doc is null.");
    }
    if (!xpath.startsWith(WsXmlDocument.WS_DATA_PATH)) {
      xpath = WsXmlDocument.WS_DATA_PATH + xpath;
    }
    return this.xmlDocument.selectSingleNode(xpath);
  }

  public void createMethodParamNode(Map<String, Object> map) {

    if (MapUtils.isNotEmpty(map)) {
      Element methodElement = (Element) this.getWsInterfaceMethod();
      for (Map.Entry<String, Object> entry : map.entrySet()) {
        methodElement.addElement(entry.getKey()).setText(ObjectUtils.toString(entry.getValue(), ""));
      }
    }
  }

  /**
   * 
   * @param parentNode
   * @param map
   */
  public void createNode(Node parentNode, Map<String, String> map) {

    if (parentNode != null && MapUtils.isNotEmpty(map)) {
      Element parentElement = (Element) parentNode;
      String value = "";
      for (Map.Entry<String, String> entry : map.entrySet()) {
        value = entry.getValue() == null ? "" : entry.getValue();
        this.createElement(parentElement, entry.getKey()).setText(value);
      }
    }
  }

  /**
   * 创建返回节点.
   * 
   * @param resultType 1、成功； 2、缺少必要参数；3、未关联人员；4、远程调用同步出错
   * @param errorMsg
   * @return
   */
  public String createResultData(int resultType, Map<String, String> map) {

    Node node = this.xmlDocument.selectSingleNode(WsXmlDocument.WS_RESULT_PATH);
    Element element = (Element) node;
    element.addAttribute("value", resultType + "");
    if (resultType == 1) {
      element = element.addElement("data");
    }
    this.createNode(element, map);

    return this.xmlDocument.asXML();
  }

  public String createResultDataByPage(int totalCount, Map<String, String> map) {
    Node node = this.xmlDocument.selectSingleNode(WsXmlDocument.WS_RESULT_PATH);
    Element element = (Element) node;
    element.addAttribute("total_count", totalCount + "");
    this.createNode(element, map);
    return this.xmlDocument.asXML();
  }

  public String getMethodParamVal(String xpath) {

    Node node = this.getNode(WsXmlDocument.WS_DATA_PATH + WsXmlDocument.WS_INTERFACE_METHOD_PATH + xpath);
    if (node != null) {
      return node.getStringValue();
    }
    return null;
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

  public String getXmlString() {
    if (this.xmlDocument == null) {
      throw new java.lang.NullPointerException("xml doc is null.");
    }
    String xml = this.xmlDocument.asXML();
    return xml;
  }

  public Document getXmlDocument() {
    return xmlDocument;
  }

  /**
   * @param elePath xml元素路径 (e.g. /publication, /pub_members/pub_member[01], /pub_members, )
   * @return Element
   */
  public Element createElement(Element rootNode, String elePath) {
    if (XmlUtils.isEmpty(elePath)) {
      throw new java.lang.NullPointerException("can't create Element with NULL (elePath).");
    }

    elePath = elePath.toLowerCase();
    String[] paths = XmlUtils.splitXPath(elePath); // 分割元素和元素的属性
    String[] segs = paths[0].split("/"); // will return ['/','persons']
    // or
    // ['/','persons','person[1]','psn_id']
    Element parentEle = (Element) rootNode.selectSingleNode(segs[1]);
    if (parentEle == null) {
      parentEle = rootNode.addElement(segs[1]);
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
        Element childEle = null;
        // 页面提交的数据顺序是不确定的，因此如果前面序号的节点后到，则提前创建，防止页面获取XML数据乱序
        for (int i = 1; i <= seqNo; i++) {
          selectedPath = name + "[@seq_no=" + String.valueOf(i) + "]";
          ele = (Element) parentEle.selectSingleNode(selectedPath); // segs[2]如:pub_member[01]
          if (ele == null) {
            ele = parentEle.addElement(name);
            ele.addAttribute("seq_no", String.valueOf(i));
          }
          if (segs.length > 3) {
            childEle = (Element) ele.selectSingleNode(segs[3]);
            if (childEle == null) {
              childEle = ele.addElement(segs[3]);
            }
            ele = childEle;
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
}
