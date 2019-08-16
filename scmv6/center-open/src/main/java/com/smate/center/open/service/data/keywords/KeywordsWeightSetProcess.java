package com.smate.center.open.service.data.keywords;

import java.util.List;

import com.smate.center.open.model.keywords.KeywordSplit;

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
