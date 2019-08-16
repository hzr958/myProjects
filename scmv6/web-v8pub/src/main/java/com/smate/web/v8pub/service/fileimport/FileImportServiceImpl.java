package com.smate.web.v8pub.service.fileimport;

import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.multipart.MultipartFile;

import com.smate.web.v8pub.service.fileimport.extract.FileExtractService;
import com.smate.web.v8pub.service.fileimport.extract.PubSaveDataForm;

/**
 * 
 * @author aijiangbin
 * @date 2018年7月30日
 */
public class FileImportServiceImpl implements FileImportService {
  private Logger logger = LoggerFactory.getLogger(getClass());

  private Map<String, FileExtractService> fileExtractService;

  @Override
  public PubSaveDataForm initFile(MultipartFile file, String dbType) throws Exception {
    if (file == null || StringUtils.isBlank(dbType) || fileExtractService.get(dbType) == null) {
      return null;
    }
    PubSaveDataForm form = fileExtractService.get(dbType).extract(file);
    return form;
  }

  public Map<String, FileExtractService> getFileExtractService() {
    return fileExtractService;
  }

  public void setFileExtractService(Map<String, FileExtractService> fileExtractService) {
    this.fileExtractService = fileExtractService;
  }



}
