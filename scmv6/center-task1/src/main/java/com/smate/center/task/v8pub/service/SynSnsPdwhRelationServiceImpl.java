package com.smate.center.task.v8pub.service;

import java.util.List;

import org.apache.commons.collections4.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.smate.center.task.exception.ServiceException;
import com.smate.center.task.v8pub.dao.pdwh.PdwhPubCommentDAO;
import com.smate.center.task.v8pub.dao.pdwh.PdwhPubLikeDAO;
import com.smate.center.task.v8pub.dao.pdwh.PdwhPubShareDAO;
import com.smate.center.task.v8pub.dao.pdwh.PdwhPubStatisticsDAO;
import com.smate.center.task.v8pub.dao.sns.PubCommentDAO;
import com.smate.center.task.v8pub.dao.sns.PubLikeDAO;
import com.smate.center.task.v8pub.dao.sns.PubPdwhSnsRelationBakCommentDAO;
import com.smate.center.task.v8pub.dao.sns.PubPdwhSnsRelationBakDAO;
import com.smate.center.task.v8pub.dao.sns.PubPdwhSnsRelationBakShareDAO;
import com.smate.center.task.v8pub.dao.sns.PubShareDAO;
import com.smate.center.task.v8pub.dao.sns.PubStatisticsDAO;
import com.smate.center.task.v8pub.pdwh.po.PdwhPubCommentPO;
import com.smate.center.task.v8pub.pdwh.po.PdwhPubLikePO;
import com.smate.center.task.v8pub.pdwh.po.PdwhPubSharePO;
import com.smate.center.task.v8pub.pdwh.po.PdwhPubStatisticsPO;
import com.smate.center.task.v8pub.sns.po.PubCommentPO;
import com.smate.center.task.v8pub.sns.po.PubLikePO;
import com.smate.center.task.v8pub.sns.po.PubSharePO;
import com.smate.center.task.v8pub.sns.po.PubStatisticsPO;
import com.smate.core.base.pub.po.dao.PubPdwhSnsRelationDAO;

@Service
@Transactional(rollbackFor = Exception.class)
public class SynSnsPdwhRelationServiceImpl implements SynSnsPdwhRelationService {
  protected final Logger logger = LoggerFactory.getLogger(getClass());
  @Autowired
  private PubPdwhSnsRelationBakDAO pubPdwhSnsRelationBakDAO;
  @Autowired
  private PubPdwhSnsRelationBakCommentDAO pubPdwhSnsRelationBakCommentDAO;
  @Autowired
  private PubPdwhSnsRelationBakShareDAO pubPdwhSnsRelationBakShareDAO;
  @Autowired
  private PubStatisticsDAO pubStatisticsDAO;
  @Autowired
  private PubLikeDAO pubLikeDAO;
  @Autowired
  private PdwhPubLikeDAO pdwhPubLikeDAO;
  @Autowired
  private PdwhPubStatisticsDAO pdwhPubStatisticsDAO;
  @Autowired
  private PdwhPubCommentDAO pdwhPubCommentDAO;
  @Autowired
  private PubCommentDAO pubCommentDAO;
  @Autowired
  private PdwhPubShareDAO pdwhPubShareDAO;
  @Autowired
  private PubShareDAO pubShareDAO;
  @Autowired
  private PubPdwhSnsRelationDAO pubPdwhSnsRelationDAO;

  @Override
  public List<Long> getPubPdwhIdList(Long startId, Long endId, Integer size) throws ServiceException {
    return pubPdwhSnsRelationBakDAO.getPubPdwhIdList(startId, endId, size);
  }


  @Override
  public List<Long> getSnsPubListByPdwhId(Long pdwhPubId) throws ServiceException {
    return pubPdwhSnsRelationDAO.getSnsPubIdListByPdwhId(pdwhPubId);
  }

  @Override
  public List<Long> getCommentPubPdwhIdList(Long startId, Long endId, Integer size) throws ServiceException {
    return pubPdwhSnsRelationBakCommentDAO.getPubPdwhIdList(startId, endId, size);
  }


