package com.smate.center.task.service.sns.quartz;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.smate.center.task.dao.sns.quartz.ConstRegionDao;
import com.smate.center.task.model.sns.pub.ConstRegion;


/**
 * 国家与地区/省份的公共读取业务模块.
 * 
 * @author zb
 * 
 */
@Service("constRegionService")
@Transactional(rollbackFor = Exception.class)
public class ConstRegionServiceImpl extends EntityManagerImpl<ConstRegion, Long> implements ConstRegionService {

  protected final Logger logger = LoggerFactory.getLogger(getClass());

  @Autowired
  private ConstRegionDao constRegionDao;

  @Override
  protected ConstRegionDao getEntityDao() {
    return constRegionDao;
  }


  /**
   * 获取所在国家地区.
   * 
   * @param id
   * @return
   * @throws DaoException
   */
  @Override
  public String getRegionsById(Long id) throws Exception {

    try {
      List<String> result = new ArrayList<String>();
      if (id != null) {
        ConstRegion lastRegion = constRegionDao.getConstRegionById(id);
        if (lastRegion != null) {

          ConstRegion middleRegion = constRegionDao.getConstRegionById(lastRegion.getSuperRegionId());
          if (middleRegion != null) {
            ConstRegion firstRegion = constRegionDao.getConstRegionById(middleRegion.getSuperRegionId());
            if (firstRegion != null) {// 第一级
              result.add(String.valueOf(middleRegion.getSuperRegionId()));
            }
            result.add(String.valueOf(lastRegion.getSuperRegionId()));// 第二级
          }
          result.add(String.valueOf(id));// 第三级
        }
      }

      return StringUtils.join(result, ",");
    } catch (Exception e) {
      logger.error("获取所在国家地区错误：", e);
      throw new Exception(e);
    }
  }

  @Override
  public Map<String, String> buildCountryAndCityName(Long countryId, Locale locale) throws Exception {
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
    String[] zhRegionNameSplit = zhRegionName.split(",");
    if (zhRegionNameSplit.length >= 3) {
      countryInfo.put("countryName", zhRegionNameSplit[0] + zhRegionNameSplit[1]);
      countryInfo.put("cityName", zhRegionNameSplit[2]);
    } else {
      countryInfo.put("countryName", zhRegionName.replace(",", ""));
    }
    return countryInfo;
  }

}
