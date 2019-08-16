package com.smate.center.job.framework.cluster.support;

import com.smate.center.job.framework.dto.OfflineJobDTO;
import com.smate.center.job.framework.dto.TaskletDTO;
import java.util.List;

/**
 * 任务分片功能接口，提供自动对离线任务进行分片的功能
 *
 * @author Created by hcj
 * @date 2018/07/12 9:56
 */
public interface JobPartitionable {

  /**
   * 自动对离线任务进行分片
   *
   * @param offlineJobDTO
   * @return 分片后的 {@link TaskletDTO} 的集合
   */
  List<TaskletDTO> autoPartition(final OfflineJobDTO offlineJobDTO);
}
