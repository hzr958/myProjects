package com.smate.center.task.quartz.pdwh;

import java.util.List;

import org.apache.commons.collections4.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.smate.center.task.base.TaskAbstract;
import com.smate.center.task.exception.SingleTaskException;
import com.smate.center.task.model.rcmd.quartz.PubConfirmRolPub;
import com.smate.center.task.service.pdwh.quartz.RenamePdwhFulltextFileService;
import com.smate.center.task.service.pdwh.quartz.UpdateConfirmPubBriefService;
import com.smate.center.task.sys.quartz.service.QuartzCronExpressionService;
import com.smate.core.base.utils.cache.CacheService;

/**
 * 更新成果指派表PUB_CONFIRM_ROLPUB的brief字段
 *
 */
@Deprecated
public class UpdateConfirmPubBriefTask extends TaskAbstract {
  private final Logger logger = LoggerFactory.getLogger(getClass());
  private final static Integer SIZE = 5000; // 每次处理的个数
  private final static String TRIGGERNAME = "UpdateConfirmPubBriefTaskTrigger";
  @Autowired
  private UpdateConfirmPubBriefService updateConfirmPubBriefService;
  @Autowired
  private CacheService cacheService;
  public static String INDEX_TYPE_PUB = "pub_confirm_index";
  @Autowired
  private QuartzCronExpressionService quartzCronExpressionService;

  public UpdateConfirmPubBriefTask() {
    super();
  }

  public UpdateConfirmPubBriefTask(String beanName) {
    super(beanName);
  }

  @Autowired
  RenamePdwhFulltextFileService renamePdwhFulltextFileService;

  public void doRun() throws SingleTaskException {
    try {
      if (quartzCronExpressionService.getQuartzCronExpression(TRIGGERNAME).getStatus() == 2) {
        this.cacheService.put(INDEX_TYPE_PUB, 60 * 60 * 24, "confirm_pub_endId", 0L);
      }
    } catch (Exception e1) {
      e1.printStackTrace();
    }
    if (!super.isAllowExecution()) {
      logger.info("=========UpdateConfirmPubBriefTask已关闭==========");
      return;
    }

    try {
      Long lastId = (Long) cacheService.get(INDEX_TYPE_PUB, "confirm_pub_endId");
      if (lastId == null && quartzCronExpressionService.getQuartzCronExpression(TRIGGERNAME).getStatus() == 2) {
        lastId = 0L;
      }

      List<PubConfirmRolPub> pubList = updateConfirmPubBriefService.getUpdatePubIdList(SIZE, lastId);
      if (CollectionUtils.isEmpty(pubList)) {
        quartzCronExpressionService.toStopScheduleJob(TRIGGERNAME);
        return;
      }
      updateConfirmPubBriefService.updatePubConfirmBrie(pubList);
      /*
       * UpdateSnsPubBriefSubTask subTask = new UpdateSnsPubBriefSubTask(pubList); Thread t = new
       * Thread(subTask); t.start();
       */
      lastId = pubList.get(pubList.size() - 1).getRolPubId();
      this.cacheService.put(INDEX_TYPE_PUB, 60 * 60 * 24, "confirm_pub_endId", lastId);
    } catch (Exception e) {
      logger.error("更新个人库成果的brief_desc字段线程出错", e);
    }
  }
}
