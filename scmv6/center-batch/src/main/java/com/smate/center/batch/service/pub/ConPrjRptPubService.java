package com.smate.center.batch.service.pub;

import java.io.Serializable;
import java.util.List;

import com.smate.center.batch.exception.pub.ServiceException;
import com.smate.center.batch.model.sns.pub.ConPrjRptPub;

public interface ConPrjRptPubService extends Serializable {

  /**
   * 查找某个人的报告成果
   * 
   * @param psnId
   * @param nsfcRptId
   * @return
   * @throws ServiceException
   */
  public List<ConPrjRptPub> findRptPubs(Long psnId, Long nsfcRptId) throws ServiceException;

  /**
   * 获取以提交的成果ID
   * 
   * @param nsfcRptId
   * @return
   * @throws ServiceException
   */
  public List<Long> findRptPubIds(Long nsfcRptId) throws ServiceException;

  /**
   * 添加成果到报告
   * 
   * @param nsfcRptId
   * @param pubIds
   * @throws ServiceException
   */
  public void addPubFromMate(Long nsfcRptId, String pubIds) throws ServiceException;

  /**
   * 移除报告的成果
   * 
   * @param psnId
   * @param nsfcRptId
   * @param pubIds
   * @throws ServiceException
   */
  public void removePubFromRpt(Long psnId, Long nsfcRptId, String pubIds) throws ServiceException;

  /**
   * 更新标注
   * 
   * @param nsfcRptId
   * @param pubId
   * @param isTag
   * @throws ServiceException
   */
  public void updateTag(Long nsfcRptId, Long pubId, Integer isTag) throws ServiceException;

  /**
   * 更新引用情况
   * 
   * @param nsfcRptId
   * @param pubId
   * @param listInfo
   * @throws ServiceException
   */
  public void updateListInfo(Long nsfcRptId, Long pubId, String listInfo) throws ServiceException;

  /**
   * 成果排序
   * 
   * @param nsfcRptId
   * @param jsonParams
   * @throws ServiceException
   */
  public void updatePubSeq(Long nsfcRptId, String jsonParams) throws ServiceException;

  /**
   * 更新SCM成果到报告成果
   * 
   * @param pubId
   * @throws ServiceException
   */
  public void updateMatePubToConPub(Long pubId) throws ServiceException;
}
