package com.smate.center.batch.quartz;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.smate.center.batch.model.sns.relationanalysis.KeywordCategory;
import com.smate.center.batch.model.sns.relationanalysis.KeywordsNew;
import com.smate.center.batch.service.relationanalysis.KeywordCategoryService;
import com.smate.core.base.utils.exception.BatchTaskException;

public class RelationAnalysisKeywordsSplitTask {

  private final Logger logger = LoggerFactory.getLogger(getClass());
  private static boolean isRunning = false;
  @Autowired
  private KeywordCategoryService keywordCategoryService;

  public void run() throws BatchTaskException {
    logger.debug("====================================RelationAnalysisKeywordsSplitTask===开始运行");

    if (isRunning) {
      return;
    }

    if (isRun() == false) {
      logger.debug("====================================RelationAnalysisKeywordsSplitTask===开关关闭");
      return;
    } else {
      try {
        isRunning = true;
        doRun();

      } catch (BatchTaskException e) {
        logger.error("RelationAnalysisKeywordsSplitTask,运行异常", e);
        throw new BatchTaskException(e);
      }
    }
  }

  public void doRun() throws BatchTaskException {
    try {


      logger.debug("===========================================RelationAnalysisKeywordsSplitTask=========开始1");

      logger.debug(
          "===========================================RelationAnalysisKeywordsSplitTask  获取keyword_category_20160120表数据=========2");

      ArrayList<Integer> enG = new ArrayList<Integer>();
      ArrayList<Integer> zhG = new ArrayList<Integer>();

      for (Integer i = 1; i < 50; i++) {

        KeywordCategory one = keywordCategoryService.getDataById(i);


        if (one != null) {
          String[] keywordZhList = one.getKeywordsZh().split("/");
          String[] keywordEnList = one.getKeywordsEn().split("/");
          List<String> kZhList = new ArrayList<String>();
          List<String> kEnList = new ArrayList<String>();
          // 清理
          for (String keywordZh : keywordZhList) {

            if (StringUtils.isNotEmpty(keywordZh)) {
              keywordZh = keywordZh.trim();
              if (keywordZh.indexOf(";") != -1) {
                keywordZh = keywordZh.replace(";", ",");
              }
              kZhList.add(keywordZh);
            }
          }

          for (String keywordEn : keywordEnList) {

            if (StringUtils.isNotEmpty(keywordEn)) {
              keywordEn.trim();
              if (keywordEn.indexOf(";") != -1) {
                keywordEn.replace(";", ",");
              }
              kEnList.add(keywordEn);
            }
          }


          Integer length = kZhList.size() >= kEnList.size() ? kZhList.size() : kEnList.size();

          Integer enL = kEnList.size();
          Integer zhL = kZhList.size();

          Integer gap = zhL - enL;

          if (enL > zhL) {
            enG.add(one.getKcId());
          }
          if (zhL > enL) {
            zhG.add(one.getKcId());
          }

          // 插入新表中
          Integer kcId = one.getKcId();
          String categoryZh = one.getCategoryZh();
          String categoryEn = one.getCategoryEn();
          Integer categoryId = one.getCategoryId();
          Integer desciplineId = one.getDesciplineId();
          String desciplineZh = one.getDesciplineZh();
          String desciplineEn = one.getDesciplineEn();

          for (int k = 0; k < length; k++) {
            KeywordsNew kwn = new KeywordsNew();
            kwn.setKcId(kcId);
            kwn.setCategoryId(categoryId);
            kwn.setCategoryZh(categoryZh);
            kwn.setCategoryEn(categoryEn);
            kwn.setDesciplineId(desciplineId);
            kwn.setDesciplineZh(desciplineZh);
            kwn.setDesciplineEn(desciplineEn);
            if (gap >= 0) {
              if (k >= gap) {
                kwn.setKeywordsEn(kEnList.get(k - gap));
              }
              kwn.setKeywordsZh(kZhList.get(k));
            }
            if (gap < 0) {
              if (k >= gap) {
                kwn.setKeywordsZh(kZhList.get(k + gap));
              }
              kwn.setKeywordsEn(kEnList.get(k));
            }

            this.keywordCategoryService.saveToKeywordsNew(kwn);
          }


        } else {
          logger.debug(
              "===========================================RelationAnalysisKeywordsSplitTask  没有获取到keyword_category_20160120表数据!!!!============3");

        }
      }

      logger.debug("===========================================RelationAnalysisKeywordsSplitTask,关键字拆分完成。");


    } catch (Exception e) {
      logger.error("RelationAnalysisKeywordsSplitTask,运行异常", e);
    }

  }

  public boolean isRun() throws BatchTaskException {
    // 任务开关控制逻辑
    // return true;
    return false;
  }


}
