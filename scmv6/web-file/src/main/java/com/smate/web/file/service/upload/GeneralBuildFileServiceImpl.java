package com.smate.web.file.service.upload;

import java.io.File;

import com.smate.core.base.utils.file.FileUtils;
import com.smate.web.file.model.FileInfo;
import com.smate.web.file.utils.FileNamePathParseUtil;

/**
 * 普通 通用构建文件实现 (只是通用类型 请谨慎修改 不扩展) 如有特殊需求 请另外 继承 父类做实现
 * 
 * @author tsz
 *
 */
public class GeneralBuildFileServiceImpl extends BuildFileServiceBase {

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
