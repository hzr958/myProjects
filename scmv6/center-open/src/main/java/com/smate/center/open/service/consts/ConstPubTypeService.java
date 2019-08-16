package com.smate.center.open.service.consts;

/**
 * 
 */


import java.util.List;

import com.smate.center.open.model.consts.ConstPubType;



/**
 * @author ajb
 * 
 */

public interface ConstPubTypeService {

  /**
   * 读取所有Enabled=1的类别.
   * 
   * @return
   */
  List<ConstPubType> getAll() throws Exception;

  ConstPubType get(int id) throws Exception;

  String queryResultTypeName(Integer typeId) throws Exception;

}

