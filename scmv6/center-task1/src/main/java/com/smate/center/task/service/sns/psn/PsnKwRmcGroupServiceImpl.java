package com.smate.center.task.service.sns.psn;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.smate.center.task.dao.sns.quartz.KwRmcGroupDao;
import com.smate.center.task.dao.sns.quartz.PsnKwRmcGidDao;
import com.smate.center.task.exception.ServiceException;
import com.smate.core.base.utils.string.ServiceUtil;

/**
 * @author lichangwen
 * 
 */
@Service("psnKwRmcGroupService")
@Transactional(rollbackFor = Exception.class)
public class PsnKwRmcGroupServiceImpl implements PsnKwRmcGroupService {
  @Autowired
  private PsnKwRmcGidDao psnKwRmcGidDao;
  @Autowired
  private KwRmcGroupDao kwRmcGroupDao;

  @SuppressWarnings("unchecked")
  @Override
  public int getMatchPsnKwRmcGids(Long psnId, List<String> kws) throws ServiceException {
    if (CollectionUtils.isEmpty(kws)) {
      return 0;
    }
    List<Long> kwGids = new ArrayList<Long>();
    Collection<Collection<String>> container2 = ServiceUtil.splitStrList(kws, 60);
    for (Collection<String> kws2 : container2) {
      if (CollectionUtils.isEmpty(kws2)) {
        break;
      }
      List<Long> dbKwGids = kwRmcGroupDao.getKwRmcGids(kws2);
      kwGids = (List<Long>) CollectionUtils.union(kwGids, dbKwGids);
    }
    List<Long> psnGids = psnKwRmcGidDao.getPsnKwRmcGids(psnId);
    if (CollectionUtils.isEmpty(psnGids) || CollectionUtils.isEmpty(kwGids)) {
      return 0;
    }
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

}
