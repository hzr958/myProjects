package com.smate.web.v8pub.enums;

/**
 * 成果被收录状态枚举
 * 
 * @author YJ
 *
 *         2018年6月4日
 */
public enum PubSituationStatusEnum {

  // 0 未收录
  PUB_UNSITUATION(0),
  // 1 已收录
  PUB_SITUATION(1);

  public Integer value;

  private PubSituationStatusEnum(Integer value) {
    this.value = value;
  }
}
