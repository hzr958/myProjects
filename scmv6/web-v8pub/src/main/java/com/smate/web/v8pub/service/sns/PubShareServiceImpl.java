package com.smate.web.v8pub.service.sns;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import javax.annotation.Resource;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang.ObjectUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang3.StringEscapeUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.freemarker.FreeMarkerTemplateUtils;
import org.springframework.web.client.RestTemplate;

import com.smate.center.mail.connector.model.MailLinkInfo;
import com.smate.center.mail.connector.model.MailOriginalDataInfo;
import com.smate.core.base.email.service.EmailCommonService;
import com.smate.core.base.email.service.EmailSendService;
import com.smate.core.base.psn.dao.PsnProfileUrlDao;
import com.smate.core.base.psn.model.PsnProfileUrl;
import com.smate.core.base.pub.dao.PubIndexUrlDao;
import com.smate.core.base.pub.model.PubIndexUrl;
import com.smate.core.base.pub.po.dao.PubPdwhSnsRelationDAO;
import com.smate.core.base.utils.constant.ShortUrlConst;
import com.smate.core.base.utils.dao.security.PersonDao;
import com.smate.core.base.utils.dao.security.UserDao;
import com.smate.core.base.utils.json.JacksonUtils;
import com.smate.core.base.utils.model.cas.security.User;
import com.smate.core.base.utils.model.security.Person;
import com.smate.core.base.utils.security.Des3Utils;
import com.smate.web.v8pub.dao.pdwh.PdwhPubIndexUrlDao;
import com.smate.web.v8pub.dao.pdwh.PubPdwhDAO;
import com.smate.web.v8pub.dao.sns.PubShareDAO;
import com.smate.web.v8pub.dao.sns.PubSnsDAO;
import com.smate.web.v8pub.exception.ServiceException;
import com.smate.web.v8pub.po.pdwh.PubPdwhPO;
import com.smate.web.v8pub.po.sns.PubSharePO;
import com.smate.web.v8pub.po.sns.PubSnsPO;
import com.smate.web.v8pub.service.pdwh.PdwhPubShareService;
import com.smate.web.v8pub.service.pdwh.PdwhPubStatisticsService;
import com.smate.web.v8pub.vo.PubShareVo;
import com.smate.web.v8pub.vo.pdwh.PdwhPubOperateVO;
import com.smate.web.v8pub.vo.sns.PubOperateVO;

import freemarker.template.Configuration;
import freemarker.template.TemplateException;

/**
 * 成果分享服务实现类
 * 
 * @author aijiangbin
 * @date 2018年6月1日
 */

@Service
@Transactional(rollbackFor = Exception.class)
public class PubShareServiceImpl implements PubShareService {

  private Logger logger = LoggerFactory.getLogger(getClass());
  @Resource(name = "freemarkerConfiguration")
  private Configuration freemarkerConfiguration;
  private static final String ENCODING = "utf-8";
  @Value("${domainscm}")
  private String domainscm;
  @Autowired
  private PubShareDAO pubShareDAO;
  @Autowired
  private PubStatisticsService newPubStatisticsService;
  @Autowired
  private EmailSendService sharedYourPubEmailService;
  @Autowired
  private EmailCommonService emailCommonService;
  @Autowired
  private PersonDao personDao;
  @Autowired
  private PubPdwhDAO pubPdwhDAO;
  @Autowired
  private PubSnsDAO pubSnsDAO;
  @Autowired
  private PubIndexUrlDao pubIndexUrlDao;
  @Autowired
  private PdwhPubIndexUrlDao pdwhPubIndexUrlDao;
  @Autowired
  private PsnProfileUrlDao psnProfileUrlDao;
  @Autowired
  private UserDao userDao;
  @Autowired
  private PubPdwhSnsRelationDAO pubPdwhSnsRelationDAO;
  @Autowired
  private PdwhPubShareService pdwhPubShareService;
  @Autowired
  private PdwhPubStatisticsService newPdwhPubStatisticsService;
  @Autowired
  private RestTemplate restTemplate;
  @Value(value = "${sendEmail.restful.url}")
  private String sendRestfulUrl;

  @Override
  public void save(PubSharePO pubShare) throws ServiceException {
    try {
      pubShareDAO.save(pubShare);
    } catch (Exception e) {
      logger.error("成果分享服务：添加分享记录异常,pubId" + pubShare.getPubId(), e);
      throw new ServiceException(e);
    }

  }

