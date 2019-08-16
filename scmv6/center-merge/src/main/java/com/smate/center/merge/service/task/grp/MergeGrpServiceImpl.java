package com.smate.center.merge.service.task.grp;

import com.smate.center.merge.service.task.MergeBaseService;
import com.smate.center.merge.service.task.MergeService;
import org.apache.commons.collections4.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

/**
 * 群组合并处理类(群组合并入口).
 * 
 * @author tsz
 *
 */
public class MergeGrpServiceImpl extends MergeBaseService {
  protected final Logger logger = LoggerFactory.getLogger(getClass());
  // 群组相关合并
  private List<MergeService> mergeGrpDealList;

  @Override
  public boolean checkRun(Long savePsnId, Long delPsnId) throws Exception {
    if (CollectionUtils.isNotEmpty(mergeGrpDealList)) {
      return true;
    }
    return false;
  }

  /**
   * 处理群组相关合并.
   */
  @Override
  public boolean dealMerge(Long savePsnId, Long delPsnId) throws Exception {
    try {
      // 循环处理群组相关合并
      for (MergeService mergeService : mergeGrpDealList) {
        mergeService.runMerge(savePsnId, delPsnId);
      }
    } catch (Exception e) {
      logger.error("帐号合并-群组合并出错  savePsnId=" + savePsnId + ",delPsnId=" + delPsnId, e);
      throw new Exception("帐号合并-群组合并出错  savePsnId=" + savePsnId + ",delPsnId=" + delPsnId, e);
    }
    return true;
  }

  public List<MergeService> getMergeGrpDealList() {
    return mergeGrpDealList;
  }

  public void setMergeGrpDealList(List<MergeService> mergeGrpDealList) {
    this.mergeGrpDealList = mergeGrpDealList;
  }
}
