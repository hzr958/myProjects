package com.smate.center.task.service.fund;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import javax.transaction.Transactional;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.smate.center.mail.connector.model.MailLinkInfo;
import com.smate.center.mail.connector.service.MailHandleOriginalDataService;
import com.smate.center.task.dao.fund.rcmd.ConstFundAgencyDao;
import com.smate.center.task.dao.fund.rcmd.ConstFundCategoryDao;
import com.smate.center.task.dao.fund.rcmd.ConstFundCategoryDisDao;
import com.smate.center.task.dao.fund.sns.PsnFundRecommendDao;
import com.smate.center.task.dao.open.OpenUserUnionDao;
import com.smate.center.task.dao.sns.psn.WeChatMessagePsnDao;
import com.smate.center.task.dao.sns.quartz.CategoryMapBaseDao;
import com.smate.center.task.model.fund.rcmd.ConstFundAgency;
import com.smate.center.task.model.fund.rcmd.ConstFundCategory;
import com.smate.center.task.model.fund.sns.PsnFundRecommend;
import com.smate.center.task.model.sns.psn.WeChatMessagePsn;
import com.smate.center.task.model.sns.pub.CategoryMapBase;
import com.smate.center.task.service.email.PromoteMailInitDataService;
import com.smate.center.task.service.oauth.OauthLoginService;
import com.smate.core.base.psn.dao.PsnProfileUrlDao;
import com.smate.core.base.psn.model.PsnProfileUrl;
import com.smate.core.base.utils.constant.ShortUrlConst;
import com.smate.core.base.utils.dao.security.PersonDao;
import com.smate.core.base.utils.dao.wechat.WeChatRelationDao;
import com.smate.core.base.utils.json.JacksonUtils;
import com.smate.core.base.utils.model.security.Person;
import com.smate.core.base.utils.security.SecurityUtils;
import com.smate.core.base.utils.string.ServiceUtil;

@Service("psnFundRcmdMailService")
@Transactional(rollbackOn = Exception.class)
public class PsnFundRcmdMailServiceImpl implements PsnFundRcmdMailService {
  private final Logger logger = LoggerFactory.getLogger(getClass());
  @Value("${domainscm}")
  private String domainScm;
  @Autowired
  private PromoteMailInitDataService promoteMailInitDataService;
  @Autowired
  private OauthLoginService oauthLoginService;
  @Autowired
  private ConstFundCategoryDao constFundCategoryDao;
  @Autowired
  private PsnFundRecommendDao psnFundRecommendDao;
  @Autowired
  private ConstFundAgencyDao constFundAgencyDao;
  @Autowired
  private PsnProfileUrlDao psnProfileUrlDao;
  @Autowired
  private ConstFundCategoryDisDao constFundCategoryDisDao;
  @Autowired
  private CategoryMapBaseDao categoryMapBaseDao;
  @Autowired
  private PersonDao personDao;
  @Autowired
  private OpenUserUnionDao openUserUnionDao;
  @Autowired
  private WeChatMessagePsnDao weChatMessagePsnDao;
  @Autowired
  private WeChatRelationDao weChatRelationDao;
  @Autowired
  private MailHandleOriginalDataService mailHandleOriginalDataService;

  @Override
  public List<Long> getFundList() {
    return constFundCategoryDao.getFundIds();
  }

  @Override
  public List<Long> getPsnIdsByFund(List<Long> fundIds) {
    return psnFundRecommendDao.getPsnIdsByFund(fundIds);
  }

  @Override
  public Person getPsnInfo(Long psnId) {
    return personDao.getPeronsForEmail(psnId);
  }

