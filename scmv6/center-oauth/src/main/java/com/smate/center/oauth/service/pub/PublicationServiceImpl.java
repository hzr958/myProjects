package com.smate.center.oauth.service.pub;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.smate.center.oauth.dao.pub.PublicationDao;
import com.smate.center.oauth.exception.DaoException;
import com.smate.center.oauth.exception.ServiceException;
import com.smate.center.oauth.model.pub.Publication;

/**
 * 成果、参考文献SERVICE. 增删改
 * 
 * @author liqinghua
 * 
 */
@Service("publicationService")
@Transactional(rollbackFor = Exception.class)
public class PublicationServiceImpl implements PublicationService {
  private static final long serialVersionUID = -2781411896625319233L;
  protected final Logger logger = LoggerFactory.getLogger(getClass());
  @Autowired
  private PublicationDao publicationDao;

  /**
   * 
   * @author liangguokeng
   */
  @Override
  public List<Publication> findPubIdsByPsnId(Long psnId) throws ServiceException {
    try {
      return publicationDao.findPubIdsByPsnId(psnId);
    } catch (DaoException e) {
      logger.error("后台任务，查询成果Id出错", e);
      e.printStackTrace();
    }
    return null;
  }


}
