package com.smate.core.base.psn.service.psnpub;

import com.smate.core.base.exception.ServiceException;
import com.smate.core.base.psn.model.PsnPubPO;

import java.util.Date;
import java.util.List;

/**
 * 个人成果关系service
 * 
 * @author yhx
 * @date 2018年6月1日
 */
public interface PsnPubService {

  public PsnPubPO getPsnPub(Long pub_id, Long ownerPsnId) throws ServiceException;

  void savePsnPub(PsnPubPO psnPub) throws ServiceException;

  /**
   * 获取psnId用户下拥有的所有成果关系对象
   * 
   * @param psnId
   * @return
   * @throws ServiceException
   */
  List<PsnPubPO> getPubsByPsnId(Long psnId) throws ServiceException;

  /**
   * 获取psnId用户下拥有的所有成果pubId
   * 
   * @param psnId
   * @return
   * @throws ServiceException
   */
  List<Long> getPubIdsByPsnId(Long psnId) throws ServiceException;

  /**
   * 更新个人与成果的关系为删除状态
   * 
   * @param psnId
   * @param pubId
   * @throws ServiceException
   */
  void updateStatusByPsnIdAndPubId(Long psnId, Long pubId) throws ServiceException;

  /**
   * 获取成果拥有者
   * 
   * @param pubId
   * @return
   * @throws ServiceException
   */
  Long getPubOwnerId(Long pubId) throws ServiceException;

  /**
   * 查找成果的状态
   * 
   * @param pubId
   * @return
   */
  public int findPsnPubStatus(Long pubId);

  /**
   * 获取人员成果的状态值
   * 
   * @param pubId
   * @return
   * @throws ServiceException
   */
  public Integer getStatus(Long pubId) throws ServiceException;

  /**
   * 更新成果时间
   * 
   * @param pubId
   * @param date
   * @return
   */
  public Integer updatePubDate(Long pubId, Date date);

}
