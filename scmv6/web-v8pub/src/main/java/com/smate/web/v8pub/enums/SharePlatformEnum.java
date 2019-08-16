package com.smate.web.v8pub.enums;

/**
 * 成果分享平台枚举
 * 
 * @author aijiangbin
 * @date 2018年6月1日
 */
public enum SharePlatformEnum {

  Dynamic(1), Friend(2), Group(3), WeChat(4), Sina(5), Facebook(6), Linkedin(7), QQPLATFORM(8);

  private Integer value;

  SharePlatformEnum(int value) {
    this.value = value;
  }


}
