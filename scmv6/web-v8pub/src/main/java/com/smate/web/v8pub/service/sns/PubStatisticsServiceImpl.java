package com.smate.web.v8pub.service.sns;

import java.util.List;

import org.apache.commons.collections4.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.smate.core.base.pub.enums.PubDbEnum;
import com.smate.core.base.pub.po.dao.PubPdwhSnsRelationDAO;
import com.smate.core.base.pub.vo.PubDetailVO;
import com.smate.web.v8pub.dao.sns.CollectedPubDao;
import com.smate.web.v8pub.dao.sns.PubLikeDAO;
import com.smate.web.v8pub.dao.sns.PubStatisticsDAO;
import com.smate.web.v8pub.enums.PubLikeStatusEnum;
import com.smate.web.v8pub.exception.ServiceException;
import com.smate.web.v8pub.po.sns.PubStatisticsPO;

@Service("newPubStatisticsService")
@Transactional(rollbackFor = Exception.class)
public class PubStatisticsServiceImpl implements PubStatisticsService {
  private Logger logger = LoggerFactory.getLogger(getClass());

  @Autowired
  private PubStatisticsDAO pubStatisticsDAO;
  @Autowired
  private PubPdwhSnsRelationDAO pubPdwhSnsRelationDAO;
  @Autowired
  private PubLikeDAO pubLikeDAO;
  @Autowired
  private CollectedPubDao collectedPubDao;

  @Override
  public void updateCommentStatistics(Long pubId) throws ServiceException {
    try {
      PubStatisticsPO pubStatisticsPO = pubStatisticsDAO.get(pubId);
      if (pubStatisticsPO == null) {
        pubStatisticsPO = new PubStatisticsPO();
        pubStatisticsPO.setPubId(pubId);
        pubStatisticsPO.setCommentCount(1);
      } else {
        int commentCount = pubStatisticsPO.getCommentCount() == null ? 0 : pubStatisticsPO.getCommentCount();
        pubStatisticsPO.setCommentCount(commentCount + 1);
      }
      pubStatisticsDAO.save(pubStatisticsPO);
    } catch (Exception e) {
      logger.error("更新评论统计数出错！pubId={}", pubId);
      throw new ServiceException(e);
    }

  }

  @Override
  public void updateShareStatistics(Long pubId) throws ServiceException {
    try {
      PubStatisticsPO pubStatisticsPO = pubStatisticsDAO.get(pubId);
      if (pubStatisticsPO == null) {
        pubStatisticsPO = new PubStatisticsPO();
        pubStatisticsPO.setPubId(pubId);
        pubStatisticsPO.setShareCount(1);
      } else {
        int shareCount = pubStatisticsPO.getShareCount() == null ? 0 : pubStatisticsPO.getShareCount();
        pubStatisticsPO.setShareCount(shareCount + 1);
      }
      pubStatisticsDAO.save(pubStatisticsPO);
    } catch (Exception e) {
      logger.error("更新分享统计数出错！pubId={}", pubId);
      throw new ServiceException(e);
    }
  }

  @Override
  public void updateAwardStatistics(Long pubId, Integer awardCount) throws ServiceException {
    try {
      PubStatisticsPO pubStatisticsPO = pubStatisticsDAO.get(pubId);
      if (pubStatisticsPO == null) {
        pubStatisticsPO = new PubStatisticsPO(pubId, 0, 0, 0, 0, 0);
      } else {
        pubStatisticsPO.setAwardCount(awardCount);
      }
      pubStatisticsDAO.save(pubStatisticsPO);
    } catch (Exception e) {
      logger.error("更新赞统计数出错！pubId={}", pubId);
      throw new ServiceException(e);
    }

  }

  @Override
  public void updateReadStatistics(Long pubId) throws ServiceException {
    try {
      PubStatisticsPO pubStatisticsPO = pubStatisticsDAO.get(pubId);
      if (pubStatisticsPO == null) {
        pubStatisticsPO = new PubStatisticsPO();
        pubStatisticsPO.setPubId(pubId);
        pubStatisticsPO.setReadCount(1);
      } else {
        int readCount = pubStatisticsPO.getReadCount() == null ? 0 : pubStatisticsPO.getReadCount();
        pubStatisticsPO.setReadCount(readCount + 1);
      }
      pubStatisticsDAO.save(pubStatisticsPO);
    } catch (Exception e) {
      logger.error("更新阅读统计数出错！pubId={}", pubId);
      throw new ServiceException(e);
    }
  }

  @Override
  public void updateRefStatistics(Long pubId, Integer refCount) throws ServiceException {
    try {
      PubStatisticsPO pubStatisticsPO = pubStatisticsDAO.get(pubId);
      if (pubStatisticsPO == null) {
        pubStatisticsPO = new PubStatisticsPO(pubId, 0, 0, 0, 0, 0);
      } else {
        pubStatisticsPO.setRefCount(refCount);
      }
      pubStatisticsDAO.save(pubStatisticsPO);
    } catch (Exception e) {
      logger.error("更新引用统计数出错！pubId={}", pubId);
      throw new ServiceException(e);
    }
  }

