package com.smate.center.batch.chain.pub.rol;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.math.NumberUtils;
import org.dom4j.Element;

import com.smate.center.batch.model.sns.pub.PubXmlProcessContext;
import com.smate.center.batch.model.sns.pub.PublicationIns;
import com.smate.center.batch.oldXml.pub.PubXmlDocument;
import com.smate.center.batch.service.pub.IPubXmlTask;
import com.smate.core.base.utils.constant.PubXmlConstants;
import com.smate.core.base.utils.data.XmlUtil;

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
    return true;
  }

  @SuppressWarnings("rawtypes")
  @Override
  public boolean run(PubXmlDocument xmlDocument, PubXmlProcessContext context) throws Exception {

    // 指派作者节点
    String authorNames = xmlDocument.getXmlNodeAttribute(PubXmlConstants.PUBLICATION_XPATH, "author_names");
    if (authorNames.endsWith(";")) {
      authorNames = authorNames.substring(0, authorNames.length() - 1);
      xmlDocument.setXmlNodeAttribute(PubXmlConstants.PUBLICATION_XPATH, "author_names", authorNames);
    }

    // 设定pub mebmer owner's memberr id, 匹配作者单位的ID
    List authors = xmlDocument.getNodes(PubXmlConstants.PUB_MEMBERS_MEMBER_XPATH);
    Map<String, String> authorInsIds = new HashMap<String, String>();// 缓存匹配到的ID
    String lang = context.getCurrentLanguage();
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

      // 第一作者
      String memberPsnId = org.apache.commons.lang.StringUtils.trimToEmpty(ele.attributeValue("member_psn_id"));
      if (!"".equals(memberPsnId) && i == 0) {
        xmlDocument.setXmlNodeAttribute(PubXmlConstants.PUBLICATION_XPATH, "first_author", memberPsnId);
      } else {
        xmlDocument.setXmlNodeAttribute(PubXmlConstants.PUBLICATION_XPATH, "first_author", "");
      }
      // 获取单位名称，1表示单位有地址，但是匹配不到任何一个机构
      if (NumberUtils.isDigits(insId) && !"1".equals(insId)) {
        PublicationIns ins = context.getXmlServiceFactory().getPublicationInsService().lookUpById(Long.valueOf(insId));
        if (ins != null) {
          if ("en".equalsIgnoreCase(lang)) {
            insName = ins.getEnName();
          } else {
            insName = ins.getZhName();
          }
        } else {
          insId = "";
        }
      }
      // 获取单位ID
      if ("".equals(insId) && !"".equals(insName)) {// ID为空，名字不为空
        // 匹配单位ID
        if (authorInsIds.keySet().contains(insName.toLowerCase())) {
          insId = authorInsIds.get(insName.toLowerCase());
        } else {
          // 匹配单位ID
          PublicationIns ins = context.getXmlServiceFactory().getPublicationInsService().lookUpByName(insName);
          if (ins != null) {
            insId = String.valueOf(ins.getInsId());
            authorInsIds.put(insName, insId);
          } else {
            insName = "";
          }
        }
      }
      ele.addAttribute("seq_no", String.valueOf(i + 1));
      ele.addAttribute("ins_name", insName);
      ele.addAttribute("ins_id", insId);
    }

    return true;

  }

}
