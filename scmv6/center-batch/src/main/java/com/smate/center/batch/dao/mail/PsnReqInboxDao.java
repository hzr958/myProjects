package com.smate.center.batch.dao.mail;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;

import com.smate.center.batch.exception.pub.DaoException;
import com.smate.center.batch.model.mail.PsnReqInbox;
import com.smate.core.base.utils.data.SnsHibernateDao;
import com.smate.core.base.utils.model.Page;
import com.smate.core.base.utils.security.SecurityUtils;

/**
 * 站内请求收件箱.
 * 
 * @author oyh
 * 
 */
@Repository
public class PsnReqInboxDao extends SnsHibernateDao<PsnReqInbox, Long> {
  @SuppressWarnings("unchecked")
  public Page<PsnReqInbox> getPsnInbox(Page<PsnReqInbox> page, Map paramMap) throws DaoException {
    Long psnId = SecurityUtils.getCurrentUserId();
    List paramLst = new ArrayList();
    paramLst.add(psnId);
    StringBuffer hqlCount = new StringBuffer("select count(*) from PsnReqInbox where psnId=?");
    StringBuffer hqlList = new StringBuffer("from PsnReqInbox where psnId=?");
    if (paramMap != null) {
      if (String.valueOf(paramMap.get("status")).equals("2") || paramMap.get("status") == null) {// 全部
        hqlCount.append(" and status in (0,1)");
        hqlList.append(" and status in (0,1)");
      } else if (String.valueOf(paramMap.get("status")).equals("0")) {// 未读
        hqlCount.append(" and status=0");
        hqlList.append(" and status=0");
      } else if (String.valueOf(paramMap.get("status")).equals("1")) {// 已读
        hqlCount.append(" and status=1");
        hqlList.append(" and status=1");
      }
      if (paramMap.get("searcKey") != null) {// 已读
        hqlCount.append(" and (mailBox.title like ? or mailBox.psnName like ?)");
        hqlList.append(" and (mailBox.title like ? or mailBox.psnName like ?)");
        paramLst.add("%" + paramMap.get("searcKey") + "%");
        paramLst.add("%" + paramMap.get("searcKey") + "%");
      }
    }
    hqlList.append(" order by id desc");

    long count = (Long) findUnique(hqlCount.toString(), paramLst.toArray());
    page.setTotalCount(count);
    Query q = createQuery(hqlList.toString(), paramLst.toArray());
    setPageParameter(q, page);
    List result = q.list();
    page.setResult(result);
    return page;
  }

  @SuppressWarnings("unchecked")
  public Page<PsnReqInbox> getPsnInbox(Page<PsnReqInbox> page, String searchKey) throws DaoException {
    Long psnId = SecurityUtils.getCurrentUserId();

    List<PsnReqInbox> inboxs = new ArrayList<PsnReqInbox>();
    Criteria criteria =
        super.getSession().createCriteria(PsnReqInbox.class).add(Restrictions.in("status", new Object[] {0, 1}))
            .add(Restrictions.eq("psnId", psnId)).createCriteria("mailBox").add(

                Restrictions.disjunction().add(Restrictions.like("title", "%" + searchKey + "%"))
                    .add(Restrictions.like("psnName", "%" + searchKey + "%")))
            .addOrder(Order.desc("id"));

    inboxs = criteria.list();

    page.setTotalCount(inboxs.size());
    setPageParameter(criteria, page);
    page.setResult(criteria.list());

    return page;
  }

  /**
   * 更新站内请求收件状态_MJG_SCM-5910.
   * 
   * @param status
   * @param id
   */
  public void updateReqInboxStatus(Integer status, Long id) {
    String hql = "update PsnReqInbox t set t.status=? where t.id=? ";
    super.createQuery(hql, status, id).executeUpdate();
  }

}
