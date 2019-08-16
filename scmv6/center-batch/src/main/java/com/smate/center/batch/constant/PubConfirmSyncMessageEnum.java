package com.smate.center.batch.constant;

/**
 * 成果指派枚举.
 * 
 * @author liqinghua
 * 
 */
public enum PubConfirmSyncMessageEnum {

  /**
   * 个人确认成果.
   */
  CONFIRM,
  /**
   * 个人同步XML成功.
   */
  SYNC_XML_SUCCESS,
  /**
   * 个人同步XML失败.
   */
  SYNC_XML_ERROR,
  /**
   * 查询到严格匹配成果，自动确认成果.
   */
  AUTO_CONFIRM,
  /**
   * 仅仅确认是我的成果，不导入成果（查重后用户选择忽略）.
   */
  JUST_CONFIRM
}
