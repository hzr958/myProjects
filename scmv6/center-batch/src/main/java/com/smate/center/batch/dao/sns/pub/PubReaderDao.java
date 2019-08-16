package com.smate.center.batch.dao.sns.pub;

import java.util.List;

import org.hibernate.Query;
import org.springframework.stereotype.Repository;

import com.smate.center.batch.form.pub.PubReaderForm;
import com.smate.center.batch.model.sns.pub.PubReader;
import com.smate.core.base.utils.data.SnsHibernateDao;
import com.smate.core.base.utils.model.Page;


@Repository
public class PubReaderDao extends SnsHibernateDao<PubReader, Long> {

  public void deletePubReader(Long pubId) {
    String hql = "delete from PubReader where pubId=?";
    super.createQuery(hql, pubId).executeUpdate();
  }

  @SuppressWarnings("unchecked")
  public List<Long> getPubReaders(Long pubId, Long psnId) {
    String hql = "select psnId from PubReader where pubId=? and psnId not in(?)";
    return super.createQuery(hql, pubId, psnId).list();
  }

  @SuppressWarnings({"unchecked", "rawtypes"})
  public Page getPubReaders(Page<PubReaderForm> page, Long pubId) {
    String hql = "select new com.iris.scm.scmweb.model.person.PubReaderForm(pubId,psnId) from PubReader where pubId=?";
    Query q = createQuery(hql, pubId);
    if (page.isAutoCount()) {
      long totalCount = countHqlResult(hql, pubId);
      page.setTotalCount(totalCount);
    }
    q.setFirstResult(page.getFirst() - 1);
    q.setMaxResults(page.getPageSize());
    List result = q.list();
    page.setResult(result);
    return page;
  }

  public void ajaxDelPubReader(Long pubId, Long psnId) {
    String hql = "delete from PubReader where pubId=? and psnId=?";
    createQuery(hql, pubId, psnId).executeUpdate();
  }
}
