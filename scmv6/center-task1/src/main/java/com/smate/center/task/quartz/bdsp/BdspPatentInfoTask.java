package com.smate.center.task.quartz.bdsp;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.smate.center.task.base.TaskAbstract;
import com.smate.center.task.exception.TaskException;
import com.smate.center.task.model.bdsp.BdspDataConstant;
import com.smate.center.task.model.pdwh.quartz.PdwhPublication;
import com.smate.center.task.model.snsbak.bdsp.BdspProject;
import com.smate.center.task.service.bdspimp.BdspBuildDataService;
import com.smate.web.v8pub.dom.pdwh.PubPdwhDetailDOM;

/**
 * 专利数据 5
 * 
 * @author zzx
 *
 */
public class BdspPatentInfoTask extends TaskAbstract {
  protected final Logger logger = LoggerFactory.getLogger(getClass());
  private final static Integer SIZE = 520; // 每次刷新获取的个数
  @Autowired
  private BdspBuildDataService bdspBuildDataService;

  public BdspPatentInfoTask() {}

  public BdspPatentInfoTask(String beanName) {
    super(beanName);
  }

  public void handle() {
    if (!super.isAllowExecution()) {
      logger.info("=========BdspPatentInfoTask 已关闭==========");
      return;
    }
    List<PubPdwhDetailDOM> list = bdspBuildDataService.patentList(SIZE);
    if (list != null && list.size() > 0) {
      for (PubPdwhDetailDOM one : list) {
        Integer status = 1;
        String msg = "执行成功";
        try {
          bdspBuildDataService.handlePatentInfo(one);
        } catch (Exception e) {
          // 吃掉异常
          logger.error("Bdsp专利数据任务任务出错，pubId=" + one.getPubId(), e);
          status = 2;
          msg = e.getMessage();
        }
        bdspBuildDataService.updateLogRecord(status, msg, one.getPubId(), BdspDataConstant.dataType_patent);
      }
    } else {
      try {
        super.closeOneTimeTask();
      } catch (TaskException e) {
      }
    }

  }

}