  /**
   * 获取人员的推荐基金列表.
   *
   * @param psnId
   * @return
   */
  @Override
  public List<PsnFundRecommend> getPsnFundRecommendList(Long psnId, List<Long> fundIds) {
    List<PsnFundRecommend> recomList = psnFundRecommendDao.getPsnFundRecommendList(psnId, fundIds);
    if (CollectionUtils.isNotEmpty(recomList)) {
      try {
        for (PsnFundRecommend fundRecommend : recomList) {
          ConstFundCategory fund = constFundCategoryDao.get(fundRecommend.getFundId());
          ConstFundAgency fundAgency = constFundAgencyDao.get(fund.getAgencyId());
          List<Long> disIds = constFundCategoryDisDao.getFundDiscIdList(fundRecommend.getFundId());
          StringBuffer disName = new StringBuffer();
          if (CollectionUtils.isNotEmpty(disIds)) {
            List<CategoryMapBase> baseList = categoryMapBaseDao.findCategoryMapBases(disIds);
            for (int i = 0; i < baseList.size(); i++) {
              disName.append(StringUtils.isNotBlank(baseList.get(i).getCategoryZh()) ? baseList.get(i).getCategoryZh()
                  : baseList.get(i).getCategoryEn());
              if (i < baseList.size() - 1) {
                disName.append("，");
              }
            }
          }
          if (StringUtils.isNotBlank(fundAgency.getNameEn()) || StringUtils.isNotBlank(fundAgency.getNameZh())) {
            fundRecommend.setFundGuideUrl(fund.getGuideUrl());
            fundRecommend.setAgencyId(fundAgency.getId());
            // 获取到的基金代理名和基金类别名称中英文不一致
            fundRecommend
                .setCategoryViewName(StringUtils.isNotBlank(fund.getNameZh()) ? fund.getNameZh() : fund.getNameEn());
            fundRecommend.setAgencyViewName(
                StringUtils.isNotBlank(fundAgency.getNameZh()) ? fundAgency.getNameZh() : fundAgency.getNameEn());
            fundRecommend.setFundStartDate(fund.getStartDate());
            fundRecommend.setFundEndDate(fund.getEndDate());
            fundRecommend.setDisName(disName.toString());
          }

        }
      } catch (Exception e) {
        e.printStackTrace();
      }
    }
    return recomList;
  }

  @Override
  public void buildRcmdMailInfo(Person person, List<PsnFundRecommend> reFundList) {
    try {
      Long currentUserId = SecurityUtils.getCurrentUserId();
      Long receivePsnId = person.getPersonId();
      String receivePsnName = StringUtils.isNotBlank(person.getName()) ? person.getName()
          : person.getFirstName() + " " + person.getLastName();
      int counts = reFundList.size();
      Long openId = oauthLoginService.getOpenId("00000000", person.getPersonId(), 2);
      String AID = oauthLoginService.getAutoLoginAID(openId, "ResetPWD");
      String viewUrl = domainScm + "/prjweb/fund/main?module=recommend&" + "AID=" + AID + "&resetpwd=true";
      String psnUrl = null;
      // 个人主页链接
      PsnProfileUrl profileUrl = psnProfileUrlDao.get(person.getPersonId());
      if (profileUrl != null && StringUtils.isNotBlank(profileUrl.getPsnIndexUrl())) {
        psnUrl = domainScm + "/" + ShortUrlConst.P_TYPE + "/" + profileUrl.getPsnIndexUrl();
      }
      // 定义接口接收的参数
      Map<String, String> paramData = new HashMap<String, String>();
      // 定义构造邮件模版参数集
      Map<String, String> mailData = new HashMap<String, String>();
      // 构造必需的参数
      String email = person.getEmail();
      Integer templateCode = 10028;
      String msg = "资助机会推荐";
      mailHandleOriginalDataService.buildNecessaryParam(email, currentUserId, receivePsnId, templateCode, msg,
          paramData);
      // 定义要跟踪的链接集，系统会生成短地址并跟踪访问记录
      List<String> linkList = new ArrayList<String>();
      MailLinkInfo l1 = new MailLinkInfo();
      l1.setKey("domainUrl");
      l1.setUrl(domainScm);
      l1.setUrlDesc("科研之友首页");
      linkList.add(JacksonUtils.jsonObjectSerializer(l1));
      MailLinkInfo l2 = new MailLinkInfo();
      l2.setKey("psnUrl");
      l2.setUrl(psnUrl);
      l2.setUrlDesc("个人主页");
      linkList.add(JacksonUtils.jsonObjectSerializer(l2));
      MailLinkInfo l3 = new MailLinkInfo();
      l3.setKey("viewUrl");
      l3.setUrl(viewUrl);
      l3.setUrlDesc("查看");
      linkList.add(JacksonUtils.jsonObjectSerializer(l3));

      List<String> subjectParamLinkList = new ArrayList<String>();
      subjectParamLinkList.add(receivePsnName);
      subjectParamLinkList.add(counts + "");
      mailData.put("subjectParamList", JacksonUtils.listToJsonStr(subjectParamLinkList));
      // 邮件模版需要的其他参数可以继续往mailData添加,如：
      mailData.put("receivePsnName", receivePsnName);
      mailData.put("counts", counts + "");

      this.getFunds(reFundList, mailData, linkList);
      mailData.put("linkList", JacksonUtils.listToJsonStr(linkList));
      paramData.put("mailData", JacksonUtils.mapToJsonStr(mailData));
      Map<String, String> resutlMap = mailHandleOriginalDataService.doHandle(paramData);
      if (!"success".equals(resutlMap.get("result"))) {
        logger.error(resutlMap.get("msg"));
      }
    } catch (Exception e) {
      logger.error("保存邮件发送信息错误", e);
    }
  }

