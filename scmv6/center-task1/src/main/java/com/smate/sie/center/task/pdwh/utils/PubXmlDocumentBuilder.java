package com.smate.sie.center.task.pdwh.utils;

import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.dom4j.Attribute;
import org.dom4j.Element;
import org.dom4j.Node;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.smate.center.task.model.sns.quartz.ConstPubType;
import com.smate.center.task.single.constants.PublicationArticleType;
import com.smate.core.base.utils.constant.PubXmlConstants;
import com.smate.core.base.utils.data.XmlUtil;
import com.smate.sie.center.task.pdwh.task.PubXmlDocument;

/**
 * @author yamingd XmlDocument构造器
 */
public class PubXmlDocumentBuilder {
  private final static Logger logger = LoggerFactory.getLogger(PubXmlDocumentBuilder.class);

  /**
   * 合并2个XmlDocument.
   * 
   * @param newImportDoc 新导入的XmlDocument
   * @param oldSavedXml 旧导入的Xml串
   * @throws Exception Exception
   */
  public static void mergeWhenImport(PubXmlDocument newImportDoc, String oldSavedXml) throws Exception {
    if (XmlUtil.isEmpty(oldSavedXml)) {
      return;
    }
    PubXmlDocument oldSavedDoc = null;
    try {
      oldSavedDoc = new PubXmlDocument(oldSavedXml);
    } catch (Exception e) {
      logger.error("xml 合并，xml解析失败:" + oldSavedXml);
      return;
    }
    // 拷贝Fulltext节点
    Node node = oldSavedDoc.getNode(PubXmlConstants.PUB_FULLTEXT_XPATH);
    if (node != null) {
      newImportDoc.copyPubElement((Element) node);
      newImportDoc.setXmlNodeAttribute(PubXmlConstants.PUB_FULLTEXT_XPATH, "permission", "1");
    }

    // 拷贝附件节点
    Element newAttachs = newImportDoc.createElement(PubXmlConstants.PUB_ATTACHMENTS_XPATH);
    List attachs = oldSavedDoc.getNodes(PubXmlConstants.PUB_ATTACHMENTS_ATTACHMENT_XPATH);
    for (int i = 0; i < attachs.size(); i++) {
      Node attach = (Node) attachs.get(i);
      Element newAttach = newAttachs.addElement("pub_attachment");
      newImportDoc.setXmlNodeAttribute(PubXmlConstants.PUB_ATTACHMENTS_ATTACHMENT_XPATH, "permission", "1");
      newImportDoc.copyPubElement(newAttach, (Element) attach);
    }
    // 拷贝期刊节点
    // Element ele = (Element)
    // oldSavedDoc.getNode(PubXmlConstants.PUB_JOURNAL_XPATH);
    // Element newEle = (Element)
    // newImportDoc.getNode(PubXmlConstants.PUB_JOURNAL_XPATH);
    // if (ele != null && newEle == null) {
    // newEle =
    // newImportDoc.createElement(PubXmlConstants.PUB_JOURNAL_XPATH);
    // newImportDoc.copyPubElement(newEle, ele);
    // }

    // 拷贝作者节点
    Element ele = (Element) oldSavedDoc.getNode(PubXmlConstants.PUB_MEMBERS_XPATH);
    Element auele = (Element) oldSavedDoc.getNode(PubXmlConstants.PUB_AUTHORS_XPATH);
    if (ele != null) {
      // 删除新导入成果作者信息，保留原来成果的作者信息.
      newImportDoc.removeNode(PubXmlConstants.PUB_MEMBERS_XPATH);
      newImportDoc.removeNode(PubXmlConstants.PUB_AUTHORS_XPATH);

      Element newEle = newImportDoc.createElement(PubXmlConstants.PUB_MEMBERS_XPATH);
      List members = ele.elements("pub_member");
      for (int index = 0; index < members.size(); index++) {
        Element author = newEle.addElement("pub_member");
        newImportDoc.copyPubElement(author, (Element) members.get(index));
      }
      // 保留之前成果的导入作者.
      if (auele != null) {
        newEle = newImportDoc.createElement(PubXmlConstants.PUB_AUTHORS_XPATH);
        List authors = ele.elements("author");
        for (int index = 0; index < authors.size(); index++) {
          Element author = newEle.addElement("author");
          newImportDoc.copyPubElement(author, (Element) authors.get(index));
        }
      }
    }
    // 合并收录情况
    ele = (Element) oldSavedDoc.getNode(PubXmlConstants.PUB_LIST_XPATH);
    Element newEle = (Element) newImportDoc.getNode(PubXmlConstants.PUB_LIST_XPATH);
    if (newEle == null && ele != null) {
      newEle = newImportDoc.createElement(PubXmlConstants.PUB_LIST_XPATH);
      newImportDoc.copyPubElement(newEle, ele);
    } else if (newEle != null && ele != null) {
      newImportDoc.mergeList(newEle, ele);
    }
    // 完善自身的收录情况
    newImportDoc.fillPubListForImport();

    // publication 合并isbn等
    String[] attrs = PubXmlConstants.PUB_MERGE_REMAIN_PUBLICATION_ATTR;
    ele = (Element) oldSavedDoc.getNode(PubXmlConstants.PUBLICATION_XPATH);
    newEle = (Element) newImportDoc.getNode(PubXmlConstants.PUBLICATION_XPATH);
    newImportDoc.mergeAttr(newEle, ele, attrs);

    // 拷贝PubMeta节点, 合并isi_id、sps_id等
    attrs = PubXmlConstants.PUB_MERGE_REMAIN_META_ATTR;
    ele = (Element) oldSavedDoc.getNode(PubXmlConstants.PUB_META_XPATH);
    if (ele != null) {
      newEle = (Element) newImportDoc.getNode(PubXmlConstants.PUB_META_XPATH);
      if (newEle == null) {
        newEle = newImportDoc.createElement(PubXmlConstants.PUB_META_XPATH);
      }
      newImportDoc.mergeAttr(newEle, ele, attrs);
    }
  }

