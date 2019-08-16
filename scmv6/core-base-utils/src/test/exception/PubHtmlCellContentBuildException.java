package com.smate.core.base.utils.exception;

/**
 * @author yamingd 通过Xml构建成果页面表格Cell的内容错误异常
 */
public class PubHtmlCellContentBuildException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = -8899842058768769039L;

	public PubHtmlCellContentBuildException(Long pubId, Throwable ex) {
		super("构建成果页面表格Cell内容错误，pubId=" + String.valueOf(pubId), ex);
	}
}