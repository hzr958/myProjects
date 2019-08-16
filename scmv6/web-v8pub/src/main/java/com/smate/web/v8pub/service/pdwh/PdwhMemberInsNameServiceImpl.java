package com.smate.web.v8pub.service.pdwh;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.smate.web.v8pub.dao.pdwh.PdwhMemberInsNameDAO;
import com.smate.web.v8pub.exception.ServiceException;
import com.smate.web.v8pub.po.pdwh.PdwhMemberInsNamePO;

/**
 * 基准库成果作者单位机构服务实现
 * 
 * @author YJ
 *
 *         2018年12月27日
 */

@Service
@Transactional(rollbackFor = Exception.class)
public class PdwhMemberInsNameServiceImpl implements PdwhMemberInsNameService {

  private Logger logger = LoggerFactory.getLogger(getClass());

  @Autowired
  private PdwhMemberInsNameDAO pdwhMemberInsNameDAO;

  @Override
  public PdwhMemberInsNamePO get(Long id) throws ServiceException {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public void save(PdwhMemberInsNamePO pdwhMemberInsNamePO) throws ServiceException {
    try {
      pdwhMemberInsNameDAO.save(pdwhMemberInsNamePO);
    } catch (Exception e) {
      logger.error("保存基准库成果作者单位信息出错！", e);
      throw new ServiceException(e);
    }

  }

  @Override
  public void update(PdwhMemberInsNamePO pdwhMemberInsNamePO) throws ServiceException {
    // TODO Auto-generated method stub

  }

  @Override
  public void saveOrUpdate(PdwhMemberInsNamePO pdwhMemberInsNamePO) throws ServiceException {
    try {
      pdwhMemberInsNameDAO.saveOrUpdate(pdwhMemberInsNamePO);
    } catch (Exception e) {
      logger.error("保存或更新基准库成果作者单位信息出错！", e);
      throw new ServiceException(e);
    }

  }

  @Override
  public void deleteById(Long id) throws ServiceException {
    // TODO Auto-generated method stub

  }

  @Override
  public void delete(PdwhMemberInsNamePO pdwhMemberInsNamePO) throws ServiceException {
    // TODO Auto-generated method stub

  }

  @Override
  public void deleteAll(Long pdwhPubId) throws ServiceException {
    try {
      pdwhMemberInsNameDAO.deleteAll(pdwhPubId);
    } catch (Exception e) {
      logger.error("删除指定pubId的成果作者单位信息出错！pdwhPubId={}", pdwhPubId, e);
      throw new ServiceException(e);
    }

  }

}
