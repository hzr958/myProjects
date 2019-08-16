package com.smate.center.batch.quartz;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import org.apache.commons.collections4.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.smate.center.batch.model.pub.pubtopubsimple.PubToPubSimpleErrorLog;
import com.smate.center.batch.model.sns.pub.ScmPubXml;
import com.smate.center.batch.service.TaskMarkerService;
import com.smate.center.batch.service.pdwh.prj.NsfcKeywordsService;
import com.smate.center.batch.service.pub.pubtopubsimple.PublicationToPubSimpleService;
import com.smate.center.batch.service.pubfulltexttoimage.ScmPubXmlService;
import com.smate.core.base.utils.cache.CacheService;
import com.smate.core.base.utils.exception.BatchTaskException;

/**
 * 计算nsfc项目补充关键词（来源：自填关键词与项目关联成果关键词）的tf与长度为2的子集co-tf，的详细信息，用于最终关键词排序与推荐
 * 
 */
public class PubToPubsimpleTask2 {
  private final Logger logger = LoggerFactory.getLogger(getClass());

  private final static Integer SIZE = 2000; // 每次刷新获取的个数

  @Autowired
  private TaskMarkerService taskMarkerService;
  @Autowired
  private NsfcKeywordsService nsfcKeywordsService;

  @Autowired
  private CacheService cacheService;

  public void run() throws BatchTaskException {
    logger.debug("====================================pubToPubsimpleTask__2===开始运行");
    if (isRun() == false) {
      logger.debug("====================================pubToPubsimpleTask__2===开关关闭");
      return;
    } else {
      try {
        doRun();
      } catch (BatchTaskException e) {
        logger.error("pubToPubsimpleTask__2,运行异常", e);
        throw new BatchTaskException(e);
      }
    }
  }

  public void doRun() throws BatchTaskException {
    // SIZE
    List<BigDecimal> prjIdList = nsfcKeywordsService.getToHandleKwList(SIZE);
    if (prjIdList == null || prjIdList.size() == 0) {
      logger.info("====================================pubToPubsimpleTask__2===运行完毕");
      return;
    }
    for (BigDecimal id : prjIdList) {
      Long prjId = id.longValue();
      try {
        this.nsfcKeywordsService.handleSubsets(prjId);
        this.nsfcKeywordsService.updateStatus(prjId, 1);
      } catch (Exception e) {
        logger.error("nsfc项目计算项目cotf错误,prjId:" + prjId, e);
        this.nsfcKeywordsService.updateStatus(prjId, 3);
      }
    }

  }



  public boolean isRun() throws BatchTaskException {
    // 任务开关控制逻辑
    return true;
    // return taskMarkerService.getApplicationQuartzSettingValue("PubToPubsimpleTask2") == 1;
  }
}
