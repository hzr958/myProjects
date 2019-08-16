package com.smate.center.batch.service.pdwh.pub;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.smate.center.batch.dao.pdwh.pub.DbCachePfetchQueryDao;
import com.smate.center.batch.exception.pub.ServiceException;
import com.smate.center.batch.model.pdwh.pub.DbCachePfetchQuery;
import com.smate.center.batch.service.pub.mq.GetPdwhIdMessage;

/**
 * 在线导入成果，查询基准库ID消息.
 * 
 * @author liqinghua
 * 
 */
@Service("dbCachePfetchQueryService")
@Transactional(rollbackFor = Exception.class)
public class DbCachePfetchQueryServiceImpl implements DbCachePfetchQueryService {

  /**
   * 
   */
  private static final long serialVersionUID = 2859384478901580426L;
  public final static int MAX_QUERY_NUM = 5;
  protected final Logger logger = LoggerFactory.getLogger(getClass());
  @Autowired
  private DbCachePfetchQueryDao dbCachePfetchQueryDao;


  @Override
  public void saveGetPdwhIdMessage(GetPdwhIdMessage msg) throws ServiceException {
    try {
      DbCachePfetchQuery query = new DbCachePfetchQuery(msg.getPubId(), msg.getPsnId(), msg.getInsId(), msg.getDbId(),
          msg.getTitleHash(), msg.getUnitHash(), msg.getPatentHash(), msg.getSourceIdHash());
      this.dbCachePfetchQueryDao.save(query);
    } catch (Exception e) {
      logger.error("保存查询基准库ID消息", e);
      throw new ServiceException("保存查询基准库ID消息", e);
    }

  }
}
