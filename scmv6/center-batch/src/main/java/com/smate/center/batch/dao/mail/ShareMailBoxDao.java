package com.smate.center.batch.dao.mail;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.hibernate.Query;
import org.springframework.stereotype.Repository;

import com.smate.center.batch.exception.pub.DaoException;
import com.smate.center.batch.model.mail.ShareMailBox;
import com.smate.center.batch.model.sns.pub.Message;
import com.smate.center.batch.model.sns.pub.SnsPersonSyncMessage;
import com.smate.core.base.utils.data.SnsHibernateDao;
import com.smate.core.base.utils.model.Page;
import com.smate.core.base.utils.security.SecurityUtils;


/**
 * 文献/文件发件箱
 * 
 * @author oyh
 * 
 */

@Repository
public class ShareMailBoxDao extends SnsHibernateDao<ShareMailBox, Long> {

  @SuppressWarnings("unchecked")
  public Page<ShareMailBox> queryShareMailBoxByPage(Page<ShareMailBox> page, Message message) throws DaoException {
    StringBuffer hql = new StringBuffer();
    List<Object> params = new ArrayList<Object>();
    hql.append("from ShareMailBox t where t.status=? and t.senderId=?");
    params.add(0);
    params.add(SecurityUtils.getCurrentUserId());
    String searchKey = message.getSearchKey();
    if (StringUtils.isNotBlank(searchKey)) {
      searchKey = searchKey.toLowerCase();
      hql.append(
          " and (exists(select 1 from ShareMailBoxCon t2 where t2.mailId=t.mailId and (lower(t2.titleZh) like ? or lower(t2.titleEn) like ?)) or (lower(t.enReceiver) like ? or lower(t.zhReceiver) like ? or lower(t.psnName) like ? or lower(t.firstName) like ? or lower(t.lastName) like ? ))");
      params.add("%" + searchKey + "%");
      params.add("%" + searchKey + "%");
      params.add("%" + searchKey + "%");
      params.add("%" + searchKey + "%");
      params.add("%" + searchKey + "%");
      params.add("%" + searchKey + "%");
      params.add("%" + searchKey + "%");
    }
    long count = (Long) findUnique("select count(t.mailId) " + hql.toString(), params.toArray());
    page.setTotalCount(count);

    Query q = createQuery(hql.toString() + " order by t.mailId desc", params.toArray());
    setPageParameter(q, page);
    page.setResult(q.list());
    return page;
  }

  public void updatePersonInfo(SnsPersonSyncMessage msg) throws DaoException {
    try {
      String hql = "update ShareMailBox t set t.psnName=?,t.firstName=?,t.lastName=?,t.psnHeadUrl=? where t.senderId=?";
      super.createQuery(hql,
          new Object[] {msg.getName(), msg.getFirstName(), msg.getLastName(), msg.getAvatars(), msg.getPsnId()})
              .executeUpdate();
    } catch (Exception e) {

      logger.error("同步发件箱人员{}数据失败！", msg.getPsnId());
      throw new DaoException(e);

    }
  }

  /**
   * 获取推荐发件箱记录列表.
   * 
   * @param psnId
   * @return
   */
  @SuppressWarnings("unchecked")
  public List<ShareMailBox> getShareMailBoxList(Long psnId) {
    String ql = "from ShareMailBox where senderId = ?";
    return super.createQuery(ql, psnId).list();
  }

  @SuppressWarnings("unchecked")
  public List<ShareMailBox> getShareMailBoxBySid(Long senderId, String des3Sid) throws DaoException {
    String hql = "from ShareMailBox t where t.senderId=? and t.title like ?";
    return super.createQuery(hql, new Object[] {senderId, "%showShareList('" + des3Sid + "%"}).list();
  }

  /**
   * 根据人员ID删除发件箱记录.
   * 
   * @param psnId
   */
  public void delMailBoxByPsnId(Long psnId) {
    String hql = "delete from ShareMailBox t where senderId=?";
    super.createQuery(hql, new Object[] {psnId}).executeUpdate();
  }

  public ShareMailBox queryShareMailBoxByResSendId(Long resSendId) throws DaoException {
    return super.findUnique("from ShareMailBox t where t.resSendId=?", new Object[] {resSendId});
  }

  @SuppressWarnings("unchecked")
  public ShareMailBox getPrev(Long curMailId) throws DaoException {
    List<ShareMailBox> mailBoxs =
        super.createQuery("from ShareMailBox t where t.mailId>? and t.status<>1 and t.senderId=? order by t.mailId asc",
            new Object[] {curMailId, SecurityUtils.getCurrentUserId()}).setMaxResults(1).list();

    return (mailBoxs == null || mailBoxs.size() == 0) ? null : mailBoxs.get(0);
  }

  @SuppressWarnings("unchecked")
  public ShareMailBox getNext(Long curMailId) throws DaoException {
    List<ShareMailBox> mailBoxs = super.createQuery(
        "from ShareMailBox t where t.mailId<? and t.status<>1 and t.senderId=? order by t.mailId desc",
        new Object[] {curMailId, SecurityUtils.getCurrentUserId()}).setMaxResults(1).list();

    return (mailBoxs == null || mailBoxs.size() == 0) ? null : mailBoxs.get(0);
  }

  public ShareMailBox queryShareMailBox(Long resSendId) throws DaoException {
    String hql = "from ShareMailBox t where t.resSendId=?";
    List<ShareMailBox> list = super.createQuery(hql, resSendId).setMaxResults(1).list();
    if (CollectionUtils.isNotEmpty(list)) {
      return list.get(0);
    }
    return null;
  }

  /**
   * 保存站内分享发件箱数据记录.
   * 
   * @param mailbox
   */
  public void saveShareMailBox(ShareMailBox mailbox) {
    if (mailbox != null) {
      if (mailbox.getMailId() != null) {
        super.getSession().update(mailbox);
      } else {
        super.save(mailbox);
      }
    }
  }

  /**
   * 清除分享发件箱的内容数据(定时器任务msseageOldDataTaskTriggers终止后此方法将废弃)_MJG_SCM-6097.
   * 
   * @param mailbox
   */
  public void cleanShareMailBox(ShareMailBox mailbox) {
    String sql =
        "update share_mailbox t set t.title=null,t.en_title=null,t.ext_other_info=null,t.content=null,t.zhReceiver=null,t.enReceiver=null where t.mail_id="
            + mailbox.getMailId();
    super.getSession().createSQLQuery(sql).executeUpdate();
  }

  /**
   * scm-5731 更新分享发件箱的收件人信息
   * 
   * @param receiverNames
   * @param id
   */
  public void updateShareMailBoxReceiverNames(String zhReceiver, String enReceiver, Long id) {
    String hql = "update ShareMailBox t set t.zhReceiver = ?, t.enReceiver = ? where t.mailId = ?";
    super.createQuery(hql, zhReceiver, enReceiver, id).executeUpdate();
  }

  /**
   * scm-5731 获取要更新收件人姓名字段的记录
   * 
   * @return
   */
  public List<ShareMailBox> getUpdateShareMailBoxList() {
    String hql =
        "from ShareMailBox t where t.status=? and t.senderId=? and t.extOtherInfo is not null and (t.zhReceiver is null or t.enReceiver is null)";
    return super.createQuery(hql, 0, SecurityUtils.getCurrentUserId()).list();
  }
}
