package com.smate.web.psn.service.statistics;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang.ObjectUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.smate.core.base.psn.dao.PsnStatisticsDao;
import com.smate.core.base.psn.model.PsnStatistics;
import com.smate.core.base.pub.model.Publication;
import com.smate.core.base.statistics.PsnStatisticsUtils;
import com.smate.web.psn.dao.friend.FriendDao;
import com.smate.web.psn.dao.group.GroupInvitePsnDao;
import com.smate.web.psn.dao.project.ProjectDao;
import com.smate.web.psn.dao.pub.PublicationDao;
import com.smate.web.psn.dao.rcmdsync.PsnStatisticsRcmdDao;
import com.smate.web.psn.dao.rcmdsync.PubFulltextPsnRcmdDao;
import com.smate.web.psn.dao.statistics.AwardStatisticsDao;
import com.smate.web.psn.exception.PsnException;
import com.smate.web.psn.model.consts.ConstPublicationType;
import com.smate.web.psn.model.rcmd.PsnStatisticsRcmd;

/**
 * 人员信息统计服务接口 实现
 * 
 * @author tsz
 *
 */

@Service("psnStatisticsService")
@Transactional(rollbackFor = Exception.class)
public class PsnStatisticsServiceImpl implements PsnStatisticsService {

  protected final Logger logger = LoggerFactory.getLogger(getClass());
  @Autowired
  private PsnStatisticsDao psnStatisticsDao;
  @Autowired
  private AwardStatisticsDao awardStatisticsDao;
  @Autowired
  private GroupInvitePsnDao groupInvitePsnDao;
  @Autowired
  private FriendDao friendDao;
  @Autowired
  private ProjectDao projectDao;
  @Autowired
  private PublicationDao publicationDao;
  @Autowired
  private PubFulltextPsnRcmdDao pubFulltextPsnRcmdDao;
  @Autowired
  private PsnStatisticsRcmdDao psnStatisticsRcmdDao;

  @Override
  public PsnStatistics getPsnStatistics(Long psnId) throws PsnException {
    if (psnId == null) {
      return null;
    }

    PsnStatistics psnStatistics = psnStatisticsDao.get(psnId);
    if (psnStatistics == null) {// 刚注册的用户在psnStatistics没有数据，为null，给它new一个新的对象，初值都为0
      psnStatistics = new PsnStatistics();
    } else {// 如果这个人的某一项为null，也给他赋初值为0
      psnStatistics.setPrjSum(psnStatistics.getPrjSum() != null ? psnStatistics.getPrjSum() : 0);
      psnStatistics.setPubSum(psnStatistics.getPubSum() != null ? psnStatistics.getPubSum() : 0);
      // psnStatistics.setPatentSum(psnStatistics.getPatentSum()!=null?psnStatistics.getPatentSum():0);
      psnStatistics.setFrdSum(psnStatistics.getFrdSum() != null ? psnStatistics.getFrdSum() : 0);
    }
    return psnStatistics;
  }

