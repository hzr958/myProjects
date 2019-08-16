package com.smate.core.base.utils.service.consts;

import org.hibernate.service.spi.ServiceException;

import com.smate.core.base.utils.exception.SmateException;
import com.smate.core.base.utils.model.consts.SieConstRegion;

/**
 * 
 * @author hd
 *
 */
public interface SieConstRegionService {

  /**
   * 通过superRegionId查询国家和区域数据.
   * 
   * @param superRegionId
   * @return
   * @throws ServiceException
   */
  String findRegionJsonData(Long superRegionId) throws SmateException;

  SieConstRegion getRegionByName(String name) throws SmateException;

  /**
   * 通过superRegionId查询中国区域数据.
   * 
   * @param superRegionId
   * @return
   * @throws ServiceException
   */
  String findCnJsonData(Long superRegionId) throws SmateException;

  SieConstRegion getRegionById(Long id) throws SmateException;

  /**
   * 查询region_id
   * 
   * @param regionName
   * @return
   * @throws ServiceException
   */
  Long findRegionId(String regionName) throws SmateException;
}
