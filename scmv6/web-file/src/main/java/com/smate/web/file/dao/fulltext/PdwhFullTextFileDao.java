package com.smate.web.file.dao.fulltext;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.smate.core.base.utils.data.PdwhHibernateDao;
import com.smate.web.file.model.fulltext.pdwh.PdwhFullTextFile;

/**
 * 基准库全文附件Dao
 * 
 * @author houchuanjie
 *
 */
@Repository
public class PdwhFullTextFileDao extends PdwhHibernateDao<PdwhFullTextFile, Long> {
  @SuppressWarnings("unchecked")
  public PdwhFullTextFile getByPubId(Long pubId) {
    String hql = "select t from PdwhFullTextFile t where t.pubId=? order by t.fileId desc";
    List<PdwhFullTextFile> list = super.createQuery(hql, pubId).list();
    if (list != null && list.size() > 0) {
      return list.get(0);
    }
    return null;
  }

  public PdwhFullTextFile getByFileId(Long pubId) {
    String hql = "select t from PdwhFullTextFile t where t.fileId=? ";
    return (PdwhFullTextFile) super.createQuery(hql, pubId).uniqueResult();
  }
}
