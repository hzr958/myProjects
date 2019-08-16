package com.smate.center.batch.service.pub;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.smate.center.batch.dao.sns.pub.PubCommentDao;
import com.smate.core.base.utils.model.security.Person;

/**
 * 成果、文献评论
 * 
 * @author zll
 *
 */

@Service("publicationCommentService")
@Transactional(rollbackFor = Exception.class)
public class PublicationCommentServiceImpl implements PublicationCommentService {
  protected final Logger logger = LoggerFactory.getLogger(getClass());
  @Autowired
  private PubCommentDao pubCommentDao;

  @Override
  public void syncPubCommentPsn(Person person) {
    try {
      pubCommentDao.updatePsnInf(person.getPersonId(), person.getAvatars(), person.getName());
    } catch (Exception e) {
      logger.error("更新成果评论信息人员信息", e);
    }

  }

}
