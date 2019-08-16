package com.smate.center.task.single.process.pub;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.util.Assert;

import com.smate.center.task.single.service.pub.IPubXmlTask;

/**
 * base XML处理过程.
 * 
 * @author liqinghua
 * 
 */
public abstract class BasePubXmlProcess implements IPubXmlProcess {

  private Map<String, IPubXmlTask> tasksMap = null;

  @Override
  public void setTasks(List<IPubXmlTask> tasks) {

    Assert.notNull(tasks);
    Assert.notEmpty(tasks);
    this.tasksMap = new HashMap<String, IPubXmlTask>();
    for (IPubXmlTask task : tasks) {
      tasksMap.put(task.getName(), task);
    }
  }

  @Override
  public IPubXmlTask getPubXmlTask(String name) {
    if (this.tasksMap == null) {
      return null;
    }
    return this.tasksMap.get(name);
  }

}
