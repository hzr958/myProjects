package com.smate.web.file.service.download;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;

import com.smate.web.file.dao.GrpFileDao;
import com.smate.web.file.exception.FileNotExistException;
import com.smate.web.file.form.FileDownloadForm;
import com.smate.web.file.model.V_GrpFile;
import com.smate.web.file.utils.FileNamePathParseUtil;

/**
 * 群组文件下载
 * 
 * @author tsz
 *
 */
@Transactional(rollbackOn = Exception.class)
public class BuildGroupFileResServiceImpl extends BuildResServiceBase {

  @Autowired
  private GrpFileDao grpFileDao;

  @Override
  public void check(FileDownloadForm form) throws Exception {
    V_GrpFile grpFile = grpFileDao.get(form.getFileId());
    if (grpFile == null || grpFile.getFileStatus() != 0) {
      form.setResult(false);
      form.setResultMsg("文件不存在！");
      throw new FileNotExistException();
    }
    super.checkArchiveFile(grpFile.getArchiveFileId(), form);
    form.setOwnerPsnId(grpFile.getUploadPsnId());
  }

  @Override
  public void buildResUrl(FileDownloadForm form) throws Exception {
    // 构建文件真实地址
    // 构建下载资源
    String url = "/" + basicPath + FileNamePathParseUtil.parseFileNameDir(form.getArchiveFile().getFilePath());
    form.setResPath(url);
  }

  @Override
  public void extend(FileDownloadForm form) throws Exception {
    // TODO 处理群组文件下载业务

  }
}
