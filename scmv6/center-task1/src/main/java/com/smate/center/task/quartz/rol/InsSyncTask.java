package com.smate.center.task.quartz.rol;

import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.smate.center.task.base.TaskAbstract;
import com.smate.center.task.model.bpo.TmpInsInfo;
import com.smate.center.task.service.rol.quartz.InsSyncServiceImpl;

/**
 * 单位同步任务
 * 
 * @author hd
 *
 */
public class InsSyncTask extends TaskAbstract {

  Logger logger = LoggerFactory.getLogger(getClass());
  private static int batchSize = 100;
  @Autowired
  private InsSyncServiceImpl insSyncService;

  public InsSyncTask() {
    super();
  }

  public InsSyncTask(String beanName) {
    super(beanName);
  }


  public void doRun() throws Exception {
    if (!super.isAllowExecution()) {
      return;
    }
    while (true) {
      List<TmpInsInfo> insList = insSyncService.findSyncInsList(batchSize);
      if (insList == null || insList.size() == 0) {
        return;
      }
      for (TmpInsInfo tmpInsInfo : insList) {
        try {
          insSyncService.syncNsfcIns(tmpInsInfo);
          // 成功
          tmpInsInfo.setSynFlag(1L);
          insSyncService.updateTmpInsInfo(tmpInsInfo);
        } catch (Exception e) {
          tmpInsInfo.setSynFlag(2L);
          tmpInsInfo.setSynMsg(StringUtils.substring(e.getMessage(), 0, 2000));
          // 失败
          insSyncService.updateTmpInsInfo(tmpInsInfo);
          logger.error("单位同步任务出错", e);
        }
      }
    }


  }

}