  @SuppressWarnings({"unchecked", "rawtypes"})
  public void getFunds(List<PsnFundRecommend> reFundList, Map<String, String> mailData, List<String> linkList) {
    Map<String, String> objListMap = new HashMap<String, String>();
    List<HashMap<String, String>> funds = new ArrayList<HashMap<String, String>>();
    for (int i = 0; i < reFundList.size(); i++) {
      if (i < MAIL_LIST_SIZE_LIMIT.intValue()) {
        PsnFundRecommend fund = reFundList.get(i);
        if (i == 0) {
          // 邮件标题中需要用到的基金名称
          mailData.put("mFundName", "".equals(fund.getCategoryViewName()) || fund.getCategoryViewName() == null ? ""
              : fund.getCategoryViewName());
        }
        HashMap tempMap = new HashMap<String, String>();
        tempMap.put("fundId", fund.getFundId());
        tempMap.put("fundName", fund.getCategoryViewName());
        tempMap.put("agencyName", fund.getAgencyViewName());
        tempMap.put("disName", fund.getDisName());
        String startDate = null;
        String endDate = null;
        if (fund.getFundStartDate() != null && StringUtils.isNotBlank(fund.getFundStartDate().toString())) {
          startDate = new SimpleDateFormat("yyyy-MM-dd").format(fund.getFundStartDate());
        }
        if (fund.getFundEndDate() != null && StringUtils.isNotBlank(fund.getFundEndDate().toString())) {
          endDate = new SimpleDateFormat("yyyy-MM-dd").format(fund.getFundEndDate());
        }
        tempMap.put("startDate", startDate);
        tempMap.put("endDate", endDate);
        funds.add(tempMap);
        MailLinkInfo l4 = new MailLinkInfo();
        l4.setKey("fundDetail_" + i);
        l4.setUrl(domainScm + "/prjweb/funddetails/show?encryptedFundId="
            + ServiceUtil.encodeToDes3(fund.getFundId().toString()));
        l4.setUrlDesc("基金详情");
        linkList.add(JacksonUtils.jsonObjectSerializer(l4));
      }
    }
    objListMap.put("fundList", JacksonUtils.listToJsonStr(funds));
    mailData.put("objListMap", JacksonUtils.mapToJsonStr(objListMap));
  }

