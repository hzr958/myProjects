package com.smate.center.open.service.reschproject;

import java.util.List;

import com.smate.center.open.exception.OpenPsnInfoDaoException;
import com.smate.core.base.consts.model.Institution;

public interface InstitutionManager {


  /**
   * @param insNameZh
   * @param insNameEn
   * @return
   * @throws ServiceException
   */
  Long getInsIdByName(String insNameZh, String insNameEn) throws Exception;

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
  Institution getInstitution(Long insId) throws OpenPsnInfoDaoException;

  /**
   * 通过名称找单位.
   * 
   * @param name
   * @return
   * @throws PsnInfoDaoException
   */
  Institution findByName(String name) throws OpenPsnInfoDaoException;

  public List<Institution> getInsListByName(String insNameZh, String insNameEn, Long natureType);

}
