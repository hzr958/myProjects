package com.smate.center.task.quartz;

import java.util.List;

import org.apache.commons.collections4.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.smate.center.task.base.TaskAbstract;
import com.smate.center.task.model.rcmd.quartz.PublicationConfirm;
import com.smate.center.task.service.rcmd.quartz.PublicationConfirmService;

/**
 * 个人确认成果后，可能ROL那边系统错误，导致成果没有确认，需要程序自动确认.
 * 
 * @author zjh
 *
 */
@Deprecated
public class PubReconfirmTask extends TaskAbstract {
  Logger logger = LoggerFactory.getLogger(getClass());
  @Autowired
  private PublicationConfirmService publicationConfirmService;

  public PubReconfirmTask() {
    super();
  }

  public PubReconfirmTask(String beanName) {
    super(beanName);
  }

  public void run() {
    Long startId = 0L;
    try {
      while (true) {
        List<PublicationConfirm> list = publicationConfirmService.loadReconfirmList(startId);
        if (CollectionUtils.isEmpty(list)) {
          break;
        }
        for (PublicationConfirm pubConfirm : list) {
          try {
            startId = pubConfirm.getId();
            publicationConfirmService.reconfirmPublication(pubConfirm);
          } catch (Exception e) {
            publicationConfirmService.updateConfirmSyncNum(pubConfirm);
            logger.error("个人确认成果后，可能ROL那边系统错误，导致成果没有确认，需要程序自动确认失败", e);
          }

        }
      }

    } catch (Exception e) {
      logger.error("个人确认成果后，可能ROL那边系统错误，导致成果没有确认，需要程序自动确认失败", e);
    }

  }

}
