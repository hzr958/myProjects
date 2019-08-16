package com.smate.center.batch.quartz;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.smate.center.batch.service.pdwh.prj.NsfcKeywordsService;
import com.smate.core.base.utils.exception.BatchTaskException;

public class PdwhPubKeywordsTask2 {
  private final Logger logger = LoggerFactory.getLogger(getClass());

  private final static Integer SIZE = 5000; // 每次刷新获取的个数

  @Autowired
  private NsfcKeywordsService nsfcKeywordsService;


  public void run() throws BatchTaskException {
    logger.debug("====================================ScmBaseKeywordsTask2===开始运行");
    if (isRun() == false) {
      logger.debug("====================================ScmBaseKeywordsTask2===开关关闭");
      return;
    } else {
      try {
        doRun();
      } catch (BatchTaskException e) {
        logger.error("ScmBaseKeywordsTask2,运行异常", e);
      }
    }
  }

  public void doRun() throws BatchTaskException {
    List<String> toHandleList = this.nsfcKeywordsService.getNsfcCategoryToHandleKwList(7, SIZE);

    if (toHandleList == null || toHandleList.size() == 0) {
      logger.info("=====ScmBaseKeywordsTask2处理完毕=====");
      return;
    }

    for (String category : toHandleList) {
      try {
        List<String> nsfcKws = this.nsfcKeywordsService.getNsfcBaseKwByCategory(category, 1);
        // 获取包含关键词的成果id
        List<String> pubKws = new ArrayList<String>();
        if (nsfcKws != null && nsfcKws.size() > 0) {
          pubKws = this.nsfcKeywordsService.getPubKws(nsfcKws, category, 1);
          if (pubKws.size() > 0) {
            this.nsfcKeywordsService.deleteScmKwsByCategory(category, 1);
          } else {
            this.nsfcKeywordsService.updateNsfcCategoryStatus(category, 2);
            continue;
          }
        }
        for (String kw : pubKws) {
          // 计算TF与COTF
          if (StringUtils.isEmpty(kw) || kw.length() > 299) {
            continue;
          }
          Integer tf = this.nsfcKeywordsService.getPubKwTf(category, kw);
          Integer cotf = this.nsfcKeywordsService.getPubKwCotf(category, pubKws, kw, 1);
          this.nsfcKeywordsService.saveScmKwsInfo(category, kw, tf, cotf);
        }
        this.nsfcKeywordsService.updateNsfcCategoryStatus(category, 1);
      } catch (Exception e) {
        logger.error("计算NSFC关键词出错，对应学部为" + category, e);
        this.nsfcKeywordsService.updateNsfcCategoryStatus(category, 3);
      }
    }


  }

  public boolean isRun() throws BatchTaskException {
    // 任务开关控制逻辑
    return true;
    // return taskMarkerService.getApplicationQuartzSettingValue("PubToPubsimpleTask2") == 1;
  }

}