  /**
   * 个人库 赞/取消操作 更新赞统计数
   */
  @Override
  public void updateLikeStatistics(Long pubId, PubLikeStatusEnum status) throws ServiceException {
    try {
      PubStatisticsPO pubStatisticsPO = pubStatisticsDAO.get(pubId);
      if (pubStatisticsPO == null) {
        pubStatisticsPO = new PubStatisticsPO();
        pubStatisticsPO.setPubId(pubId);
      }
      Integer awardCount = pubStatisticsPO.getAwardCount() == null ? 0 : pubStatisticsPO.getAwardCount();
      switch (status) {
        case PUB_LIKE:
          pubStatisticsPO.setAwardCount(awardCount + 1);
          break;
        case PUB_UNLIKE:
          pubStatisticsPO.setAwardCount(awardCount - 1 < 0 ? 0 : pubStatisticsPO.getAwardCount() - 1);
          break;
      }
      pubStatisticsDAO.save(pubStatisticsPO);
    } catch (Exception e) {
      logger.error("个人库更新赞统计数异常,pubId=" + pubId, e);
      throw new ServiceException(e);
    }

  }

  @Override
  public PubStatisticsPO get(Long id) throws ServiceException {
    return pubStatisticsDAO.get(id);
  }

  @Override
  public void save(PubStatisticsPO pubStatisticsPO) throws ServiceException {
    try {
      pubStatisticsDAO.save(pubStatisticsPO);
    } catch (Exception e) {
      logger.error("保存成果统计数据失败，PubStatisticsPO={}", pubStatisticsPO, e);
      throw new ServiceException(e);
    }

  }

  @Override
  public void update(PubStatisticsPO pubStatisticsPO) throws ServiceException {
    try {
      pubStatisticsDAO.update(pubStatisticsPO);
    } catch (Exception e) {
      logger.error("更新成果统计数据失败，PubStatisticsPO={}", pubStatisticsPO, e);
      throw new ServiceException(e);
    }

  }

  @Override
  public void saveOrUpdate(PubStatisticsPO pubStatisticsPO) throws ServiceException {
    try {
      pubStatisticsDAO.saveOrUpdate(pubStatisticsPO);
    } catch (Exception e) {
      logger.error("保存更新成果统计数据失败，PubStatisticsPO={}", pubStatisticsPO, e);
      throw new ServiceException(e);
    }

  }

  @Override
  public void deleteById(Long id) throws ServiceException {
    try {
      pubStatisticsDAO.delete(id);
    } catch (Exception e) {
      logger.error("根据id删除成果统计数据失败，id={}", id, e);
      throw new ServiceException(e);
    }

  }

  @Override
  public void delete(PubStatisticsPO entity) throws ServiceException {
    // TODO Auto-generated method stub

  }

  @Override
  public Long getSnsPubReadCounts(Long pdwhPubId) throws ServiceException {
    Long readCount = 0L;
    List<Long> snsPubIds = pubPdwhSnsRelationDAO.getSnsPubIdListByPdwhId(pdwhPubId);
    if (CollectionUtils.isNotEmpty(snsPubIds)) {
      readCount = pubStatisticsDAO.findPubReadCounts(snsPubIds);
      if (readCount == null) {
        readCount = 0L;
      }
    }
    return readCount;
  }

  @Override
  public int getAwardCounts(Long pubId) throws ServiceException {
    int awardCount = 0;
    PubStatisticsPO pubStatistics = pubStatisticsDAO.get(pubId);
    if (pubStatistics != null) {
      awardCount = pubStatistics.getAwardCount() == null ? 0 : pubStatistics.getAwardCount();
    }
    return awardCount;
  }

  /**
   * 初始化统计信息
   */
  @Override
  public void initStatistics(PubDetailVO pubDetailVO) throws ServiceException {
    PubStatisticsPO pubStatistics = pubStatisticsDAO.get(pubDetailVO.getPubId());
    if (pubStatistics == null) {
      pubDetailVO.setShareCount(0);
      pubDetailVO.setAwardCount(0);
      pubDetailVO.setCommentCount(0);
    } else {
      Integer shareCount = pubStatistics.getShareCount() == null ? 0 : pubStatistics.getShareCount();
      pubDetailVO.setShareCount(shareCount);
      Integer awardCount = pubStatistics.getAwardCount() == null ? 0 : pubStatistics.getAwardCount();
      pubDetailVO.setAwardCount(awardCount);
      pubDetailVO.setCommentCount(pubStatistics.getCommentCount() == null ? 0 : pubStatistics.getCommentCount());
    }
    long count = pubLikeDAO.getLikeRecord(pubDetailVO.getPubId(), pubDetailVO.getPsnId());
    if (count > 0) {
      pubDetailVO.setIsAward(1);
    }

  }

  @Override
  public Boolean isCollectedPub(Long psnId, Long pubId, PubDbEnum pubDb) throws ServiceException {
    boolean collectedPub = false;
    if (PubDbEnum.SNS.equals(pubDb)) {
      collectedPub = collectedPubDao.isCollectedPub(psnId, pubId, PubDbEnum.SNS);
    } else {
      collectedPub = collectedPubDao.isCollectedPub(psnId, pubId, PubDbEnum.PDWH);
    }
    return collectedPub;
  }

  @Override
  public Long countPubTotalCitations(List<Long> pubIdList) throws ServiceException {
    try {
      return pubStatisticsDAO.countPubTotalCitations(pubIdList);
    } catch (Exception e) {
      logger.error("统计所有成果的引用数之和异常，pubIds={}", pubIdList, e);
      throw new ServiceException(e);
    }
  }

  @Override
  public int getShareCounts(Long pubId) throws ServiceException {
    int shareCount = 0;
    PubStatisticsPO pubStatistics = pubStatisticsDAO.get(pubId);
    if (pubStatistics != null) {
      shareCount = pubStatistics.getShareCount() == null ? 0 : pubStatistics.getShareCount();
    }
    return shareCount;
  }

}
