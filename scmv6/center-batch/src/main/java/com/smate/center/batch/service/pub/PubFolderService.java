package com.smate.center.batch.service.pub;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

import com.smate.center.batch.exception.pub.DaoException;
import com.smate.center.batch.form.pub.PubFolderForm;
import com.smate.center.batch.model.sns.pub.PubFolder;
import com.smate.center.batch.model.sns.pub.PubFolderItems;

/**
 * 成果/文献文件夹管理.
 * 
 * @author lqh
 * 
 */
public interface PubFolderService extends Serializable {

  /**
   * 获取单个文件夹.
   * 
   * @see com.iris.scm.scmweb.service.forder.FolderManagerImpl#getFolder()
   */
  PubFolder getPubFolder(Long folderId);

  /**
   * 保存新创建的文件夹.
   * 
   * @see com.iris.scm.scmweb.service.forder.FolderManagerImpl#saveFolder(com.iris.scm.scmweb.model.folder.Folder)
   */
  PubFolderForm savePubFolder(PubFolderForm pubFolder);

  /**
   * 删除文件夹.
   * 
   * @param PubFolderId
   * @throws DaoException
   * @see com.iris.scm.scmweb.service.PubFolderServiceImpl.PubFolderManagerImpl#deletePubFolderById(Long
   *      PubFolderId)
   */
  void deletePubFolderById(Long pubFolderId);

  /**
   * 根据用户ID,获取文件夹列表.
   * 
   * @param docIds
   * @return
   * @throws DaoException
   */
  List<PubFolder> findPubFolderByPsnIds(Integer articleType);

  @SuppressWarnings("rawtypes")
  Map findCurrPsnPubFolders(Integer articleType);

  /**
   * 保存编辑文件夹数据.
   * 
   * @param fdId
   * @param name
   * @param description
   * @return @
   */
  PubFolder saveEditPubFolder(PubFolderForm pubFolderForm);

  /**
   * 成果是否在文件夹ID中.
   * 
   * @param folderId
   * @param pubId
   * @return @
   */
  public Boolean isPublicationInPubFolder(Long folderId, Long pubId);

  /**
   * 从文件夹中删除成果.
   * 
   * @param folderId
   * @param pubId @
   */
  public void removePublicationFromPubFolder(Long folderId, Long pubId);

  /**
   * 从文件夹中删除成果.
   * 
   * @param articleType TODO
   * @param folderId
   * @param pubId
   * 
   * @
   */
  public void removePublicationFromPubFolder(String pubIds, Integer articleType);

  /**
   * 从文件夹中读取所有的成果.
   * 
   * @param folderId
   * @return @
   */
  public List<PubFolderItems> getPubFolderItemsByFolderId(Long folderId);

  /**
   * 编辑标签检查.
   * 
   * @param psnId
   * @param pubIds
   * @param articleType
   * @return @
   */
  @SuppressWarnings("rawtypes")
  List<Map<String, Object>> changePubFolders(Object[] pubIds, int articleType);

}
