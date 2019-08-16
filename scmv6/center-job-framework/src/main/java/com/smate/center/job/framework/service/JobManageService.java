package com.smate.center.job.framework.service;

import com.smate.center.job.web.vo.OfflineJobVO;
import com.sun.istack.internal.NotNull;

/**
 * 任务管理服务接口
 */
public interface JobManageService {

  /**
   * 停止任务执行
   * @param jobId
   * @return
   */
  OfflineJobVO stopJob(@NotNull String jobId) throws IllegalArgumentException;

  /**
   * 启动任务执行
   * @param jobId
   * @return
   */
  OfflineJobVO runJob(@NotNull String jobId);
}
