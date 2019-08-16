package com.smate.center.batch.service.institution;

import java.util.List;

import com.smate.center.batch.exception.pub.ServiceException;
import com.smate.center.batch.model.sns.pub.Institution;



/**
 * 单位机构服务接口.
 * 
 * @author lichangwen
 * 
 */
public interface InstitutionManager {

  public List<Institution> getInsListByName(String insNameZh, String insNameEn, Long natureType);

  public String getLocaleInsName(Long insId) throws ServiceException;

}
