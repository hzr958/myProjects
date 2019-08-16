package com.smate.center.task.service.sns.quartz;

import java.util.List;
/**
 * isis注册后台任务接口
 */
import com.smate.center.task.model.sns.psn.RegisterIsisPersonTmp;

/**
 * 
 * @author zzx
 *
 */
public interface RegisterIsisPsnService {
  /**
   * 获取要注册的人员列表
   * 
   * @param batchSize
   * @return
   * @throws Exception
   */
  List<RegisterIsisPersonTmp> getList(int batchSize);

  /**
   * 执行注册流程
   * 
   * @param r
   * @throws Exception
   */
  void doRegister(RegisterIsisPersonTmp r) throws Exception;

  /**
   * 保存记录
   * 
   * @param r
   */
  void save(RegisterIsisPersonTmp r);

}
