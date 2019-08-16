package com.smate.web.group.service.grp.member;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

import com.smate.center.mail.connector.model.MailLinkInfo;
import com.smate.center.mail.connector.model.MailOriginalDataInfo;
import com.smate.center.mail.connector.service.MailHandleOriginalDataService;
import com.smate.core.base.consts.dao.ConstRegionDao;
import com.smate.core.base.consts.model.ConstRegion;
import com.smate.core.base.email.dao.MailInitDataDao;
import com.smate.core.base.email.model.MailInitData;
import com.smate.core.base.email.service.EmailCommonService;
import com.smate.core.base.email.service.MailInitDataService;
import com.smate.core.base.psn.dao.PsnProfileUrlDao;
import com.smate.core.base.psn.dao.PsnStatisticsDao;
import com.smate.core.base.psn.model.PsnProfileUrl;
import com.smate.core.base.psn.model.PsnStatistics;
import com.smate.core.base.statistics.PsnStatisticsUtils;
import com.smate.core.base.utils.constant.EmailConstants;
import com.smate.core.base.utils.constant.MsgConstants;
import com.smate.core.base.utils.constant.ShortUrlConst;
import com.smate.core.base.utils.dao.security.PersonDao;
import com.smate.core.base.utils.dao.security.RegisterTempDao;
import com.smate.core.base.utils.json.JacksonUtils;
import com.smate.core.base.utils.model.reg.RegisterTemp;
import com.smate.core.base.utils.model.security.Person;
import com.smate.core.base.utils.psninfo.PsnInfoUtils;
import com.smate.core.base.utils.security.Des3Utils;
import com.smate.core.base.utils.security.SecurityUtils;
import com.smate.core.base.utils.string.ExcelUtils;
import com.smate.core.base.utils.string.ServiceUtil;
import com.smate.core.base.utils.struts2.Struts2Utils;
import com.smate.web.group.action.grp.form.GrpMemberForm;
import com.smate.web.group.constant.grp.GrpConstant;
import com.smate.web.group.dao.group.psn.PsnCopartnerDao;
import com.smate.web.group.dao.grp.grpbase.GrpBaseInfoDao;
import com.smate.web.group.dao.grp.grpbase.GrpIndexUrlDao;
import com.smate.web.group.dao.grp.grpbase.GrpStatisticsDao;
import com.smate.web.group.dao.grp.member.GrpMemberDao;
import com.smate.web.group.dao.grp.member.GrpMemberRcmdDao;
import com.smate.web.group.dao.grp.member.GrpProposerDao;
import com.smate.web.group.dao.grp.member.RecentSelectedDao;
import com.smate.web.group.dao.grp.pub.GrpPubsDao;
import com.smate.web.group.model.group.psn.PsnCopartner;
import com.smate.web.group.model.grp.grpbase.GrpBaseinfo;
import com.smate.web.group.model.grp.grpbase.GrpIndexUrl;
import com.smate.web.group.model.grp.grpbase.GrpStatistics;
import com.smate.web.group.model.grp.member.GrpMember;
import com.smate.web.group.model.grp.member.GrpProposer;
import com.smate.web.group.model.grp.member.RecentSelected;

/**
 * 群组成员操作service实现类
 * 
 * @author zzx
 *
 */
@Service("grpMemberOptService")
@Transactional(rollbackFor = Exception.class)
public class GrpMemberOptServiceImpl implements GrpMemberOptService {
  protected Logger logger = LoggerFactory.getLogger(getClass());
  @Autowired
  private MailHandleOriginalDataService mailHandleOriginalDataService;
  @Autowired
  private MailInitDataService mailInitDataService;
  @Autowired
  private GrpMemberDao grpMemberDao;
  @Autowired
  private GrpProposerDao grpProposerDao;
  @Autowired
  private PsnProfileUrlDao psnProfileUrlDao;
  @Autowired
  private GrpMemberRcmdDao grpMemberRcmdDao;
  @Autowired
  private GrpBaseInfoDao grpBaseInfoDao;
  @Autowired
  private GrpStatisticsDao grpStatisticsDao;
  @Autowired
  private PersonDao personDao;
  @Autowired
  private GrpPubsDao grpPubsDao;
  @Autowired
  private RecentSelectedDao recentSelectedDao;
  @Autowired
  private PsnStatisticsDao psnStatisticsDao;
  @Autowired
  private GrpIndexUrlDao grpIndexUrlDao;
  @Autowired
  private RestTemplate restTemplate;
  @Value("${initOpen.restful.url}")
  private String openResfulUrl;
  @Value("${domainscm}")
  private String domainscm;
  @Value(value = "${sendEmail.restful.url}")
  private String sendRestfulUrl;
  @Autowired
  private MailInitDataDao mailInitDataDao;
  @Autowired
  private ConstRegionDao constRegionDao;
  @Autowired
  private RegisterTempDao registerTempDao;
  private static String HOME = "/dynweb/main";

  @Autowired
  private PsnCopartnerDao psnCopartnerDao;

  @Override
  public void setGrpMemberRole(GrpMemberForm form) {
    String updateRole = form.getUpdateRole();
    if (updateRole == null) {// 数据效验
      logger.error("修改群组成员角色updateRole为空，被修改人Id" + form.getTargetPsnId());
      return;
    }
    // 修改目标人员的群组角色
    grpMemberDao.updateRol(NumberUtils.toInt(updateRole), form.getTargetPsnId(), form.getGrpId());
    if ("1".equals(updateRole)) {// 如果是变更群组拥有者操作，则还有修改自己为普通成员
      grpBaseInfoDao.get(form.getGrpId()).setOwnerPsnId(form.getTargetPsnId());
      GrpMember member = grpMemberDao.getByPsnIdAndGrpId(form.getPsnId(), form.getGrpId());
      if (member != null) {
        member.setGrpRole(GrpConstant.GRP_ROLE_MEMBER);
        grpMemberDao.save(member);
      }
    }
  }

  @Override
  public void delGrpMember(GrpMemberForm form) {
    Integer delType = form.getDelType();
    if (delType != null) {// 数据效验
      // status:群组成员状态[01=正常,99=删除（被移除出群组）,98=删除（自动退出群组）]
      if (delType == GrpConstant.GRP_DEL_MEMBER_TYPE_B) {
        form.setTargetPsnId(form.getPsnId());
      }
      grpMemberDao.setStatusById(form.getTargetPsnId(), form.getGrpId(), form.getDelType().toString());
      // 删除相关成果
      Integer delPubcount = grpPubsDao.delGrpPubsForexitGrp(form.getTargetPsnId(), form.getGrpId());
      if (delPubcount != null && delPubcount > 0) {
        GrpStatistics statistics = grpStatisticsDao.get(form.getGrpId());
        if (statistics != null) {
          Integer sumPubs = statistics.getSumPubs();
          if (sumPubs != null && sumPubs != 0) {
            statistics.setSumPubs(sumPubs - delPubcount);
            grpStatisticsDao.save(statistics);
          }
        }

      }
      // 统计删除人的群组数量
      PsnStatistics psnStatistics = psnStatisticsDao.get(form.getTargetPsnId());
      if (psnStatistics != null) {
        psnStatistics.setGroupSum(psnStatistics.getGroupSum() == null ? 0 : (psnStatistics.getGroupSum() - 1));
        // 属性为null的保存为0
        PsnStatisticsUtils.buildZero(psnStatistics);
        psnStatisticsDao.save(psnStatistics);
      }
    } else {
      logger.error("移除群组成员角色delType参数不正确，被修改人Id" + form.getTargetPsnId());
    }

  }

