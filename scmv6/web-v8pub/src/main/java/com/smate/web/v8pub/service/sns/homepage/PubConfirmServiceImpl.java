package com.smate.web.v8pub.service.sns.homepage;

import com.smate.center.mail.connector.model.MailLinkInfo;
import com.smate.center.mail.connector.model.MailOriginalDataInfo;
import com.smate.core.base.email.service.EmailCommonService;
import com.smate.core.base.psn.dao.PsnProfileUrlDao;
import com.smate.core.base.psn.model.PsnProfileUrl;
import com.smate.core.base.pub.vo.PubInfo;
import com.smate.core.base.utils.constant.ShortUrlConst;
import com.smate.core.base.utils.dao.security.PersonDao;
import com.smate.core.base.utils.json.JacksonUtils;
import com.smate.core.base.utils.model.security.Person;
import com.smate.core.base.utils.number.NumberUtils;
import com.smate.core.base.utils.security.Des3Utils;
import com.smate.web.v8pub.consts.V8pubConst;
import com.smate.web.v8pub.dao.pdwh.PdwhPubIndexUrlDao;
import com.smate.web.v8pub.dao.pdwh.PubPdwhDAO;
import com.smate.web.v8pub.dao.sns.FriendDao;
import com.smate.web.v8pub.dao.sns.PubAssignLogDAO;
import com.smate.web.v8pub.dao.sns.PubFulltextPsnRcmdDao;
import com.smate.web.v8pub.dao.sns.PubSnsDAO;
import com.smate.web.v8pub.enums.PubHandlerStatusEnum;
import com.smate.web.v8pub.exception.ServiceException;
import com.smate.web.v8pub.po.pdwh.PdwhPubIndexUrl;
import com.smate.web.v8pub.po.pdwh.PubPdwhPO;
import com.smate.web.v8pub.po.sns.PubFulltextPsnRcmd;
import com.smate.web.v8pub.po.sns.PubSnsPO;
import com.smate.web.v8pub.service.oauth.OauthLoginService;
import com.smate.web.v8pub.service.pdwh.PubAssignLogService;
import com.smate.web.v8pub.service.pdwh.PubPdwhDetailService;
import com.smate.web.v8pub.service.restful.PublicImportAndConfirmPubService;
import com.smate.web.v8pub.vo.PubListVO;
import org.apache.commons.beanutils.BeanUtils;
import org.codehaus.plexus.util.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.web.client.RestTemplate;

import javax.annotation.Resource;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 
 * @author aijiangbin
 * @date 2018年8月9日
 */

@Service
@Transactional(rollbackFor = Exception.class)
public class PubConfirmServiceImpl implements PubConfirmService {

  private final Logger logger = LoggerFactory.getLogger(getClass());

  public final static String SNS_QUERY_PUB = "/data/pub/query/list";

  @Autowired
  private PubAssignLogDAO pubAssignLogDAO;
  @Autowired
  private PubPdwhDetailService pubPdwhDetailService;
  @Autowired
  private PublicImportAndConfirmPubService publicImportAndConfirmPubService;
  @Autowired
  private PubFulltextPsnRcmdDao pubFulltextPsnRcmdDao;
  @Autowired
  private PubSnsDAO pubSnsDAO;
  @Autowired
  private PubPdwhDAO pubPdwhDAO;
  @Autowired
  private PersonDao personDao;
  @Autowired
  private PdwhPubIndexUrlDao pdwhPubIndexUrlDao;
  @Value("${domainscm}")
  private String domainscm;
  @Value("${domainMobile}")
  private String domainMobile;
  @Resource(name = "restTemplate")
  private RestTemplate restTemplate;
  @Autowired
  private PsnProfileUrlDao psnProfileUrlDao;
  @Autowired
  private EmailCommonService emailCommonService;
  @Value(value = "${sendEmail.restful.url}")
  private String sendRestfulUrl;
  @Autowired
  private OauthLoginService oauthLoginService;
  @Autowired
  private FriendDao friendDao;
  @Autowired
  private PubAssignLogService pubAssignLogService;

