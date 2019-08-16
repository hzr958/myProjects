package com.smate.web.v8pub.service.poi;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.Collection;
import java.util.UUID;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.smate.core.base.exception.ServiceException;


/**
 * 导出excel.
 * 
 * @author chenxiangrong
 * 
 * @param <T>
 */
@Service("poiService")
@Transactional(rollbackFor = Exception.class)
public class PoiServiceImpl<T> implements PoiService<T> {
  protected final Logger logger = LoggerFactory.getLogger(getClass());
  private String fileRoot;

  public HSSFWorkbook exportExcel(String title, Collection<T> dataset, int flag, String pattern) throws Exception {
    return new POIHandler<T>().exportExcel(title, dataset, flag, pattern);

  }

  @Override
  public HSSFWorkbook exportExcelTable(String title, Collection<T> dataset, String pattern) throws Exception {
    return new POIHandler<T>().exportExcelTable(title, dataset, pattern);
  }

  @Override
  public HSSFWorkbook exportExcelByTemp(String fileTempPath, Collection<T> dataset, String pattern) throws Exception {
    return new POIHandler<T>().exportExcelByTemp(dataset, pattern, fileTempPath);
  }

  /**
   * 读取html内容.
   * 
   * @param urlStr
   * @return
   * @throws ServiceException
   */
  public String getHtmlContent(String urlStr) throws ServiceException {
    logger.debug("进入html获取！");
    try {
      URL url = new URL(urlStr);
      BufferedReader br = new BufferedReader(new InputStreamReader(url.openStream(), "UTF-8"));
      String s = "";
      StringBuffer sb = new StringBuffer("");
      while ((s = br.readLine()) != null) {
        sb.append(s + "\r\n");
      }
      br.close();
      return sb.toString();
    } catch (Exception e) {
      logger.error("获取html内容时出错了！");
      throw new ServiceException(e);
    }
  }

  /**
   * 生成文件名.
   * 
   * @return
   */
  public String getFileName() {
    String fileStr = UUID.randomUUID().toString();
    String[] fileArray = fileStr.split("-");
    String fileName = "";
    for (int i = 0; i < fileArray.length; i++)
      fileName = fileName + fileArray[i];
    return fileName;
  }

  public String getFileRoot() {
    return fileRoot;
  }

  public void setFileRoot(String fileRoot) {
    this.fileRoot = fileRoot;
  }
}
