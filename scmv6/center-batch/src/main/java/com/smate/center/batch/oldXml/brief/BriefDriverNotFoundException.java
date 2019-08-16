package com.smate.center.batch.oldXml.brief;

import com.smate.core.base.utils.exception.BatchTaskException;

/**
 * @author yamingd Brief生成驱动找不到异常.
 */
public class BriefDriverNotFoundException extends BatchTaskException {

  /**
   * 
   */
  private static final long serialVersionUID = -4570131272047574588L;

  public BriefDriverNotFoundException(String forTmplForm, int typeId) {

    super("找不到Brief生成驱动, tmplForm=" + forTmplForm + ", typeId=" + typeId);

  }
}
