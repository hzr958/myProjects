package com.smate.center.batch.dao.mail;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.hibernate.Query;
import org.springframework.stereotype.Repository;

import com.smate.center.batch.exception.pub.DaoException;
import com.smate.center.batch.model.mail.InsideInbox;
import com.smate.center.batch.model.sns.pub.SnsPersonSyncMessage;
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
public class InsideInboxDao extends SnsHibernateDao<InsideInbox, Long> {

  @SuppressWarnings({"unchecked"})
  public Page<InsideInbox> queryInsideInboxByPage(Page<InsideInbox> page, Map<String, String> paramMap)
      throws DaoException {
    Long psnId = SecurityUtils.getCurrentUserId();
    List<Object> paramLst = new ArrayList<Object>();
    StringBuffer hql = new StringBuffer(" from InsideInbox t where t.psnId=?");
    paramLst.add(psnId);
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
            "and (exists(select 1 from InsideInboxCon t2 where t2.inboxId=t.id and (lower(t2.titleZh) like ? or lower(t2.titleEn) like ? or lower(t2.content) like ?)) or "
                + "exists(select 1 from Person p where p.personId = t.senderId and (lower(p.name) like ? or lower(p.ename) like ?)))");
        paramLst.add("%" + searchKey + "%");
        paramLst.add("%" + searchKey + "%");
        paramLst.add("%" + searchKey + "%");
        paramLst.add("%" + searchKey + "%");
        paramLst.add("%" + searchKey + "%");
      }
    }
    long count = (Long) findUnique("select count(*)" + hql, paramLst.toArray());
    page.setTotalCount(count);

    Query q = createQuery(hql + " order by t.id desc", paramLst.toArray());
    setPageParameter(q, page);
    page.setResult(q.list());
    return page;
  }


  public Long getTotalInsideMsg() throws DaoException {
    Long psnId = SecurityUtils.getCurrentUserId();
    return findUnique("select count(*) from InsideInbox where status in (0,1) and psnId=?", new Object[] {psnId});

  }

  /**
   * 获取当前用户未读数.
   * 
   * @return
   * @throws DaoException
   */
  public Long getUnreadInsideMsg() throws DaoException {
    Long psnId = SecurityUtils.getCurrentUserId();
    return this.getUnreadInsideMsg(psnId);

  }

  /**
   * 获取指定用户未读数.
   * 
   * @return
   * @throws DaoException
   */
  public Long getUnreadInsideMsg(Long psnId) throws DaoException {
    return findUnique("select count(psnId) from InsideInbox where status=? and psnId=?", new Object[] {0, psnId});
  }

  public void updatePersonInfo(SnsPersonSyncMessage msg) throws DaoException {
    try {
      String hql = "update InsideInbox t set t.psnName=?,t.firstName=?,t.lastName=?,t.psnHeadUrl=? where t.psnId=?";
      super.createQuery(hql,
          new Object[] {msg.getName(), msg.getFirstName(), msg.getLastName(), msg.getAvatars(), msg.getPsnId()})
              .executeUpdate();
    } catch (Exception e) {

      logger.error("同步收件箱人员{}数据失败！", msg.getPsnId());
      throw new DaoException(e);

    }
  }

  public InsideInbox find(Long mailId, Long psnId) throws DaoException {

    String hql = "From InsideInbox t where t.mailId=?  and t.psnId=? and status<>2";

    return (InsideInbox) super.createQuery(hql, new Object[] {mailId, psnId}).uniqueResult();
  }

  /**
   * 更新被邀请加入组的短信状态
   * 
   * @param recId 接收人Id
   * @param groupName 组Id
   * @throws DaoException
   */
  public void updateInvitedGroupInboxStatusToReaded(Long recId, String groupName) throws DaoException {
    try {
      String hql =
          "update InsideInbox t set t.status=1 where t.psnId=? and t.status=0 and t.mailId in (select a.mailId from InsideMailBox a where a.title like ? and (a.title like '%请求加入您的群组%' or a.title like '%request to join your group%'))";
      this.createQuery(hql, new Object[] {recId, "%" + groupName + "%"}).executeUpdate();
    } catch (Exception e) {
      logger.error("更新被邀请加入组的短信状态出错！", "收信人：" + recId + "; 组名：" + groupName);
      throw new DaoException(e);
    }
  }

  /**
   * 获取站内收件箱记录列表.
   * 
   * @param psnId
   * @return
   */
  @SuppressWarnings("unchecked")
  public List<InsideInbox> getInsideInboxList(Long psnId) {
    String ql = "from InsideInbox where psnId = ?";
    return super.createQuery(ql, psnId).list();
  }

  /**
   * 根据人员ID删除收件箱记录.
   * 
   * @param psnId
   */
  public void delInBoxByPsnId(Long psnId) {
    String hql = "delete from InsideInbox t where psnId=?";
    super.createQuery(hql, new Object[] {psnId}).executeUpdate();
  }

  /**
   * 根据mailId查找InsideInbox
   * 
   * @param mailId
   * @return
   * @throws DaoException
   */
  @SuppressWarnings("unchecked")
  public List<InsideInbox> findInsideInboxListByMailId(Long mailId) throws DaoException {
    String hql = "from InsideInbox t where t.mailId = ?";
    return this.createQuery(hql, mailId).list();
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
    return this.createQuery("select t.psnId from InsideInbox t where t.mailId = ?", mailId).list();
  }

  /**
   * 查询当前收件箱记录的上一条站内信.
   * 
   * @param curId
   * @return
   * @throws DaoException
   */
  @SuppressWarnings("unchecked")
  public InsideInbox getPrev(Long curId) throws DaoException {
    List<InsideInbox> inboxs =
        super.createQuery("from InsideInbox t where t.id>? and t.status<>2 and t.psnId=? order by t.id asc",
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
  public InsideInbox getNext(Long curId) throws DaoException {
    List<InsideInbox> inboxs =
        super.createQuery("from InsideInbox t where t.id<? and t.status<>2 and t.psnId=? order by t.id desc",
            new Object[] {curId, SecurityUtils.getCurrentUserId()}).setFetchSize(1).list();

    return (inboxs == null || inboxs.size() == 0) ? null : inboxs.get(0);
  }

  @SuppressWarnings("unchecked")
  public List<InsideInbox> queryInsideInboxBySendPsnId(Long psnId) {
    String ql = "from InsideInbox where senderId = ?";
    return super.createQuery(ql, psnId).list();
  }

  /**
   * 更新站内短消息收件状态_MJG_SCM-5910.
   * 
   * @param status
   * @param id
   */
  public void updateInboxStatus(Integer status, Long id) {
    String hql = "update InsideInbox t set t.status=? where t.id=? ";
    super.createQuery(hql, status, id).executeUpdate();
  }

  /**
   * 保存站内消息记录.
   * 
   * @param inbox
   */
  public void saveInsideInbox(InsideInbox inbox) {
    if (inbox != null) {
      if (inbox.getId() != null) {
        super.getSession().update(inbox);
      } else {
        super.save(inbox);
      }
    }
  }

  /**
   * 清除站内消息发件箱的内容数据(定时器任务msseageOldDataTaskTriggers终止后此方法将废弃)_MJG_SCM-6097.
   * 
   * @param mailbox
   */
  public void cleanInsideInbox(InsideInbox inbox) {
    String sql =
        "update inside_inbox t set t.title=null,t.title_en=null,t.ext_other_info=null,t.content=null where t.id="
            + inbox.getId();
    super.getSession().createSQLQuery(sql).executeUpdate();
  }
}
