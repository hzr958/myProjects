package com.smate.center.open.service.data.psnbaseinfo;

import java.util.HashMap;
import java.util.Map;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.transaction.annotation.Transactional;

import com.smate.center.open.consts.OpenConsts;
import com.smate.center.open.dao.publication.PubSimpleDao;
import com.smate.center.open.service.login.ThirdLoginService;
import com.smate.core.base.project.dao.ProjectDao;
import com.smate.core.base.psn.dao.PsnProfileUrlDao;
import com.smate.core.base.psn.dao.PsnStatisticsDao;
import com.smate.core.base.psn.model.PsnProfileUrl;
import com.smate.core.base.psn.model.PsnStatistics;
import com.smate.core.base.utils.model.security.Person;

/**
 * 科创云在添加参与人时，需要通过科研之友获取参与人的个人主页
 * 
 * @author ajb
 *
 */
@Transactional(rollbackFor = Exception.class)
public class ThirdPsnBaseInfoExtendServiceKCYImpl implements ThirdPsnBaseInfoExtendService {

  protected Logger logger = LoggerFactory.getLogger(getClass());
  public static final Integer PATENT_TYPE = 5;
  public final static String SCM_TOKEN = "00000000";// 科研之友的token
  public final static Integer CREATE_TYPE = 2; // 人员注册 创建类型

  @Autowired
  private PsnProfileUrlDao psnProfileUrlDao;
  @Autowired
  private PsnStatisticsDao psnStatisticsDao;
  @Value("${domainscm}")
  private String domainScm;
  @Resource
  private ThirdLoginService thirdLoginService;


  @Override
  public Map<String, Object> extendPsnInfo(Person person, Long openId) {

    Map<String, Object> map = new HashMap<String, Object>();
    PsnProfileUrl psnProfileUrl = psnProfileUrlDao.get(person.getPersonId());
    String psnHomeUrl = psnProfileUrl != null ? this.domainScm + "/P/" + psnProfileUrl.getPsnIndexUrl() : "";
    map.put("psnHomeUrl", psnHomeUrl);
    PsnStatistics statistics = psnStatisticsDao.get(person.getPersonId());
    map.put("prjCount", statistics == null ? 0L : statistics.getPrjSum());
    map.put("pubCount", statistics == null ? 0L : statistics.getPubSum());
    map.put("patCount", statistics == null ? 0L : statistics.getPatentSum());
    openId = thirdLoginService.getOpenId(SCM_TOKEN, person.getPersonId(), CREATE_TYPE);
    map.put(OpenConsts.MAP_OPENID, openId);


    return map;
  }


}
