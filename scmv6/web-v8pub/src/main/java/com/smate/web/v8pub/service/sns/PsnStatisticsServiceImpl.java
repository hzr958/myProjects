package com.smate.web.v8pub.service.sns;

import java.util.List;

import org.apache.commons.collections4.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.smate.core.base.psn.dao.PsnStatisticsDao;
import com.smate.core.base.psn.dao.PsnStatisticsRefreshDao;
import com.smate.core.base.psn.dao.psncnf.PsnConfigDao;
import com.smate.core.base.psn.dao.psncnf.PsnConfigPubDao;
import com.smate.core.base.psn.model.PsnStatistics;
import com.smate.core.base.psn.model.PsnStatisticsRefresh;
import com.smate.core.base.psn.model.psncnf.PsnConfig;
import com.smate.web.v8pub.dao.sns.PubSnsDAO;
import com.smate.web.v8pub.exception.ServiceException;
import com.smate.web.v8pub.po.sns.PubSnsPO;

/**
 * 人员统计数服务实现
 * 
 * @author changwen
 * 
 */
@Service("psnStatisticsService")
@Transactional(rollbackFor = Exception.class)
public class PsnStatisticsServiceImpl implements PsnStatisticsService {
  private Logger logger = LoggerFactory.getLogger(getClass());
  @Autowired
  private PsnStatisticsDao psnStatisticsDao;
  @Autowired
  private PsnConfigDao psnConfigDao;
  @Autowired
  private PsnConfigPubDao psnConfigPubDao;
  @Autowired
  private PubSnsDAO pubSnsDAO;
  @Autowired
  private PsnStatisticsRefreshDao psnStatisticsRefreshDao;

  @Override
  public PsnStatistics getPsnStatistics(Long psnId) throws ServiceException {
    PsnStatistics psnStatistics = null;
    try {
      return psnStatisticsDao.get(psnId);
    } catch (Exception e) {
      logger.error("个人统计数服务：根据psnId获取人员统计数记录出错！", psnStatistics, e);
      throw new ServiceException(e);
    }
  }

  @Override
  public void savePsnStatistics(PsnStatistics psnStatistics) throws ServiceException {
    try {
      psnStatisticsDao.save(psnStatistics);
    } catch (Exception e) {
      logger.error("个人统计数服务：保存人员统计数记录出错！", psnStatistics, e);
      throw new ServiceException(e);
    }
  }

  @Override
  public boolean hasPrivatePub(Long psnId) {
    boolean hasPrivatePub = false;
    PsnConfig conf = psnConfigDao.getByPsn(psnId);
    if (conf != null) {
      hasPrivatePub = psnConfigPubDao.hasPrivatePub(conf.getCnfId(), psnId);
    }
    return hasPrivatePub;
  }

  @Override
  public int getHindex(Long psnId) throws Exception {
    List<PubSnsPO> pubs = pubSnsDAO.queryPubsByPsnId(psnId);
    int hidex = 0;
    if (CollectionUtils.isNotEmpty(pubs)) {
      for (int i = 1; i <= pubs.size(); i++) {
        PubSnsPO pub = pubs.get(i - 1);
        Integer citeTimes = (int) (pub.getCitations() == null ? 0 : pub.getCitations());
        if (citeTimes.intValue() >= i) {
          hidex += 1;
        }
      }
    }
    return hidex;
  }

  @Override
  public void updatePsnStatisticsRefresh(Long psnId) {
    PsnStatisticsRefresh psr = psnStatisticsRefreshDao.get(psnId);
    if (psr == null) {
      psr = new PsnStatisticsRefresh();
      psr.setPsnId(psnId);
    }
    psr.setStatus(0);
    psnStatisticsRefreshDao.saveOrUpdate(psr);
  }
}