  @Override
  public void iviteGrpApplication(GrpMemberForm form) {
    String targetdes3GrpId = form.getTargetdes3GrpId();
    Integer isAccept = form.getDisposeType();// 处理邀请操作[1=同意,0=忽略]
    if (StringUtils.isNotBlank(targetdes3GrpId) && (isAccept == 1 || isAccept == 0)) {// 数据效验
      String[] des3GrpIdArr = targetdes3GrpId.split(",");
      Long grpId = null;
      List<Long> grpIds = new ArrayList<Long>();// 后面更新群组人员统计用
      for (String des3GrpId : des3GrpIdArr) {
        if (StringUtils.isNotBlank(des3GrpId)) {
          grpId = Long.parseLong(Des3Utils.decodeFromDes3(des3GrpId));
          // （同意或者忽略） 修改grp_Proposer记录
          grpProposerDao.setAccept(form.getPsnId(), grpId, form.getPsnId(), isAccept, 2);
          if (isAccept == 1) {// 如果是同意操作，除了修改grp_Proposer记录，还有新添一条Grp_Member记录
            GrpMember g = new GrpMember();
            g.setGrpId(grpId);
            g.setPsnId(form.getPsnId());
            grpMemberDao.addGrpMember(g);
            this.updateGrpMemberCount(grpId);
            // 发邮件
            Person sender = personDao.get(form.getPsnId());
            GrpBaseinfo grpBaseinfo = grpBaseInfoDao.get(grpId);
            sendGrpNotityEmail(sender, grpBaseinfo, null, false);
            // 项目群组要计算项目合作者
            if ((grpBaseinfo.getGrpCategory() != null) && grpBaseinfo.getGrpCategory().equals(11)
                && StringUtils.isNotBlank(grpBaseinfo.getProjectNo())) {
              this.savePsnPrjCopartner(grpId, form.getPsnId());
            }
          }
          grpIds.add(grpId);
        }
      }
      int size = grpIds.size();
      if (size > 0) {
        PsnStatistics psnStatistics = psnStatisticsDao.get(form.getPsnId());
        if (psnStatistics != null) {
          psnStatistics.setGroupSum(psnStatistics.getGroupSum() == null ? size : (psnStatistics.getGroupSum() + size));
        }
      }
      form.setGrpIds(grpIds);
    }

  }

  @Override
  public void disposeGrpApplication(GrpMemberForm form) {
    String targetPsnIds = form.getTargetPsnIds();
    Integer isAccept = form.getDisposeType();// 处理人员申请操作[1=同意,0=忽略]
    if (StringUtils.isNotBlank(targetPsnIds) && (isAccept == 1 || isAccept == 0)) {// 数据效验
      String[] psnIds = targetPsnIds.split(",");
      for (String psnId : psnIds) {
        if (StringUtils.isNotBlank(psnId)) {
          Long id = Long.parseLong(Des3Utils.decodeFromDes3(psnId));
          if (id == null || id == 0L || grpMemberDao.isMember(id, form.getGrpId())) {// 如果已经是群组成员直接跳过
            continue;
          } else {
            // （同意或者忽略） 修改grp_Proposer记录
            grpProposerDao.setAccept(id, form.getGrpId(), form.getPsnId(), isAccept, 1);
            if (isAccept == 1) {// 如果是同意操作，除了修改grp_Proposer记录，还有新添一条Grp_Member记录
              GrpMember g = new GrpMember();
              g.setGrpId(form.getGrpId());
              g.setPsnId(id);
              grpMemberDao.addGrpMember(g);
              PsnStatistics psnStatistics = psnStatisticsDao.get(id);
              if (psnStatistics != null) {
                psnStatistics.setGroupSum(psnStatistics.getGroupSum() == null ? 1 : (psnStatistics.getGroupSum() + 1));
              }
              Person sender = personDao.get(id);
              GrpBaseinfo grpBaseinfo = grpBaseInfoDao.get(form.getGrpId());
              sendGrpNotityEmail(sender, grpBaseinfo, form.getPsnId(), true);
              // 项目群组要计算项目合作者
              if ((grpBaseinfo.getGrpCategory() != null) && grpBaseinfo.getGrpCategory().equals(11)
                  && StringUtils.isNotBlank(grpBaseinfo.getProjectNo())) {
                this.savePsnPrjCopartner(form.getGrpId(), id);
              }
            }
          }
        }
      }
    } else {
    }

  }

  private void savePsnPrjCopartner(Long grpId, Long psnId) {
    // 根据项目群组找到合作者
    List<Long> coPsnIdList = grpMemberDao.getPrjCoBygrpId(grpId, psnId);
    for (Long coPsnId : coPsnIdList) {
      // 保存项目合作者记录
      PsnCopartner psnCopartner = psnCopartnerDao.getPsnCopartner(psnId, coPsnId, grpId, 2);
      if (psnCopartner == null) {
        psnCopartner = new PsnCopartner(psnId, coPsnId, grpId, 2);
      }
      psnCopartnerDao.saveOrUpdate(psnCopartner);
      // 把当前人推给加入了当前群组的人当合作者
      PsnCopartner psnCopartner2 = psnCopartnerDao.getPsnCopartner(coPsnId, psnId, grpId, 2);
      if (psnCopartner2 == null) {
        psnCopartner2 = new PsnCopartner(coPsnId, psnId, grpId, 2);
      }
      psnCopartnerDao.saveOrUpdate(psnCopartner2);
    }

  }

  /**
   * 
   * @param sender 发送者
   * @param baseinfo 群组信息
   * @param operator 管理员操作者，存在不需要给这个人发送消息
   * @param needSendMe 是否需要给自己发邮件
   */
  // 发送群组通知邮件
  public void sendGrpNotityEmail(Person sender, GrpBaseinfo baseinfo, Long operator, Boolean needSendMe) {

    List<Long> grpManagers = grpMemberDao.getGrpManagers(baseinfo.getGrpId());

    try {
      // 管理员发邮件
      for (Long psnId : grpManagers) {
        if ((operator != null && operator.longValue() == psnId.longValue())) {
          continue;
        }
        Person receiver = personDao.get(psnId);
        restSendGrpManageNotityEmail(baseinfo, sender, receiver);
      }
      // 给自己发邮件
      if (needSendMe) {
        restSendGrpNotityEmail(baseinfo, sender, sender);
      }
    } catch (Exception e) {
      e.printStackTrace();
      logger.error("发送群组通知邮件异常 grpId=" + baseinfo.getGrpId() + " psnId=" + sender.getPersonId());
    }
  }

  /**
   * 调用接口发送同意加入群组通知自己的邮件
   * 
   * @param baseinfo 群组基本信息
   * @param sender 发件人
   * @param receiver 收件人 自己
   * @throws Exception
   */
  private void restSendGrpNotityEmail(GrpBaseinfo baseinfo, Person sender, Person receiver) throws Exception {
    // 群组通知
    if (sender == null || receiver == null || baseinfo == null) {
      throw new Exception("构建群组删除回复，邮件对象为空" + this.getClass().getName());
    }
    // 定义接口接收的参数
    Map<String, String> paramData = new HashMap<String, String>();
    // 定义构造邮件模版参数集
    Map<String, Object> mailData = new HashMap<String, Object>();
    String language = "";
    language = receiver.getEmailLanguageVersion();
    if (StringUtils.isBlank(language)) {
      language = LocaleContextHolder.getLocale().toString();
    }
    String personName = PsnInfoUtils.getPersonName(sender, language);
    // 跟踪链接 根据key放置到模板中 所有的链接不再需要放到mailData中
    GrpIndexUrl grpIndexUrl = grpIndexUrlDao.get(baseinfo.getGrpId());
    String viewGroupUrl = grpIndexUrl != null ? this.domainscm + "/G/" + grpIndexUrl.getGrpIndexUrl()
        : this.domainscm + "/groupweb/grpinfo/main?des3GrpId=" + Des3Utils.encodeToDes3(baseinfo.getGrpId().toString());
    List<String> linkList = new ArrayList<String>();
    MailLinkInfo l1 = new MailLinkInfo();
    l1.setKey("domainUrl");
    l1.setUrl(domainscm);
    l1.setUrlDesc("科研之友首页");
    linkList.add(JacksonUtils.jsonObjectSerializer(l1));
    MailLinkInfo l2 = new MailLinkInfo();
    l2.setKey("viewGroupUrl");
    l2.setUrl(viewGroupUrl);
    l2.setUrlDesc("查看群组链接");
    linkList.add(JacksonUtils.jsonObjectSerializer(l2));
    mailData.put("linkList", JacksonUtils.listToJsonStr(linkList));
    mailData.put("groupName", baseinfo.getGrpName());
    mailData.put("groupDesc", baseinfo.getGrpDescription());// 群组列表
    mailData.put("psnName", personName);
    // 主题参数，添加如下：
    List<String> subjectParamLinkList = new ArrayList<String>();
    subjectParamLinkList.add(personName);
    subjectParamLinkList.add(baseinfo.getGrpName());
    mailData.put("subjectParamList", JacksonUtils.listToJsonStr(subjectParamLinkList));
    // 构造必需的参数
    MailOriginalDataInfo info = new MailOriginalDataInfo();
    Integer tempcode = 10020;
    info.setSenderPsnId(0L);
    info.setReceiverPsnId(receiver.getPersonId());
    info.setReceiver(receiver.getEmail());
    info.setMsg("发送同意加入群组通知自己邮件");
    info.setMailTemplateCode(tempcode);
    paramData.put("mailOriginalData", JacksonUtils.jsonObjectSerializerNoNull(info));
    paramData.put("mailData", JacksonUtils.mapToJsonStr(mailData));
    restTemplate.postForObject(this.sendRestfulUrl, paramData, Object.class);
  }

