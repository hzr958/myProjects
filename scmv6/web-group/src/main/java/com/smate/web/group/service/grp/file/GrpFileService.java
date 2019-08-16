package com.smate.web.group.service.grp.file;

import com.smate.web.group.action.grp.form.GrpFileForm;
import com.smate.web.group.model.grp.file.GrpFile;

/**
 * 群组文件service
 * 
 * @author AiJiangBin
 *
 */
public interface GrpFileService {

  /**
   * 群组文件列表
   * 
   * @param grpFileForm
   * @return
   */
  public void findGrpFileList(GrpFileForm grpFileForm) throws Exception;

  /**
   * 
   * 检查文件的编辑删除权限
   * 
   * @param psnId
   * @param grpId
   * @param grpFileId
   * @return
   */
  public Boolean checkFilePermit(GrpFileForm grpFileForm) throws Exception;

  /**
   * 判断群组文件是否属于我
   * 
   * @param grpId
   * @param psnId
   * @param grpFileId
   * @return
   */
  public Boolean checkIsMyGrpFile(Long grpId, Long psnId, Long grpFileId) throws Exception;

  /**
   * 把我的文件 ，添加到群组文件中
   * 
   * @param grpFileForm
   */
  public void addMyFileForGrp(GrpFileForm grpFileForm) throws Exception;

  /**
   * 检查群组文件文件是否存在
   * 
   * @param grpFileId
   * @return
   */
  public Boolean checkExitGrpFile(Long grpFileId) throws Exception;

  /**
   * 编辑群组文件
   * 
   * @param grpFileForm
   */
  public void editGrpFile(GrpFileForm grpFileForm) throws Exception;

  /**
   * 删除群组文件
   * 
   * @param grpFileForm
   */
  public void deleteGrpFile(GrpFileForm grpFileForm) throws Exception;

  /**
   * 收藏群组文件
   * 
   * @param grpFileForm
   */
  public void collectGrpFile(GrpFileForm grpFileForm) throws Exception;

  /**
   * 标记群组文件的类型：课件或作业
   * 
   * @param grpFileForm
   * @return
   */
  public int flagGrpFileType(GrpFileForm grpFileForm) throws Exception;

  /**
   * 群组上传文件的成员
   * 
   * @param grpFileForm
   */
  public void findGrpFileMember(GrpFileForm grpFileForm) throws Exception;

  /**
   * 保存上传的群组文件
   * 
   * @param grpFileForm
   */
  public void saveUploadGrpFile(GrpFileForm grpFileForm) throws Exception;

  /**
   * 查询群组文件
   * 
   * @param grpFileForm
   */
  public GrpFile findGrpFile(GrpFileForm grpFileForm) throws Exception;

  /**
   * 分享群组文件
   * 
   * @param grpFileForm
   */
  public GrpFile shareGrpFiles(GrpFileForm grpFileForm) throws Exception;

  /**
   * 分享群组文件
   * 
   * @param grpFileForm
   */
  public GrpFile shareGrpFilesByEmail(GrpFileForm grpFileForm) throws Exception;

}