  @Override
  public List<Long> getSharePubPdwhIdList(Long startId, Long endId, Integer size) throws ServiceException {
    return pubPdwhSnsRelationBakShareDAO.getPubPdwhIdList(startId, endId, size);
  }

  @Override
  public void synSnsLikeToPdwh(Long pdwhPubId, Long snsPubId) throws ServiceException {
    List<PubLikePO> pubLikeList = pubLikeDAO.findByPubId(snsPubId);
    if (pubLikeList != null && pubLikeList.size() > 0) {
      for (PubLikePO pubLike : pubLikeList) {
        insertLikeSnsToPdwh(pdwhPubId, pubLike);// 将个人库的赞记录同步到基准库，有更新，没有新增
      }
    }
  }

  @Override
  public void synPdwhLikeToSns(Long pdwhPubId) throws ServiceException {
    List<Long> snsPubIds = getSnsPubListByPdwhId(pdwhPubId);
    List<PdwhPubLikePO> pdwhLikeList = pdwhPubLikeDAO.findByPubId(pdwhPubId);
    Long awardPdwhCount = pdwhPubLikeDAO.getAwardCount(pdwhPubId);
    if (CollectionUtils.isNotEmpty(snsPubIds)) {
      for (Long snsPubId : snsPubIds) {
        pubLikeDAO.deleteAll(snsPubId);// 先删除，后插入
        if (CollectionUtils.isNotEmpty(pdwhLikeList)) {
          for (PdwhPubLikePO pdwhPubLike : pdwhLikeList) {
            insertPdwhLikeToSns(snsPubId, pdwhPubLike);// 将基准库的赞记录插入到个人库
          }
        }
        PubStatisticsPO pubStatistics = pubStatisticsDAO.get(snsPubId);// 初始化赞统计数
        if (pubStatistics != null) {
          pubStatistics.setAwardCount(awardPdwhCount.intValue());
        } else {
          pubStatistics = new PubStatisticsPO(snsPubId, awardPdwhCount.intValue(), 0, 0, 0, 0);
        }
        pubStatisticsDAO.save(pubStatistics);
      }
    }
  }

  public void insertPdwhLikeToSns(Long pubId, PdwhPubLikePO pdwhPubLike) {
    PubLikePO pubLikePO = new PubLikePO();
    pubLikePO.setPubId(pubId);
    pubLikePO.setPsnId(pdwhPubLike.getPsnId());
    pubLikePO.setStatus(pdwhPubLike.getStatus());
    pubLikePO.setGmtCreate(pdwhPubLike.getGmtCreate());
    pubLikePO.setGmtModified(pdwhPubLike.getGmtModified());
    pubLikeDAO.save(pubLikePO);
  }

  @Override
  public void initPdwhLikeStatistics(Long pdwhPubId) throws ServiceException {
    Long awardPdwhCount = pdwhPubLikeDAO.getAwardCount(pdwhPubId);
    PdwhPubStatisticsPO pdwhStatistics = pdwhPubStatisticsDAO.get(pdwhPubId);
    if (pdwhStatistics != null) {
      pdwhStatistics.setAwardCount(awardPdwhCount.intValue());
    } else {
      pdwhStatistics = new PdwhPubStatisticsPO(pdwhPubId, awardPdwhCount.intValue(), 0, 0, 0, 0);
    }
    pdwhPubStatisticsDAO.save(pdwhStatistics);
  }

  public void insertLikeSnsToPdwh(Long pdwhPubId, PubLikePO pubLike) {
    PdwhPubLikePO pdwhPubLike = pdwhPubLikeDAO.findByPubIdAndPsnId(pdwhPubId, pubLike.getPsnId());
    if (pdwhPubLike == null) {
      pdwhPubLike = new PdwhPubLikePO();
      pdwhPubLike.setPdwhPubId(pdwhPubId);
      pdwhPubLike.setPsnId(pubLike.getPsnId());
    }
    pdwhPubLike.setStatus(pubLike.getStatus());
    pdwhPubLike.setGmtCreate(pubLike.getGmtCreate());
    pdwhPubLike.setGmtModified(pubLike.getGmtModified());
    pdwhPubLikeDAO.saveOrUpdate(pdwhPubLike);
  }

