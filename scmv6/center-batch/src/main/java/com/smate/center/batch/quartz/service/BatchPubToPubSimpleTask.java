package com.smate.center.batch.quartz.service;

import java.util.List;

import org.apache.commons.collections4.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.smate.center.batch.connector.model.job.BatchQuartz;
import com.smate.center.batch.model.pub.pubtopubsimple.PubToPubSimpleErrorLog;
import com.smate.center.batch.model.sns.pub.Publication;
import com.smate.center.batch.model.sns.pub.ScmPubXml;
import com.smate.center.batch.service.pub.pubtopubsimple.PublicationToPubSimpleService;
import com.smate.center.batch.service.pubfulltexttoimage.ScmPubXmlService;
import com.smate.core.base.utils.cache.CacheService;
import com.smate.core.base.utils.exception.BatchTaskException;

/**
 * 用于人员merge后同步老数据到v_pub_simple,v_pub_data_store
 * 
 */
public class BatchPubToPubSimpleTask implements BatchQuartzTaskService {

  Logger logger = LoggerFactory.getLogger(this.getClass());

  private final static Integer SIZE = 5000; // 每次刷新获取的个数

  private Long startPubId = 1L;

  private Long endPubId = 1200000000000L;

  @Autowired
  private ScmPubXmlService scmPubXmlService;

  @Autowired
  private PublicationToPubSimpleService publicationToPubSimpleService;

  @Autowired
  private CacheService cacheService;

  @Override
  public void taskExecute(BatchQuartz task) throws BatchTaskException {
    try {
      Long lastPubId = 0L;
      logger.debug("===========================================BatchPubToPubSimpleTask=========开始1");
      List<Long> pubList = publicationToPubSimpleService.getSnsPublication(SIZE, lastPubId, startPubId, endPubId);
      logger.debug("===========================================BatchPubToPubSimpleTask  获取Publication表数据=========2");

      if (CollectionUtils.isNotEmpty(pubList)) {
        for (Long pubId : pubList) {
          try {
            Publication pub = publicationToPubSimpleService.getSnsPublicationById(pubId);

            if (pub == null) {
              continue;
            }

            ScmPubXml scmPubXml = scmPubXmlService.getPubXml(pubId);

            if (scmPubXml == null) {
              throw new Exception("PubId = " + pubId + " 的xml为空");
            }

            this.publicationToPubSimpleService.copyPubSimpleData(pub, scmPubXml);
          } catch (Exception e) {
            PubToPubSimpleErrorLog error = new PubToPubSimpleErrorLog();
            error.setPubId(pubId);
            String errorMsg = e.toString();
            error.setErrorMsg(errorMsg);
            this.publicationToPubSimpleService.saveError(error);
            logger.debug("BatchPubToPubSimpleTask出错==============", e);
          }
        }
      } else {
        logger.debug(
            "===========================================BatchPubToPubSimpleTask  没有获取到Publication表数据!!!!============3");
      }

    } catch (Exception e) {
      logger.error("BatchPubToPubSimpleTask运行异常", e);
      throw new BatchTaskException(e);
    }
  }

}