  /**
   * 个人库成果分享回调
   */
  @Override
  public void shareOpt(PubOperateVO pubOperateVO) throws ServiceException {
    Long pubId = pubOperateVO.getPubId();
    try {// 分享给联系人，分享给几个联系人就插入几条分享记录
      // SCM-23563 个人库成果先直接操作，如果是关联成果，再来同步数据
      updateSnsShareStatistics(pubOperateVO, pubId);
      // SCM-23420 数据来源调整 跟基准库关联的成果，操作要通知所有关联成果的所属用户
      Long pdwhPubId = pubPdwhSnsRelationDAO.getPdwhPubIdBySnsPubId(pubId);
      if (pdwhPubId != null && pdwhPubId > 0L) {
        pdwhShareOpt(pubOperateVO, pdwhPubId);
        List<Long> snsPubIds = pubPdwhSnsRelationDAO.getSnsOpenPubIds(pdwhPubId, 0L);
        if (CollectionUtils.isNotEmpty(snsPubIds)) {
          for (Long snsPubId : snsPubIds) {
            if (!snsPubId.equals(pubId)) {
              updateSnsShareStatistics(pubOperateVO, snsPubId);// 个人库数据同步
            }
          }
        }
      }
    } catch (Exception e) {
      logger.error("成果分享回调异常,pubId=" + pubId, e);
      throw new ServiceException(e);
    }
  }

  @Override
  public void updateSnsShareStatistics(PubOperateVO pubOperateVO, Long snsPubId) {
    if (StringUtils.isNotBlank(pubOperateVO.getSharePsnGroupIds())) {
      String[] sharePsnGroupIds = pubOperateVO.getSharePsnGroupIds().split(",");
      if (sharePsnGroupIds.length > 0) {
        for (String sharePsnGroupId : sharePsnGroupIds) {
          pubOperateVO.setSharePsnGroupId(Long.parseLong(Des3Utils.decodeFromDes3(sharePsnGroupId)));
          // 往成果分享新插入记录
          insertShare(pubOperateVO, snsPubId);
          // 更新成果统计表 分享数
          newPubStatisticsService.updateShareStatistics(snsPubId);
        }
      } else {// 分享到群组、动态等 插入一条分享记录
        insertShare(pubOperateVO, snsPubId);
        newPubStatisticsService.updateShareStatistics(snsPubId);
      }
    } else {
      insertShare(pubOperateVO, snsPubId);
      newPubStatisticsService.updateShareStatistics(snsPubId);
    }
  }

  public void pdwhShareOpt(PubOperateVO pubOperateVO, Long pdwhPubId) {
    PdwhPubOperateVO pdwhPubOperateVO = new PdwhPubOperateVO();
    pdwhPubOperateVO.setPdwhPubId(pdwhPubId);
    pdwhPubOperateVO.setPsnId(pubOperateVO.getPsnId());
    pdwhPubOperateVO.setSharePsnGroupIds(pubOperateVO.getSharePsnGroupIds());
    pdwhPubOperateVO.setComment(pubOperateVO.getComment());
    pdwhPubOperateVO.setPlatform(pubOperateVO.getPlatform());
    pdwhPubShareService.pdwhShare(pdwhPubOperateVO, pdwhPubId);
  }

  private void insertShare(PubOperateVO pubOperateVO, Long snsPubId) {
    try {
      PubSharePO pubShare = new PubSharePO();
      if (StringUtils.isNotBlank(String.valueOf(pubOperateVO.getSharePsnGroupId()))) {
        pubShare.setSharePsnGroupId(pubOperateVO.getSharePsnGroupId());
      }
      pubShare.setPubId(snsPubId);
      pubShare.setPsnId(pubOperateVO.getPsnId());
      pubShare.setComment(pubOperateVO.getComment());
      pubShare.setPlatform(pubOperateVO.getPlatform());
      pubShare.setStatus(0);
      pubShare.setGmtCreate(new Date());
      pubShare.setGmtModified(new Date());
      save(pubShare);
    } catch (Exception e) {
      logger.error("个人库成果分享插入异常,pubId=" + snsPubId, e);
    }
  }

