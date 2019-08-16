package com.smate.center.open.service.nsfc;

import java.util.List;

import org.apache.commons.collections4.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.smate.center.open.dao.nsfc.IrisExcludedPubDao;
import com.smate.core.base.pub.model.IrisExcludedPub;

/**
 * IRIS业务系统需要排除的成果ServiceImpl.
 * 
 * @author pwl
 * 
 */
@Service("irisExcludedPubService")
@Transactional(rollbackFor = Exception.class, propagation = Propagation.REQUIRES_NEW)
public class IrisExcludedPubServiceImpl implements IrisExcludedPubService {

  /**
   * 
   */
  private static final long serialVersionUID = -6166326369388738299L;

  protected final Logger logger = LoggerFactory.getLogger(getClass());

  @Autowired
  private IrisExcludedPubDao irisExcludedPubDao;

  @Override
  public int deleteIrisExcludedPub(String uuid) throws Exception {
    try {
      return irisExcludedPubDao.deleteIrisExcludedPub(uuid);
    } catch (Exception e) {
      logger.error("IRIS业务系统删除IrisExcludedPub数据出现异常：uuid=" + uuid, e);
      throw new Exception(e);
    }
  }

  @Override
  public void saveIrisExcludedPub(List<Long> pubIdList, String uuid) throws Exception {
    try {
      if (CollectionUtils.isNotEmpty(pubIdList)) {
        for (Long pubId : pubIdList) {
          IrisExcludedPub irisExcludedPub = new IrisExcludedPub(pubId, uuid);
          this.irisExcludedPubDao.save(irisExcludedPub);
        }
      }
    } catch (Exception e) {
      logger.error("IRIS业务系统删除IrisExcludedPub数据出现异常：uuid=" + uuid, e);
      throw new Exception(e);
    }
  }

}
