package com.smate.center.batch.service.pub;

import java.io.Serializable;

import com.smate.center.batch.exception.pub.ServiceException;
import com.smate.center.batch.model.sns.pub.GroupPubs;

/**
 * 群组中成果部分逻辑.
 * 
 * @author LY
 * 
 */
public interface GroupPubsService extends Serializable {

  /**
   * 根据groupId和pubId在查找群组中的成果.
   * 
   * @param groupId
   * @param pubId
   * @param nodeId
   * @return
   * @throws ServiceException
   */
  public GroupPubs findGroupPub(Long groupId, Long pubId, Integer nodeId) throws ServiceException;

  /**
   * 保存成果至群组成果.
   * 
   * @param groupPub
   * @throws ServiceException
   */
  public void saveGroupPub(GroupPubs groupPub) throws ServiceException;

  /**
   * 群组中删除群组成果.
   * 
   * @param groupId
   * @param pubId
   * @param psnId
   * @throws ServiceException
   */
  public void deleteGroupPub(Long groupId, Long pubId) throws ServiceException;

  /**
   * 查找是否有全文在群组中.
   * 
   * @param pubId
   * @param psnId
   * @return
   * @throws ServiceException
   */
  public boolean isFullTextInGroup(Long pubId, Long psnId) throws ServiceException;

  /**
   * 查找成果在开放群组中的数量.
   * 
   * @param pubId
   * @return
   * @throws ServiceException
   */
  public Integer findPubCountInOpenGroup(Long pubId) throws ServiceException;

  /**
   * 查找成果在我加入的群组中的数量.
   * 
   * @param pubId
   * @param psnId
   * @return
   * @throws ServiceException
   */
  public Integer findPubCountInMyGroup(Long pubId, Long psnId) throws ServiceException;

}