  private void sendSharedEmail(Long sharerPsnId, Long ownerPsnId, Long pubId) {
    try {
      Map<String, Object> map = new HashMap<String, Object>();
      map.put("sharedPsnId", sharerPsnId);
      map.put("psnId", ownerPsnId);
      map.put("pubId", pubId);
      sharedYourPubEmailService.syncEmailInfo(map);
    } catch (Exception e) {
      logger.error("发送分享邮件错误：pubId:" + pubId + ",sharerPsnId:" + sharerPsnId, e);
    }
  }

  @Override
  public void sendShareEmail(PubShareVo pubShareVo, String mailOrPsnId, int i) throws Exception {
    String mailReg = "^([a-z0-9A-Z]+[-|_|\\.]?)+[a-z0-9A-Z]@([a-z0-9A-Z]+(-[a-z0-9A-Z]+)?\\.)+[a-zA-Z]{2,}$";// 邮箱地址匹配
    Person sender = personDao.findPsnInfoForEmail(pubShareVo.getPsnId());
    Long receverPsnId = null;
    if (Pattern.matches(mailReg, mailOrPsnId)) {// 是邮箱地址
      if (StringUtils.isNotEmpty(mailOrPsnId) && sender != null) {
        User user = userDao.findByLoginName(mailOrPsnId);// 判断当前邮件是不是在站内注册了
        if (user == null) {// 为站外人员直接发送邮件
          Person receiver = new Person();
          receiver.setPersonId(0L);
          receiver.setEmail(mailOrPsnId);
          restSendSharePubEmail(pubShareVo, sender, receiver, i);
          return;
        } else {// 获取到对应的人员
          receverPsnId = user.getId();
        }
      }
    }
    if (receverPsnId == null) {
      receverPsnId = Long.valueOf(Des3Utils.decodeFromDes3(mailOrPsnId));
    }
    if (receverPsnId.equals(pubShareVo.getPsnId())) {// 发送和接收者为同一个人
      return;
    }
    Person receiver = personDao.findPsnInfoForEmail(receverPsnId);
    if (receiver != null) {
      restSendSharePubEmail(pubShareVo, sender, receiver, i);
    }
  }

