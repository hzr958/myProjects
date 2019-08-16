package com.smate.center.task.service.pdwh.quartz;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.smate.center.task.dao.sns.psn.CityIpDao;
import com.smate.center.task.dao.sns.psn.VistStatisticsDao;
import com.smate.center.task.dao.sns.quartz.ConstRegionDao;
import com.smate.center.task.model.sns.psn.VistStatistics;
import com.smate.center.task.v8pub.dao.sns.PubViewDAO;
import com.smate.center.task.v8pub.sns.po.PubViewPO;
import com.smate.core.base.psn.dao.PsnPubDAO;
import com.smate.core.base.utils.dao.security.PersonDao;
import com.smate.core.base.utils.date.DateUtils;
import com.smate.core.base.utils.number.NumberUtils;

/**
 * 通过这个service层实现 修复成果改造导致VIST_STATISTICS表中缺失的部分数据和部分成果查看数量 执行数据插入的一次性任务
 * 
 * @author syl
 *
 */
@Transactional(rollbackFor = Exception.class)
@Service("InsertDataFromV_pub_viewToVist_statisticsService")
public class InsertDataFromPubViewToVistStatisticsServiceImpl implements InsertDataFromPubViewToVistStatisticsService {

  protected final Logger logger = LoggerFactory.getLogger(getClass());
  @Autowired
  private PubViewDAO pubViewDao;

  @Autowired
  private PsnPubDAO psnPubDao;

  @Autowired
  private CityIpDao cityIpDao;

  @Autowired
  private ConstRegionDao constRegionDao;

  @Autowired
  private PersonDao personDao;

  @Autowired
  private VistStatisticsDao vistStatisticsDao;

  @Override

  public void doInsertData(PubViewPO pubViewPO) {
    try {
      Long formateDate = DateUtils.getDateTime(pubViewPO.getGmtCreate());
      Long pubId = pubViewPO.getPubId();
      Long viewPsnId = pubViewPO.getViewPsnId();
      Long psnId = psnPubDao.getPsnOwner(pubViewPO.getPubId());
      String ip = pubViewPO.getIp();
      Long regionId = 0L;
      if (psnId != null && pubViewPO.getIp() != null) {
        regionId = getProvinceRegionId(pubViewPO.getIp(), psnId);
      }
      Integer actionType = new Integer(1);
      VistStatistics viStatistics =
          vistStatisticsDao.findVistRecord(viewPsnId, psnId, pubId, actionType, formateDate, ip, regionId);
      if (viStatistics != null) // 存在相同的数据，进行更新操作
      {
        long count1 = viStatistics.getCount() == null ? 0 : viStatistics.getCount();
        long count2 = pubViewPO.getTotalCount() == null ? 0 : pubViewPO.getTotalCount();
        if (count1 >= count2) {
          return;
        } else {
          viStatistics.setCount(count2);
        }
        vistStatisticsDao.update(viStatistics);
      } else// 进行插入操作
      {
        viStatistics = new VistStatistics();
        viStatistics.setActionKey(pubId);
        viStatistics.setActionType(1);// 为1时表示为成果
        viStatistics.setCount(pubViewPO.getTotalCount());
        viStatistics.setCreateDate(pubViewPO.getGmtCreate());
        viStatistics.setFormateDate(formateDate);
        viStatistics.setIp(ip);
        viStatistics.setPsnId(viewPsnId);// 访问人员的id
        viStatistics.setVistPsnId(psnId);// 被访问人的psnid
        viStatistics.setProvinceRegionId(regionId);// 被访问的省份代码
        vistStatisticsDao.save(viStatistics);
      }
    } catch (Exception e) {
      logger.error("插入数据到VIST_STATISTICS表失败：对应表v_pub_view中的id为：" + pubViewPO.getId(), e);
    }
  }


  /**
   * 通过ip获取对应的ProvinceRegionId
   * 
   * @param ip
   * @return
   */
  private Long getProvinceRegionId(String ip, Long psnId) {
    Long regionId = 0L;
    Long ipLong = 0L;
    try {
      ipLong = buildIPParam(ip);
      if (ipLong != null) {// 根据ip获取省份id
        String regionName = cityIpDao.findProvinceByIp(ipLong);
        regionId = constRegionDao.getRegionIdByName(regionName == null ? "" : regionName);
      }
      if (NumberUtils.isNullOrZero(regionId)) {// 根据个人设置获取省份id
        regionId = personDao.getPsnRegionIdByObjectId(psnId);
      }
      regionId = regionId == null ? 0L : regionId;
    } catch (Exception e) {
      logger.error("通过ip获取对应的ProvinceRegionId失败，对应psnid:" + psnId + "  ip:" + ip, e);
    }
    return regionId;
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


  @Override
  public Long getCountNum() {
    return pubViewDao.getConutNum();
  }


  @Override
  public List<PubViewPO> queryNeedInsertData(int start, int size) {
    return pubViewDao.queryNeedInsertDate(start, size);
  }

}
