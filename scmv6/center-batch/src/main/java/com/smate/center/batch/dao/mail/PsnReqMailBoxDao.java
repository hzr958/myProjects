package com.smate.center.batch.dao.mail;

import java.util.List;

import org.hibernate.Query;
import org.springframework.stereotype.Repository;

import com.smate.center.batch.exception.pub.DaoException;
import com.smate.center.batch.model.mail.PsnReqMailBox;
import com.smate.core.base.utils.data.SnsHibernateDao;
import com.smate.core.base.utils.model.Page;
import com.smate.core.base.utils.security.SecurityUtils;


/**
 * 站内请求Dao
 * 
 * @author oyh
 * 
 */

@Repository
public class PsnReqMailBoxDao extends SnsHibernateDao<PsnReqMailBox, Long> {
  @SuppressWarnings("unchecked")
  public Page<PsnReqMailBox> getPsnMailBox(Page<PsnReqMailBox> page) throws DaoException {
    Long psnId = SecurityUtils.getCurrentUserId();
    long count = (Long) findUnique(
        "select count(*) from PsnReqMailBox where status<>1 and senderId=" + psnId + " and inboxs.size>0 ");
    page.setTotalCount(count);
    Query q = createQuery(
        "from PsnReqMailBox where status<>1 and senderId=" + psnId + "  and inboxs.size>0 order by mailId desc");
    setPageParameter(q, page);
    List result = q.list();
    page.setResult(result);
    return page;
  }

  /**
   * 更新站内邀请发件记录_MJG_SCM-5910.
   * 
   * @param id
   * @param status
   */
  public void updateReqMailBox(Long id, Integer status) {
    String hql = "update PsnReqMailBox t set t.status=? where t.mailId=? ";
    super.createQuery(hql, status, id).executeUpdate();
  }
}
