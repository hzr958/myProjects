package com.smate.center.batch.service.pub;

import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import com.smate.center.batch.dao.sns.pub.PubSubmissionDao;
import com.smate.center.batch.dao.sns.pub.PublicationDao;
import com.smate.center.batch.exception.pub.DaoException;
import com.smate.center.batch.exception.pub.ServiceException;
import com.smate.center.batch.model.sns.pub.Publication;
import com.smate.center.batch.model.sns.pub.PublicationSubmmission;
import com.smate.center.batch.model.sns.pub.PublicationXml;

/**
 *
 */
@Service("httpMyPublicationService")
@Transactional(rollbackFor = Exception.class)
public class HttpMyPublicationServiceImpl implements HttpMyPublicationService {

  /**
   * 
   */
  protected final Logger logger = LoggerFactory.getLogger(getClass());

  @Autowired
  private PublicationXmlService publicationXmlService;

  @Autowired
  private PublicationDao publicationDao;

  @Autowired
  private PubSubmissionDao pubSubmissionDao;

  @Autowired
  private MyPublicationQueryService myPublicationQueryService;

  @Autowired
  private ScholarPublicationXmlManager scholarPublicationXmlManager;

  @Override
  public List<PublicationXml> fetchSubmittedXml(Long[] pubIds) throws ServiceException {

    try {
      List<PublicationXml> list = this.publicationXmlService.getBatchXmlByPubIds(pubIds);
      return list;
    } catch (Exception e) {
      logger.error("远程读取成果XML错误", e);
      throw new ServiceException(e);
    }

  }

  @Override
  public boolean syncSubmissionState(Long[] pubIds, Long insId, Integer state) throws ServiceException {

    Assert.notNull(pubIds);
    Assert.notNull(insId);
    Assert.notNull(state);

    try {
      for (Long pubId : pubIds) {
        PublicationSubmmission item = this.pubSubmissionDao.getByUqId(insId, pubId);
        // 创建
        if (item == null) {
          Publication pub = this.publicationDao.get(pubId);
          this.pubSubmissionDao.addPubSubmission(insId, pubId, pub.getPsnId(), pub.getVersionNo(), state);
        } else if (state == 0) {
          // 准备中/未提交，拒绝成果提交
          this.pubSubmissionDao.delete(item);
        } else {// 更新状态
          item.setState(state);
          item.setLastSync(new Date());
          this.pubSubmissionDao.saveState(item);
        }
      }

      this.publicationDao.updatePubInsPubStatus(pubIds, insId, state);

    } catch (DaoException e) {
      logger.error("syncSubmissionState同步成果提交状态错误", e);
      throw new ServiceException("syncSubmissionState同步成果提交状态错误.", e);
    }

    return true;
  }

}
