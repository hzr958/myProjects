package com.smate.center.task.quartz;

import java.util.Date;
import java.util.List;

import org.apache.commons.collections4.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.smate.center.task.base.TaskAbstract;
import com.smate.center.task.exception.SingleTaskException;
import com.smate.center.task.model.pdwh.quartz.TmpPublicationForSnsGroup;
import com.smate.center.task.service.pdwh.quartz.PdwhPubForGroupService;
import com.smate.center.task.service.sns.quartz.TaskMarkerService;
import com.smate.center.task.single.service.pub.ScmPubXmlService;
import com.smate.core.base.utils.cache.CacheService;

@Deprecated
public class PubToPubsimpleTask5 extends TaskAbstract {
  private final Logger logger = LoggerFactory.getLogger(getClass());

  private final static Integer SIZE = 1000; // 每次刷新获取的个数

  /*
   * private Long startPubId = 0L;
   * 
   * private Long endPubId = 120000008774000L;
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

  public PubToPubsimpleTask5() {
    super();
  }

  public PubToPubsimpleTask5(String beanName) {
    super(beanName);
  }

  @Autowired
  private ScmPubXmlService scmPubXmlService;

  @Autowired
  private PdwhPubForGroupService pdwhPubForGroupService;

  @Autowired
  private TaskMarkerService taskMarkerService;

  @Autowired
  private CacheService cacheService;



  public void doRun() throws SingleTaskException {
    if (!super.isAllowExecution()) {
      logger.info("=========PubToPubSimpleTask5已关闭==========");
      return;
    }

    try {

      List<TmpPublicationForSnsGroup> pubList = pdwhPubForGroupService.getPdwhPubInfo(SIZE, startPubId, endPubId);

      // List<Long> pubList =
      // publicationToPubSimpleService.getPubsByPsnId(1000000002544L);
      if (CollectionUtils.isEmpty(pubList)) {
        logger.info("===========================================PubToPubSimpleTask5  没有获取到表数据!!!!============, time = "
            + new Date());
        return;
      }

      for (TmpPublicationForSnsGroup pub : pubList) {
        try {
          pdwhPubForGroupService.fetchPubFundingInfo(pub);
          pdwhPubForGroupService.saveStatus(pub, 1);
        } catch (Exception e) {
          pdwhPubForGroupService.saveStatus(pub, 9);
          logger.debug("PubToPubSimpleTask5出错============== pubId = " + pub.getPubAllId(), e);
        }
      }

    } catch (Exception e) {
      logger.error("pubToPubsimpleTask5,运行异常", e);
    }

  }


}
