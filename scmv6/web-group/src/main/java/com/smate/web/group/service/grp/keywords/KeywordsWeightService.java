package com.smate.web.group.service.grp.keywords;

import java.util.List;

import com.smate.web.group.model.grp.keywords.KeywordSplit;

/**
 * 关键词权重配置.
 * 
 * @author lqh
 * 
 */
public interface KeywordsWeightService {

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
