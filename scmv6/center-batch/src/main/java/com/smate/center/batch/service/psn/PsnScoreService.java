package com.smate.center.batch.service.psn;

import java.util.List;

import com.smate.center.batch.exception.pub.ServiceException;
import com.smate.center.batch.model.sns.pub.PsnScoreDetail;
import com.smate.center.batch.model.sns.pub.PsnScoreInit;
import com.smate.center.batch.model.sns.pub.PsnScoreRefresh;

/**
 * 人员信息完整度service接口.
 * 
 * @author chenxiangrong
 * 
 */
public interface PsnScoreService {


  /**
   * 得分详情.
   * 
   * @param psnId
   * @return
   * @throws ServiceException
   */
  List<PsnScoreDetail> getPsnScoreDetail(Long psnId) throws ServiceException;

  /**
   * 获取人员信息度刷新记录列表.
   * 
   * @param maxSize
   * @return
   * @throws ServiceException
   */
  List<PsnScoreRefresh> getPsnScoreRefreshList(int maxSize) throws ServiceException;

  /**
   * 需要初始化的列表.
   * 
   * @param maxSize
   * @return
   * @throws ServiceException
   */
  List<PsnScoreInit> getPsnScoreInitList(int maxSize) throws ServiceException;


  /**
   * 删除初始化记录.
   * 
   * @param psnId
   * @throws ServiceException
   */
  void deletePsnScoreInit(Long psnId) throws ServiceException;


  /**
   * 删除刷新记录.
   * 
   * @param psnId
   * @throws ServiceException
   */
  void deletePsnScoreRefresh(Long psnId) throws ServiceException;

  /**
   * 保存需初始化计分的人员记录.
   * 
   * @param psnId
   */
  void savePsnScoreInit(Long psnId);

  List<PsnScoreInit> getpsnScoreInit(Long psnId) throws ServiceException;

  List<PsnScoreRefresh> getPsnScoreRefresh(Long psnId) throws ServiceException;

  void delScoreRefresh(PsnScoreRefresh psnScoreRefresh) throws ServiceException;

  void delScoreInit(PsnScoreInit psnScoreInit) throws ServiceException;

  void delScoreDetail(PsnScoreDetail psnScoreDetail) throws ServiceException;
}
