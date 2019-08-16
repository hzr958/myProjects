package com.smate.center.task.quartz.email;

import java.util.List;

import org.apache.commons.collections4.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.smate.center.task.base.TaskAbstract;
import com.smate.center.task.service.email.NoticeBeEndorsedService;
import com.smate.core.base.psn.model.profile.KeywordIdentification;

/**
 * 赞研究领域邮件
 * 
 * @author zjh
 *
 */
public class NoticeBeEndorseeAndEndorseTask extends TaskAbstract {
  protected final Logger logger = LoggerFactory.getLogger(getClass());
  @Autowired
  private NoticeBeEndorsedService noticeBeEndorsedService;

  public NoticeBeEndorseeAndEndorseTask() {}

  public NoticeBeEndorseeAndEndorseTask(String beanName) {
    super(beanName);
  }

  public void run() {
    try {
      Integer startSize = 0;
      Boolean isRun = true;
      while (isRun) {

        List<KeywordIdentification> currentEndorsedInfo = noticeBeEndorsedService.getCurrentEndorsedInfo(startSize);
        // 将前一天生成的记录同步到状态表
        for (KeywordIdentification Id : currentEndorsedInfo) {
          noticeBeEndorsedService.savetoStatus(Id);
        }

        List<Long> psnIdList = noticeBeEndorsedService.getBeEndorsedPsnId(startSize);

        if (CollectionUtils.isNotEmpty(psnIdList)) {
          for (Long psnId : psnIdList) {
            try {
              noticeBeEndorsedService.sendEmail(psnId);
            } catch (Exception e) {
              logger.error("赞研究领域邮件发送失败，人员id是" + psnId, e);
            }
          }
        } else {
          isRun = false;
        }
        startSize++;
      }

    } catch (Exception e) {
      logger.error("赞研究领域邮件发送失败", e);
    }

  }

}
