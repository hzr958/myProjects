package com.smate.center.task.quartz.bdsp;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.smate.center.task.base.TaskAbstract;
import com.smate.center.task.exception.TaskException;
import com.smate.center.task.model.bdsp.BdspInterfacePushDataForm;
import com.smate.center.task.service.bdspimp.BdspInterfacePushDataService;

/**
 * bdsp接口推送数据任务--SCMAPP-578
 * 
 * @author zzx
 *
 */
public class BdspInterfacePushDataTask extends TaskAbstract {
  protected final Logger logger = LoggerFactory.getLogger(getClass());
  private BdspInterfacePushDataForm form;
  @Autowired
  private BdspInterfacePushDataService bdspInterfacePushDataService;

  public BdspInterfacePushDataTask() {}

  public BdspInterfacePushDataTask(String beanName) {
    super(beanName);
  }

  public void handle() {
    if (!super.isAllowExecution()) {
      logger.info("=========bdsp接口推送数据任务 BdspInterfacePushDataTask 已关闭==========");
      return;
    }
    // 获取更新数据并更新到日志表待推送
    long a1 = System.currentTimeMillis();
    bdspInterfacePushDataService.findUpdatePsnData(form);
    bdspInterfacePushDataService.findUpdatePrjData(form);
    bdspInterfacePushDataService.findUpdatePaperData(form);
    bdspInterfacePushDataService.findUpdatePatentData(form);
    long a2 = System.currentTimeMillis();
    do {
      try {
        form = new BdspInterfacePushDataForm();
        // 获取需要更新的数据的类型-自定义更新的类型-APP_QUARTZ_SETTING-bdspUpdateDataType
        bdspInterfacePushDataService.findUpdateDataType(form);
        // 数据更新效验-是否有更新的数据-构建推送格式
        long a = System.currentTimeMillis();
        bdspInterfacePushDataService.checkDataHasUpdate(form);
        long b = System.currentTimeMillis();
        // 数据更新推送-解析接口返回结果
        bdspInterfacePushDataService.pushData(form);
        long c = System.currentTimeMillis();
        // 保存psn推送记录
        bdspInterfacePushDataService.savePushPsnLog(form);
        // 保存prj推送记录
        bdspInterfacePushDataService.savePushPrjLog(form);
        // 保存paper推送记录
        bdspInterfacePushDataService.savePushPaperLog(form);
        // 保存patent推送记录
        bdspInterfacePushDataService.savePushPatentLog(form);
        long g = System.currentTimeMillis();
        logger.error("-----------------------------------------------------------构建需要推送的数据到日记表的时间=" + (a2 - a1));
        logger.error("-----------------------------------------------------------构建推送格式花费的时间=" + (b - a));
        logger.error("-----------------------------------------------------------推送数据并解析结果花费的时间=" + (c - b));
        logger.error("-----------------------------------------------------------保存推送记录花费的时间=" + (g - c));
      } catch (Exception e) {
        // 报错会关闭任务-要检查错误原因才继续推送
        logger.error("BdspInterfacePushDataTask任务出错-已暂时关闭-请及时处理---", e);
        bdspInterfacePushDataService.closeTask();
        try {
          super.closeOneTimeTask();
        } catch (TaskException e1) {
          e1.printStackTrace();
        }
      }
    } while (hasDataToUpdate(form));

  }

  /**
   * 停止任务控制
   * 
   * @param f
   * @return
   */
  private boolean hasDataToUpdate(BdspInterfacePushDataForm f) {
    // 是否还有需要更新的数据
    boolean dataStatus = f.isHasDataPaperToUpdate() || f.isHasDataPatentToUpdate() || f.isHasDataPrjToUpdate()
        || f.isHasDataPsnToUpdate();
    // 自定义控制开关-APP_QUARTZ_SETTING-bdspUpdateDataTaskStatus
    boolean taskStatus = bdspInterfacePushDataService.findTaskStatus();
    return dataStatus && taskStatus;
  }

}