  /**
   * 处理solr返回的时间字符串
   *
   * @param dateStr
   * @param result
   * @return
   * @throws ParseException
   */
  private String formateDate(String dateStr, String result) {
    try {
      if (StringUtils.isNotBlank(dateStr)) {
        SimpleDateFormat sdf = new SimpleDateFormat("MMM d, yyyy K:m:s a", Locale.ENGLISH);
        Date date = sdf.parse(dateStr);
        if (date != null) {
          result = new SimpleDateFormat("yyyy-MM-dd").format(date);
        }
      }
    } catch (Exception e) {
      logger.error("时间转换出错， dateStr = " + dateStr, e);
    }
    return result;
  }

  @Override
  public boolean getDataByEmail(String email) {
    return promoteMailInitDataService.getDataByEmail(email);

  }

  @Override
  public void saveWeChatMessagePsn(Person person, PsnFundRecommend fund, Long openId, int totalFund) {
    /*
     * {"first":"你有10篇","keyword1":"基金推荐",
     * "keyword2":"你可能是\"来自中国猕猴的环孢子虫新种Cyclospora macacae的鉴定分析\"等10篇论文的作者",
     * "remark":"2016-10-31更新,请点击认领","smateTempId":"2"}
     */
    Map<String, String> dataMap = new HashMap<String, String>();
    String psnName = StringUtils.isNotBlank(person.getZhName()) ? person.getZhName() : person.getEname();
    String name = StringUtils.isNotBlank(psnName) ? psnName : "你好";
    // 这里的空格不能去掉
    dataMap.put("first", name + ":\n     你有" + totalFund + "个最新的资助机会，立刻查看申报吧");
    dataMap.put("keyword1", fund.getCategoryViewName());
    dataMap.put("keyword2", fund.getAgencyViewName());
    String startDate = null;
    String endDate = null;
    if (fund.getFundStartDate() != null && StringUtils.isNotBlank(fund.getFundStartDate().toString())) {
      startDate = new SimpleDateFormat("yyyy-MM-dd").format(fund.getFundStartDate());
    }
    if (fund.getFundEndDate() != null && StringUtils.isNotBlank(fund.getFundEndDate().toString())) {
      endDate = new SimpleDateFormat("yyyy-MM-dd").format(fund.getFundEndDate());
    }
    if (StringUtils.isNotBlank(startDate) && StringUtils.isNotBlank(endDate)) {
      dataMap.put("keyword3", startDate + "——" + endDate);
    } else if (StringUtils.isBlank(startDate)) {
      dataMap.put("keyword3", endDate);
    } else if (StringUtils.isBlank(endDate)) {
      dataMap.put("keyword3", startDate);
    }
    String disName = StringUtils.isNotBlank(fund.getDisName()) ? fund.getDisName() : "不限";
    dataMap.put("keyword4", disName + "\n");
    dataMap.put("remark", "连接科研与创新人员、分享与发现知识、让创新更高效");
    dataMap.put("smateTempId", "4");
    if (openId != null) {
      WeChatMessagePsn message = new WeChatMessagePsn();
      message.setContent(JacksonUtils.mapToJsonStr(dataMap));
      message.setCreateTime(new Date());
      message.setOpenId(openId);
      message.setStatus(0);
      message.setToken("00000000");
      weChatMessagePsnDao.save(message);
    }
  }

  @Override
  public Long getUserOpenId(Long personId, String token) {
    return openUserUnionDao.getUserOpenId(personId, token);
  }

  @Override
  public boolean getDataByOpenId(Long openId, String token) {
    List<Long> ids = weChatMessagePsnDao.getDataByOpenId(openId, token);
    if (CollectionUtils.isEmpty(ids)) {
      List<String> weChatOpenIdList = weChatRelationDao.findWeChatOpenIdList(openId);
      if (CollectionUtils.isNotEmpty(weChatOpenIdList)) {
        return true;
      }
    }
    return false;
  }

  @Override
  public void updateSendmailStatus(Long psnId, List<Long> fundIds) {
    psnFundRecommendDao.updateSendmailStatus(psnId, fundIds);
  }

}
