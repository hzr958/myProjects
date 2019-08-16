package com.smate.center.batch.service.pub;

import java.util.List;

import com.smate.center.batch.exception.pub.DaoException;
import com.smate.center.batch.exception.pub.ServiceException;
import com.smate.center.batch.model.rol.pub.InstitutionRol;
import com.smate.center.batch.model.sns.pub.InstitutionAdd;

/**
 * 单位机构服务接口.
 * 
 * @author lichangwen
 * 
 */
public interface InstitutionAddManager {

  /**
   * @param name
   * @return List<Institution>
   * @throws ServiceException
   */
  String findListByName(String name) throws ServiceException;

  /**
   * @param insId
   * @return Institution
   * @throws ServiceException
   */
  InstitutionAdd getInstitution(Long insId) throws ServiceException;

  /**
   * 
   * @param institution
   * @throws ServiceException
   */
  void saveInstitution(InstitutionAdd institution) throws ServiceException;

  Long getInsIdByName(String insNameZh, String insNameEn) throws ServiceException;

  /**
   * 根据机构名称获取机构列表
   * 
   * @param name
   * @return
   */
  public List<InstitutionAdd> getListByName(String name) throws DaoException;

  /**
   * 从单位端同步单位.
   * 
   * @param insRol
   * @throws ServiceException
   */
  void saveInstitution(InstitutionRol insRol) throws ServiceException;

  /**
   * 模糊匹配机构名称，获取机构ID列表.
   * 
   * @param name
   * @return
   * @throws ServiceException
   */
  List<Long> getInsIdsByName(String name) throws ServiceException;

  /**
   * 匹配机构名称，获取机构ID.
   * 
   * @param name
   * @return
   * @throws ServiceException
   */
  Long getInsIdByName(String name) throws ServiceException;

  /**
   * 模糊匹配机构名称，获取机构ID列表.
   * 
   * @param name
   * @return
   */
  List<InstitutionAdd> getInsListByName(String name, int size);
}
