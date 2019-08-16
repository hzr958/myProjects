package com.smate.center.task.quartz.pdwh;

import java.util.List;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.smate.center.task.base.TaskAbstract;
import com.smate.center.task.exception.SingleTaskException;
import com.smate.center.task.model.pdwh.quartz.PubFundingInfo;
import com.smate.center.task.service.pdwh.quartz.PdwhPublicationService;
import com.smate.center.task.service.pdwh.quartz.PublicationXmlPdwhService;
import com.smate.center.task.service.sns.quartz.TaskMarkerService;
import com.smate.core.base.utils.cache.CacheService;
import com.smate.web.v8pub.dom.pdwh.PubPdwhDetailDOM;

public class SaveFundInfoFromXMlTask3 extends TaskAbstract {
  private final Logger logger = LoggerFactory.getLogger(getClass());
  private int batchSize = 200;
  private Long PubId = 19901336968L;
  @Autowired
  private PdwhPublicationService pdwhPublicationService;
  @Autowired
  private PublicationXmlPdwhService publicationXmlPdwhService;
  @Autowired
  private TaskMarkerService taskMarkerService;
  @Autowired
  private CacheService cacheService;
  public static String PDWH_PUB_ID_CACHE3 = "pdwh_pub_id_cache3";

  public SaveFundInfoFromXMlTask3() {
    super();
  }

  public SaveFundInfoFromXMlTask3(String beanName) {
    super(beanName);
  }

  public void doRun() throws SingleTaskException {
    if (!super.isAllowExecution()) {
      logger.info("=========SaveFundInfoFromXMlTask 已关闭==========");
      return;
    }
    // 是否移除psn_id缓存
    if (taskMarkerService.getApplicationQuartzSettingValue("SaveFundInfoFromXMlTask_removePubIdCache3") == 1) {
      cacheService.remove(PDWH_PUB_ID_CACHE3, "last_pub_id");
    }
    logger.error("===========lastPubId为=======" + cacheService.get(PDWH_PUB_ID_CACHE3, "last_pub_id"));
    try {
      Long lastPubId = (Long) cacheService.get(PDWH_PUB_ID_CACHE3, "last_pub_id");
      if (lastPubId == null) {
        lastPubId = 20670637L;
      }
      List<PubPdwhDetailDOM> pubList = pdwhPublicationService.getPdwhPubIds(lastPubId, batchSize);
      if (CollectionUtils.isEmpty(pubList) || pubList.get(0).getPubId() > PubId) {
        return;
      }
      lastPubId = pubList.get(pubList.size() - 1).getPubId();
      this.cacheService.put(PDWH_PUB_ID_CACHE3, 60 * 60 * 24, "last_pub_id", lastPubId);
      for (PubPdwhDetailDOM pub : pubList) {
        String fundInfo = StringUtils.trimToEmpty(pub.getFundInfo());
        PubFundingInfo PubFundingInfo =
            new PubFundingInfo(pub.getPubId(), pub.getSrcDbId(), StringUtils.substring(fundInfo, 0, 3000));
        try {
          pdwhPublicationService.savePubFundingInfo(PubFundingInfo);
        } catch (Exception e) {
          logger.error("保存信息至PubFundingInfo出错,pubId = " + pub.getPubId(), e);
        }
      }
    } catch (Exception e1) {
      logger.error("SaveFundInfoFromXMlTask出错,,,,,,,", e1);
    }
  }
}
