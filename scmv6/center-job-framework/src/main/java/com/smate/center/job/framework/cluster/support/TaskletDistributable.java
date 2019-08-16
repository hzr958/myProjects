package com.smate.center.job.framework.cluster.support;

import com.smate.center.job.framework.dto.TaskletDTO;
import java.util.List;

/**
 * 任务分发器统一接口
 *
 * @author houchuanjie
 * @date 2018/04/12 11:42
 */
public interface TaskletDistributable {

  /**
   * 任务分派到各个服务器节点
   *
   * @param list 经过加工需要分配至节点的小任务列表
   */
  void distribute(List<TaskletDTO> list);
}
