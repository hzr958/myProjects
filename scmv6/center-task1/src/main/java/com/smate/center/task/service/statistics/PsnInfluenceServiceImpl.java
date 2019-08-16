package com.smate.center.task.service.statistics;

import java.util.Date;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.smate.center.task.dao.sns.psn.ETemplateInfluenceCountDao;
import com.smate.center.task.dao.sns.psn.VistStatisticsDao;
import com.smate.center.task.dao.sns.quartz.AwardStatisticsDao;
import com.smate.center.task.dao.sns.quartz.PublicationDao;
import com.smate.center.task.dao.sns.quartz.ShareStatisticsDao;
import com.smate.center.task.exception.DaoException;
import com.smate.center.task.exception.ServiceException;
import com.smate.center.task.model.sns.psn.ETemplateInfluenceCount;
import com.smate.core.base.exception.DAOException;
import com.smate.core.base.psn.dao.PsnStatisticsDao;
import com.smate.core.base.psn.model.PsnStatistics;
import com.smate.core.base.statistics.dao.DownloadCollectStatisticsDao;


@Service("psnInfluenceService")
@Transactional(rollbackFor = Exception.class)
public class PsnInfluenceServiceImpl implements PsnInfluenceService {

  protected Logger logger = LoggerFactory.getLogger(getClass());
  @Autowired
  private PsnStatisticsDao psnStatisticsDao;
  @Autowired
  private DownloadCollectStatisticsDao downloadCollectStatisticsDao;
  @Autowired
  private AwardStatisticsDao awardStatisticsDao;
  @Autowired
  private ShareStatisticsDao shareStatisticsDao;
  @Autowired
  private VistStatisticsDao vistStatisticsDao;
  @Autowired
  private PublicationDao publicationDao;
  @Autowired
  private ETemplateInfluenceCountDao eTemplateInfluenceCountDao;


  @Override
  public Long checkLastMonthHadSend() throws ServiceException {
    try {
      return eTemplateInfluenceCountDao.checkLastMonthHadSend();
    } catch (DaoException e) {
      logger.error("科研影响力推广邮件判断上月是否已发送邮件时出错", e.getMessage());
      throw new ServiceException(e);
    }
  }

  /**
   * 获取已处理过的记录
   * 
   * @param size
   * @return
   * @throws ServiceException
   */
  @Override
  public List<ETemplateInfluenceCount> findETemplateInfluenceCount(int size) throws ServiceException {
    return eTemplateInfluenceCountDao.getETemplateInfluenceCount(size);
  }

  @SuppressWarnings("unchecked")
  @Override
  public List<Map> findVistPsn(Integer size) {
    try {
      return vistStatisticsDao.findVistPsn(size);
    } catch (DaoException e) {
      logger.error("科研影响力推广邮件获取阅读数时出错", e.getMessage());
      throw new ServiceException(e);
    }
  }

  @SuppressWarnings({"unchecked", "rawtypes"})
  @Override
  public List<Map> findAwardPsn(Integer size) throws ServiceException {
    try {
      return awardStatisticsDao.findAwardPsn(size);
    } catch (DaoException e) {
      logger.error("获取赞数时出错", e.getMessage());
      throw new ServiceException(e);
    }
  }

  @SuppressWarnings({"unchecked", "rawtypes"})
  @Override
  public List<Map> findSharePsn(Integer size) throws ServiceException {

    return shareStatisticsDao.findSharePsn(size);
  }

  /**
   * 收藏下载数
   */
  @SuppressWarnings({"rawtypes"})
  @Override
  public void findDownloadCollectStatistics(List<Map> dcRecordList) throws ServiceException {
    try {
      for (Map map : dcRecordList) {
        Long monthCount = (Long) map.get("count");
        Long psnId = (Long) map.get("psnId");
        Long pubCount = downloadCollectStatisticsDao.findPsnPubTotalNum(psnId);
        ETemplateInfluenceCount influenCount = this.getETemplateInfluenceCount(psnId);
        influenCount.setMonthDownloadCount(monthCount);
        influenCount.setDownloadCount(pubCount);
        eTemplateInfluenceCountDao.save(influenCount);
      }
    } catch (Exception e) {
      logger.error("科研影响力推广邮件保存收藏下载时出错", e.getMessage());
      throw new ServiceException(e);
    }
  }