  /**
   * 合并2个XmlDocument.
   * 
   * @param data 页面Post回的数据
   * @param oldXmlData 旧Xml数据
   * @param typeId TODO
   * @return XmlDocument
   * @throws Exception Exception
   */
  @SuppressWarnings("unchecked")
  public static PubXmlDocument merge(Map data, String oldXmlData, Integer typeId) throws Exception {
    PubXmlDocument xmlDoc = build(data);
    if (!XmlUtil.isEmpty(oldXmlData)) {
      PubXmlDocument oldXmlDoc = new PubXmlDocument(oldXmlData);
      // FIXME 对编辑成果来说 1、是把所有的属性合并，2、还是只对在编辑的情况下，对切换回原始类型时合并属性。
      // 目前做2情况
      if (typeId == oldXmlDoc.getPubTypeId()) {
        Node oldEl = oldXmlDoc.getPublication();
        List<Attribute> attributes = ((Element) oldEl).attributes();
        if (CollectionUtils.isNotEmpty(attributes)) {
          for (Attribute attr : attributes) {
            if (StringUtils.isBlank(xmlDoc.getXmlNodeAttribute(PubXmlConstants.PUBLICATION_XPATH, attr.getName()))
                && StringUtils
                    .isNotBlank(oldXmlDoc.getXmlNodeAttribute(PubXmlConstants.PUBLICATION_XPATH, attr.getName()))) {
              xmlDoc.setXmlNodeAttribute(PubXmlConstants.PUBLICATION_XPATH, attr.getName(),
                  oldXmlDoc.getXmlNodeAttribute(PubXmlConstants.PUBLICATION_XPATH, attr.getName()));
            }
          }
        }
      }
      String[] copyEle = PubXmlConstants.PUB_NODES;
      for (String item : copyEle) {
        String xpath = "/" + item;
        Node oldNode = oldXmlDoc.getNode(xpath);
        Node newNode = xmlDoc.getNode(xpath);
        if (newNode == null && oldNode != null) {
          xmlDoc.copyPubElement((Element) oldNode);
        }
      }
      String authorNames = oldXmlDoc.getXmlNodeAttribute(PubXmlConstants.PUBLICATION_XPATH, "author_names");
      xmlDoc.setNodeAttribute(PubXmlConstants.PUBLICATION_XPATH, "author_names",
          authorNames.replaceAll("</?[^>]+>", ""));
    }
    String zhTitle = xmlDoc.getXmlNodeAttribute(PubXmlConstants.PUBLICATION_XPATH, "zh_title");
    xmlDoc.setNodeAttribute(PubXmlConstants.PUBLICATION_XPATH, "zh_title", zhTitle.replaceAll("</?[^>]+>", ""));
    String enTitle = xmlDoc.getXmlNodeAttribute(PubXmlConstants.PUBLICATION_XPATH, "en_title");
    xmlDoc.setNodeAttribute(PubXmlConstants.PUBLICATION_XPATH, "en_title", enTitle.replaceAll("</?[^>]+>", ""));
    return xmlDoc;
  }

  /**
   * 使用客户端发回的数据构建XmlDocument.
   * 
   * @param data (Map)
   * @return XmlDocument
   * @throws Exception Exception
   */
  @SuppressWarnings("unchecked")
  public static PubXmlDocument build(Map data) throws Exception {

    if (data == null) {
      throw new java.lang.NullPointerException("can't build XmlDocument with NULL paramters.");
    }
    PubXmlDocument xmlDoc = new PubXmlDocument();
    Iterator itor = data.keySet().iterator();
    while (itor.hasNext()) {
      String key = String.valueOf(itor.next());
      if (XmlUtil.isAttributePath(key)) {
        // 为XML节点、属性(split("/@"))
        String[] xpath = XmlUtil.splitXPath(key);
        // 获取序号[01][02]..
        Integer seqNo = XmlUtil.getSeqNo(xpath[0]);
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
        element.addAttribute(xpath[1], StringUtils.trimToEmpty(val[0]));

        if (seqNo != null) {
          element.addAttribute("seq_no", String.valueOf(seqNo));
        }
      }
    }
    return xmlDoc;
  }

