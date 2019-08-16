package com.smate.web.v8pub.service.pdwh;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.smate.core.base.pub.vo.PubDetailVO;
import com.smate.web.v8pub.dao.pdwh.PdwhPubLikeDAO;
import com.smate.web.v8pub.dao.pdwh.PdwhPubStatisticsDAO;
import com.smate.web.v8pub.enums.PubLikeStatusEnum;
import com.smate.web.v8pub.exception.ServiceException;
import com.smate.web.v8pub.po.pdwh.PdwhPubStatisticsPO;

@Service("newPdwhPubStatisticsService")
@Transactional(rollbackFor = Exception.class)
public class PdwhPubStatisticsServiceImpl implements PdwhPubStatisticsService {
  private Logger logger = LoggerFactory.getLogger(getClass());

  @Autowired
  private PdwhPubStatisticsDAO pdwhStatisticsDAO;
  @Autowired
  private PdwhPubLikeDAO pdwhPubLikeDAO;

  @Override
  public void updateCommentStatistics(Long pdwhPubId) throws ServiceException {
    try {
      PdwhPubStatisticsPO pdwhPubStatisticsPO = pdwhStatisticsDAO.get(pdwhPubId);
      if (pdwhPubStatisticsPO == null) {
        pdwhPubStatisticsPO = new PdwhPubStatisticsPO();
        pdwhPubStatisticsPO.setPdwhPubId(pdwhPubId);
        pdwhPubStatisticsPO.setCommentCount(1);
      } else {
        int commentCount = pdwhPubStatisticsPO.getCommentCount() == null ? 0 : pdwhPubStatisticsPO.getCommentCount();
        pdwhPubStatisticsPO.setCommentCount(commentCount + 1);
      }
      pdwhStatisticsDAO.save(pdwhPubStatisticsPO);
    } catch (Exception e) {
      logger.error("基准库更新评论统计数出错！pdwhPubId={}", pdwhPubId);
      throw new ServiceException(e);
    }
  }

  @Override
  public void updateShareStatistics(Long pdwhPubId) throws ServiceException {
    try {
      PdwhPubStatisticsPO pdwhPubStatisticsPO = pdwhStatisticsDAO.get(pdwhPubId);
      if (pdwhPubStatisticsPO == null) {
        pdwhPubStatisticsPO = new PdwhPubStatisticsPO();
        pdwhPubStatisticsPO.setPdwhPubId(pdwhPubId);
        pdwhPubStatisticsPO.setShareCount(1);
      } else {
        int shareCount = pdwhPubStatisticsPO.getShareCount() == null ? 0 : pdwhPubStatisticsPO.getShareCount();
        pdwhPubStatisticsPO.setShareCount(shareCount + 1);
      }
      pdwhStatisticsDAO.save(pdwhPubStatisticsPO);
    } catch (Exception e) {
      logger.error("基准库更新分享统计数出错！pdwhPubId={}", pdwhPubId);
      throw new ServiceException(e);
    }
  }

  @Override
  public void updateAwardStatistics(Long pdwhPubId, Integer awardCount) throws ServiceException {
    try {
      PdwhPubStatisticsPO pdwhPubStatisticsPO = pdwhStatisticsDAO.get(pdwhPubId);
      if (pdwhPubStatisticsPO == null) {
        pdwhPubStatisticsPO = new PdwhPubStatisticsPO(pdwhPubId, 0, 0, 0, 0, 0);
      } else {
        pdwhPubStatisticsPO.setAwardCount(awardCount);
      }
      pdwhStatisticsDAO.save(pdwhPubStatisticsPO);
    } catch (Exception e) {
      logger.error("基准库更新赞统计数出错！pdwhPubId={}", pdwhPubId);
      throw new ServiceException(e);
    }
  }

