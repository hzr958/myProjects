package com.smate.center.job.framework.support;

import com.smate.center.job.framework.dto.TaskletDTO;
import com.smate.center.job.framework.support.AbstractMediator;
import com.smate.center.job.framework.support.TaskletQueueColleague;
import org.springframework.stereotype.Component;

/**
 * 子任务队列的中介者，持有各节点的子任务队列，用于主调度给各节点分发子任务
 *
 * @author Created by hcj
 * @date 2018/07/23 10:20
 */
@Component
public class TaskletMediator extends AbstractMediator<TaskletQueueColleague, TaskletDTO> {

  /**
   * 向指定节点名称的服务器主动提供任务
   *
   * @param who 指定的服务器节点名称
   * @param taskletDTO 任务信息
   * @return 成功返回true，失败返回false
   */
  public boolean offer(String who, TaskletDTO taskletDTO) {
    return notify(who, taskletDTO);
  }
}
