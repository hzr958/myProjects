package com.smate.center.task.service.pdwh.quartz;

import java.io.File;
import java.io.IOException;

import org.apache.commons.io.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.smate.center.task.exception.ServiceException;
import com.smate.center.task.single.service.pub.DbCacheBfetchService;
import com.smate.center.task.single.util.pub.ImplXmlFileNameFilter;

@Service("dbCacheBfetchFileService")
public class DbCacheBfetchFileServiceImpl implements DbCacheBfetchFileService {
  protected final Logger logger = LoggerFactory.getLogger(getClass());
  @Autowired
  private DbCacheBfetchService dbCacheBfetchService;
  private String finishPrefix = "finish";
  private String skipDir = "tmp_tohandle";
  private Integer xmlType;// 导入的xml类型 ，目前只有rainpat的xml和其他导入不同需要单独处理

  @Override
  public void readerFile(String dir, Integer xmlType) throws ServiceException {
    this.xmlType = xmlType;
    File file = new File(dir);
    if (!file.exists()) {
      file.mkdirs();
      logger.error("目录:" + dir + " 不存在，将自动创建");
    }
    this.readerFile(file);
  }

  /** 读取文件写入数据库. */
  private void readerFile(File file) throws ServiceException {
    if (!file.isDirectory()) {
      String fileName = file.getName().toLowerCase();
      //
      // 108_2017_32768_85_2.xml
      // 5544_2006_32768_4425_8.xml
      // 5544 表示：香港中文大学ID
      // 2006 表示：抓取数据的年份
      // 32768表示：抓取的数据库为 SCI，65536表示SSCI,131072表示ISTP
      // 4425表示 抓取香港中文大学2006的数据总数为4425条
      // 1表示页数。当前这个文件为第一页数据。

      switch (xmlType) {
        case 0:
          if (!fileName.matches("^[0-9]+_[0-9]+_[0-9]+_[0-9]+_[0-9]+\\.xml$")) {
            return;
          }
          break;
        case 1:
          // rainapt专利
          if (!fileName.matches("^[0-9]{4}-[0-9]{1,}\\.xml$")) {
            return;
          }
          break;

        default:
          if (!fileName.matches("^[0-9]+_[0-9]+_[0-9]+_[0-9]+_[0-9]+\\.xml$")) {
            return;
          }
          break;
      }
      Integer pubYear;
      Long insId = 0L;
      if (xmlType.equals(1)) {// raintpat
        pubYear = Integer.valueOf(fileName.substring(0, 4));
      } else {
        int firstUlineIdx = fileName.indexOf("_");
        if (firstUlineIdx <= 0) {
          return;
        }
        insId = Long.valueOf(fileName.substring(0, firstUlineIdx));
        pubYear = Integer.valueOf(fileName.substring(firstUlineIdx + 1, firstUlineIdx + 5));
      }
      String xml = null;
      try {
        xml = FileUtils.readFileToString(file, "utf-8");
      } catch (IOException e) {
        logger.error("读取文件内容失败：" + fileName);
        throw new ServiceException("读取文件内容失败：" + fileName, e);
      }
      if (xml != null) {
        dbCacheBfetchService.saveXml(insId, pubYear, fileName, xml);// 专利insid=0
      }
      File newFile = new File(file.getPath().replace(fileName, "") + finishPrefix + "_" + fileName);
      try {
        FileUtils.moveFile(file, newFile);
      } catch (IOException e) {
        logger.error("文件重命名失败：" + fileName);
        throw new ServiceException("文件重命名失败：" + fileName, e);
      }
    } else {
      // 递归读取文件
      String[] list = file.list(new ImplXmlFileNameFilter(xmlType));
      String path = file.getPath();
      File tmpfile = null;
      for (String string : list) {
        if (string.contains(skipDir)) {
          continue;
        }
        tmpfile = new File(path + "//" + string);
        readerFile(tmpfile);
      }

    }
  }
}
