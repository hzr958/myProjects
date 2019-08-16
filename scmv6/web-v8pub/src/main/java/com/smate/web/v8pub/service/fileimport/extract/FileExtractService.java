package com.smate.web.v8pub.service.fileimport.extract;

import org.springframework.web.multipart.MultipartFile;

/**
 * 文件提取服务类
 * 
 * @author aijiangbin
 * @date 2018年7月30日
 */
public interface FileExtractService {

  /**
   * 提取源文件
   * 
   * @param sourceFile
   */
  PubSaveDataForm extract(MultipartFile sourceFile) throws Exception;

}
