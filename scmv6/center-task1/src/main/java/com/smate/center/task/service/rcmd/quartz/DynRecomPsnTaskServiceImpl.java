package com.smate.center.task.service.rcmd.quartz;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.smate.center.task.dao.rcmd.quartz.DynRecommendPsnTaskDao;
import com.smate.center.task.model.rcmd.quartz.DynRecommendPsnTask;

/**
 * 动态推荐人员服务实现.
 * 
 * @author zjh
 *
 */
@Service("dynRecomPsnTaskService")
@Transactional(rollbackFor = Exception.class)
public class DynRecomPsnTaskServiceImpl implements DynRecomPsnTaskService {
  @Autowired
  private DynRecommendPsnTaskDao dynRecommendPsnTaskDao;

  @Override
  public void saveDynRePsnTask(Long psnId, int dynReType) {
    DynRecommendPsnTask rePsnTask = dynRecommendPsnTaskDao.getDynRecommendPsnTask(psnId, dynReType);
    if (rePsnTask == null) {
      rePsnTask = new DynRecommendPsnTask();
      rePsnTask.setPsnId(psnId);
      rePsnTask.setRecomType(dynReType);
    }
    rePsnTask.setStatus(0);// 推荐状态0-待更新；1-已更新.
    dynRecommendPsnTaskDao.save(rePsnTask);

  }

}
