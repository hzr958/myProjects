package com.smate.center.task.quartz;

import java.util.Date;
import java.util.List;

import org.apache.commons.collections4.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.smate.center.task.base.TaskAbstract;
import com.smate.center.task.model.sns.quartz.PubToPubSimpleErrorLog;
import com.smate.center.task.model.sns.quartz.ScmPubXml;
import com.smate.center.task.service.sns.quartz.PublicationToPubSimpleService;
import com.smate.center.task.service.sns.quartz.TaskMarkerService;
import com.smate.center.task.single.service.pub.ScmPubXmlService;
import com.smate.core.base.utils.cache.CacheService;
import com.smate.core.base.utils.exception.BatchTaskException;

@Deprecated
public class PubToPubsimpleTask9 extends TaskAbstract {
  private final Logger logger = LoggerFactory.getLogger(getClass());

  private final static Integer SIZE = 5000; // 每次刷新获取的个数

  /*
   * private Long startPubId = 1000009000000L;
   * 
   * private Long endPubId = 1000010500000L;
   */

  private Long startPubId;

  private Long endPubId;

  public Long getStartPubId() {
    return startPubId;
  }

  public void setStartPubId(Long startPubId) {
    this.startPubId = startPubId;
  }

  public Long getEndPubId() {
    return endPubId;
  }

  public void setEndPubId(Long endPubId) {
    this.endPubId = endPubId;
  }

  public PubToPubsimpleTask9() {
    super();
  }

  public PubToPubsimpleTask9(String beanName) {
    super(beanName);
  }

  @Autowired
  private ScmPubXmlService scmPubXmlService;

  @Autowired
  private PublicationToPubSimpleService publicationToPubSimpleService;

  @Autowired
  private TaskMarkerService taskMarkerService;

  @Autowired
  private CacheService cacheService;


  public void doRun() throws BatchTaskException {
    if (!super.isAllowExecution()) {
      logger.info("=========PubToPubSimpleTask9已关闭==========");
      return;
    }
    if (taskMarkerService.getApplicationQuartzSettingValue("PubToPubsimpleTask_removePubCache9") == 1) {
      cacheService.remove("PubToPubsimpleTask9", "last_pub_id");
    }

    try {
      Long lastPubId = (Long) cacheService.get("PubToPubsimpleTask9", "last_pub_id");
      if (lastPubId == null) {
        lastPubId = startPubId;
      }
      logger.debug("==========================================PubToPubSimpleTask9=========开始1");

      List<Long> pubList = publicationToPubSimpleService.getSnsPubSimpleIds(SIZE, lastPubId, endPubId);

      // List<Long> pubList =
      // publicationToPubSimpleService.getPubsByPsnId(1000000002544L);
      if (CollectionUtils.isEmpty(pubList)) {
        logger.info(
            "===========================================PubToPubSimpleTask9  没有获取到pubSimple表数据!!!!============, time = "
                + new Date());
        return;
      }

      Integer lastIndex = pubList.size() - 1;
      Long lastId = pubList.get(lastIndex);
      this.cacheService.put("PubToPubsimpleTask9", 60 * 60 * 24, "last_pub_id", lastId);

      for (Long pubId : pubList) {
        try {
          ScmPubXml scmPubXml = scmPubXmlService.getPubXml(pubId);
          if (scmPubXml == null) {
            throw new Exception("PubId = " + pubId + " 的xml为空");
          }
          this.publicationToPubSimpleService.copyPubXmlToDataStore(scmPubXml);
        } catch (Exception e) {
          PubToPubSimpleErrorLog error = new PubToPubSimpleErrorLog();
          error.setPubId(pubId);
          String errorMsg = e.toString();
          error.setErrorMsg(errorMsg);
          this.publicationToPubSimpleService.saveError(error);
          logger.debug("PubToPubSimpleTask9出错==============", e);
        }
      }

    } catch (Exception e) {
      logger.error("pubToPubsimpleTask9,运行异常", e);
    }

  }


}
