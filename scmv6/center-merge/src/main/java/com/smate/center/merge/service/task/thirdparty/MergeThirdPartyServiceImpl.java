package com.smate.center.merge.service.task.thirdparty;

import com.smate.center.merge.service.task.MergeBaseService;
import com.smate.center.merge.service.task.MergeService;
import org.apache.commons.collections4.CollectionUtils;

import java.util.List;

/**
 * 合并，第三方关系处理(业务系统关联记录,群组关联记录,微信，qq关联记录),群组关联在群组合并处处理.
 * 
 * @author tsz
 *
 * @date 2018年9月11日
 */
public class MergeThirdPartyServiceImpl extends MergeBaseService {
  private List<MergeService> mergeThirdPartyDealList;

  public List<MergeService> getMergeThirdPartyDealList() {
    return mergeThirdPartyDealList;
  }

  public void setMergeThirdPartyDealList(List<MergeService> mergeThirdPartyDealList) {
    this.mergeThirdPartyDealList = mergeThirdPartyDealList;
  }

  @Override
  public boolean checkRun(Long savePsnId, Long delPsnId) throws Exception {
    if (CollectionUtils.isNotEmpty(mergeThirdPartyDealList)) {
      return true;
    }
    return false;
  }

  @Override
  public boolean dealMerge(Long savePsnId, Long delPsnId) throws Exception {
    try {
      for (MergeService mergeService : mergeThirdPartyDealList) {
        mergeService.runMerge(savePsnId, delPsnId);
      }
    } catch (Exception e) {
      logger.error("帐号合并-第三方关联数据出错  savePsnId=" + savePsnId + ",delPsnId=" + delPsnId, e);
      throw new Exception("帐号合并-第三方关联数据出错  savePsnId=" + savePsnId + ",delPsnId=" + delPsnId, e);
    }
    return true;
  }
}
