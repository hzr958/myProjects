package com.smate.center.job.web.vo;

import com.smate.center.job.common.po.OnlineJobPO;
import java.util.Date;
import org.apache.commons.lang3.builder.ToStringBuilder;

/**
 * 在线任务页面数据模型
 */
public class OnlineJobVO extends BaseJobVO {

  private static final long serialVersionUID = -2237207960950945554L;
  private Date startTime; //执行开始时间
  private Long elapsed;   //执行耗时，ms

  public OnlineJobVO() {
  }

  public OnlineJobVO(OnlineJobPO onlineJobPO) {
    this.setId(onlineJobPO.getId());
    this.setName(onlineJobPO.getJobName());
    this.setStatus(onlineJobPO.getStatus());
    this.setPriority(onlineJobPO.getPriority());
    this.setParamsMap(onlineJobPO.getDataMap());
    this.setCreateTime(onlineJobPO.getGmtCreate());
    this.setModifiedTime(onlineJobPO.getGmtModified());
  }

  public Date getStartTime() {
    return startTime;
  }

  public void setStartTime(Date startTime) {
    this.startTime = startTime;
  }

  public Long getElapsed() {
    return elapsed;
  }

  public void setElapsed(Long elapsed) {
    this.elapsed = elapsed;
  }

  @Override
  public String toString() {
    return new ToStringBuilder(this)
        .append("id", id)
        .append("name", name)
        .append("priority", priority)
        .append("status", status)
        .append("paramsMap", paramsMap)
        .append("errMsg", errMsg)
        .append("createTime", createTime)
        .append("modifiedTime", modifiedTime)
        .append("startTime", startTime)
        .append("elapsed", elapsed)
        .toString();
  }
}
