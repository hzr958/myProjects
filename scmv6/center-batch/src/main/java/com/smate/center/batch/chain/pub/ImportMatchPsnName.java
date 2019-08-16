package com.smate.center.batch.chain.pub;

import java.util.List;

import org.apache.commons.lang.StringUtils;
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
 * 个人导入成果，匹配用户姓名.
 * 
 * @author liqinghua
 * 
 */
public class ImportMatchPsnName implements IPubXmlTask {

  private String name = "import_match_psnname";

  @Autowired
  private PersonManager personManager;

  @Override
  public String getName() {
    return name;
  }

  @Override
  public boolean can(PubXmlDocument xmlDocument, PubXmlProcessContext context) {
    return context.isImport();
  }

  @SuppressWarnings("rawtypes")
  @Override
  public boolean run(PubXmlDocument xmlDocument, PubXmlProcessContext context) throws Exception {

    List authors = xmlDocument.getNodes(PubXmlConstants.PUB_MEMBERS_MEMBER_XPATH);
    if (authors == null || authors.size() == 0) {
      return true;
    }
    // 用户中文名字相同，设置为本人姓名
    Person psn = personManager.getPerson(context.getCurrentUserId());
    String name = psn.getName();// 中文名范例：陈小明
    String enName = "";// 英文名范例：chenxiaoming
    String enName2 = "";// 英文名范例：xiaomingchen
    if (StringUtils.isNotBlank(psn.getFirstName()) && StringUtils.isNotBlank(psn.getLastName())) {
      enName = XmlUtil.getCleanAuthorName3(psn.getLastName() + psn.getFirstName().trim());
      enName2 = XmlUtil.getCleanAuthorName3(psn.getFirstName().trim() + psn.getLastName());
    }
    for (int i = 0; i < authors.size(); i++) {
      Element ele = (Element) authors.get(i);
      String mName = XmlUtil.getCleanAuthorName3(ele.attributeValue("member_psn_name"));
      if (!"".equals(mName)
          && (mName.equalsIgnoreCase(name) || mName.equalsIgnoreCase(enName) || mName.equalsIgnoreCase(enName2))) {
        ele.addAttribute("owner", "1");
        ele.addAttribute("member_psn_id", context.getCurrentUserId().toString());
        return true;
      }
    }
    return true;
  }

}
