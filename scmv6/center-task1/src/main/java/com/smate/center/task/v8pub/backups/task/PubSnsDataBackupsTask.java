package com.smate.center.task.v8pub.backups.task;

import java.util.List;
import org.apache.commons.collections4.CollectionUtils;
import org.codehaus.plexus.util.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import com.smate.center.task.base.TaskAbstract;
import com.smate.center.task.v8pub.backups.model.PubDataBackups;
import com.smate.center.task.v8pub.backups.service.PubBackupsService;

public class PubSnsDataBackupsTask extends TaskAbstract {

  protected Logger logger = LoggerFactory.getLogger(getClass());

  private Integer SIZE = 500;

  @Autowired
  private PubBackupsService pubBackupsService;

  public PubSnsDataBackupsTask() {
    super();
  }

  public PubSnsDataBackupsTask(String beanName) {
    super(beanName);
  }

  public void doRun() {
    try {
      if (!super.isAllowExecution()) {
        return;
      }
      while (true) {
        // 0 为个人库
        List<PubDataBackups> pubList = pubBackupsService.findPubNeedDeal(SIZE, 0);
        if (CollectionUtils.isNotEmpty(pubList)) {
          for (PubDataBackups pubData : pubList) {
            try {
              pubBackupsService.backupSnsData(pubData);
            } catch (Exception e) {
              logger.error("备份SNS库成果数据失败！pubId={}", pubData.getPubId(), e);
              pubData.setErrorMsg(StringUtils.substring(e.getMessage(), 0, 500));
              pubData.setStatus(99);
              pubBackupsService.save(pubData);
            }
          }
        } else {
          break;
        }
      }
    } catch (Exception e) {
      logger.error("PubSnsDataBackupsTask运行异常", e);
    }

  }
}
