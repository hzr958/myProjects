package com.smate.center.batch.service.psn;

import java.util.List;
import java.util.Map;

import com.smate.center.batch.exception.pub.ServiceException;
import com.smate.center.batch.model.sns.pub.PsnInsSns;

/**
 * 
 * 
 */
public interface PsnInsService {

  /**
   * 添加人员与单位关系.
   * 
   * @param psnIns
   * @throws ServiceException
   */
  void addPsnIns(PsnInsSns psnIns) throws ServiceException;

  /**
   * 删除人员与单位关系.
   * 
   * @param psnId
   * @param insId
   * @throws ServiceException
   */
  void delPsnIns(Long psnId, Long insId) throws ServiceException;

  /**
   * 获取人员与单位关系.
   * 
   * @param psnId
   * @param insId
   * @return
   * @throws ServiceException
   */
  PsnInsSns findPsnInsSns(Long psnId, Long insId) throws ServiceException;

  /**
   * 保存人员与单位关系.
   * 
   * @param psnIns
   * @throws ServiceException
   */
  void savePsnInsSns(PsnInsSns psnIns) throws ServiceException;

  /**
   * 人员与单位关系的单位ID列表.
   * 
   * @param psnId
   * @return
   * @throws ServiceException
   */
  List<Long> findInsIdsByPsnId(Long psnId) throws ServiceException;

  /**
   * 人员与单位关系的单位ID列表(允许提交成果).
   * 
   * @param psnId
   * @return
   * @throws ServiceException
   */
  List<Long> findAllowSubmitInsIdsByPsnId(Long psnId) throws ServiceException;

  /**
   * 同步V2.6人员与单位关系数据，只保存状态为A的数据.
   * 
   * @param oldData
   * @param isResearch
   * @throws ServiceException
   */
  void syncOldPsnIns(Map<String, Object> oldData, boolean isResearch) throws ServiceException;

  /**
   * 查找个人加入单位.
   * 
   * @param psnId
   * @return
   */
  List<PsnInsSns> findPsnInsSnsList(Long psnId);

  void delPsnIns(PsnInsSns psnIns) throws ServiceException;

  void updatePsnIns(Long fromPsnId, Long toPsnId, Long insId) throws ServiceException;

  List<Long> findPsnInsIdsByPsnId(Long psnId) throws ServiceException;
}
