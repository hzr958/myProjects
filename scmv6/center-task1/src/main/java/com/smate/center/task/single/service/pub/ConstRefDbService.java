package com.smate.center.task.single.service.pub;

import java.util.List;
import java.util.Map;

import com.smate.center.task.model.sns.quartz.ConstRefDb;
import com.smate.core.base.utils.exception.PubException;


/**
 * 第三方数据库表.
 * 
 * @author liqinghua
 * 
 */
public interface ConstRefDbService {

  /**
   * 获取所有数据列表.
   * 
   * @return
   * @throws PubException
   */
  List<ConstRefDb> getAllConstRefDb() throws PubException;

  /**
   * 获取所有数据列表.
   * 
   * @return
   * @throws PubException
   */
  List<ConstRefDb> getAllLocaleConstRefDb() throws PubException;

  /**
   * 获取单个指定ID的数据.
   * 
   * @param id
   * @return
   * @throws PubException
   */
  ConstRefDb getConstRefDbById(Long id) throws PubException;

  ConstRefDb getConstRefDbByCode(String sourceDbCode) throws PubException;

  ConstRefDb getConstImportRefDbByCode(String sourceDbCode) throws PubException;

  /**
   * 接收文献数据库定义同步.
   * 
   * @param list
   * @throws PubException
   */
  void pullConstRefDbSyn(List<ConstRefDb> list) throws PubException;

  /**
   * 查找出所有的DB Code ID--Code
   * 
   * @return
   * @throws PubException
   */
  Map<Long, String> findAllDBCode() throws PubException;
}
