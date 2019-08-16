package com.smate.sie.center.task.pdwh.service;

import com.smate.center.task.exception.DaoException;
import com.smate.core.base.utils.exception.SysServiceException;
import com.smate.sie.center.task.dao.SiePatentLogDao;
import com.smate.sie.center.task.pub.enums.PublicationOperationEnum;
import net.sf.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.Serializable;
import java.util.Map;

/**
 * 专利日志.
 * 
 * @author jszhou
 *
 */
@Service("patentLogService")
@Transactional(rollbackFor = Exception.class)
public class SiePatentLogServiceImpl implements Serializable, SiePatentLogService {

  /**
   * 
   */
  private static final long serialVersionUID = 2539385106041868420L;

  private final Logger logger = LoggerFactory.getLogger(SiePatentLogServiceImpl.class);

  @Autowired
  private SiePatentLogDao patentLogDao;

  public void logOp(long patId, long opPsnId, Long insId, PublicationOperationEnum op, Map<String, String> opDetail)
      throws SysServiceException {

    try {
      String detail = "";
      if (opDetail != null && opDetail.size() > 0) {
        detail = JSONObject.fromObject(opDetail).toString();
      }
      patentLogDao.logOp(patId, opPsnId, insId, op.toString(), detail);
    } catch (DaoException e) {
      logger.error("保存专利日志错误", e);
      throw new SysServiceException(e);
    }
  }

  @Override
  public void logOpDetail(long patId, long opPsnId, Long insId, PublicationOperationEnum op, String opDetail)
      throws SysServiceException {
    try {
      patentLogDao.logOp(patId, opPsnId, insId, op.toString(), opDetail);
    } catch (DaoException e) {
      logger.error("保存专利日志错误", e);
      throw new SysServiceException(e);
    }

  }
}
