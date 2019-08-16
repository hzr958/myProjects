package com.smate.core.base.file.enums;

import com.smate.core.base.enums.IBaseEnum;

/**
 * 文件类型枚举类
 * 
 * @author houchuanjie
 *
 */
public enum FileTypeEnum implements IBaseEnum<String> {
  /**
   * 个人文件
   */
  PSN(0, "个人文件类型"),
  /**
   * 群组文件
   */
  GROUP(1, "群组文件类型"),
  /**
   * 个人库全文
   */
  SNS_FULLTEXT(2, "个人库成果全文类型"),
  /**
   * 基准库全文
   */
  PDWH_FULLTEXT(3, "基准库成果全文类型"),
  /**
   * 认领的全文
   */
  RCMD_FULLTEXT(4, "全文认领的全文类型"),

  /**
   * 全文附件
   */
  FULLTEXT_ATTACHMENT(5, "全文附件类型"),

  /**
   * 机构版上传全文
   */
  SIE_FULLTEXT(6, "机构版全文文件类型"),
  /**
   * 机构版基准库上传全文
   */
  SIE_PDWH_FULLTEXT(7, "机构版基准库全文文件类型"),

  /**
   * archiveFile 文件下载，编辑时，添加的文件，附件时的下载功能
   */
  ARCHIVE_FILE(8, " archiveFile 文件下载"),

  NEWS_FILE(9, " 新闻相关 文件下载");

  private Integer value;
  private String description;

  private FileTypeEnum(Integer val, String desc) {
    this.value = val;
    this.description = desc;
  }

  @Override
  public String getValue() {
    return this.value.toString();
  }

  public static FileTypeEnum valueOf(Integer type) {
    if (type == null) {
      throw new NullPointerException("类型转换失败，不能将null类型转换为FileTypeEnum类型！");
    }
    switch (type) {
      case 0:
        return PSN;
      case 1:
        return GROUP;
      case 2:
        return SNS_FULLTEXT;
      case 3:
        return PDWH_FULLTEXT;
      case 4:
        return RCMD_FULLTEXT;
      case 5:
        return FULLTEXT_ATTACHMENT;
      case 6:
        return SIE_FULLTEXT;
      case 7:
        return SIE_PDWH_FULLTEXT;
      case 8:
        return ARCHIVE_FILE;
      default:
        throw new IllegalArgumentException("试图将整数值：" + type + "转换为FileTypeEnum类型失败！原因：没有对应该值的枚举类型！");
    }
  }

  @Override
  public String getDescription() {
    return this.description;
  }

  @Override
  public String toString() {
    return getValue();
  }
}
