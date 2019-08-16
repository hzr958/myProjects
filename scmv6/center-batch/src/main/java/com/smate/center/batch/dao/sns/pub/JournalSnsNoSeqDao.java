package com.smate.center.batch.dao.sns.pub;

import org.springframework.stereotype.Repository;

import com.smate.center.batch.exception.pub.DaoException;
import com.smate.center.batch.model.sns.pub.JournalSnsNoSeq;
import com.smate.core.base.utils.data.SnsHibernateDao;


/**
 * sns冗余期刊数据dao 无序列供任务同步数据用
 * 
 * @author tsz
 * 
 */
@Repository
public class JournalSnsNoSeqDao extends SnsHibernateDao<JournalSnsNoSeq, Long> {

  /**
   * 添加期刊.
   * 
   * @param journal 期刊实体
   * @throws DaoException DaoException
   */
  public void addJournal(JournalSnsNoSeq journal) throws DaoException {
    super.save(journal);
  }

}
