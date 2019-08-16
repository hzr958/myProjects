package com.smate.web.psn.service.file;

import java.util.List;

import com.smate.core.base.psn.model.StationFile;
import com.smate.core.base.utils.model.Page;
import com.smate.web.psn.form.FileMainForm;
import com.smate.web.psn.form.StationFileInfo;

/**
 * 我的文件服务接口
 * 
 * @author zk
 *
 */
public interface MyFileService {
  void getFileListForPsn(FileMainForm form) throws Exception;

  /**
   * 在群组模块获取我的文件夹
   * 
   * @param psnId
   * @param groupId
   * @return
   */
  List<StationFile> findFileForGroup(Long psnId, Long groupId, Integer pageSize, Integer pageNo);

  /**
   * 在群组文模块获取的文件列表_最新
   * 
   * @param psnId
   * @param groupId
   * @param pageSize
   * @param pageNo
   * @return
   * @return
   */
  List<StationFileInfo> findFileForGrp(Long psnId, Long groupId, Page<StationFile> page, String searchKey);

  /**
   * 
   * @param psnId
   * @param groupId
   * @param page
   * @param searchKey
   * @return
   * @throws Exception
   */
  List<StationFileInfo> findFileForPsn(Long psnId, Page<StationFile> page, String searchKey) throws Exception;

  /**
   * 收藏二个人文件
   * 
   * @param psnId
   * @param fileId
   * @throws Exception
   */
  void collectionStationFile(Long psnId, Long fileId) throws Exception;

  void getFileListCallBack(FileMainForm form) throws Exception;

  /**
   * 保存上传的文件
   * 
   * @param form
   * @return TODO
   * @throws Exception
   */
  Long saveMyUploadFile(FileMainForm form) throws Exception;

  public void saveFileDesc(FileMainForm form) throws Exception;

  public void delMyFile(FileMainForm form) throws Exception;

  /**
   * 文件分享记录列表
   * 
   * @param form
   * @throws Exception
   */
  void getShareRecordsList(FileMainForm form) throws Exception;

  void cancelFileShare(FileMainForm form) throws Exception;

  /*
   * 移动端获取文件列表
   */
  public void getFileList(FileMainForm form) throws Exception;

  public Page getPsnFileListInGroup(Page<StationFile> page) throws Exception;

  /**
   * 批量删除我的文件
   */
  void batchDelMyFile(FileMainForm form) throws Exception;

  /**
   * 当前文件是否存在并且状态正常
   */
  boolean checkCurrFileIsExist(Long psnId, Long fileId);

  public  void updateContent(FileMainForm form) ;
}
