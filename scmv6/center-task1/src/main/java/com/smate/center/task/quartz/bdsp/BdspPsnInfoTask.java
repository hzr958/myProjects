package com.smate.center.task.quartz.bdsp;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.smate.center.task.base.TaskAbstract;
import com.smate.center.task.exception.TaskException;
import com.smate.center.task.model.bdsp.BdspDataConstant;
import com.smate.center.task.service.bdspimp.BdspBuildDataService;
import com.smate.core.base.utils.model.security.Person;

/**
 * 科研人员任务
 * 
 * @author zzx
 *
 */
public class BdspPsnInfoTask extends TaskAbstract {
  protected final Logger logger = LoggerFactory.getLogger(getClass());
  private final static Integer SIZE = 520; // 每次刷新获取的个数
  @Autowired
  private BdspBuildDataService bdspBuildDataService;

  public BdspPsnInfoTask() {}

  public BdspPsnInfoTask(String beanName) {
    super(beanName);
  }

  public void handle() {
    if (!super.isAllowExecution()) {
      logger.info("=========BdspPsnInfoTask 已关闭==========");
      return;
    }
    List<Person> list = bdspBuildDataService.psnList(SIZE);
    if (list != null && list.size() > 0) {
      for (Person one : list) {
        Integer status = 1;
        String msg = "执行成功";
        try {
          bdspBuildDataService.handlePsnInfo(one);
        } catch (Exception e) {
          // 吃掉异常
          logger.error("Bdsp科研人员任务任务出错，psnId=" + one.getPersonId(), e);
          status = 2;
          msg = e.getMessage();
        }
        bdspBuildDataService.updateLogRecord(status, msg, one.getPersonId(), BdspDataConstant.dataType_psn);
      }
    } else {
      try {
        super.closeOneTimeTask();
      } catch (TaskException e) {
      }
    }

  }

}
