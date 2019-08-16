package com.smate.center.batch.service.pub;

import java.io.Serializable;
import java.util.List;

import com.smate.center.batch.exception.pub.ServiceException;
import com.smate.center.batch.model.sns.pub.GroupRefs;
import com.smate.center.batch.model.sns.pub.PublicationEndNote;

/**
 * 群组中文献部分逻辑.
 * 
 * @author LY
 * 
 */
public interface GroupRefsService extends Serializable {

  /**
   * 根据groupId和pubId在查找群组中的成果.
   * 
   * @param groupId
   * @param pubId
   * @param nodeId
   * @return
   * @throws ServiceException
   */
  public GroupRefs findGroupRef(Long groupId, Long pubId, Integer nodeId) throws ServiceException;

  /**
   * 保存成果至群组成果.
   * 
   * @param groupPub
   * @throws ServiceException
   */
  public void saveGroupRef(GroupRefs groupRef) throws ServiceException;

  /**
   * 群组中删除群组文献.
   * 
   * @param groupId
   * @param pubId
   * @param psnId
   * @throws ServiceException
   */
  public void deleteGroupRef(Long groupId, Long pubId) throws ServiceException;

  /**
   * 文献.
   * 
   * @param pubIds
   * @return
   * @throws ServiceException
   */
  public List<PublicationEndNote> findGroupPubByIds(List<Long> pubIds) throws ServiceException;
}
