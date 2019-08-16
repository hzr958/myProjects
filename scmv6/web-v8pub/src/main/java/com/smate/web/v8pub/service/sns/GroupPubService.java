package com.smate.web.v8pub.service.sns;

import com.smate.web.v8pub.exception.ServiceException;
import com.smate.web.v8pub.po.sns.group.GroupPubPO;
import com.smate.web.v8pub.service.BaseService;

public interface GroupPubService extends BaseService<Long, GroupPubPO> {

  /**
   * 更新群组成果与个人库成果关系表中的更新时间
   * 
   * @param pubId
   */
  void updateGrpPubsGmtModified(Long pubId) throws ServiceException;

  /**
   * 更新群组与个人库成果关系表中的状态值
   * 
   * @param grpId
   * @param pubId
   * @throws ServiceException
   */
  void updateStatusByGrpIdAndPubId(Long grpId, Long pubId, Integer status) throws ServiceException;

  /**
   * 群组是否存在当前成果
   * 
   * @return
   * @throws ServiceException
   */
  public boolean existGrpPub(Long grpId, Long pubId) throws ServiceException;;

  /**
   * 根据grpId和pubId获取关系对象
   * 
   * @param grpId
   * @param pubId
   * @return
   * @throws ServiceException
   */
  GroupPubPO getByGrpIdAndPubId(Long grpId, Long pubId) throws ServiceException;
  /**
   * 查询群组成果，不要添加状态
   * 查询群组成果，不要添加状态
   * 查询群组成果，不要添加状态
   * 查询群组成果，不要添加状态
   *
   * @param grpId
   * @param pubId
   * @return
   * @throws ServiceException
   */
  GroupPubPO getGrpPub(Long grpId, Long pubId) throws ServiceException;

  /**
   * 查找成果的状态
   * 
   * @return
   */
  public int findGrpPubStatus(Long grpId, Long pubId) throws ServiceException;

  /**
   * 获取群组中成果拥有者的psnId
   * 
   * @param grpId
   * @param pubId
   * @return
   */
  Long getPubOwnerPsnId(Long grpId, Long pubId) throws ServiceException;

  /**
   * 获取群组成果通过pubId
   * 
   * @param pubId
   * @return
   * @throws ServiceException
   */
  GroupPubPO getByPubId(Long pubId) throws ServiceException;

  /**
   * 通过PubId获取此条成果当前的与群组的关系
   * 
   * @param pubId
   * @return
   */
  Integer getStatus(Long pubId);
}
