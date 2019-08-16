package com.smate.core.base.utils.xss;

/**
 * 拦截请求属性的类型
 * 
 * @author sxc
 * 
 */
public class TnterceptType {

  /**
   * 拦截js类型中含有特殊字符
   */
  public static final String INTERCEPT_JS = "js";

  /**
   * 拦截html类型中含有特殊字符
   */
  public static final String INTERCEPT_HTML = "html";

  /**
   * 拦截xml类型中含有特殊字符
   */
  public static final String INTERCEPT_XML = "xml";

  /**
   * 拦截vbs类型中含有特殊字符
   */
  public static final String INTERCEPT_VBS = "vbs";

  /**
   * 拦截url类型中含有特殊字符
   */
  public static final String INTERCEPT_URL = "url";

  /**
   * 请求路径拦截为intercept
   */
  public static final String INTERCEPT_YES = "intercept";

  /**
   * 请求路径不拦截为allow
   */
  public static final String INTERCEPT_NO = "allow";

}
