package com.smate.web.file.service.upload;

import com.smate.core.base.utils.file.ArchiveFileUtil;
import com.smate.core.base.utils.file.FileUtils;
import com.smate.core.base.utils.string.StringUtils;
import com.smate.web.file.model.FileInfo;
import com.smate.web.file.utils.FileNamePathParseUtil;
import org.apache.commons.lang3.RandomStringUtils;

import java.io.File;

/**
 * 新闻消息相关文件
 */
public class NewsFileBuildFileServiceImpl extends BuildFileServiceBase {

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
    if(StringUtils.isBlank(fileInfo.getFileSuffix())){
      writeImageFile(fileInfo);
      return ;
    }
    String[] fileNamePath = FileNamePathParseUtil.generalUniqueDirWithFileName(fileInfo.getFileSuffix());
    String fileAllPath = fileInfo.getRootPath() + super.getBasicPath() + fileNamePath[0] + fileNamePath[1];
    fileInfo.setFilePath(super.getBasicPath() + fileNamePath[0] + fileNamePath[1]);
    fileInfo.setFilePathName(fileNamePath[1]);
    FileUtils.copyFile(fileInfo.getFile(), new File(fileAllPath));
  }

  public void writeImageFile(FileInfo fileInfo) throws Exception {
    try {
      fileInfo.setFileUniqueKey( RandomStringUtils.randomNumeric(8));
      String[] fileNamePath =
          FileNamePathParseUtil.generalDirWithFileName(fileInfo.getFileUniqueKey() + "." + super.getImgType());
      String fileAllPath = fileInfo.getRootPath() + "/" + super.getBasicPath() + fileNamePath[0] + fileNamePath[1];
      fileInfo.setFilePath(super.getBasicPath() + fileNamePath[0] + fileNamePath[1]);
      fileInfo.setFilePathName(fileNamePath[1]);
      fileInfo.setFileName(fileInfo.getFileUniqueKey() + "." + super.getImgType());
      // 生成png图片
      ArchiveFileUtil.writeBase64Str2File(fileAllPath, fileInfo.getImgData());

    } catch (Exception e) {
      logger.error("图片上传出错 ", e);
      throw new Exception(e);
    }

  }
  @Override
  public void saveFile(FileInfo fileInfo) throws Exception {}
}
