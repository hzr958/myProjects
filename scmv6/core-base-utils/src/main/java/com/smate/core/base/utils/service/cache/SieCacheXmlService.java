package com.smate.core.base.utils.service.cache;

/**
 * 
 * @author yxs
 * @since 2018年3月8日
 * @descript 临时缓存接口
 */
public interface SieCacheXmlService {
  /**
   * @param xml
   * @throws ServiceException
   */
  void put(String id, String xml);

  /**
   * @param id
   * @return xml
   * @throws ServiceException
   */
  String get(String id);

  /**
   * @param id
   * @throws ServiceException
   */
  void remove(String id);
}