  @Override
  public PsnStatistics initPsnStatistics(Long psnId, Integer frdSum) throws PsnException {
    try {
      PsnStatistics psnStatistics = psnStatisticsDao.get(psnId);
      if (psnStatistics != null) {
        return psnStatistics;
      }
      psnStatistics = new PsnStatistics();
      psnStatistics.setPsnId(psnId);
      psnStatistics.setPubAwardSum(awardStatisticsDao.countAward(psnId, 1).intValue());
      psnStatistics.setGroupSum(groupInvitePsnDao.getGroupCountByPsnId(psnId));
      psnStatistics.setFrdSum(frdSum);
      psnStatistics.setPrjSum(projectDao.getSumProject(psnId));
      psnStatistics
          .setPatentSum(publicationDao.countPubByPsnId(psnId, ConstPublicationType.PUB_PATENT_TYPE).intValue());
      // 增加设置成果全文推荐数_MJG_SCM-5991.
      Long pubFTextReSum = 0L;
      try {
        pubFTextReSum = pubFulltextPsnRcmdDao.queryRcmdFulltextCount(psnId);
        if (pubFTextReSum.longValue() > 0) {
          psnStatistics.setPubFullTextSum(NumberUtils.toInt(ObjectUtils.toString(pubFTextReSum)));
        }
      } catch (Exception e) {
        logger.error("获取个人统计信息时初始化获取成果全文推荐数出错，psnId=" + psnId + ":", e);
      }

      List<Publication> pubs = publicationDao.queryPubsByPsnId(psnId);
      if (CollectionUtils.isNotEmpty(pubs)) {
        Map<String, Integer> langMap = getLangauge(pubs);
        int hidex = getHindex(pubs);
        psnStatistics.setPubSum(pubs.size());
        psnStatistics.setHindex(hidex);
        psnStatistics.setZhSum(NumberUtils.toInt(ObjectUtils.toString(langMap.get("zhcount"))));
        psnStatistics.setEnSum(NumberUtils.toInt(ObjectUtils.toString(langMap.get("enCount"))));
        psnStatistics.setCitedSum(publicationDao.queryPubsCiteTimesByPsnId(psnId));
        // 属性为null的保存为0
        PsnStatisticsUtils.buildZero(psnStatistics);
        psnStatisticsDao.save(psnStatistics);
      }
      return psnStatistics;
    } catch (Exception e) {
      logger.error("初始化人员统计信息psnId=" + psnId, e);
      throw new PsnException("初始化人员统计信息psnId=" + psnId, e);
    }
  }

  @Override
  public void updatePsnStatisticsByFrd(Long psnId, Integer frdSum) throws PsnException {
    PsnStatistics psnStatistics = this.initPsnStatistics(psnId, frdSum);
    if (psnStatistics != null) {
      // Long psnFrdSum = friendDao.getFriendCount(psnId);
      // //同一个事务不能获取最新的好友数
      psnStatistics.setFrdSum(frdSum);
      // 属性为null的保存为0
      PsnStatisticsUtils.buildZero(psnStatistics);
      psnStatisticsDao.save(psnStatistics);
      // 同步到rcmd去
      this.syncPsnStatisticsRcmd(psnStatistics);
      // TODO 同步到SIE(SIE在改造，暂时不同步)
    }
  }

  /**
   * 同步统计信息到推荐库
   * 
   * @param psnSta
   */
  private void syncPsnStatisticsRcmd(PsnStatistics statistics) {
    PsnStatisticsRcmd s = psnStatisticsRcmdDao.findByPsnId(statistics.getPsnId());
    if (s == null) {
      s = new PsnStatisticsRcmd();
    }
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
    psnStatisticsRcmdDao.save(s);
  }

  private Map<String, Integer> getLangauge(List<Publication> pubs) {
    Map<String, Integer> langMap = new HashMap<String, Integer>();
    int zhcount = 0;
    int enCount = 0;
    if (CollectionUtils.isNotEmpty(pubs)) {
      for (Publication pub : pubs) {
        if (pub.getZhTitleHash() == null) {
          enCount++;
        } else {
          zhcount++;
        }
      }
    }
    langMap.put("zhcount", zhcount);
    langMap.put("enCount", enCount);
    return langMap;
  }

  private int getHindex(List<Publication> pubs) {
    int hidex = 0;
    if (CollectionUtils.isNotEmpty(pubs)) {
      for (int i = 1; i <= pubs.size(); i++) {
        Publication pub = pubs.get(i - 1);
        Integer citeTimes = pub.getCitedTimes() == null ? 0 : pub.getCitedTimes();
        if (citeTimes.intValue() >= i) {
          hidex += 1;
        }
      }
    }
    return hidex;
  }

  @Override
  public void updatePsnVisitSum(Long psnId) throws PsnException {
    try {
      PsnStatistics pst = this.getPsnStatistics(psnId);
      if (pst != null) {
        Integer visitSum = pst.getVisitSum() != null ? pst.getVisitSum() : 0;
        pst.setVisitSum(visitSum + 1);
        psnStatisticsDao.save(pst);
      }
    } catch (Exception e) {
      logger.error("更新人员访问统计数出错， psnId=" + psnId, e);
      throw new PsnException("更新人员访问统计数出错, psnId=" + psnId, e);
    }
  }

}
