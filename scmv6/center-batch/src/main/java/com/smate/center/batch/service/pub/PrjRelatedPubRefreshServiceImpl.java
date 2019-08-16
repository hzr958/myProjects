package com.smate.center.batch.service.pub;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.smate.center.batch.dao.sns.prj.PrjRelatedPubRefreshDao;
import com.smate.center.batch.exception.pub.ServiceException;
import com.smate.center.batch.model.sns.prj.PrjRelatedPubRefresh;
import com.smate.center.batch.oldXml.prj.PrjRelatedPubContant;

/**
 * 项目相关成果刷新SERVICE实现.
 * 
 * @author xys
 * 
 */
@Service("prjRelatedPubRefreshService")
@Transactional(rollbackFor = Exception.class)
public class PrjRelatedPubRefreshServiceImpl implements PrjRelatedPubRefreshService {

  /**
   * 
   */
  private static final long serialVersionUID = 8360188115022012907L;
  protected final Logger logger = LoggerFactory.getLogger(getClass());
  @Autowired
  private PrjRelatedPubRefreshDao prjRelatedPubRefreshDao;


  @Override
  public void savePrjRelatedPubNeedRefresh(Long prjId, Long pubId, Long psnId, Integer refreshSource)
      throws ServiceException {
    PrjRelatedPubRefresh prjRelatedPubRefresh = null;
    if (refreshSource == PrjRelatedPubContant.REFRESH_SOURCE_PRJ) {
      prjRelatedPubRefresh = prjRelatedPubRefreshDao.getPrjRelatedPubRefreshByPrjId(prjId);
    } else if (refreshSource == PrjRelatedPubContant.REFRESH_SOURCE_PUB) {
      prjRelatedPubRefresh = prjRelatedPubRefreshDao.getPrjRelatedPubRefreshByPubId(pubId);
    }
    if (prjRelatedPubRefresh == null) {
      prjRelatedPubRefresh = new PrjRelatedPubRefresh(prjId, pubId, psnId, refreshSource, 0);
    } else {
      prjRelatedPubRefresh.setStatus(0);
    }
    this.savePrjRelatedPubRefresh(prjRelatedPubRefresh);
  }

  @Override
  public void savePrjRelatedPubRefresh(PrjRelatedPubRefresh prjRelatedPubRefresh) throws ServiceException {
    try {
      prjRelatedPubRefreshDao.save(prjRelatedPubRefresh);
    } catch (Exception e) {
      logger.error(
          "保存项目相关成果刷新记录出错prjId: " + (prjRelatedPubRefresh.getPrjId() != null ? prjRelatedPubRefresh.getPrjId() : "")
              + ",pubId: " + (prjRelatedPubRefresh.getPubId() != null ? prjRelatedPubRefresh.getPubId() : "")
              + ",status: " + prjRelatedPubRefresh.getStatus(),
          e);
      throw new ServiceException(e);
    }
  }


}
