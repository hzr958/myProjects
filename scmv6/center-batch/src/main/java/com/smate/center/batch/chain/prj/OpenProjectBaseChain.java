package com.smate.center.batch.chain.prj;

import com.smate.center.batch.context.OpenProjectContext;
import com.smate.center.batch.model.sns.prj.OpenProject;

/**
 * 第三方项目合并处理链接口定义
 * 
 * @author LXZ
 * 
 * @since 6.0.1
 * @version 6.0.1
 * @throws Exception
 */
public interface OpenProjectBaseChain {

  /**
   * 链入口权限判断
   * 
   * @author LXZ
   * 
   * @since 6.0.1
   * @version 6.0.1
   * @throws Exception
   */
  public boolean can(OpenProjectContext context, OpenProject project);

  /**
   * 链执行逻辑入口
   * 
   * @author LXZ
   * 
   * @since 6.0.1
   * @version 6.0.1
   * @param context
   * @param project
   * @return
   * @throws Exception
   */
  public OpenProjectContext run(OpenProjectContext context, OpenProject project) throws Exception;
}
