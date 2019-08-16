package com.smate.web.management.service.analysis;

import com.smate.web.management.model.analysis.sns.RecommendScore;


/**
 * 基金、论文合作者推荐计算接口.
 * 
 * @author zhuangyanming
 * 
 */
public interface PsnCooperator {
  void handler(RecommendScore rs) throws Exception;
}
