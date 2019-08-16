package com.smate.center.batch.service.pdwh.pub;

import java.io.Serializable;
import java.util.List;

import com.smate.center.batch.exception.pub.ServiceException;
import com.smate.center.batch.model.pdwh.pub.DbCachePfetchQuery;
import com.smate.center.batch.service.pub.mq.GetPdwhIdMessage;

/**
 * 在线导入成果，查询基准库ID消息.
 * 
 * @author liqinghua
 * 
 */

public interface DbCachePfetchQueryService extends Serializable {

  /**
   * 保存查询基准库ID消息.
   * 
   * @param msg
   * @throws ServiceException
   */
  public void saveGetPdwhIdMessage(GetPdwhIdMessage msg) throws ServiceException;
}
