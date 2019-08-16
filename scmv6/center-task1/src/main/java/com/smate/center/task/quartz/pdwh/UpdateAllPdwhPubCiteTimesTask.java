package com.smate.center.task.quartz.pdwh;

import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections4.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.smate.center.task.base.TaskAbstract;
import com.smate.center.task.exception.SingleTaskException;
import com.smate.center.task.exception.TaskException;
import com.smate.center.task.service.pdwh.quartz.UpdateAllPdwhPubCiteTimesService;

/**
 * 更新所有pdwh成果引用
 * 
 * @author LJ
 *
 *         2017年7月3日
 */
public class UpdateAllPdwhPubCiteTimesTask extends TaskAbstract {
  private static final int BATCH_SIZE = 3000;
  private final Logger logger = LoggerFactory.getLogger(getClass());

  @Autowired
  private UpdateAllPdwhPubCiteTimesService updateAllPdwhPubCiteTimesService;

  public UpdateAllPdwhPubCiteTimesTask() {
    super();
  }

  public UpdateAllPdwhPubCiteTimesTask(String beanName) {
    super(beanName);
  }

  public void doRun() throws SingleTaskException {
    /**
     * 本任务执行前需要初始化数据到临时任务表（获取pubid保存到临时任务表）,sql eg:
     * <p>
     * insert into scmpdwh.TMP_TASK_INFO_RECORD (job_id,job_type,handle_id)
     * <p>
     * select scmpdwh.SEQ_TMP_TASK_INFO_RECORD.nextval,2,t.pub_id
     * <p>
     * from scmpdwh.pdwh_publication t ;
     * <p>
     */
    if (!super.isAllowExecution()) {
      return;
    }
    List<Long> pdwhPubList = null;

    while (true) {
      try {
        pdwhPubList = updateAllPdwhPubCiteTimesService.getPdwhPubList(BATCH_SIZE);
      } catch (Exception e) {
        logger.error("UpdateAllPdwhPubCiteTimesTask批量获取pdwhpubId出错！", e);
      }

      if (CollectionUtils.isEmpty(pdwhPubList)) {
        logger.info("所有pdwh成果引用更新完毕");
        try {
          super.closeOneTimeTask();
        } catch (TaskException e) {
          logger.error("自动关闭UpdateAllPdwhPubCiteTimesTask任务出错！", e);
        }
        break;
      }

      for (Long pdwhPubId : pdwhPubList) {

        try {
          Map<Integer, Integer> map = updateAllPdwhPubCiteTimesService.handlePdwhCiteTimes(pdwhPubId);
          // 分dbid保存引用记录
          Iterator<Map.Entry<Integer, Integer>> entries = map.entrySet().iterator();
          while (entries.hasNext()) {
            Map.Entry<Integer, Integer> entry = entries.next();
            updateAllPdwhPubCiteTimesService.updateCitedTimes(pdwhPubId, entry.getKey(), entry.getValue());
          }
        } catch (Exception e) {
          logger.error("更新pdwh引用次数出错，pubid:" + pdwhPubId, e);
          updateAllPdwhPubCiteTimesService.updateTaskStatus(pdwhPubId, 2, "更新pdwh引用次数出错");
        }
      }

    }

  }

}
