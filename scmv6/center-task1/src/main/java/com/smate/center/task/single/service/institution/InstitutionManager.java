package com.smate.center.task.single.service.institution;

import java.util.List;

import com.smate.center.task.exception.ServiceException;
import com.smate.center.task.model.sns.quartz.Institution;



/**
 * 单位机构服务接口.
 * 
 * @author lichangwen
 * 
 */
public interface InstitutionManager {

  public List<Institution> getInsListByName(String insNameZh, String insNameEn, Long natureType);

  public String getLocaleInsName(Long insId) throws ServiceException;

  public Institution getInstitution(Long insId) throws ServiceException;

  public Institution findByName(String name) throws ServiceException;

  public Institution getInstitution(String insName, Long insId);

}
