package com.smate.core.base.pub.enums;

import com.smate.core.base.enums.IBaseEnum;

/**
 * 全文请求记录的处理状态枚举类
 * 
 * @author houchuanjie
 * @date 2017年7月26日
 */
public enum PubFullTextReqStatusEnum implements IBaseEnum<Integer> {
  /**
   * 未处理
   */
  UNPROCESSED(0, "未处理"),
  /**
   * 已同意
   */
  AGREE(1, "同意"),
  /**
   * 忽略/拒绝
   */
  IGNORE(2, "忽略"),
  /**
   * 上传
   */
  UPLOAD(3, "上传"),
  /**
   * 
   */
  OTHER(4, "用户选择其他版本"),
  /**
   * 已作废
   */
  REPEAT(-1, "已作废"),
  /**
   * 已被其他用户处理
   */
  PROCESSED(-2, "其他用户已处理");

  private Integer value;
  private String description;

  private PubFullTextReqStatusEnum(Integer val, String desc) {
    this.value = val;
    this.description = desc;
  }

  @Override
  public Integer getValue() {
    return value;
  };

  @Override
  public String getDescription() {
    return description;
  };

  public static PubFullTextReqStatusEnum valueOf(Integer val) {
    if (val == null) {
      throw new NullPointerException("转换PubFullTextReqStatusEnum类型失败！参数不能为空指针！");
    }
    switch (val) {
      case 1: // 同意
        return AGREE;
      case 2: // 忽略或拒绝
        return IGNORE;
      case 3: // 上传全文
        return UPLOAD;
      case 4:
        return OTHER;
      case -1:
        return REPEAT;
      case -2:
        return PROCESSED;
      default:
        return UNPROCESSED;
    }
  }

}
