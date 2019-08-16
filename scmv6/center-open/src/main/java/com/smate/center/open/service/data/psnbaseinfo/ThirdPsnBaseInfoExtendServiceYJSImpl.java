package com.smate.center.open.service.data.psnbaseinfo;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.transaction.annotation.Transactional;

import com.smate.center.open.dao.publication.PubSimpleDao;
import com.smate.center.open.dao.rcmd.pub.PublicationConfirmDao;
import com.smate.center.open.service.data.autologin.AutoLoginTypeEnum;
import com.smate.center.open.service.user.UserService;
import com.smate.core.base.psn.dao.PsnStatisticsDao;
import com.smate.core.base.psn.model.PsnStatistics;
import com.smate.core.base.utils.model.security.Person;

/**
 * 云计算，获取人员信息
 * 
 * @author ajb
 *
 */
@Transactional(rollbackFor = Exception.class)
public class ThirdPsnBaseInfoExtendServiceYJSImpl implements ThirdPsnBaseInfoExtendService {

  protected Logger logger = LoggerFactory.getLogger(getClass());
  public static final Integer PATENT_TYPE = 5;


  @Autowired
  private PsnStatisticsDao psnStatisticsDao;
  @Autowired
  private PublicationConfirmDao publicationConfirmDao;
  @Autowired
  private UserService UserService;
  @Value("${domainscm}")
  private String domainScm;

  /**
   * 取成果统计数，专利统计数，待认领成果统计 成果列表链接，专利列表链接（成果列表链接自动按专利过滤）, 待认领成果列表链接.
   */
  @Override
  public Map<String, Object> extendPsnInfo(Person person, Long openId) {

    Map<String, Object> map = new HashMap<String, Object>();
    PsnStatistics psnStatistics = psnStatisticsDao.get(person.getPersonId());
    Integer pubCount = psnStatistics.getPubSum();
    Integer patCount = psnStatistics.getPatentSum();
    Long pubConfirmCount = publicationConfirmDao.queryPubConfirmCount(person.getPersonId());
    map.put("pubCount", pubCount == null ? 0 : pubCount);
    map.put("patCount", patCount == null ? 0 : patCount);
    map.put("pubConfirmCount", pubConfirmCount);

    String aid = buildAid(person, openId);
    String pubConfirmListUrl =
        this.domainScm + "/psnweb/homepage/show?module=pub&showDialogPub=showPubConfirmMore&AID=" + aid;
    String pubListUrl = this.domainScm + "/psnweb/homepage/show?module=pub&AID=" + aid;
    String patListUrl = this.domainScm + "/psnweb/homepage/show?module=pub&filterPubType=5&AID=" + aid;
    map.put("pubConfirmListUrl", pubConfirmListUrl);
    map.put("pubListUrl", pubListUrl);
    map.put("patListUrl", patListUrl);
    return map;
  }

  /**
   * 构建aid
   * 
   * @param person
   * @param openId
   * @return
   */
  private String buildAid(Person person, Long openId) {
    String autoLoginType = "YJS";
    long overTimeMill = AutoLoginTypeEnum.valueOf(autoLoginType).toLong();
    Date now = new Date();
    Date overTime = new Date(now.getTime() + overTimeMill);
    String token = "00000000";
    String aid = UserService.getAID(openId, autoLoginType, overTime, person.getPersonId(), token);
    return aid;
  }

}
