package com.smate.web.v8pub.utils;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

/**
 * spring 常量 servie 主要是 springMVC 获取不到的常量
 * 
 * 
 * @author aijiangbin
 * @date 2018年8月10日
 */
@Service
public class SpringConstUtil {

  @Value("${domainscm}")
  public String domainscm;

}