  @Override
  public void updateReadStatistics(Long pdwhPubId) throws ServiceException {
    try {
      PdwhPubStatisticsPO pdwhPubStatisticsPO = pdwhStatisticsDAO.get(pdwhPubId);
      if (pdwhPubStatisticsPO == null) {
        pdwhPubStatisticsPO = new PdwhPubStatisticsPO();
        pdwhPubStatisticsPO.setPdwhPubId(pdwhPubId);
        pdwhPubStatisticsPO.setReadCount(1);
      } else {
        int readCount = pdwhPubStatisticsPO.getReadCount() == null ? 0 : pdwhPubStatisticsPO.getReadCount();
        pdwhPubStatisticsPO.setReadCount(readCount + 1);
      }
      pdwhStatisticsDAO.save(pdwhPubStatisticsPO);
    } catch (Exception e) {
      logger.error("基准库更新阅读统计数出错！pdwhPubId={}", pdwhPubId);
      throw new ServiceException(e);
    }
  }

  @Override
  public void updateRefStatistics(Long pdwhPubId, Integer refCount) throws ServiceException {
    try {
      PdwhPubStatisticsPO pdwhPubStatisticsPO = pdwhStatisticsDAO.get(pdwhPubId);
      if (pdwhPubStatisticsPO == null) {
        pdwhPubStatisticsPO = new PdwhPubStatisticsPO(pdwhPubId, 0, 0, 0, 0, 0);
      } else {
        pdwhPubStatisticsPO.setRefCount(refCount);
      }
      pdwhStatisticsDAO.save(pdwhPubStatisticsPO);
    } catch (Exception e) {
      logger.error("基准库更新引用统计数出错！pdwhPubId={}", pdwhPubId);
      throw new ServiceException(e);
    }
  }

  @Override
  public void updateLikeStatistics(Long pdwhPubId, PubLikeStatusEnum status) throws ServiceException {
    try {
      PdwhPubStatisticsPO pdwhPubStatisticsPO = pdwhStatisticsDAO.get(pdwhPubId);
      if (pdwhPubStatisticsPO == null) {
        pdwhPubStatisticsPO = new PdwhPubStatisticsPO();
        pdwhPubStatisticsPO.setPdwhPubId(pdwhPubId);
      }
      Integer awardCount = pdwhPubStatisticsPO.getAwardCount() == null ? 0 : pdwhPubStatisticsPO.getAwardCount();
      switch (status) {
        case PUB_LIKE:
          pdwhPubStatisticsPO.setAwardCount(awardCount + 1);
          break;
        case PUB_UNLIKE:
          pdwhPubStatisticsPO.setAwardCount(awardCount - 1 < 0 ? 0 : pdwhPubStatisticsPO.getAwardCount() - 1);
          break;
      }
      pdwhStatisticsDAO.save(pdwhPubStatisticsPO);
    } catch (Exception e) {
      logger.error("基准库更新赞统计数异常,pdwhPubId=" + pdwhPubId, e);
      throw new ServiceException(e);
    }
  }

  @Override
  public PdwhPubStatisticsPO get(Long id) throws ServiceException {
    return pdwhStatisticsDAO.get(id);
  }

  @Override
  public void save(PdwhPubStatisticsPO pdwhPubStatisticsPO) throws ServiceException {
    try {
      pdwhStatisticsDAO.save(pdwhPubStatisticsPO);
    } catch (Exception e) {
      logger.error("保存成果统计数据失败，pdwhPubStatisticsPO={}", pdwhPubStatisticsPO, e);
      throw new ServiceException(e);
    }

  }

  @Override
  public void update(PdwhPubStatisticsPO pdwhPubStatisticsPO) throws ServiceException {
    try {
      pdwhStatisticsDAO.update(pdwhPubStatisticsPO);
    } catch (Exception e) {
      logger.error("更新成果统计数据失败，pdwhPubStatisticsPO={}", pdwhPubStatisticsPO, e);
      throw new ServiceException(e);
    }

  }

  @Override
  public void saveOrUpdate(PdwhPubStatisticsPO pdwhPubStatisticsPO) throws ServiceException {
    try {
      pdwhStatisticsDAO.saveOrUpdate(pdwhPubStatisticsPO);
    } catch (Exception e) {
      logger.error("保存或更新成果统计数据失败，pdwhPubStatisticsPO={}", pdwhPubStatisticsPO, e);
      throw new ServiceException(e);
    }

  }

