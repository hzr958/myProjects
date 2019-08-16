package com.smate.center.batch.service.pub;

import com.smate.center.batch.exception.pub.ServiceException;
import com.smate.center.batch.model.sns.pub.PubXmlProcessContext;
import com.smate.center.batch.model.sns.pub.Publication;

/**
 * 成果与群组的关系模块.
 * 
 * @author LY
 * 
 */
public interface PublicationGroupService {

  /**
   * 为群组检索/录入成果.
   * 
   * @param articleType
   * @param groupFolderId
   * @param dynFlag
   * @param string
   * @param string2
   */
  public void addPublicationToGroup(String pubId, String groupId, Integer articleType, Long groupFolderId, int dynFlag)
      throws ServiceException;

  /**
   * 成果被删除需同步到群组.
   * 
   * @param pubId
   * @param ownerPsnId
   * @throws ServiceException
   */
  public void delPublication(Long pubId, Long ownerPsnId) throws ServiceException;

  /**
   * 成果被编辑需同步到群组.
   * 
   * @param doc
   * @param pub
   * @param context
   * @throws ServiceException
   */
  public void savePublicationEdit(Publication pub, PubXmlProcessContext context) throws ServiceException;

  public void addPublicationToGroup(Long psnId, String pubId, String groupId, Integer articleType, Long groupFolderId,
      int dynFlag) throws ServiceException;


}
