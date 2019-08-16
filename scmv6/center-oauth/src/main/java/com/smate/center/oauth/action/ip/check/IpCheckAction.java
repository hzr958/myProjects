package com.smate.center.oauth.action.ip.check;

import com.opensymphony.xwork2.ActionSupport;
import com.smate.core.base.utils.app.AppActionUtils;
import com.smate.core.base.utils.constant.IOSHttpStatus;
import com.smate.core.base.utils.string.StringUtils;
import com.smate.core.base.utils.struts2.Struts2Utils;
import net.ipip.ipdb.City;
import net.ipip.ipdb.CityInfo;
import org.apache.struts2.convention.annotation.Action;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;

import java.util.HashMap;
import java.util.Map;

/**
 * 判断ip归属,以及帐号绑定验证方式.
 * 
 * @author tsz
 *
 */
public class IpCheckAction extends ActionSupport {

  /**
   * 
   */
  private static final long serialVersionUID = 1L;
  protected Logger logger = LoggerFactory.getLogger(getClass());

  @Value("${ipip.ipdb.file}")
  private String ipdbFile;

  /**
   * 创建并关联账号
   * 
   * @return
   */
  @Action("/oauth/ip")
  public String ipInfo() {
    Map<String, Object> map = new HashMap<String, Object>();
    String ip = Struts2Utils.getRemoteAddr();
    // 获取ip所在国家地区
    CityInfo info = ipQuery(ip);
    // 判断地区 中国大陆 就验证手机号 非中国大陆就验证邮箱
    map.put("result", "2");// 默认 邮箱验证
    if (info != null) {
      String country_name = info.getCountryName();
      String region_name = info.getRegionName();
      if ("中国".equals(country_name) || "局域网".equals(country_name)) {
        if ("香港".equals(region_name) || "澳门".equals(region_name) || "台湾".equals(region_name)) {
          map.put("result", "2");// 邮箱验证
        } else {
          map.put("result", "1");// 手机号验证
        }
      }
    }
    map.put("status", "success");
    Struts2Utils.renderJson(map, "encoding:UTF-8");
    return null;
  }
  /**
   * 创建并关联账号
   *
   * @return
   */
  @Action("/oauth/find/ip")
  public String findiIpInfo() {
    String status = IOSHttpStatus.OK;// 默认状态
    Map<String, Object> map = new HashMap<String, Object>();
    String ip = Struts2Utils.getParameter("ip");
    map.put("result", "2");// 默认 国外ip
    if(StringUtils.isNotBlank(ip)){
      // 获取ip所在国家地区
      CityInfo info = ipQuery(ip);
      // 判断地区 中国大陆 就验证手机号 非中国大陆就验证邮箱
      if (info != null) {
        String country_name = info.getCountryName();
        String region_name = info.getRegionName();
        if ("中国".equals(country_name) || "局域网".equals(country_name)) {
          if ("香港".equals(region_name) || "澳门".equals(region_name) || "台湾".equals(region_name)) {

          } else {
            map.put("result", "1");// 手机号验证
          }
        }
      }
    }
    AppActionUtils.renderAPPReturnJson(map, 0, status);
    return  null ;
  }

  private CityInfo ipQuery(String currentIp) {
    try {
      // City类可用于IPDB格式的IPv4免费库，IPv4与IPv6的每周高级版、每日标准版、每日高级版、每日专业版、每日旗舰版
      City db = new City(ipdbFile);
      CityInfo info = db.findInfo(currentIp, "CN");
      // CityInfo info = db.findInfo("61.244.148.166", "CN");
      return info;
    } catch (Exception e) {
      logger.error("获取ip信息出错,ip=" + currentIp, e);
      return null;
    }
  }


}