  /**
   * 调用接口发送同意加入群组通知管理员邮件
   * 
   * @param baseinfo 群组基本信息
   * @param sender 发件人
   * @param receiver 收件人
   * @throws Exception
   */
  public void restSendGrpManageNotityEmail(GrpBaseinfo baseinfo, Person sender, Person receiver) throws Exception {
    // 群组通知
    if (sender == null || receiver == null || baseinfo == null) {
      throw new Exception("构建群组删除回复，邮件对象为空" + this.getClass().getName());
    }
    // 定义接口接收的参数
    Map<String, String> paramData = new HashMap<String, String>();
    // 定义构造邮件模版参数集
    Map<String, Object> mailData = new HashMap<String, Object>();
    String language = "";
    language = receiver.getEmailLanguageVersion();
    if (StringUtils.isBlank(language)) {
      language = LocaleContextHolder.getLocale().toString();
    }
    // 跟踪链接 根据key放置到模板中 所有的链接不再需要放到mailData中
    PsnProfileUrl psnProfileUrl = psnProfileUrlDao.get(sender.getPersonId());
    String viewPsnUrl = "";
    if (psnProfileUrl != null && StringUtils.isNotBlank(psnProfileUrl.getPsnIndexUrl())) {
      viewPsnUrl = this.domainscm + "/P/" + psnProfileUrl.getPsnIndexUrl();
    } else {
      // http://dev.scholarmate.com/psnweb/homepage/show?des3PsnId=gdC9pv0cs%252BvIFzRJNrcXAA%253D%253D
      viewPsnUrl =
          this.domainscm + "/psnweb/homepage/show?des3PsnId=" + Des3Utils.encodeToDes3(sender.getPersonId().toString());
    }
    GrpIndexUrl grpIndexUrl = grpIndexUrlDao.get(baseinfo.getGrpId());
    String viewGroupUrl = grpIndexUrl != null ? this.domainscm + "/G/" + grpIndexUrl.getGrpIndexUrl()
        : this.domainscm + "/groupweb/grpinfo/main?des3GrpId=" + Des3Utils.encodeToDes3(baseinfo.getGrpId().toString());
    List<String> linkList = new ArrayList<String>();
    MailLinkInfo l1 = new MailLinkInfo();
    l1.setKey("domainUrl");
    l1.setUrl(domainscm);
    l1.setUrlDesc("科研之友首页");
    linkList.add(JacksonUtils.jsonObjectSerializer(l1));
    MailLinkInfo l2 = new MailLinkInfo();
    l2.setKey("viewPsnUrl");
    l2.setUrl(viewPsnUrl);
    l2.setUrlDesc("查看人员链接");
    linkList.add(JacksonUtils.jsonObjectSerializer(l2));
    MailLinkInfo l3 = new MailLinkInfo();
    l3.setKey("viewGroupUrl");
    l3.setUrl(viewGroupUrl);
    l3.setUrlDesc("查看群组链接");
    linkList.add(JacksonUtils.jsonObjectSerializer(l3));
    mailData.put("linkList", JacksonUtils.listToJsonStr(linkList));
    /// resmod/smate-pc/img/logo_psndefault.png
    mailData.put("avatars",
        StringUtils.isNotBlank(sender.getAvatars()) ? sender.getAvatars() : "/resmod/smate-pc/img/logo_psndefault.png");
    mailData.put("adminPsnName", PsnInfoUtils.getPersonName(receiver, language));
    mailData.put("viewName", PsnInfoUtils.getPersonName(sender, language));
    mailData.put("groupName", baseinfo.getGrpName());
    // 主题参数，添加如下：
    List<String> subjectParamLinkList = new ArrayList<String>();
    subjectParamLinkList.add(mailData.get("viewName").toString());
    mailData.put("subjectParamList", JacksonUtils.listToJsonStr(subjectParamLinkList));
    // 构造必需的参数
    MailOriginalDataInfo info = new MailOriginalDataInfo();
    Integer tempcode = 10060;
    info.setSenderPsnId(sender.getPersonId());
    info.setReceiverPsnId(receiver.getPersonId());
    info.setReceiver(receiver.getEmail());
    info.setMsg("发送同意加入群组通知管理员邮件");
    info.setMailTemplateCode(tempcode);
    paramData.put("mailOriginalData", JacksonUtils.jsonObjectSerializerNoNull(info));
    paramData.put("mailData", JacksonUtils.mapToJsonStr(mailData));
    restTemplate.postForObject(this.sendRestfulUrl, paramData, Object.class);
  }


  @Override
  public void invitedJoinGrp(GrpMemberForm form) {
    String targetPsnIds = form.getTargetPsnIds();
    if (StringUtils.isNotBlank(targetPsnIds)) {// 数据效验
      String[] psnIds = targetPsnIds.split(",");
      // 发邮件需要的参数
      // String grpName =
      // grpBaseInfoDao.getGrpNameByGrpId(form.getGrpId());
      // Person person2 = personDao.getPersonName(form.getPsnId());
      for (String psnId : psnIds) {
        Long id = Long.parseLong(Des3Utils.decodeFromDes3(psnId));
        form.setReveiverIds(form.getReveiverIds() + id + ",");
        if (id == null || id == 0L || grpMemberDao.isMember(id, form.getGrpId())) {// 如果已经是群组成员直接跳过
          // continue;
        } else {// 无论是不是直接加入群组，都往申请成员关系表插入一条记录
          GrpProposer gp = new GrpProposer();
          gp.setGrpId(form.getGrpId());
          gp.setPsnId(id);
          gp.setType(2);
          gp.setIsAccept(2);
          gp.setInviterId(form.getPsnId());
          grpProposerDao.addGrpProposer(gp);
          // 发送邀请加入群组的邮件
          // sendInviteAttendGroupMail(id, form);
          try {
            newSendInviteAttendGroupMail(id, form);
          } catch (Exception e) {
          }
        }
        if (form.getIsReferrer().intValue() == 1) {// 如果是从推荐库邀请的，那么要记录推荐库的邀请记录
          grpMemberRcmdDao.setAccept(id, form.getGrpId(), 1);
        }
      }
      // 发送邀请站内信
      form.setMsgType(MsgConstants.MSG_TYPE_ADD_GRP_INVITE);
      sendMsg(form);
    }
  }

