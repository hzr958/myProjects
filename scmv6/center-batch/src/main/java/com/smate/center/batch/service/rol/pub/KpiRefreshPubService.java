package com.smate.center.batch.service.rol.pub;

import java.io.Serializable;
import java.util.List;
import java.util.Set;

import com.smate.center.batch.exception.pub.ServiceException;
import com.smate.center.batch.model.rol.pub.KpiCityInf;
import com.smate.center.batch.model.rol.pub.KpiDistrictInf;
import com.smate.center.batch.model.rol.pub.KpiInsInf;
import com.smate.center.batch.model.rol.pub.KpiPubUnit;
import com.smate.center.batch.model.rol.pub.KpiRefreshPub;
import com.smate.center.batch.model.rol.pub.PublicationRol;



/**
 * 成果统计更新service.
 * 
 * @author liqinghua
 * 
 */
public interface KpiRefreshPubService extends Serializable {

  /**
   * 删除指定部门之外的成果关联关系.
   * 
   * @param pubId
   * @param remainIds
   * @throws ServiceException
   */
  public void removeExtPubUnit(Long pubId, Set<Long> remainIds) throws ServiceException;

  /**
   * 获取成果关联的部门冗余列表.
   * 
   * @param pubId
   * @return
   * @throws ServiceException
   */
  public List<KpiPubUnit> getKpiPubUnitByPubId(Long pubId) throws ServiceException;

  /**
   * 添加部门成果统计更新.
   * 
   * @param unitIds
   * @throws ServiceException
   */
  public void addUnitPubRefresh(Set<Long> unitIds, Long indId) throws ServiceException;

  /**
   * 添加单位成果统计更新.
   * 
   * @param insId
   * @throws ServiceException
   */
  public void addInsPubRefresh(Long insId) throws ServiceException;

  /**
   * 添加市管辖区成果统计更新.
   * 
   * @param cityId
   * @throws ServiceException
   */
  public void addDisPubRefresh(Long disId) throws ServiceException;

  /**
   * 添加地区成果统计更新.
   * 
   * @param cityId
   * @throws ServiceException
   */
  public void addCityPubRefresh(Long cityId) throws ServiceException;

  /**
   * 添加省份成果统计更新.
   * 
   * @param prvId
   * @throws ServiceException
   */
  public void addPrvPubRefresh(Long prvId) throws ServiceException;

  /**
   * 添加成果统计更新.
   * 
   * @param pubId
   * @param isDel
   * @throws ServiceException
   */
  public void addPubRefresh(Long pubId, boolean isDel) throws ServiceException;

  /**
   * 设置成果KPI完整度.
   * 
   * @param pub
   * @return
   * @throws ServiceException
   */
  public PublicationRol validatePubKpi(PublicationRol pub) throws ServiceException;

  /**
   * 添加成果统计更新.
   * 
   * @param pubId
   * @param isDel
   * @throws ServiceException
   */
  public void addPubRefresh(PublicationRol pub, boolean isDel) throws ServiceException;

  /**
   * 添加部门关联成果更新(部门删除、合并部门).
   * 
   * @param unitId
   * @param insId
   * @param isDel
   * @return 部门成果ID列表
   * @throws ServiceException
   */
  public List<Long> addRefreshUnitPub(Long unitId, Long insId, boolean isDel) throws ServiceException;

  /**
   * 添加部门关联成果更新(合并部门).
   * 
   * @param pubIds
   * @param isDel
   * @throws ServiceException
   */
  public void addRefreshToUnitPub(List<Long> pubIds, boolean isDel) throws ServiceException;

  /**
   * 添加人员关联成果更新（人员删除、移动部门）.
   * 
   * @param psnId
   * @param insId
   * @throws ServiceException
   */
  public void addRefreshPsnPub(Long psnId, Long insId) throws ServiceException;

  public void addRefreshInsPsnPub(Long psnId, Long insId) throws ServiceException;

  /**
   * 获取需要更新成果统计数据冗余的成果ID.
   * 
   * @param maxSize
   * @return
   * @throws ServiceException
   */
  public List<KpiRefreshPub> loadNeedRefreshPubId(Integer maxSize) throws ServiceException;

  /**
   * 更新成果KPI统计.
   * 
   * @param pubId
   */
  public void refreshPubKpi(KpiRefreshPub refPub) throws ServiceException;

  /**
   * 保存更新KPI统计错误日志.
   * 
   * @param e
   * @param keyId
   * @param type 1:成果更新，2部门更新，3机构更新，4地区更新，5省份更新.
   * @throws ServiceException
   */
  public void saveKpiRefreshError(Exception e, Long keyId, Integer type) throws ServiceException;

  /**
   * 删除成果更新数据.
   * 
   * @param pubId
   * @throws ServiceException
   */
  public void removeKpiRefreshPub(Long pubId) throws ServiceException;

  /**
   * 更新单位成果默认统计数.
   * 
   * @param insInf
   * @return TODO
   * @throws ServiceException
   */
  public KpiInsInf fillKpiInsInfoPub(KpiInsInf insInf) throws ServiceException;

  /**
   * 更新市管辖区成果默认统计数.
   * 
   * @param disInf
   * @return
   * @throws ServiceException
   */
  public KpiDistrictInf fillKpiDistrictInfoPub(KpiDistrictInf disInf) throws ServiceException;

  /**
   * 更新地区成果默认统计数.
   * 
   * @param cyInf
   * @return TODO
   * @throws ServiceException
   */
  public KpiCityInf fillKpiCityInfoPub(KpiCityInf cyInf) throws ServiceException;

  /**
   * 更新省份成果默认统计条件.
   * 
   * @param prvId
   * @throws ServiceException
   */
  public void refreshPrvPubDefault(Long prvId) throws ServiceException;

}
