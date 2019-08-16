package com.smate.center.batch.service.projectmerge;

import com.smate.center.batch.model.sns.prj.OpenProject;

/**
 * Open 系统 - 第三方项目合并任务入口
 * 
 * @author LXZ
 * 
 * @since 6.0.1
 * @version 6.0.1
 */
public interface OpenProjectProcessorService {
  public Integer run(OpenProject prj);

}
