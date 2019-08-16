package com.smate.sie.center.task.pdwh.service;

import java.io.Serializable;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.smate.center.task.exception.DaoException;
import com.smate.core.base.utils.exception.SysServiceException;
import com.smate.sie.center.task.dao.SiePublicationLogDao;
import com.smate.sie.center.task.pub.enums.PublicationOperationEnum;

import net.sf.json.JSONObject;

/**
 * 成果日志.
 * 
 * @author jszhou
 *
 */
@Service("siePublicationLogService")
@Transactional(rollbackFor = Exception.class)
public class SiePublicationLogServiceImpl implements Serializable, SiePublicationLogService {

  /**
   * 
   */
  private static final long serialVersionUID = 2539385106041868420L;

  private final Logger logger = LoggerFactory.getLogger(SiePublicationLogServiceImpl.class);

  @Autowired
  private SiePublicationLogDao publicationLogDao;

  public void logOp(long pubId, long opPsnId, Long insId, PublicationOperationEnum op, Map<String, String> opDetail)
      throws SysServiceException {
    try {
      String detail = "";
      if (opDetail != null && opDetail.size() > 0) {
        detail = JSONObject.fromObject(opDetail).toString();
      }
      publicationLogDao.logOp(pubId, opPsnId, insId, op.toString(), detail);
    } catch (DaoException e) {
      logger.error("保存成果日志错误", e);
      throw new SysServiceException(e);
    }
  }

  @Override
  public void logOpDetail(long pubId, long opPsnId, Long insId, PublicationOperationEnum op, String opDetail)
      throws SysServiceException {
    try {
      publicationLogDao.logOp(pubId, opPsnId, insId, op.toString(), opDetail);
    } catch (DaoException e) {
      logger.error("保存成果日志错误", e);
      throw new SysServiceException(e);
    }

  }
}
