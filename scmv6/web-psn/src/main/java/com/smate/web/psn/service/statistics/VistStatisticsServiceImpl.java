package com.smate.web.psn.service.statistics;

import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.smate.core.base.consts.dao.ConstRegionDao;
import com.smate.core.base.utils.dao.security.PersonDao;
import com.smate.core.base.utils.date.DateUtils;
import com.smate.core.base.utils.number.NumberUtils;
import com.smate.web.psn.dao.statistics.CityIpDao;
import com.smate.web.psn.dao.statistics.VistStatisticsDao;
import com.smate.web.psn.exception.ServiceException;
import com.smate.web.psn.model.statistics.VistStatistics;

/**
 * 访问他人记录
 * 
 * @author zx
 *
 */
@Service("vistStatisticsService")
@Transactional(rollbackFor = Exception.class)
public class VistStatisticsServiceImpl implements VistStatisticsService {

  protected final Logger logger = LoggerFactory.getLogger(getClass());

  @Autowired
  private VistStatisticsDao vistStatisticsDao;
  @Autowired
  private CityIpDao cityIpDao;
  @Autowired
  private ConstRegionDao constRegionDao;
  @Autowired
  private PsnStatisticsService psnStatisticsService;
  @Autowired
  private PersonDao personDao;

  /**
   * 添加他人访问主页的记录
   */
  @Override
  public void addVistRecord(Long psnId, Long vistPsnId, Long actionKey, Integer actionType, String ip)
      throws ServiceException {
    try {
      Date nowDate = new Date();
      long formateDate = DateUtils.getDateTime(nowDate);
      Long ipLong = 0L;
      Long regionId = 0L;
      try {
        ipLong = buildIPParam(ip);
        if (ipLong != null) {// 根据ip获取省份id
          String regionName = cityIpDao.findProvinceByIp(ipLong);
          regionId = constRegionDao.getRegionIdByName(regionName == null ? "" : regionName);
        }
        if (NumberUtils.isNullOrZero(regionId)) {// 根据个人设置获取省份id
          regionId = personDao.getPsnRegionIdByObjectId(psnId);
        }
      } catch (Exception e) {
        logger.error("添加vistStatistics记录的regionId出错，LongIp=" + ipLong + "StringIp=" + ip, e);
      }
      regionId = regionId == null ? 0L : regionId;
      VistStatistics vistStatistics = vistStatisticsDao.findVistRecord(psnId, vistPsnId, actionKey, actionType,
          formateDate, ip, regionId == null ? 0 : regionId);
      if (vistStatistics == null) {
        vistStatistics = new VistStatistics();
        vistStatistics.setPsnId(psnId);
        vistStatistics.setVistPsnId(vistPsnId);
        vistStatistics.setActionKey(actionKey);
        vistStatistics.setActionType(actionType);
        vistStatistics.setCreateDate(nowDate);
        vistStatistics.setFormateDate(formateDate);
        vistStatistics.setCount(1l);
        vistStatistics.setIp(ip);
        vistStatistics.setProvinceRegionId(regionId == null ? 0 : regionId);

      } else {
        vistStatistics.setCreateDate(nowDate);
        vistStatistics.setFormateDate(formateDate);
        vistStatistics.setCount(vistStatistics.getCount() + 1);
      }
      vistStatisticsDao.save(vistStatistics);
      // 增加人员阅读统计数
      psnStatisticsService.updatePsnVisitSum(vistPsnId);
    } catch (Exception e) {
      logger.error("保存访问记录出错！PsnId=" + psnId + " vistPsnId=" + vistPsnId + " actionKey=" + actionKey + " actionType"
          + actionType, e);
      throw new ServiceException(e);
    }
  }

  /**
   * 构建IP地址参数.
   * 
   * @param ip
   * @return
   */
  private Long buildIPParam(String ip) throws Exception {
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
