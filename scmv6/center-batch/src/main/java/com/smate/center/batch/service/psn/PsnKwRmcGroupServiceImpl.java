package com.smate.center.batch.service.psn;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections4.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.smate.center.batch.dao.sns.psn.inforefresh.KwRmcGroupDao;
import com.smate.center.batch.dao.sns.psn.inforefresh.PsnKwRmcGidDao;
import com.smate.center.batch.exception.pub.ServiceException;
import com.smate.core.base.utils.string.ServiceUtil;

/**
 * @author lichangwen
 * 
 */
@Service("psnKwRmcGroupService")
@Transactional(rollbackFor = Exception.class)
public class PsnKwRmcGroupServiceImpl implements PsnKwRmcGroupService {
  private final Logger logger = LoggerFactory.getLogger(getClass());
  @Autowired
  private PsnKwRmcGidDao psnKwRmcGidDao;
  @Autowired
  private KwRmcGroupDao kwRmcGroupDao;

  @SuppressWarnings("unchecked")
  @Override
  public int getMatchPsnKwRmcGids(Long psnId, List<String> kws) throws ServiceException {
    if (CollectionUtils.isEmpty(kws))
      return 0;
    List<Long> kwGids = new ArrayList<Long>();
    Collection<Collection<String>> container2 = ServiceUtil.splitStrList(kws, 60);
    for (Collection<String> kws2 : container2) {
      if (CollectionUtils.isEmpty(kws2))
        break;
      List<Long> dbKwGids = kwRmcGroupDao.getKwRmcGids(kws2);
      kwGids = (List<Long>) CollectionUtils.union(kwGids, dbKwGids);
    }
    List<Long> psnGids = psnKwRmcGidDao.getPsnKwRmcGids(psnId);
    if (CollectionUtils.isEmpty(psnGids) || CollectionUtils.isEmpty(kwGids))
      return 0;
    int count = 0;
    for (Long gid : kwGids) {
      for (Long psnGid : psnGids) {
        if (gid.equals(psnGid)) {
          count++;
        }
      }
    }
    return count;
  }

  @SuppressWarnings("unchecked")
  @Override
  public List<Map<String, Object>> getMatchPsnIds(List<String> pubKws) throws ServiceException {
    try {
      if (CollectionUtils.isEmpty(pubKws))
        return null;
      List<Long> kwGids = new ArrayList<Long>();
      Collection<Collection<String>> container2 = ServiceUtil.splitStrList(pubKws, 60);
      for (Collection<String> kws2 : container2) {
        if (CollectionUtils.isEmpty(kws2))
          break;
        List<Long> dbKwGids = kwRmcGroupDao.getKwRmcGids(kws2);
        kwGids = (List<Long>) CollectionUtils.union(kwGids, dbKwGids);
      }
      return psnKwRmcGidDao.getPsnIds(kwGids);
    } catch (Exception e) {
      logger.error("", e);
    }
    return null;
  }

  @Override
  public List<String> querykwBykwTxt(String kwTxt) throws ServiceException {

    return null;
  }
}
