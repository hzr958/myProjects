package com.smate.center.merge.service.task.del;

import com.smate.center.merge.service.task.MergeBaseService;
import com.smate.center.merge.service.task.MergeService;
import org.apache.commons.collections4.CollectionUtils;

import java.util.List;

/**
 * 删除各种数据处理入口.
 * 
 * @author tsz
 *
 * @date 2018年9月11日
 */
public class MergeDelServiceImpl extends MergeBaseService {
  // 删除数据处理
  private List<MergeService> mergeDelDealList;

  public List<MergeService> getMergeDelDealList() {
    return mergeDelDealList;
  }

  public void setMergeDelDealList(List<MergeService> mergeDelDealList) {
    this.mergeDelDealList = mergeDelDealList;
  }

  @Override
  public boolean checkRun(Long savePsnId, Long delPsnId) throws Exception {
    if (CollectionUtils.isNotEmpty(mergeDelDealList)) {
      return true;
    }
    return false;
  }

  @Override
  public boolean dealMerge(Long savePsnId, Long delPsnId) throws Exception {
    // 删除各种数据
    try {
      for (MergeService mergeService : mergeDelDealList) {
        mergeService.runMerge(savePsnId, delPsnId);
      }
    } catch (Exception e) {
      logger.error("帐号合并-删除数据出错  savePsnId=" + savePsnId + ",delPsnId=" + delPsnId, e);
      throw new Exception("帐号合并-删除数据出错  savePsnId=" + savePsnId + ",delPsnId=" + delPsnId, e);
    }
    return true;
  }
}
