package com.smate.center.batch.service.rol.pub;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.smate.center.batch.dao.rol.pub.SiePsnStatisticsDao;
import com.smate.center.batch.model.rol.pub.SiePsnStatistics;

/**
 * 同步PsnStatistics记录到Rol.
 * 
 * @author zyx
 * 
 */
@Service("siePsnStatisticsSyncService")
@Transactional(rollbackFor = Exception.class)
public class SiePsnStatisticsSyncServiceImpl implements SiePsnStatisticsSyncService {
  @Autowired
  private SiePsnStatisticsDao statisticsDao;

  @Override
  public void save(SiePsnStatistics statistics) {
    SiePsnStatistics s = statisticsDao.findByPsnId(statistics.getPsnId());
    if (s != null) {
      s.setPsnId(statistics.getPsnId());
      s.setCitedSum(statistics.getCitedSum());
      s.setEnSum(statistics.getEnSum());
      s.setHindex(statistics.getHindex());
      s.setPrjSum(statistics.getPrjSum());
      s.setPubSum(statistics.getPubSum());
      s.setZhSum(statistics.getZhSum());
      s.setFrdSum(statistics.getFrdSum());
      s.setGroupSum(statistics.getGroupSum());
      s.setPubAwardSum(statistics.getPubAwardSum());
      s.setPatentSum(statistics.getPatentSum());
      statisticsDao.save(s);
    } else {
      statisticsDao.save(statistics);
    }
  }

  public SiePsnStatistics findByPsnId(Long psnId) {
    return statisticsDao.findByPsnId(psnId);
  }
}
