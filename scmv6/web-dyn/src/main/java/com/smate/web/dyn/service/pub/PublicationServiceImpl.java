package com.smate.web.dyn.service.pub;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.smate.core.base.pub.model.Publication;
import com.smate.core.base.utils.exception.DynException;
import com.smate.web.dyn.dao.pub.PubSimpleDao;
import com.smate.web.dyn.dao.pub.PubSnsDAO;
import com.smate.web.dyn.dao.pub.PublicationDao;

/**
 * 成果、参考文献SERVICE
 * 
 * @author zk
 * 
 */
@Service("publicationService")
@Transactional(rollbackFor = Exception.class)
public class PublicationServiceImpl implements PublicationService {

  protected final Logger logger = LoggerFactory.getLogger(getClass());

  @Autowired
  private PublicationDao publicationDao;
  @Autowired
  private PubSimpleDao pubSimpleDao;
  @Autowired
  private PubSnsDAO pubSnsDAO;

  @Override
  public Publication getPubOwnerPsnIdOrStatus(Long pubId) throws DynException {
    return publicationDao.getPubOwnerPsnIdOrStatus(pubId);
  }

  @Override
  public Publication getPubForComments(Long pubId) throws DynException {
    return publicationDao.getPubForComments(pubId);
  }

  @Override
  public void savePub(Publication pub) throws DynException {
    publicationDao.save(pub);
  }

  @Override
  public Long getPubOwner(Long pubId) throws DynException {
    Long pubOwner = this.pubSnsDAO.getPubOwner(pubId);
    return pubOwner;
  }

  @Override
  public void updatePub(Publication pub) throws DynException {
    publicationDao.updatePub(pub);
  }

}
