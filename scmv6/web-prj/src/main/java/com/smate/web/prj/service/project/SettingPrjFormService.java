package com.smate.web.prj.service.project;

import java.util.Optional;

import com.smate.core.base.exception.ServiceException;
import com.smate.web.prj.model.common.SettingPrjForm;

/**
 * SettingPrjFormService.
 * 
 * @author liqinghua
 * 
 */
public interface SettingPrjFormService {

  /**
   * 传入单位ID获取单个SettingPrjForm实体.
   * 
   * @param insId
   * @return
   * @throws ServiceException
   */
  Optional<SettingPrjForm> getSettingPrjFormByInsId(Long insId) throws ServiceException;

  /**
   * 传入ID获取单个SettingPrjForm实体.
   * 
   * @param id
   * @return
   * @throws ServiceException
   */
  Optional<SettingPrjForm> getSettingPrjFormById(int id) throws ServiceException;
}
