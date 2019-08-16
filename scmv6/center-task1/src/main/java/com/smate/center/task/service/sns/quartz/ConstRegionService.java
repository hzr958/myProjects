package com.smate.center.task.service.sns.quartz;

import java.util.Locale;
import java.util.Map;
import com.smate.center.task.model.sns.pub.ConstRegion;

public interface ConstRegionService extends EntityManager<ConstRegion, Long> {


  /**
   * 获取所在国家地区.
   * 
   * @param id
   * @return
   * @throws DaoException
   */
  String getRegionsById(Long id) throws Exception;

  Map<String, String> buildCountryAndCityName(Long countryId, Locale locale) throws Exception;
}
