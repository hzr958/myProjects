package com.smate.web.management.service.journal;

public enum JournalImportStatusEnum {
  /**
   * 新增期刊
   */
  ADD("新增期刊"),
  /**
   * 更新期刊
   */
  UPDATE("更新期刊"),
  /**
   * 转手工处理
   */
  MANUAL("转手工处理"),
  /**
   * 没有dbId
   */
  NODBID("没有dbId"),
  /**
   * 导入错误
   */
  ERROR("导入错误"),
  /**
   * 当中英文标题都为空，或者DBCode为空时不进行导入
   */
  NOTIMPORT("不进行导入"),
  /**
   * 所有未知的错误状态
   */
  OTHER("其他状态");
  private String description;

  private JournalImportStatusEnum(String description) {
    this.description = description;
  }

  public String getDescription() {
    return this.description;
  }
}
