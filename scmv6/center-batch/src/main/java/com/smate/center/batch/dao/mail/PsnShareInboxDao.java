package com.smate.center.batch.dao.mail;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;

import com.smate.center.batch.exception.pub.DaoException;
import com.smate.center.batch.model.mail.PsnInviteInbox;
import com.smate.center.batch.model.mail.PsnShareInbox;
import com.smate.core.base.utils.data.SnsHibernateDao;
import com.smate.core.base.utils.model.Page;
import com.smate.core.base.utils.security.SecurityUtils;

/**
 * 文献/文件收件箱
 * 
 * @author oyh
 * 
 */
@Repository
public class PsnShareInboxDao extends SnsHibernateDao<PsnShareInbox, Long> {
  @SuppressWarnings("unchecked")
  public Page<PsnShareInbox> getPsnInbox(Page<PsnShareInbox> page, Map paramMap) throws DaoException {
    Long psnId = SecurityUtils.getCurrentUserId();
    List paramLst = new ArrayList();
    paramLst.add(psnId);
    StringBuffer hqlCount = new StringBuffer("select count(*) from PsnShareInbox where psnId=?");
    StringBuffer hqlList = new StringBuffer("from PsnShareInbox where psnId=?");
    if (paramMap != null) {
      if (String.valueOf(paramMap.get("status")).equals("2") || paramMap.get("status") == null
          || "".equals(paramMap.get("status"))) {// 全部
        hqlCount.append(" and status in (0,1)");
        hqlList.append(" and status in (0,1)");
      } else if (String.valueOf(paramMap.get("status")).equals("0")) {// 未读
        hqlCount.append(" and status=0");
        hqlList.append(" and status=0");
      } else if (String.valueOf(paramMap.get("status")).equals("1")) {// 已读
        hqlCount.append(" and status=1");
        hqlList.append(" and status=1");
      }
      if (paramMap.containsKey("searchKey")) {// 已读
        String searchKey = paramMap.get("searchKey").toString().toLowerCase();
        hqlCount.append(
            " and (lower(mailBox.title) like ? or lower(mailBox.psnName) like ? or lower(mailBox.firstName||' '|| mailBox.lastName) like ?)");
        hqlList.append(
            " and (lower(mailBox.title) like ? or lower(mailBox.psnName) like ? or lower(mailBox.firstName||' '|| mailBox.lastName) like ?)");
        paramLst.add("%" + searchKey + "%");
        paramLst.add("%" + searchKey + "%");
        paramLst.add("%" + searchKey + "%");
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
  public Page<PsnShareInbox> getPsnInbox(Page<PsnShareInbox> page, String searchKey) throws DaoException {
    Long psnId = SecurityUtils.getCurrentUserId();

    List<PsnShareInbox> inboxs = new ArrayList<PsnShareInbox>();
    Criteria criteria =
        super.getSession().createCriteria(PsnShareInbox.class).add(Restrictions.in("status", new Object[] {0, 1}))
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

  public Criteria createCriteria(Criteria criteria, String searchKey) throws DaoException {

    return criteria.createCriteria("mailBox").add(

        Restrictions.disjunction().add(Restrictions.like("title", "%" + searchKey + "%"))
            .add(Restrictions.like("psnName", "%" + searchKey + "%"))
            .add(Restrictions.like("content", "%" + searchKey + "%"))

    );

  }

  public Criteria createBaseCriteria() throws DaoException {

    Long psnId = SecurityUtils.getCurrentUserId();
    return super.getSession().createCriteria(PsnInviteInbox.class).add(Restrictions.in("status", new Object[] {0, 1}))
        .add(Restrictions.eq("psnId", psnId));

  }

  @SuppressWarnings("unchecked")
  public PsnShareInbox getPrevPsnShareInbox(String searchKey, Long curId) throws DaoException {
    Long psnId = SecurityUtils.getCurrentUserId();
    List<PsnShareInbox> inboxs = new ArrayList<PsnShareInbox>();
    List params = new ArrayList();

    StringBuffer hql = new StringBuffer();
    hql.append("from PsnShareInbox t where t.status<>? and t.psnId=? and t.id>?");
    params.add(2);
    params.add(psnId);
    params.add(curId);

    if (StringUtils.isNotBlank(searchKey)) {
      hql.append(" and (t.mailBox.title like ? or t.mailBox.content like ? or t.mailBox.psnName like ?)");
      params.add(searchKey);
      params.add(searchKey);
      params.add(searchKey);
    }
    hql.append(" order by t.id");

    inboxs = super.createQuery(hql.toString(), params.toArray()).setMaxResults(1).list();

    return inboxs != null && inboxs.size() > 0 ? inboxs.get(0) : null;
  }

  @SuppressWarnings("unchecked")
  public PsnShareInbox getNextPsnShareInbox(String searchKey, Long curId) throws DaoException {
    Long psnId = SecurityUtils.getCurrentUserId();
    List<PsnShareInbox> inboxs = new ArrayList<PsnShareInbox>();
    List params = new ArrayList();

    StringBuffer hql = new StringBuffer();
    hql.append("from PsnShareInbox t where t.status<>? and t.psnId=? and t.id<?");
    params.add(2);
    params.add(psnId);
    params.add(curId);

    if (StringUtils.isNotBlank(searchKey)) {
      hql.append(" and (t.mailBox.title like ? or t.mailBox.content like ? or t.mailBox.psnName like ?)");
      params.add(searchKey);
      params.add(searchKey);
      params.add(searchKey);
    }
    hql.append(" order by t.id desc");

    inboxs = super.createQuery(hql.toString(), params.toArray()).setMaxResults(1).list();

    return inboxs != null && inboxs.size() > 0 ? inboxs.get(0) : null;
  }

  @SuppressWarnings("unchecked")
  public PsnShareInbox getPrev(Long curId) throws DaoException {

    String hql = "from PsnShareInbox t where t.id>? and t.status<>2 and t.psnId=?  order by t.id asc";
    Long psnId = SecurityUtils.getCurrentUserId();
    List<PsnShareInbox> inboxs = super.createQuery(hql, new Object[] {curId, psnId}).setFetchSize(1).list();

    return (inboxs == null || inboxs.size() == 0) ? null : inboxs.get(0);
  }

  @SuppressWarnings("unchecked")
  public PsnShareInbox getNext(Long curId) throws DaoException {

    String hql = "from PsnShareInbox t where t.id<? and t.status<>2 and t.psnId=? order by t.id desc";
    Long psnId = SecurityUtils.getCurrentUserId();
    List<PsnShareInbox> inboxs = super.createQuery(hql, new Object[] {curId, psnId}).setFetchSize(1).list();

    return (inboxs == null || inboxs.size() == 0) ? null : inboxs.get(0);
  }

  /**
   * 获取未读分享消息数.
   * 
   * @param psnId
   * @return
   * @throws DaoException
   */
  public Long getUnreadShareMsg(Long psnId) throws DaoException {
    return findUnique("select count(psnId) from PsnShareInbox where status=? and psnId=?", new Object[] {0, psnId});
  }

  /**
   * 获取总数.
   * 
   * @param psnId
   * @return
   * @throws DaoException
   */
  public Long getShareMsg(Long psnId) throws DaoException {
    return findUnique("select count(psnId) from PsnShareInbox where status<>? and psnId=?", new Object[] {2, psnId});
  }

  /**
   * 获取最大条未读分享消息.
   * 
   * @param psnId
   * @param maxSize
   * @return
   * @throws DaoException
   */
  @SuppressWarnings("unchecked")
  public List<PsnShareInbox> getUnreadShareMsgByMax(Long psnId, int maxSize) throws DaoException {
    String hql = "from PsnShareInbox where status=? and psnId=?";
    return super.createQuery(hql, new Object[] {0, psnId}).setMaxResults(maxSize).list();
  }
}
