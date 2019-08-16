package com.smate.web.management.service.analysis;

import java.util.Map;

import com.smate.web.management.model.analysis.sns.RecommendScore;

/**
 * 
 * 基金、论文合作者推荐：遍历所有符合必要条件的人员接口.
 * 
 * @author zhuangyanming
 * 
 */
public interface PsnCooperatorTraversal {


  /**
   * 可能合作者推荐计算.
   * 
   * @param psnId
   * @param status
   * @throws Exception
   */
  void psnCooperatorRun(Long psnId, Integer status) throws Exception;

  /**
   * 可能合作者推荐计算(关键词专用)
   * 
   * @param psnId
   * @param rsMap
   * @throws Exception
   */
  boolean psnCooperatorRun(Long psnId, Map<Long, RecommendScore> rsMap) throws Exception;

}
