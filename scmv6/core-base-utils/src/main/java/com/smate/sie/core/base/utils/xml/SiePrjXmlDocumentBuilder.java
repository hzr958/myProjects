package com.smate.sie.core.base.utils.xml;

import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringEscapeUtils;
import org.dom4j.Element;
import org.dom4j.Node;

import com.smate.core.base.utils.data.XmlUtil;

/**
 * @author yamingd XmlDocument构造器
 */
public class SiePrjXmlDocumentBuilder {

  /**
   * 合并2个XmlDocument.
   * 
   * @param newImportDoc 新导入的XmlDocument
   * @param oldSavedXml 旧导入的Xml串
   * @throws Exception Exception
   */
  @SuppressWarnings("unchecked")
  public static void mergeWhenImport(SiePrjXmlDocument newImportDoc, String oldSavedXml) throws Exception {
    if (XmlUtil.isEmpty(oldSavedXml)) {
      return;
    }
    SiePrjXmlDocument oldSavedDoc = new SiePrjXmlDocument(oldSavedXml);
    // 拷贝Fulltext节点
    Node node = oldSavedDoc.getNode(SiePrjXmlConstants.PRJ_FULLTEXT_XPATH);
    if (node != null) {
      newImportDoc.copyPrjElement((Element) node);
    }
    // 拷贝PrjMeta节点，不合并，使用新数据
    // node = oldSavedDoc.getNode(SiePrjXmlConstants.PRJ_META_XPATH);
    // if (node != null) {
    // newImportDoc.copyPrjElement((Element) node);
    // }

    // 拷贝project节点，不合并，使用新数据
    // Element ele = (Element)
    // oldSavedDoc.getNode(SiePrjXmlConstants.PROJECT_XPATH);
    // Element newEle = (Element)
    // newImportDoc.getNode(SiePrjXmlConstants.PROJECT_XPATH);
    // if (ele != null && newEle == null) {
    // newEle =
    // newImportDoc.createElement(SiePrjXmlConstants.PROJECT_XPATH);
    // newImportDoc.copyPrjElement(newEle, ele);
    // }

    // 拷贝附件节点
    Element newAttachs = newImportDoc.createElement(SiePrjXmlConstants.PRJ_ATTACHMENTS_XPATH);
    List attachs = oldSavedDoc.getNodes(SiePrjXmlConstants.PRJ_ATTACHMENTS_ATTACHMENT_XPATH);
    for (int i = 0; i < attachs.size(); i++) {
      Node attach = (Node) attachs.get(i);
      Element newAttach = newAttachs.addElement("prj_attachment");
      newImportDoc.copyPrjElement(newAttach, (Element) attach);
    }
    // 删除导入项目参与人
    newImportDoc.removeNode(SiePrjXmlConstants.PRJ_MEMBERS_XPATH);
    // 拷贝作者节点
    Element ele = (Element) oldSavedDoc.getNode(SiePrjXmlConstants.PRJ_MEMBERS_XPATH);
    Element newEle = (Element) newImportDoc.getNode(SiePrjXmlConstants.PRJ_MEMBERS_XPATH);
    if (ele != null && newEle == null) {
      newEle = newImportDoc.createElement(SiePrjXmlConstants.PRJ_MEMBERS_XPATH);
      newEle.addAttribute("merge_author", "1");
      List authors = ele.elements("prj_member");
      for (int index = 0; index < authors.size(); index++) {
        Element author = newEle.addElement("prj_member");
        newImportDoc.copyPrjElement(author, (Element) authors.get(index));
      }
    }
  }

  /**
   * 使用客户端发回的数据构建XmlDocument.
   * 
   * @param data (Map)
   * @return XmlDocument
   * @throws Exception Exception
   */
  @SuppressWarnings("unchecked")
  public static SiePrjXmlDocument build(Map data) throws Exception {

    if (data == null) {
      throw new java.lang.NullPointerException("can't build XmlDocument with NULL paramters.");
    }
    SiePrjXmlDocument xmlDoc = new SiePrjXmlDocument();
    Iterator itor = data.keySet().iterator();
    while (itor.hasNext()) {
      String key = String.valueOf(itor.next());
      if (XmlUtil.isAttributePath(key)) {
        // 为XML节点、属性(split("/@"))
        String[] xpath = XmlUtil.splitXPath(key);
        // 获取序号[01][02]..
        Integer seqNo = XmlUtil.getSeqNo(xpath[0]);
        // seq = 0的为模板
        if (seqNo != null && seqNo == 0) {
          continue;
        }
        // 将节点组织成xpath(/pub_members/pub_member[1] --
        // /pub_members/pub_member[@seq_no=1])
        String selectedPath = xpath[0];
        if (seqNo != null) {
          selectedPath = selectedPath.replaceAll("\\[\\d+\\]", "");
          selectedPath = selectedPath + "[@seq_no=" + seqNo.toString() + "]";
        }
        Element element = (Element) xmlDoc.getNode(selectedPath);
        // 节点不存在，则创建
        if (element == null) {
          element = xmlDoc.createElement(xpath[0]);
        }
        // 设置属性值
        String[] val = (String[]) data.get(key);
        if ("/project/@zh_title".equals(key) || "/project/@en_title".equals(key) || "/project/@zh_abstract".equals(key)
            || "/project/@en_abstract".equals(key)) {
          val[0] = StringEscapeUtils.unescapeHtml4(val[0]);
          val[0] = StringEscapeUtils.unescapeHtml4(val[0]);
        }
        element.addAttribute(xpath[1], val[0]);

        if (seqNo != null) {
          element.addAttribute("seq_no", String.valueOf(seqNo));
        }
      }
    }
    return xmlDoc;
  }

