package com.smate.center.batch.dao.mail;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.hibernate.Query;
import org.springframework.stereotype.Repository;

import com.smate.center.batch.exception.pub.DaoException;
import com.smate.center.batch.model.mail.InsideMailBox;
import com.smate.center.batch.model.sns.pub.Message;
import com.smate.center.batch.model.sns.pub.SnsPersonSyncMessage;
import com.smate.core.base.utils.data.SnsHibernateDao;
import com.smate.core.base.utils.model.Page;
import com.smate.core.base.utils.security.SecurityUtils;


/**
 * 站内短信发件箱
 * 
 * @author oyh
 * 
 */

@Repository
public class InsideMailBoxDao extends SnsHibernateDao<InsideMailBox, Long> {

  @SuppressWarnings("unchecked")
  public Page<InsideMailBox> queryInsideMailBoxByPage(Page<InsideMailBox> page, Message message) throws DaoException {
    Long psnId = SecurityUtils.getCurrentUserId();
    StringBuffer hql = new StringBuffer();
    List<Object> params = new ArrayList<Object>();
    hql.append("from InsideMailBox t where t.status=? and t.psnId=?");
    params.add(0);
    params.add(psnId);
    if (StringUtils.isNotBlank(message.getSearchKey())) {
      String searchKey = message.getSearchKey().toLowerCase();
      hql.append(
          " and (exists(select 1 from InsideMailBoxCon t2 where t2.mailId=t.mailId and (lower(t2.content) like ? or lower(t2.titleZh) like ? or lower(t2.titleEn) like ?)) or (lower(t.zhReceivers) like ? or lower(t.enReceivers) like ?))");
      params.add("%" + searchKey + "%");
      params.add("%" + searchKey + "%");
      params.add("%" + searchKey + "%");
      params.add("%" + searchKey + "%");
      params.add("%" + searchKey + "%");

    }

    long count = (Long) findUnique("select count(*) " + hql.toString(), params.toArray());
    page.setTotalCount(count);

    Query q = super.createQuery(hql + " order by t.mailId desc", params.toArray());
    setPageParameter(q, page);
    page.setResult(q.list());
    return page;
  }

  public void updatePersonInfo(SnsPersonSyncMessage msg) throws DaoException {
    try {
      String hql =
          "update InsideMailBox t set t.psnName=?,t.firstName=?,t.lastName=?,t.psnHeadUrl=? where t.senderId=?";
      super.createQuery(hql,
          new Object[] {msg.getName(), msg.getFirstName(), msg.getLastName(), msg.getAvatars(), msg.getPsnId()})
              .executeUpdate();
    } catch (Exception e) {

      logger.error("同步发件箱人员{}数据失败！", msg.getPsnId());
      throw new DaoException(e);

    }
  }

  /**
   * 获取站内发件箱记录列表.
   * 
   * @param psnId
   * @return
   */
  @SuppressWarnings("unchecked")
  public List<InsideMailBox> getInsideMailBoxList(Long psnId) {
    String ql = "from InsideMailBox where psnId = ?";
    return super.createQuery(ql, psnId).list();
  }

  /**
   * 根据人员ID删除发件箱记录.
   * 
   * @param psnId
   */
  public void delMailBoxByPsnId(Long psnId) {
    String hql = "delete from InsideMailBox t where psnId=?";
    super.createQuery(hql, new Object[] {psnId}).executeUpdate();
  }

  @SuppressWarnings("unchecked")
  public InsideMailBox getPrev(Long curMailId) throws DaoException {
    List<InsideMailBox> mailBoxs =
        super.createQuery("from InsideMailBox t where t.mailId>? and t.status<>1 and t.psnId=? order by t.mailId asc",
            new Object[] {curMailId, SecurityUtils.getCurrentUserId()}).setMaxResults(1).list();

    return (mailBoxs == null || mailBoxs.size() == 0) ? null : mailBoxs.get(0);
  }

  @SuppressWarnings("unchecked")
  public InsideMailBox getNext(Long curMailId) throws DaoException {
    List<InsideMailBox> mailBoxs =
        super.createQuery("from InsideMailBox t where t.mailId<? and t.status<>1 and t.psnId=? order by t.mailId desc",
            new Object[] {curMailId, SecurityUtils.getCurrentUserId()}).setMaxResults(1).list();

    return (mailBoxs == null || mailBoxs.size() == 0) ? null : mailBoxs.get(0);
  }

  /**
   * 更新发件箱状态记录_MJG_SCM-5910.
   * 
   * @param status
   * @param id
   */
  public void updateMailBoxStatus(Integer status, Long id) {
    String hql = "update InsideMailBox t set t.status=? where t.mailId=? ";
    super.createQuery(hql, status, id).executeUpdate();
  }

  /**
   * 保存站内消息发件记录.
   * 
   * @param mailBox
   */
  public void saveInsideMailBox(InsideMailBox mailBox) {
    if (mailBox != null) {
      if (mailBox.getMailId() != null) {
        super.getSession().update(mailBox);
      } else {
        super.save(mailBox);
      }
    }
  }

  /**
   * 清除站内消息发件箱的内容数据(定时器任务msseageOldDataTaskTriggers终止后此方法将废弃)_MJG_SCM-6097.
   * 
   * @param mailbox
   */
  public void cleanInsideMailBox(InsideMailBox mailBox) {
    String sql =
        "update inside_mailbox t set t.title=null,t.title_en=null,t.ext_other_info=null,t.content=null,t.zhReceivers=null,t.enReceivers=null where t.mail_id="
            + mailBox.getMailId();
    super.getSession().createSQLQuery(sql).executeUpdate();
  }

  /**
   * scm-5731 更新站内消息发件箱的收件人姓名
   * 
   * @param receiverNames
   * @param id
   */
  public void updateReceiverNames(String zhReceivers, String enReceivers, Long id) {
    String hql = "update InsideMailBox t set t.zhReceivers=?, t.enReceivers=? where t.mailId=?";
    super.createQuery(hql, zhReceivers, enReceivers, id).executeUpdate();
  }

  /**
   * scm-5731 获取站内消息发件箱需要更新收件人姓名字段的记录
   * 
   * @return
   */
  public List<InsideMailBox> getUpdateInsideMailBoxList() {
    String hql =
        "from InsideMailBox t where t.status=? and t.psnId=? and t.extOtherInfo is not null and (t.zhReceivers is null or t.enReceivers is null)";
    return super.createQuery(hql, 0, SecurityUtils.getCurrentUserId()).list();
  }
}
