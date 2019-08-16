package com.smate.center.task.service.bdspimp;

import com.smate.center.task.model.bdsp.BdspInterfacePushDataForm;

public interface BdspInterfacePushDataService {
  /**
   * 数据更新效验-是否有更新的数据-推送格式构建
   * 
   * @param form
   */
  void checkDataHasUpdate(BdspInterfacePushDataForm form);

  /**
   * 获取需要更新的数据的类型-自定义更新的类型-APP_QUARTZ_SETTING-bdspUpdateDataType
   * 
   * @param form
   */
  void findUpdateDataType(BdspInterfacePushDataForm form);

  /**
   * 数据更新推送-更新推送日志
   * 
   * @param form
   */
  void pushData(BdspInterfacePushDataForm form);

  /**
   * 停止任务控制 1、是否还有需要更新的数据 2、自定义控制开关-APP_QUARTZ_SETTING-bdspUpdateDataTaskStatus
   * 
   * @return
   */
  boolean findTaskStatus();

  /**
   * 保存人员推送记录
   * 
   * @param form
   */
  void savePushPsnLog(BdspInterfacePushDataForm form);

  /**
   * 保存项目推送记录
   * 
   * @param form
   */
  void savePushPrjLog(BdspInterfacePushDataForm form);

  /**
   * 保存论文推送记录
   * 
   * @param form
   */
  void savePushPaperLog(BdspInterfacePushDataForm form);

  /**
   * 保存专利推送记录
   * 
   * @param form
   */
  void savePushPatentLog(BdspInterfacePushDataForm form);

  /**
   * 中止任务
   */
  void closeTask();

  void findUpdatePsnData(BdspInterfacePushDataForm form);

  void findUpdatePrjData(BdspInterfacePushDataForm form);

  void findUpdatePaperData(BdspInterfacePushDataForm form);

  void findUpdatePatentData(BdspInterfacePushDataForm form);

}
