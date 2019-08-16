package com.smate.web.file.service.upload;

import com.smate.web.file.form.FileUploadForm;
import com.smate.web.file.model.FileInfo;

/**
 * 构建文件 接口
 * 
 * @author tsz
 *
 */
public interface BuildFileService {

  /**
   * 构建文件类
   * 
   * @return
   * @throws Exception
   */
  public Boolean buildFile(FileInfo fileInfo) throws Exception;

  /**
   * 构建文件上传资源
   * 
   * @author houchuanjie
   * @date 2018年3月9日 下午6:31:45
   * @param form
   */
  public void build(FileUploadForm form);

}
