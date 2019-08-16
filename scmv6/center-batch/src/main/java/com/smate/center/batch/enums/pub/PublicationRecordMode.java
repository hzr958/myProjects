package com.smate.center.batch.enums.pub;

/**
 * @author yamingd 成果数据新增方式
 */
public interface PublicationRecordMode {

  /**
   * 录入/0.
   */
  static final int ENTER = 0;
  /**
   * 在线数据库导入/1.
   */
  static final int ONLINE_IMPORT_FROM_DATABASE = 1;
  /**
   * 在线文件导入/2.
   */
  static final int ONLINE_IMPORT_FROM_FILE = 2;
  /**
   * 离线后台导入/3.
   */
  static final int OFFLINE_IMPORT = 3;

  /**
   * 从单位库同步回个人库的.
   */
  static final int SYNC_FROM_INS = 4;
  /**
   * 从同步个人库到单位库的.
   */
  static final int SYNC_FROM_SNS = 5;
  /**
   * 基准库导入.
   */
  static final int PDWH_IMPORT_FROM_DATABASE = 6;
}
