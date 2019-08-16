package com.smate.center.task.single.service.person;

import java.util.List;

import org.apache.commons.collections4.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.smate.center.task.dao.sns.quartz.PublicationQueryDao;
import com.smate.center.task.exception.ServiceException;
import com.smate.center.task.single.constants.MessageConstant;
import com.smate.center.task.v8pub.dao.sns.PubSnsDAO;
import com.smate.center.task.v8pub.sns.po.PubSnsPO;
import com.smate.core.base.psn.dao.PsnStatisticsDao;
import com.smate.core.base.psn.dao.PsnStatisticsRefreshDao;
import com.smate.core.base.psn.model.PsnStatistics;
import com.smate.core.base.psn.model.PsnStatisticsRefresh;
import com.smate.core.base.utils.cache.CacheService;

/**
 * @author changwen
 * 
 */
@Service("psnStatisticsService")
@Transactional(rollbackFor = Exception.class)
public class PsnStatisticsServiceImpl implements PsnStatisticsService {
  private static final long serialVersionUID = 1875829508379902571L;
  private Logger logger = LoggerFactory.getLogger(getClass());
  @Autowired
  private PsnStatisticsUpdateService psnStatisticsUpdateService;
  @Autowired
  private PsnStatisticsDao psnStatisticsDao;
  @Autowired
  private PublicationQueryDao publicationQueryDao;
  @Autowired
  private CacheService cacheService;
  @Autowired
  private PubSnsDAO pubSnsDao;
  @Autowired
  private PsnStatisticsRefreshDao psnStatisticsRefreshDao;

  @Override
  public PsnStatistics getPsnStatistics(Long psnId) {
    PsnStatistics psnStatistics = psnStatisticsDao.get(psnId);
    if (psnStatistics == null) {
      return psnStatisticsUpdateService.initPsnStatics(psnId);
    }
    return psnStatistics;
  }

  @Override
  public Integer getPsnPendingConfirmPubNum(Long psnId) {
    PsnStatistics ps = getPsnStatistics(psnId);
    if (ps != null) {
      return ps.getPcfPubSum();
    }
    return 0;
  }

  /**
   * 获取用户的成果全文推荐总数_MJG_SCM-5991.
   * 
   * @param psnId
   * @return
   * @throws ServiceException
   */
  public Integer getPubFulltextReSum(Long psnId) {
    PsnStatistics ps = getPsnStatistics(psnId);
    if (ps != null) {
      return ps.getPubFullTextSum();
    }
    return 0;
  }

  @Override
  public void updateStatisticsPcfPub(Long psnId, int pendingCfmPubNum) throws ServiceException {
    try {
      PsnStatistics psnStatistics = psnStatisticsDao.get(psnId);
      if (psnStatistics == null) {
        psnStatistics = new PsnStatistics();
        psnStatistics.setPsnId(psnId);
      }
      psnStatistics.setPcfPubSum(pendingCfmPubNum);
      // 属性为null的保存为0
      buildZero(psnStatistics);
      psnStatisticsDao.save(psnStatistics);
      cacheService.remove(MessageConstant.MSG_TIP_CACHE_NAME, MessageConstant.MSG_TIP_CACHE_KEY + psnId);
    } catch (Exception e) {
      logger.error("设置用户待认领成果数据psnId=" + psnId, e);
      throw new ServiceException("设置用户待认领成果数据psnId=" + psnId, e);
    }
  }

  @Override
  public void savePsnStatistics(PsnStatistics p) {
    // 属性为null的保存为0
    buildZero(p);
    psnStatisticsDao.save(p);
  }

  private void buildZero(PsnStatistics p) {
    // 1.成果总数
    if (p.getPubSum() == null) {
      p.setPubSum(0);
    }
    // 2.成果引用次数总数
    if (p.getCitedSum() == null) {
      p.setCitedSum(0);
    }
    // 3.hindex指数
    if (p.getHindex() == null) {
      p.setHindex(0);
    }
    // 4.中文成果数
    if (p.getZhSum() == null) {
      p.setZhSum(0);
    }
    // 5.英文成果数
    if (p.getEnSum() == null) {
      p.setEnSum(0);
    }
    // 6.项目总数
    if (p.getPrjSum() == null) {
      p.setPrjSum(0);
    }
    // 7.好友总数
    if (p.getFrdSum() == null) {
      p.setFrdSum(0);
    }
    // 8.群组总数
    if (p.getGroupSum() == null) {
      p.setGroupSum(0);
    }
    // 9.成果被赞的总数
    if (p.getPubAwardSum() == null) {
      p.setPubAwardSum(0);
    }
    // 10.专利数
    if (p.getPatentSum() == null) {
      p.setPatentSum(0);
    }
    // 11.待认领成果数
    if (p.getPcfPubSum() == null) {
      p.setPcfPubSum(0);
    }
    // 12.成果全文推荐数
    if (p.getPubFullTextSum() == null) {
      p.setPubFullTextSum(0);
    }
    // 13.公开成果总数
    if (p.getOpenPubSum() == null) {
      p.setOpenPubSum(0);
    }
    // 14.公开项目总数
    if (p.getOpenPrjSum() == null) {
      p.setOpenPrjSum(0);
    }
    // 15.访问总数
    if (p.getVisitSum() == null) {
      p.setVisitSum(0);
    }
  }

  @Override
  public void updatePsnStatistics(Long psnId) throws Exception {
    initPsnStatistics(psnId);
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

  private int getHindex(List<PubSnsPO> pubs) {
    int hidex = 0;
    if (CollectionUtils.isNotEmpty(pubs)) {
      for (int i = 1; i <= pubs.size(); i++) {
        PubSnsPO pub = pubs.get(i - 1);
        Integer citeTimes = (int) (pub.getCitations() == null ? 0 : pub.getCitations());
        if (citeTimes.intValue() >= i) {
          hidex += 1;
        }
      }
    }
    return hidex;
  }

  @Override
  public void ToRefresh() throws Exception {
    psnStatisticsRefreshDao.getToRefresh();
  }

  @Override
  public List<PsnStatisticsRefresh> getToBeRefresh(Long startId, int size) throws Exception {
    return psnStatisticsRefreshDao.getToBeRefresh(startId, size);
  }

  @Override
  public void updatePsnStatisticsRefresh(PsnStatisticsRefresh psr) throws Exception {
    psnStatisticsRefreshDao.update(psr);
  }
}
