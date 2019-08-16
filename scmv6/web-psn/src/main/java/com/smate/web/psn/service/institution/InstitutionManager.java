package com.smate.web.psn.service.institution;

import com.smate.core.base.consts.model.Institution;
import com.smate.web.psn.exception.PsnException;
import com.smate.web.psn.exception.PsnInfoDaoException;


/**
 * 单位机构服务接口
 * 
 * @author Administrator
 *
 */
public interface InstitutionManager {

  /**
   * 通过单位名称获取单位ID
   */
  Long getInsIdByName(String insNameZh, String insNameEn) throws PsnException;

  /**
   * 根据单位ID获取对应单位的名称详细信息.
   * 
   * @param insName
   * @param insId
   * @return
   */
  Institution getInstitution(String insName, Long insId);

  /**
   * 通过单位编号取得单位实体
   * 
   * @param insId
   * @return
   * @throws PsnInfoDaoException
   */
  Institution getInstitution(Long insId) throws PsnInfoDaoException;

  /**
   * 通过名称找单位.
   * 
   * @param name
   * @return
   * @throws PsnInfoDaoException
   */
  Institution findByName(String name) throws PsnInfoDaoException;
}
