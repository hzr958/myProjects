package com.smate.web.file.enums;

import com.smate.core.base.enums.IBaseEnum;

/**
 * 上传文件类型枚举类，暂未用到，文件上传优化改造时使用
 * 
 * @author houchuanjie
 * @date 2018年3月9日 下午5:39:12
 */
public enum UploadFileTypeEnum implements IBaseEnum<Integer> {
  /**
   * 通用类型
   */
  GENERAL(-1, "通用类型"),
  /**
   * 个人文件
   */
  PSN(0, "个人文件"),
  /**
   * 群组文件
   */
  GROUP(1, "群组文件"),
  /**
   * 个人库全文
   */
  SNS_FULLTEXT(2, "个人库成果全文类型"),
  /**
   * 基准库全文
   */
  PDWH_FULLTEXT(3, "基准库成果全文类型"),;
  private Integer value;
  private String description;

  private UploadFileTypeEnum(Integer val, String desc) {
    this.value = val;
    this.description = desc;
  }

  /**
   * 根据整型值转换为对应的枚举值，当type为{@code null}时，抛出空指针异常，type没有对应枚举值时抛出参数不合法异常
   *
   * @author houchuanjie
   * @date 2018年3月9日 下午5:55:48
   * @param type
   * @return
   * @throws NullPointerException 当且仅当 type == null 时，抛出此异常
   * @throws IllegalArgumentException 当且仅当 type != null 且没有对应枚举值时，抛出此异常
   */
  public static UploadFileTypeEnum valueOf(Integer type) throws NullPointerException, IllegalArgumentException {
    if (type == null) {
      throw new NullPointerException("类型转换失败，不能将null类型转换为FileTypeEnum类型！");
    }
    switch (type) {
      case -1:
        return GENERAL;
      case 0:
        return PSN;
      case 1:
        return GROUP;
      case 2:
        return SNS_FULLTEXT;
      case 3:
        return PDWH_FULLTEXT;
      default:
        throw new IllegalArgumentException("试图将整数值：" + type + "转换为UploadFileTypeEnum类型失败！原因：没有对应该值的枚举类型！");
    }
  }

  @Override
  public Integer getValue() {
    return this.value;
  }

  @Override
  public String getDescription() {
    return this.description;
  }
}
