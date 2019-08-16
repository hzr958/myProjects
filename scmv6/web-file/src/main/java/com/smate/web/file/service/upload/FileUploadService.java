package com.smate.web.file.service.upload;

import com.smate.web.file.model.FileInfo;

/**
 * 文件上传服务类
 * 
 * @author tsz
 *
 */
public interface FileUploadService {

  /**
   * 保存文件
   * 
   * @return
   * @throws Exception
   */
  public Boolean saveFile(FileInfo fileInfo) throws Exception;

  /**
   * 构建并保存上传文件资源
   *
   * @author houchuanjie
   * @date 2018年3月9日 下午5:05:10
   * @param form
   * @throws Exception
   */
  /* public void buildUploadRes(FileUploadForm form) throws Exception; */
}
