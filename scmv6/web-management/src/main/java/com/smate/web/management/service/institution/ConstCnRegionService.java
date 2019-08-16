package com.smate.web.management.service.institution;

import java.io.Serializable;
import java.util.List;

import com.smate.web.management.model.institution.rol.ConstCnCity;
import com.smate.web.management.model.institution.rol.ConstCnProvince;

/**
 * 中国省市常量service.
 * 
 * @author zjh
 * 
 */
public interface ConstCnRegionService extends Serializable {
  /**
   * 获取所有省份.
   */
  List<ConstCnProvince> getAllProvince() throws Exception;

  List<ConstCnCity> getAllCity(Long provinceId) throws Exception;

}
