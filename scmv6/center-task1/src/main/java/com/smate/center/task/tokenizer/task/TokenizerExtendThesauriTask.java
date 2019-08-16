package com.smate.center.task.tokenizer.task;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.smate.center.task.base.TaskAbstract;
import com.smate.center.task.exception.TaskException;
import com.smate.center.task.service.tokenizer.TokenizerThesauriService;

/**
 * 分词器扩展词库任务
 * 
 * @author zk
 *
 */
public class TokenizerExtendThesauriTask extends TaskAbstract {

  protected Logger logger = LoggerFactory.getLogger(getClass());
  @Autowired
  private TokenizerThesauriService tokenizerThesauriService;

  public TokenizerExtendThesauriTask() {}

  public TokenizerExtendThesauriTask(String beanName) {
    super(beanName);
  }

  /**
   * 入口
   */
  public void run() {
    try {
      if (!super.isAllowExecution()) {
        return;
      }
      tokenizerThesauriService.createThesauri();
      closeOneTimeTask();
    } catch (TaskException e) {
      e.printStackTrace();
    }
  }
}
