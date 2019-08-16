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
import com.smate.center.batch.model.mail.PsnMessageNoticeInBox;
import com.smate.core.base.utils.data.SnsHibernateDao;
import com.smate.core.base.utils.model.Page;
import com.smate.core.base.utils.security.SecurityUtils;

/**
 * 站内通知收件箱.
 * 
 * @author yangpeihai
 * 
 */
@Repository
public class PsnMsgNoticeInBoxDao extends SnsHibernateDao<PsnMessageNoticeInBox, Long> {
  /**
   * 根据当前用户查询站内通知箱的信息.
   * 
   * @param page
   * @return
   */
  @SuppressWarnings("unchecked")
  public Page<PsnMessageNoticeInBox> getPsnInbox(Page<PsnMessageNoticeInBox> page, Map paramMap) {
    Long currUserId = SecurityUtils.getCurrentUserId();

    StringBuffer hqlCount = new StringBuffer("select count(*) from PsnMessageNoticeInBox where senderId=?");
    StringBuffer hqlList = new StringBuffer("from PsnMessageNoticeInBox where senderId=?");
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
    }
    hqlList.append(" order by messageNoticeOutBox.optDate desc");

    Long totalCount = findUnique(hqlCount.toString(), currUserId);
    page.setTotalCount(totalCount);
    Query query = this.createQuery(hqlList.toString(), currUserId);
    this.setPageParameter(query, page);
    List lst = query.list();
    page.setResult(lst);
    return page;
  }

  @SuppressWarnings("unchecked")
  public Page<PsnMessageNoticeInBox> getPsnInbox(Page<PsnMessageNoticeInBox> page) throws DaoException {
    Long currUserId = SecurityUtils.getCurrentUserId();
    Long totalCount =
        findUnique("select count(*) from PsnMessageNoticeInBox where status in (0,1) and senderId=?", currUserId);
    page.setTotalCount(totalCount);
    Query query = this.createQuery(
        "from PsnMessageNoticeInBox where status in(0,1) and senderId=? order by messageNoticeOutBox.optDate desc",
        currUserId);
    List lst = query.list();
    page.setResult(lst);
    return page;
  }

  @SuppressWarnings("unchecked")
  public PsnMessageNoticeInBox getPrevPsnInbox(String searchKey, Long curId) throws DaoException {

    if (StringUtils.isEmpty(searchKey)) {

      return getPrev(curId);
    }

    List<PsnMessageNoticeInBox> inboxs = new ArrayList<PsnMessageNoticeInBox>();

    inboxs = createCriteria(createBaseCriteria().add(Restrictions.gt("noticeId", curId)).addOrder(Order.asc("Id")),
        searchKey).setMaxResults(1).list();

    return inboxs != null && inboxs.size() > 0 ? inboxs.get(0) : null;
  }

  @SuppressWarnings("unchecked")
  public PsnMessageNoticeInBox getNextPsnInbox(String searchKey, Long curId) throws DaoException {

    if (StringUtils.isEmpty(searchKey)) {
      return getNext(curId);
    }
    List<PsnMessageNoticeInBox> inboxs = new ArrayList<PsnMessageNoticeInBox>();

    inboxs = createCriteria(createBaseCriteria().add(Restrictions.lt("noticeId", curId)).addOrder(Order.desc("Id")),
        searchKey).setMaxResults(1).list();

    return inboxs != null && inboxs.size() > 0 ? inboxs.get(0) : null;
  }

  public Criteria createCriteria(Criteria criteria, String searchKey) throws DaoException {

    return criteria.createCriteria("messageNoticeOutBox").add(

        Restrictions.disjunction().add(Restrictions.like("title", "%" + searchKey + "%"))
            .add(Restrictions.like("psnName", "%" + searchKey + "%"))
            .add(Restrictions.like("content", "%" + searchKey + "%"))

    );

  }

  public Criteria createBaseCriteria() throws DaoException {

    Long psnId = SecurityUtils.getCurrentUserId();
    return super.getSession().createCriteria(PsnMessageNoticeInBox.class)
        .add(Restrictions.in("status", new Object[] {0, 1})).add(Restrictions.eq("senderId", psnId));

  }

  @SuppressWarnings("unchecked")
  public PsnMessageNoticeInBox getPrev(Long curId) throws DaoException {

    String hql = "from PsnMessageNoticeInBox t where t.id>? and t.status<>2 and t.senderId=?  order by t.id asc";
    Long psnId = SecurityUtils.getCurrentUserId();
    List<PsnMessageNoticeInBox> inboxs = super.createQuery(hql, new Object[] {curId, psnId}).setFetchSize(1).list();

    return (inboxs == null || inboxs.size() == 0) ? null : inboxs.get(0);
  }

  @SuppressWarnings("unchecked")
  public PsnMessageNoticeInBox getNext(Long curId) throws DaoException {

    String hql = "from PsnMessageNoticeInBox t where t.id<? and t.status<>2 and t.senderId=? order by t.id desc";
    Long psnId = SecurityUtils.getCurrentUserId();
    List<PsnMessageNoticeInBox> inboxs = super.createQuery(hql, new Object[] {curId, psnId}).setFetchSize(1).list();

    return (inboxs == null || inboxs.size() == 0) ? null : inboxs.get(0);
  }

  /**
   * 获取未读的的系统消息 数.
   * 
   * @return
   */
  public Long getUnReadMsgNoticeCount(Long psnId) {
    Long unReadCount = findUnique("select count(senderId) from PsnMessageNoticeInBox where status=? and senderId=?",
        new Object[] {0, psnId});
    return unReadCount;
  }

  public Long getMsgNoticeCount(Long psnId) {
    Long unReadCount = findUnique("select count(senderId) from PsnMessageNoticeInBox where status<>? and senderId=?",
        new Object[] {2, psnId});
    return unReadCount;
  }

  /**
   * 获取最大条系统消息.
   * 
   * @param psnId
   * @param maxSize
   * @return
   * @throws DaoException
   */
  public List<PsnMessageNoticeInBox> getUnReadMsgNoticeByMax(Long psnId, int maxSize) throws DaoException {
    String hql = "from PsnMessageNoticeInBox where status=? and senderId=?";
    return super.createQuery(hql, new Object[] {0, psnId}).setMaxResults(maxSize).list();
  }

  /**
   * 更新保存消息收件记录状态_MJG_SCM-5910.
   * 
   * @param status
   * @param id
   */
  public void updateInboxStatus(Integer status, Long id) {
    String hql = "update PsnMessageNoticeInBox t set t.status=? where t.id=? ";
    super.createQuery(hql, status, id).executeUpdate();
  }
}
