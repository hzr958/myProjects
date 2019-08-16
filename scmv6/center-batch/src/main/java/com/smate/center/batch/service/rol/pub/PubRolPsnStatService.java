package com.smate.center.batch.service.rol.pub;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

import com.smate.center.batch.exception.pub.ServiceException;
import com.smate.center.batch.model.rol.pub.PubRolPsnStat;
import com.smate.core.base.utils.model.Page;


/**
 * 成果提交统计数.
 * 
 * @author liqinghua
 * 
 */
public interface PubRolPsnStatService extends Serializable {

  /**
   * 更新单位成果指派统计数.
   * 
   * @param insId
   * @throws ServiceException
   */
  public void refreshPubRolPsnStat(Long insId) throws ServiceException;

  /**
   * 更新单位人员成果指派统计数.
   * 
   * @param insId
   * @param psnId
   * @throws ServiceException
   */
  public void refreshPubRolPsnStat(Long insId, Long psnId) throws ServiceException;

  /**
   * 更新单位人员成果指派统计数.
   * 
   * @param insId
   * @param psnIds
   * @throws ServiceException
   */
  public void refreshPubRolPsnStat(Long insId, List<Long> psnIds) throws ServiceException;

  /**
   * 更新成果人员指派统计数.
   * 
   * @param insId
   * @param pubId
   * @throws ServiceException
   */
  public void refreshPubPsnStat(Long insId, Long pubId) throws ServiceException;

  /**
   * 获取需发送认领成果邮件人员列表.
   * 
   * @param page
   * @param unitStr
   * @param simpleSearchContent TODO
   * @param unitId TODO
   * @param psnName TODO
   * @param email TODO
   * @return
   * @throws ServiceException
   */
  public Page<PubRolPsnStat> queryAssignMailList(Page<PubRolPsnStat> page, String unitStr, String simpleSearchContent,
      Long unitId, String psnName, String email) throws ServiceException;

  /**
   * 发送认领邮件.
   * 
   * @param domain 当前单位域名.
   * @param psnIds
   * @throws ServiceException
   */
  public void sendAssignMail(Map<String, String> confMap, List<Long> psnIds) throws ServiceException;

  /**
   * 根据insId统计部门人员
   * 
   * @param insId
   * @param cyFlag
   * @param unitId
   * @return
   * @throws ServiceException
   */
  public Map<String, Long> countPsnByUnitId(Long insId, Integer cyFlag, Long unitId) throws ServiceException;
}
