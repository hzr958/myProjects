package com.smate.core.base.utils.exception;

/**
 * 通过Xml构建项目页面表格Cell的内容错误异常.
 * 
 * @author liqinghua
 * 
 */
public class PrjHtmlCellContentBuildException extends Exception {

  /**
   * 
   */
  private static final long serialVersionUID = -8899842058768769039L;

  public PrjHtmlCellContentBuildException(Long prjId, Throwable ex) {
    super("构建项目页面表格Cell内容错误，pubId=" + String.valueOf(prjId), ex);
  }
}