  @Override
  public void updatePubPdwhSnsRelationBak(Long pdwhPubId, int status, String errMsg) throws ServiceException {
    pubPdwhSnsRelationBakDAO.updatePubPdwhSnsRelationBak(pdwhPubId, status, errMsg);
  }

  /**
   * 关联成果，将个人库的评论记录同步到基准库
   */
  @Override
  public void synSnsCommentToPdwh(Long pdwhPubId, Long snsPubId) throws ServiceException {
    List<PubCommentPO> pubCommentList = pubCommentDAO.findByPubId(snsPubId);
    if (pubCommentList != null && pubCommentList.size() > 0) {
      for (PubCommentPO pubComment : pubCommentList) {
        // 将个人库的评论记录同步到基准库，直接新增
        PdwhPubCommentPO pdwhPubCommentPO = new PdwhPubCommentPO();
        pdwhPubCommentPO.setPdwhPubId(pdwhPubId);
        pdwhPubCommentPO.setPsnId(pubComment.getPsnId());
        pdwhPubCommentPO.setContent(pubComment.getContent());
        pdwhPubCommentPO.setStatus(pubComment.getStatus());
        pdwhPubCommentPO.setGmtCreate(pubComment.getGmtCreate());
        pdwhPubCommentPO.setGmtModified(pubComment.getGmtModified());
        pdwhPubCommentDAO.saveOrUpdate(pdwhPubCommentPO);
      }
    }
  }

  /**
   * 初始化基准库的评论统计数
   */
  @Override
  public void initPdwhCommentStatistics(Long pdwhPubId) throws ServiceException {
    Long commentPdwhCount = pdwhPubCommentDAO.getCommentsCount(pdwhPubId);
    PdwhPubStatisticsPO pdwhStatistics = pdwhPubStatisticsDAO.get(pdwhPubId);
    if (pdwhStatistics != null) {
      pdwhStatistics.setCommentCount(commentPdwhCount.intValue());
    } else {
      pdwhStatistics = new PdwhPubStatisticsPO(pdwhPubId, 0, 0, commentPdwhCount.intValue(), 0, 0);
    }
    pdwhPubStatisticsDAO.save(pdwhStatistics);
  }

  /**
   * 将关联成果基准库的评论记录同步到个人库
   */
  @Override
  public void synPdwhCommentToSns(Long pdwhPubId) throws ServiceException {
    List<Long> snsPubIds = getSnsPubListByPdwhId(pdwhPubId);
    List<PdwhPubCommentPO> pdwhCommentList = pdwhPubCommentDAO.findByPubId(pdwhPubId);
    Long commentPdwhCount = pdwhPubCommentDAO.getCommentsCount(pdwhPubId);
    if (CollectionUtils.isNotEmpty(snsPubIds)) {
      for (Long snsPubId : snsPubIds) {
        pubCommentDAO.deleteAll(snsPubId);// 先删除，后插入
        if (CollectionUtils.isNotEmpty(pdwhCommentList)) {
          for (PdwhPubCommentPO pdwhPubComment : pdwhCommentList) {
            // 将基准库的评论记录插入到个人库
            PubCommentPO pubCommentPO = new PubCommentPO();
            pubCommentPO.setPubId(snsPubId);
            pubCommentPO.setPsnId(pdwhPubComment.getPsnId());
            pubCommentPO.setContent(pdwhPubComment.getContent());
            pubCommentPO.setStatus(pdwhPubComment.getStatus());
            pubCommentPO.setGmtCreate(pdwhPubComment.getGmtCreate());
            pubCommentPO.setGmtModified(pdwhPubComment.getGmtModified());
            pubCommentDAO.saveOrUpdate(pubCommentPO);
          }
        }
        PubStatisticsPO pubStatistics = pubStatisticsDAO.get(snsPubId);// 初始化赞统计数
        if (pubStatistics != null) {
          pubStatistics.setCommentCount(commentPdwhCount.intValue());
        } else {
          pubStatistics = new PubStatisticsPO(snsPubId, 0, 0, commentPdwhCount.intValue(), 0, 0);
        }
        pubStatisticsDAO.save(pubStatistics);
      }
    }
  }

