package com.smate.center.task.dao.sns.quartz;

import java.util.List;

import com.smate.center.task.exception.DaoException;
import com.smate.center.task.model.sns.quartz.PubFolderItems;

/**
 * 成果/文献数据库访问接口.
 * 
 * @author LY
 * 
 */
public interface PubFolderItemsDao {
  /**
   * 成果添加至成果文件夹.
   * 
   * @param pubId
   * @param folderId
   * @throws DaoException
   */
  public Long addPublicationToPubFolder(PubFolderItems pubFolderItems);

  /**
   * 从成果/文献--文件夹关系中删除成果.
   * 
   * @param pubId
   * @param folderId
   * @throws DaoException
   */
  public void removePublicationFromPubFolder(PubFolderItems pubFolderItems);

  /**
   * 按文件夹ID和成果ID统计，该成果在该文件夹中的个数.
   * <p/>
   * 最多一个
   * 
   * @param folderId
   * @param pubId
   * @return
   * @throws DaoException
   */
  public Long getStatByPubId(Long folderId, Long pubId);

  /**
   * 按文件ID统计文件夹中的个数.
   * 
   * @param folderId
   * @return
   * @throws DaoException
   */
  public Long getStatByFolderId(Long folderId);

  /**
   * 根据文件夹ID查询成果列表.
   * 
   * @param articleType 1：成果；2：文献
   * @return
   */
  public List<PubFolderItems> getPubFolderItemsById(Long folderId);

  /**
   * 根据文件夹ID和成果ID查询成果-文件夹关系.
   * 
   * @param folderId
   * @param pubId
   * @return
   * @throws DaoException
   */
  public PubFolderItems getPubFolderItems(Long folderId, Long pubId);

  /**
   * 删除文件夹ID中的所有成果.
   * 
   * @param foderId
   * @throws DaoException
   */
  public void removePublicationByFolderId(Long folderId);

  /**
   * 从文件夹中同时删除多个成果.
   * 
   * @param pubIds
   */
  public void removePublicationFromPubFolder(String pubIds);

  /**
   * 从文件夹中同时删除多个成果.
   * 
   * @param pubIds
   */
  public void removePublicationFromPubFolder(List<Long> pubIds);

  /**
   * 编辑标签检查.
   * 
   * @param psnId
   * @param pubIds
   * @param articleType
   * @return
   * @throws DaoException
   */
  public List<Object[]> changePubFolders(Long psnId, Object[] pubIds, int articleType);

  /**
   * @param pubIds
   * @param folderIds
   * @throws DaoException
   */
  public void deleteFdFlByUnchangingIds(Object[] pubIds, Object[] folderIds);

  public void removePubFolderItemsById(Long id);

  /**
   * 添加成果到标签下
   * 
   * @param pubFolderItems
   * @throws DaoException
   */
  public void savePubFolderItems(PubFolderItems pubFolderItems);
}
