package com.smate.web.file.service.upload;

import com.smate.core.base.utils.file.FileUtils;
import com.smate.web.file.model.FileInfo;
import com.smate.web.file.utils.FileNamePathParseUtil;

import java.io.File;

/**
 * 成果文件，保存导入成果的原始文件
 */
public class PubSrcFileBuildFileServiceImpl extends BuildFileServiceBase {

  /**
   * 检查文件是否满足要求 （通用的 如果有特殊要求 请自己实现）
   * 
   * @throws Exception
   */
  @Override
  public Boolean checkFile(FileInfo fileInfo) throws Exception {
    // 如果没有特别的检查 直接返回true
    return true;
  }

  @Override
  public void writeFile(FileInfo fileInfo) throws Exception {
    String[] fileNamePath = FileNamePathParseUtil.generalUniqueDirWithFileName(fileInfo.getFileSuffix());
    String fileAllPath = fileInfo.getRootPath() + super.getBasicPath() + fileNamePath[0] + fileNamePath[1];
    fileInfo.setFilePath(super.getBasicPath() + fileNamePath[0] + fileNamePath[1]);
    fileInfo.setFilePathName(fileNamePath[1]);
    FileUtils.copyFile(fileInfo.getFile(), new File(fileAllPath));
  }

  @Override
  public void saveFile(FileInfo fileInfo) throws Exception {}
}
