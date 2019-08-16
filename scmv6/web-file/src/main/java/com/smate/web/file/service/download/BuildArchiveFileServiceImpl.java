package com.smate.web.file.service.download;

import com.smate.core.base.utils.file.ArchiveFileUtil;
import com.smate.web.file.exception.FileNotExistException;
import com.smate.web.file.form.FileDownloadForm;

import javax.transaction.Transactional;

/**
 * ArchiveFile文件下载
 * 
 * @author ajb
 *
 */
@Transactional(rollbackOn = Exception.class)
public class BuildArchiveFileServiceImpl extends BuildResServiceBase {

  @Override
  public void check(FileDownloadForm form) throws Exception {
    // 判断fileid是否正确
    if (form.getFileId() == null) {
      form.setResult(false);
      form.setResultMsg("文件不存在！");
      throw new FileNotExistException();
    }
    super.checkArchiveFile(form.getFileId(), form);

  }

  @Override
  public void buildResUrl(FileDownloadForm form) throws Exception {
    // 构建文件真实地址
    // 构建下载资源
    String url = "/" + basicPath + ArchiveFileUtil.getFilePath(form.getArchiveFile().getFilePath());
    form.setResPath(url);
  }

  @Override
  public void extend(FileDownloadForm form) throws Exception {
    // TODO 处理个人文件下载业务
  }
}