  private void newSendInviteAttendGroupMail(Long personId, GrpMemberForm form) throws Exception {
    Long currentUserId = SecurityUtils.getCurrentUserId();
    Person sendPsn = personDao.getPersonName(currentUserId);// 发出邀请的人
    Person invitePsn = personDao.get(personId);// 被邀请加入群组的人
    String languageVersion = invitePsn.getEmailLanguageVersion();
    PsnProfileUrl psnProfileurl = psnProfileUrlDao.get(sendPsn.getPersonId());
    String sendPsnUrl = domainscm + "/" + ShortUrlConst.P_TYPE + "/" + psnProfileurl.getPsnIndexUrl();
    GrpIndexUrl grpIndexUrl = grpIndexUrlDao.get(form.getGrpId());
    String viewUrl = domainscm + "/" + ShortUrlConst.G_TYPE + "/" + grpIndexUrl.getGrpIndexUrl();
    String operateUrl = domainscm + "/groupweb/mygrp/main?model=myGrp";
    // 定义接口接收的参数
    Map<String, String> paramData = new HashMap<String, String>();
    // 定义构造邮件模版参数集
    Map<String, String> mailData = new HashMap<String, String>();
    // 构造必需的参数
    String email = invitePsn.getEmail();
    Integer templateCode = 10097;
    String msg = "邀请站内人员加入群组";
    mailHandleOriginalDataService.buildNecessaryParam(email, currentUserId, personId, templateCode, msg, paramData);

    // 定义要跟踪的链接集，系统会生成短地址并跟踪访问记录
    List<String> linkList = new ArrayList<String>();

    MailLinkInfo l1 = new MailLinkInfo();
    l1.setKey("domainUrl");
    l1.setUrl(domainscm);
    l1.setUrlDesc("科研之友首页");
    linkList.add(JacksonUtils.jsonObjectSerializer(l1));

    MailLinkInfo l2 = new MailLinkInfo();
    l2.setKey("inviteUrl");
    l2.setUrl(operateUrl);
    l2.setUrlDesc("同意加入群组链接");
    linkList.add(JacksonUtils.jsonObjectSerializer(l2));

    MailLinkInfo l3 = new MailLinkInfo();
    l3.setKey("groupViewUrl");
    l3.setUrl(viewUrl);
    l3.setUrlDesc("群组详情");
    linkList.add(JacksonUtils.jsonObjectSerializer(l3));

    MailLinkInfo l4 = new MailLinkInfo();
    l4.setKey("psnUrl");
    l4.setUrl(sendPsnUrl);
    l4.setUrlDesc("邀请人的详情链接");
    linkList.add(JacksonUtils.jsonObjectSerializer(l4));

    mailData.put("linkList", JacksonUtils.listToJsonStr(linkList));
    // 如果需要主题参数，添加如下：
    List<String> subjectParamLinkList = new ArrayList<String>();
    String sendPsnName = "";
    String invitePsnName = "";
    String grpName = grpBaseInfoDao.getGrpNameByGrpId(form.getGrpId());// 群组名称
    if ("zh_CN".equals(languageVersion)) {
      sendPsnName = sendPsn.getZhName();
      invitePsnName = invitePsn.getZhName();
    } else {
      sendPsnName = sendPsn.getEnName();
      invitePsnName = invitePsn.getEnName();
    }
    subjectParamLinkList.add(sendPsnName);
    subjectParamLinkList.add(grpName);
    mailData.put("subjectParamList", JacksonUtils.listToJsonStr(subjectParamLinkList));

    // TODO 邮件模版需要的其他参数可以继续往mailData添加,如：
    mailData.put("invitePsnName", invitePsnName);
    mailData.put("psnName", sendPsnName);
    mailData.put("invitePsnId", personId.toString());
    mailData.put("groupName", grpName);
    paramData.put("mailData", JacksonUtils.mapToJsonStr(mailData));

    Map<String, String> resutlMap = mailHandleOriginalDataService.doHandle(paramData);
    if ("success".equals(resutlMap.get("result"))) {
      // 构造邮件成功
    } else {
      // 构造邮件失败
      logger.error(resutlMap.get("msg"));
    }

  }

  /**
   * 邀请加入群组
   * 
   * @param psnId
   * @param form
   */
  public void sendInviteAttendGroupMail(Long psnId, GrpMemberForm form) {
    HashMap<String, Object> emailMap = new HashMap<String, Object>();
    Person sendPsn = personDao.getPersonName(SecurityUtils.getCurrentUserId());// 发出邀请的人
    Person invitePsn = personDao.get(psnId);// 被邀请加入群组的人
    String sendPsnName = "";// 发出邀请的人名
    String subject = "";// 邮件主题
    String mailTemplate = "";// 邮件模板
    String grpName = grpBaseInfoDao.getGrpNameByGrpId(form.getGrpId());// 群组名称
    String languageVersion = invitePsn.getEmailLanguageVersion();
    if (StringUtils.isBlank(languageVersion)) {
      languageVersion = LocaleContextHolder.getLocale().toString();
    }
    if ("zh_CN".equals(languageVersion)) {
      sendPsnName =
          StringUtils.isEmpty(sendPsn.getName()) ? sendPsn.getFirstName() + sendPsn.getLastName() : sendPsn.getName();
      subject = sendPsnName + "邀请您加入群组" + grpName;
      mailTemplate = "Invite_Attend_Group_Template_zh_CN.ftl";

    } else {
      sendPsnName = sendPsn.getFirstName() + " " + sendPsn.getLastName();
      if (StringUtils.isBlank(sendPsn.getFirstName()) || StringUtils.isBlank(sendPsn.getLastName())) {
        sendPsnName = sendPsn.getName();
      }
      subject = sendPsnName + " has invited you to join group " + grpName;
      mailTemplate = "Invite_Attend_Group_Template_en_US.ftl";
    }
    GrpIndexUrl grpIndexUrl = grpIndexUrlDao.get(form.getGrpId());
    String viewUrl = "";
    if (grpIndexUrl != null && StringUtils.isNotBlank(grpIndexUrl.getGrpIndexUrl())) {
      viewUrl = domainscm + "/" + ShortUrlConst.G_TYPE + "/" + grpIndexUrl.getGrpIndexUrl();
    }
    PsnProfileUrl psnProfileurl = psnProfileUrlDao.get(sendPsn.getPersonId());
    String sendPsnUrl = "";
    if (psnProfileurl != null && StringUtils.isNotBlank(psnProfileurl.getPsnIndexUrl())) {
      sendPsnUrl = domainscm + "/" + ShortUrlConst.P_TYPE + "/" + psnProfileurl.getPsnIndexUrl();
    }
    String operateUrl = domainscm + "/groupweb/mygrp/main?model=myGrp";
    emailMap.put("inviteUrl", operateUrl);
    emailMap.put("groupViewUrl", viewUrl);
    emailMap.put("domainUrl", domainscm);
    emailMap.put("psnName", sendPsnName);
    emailMap.put("psnUrl", sendPsnUrl);
    emailMap.put("invitePsnId", psnId);
    emailMap.put("groupName", grpName);
    emailMap.put(EmailConstants.EMAIL_SUBJECT_KEY, subject);
    emailMap.put(EmailConstants.EMAIL_RECEIVEEMAIL_KEY, invitePsn.getEmail());// 收件人邮箱
    emailMap.put(EmailConstants.EMAIL_RECEIVE_PSNID_KEY, invitePsn.getPersonId());// 收件人id
    emailMap.put(EmailConstants.EMAIL_SENDER_PSNID_KEY, sendPsn.getPersonId());// 发件人id
    emailMap.put(EmailConstants.EMAIL_TEMPLATE_KEY, mailTemplate);// 邮件模板名称
    try {
      mailInitDataService.saveMailInitData(emailMap);
    } catch (Exception e) {
      logger.info("保存邀请加入群组邮件初始化信息出错,群组id是" + form.getGrpId() + "被邀请加入群组的人员id是：" + psnId, e);
    }
  }

  @Override
  public void applyJoinGrp(GrpMemberForm form) {
    // 获取群组公开类型
    String openType = grpBaseInfoDao.getGrpOpenType(form.getGrpId());
    Integer grpCategory = grpBaseInfoDao.getGrpCatetory(form.getGrpId());
    if (form.getPsnId() == null || form.getPsnId() == 0L || grpMemberDao.isMember(form.getPsnId(), form.getGrpId())) {// 如果已经是群组成员直接跳过
    } else {// 无论是不是直接加入群组，都往申请成员关系表插入一条记录
      GrpProposer gp = new GrpProposer();
      gp.setGrpId(form.getGrpId());
      gp.setPsnId(form.getPsnId());
      gp.setType(1);

      if ("O".equals(openType) || (grpCategory != null) && grpCategory.equals(12)) {
        gp.setIsAccept(1);
        GrpMember gm = new GrpMember();
        gm.setGrpId(form.getGrpId());
        gm.setPsnId(form.getPsnId());
        grpMemberDao.addGrpMember(gm);
        PsnStatistics psnStatistics = psnStatisticsDao.get(form.getPsnId());
        if (psnStatistics != null) {
          psnStatistics.setGroupSum(psnStatistics.getGroupSum() == null ? 1 : (psnStatistics.getGroupSum() + 1));
        }
        // 如果是直接申请加入，则要清空被邀请记录
        GrpProposer grpProposer = grpProposerDao.getGrpProposer(form.getPsnId(), form.getGrpId(), 2);
        if (grpProposer != null) {
          grpProposer.setIsAccept(1);
          grpProposerDao.save(grpProposer);
        }

        Person sender = personDao.get(form.getPsnId());
        GrpBaseinfo grpBaseinfo = grpBaseInfoDao.get(form.getGrpId());
        sendGrpNotityEmail(sender, grpBaseinfo, null, false);
        // 加入成功标识
        form.setAddSuccess(true);

      } else if ("H".equals(openType) || "P".equals(openType)) {
        gp.setIsAccept(2);
        // 给管理员以上级别的发一条站内信 --非公开的要发站内信
        form.setMsgType(MsgConstants.MSG_TYPE_ADD_GRP_REQUEST);
        sendMsg(form);
      }
      grpProposerDao.addGrpProposer(gp);
      // 发送请求加入群组的邮件
      sendMailForApplyJoinGrp(form);
    }
  }

