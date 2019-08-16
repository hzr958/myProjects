package com.smate.center.task.service.sns.pub;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.smate.center.task.dao.sns.psn.PsnJournalRefcDao;
import com.smate.center.task.exception.ServiceException;


/**
 * 人员参考文献期刊数据(用于阅读的期刊统计).
 * 
 * @author liqinghua
 * 
 */
@Service("publicationRefcJournalService")
@Transactional(rollbackFor = Exception.class)
public class PublicationRefcJournalServiceImpl implements PublicationRefcJournalService {

  /**
   * 
   */
  private static final long serialVersionUID = 424317106963771688L;
  protected final Logger logger = LoggerFactory.getLogger(getClass());
  @Autowired
  private PsnJournalRefcDao psnJournalRefcDao;


  @Override
  public int getPsnJnlByRefc(Long psnId, String issn) throws ServiceException {
    return psnJournalRefcDao.getPsnJnlByRefc(psnId, issn);
  }



}