  /**
   * 获取成果影响力统计
   */
  public ETemplateInfluenceCount getETemplateInfluenceCount(Long psnId) throws ServiceException {
    try {
      ETemplateInfluenceCount influenCount = eTemplateInfluenceCountDao.getETemplateInfluenceCount(psnId);
      if (influenCount == null) {
        influenCount = new ETemplateInfluenceCount();
        influenCount.setPsnId(psnId);
        influenCount.setStatus(0);
        influenCount.setMonthAwardCount(0L);
        influenCount.setAwardCount(0L);
        influenCount.setMonthDownloadCount(0L);
        influenCount.setDownloadCount(0L);
        influenCount.setMonthPubCount(0L);
        influenCount.setPubCount(0L);
        influenCount.setMonthReadCount(0L);
        influenCount.setReadCount(0L);
        influenCount.setMonthCitedTimesCount(0L);
        influenCount.setCitedTimesCount(0L);
        influenCount.setMonthShareCount(0L);
        influenCount.setShareCount(0L);
        influenCount.setHindex(0);
        influenCount.setCreateDate(new Date());
        eTemplateInfluenceCountDao.saveETemplateInfluenceCount(influenCount);
      }
      return influenCount;
    } catch (Exception e) {
      logger.error("科研影响力推广邮件获取数据表时出错", e.getMessage());
      throw new ServiceException(e);
    }
  }

  @Override
  public List findDownloadCollectStatistics(Integer size) throws ServiceException {

    try {
      return downloadCollectStatisticsDao.findRecord(size);
    } catch (DAOException e) {
      logger.error("获取下载或收藏记录出错 ", e);
      throw new ServiceException(e);
    }
  }

  @SuppressWarnings("unchecked")
  @Override
  public List<Map> getLastMonthPsnPubs(Integer size) throws ServiceException {
    try {
      return publicationDao.getLastMonthPsnPubs(size);
    } catch (DaoException e) {
      logger.error("批量获取成果数时出错", e.getMessage());
      throw new ServiceException(e);
    }
  }

  @SuppressWarnings("unchecked")
  @Override
  public List<Map> getLastMonthPsnCitedTimes(Integer size) throws ServiceException {
    try {
      return publicationDao.getLastMonthCitedTimes(size);
    } catch (DaoException e) {
      logger.error("批量获取成果数时出错", e.getMessage());
      throw new ServiceException(e);
    }
  }

  @Override
  public void updateInfluence(ETemplateInfluenceCount influence) throws ServiceException {
    try {
      eTemplateInfluenceCountDao.saveETemplateInfluenceCount(influence);
    } catch (DaoException e) {
      logger.error("更新科研影响力推广邮件数据时出错 object=" + influence.toString(), e.getMessage());
      throw new ServiceException(e);
    }
  }

  /**
   * 获取成果数
   */
  @SuppressWarnings({"rawtypes"})
  @Override
  public void findPubStatistics(List<Map> monthPubs) throws ServiceException {
    try {
      for (Map map : monthPubs) {
        Long monthCount = (Long) map.get("count");
        Long psnId = (Long) map.get("psnId");
        ETemplateInfluenceCount influenCount = this.getETemplateInfluenceCount(psnId);
        influenCount.setMonthPubCount(monthCount);
        // Long pubCount = publicationDao.getTotalPubsByPsnId(psnId);
        // influenCount.setPubCount(pubCount);
        eTemplateInfluenceCountDao.save(influenCount);
      }
    } catch (Exception e) {
      logger.error("科研影响力推广邮件获取成果数时出错", e.getMessage());
      throw new ServiceException(e);
    }
  }

  /**
   * 获取论文引用数
   */
  @SuppressWarnings({"rawtypes"})
  @Override
  public void findCitedTimesStatistics(List<Map> monthCitedTimess) throws ServiceException {
    try {
      for (Map map : monthCitedTimess) {
        Long monthCount = (Long) map.get("count");
        Long psnId = (Long) map.get("psnId");
        ETemplateInfluenceCount influenCount = this.getETemplateInfluenceCount(psnId);
        influenCount.setMonthCitedTimesCount(monthCount);
        // Long citedCount = publicationDao.getTotalCitedTimesByPsnId(psnId);
        // influenCount.setCitedTimesCount(citedCount);
        eTemplateInfluenceCountDao.save(influenCount);
      }
    } catch (Exception e) {
      logger.error("科研影响力推广邮件获取论文引用数时出错", e.getMessage());
      throw new ServiceException(e);
    }
  }