  @Override
  public void sendMsg(GrpMemberForm form) {
    try {
      // 调open接口发送消息
      Map<String, Object> map = buildSendMsgParam(form);
      Object obj = restTemplate.postForObject(this.openResfulUrl, map, Object.class);
      return;
    } catch (Exception e) {
      logger.error("发送站内信异常：" + e.toString());
    }

  }

  /**
   * 构建发消息需要的参数
   * 
   * @param form
   * @return
   */
  private Map<String, Object> buildSendMsgParam(GrpMemberForm form) throws Exception {
    Map<String, Object> map = new HashMap<String, Object>();
    Map<String, Object> dataMap = new HashMap<String, Object>();
    map.put("openid", "99999999");
    map.put("token", "00000000msg77msg");
    dataMap.put(MsgConstants.MSG_TYPE, form.getMsgType());
    dataMap.put(MsgConstants.MSG_SENDER_ID, form.getPsnId());
    // 构建参数
    buildDataMapForSendMsg(form, dataMap);

    map.put("data", JacksonUtils.mapToJsonStr(dataMap));
    return map;
  }

  /**
   * 构建接收人，不同站内信的接收人不同 群组中的角色[1=群组拥有者,2=管理员, 3=组员]
   * 
   * @return
   */
  private void buildDataMapForSendMsg(GrpMemberForm form, Map<String, Object> dataMap) {
    String receiverIds = "";
    List<Integer> roleList = new ArrayList<Integer>();
    if (MsgConstants.MSG_TYPE_ADD_GRP_REQUEST.equals(form.getMsgType())) {
      roleList.add(1);
      roleList.add(2);
      List<Long> receiverIdList = grpMemberDao.findManagerPsnIdByGrpId(form.getGrpId(), roleList);
      for (Long psnId : receiverIdList) {
        receiverIds += psnId.toString() + ",";
      }
      dataMap.put(MsgConstants.MSG_REQUEST_GRP_ID, form.getGrpId());
    } else if (MsgConstants.MSG_TYPE_ADD_GRP_INVITE.equals(form.getMsgType())) {
      receiverIds = form.getReveiverIds();
      dataMap.put(MsgConstants.MSG_RCMD_GRP_ID, form.getGrpId());
    }
    dataMap.put(MsgConstants.MSG_RECEIVER_IDS, receiverIds);
  }

  /**
   * 请求加入群组
   * 
   * @param form
   */
  public void sendMailForApplyJoinGrp(GrpMemberForm form) {
    Person sender = personDao.getPersonName(form.getPsnId());// 发出请求加入群组的人
    List<Long> grpManagerIds = grpMemberDao.getGrpManagers(form.getGrpId());// 群组管理员
    for (Long psnId : grpManagerIds) {
      String grpName = grpBaseInfoDao.getGrpNameByGrpId(form.getGrpId());// 群组名称
      HashMap<String, Object> paramData = new HashMap<String, Object>();
      // 定义构造邮件模版参数集
      Map<String, String> mailData = new HashMap<String, String>();
      // 构造必需的参数
      MailOriginalDataInfo info = new MailOriginalDataInfo();
      Person receiver = personDao.get(psnId);
      String language = receiver.getEmailLanguageVersion();
      if (StringUtils.isBlank(language)) {
        language = LocaleContextHolder.getLocale().toString();
      }
      String operateUrl = domainscm + "/groupweb/grpinfo/main?ispending=1&des3GrpId="
          + ServiceUtil.encodeToDes3(form.getGrpId().toString()) + "&model=member";
      GrpIndexUrl grpIndexUrl = grpIndexUrlDao.get(form.getGrpId());
      String viewUrl = "";
      if (grpIndexUrl != null && StringUtils.isNotBlank(grpIndexUrl.getGrpIndexUrl())) {
        viewUrl = domainscm + "/" + ShortUrlConst.G_TYPE + "/" + grpIndexUrl.getGrpIndexUrl();
      }
      String psnUrl = "";
      PsnProfileUrl psnProfileUrl = psnProfileUrlDao.get(sender.getPersonId());
      if (psnProfileUrl != null && StringUtils.isNotBlank(psnProfileUrl.getPsnIndexUrl())) {
        psnUrl = domainscm + "/" + ShortUrlConst.P_TYPE + "/" + psnProfileUrl.getPsnIndexUrl();
      }
      Integer templateCode = 10019;
      info.setReceiver(receiver.getEmail());// 接收邮箱
      info.setSenderPsnId(sender.getPersonId());// 发送人psnId，0=系统邮件
      info.setReceiverPsnId(receiver.getPersonId());// 接收人psnId，0=非科研之友用户
      info.setMailTemplateCode(templateCode);// 模版标识，参考V_MAIL_TEMPLATE
      info.setMsg("申请加入群组");// 描述
      paramData.put("mailOriginalData", JacksonUtils.jsonObjectSerializer(info));
      // 定义要跟踪的链接集，系统会生成短地址并跟踪访问记录
      List<String> linkList = new ArrayList<String>();
      MailLinkInfo l1 = new MailLinkInfo();
      l1.setKey("domainUrl");
      l1.setUrl(domainscm);
      l1.setUrlDesc("科研之友主页");
      MailLinkInfo l2 = new MailLinkInfo();
      l1.setKey("viewInvitePsnUrl");
      l1.setUrl(psnUrl);
      l1.setUrlDesc("发件人主页");
      MailLinkInfo l3 = new MailLinkInfo();
      l1.setKey("viewUrl");
      l1.setUrl(viewUrl);
      l1.setUrlDesc("群组详情url");
      MailLinkInfo l4 = new MailLinkInfo();
      l1.setKey("operateUrl");
      l1.setUrl(operateUrl);
      l1.setUrlDesc("同意加入群组url");
      linkList.add(JacksonUtils.jsonObjectSerializer(l1));
      linkList.add(JacksonUtils.jsonObjectSerializer(l2));
      linkList.add(JacksonUtils.jsonObjectSerializer(l3));
      linkList.add(JacksonUtils.jsonObjectSerializer(l4));
      mailData.put("linkList", JacksonUtils.listToJsonStr(linkList));
      mailData.put("adminPsnName", PsnInfoUtils.getPersonName(receiver, language));
      mailData.put("psnName", PsnInfoUtils.getPersonName(sender, language));
      mailData.put("viewUrl", viewUrl); // 群组url
      mailData.put("invitePsnId", receiver.getPersonId().toString());
      mailData.put("groupName", grpName);
      mailData.put("viewInvitePsnUrl", psnUrl);
      mailData.put("operateUrl", operateUrl);
      List<String> subjectParamLinkList = new ArrayList<String>();
      subjectParamLinkList.add(PsnInfoUtils.getPersonName(sender, language));
      subjectParamLinkList.add(grpName);
      mailData.put("subjectParamList", JacksonUtils.listToJsonStr(subjectParamLinkList));
      paramData.put("mailData", JacksonUtils.mapToJsonStr(mailData));
      // 保存邮件初始信息
      restTemplate.postForObject(this.sendRestfulUrl, paramData, Object.class);
    }
  }

  @Override
  public void sendMessageForGrpInvited(GrpMemberForm form) {
    // TODO Auto-generated method stub

  }

  @Override
  public void sendMessageForGrpApply(GrpMemberForm form) {
    // TODO Auto-generated method stub

  }

  @Override
  public void updateVisitDate(Long psnId, Long grpId) {
    GrpMember grpMember = grpMemberDao.getByPsnIdAndGrpId(psnId, grpId);
    if (grpMember != null) {
      grpMember.setLastVisitDate(new Date());
      grpMemberDao.save(grpMember);
    }
  }