  /**
   * 调用接口发送分享成果邮件
   * 
   * @param pubShareVo
   * @param sender
   * @param receiver
   * @throws Exception
   */
  private void restSendSharePubEmail(PubShareVo pubShareVo, Person sender, Person receiver, int i) throws Exception {

    // 全文请求使用新模板
    if (sender == null || receiver == null) {
      throw new Exception("发送分享成果邮件回复，邮件对象为空" + this.getClass().getName());
    }
    String language = StringUtils.isNotBlank(receiver.getEmailLanguageVersion()) ? receiver.getEmailLanguageVersion()
        : sender.getEmailLanguageVersion();
    if (StringUtils.isBlank(language)) {
      language = LocaleContextHolder.getLocale().getLanguage();
    }
    // 定义接口接收的参数
    Map<String, String> paramData = new HashMap<String, String>();
    // 定义构造邮件模版参数集
    Map<String, Object> mailData = new HashMap<String, Object>();
    String recvName = emailCommonService.getPsnNameByEmailLangage(receiver, language);
    String psnName = emailCommonService.getPsnNameByEmailLangage(sender, language);
    Long pubId = Long.valueOf(Des3Utils.decodeFromDes3(pubShareVo.getDes3PubIds()));

    // 1跟踪链接 根据key放置到模板中 所有的链接不再需要放到mailData中
    PsnProfileUrl psnProfileUrl = psnProfileUrlDao.get(sender.getPersonId());
    String senderPersonUrl = "";
    if (psnProfileUrl != null && StringUtils.isNotBlank(psnProfileUrl.getPsnIndexUrl())) {
      senderPersonUrl = domainscm + "/" + ShortUrlConst.P_TYPE + "/" + psnProfileUrl.getPsnIndexUrl();
    }
    // 可能是个人成果，可能是群组成果。文献好像不能用短地址
    String pubDetail = "";
    PubIndexUrl pubIndexUrl = pubIndexUrlDao.get(pubId);
    String pdwhPubIndexUrl = pdwhPubIndexUrlDao.getIndexUrlByPubId(pubId);
    if (pubIndexUrl != null && StringUtils.isNotBlank(pubIndexUrl.getPubIndexUrl())) {
      pubDetail = domainscm + "/A/" + pubIndexUrl.getPubIndexUrl();
    } else if (StringUtils.isNotBlank(pdwhPubIndexUrl)) {
      pubDetail = domainscm + "/S/" + pdwhPubIndexUrl;
    }
    List<String> linkList = new ArrayList<String>();
    MailLinkInfo l1 = new MailLinkInfo();
    l1.setKey("domainUrl");
    l1.setUrl(domainscm);
    l1.setUrlDesc("科研之友首页");
    linkList.add(JacksonUtils.jsonObjectSerializer(l1));
    MailLinkInfo l2 = new MailLinkInfo();
    l2.setKey("senderPersonUrl");
    l2.setUrl(senderPersonUrl);
    l2.setUrlDesc("发件人主页链接");
    linkList.add(JacksonUtils.jsonObjectSerializer(l2));
    MailLinkInfo l3 = new MailLinkInfo();
    l3.setKey("pubDetail");
    l3.setUrl(pubDetail);
    l3.setUrlDesc("成果详情链接");
    linkList.add(JacksonUtils.jsonObjectSerializer(l3));
    MailLinkInfo l4 = new MailLinkInfo();
    l4.setKey("viewUrl");
    l4.setUrl(this.domainscm + "/dynweb/showmsg/msgmain?model=chatMsg");
    l4.setUrlDesc("查看站内信地址");
    linkList.add(JacksonUtils.jsonObjectSerializer(l4));
    mailData.put("linkList", JacksonUtils.listToJsonStr(linkList));

    // 2构造必需的参数
    MailOriginalDataInfo info = new MailOriginalDataInfo();
    Integer tempcode = 10102;
    info.setSenderPsnId(sender.getPersonId());
    info.setReceiverPsnId(receiver.getPersonId());
    info.setReceiver(receiver.getEmail());
    info.setMsg("分享我的文件邮件");
    info.setMailTemplateCode(tempcode);
    paramData.put("mailOriginalData", JacksonUtils.jsonObjectSerializerNoNull(info));


    // 3模版参数集
    mailData.put("type", pubShareVo.getResType().toString());
    if (pubShareVo.getDbid() != null || (pubShareVo.getDatabaseType() != null && pubShareVo.getDatabaseType() == 2)) {
      PubPdwhPO pdwhPub = pubPdwhDAO.get(pubId);
      if (pdwhPub == null) {
        return;
      }
      String title = StringEscapeUtils.unescapeHtml4(pdwhPub.getTitle());
      Pattern p = Pattern.compile("[\u4e00-\u9fa5]");
      if (p.matcher(title).find()) {
        mailData.put("minZhShareTitle", "“" + title + "”");
        mailData.put("minEnShareTitle", "“" + title + "”");
      } else {
        mailData.put("minZhShareTitle", "\"" + title + "\"");
        mailData.put("minEnShareTitle", "\"" + title + "\"");
      }
    } else {
      if (pubShareVo.getResType() == 1 || pubShareVo.getResType() == 2) {
        PubSnsPO snsPub = pubSnsDAO.getPubsnsById(pubId);
        if (snsPub == null) {
          return;
        }
        String title = StringEscapeUtils.unescapeHtml4(snsPub.getTitle());
        Pattern p = Pattern.compile("[\u4e00-\u9fa5]");
        if (p.matcher(title).find()) {
          mailData.put("minZhShareTitle", "“" + title + "”");
          mailData.put("minEnShareTitle", "“" + title + "”");

        } else {
          mailData.put("minZhShareTitle", "\"" + title + "\"");
          mailData.put("minEnShareTitle", "\"" + title + "\"");
        }
        // SCM-23420 数据来源调整 跟基准库关联的成果，操作要通知所有关联成果的所属用户（注意一个用户 多条成果关联同一个基准库 成果的情况）
        Long pdwhPubId = pubPdwhSnsRelationDAO.getPdwhPubIdBySnsPubId(pubId);
        if (pdwhPubId != null && pdwhPubId > 0L) {
          if (i == 1) {
            buildPdwhSnsRelationEmail(pubShareVo, pubId, pdwhPubId);
          }
        } else {
          // Long resOwnerPsnId = pubSnsDAO.getOwnerPubPsnId(pubId);
          Long resOwnerPsnId = snsPub.getCreatePsnId();
          this.sendSharePubEmail(pubShareVo.getPsnId(), resOwnerPsnId, pubId);// 分享了你的成果邮件
        }
      }
    }
    mailData.put("recvName", recvName);
    mailData.put("psnName", psnName);
    // 只能分享一条成果
    mailData.put("total", "1");
    mailData.put("mailContext", "");
    if (StringUtils.isNotEmpty(pubShareVo.getContent())) {
      mailData.put("recommendReason", pubShareVo.getContent());
    }


    // 4主题参数，添加如下：
    List<String> subjectParamLinkList = new ArrayList<String>();
    String subjectType = "";
    String subjectCount = mailData.get("total").toString();
    if ("zh".equals(language) || "zh_CN".equals(language)) {
      subjectType = "成果";
    } else {
      subjectType = "publications";
      if ("1".equals(subjectCount)) {
        subjectCount = "a";
        subjectType = "publication";
      }
    }
    subjectParamLinkList.add(psnName);
    subjectParamLinkList.add(subjectCount);
    subjectParamLinkList.add(subjectType);
    mailData.put("subjectParamList", JacksonUtils.listToJsonStr(subjectParamLinkList));

    paramData.put("mailData", JacksonUtils.mapToJsonStr(mailData));
    restTemplate.postForObject(this.sendRestfulUrl, paramData, Object.class);
  }

