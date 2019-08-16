package com.smate.center.batch.service.pub;

import java.util.List;

import com.smate.center.batch.exception.pub.ServiceException;
import com.smate.center.batch.model.sns.pub.GroupPsnNodeForm;
import com.smate.center.batch.model.sns.pub.GroupPubs;
import com.smate.center.batch.model.sns.pub.Publication;
import com.smate.center.batch.service.pub.mq.GroupPubRemoveSyncMessage;

/**
 * 我的群组和成果文献的关系.
 * 
 * @author LY
 * 
 */
public interface GroupPublicationService {

  /**
   * 我创建和我参与的群组.
   * 
   * @param articleType TODO
   * @param isCurrResNum TODO
   * 
   * @return @
   */
  public List<GroupPsnNodeForm> getGroupInvitePsnNodes(Integer articleType, boolean isCurrResNum);

  /**
   * 加入成果至群主.
   * 
   * @param des3PubIds
   * @param groupIds
   * @param long1
   * @throws ServiceException
   */
  public void addPublicationToGroup(String des3PubIds, String groupIds, Integer articleType, Long groupFolderIds,
      int dynFlag) throws ServiceException;

  /**
   * 判断成果是否已经加入群组.
   * 
   * @param pubId
   * @param groupId
   * @param articleType
   * @return
   * @throws ServiceException
   */
  public boolean isAddGroupByPubId(Long pubId, Long groupId, Integer articleType) throws ServiceException;

  /**
   * 更新群组中的成果.
   * 
   * @param receiveMessage
   * @throws ServiceException
   */
  void groupPublicationUpdate(Publication pub, Integer nodeId, Long groupId) throws ServiceException;

  /**
   * 更新群组中的文献.
   * 
   * @param receiveMessage
   * @throws ServiceException
   */
  void groupReferenceUpdate(Publication pub, Integer nodeId, Long groupId) throws ServiceException;

  /**
   * mypub在我的成果中移除群组,mygroup在我的群组中移除成果.
   * <p/>
   * JMS -接收端从 成果文献移除群组时，同步到群组的创建节点---接收端..
   * 
   * @param receiveMessage
   */
  public void receivePublicationRemoveGroupSync(GroupPubRemoveSyncMessage receiveMessage) throws ServiceException;

  /**
   * 从群组中移除成果.
   * 
   * @param groupId
   * @param psnId
   * @param pubId
   * @throws ServiceException
   */
  public void deletePublicationFromGroup(Long groupId, Long psnId, Long pubId) throws ServiceException;

  /**
   * 从群组中移除文献.
   * 
   * @param groupId
   * @param psnId
   * @param pubId
   * @throws ServiceException
   */
  public void deleteReferenceFromGroup(Long groupId, Long psnId, Long pubId) throws ServiceException;

  public List<GroupPubs> getGroupPubsList(Long groupId);

  public List<String> getGroupKw(Long groupId);

  public Integer getShareKwCount(String pubKw, List<String> groupKw);

  public void saveGroupPub(GroupPubs groupPubs);

  public Integer getPubGroupShareKwCount(GroupPubs groupPub) throws Exception;

  public Integer groupPubIsLabeled(GroupPubs groupPub) throws Exception;

  public void updateGroupPubsInfo();

  public void addPublicationToGroup(Long psnId, String des3PubIds, String groupIds, Integer articleType, Long folderId,
      int dynFlag) throws ServiceException;
}
