package com.smate.center.task.dao.pdwh.quartz;

import java.util.List;

import org.apache.commons.collections4.CollectionUtils;
import org.springframework.stereotype.Repository;

import com.smate.center.task.model.pdwh.quartz.PdwhFullTextFile;
import com.smate.core.base.utils.data.PdwhHibernateDao;

/**
 * 基准库全文文件dao
 * 
 * @author zjh
 *
 */
@Repository
public class PdwhFullTextFileDao extends PdwhHibernateDao<PdwhFullTextFile, Long> {

  public Long getCountByPubAllId(Long pubId) {
    String hql = "select count(1) from PdwhFullTextFile t where t.pubId =:pubId";
    return (Long) super.createQuery(hql).setParameter("pubId", pubId).uniqueResult();
  }

  public Long getFileIdByPubId(Long pubId) {
    String hql = "select t.fileId from PdwhFullTextFile t where t.pubId =:pubId order by t.fileId desc";
    List<Long> list = super.createQuery(hql).setParameter("pubId", pubId).list();

    if (CollectionUtils.isEmpty(list)) {
      return null;
    } else {
      return list.get(0);
    }
  }

  public List<Long> findFileIdList(Long pubId) {
    String hql = "select t.fileId from PdwhFullTextFile t where t.pubId =:pubId";
    return super.createQuery(hql).setParameter("pubId", pubId).list();
  }

  @SuppressWarnings("unchecked")
  public List<PdwhFullTextFile> findFileList(Long pubId) {
    String hql = "from PdwhFullTextFile t where t.pubId =:pubId order by t.fileId asc";
    return super.createQuery(hql).setParameter("pubId", pubId).list();
  }
}
