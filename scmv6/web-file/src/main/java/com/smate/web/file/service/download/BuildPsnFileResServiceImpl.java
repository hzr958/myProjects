package com.smate.web.file.service.download;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;

import com.smate.core.base.psn.dao.PsnFileDao;
import com.smate.core.base.psn.model.PsnFile;
import com.smate.core.base.utils.file.ArchiveFileUtil;
import com.smate.core.base.utils.security.SecurityUtils;
import com.smate.web.file.dao.PsnFileShareRecordQueryDao;
import com.smate.web.file.exception.FileDownloadNoPermissionException;
import com.smate.web.file.exception.FileNotExistException;
import com.smate.web.file.form.FileDownloadForm;

/**
 * 个人文件下载
 * 
 * @author tsz
 *
 */
@Transactional(rollbackOn = Exception.class)
public class BuildPsnFileResServiceImpl extends BuildResServiceBase {

  @Autowired
  private PsnFileDao psnFileDao;

  @Autowired
  private PsnFileShareRecordQueryDao shareRecordQueryDao;

  @Override
  public void check(FileDownloadForm form) throws Exception {

    // 判断fileid是否正确
    PsnFile psnFile = psnFileDao.get(form.getFileId());
    if (psnFile == null || psnFile.getStatus() != 0) {
      form.setResult(false);
      form.setResultMsg("文件不存在！");
      throw new FileNotExistException();
    }
    // 不是短地址
    if (!form.isShortUrl()) {
      Integer permission = psnFile.getPermission();
      switch (permission) {
        case 0: // 所有人下载
          break;
        default: { // 隐私
          // 判断是不是自己的文件
          Long ownerPsnId = psnFile.getOwnerPsnId();
          Long currentUserId = SecurityUtils.getCurrentUserId();
          boolean flag = currentUserId != 0 && ownerPsnId.equals(currentUserId);
          if (flag)
            break;
          // 如果不是自己的文件，则判断是否被分享过
          flag = shareRecordQueryDao.isExistIn(currentUserId, psnFile.getId());
          // 没有权限
          if (!flag) {
            form.setResult(false);
            form.setResultMsg("没有下载该文件的权限！");
            throw new FileDownloadNoPermissionException();
          }
        }
      }

    }
    form.setOwnerPsnId(psnFile.getOwnerPsnId());
    super.checkArchiveFile(psnFile.getArchiveFileId(), form);

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
