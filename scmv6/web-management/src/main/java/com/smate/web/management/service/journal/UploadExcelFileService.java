package com.smate.web.management.service.journal;

import java.io.File;

public interface UploadExcelFileService {


  /**
   * 保存上传的excel文件
   * 
   * @param xlsFile
   * @param fileName
   */
  void saveExcelFile(File xlsFile, String fileName);

}
