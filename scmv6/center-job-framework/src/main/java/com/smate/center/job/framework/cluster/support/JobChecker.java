package com.smate.center.job.framework.cluster.support;

import com.smate.center.job.common.enums.JobStatusEnum;
import com.smate.center.job.framework.dto.BaseJobDTO;
import com.smate.center.job.framework.dto.OfflineJobDTO;
import com.smate.center.job.framework.service.OfflineJobService;
import com.smate.center.job.framework.service.OnlineJobService;
import com.smate.core.base.exception.DAOException;
import com.smate.core.base.utils.string.StringUtils;
import java.text.MessageFormat;
import java.util.Date;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 任务检查器
 *
 * @author houchuanjie
 * @date 2018年4月26日 下午2:29:59
 */
@Service
public class JobChecker implements JobCheckable {

  private Logger logger = LoggerFactory.getLogger(getClass());
  @Autowired
  private OfflineJobService offlineJobService;
  @Autowired
  private OnlineJobService onlineJobService;

  @Override
  public boolean check(BaseJobDTO job) {
    logger.debug("检查任务配置...jobId={}", job.getId());
    job.setStartTime(new Date());
    // 离线任务，需要检查任务配置的数据源、数据表、唯一键列是否存在以及唯一键列是否真的唯一
    switch (job.getJobType()) {
      case OFFLINE: {
        OfflineJobDTO offlineJob = (OfflineJobDTO) job;
        return checkOffline(offlineJob);
      }
      case ONLINE: {
        logger.debug("任务配置检查通过！");
        return true;
      }
      default:
        job.setElapsedTime(0L);
        return false;
    }
  }

  private boolean checkOffline(OfflineJobDTO offlineJob) {
    boolean flag = offlineJobService.hasDbSessionEnum(offlineJob.getDbSessionEnum());
    if (!flag) {
      String errMsg = MessageFormat.format("任务配置检查未通过：指定数据源''{0}''不存在！除非该任务信息正确配置，否则将不会执行！",
          offlineJob.getDbSessionEnum().toString());
      logErrOfflineJob(offlineJob, errMsg);
      return false;
    }
    flag = offlineJobService.hasTable(offlineJob);
    if (!flag) {
      String errMsg = MessageFormat
          .format("任务配置检查未通过：指定表''{0}''不存在！除非该任务信息正确配置，否则将不会执行！", offlineJob.getTableName());
      logErrOfflineJob(offlineJob, errMsg);
      return false;
    }
    flag = offlineJobService.hasUniqueKey(offlineJob);
    if (!flag) {
      String errMsg = MessageFormat
          .format("任务配置检查未通过：列''{0}''不唯一，存在重复数据！除非该任务信息正确配置，否则将不会执行！", offlineJob.getUniqueKey());
      logErrOfflineJob(offlineJob, errMsg);
      return false;
    }
    if (offlineJob.getEnd() - offlineJob.getBegin() <= 0) {
      String errMsg = MessageFormat
          .format("任务配置检查未通过：[{0}, {1})区间值不正确，必须确保end大于begin！", offlineJob.getBegin(),
              offlineJob.getEnd());
      logErrOfflineJob(offlineJob, errMsg);
      return false;
    }
    String filter = offlineJob.getFilter();
    if (StringUtils.isNotBlank(filter)) {
      int where = filter.indexOf("where");
      filter = StringUtils.substring(filter.toLowerCase(), where == -1 ? 0 : where + 5);
      offlineJob.setFilter(filter);
    }
    long recordCount = 0;
    try {
      recordCount = offlineJobService.getRecordCount(offlineJob);
    } catch (DAOException e) {
      String errMsg = "任务配置检查未通过：获取要处理的总记录数时出错！" + e.getMessage();
      logErrOfflineJob(offlineJob, errMsg);
      return false;
    }
    offlineJob.setCount(recordCount);
    if (recordCount <= 0) {
      String errMsg =
          "任务配置检查未通过：在[" + offlineJob.getBegin() + ", " + offlineJob.getEnd() + ")区间内没有要处理的任何记录！";
      logErrOfflineJob(offlineJob, errMsg);
      return false;
    }
    if (offlineJob.getThreadCount() <= 0) {
      String errMsg = "任务配置检查未通过：线程数threadCount至少为1！";
      logErrOfflineJob(offlineJob, errMsg);
      return false;
    }
    //更新任务要处理的记录数
    offlineJobService.updateJobInfo(offlineJob);
    logger.debug("任务配置检查通过！");
    return true;
  }

  private void logErrOfflineJob(OfflineJobDTO offlineJob, String errMsg) {
    logger.error("{}jobId='{}'", errMsg, offlineJob.getId());
    offlineJob.setStatus(JobStatusEnum.FAILED);
    offlineJob.setErrMsg(errMsg);
    offlineJob.setElapsedTime(0L);
    offlineJobService.updateJobInfo(offlineJob);
  }

}
