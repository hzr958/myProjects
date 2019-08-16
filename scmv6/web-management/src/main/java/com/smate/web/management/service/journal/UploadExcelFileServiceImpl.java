package com.smate.web.management.service.journal;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

/**
 * 上传excel文件sevice
 * 
 * @author hht
 * @Time 2019年4月19日 下午4:56:53
 */
@Service
public class UploadExcelFileServiceImpl implements UploadExcelFileService {
  private Logger logger = LoggerFactory.getLogger(getClass());

  @Value("${managefile.root}")
  private String managefileRoot;

  /**
   * 保存excel文件
   */
  @Override
  public void saveExcelFile(File xlsFile, String fileName) {
    try {
      String date = new SimpleDateFormat("yyyyMMddHHmm").format(new Date());
      String fileExt = fileName.substring(fileName.indexOf("."), fileName.length());
      fileName = fileName.substring(0, fileName.lastIndexOf("."));
      String filePath = managefileRoot + "/jnlbatchimp/" + fileName + date + fileExt;
      File file = new File(filePath);
      // 目录是否存在
      File parentFile = file.getParentFile();
      if (!parentFile.exists()) {
        parentFile.mkdirs();
      }
      logger.info("##写文件路径:" + filePath);
      FileOutputStream fos = new FileOutputStream(filePath);
      FileInputStream fis = new FileInputStream(xlsFile);
      byte[] buffer = new byte[1024];
      int len = 0;
      while ((len = fis.read(buffer)) > 0) {
        fos.write(buffer, 0, len);
      }
      fos.close();
      fis.close();
    } catch (Exception e) {
      logger.error("#######保存excel文件失败", e);
    }
  }


}