  @Override
  public void queryPubComfirm(PubListVO pubListVO) {
    String SERVER_URL = domainscm + SNS_QUERY_PUB;
    // 设置请求头部
    HttpHeaders requestHeaders = new HttpHeaders();
    requestHeaders.setContentType(MediaType.APPLICATION_JSON_UTF8);
    HttpEntity<String> requestEntity =
        new HttpEntity<String>(JacksonUtils.jsonObjectSerializer(pubListVO.getPubQueryDTO()), requestHeaders);
    Map<String, Object> object =
        (Map<String, Object>) restTemplate.postForObject(SERVER_URL, requestEntity, Object.class);
    if (object != null && object.get("status").equals(V8pubConst.SUCCESS)) {
      List<Map<String, Object>> resultList = (List<Map<String, Object>>) object.get("resultList");
      Object totalCount = object.get("totalCount");
      pubListVO.setTotalCount(NumberUtils.toInt(totalCount.toString()));
      List<PubInfo> list = new ArrayList<>(16);
      pubListVO.setResultList(list);
      if (resultList != null && resultList.size() > 0) {
        for (Map<String, Object> map : resultList) {
          PubInfo pubInfo = new PubInfo();
          list.add(pubInfo);
          try {
            BeanUtils.populate(pubInfo, map);
          } catch (IllegalAccessException | InvocationTargetException e) {
            logger.error("复制属性异常", e);
          }

        }
      }
    }
  }

  public boolean checkPubfulltextConfirm(PubListVO pubListVO) {
    List<PubFulltextPsnRcmd> result = pubFulltextPsnRcmdDao.queryRcmdPubFulltext(pubListVO.getPsnId());
    if (org.apache.commons.collections4.CollectionUtils.isNotEmpty(result)) {
      try {
        for (PubFulltextPsnRcmd pubFulltextPsnRcmd : result) {
          PubSnsPO pubSns = pubSnsDAO.get(pubFulltextPsnRcmd.getPubId());
          if (pubSns != null) {
            return true;
          }
        }
      } catch (Exception e) {
        logger.error("pc全文认领-PropertyUtils.copyProperties出错", e);
      }
    }
    return false;

  }

  @Override
  public String affirmPubComfirm(String des3PdwhPubId, String des3PsnId) {
    Map<String, String> result = new HashMap<>();
    try {
      // 参数的处理
      Long pdwhPubId = checkId(des3PdwhPubId);
      Long psnId = checkId(des3PsnId);
      result = checkParams(pdwhPubId, psnId);
      // 业务的处理
      if (CollectionUtils.isEmpty(result)) {
        // 1.调用成果保存的接口，将基准库成果导入至个人库中
        String saveResult = publicImportAndConfirmPubService.importAndConfirmPdwhPub(pdwhPubId, psnId, null, null, 1);
        Map saveResultMap = JacksonUtils.jsonToMap(saveResult);
        if ("SUCCESS".equals(saveResultMap.get("status").toString())) {
          result.put("status", PubHandlerStatusEnum.SUCCESS.getValue());
          result.put("msg", "成果已导入到个人库");
          Long snsPubId = NumberUtils.toLong(Des3Utils.decodeFromDes3(saveResultMap.get("des3PubId").toString()));
          // 导入成果后，更新认领状态
          updateConfirmResult(pdwhPubId, psnId, snsPubId, 1);
          // 成果认领后计算成果合作者
          pubAssignLogService.psnPubCopartnerRcmd(pdwhPubId, psnId);
          // 发送通知合作者邮件
          preSendNotifyPartnerMail(pdwhPubId, psnId);
        } else {
          result.put("status", PubHandlerStatusEnum.ERROR.getValue());
          result.put("msg", "成果导入失败");
        }

      }
    } catch (Exception e) {
      logger.error("确认成果认领业务失败", e);
      result.put("status", PubHandlerStatusEnum.ERROR.getValue());
      result.put("msg", e.getMessage());
    }
    return JacksonUtils.jsonMapSerializer(result);
  }



  /**
   * @param psnId 本人id
   * @param pdwhPubId 基准库成果id
   * @throws Exception
   * 
   */
  @SuppressWarnings("unchecked")
  private void preSendNotifyPartnerMail(Long pdwhPubId, Long psnId) {
    List<Long> partnerIds = pubAssignLogDAO.getPartnerPsnIdsByPubConfirmId(pdwhPubId, psnId);
    if (partnerIds != null && partnerIds.size() > 0) {
      // 过滤掉不是好友的合作者人员
      List<Long> sendMailPartnerIds = friendDao.filterFriendPsn(psnId, partnerIds);
      // 1.获取基准库成果
      PubPdwhPO pubPdwhPO = pubPdwhDAO.get(pdwhPubId);
      sendMailPartnerIds.forEach(partnerId -> {
        try {
          sendNotifyPartnerMail(partnerId, psnId, pubPdwhPO);
        } catch (Exception e) {
          logger.error("成果认领，发送通知合作者出错", e);
        }
      });
    }
  }

