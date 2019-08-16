package com.smate.center.batch.service.prj;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.smate.center.batch.dao.sns.prj.PrjCommentsDao;
import com.smate.core.base.utils.model.security.Person;

/**
 * 项目的评论.
 * 
 * @author zll
 * 
 */
@Service("projectCommentService")
@Transactional(rollbackFor = Exception.class)
public class ProjectCommentServiceImpl implements ProjectCommentService {
  protected final Logger logger = LoggerFactory.getLogger(getClass());
  @Autowired
  private PrjCommentsDao prjCommentsDao;

  @Override
  public void updatePsnInf(Person person) {
    try {
      prjCommentsDao.updatePsnInf(person.getPersonId(), person.getAvatars(), person.getName());
    } catch (Exception e) {
      logger.error("更新项目评论信息人员信息", e);
    }

  }


}
