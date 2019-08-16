package com.smate.core.base.utils.sys;

import java.io.UnsupportedEncodingException;

/**
 * 系统公共业务逻辑基础服务类.
 * 
 * @author zk
 * 
 */
public interface ScmSystemUtil {

  static final String ENCODING = "utf-8";

  /**
   * 获取自动登录地址固定部分(获取完整的自动登录地址需在返回值后追加参数service及对应值).
   * 
   * @param personID
   * @param casUrl
   * @return
   * @throws UnsupportedEncodingException
   */
  String getAutoLoginUrl(Long personID, String casUrl) throws UnsupportedEncodingException;

  String getSysDomain();
}
