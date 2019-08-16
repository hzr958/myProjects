package com.smate.center.batch.quartz.service;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.smate.center.batch.connector.model.job.BatchJobs;
import com.smate.center.batch.connector.model.job.BatchQuartz;
import com.smate.center.batch.connector.service.job.BatchJobsService;
import com.smate.center.batch.constant.BatchConfConstant;
import com.smate.center.batch.constant.BatchJobDetailConstant;
import com.smate.center.batch.service.pub.fulltext.InputIsiPubFulltextService;
import com.smate.core.base.utils.exception.BatchTaskException;
import com.smate.core.base.utils.json.JacksonUtils;

/**
 * 成果全文导入 入口
 * 
 * @author tsz
 *
 */

public class BatchInputFullTextServiceImpl implements BatchQuartzTaskService {

  private final Logger logger = LoggerFactory.getLogger(getClass());

  @Autowired
  private InputIsiPubFulltextService inputIsiPubFulltextService;
  @Autowired
  private BatchJobsService batchJobsService;

  @Override
  public void taskExecute(BatchQuartz task) throws BatchTaskException {
    try {
      this.readFile();
    } catch (Exception e) {
      logger.error("InputIsiFulltextTaskLaunch运行异常", e);
      throw new BatchTaskException(e);
    }
  }

  public void readFile() throws BatchTaskException {
    String sourceFileDir = inputIsiPubFulltextService.getSourceFileDir();
    File file = new File(sourceFileDir);
    if (!file.exists()) {
      logger.error("目录:" + sourceFileDir + " 不存在");
      throw new BatchTaskException("目录:" + sourceFileDir + " 不存在");
    }
    this.readerFile(file);
  }



  /**
   * 递归方法
   * 
   * @param file
   * @throws BatchTaskException
   */
  private void readerFile(File file) throws BatchTaskException {
    // 方便不重启服务上传文件到临时目录待处理。
    if (file.isDirectory() && file.getName().startsWith("tmp_tohandle")) {
      return;
    }
    if (!file.isDirectory()) {
      String fileName = file.getName();
      // 标准的格式 isi_pub_id 全是数字 isi_source_id 大写字母 或数据
      if (!file.getName().startsWith("finish_") && !file.getName().startsWith("error_")
          && !file.getName().startsWith("failed_") && fileName.matches("^[0-9A-Z]+\\.pdf$")) {
        logger.error("取要上传的全文的文件----------------------" + fileName);
        // 判断过滤 格式
        try {
          BatchJobs batchJobs = new BatchJobs();
          Map<String, String> map = new HashMap<String, String>();
          map.put("msg_id", file.getPath());
          batchJobs.setJobContext(JacksonUtils.jsonMapSerializer(map));
          batchJobs.setStatus(0);
          batchJobs.setWeight(BatchConfConstant.TASK_WEIGHT_A);
          batchJobs.setStrategy(BatchJobDetailConstant.INPUT_FULL_TEXT);
          boolean result = batchJobsService.saveJob(batchJobs);
          if (!result) {
            logger.error("取要上传的全文的文件出错----------------------" + fileName);
            inputIsiPubFulltextService.changeErrorName(file, fileName);
          }
        } catch (Exception e) {
          logger.error("取要上传的全文的文件----------------------失败", e);
          inputIsiPubFulltextService.changeErrorName(file, fileName);
        }
      }
    } else {
      // 递归读取文件 可以加过滤 格式
      File[] filelist = file.listFiles();
      for (File f : filelist) {
        this.readerFile(f);
      }
    }
  }


}
