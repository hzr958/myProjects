package com.smate.center.open.service.profile;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.apache.commons.lang.ObjectUtils;
import org.apache.commons.lang.StringUtils;
import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.smate.center.open.service.publication.PublicationService;
import com.smate.core.base.psn.consts.PsnCnfConst;
import com.smate.core.base.utils.model.security.Person;
import com.smate.core.base.utils.service.consts.SysDomainConst;
import com.smate.core.base.utils.string.ServiceUtil;

@Service("psnInfoXmlService")
@Transactional(rollbackFor = Exception.class)
public class PsnInfoXmlServiceImpl implements PsnInfoXmlService {

  protected Logger logger = LoggerFactory.getLogger(getClass());
  @Autowired
  private PublicationService publicationService;
  @Autowired
  private SysDomainConst sysDomainConst;

  @Override
  public String buildPsnListXmlStr(List<Person> personList) throws Exception {
    try {
      Document doc = DocumentHelper.parseText("<?xml version=\"1.0\" encoding=\"UTF-8\"?><persons></persons>");
      for (Person person : personList) {
        try {
          Element rootNode = (Element) doc.selectSingleNode("/persons");
          Element pubElement = rootNode.addElement("person");
          String psnDes3Id = ServiceUtil.encodeToDes3(person.getPersonId().toString());
          pubElement.addElement("psn_id").addText(psnDes3Id);
          pubElement.addElement("psn_name").addText(person.getName());
          pubElement.addElement("psn_photo").addText(person.getAvatars() == null ? "" : person.getAvatars());
          pubElement.addElement("org_name").addText(person.getInsName() == null ? "" : person.getInsName());
          List<Integer> permissions = new ArrayList<Integer>();
          permissions.add(PsnCnfConst.ALLOWS);// 默认公开
          pubElement.addElement("pubs_count").addText(ObjectUtils
              .toString(publicationService.getPsnPublicPubCount(person.getPersonId(), null, null, permissions, null)));
          pubElement.addElement("all_pubs_count").addText(
              ObjectUtils.toString(publicationService.getPsnPubCount(person.getPersonId(), null, null, null, null)));
          pubElement.addElement("email").addText(this.buildEmail(person.getEmail()));
          pubElement.addElement("http").addText(
              sysDomainConst.getSnsDomain() + sysDomainConst.getSnsContext() + "/in/view?des3PsnId=" + psnDes3Id);
        } catch (Exception e) {
          logger.error("构造人员XML列表出现异常", e);
        }
      }
      return doc.getRootElement().asXML();
    } catch (Exception e) {
      throw new Exception(e);
    }
  }

  @Override
  public String buildEmail(String email) throws Exception {
    String emailStr = "";
    try {
      if (StringUtils.isNotBlank(email)) {
        if (email.length() >= 2) {
          emailStr = email.substring(0, 2) + "***" + email.substring(email.lastIndexOf("@"), email.length());
        } else {
          emailStr = "***";
        }
      }
    } catch (Exception e) {
      logger.error("重构邮箱email显示样式出现异常,email=" + email, e);
    }
    return emailStr;
  }

  @Override
  public String generateConnectedCode() throws Exception {
    try {
      char[] numAndLetterStr = "0123456789".toCharArray();
      StringBuffer strBuffer = new StringBuffer();
      Random random = new Random();
      for (int i = 0; i < 6; i++) {
        strBuffer.append(numAndLetterStr[random.nextInt(9)]);
      }
      return strBuffer.toString();
    } catch (Exception e) {
      logger.error("生成关联验证码出现异常：", e);
      throw new Exception(e);
    }
  }

}
