package com.smate.center.open.service.util;



public interface OpenRestfulUtilsService {

  /**
   * 获取保存邮件数据restful链接
   * 
   * @return
   */
  String getInitEmailRestfulUrl() throws Exception;

  /**
   * 获取保存保存生成动态restful链接
   * 
   * @return
   */
  String getInitDynRestfulUrl() throws Exception;

}
