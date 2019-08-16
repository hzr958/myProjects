package com.smate.center.task.model.rcmd.quartz;

/**
 * 成果指派枚举.
 * 
 * @author zjh
 *
 */
public enum PubAssignSyncMessageEnum {

  /**
   * 单位指派.
   */
  ASSIGN,
  /**
   * 单位删除指派.
   */
  DIS_ASSIGN,
  /**
   * 单位指派同步XML.
   */
  SYNC_ASSIGN_XML,
  /**
   * 同步XML失败.
   */
  SYC_ASSIGN_XML_ERROR

}
