package com.smate.center.batch.dao.mail;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.hibernate.Query;
import org.springframework.stereotype.Repository;

import com.smate.center.batch.exception.pub.DaoException;
import com.smate.center.batch.model.mail.ShareInbox;
import com.smate.center.batch.model.sns.pub.SnsPersonSyncMessage;
import com.smate.core.base.utils.data.SnsHibernateDao;
import com.smate.core.base.utils.model.Page;
import com.smate.core.base.utils.security.SecurityUtils;

/**
 * 文献/文件收件箱.
 * 
 * @author oyh
 * 
 */
@Repository
public class ShareInboxDao extends SnsHibernateDao<ShareInbox, Long> {

  @SuppressWarnings("unchecked")
  public Page<ShareInbox> queryShareInboxByPage(Page<ShareInbox> page, Map<String, String> paramMap)
      throws DaoException {
    List<Object> paramLst = new ArrayList<Object>();
    StringBuffer hql = new StringBuffer("from ShareInbox t where t.psnId=?");
    paramLst.add(SecurityUtils.getCurrentUserId());

    if (paramMap != null) {
      if ("0".equals(String.valueOf(paramMap.get("status")))) {// 未读
        hql.append(" and t.status=0");
      } else if ("1".equals(String.valueOf(paramMap.get("status")))) {// 已读
        hql.append(" and t.status=1");
      } else {// 全部
        hql.append(" and t.status in (0, 1)");
      }
      if (StringUtils.isNotBlank(paramMap.get("searchKey"))) {// 已读
        String searchKey = paramMap.get("searchKey").toString().toLowerCase();
        hql.append(
            " and (exists(select 1 from ShareInboxCon t2 where t2.inboxId=t.id and (lower(t2.titleZh) like ? or lower(t2.titleEn) like ?)) or "
                + "exists(select 1 from Person p where p.personId = t.senderId and (lower(p.name) like ? or lower(p.ename) like ?)))");
        paramLst.add("%" + searchKey + "%");
        paramLst.add("%" + searchKey + "%");
        paramLst.add("%" + searchKey + "%");
        paramLst.add("%" + searchKey + "%");

      }
    }
    hql.append(" order by t.id desc");

    long count = (Long) findUnique("select count(t.id) " + hql, paramLst.toArray());
    page.setTotalCount(count);

    Query q = createQuery(hql + " order by t.id desc", paramLst.toArray());
    setPageParameter(q, page);
    page.setResult(q.list());
    return page;
  }

  public Long getTotalShareMsg() throws DaoException {

    Long psnId = SecurityUtils.getCurrentUserId();

    return findUnique("select count(*) from ShareInbox where status in (0,1) and psnId=" + psnId);

  }

  public Long getUnreadShareMsg() throws DaoException {
    Long psnId = SecurityUtils.getCurrentUserId();

    return getUnreadShareMsg(psnId);
  }

  public Long getUnreadShareMsg(Long psnId) throws DaoException {

    return findUnique("select count(psnId) from ShareInbox where status in (0) and psnId= ? ", psnId);
  }

  public void updatePersonInfo(SnsPersonSyncMessage msg) throws DaoException {
    try {
      String hql = "update ShareInbox t set t.psnName=?,t.firstName=?,t.lastName=?,t.psnHeadUrl=? where t.psnId=?";
      super.createQuery(hql,
          new Object[] {msg.getName(), msg.getFirstName(), msg.getLastName(), msg.getAvatars(), msg.getPsnId()})
              .executeUpdate();
    } catch (Exception e) {

      logger.error("同步收件箱人员{}数据失败！", msg.getPsnId());
      throw new DaoException(e);

    }
  }

  public void updateStatus(List<Long> mailIds, Long receiverId) throws DaoException {
    String hql = "update ShareInbox t set t.status=1 where t.psnId=:psnId and t.mailId in(:mailIds)";
    Query query = super.createQuery(hql);
    query.setParameter("psnId", receiverId);
    query.setParameterList("mailIds", mailIds);
    query.executeUpdate();
  }

