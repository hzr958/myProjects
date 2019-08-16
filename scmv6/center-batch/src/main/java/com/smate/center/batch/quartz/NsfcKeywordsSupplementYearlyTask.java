package com.smate.center.batch.quartz;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.smate.center.batch.model.pdwh.prj.NsfcCategoryForKwdicUpdate;
import com.smate.center.batch.model.pdwh.prj.NsfcKwTfCotfForSorting;
import com.smate.center.batch.service.TaskMarkerService;
import com.smate.center.batch.service.pdwh.prj.NsfcKeywordsService;
import com.smate.core.base.utils.data.XmlUtil;
import com.smate.core.base.utils.exception.BatchTaskException;

// sortingKwInDiscode 新方法
public class NsfcKeywordsSupplementYearlyTask {
  private final Logger logger = LoggerFactory.getLogger(getClass());

  private final static Integer SIZE = 5000; // 每次刷新获取的个数

  @Autowired
  private TaskMarkerService taskMarkerService;
  @Autowired
  private NsfcKeywordsService nsfcKeywordsService;


  public void run() throws BatchTaskException {
    logger.debug("====================================NsfcKeywordsSupplementYearlyTask===开始运行");
    if (isRun() == false) {
      logger.debug("====================================NsfcKeywordsSupplementYearlyTask===开关关闭");
      return;
    } else {
      try {
        doRun();
      } catch (BatchTaskException e) {
        logger.error("NsfcKeywordsSupplementYearlyTask,运行异常", e);
      }
    }
  }

  public void doRun() throws BatchTaskException {
    List<NsfcCategoryForKwdicUpdate> toHandleList = this.nsfcKeywordsService.getNsfcDisciplineList(0, 7, SIZE);

    if (toHandleList == null || toHandleList.size() == 0) {
      logger.info("=====NsfcKeywordsSupplementYearlyTask处理完毕=====");
      return;
    }

    for (NsfcCategoryForKwdicUpdate nfku : toHandleList) {
      try {
        int toSupplementkwNum = 0;
        int applCounts = 0;
        int kwCountsDiscipline = 0;
        applCounts = nfku.getApplicationCounts();
        kwCountsDiscipline = this.nsfcKeywordsService.getNsfcDisciplineKwCounts(nfku.getNsfcApplicationCode());
        List<String> discKwList = this.nsfcKeywordsService.getNsfcDisciplineKw(nfku.getNsfcApplicationCode());
        Set<String> discKwNonDup = new HashSet<String>();
        Set<String> discKwSupplementNonDup = new HashSet<String>();
        if (CollectionUtils.isNotEmpty(discKwList)) {
          for (String discKw : discKwList) {
            if (!StringUtils.isEmpty(discKw)) {
              discKw = XmlUtil.cToe(discKw);
              discKw = discKw.toLowerCase().trim();
              discKwNonDup.add(discKw);
            }
          }
        }
        if (applCounts == 0 || kwCountsDiscipline > applCounts * 2.5) {
          // 将学科主任维护关键词全部加入词库
          this.nsfcKeywordsService.saveNsfcKws(discKwNonDup, 1, nfku.getNsfcApplicationCode());
        }
        int applC = (int) (applCounts * 2.5);
        if (kwCountsDiscipline < applC) {
          // 补充至2.5个申请书数量
          toSupplementkwNum = applC - kwCountsDiscipline;
          // 先与学科主任维护关键词查重
          // 取最多不超过applCounts2.5倍的关键词数，防止重复，少取值
          List<String> kwSupplements =
              this.nsfcKeywordsService.getNsfcKwSupplement(nfku.getNsfcApplicationCode(), applC);
          Set<String> kwSupplementsSet = this.nsfcKeywordsService.nsfcKwSortingByTfCotf(kwSupplements,
              nfku.getNsfcApplicationCode(), toSupplementkwNum, discKwNonDup);
          this.nsfcKeywordsService.saveNsfcKws(discKwSupplementNonDup, 2, nfku.getNsfcApplicationCode());
        }
        this.nsfcKeywordsService.saveNsfcCategoryForKwdicUpdate(nfku, 1);
      } catch (Exception e) {
        logger.error("计算NSFC关键词出错，对应学部为" + nfku.getNsfcApplicationCode(), e);
        this.nsfcKeywordsService.saveNsfcCategoryForKwdicUpdate(nfku, 3);
      }
    }


  }

  public boolean isRun() throws BatchTaskException {
    // 任务开关控制逻辑
    // return true;
    return taskMarkerService.getApplicationQuartzSettingValue("PubToPubsimpleTask2") == 1;
  }

  public static void main(String[] args) {
    List<NsfcKwTfCotfForSorting> sortList = new ArrayList<NsfcKwTfCotfForSorting>();
    NsfcKwTfCotfForSorting sktffs1 = new NsfcKwTfCotfForSorting("C04", "kw1", 1L, 4L);
    sortList.add(sktffs1);
    sortList.add(new NsfcKwTfCotfForSorting("C04", "kw2", 4L, 4L));
    sortList.add(new NsfcKwTfCotfForSorting("C04", "kw3", 6L, 4L));
    sortList.add(new NsfcKwTfCotfForSorting("C04", "kw4", 2L, 4L));
    Collections.sort(sortList);
    for (NsfcKwTfCotfForSorting n : sortList) {
      System.out.println(n);
    }
  }

}
