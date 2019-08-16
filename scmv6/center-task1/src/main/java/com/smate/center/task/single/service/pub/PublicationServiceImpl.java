package com.smate.center.task.single.service.pub;

import java.util.List;
import java.util.Map;

import org.apache.commons.collections4.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.smate.center.task.dao.sns.quartz.PubInfoTmpDao;
import com.smate.center.task.dao.sns.quartz.PublicationDao;
import com.smate.center.task.exception.DaoException;
import com.smate.center.task.exception.PublicationNotFoundException;
import com.smate.center.task.exception.ServiceException;
import com.smate.center.task.model.sns.pub.PubInfoTmp;
import com.smate.center.task.model.sns.quartz.Publication;
import com.smate.center.task.model.sns.quartz.PublicationForm;

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
  private ScholarPublicationXmlManager scholarPublicationXmlManager;
  @Autowired
  private PublicationDao publicationDao;
  @Autowired
  private PubInfoTmpDao pubInfoTmpDao;

  @Override
  public PublicationForm getPublication(PublicationForm form) throws ServiceException {
    try {
      return this.scholarPublicationXmlManager.loadXml(form);
    } catch (PublicationNotFoundException e) {
      logger.error("没有找到成果" + form.getDes3Id());
    }
    return null;
  }


  @Override
  public PublicationForm getPublication(Long pubId) throws ServiceException {
    PublicationForm form = new PublicationForm();
    form.setPubId(pubId);
    return this.getPublication(form);
  }

  @Override
  public Long getTotalPubsByPsnId(Long psnId) throws ServiceException {
    try {
      return this.publicationDao.getTotalPubsByPsnId(psnId);

    } catch (Exception e) {
      logger.error("获取成果总数", e);
      throw new ServiceException(e);
    }
  }

  @Override
  public List<Publication> findPubByPnsId(Long psnId) throws ServiceException {
    return this.publicationDao.findPubByPnsId(psnId);
  }

  @SuppressWarnings("unchecked")
  @Override
  public List<Map<String, Object>> matchPubPsnIds(Publication pub) throws ServiceException {
    List<Map<String, Object>> list1 = this.publicationDao.matchPubPsnIds(pub);
    List<Map<String, Object>> list2 = this.publicationDao.matchPubConfimPsnIds(pub);
    if (CollectionUtils.isEmpty(list1)) {
      return list2;
    }
    if (CollectionUtils.isEmpty(list2)) {
      return list1;
    }
    return (List<Map<String, Object>>) CollectionUtils.union(list1, list2);
  }

  @Override
  public int pubMatchName(Long tmPsnId, String zhName, String likeName) throws ServiceException {
    return publicationDao.pubMatchName(tmPsnId, zhName, likeName);
  }


  @Override
  public List<Publication> getPubByPsnId() throws ServiceException {
    return publicationDao.getPubByPsnId();
  }


  @Override
  public void savePubInfoTmp(PubInfoTmp pubInfo) throws ServiceException {
    pubInfoTmpDao.save(pubInfo);
  }


  @Override
  public List<Map> getLastMonthPsnPubs(Integer size) throws ServiceException {
    try {
      return publicationDao.getLastMonthPsnPubs(size);
    } catch (DaoException e) {
      logger.error("批量获取成果数时出错", e.getMessage());
      throw new ServiceException(e);
    }
  }


  @Override
  public List<Map> getLastMonthPsnCitedTimes(Integer size) {
    try {
      return publicationDao.getLastMonthCitedTimes(size);
    } catch (DaoException e) {
      logger.error("批量获取成果数时出错", e.getMessage());
      throw new ServiceException(e);
    }
  }


}
