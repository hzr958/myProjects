package com.smate.center.batch.quartz;

import java.io.File;
import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.smate.center.batch.service.TaskMarkerService;
import com.smate.center.batch.service.pdwh.prj.PdwhFulltextDownloadService;
import com.smate.core.base.utils.exception.BatchTaskException;

/**
 * 将指定的基准库成果的全文下载到本地
 * 
 * @author SYL
 *
 */
public class PdwhFullTextDownloadToLocalTask {
  private final Logger logger = LoggerFactory.getLogger(getClass());

  @Autowired
  private TaskMarkerService taskMarkerService;

  @Autowired
  private PdwhFulltextDownloadService pdwhFulltextDownloadService;

  private String outDirPath = "/home/smate/tmpfulltext";// 要输出到的文件夹路径

  private int batchSize = 5000;// 每次要下载的数量

  public void run() throws BatchTaskException {
    logger.debug("====================================PdwhFullTextDownloadToLocalTask===开始运行");
    if (!isRun()) {
      logger.debug("====================================PdwhFullTextDownloadToLocalTask===开关关闭");
      return;
    }
    taskMarkerService.closeQuartzApplication("PdwhFullTextDownloadToLocalTask");// 关闭任务
    doRun();
  }

  private void doRun() {
    File outDir = new File(outDirPath);
    if (!(outDir.exists() && outDir.isDirectory())) {
      outDir.mkdir();
    }
    long lastPubId = 0L;
    while (true) {
      List<Map<String, Object>> list = pdwhFulltextDownloadService.queryNeedDownloadFile(lastPubId, batchSize);
      if (list == null || list.size() == 0) {
        return;
      }
      for (Map<String, Object> map : list) {
        try {
          pdwhFulltextDownloadService.downloadFile(String.valueOf(map.get("PUB_ID")),
              String.valueOf(map.get("FILE_PATH")), outDir);
        } catch (Exception e) {
          logger.error("========PdwhFullTextDownloadToLocalTask出现异常  pubId=", String.valueOf(map.get("PUB_ID")));
          e.printStackTrace();
        }
      }
      lastPubId = ((BigDecimal) list.get(list.size() - 1).get("PUB_ID")).longValue();
    }
  }

  public boolean isRun() throws BatchTaskException {
    // 任务开关控制逻辑
    return taskMarkerService.getApplicationQuartzSettingValue("PdwhFullTextDownloadToLocalTask") == 1;
  }
}
