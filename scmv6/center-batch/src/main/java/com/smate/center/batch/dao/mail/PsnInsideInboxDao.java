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
import com.smate.center.batch.model.mail.PsnInsideInbox;
import com.smate.core.base.utils.data.SnsHibernateDao;
import com.smate.core.base.utils.model.Page;
import com.smate.core.base.utils.security.SecurityUtils;


/**
 * 站内短信收件箱.
 * 
 * @author oyh
 * 
 */
@Repository
public class PsnInsideInboxDao extends SnsHibernateDao<PsnInsideInbox, Long> {
  @SuppressWarnings("unchecked")
  public Page<PsnInsideInbox> getPsnInbox(Page<PsnInsideInbox> page, Map paramMap) throws DaoException {
    Long psnId = SecurityUtils.getCurrentUserId();
    List paramLst = new ArrayList();
    StringBuffer hqlCount = new StringBuffer("select count(*) from PsnInsideInbox where psnId=?");
    StringBuffer hqlList = new StringBuffer("from InsideInbox where psnId=?");
    paramLst.add(psnId);
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
            " and (lower(mailBox.title) like ? or lower(mailBox.psnName) like ? or lower(mailBox.firstName||' '|| mailBox.lastName) like ? or lower(mailBox.content) like ?)");
        hqlList.append(
            " and (lower(mailBox.title) like ? or lower(mailBox.psnName) like ? or lower(mailBox.firstName||' '|| mailBox.lastName) like ? or lower(mailBox.content) like ?)");
        paramLst.add("%" + searchKey + "%");
        paramLst.add("%" + searchKey + "%");
        paramLst.add("%" + searchKey + "%");
        paramLst.add("%" + searchKey + "%");
      }
    }
    hqlList.append(" order by id desc");
    long count = (Long) findUnique(hqlCount.toString(), paramLst.toArray());
    page.setTotalCount(count);
    Query q = createQuery(hqlList.toString(), paramLst.toArray());
    q = setPageParameter(q, page);
    List result = q.list();
    page.setResult(result);
    return page;
  }

  @SuppressWarnings("unchecked")
  public Page<PsnInsideInbox> getPsnInbox(Page<PsnInsideInbox> page, String searchKey) throws DaoException {

    List<PsnInsideInbox> inboxs = new ArrayList<PsnInsideInbox>();
    Criteria criteria = createCriteria(createBaseCriteria().addOrder(Order.desc("id")), searchKey);
    inboxs = criteria.list();

    page.setTotalCount(inboxs.size());
    setPageParameter(criteria, page);
    page.setResult(criteria.list());

    return page;
  }

  @SuppressWarnings("unchecked")
  public PsnInsideInbox getPrevPsnInbox(String searchKey, Long curId) throws DaoException {
    Long psnId = SecurityUtils.getCurrentUserId();
    List<PsnInsideInbox> inboxs = new ArrayList<PsnInsideInbox>();
    List params = new ArrayList();

    StringBuffer hql = new StringBuffer();
    hql.append("from PsnInsideInbox t where t.status<>? and t.psnId=? and t.id>?");
    params.add(2);
    params.add(psnId);
    params.add(curId);
    if (StringUtils.isNotBlank(searchKey)) {
      hql.append(" and (t.mailBox.title like ? or t.mailBox.psnName like ? or t.mailBox.content like ?)");
      params.add(searchKey);
      params.add(searchKey);
      params.add(searchKey);
    }
    hql.append(" order by t.id");
    inboxs = super.createQuery(hql.toString(), params.toArray()).setMaxResults(1).list();

    return inboxs != null && inboxs.size() > 0 ? inboxs.get(0) : null;
  }

  @SuppressWarnings("unchecked")
  public PsnInsideInbox getNextPsnInbox(String searchKey, Long curId) throws DaoException {
    Long psnId = SecurityUtils.getCurrentUserId();
    List<PsnInsideInbox> inboxs = new ArrayList<PsnInsideInbox>();
    List params = new ArrayList();

    StringBuffer hql = new StringBuffer();
    hql.append("from PsnInsideInbox t where t.status<>? and t.psnId=? and t.id<?");
    params.add(2);
    params.add(psnId);
    params.add(curId);
    if (StringUtils.isNotBlank(searchKey)) {
      hql.append(" and (t.mailBox.title like ? or t.mailBox.psnName like ? or t.mailBox.content like ?)");
      params.add(searchKey);
      params.add(searchKey);
      params.add(searchKey);
    }
    hql.append(" order by t.id desc");
    inboxs = super.createQuery(hql.toString(), params.toArray()).setMaxResults(1).list();

    return inboxs != null && inboxs.size() > 0 ? inboxs.get(0) : null;
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
    return super.getSession().createCriteria(PsnInsideInbox.class).add(Restrictions.in("status", new Object[] {0, 1}))
        .add(Restrictions.eq("psnId", psnId));

  }

  @SuppressWarnings("unchecked")
  public PsnInsideInbox getPrev(Long curId) throws DaoException {

    String hql = "from PsnInsideInbox t where t.id>? and t.status<>2 and t.psnId=?  order by t.id asc";
    Long psnId = SecurityUtils.getCurrentUserId();
    List<PsnInsideInbox> inboxs = super.createQuery(hql, new Object[] {curId, psnId}).setFetchSize(1).list();

    return (inboxs == null || inboxs.size() == 0) ? null : inboxs.get(0);
  }

  @SuppressWarnings("unchecked")
  public PsnInsideInbox getNext(Long curId) throws DaoException {

    String hql = "from PsnInsideInbox t where t.id<? and t.status<>2 and t.psnId=? order by t.id desc";
    Long psnId = SecurityUtils.getCurrentUserId();
    List<PsnInsideInbox> inboxs = super.createQuery(hql, new Object[] {curId, psnId}).setFetchSize(1).list();

    return (inboxs == null || inboxs.size() == 0) ? null : inboxs.get(0);
  }

  /**
   * 获取指定用户未读数.
   * 
   * @return
   * @throws DaoException
   */
  public Long getUnreadInsideMsg(Long psnId) throws DaoException {
    return findUnique("select count(psnId) from PsnInsideInbox where status=? and psnId=?", new Object[] {0, psnId});
  }

  /**
   * 获取总数.
   * 
   * @return
   * @throws DaoException
   */
  public Long getInsideMsg(Long psnId) throws DaoException {
    return findUnique("select count(psnId) from PsnInsideInbox where status<>? and psnId=?", new Object[] {2, psnId});
  }

  /**
   * 获取最大条未读短信.
   * 
   * @param psnId
   * @param maxSize
   * @return
   * @throws DaoException
   */
  @SuppressWarnings("unchecked")
  public List<PsnInsideInbox> getUnreadInsideMsgByMax(Long psnId, int maxSize) throws DaoException {
    String hql = "from PsnInsideInbox where status=? and psnId=?";
    return super.createQuery(hql, new Object[] {0, psnId}).setMaxResults(maxSize).list();
  }
}