  @Override
  public void recordMemberRcmdAccept(Long recommendPsnId, Long grpId, Integer isAccept) {
    // grpMemberRcmdDao.setAccept(recommendPsnId, grpId, isAccept);

  }

  @Override
  public void updateGrpMemberCount(Long grpId) {
    if (grpId != null) {
      Long memberCount = grpMemberDao.getGrpMemberCount(grpId);
      Long proposerCount = grpProposerDao.getPendingApproval(grpId);
      GrpStatistics statistics = grpStatisticsDao.get(grpId);
      if (statistics == null) {
        statistics = new GrpStatistics();
        statistics.setGrpId(grpId);
        statistics.setSumMember(memberCount.intValue());
        statistics.setSumToMember(proposerCount.intValue());
      } else {
        statistics.setSumMember(memberCount.intValue());
        statistics.setSumToMember(proposerCount.intValue());
      }
      grpStatisticsDao.save(statistics);
    }
  }

  @Override
  public void invitedJoinGrpByEmail(GrpMemberForm form) {
    String emails = form.getEmails();
    // 获取群组公开类型
    String openType = grpBaseInfoDao.getGrpOpenType(form.getGrpId());
    if (StringUtils.isNotBlank(emails)) {
      // 数据效验
      String[] emailsArr = emails.split(",");
      // 发邮件需要的参数
      for (String email : emailsArr) {
        if (StringUtils.isBlank(email)) {
          // continue;
        } else {
          List<Person> personList = personDao.getPersonByEmail(email);
          if (personList != null && personList.size() > 0) {
            /*
             * for (Person p : personList) {
             * 
             * }
             */
            // 修改为只对匹配到的第一个人操作
            Person p = personList.get(0);
            // 如果已经是群组成员直接跳过
            if (!grpMemberDao.isGrpMember(p.getPersonId(), form.getGrpId())) {
              form.setReveiverIds(form.getReveiverIds() + p.getPersonId() + ",");
              // 无论是不是直接加入群组，都往申请成员关系表插入一条记录
              GrpProposer gp = new GrpProposer();
              gp.setGrpId(form.getGrpId());
              gp.setPsnId(p.getPersonId());
              gp.setType(2);
              gp.setInviterId(form.getPsnId());
              gp.setIsAccept(2);
              grpProposerDao.addGrpProposer(gp);
              // TODO 发送邮件
              // sendInviteAttendGroupMail(personList.get(0).getPersonId(),
              // form);
              try {
                newSendInviteAttendGroupMail(personList.get(0).getPersonId(), form);
              } catch (Exception e) {
                logger.error("发送群组邀请站内人员出错", e);
              }
            }
            // 发送邮件邀请的站内信
            form.setMsgType(MsgConstants.MSG_TYPE_ADD_GRP_INVITE);
            sendMsg(form);
          } else {// 邀请站外人员
            try {
              // sendInviteGrpMailForTourist(form, email);
              newsendInviteGrpMailForTourist(form, email);
            } catch (Exception e) {
              logger.error("发送群组邀请站外人员出错" + email, e);
            }
          }
        }
      }
    }
  }

  private void newsendInviteGrpMailForTourist(GrpMemberForm form, String email) throws Exception {
    Long currentUserId = SecurityUtils.getCurrentUserId();
    Person sendPsn = personDao.get(currentUserId);// 发出邀请的人
    String languageVersion = sendPsn.getEmailLanguageVersion();
    if (StringUtils.isBlank(languageVersion)) {
      languageVersion = LocaleContextHolder.getLocale().toString();
    }
    PsnProfileUrl psnProfileurl = psnProfileUrlDao.get(currentUserId);
    String sendPsnUrl = domainscm + "/" + ShortUrlConst.P_TYPE + "/" + psnProfileurl.getPsnIndexUrl();
    GrpIndexUrl grpIndexUrl = grpIndexUrlDao.get(form.getGrpId());
    String viewUrl = domainscm + "/" + ShortUrlConst.G_TYPE + "/" + grpIndexUrl.getGrpIndexUrl();

    // 定义接口接收的参数
    Map<String, String> paramData = new HashMap<String, String>();
    // 定义构造邮件模版参数集
    Map<String, String> mailData = new HashMap<String, String>();
    // 构造必需的参数
    Long psnId = 0L;
    Integer templateCode = 10098;
    String msg = "邀请站外人员加入群组";
    mailHandleOriginalDataService.buildNecessaryParam(email, currentUserId, psnId, templateCode, msg, paramData);

    // 定义要跟踪的链接集，系统会生成短地址并跟踪访问记录
    List<String> linkList = new ArrayList<String>();

    MailLinkInfo l1 = new MailLinkInfo();
    l1.setKey("domainUrl");
    l1.setUrl(domainscm);
    l1.setUrlDesc("科研之友首页");
    linkList.add(JacksonUtils.jsonObjectSerializer(l1));

    MailLinkInfo l2 = new MailLinkInfo();
    l2.setKey("groupViewUrl");
    l2.setUrl(viewUrl);
    l2.setUrlDesc("群组详情");
    linkList.add(JacksonUtils.jsonObjectSerializer(l2));

    MailLinkInfo l3 = new MailLinkInfo();
    l3.setKey("inviteUrl");
    String inviteUrl = buildInviteUrl(email, form.getGrpId());// 按钮请求链接地址.
    l3.setUrl(inviteUrl);
    l3.setUrlDesc("同意加入群组链接");
    linkList.add(JacksonUtils.jsonObjectSerializer(l3));

    MailLinkInfo l4 = new MailLinkInfo();
    l4.setKey("psnUrl");
    l4.setUrl(sendPsnUrl);
    l4.setUrlDesc("邀请人的详情链接");
    linkList.add(JacksonUtils.jsonObjectSerializer(l4));

    mailData.put("linkList", JacksonUtils.listToJsonStr(linkList));
    // 如果需要主题参数，添加如下：
    List<String> subjectParamLinkList = new ArrayList<String>();
    String sendPsnName = "";
    String grpName = grpBaseInfoDao.getGrpNameByGrpId(form.getGrpId());// 群组名称
    if ("zh_CN".equals(languageVersion)) {
      sendPsnName = sendPsn.getZhName();
    } else {
      sendPsnName = sendPsn.getEnName();
    }
    subjectParamLinkList.add(sendPsnName);
    subjectParamLinkList.add(grpName);
    mailData.put("subjectParamList", JacksonUtils.listToJsonStr(subjectParamLinkList));

    // TODO 邮件模版需要的其他参数可以继续往mailData添加,如：
    mailData.put("groupName", grpName);
    mailData.put("psnName", sendPsnName);
    mailData.put("avatars", sendPsn.getAvatars());
    if (sendPsn.getRegionId() != null && sendPsn.getRegionId() != 0) {
      ConstRegion constRegion = constRegionDao.findRegionNameById(sendPsn.getRegionId());
      if (constRegion != null) {
        if ("zh_CN".equals(languageVersion)) {
          mailData.put("regions", constRegion.getZhName());
        } else {
          mailData.put("regions", constRegion.getEnName());
        }
      }
    }
    paramData.put("mailData", JacksonUtils.mapToJsonStr(mailData));

    Map<String, String> resutlMap = mailHandleOriginalDataService.doHandle(paramData);
    if ("success".equals(resutlMap.get("result"))) {
      // 构造邮件成功
    } else {
      // 构造邮件失败
      logger.error(resutlMap.get("msg"));
    }

  }

  private void sendInviteGrpMailForTourist(GrpMemberForm form, String email) throws Exception {
    Map<String, Object> map = new HashMap<String, Object>();
    map.put("psnId", SecurityUtils.getCurrentUserId());
    Map<String, Object> params = buildInviteMailData(map, form, email);
    MailInitData mid = new MailInitData();
    mid.setCreateDate(new Date());
    mid.setFromNodeId(1);
    mid.setMailData(JacksonUtils.jsonObjectSerializer(params));
    mid.setStatus(1);
    mid.setToAddress(params.get(EmailConstants.EMAIL_RECEIVEEMAIL_KEY).toString());
    mailInitDataDao.save(mid);
  }

