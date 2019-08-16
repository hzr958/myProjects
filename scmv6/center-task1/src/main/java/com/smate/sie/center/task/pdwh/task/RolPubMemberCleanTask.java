package com.smate.sie.center.task.pdwh.task;

import java.util.List;

import org.dom4j.Element;

import com.smate.core.base.utils.constant.PubXmlConstants;
import com.smate.sie.center.task.pdwh.utils.XmlUtil;

/**
 * 成果作者详情处理.
 * 
 * @author yamingd
 */
public class RolPubMemberCleanTask implements IPubXmlTask {

  /**
   * 
   */
  private final String name = "rol_pub_member_clean";

  /*
   * (non-Javadoc)
   * 
   * @see com.iris.scm.xml.IXmlTask#getName()
   */
  @Override
  public String getName() {
    return this.name;
  }

  /*
   * (non-Javadoc)
   * 
   * @see com.iris.scm.xml.IXmlTask#can(com.iris.scm.xml.XmlDocument,
   * com.iris.scm.xml.XmlProcessContext)
   */
  @Override
  public boolean can(PubXmlDocument xmlDocument, PubXmlProcessContext context) {
    return false;
  }

  @SuppressWarnings("rawtypes")
  @Override
  public boolean run(PubXmlDocument xmlDocument, PubXmlProcessContext context) throws Exception {
    // 指派作者节点
    String authorNames = xmlDocument.getXmlNodeAttribute(PubXmlConstants.PUBLICATION_XPATH, "author_names");
    if (authorNames.endsWith(";")) {
      authorNames = authorNames.substring(0, authorNames.length() - 1);
      authorNames = XmlUtil.formateAuthorNames(authorNames);
      xmlDocument.setXmlNodeAttribute(PubXmlConstants.PUBLICATION_XPATH, "author_names", authorNames);
    }
    // 设定pub mebmer owner's memberr id, 匹配作者单位的ID
    List authors = xmlDocument.getNodes(PubXmlConstants.PUB_MEMBERS_MEMBER_XPATH);
    for (int i = 0; i < authors.size(); i++) {
      Element ele = (Element) authors.get(i);
      String email = org.apache.commons.lang.StringUtils.trimToEmpty(ele.attributeValue("email"));
      String authorPos = org.apache.commons.lang.StringUtils.trimToEmpty(ele.attributeValue("author_pos"));// 通讯作者
      String insId = org.apache.commons.lang.StringUtils.trimToEmpty(ele.attributeValue("ins_id"));
      String insName = org.apache.commons.lang.StringUtils.trimToEmpty(ele.attributeValue("ins_name"));
      email = XmlUtil.changeSBCChar(email); // 全角转半角
      ele.addAttribute("email", email);
      // 单位清除owner
      ele.addAttribute("owner", "0");
      if ("".equals(authorPos)) {
        ele.addAttribute("author_pos", "0");
      }
      ele.addAttribute("seq_no", String.valueOf(i + 1));
      ele.addAttribute("ins_name", insName);
      ele.addAttribute("ins_id", insId);
    }
    return true;

  }

}
