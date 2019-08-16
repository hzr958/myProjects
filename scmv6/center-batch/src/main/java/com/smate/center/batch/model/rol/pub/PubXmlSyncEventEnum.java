package com.smate.center.batch.model.rol.pub;

import java.io.Serializable;

/**
 * 成果xml同步事件枚举.
 * 
 * @author yamingd
 * 
 */
public interface PubXmlSyncEventEnum extends Serializable {

  /**
   * 研究人员提交成果.
   */
  static final int SUBMIT_OUTPUTS = 1;

  /**
   * 研究人员确认成果.
   */
  static final int CONFIRM_OUTPUTS = 2;
}