  @Override
  public void updateCommentPubPdwhSnsRelationBak(Long pdwhPubId, int status, String errMsg) throws ServiceException {
    pubPdwhSnsRelationBakCommentDAO.updatePubPdwhSnsRelationBak(pdwhPubId, status, errMsg);
  }

  /**
   * 关联成果，将个人库的分享记录同步到基准库
   */
  @Override
  public void synSnsShareToPdwh(Long pdwhPubId, Long snsPubId) throws ServiceException {
    List<PubSharePO> pubShareList = pubShareDAO.getShareRecords(snsPubId);
    if (pubShareList != null && pubShareList.size() > 0) {
      for (PubSharePO pubShare : pubShareList) {
        // 将个人库的分享记录同步到基准库，直接新增
        PdwhPubSharePO pdwhShare = new PdwhPubSharePO();
        pdwhShare.setPdwhPubId(pdwhPubId);
        pdwhShare.setPsnId(pubShare.getPsnId());
        pdwhShare.setComment(pubShare.getComment());
        pdwhShare.setPlatform(pubShare.getPlatform());
        pdwhShare.setStatus(pubShare.getStatus());
        pdwhShare.setSharePsnGroupId(pubShare.getSharePsnGroupId());
        pdwhShare.setGmtCreate(pubShare.getGmtCreate());
        pdwhShare.setGmtModified(pubShare.getGmtModified());
        pdwhPubShareDAO.saveOrUpdate(pdwhShare);
      }
    }
  }

  /**
   * 初始化基准库的分享统计数
   */
  @Override
  public void initPdwhShareStatistics(Long pdwhPubId) throws ServiceException {
    Long sharePdwhCount = pdwhPubShareDAO.getShareCount(pdwhPubId);
    PdwhPubStatisticsPO pdwhStatistics = pdwhPubStatisticsDAO.get(pdwhPubId);
    if (pdwhStatistics != null) {
      pdwhStatistics.setShareCount(sharePdwhCount.intValue());
    } else {
      pdwhStatistics = new PdwhPubStatisticsPO(pdwhPubId, 0, sharePdwhCount.intValue(), 0, 0, 0);
    }
    pdwhPubStatisticsDAO.save(pdwhStatistics);
  }

  /**
   * 将关联成果基准库的评论记录同步到个人库
   */
  @Override
  public void synPdwhShareToSns(Long pdwhPubId) throws ServiceException {
    List<Long> snsPubIds = getSnsPubListByPdwhId(pdwhPubId);
    List<PdwhPubSharePO> pdwhShareList = pdwhPubShareDAO.getShareRecords(pdwhPubId);
    Long sharePdwhCount = pdwhPubShareDAO.getShareCount(pdwhPubId);
    if (CollectionUtils.isNotEmpty(snsPubIds)) {
      for (Long snsPubId : snsPubIds) {
        pubShareDAO.deleteAll(snsPubId);// 先删除，后插入
        if (CollectionUtils.isNotEmpty(pdwhShareList)) {
          for (PdwhPubSharePO pdwhPubShare : pdwhShareList) {
            // 将基准库的评论记录插入到个人库
            PubSharePO pubSharePO = new PubSharePO();
            pubSharePO.setPubId(snsPubId);
            pubSharePO.setPsnId(pdwhPubShare.getPsnId());
            pubSharePO.setComment(pdwhPubShare.getComment());
            pubSharePO.setPlatform(pdwhPubShare.getPlatform());
            pubSharePO.setStatus(pdwhPubShare.getStatus());
            pubSharePO.setSharePsnGroupId(pdwhPubShare.getSharePsnGroupId());
            pubSharePO.setGmtCreate(pdwhPubShare.getGmtCreate());
            pubSharePO.setGmtModified(pdwhPubShare.getGmtModified());
            pubShareDAO.saveOrUpdate(pubSharePO);
          }
        }
        PubStatisticsPO pubStatistics = pubStatisticsDAO.get(snsPubId);// 初始化赞统计数
        if (pubStatistics != null) {
          pubStatistics.setShareCount(sharePdwhCount.intValue());
        } else {
          pubStatistics = new PubStatisticsPO(snsPubId, 0, sharePdwhCount.intValue(), 0, 0, 0);
        }
        pubStatisticsDAO.save(pubStatistics);
      }
    }
  }

