package com.smate.center.batch.dao.mail;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.hibernate.Query;
import org.springframework.stereotype.Repository;

import com.smate.center.batch.exception.pub.DaoException;
import com.smate.center.batch.model.mail.PsnShareMailBox;
import com.smate.center.batch.model.sns.pub.Message;
import com.smate.core.base.utils.data.SnsHibernateDao;
import com.smate.core.base.utils.model.Page;
import com.smate.core.base.utils.security.SecurityUtils;


/**
 * 文献/文件发件箱.
 * 
 * @author oyh
 * 
 */

@Repository
public class PsnShareMailBoxDao extends SnsHibernateDao<PsnShareMailBox, Long> {
  @SuppressWarnings("unchecked")
  public Page<PsnShareMailBox> getPsnMailBox(Page<PsnShareMailBox> page) throws DaoException {
    Long psnId = SecurityUtils.getCurrentUserId();

    long count = (Long) findUnique(
        "select count(*) from PsnShareMailBox where status<>1 and  senderId=" + psnId + " and inboxs.size>0");

    page.setTotalCount(count);
    Query q = createQuery(
        "from PsnShareMailBox where status<>1 and senderId=" + psnId + " and inboxs.size>0 order by mailId desc");
    setPageParameter(q, page);
    List result = q.list();
    page.setResult(result);
    return page;
  }

  @SuppressWarnings("unchecked")
  public Page<PsnShareMailBox> getPsnMailBox(Page<PsnShareMailBox> page, Message message) throws DaoException {
    Long psnId = SecurityUtils.getCurrentUserId();
    StringBuffer hql = new StringBuffer();
    List params = new ArrayList();
    hql.append(
        "from PsnShareMailBox t1 where t1.status=? and  t1.senderId=? and exists(select t2.id from PsnShareInbox t2 where t1.mailId=t2.mailBox.mailId)");
    params.add(0);
    params.add(psnId);
    String searchKey = message.getSearchKey();
    if (StringUtils.isNotBlank(searchKey)) {
      searchKey = searchKey.toLowerCase();
      hql.append(" and (lower(t1.title) like ? or lower(t1.zhReceiver) like ? or lower(t1.enReceiver) like ?)");
      params.add("%" + searchKey + "%");
      params.add("%" + searchKey + "%");
      params.add("%" + searchKey + "%");
    }
    long count = (Long) findUnique("select count(*) " + hql.toString(), params.toArray());
    page.setTotalCount(count);

    hql.append(" order by t1.mailId desc");
    Query q = createQuery(hql.toString(), params.toArray());
    setPageParameter(q, page);
    List result = q.list();
    page.setResult(result);
    return page;
  }

  @SuppressWarnings("unchecked")
  public PsnShareMailBox getPrev(Long curId) throws DaoException {

    String hql =
        "from PsnShareMailBox t where t.mailId>? and t.status<>1 and senderId=? and inboxs.size>0  order by t.mailId asc";
    Long psnId = SecurityUtils.getCurrentUserId();
    List<PsnShareMailBox> mailBoxs = super.createQuery(hql, new Object[] {curId, psnId}).setMaxResults(1).list();

    return (mailBoxs == null || mailBoxs.size() == 0) ? null : mailBoxs.get(0);
  }

  @SuppressWarnings("unchecked")
  public PsnShareMailBox getNext(Long curId) throws DaoException {

    String hql =
        "from PsnShareMailBox t where t.mailId<? and t.status<>1 and senderId=? and inboxs.size>0  order by t.mailId desc";
    Long psnId = SecurityUtils.getCurrentUserId();
    List<PsnShareMailBox> mailBoxs = super.createQuery(hql, new Object[] {curId, psnId}).setMaxResults(1).list();

    return (mailBoxs == null || mailBoxs.size() == 0) ? null : mailBoxs.get(0);
  }

  @SuppressWarnings("unchecked")
  public List<PsnShareMailBox> getMailBoxsByRecv(int maxSize) throws DaoException {
    String hql = "from PsnShareMailBox t where t.status=0 and t.zhReceiver is null and t.enReceiver is null";
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
    String hql = "update PsnShareMailBox t set t.zhReceiver=?,t.enReceiver=? where t.mailId=?";
    super.createQuery(hql, new Object[] {zhReceivers, enReceivers, mailId}).executeUpdate();
  }

  /**
   * 更新站内分享发件记录状态_MJG_SCM-5910.
   * 
   * @param status
   * @param id
   */
  public void updateShareMailStatus(Integer status, Long id) {
    String hql = "update PsnShareMailBox t set t.status=? where t.mailId=? ";
    super.createQuery(hql, status, id).executeUpdate();
  }
}
