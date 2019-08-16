package com.smate.web.file.service.upload;

import org.apache.commons.lang3.StringUtils;

import com.smate.core.base.utils.file.ArchiveFileUtil;
import com.smate.web.file.model.FileInfo;
import com.smate.web.file.utils.FileNamePathParseUtil;

/**
 * 群组头像上传
 * 
 * @author tsz
 *
 */
public class GroupAvatarsBuildFileServiceImpl extends BuildFileServiceBase {

  @Override
  public Boolean checkFile(FileInfo fileInfo) throws Exception {
    if (StringUtils.isBlank(fileInfo.getFileUniqueKey())) {
      fileInfo.setResultMsg("唯一文件关健标识不能为空!");
      return false;
    }
    return true;
  }

  @Override
  public void writeFile(FileInfo fileInfo) throws Exception {

    try {
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
  public void saveFile(FileInfo fileInfo) throws Exception {

  }

}
