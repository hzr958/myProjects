package com.smate.center.batch.service.pub;

import java.util.List;

import com.smate.center.batch.exception.pub.DaoException;
import com.smate.center.batch.exception.pub.ServiceException;
import com.smate.center.batch.model.sns.pub.ConstRegion;

/**
 * 
 * 
 */
public interface ConstRegionService extends EntityManager<ConstRegion, Long> {

  /**
   * 获取所有省份.
   */
  List<ConstRegion> getAllProvince() throws ServiceException;

  /**
   * 获取所有国家或地区.
   */
  List<ConstRegion> getAllCountryAndRegion() throws ServiceException;

  /**
   * 获取所有城市.
   * 
   * @return
   * @throws ServiceException
   */
  List<ConstRegion> getAllCity() throws ServiceException;

  /**
   * 获取单个数据.
   * 
   * @param id
   * @return
   * @throws DaoException
   */
  ConstRegion getRegionRolById(Long id) throws ServiceException;

  ConstRegion getConstRegionByName(String country) throws ServiceException;

  /**
   * 保存国家或地区.
   */
  @Override
  void save(ConstRegion constRegion);

  /**
   * 获取所有国家或地区数据.
   * 
   * @return
   * @throws ServiceException
   */
  List<ConstRegion> getAllRegion() throws ServiceException;

  /**
   * 获取指定国家或地区的省份.
   * 
   * @param superRegionId
   * @return
   * @throws ServiceException
   */
  List<ConstRegion> getRegionBySuperId(Long superRegionId) throws ServiceException;

  /**
   * 查询国家或地区的ID是否存在.
   * 
   * @param regionId
   * @return
   * @throws ServiceException
   */
  Boolean isRegionIdExit(Long regionId) throws ServiceException;

  /**
   * 判断ID是否已经存在.
   * 
   * @param regionId
   * @return
   * @throws ServiceException
   */
  List<Long> isRegionIdExit(List<Long> regionId) throws ServiceException;

  /**
   * 接收国家或地区同步.
   * 
   * @param list
   * @throws ServiceException
   */
  void pullConstRegionSyn(List<ConstRegion> list) throws ServiceException;

  /**
   * 根据传入ID，语言返回所在地
   * 
   * 英文:city+province+country
   * 
   * 中文country+province+city .
   * 
   * @param regionId
   * @param lan
   * @return
   * @throws ServiceException
   */
  String getConstRegionShow(Long regionId, String lan) throws ServiceException;

  /**
   * V2.6的ID对应过来的V3的ID.
   * 
   * @param oldId
   * @return
   * @throws ServiceException
   */
  Long getOldMapingId(Integer oldId) throws ServiceException;

  /**
   * 根据编码获取regionId
   * 
   * @param regionCode
   * @return
   * @throws ServiceException
   */
  Long getRegionIdByCode(String regionCode) throws ServiceException;

  /**
   * 通过superRegionId查询国家和区域数据.
   * 
   * @param superRegionId
   * @return
   * @throws ServiceException
   */
  String findRegionJsonData(Long superRegionId) throws ServiceException;

  /**
   * 获取所在国家地区.
   * 
   * @param id
   * @return
   * @throws DaoException
   */
  String getRegionsById(Long id) throws ServiceException;

  /**
   * 获取国家或地区
   * 
   * @param codeList
   * @return
   * @throws ServiceException
   */
  List<ConstRegion> findRegionByIds(List<Long> ids) throws ServiceException;

  /**
   * 根据机构ID获取其所有父级单位ID(包含当前单位ID).
   * 
   * @param regionId
   * @return
   */
  List<Long> getSuperRegionList(Long regionId);
}
