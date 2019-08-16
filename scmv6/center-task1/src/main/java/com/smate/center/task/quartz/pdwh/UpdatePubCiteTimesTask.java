package com.smate.center.task.quartz.pdwh;

import java.io.File;

import org.apache.commons.io.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.smate.center.task.base.TaskAbstract;
import com.smate.center.task.exception.SingleTaskException;
import com.smate.center.task.service.pdwh.quartz.UpdatePubCiteTimesService;

/**
 * 成果引用更新
 * 
 * @author LJ
 *
 */
public class UpdatePubCiteTimesTask extends TaskAbstract {

  private final Logger logger = LoggerFactory.getLogger(getClass());
  @Autowired
  UpdatePubCiteTimesService updatePubCiteTimesService;
  private String skipDir;

  public UpdatePubCiteTimesTask() {
    super();
  }

  public UpdatePubCiteTimesTask(String beanName) {
    super(beanName);
  }

  public String getSkipDir() {
    return skipDir;
  }

  public void setSkipDir(String skipDir) {
    this.skipDir = skipDir;
  }

  public void doRun() throws SingleTaskException {

    if (!super.isAllowExecution()) {
      return;
    }

    // 递归读取处理文件
    File readFile = null;
    try {
      readFile = updatePubCiteTimesService.getfile();
    } catch (Exception e) {
    }
    this.readerFile(readFile);

  }

  /**
   * 读取并处理文件
   * 
   * @param file
   */
  private void readerFile(File file) {
    // 方便不重启服务上传文件到临时目录待处理。
    if (file.isDirectory() && file.getName().startsWith(skipDir)) {
      return;
    }
    if (!file.isDirectory()) {
      String fileName = file.getName();
      // 跳过处理过的文件
      if (!file.getName().startsWith("finish_") && !file.getName().startsWith("error_")
          && !file.getName().startsWith("failed_")) {
        try {
          updatePubCiteTimesService.handleXML(file);
        } catch (Exception e) {
          changeFailed(file, fileName);
          logger.error("处理XML成果引用更新出错，文件名：" + file.getName(), e);
        }

      }
    } else {
      String[] list = file.list();
      String path = file.getPath();
      File tmpfile = null;
      for (String string : list) {
        tmpfile = new File(path + "//" + string);
        readerFile(tmpfile);
      }

    }
  }

  /**
   * 标记为处理失败
   * 
   * @param file
   * @param fileName
   * @throws Exception
   */
  public void changeFailed(File file, String fileName) {
    File newFile = new File(file.getPath().replace(fileName, "") + "failed" + "_" + fileName);
    try {
      FileUtils.moveFile(file, newFile);
    } catch (Exception e) {
      logger.error("文件标记处理失败：" + "path:" + file.getPath(), e);
    }
  }
}
