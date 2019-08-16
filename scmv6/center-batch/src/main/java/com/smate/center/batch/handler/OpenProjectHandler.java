package com.smate.center.batch.handler;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.smate.center.batch.chain.prj.OpenProjectBaseChain;
import com.smate.center.batch.context.OpenProjectContext;
import com.smate.center.batch.model.sns.prj.OpenProject;


/**
 * 第三方项目合并处理链控制器 chain control
 * 
 * @author LXZ
 * 
 * @since 6.0.1
 * @version 6.0.1
 */
public class OpenProjectHandler {
  protected Logger logger = LoggerFactory.getLogger(getClass());
  private List<OpenProjectBaseChain> executeChain = null;// 链对象

  /**
   * 执行链处理入口
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
  public OpenProjectContext doHandler(OpenProjectContext context, OpenProject project) throws Exception {
    if (executeChain != null) {
      for (OpenProjectBaseChain chain : executeChain) {
        if (chain.can(context, project)) {
          chain.run(context, project);
        }
      }
    }
    return context;
  }

  public List<OpenProjectBaseChain> getExecuteChain() {
    return executeChain;
  }

  public void setExecuteChain(List<OpenProjectBaseChain> executeChain) {
    this.executeChain = executeChain;
  }
}
