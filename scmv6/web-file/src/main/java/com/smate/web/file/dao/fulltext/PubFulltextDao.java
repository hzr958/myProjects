package com.smate.web.file.dao.fulltext;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

import com.smate.core.base.utils.data.SnsHibernateDao;
import com.smate.web.file.model.fulltext.PubFulltext;

/**
 * 成果全文Dao.
 * 
 * @author tsz
 * 
 */
@Repository
public class PubFulltextDao extends SnsHibernateDao<PubFulltext, Long> {
  private Logger logger = LoggerFactory.getLogger(getClass());

  /**
   * 根据文件ID获取全文信息
   * 
   * @param fileId
   * @return
   */

  public PubFulltext getPubFulltextByFiledID(Long fileId) {

    PubFulltext pubFulltext = null;
    try {
      String hql = "from PubFulltext t where t.fulltextFileId=:fulltextFileId";
      pubFulltext = (PubFulltext) super.createQuery(hql).setParameter("fulltextFileId", fileId).uniqueResult();

    } catch (Exception e) {
      logger.error("获取全文附件出现错误，fileId={}, 原因：{}", fileId, e.getMessage());
      throw e;
    }
    return pubFulltext;
  }
}
