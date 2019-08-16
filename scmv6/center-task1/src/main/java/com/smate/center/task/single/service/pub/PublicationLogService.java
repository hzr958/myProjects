package com.smate.center.task.single.service.pub;

import java.util.Map;

import com.smate.center.task.exception.DaoException;
import com.smate.center.task.exception.ServiceException;
import com.smate.center.task.single.enums.pub.PublicationOperationEnum;

/**
 * 成果日志.
 * 
 * @author yamingd
 * 
 */
public interface PublicationLogService {

  /**
   * @param pubId 成果ID
   * @param opPsnId 操作人
   * @param op 操作枚举
   * @param opDetail 操作详细
   * @throws DaoException
   */
  public void logOp(long pubId, long opPsnId, Long insId, PublicationOperationEnum op, Map<String, String> opDetail)
      throws ServiceException;

  /**
   * 
   * @param pubId
   * @param opPsnId
   * @param insId
   * @param op
   * @param opDetail
   * @throws ServiceException
   */
  public void logOpDetail(long pubId, long opPsnId, Long insId, PublicationOperationEnum op, String opDetail)
      throws ServiceException;

}
