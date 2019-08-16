package com.smate.center.task.service.group;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.transaction.Transactional;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.smate.center.mail.connector.model.MailLinkInfo;
import com.smate.center.mail.connector.model.MailOriginalDataInfo;
import com.smate.center.task.dao.group.EmailGroupPubPsnDao;
import com.smate.center.task.dao.group.GroupPublicationDao;
import com.smate.center.task.dao.group.GrpPubsDao;
import com.smate.center.task.dao.sns.grp.GrpMemberDao;
import com.smate.center.task.dao.sns.quartz.GrpBaseInfoDao;
import com.smate.center.task.dao.sns.quartz.GrpIndexUrlDao;
import com.smate.center.task.dao.sns.quartz.GrpPubIndexUrlDao;
import com.smate.center.task.model.group.EmailGroupPubPsn;
import com.smate.center.task.model.grp.GrpBaseinfo;
import com.smate.center.task.model.grp.GrpPubs;
import com.smate.center.task.model.sns.quartz.GrpPubIndexUrl;
import com.smate.center.task.single.service.person.PersonService;
import com.smate.center.task.v8pub.dao.sns.PubFullTextDAO;
import com.smate.center.task.v8pub.dao.sns.PubSnsDAO;
import com.smate.center.task.v8pub.sns.po.PubFullTextPO;
import com.smate.center.task.v8pub.sns.po.PubSnsPO;
import com.smate.core.base.email.service.EmailCommonService;
import com.smate.core.base.psn.dao.PsnProfileUrlDao;
import com.smate.core.base.psn.model.PsnProfileUrl;
import com.smate.core.base.utils.constant.ShortUrlConst;
import com.smate.core.base.utils.json.JacksonUtils;
import com.smate.core.base.utils.model.security.Person;
import com.smate.core.base.utils.security.Des3Utils;
import com.smate.core.base.utils.string.ServiceUtil;
import com.smate.core.base.utils.sys.ScmSystemUtil;

/**
 * 邮件初始数据服务类
 * 
 * @author zk
 *
 */
@Service("groupPublicationAddEmailService")
@Transactional(rollbackOn = Exception.class)
public class GroupPublicationAddEmailServiceImpl implements GroupPublicationAddEmailService {
  private Logger logger = LoggerFactory.getLogger(getClass());
  @Value("${domainMobile}")
  private String domainMobile;
  @Value("${domainscm}")
  private String domainscm;
  @Autowired
  private GroupPublicationDao groupPublicationDao;

  @Autowired
  private PersonService personService;

  @Autowired
  private ScmSystemUtil scmSystemUtil;

  @Autowired
  private GrpPubsDao grpPubsDao;
  @Autowired
  private GrpBaseInfoDao grpBaseInfoDao;
  @Autowired
  private GrpMemberDao grpMemberDao;
  @Autowired
  private EmailGroupPubPsnDao emailGroupPubPsnDao;
  @Autowired
  private PubSnsDAO pubSnsDAO;
  @Autowired
  private PubFullTextDAO pubFullTextDAO;
  @Autowired
  private GrpPubIndexUrlDao grpPubIndexUrlDao;
  @Autowired
  private PsnProfileUrlDao psnProfileUrlDao;
  @Value(value = "${sendEmail.restful.url}")
  private String sendRestfulUrl;
  @Autowired
  private RestTemplate restTemplate;
  @Autowired
  private GrpIndexUrlDao grpIndexUrlDao;

  /**
   * 更新今天新增的成果id,人员id,群组id,更新时间到表AddGroupPubEmail
   */
  @Override
  public int updateAddGroupPubEmail() throws Exception {
    return 0;
  }

  /**
   * 获取当天状态为0在群组中添加了成果的人员id列表
   */
  @Override
  public List<Long> getAddGroupPub() throws Exception {
    List<Long> groupPubList = groupPublicationDao.getAddGroupPubEmail();
    return groupPubList;

  }



  /**
   * 
   * @param person
   * @param language zh=中文
   * @return
   */
  String getPersonName(Person person, String language) {
    if ("zh".equals(language) || "zh_CN".equals(language)) {
      if (StringUtils.isNotBlank(person.getName())) {
        return person.getName();
      } else if (StringUtils.isNotBlank(person.getFirstName()) && StringUtils.isNotBlank(person.getLastName())) {
        return person.getFirstName() + " " + person.getLastName();
      } else {
        return person.getEname();
      }
    } else {
      if (StringUtils.isNotBlank(person.getEname())) {
        return person.getEname();
      } else if (StringUtils.isNotBlank(person.getFirstName()) && StringUtils.isNotBlank(person.getLastName())) {
        return person.getFirstName() + " " + person.getLastName();
      } else {
        return person.getName();
      }

    }
  }

