package com.smate.web.v8pub.service;

import com.smate.web.v8pub.exception.ServiceException;

/**
 * 基本服务接口
 * 
 * @param <PK> 主键类型
 * @param <E> 实体类
 * @author houchuanjie
 * @date 2018/05/31 16:07
 */
public interface BaseService<PK, E> {

  /**
   * 通过主键获取对象
   * 
   * @param id
   * @return
   * @throws ServiceException
   */
  E get(PK id) throws ServiceException;

  /**
   * 保存
   * 
   * @param entity
   * @throws ServiceException
   */
  void save(E entity) throws ServiceException;

  /**
   * 更新
   * 
   * @param entity
   * @throws ServiceException
   */
  void update(E entity) throws ServiceException;

  /**
   * 更新或保存
   * 
   * @param entity
   * @throws ServiceException
   */
  void saveOrUpdate(E entity) throws ServiceException;

  /**
   * 通过主键删除
   * 
   * @param id
   * @throws ServiceException
   */
  void deleteById(PK id) throws ServiceException;

  /**
   * 删除实体
   * 
   * @param entity
   * @throws ServiceException
   */
  void delete(E entity) throws ServiceException;
}
