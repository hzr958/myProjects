package com.smate.center.batch.service.pub;

import java.util.List;

import org.apache.commons.collections4.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.smate.center.batch.dao.sns.pub.IrisExcludedPubDao;
import com.smate.center.batch.exception.pub.ServiceException;
import com.smate.center.batch.model.sns.pub.IrisExcludedPub;

/**
 * IRIS业务系统需要排除的成果ServiceImpl.
 * 
 * @author pwl
 * 
 */
@Service("irisExcludedPubService")
@Transactional(rollbackFor = Exception.class)
public class IrisExcludedPubServiceImpl implements IrisExcludedPubService {

  /**
   * 
   */
  private static final long serialVersionUID = -6166326369388738299L;

  protected final Logger logger = LoggerFactory.getLogger(getClass());

  @Autowired
  private IrisExcludedPubDao irisExcludedPubDao;

  @Override
  public void deleteIrisExcludedPub(String uuid) throws ServiceException {
    try {
      irisExcludedPubDao.deleteIrisExcludedPub(uuid);
    } catch (Exception e) {
      logger.error("IRIS业务系统删除IrisExcludedPub数据出现异常：uuid=" + uuid, e);
      throw new ServiceException(e);
    }
  }

  @Override
  public void saveIrisExcludedPub(List<Long> pubIdList, String uuid) throws ServiceException {
    try {
      if (CollectionUtils.isNotEmpty(pubIdList)) {
        for (Long pubId : pubIdList) {
          IrisExcludedPub irisExcludedPub = new IrisExcludedPub(pubId, uuid);
          this.irisExcludedPubDao.save(irisExcludedPub);
        }
      }
    } catch (Exception e) {
      logger.error("IRIS业务系统删除IrisExcludedPub数据出现异常：uuid=" + uuid, e);
      throw new ServiceException(e);
    }
  }

}