  @Override
  public boolean isExists(Long pubId, Long groupId) throws Exception {
    return emailGroupPubPsnDao.isExists(pubId, groupId);
  }

  @Override
  public void save(EmailGroupPubPsn emailGroupPubPsn) throws Exception {
    emailGroupPubPsnDao.save(emailGroupPubPsn);
  }


  /**
   * 查找当天上传成果的群组id，排除当前已经发邮件的群组id
   */
  @Override
  public List<Long> getUploadPubGrpId() throws Exception {

    return grpPubsDao.getUploadPubGrpId();
  }

  /**
   * 查找当天上传成果
   */
  @Override
  public GrpPubs getTodayUplaodGrpPubsByGrpId(Long grpId) throws Exception {

    return grpPubsDao.getTodayUplaodGrpPubsByGrpId(grpId);
  }

  @Override
  public void sendGrpAddpubSendEmail(Map<String, Object> mailData, GrpPubs grpPubs) throws Exception {
    Long psnId = grpPubs.getCreatePsnId();
    // 通过PsnId获取人员id,姓名，邮箱语言、性别
    Person sender = personService.getPeronsForEmail(psnId);
    // 获取成果数据
    PubSnsPO pub = pubSnsDAO.get(grpPubs.getPubId());
    // 获取群组基本信息
    GrpBaseinfo grpBaseinfo = grpBaseInfoDao.get(grpPubs.getGrpId());
    if (grpBaseinfo == null) {
      return;
    }
    mailData.put("addpubCount", 1);// 成果数
    mailData.put("groupName", grpBaseinfo.getGrpName());// 群组信息
    Long grpId = grpBaseinfo.getGrpId();
    // 获取群组成员列表（收件人）并保存数据
    List<Long> receivePsnIds = grpMemberDao.getGrpMemberIdByGrpId(grpId);
    if (receivePsnIds != null && receivePsnIds.size() > 0) {
      for (Long receivePsnId : receivePsnIds) {
        // 给除自己外的成员发送
        if (!receivePsnId.toString().equals(sender.getPersonId().toString())) {
          // 获取人员id,姓名，邮箱语言、性别
          Person receiver = personService.getPeronsForEmail(receivePsnId);
          if (12 != grpBaseinfo.getGrpCategory()) {
            restSendGrpAddpubSendEmail(mailData, sender, receiver, pub, grpPubs, grpId);
          }
        }
      }

    }

  }


