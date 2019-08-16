package com.smate.center.open.service.common;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.smate.center.open.exception.OpenNsfcException;
import com.smate.center.open.model.nsfc.NsfcwsPerson;
import com.smate.center.open.model.nsfc.NsfcwsPublication;
import com.smate.center.open.service.consts.ConstPubTypeService;
import com.smate.center.open.service.friend.FriendService;
import com.smate.center.open.service.publication.PublicationListService;
import com.smate.center.open.service.publication.PublicationService;
import com.smate.center.open.service.user.UserService;
import com.smate.core.base.psn.consts.PsnCnfConst;
import com.smate.core.base.pub.model.Publication;
import com.smate.core.base.utils.constant.ServiceConstants;
import com.smate.core.base.utils.data.XmlUtil;
import com.smate.core.base.utils.model.cas.security.SysRolUser;
import com.smate.core.base.utils.model.security.Person;
import com.smate.core.base.utils.string.ServiceUtil;

/**
 * Iris业务系统与SNS交互时需要用到的公共辅助接口实现.
 * 
 * @author pwl
 * 
 */
@Service("IrisCommonService")
@Transactional(rollbackFor = Exception.class)
public class IrisCommonServiceImpl implements IrisCommonService {

  private Logger logger = LoggerFactory.getLogger(getClass());
  @Autowired
  private UserService userService;
  @Autowired
  private FriendService friendService;
  @Autowired
  private ConstPubTypeService constPubTypeService;
  @Autowired
  private PublicationListService publicationListService;
  @Autowired
  private PublicationService publicationService;

  @Value("${domainscm}")
  private String domainscm;

  @Override
  public List<Integer> getQueryPubPermission(String psnGuidID, Long psnId) throws Exception {
    List<Integer> permissions = new ArrayList<Integer>();
    permissions.add(PsnCnfConst.ALLOWS);// 默认公开
    if (StringUtils.isNotBlank(psnGuidID)) {
      Long connectedPsnId = this.getConnectedPsnId(psnGuidID);
      if (connectedPsnId != null) {
        boolean isFriend = friendService.isPsnFirend(connectedPsnId, psnId);
        if (isFriend) {
          permissions.add(PsnCnfConst.ALLOWS_FRIEND + PsnCnfConst.ALLOWS_SELF);// 好友可见
        }
      }
    }
    return permissions;
  }

  @Override
  public Long getConnectedPsnId(String psnGuidID) throws Exception {
    Long psnId = null;
    try {
      List<Long> psnIdList = userService.getConnectedPsnByGuid(psnGuidID);
      if (CollectionUtils.isNotEmpty(psnIdList)) {
        psnId = psnIdList.get(0);
      }
    } catch (Exception e) {
      throw new Exception("IRIS业务系统接口查询guid=%s关联的SNS系统用户的psnId出现异常", e);
    }
    return psnId;
  }

  @Override
  public String buildPubListXmlStr(List<Publication> publicationList) throws Exception {
    try {
      Document doc =
          DocumentHelper.parseText("<?xml version=\"1.0\" encoding=\"UTF-8\"?><publications></publications>");
      for (Publication publication : publicationList) {
        try {
          Element rootNode = (Element) doc.selectSingleNode("/publications");
          Element pubElement = rootNode.addElement("publication");
          pubElement.addElement("pub_id").addText(publication.getPubId().toString());
          pubElement.addElement("pub_type").addText(constPubTypeService.queryResultTypeName(publication.getPubType()));
          String title =
              StringUtils.isNotBlank(publication.getZhTitle()) ? publication.getZhTitle() : publication.getEnTitle();
          pubElement.addElement("title").addText(title);
          pubElement.addElement("authors").addText(XmlUtil.formateSymbolAuthors(title, publication.getAuthorNames()));
          String source = StringUtils.isNotBlank(publication.getBriefDesc()) ? publication.getBriefDesc()
              : publication.getBriefDescEn();
          pubElement.addElement("source").addText(source == null ? "" : source);
          String citedList = publicationListService
              .convertPubListToString(publicationListService.getPublicationList(publication.getPubId()));
          pubElement.addElement("listed").addText(citedList == null ? "" : citedList);
          pubElement.addElement("cited").addText("");
          pubElement.addElement("isicited")
              .addText(publication.getCitedTimes() != null ? publication.getCitedTimes().toString() : "0");
          pubElement.addElement("doi").addText(publication.getDOI() == null ? "" : publication.getDOI());
          pubElement.addElement("http").addText(domainscm + "/publication/view?des3Id="
              + ServiceUtil.encodeToDes3(publication.getPubId().toString()) + "," + ServiceConstants.SCHOLAR_NODE_ID_1);
          // Long rolPubId =
          // publicationConfirmService.getPsnConfirmPubByRolPubId(publication.getPsnId(),
          // publication.getId());
          // pubElement.addElement("rol_pub_id").addText(ObjectUtils.toString(rolPubId));
        } catch (Exception e) {
          logger.error("构造成果XML列表出现异常", e);
        }
      }
      return doc.getRootElement().asXML();
    } catch (Exception e) {
      throw new OpenNsfcException(e);
    }
  }

