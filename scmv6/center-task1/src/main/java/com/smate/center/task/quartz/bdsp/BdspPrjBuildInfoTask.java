package com.smate.center.task.quartz.bdsp;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.smate.center.task.base.TaskAbstract;
import com.smate.center.task.exception.TaskException;
import com.smate.center.task.model.bdsp.BdspDataConstant;
import com.smate.center.task.model.snsbak.bdsp.BdspProject;
import com.smate.center.task.service.bdspimp.BdspBuildDataService;
import com.smate.core.base.utils.model.security.Person;

/**
 * 项目信息构建
 * 
 * @author zzx
 *
 */
public class BdspPrjBuildInfoTask extends TaskAbstract {
  protected final Logger logger = LoggerFactory.getLogger(getClass());
  private final static Integer SIZE = 520; // 每次刷新获取的个数
  @Autowired
  private BdspBuildDataService bdspBuildDataService;

  public BdspPrjBuildInfoTask() {}

  public BdspPrjBuildInfoTask(String beanName) {
    super(beanName);
  }

  public void handle() {
    if (!super.isAllowExecution()) {
      logger.info("=========BdspPrjBuildInfoTask 已关闭==========");
      return;
    }
    List<BdspProject> list = bdspBuildDataService.prjList(SIZE);
    if (list != null && list.size() > 0) {
      for (BdspProject one : list) {
        Integer status = 1;
        String msg = "执行成功";
        try {
          bdspBuildDataService.handlePrjInfo(one);
        } catch (Exception e) {
          // 吃掉异常
          logger.error("Bdsp项目信息任务任务出错，prjId=" + one.getPrjCode(), e);
          status = 2;
          msg = e.getMessage();
        }
        bdspBuildDataService.updateLogRecord(status, msg, one.getPrjCode(), BdspDataConstant.dataType_prj);
      }
    } else {
      try {
        super.closeOneTimeTask();
      } catch (TaskException e) {
      }
    }

  }

}
