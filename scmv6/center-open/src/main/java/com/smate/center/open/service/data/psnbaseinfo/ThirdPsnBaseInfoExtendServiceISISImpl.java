package com.smate.center.open.service.data.psnbaseinfo;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.transaction.annotation.Transactional;

import com.smate.core.base.psn.dao.PsnProfileUrlDao;
import com.smate.core.base.psn.dao.WorkHistoryDao;
import com.smate.core.base.psn.model.PsnProfileUrl;
import com.smate.core.base.utils.constant.ShortUrlConst;
import com.smate.core.base.utils.model.security.Person;
import com.smate.core.base.utils.string.ServiceUtil;

/**
 * isis 项目 获取人员详细信息服务
 * 
 * @author tsz
 *
 */
@Transactional(rollbackFor = Exception.class)
public class ThirdPsnBaseInfoExtendServiceISISImpl implements ThirdPsnBaseInfoExtendService {

  protected Logger logger = LoggerFactory.getLogger(getClass());

  @Autowired
  private WorkHistoryDao workHistoryDao;
  @Autowired
  private PsnProfileUrlDao psnProfileUrlDao;

  @Value("${domainscm}")
  private String domain;

  @Override
  public Map<String, Object> extendPsnInfo(Person person, Long openId) {

    Map<String, Object> map = new HashMap<String, Object>();
    map.put(BaseInfoConsts.RESULT_MAP_FIRSTNAME,
        StringUtils.isNotEmpty(person.getFirstName()) ? person.getFirstName() : "");
    map.put(BaseInfoConsts.RESULT_MAP_LASTNAME,
        StringUtils.isNotBlank(person.getLastName()) ? person.getLastName() : "");
    map.put(BaseInfoConsts.RESULT_MAP_MOBILE, StringUtils.isNotBlank(person.getMobile()) ? person.getMobile() : "");
    // 站外访问短地址地址
    map.put("outsidePsnUrl", buildPsnShortUrl(person.getPersonId()));

    if (person.getSex() != null) {
      map.put(BaseInfoConsts.RESULT_MAP_SEX, person.getSex() == 1 ? "男" : "女");
    } else {
      map.put(BaseInfoConsts.RESULT_MAP_SEX, "");
    }
    map.put("insName", StringUtils.isNotBlank(person.getInsName()) ? person.getInsName() : "");
    map.put("department", StringUtils.isNotBlank(person.getDepartment()) ? person.getDepartment() : "");
    map.put("position", StringUtils.isNotBlank(person.getPosition()) ? person.getPosition() : "");
    return map;
  }

  private String buildPsnShortUrl(Long psnId) {
    String url;
    PsnProfileUrl psnProfileUrl = psnProfileUrlDao.find(psnId);
    if (psnProfileUrl != null && StringUtils.isNotBlank(psnProfileUrl.getPsnIndexUrl())) {
      url = domain + "/" + ShortUrlConst.P_TYPE + "/" + psnProfileUrl.getPsnIndexUrl();
    } else {
      url = domain + "/scmwebsns/in/view?des3PsnId=" + ServiceUtil.encodeToDes3(psnId + "");
    }
    return url;
  }

}
