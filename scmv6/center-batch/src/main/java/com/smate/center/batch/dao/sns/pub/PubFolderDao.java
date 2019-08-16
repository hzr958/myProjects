package com.smate.center.batch.dao.sns.pub;

import java.util.List;

import com.smate.center.batch.model.sns.pub.PubFolder;

/**
 * 成果/文献文件夹数据库访问接口.
 * 
 * @author LY
 * 
 */
public interface PubFolderDao {
  /**
   * 根据人员ID查询文件夹.
   * 
   * @param articleType 1：成果；2：文献
   * @return
   */
  public List<PubFolder> getPubFolderByPsnId(Long psnId, Integer articleType);

  /**
   * 新增文件夹.
   * 
   * @param pubFolder @
   */
  public Long addPubFolder(PubFolder pubFolder);

  /**
   * 编辑文件夹.
   * 
   * @param pubFolder @
   */
  public Long updatePubFolder(PubFolder pubFolder);

  /**
   * 删除文件夹.
   * 
   * @param id @
   */
  public void removePubFolder(Long id);

  /**
   * 删除文件夹.
   * 
   * @param folder @
   */
  public void removePubFolder(PubFolder folder);

  /**
   * 根据文件夹ID查询文件夹.
   * 
   * @param id
   * @return @
   */
  public PubFolder getPubFolderById(Long id);

  public PubFolder getFolderByName(String folderName, Long psnId, Integer articleType);

  /**
   * 获取指定用户的成果文件夹.
   * 
   * @param psnId
   * @return @
   */
  public List<PubFolder> queryPubFolderByPsnId(Long psnId);
}
