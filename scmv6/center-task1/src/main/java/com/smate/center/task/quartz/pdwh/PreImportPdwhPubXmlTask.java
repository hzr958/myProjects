package com.smate.center.task.quartz.pdwh;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.smate.center.task.base.TaskAbstract;
import com.smate.center.task.service.pdwh.quartz.DbCacheBfetchFileService;
import com.smate.center.task.service.sns.quartz.TaskMarkerService;

/**
 * 老系统迁移过来，基准库成果xml导入预处理任务
 * 
 * @author LIJUN
 * @date 2018年4月17日
 */
public class PreImportPdwhPubXmlTask extends TaskAbstract {

  public PreImportPdwhPubXmlTask() {
    super();
  }

  public PreImportPdwhPubXmlTask(String beanName) {
    super(beanName);
  }

  private final Logger logger = LoggerFactory.getLogger(getClass());
  private String fileDir = null;
  @Autowired
  private DbCacheBfetchFileService dbCacheBfetchFileService;
  @Autowired
  private TaskMarkerService taskMarkerService;

  public void doRun() throws Exception {
    if (!super.isAllowExecution()) {
      return;
    }
    Integer value = taskMarkerService.getApplicationQuartzSettingValue("PdwhPubXmlType");
    if (value == null) {
      value = 0;// 默认为0，普通的成果
    }
    try {
      dbCacheBfetchFileService.readerFile(fileDir, value);
    } catch (Exception e) {
      logger.error("PreImportPdwhPubXmlTask读取文件错误", e);
    }

  }

  public String getFileDir() {
    return fileDir;
  }

  public void setFileDir(String fileDir) {
    this.fileDir = fileDir;
  }

}