  /**
   * 获取站内通知收件箱记录列表.
   * 
   * @param psnId
   * @return
   */
  @SuppressWarnings("unchecked")
  public List<ShareInbox> getShareInboxList(Long psnId) {
    String ql = "from ShareInbox where psnId = ?";
    return super.createQuery(ql, psnId).list();
  }

  /**
   * 根据人员ID删除收件箱记录.
   * 
   * @param psnId
   */
  public void delInBoxByPsnId(Long psnId) {
    String hql = "delete from ShareInbox t where psnId=?";
    super.createQuery(hql, new Object[] {psnId}).executeUpdate();
  }

  /**
   * 根据mailId查找ShareInbox
   * 
   * @param mailId
   * @return
   * @throws DaoException
   */
  public List<ShareInbox> findShareInboxListByMailId(Long mailId) throws DaoException {
    String hql = "from ShareInbox t where t.mailId = ?";
    return this.createQuery(hql, mailId).list();
  }

  @SuppressWarnings("unchecked")
  public ShareInbox queryShareInbox(Long psnId, Long resRecId) throws DaoException {
    List<ShareInbox> list =
        super.createQuery("from ShareInbox t where t.psnId=? and t.resRecId=?", new Object[] {psnId, resRecId}).list();
    if (CollectionUtils.isNotEmpty(list)) {
      return list.get(0);
    }
    return null;
  }

  /**
   * 通过发件箱id查询收件人.
   * 
   * @param mailId
   * @return
   * @throws DaoException
   */
  @SuppressWarnings("unchecked")
  public List<Long> queryReceiverIdByMail(Long mailId) throws DaoException {
    return this.createQuery("select t.psnId from ShareInbox t where t.mailId = ?", mailId).list();
  }

  /**
   * 查询当前收件箱记录的上一条站内信.
   * 
   * @param curId
   * @return
   * @throws DaoException
   */
  @SuppressWarnings("unchecked")
  public ShareInbox getPrev(Long curId) throws DaoException {
    List<ShareInbox> inboxs =
        super.createQuery("from ShareInbox t where t.id>? and t.status<>2 and t.psnId=? order by t.id asc",
            new Object[] {curId, SecurityUtils.getCurrentUserId()}).setFetchSize(1).list();

    return (inboxs == null || inboxs.size() == 0) ? null : inboxs.get(0);
  }

  /**
   * 查询当前收件箱记录的吓一跳站内信.
   * 
   * @param curId
   * @return
   * @throws DaoException
   */
  @SuppressWarnings("unchecked")
  public ShareInbox getNext(Long curId) throws DaoException {
    List<ShareInbox> inboxs =
        super.createQuery("from ShareInbox t where t.id<? and t.status<>2 and t.psnId=? order by t.id desc",
            new Object[] {curId, SecurityUtils.getCurrentUserId()}).setFetchSize(1).list();

    return (inboxs == null || inboxs.size() == 0) ? null : inboxs.get(0);
  }

  @SuppressWarnings("unchecked")
  public List<ShareInbox> queryShareInboxBySendPsnId(Long psnId) {
    String ql = "from ShareInbox where senderId = ?";
    return super.createQuery(ql, psnId).list();
  }

  /**
   * 更新站内分享收件状态_MJG_SCM-5910.
   * 
   * @param status
   * @param id
   */
  public void updateInboxStatus(Integer status, Long id) {
    String hql = "update ShareInbox t set t.status=? where t.id=? ";
    super.createQuery(hql, status, id).executeUpdate();
  }

  /**
   * 保存站内分享收件记录_MJG_SCM-6097.
   * 
   * @param inbox
   */
  public void saveShareInBox(ShareInbox inbox) {
    if (inbox != null) {
      if (inbox.getMailId() != null) {
        super.getSession().update(inbox);
      } else {
        super.save(inbox);
      }
    }
  }

  /**
   * 清除分享发件箱的内容数据(定时器任务msseageOldDataTaskTriggers终止后此方法将废弃)_MJG_SCM-6097.
   * 
   * @param mailbox
   */
  public void cleanShareInbox(ShareInbox inbox) {
    String sql =
        "update share_inbox t set t.title=null,t.en_title=null,t.ext_other_info=null,t.content=null where t.id="
            + inbox.getId();
    super.getSession().createSQLQuery(sql).executeUpdate();
  }
}
