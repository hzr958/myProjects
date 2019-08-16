package com.smate.center.batch.dao.mail;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.hibernate.Query;
import org.springframework.stereotype.Repository;

import com.smate.center.batch.exception.pub.DaoException;
import com.smate.center.batch.model.mail.FullTextInBox;
import com.smate.core.base.utils.data.SnsHibernateDao;
import com.smate.core.base.utils.model.Page;


/**
 * 全文请求收件箱Dao.
 * 
 * @author cxr
 * 
 */
@Repository
public class FullTextInboxDao extends SnsHibernateDao<FullTextInBox, Long> {
  /**
   * 获取某人收件箱列表.
   * 
   * @param receiverId
   * @return
   * @throws DaoException
   */
  @SuppressWarnings("unchecked")
  public List<FullTextInBox> getInboxList(Long receiverId, Integer opStatus) throws DaoException {
    StringBuffer hql = new StringBuffer();
    hql.append("from FullTextInBox t where t.status=:status and t.receiverId=:receiverId");
    if (opStatus != null) {
      hql.append(" and t.opStatus=:opStatus");
    }

    Query query = super.createQuery(hql.toString());
    query.setParameter("status", 0);
    query.setParameter("receiverId", receiverId);
    if (opStatus != null) {
      query.setParameter("opStatus", opStatus);
    }

    return query.list();
  }

  /**
   * 分页获取收件列表.
   * 
   * @param receiverId
   * @param opStatus
   * @param page
   * @return
   * @throws DaoException
   */
  @SuppressWarnings({"rawtypes", "unchecked"})
  public Page<FullTextInBox> getInboxPage(Long receiverId, Integer opStatus, String searchKey, Page<FullTextInBox> page)
      throws DaoException {
    StringBuffer hql = new StringBuffer();
    List params = new ArrayList();
    hql.append("from FullTextInBox t where t.status=? and t.receiverId=?");
    params.add(0);
    params.add(receiverId);
    if (opStatus != null) {
      if (opStatus == 3) {// 已处理
        hql.append(" and t.opStatus in(?,?)");
        params.add(1);
        params.add(2);
      } else if (opStatus == 0) {
        hql.append(" and t.opStatus=?");
        params.add(0);
      }
    }

    if (StringUtils.isNotBlank(searchKey)) {
      hql.append(" and (t.mailTitle like ? or t.mailEnTitle like ? or t.paramJson like ?)");
      params.add("%" + searchKey + "%");
      params.add("%" + searchKey + "%");
      params.add("%" + searchKey + "%");
    }
    hql.append(" order by t.inboxId desc");
    // 查询总页数
    Query queryCt = super.createQuery("select count(t.inboxId) " + hql.toString(), params.toArray());
    Long count = (Long) queryCt.uniqueResult();
    page.setTotalCount(count.intValue());

    // 查询数据实体
    Query queryResult = super.createQuery(hql.toString(), params.toArray());
    queryResult.setFirstResult(page.getFirst() - 1);
    queryResult.setMaxResults(page.getPageSize());
    page.setResult(queryResult.list());
    return page;
  }

  /**
   * 更新收件状态.
   * 
   * @param inboxId
   * @param status
   * @throws DaoException
   */
  public void updateInboxStatus(Long inboxId, int status) throws DaoException {
    String hql = "update FullTextInBox t set t.status=? where t.inboxId=?";
    super.createQuery(hql, new Object[] {status, inboxId}).executeUpdate();
  }

  /**
   * 批量更新收件状态.
   * 
   * @param inboxIds
   * @param status
   * @throws DaoException
   */
  public void updateInboxStatusBatch(List<Long> inboxIds, int status) throws DaoException {
    String hql = "update FullTextInBox t set t.status=:status where t.inboxId in(:inboxIds)";
    super.createQuery(hql).setParameter("status", status).setParameterList("inboxIds", inboxIds).executeUpdate();
  }

  /**
   * 操作收件.
   * 
   * @param inboxId
   * @param opStatus
   * @throws DaoException
   */
  public void updateInboxOpStatus(Long inboxId, int opStatus) throws DaoException {
    String hql = "update FullTextInBox t set t.opStatus=? where t.inboxId=?";
    super.createQuery(hql, new Object[] {opStatus, inboxId}).executeUpdate();
  }

  /**
   * 批量操作收件.
   * 
   * @param inboxIds
   * @param opStatus
   * @throws DaoException
   */
  public void updateInboxOpStatusBatch(List<Long> inboxIds, int opStatus) throws DaoException {
    String hql = "update FullTextInBox t set t.opStatus=:opStatus where t.inboxId in(:inboxIds)";
    super.createQuery(hql).setParameter("opStatus", opStatus).setParameterList("inboxIds", inboxIds).executeUpdate();

  }

  /**
   * 获取未处理消息数.
   * 
   * @param receiverId
   * @return
   * @throws DaoException
   */
  public int getUnReadCount(Long receiverId) throws DaoException {
    String hql = "select count(t.inboxId) from FullTextInBox t where t.opStatus=? and t.status=? and t.receiverId=?";
    Long count = (Long) super.createQuery(hql, new Object[] {0, 0, receiverId}).uniqueResult();
    return count.intValue();
  }

  /**
   * 获取全文请求总数.
   * 
   * @param receiverId
   * @return
   * @throws DaoException
   */
  public int getTotalCount(Long receiverId) throws DaoException {
    String hql = "select count(t.inboxId) from FullTextInBox t where t.status=? and t.receiverId=?";
    Long count = (Long) super.createQuery(hql, new Object[] {0, receiverId}).uniqueResult();
    return count.intValue();
  }

  /**
   * 根据人员ID删除收件箱记录.
   * 
   * @param psnId
   */
  public void delInBoxByPsnId(Long psnId) {
    String hql = "delete from FullTextInBox t where receiverId=?";
    super.createQuery(hql, new Object[] {psnId}).executeUpdate();
  }

  /**
   * 获取全文检索收件箱记录列表.
   * 
   * @param psnId
   * @return
   */
  @SuppressWarnings("unchecked")
  public List<FullTextInBox> getFullTextInBoxList(Long psnId) {
    String ql = "from FullTextInBox where receiverId = ?";
    return super.createQuery(ql, psnId).list();
  }

  /**
   * 根据mailId查找FullTextInBox
   * 
   * @param mailId
   * @return
   * @throws DaoException
   */
  public List<FullTextInBox> findFullTextInBoxListByMailId(Long mailId) throws DaoException {
    String hql = "from FullTextInBox t where t.mailId = ?";
    return this.createQuery(hql, mailId).list();
  }
}