  private void sendNotifyPartnerMail(Long partnerId, Long currentPsnId, PubPdwhPO pubPdwhPO) throws Exception {
    Person sender = personDao.findPsnInfoForEmail(currentPsnId);
    Person receiver = personDao.findPsnInfoForEmail(partnerId);
    if (sender == null || receiver == null) {
      return;
    }
    List<PubInfo> pubdetails = new ArrayList<PubInfo>();
    buildPubInfo(pubdetails, pubPdwhPO);
    // 定义接口接收的参数
    Map<String, String> paramData = new HashMap<String, String>();
    // 定义构造邮件模版参数集
    Map<String, Object> mailData = new HashMap<String, Object>();
    // 构造必需的参数
    MailOriginalDataInfo info = new MailOriginalDataInfo();
    String language = "";
    language = receiver.getEmailLanguageVersion();
    if (StringUtils.isBlank(language)) {
      language = LocaleContextHolder.getLocale().toString();
    }
    if (pubPdwhPO != null) {
      Integer tempcode = 10021;
      info.setSenderPsnId(sender.getPersonId());
      info.setReceiverPsnId(receiver.getPersonId());
      info.setReceiver(receiver.getEmail());
      info.setMsg("认领成果之后，发送通知合作者邮件");
      info.setMailTemplateCode(tempcode);
      paramData.put("mailOriginalData", JacksonUtils.jsonObjectSerializerNoNull(info));
      // 认领成果人链接
      String frdUrl = null;
      PsnProfileUrl profileUrl = psnProfileUrlDao.get(sender.getPersonId());
      if (profileUrl != null && StringUtils.isNotBlank(profileUrl.getPsnIndexUrl())) {
        frdUrl = this.domainscm + "/" + ShortUrlConst.P_TYPE + "/" + profileUrl.getPsnIndexUrl();
      }
      mailData.put("frdUrl", frdUrl);
      // SCM-11425 重置密码功能实现 调用远程服务获取openId和AID
      Long openId = oauthLoginService.getOpenId("00000000", receiver.getPersonId(), 2);
      String AID = oauthLoginService.getAutoLoginAID(openId, "ResetPWD");
      String pubAllUrl = EmailCommonService.PC_OR_MB_TOKEN + "pc=" + domainscm
          + "/psnweb/homepage/show?module=pub&jumpto=puball&menuId=1200&" + "AID=" + AID + "&resetpwd=true"
          + "&&mobile=" + domainMobile + "/psnweb/mobile/msgbox?model=centerMsg&whoFirst=pubRcmd";
      String psnName = emailCommonService.getPsnNameByEmailLangage(sender, language);
      String copPsnName = emailCommonService.getPsnNameByEmailLangage(receiver, language);
      mailData.put("pubAllUrl", pubAllUrl);
      mailData.put("frdPubAllUrl", pubAllUrl);
      mailData.put("psnName", psnName);
      mailData.put("copPsnName", copPsnName);
      mailData.put("pubDetails", pubdetails);
      // 跟踪链接 根据key放置到模板中 所有的链接不再需要放到mailData中
      List<String> linkList = new ArrayList<String>();
      MailLinkInfo l1 = new MailLinkInfo();
      l1.setKey("domainUrl");
      l1.setUrl(domainscm);
      l1.setUrlDesc("科研之友首页");
      linkList.add(JacksonUtils.jsonObjectSerializer(l1));
      MailLinkInfo l2 = new MailLinkInfo();
      l2.setKey("frdUrl");
      l2.setUrl(frdUrl);
      l2.setUrlDesc("认领成果人主页链接");
      linkList.add(JacksonUtils.jsonObjectSerializer(l2));
      MailLinkInfo l3 = new MailLinkInfo();
      l3.setKey("pubAllUrl");
      l3.setUrl(pubAllUrl);
      l3.setUrlDesc("成果详情链接");
      linkList.add(JacksonUtils.jsonObjectSerializer(l3));
      MailLinkInfo l4 = new MailLinkInfo();
      l4.setKey("frdPubAllUrl");
      l4.setUrl(pubAllUrl);
      l4.setUrlDesc("更多成果链接");
      linkList.add(JacksonUtils.jsonObjectSerializer(l4));
      // 主题参数，添加如下：
      List<String> subjectParamLinkList = new ArrayList<String>();
      subjectParamLinkList.add(copPsnName);
      subjectParamLinkList.add(psnName);
      mailData.put("linkList", JacksonUtils.listToJsonStr(linkList));
      mailData.put("subjectParamList", JacksonUtils.listToJsonStr(subjectParamLinkList));
      paramData.put("mailData", JacksonUtils.mapToJsonStr(mailData));
      restTemplate.postForObject(this.sendRestfulUrl, paramData, Object.class);
    }
  }

