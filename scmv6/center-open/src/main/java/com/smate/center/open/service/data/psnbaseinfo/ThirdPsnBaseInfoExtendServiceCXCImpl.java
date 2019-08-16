package com.smate.center.open.service.data.psnbaseinfo;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.transaction.annotation.Transactional;

import com.smate.center.open.cache.OpenCacheService;
import com.smate.center.open.dao.consts.ConstRegionDao;
import com.smate.core.base.psn.dao.PsnProfileUrlDao;
import com.smate.core.base.psn.dao.WorkHistoryDao;
import com.smate.core.base.psn.model.PsnProfileUrl;
import com.smate.core.base.psn.model.WorkHistory;
import com.smate.core.base.utils.model.security.Person;
import com.smate.core.base.utils.security.Des3Utils;

/**
 * 创新城 项目 获取人员详细信息服务
 * 
 * @author tsz
 *
 */
@Transactional(rollbackFor = Exception.class)
public class ThirdPsnBaseInfoExtendServiceCXCImpl implements ThirdPsnBaseInfoExtendService {

  protected Logger logger = LoggerFactory.getLogger(getClass());

  @Autowired
  private ConstRegionDao constRegionDao;
  @Autowired
  private WorkHistoryDao workHistoryDao;
  @Autowired
  private OpenCacheService openCacheService;
  @Autowired
  private PsnProfileUrlDao psnProfileUrlDao;
  @Value("${domainscm}")
  private String domainScm;

  /**
   * 邮箱,姓名，手机号 ，firstName,lastName,头像,所在单位,部门，职称,单位地址
   */
  @Override
  public Map<String, Object> extendPsnInfo(Person person, Long openId) {

    Map<String, Object> map = new HashMap<String, Object>();
    map.put(BaseInfoConsts.RESULT_MAP_FIRSTNAME,
        StringUtils.isNotBlank(person.getFirstName()) ? person.getFirstName() : "");
    map.put(BaseInfoConsts.RESULT_MAP_LASTNAME,
        StringUtils.isNotBlank(person.getLastName()) ? person.getLastName() : "");
    map.put(BaseInfoConsts.RESULT_MAP_MOBILE, StringUtils.isNotBlank(person.getMobile()) ? person.getMobile() : "");
    map.put(BaseInfoConsts.RESULT_MAP_OTHERNAME,
        StringUtils.isNotBlank(person.getOtherName()) ? person.getOtherName() : "");

    try {
      WorkHistory workHistory = workHistoryDao.getWorkHistory(person.getPersonId());
      if (workHistory != null) {
        map.put("insName", StringUtils.isNotBlank(workHistory.getInsName()) ? workHistory.getInsName() : "");
        map.put("department", StringUtils.isNotBlank(workHistory.getDepartment()) ? workHistory.getDepartment() : "");
        map.put("position", StringUtils.isNotBlank(workHistory.getPosition()) ? workHistory.getPosition() : "");
      }
    } catch (Exception e) {
      logger.error("获取人员信息中 获取首要工作经历出错 openid=" + openId, e);
    }
    PsnProfileUrl psnUrl = psnProfileUrlDao.find(person.getPersonId());
    if (psnUrl != null && StringUtils.isNotBlank(psnUrl.getUrl())) {
      map.put("psnProfileUrl", domainScm + BaseInfoConsts.PSN_HOMEPAGE_URL + "?des3PsnId="
          + Des3Utils.encodeToDes3(person.getPersonId().toString()));
    } else {
      map.put("psnProfileUrl", "");
    }
    if (person.getSex() != null) {
      map.put(BaseInfoConsts.RESULT_MAP_SEX, person.getSex() == 1 ? "男" : "女");
    } else {
      map.put(BaseInfoConsts.RESULT_MAP_SEX, "");
    }
    // 获取缓存 动态 值
    //
    Object obj = openCacheService.get("DYN_OPENID_KEY_CACHE", person.getPersonId().toString());
    if (obj != null) {
      map.put("key", obj);
    }
    // 人员加密ID
    map.put(BaseInfoConsts.PSN_DES3_ID, person.getPersonDes3Id());
    return map;
  }

}
