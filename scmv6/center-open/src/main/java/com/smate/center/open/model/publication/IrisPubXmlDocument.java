package com.smate.center.open.model.publication;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.dom4j.Node;

import com.smate.core.base.utils.constant.PubXmlConstants;
import com.smate.core.base.utils.data.XmlUtil;

public class IrisPubXmlDocument {

  private Document xmlDocument = null;

  /**
   * @throws DocumentException DocumentException
   */
  public IrisPubXmlDocument(Long pubId) throws DocumentException {
    try {
      String xml = PubXmlConstants.XML_DECL;
      xml = xml + "<data pub_id=\"" + pubId + "\"></data>";
      xmlDocument = DocumentHelper.parseText(xml);
    } catch (DocumentException e) {
      throw e;
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


  public String getRoolElementXmlString() {
    if (this.xmlDocument == null) {
      throw new java.lang.NullPointerException("xml doc is null.");
    }
    String xml = this.xmlDocument.getRootElement().asXML();
    // 删除word特殊字符
    xml = XmlUtil.trimWordChar(xml);
    return xml;
  }
}
