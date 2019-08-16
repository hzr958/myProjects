package com.smate.core.base.utils.constant;

/**
 * 
 * IOS客户端数据返回HTTP状态码
 * 
 * @author LJ
 *
 */
public class IOSHttpStatus {
  public final static String OK = "200"; // 执行成功
  public final static String NOT_MODIFIED = "304"; // 没有数据返回
  public final static String BAD_REQUEST = "400"; // 请求数据不合法
  public final static String FORBIDDEN = "403"; // 没有权限访问对应资源
  public final static String NOT_FOUND = "404"; // 请求资源不存在
  public final static String INTERNAL_SERVER_ERROR = "500"; // 服务器内部错误
  public final static String BAD_GATEWAY = "502"; // SDK/API关闭或正在升级
  public final static String SERVICE_UNAVAILABLE = "503"; // 服务端资源不可用
  public final static String URL_OUTOFTIME = "504"; // 该请求链接失效
  public final static String PARAM_ERROR = "505"; // 该请求参数错误
}
