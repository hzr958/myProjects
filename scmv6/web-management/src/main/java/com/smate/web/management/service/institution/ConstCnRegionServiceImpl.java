package com.smate.web.management.service.institution;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.smate.web.management.dao.institution.rol.ConstCnCityDao;
import com.smate.web.management.dao.institution.rol.ConstCnProvinceDao;
import com.smate.web.management.model.institution.rol.ConstCnCity;
import com.smate.web.management.model.institution.rol.ConstCnProvince;

@Service("ConstCnRegionService")
@Transactional(rollbackFor = Exception.class)
public class ConstCnRegionServiceImpl implements ConstCnRegionService {

  /**
   * 
   */
  private static final long serialVersionUID = -5970896073181197635L;
  Logger logger = LoggerFactory.getLogger(getClass());
  @Autowired
  private ConstCnProvinceDao constCnProvinceDao;
  @Autowired
  private ConstCnCityDao constCnCityDao;

  /**
   * 
   * 获取所有省份.
   */
  @Override
  public List<ConstCnProvince> getAllProvince() throws Exception {
    try {
      return constCnProvinceDao.getAllProvince(LocaleContextHolder.getLocale());
    } catch (Exception e) {
      logger.error("getAllProvince获取所有省份错误：", e);
      throw new Exception(e);
    }

  }

  /**
   * 获取所有市区.
   */
  @Override
  public List<ConstCnCity> getAllCity(Long provinceId) throws Exception {
    try {
      if (provinceId == null) {
        return null;
      }
      return constCnCityDao.getAllCit(provinceId);
    } catch (Exception e) {
      logger.error("getAllProvince获取所有省份错误：", e);
      throw new Exception(e);
    }
  }
}
