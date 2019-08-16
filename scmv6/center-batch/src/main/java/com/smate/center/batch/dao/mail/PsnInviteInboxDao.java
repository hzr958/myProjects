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
import com.smate.core.base.utils.data.SnsHibernateDao;
import com.smate.core.base.utils.model.Page;
import com.smate.core.base.utils.security.SecurityUtils;

/**
 * 站内邀请收件箱
 * 
 * @author oyh
 * 
 */
@Repository
public class PsnInviteInboxDao extends SnsHibernateDao<PsnInviteInbox, Long> {
  @SuppressWarnings("unchecked")
  public Page<PsnInviteInbox> getPsnInbox(Page<PsnInviteInbox> page, Map paramMap) throws DaoException {
    Long psnId = SecurityUtils.getCurrentUserId();
    List paramLst = new ArrayList();
    StringBuffer hqlCount = new StringBuffer("select count(*) from PsnInviteInbox t where t.psnId=? and t.status<>2");
    StringBuffer hqlList = new StringBuffer("from PsnInviteInbox t where t.psnId=? and t.status<>2");
    paramLst.add(psnId);
    if (paramMap != null) {
      if (String.valueOf(paramMap.get("status")).equals("2") || paramMap.get("status") == null) {// 全部

      } else if (String.valueOf(paramMap.get("status")).equals("0")) {// 未处理
        hqlCount.append(" and t.optStatus=0");
        hqlList.append(" and t.optStatus=0");
      } else if (String.valueOf(paramMap.get("status")).equals("1")) {// 已处理
        hqlCount.append(" and t.optStatus in(1,2,3,4,5,6,7)");
        hqlList.append(" and t.optStatus in(1,2,3,4,5,6,7)");
      }
      if (paramMap.containsKey("searchKey")) {// 已读
        String searchKey = paramMap.get("searchKey").toString().toLowerCase();

        hqlCount.append(
            " and (exists(select 1 from InviteMailBoxCon t2 where t2.mailId=t.mailBox.mailId and ((lower(t2.titleZh) like ? or lower(t2.titleEn) like ? or lower(t2.content) like ?))) or (lower(t.mailBox.psnName) like ? or lower(t.mailBox.firstName||' '|| t.mailBox.lastName) like ?))");
        hqlList.append(
            " and (exists(select 1 from InviteMailBoxCon t2 where t2.mailId=t.mailBox.mailId and ((lower(t2.titleZh) like ? or lower(t2.titleEn) like ? or lower(t2.content) like ?))) or (lower(t.mailBox.psnName) like ? or lower(t.mailBox.firstName||' '|| t.mailBox.lastName) like ?))");
        paramLst.add("%" + searchKey + "%");
        paramLst.add("%" + searchKey + "%");
        paramLst.add("%" + searchKey + "%");
        paramLst.add("%" + searchKey + "%");
        paramLst.add("%" + searchKey + "%");
      }
    }
    hqlList.append(" order by t.id desc");
    long count = (Long) findUnique(hqlCount.toString(), paramLst.toArray());
    page.setTotalCount(count);
    Query q = createQuery(hqlList.toString(), paramLst.toArray());

    setPageParameter(q, page);
    List result = q.list();
    page.setResult(result);
    return page;
  }

  @SuppressWarnings("unchecked")
  public List<PsnInviteInbox> getPsnInboxById(Long[] ids) throws DaoException {
    StringBuffer hqlList = new StringBuffer("from PsnInviteInbox t where t.id in (:ids)");
    Query q = createQuery(hqlList.toString()).setParameterList("ids", ids);

    return q.list();
  }

