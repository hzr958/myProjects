package com.smate.center.batch.service.pub;

import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Component;

import com.smate.center.batch.model.sns.pub.KeywordSplit;
import com.smate.center.batch.util.pub.KeywordsDicUtils;

/**
 * 成果关键词权重设置.
 * 
 * @author lqh
 * 
 */
@Component("pubKeywordsWeightSetProcess")
public class PubKeywordsWeightSetProcess implements KeywordsWeightSetProcess {

  @Override
  public List<KeywordSplit> setKwsWeight(List<KeywordSplit> list) {

    for (KeywordSplit kwsp : list) {

      setKwsWeight(kwsp);
    }
    return list;
  }

  @Override
  public KeywordSplit setKwsWeight(KeywordSplit kwsp) {

    Integer type = kwsp.getType();
    if (type == null) {
      // 1英文，2中文
      type = StringUtils.isAsciiPrintable(kwsp.getKeyword()) ? 1 : 2;
    }
    double lenPercent = 0;
    Integer len = kwsp.getWlen();
    if (len == null) {
      len = KeywordsDicUtils.getKwWordLength(kwsp.getKeyword());
    }

    // 获取长度百分比
    if (type == 1) {
      // 长度为1
      if (len == 1) {
        lenPercent = 0.8d;
      } else if (len >= 2) {
        lenPercent = 1.0;
      }
    } else if (type == 2) {
      if (len == 3 || len == 2) {
        lenPercent = 0.8d;
      } else if (len > 3) {
        lenPercent = 1.0;
      }
    }

    // 根据位置查找权重
    double wt = 0;
    int kwNum = kwsp.getKwNum();
    int titleNum = kwsp.getTitleNum();
    int absNum = kwsp.getAbsNum();
    // 关键词、标题、摘要中都出现
    if (kwNum > 0 && titleNum > 0 && absNum > 0) {
      wt = 1.5;
      // 关键词、标题中出现
    } else if (kwNum > 0 && titleNum > 0) {
      wt = 1.2;
      // 关键词摘要中出现
    } else if (kwNum > 0 && absNum > 0) {
      wt = 1.1;
      // 关键词中出现
    } else if (kwNum > 0) {
      wt = 1.0;
      // 标题摘要中出现
    } else if (titleNum > 0 && absNum > 0) {
      wt = 0.8;
      // 标题中出现
    } else if (titleNum > 0) {
      wt = 0.7;
    } else if (absNum > 0) {
      // 摘要中出现
      wt = Math.min(absNum * 0.2, 0.6);
    }
    kwsp.setWeight(wt * lenPercent);
    return kwsp;
  }
}
