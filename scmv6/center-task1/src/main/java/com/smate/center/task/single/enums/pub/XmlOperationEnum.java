package com.smate.center.task.single.enums.pub;

/**
 * @author yamingd Xml操作枚举.
 */
public enum XmlOperationEnum {

  /**
   * 录入.
   */
  Enter("Enter"),
  /**
   * 导入.
   */
  Import("Import"),
  /**
   * 离线导入.
   */
  OfflineImport("OfflineImport"),
  /**
   * 修改.
   */
  Edit("Edit"),

  /**
   * 从单位库push回个人库.
   */
  PushFromIns("PushFromIns"),

  /**
   * 从个位库pull回单位库.
   */
  SyncFromSNS("SyncFromSNS"),
  /**
   * 个人推荐成果Push给他人.
   */
  SyncToSNS("SyncToSNS"),
  /**
   * 从v2.6同步数据.
   */
  SyncFromOld("SyncFromOld"),
  /**
   * 从v2.6同步项目结题报告补充数据.
   */
  SyncFromOldPrj("SyncFromOldPrj"),
  /**
   * 基准库导入成果.
   */
  ImportPdwh("ImportPdwh");

  private String value;

  private XmlOperationEnum(String value) {
    this.value = value;
  }

  @Override
  public String toString() {
    return this.value;
  }

}
