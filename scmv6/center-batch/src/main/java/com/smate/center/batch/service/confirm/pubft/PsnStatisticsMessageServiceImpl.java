package com.smate.center.batch.service.confirm.pubft;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.smate.center.batch.dao.rcmd.pub.RcmdPsnStatisticsDao;
import com.smate.center.batch.dao.rol.pub.RolPsnStatisticsDao;
import com.smate.center.batch.dao.sns.psn.PsnHtmlRefreshDao;
import com.smate.center.batch.model.rcmd.pub.RcmdPsnStatistics;
import com.smate.center.batch.model.rol.prj.RolPsnStatistics;
import com.smate.center.batch.model.sns.pub.PsnHtmlRefresh;
import com.smate.core.base.psn.model.PsnStatistics;

@Service("PsnStatisticsMessageService")
@Transactional(rollbackFor = Exception.class)
public class PsnStatisticsMessageServiceImpl implements PsnStatisticsMessageService {
  @Autowired
  private RcmdPsnStatisticsDao rcmdStatisticsDao;
  @Autowired
  private RolPsnStatisticsDao rolStatisticsDao;
  @Autowired
  private PsnHtmlRefreshDao psnHtmlRefreshDao;

  @Override
  public void updatePsnStatics(PsnStatistics statistics) throws Exception {
    saveRcmdPsnStatic(statistics);
    saveToRefreshTask(statistics.getPsnId());
    saveRolPsnStatic(statistics);
  }

  /**
   * 设置psnId需要刷新
   */
  private void saveToRefreshTask(Long psnId) {
    PsnHtmlRefresh refresh = psnHtmlRefreshDao.findByPsnId(psnId);
    if (refresh == null) {
      refresh = new PsnHtmlRefresh();
      refresh.setPsnId(psnId);
    }
    refresh.setTempCode(0);
    refresh.setStatus(1);
    psnHtmlRefreshDao.save(refresh);
  }

  /**
   * 更新推荐库的
   * 
   * @param statistics
   */
  private void saveRcmdPsnStatic(PsnStatistics statistics) {
    RcmdPsnStatistics s = rcmdStatisticsDao.findByPsnId(statistics.getPsnId());
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
      rcmdStatisticsDao.save(s);
    }
  }

  /**
   * 更新rol库的
   * 
   * @param statistics
   */
  private void saveRolPsnStatic(PsnStatistics statistics) {
    RolPsnStatistics s = rolStatisticsDao.findByPsnId(statistics.getPsnId());
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
      rolStatisticsDao.save(s);
    }
  }

}
