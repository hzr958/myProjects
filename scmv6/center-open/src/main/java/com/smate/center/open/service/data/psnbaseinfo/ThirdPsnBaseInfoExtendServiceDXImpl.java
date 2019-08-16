package com.smate.center.open.service.data.psnbaseinfo;

import com.smate.core.base.psn.dao.PsnProfileUrlDao;
import com.smate.core.base.psn.dao.PsnStatisticsDao;
import com.smate.core.base.psn.model.PsnProfileUrl;
import com.smate.core.base.psn.model.PsnStatistics;
import com.smate.core.base.utils.model.security.Person;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.Map;

/**
 * 广东农业科学院,华中师范大学,三峡大学,哈尔滨工程大学,华东师范大学,合肥工业大学 获取人员信息
 * 
 * @author aijiangbin
 * @date 2018年8月23日
 */
@Transactional(rollbackFor = Exception.class)
public class ThirdPsnBaseInfoExtendServiceDXImpl implements ThirdPsnBaseInfoExtendService {

  protected Logger logger = LoggerFactory.getLogger(getClass());

  @Autowired
  private PsnProfileUrlDao psnProfileUrlDao;
  @Autowired
  private PsnStatisticsDao psnStatisticsDao;
  @Value("${domainscm}")
  private String domainScm;



  @Override
  public Map<String, Object> extendPsnInfo(Person person, Long openId) {

    Map<String, Object> map = new HashMap<String, Object>();
    PsnProfileUrl psnProfileUrl = psnProfileUrlDao.get(person.getPersonId());
    String psnHomeUrl = psnProfileUrl != null ? this.domainScm + "/P/" + psnProfileUrl.getPsnIndexUrl() : "";
    map.put("psnHomeUrl", psnHomeUrl);
    PsnStatistics statistics = psnStatisticsDao.get(person.getPersonId());
    map.put("openPubCount", statistics == null ? 0L : statistics.getOpenPubSum());
    map.put("allPubCount", statistics == null ? 0L : statistics.getPubSum());
    map.put("psnId", person.getPersonDes3Id());
    map.put("insName", person.getInsName() != null ? person.getInsName() : "");

    return map;
  }


}
