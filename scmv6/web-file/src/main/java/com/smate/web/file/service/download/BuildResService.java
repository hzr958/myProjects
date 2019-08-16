package com.smate.web.file.service.download;

import com.smate.web.file.form.FileDownloadForm;

/**
 * 检查下载资源接口 主要检查当前登录人 与下载资源之间的关系
 * 
 * @author tsz
 *
 */
public interface BuildResService {
  /**
   * 检查权限
   * 
   * @param form
   * @return
   * @throws Exception
   */
  public void build(FileDownloadForm form) throws Exception;

  /**
   * 文件下载业务扩展 (处理文件下载 具体业务处理 比如记录全文下载记录 群组文件下载记录......)
   * 
   * 可以空实现
   * 
   * @param form
   * @return
   */
  public abstract void extend(FileDownloadForm form) throws Exception;
}
