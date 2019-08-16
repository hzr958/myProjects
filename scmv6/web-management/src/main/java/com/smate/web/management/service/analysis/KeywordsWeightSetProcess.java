package com.smate.web.management.service.analysis;

import java.util.List;

import com.smate.web.management.model.analysis.KeywordSplit;

/**
 * 关键词权重配置.
 * 
 * @author lqh
 * 
 */
public interface KeywordsWeightSetProcess {

  /**
   * 设置关键词权重.
   * 
   * @param list
   * @return
   */
  public List<KeywordSplit> setKwsWeight(List<KeywordSplit> list);

  /**
   * 设置关键词权重.
   * 
   * @param list
   * @return
   */
  public KeywordSplit setKwsWeight(KeywordSplit kwsp);
}