  /**
   * @param type 成果类型
   * @param currentUserId 当前用户ID
   * @param currentUserName 当前用户名
   * @param currentInsId 当前单位ID,可为空
   * @param currentInsName 当前单位名，可为空
   * @param currentUserEmail 当前用户Email
   * @param formTmpl : 录入模板名称
   * @return XmlDocument
   * @throws Exception Exception
   */
  public static PubXmlDocument createForR(ConstPubType type, Long currentUserId, String currentUserName,
      Long currentInsId, String currentInsName, String currentUserEmail, String formTmpl, Integer articleType,
      Integer currentNodeId) throws Exception {

    if (currentUserId == null) {
      throw new java.lang.NullPointerException("currentUserId参数不能为空.");
    }
    if (currentNodeId == null) {
      throw new java.lang.NullPointerException("currentNodeId参数不能为空.");
    }

    PubXmlDocument xmlDoc = new PubXmlDocument();

    Element elem = null;
    if (articleType != PublicationArticleType.REFERENCE) {
      // reference 不需要默认作者.
      elem = xmlDoc.createElement(PubXmlConstants.PUB_MEMBERS_MEMBER_XPATH);
      elem.addAttribute("seq_no", "1");
      elem.addAttribute("email", currentUserEmail);
      elem.addAttribute("member_psn_name", currentUserName);
      elem.addAttribute("member_psn_id", String.valueOf(currentUserId));
      elem.addAttribute("owner", "1");
      elem.addAttribute("ins_id1", currentInsId == null ? "" : String.valueOf(currentInsId));
      elem.addAttribute("ins_name1", currentInsName == null ? "" : currentInsName.trim());
    }
    elem = xmlDoc.createElement(PubXmlConstants.PUB_TYPE_XPATH);
    elem.addAttribute("id", String.valueOf(type.getId()));
    elem.addAttribute("zh_name", type.getZhName());
    elem.addAttribute("en_name", type.getEnName());

    Node node = xmlDoc.getPubMeta();
    elem = node != null ? (Element) node : xmlDoc.createElement(PubXmlConstants.PUB_META_XPATH);
    elem.addAttribute("record_psn_id", String.valueOf(currentUserId));
    elem.addAttribute("create_psn_id", String.valueOf(currentUserId));
    elem.addAttribute("tmpl_form", formTmpl);
    elem.addAttribute("record_from", "0"); // 录入
    elem.addAttribute("record_node_id", String.valueOf(currentNodeId));
    return xmlDoc;
  }

  /**
   * @param type 成果类型
   * @param currentUserId 当前用户ID
   * @param currentInsId 当前单位ID
   * @param formTmpl : 录入模板名称
   * @return XmlDocument
   * @throws Exception Exception
   */
  public static PubXmlDocument createForRO(ConstPubType type, long currentUserId, Long currentInsId, String formTmpl,
      Integer currentNodeId, Map<String, Object> authorMsg) throws Exception {

    if (currentInsId == null) {
      throw new java.lang.NullPointerException("currentInsId can't be NULL.");
    }
    if (currentNodeId == null) {
      throw new java.lang.NullPointerException("currentNodeId参数不能为空.");
    }

    PubXmlDocument xmlDoc = new PubXmlDocument();
    // 成果类型节点信息
    Element elem = xmlDoc.createElement(PubXmlConstants.PUB_TYPE_XPATH);
    elem.addAttribute("id", String.valueOf(type.getId()));
    elem.addAttribute("zh_name", type.getZhName());
    elem.addAttribute("en_name", type.getEnName());
    // meta节点信息
    Node node = xmlDoc.getPubMeta();
    elem = node != null ? (Element) node : xmlDoc.createElement(PubXmlConstants.PUB_META_XPATH);
    elem.addAttribute("record_ins_id", String.valueOf(currentInsId));
    elem.addAttribute("create_psn_id", String.valueOf(currentUserId));
    elem.addAttribute("tmpl_form", formTmpl);
    elem.addAttribute("record_from", "0"); // 录入
    elem.addAttribute("record_node_id", String.valueOf(currentNodeId));
    // 初始化作者信息
    if (authorMsg != null) {
      String userEmail = (String) authorMsg.get("psnEmail");
      String psnName = (String) authorMsg.get("psnName");
      Long psnId = (Long) authorMsg.get("psnId");
      String insName = (String) authorMsg.get("insName");
      Long insId = (Long) authorMsg.get("insId");
      // reference 不需要默认作者.
      elem = xmlDoc.createElement(PubXmlConstants.PUB_MEMBERS_MEMBER_XPATH);
      elem.addAttribute("seq_no", "1");
      elem.addAttribute("email", userEmail == null ? "" : userEmail);
      elem.addAttribute("member_psn_name", psnName == null ? "" : psnName);
      elem.addAttribute("member_psn_acname", psnName == null ? "" : psnName);
      elem.addAttribute("member_psn_id", psnId.toString());
      elem.addAttribute("ins_id", insId == null ? "" : insId.toString());
      elem.addAttribute("ins_name", insName == null ? "" : insName);
    }
    return xmlDoc;
  }
}
