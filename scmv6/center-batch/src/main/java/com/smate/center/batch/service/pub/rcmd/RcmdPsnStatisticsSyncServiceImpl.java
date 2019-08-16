package com.smate.center.batch.service.pub.rcmd;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.smate.center.batch.dao.rcmd.pub.RcmdPsnStatisticsDao;
import com.smate.center.batch.model.rcmd.pub.RcmdPsnStatistics;


/**
 * 推荐服务人员统计信息同步服务类
 * 
 * @author zk
 * 
 */
@Service("rcmdPsnStatisticsSyncService")
@Transactional(rollbackFor = Exception.class)
public class RcmdPsnStatisticsSyncServiceImpl implements RcmdPsnStatisticsSyncService {
  @Autowired
  private RcmdPsnStatisticsDao statisticsDao;

  @Override
  public void save(RcmdPsnStatistics statistics) {
    RcmdPsnStatistics s = statisticsDao.findByPsnId(statistics.getPsnId());
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
      s.setPcfPubSum(statistics.getPcfPubSum());
      s.setPubFullTextSum(statistics.getPubFullTextSum());
      statisticsDao.save(s);
    } else {
      statisticsDao.save(statistics);
    }
  }

  public RcmdPsnStatistics findByPsnId(Long psnId) {
    return statisticsDao.findByPsnId(psnId);
  }
}
