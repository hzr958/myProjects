package com.smate.web.v8pub.service.fileimport;

import org.springframework.web.multipart.MultipartFile;

import com.smate.web.v8pub.service.fileimport.extract.PubSaveDataForm;

/**
 * 文件导入服务了
 * 
 * @author aijiangbin
 * @date 2018年7月30日
 */
public interface FileImportService {

  /**
   * 初始化，解析文件
   * 
   * @param file
   * @param dbType
   * @return
   */
  public PubSaveDataForm initFile(MultipartFile file, String dbType) throws Exception;

}
