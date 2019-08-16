package com.smate.web.v8pub.service.sns;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.smate.web.v8pub.dao.sns.PubIndustryDAO;
import com.smate.web.v8pub.exception.ServiceException;
import com.smate.web.v8pub.po.sns.PubIndustryPO;

/**
 * 行业信息服务实现
 * 
 * @author YJ
 *
 *         2019年5月24日
 */
@Service("pubIndustryService")
@Transactional(rollbackFor = Exception.class)
public class PubIndustryServiceImpl implements PubIndustryService {

  private Logger logger = LoggerFactory.getLogger(getClass());
  @Autowired
  private PubIndustryDAO pubIndustryDAO;

  @Override
  public PubIndustryPO get(Long id) throws ServiceException {
    return pubIndustryDAO.get(id);
  }

  @Override
  public void save(PubIndustryPO pubIndustryPO) throws ServiceException {
    pubIndustryDAO.saveOrUpdate(pubIndustryPO);
  }

  @Override
  public void update(PubIndustryPO pubIndustryPO) throws ServiceException {
    // TODO Auto-generated method stub

  }

  @Override
  public void saveOrUpdate(PubIndustryPO pubIndustryPO) throws ServiceException {
    pubIndustryDAO.saveOrUpdate(pubIndustryPO);
  }

  @Override
  public void deleteById(Long pubId) throws ServiceException {
    pubIndustryDAO.deleteById(pubId);
  }

  @Override
  public void delete(PubIndustryPO pubIndustryPO) throws ServiceException {
    // TODO Auto-generated method stub

  }

}
