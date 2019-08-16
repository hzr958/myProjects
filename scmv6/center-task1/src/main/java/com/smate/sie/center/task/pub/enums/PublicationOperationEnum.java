package com.smate.sie.center.task.pub.enums;

/**
 * @author yamingd 成果操作枚举，用于记录日志.
 */
public enum PublicationOperationEnum {

  /**
   * 手工录入成果.
   */
  Create("1"),

  /**
   * 手工修改成果.
   */
  Update("2"),

  /**
   * 联邦检索、文件导入成果.
   */
  Import("3"),

  /**
   * 联邦检索、文件导入覆盖成果
   */
  ImportOverwrite("4"),

  /**
   * 删除成果.
   */
  Delete("5"),

  /**
   * 删除PubMember记录.
   */
  RemovePubMember("6"),

  /**
   * 基准库指派
   */
  ImportPdwh("7"),

  /**
   * 基准库指派覆盖.
   */
  ImportPdwhOverwrite("8");

  private String value;

  private PublicationOperationEnum(String value) {

    this.value = value;
  }

  @Override
  public String toString() {
    return this.value;
  }
}
