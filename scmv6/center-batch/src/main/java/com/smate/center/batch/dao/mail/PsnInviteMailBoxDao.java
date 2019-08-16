package com.smate.center.batch.dao.mail;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.hibernate.Query;
import org.springframework.stereotype.Repository;

import com.smate.center.batch.exception.pub.DaoException;
import com.smate.center.batch.model.mail.PsnInviteMailBox;
import com.smate.center.batch.model.sns.pub.Message;
import com.smate.core.base.utils.data.SnsHibernateDao;
import com.smate.core.base.utils.model.Page;
import com.smate.core.base.utils.security.SecurityUtils;

/**
 * 站内邀请Dao
 * 
 * @author oyh
 * 
 */

@Repository
@Deprecated
public class PsnInviteMailBoxDao extends SnsHibernateDao<PsnInviteMailBox, Long> {
  @SuppressWarnings("unchecked")
  public Page<PsnInviteMailBox> getPsnMailBox(Page<PsnInviteMailBox> page) throws DaoException {
    Long psnId = SecurityUtils.getCurrentUserId();
    long count = (Long) findUnique(
        "select count(*) from PsnInviteMailBox where status<>1 and senderId=" + psnId + " and inboxs.size>0");
    page.setTotalCount(count);
    Query q = createQuery(
        "from PsnInviteMailBox where status<>1 and senderId=" + psnId + " and inboxs.size>0  order by mailId desc");
    setPageParameter(q, page);
    List result = q.list();
    page.setResult(result);
    return page;
  }

  @SuppressWarnings("unchecked")
  public Page<PsnInviteMailBox> getPsnMailBox(Page<PsnInviteMailBox> page, Message message) throws DaoException {
    Long psnId = SecurityUtils.getCurrentUserId();
    StringBuffer hql = new StringBuffer();
    List params = new ArrayList();

    hql.append(
        "from PsnInviteMailBox t1 where t1.status=? and t1.senderId=? and exists(select t2.id from PsnInviteInbox t2 where t1.mailId=t2.mailBox.mailId)");
    params.add(0);
    params.add(psnId);
    if (StringUtils.isNotBlank(message.getSearchKey())) {
      String searchKey = message.getSearchKey().toLowerCase();
      hql.append(
          " and (exists(select 1 from InviteMailBoxCon t3 where t3.mailId=t1.mailId and (lower(t3.content) like ? or lower(t3.titleZh) like ? or lower(t3.titleEn) like ?)) or (lower(t1.zhReceiver) like ? or lower(t1.enReceiver) like ?))");
      params.add("%" + searchKey + "%");
      params.add("%" + searchKey + "%");
      params.add("%" + searchKey + "%");
      params.add("%" + searchKey + "%");
      params.add("%" + searchKey + "%");

    }
    long count = (Long) findUnique("select count(*) " + hql.toString(), params.toArray());
    page.setTotalCount(count);
    hql.append(" order by t1.mailId desc");
    Query q = super.createQuery(hql.toString(), params.toArray());
    setPageParameter(q, page);
    List result = q.list();
    page.setResult(result);
    return page;
  }

  @SuppressWarnings("unchecked")
  public Page<PsnInviteMailBox> getPsnMailBoxForBpo(Page<PsnInviteMailBox> page, Message message) throws DaoException {

    StringBuilder sb = new StringBuilder();
    List params = new ArrayList();
    sb.append(" from PsnInviteMailBox t where 1=1 ");

    if (StringUtils.isNotBlank(message.getSearchKey())) {

      sb.append(
          " and (t.content like ? or t.title like ?  or t.psnName like ?  or t.firstName ||' '|| t.lastName like ? )");
      params.add("%" + message.getSearchKey() + "%");
      params.add("%" + message.getSearchKey() + "%");
      params.add("%" + message.getSearchKey() + "%");
      params.add("%" + message.getSearchKey() + "%");

    }

    if (message.getFrom() != null && message.getTo() != null) {

      sb.append(" and t.optDate between ? and ? ");
      params.add(message.getFrom());
      params.add(message.getTo());

    }

    long count = (Long) super.createQuery("select count(*) " + sb.toString(), params.toArray()).uniqueResult();
    page.setTotalCount(count);
    Query q = createQuery(sb.toString() + " order by t.mailId desc", params.toArray());
    setPageParameter(q, page);
    List result = q.list();
    page.setResult(result);
    return page;
  }

  @SuppressWarnings("unchecked")
  public PsnInviteMailBox getPrev(Long curId) throws DaoException {

    String hql =
        "from PsnInviteMailBox t where t.mailId>? and t.status<>1 and senderId=? and inboxs.size>0  order by t.mailId asc";
    Long psnId = SecurityUtils.getCurrentUserId();
    List<PsnInviteMailBox> mailBoxs = super.createQuery(hql, new Object[] {curId, psnId}).setMaxResults(1).list();

    return (mailBoxs == null || mailBoxs.size() == 0) ? null : mailBoxs.get(0);
  }

  @SuppressWarnings("unchecked")
  public PsnInviteMailBox getNext(Long curId) throws DaoException {

    String hql =
        "from PsnInviteMailBox t where t.mailId<? and t.status<>1 and senderId=? and inboxs.size>0  order by t.mailId desc";
    Long psnId = SecurityUtils.getCurrentUserId();
    List<PsnInviteMailBox> mailBoxs = super.createQuery(hql, new Object[] {curId, psnId}).setMaxResults(1).list();

    return (mailBoxs == null || mailBoxs.size() == 0) ? null : mailBoxs.get(0);
  }

  @SuppressWarnings("unchecked")
  public List<PsnInviteMailBox> getMailBoxsByRecv(int maxSize) throws DaoException {
    String hql = "from PsnInviteMailBox t where t.status=0 and t.zhReceiver is null and t.enReceiver is null";
    return super.createQuery(hql).setMaxResults(maxSize).list();
  }

  /**
   * 更新冗余的收件人信息.
   * 
   * @param mailId
   * @param zhReceivers
   * @param enReceivers
   * @throws DaoException
   */
  public void updateMailBoxRecv(Long mailId, String zhReceivers, String enReceivers) throws DaoException {
    String hql = "update PsnInviteMailBox t set t.zhReceiver=?,t.enReceiver=? where t.mailId=?";
    super.createQuery(hql, new Object[] {zhReceivers, enReceivers, mailId}).executeUpdate();
  }

  /**
   * 更新站内邀请发件记录状态.
   * 
   * @param status
   * @param id
   */
  public void updateInviteMailStatus(Integer status, Long id) {
    String hql = "update PsnInviteMailBox t set t.status=? where t.mailId=? ";
    super.createQuery(hql, status, id).executeUpdate();
  }
}