  @Override
  public void deleteById(Long pdwhId) throws ServiceException {
    try {
      pdwhStatisticsDAO.delete(pdwhId);
    } catch (Exception e) {
      logger.error("根据pdwhId删除基准库成果统计表出错！pdwhId={}", pdwhId, e);
      throw new ServiceException(e);
    }

  }

  @Override
  public void delete(PdwhPubStatisticsPO pdwhPubStatisticsPO) throws ServiceException {
    try {
      pdwhStatisticsDAO.delete(pdwhPubStatisticsPO);
    } catch (Exception e) {
      logger.error("删除成果统计数据失败，pdwhPubStatisticsPO={}", pdwhPubStatisticsPO, e);
      throw new ServiceException(e);
    }

  }

  @Override
  public void pdwhInitStatistics(PubDetailVO pubDetailVO) throws ServiceException {
    PdwhPubStatisticsPO pdwhStatistics = pdwhStatisticsDAO.get(pubDetailVO.getPubId());
    if (pdwhStatistics == null) {
      pubDetailVO.setShareCount(0);
      pubDetailVO.setAwardCount(0);
      pubDetailVO.setCommentCount(0);
    } else {
      Integer shareCount = pdwhStatistics.getShareCount() == null ? 0 : pdwhStatistics.getShareCount();
      pubDetailVO.setShareCount(shareCount);
      Integer awardCount = pdwhStatistics.getAwardCount() == null ? 0 : pdwhStatistics.getAwardCount();
      pubDetailVO.setAwardCount(awardCount);
      Integer commentCount = pdwhStatistics.getCommentCount() == null ? 0 : pdwhStatistics.getCommentCount();
      pubDetailVO.setCommentCount(commentCount);
    }
    long count = pdwhPubLikeDAO.getLikeRecord(pubDetailVO.getPubId(), pubDetailVO.getPsnId());
    if (count > 0) {
      pubDetailVO.setIsAward(1);
    }
  }

  @Override
  public int getAwardCounts(Long pdwhPubId) throws ServiceException {
    int awardCount = 0;
    PdwhPubStatisticsPO pdwhStatistics = pdwhStatisticsDAO.get(pdwhPubId);
    if (pdwhStatistics != null) {
      awardCount = pdwhStatistics.getAwardCount() == null ? 0 : pdwhStatistics.getAwardCount();
    }
    return awardCount;
  }

  @Override
  public int getShareCounts(Long pdwhPubId) throws ServiceException {
    int shareCount = 0;
    PdwhPubStatisticsPO pdwhStatistics = pdwhStatisticsDAO.get(pdwhPubId);
    if (pdwhStatistics != null) {
      shareCount = pdwhStatistics.getShareCount() == null ? 0 : pdwhStatistics.getShareCount();
    }
    return shareCount;
  }

  @Override
  public void getPdwhStatistics(Long pdwhPubId, PubDetailVO pubDetailVO) throws ServiceException {
    PdwhPubStatisticsPO pdwhStatistics = pdwhStatisticsDAO.get(pdwhPubId);
    if (pdwhStatistics == null) {
      pubDetailVO.setShareCount(0);
      pubDetailVO.setAwardCount(0);
      pubDetailVO.setCommentCount(0);
    } else {
      Integer shareCount = pdwhStatistics.getShareCount() == null ? 0 : pdwhStatistics.getShareCount();
      pubDetailVO.setShareCount(shareCount);
      Integer awardCount = pdwhStatistics.getAwardCount() == null ? 0 : pdwhStatistics.getAwardCount();
      pubDetailVO.setAwardCount(awardCount);
      Integer commentCount = pdwhStatistics.getCommentCount() == null ? 0 : pdwhStatistics.getCommentCount();
      pubDetailVO.setCommentCount(commentCount);
    }
    long count = pdwhPubLikeDAO.getLikeRecord(pdwhPubId, pubDetailVO.getPsnId());
    if (count > 0) {
      pubDetailVO.setIsAward(1);
    }

  }

}
