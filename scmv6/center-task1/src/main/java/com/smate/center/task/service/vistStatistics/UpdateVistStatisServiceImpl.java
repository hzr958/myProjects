package com.smate.center.task.service.vistStatistics;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.transaction.Transactional;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.smate.center.task.dao.sns.psn.CityIpDao;
import com.smate.center.task.dao.sns.psn.VistStatisticsDao;
import com.smate.center.task.dao.sns.quartz.ConstRegionDao;
import com.smate.center.task.model.sns.psn.VistStatistics;
import com.smate.core.base.utils.dao.security.PersonDao;

@Service("updateVistStatisService")
@Transactional(rollbackOn = Exception.class)
public class UpdateVistStatisServiceImpl implements UpdateVistStatisService {
  private final Logger logger = LoggerFactory.getLogger(getClass());
  @Autowired
  private VistStatisticsDao vistStatisticsDao;
  @Autowired
  private CityIpDao cityIpDao;
  @Autowired
  private ConstRegionDao constRegionDao;
  @Autowired
  private PersonDao personDao;

  @Override
  public List<VistStatistics> getVistStatisId(Long starId, Integer size) throws Exception {
    return vistStatisticsDao.getVistStatisIds(starId, size);
  }

  @Override
  public void updateVistRegionId(VistStatistics vist) {
    Long ip = 0L;
    try {
      ip = buildIPParam(vist.getIp());
      if (ip != null) {
        String regionName = cityIpDao.findProvinceByIp(ip);
        if (StringUtils.isBlank(regionName)) {
          Long regionId = personDao.getPsnRegionIdByObjectId(vist.getPsnId());
          List<Long> superRegionIds = constRegionDao.getSuperRegionList(regionId == null ? 0 : regionId, true);
          if (CollectionUtils.isNotEmpty(superRegionIds) && superRegionIds.size() >= 2) {
            if (new Long(156).equals(superRegionIds.get(superRegionIds.size() - 1))) {
              vistStatisticsDao.updateRegionId(vist.getId(), superRegionIds.get(superRegionIds.size() - 2));
            }
          }
        } else {
          Long regionId = constRegionDao.getRegionIdByName(regionName == null ? "" : regionName);
          vistStatisticsDao.updateRegionId(vist.getId(), regionId);
        }
      }
    } catch (Exception e) {
      logger.error("更新vistStatistics表的regionId出错，LongIp=" + ip + "StringIp=" + vist.getIp(), e);
    }

  }

  /**
   * 构建IP地址参数.
   * 
   * @param ip
   * @return
   */
  private Long buildIPParam(String ip) throws Exception {
    if (ip == null) {
      return 0L;
    }
    /**
     * 判断IP格式和范围
     */
    String rexp = "([1-9]|[1-9]\\d|1\\d{2}|2[0-4]\\d|25[0-5])(\\.(\\d|[1-9]\\d|1\\d{2}|2[0-4]\\d|25[0-5])){3}";
    Pattern pat = Pattern.compile(rexp);
    Matcher mat = pat.matcher(ip);
    if (!mat.find()) {// 不是ip地址
      return 0L;
    }
    String[] ipArray = ip.split("\\.");
    Long result = 0l;
    for (int i = 0; i < ipArray.length; i++) {
      Double ipData = (Long.valueOf(ipArray[i]) * Math.pow(256, 3 - i));
      result = result + ipData.longValue();
    }
    return result;
  }
}
