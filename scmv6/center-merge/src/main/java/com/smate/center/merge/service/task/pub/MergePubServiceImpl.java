package com.smate.center.merge.service.task.pub;

import com.smate.center.merge.service.task.MergeBaseService;
import com.smate.center.merge.service.task.MergeService;
import org.apache.commons.collections4.CollectionUtils;

import java.util.List;

/**
 * 成果合并入口.
 * 
 * @author tsz
 *
 * @date 2018年9月11日
 */
public class MergePubServiceImpl extends MergeBaseService {
  private List<MergeService> mergePubDealList;

  public List<MergeService> getMergePubDealList() {
    return mergePubDealList;
  }

  public void setMergePubDealList(List<MergeService> mergePubDealList) {
    this.mergePubDealList = mergePubDealList;
  }

  @Override
  public boolean checkRun(Long savePsnId, Long delPsnId) throws Exception {
    if (CollectionUtils.isNotEmpty(mergePubDealList)) {
      return true;
    }
    return false;
  }

  @Override
  public boolean dealMerge(Long savePsnId, Long delPsnId) throws Exception {
    try {
      for (MergeService mergeService : mergePubDealList) {
        mergeService.runMerge(savePsnId, delPsnId);
      }
    } catch (Exception e) {
      logger.error("帐号合并-合并成果  savePsnId=" + savePsnId + ",delPsnId=" + delPsnId, e);
      throw new Exception("帐号合并-合并成果  savePsnId=" + savePsnId + ",delPsnId=" + delPsnId, e);
    }
    return true;
  }
}
