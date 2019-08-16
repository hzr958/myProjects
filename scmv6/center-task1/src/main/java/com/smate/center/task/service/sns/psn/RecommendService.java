package com.smate.center.task.service.sns.psn;

import java.util.List;
import java.util.Map;

import com.smate.center.task.exception.ServiceException;
import com.smate.center.task.model.sns.psn.FriendSysRecommend;
import com.smate.center.task.model.sns.psn.FriendSysRecommendLog;
import com.smate.center.task.model.sns.psn.FriendSysRecommendScore;
import com.smate.center.task.model.sns.psn.PsnKnowCopartner;
import com.smate.core.base.utils.model.Page;

public interface RecommendService {
  /**
   * @param psnId
   * @param tempPsnId
   * @return
   * @throws ServiceException
   */
  FriendSysRecommend getFriendSysRecommend(Long psnId, Long tempPsnId) throws ServiceException;

  /**
   * @param key
   * @param i
   * @throws ServiceException
   */
  void upAppSettingConstant(String key, Object value) throws ServiceException;

  /**
   * 删除智能推荐日志.
   * 
   * @throws ServiceException
   */
  void delAllFriendSysRecommendLog() throws ServiceException;

  /**
   * @throws ServiceException
   */
  void removeAllRecommendScore() throws ServiceException;

  /**
   * 保存智能推荐日志.
   * 
   * @param recommendLog
   * @return
   * @throws ServiceException
   */
  /**
   * 保存智能推荐日志.
   * 
   * @param recommendLog
   * @return
   * @throws ServiceException
   */
  FriendSysRecommendLog saveFriendSysRecommendLog(FriendSysRecommendLog recommendLog) throws ServiceException;

  /**
   * @param friendSysRecommendScore
   * @throws ServiceException
   */
  void saveRecommendScore(FriendSysRecommendScore friendSysRecommendScore) throws ServiceException;

  /**
   * @param page
   * @return
   * @throws ServiceException
   */

  List<Long> findGroupInvitePsnByPrj(List<Long> psnIds) throws ServiceException;

  /**
   * 获取psnId所在的项目群组Ids.
   * 
   * @param psnId
   * 
   * @return
   * @throws ServiceException
   */
  List<Long> findPrjGroupIdsByPsnId(Long psnId) throws ServiceException;

  /**
   * @param groupIds
   * @param psnId
   * @return
   * @throws ServiceException
   */
  List<Map<String, Object>> findGroupInvitePsnIdsByMap(List<Long> groupIds, Long psnId) throws ServiceException;

  /**
   * 获取学科相同的人员.
   * 
   * @param psnId
   * @return
   * @throws ServiceException
   */
  Page findFriendDisByPsnId(Long psnId, Page page) throws ServiceException;

  /**
   * @param psnId
   * @param page
   * @return
   */
  Page<Long> findRecommendScoreByPsnId(Page<Long> page) throws ServiceException;

  /**
   * @param psnId
   * @param page
   * @return
   * @throws ServiceException
   */
  Page<FriendSysRecommendScore> findRecommendScore(Long psnId, Page<FriendSysRecommendScore> page)
      throws ServiceException;

  /**
   * 根据psnId原已经存在于推荐表的旧数据,原因可能由于数据变化，导致以前推荐的人员，现在推荐得分列表中已经没有此人.
   * 
   * @param psnIdList
   * @throws ServiceException
   */
  void delRecommendByPsnId(Long psnId) throws ServiceException;

  /**
   * @param psnId
   * @throws ServiceException
   */
  void delPsnKnowCopartnerByPsnId(Long psnId) throws ServiceException;

  /**
   * @param psnId
   * @param recommendPsnId
   * @return
   * @throws ServiceException
   */
  List<FriendSysRecommendScore> findScore(Long psnId, Long recommendPsnId, Integer recommenType)
      throws ServiceException;

  /**
   * 是否有100条记录.
   * 
   * @param psnId
   * @return
   * @throws ServiceException
   */
  boolean isMax(Long psnId) throws ServiceException;

  /**
   * @param psnId
   * @return
   * @throws ServiceException
   */
  FriendSysRecommend getRecommendMin(Long psnId) throws ServiceException;

  void delFriendSysRecommend(Long id) throws ServiceException;

  /**
   * @param friendSysRecommend
   * @throws ServiceException
   */
  void save(FriendSysRecommend friendSysRecommend) throws ServiceException;

  /**
   * @param psnId
   * @return
   * @throws ServiceException
   */
  PsnKnowCopartner getPsnKnowCopartner(Long psnId, Long cptPsnId) throws ServiceException;

  /**
   * @param cpt
   * @throws ServiceException
   */
  void savePsnKnowCopartner(PsnKnowCopartner cpt) throws ServiceException;
}
