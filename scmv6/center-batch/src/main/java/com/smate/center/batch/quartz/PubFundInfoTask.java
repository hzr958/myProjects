package com.smate.center.batch.quartz;

import java.util.Arrays;
import java.util.List;

import org.apache.commons.collections4.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.smate.center.batch.model.pdwh.pub.cnki.CnkiPubAssign;
import com.smate.center.batch.service.pdwh.pub.CnkiPublicationService;
import com.smate.core.base.utils.cache.CacheService;
import com.smate.core.base.utils.exception.BatchTaskException;

public class PubFundInfoTask {
  private final Logger logger = LoggerFactory.getLogger(getClass());

  @Autowired
  private CnkiPublicationService cnkiPublicationService;

  @Autowired
  private CacheService cacheService;

  public void run() throws BatchTaskException {
    logger.debug("====================================PubFundInfo===开始运行");
    if (isRun() == false) {
      logger.debug("====================================PubFundInfo===开关关闭");
      return;
    } else {
      try {

        doRun();

      } catch (BatchTaskException e) {
        logger.error("PubFundInfo,运行异常", e);
        throw new BatchTaskException(e);
      }
    }
  }

  public void doRun() throws BatchTaskException {
    try {

      List<Long> ins = Arrays.asList(new Long[] {201605111L, 201605112L});
      for (Long insId : ins) {

        logger.debug("===========================================PubFundInfo=========开始1");
        List<Long> pubList = cnkiPublicationService.getCnkiPubId(insId);
        logger.debug("===========================================PubFundInfo  获取cnki_pub_assign表数据=========2");

        if (CollectionUtils.isNotEmpty(pubList)) {
          for (Long pubId : pubList) {
            CnkiPubAssign pub = cnkiPublicationService.getCnkiPubAssign(pubId, insId);
            try {
              if (pub == null) {
                continue;
              }
              String xml = pub.getXmlData();
              cnkiPublicationService.dealCnkiXml(xml, pubId, insId);
              cnkiPublicationService.savePubAssignStatus(pub, 6);
            } catch (Exception e) {
              cnkiPublicationService.savePubAssignStatus(pub, 7);
              logger.debug("BatchPubToPubSimpleTask出错==============", e);
            }
          }

        } else {
          logger.debug("===========================================PubFundInfo  没有获取到Publication表数据!!!!============3");
        }
      }
    } catch (Exception e) {
      logger.error("pubToPubsimpleTask__1,运行异常", e);
    }

  }

  public boolean isRun() throws BatchTaskException {
    // 任务开关控制逻辑
    // return true;
    return false;
  }
}
