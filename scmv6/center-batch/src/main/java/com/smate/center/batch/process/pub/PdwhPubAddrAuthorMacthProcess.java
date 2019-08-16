package com.smate.center.batch.process.pub;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.Assert;

import com.smate.center.batch.chain.pub.pdwh.match.PdwhPubMatchHandleTask;
import com.smate.web.v8pub.dom.pdwh.PubPdwhDetailDOM;

/**
 * 基准库成果地址和作者信息匹配任务处理链
 * 
 * @author LIJUN
 * @date 2018年3月19日
 */
public class PdwhPubAddrAuthorMacthProcess {
  private Logger logger = LoggerFactory.getLogger(getClass());
  List<PdwhPubMatchHandleTask> tasks;

  public List<PdwhPubMatchHandleTask> getTasks() {
    Assert.notNull(tasks);
    Assert.notEmpty(tasks);
    return tasks;
  }

  public void setTasks(List<PdwhPubMatchHandleTask> tasks) {
    this.tasks = tasks;
  }

  public void start(PubPdwhDetailDOM pdwhPub, String context) throws Exception {
    for (PdwhPubMatchHandleTask task : tasks) {
      if (task.can(pdwhPub, context)) {
        try {
          task.run(pdwhPub, context);
        } catch (Exception e) {
          logger.error("PdwhPubAddrAuthorMacthProcess运行出错，错误taskName : " + task.getName(), e);
          throw new Exception(e);
        }

      }
    }
  }
}
