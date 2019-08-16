package com.smate.core.base.utils.exception;

/**
 * @author yamingd
 * 找不到校验者异常.
 */
public class ValidatorNotFoundException extends Exception{

	/**
	 * 
	 */
	private static final long serialVersionUID = -7077104324320615360L;

	
	public ValidatorNotFoundException(String forTmplForm,int typeId) {
		super("找不到Xml Validator, tmplForm="+forTmplForm+", typeId="+typeId);
	}
}