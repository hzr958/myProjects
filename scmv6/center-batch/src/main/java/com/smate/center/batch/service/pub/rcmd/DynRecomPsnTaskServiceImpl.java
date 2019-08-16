package com.smate.center.batch.service.pub.rcmd;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.smate.center.batch.dao.rcmd.pub.DynRecommendPsnTaskDao;
import com.smate.center.batch.exception.pub.ServiceException;
import com.smate.center.batch.model.rcmd.pub.DynRecommendPsnTask;


/**
 * 动态推荐人员服务实现.
 * 
 * @author mjg
 * 
 */
@Service("dynRecomPsnTaskService")
@Transactional(rollbackFor = Exception.class)
public class DynRecomPsnTaskServiceImpl implements DynRecomPsnTaskService {

  @Autowired
  private DynRecommendPsnTaskDao dynRecommendPsnTaskDao;

  /**
   * 获取指定数量的待刷新人员.
   * 
   * @param maxSize
   * @author xys
   * 
   * @return
   */
  public List<DynRecommendPsnTask> getPsnsNeedRefresh(int maxSize) {
    return dynRecommendPsnTaskDao.getPsnsNeedRefresh(maxSize);
  }

  /**
   * 保存推荐动态人员数据.
   * 
   * @param psnId
   * @param dynReType
   */
  public void saveDynRePsnTask(Long psnId, int dynReType) {
    DynRecommendPsnTask rePsnTask = dynRecommendPsnTaskDao.getDynRecommendPsnTask(psnId, dynReType);
    if (rePsnTask == null) {
      rePsnTask = new DynRecommendPsnTask();
      rePsnTask.setPsnId(psnId);
      rePsnTask.setRecomType(dynReType);
    }
    rePsnTask.setStatus(0);// 推荐状态0-待更新；1-已更新.
    dynRecommendPsnTaskDao.saveDynRecommendPsnTask(rePsnTask);
  }

  @Override
  public void saveDynRePsnTask(DynRecommendPsnTask dynRcmdPsnTask) throws ServiceException {
    dynRecommendPsnTaskDao.saveDynRecommendPsnTask(dynRcmdPsnTask);
  }
}
