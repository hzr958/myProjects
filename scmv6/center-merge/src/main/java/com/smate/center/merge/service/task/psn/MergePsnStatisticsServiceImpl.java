package com.smate.center.merge.service.task.psn;

import java.util.List;

import org.apache.commons.collections4.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import com.smate.center.merge.dao.person.PsnStatisticsDao;
import com.smate.center.merge.dao.pub.PubSnsDao;
import com.smate.center.merge.model.sns.person.PsnStatistics;
import com.smate.center.merge.model.sns.pub.PubSns;
import com.smate.center.merge.service.task.MergeBaseService;

/**
 * 处理人员统计数.
 * 
 * @author tsz
 *
 */
@Transactional(rollbackFor = Exception.class)
public class MergePsnStatisticsServiceImpl extends MergeBaseService {
  protected final Logger logger = LoggerFactory.getLogger(getClass());
  @Autowired
  private PsnStatisticsDao psnStatisticsDao;
  @Autowired
  private PubSnsDao pubSnsDao;

  @Override
  public boolean checkRun(Long savePsnId, Long delPsnId) throws Exception {
    return true;
  }

  /**
   * 处理相关合并.
   */
  @Override
  public boolean dealMerge(Long savePsnId, Long delPsnId) throws Exception {
    // 没有 统计记录 要初始化
    initPsnStatistics(savePsnId);
    return true;
  }

  private void initPsnStatistics(Long savePsnId) throws Exception {
    try {
      PsnStatistics ps = psnStatisticsDao.get(savePsnId);
      if (ps == null) {
        ps = new PsnStatistics();
        ps.setPsnId(savePsnId);
      }
      ps.setCitedSum(psnStatisticsDao.getPubCitedSum(savePsnId));
      ps.setFrdSum(psnStatisticsDao.getFriendSum(savePsnId));
      ps.setGroupSum(psnStatisticsDao.getGrpSum(savePsnId));
      ps.setOpenPrjSum(psnStatisticsDao.getOpenPrjSum(savePsnId));
      ps.setOpenPubSum(psnStatisticsDao.getOpenPubSum(savePsnId));
      ps.setPatentSum(psnStatisticsDao.getPatentSum(savePsnId));
      ps.setPcfPubSum(psnStatisticsDao.getPubAssignSum(savePsnId));
      ps.setPrjSum(psnStatisticsDao.getPrjSum(savePsnId));
      ps.setPubAwardSum(psnStatisticsDao.getPubLikeSum(savePsnId));
      ps.setPubFullTextSum(psnStatisticsDao.getPubFulltextSum(savePsnId));
      ps.setPubSum(psnStatisticsDao.getPubSum(savePsnId));
      ps.setVisitSum(psnStatisticsDao.getPsnVistSum(savePsnId));
      // 不区分中英文了
      ps.setHindex(this.getHindex(pubSnsDao.queryPubsByPsnId(savePsnId)));
      psnStatisticsDao.save(ps);
    } catch (Exception e) {
      logger.error("初始化人员统计信息出错psnId=" + savePsnId, e);
      throw new Exception("初始化人员统计信息出错psnId=" + savePsnId, e);
    }
  }

  private int getHindex(List<PubSns> pubs) {
    int hidex = 0;
    if (CollectionUtils.isNotEmpty(pubs)) {
      for (int i = 1; i <= pubs.size(); i++) {
        PubSns pub = pubs.get(i - 1);
        Integer citeTimes = (int) (pub.getCitations() == null ? 0 : pub.getCitations());
        if (citeTimes.intValue() >= i) {
          hidex += 1;
        }
      }
    }
    return hidex;
  }
}
