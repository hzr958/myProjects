package com.smate.sie.center.task.pdwh.service;

import java.util.List;
import java.util.Map;

import com.smate.core.base.utils.exception.PubException;
import com.smate.sie.center.task.model.SieConstRefDb;

/**
 * 第三方数据库表接口.
 * 
 * @author jszhou
 *
 */
public interface SieConstRefDbService {

  /**
   * 获取所有数据列表.
   * 
   * @return
   * @throws PubException
   */
  List<SieConstRefDb> getAllConstRefDb() throws PubException;

  /**
   * 获取所有数据列表.
   * 
   * @return
   * @throws PubException
   */
  List<SieConstRefDb> getAllLocaleConstRefDb() throws PubException;

  /**
   * 获取单个指定ID的数据.
   * 
   * @param id
   * @return
   * @throws PubException
   */
  SieConstRefDb getConstRefDbById(Long id) throws PubException;

  SieConstRefDb getConstRefDbByCode(String sourceDbCode) throws PubException;

  SieConstRefDb getConstImportRefDbByCode(String sourceDbCode) throws PubException;

  /**
   * 接收文献数据库定义同步.
   * 
   * @param list
   * @throws PubException
   */
  void pullConstRefDbSyn(List<SieConstRefDb> list) throws PubException;

  /**
   * 查找出所有的DB Code ID--Code
   * 
   * @return
   * @throws PubException
   */
  Map<Long, String> findAllDBCode() throws PubException;
}
