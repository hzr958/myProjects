package com.smate.center.batch.service.pub;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.smate.center.batch.dao.sns.psn.PsnJournalRefcDao;
import com.smate.center.batch.dao.sns.pub.PublicationRefcJournalDao;
import com.smate.center.batch.exception.pub.ServiceException;

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
  private PublicationRefcJournalDao pubRefcJournalDao;
  @Autowired
  private PsnJournalRefcDao psnJournalRefcDao;

  @Override
  public void savePubRefcJournal(Long pubId, Long psnId, String issn, String jname, Long jnlId, Integer pubYear)
      throws ServiceException {

    try {
      if (StringUtils.isBlank(issn) && StringUtils.isBlank(jname)) {
        this.delPubRefcJournal(pubId);
        return;
      }
      issn = StringUtils.substring(issn, 0, 100);
      this.pubRefcJournalDao.savePubJournal(pubId, psnId, issn, jname, jnlId, pubYear);
    } catch (Exception e) {
      logger.error("保存参考文献期刊pubId:" + pubId, e);
      throw new ServiceException("保存参考文献期刊pubId:" + pubId, e);
    }
  }

  @Override
  public void delPubRefcJournal(Long pubId) throws ServiceException {

    try {
      this.pubRefcJournalDao.delPubJournal(pubId);
    } catch (Exception e) {
      logger.error("删除参考文献期刊pubId:" + pubId, e);
      throw new ServiceException("删除参考文献期刊pubId:" + pubId, e);
    }
  }

  @Override
  public void updatePsnJournalRefc(Long psnId) throws ServiceException {
    try {
      psnJournalRefcDao.updatePsnJournalRefc(psnId);
    } catch (Exception e) {
      logger.error("更新人员最新收藏文献期刊psnId:" + psnId, e);
      throw new ServiceException("更新人员最新收藏文献期刊psnId:" + psnId, e);
    }
  }

  @Override
  public int getPsnJnlByRefc(Long psnId, String issn) throws ServiceException {
    return psnJournalRefcDao.getPsnJnlByRefc(psnId, issn);
  }



}
