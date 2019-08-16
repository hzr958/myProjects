package com.smate.center.batch.enums.pub;

/**
 * @author yamingd 成果操作枚举，用于记录日志.
 */
public enum PublicationOperationEnum {

  /**
   * 创建成果.
   */
  Create,

  /**
   * 删除成果.
   */
  Delete,

  /**
   * 覆盖成果.
   */
  Overwrite,

  /**
   * 修改成果.
   */
  Update,
  /**
   * 指派作者.
   */
  AssignPubAuthor,
  /**
   * 批准R提交的成果.
   */
  Approve,
  /**
   * 拒绝R提交的成果.
   */
  Reject,
  /**
   * 在线导入成果.
   */
  Import,
  /**
   * 离线导入成果.
   */
  OfflineImport,
  /**
   * 拒绝已批准成果.
   */
  RejectApproved,
  /**
   * 拒绝R的申请撤销请求.
   */
  RejectWithdrawRequest,

  /**
   * 同意R的申请撤销请求.
   */
  ApproveWithdrawRequest,

  /**
   * 提交成果.
   */
  Submit,

  /**
   * 提交申请撤销请求.
   */
  SubmitWithdrawRequest,

  /**
   * 重新提交.
   */
  ReSubmit,

  /**
   * 确认成果.
   */
  ConfirmAuthor,

  /**
   * 完成认领成果.
   */
  FinishAuthorMatch,

  /**
   * RO发布成果.
   */
  Publish,

  /**
   * RO取消发布成果.
   */
  CancelPublish,

  /**
   * RO提交R的成果.
   */
  SubmitForR,
  /**
   * 撤销已提交的成果.
   */
  WithdrawSubmittedOutput,
  /**
   * 撤销已批准的成果.
   */
  WithdrawApprovedOutput,
  /**
   * 拒绝认领成果.
   */
  NotAnAuthor,

  /**
   * 取消指派.
   */
  CancelAssignAuthor,

  /**
   * 查重合并.
   */
  DuplicationMerge,

  /**
   * 导入覆盖.
   */
  ImportOverwrite,

  /**
   * 删除PubMember记录.
   */
  RemovePubMember,
  /**
   * 删除PubIns记录.
   */
  RemovePubIns,

  /**
   * 从单位库拉回成果数据.
   */
  PushFromIns,
  /**
   * 删除成果与人员的关系.
   */
  RemovePubPsn,
  /*
   * 同步SNS的XML
   */
  SyncFromSNS,

  /**
   * 同步更新待认领成果数.
   */
  SyncPendingConfirmPubTotal,

  /**
   * 同步更新已认领成果数.
   */
  SyncConfirmedPubTotal,
  /*
   * 同步SNS的XML错误
   */
  SyncFromSNSError,
  /*
   * 删除推荐成果
   */
  DeleteCommendPub,
  /*
   * 确认推荐成果
   */
  ConfirmCommendPub
}