  @SuppressWarnings("unchecked")
  public Page<PsnInviteInbox> getPsnInbox(Page<PsnInviteInbox> page, String searchKey) throws DaoException {

    List<PsnInviteInbox> inboxs = new ArrayList<PsnInviteInbox>();

    Criteria criteria = createCriteria(createBaseCriteria().addOrder(Order.desc("id")), searchKey);

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
  public PsnInviteInbox getPrevPsnInviteInbox(String searchKey, Long curId) throws DaoException {
    Long psnId = SecurityUtils.getCurrentUserId();
    List<PsnInviteInbox> inboxs = new ArrayList<PsnInviteInbox>();
    List params = new ArrayList();
    StringBuffer hql = new StringBuffer();
    hql.append("from PsnInviteInbox t where t.status<> and t.psnId=? and t.id>?");
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
  public PsnInviteInbox getNextPsnInviteInbox(String searchKey, Long curId) throws DaoException {
    Long psnId = SecurityUtils.getCurrentUserId();
    List<PsnInviteInbox> inboxs = new ArrayList<PsnInviteInbox>();
    List params = new ArrayList();
    StringBuffer hql = new StringBuffer();
    hql.append("from PsnInviteInbox t where t.status<> and t.psnId=? and t.id<?");
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
  public PsnInviteInbox getPrev(Long curId) throws DaoException {

    String hql = "from PsnInviteInbox t where t.id>? and t.status<>2 and t.psnId=?  order by t.id asc";
    Long psnId = SecurityUtils.getCurrentUserId();
    List<PsnInviteInbox> inboxs = super.createQuery(hql, new Object[] {curId, psnId}).setFetchSize(1).list();

    return (inboxs == null || inboxs.size() == 0) ? null : inboxs.get(0);
  }

  @SuppressWarnings("unchecked")
  public PsnInviteInbox getNext(Long curId) throws DaoException {

    String hql = "from PsnInviteInbox t where t.id<? and t.status<>2 and t.psnId=? order by t.id desc";
    Long psnId = SecurityUtils.getCurrentUserId();
    List<PsnInviteInbox> inboxs = super.createQuery(hql, new Object[] {curId, psnId}).setFetchSize(1).list();

    return (inboxs == null || inboxs.size() == 0) ? null : inboxs.get(0);
  }

  public PsnInviteInbox getPsnInviteInboxById(Long id) throws DaoException {
    String hql = "from PsnInviteInbox t where t.id = ? ";
    Query q = super.createQuery(hql, id);
    return (PsnInviteInbox) q.uniqueResult();
  }

  /**
   * 获取用户未读请求数.
   * 
   * @param psnId
   * @return
   * @throws DaoException
   */
  public Long getUnreadInviteMsg(Long psnId) throws DaoException {
    return findUnique("select count(psnId) from PsnInviteInbox where optStatus =? and psnId=? and status<>?",
        new Object[] {0, psnId, 2});
  }

  /**
   * 获取总数.
   * 
   * @param psnId
   * @return
   * @throws DaoException
   */
  public Long getInviteMsg(Long psnId) throws DaoException {
    return findUnique("select count(psnId) from PsnInviteInbox where psnId=? and status<>?", new Object[] {psnId, 2});
  }

  /**
   * 获取最大条未读请求.
   * 
   * @param psnId
   * @param maxSize
   * @return
   * @throws DaoException
   */
  @SuppressWarnings("unchecked")
  public List<PsnInviteInbox> getUnreadInviteMsgByMax(Long psnId, int maxSize) throws DaoException {
    String hql = "from PsnInviteInbox where optStatus =? and psnId=?";
    return super.createQuery(hql, new Object[] {0, psnId}).setMaxResults(maxSize).list();
  }

  /**
   * 查询某个人的某种邀请信息
   * 
   * @param psnId
   * @param inviteType
   * @param maxSize
   * @return
   * @throws DaoException
   */
  public List<PsnInviteInbox> findInviteReg(Long psnId, Integer inviteType, int maxSize) throws DaoException {
    String hql =
        "select t from PsnInviteInbox t where t.psnId = ? and t.mailBox.inviteType = ? and t.mailBox.senderId<>-1 and t.status<>2 and t.optStatus = 0 order by t.mailBox.optDate desc";
    return super.createQuery(hql, psnId, inviteType).setMaxResults(maxSize).list();
  }
}