  public void buildPdwhSnsRelationEmail(PubShareVo pubShareVo, Long pubId, Long pdwhPubId) {
    List<Long> snsPubIds = pubPdwhSnsRelationDAO.getSnsOpenPubIds(pdwhPubId, 0L);
    if (CollectionUtils.isNotEmpty(snsPubIds)) {
      List<Long> pubOwnerList = pubSnsDAO.getSnsPubList(snsPubIds);
      List<Long> newPubOwnerList = pubOwnerList.stream().distinct().collect(Collectors.toList());
      List<Map<Long, Long>> pubPsbList = pubSnsDAO.getSnsPub(snsPubIds);
      if (CollectionUtils.isNotEmpty(newPubOwnerList)) {
        for (Long ownerId : newPubOwnerList) {// 操作要通知所有关联成果的所属用户
          if (pubPsbList != null && pubPsbList.size() > 0) {
            Long newPubId = null;
            for (Map<Long, Long> map : pubPsbList) {
              if (ownerId.equals((Long) map.get("createPsnId"))) {
                newPubId = map.get("pubId");
              }
            }
            this.sendSharePubEmail(pubShareVo.getPsnId(), ownerId, newPubId);// 分享了你的成果邮件
          }
        }
      }
    }
  }

  /**
   * 分享了你的成果
   * 
   * @param sharerPsnId
   * @param resOwnerPsnId
   * @param resType
   * @param resId
   */
  private void sendSharePubEmail(Long sharerPsnId, Long resOwnerPsnId, Long resId) {
    try {
      if (!sharerPsnId.equals(resOwnerPsnId)) {
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("sharedPsnId", sharerPsnId);
        map.put("psnId", resOwnerPsnId);
        map.put("pubId", resId);
        // /////////////////SCM-14335////////////////////////
        PubIndexUrl pubIndexUrl = pubIndexUrlDao.get(resId);
        String url = "";
        if (pubIndexUrl != null) {
          url = domainscm + "/" + ShortUrlConst.A_TYPE + "/" + pubIndexUrl.getPubIndexUrl();
        } else {
          url = domainscm + "/pub/outside/details?des3PubId=" + Des3Utils.encodeToDes3(resId + "");
        }
        map.put("pubDetailUrl", url);
        // //////////////////SCM-14335///////////////////////
        sharedYourPubEmailService.syncEmailInfo(map);
      }
    } catch (Exception e) {
      logger.error("发送分享邮件错误：resType:{},resId:{},sharerPsnId:{}", resId, sharerPsnId, e);
    }
  }

  private String buildEmailTitle(Map<String, Object> ctxMap) {
    String msg = "";
    try {
      msg = FreeMarkerTemplateUtils.processTemplateIntoString(
          freemarkerConfiguration.getTemplate(ObjectUtils.toString(ctxMap.get("tmpUrl")), ENCODING), ctxMap);
    } catch (IOException e) {
      logger.error("构建Email标题失败，没有找到对应的Email标题模板！", e);
    } catch (TemplateException e) {
      logger.error("构造Email标题失败,FreeMarker处理失败", e);
    }
    return msg;
  }
}