  @Override
  public void updateSharePubPdwhSnsRelationBak(Long pdwhPubId, int status, String errMsg) throws ServiceException {
    pubPdwhSnsRelationBakShareDAO.updatePubPdwhSnsRelationBak(pdwhPubId, status, errMsg);
  }

  @Override
  public void synShare(Long pdwhPubId) throws ServiceException {
    int status = 1;
    String errMsg = "";
    try {
      List<Long> snsPubIds = getSnsPubListByPdwhId(pdwhPubId);
      if (snsPubIds != null && snsPubIds.size() > 0) {
        for (Long snsPubId : snsPubIds) {
          // 将关联成果sns库的历史分享记录同步到基准库
          synSnsShareToPdwh(pdwhPubId, snsPubId);
        }
        // 初始化分享统计数
        initPdwhShareStatistics(pdwhPubId);
        // 将基准库的分享记录同步到关联的sns库
        synPdwhShareToSns(pdwhPubId);

      }
    } catch (Exception e) {
      logger.error("关联成果个人库基准库分享记录同步数据出错！" + pdwhPubId, e);
      status = 99;
      errMsg = e.getMessage();
    }
    updateSharePubPdwhSnsRelationBak(pdwhPubId, status, errMsg);
  }

  public void synLike(Long pdwhPubId) throws ServiceException {
    int status = 1;
    String errMsg = "";
    try {
      List<Long> snsPubIds = getSnsPubListByPdwhId(pdwhPubId);
      if (snsPubIds != null && snsPubIds.size() > 0) {
        for (Long snsPubId : snsPubIds) {
          // 将关联成果sns库的历史赞记录同步到基准库
          synSnsLikeToPdwh(pdwhPubId, snsPubId);
        }
        // 初始化赞统计数
        initPdwhLikeStatistics(pdwhPubId);
        // 将基准库的赞记录同步到关联的sns库
        synPdwhLikeToSns(pdwhPubId);

      }
    } catch (Exception e) {
      logger.error("关联成果个人库基准库赞记录同步数据出错！" + pdwhPubId, e);
      status = 99;
      errMsg = e.getMessage();
    }
    updatePubPdwhSnsRelationBak(pdwhPubId, status, errMsg);
  }

  public void synComment(Long pdwhPubId) throws ServiceException {
    int status = 1;
    String errMsg = "";
    try {
      List<Long> snsPubIds = getSnsPubListByPdwhId(pdwhPubId);
      if (snsPubIds != null && snsPubIds.size() > 0) {
        for (Long snsPubId : snsPubIds) {
          // 将关联成果sns库的历史评论记录同步到基准库
          synSnsCommentToPdwh(pdwhPubId, snsPubId);
        }
        // 初始化评论统计数
        initPdwhCommentStatistics(pdwhPubId);
        // 将基准库的评论记录同步到关联的sns库
        synPdwhCommentToSns(pdwhPubId);

      }
    } catch (Exception e) {
      logger.error("关联成果个人库基准库评论记录同步数据出错！" + pdwhPubId, e);
      status = 99;
      errMsg = e.getMessage();
    }
    updateCommentPubPdwhSnsRelationBak(pdwhPubId, status, errMsg);
  }
}