  private void buildPubInfo(List<PubInfo> pubdetails, PubPdwhPO pdwhPub) {
    PubInfo pubInfo = new PubInfo();
    pubInfo.setTitle(pdwhPub.getTitle());
    if (pdwhPub.getPubType() != null) {
      if (pdwhPub.getPubType() == 3 || pdwhPub.getPubType() == 4 || pdwhPub.getPubType() == 8) {
        pubInfo.setPubTypeName("论文");
      } else if (pdwhPub.getPubType() == 5) {
        pubInfo.setPubTypeName("专利");
      } else if (pdwhPub.getPubType() == 1) {
        pubInfo.setPubTypeName("奖励");
      } else if (pdwhPub.getPubType() == 2) {
        pubInfo.setPubTypeName("书/著作");
      } else if (pdwhPub.getPubType() == 7) {
        pubInfo.setPubTypeName("其他");
      } else if (pdwhPub.getPubType() == 10) {
        pubInfo.setPubTypeName("书籍章节");
      }
    }
    pubInfo.setBriefDesc(pdwhPub.getBriefDesc());

    String authorNames = pdwhPub.getAuthorNames();
    if (StringUtils.isNotBlank(authorNames)) {
      String endChar = authorNames.substring(authorNames.length() - 1, authorNames.length());
      if (endChar.equalsIgnoreCase(";") || endChar.equalsIgnoreCase("；")) {
        authorNames = authorNames.substring(0, authorNames.length() - 1);
      }
    }
    pubInfo.setAuthorNames(authorNames);
    PdwhPubIndexUrl pdwhPubIndexUrl = pdwhPubIndexUrlDao.get(pdwhPub.getPubId());
    if (pdwhPubIndexUrl != null && StringUtils.isNotBlank(pdwhPubIndexUrl.getPubIndexUrl())) {
      pubInfo.setPubIndexUrl(domainscm + "/" + ShortUrlConst.S_TYPE + "/" + pdwhPubIndexUrl.getPubIndexUrl());
    } else {
      String url =
          this.domainscm + "/pub/details/pdwh?des3PubId=" + Des3Utils.encodeToDes3(pdwhPub.getPubId().toString());
      pubInfo.setPubIndexUrl(url);
    }
    pubdetails.add(pubInfo);
  }

  private Map<String, String> checkParams(Long pdwhPubId, Long psnId) {
    Map<String, String> result = new HashMap<>();
    if (NumberUtils.isNullOrZero(pdwhPubId)) {
      result.put("status", PubHandlerStatusEnum.ERROR.getValue());
      result.put("msg", "pdwhPubId不合法,pdwhPubId=" + pdwhPubId);
    }
    if (NumberUtils.isNullOrZero(psnId)) {
      result.put("status", PubHandlerStatusEnum.ERROR.getValue());
      result.put("msg", "psnId不合法,psnId=" + psnId);
    }
    if (null == pubPdwhDetailService.getByPubId(pdwhPubId)) {
      result.put("status", PubHandlerStatusEnum.ERROR.getValue());
      result.put("msg", "成果不存在，pdwhPubId=" + pdwhPubId);
    }
    return result;
  }

  private Long checkId(String des3Id) {
    if (StringUtils.isEmpty(des3Id)) {
      return null;
    }
    return Long.valueOf(Des3Utils.decodeFromDes3(des3Id));
  }

  @Override
  public void updateConfirmResult(Long pdwhPubId, Long psnId, Long snsPubId, Integer result) throws ServiceException {
    try {
      pubAssignLogDAO.updateConfirmResult(pdwhPubId, psnId, snsPubId, result);
    } catch (Exception e) {
      logger.error("更新成果认领结果出错！", e);
      throw new ServiceException(e);
    }
  }

  @Override
  public String ignorePubComfirm(String des3PdwhPubId, String des3PsnId) throws ServiceException {
    Map<String, String> result = new HashMap<>();
    try {
      // 参数的处理
      Long pdwhPubId = checkId(des3PdwhPubId);
      Long psnId = checkId(des3PsnId);
      result = checkParams(pdwhPubId, psnId);
      // 业务的处理
      if (CollectionUtils.isEmpty(result)) {
        updateConfirmResult(pdwhPubId, psnId, null, 2);
        result.put("status", PubHandlerStatusEnum.SUCCESS.getValue());
      }
    } catch (Exception e) {
      logger.error("忽略成果认领业务失败", e);
      result.put("status", PubHandlerStatusEnum.ERROR.getValue());
      result.put("msg", e.getMessage());
    }
    return JacksonUtils.jsonMapSerializer(result);
  }

  @Override
  public Long getPubConfirmCount(Long psnId) throws ServiceException {
    List<Long> confirmPubIds = pubAssignLogDAO.queryPubConfirmCount(psnId);
    if (confirmPubIds != null && confirmPubIds.size() > 0) {
      return pubPdwhDAO.getPubCount(confirmPubIds);
    }
    return 0L;
  }

}
