package com.smate.center.batch.quartz;

import java.util.List;

import org.apache.commons.collections4.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.smate.center.batch.model.rol.pub.PublicationRol;
import com.smate.center.batch.service.TaskMarkerService;
import com.smate.center.batch.service.pub.mq.PubAssignMessageProducer;
import com.smate.center.batch.service.rol.pub.PublicationRolService;
import com.smate.core.base.utils.cache.CacheService;
import com.smate.core.base.utils.exception.BatchTaskException;

public class RePubAssignSendTask {
  private static final int BATCH_SIZE = 50;
  private final Logger logger = LoggerFactory.getLogger(getClass());
  @Autowired
  private PublicationRolService publicationRolService;
  @Autowired
  private TaskMarkerService taskMarkerService;
  @Autowired
  private CacheService cacheService;
  @Autowired
  private PubAssignMessageProducer pubAssignMessageProducer;

  public void run() throws BatchTaskException {
    logger.info("====================================RePubAssignSendTask===开始运行");
    if (isRun() == false) {
      logger.info("====================================RePubAssignSendTask===开关关闭");
      return;
    } else {
      try {
        doRun();
      } catch (BatchTaskException e) {
        logger.error("RePubAssignSendTask,运行异常", e);
        throw new BatchTaskException(e);
      }
    }
  }

  private void doRun() throws BatchTaskException {
    logger.info("===========================================RePubAssignSendTask=========开始1");
    Long lastPubId = 0L;
    List<PublicationRol> pubList = publicationRolService.getPublicationList(BATCH_SIZE, lastPubId);
    if (CollectionUtils.isEmpty(pubList)) {
      taskMarkerService.closeQuartzApplication("RePubAssignSendTask");
    }
    for (PublicationRol publicationRol : pubList) {
      try {
        pubAssignMessageProducer.assignByPub(publicationRol.getInsId(), publicationRol.getCreatePsnId(),
            publicationRol.getId(), 1);
      } catch (Exception e) {
        logger.error("成果指派出错 ，pubid=" + publicationRol.getId(), e);
      }

    }
  }

  public boolean isRun() throws BatchTaskException {
    // 任务开关控制逻辑
    // return true;
    return taskMarkerService.getApplicationQuartzSettingValue("RePubAssignSendTask") == 1;
  }

}
