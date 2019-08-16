package com.smate.center.task.single.service.pub;

import java.util.List;

import com.smate.center.task.model.sns.pub.KeywordSplit;

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
