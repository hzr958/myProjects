package com.smate.web.file.service.download;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;

import com.smate.web.file.dao.FilePubFulltextPsnRcmdDao;
import com.smate.web.file.exception.FileNotExistException;
import com.smate.web.file.form.FileDownloadForm;
import com.smate.web.file.utils.FileNamePathParseUtil;

/**
 * 
 * 全文认领的全文下载
 * 
 * @author lhd
 *
 */
@Transactional(rollbackOn = Exception.class)
public class BuildRcmdFulltextFileResServiceImpl extends BuildResServiceBase {

  @Autowired
  private FilePubFulltextPsnRcmdDao FilePubFulltextPsnRcmdDao;

  @Override
  public void check(FileDownloadForm form) throws Exception {
    Long count = FilePubFulltextPsnRcmdDao.queryRcmdPubFulltext(form);
    if (count.intValue() == 0) {
      form.setResult(false);
      form.setResultMsg("该文件没有推荐记录！fileId= " + form.getFileId());
      throw new FileNotExistException();
    }
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

  }

}
