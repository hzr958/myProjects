package com.smate.center.batch.quartz;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.google.gdata.util.common.base.StringUtil;
import com.smate.center.batch.model.sns.pub.ProjectDataFiveYear;
import com.smate.center.batch.service.TaskMarkerService;
import com.smate.center.batch.service.pub.ProjectDataFiveYearService;
import com.smate.core.base.utils.exception.BatchTaskException;

public class ProjectDataFiveYearTask {
  private final Logger logger = LoggerFactory.getLogger(getClass());
  @Autowired
  private TaskMarkerService taskMarkerService;
  private final static Integer SIZE = 3000; // 每次获取成果个数
  private Long id = 483588L;
  @Autowired
  private ProjectDataFiveYearService projectDataFiveYearService;
  private static BufferedWriter bw;
  static {
    File file1 = new File("/opt/hadoopfiles/project/input/input1.txt");
    try {
      if (!file1.exists()) {
        file1.createNewFile();
      }
      bw = new BufferedWriter(new FileWriter(file1, true));
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  public void run() throws BatchTaskException {
    logger.info("====================================ProjectDataFiveYearTask===开始运行");
    if (isRun() == false) {
      logger.info("====================================ProjectDataFiveYearTask===开关关闭");
      return;
    } else {
      try {
        taskMarkerService.closeQuartzApplication("ProjectDataFiveYearTask");
        doRun();
      } catch (BatchTaskException e) {
        logger.error("PdwhPubKeywordsTask1,运行异常", e);
        throw new BatchTaskException(e);
      }
    }
  }

  private void doRun() throws BatchTaskException {
    try {
      logger.info("===========================================ProjectDataFiveYearTask=========开始1");
      StringBuilder strBuilder = new StringBuilder();
      for (Long i = id; i < 732484L; i += SIZE) {
        List<ProjectDataFiveYear> dataList = projectDataFiveYearService.getProjectDataList(SIZE, i);
        for (ProjectDataFiveYear projectData : dataList) {
          try {
            String applicationCode = projectData.getApplicationCode();
            Set<String> zhkeywordsSet = null;
            Set<String> enkeywordsSet = null;
            // 处理关键字的大小写、空格、sha加密
            if (!StringUtil.isEmpty(projectData.getZhKeywords())) {
              zhkeywordsSet = projectDataFiveYearService.handlePubKeywords(projectData.getZhKeywords());
            }
            if (!StringUtil.isEmpty(projectData.getEnKeywords())) {
              enkeywordsSet = projectDataFiveYearService.handlePubKeywords(projectData.getEnKeywords());
            }
            strBuilder.append(projectData.getId());
            strBuilder.append(" ");
            strBuilder = projectDataFiveYearService.conbinePubKeywords(applicationCode, zhkeywordsSet, strBuilder);
            strBuilder = projectDataFiveYearService.conbinePubKeywords(applicationCode, enkeywordsSet, strBuilder);
            strBuilder.append(System.getProperty("line.separator"));
          } catch (Exception e) {
            logger.info("PdwhPubKeywordsTask1 -- 关键词组合出错  pubid ：" + projectData.getId(), e);

          }
        }
        bw.write(strBuilder.toString());
        bw.flush();
      }
      bw.close();
    } catch (Exception e) {
      logger.error("====================================ProjectDataFiveYearTask==处理出错", e);
    }

  }

  public boolean isRun() throws BatchTaskException {
    // 任务开关控制逻辑
    // return true;
    return taskMarkerService.getApplicationQuartzSettingValue("ProjectDataFiveYearTask") == 1;
  }
}
