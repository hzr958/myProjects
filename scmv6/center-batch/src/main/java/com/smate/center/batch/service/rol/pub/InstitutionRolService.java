package com.smate.center.batch.service.rol.pub;

import java.util.List;
import java.util.Map;

import com.smate.center.batch.exception.pub.ServiceException;
import com.smate.center.batch.model.rol.pub.InstitutionRol;
import com.smate.core.base.utils.model.InsPortal;

/**
 * 
 * @author new
 * 
 */
public interface InstitutionRolService {

  /**
   * @param name
   * @return
   * @throws ServiceException
   */
  InstitutionRol findByName(String name) throws ServiceException;

  /**
   * @param insId
   * @return
   * @throws ServiceException
   */
  InstitutionRol getInstitution(Long insId) throws ServiceException;

  /**
   * 单位设置，修改单位信息数据获取.
   * 
   * @return
   * @throws ServiceException
   */
  InstitutionRol getInstitutionEdit() throws ServiceException;

  /**
   * 单位名字修改，数据获取.
   * 
   * @return
   * @throws ServiceException
   */
  InstitutionRol getInstitutionCr() throws ServiceException;

  /**
   * 保存单位信息.
   * 
   * @param ins
   * @throws DaoException
   */
  void saveInstitutionRol(InstitutionRol ins) throws ServiceException;

  /**
   * @param insId
   * @return
   * @throws ServiceException
   */
  String getInsName() throws ServiceException;

  /**
   * @param insId
   * @return
   * @throws ServiceException
   */
  String getInsName(Long insId) throws ServiceException;

  /**
   * V2.6数据同步.
   * 
   * @param oldData
   * @throws ServiceException
   */
  void syncInsByOldIns(Map<String, Object> oldData) throws ServiceException;

  /**
   * 根据省份查询所有的单位.
   * 
   * @param prvId
   * @return
   * @throws ServiceException
   */
  List<InstitutionRol> getAllInstitutionByPrvId(Long prvId) throws ServiceException;

  /**
   * 根据地区查询所有的单位.
   * 
   * @param cyId
   * @return
   * @throws ServiceException
   */
  List<InstitutionRol> getAllInstitutionByCyId(Long cyId) throws ServiceException;

  /**
   * 根据市级区查询所有的单位.
   * 
   * @param disId
   * @return
   * @throws ServiceException
   */
  List<InstitutionRol> getAllInstitutionByDisId(Long disId) throws ServiceException;

  /**
   * 查找选中单位.
   * 
   * @param ids
   * @return
   * @throws ServiceException
   */
  List<InstitutionRol> getInsByIds(List<Long> ids) throws ServiceException;

  /**
   * 查找加入科研在线单位域名.
   * 
   * @param insName
   * @return
   * @throws ServiceException
   */
  InsPortal getJoinInsPortal(String insName) throws ServiceException;

  /**
   * 查找指定省份单位列表.
   * 
   * @param prvId
   * @return
   * @throws ServiceException
   */
  List<InstitutionRol> queryPrvInsList(Long prvId) throws ServiceException;

  /**
   * 查找指定名称单位列表.
   * 
   * @param prvId
   * @return
   * @throws ServiceException
   */
  List<InstitutionRol> queryJoinInsList(String queryName) throws ServiceException;

}