  /**
   * @param currentUserId 当前用户ID
   * @param currentUserName 当前用户名
   * @param currentInsId 当前单位ID,可为空
   * @param currentInsName 当前单位名，可为空
   * @param currentUserEmail 当前用户Email
   * @param formTmpl : 录入模板名称
   * @param currentUserNameEn TODO
   * @param type 项目类型
   * @return XmlDocument
   * @throws Exception Exception
   */
  public static SiePrjXmlDocument createForR(Long currentUserId, String currentUserName, Long currentInsId,
      String currentInsName, String currentUserEmail, String formTmpl, Integer currentNodeId, String currentUserNameEn)
      throws Exception {

    if (currentUserId == null) {
      throw new java.lang.NullPointerException("currentUserId参数不能为空.");
    }
    if (currentNodeId == null) {
      throw new java.lang.NullPointerException("currentNodeId参数不能为空.");
    }

    SiePrjXmlDocument xmlDoc = new SiePrjXmlDocument();

    Element elem = null;
    // 默认作者.
    elem = xmlDoc.createElement(SiePrjXmlConstants.PRJ_MEMBERS_MEMBER_XPATH);
    elem.addAttribute("seq_no", "1");
    elem.addAttribute("email", currentUserEmail);
    elem.addAttribute("member_psn_name", currentUserName);
    elem.addAttribute("member_psn_name_en", currentUserNameEn);
    elem.addAttribute("member_psn_id", String.valueOf(currentUserId));
    elem.addAttribute("owner", "1");
    elem.addAttribute("ins_id1", currentInsId == null ? "" : String.valueOf(currentInsId));
    elem.addAttribute("ins_name1", currentInsName == null ? "" : currentInsName.trim());

    Node node = xmlDoc.getPrjMeta();
    elem = node != null ? (Element) node : xmlDoc.createElement(SiePrjXmlConstants.PRJ_META_XPATH);
    elem.addAttribute("record_psn_id", String.valueOf(currentUserId));
    elem.addAttribute("create_psn_id", String.valueOf(currentUserId));
    elem.addAttribute("tmpl_form", formTmpl);
    elem.addAttribute("record_from", "0"); // 录入
    elem.addAttribute("record_node_id", String.valueOf(currentNodeId));
    return xmlDoc;
  }

  /**
   * @param type 项目类型
   * @param currentUserId 当前用户ID
   * @param currentInsId 当前单位ID
   * @param formTmpl : 录入模板名称
   * @return XmlDocument
   * @throws Exception Exception
   */
  public static SiePrjXmlDocument createForRO(long currentUserId, Long currentInsId, String formTmpl,
      Integer currentNodeId) throws Exception {

    if (currentInsId == null) {
      throw new java.lang.NullPointerException("currentInsId can't be NULL.");
    }
    if (currentNodeId == null) {
      throw new java.lang.NullPointerException("currentNodeId参数不能为空.");
    }

    SiePrjXmlDocument xmlDoc = new SiePrjXmlDocument();
    // meta节点信息
    Node node = xmlDoc.getPrjMeta();
    Element elem = node != null ? (Element) node : xmlDoc.createElement(SiePrjXmlConstants.PRJ_META_XPATH);
    elem.addAttribute("record_ins_id", String.valueOf(currentInsId));
    elem.addAttribute("create_psn_id", String.valueOf(currentUserId));
    elem.addAttribute("tmpl_form", formTmpl);
    elem.addAttribute("record_from", "0"); // 录入
    elem.addAttribute("record_node_id", String.valueOf(currentNodeId));
    return xmlDoc;
  }
}
