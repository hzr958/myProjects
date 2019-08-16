package com.smate.web.file.service.download;

import com.smate.web.file.form.FileDownloadForm;

/**
 * 文件下载服务
 * 
 * @author tsz
 *
 */
public interface FileDownloadService {
  /**
   * 构建下载资源
   * 
   * @param form
   * @throws Exception
   */
  public void buildDownloadRes(FileDownloadForm form) throws Exception;

  /**
   * 下载后的扩展方法
   * 
   * @param form
   * @throws Exception
   */
  public void downloadAfterExtend(FileDownloadForm form) throws Exception;
}
