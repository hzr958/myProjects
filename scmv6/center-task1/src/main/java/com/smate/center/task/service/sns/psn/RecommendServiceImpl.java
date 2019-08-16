package com.smate.center.task.service.sns.psn;

import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.smate.center.task.dao.group.GroupInvitePsnDao;
import com.smate.center.task.dao.sns.psn.FriendSysRecommendDao;
import com.smate.center.task.dao.sns.psn.FriendSysRecommendLogDao;
import com.smate.center.task.dao.sns.psn.FriendSysRecommendScoreDao;
import com.smate.center.task.exception.DaoException;
import com.smate.center.task.exception.ServiceException;
import com.smate.center.task.model.sns.psn.FriendSysRecommend;
import com.smate.center.task.model.sns.psn.FriendSysRecommendLog;
import com.smate.center.task.model.sns.psn.FriendSysRecommendScore;
import com.smate.center.task.model.sns.psn.PsnKnowCopartner;
import com.smate.core.base.utils.model.Page;

@Service("recommendService")
@Transactional(rollbackFor = Exception.class)
public class RecommendServiceImpl implements RecommendService {
  private final Logger logger = LoggerFactory.getLogger(getClass());
  @Autowired
  private FriendSysRecommendDao friendSysRecommendDao;
  @Autowired
  private FriendSysRecommendLogDao friendSysRecommendLogDao;
  @Autowired
  private FriendSysRecommendScoreDao friendSysRecommendScoreDao;
  @Autowired
  private GroupInvitePsnDao groupInvitePsnDao;
  @Autowired
  private PsnKnowCopartnerDao psnKnowCopartnerDao;



  @Override
  public void upAppSettingConstant(String key, Object value) throws ServiceException {
    friendSysRecommendDao.upAppSettingConstantByRecommend(key, value);
  }

  @Override
  public void delAllFriendSysRecommendLog() throws ServiceException {
    friendSysRecommendLogDao.delAllFriendSysRecommendLog();
  }

  @Override
  public void removeAllRecommendScore() throws ServiceException {
    try {
      friendSysRecommendScoreDao.deleteAll();
    } catch (DaoException e) {
      logger.error("清空好友智能推荐得分表出错", e);
    }
  }

  @Override
  public FriendSysRecommendLog saveFriendSysRecommendLog(FriendSysRecommendLog recommendLog) throws ServiceException {
    friendSysRecommendLogDao.save(recommendLog);
    return recommendLog;
  }

  @Override
  public void saveRecommendScore(FriendSysRecommendScore score) throws ServiceException {
    try {
      Integer type = score.getRecommendType();
      FriendSysRecommendScore dbRcdScore =
          friendSysRecommendScoreDao.findRecommendScore(score.getPsnId(), score.getRecommendPsnId(), type);
      if (dbRcdScore != null) {
        double dbTempScore = dbRcdScore.getRecommendScore();
        if (RecommendType.RECOMMEND_WORK.equals(type) || RecommendType.RECOMMEND_EDU.equals(type)) {
          dbTempScore = dbTempScore < 100 ? Math.min((type + dbTempScore), 100) + 5 : dbTempScore + 5;
        }
        if (RecommendType.RECOMMEND_OF_FRIEND.equals(type) || RecommendType.RECOMMEND_PUBLICATION.equals(type)
            || RecommendType.RECOMMEND_PROJECT.equals(type)) {
          dbTempScore = dbTempScore + 1;
        }
        dbRcdScore.setRecommendScore(dbTempScore);
        friendSysRecommendScoreDao.save(dbRcdScore);
      } else {
        friendSysRecommendScoreDao.save(score);
      }
    } catch (Exception e) {
      logger.error("保存系统推荐人员得分出错,FriendSysRecommendScore:{}", score.toString(), e);
    }
  }

  @Override
  public List<Long> findGroupInvitePsnByPrj(List<Long> psnIds) throws ServiceException {
    return groupInvitePsnDao.findGroupInvitePsnByPrj(psnIds);
  }

  @Override
  public List<Long> findPrjGroupIdsByPsnId(Long psnId) throws ServiceException {
    return groupInvitePsnDao.findPrjGroupIdsByPsnId(psnId);
  }

  @Override
  public List<Map<String, Object>> findGroupInvitePsnIdsByMap(List<Long> groupIds, Long psnId) throws ServiceException {
    return groupInvitePsnDao.findGroupInvitePsnIdsByMap(groupIds, psnId);
  }

  @SuppressWarnings("unchecked")
  @Override
  public Page findFriendDisByPsnId(Long psnId, Page page) throws ServiceException {
    return friendSysRecommendDao.findFriendDisByPsnId(psnId, page);
  }

  @Override
  public Page<Long> findRecommendScoreByPsnId(Page<Long> page) throws ServiceException {
    return friendSysRecommendScoreDao.findRecommendScoreByPsnId(page);
  }

  @Override
  public Page<FriendSysRecommendScore> findRecommendScore(Long psnId, Page<FriendSysRecommendScore> page)
      throws ServiceException {
    return friendSysRecommendScoreDao.findRecommendScore(psnId, page);
  }

  @Override
  public void delRecommendByPsnId(Long psnId) throws ServiceException {
    // 可能用户修改了资料，未能再次匹配，需要删除这种冗余数据
    friendSysRecommendDao.findRecommendByDelPsnIds(psnId);
  }

  @Override
  public void delPsnKnowCopartnerByPsnId(Long psnId) throws ServiceException {
    psnKnowCopartnerDao.delPsnKnowCopartnerByPsnId(psnId);
  }

  @Override
  public List<FriendSysRecommendScore> findScore(Long psnId, Long recommendPsnId, Integer recommenType)
      throws ServiceException {
    return friendSysRecommendScoreDao.findScore(psnId, recommendPsnId, recommenType);
  }

  @Override
  public boolean isMax(Long psnId) throws ServiceException {
    try {
      return friendSysRecommendDao.isMax(psnId);
    } catch (DaoException e) {
      logger.error("获取智能推荐总人数出错,psnId:{}", psnId, e);
      return true;
    }
  }

  @Override
  public FriendSysRecommend getRecommendMin(Long psnId) throws ServiceException {
    try {
      return friendSysRecommendDao.getRecommendMin(psnId);
    } catch (DaoException e) {
      logger.error("根据psnId:{}，获取智能推荐好友列表出错", psnId, e);
      return null;
    }
  }

  @Override
  public void delFriendSysRecommend(Long id) throws ServiceException {
    friendSysRecommendDao.delete(id);
  }

  @Override
  public FriendSysRecommend getFriendSysRecommend(Long psnId, Long tempPsnId) throws ServiceException {
    return friendSysRecommendDao.getFriendSysRecommend(psnId, tempPsnId);
  }

  @Override
  public void save(FriendSysRecommend sysRecommend) throws ServiceException {
    friendSysRecommendDao.save(sysRecommend);
  }

  @Override
  public PsnKnowCopartner getPsnKnowCopartner(Long psnId, Long cptPsnId) throws ServiceException {
    return psnKnowCopartnerDao.getPsnKnowCopartner(psnId, cptPsnId);
  }

  @Override
  public void savePsnKnowCopartner(PsnKnowCopartner cpt) throws ServiceException {
    psnKnowCopartnerDao.save(cpt);
  }
}
