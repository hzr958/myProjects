package com.smate.sie.center.task.service;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.smate.core.base.utils.dao.consts.SieConstRegionDao;
import com.smate.core.base.utils.exception.SysServiceException;
import com.smate.core.base.utils.model.consts.SieConstRegion;
import com.smate.core.base.utils.number.NumberUtils;
import com.smate.sie.center.task.dao.SieCityIpDao;
import com.smate.sie.center.task.model.SieCityIp;

@Service("sieCityIpService")
@Transactional(rollbackFor = Exception.class)
public class SieCityIpServiceImpl implements SieCityIpService {

  private final Logger logger = LoggerFactory.getLogger(getClass());
  @Autowired
  private SieCityIpDao sieCityIpDao;
  @Autowired
  private SieConstRegionDao sieConstRegionDao;

  @Override
  public SieCityIp parseIP(String ipStr) throws SysServiceException {
    SieCityIp cityIp = null;
    try {

      Long ip = buildIPParam(ipStr);
      cityIp = sieCityIpDao.findByIp(ip);

    } catch (Exception e) {
      logger.error("解析IP异常 ！ipStr :{}", new Object[] {ipStr, e});
      throw new SysServiceException("解析IP异常 ！ipStr :" + ipStr, e);
    }
    return cityIp;
  }

  @Override
  public Map<String, String> parseIP2(String ipStr) throws SysServiceException {
    Map<String, String> result = null;
    try {
      if (ipStr.trim().startsWith("192.168") || ipStr.trim().equals("127.0.0.1")) {
        return result;
      }
      result = ParseIpUtils.getAddresses(ipStr.trim(), "utf-8");
      if (result == null) {
        SieCityIp cityIp = this.parseIP(ipStr.trim());
        if (cityIp != null) {
          result = new HashMap<String, String>();
          result.put(ParseIpUtils.STR_COUNTRY, "中国");
          result.put(ParseIpUtils.STR_PRVO, cityIp.getProvince());
          result.put(ParseIpUtils.STR_CITY, getFormatCity(cityIp.getProvince(), cityIp.getCity()));
        }

      } else {
        String cityIdStr = result.get("city_id");
        if (cityIdStr != null && NumberUtils.isDigits(cityIdStr)) {
          Long cityId = NumberUtils.parseLong(cityIdStr);
          SieConstRegion region = sieConstRegionDao.get(cityId);
          if (region != null) {
            result.put(ParseIpUtils.STR_CITY, region.getZhName());
          }

        }
        String regionIdStr = result.get("region_id");
        if (regionIdStr != null && NumberUtils.isDigits(regionIdStr)) {
          Long regionId = NumberUtils.parseLong(regionIdStr);
          SieConstRegion region = sieConstRegionDao.get(regionId);
          if (region != null) {
            result.put(ParseIpUtils.STR_PRVO, region.getZhName());
          }
        }
      }
      if (result != null && StringUtils.isNotBlank(result.get(ParseIpUtils.STR_CITY))) {
        SieConstRegion region = sieConstRegionDao.getConstRegionByName(result.get(ParseIpUtils.STR_CITY).trim());
        if (region == null) {
          result.put(ParseIpUtils.STR_CITY, null);
        }
      }

    } catch (Exception e) {
      logger.error("解析IP异常 ！ipStr :{}", new Object[] {ipStr, e});
      throw new SysServiceException("解析IP异常 ！ipStr :" + ipStr, e);
    }
    return result;
  }

  /**
   * 构建IP地址参数.
   * 
   * @param ip
   * @return
   */
  private Long buildIPParam(String ip) throws Exception {
    if (ip == null) {
      return 0L;
    }
    /**
     * 判断IP格式和范围
     */
    String rexp = "([1-9]|[1-9]\\d|1\\d{2}|2[0-4]\\d|25[0-5])(\\.(\\d|[1-9]\\d|1\\d{2}|2[0-4]\\d|25[0-5])){3}";
    Pattern pat = Pattern.compile(rexp);
    Matcher mat = pat.matcher(ip);
    if (!mat.find()) {// 不是ip地址
      return 0L;
    }
    String[] ipArray = ip.split("\\.");
    Long result = 0l;
    for (int i = 0; i < ipArray.length; i++) {
      Double ipData = (Long.valueOf(ipArray[i]) * Math.pow(256, 3 - i));
      result = result + ipData.longValue();
    }
    return result;
  }

  private String getFormatCity(String prvo, String city) {
    switch (prvo.trim()) {
      case "上海市":
        return "上海市";
      case "北京市":
        return "北京市";
      case "天津市":
        return "天津市";
      case "重庆市":
        return "重庆市";
      default:
        return city;
    }
  }

}
