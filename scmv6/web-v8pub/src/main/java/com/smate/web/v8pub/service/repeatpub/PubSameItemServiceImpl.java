package com.smate.web.v8pub.service.repeatpub;

import com.smate.web.v8pub.dao.repeatpub.PubSameItemDAO;
import com.smate.web.v8pub.exception.ServiceException;
import com.smate.web.v8pub.po.repeatpub.PubSameItemPO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 单项重复成果记录信息服务类
 * 
 * @author YJ
 *
 *         2018年9月13日
 */
@Service("pubSameItemService")
@Transactional(rollbackFor = Exception.class)
public class PubSameItemServiceImpl implements PubSameItemService {

  private Logger logger = LoggerFactory.getLogger(getClass());

  @Autowired
  private PubSameItemDAO pubSameItemDao;


  @Override
  public PubSameItemPO get(Long id) throws ServiceException {
    try {
      return pubSameItemDao.get(id);
    } catch (Exception e) {
      logger.error("通过逻辑主键id获取重复成果记录出错！id={}", id, e);
      throw new ServiceException(e);
    }
  }

  @Override
  public void save(PubSameItemPO pubSameItemPO) throws ServiceException {
    try {
      pubSameItemDao.save(pubSameItemPO);
    } catch (Exception e) {
      logger.error("保存重复成果记录出错！pubSameItemPO={}", pubSameItemPO, e);
      throw new ServiceException(e);
    }

  }

  @Override
  public void update(PubSameItemPO pubSameItemPO) throws ServiceException {
    try {
      pubSameItemDao.update(pubSameItemPO);
    } catch (Exception e) {
      logger.error("更新重复成果记录出错！pubSameItemPO={}", pubSameItemPO, e);
      throw new ServiceException(e);
    }
  }

  @Override
  public void saveOrUpdate(PubSameItemPO pubSameItemPO) throws ServiceException {
    try {
      pubSameItemDao.saveOrUpdate(pubSameItemPO);
    } catch (Exception e) {
      logger.error("保存或更新重复成果记录出错！pubSameItemPO={}", pubSameItemPO, e);
      throw new ServiceException(e);
    }

  }

  @Override
  public void deleteById(Long id) throws ServiceException {
    try {
      pubSameItemDao.delete(id);
    } catch (Exception e) {
      logger.error("通过逻辑主键id删除重复成果记录出错！id={}", id, e);
      throw new ServiceException(e);
    }

  }

  @Override
  public void delete(PubSameItemPO pubSameItemPO) throws ServiceException {
    try {
      pubSameItemDao.delete(pubSameItemPO);
    } catch (Exception e) {
      logger.error("删除重复成果记录出错！pubSameItemPO={}", pubSameItemPO, e);
      throw new ServiceException(e);
    }

  }

  @Override
  public List<PubSameItemPO> getNoDealPubSameItems(Long recordId, Long userId, Long pubSameItemId) {
    try {
      return pubSameItemDao.getNoDealPubSameItems(recordId, userId, pubSameItemId);
    } catch (Exception e) {
      logger.error("获取未处理的重复成果记录列表出错！pubSameItemId={}", pubSameItemId, e);
      throw new ServiceException(e);
    }

  }

  @Override
  public List<PubSameItemPO> getByPsnIdAndPubId(Long pubId, Long psnId) throws ServiceException {
    try {
      return pubSameItemDao.getByPsnIdAndPubId(pubId, psnId);
    } catch (Exception e) {
      logger.error("通过psnId和pubId获取重复成果记录列表出错！pubId={}", pubId, e);
      throw new ServiceException(e);
    }
  }

}
