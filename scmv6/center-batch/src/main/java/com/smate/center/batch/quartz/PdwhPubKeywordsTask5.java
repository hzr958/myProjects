package com.smate.center.batch.quartz;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;
import java.util.Set;

import org.apache.commons.collections4.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.google.gdata.util.common.base.StringUtil;
import com.smate.center.batch.model.sns.pub.PdwhPubKeywordsCategory;
import com.smate.center.batch.service.TaskMarkerService;
import com.smate.center.batch.service.pub.PdwhPubKeywordsService;
import com.smate.core.base.utils.exception.BatchTaskException;

public class PdwhPubKeywordsTask5 {
  private final Logger logger = LoggerFactory.getLogger(getClass());
  @Autowired
  private TaskMarkerService taskMarkerService;
  private final static Integer SIZE = 3000; // 每次获取成果个数
  private Long startPubId = 12421088L;
  private Long endPubId = 25918274L;
  @Autowired
  private PdwhPubKeywordsService pdwhPubKeywordsService;
  private static BufferedWriter bw;

  static {
    File file1 = new File("/opt/hadoopfiles/publication/input/input5.txt");
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
    logger.info("====================================PdwhPubKeywordsTask5===开始运行");
    if (isRun() == false) {
      logger.info("====================================PdwhPubKeywordsTask5===开关关闭");
      return;
    } else {
      try {
        taskMarkerService.closeQuartzApplication("PdwhPubKeywordsTask5");
        doRun();
      } catch (BatchTaskException e) {
        logger.error("PdwhPubKeywordsTask5,运行异常", e);
        throw new BatchTaskException(e);
      }
    }
  }

  private void doRun() throws BatchTaskException {
    try {
      logger.info("===========================================PdwhPubKeywordsTask5=========开始1");
      for (Long i = startPubId; i < endPubId; i += SIZE) {
        StringBuilder strBuilder = new StringBuilder();
        List<PdwhPubKeywordsCategory> pdwhPubKeywords = pdwhPubKeywordsService.getPdwhPubKeywords(SIZE, i, endPubId);
        if (CollectionUtils.isNotEmpty(pdwhPubKeywords)) {
          for (PdwhPubKeywordsCategory pubKeywords : pdwhPubKeywords) {
            try {
              Long categoryId = pubKeywords.getScmCategoryId();
              Set<String> kerywordsSet = null;
              // 处理关键字的大小写、空格、sha加密
              StringBuffer kerywords = new StringBuffer();
              kerywords.append(pubKeywords.getZhKeywords());
              kerywords.append(pubKeywords.getEnKeywords());
              if (!StringUtil.isEmpty(kerywords.toString())) {
                kerywordsSet = pdwhPubKeywordsService.handlePubKeywords(kerywords.toString());
              }
              strBuilder.append(pubKeywords.getPubId());
              strBuilder.append(" ");
              strBuilder = pdwhPubKeywordsService.conbinePubKeywords(categoryId, kerywordsSet, strBuilder);
              strBuilder.append(System.getProperty("line.separator"));
            } catch (Exception e) {
              logger.info("PdwhPubKeywordsTask1 -- 关键词组合出错  pubid ：" + pubKeywords.getPubId(), e);
              pdwhPubKeywordsService.saveOpResult(pubKeywords.getPubId(), 9);

            }
          }
        }
        bw.write(strBuilder.toString());
        bw.flush();
      }
      bw.close();
    } catch (Exception e) {
      logger.error("====================================PdwhPubKeywordsTask5==处理出错", e);
    }
  }

  public boolean isRun() throws BatchTaskException {
    // 任务开关控制逻辑
    // return true;
    return taskMarkerService.getApplicationQuartzSettingValue("PdwhPubKeywordsTask5") == 1;
  }
}