  @Override
  public String buildGooglePubListXmlStr(List<NsfcwsPublication> publicationList) throws Exception {
    try {
      Document doc =
          DocumentHelper.parseText("<?xml version=\"1.0\" encoding=\"UTF-8\"?><publications></publications>");
      for (NsfcwsPublication publication : publicationList) {
        try {
          Element rootNode = (Element) doc.selectSingleNode("/publications");
          Element pubElement = rootNode.addElement("publication");
          pubElement.addElement("pub_id").addText(publication.getId().toString());
          pubElement.addElement("pub_type")
              .addText(constPubTypeService.queryResultTypeName(publication.getPubTypeId()));
          String title =
              StringUtils.isNotBlank(publication.getZhTitle()) ? publication.getZhTitle() : publication.getEnTitle();
          pubElement.addElement("title").addText(title);
          pubElement.addElement("authors").addText(XmlUtil.formateSymbolAuthors(title, publication.getAuthorNames()));
          String source = StringUtils.isNotBlank(publication.getBriefDescZh()) ? publication.getBriefDescZh()
              : publication.getBriefDescEn();
          pubElement.addElement("source").addText(source == null ? "" : source);
          pubElement.addElement("listed").addText(publication.getCitedList() == null ? "" : publication.getCitedList());
          pubElement.addElement("cited").addText("");
          pubElement.addElement("isicited")
              .addText(publication.getCitedTimes() != null ? publication.getCitedTimes().toString() : "0");
          pubElement.addElement("doi").addText(publication.getDoi() == null ? "" : publication.getDoi());
        } catch (Exception e) {
          logger.error("构造成果XML列表出现异常", e);
        }
      }
      return doc.getRootElement().asXML();
    } catch (Exception e) {
      throw new Exception(e);
    }
  }

  @Override
  public int checkPsnConnected(String psnGuidID, Long psnId) throws Exception {
    int isConnected = 0;
    try {
      SysRolUser sysRolUser = userService.getSysRolUser(psnGuidID, psnId);
      if (sysRolUser != null) {
        isConnected = 1;
      }
    } catch (Exception e) {
      logger.error(String.format("检查IRIS业务系统用户guid=%s和SNS系统用户psnId=%s是否关联出现异常", psnGuidID, psnId), e);
    }
    return isConnected;
  }

  @Override
  public String buildGooglePsnListXmlStr(List<NsfcwsPerson> personList) throws Exception {
    try {
      Document doc = DocumentHelper.parseText("<?xml version=\"1.0\" encoding=\"UTF-8\"?><persons></persons>");
      for (NsfcwsPerson person : personList) {
        try {
          Element rootNode = (Element) doc.selectSingleNode("/persons");
          Element pubElement = rootNode.addElement("person");
          pubElement.addElement("psn_id").addText(person.getDes3Id());
          pubElement.addElement("psn_name").addText(person.getName());
          pubElement.addElement("psn_photo").addText(person.getAvatars() == null ? "" : person.getAvatars());
          pubElement.addElement("org_name").addText(person.getInsName() == null ? "" : person.getInsName());
          pubElement.addElement("pubs_count").addText(person.getPubNum() == null ? "0" : person.getPubNum().toString());
          pubElement.addElement("email").addText(this.buildEmail(person.getEmail()));

          pubElement.addElement("http")
              .addText(StringUtils.startsWith(person.getHttp(), "http://") ? person.getHttp() : "");
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
      logger.error("重构邮箱email={}显示样式出现异常：", e);

    }
    return emailStr;
  }

  @Override
  public String buildPsnListXmlStr(List<Person> personList) throws Exception {
    try {
      Document doc = DocumentHelper.parseText("<?xml version=\"1.0\" encoding=\"UTF-8\"?><persons></persons>");
      for (Person person : personList) {
        try {
          Element rootNode = (Element) doc.selectSingleNode("/persons");
          Element pubElement = rootNode.addElement("person");
          pubElement.addElement("psn_id").addText(person.getPersonId().toString());
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
          pubElement.addElement("http").addText(domainscm + "/in/view?des3PsnId=" + person.getPersonId());

        } catch (Exception e) {
          logger.error("构造人员XML列表出现异常", e);
        }
      }
      return doc.getRootElement().asXML();
    } catch (Exception e) {
      throw new Exception(e);
    }
  }
}
