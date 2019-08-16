package com.smate.web.v8pub.service.repeatpub;

import com.smate.web.v8pub.dao.repeatpub.PubSameRecordDAO;
import com.smate.web.v8pub.exception.ServiceException;
import com.smate.web.v8pub.po.repeatpub.PubSameRecordPO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * 重复成果记录组信息服务类
 * 
 * @author YJ
 *
 *         2018年9月13日
 */

@Service("pubSameRecordService")
@Transactional(rollbackFor = Exception.class)
public class PubSameRecordServiceImpl implements PubSameRecordService {

  private Logger logger = LoggerFactory.getLogger(getClass());

  @Autowired
  private PubSameRecordDAO pubSameRecordDao;

  @Override
  public PubSameRecordPO get(Long recordId) throws ServiceException {
    try {
      return pubSameRecordDao.get(recordId);
    } catch (Exception e) {
      logger.error("通过分组recordId获取重复成果分组记录出错！recordId={}", recordId, e);
      throw new ServiceException(e);
    }
  }

  @Override
  public void save(PubSameRecordPO pubSameRecordPO) throws ServiceException {
    try {
      pubSameRecordDao.save(pubSameRecordPO);
    } catch (Exception e) {
      logger.error("保存重复成果分组记录出错！pubSameRecordPO={}", pubSameRecordPO, e);
      throw new ServiceException(e);
    }
  }

  @Override
  public void update(PubSameRecordPO pubSameRecordPO) throws ServiceException {
    try {
      pubSameRecordDao.update(pubSameRecordPO);
    } catch (Exception e) {
      logger.error("更新重复成果分组记录出错！pubSameRecordPO={}", pubSameRecordPO, e);
      throw new ServiceException(e);
    }
  }

  @Override
  public void saveOrUpdate(PubSameRecordPO pubSameRecordPO) throws ServiceException {
    try {
      pubSameRecordDao.saveOrUpdate(pubSameRecordPO);
    } catch (Exception e) {
      logger.error("更新或保存重复成果分组记录出错！pubSameRecordPO={}", pubSameRecordPO, e);
      throw new ServiceException(e);
    }
  }

  @Override
  public void deleteById(Long recordId) throws ServiceException {
    try {
      pubSameRecordDao.delete(recordId);
    } catch (Exception e) {
      logger.error("通过分组recordId删除重复成果分组记录出错！recordId={}", recordId, e);
      throw new ServiceException(e);
    }
  }

  @Override
  public void delete(PubSameRecordPO pubSameRecordPO) throws ServiceException {
    try {
      pubSameRecordDao.delete(pubSameRecordPO);
    } catch (Exception e) {
      logger.error("删除重复成果分组记录出错！pubSameRecordPO={}", pubSameRecordPO, e);
      throw new ServiceException(e);
    }

  }

}
