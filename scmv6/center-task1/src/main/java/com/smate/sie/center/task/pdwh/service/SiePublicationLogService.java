package com.smate.sie.center.task.pdwh.service;

import com.smate.center.task.exception.DaoException;
import com.smate.core.base.utils.exception.SysServiceException;
import com.smate.sie.center.task.pub.enums.PublicationOperationEnum;

import java.util.Map;

/**
 * 成果日志.
 * 
 * @author jszhou
 *
 */
public interface SiePublicationLogService {

  /**
   * @param pubId 成果ID
   * @param opPsnId 操作人
   * @param op 操作枚举
   * @param opDetail 操作详细
   * @throws DaoException
   */
  public void logOp(long pubId, long opPsnId, Long insId, PublicationOperationEnum op, Map<String, String> opDetail)
      throws SysServiceException;

  /**
   * @param pubId
   * @param opPsnId
   * @param insId
   * @param op
   * @param opDetail
   * @throws ServiceException
   */
  public void logOpDetail(long pubId, long opPsnId, Long insId, PublicationOperationEnum op, String opDetail)
      throws SysServiceException;

}
