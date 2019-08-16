package com.smate.center.job.framework.cluster;

import com.smate.center.job.framework.dto.BaseJobDTO;
import com.smate.center.job.framework.dto.OfflineJobDTO;
import com.smate.center.job.framework.dto.TaskletDTO;
import com.smate.center.job.framework.zookeeper.support.CustomDistributedMap;
import java.math.BigDecimal;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * 子任务收集统计类，负责已分配的子任务状态、进度信息统计
 *
 * @author Created by hcj
 * @date 2018/07/09 17:54
 */
public abstract class AbstractSimpleTaskletCollector implements TaskletCollectable {

  protected Logger logger = LoggerFactory.getLogger(getClass());
  /**
   * 集群节点资源管理器
   */
  @Autowired
  protected ClusterResourceManageable resourceManager;
  /**
   * 已分配任务集合
   */
  protected CustomDistributedMap<BaseJobDTO> distributedJobMap;

  /**
   * 计算执行进度百分比
   *
   * @param offlineJobDTO
   * @param list
   */
  protected void computePercentage(final OfflineJobDTO offlineJobDTO, final List<TaskletDTO> list) {
    // 计算任务的所有分片执行总进度
    BigDecimal total = BigDecimal.valueOf(list.stream().mapToDouble(TaskletDTO::getPercent).sum());
    // 计算任务执行百分比
    BigDecimal percent = total
        .divide(BigDecimal.valueOf(offlineJobDTO.getThreadCount()), 3, BigDecimal.ROUND_DOWN);
    offlineJobDTO.setPercent(percent.doubleValue());
  }

  public void setResourceManager(ClusterResourceManageable resourceManager) {
    this.resourceManager = resourceManager;
  }
}
