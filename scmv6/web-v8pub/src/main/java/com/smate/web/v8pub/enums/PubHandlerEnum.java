package com.smate.web.v8pub.enums;

/**
 * 成果处理器枚举
 * 
 * @author tsz
 *
 * @date 2018年6月14日
 */
public enum PubHandlerEnum {

  /**
   * sns库成果保存处理器
   */
  SAVE_SNS("saveSnsPubHandler"),
  /**
   * sns库成果更新处理器
   */
  UPDATE_SNS("updateSnsPubHandler"),
  /**
   * pdwh库成果保存处理器
   */
  SAVE_PDWH("savePdwhPubHandler"),
  /**
   * sns库成果更新处理器
   */
  UPDATE_PDWH("updatePdwhPubHandler"),
  /**
   * sns库成果全文更新处理器
   */
  UPDATE_SNS_FULLTEXT("updateSnsFullTextHandler"),
  /**
   * pdwh库成果全文更新处理器
   */
  UPDATE_PDWH_FULLTEXT("updatePdwhFullTextHandler"),
  /**
   * pdwh库成果引用次数更新处理器
   */
  UPDATE_PDWH_CITATIONS("updatePdwhCitationsHandler"),
  /**
   * sns库成果全文更新处理器
   */
  UPDATE_SNS_CITATIONS("updateSnsCitationsHandler"),
  // UPDATE_SNS_CITATIONS("updatePubCitationsHandler"),
  /**
   * 个人成果删除处理器
   */
  DELETE_PSN_PUB("deletePsnPubHandler"),
  /**
   * 群组成果删除处理器
   */
  DELETE_GROUP_PUB("deleteGroupPubHandler"),
  /**
   * 导入他人成果至个人库处理器
   */
  PSNPUB_TO_SNS("psnPubToSnsHandler"),
  /**
   * 基准库成果删除处理器
   */
  DELETE_PDWH_PUB("deletePdwhPubHandler");

  public String name;

  private PubHandlerEnum(String name) {
    this.name = name;
  }

  public String getName() {
    return name;
  }

  @Override
  public String toString() {
    return name;
  }
}
