package com.smate.center.batch.service.psn.inforefresh;

import java.util.List;

import com.smate.center.batch.exception.pub.ServiceException;
import com.smate.center.batch.model.sns.psn.PsnRefPsnRecScore;

public interface PsnRefPsnRecScoreService {

  /**
   * 个人文献推荐- 删除给个人文献推荐的临时数据
   * 
   * @throws ServiceException
   */
  void truncatePsnRefRecommendScore() throws ServiceException;

  /**
   * 个人文献推荐-推荐度(相关度 +质量)×Ln(2.72+合作度)
   * 
   * @param psnId
   * @throws ServiceException
   */
  void psnRefDegrees(Long psnId) throws ServiceException;

  /**
   * @param psnRefRecScore
   * @throws ServiceException
   */
  void save(PsnRefPsnRecScore psnRefRecScore) throws ServiceException;

  /**
   * 获取中英文推荐文献排名前size条数据
   * 
   * @param size
   * @return
   * @throws ServiceException
   */
  List<PsnRefPsnRecScore> getDescRefList(Long psnId, int language, int size) throws ServiceException;
}