  private Map<String, Object> buildInviteMailData(Map<String, Object> map, GrpMemberForm form, String email)
      throws Exception {
    Map<String, Object> mailMap = new HashMap<String, Object>();
    mailMap.put("unsubscribeUrl", domainscm + "/oauth/pc/register");
    Person person = personDao.get(SecurityUtils.getCurrentUserId());
    String mailTemplate = null;
    String inviteUrl = buildInviteUrl(email, form.getGrpId());// 按钮请求链接地址.
    String psnShortUrl = null;// 个人站外主页地址
    String languageVersion = person.getEmailLanguageVersion();
    if (StringUtils.isBlank(languageVersion)) {
      languageVersion = LocaleContextHolder.getLocale().toString();
    }
    String grpName = grpBaseInfoDao.getGrpNameByGrpId(form.getGrpId());// 群组名称
    mailMap.put("groupName", grpName);
    // 邮件内容_email_receive_psnId
    mailMap.put(EmailConstants.EMAIL_RECEIVE_PSNID_KEY, null);
    // 邮件内容_emailTypeKey
    mailMap.put(EmailConstants.EMAIL_TYPE_KEY, EmailConstants.COMMON_EMAIL);
    // 邮件内容_email_receiveEmail
    mailMap.put(EmailConstants.EMAIL_RECEIVEEMAIL_KEY, email);
    // 邮件内容_email_Template
    mailTemplate = "Invite_Outside_Attend_Group_Template_" + languageVersion + ".ftl";
    mailMap.put(EmailConstants.EMAIL_TEMPLATE_KEY, mailTemplate);
    // 邮件内容_email
    mailMap.put("email", URLEncoder.encode(email, EmailCommonService.ENCODING));
    // 邮件内容_psnName
    String psName = getPsnNameByEmailLangage(person, languageVersion);
    mailMap.put("psnName", psName);
    // 邮件内容_inviteUrl
    PsnProfileUrl profileUrl = psnProfileUrlDao.get(SecurityUtils.getCurrentUserId());
    if (profileUrl != null && StringUtils.isNotBlank(profileUrl.getPsnIndexUrl())) {
      psnShortUrl = domainscm + "/" + ShortUrlConst.P_TYPE + "/" + profileUrl.getPsnIndexUrl();
    }
    mailMap.put("psnUrl", psnShortUrl);
    GrpIndexUrl grpIndexUrl = grpIndexUrlDao.get(form.getGrpId());
    String viewUrl = "";
    if (grpIndexUrl != null && StringUtils.isNotBlank(grpIndexUrl.getGrpIndexUrl())) {
      viewUrl = domainscm + "/" + ShortUrlConst.G_TYPE + "/" + grpIndexUrl.getGrpIndexUrl();
    }
    mailMap.put("groupViewUrl", viewUrl);
    mailMap.put("inviteUrl", inviteUrl);
    // 邮件内容_domainUrl
    mailMap.put("domainUrl", domainscm);
    // 邮件内容_avatars
    mailMap.put("avatars", person.getAvatars());
    // 邮件内容_email_subject
    String subject = this.getInviteMailSubject(psName, languageVersion);
    mailMap.put(EmailConstants.EMAIL_SUBJECT_KEY, subject);
    // 邮件内容_titolo
    getTitolo(person, mailMap);
    // 邮件内容_regions
    if (person.getRegionId() != null && !"".equals(person.getRegionId().toString())) {
      getRegion(person.getRegionId(), languageVersion, mailMap);
    }
    return mailMap;
  }

  private String buildInviteUrl(String email, Long grpId) {
    try {
      String inviteUrl = domainscm + "/oauth/pc/register";
      Long token = registerTempDao.getRegisterTempId();
      Date date = new Date();
      Map<String, Object> map = new HashMap<String, Object>();
      map.put("grpId", grpId);
      RegisterTemp rt = new RegisterTemp();
      rt.setToken(token);
      rt.setEmail(email);
      rt.setOperatorId(SecurityUtils.getCurrentUserId());
      rt.setParam(JacksonUtils.mapToJsonStr(map));
      rt.setCreateDate(date);
      rt.setUpdateDate(date);
      rt.setStatus(0);
      rt.setTempType(1);
      registerTempDao.save(rt);
      return inviteUrl + "?token=" + URLEncoder.encode(Des3Utils.encodeToDes3(token.toString()), "utf-8")
          + "&targetUrl=" + domainscm + "/groupweb/grpmember/ajaxregosterback";
    } catch (UnsupportedEncodingException e) {
      logger.error("构建群组邀请链接出错", e);
    }
    return null;
  }

  /**
   * 根据设置邮件的接收语言，获取地区
   * 
   * @param regionId
   * @param languageVersion
   * @param mailMap
   */
  private void getRegion(Long regionId, String languageVersion, Map<String, Object> mailMap) {
    ConstRegion constRegion = constRegionDao.findRegionNameById(regionId);
    if (constRegion != null) {
      if ("zh_CN".equals(languageVersion)) {
        mailMap.put("regions", constRegion.getZhName());
      } else {
        mailMap.put("regions", constRegion.getEnName());
      }
    }

  }

  /**
   * 构建人员单位+部门+职称
   * 
   * @param person
   * @param mailMap
   */
  private void getTitolo(Person person, Map<String, Object> mailMap) {
    String insName = person.getInsName();
    String department = person.getDepartment();
    String position = person.getPosition();
    String insAndDep = "";
    String pos = "";
    // 构建单位+部门信息
    if (StringUtils.isBlank(insName) || StringUtils.isBlank(department)) {
      insAndDep = (StringUtils.isBlank(insName) ? "" : insName) + (StringUtils.isBlank(department) ? "" : department);
    } else {
      insAndDep = insName + ", " + department;
    }
    // 构建职称+头衔信息
    if (StringUtils.isNotBlank(position)) {
      pos = position;
    }
    if (StringUtils.isBlank(insAndDep) || StringUtils.isBlank(pos)) {
      mailMap.put("titolo", (StringUtils.isBlank(insAndDep) ? "" : insAndDep) + (StringUtils.isBlank(pos) ? "" : pos));
    } else {
      mailMap.put("titolo", (insAndDep + ", " + pos));
    }
  }

  /**
   * 获取请求加为好友的邮件标题.
   * 
   * @param psName
   * @param languageVersion
   * @return
   */
  private String getInviteMailSubject(String psName, String languageVersion) {
    String subject = null;
    if (Locale.CHINA.toString().equals(languageVersion)) {
      subject = psName + "邀请你加入科研之友";
    } else {
      subject = psName + " invites you to join ScholarMate";
    }
    return subject;
  }

  /**
   * 根据设置邮件的接收语言，获取用户名
   * 
   * @param person
   * @param emailLangage
   * @return
   */
  private String getPsnNameByEmailLangage(Person person, String emailLangage) {
    if (person == null) {
      return null;
    }
    if (emailLangage == null) {
      emailLangage = LocaleContextHolder.getLocale().toString();
    }
    String psnName = "";
    if ("zh_CN".equals(emailLangage)) {
      psnName = person.getName();
      if (StringUtils.isBlank(psnName)) {
        psnName = person.getFirstName() + " " + person.getLastName();
      }
    } else {
      psnName = person.getFirstName() + " " + person.getLastName();
      if (StringUtils.isBlank(person.getFirstName()) || StringUtils.isBlank(person.getLastName())) {
        psnName = person.getName();
      }
    }
    return psnName;
  }

  @Override
  public void cancelJoinGrp(GrpMemberForm form) {
    GrpProposer grpProposer = grpProposerDao.getByPsnIdAndGrpId(form.getPsnId(), form.getGrpId(), 1);
    if (grpProposer != null) {
      grpProposer.setIsAccept(0);
      grpProposerDao.save(grpProposer);
    }

  }

  @Override
  public void updateSelectedDate(GrpMemberForm form) {
    String targetPsnIds = form.getTargetPsnIds();
    if (StringUtils.isNotBlank(targetPsnIds)) {
      String[] targetPsnIdArr = targetPsnIds.split(",");
      if (targetPsnIdArr != null) {
        Long selectedPsnId = null;
        for (String s : targetPsnIdArr) {
          if (StringUtils.isNotBlank(s)) {
            selectedPsnId = Long.parseLong(Des3Utils.decodeFromDes3(s));
            RecentSelected recentSelected = recentSelectedDao.getRecentSelected(form.getPsnId(), selectedPsnId);
            if (recentSelected == null) {
              recentSelected = new RecentSelected();
              recentSelected.setPsnId(form.getPsnId());
              recentSelected.setSelectedPsnId(selectedPsnId);
              recentSelected.setSelectedDate(new Date());
            } else {
              recentSelected.setSelectedDate(new Date());
            }
            recentSelectedDao.save(recentSelected);
          }
        }
      }
    }
  }

