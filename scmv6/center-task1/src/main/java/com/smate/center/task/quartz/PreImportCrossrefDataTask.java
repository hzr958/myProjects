package com.smate.center.task.quartz;

import java.io.File;
import java.io.IOException;
import java.util.List;

import org.apache.commons.io.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;

import com.smate.center.task.base.TaskAbstract;
import com.smate.center.task.exception.ServiceException;
import com.smate.center.task.model.common.CrossrefYearCount;
import com.smate.center.task.service.publicpub.CrossRefDataService;
import com.smate.core.base.utils.file.EncodingDetect;

/**
 * crossref数据预处理
 * 
 * @author Administrator
 *
 */
public class PreImportCrossrefDataTask extends TaskAbstract {
  public PreImportCrossrefDataTask() {
    super();
  }

  public PreImportCrossrefDataTask(String beanName) {
    super(beanName);
  }

  private final Logger logger = LoggerFactory.getLogger(getClass());
  @Value("${crossref.file.root}")
  private String dir;
  private String finishPrefix = "finish";
  @Autowired
  private CrossRefDataService crossRefDataService;

  public void doRun() throws Exception {
    if (!super.isAllowExecution()) {
      return;
    }
    try {
      List<CrossrefYearCount> yearCountList = crossRefDataService.getImportCrossrefData();
      for (CrossrefYearCount crossrefYearCount : yearCountList) {
        Long year = crossrefYearCount.getYear();// 年份
        File file = new File(dir + year);
        if (!file.exists()) {
          logger.error("目录:" + dir + " 不存在，将自动创建");
        }
        this.readerFile(file);
      }
    } catch (Exception e) {
      logger.error("PreImportCrossrefDataTask 出错 -----", e);
    }
  }


  private void readerFile(File file) {

    if (!file.isDirectory()) {

      String fileName = file.getName().toLowerCase();
      if (fileName.contains("finish")) {
        return;
      }
      // crossref_2012_0.txt
      Integer pubYear;
      int firstUlineIdx = fileName.indexOf("_");
      if (firstUlineIdx <= 0) {
        return;
      }
      pubYear = Integer.valueOf(fileName.substring(firstUlineIdx + 1, firstUlineIdx + 5));
      String json = null;
      try {
        // 获得文件编码
        String fileEncode = EncodingDetect.getJavaEncode(file.getPath());
        if ("BIG5".equals(fileEncode))
          fileEncode = "Gbk"; // EncodingDetect这个工具类 会把 ansi改成big5
        json = FileUtils.readFileToString(file, fileEncode);
      } catch (IOException e) {
        logger.error("读取文件内容失败：" + fileName);
        throw new ServiceException("读取文件内容失败：" + fileName, e);
      }
      try {
        if (json != null) {
          crossRefDataService.saveJson(pubYear, fileName, json);// 专利insid=0
        }
      } catch (Exception e) {
        logger.error("保存数据失败：" + fileName);
        throw new ServiceException("保存数据失败：" + fileName, e);
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
      String[] list = file.list();
      String path = file.getPath();
      File tmpfile = null;
      for (String string : list) {
        tmpfile = new File(path + "//" + string);
        readerFile(tmpfile);
      }

    }
  }

  public static void main(String[] args) throws Exception {
    String path = "D:/scm_dev/crossrefFile/2017/finish_crossref_2017_5.txt";
    // 获得文件编码
    String fileEncode = EncodingDetect.getJavaEncode(path);
    if ("BIG5".equals(fileEncode))
      fileEncode = "Gbk"; // EncodingDetect这个工具类 会把 ansi改成big5
    String json = FileUtils.readFileToString(new File(path), fileEncode);
    System.out.println(json);
  }
}


