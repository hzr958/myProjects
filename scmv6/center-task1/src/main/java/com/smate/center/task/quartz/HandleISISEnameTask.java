package com.smate.center.task.quartz;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.smate.center.task.base.TaskAbstract;
import com.smate.center.task.exception.SingleTaskException;
import com.smate.center.task.service.sns.psn.HandleISISEnameService;
import com.smate.core.base.utils.model.security.Person;

/**
 * 处理isis英文名字数据-一次性任务
 * 
 * @author zzx
 *
 */
public class HandleISISEnameTask extends TaskAbstract {
  private final Logger logger = LoggerFactory.getLogger(getClass());
  private static int batchSize = 200;
  @Autowired
  private HandleISISEnameService handleISISEnameService;

  public void doRun() throws SingleTaskException {
    if (!super.isAllowExecution()) {
      return;
    }
    List<Long> list = null;
    List<Person> psnlist = null;
    // 生产机有32825条待处理任务，大概跑170次左右
    Long count = 200L;
    try {
      do {
        list = handleISISEnameService.findList(batchSize);
        if (list != null && list.size() > 0) {
          psnlist = handleISISEnameService.findPsnList(list);
          if (psnlist != null && psnlist.size() > 0) {
            for (Person one : psnlist) {
              try {
                handleISISEnameService.handle(one);
                handleISISEnameService.update(one.getPersonId(), 1, "成功");
              } catch (Exception e) {
                handleISISEnameService.update(one.getPersonId(), 2, "失败");
                logger.error("处理isis英文名字--部分数据出错,psnId=" + one.getPersonId(), e);
                // 吃掉异常
              }
            }
          }
        }
        count--;
      } while (list != null && list.size() > 0 && count > 0);
      super.closeOneTimeTask();
    } catch (Exception e) {
      logger.error("处理isis英文名字数据出错", e);
    }

  }

  public HandleISISEnameTask() {
    super();
  }

  public HandleISISEnameTask(String beanName) {
    super(beanName);
  }

}
