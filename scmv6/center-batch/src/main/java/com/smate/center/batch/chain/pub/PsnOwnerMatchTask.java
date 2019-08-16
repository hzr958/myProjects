package com.smate.center.batch.chain.pub;

import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.dom4j.Element;
import org.springframework.beans.factory.annotation.Autowired;

import com.smate.center.batch.model.sns.pub.PubXmlProcessContext;
import com.smate.center.batch.oldXml.pub.PubXmlDocument;
import com.smate.center.batch.service.psn.PersonManager;
import com.smate.center.batch.service.pub.IPubXmlTask;
import com.smate.core.base.utils.constant.PubXmlConstants;
import com.smate.core.base.utils.data.XmlUtil;
import com.smate.core.base.utils.model.security.Person;

/**
 * 成果导入或者新增成果时，当前用户匹配成果作者，用于设置成果的公开权限，如果未精确或者模糊匹配上则设置为本人.
 * 
 * @author liqinghua
 * 
 */
public class PsnOwnerMatchTask implements IPubXmlTask {

  private String name = "match_owner";

  @Autowired
  private PersonManager personManager;

  @Override
  public String getName() {
    return name;
  }

  @Override
  public boolean can(PubXmlDocument xmlDocument, PubXmlProcessContext context) {
    return true;
  }

  @SuppressWarnings("rawtypes")
  @Override
  public boolean run(PubXmlDocument xmlDocument, PubXmlProcessContext context) throws Exception {
    if (!xmlDocument.isExistPubId() && context.getPubSimple().getSimpleTask() == 0) {// 新增
      xmlDocument.setXmlNodeAttribute(PubXmlConstants.PUB_META_XPATH, "isAdd", "true");
    } else {
      xmlDocument.setXmlNodeAttribute(PubXmlConstants.PUB_META_XPATH, "isAdd", "false");
    }
    List authors = xmlDocument.getNodes(PubXmlConstants.PUB_MEMBERS_MEMBER_XPATH);
    if (authors == null || authors.size() == 0) {
      xmlDocument.setXmlNodeAttribute(PubXmlConstants.PUBLICATION_XPATH, "match_owner", "false");
      return true;
    }
    String impMatchOwnerFlag =
        xmlDocument.getXmlNodeAttribute(PubXmlConstants.PUBLICATION_XPATH, "imp_match_owner_flag");
    // 如果不为空，则已经在文件导入时匹配过了，此处不在匹配了
    if (StringUtils.isNotBlank(impMatchOwnerFlag))
      return true;
    Boolean flag = false;
    Person psn = personManager.getPerson(context.getCurrentUserId());
    for (int i = 0; i < authors.size(); i++) {
      Element ele = (Element) authors.get(i);
      String pmName = ele.attributeValue("member_psn_name");
      flag = matchPubAthor(pmName, psn);
      if (flag) {
        break;
      }
    }
    xmlDocument.setXmlNodeAttribute(PubXmlConstants.PUBLICATION_XPATH, "match_owner", flag.toString());
    return true;
  }

  public boolean matchPubAthor(String pmName, Person psn) {
    if (StringUtils.isBlank(pmName) || null == psn) {
      return false;
    }
    pmName = pmName.toLowerCase();
    if (StringUtils.isNotBlank(psn.getName())) {
      if (pmName.equalsIgnoreCase(psn.getName())) {
        return true;
      }
    }
    String firstName = XmlUtil.getCleanAuthorName(psn.getFirstName());
    String lastName = XmlUtil.getCleanAuthorName(psn.getLastName());
    if (StringUtils.isBlank(firstName) || StringUtils.isBlank(lastName)) {
      return false;
    }
    String preF = firstName.substring(0, 1).toLowerCase();
    lastName = lastName.toLowerCase();
    // 尝试z lin 是否匹配上alen z lin或者 z alen lin
    int index = pmName.indexOf(preF);
    if (index > -1 && pmName.substring(index).endsWith(lastName)) {
      return true;
    }
    // 尝试lin z是否匹配上lin z alen或者lin alen z
    index = pmName.lastIndexOf(preF);
    if (index > 0 && pmName.substring(0, index).startsWith(lastName)) {
      return true;
    }
    return false;
  }


}
