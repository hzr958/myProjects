package com.smate.center.batch.dao.mail;

import java.util.List;

import org.hibernate.Query;
import org.springframework.stereotype.Repository;

import com.smate.center.batch.exception.pub.DaoException;
import com.smate.center.batch.model.mail.PsnMessageNoticeOutBox;
import com.smate.core.base.utils.data.SnsHibernateDao;
import com.smate.core.base.utils.model.Page;
import com.smate.core.base.utils.security.SecurityUtils;


/**
 * 站内通知发件箱
 * 
 * @author yangpeihai
 * 
 */

@Repository
public class PsnMsgNoticeOutBoxDao extends SnsHibernateDao<PsnMessageNoticeOutBox, Long> {
  /**
   * 获取所有状态为非删除的通知
   * 
   * @param page
   * @return
   * @throws DaoException
   */
  @SuppressWarnings("unchecked")
  public Page<PsnMessageNoticeOutBox> getPsnMailBox(Page<PsnMessageNoticeOutBox> page) throws DaoException {
    Long psnId = SecurityUtils.getCurrentUserId();
    long count = (Long) findUnique(
        "select count(*) from PsnMessageNoticeOutBox where status<>1 and senderId=? and inboxs.size>0", psnId);
    page.setTotalCount(count);
    Query q = createQuery(
        "from PsnMessageNoticeOutBox where status<>1 and senderId=? and inboxs.size>0 order by noticeId desc", psnId);
    setPageParameter(q, page);
    List result = q.list();
    page.setResult(result);
    return page;
  }

  /**
   * 上一条通知
   * 
   * @param curId
   * @return
   * @throws DaoException
   */
  @SuppressWarnings("unchecked")
  public PsnMessageNoticeOutBox getPrev(Long curId) throws DaoException {

    String hql =
        "from PsnMessageNoticeOutBox t where t.noticeId>? and t.status<>1 and t.senderId=? and t.inboxs.size>0 order by t.noticeId asc";
    Long psnId = SecurityUtils.getCurrentUserId();
    List<PsnMessageNoticeOutBox> mailBoxs = super.createQuery(hql, new Object[] {curId, psnId}).setFetchSize(1).list();
    return (mailBoxs == null || mailBoxs.size() == 0) ? null : mailBoxs.get(0);
  }

  /**
   * 下一条通知
   * 
   * @param curId
   * @return
   * @throws DaoException
   */
  @SuppressWarnings("unchecked")
  public PsnMessageNoticeOutBox getNext(Long curId) throws DaoException {

    String hql =
        "from PsnMessageNoticeOutBox t where t.noticeId<? and t.status<>1 and senderId=? and t.inboxs.size>0 order by t.noticeId desc";
    Long psnId = SecurityUtils.getCurrentUserId();
    List<PsnMessageNoticeOutBox> mailBoxs = super.createQuery(hql, new Object[] {curId, psnId}).setFetchSize(1).list();
    return (mailBoxs == null || mailBoxs.size() == 0) ? null : mailBoxs.get(0);
  }

}
