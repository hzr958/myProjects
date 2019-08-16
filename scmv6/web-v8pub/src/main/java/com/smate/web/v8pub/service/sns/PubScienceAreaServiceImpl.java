package com.smate.web.v8pub.service.sns;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.smate.web.v8pub.dao.sns.PubScienceAreaDAO;
import com.smate.web.v8pub.exception.ServiceException;
import com.smate.web.v8pub.po.sns.PubScienceAreaPO;

/**
 * sns库成果科技领域实现类
 * 
 * @author YJ
 *
 *         2018年8月7日
 */
@Service(value = "pubScienceAreaService")
@Transactional(rollbackFor = Exception.class)
public class PubScienceAreaServiceImpl implements PubScienceAreaService {

  private Logger logger = LoggerFactory.getLogger(getClass());

  @Autowired
  private PubScienceAreaDAO pubScienceAreaDAO;

  @Override
  public PubScienceAreaPO get(Long id) throws ServiceException {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public List<PubScienceAreaPO> getScienceAreaList(Long id) throws ServiceException {
    return pubScienceAreaDAO.getPubAreaList(id);
  }

  @Override
  public void save(PubScienceAreaPO pubScienceAreaPO) throws ServiceException {
    try {
      pubScienceAreaDAO.save(pubScienceAreaPO);
    } catch (Exception e) {
      logger.error("sns成果科技领域服务：保存科技领域出错！", pubScienceAreaPO);
      throw new ServiceException(e);
    }

  }

  @Override
  public void update(PubScienceAreaPO pubScienceAreaPO) throws ServiceException {
    // TODO Auto-generated method stub

  }

  @Override
  public void saveOrUpdate(PubScienceAreaPO pubScienceAreaPO) throws ServiceException {
    // TODO Auto-generated method stub

  }

  @Override
  public void deleteById(Long pubId) throws ServiceException {
    try {
      pubScienceAreaDAO.deleteById(pubId);
    } catch (Exception e) {
      logger.error("sns成果科技领域服务：删除科技领域出错！", pubId);
      throw new ServiceException(e);
    }
  }

  @Override
  public void delete(PubScienceAreaPO pubScienceAreaPO) throws ServiceException {
    // TODO Auto-generated method stub

  }

}
