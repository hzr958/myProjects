package com.smate.core.base.utils.exception;

/**
 * @author yamingd Brief生成驱动找不到异常.
 */
public class BriefDriverNotFoundException extends Exception {

  /**
   * 
   */
  private static final long serialVersionUID = -4570131272047574588L;

  public BriefDriverNotFoundException(String forTmplForm, int typeId) {

    super("找不到Brief生成驱动, tmplForm=" + forTmplForm + ", typeId=" + typeId);

  }

  public BriefDriverNotFoundException(int typeId) {
    super("找不到Brief生成驱动, typeId=" + typeId);
  }
}
