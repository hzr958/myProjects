package com.smate.center.merge.service.task.psn;

import java.util.List;

import org.apache.commons.collections4.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.smate.center.merge.service.task.MergeBaseService;
import com.smate.center.merge.service.task.MergeService;

/**
 * 个人信息合并处理(入口).
 * 
 * @author tsz
 *
 */
public class MergePsnServiceImpl extends MergeBaseService {
  protected final Logger logger = LoggerFactory.getLogger(getClass());
  // 群组相关合并
  private List<MergeService> mergePsnDealList;

  @Override
  public boolean checkRun(Long savePsnId, Long delPsnId) throws Exception {
    if (CollectionUtils.isNotEmpty(mergePsnDealList)) {
      return true;
    }
    return false;
  }

  /**
   * 处理相关合并.
   */
  @Override
  public boolean dealMerge(Long savePsnId, Long delPsnId) throws Exception {
    try {
      // 循环处理
      for (MergeService mergeService : mergePsnDealList) {
        mergeService.runMerge(savePsnId, delPsnId);
      }
    } catch (Exception e) {
      logger.error("帐号合并-人员信息合并出错  savePsnId=" + savePsnId + ",delPsnId=" + delPsnId, e);
      throw new Exception("帐号合并-人员信息合并出错  savePsnId=" + savePsnId + ",delPsnId=" + delPsnId, e);
    }
    return true;
  }

  public List<MergeService> getMergePsnDealList() {
    return mergePsnDealList;
  }

  public void setMergePsnDealList(List<MergeService> mergePsnDealList) {
    this.mergePsnDealList = mergePsnDealList;
  }
}
