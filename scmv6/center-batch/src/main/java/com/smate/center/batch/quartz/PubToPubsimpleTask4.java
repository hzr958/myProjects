package com.smate.center.batch.quartz;

import java.text.Format;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Formatter;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.smate.center.batch.model.pdwh.pubimport.TmpPdwhPubAddr;
import com.smate.center.batch.service.TaskMarkerService;
import com.smate.center.batch.service.pdwh.prj.NsfcKeywordsService;
import com.smate.center.batch.service.pdwh.pubimport.PdwhPublicationService;
import com.smate.center.batch.service.pub.pubtopubsimple.PublicationToPubSimpleService;
import com.smate.center.batch.service.pubfulltexttoimage.ScmPubXmlService;
import com.smate.core.base.utils.exception.BatchTaskException;

/**
 * 计算补充的自填关键词：对nsfc项目自填关键词与关联成果关键词计算tf与co-tf后排序 保留的数量为：前三年对应学科下申请书的最大值的2.5倍
 * 如果没有申请书数据，则为前三年项目数的5倍*2.5
 * 
 */
public class PubToPubsimpleTask4 {
  private final Logger logger = LoggerFactory.getLogger(getClass());

  private final static Integer SIZE = 5000; // 每次刷新获取的个数


  @Autowired
  private TaskMarkerService taskMarkerService;

  @Autowired
  private PdwhPublicationService pdwhPublicationService;

  @Autowired
  private NsfcKeywordsService nsfcKeywordsService;

  public void run() throws BatchTaskException {
    logger.info("====================================pubToPubsimpleTask__4===开始运行");
    if (isRun() == false) {
      logger.info("====================================pubToPubsimpleTask__4===开关关闭");
      return;
    } else {
      try {
        doRun();
      } catch (BatchTaskException e) {
        logger.error("pubToPubsimpleTask__4,运行异常", e);
        throw new BatchTaskException(e);
      }
    }
  }

  public void doRun() throws BatchTaskException {
    List<String> tohandleList = this.nsfcKeywordsService.getToHandleDiscode(SIZE);
    if (tohandleList == null || tohandleList.size() == 0) {
      logger.info("====================================pubToPubsimpleTask__4===处理完毕");
      return;
    }
    Calendar c = Calendar.getInstance();
    Format f = new SimpleDateFormat("yyyy");
    c.add(Calendar.YEAR, -2);
    Integer year = Integer.valueOf(f.format(c.getTime()));
    Integer[] lastThreeYear = {year, year + 1, year + 2};
    for (String category : tohandleList) {
      Integer rs = this.nsfcKeywordsService.sortingKwInDiscode(category, lastThreeYear);
      this.nsfcKeywordsService.updateDiscodeStatus(rs, category);
    }

  }

  public boolean isRun() throws BatchTaskException {
    // 任务开关控制逻辑
    return true;
    // return
    // taskMarkerService.getApplicationQuartzSettingValue("PubToPubsimpleTask4")
    // == 1;
  }
}
