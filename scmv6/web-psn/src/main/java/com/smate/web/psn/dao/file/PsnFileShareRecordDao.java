package com.smate.web.psn.dao.file;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.smate.core.base.utils.data.SnsHibernateDao;
import com.smate.core.base.utils.model.Page;
import com.smate.web.psn.exception.DaoException;
import com.smate.web.psn.model.file.PsnFileShareRecord;

/**
 * 个人文件分享记录dao
 * 
 * @author Administrator
 *
 */
@Repository
public class PsnFileShareRecordDao extends SnsHibernateDao<PsnFileShareRecord, Long> {

  @SuppressWarnings("unchecked")
  public List<PsnFileShareRecord> findListByShareBaseId(Long shareBaseId) {
    String hql = "from PsnFileShareRecord t where t.shareBaseId=:shareBaseId order by t.createDate desc ,t.id desc ";
    return this.createQuery(hql).setParameter("shareBaseId", shareBaseId).list();
  }

  @SuppressWarnings("unchecked")
  public List<Long> findFileIdByShareBaseId(Long shareBaseId) {
    String hql = "select distinct t.fileId from PsnFileShareRecord t where t.shareBaseId=:shareBaseId ";
    return this.createQuery(hql).setParameter("shareBaseId", shareBaseId).list();
  }

  @SuppressWarnings("unchecked")
  public List<PsnFileShareRecord> findPsnIdByShareBaseId(Long shareBaseId) {
    String hql = "from PsnFileShareRecord t where t.shareBaseId=:shareBaseId ";
    return this.createQuery(hql).setParameter("shareBaseId", shareBaseId).list();
  }

  public List<PsnFileShareRecord> queryPsnResSendResByPage(Long resSendId, Long resReveiverId, Long baseId)
      throws DaoException {
    String hql =
        "from PsnFileShareRecord t where t.sharerId=:resSendId and t.reveiverId=:resReveiverId and t.status=0 and t.shareBaseId=:baseId";
    /*
     * Page<PsnFileShareRecord> page = new Page<PsnFileShareRecord>(); Long count = (Long)
     * super.createQuery("select count(t.id) " + hql, new Object[] { resSendId, resReveiverId, baseId
     * }).uniqueResult(); page.setTotalCount(count);
     */

    /*
     * List<PsnFileShareRecord> list = super.createQuery(hql + " order by t.id", new Object[] {
     * resSendId, resReveiverId, baseId }).list(); page.setResult(list);
     */

    return super.createQuery(hql).setParameter("resSendId", resSendId).setParameter("resReveiverId", resReveiverId)
        .setParameter("baseId", baseId).list();
  }

  public Page<PsnFileShareRecord> queryPsnResSendResByPage(Long resSendId, Long resReveiverId, int pageNo, int pageSize,
      Long baseId) throws DaoException {
    String hql = "from PsnFileShareRecord t where t.sharerId=? and t.reveiverId=? and t.status=0 and t.shareBaseId=?";
    Page<PsnFileShareRecord> page = new Page<PsnFileShareRecord>();
    page.setPageNo(pageNo);
    page.setPageSize(pageSize);

    Long count = (Long) super.createQuery("select count(t.id) " + hql, new Object[] {resSendId, resReveiverId, baseId})
        .uniqueResult();
    page.setTotalCount(count);

    List<PsnFileShareRecord> list =
        super.createQuery(hql + " order by t.id", new Object[] {resSendId, resReveiverId, baseId})
            .setFirstResult(page.getFirst() - 1).setMaxResults(Integer.valueOf(count.toString())).list();
    page.setResult(list);

    return page;
  }
}
