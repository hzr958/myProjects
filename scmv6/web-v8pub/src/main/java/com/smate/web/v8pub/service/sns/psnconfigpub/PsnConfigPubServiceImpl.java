package com.smate.web.v8pub.service.sns.psnconfigpub;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.smate.core.base.psn.model.psncnf.PsnConfigPub;
import com.smate.core.base.psn.model.psncnf.PsnConfigPubPk;
import com.smate.web.v8pub.dao.sns.psnconfigpub.PsnConfigPubDAO;
import com.smate.web.v8pub.exception.ServiceException;

@Service(value = "psnConfigPubService")
@Transactional(rollbackFor = Exception.class)
public class PsnConfigPubServiceImpl implements PsnConfigPubService {

  private Logger logger = LoggerFactory.getLogger(getClass());

  @Autowired
  private PsnConfigPubDAO psnConfigPubDAO;

  @Override
  public PsnConfigPub get(PsnConfigPubPk id) throws ServiceException {
    try {
      PsnConfigPub psnConfigPub = psnConfigPubDAO.get(id);
      return psnConfigPub;
    } catch (Exception e) {
      logger.error("获取成果个人配置信息出错！PsnConfigPubPk={}", id, e);
      throw new ServiceException(e);
    }
  }

  @Override
  public void save(PsnConfigPub psnConfigPub) throws ServiceException {
    try {
      psnConfigPubDAO.save(psnConfigPub);
    } catch (Exception e) {
      logger.error("保存成果个人配置信息出错！PsnConfigPub={}", psnConfigPub, e);
      throw new ServiceException(e);
    }
  }

  @Override
  public void update(PsnConfigPub psnConfigPub) throws ServiceException {
    try {
      psnConfigPubDAO.update(psnConfigPub);
    } catch (Exception e) {
      logger.error("更新成果个人配置信息出错！PsnConfigPub={}", psnConfigPub, e);
      throw new ServiceException(e);
    }
  }

  @Override
  public void saveOrUpdate(PsnConfigPub psnConfigPub) throws ServiceException {
    try {
      psnConfigPubDAO.saveOrUpdate(psnConfigPub);
    } catch (Exception e) {
      logger.error("更新或保存成果个人配置信息出错！PsnConfigPub={}", psnConfigPub, e);
      throw new ServiceException(e);
    }

  }

  @Override
  public void deleteById(PsnConfigPubPk id) throws ServiceException {
    try {
      psnConfigPubDAO.delete(id);
    } catch (Exception e) {
      logger.error("删除成果个人配置信息出错！PsnConfigPubPk={}", id, e);
      throw new ServiceException(e);
    }
  }

  @Override
  public void delete(PsnConfigPub psnConfigPub) throws ServiceException {
    try {
      psnConfigPubDAO.delete(psnConfigPub);
    } catch (Exception e) {
      logger.error("删除成果个人配置信息出错！PsnConfigPub={}", psnConfigPub, e);
      throw new ServiceException(e);
    }
  }
}
