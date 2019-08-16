package com.smate.center.batch.process.pub;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.Assert;

import com.smate.center.batch.chain.pub.pdwh.PdwhPubHandleTask;
import com.smate.center.batch.model.pdwh.pubimport.PdwhPubImportContext;
import com.smate.center.batch.model.pdwh.pubimport.PdwhPublication;

public class PdwhPubImportSaveNewProcess {

  private Logger logger = LoggerFactory.getLogger(getClass());
  List<PdwhPubHandleTask> tasks;

  public List<PdwhPubHandleTask> getTasks() {
    return tasks;
  }

  public void setTasks(List<PdwhPubHandleTask> tasks) {
    Assert.notNull(tasks);
    Assert.notEmpty(tasks);
    this.tasks = tasks;
  }

  public void start(PdwhPublication pdwhPub, PdwhPubImportContext context) throws Exception {
    for (PdwhPubHandleTask task : tasks) {
      if (task.can(pdwhPub, context)) {
        try {
          task.run(pdwhPub, context);
        } catch (Exception e) {
          logger.error("PdwhPubImportSaveNewProcess运行出错，错误taskName : " + task.getName(), e);
          throw new Exception(e);
        }
      }
    }

  }

}