  @Override
  public void doRegosterBack(GrpMemberForm form) throws Exception {
    // 配置好首页为默认跳转地址
    form.setTargetUrl(domainscm + HOME);
    // 效验是否存在已登录
    form.setPsnId(SecurityUtils.getCurrentUserId());
    if (form.getPsnId() != null && form.getPsnId() != 0L) {
      // 根据token处理
      if (StringUtils.isNotBlank(form.getToken())) {
        RegisterTemp temp =
            registerTempDao.get(Long.parseLong(Des3Utils.decodeFromDes3(URLEncoder.encode(form.getToken(), "utf-8"))));
        if (temp != null && temp.getStatus() == 1) {
          // 处理注册回调
          doTempBack(form, temp);
        }
      }
      // 根据email处理
      if (StringUtils.isNotBlank(form.getEmails())) {
        List<RegisterTemp> list = registerTempDao.getRegisterTempByEmail(form.getEmails(), 1);
        // 处理注册回调
        loopDoTempBack(list, form);
      }

    }
  }

  /**
   * 循环处理注册回调
   * 
   * @param list
   * @param form
   * @throws Exception
   */
  private void loopDoTempBack(List<RegisterTemp> list, GrpMemberForm form) throws Exception {
    if (list != null && list.size() > 0) {
      for (RegisterTemp r : list) {
        doTempBack(form, r);
      }
    }
  }

  /**
   * 根据各个类型处理注册回调 tempType:1=群组邀请站外人员回调
   * 
   * @param form
   * @param temp
   * @throws Exception
   */
  private void doTempBack(GrpMemberForm form, RegisterTemp temp) throws Exception {
    switch (temp.getTempType()) {
      case 1:// 站外邀请群组回调
        doAddGrp(form, temp);
        break;
      // TODO 其他回调继续往这里添加
      default:
        break;
    }
  }

  /**
   * 从RegisterTemp中获取param
   * 
   * @param temp
   * @return
   * @throws Exception
   */
  private Map<String, Object> getParam(RegisterTemp temp) throws Exception {
    String param = temp.getParam();
    if (StringUtils.isNotBlank(param)) {
      return JacksonUtils.jsonToMap(param);
    }
    return null;
  }

  /**
   * 获取参数-获取群组成果id
   * 
   * @param temp
   * @return
   * @throws Exception
   */
  private Long paramFindGrpId(RegisterTemp temp) throws Exception {
    Map<String, Object> map = getParam(temp);
    if (map != null) {
      Object obj = map.get("grpId");
      if (obj != null) {
        return Long.parseLong(obj.toString());
      }
    }
    return null;
  }

  /**
   * 站外邀请群组回调处理
   * 
   * @param form
   * @param temp
   * @throws Exception
   */
  private void doAddGrp(GrpMemberForm form, RegisterTemp temp) throws Exception {
    List<Person> psnList = personDao.getPersonByEmail(temp.getEmail());
    if (!form.getPsnId().equals(temp.getOperatorId()) && psnList.size() > 0) {
      Long grpId = paramFindGrpId(temp);
      if (grpId != null && grpBaseInfoDao.isExist(grpId)) {
        Date date = new Date();
        // 自动加入群组
        GrpProposer gp = grpProposerDao.getByPsnIdAndGrpId(form.getPsnId(), grpId, 2);
        if (gp == null) {
          gp = new GrpProposer();
          gp.setCreateDate(date);
          gp.setGrpId(grpId);
          gp.setPsnId(form.getPsnId());
          gp.setType(2);
          gp.setInviterId(temp.getOperatorId());
        }
        gp.setIsAccept(2);
        grpProposerDao.save(gp);
        form.setTargetUrl(domainscm + "/groupweb/mygrp/main");
      }
      // 更新RegisterTemp状态
      temp.setPsnId(SecurityUtils.getCurrentUserId());
      temp.setStatus(2);
      temp.setUpdateDate(new Date());
      registerTempDao.save(temp);
    } else {
      form.setTargetUrl(domainscm + "/groupweb/mygrp/main");
    }

  }

  @Override
  public void uploadEmailExcelTemp(GrpMemberForm form) throws Exception {
    form.setFiterEmailCount(0L);
    File excelFile = form.getEmailExcelFile();
    if (excelFile == null) {
      form.getResultMap().put("result", "error");
      form.getResultMap().put("msg", "文件为空");
      return;
    }
    long length = excelFile.length();
    if (length > (10 * 1024 * 1024)) {
      form.getResultMap().put("result", "error");
      form.getResultMap().put("msg", "请上传小于10M的xlsx文件。");
      return;
    }
    Workbook workBook = null;
    try {
      InputStream inputStream = new FileInputStream(excelFile);
      workBook = new XSSFWorkbook(inputStream);
    } catch (Exception e) {
    }
    if (workBook == null) {
      form.getResultMap().put("result", "error");
      form.getResultMap().put("msg", "文件上传失败,请检查文件格式");
      return;
    }
    loopAnalysisPage(workBook, form);
    form.getResultMap().put("fiterEmailCount", form.getFiterEmailCount());
    form.getResultMap().put("emailList", form.getEmailList());
    form.getResultMap().put("successCount", form.getEmailList().size());
    form.getResultMap().put("result", "success");
  }

  /**
   * 遍历解析excel-页
   */
  private void loopAnalysisPage(Workbook workBook, GrpMemberForm form) {
    Sheet currSheet = null;
    for (int i = 0; i < workBook.getNumberOfSheets(); i++) {
      currSheet = workBook.getSheetAt(i);
      if (currSheet == null) {
        continue;
      } else {
        loopAnalysisRow(currSheet, form);
      }
    }
  }

  /**
   * 遍历解析excel-页 -行
   */
  private void loopAnalysisRow(Sheet currSheet, GrpMemberForm form) {
    Row currentRow = null;
    int lastRowNum = currSheet.getLastRowNum();
    for (int i = 0; i < lastRowNum + 1; i++) {
      currentRow = currSheet.getRow(i);
      if (currentRow == null) {
        continue;
      } else {
        loopAnalysisCell(currentRow, form);
      }
    }
  }

  /**
   * 遍历解析excel-行 -单元个格
   */
  private void loopAnalysisCell(Row currentRow, GrpMemberForm form) {
    Cell cell = null;
    String email = "";
    short lastCellNum = currentRow.getLastCellNum();
    for (int i = 0; i < lastCellNum + 1; i++) {
      cell = currentRow.getCell(i);
      if (cell == null) {
        continue;
      } else {
        email = String.valueOf(cell);
        if (isEmail(email) && !form.getEmailList().contains(email)) {
          form.getEmailList().add(email.trim().toLowerCase());
        } else {
          form.setFiterEmailCount(form.getFiterEmailCount() + 1);
        }
      }
    }
  }

  /**
   * 邮箱合法效验
   * 
   * @param string
   * @return
   */
  private boolean isEmail(String string) {
    if (string == null)
      return false;
    Pattern p;
    Matcher m;
    String MAIL_COAD = "(?i)^[a-z0-9]+[a-z0-9_\\-.]*@([a-z0-9][0-9a-z\\-]*\\.)+[a-z]{2,10}$";
    p = Pattern.compile(MAIL_COAD, Pattern.CASE_INSENSITIVE);
    m = p.matcher(string);
    if (m.matches())
      return true;
    else
      return false;
  }

  @Override
  public void downloadEmailExcelTemp(GrpMemberForm form) throws Exception {
    String locale = LocaleContextHolder.getLocale().toString();
    String fileName = "";
    List<String> heads = new ArrayList<String>();
    if ("en_US".equals(locale)) {
      fileName = "EmailList";
      heads.add("Email");
    } else {
      fileName = "群组邀请邮件模版";
      heads.add("邮件地址");
    }
    XSSFWorkbook createExcelFile = ExcelUtils.createExcelFile2(fileName, heads);
    HttpServletResponse response = Struts2Utils.getResponse();
    response.setContentType("application/vnd.ms-excel");
    response.setHeader("content-disposition", "attachment;filename=emailTemp.xlsx");
    createExcelFile.write(response.getOutputStream());

  }
}
