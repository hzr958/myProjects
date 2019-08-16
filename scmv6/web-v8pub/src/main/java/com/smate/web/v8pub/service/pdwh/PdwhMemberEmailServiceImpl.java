package com.smate.web.v8pub.service.pdwh;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.smate.web.v8pub.dao.pdwh.PdwhMemberEmailDAO;
import com.smate.web.v8pub.exception.ServiceException;
import com.smate.web.v8pub.po.pdwh.PdwhMemberEmailPO;

import java.util.List;

/**
 * 基准库成果作者邮件信息服务实现
 * 
 * @author YJ
 *
 *         2019年1月8日
 */

@Service(value = "pdwhMemberEmailService")
@Transactional(rollbackFor = Exception.class)
public class PdwhMemberEmailServiceImpl implements PdwhMemberEmailService {

  private Logger logger = LoggerFactory.getLogger(getClass());

  @Autowired
  private PdwhMemberEmailDAO pdwhMemberEmailDAO;

  @Override
  public PdwhMemberEmailPO get(Long id) throws ServiceException {
    return null;
  }

  @Override
  public void save(PdwhMemberEmailPO pdwhMemberEmailPO) throws ServiceException {
    try {
      pdwhMemberEmailDAO.save(pdwhMemberEmailPO);
    } catch (Exception e) {
      logger.error("保存基准库成果作者邮件出错！", e);
      throw new ServiceException(e);
    }
  }

  @Override
  public void update(PdwhMemberEmailPO pdwhMemberEmailPO) throws ServiceException {
    // TODO Auto-generated method stub

  }

  @Override
  public void saveOrUpdate(PdwhMemberEmailPO pdwhMemberEmailPO) throws ServiceException {
    // TODO Auto-generated method stub

  }

  @Override
  public void deleteById(Long id) throws ServiceException {
    // TODO Auto-generated method stub

  }

  @Override
  public void delete(PdwhMemberEmailPO pdwhMemberEmailPO) throws ServiceException {
    // TODO Auto-generated method stub

  }

  @Override
  public void deleteAll(Long pdwhPubId) throws ServiceException {
    try {
      pdwhMemberEmailDAO.deleteAll(pdwhPubId);
    } catch (Exception e) {
      logger.error("删除指定基准库成果作者邮件出错！pdwhPubId={}", pdwhPubId, e);
      throw new ServiceException(e);
    }
  }

  @Override
  public List<PdwhMemberEmailPO> findByPubId(Long pdwhPubId) throws ServiceException {
    return pdwhMemberEmailDAO.findByPubId(pdwhPubId);
  }

}
