package com.smate.web.v8pub.service.region;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.smate.core.base.consts.dao.ConstRegionDao;
import com.smate.core.base.consts.model.ConstRegion;
import com.smate.web.v8pub.exception.ServiceException;

/**
 * 行政 区划 国家 地区 实现类
 * 
 * @author YJ
 *
 *         2018年8月2日
 */

@Service(value = "constRegionService")
@Transactional(rollbackFor = Exception.class)
public class ConstRegionServiceImpl implements ConstRegionService {

  private Logger logger = LoggerFactory.getLogger(getClass());

  @Autowired
  private ConstRegionDao constRegionDao;

  @Override
  public Map<String, String> buildCountryAndCityName(Long countryId, Locale locale) throws ServiceException {
    Map<String, String> countryInfo = new HashMap<String, String>();
    String zhRegionName = "";
    String enRegionName = "";
    // 根据countryId获取城市信息（国家，省份，市区）
    int count = 0;
    while (countryId != null) {
      count++;
      if (count > 5) {
        break;
      }
      ConstRegion cre = this.constRegionDao.findRegionNameById(countryId);
      if (cre != null) {
        countryId = cre.getSuperRegionId();
        if (StringUtils.isNotBlank(zhRegionName)) {
          zhRegionName = cre.getZhName() + "," + zhRegionName;
        } else {
          zhRegionName = cre.getZhName();
        }
        if (StringUtils.isNotBlank(enRegionName)) {
          enRegionName = cre.getEnName() + "," + enRegionName;
        } else {
          enRegionName = cre.getEnName();
        }
      } else {
        break;
      }
    }
    if (locale.equals(Locale.CHINA)) {
      // 中文
      String[] zhRegionNameSplit = zhRegionName.split(",");
      if (zhRegionNameSplit.length >= 3) {
        countryInfo.put("countryName", zhRegionNameSplit[0] + zhRegionNameSplit[1]);
        countryInfo.put("cityName", zhRegionNameSplit[2]);
      } else {
        countryInfo.put("countryName", zhRegionName.replace(",", ""));
      }
    } else {
      // 英文
      String[] enRegionNameSplit = enRegionName.split(",");
      if (enRegionNameSplit.length >= 3) {
        countryInfo.put("countryName", enRegionNameSplit[0] + enRegionNameSplit[1]);
        countryInfo.put("cityName", enRegionNameSplit[2]);
      } else {
        countryInfo.put("countryName", enRegionName.replace(",", " "));
      }
    }
    return countryInfo;
  }

}
