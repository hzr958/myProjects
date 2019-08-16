package com.smate.center.batch.service.pub.mq;

/**
 * 指派触发事件类别.
 * 
 * @author yamingd
 * 
 */
public interface PubAssignMessageKindEnum {

  /**
   * 添加新成果.
   */
  static final int AddNewPub = 1;
  /**
   * 添加新人.
   */
  static final int AddNewPsn = 2;

  /**
   * 审核通过新人.
   */
  static final int ApproveNewPsn = 3;
  /**
   * 删除人.
   */
  static final int DeletePsn = 4;

  /**
   * 修改人员信息.
   */
  static final int UpdatePsn = 5;

  /**
   * 人员确定成果信息，重新匹配成果.
   */
  static final int CONFIRM_PUB = 6;
}
