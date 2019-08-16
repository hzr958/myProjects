package com.smate.web.prj.enums;

/**
 * 项目操作枚举，用于记录日志.
 * 
 * @author liqinghua
 * 
 */
public enum ProjectOperationEnum {

  /**
   * 创建项目.
   */
  Create,

  /**
   * 删除项目.
   */
  Delete,

  /**
   * 覆盖项目.
   */
  Overwrite,
  /**
   * 修改项目.
   */
  Update, Import,
  /**
   * 离线导入项目.
   */
  OfflineImport,
  /**
   * 查重合并.
   */
  DuplicationMerge,
  /**
   * 导入覆盖.
   */
  ImportOverwrite,
  /**
   * 删除PrjMember记录.
   */
  RemovePrjMember,
  /*
   * 同步SNS的XML错误
   */
  SyncFromSNSError,
  /*
   * 删除推荐项目
   */
  DeleteCommendPrj,
  /*
   * 确认推荐项目
   */
  ConfirmCommendPrj
}
