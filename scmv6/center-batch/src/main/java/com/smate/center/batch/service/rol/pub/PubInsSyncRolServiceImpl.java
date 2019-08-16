package com.smate.center.batch.service.rol.pub;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.smate.center.batch.dao.rol.pub.PubInsSyncDao;
import com.smate.center.batch.exception.pub.DaoException;
import com.smate.center.batch.exception.pub.ServiceException;
import com.smate.center.batch.model.rol.pub.PubInsSync;
import com.smate.center.batch.service.pub.mq.PubInsSyncMessage;
import com.smate.core.base.utils.model.Page;


/**
 * pub-ins同步服务，废除该类了，个人不同步该数据.
 * 
 */
@Service("pubInsSyncRolService")
@Transactional(rollbackFor = Exception.class)
public class PubInsSyncRolServiceImpl implements PubInsSyncRolService, java.io.Serializable {

  /**
   * 
   */
  private static final long serialVersionUID = -3835676295260448407L;

  /**
   * 
   */
  protected final Logger logger = LoggerFactory.getLogger(getClass());

  @Autowired
  private PubInsSyncDao pubInsSyncDao;

  @Override
  public void deletePubIns(PubInsSyncMessage syncMess) throws ServiceException {}

  @Override
  public void savePubIns(PubInsSyncMessage syncMess) throws ServiceException {}

  @Override
  public void updateSnsPubSubmittedFlag(Long snsPubId, Long insId, boolean flag) throws ServiceException {
    try {
      PubInsSync rec = this.pubInsSyncDao.getPubInsSyncByPK(snsPubId, insId);
      if (rec != null) {
        rec.setIsSubmited(flag ? 1 : 0);
        this.pubInsSyncDao.savePubInsSync(rec);
      }
    } catch (DaoException e) {
      throw new ServiceException("更新pub-ins记录提交标记错误.", e);
    }
  }

  @Override
  public Page<PubInsSync> queryPrepareOutputsForRO(Long insId, List<Long> psnIds, Integer startYear, Integer endYear,
      Integer pubTypeId, String pubTitle, String orderBy, Integer pageIndex, Integer pageSize) throws ServiceException {

    try {

      return pubInsSyncDao.queryPrepareOutputsForRO(insId, psnIds, startYear, endYear, pubTypeId, pubTitle, orderBy,
          pageIndex, pageSize);

    } catch (DaoException e) {
      throw new ServiceException("查询未提交成果列表错误.", e);
    }
  }

  @Override
  public Long getInsPsnPubCount(Long psnId, Long insId) throws ServiceException {
    try {

      return pubInsSyncDao.getInsPsnPubCount(psnId, insId);

    } catch (DaoException e) {
      logger.error("获取单位人员成果总数错误psnId:{}insId{}.", new Object[] {psnId, insId}, e);
      throw new ServiceException("获取单位人员成果总数错误.", e);
    }
  }

}
