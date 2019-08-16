package com.smate.center.job.framework.cluster.support;

import com.smate.center.job.common.enums.JobStatusEnum;
import com.smate.center.job.common.enums.JobTypeEnum;
import com.smate.center.job.framework.dto.BaseJobDTO;
import com.smate.center.job.framework.dto.OfflineJobDTO;
import com.smate.center.job.framework.dto.OnlineJobDTO;
import com.smate.center.job.framework.dto.TaskletDTO;
import com.smate.center.job.framework.service.OfflineJobService;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.Resource;
import org.apache.commons.collections4.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * 任务加工处理器
 *
 * @author houchuanjie
 * @date 2018/04/11 14:30
 */
@Component
public class SimpleJobProcessor implements JobProcessable {

  private Logger logger = LoggerFactory.getLogger(getClass());
  @Autowired
  private OfflineJobService offlineJobService;
  @Resource
  private JobPartitionable jobPartitioner;

  @Override
  public <T extends BaseJobDTO> List<TaskletDTO> process(T jobInfo) {
    if (logger.isDebugEnabled()) {
      logger.debug("正在加工任务信息...{}", jobInfo);
    }
    List<TaskletDTO> list;
    if (jobInfo.getJobType() == JobTypeEnum.OFFLINE) {
      if (logger.isDebugEnabled()) {
        logger.debug("该任务是离线任务，正在进行任务分片...");
      }
      OfflineJobDTO offlineJobDTO = (OfflineJobDTO) jobInfo;
      list = jobPartitioner.autoPartition(offlineJobDTO);
      if (logger.isDebugEnabled()) {
        logger.debug("自动分片完毕！分片数：{}", list.size());
      }
      if (CollectionUtils.isEmpty(list)) {
        logger.error("离线任务（id='{}'）{}在[{}, {})区间内没有需要处理的记录！", offlineJobDTO.getId(),
            offlineJobDTO.getUniqueKey(), offlineJobDTO.getBegin(), offlineJobDTO.getEnd());
        offlineJobDTO.setStatus(JobStatusEnum.FAILED);
        offlineJobDTO.setErrMsg(
            offlineJobDTO.getUniqueKey() + "在[" + offlineJobDTO.getBegin() + ", " + offlineJobDTO
                .getEnd() + ")区间内没有需要处理的记录！");
        offlineJobService.updateJobInfo(offlineJobDTO);
      }
      return list;
    }
    // 在线任务对象转化模型
    list = new ArrayList<>(1);
    TaskletDTO taskletDTO = new TaskletDTO((OnlineJobDTO) jobInfo, Instant.now().toEpochMilli());
    taskletDTO.setThreadNo(1);
    list.add(taskletDTO);
    if (logger.isDebugEnabled()) {
      logger.debug("该任务是在线任务，任务转换完毕！");
    }
    return list;
  }
}
