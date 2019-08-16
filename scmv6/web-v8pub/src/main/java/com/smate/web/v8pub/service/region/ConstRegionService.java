package com.smate.web.v8pub.service.region;

import java.util.Locale;
import java.util.Map;

import com.smate.web.v8pub.exception.ServiceException;

/**
 * 行政 区划 国家 地区 服务类
 * 
 * @author YJ
 *
 *         2018年8月2日
 */
public interface ConstRegionService {

  Map<String, String> buildCountryAndCityName(Long countryId, Locale locale) throws ServiceException;
}
