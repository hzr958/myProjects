package com.smate.center.task.quartz;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.smate.center.task.base.TaskAbstract;
import com.smate.center.task.exception.SingleTaskException;
import com.smate.center.task.exception.TaskException;
import com.smate.center.task.model.pdwh.pub.JxkjtPrpInfo;
import com.smate.center.task.model.pdwh.pub.JxkjtPrpInfoTemp;
import com.smate.center.task.model.pdwh.pub.JxstcPrpInfo;
import com.smate.center.task.model.pdwh.pub.JxstcPrpInfoTemp;
import com.smate.center.task.service.sns.quartz.JxstcPrpInfoIndexService;

/**
 * 读取表JxstcPrpInfo数据到sorl任务
 * 
 * @author zzx
 *
 */
public class JxstcPrpInfoIndexTask extends TaskAbstract {
  private final Logger logger = LoggerFactory.getLogger(getClass());
  private static int batchSize = 200;
  @Autowired
  private JxstcPrpInfoIndexService jxstcPrpInfoIndexService;

  public void doRun() throws SingleTaskException {
    if (!super.isAllowExecution()) {
      return;
    }
    List<JxstcPrpInfo> list = null;
    List<JxkjtPrpInfo> list2 = null;
    boolean status = true;
    Long count = 0L;
    do {
      count++;
      try {
        list = jxstcPrpInfoIndexService.findList(batchSize);
        list2 = jxstcPrpInfoIndexService.findList2(batchSize);
      } catch (Exception e) {
        logger.error("读取表JxstcPrpInfo数据到sorl任务出错", e);
      }
      if ((list == null || list.size() <= 0) && (list2 == null || list2.size() <= 0)) {
        status = false;
        try {
          super.closeOneTimeTask();
        } catch (TaskException e) {
          logger.error("关闭JxstcPrpInfoIndexTask出错！", e);
        }
      } else {
        if (list != null && list.size() > 0) {
          for (JxstcPrpInfo one : list) {
            try {
              JxstcPrpInfoTemp jxstcPrpInfoTemp = jxstcPrpInfoIndexService.dohandle(one);
              jxstcPrpInfoIndexService.saveJxstcPrpInfoTemp(jxstcPrpInfoTemp);
            } catch (Exception e) {
              logger.error("读取表JxstcPrpInfo数据到sorl任务出错", e);
            }
          }
        }
        if (list2 != null && list2.size() > 0) {
          for (JxkjtPrpInfo one : list2) {
            try {
              JxkjtPrpInfoTemp JxkjtPrpInfoTemp = jxstcPrpInfoIndexService.dohandle2(one);
              jxstcPrpInfoIndexService.saveJxkjtPrpInfoTemp(JxkjtPrpInfoTemp);
            } catch (Exception e) {
              logger.error("读取表JxkjtPrpInfo数据到sorl任务出错", e);
            }
          }
        }

      }
    } while (status && count < 77777);
  }


  public JxstcPrpInfoIndexTask() {
    super();
  }

  public JxstcPrpInfoIndexTask(String beanName) {
    super(beanName);
  }
}
