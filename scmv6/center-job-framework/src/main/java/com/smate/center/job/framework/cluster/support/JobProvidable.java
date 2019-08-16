package com.smate.center.job.framework.cluster.support;

import com.smate.center.job.common.po.BaseJobPO;
import com.smate.center.job.framework.dto.BaseJobDTO;
import com.smate.center.job.framework.dto.OfflineJobDTO;
import com.smate.center.job.framework.dto.OnlineJobDTO;

/**
 * 任务提供器接口
 *
 * @author houchuanjie
 * @date 2018/04/04 16:57
 */
public interface JobProvidable {

  /**
   * 获取一个权重较高、优先级较高的一个任务（有可能是离线任务，也可能是在线任务），此方法会从缓存中移除该对象
   *
   * @return {@link OfflineJobDTO}、{@link OnlineJobDTO}的父类{@link BaseJobDTO}对象，可能为{@code null}
   */
  BaseJobDTO get();

  /**
   * 预先获取一个权重较高、优先级较高的一个任务（有可能是离线任务，也可能是在线任务），仅提供预察看，但不会移 除该对象，再次使用{@link #peek()}或者{@link
   * #get()}将会得到同一个对象
   *
   * @return
   */
  BaseJobDTO peek();

  /**
   * 从缓存中移除某个任务
   *
   * @param jobId 任务id
   * @return 被移除的任务父类对象
   */
  BaseJobPO removeFromCache(String jobId);
}
