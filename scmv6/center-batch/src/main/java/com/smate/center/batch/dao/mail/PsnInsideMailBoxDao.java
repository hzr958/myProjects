package com.smate.center.batch.dao.mail;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.hibernate.Query;
import org.springframework.stereotype.Repository;

import com.smate.center.batch.exception.pub.DaoException;
import com.smate.center.batch.model.mail.PsnInsideMailBox;
import com.smate.center.batch.model.sns.pub.Message;
import com.smate.core.base.utils.data.SnsHibernateDao;
import com.smate.core.base.utils.model.Page;
import com.smate.core.base.utils.security.SecurityUtils;


/**
 * 站内短信发件箱.
 * 
 * @author oyh
 * 
 */

@Repository
public class PsnInsideMailBoxDao extends SnsHibernateDao<PsnInsideMailBox, Long> {
  @SuppressWarnings("unchecked")
  public Page<PsnInsideMailBox> getPsnMailBox(Page<PsnInsideMailBox> page) throws DaoException {
    Long psnId = SecurityUtils.getCurrentUserId();
    long count = (Long) findUnique(
        "select count(*) from PsnInsideMailBox where status<>1 and senderId=" + psnId + " and  inboxs.size>0");

    page.setTotalCount(count);
    Query q = createQuery(
        "from PsnInsideMailBox where status<>1 and senderId=" + psnId + " and  inboxs.size>0 order by  mailId desc");
    setPageParameter(q, page);
    List result = q.list();
    page.setResult(result);
    return page;
  }

  @SuppressWarnings("unchecked")
  public Page<PsnInsideMailBox> getPsnMailBox(Page<PsnInsideMailBox> page, Message message) throws DaoException {
    Long psnId = SecurityUtils.getCurrentUserId();
    StringBuffer hql = new StringBuffer();
    List params = new ArrayList();
    hql.append(
        "from PsnInsideMailBox t1 where t1.status=? and t1.senderId=? and exists(select t2.id from PsnInsideInbox t2 where t1.mailId=t2.mailBox.mailId)");
    params.add(0);
    params.add(psnId);
    if (StringUtils.isNotBlank(message.getSearchKey())) {
      String searchKey = message.getSearchKey().toLowerCase();
      hql.append(
          " and (lower(t1.content) like ? or lower(t1.title) like ? or lower(t1.zhReceiver) like ? or lower(t1.enReceiver) like ?) ");
      params.add("%" + searchKey + "%");
      params.add("%" + searchKey + "%");
      params.add("%" + searchKey + "%");
      params.add("%" + searchKey + "%");

    }
    long count = (Long) findUnique("select count(*) " + hql.toString(), params.toArray());

    hql.append(" order by t1.mailId desc");
    page.setTotalCount(count);
    Query q = super.createQuery(hql.toString(), params.toArray());
    setPageParameter(q, page);
    List result = q.list();
    page.setResult(result);
    return page;
  }

  @SuppressWarnings("unchecked")
  public Page<PsnInsideMailBox> getPsnMailBoxForBpo(Page<PsnInsideMailBox> page, Message message) throws DaoException {

    StringBuilder sb = new StringBuilder();
    List params = new ArrayList();
    sb.append(" from PsnInsideMailBox t where 1=1");

    if (StringUtils.isNotBlank(message.getSearchKey())) {

      sb.append(
          " and (t.content like ? or t.title like ?  or t.psnName like ?  or t.firstName ||' '|| t.lastName like ?  )");
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
  public PsnInsideMailBox getPrev(Long curId) throws DaoException {

    String hql =
        "from PsnInsideMailBox t where t.mailId>? and t.status<>1 and t.senderId=? and t.inboxs.size>0  order by t.mailId asc";
    Long psnId = SecurityUtils.getCurrentUserId();
    List<PsnInsideMailBox> mailBoxs = super.createQuery(hql, new Object[] {curId, psnId}).setMaxResults(1).list();

    return (mailBoxs == null || mailBoxs.size() == 0) ? null : mailBoxs.get(0);
  }

  @SuppressWarnings("unchecked")
  public PsnInsideMailBox getNext(Long curId) throws DaoException {

    String hql =
        "from PsnInsideMailBox t where t.mailId<? and t.status<>1 and t.senderId=? and t.inboxs.size>0 order by t.mailId desc";
    Long psnId = SecurityUtils.getCurrentUserId();
    List<PsnInsideMailBox> mailBoxs = super.createQuery(hql, new Object[] {curId, psnId}).setMaxResults(1).list();

    return (mailBoxs == null || mailBoxs.size() == 0) ? null : mailBoxs.get(0);
  }

  @SuppressWarnings("unchecked")
  public List<PsnInsideMailBox> getMailBoxsByRecv(int maxSize) throws DaoException {
    String hql = "from PsnInsideMailBox t where t.status=0 and t.zhReceiver is null and t.enReceiver is null";
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
    String hql = "update PsnInsideMailBox t set t.zhReceiver=?,t.enReceiver=? where t.mailId=?";
    super.createQuery(hql, new Object[] {zhReceivers, enReceivers, mailId}).executeUpdate();
  }
}
