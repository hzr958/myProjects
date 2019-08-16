package com.smate.web.file.service.download;

import javax.transaction.Transactional;

import com.smate.web.file.form.FileDownloadForm;
import com.smate.web.file.utils.FileNamePathParseUtil;

/**
 * SIE成果列表全文下载
 * 
 * @author 叶星源
 */
@Transactional(rollbackOn = Exception.class)
public class BuildSieFulltextFileResServiceImpl extends BuildResServiceBase {

  @Override
  public void check(FileDownloadForm form) throws Exception {
    super.checkArchiveFile(form.getFileId(), form);
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
    // TODO Auto-generated method stub

  }

}
