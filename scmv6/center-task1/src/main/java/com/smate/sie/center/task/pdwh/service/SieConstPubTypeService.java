package com.smate.sie.center.task.pdwh.service;

import java.util.List;

import com.smate.sie.center.task.model.SieConstPubType;

/**
 * @author yamingd
 * 
 */
public interface SieConstPubTypeService {

  /**
   * 读取所有Enabled=1的类别.
   * 
   * @return
   */
  List<SieConstPubType> getAll() throws Exception;

  /**
   * 按ID读取类别.
   * 
   * @param id
   * @return
   */
  SieConstPubType get(int id) throws Exception;

}