  /**
   * 获取阅读数
   */
  @SuppressWarnings({"rawtypes"})
  @Override
  public void findReadStatistics(List<Map> psnStatistics) throws ServiceException {

    try {
      for (Map map : psnStatistics) {
        Long monthCount = (Long) map.get("count");
        Long psnId = (Long) map.get("psnId");
        ETemplateInfluenceCount influenCount = this.getETemplateInfluenceCount(psnId);
        influenCount.setMonthReadCount(monthCount);
        // Long totalCount = readStatisticsDao.countReadByPsnId(psnId, DynamicConstant.RES_TYPE_PUB);
        // influenCount.setReadCount(totalCount);
        eTemplateInfluenceCountDao.save(influenCount);
      }

    } catch (Exception e) {
      logger.error("科研影响力推广邮件获取阅读数时出错", e.getMessage());
      throw new ServiceException(e);
    }
  }

  /**
   * 获取赞数
   */
  @SuppressWarnings({"rawtypes"})
  @Override
  public void findAwardStatistics(List<Map> awardList) throws ServiceException {

    try {
      for (Map map : awardList) {
        Long monthCount = (Long) map.get("count");
        Long psnId = (Long) map.get("psnId");
        Long awardCount = awardStatisticsDao.getAwardtotalPsn(psnId);
        ETemplateInfluenceCount influenCount = this.getETemplateInfluenceCount(psnId);
        influenCount.setMonthAwardCount(monthCount);
        influenCount.setAwardCount(awardCount);
        eTemplateInfluenceCountDao.save(influenCount);
      }

    } catch (Exception e) {
      logger.error("科研影响力推广邮件获取赞数时出错", e.getMessage());
      throw new ServiceException(e);
    }
  }

  /**
   * 获取分享数
   */
  @SuppressWarnings({"rawtypes"})
  @Override
  public void findShareStatistics(List<Map> shareList) throws ServiceException {

    try {
      for (Map map : shareList) {
        Long monthCount = (Long) map.get("count");
        Long psnId = (Long) map.get("psnId");
        Long shareCount = shareStatisticsDao.getSharetotalPsn(psnId);
        ETemplateInfluenceCount influenCount = this.getETemplateInfluenceCount(psnId);
        influenCount.setMonthShareCount(monthCount);
        influenCount.setShareCount(shareCount);
        eTemplateInfluenceCountDao.save(influenCount);
      }

    } catch (Exception e) {
      logger.error("科研影响力推广邮件获取分享数时出错", e.getMessage());
      throw new ServiceException(e);
    }
  }

  @Override
  public List<ETemplateInfluenceCount> findETemplateInfluenceCounts(Integer size) throws ServiceException {

    try {
      return eTemplateInfluenceCountDao.findETemplateInfluenceCounts(size);
    } catch (Exception e) {
      logger.error("科研影响力推广邮件获取批量数据表时出错", e.getMessage());
      throw new ServiceException(e);
    }
  }

  @Override
  public List<PsnStatistics> getHindexByPsnIds(List<Long> psnIdList) throws ServiceException {
    return psnStatisticsDao.getHidexByPsnIds(psnIdList);
  }

  /**
   * 处理hindex
   */
  @Override
  public void findHindexStatistics(List<PsnStatistics> pstList) throws ServiceException {

    try {
      for (PsnStatistics ps : pstList) {
        ETemplateInfluenceCount influenCount = this.getETemplateInfluenceCount(ps.getPsnId());
        influenCount.setHindex(ps.getHindex());
        influenCount.setCitedTimesCount(ps.getCitedSum().longValue());
        influenCount.setReadCount(ps.getVisitSum().longValue());
        influenCount.setPubCount(ps.getPubSum().longValue());
        eTemplateInfluenceCountDao.save(influenCount);
      }
    } catch (Exception e) {
      logger.error("科研影响力推广邮件处理hindex时出错", e.getMessage());
      throw new ServiceException(e);
    }
  }

  // 对pubCount 进行处理
  public void dealCount(Long psnId, ETemplateInfluenceCount influenCount) {
    try {
      PsnStatistics sta = psnStatisticsDao.getPsnStatisticsForInfluence(Long.valueOf(influenCount.getPsnId()));
      influenCount.setAwardCount(awardStatisticsDao.getAwardtotalPsn(Long.valueOf(influenCount.getPsnId())));// 赞
      influenCount.setPubCount(sta.getPubSum().longValue());// 论文
      influenCount.setCitedTimesCount(sta.getCitedSum().longValue());// 引用
      influenCount.setHindex(sta.getHindex());
      influenCount.setShareCount(shareStatisticsDao.getSharetotalPsn(psnId));// 分享
      influenCount.setDownloadCount(downloadCollectStatisticsDao.findPsnPubTotalNum(psnId));// 下载
      influenCount.setReadCount(sta.getVisitSum().longValue());// 阅读
      eTemplateInfluenceCountDao.save(influenCount);
    } catch (DAOException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    }

  }


}