  /**
   * 调用rest接口发送群组添加成果邮件
   * 
   * @param mailData 定义构造邮件模版参数集
   * 
   * @param sender
   * @param receiver
   * @param pub
   * @param grpPubs
   * @param grpId
   */
  protected void restSendGrpAddpubSendEmail(Map<String, Object> mailData, Person sender, Person receiver, PubSnsPO pub,
      GrpPubs grpPubs, Long grpId) {
    // 定义接口接收的参数
    Map<String, String> paramData = new HashMap<String, String>();
    // 构造必需的参数
    MailOriginalDataInfo info = new MailOriginalDataInfo();
    String language = "";
    language = receiver.getEmailLanguageVersion();
    if (StringUtils.isBlank(language)) {
      language = LocaleContextHolder.getLocale().toString();
    }
    String receiverName = getPersonName(receiver, language);
    String senderName = getPersonName(sender, language);
    mailData.put("receiverName", receiverName);
    mailData.put("senderName", senderName);
    mailData.put("groupPubName", pub.getTitle());
    mailData.put("groupPubDesc", pub.getBriefDesc());
    Integer tempCode = 10076;
    info.setSenderPsnId(sender.getPersonId());
    info.setReceiverPsnId(receiver.getPersonId());
    info.setMsg("群组添加成果邮件");
    info.setReceiver(receiver.getEmail());
    info.setMailTemplateCode(tempCode);
    paramData.put("mailOriginalData", JacksonUtils.jsonObjectSerializerNoNull(info));
    // 跟踪链接
    // 项目成果分， 成果，文献
    String model = "pub";
    String modelMobile = "&showPrjPub=1";
    Integer catacory = grpBaseInfoDao.findGrpCatacory(grpId);
    if (catacory == 11) {
      if (grpPubs.getIsProjectPub() == 0) {
        // 不是项目成果
        model = "projectRef";
        modelMobile = "&showRefPub=1";
      }
    }
    // https://dev.scholarmate.com/groupweb/grpinfo/main?des3GrpId=JvUzHyT7%2BGLv0ao3qd%2BuaQ%3D%3D&model=pub
    String optUrl = scmSystemUtil.getSysDomain() + "/groupweb/grpinfo/main?des3GrpId="
        + ServiceUtil.encodeToDes3(grpId.toString()) + "&model=" + model;
    String optUrlMobile =
        domainMobile + "/grp/mobile/grppubmain?des3GrpId=" + ServiceUtil.encodeToDes3(grpId.toString()) + modelMobile;

    String optUrlAll = EmailCommonService.PC_OR_MB_TOKEN + "pc=" + optUrl + "&&mobile=" + optUrlMobile;
    // 构建群组成果短地址
    GrpPubIndexUrl grpPub = grpPubIndexUrlDao.findByGrpIdAndPubId(grpId, pub.getPubId());
    StringBuilder optUrl1 = new StringBuilder();
    optUrl1.append(scmSystemUtil.getSysDomain()).append("/");
    if (grpPub != null && StringUtils.isNotBlank(grpPub.getPubIndexUrl())) {
      optUrl1.append(ShortUrlConst.B_TYPE).append("/").append(grpPub.getPubIndexUrl());
    } else {
      optUrl1.append("pub/outside/details?des3PubId=").append(ServiceUtil.encodeToDes3(pub.getPubId().toString()));
    }
    // 构建人员信息短地址
    PsnProfileUrl psnProfileUrl = psnProfileUrlDao.find(sender.getPersonId());
    StringBuilder senderPersonUrl = new StringBuilder();
    senderPersonUrl.append(scmSystemUtil.getSysDomain()).append("/");
    if (psnProfileUrl != null && StringUtils.isNotBlank(psnProfileUrl.getPsnIndexUrl())) {
      senderPersonUrl.append(ShortUrlConst.P_TYPE).append("/").append(psnProfileUrl.getPsnIndexUrl());
    } else {
      senderPersonUrl.append("psnweb/outside/homepage?des3PsnId=")
          .append(Des3Utils.encodeToDes3(sender.getPersonId() + ""));
    }
    List<String> linkList = new ArrayList<String>();
    MailLinkInfo l1 = new MailLinkInfo();
    l1.setKey("domainUrl");
    l1.setUrl(scmSystemUtil.getSysDomain());
    l1.setUrlDesc("科研之友首页");
    linkList.add(JacksonUtils.jsonObjectSerializer(l1));
    MailLinkInfo l2 = new MailLinkInfo();
    l2.setKey("senderPersonUrl");
    l2.setUrl(senderPersonUrl.toString());
    l2.setUrlDesc("发件人主页链接");
    linkList.add(JacksonUtils.jsonObjectSerializer(l2));
    MailLinkInfo l3 = new MailLinkInfo();
    l3.setKey("groupPubUrl");
    l3.setUrl(optUrl1.toString());
    l3.setUrlDesc("群组成果详情链接");
    linkList.add(JacksonUtils.jsonObjectSerializer(l3));
    MailLinkInfo l4 = new MailLinkInfo();
    l4.setKey("groupAllPubUrl");
    l4.setUrl(optUrlAll);
    l4.setUrlDesc("群组所有成果链接");
    linkList.add(JacksonUtils.jsonObjectSerializer(l4));
    PubFullTextPO pubFulltext = pubFullTextDAO.getPubFullTextByPubId(pub.getPubId());
    if (pubFulltext != null && pubFulltext.getFileId() != null) {
      mailData.put("groupPubFullTextImg",
          scmSystemUtil.getSysDomain() + "/resscmwebsns/images_v5/images2016/file_img1.jpg");
    } else {
      mailData.put("groupPubFullTextImg",
          scmSystemUtil.getSysDomain() + "/resscmwebsns/images_v5/images2016/file_img.jpg");
    }
    mailData.put("groupPubAuthorNames", pub.getAuthorNames());
    // 主题参数，添加如下：
    List<String> subjectParamLinkList = new ArrayList<String>();
    subjectParamLinkList.add(senderName);
    subjectParamLinkList.add(mailData.get("groupName").toString());
    subjectParamLinkList.add(mailData.get("addpubCount").toString());
    mailData.put("linkList", JacksonUtils.listToJsonStr(linkList));
    mailData.put("subjectParamList", JacksonUtils.listToJsonStr(subjectParamLinkList));
    paramData.put("mailData", JacksonUtils.mapToJsonStr(mailData));
    // 调用接口发送邮件
    try {
      restTemplate.postForObject(this.sendRestfulUrl, paramData, Object.class);
    } catch (Exception e) {
      logger.error(
          "发送邮件数据出错，psnId=" + sender.getPersonId() + ",pubId=" + grpPubs.getPubId() + ",groupId=" + grpPubs.getGrpId(),
          e);
    }
  }

}
