package com.smate.center.job.framework.dto;

import com.smate.center.job.common.enums.JobTypeEnum;
import com.smate.center.job.common.po.OnlineJobPO;

/**
 * 在线任务数据传输模型
 *
 * @author houchuanjie
 * @date 2018年4月24日 下午4:49:11
 */
public class OnlineJobDTO extends BaseJobDTO {

  private static final long serialVersionUID = 4489821789280553813L;

  public OnlineJobDTO() {
    this.setJobType(JobTypeEnum.ONLINE);
  }

  public OnlineJobDTO(OnlineJobPO onlineJobPO) {
    this.setId(onlineJobPO.getId());
    this.setJobName(onlineJobPO.getJobName());
    this.setStatus(onlineJobPO.getStatus());
    this.setPriority(onlineJobPO.getPriority());
    this.setDataMap(onlineJobPO.getDataMap());
    this.setJobType(JobTypeEnum.ONLINE);
  }


  @Override
  public String toString() {
    return "OnlineJobDTO{" +
        "id='" + id + '\'' +
        ", jobName='" + jobName + '\'' +
        ", priority=" + priority +
        ", status=" + status +
        ", jobType=" + jobType +
        ", dataMap='" + dataMap + '\'' +
        ", errMsg='" + errMsg + '\'' +
        ", startTime=" + startTime +
        ", elapsedTime=" + elapsedTime +
        ", gmtCreate=" + gmtCreate +
        ", gmtModified=" + gmtModified +
        '}';
  }
}
