package com.smate.center.batch.service.pub;

import java.io.Serializable;
import java.util.Map;

import net.sf.json.JSONObject;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.smate.center.batch.dao.sns.pub.PublicationLogDao;
import com.smate.center.batch.enums.pub.PublicationOperationEnum;
import com.smate.center.batch.exception.pub.DaoException;
import com.smate.center.batch.exception.pub.ServiceException;

/**
 * 成果日志.
 * 
 * @author liqinghua
 * 
 */
@Service("publicationLogService")
@Transactional(rollbackFor = Exception.class)
public class PublicationLogServiceImpl implements Serializable, PublicationLogService {

  /**
   * 
   */
  private static final long serialVersionUID = 2539385106041868420L;

  private final Logger logger = LoggerFactory.getLogger(PublicationLogServiceImpl.class);

  @Autowired
  private PublicationLogDao publicationLogDao;

  public void logOp(long pubId, long opPsnId, Long insId, PublicationOperationEnum op, Map<String, String> opDetail)
      throws ServiceException {

    try {
      String detail = "";
      if (opDetail != null && opDetail.size() > 0) {
        detail = JSONObject.fromObject(opDetail).toString();
      }
      publicationLogDao.logOp(pubId, opPsnId, insId, op, detail);
    } catch (DaoException e) {
      logger.error("保存成果日志错误", e);
      throw new ServiceException(e);
    }
  }

  @Override
  public void logOpDetail(long pubId, long opPsnId, Long insId, PublicationOperationEnum op, String opDetail)
      throws ServiceException {
    try {
      publicationLogDao.logOp(pubId, opPsnId, insId, op, opDetail);
    } catch (DaoException e) {
      logger.error("保存成果日志错误", e);
      throw new ServiceException(e);
    }

  }
}
