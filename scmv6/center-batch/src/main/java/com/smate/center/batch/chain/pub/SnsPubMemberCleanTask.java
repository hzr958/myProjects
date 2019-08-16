package com.smate.center.batch.chain.pub;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
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
public class SnsPubMemberCleanTask implements IPubXmlTask {

  /**
   * 
   */
  private final String name = "sns_pub_member_clean";

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

    // 专利申请人
    String applierName = xmlDocument.getXmlNodeAttribute(PubXmlConstants.PUB_PATENT_XPATH, "patent_applier");
    xmlDocument.setXmlNodeAttribute(PubXmlConstants.PUB_PATENT_XPATH, "patent_applier", applierName);

    // 设定pub mebmer owner's memberr id, 匹配作者单位的ID
    List authors = xmlDocument.getNodes(PubXmlConstants.PUB_MEMBERS_MEMBER_XPATH);
    Map<String, String> authorInsIds = new HashMap<String, String>();// 缓存匹配到的ID
    String ownerPsnId = String.valueOf(context.getCurrentUserId());
    String lang = context.getCurrentLanguage();
    boolean ownerFlag = true;
    for (int i = 0; i < authors.size(); i++) {
      Element ele = (Element) authors.get(i);
      String owner = org.apache.commons.lang.StringUtils.trimToEmpty(ele.attributeValue("owner"));// 是否是本人
      String email = org.apache.commons.lang.StringUtils.trimToEmpty(ele.attributeValue("email"));
      String authorPos = org.apache.commons.lang.StringUtils.trimToEmpty(ele.attributeValue("author_pos"));// 通讯作者
      String firstAutors = org.apache.commons.lang.StringUtils.trimToEmpty(ele.attributeValue("first_author"));// 第一作者
      String insId = org.apache.commons.lang.StringUtils.trimToEmpty(ele.attributeValue("ins_id"));
      String insName = org.apache.commons.lang.StringUtils.trimToEmpty(ele.attributeValue("ins_name"));
      String autoInsName = org.apache.commons.lang.StringUtils.trimToEmpty(ele.attributeValue("auto_ins_name"));

      insName = StringUtils.isBlank(insName) ? StringUtils.trim(autoInsName) : insName;

      String memberPsnId = org.apache.commons.lang.StringUtils.trimToEmpty(ele.attributeValue("ins_name"));
      if (!NumberUtils.isDigits(memberPsnId)) {
        memberPsnId = null;
      }
      email = XmlUtil.changeSBCChar(email); // 全角转半角
      ele.addAttribute("email", email);

      // owner == 1，设置作者ID为当前用户
      if ("1".equals(owner) && ownerFlag) {
        ele.addAttribute("member_psn_id", ownerPsnId); // owner 的 ID
        ele.addAttribute("owner", "1"); // owner 的 ID
        ownerFlag = false;
      } else {
        ele.addAttribute("member_psn_id", "");
        ele.addAttribute("owner", "0");
      }

      if ("".equals(authorPos)) {
        ele.addAttribute("author_pos", "0");
      }
      if ("".equals(firstAutors)) {
        ele.addAttribute("first_author", "0");
      }

      // 第一作者
      if (memberPsnId != null && i == 0) {
        xmlDocument.setXmlNodeAttribute(PubXmlConstants.PUBLICATION_XPATH, "first_author", memberPsnId);
      } else {
        xmlDocument.setXmlNodeAttribute(PubXmlConstants.PUBLICATION_XPATH, "first_author", "");
      }
      // 获取单位名称
      // if (NumberUtils.isDigits(insId) && !insId.equals("1")) {
      // PublicationIns ins =
      // context.getXmlServiceFactory().getPublicationInsService()
      // .lookUpById(Long.valueOf(insId));
      // if (ins != null) {
      // if ("en".equalsIgnoreCase(lang)) {
      // insName = StringUtils.isNotBlank(ins.getEnName()) ?
      // ins.getEnName() : ins.getZhName();
      // } else {
      // insName = StringUtils.isNotBlank(ins.getZhName()) ?
      // ins.getZhName() : ins.getEnName();
      // }
      // } else {
      // insId = "";
      // }
      // }
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
          }
          // else {
          // insName = "";
          // }
        }
      }
      ele.addAttribute("seq_no", String.valueOf(i + 1));
      ele.addAttribute("ins_name", insName);
      ele.addAttribute("ins_id", insId);
    }

    return true;

  }

}
