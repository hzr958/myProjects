package com.smate.web.prj.util;

import com.smate.core.base.utils.string.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.URLEncoder;

/**
 * 项目工具类
 *
 * @author aijiangbin
 * @create 2019-08-09 11:13
 **/
public class PrjUtils {

  protected final Logger logger = LoggerFactory.getLogger(getClass());

  public  static  final  String SAVA_PRJ_DATA_URL ="/prjdata/save/data";  // 保存项目的url


  /**
   *  报告类型(1进展报告 2中期报告 3审计报告 5结题报告 6验收报告)
   * @param type
   * @return
   */
  public   static  Integer getReportType(String  type){
    if(StringUtils.isBlank(type)) return  null ;
    Integer reportType = 1 ;
    switch (type){
      case "进展报告" : reportType =   1; break;
      case "中期报告" : reportType =   2; break;
      case "审计报告" : reportType =   3; break;
      case "结题报告" : reportType =   5; break;
      case "验收报告" : reportType =   6; break;

    }
    return  reportType;
  }

  public  static String encode(String param){
    if(org.apache.commons.lang3.StringUtils.isBlank(param)) return  "" ;
    try {
      //param = StringEscapeUtils.unescapeHtml4(param);
      param =  URLEncoder.encode(param,"utf-8");
      return param;
    } catch (Exception e) {

    }
    return  "";
  }
}
