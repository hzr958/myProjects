package com.smate.web.dyn.dao.msg;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.hibernate.Query;
import org.hibernate.SQLQuery;
import org.springframework.stereotype.Repository;

import com.smate.core.base.utils.constant.MsgConstants;
import com.smate.core.base.utils.data.SnsHibernateDao;
import com.smate.core.base.utils.model.Page;
import com.smate.web.dyn.form.msg.MsgShowForm;
import com.smate.web.dyn.form.msg.mobile.MobileMsgShowForm;
import com.smate.web.dyn.model.msg.MsgRelation;
import com.smate.web.dyn.model.msg.MsgShowInfo;

/**
 * 消息关系实体dao
 * 
 * @author zzx
 *
 */

@Repository
public class MsgRelationDao extends SnsHibernateDao<MsgRelation, Long> {

  /**
   * 获取站内信消息类型列表
   * 
   * @param form
   * @return
   */
  public List<MsgRelation> getMsgListByChat(MsgShowForm form) {
    String count = "select count(1) ";
    Page<MsgShowInfo> page = form.getPage();
    String hql = "from MsgRelation t where ((t.receiverId=:psnId and t.senderId=:chatPsnId ) "
        + " or (t.senderId=:psnId and t.receiverId=:chatPsnId ) ) and t.type=7 and t.status <> 2 "
        + " order by t.createDate desc,t.id desc";
    page.setTotalCount((Long) this.createQuery(count + hql).setParameter("chatPsnId", form.getChatPsnId())
        .setParameter("psnId", form.getPsnId()).uniqueResult());
    List<MsgRelation> msgRelationList = null;
    Query query =
        this.createQuery(hql).setParameter("chatPsnId", form.getChatPsnId()).setParameter("psnId", form.getPsnId());
    if (StringUtils.isBlank(form.getSearchKey())) {
      msgRelationList = query.setFirstResult(page.getFirst() - 1).setMaxResults(page.getPageSize()).list();
    } else {
      msgRelationList = query.setFirstResult(0).setMaxResults(100).list();
    }
    if (msgRelationList != null && msgRelationList.size() > 0) {
      Collections.reverse(msgRelationList);
      return msgRelationList;
    } else {
      return null;
    }
  }

  /**
   * 移动端-获取站内信消息类型列表
   * 
   * @param form
   * @return
   */
  @SuppressWarnings({"rawtypes", "unchecked"})
  public List<MsgRelation> getMobileMsgList(MobileMsgShowForm form) {
    String count = "select count(1) ";
    Page page = form.getPage();
    String hql =
        "from MsgRelation t where t.receiverId=:receiverId and t.type=7 and t.status=0 order by t.createDate desc,t.id desc";
    Object obj = super.createQuery(count + hql).setParameter("receiverId", form.getPsnId()).uniqueResult();
    page.setTotalCount(NumberUtils.toLong(obj.toString()));
    return super.createQuery(hql).setParameter("receiverId", form.getPsnId())
        .setFirstResult((form.getMobilePageNo() - 1) * page.getPageSize()).setMaxResults(page.getPageSize()).list();
  }

  /**
   * 根据消息类型获取列表
   * 
   * @param form
   */
  public List<MsgRelation> getMsgListByType(MsgShowForm form) {
    String count = "select count(1) ";
    Page<MsgShowInfo> page = form.getPage();
    StringBuilder sb = new StringBuilder();
    sb.append(
        " from MsgRelation t where t.receiverId=:psnId and (t.type =0 or t.type =4 or t.type =5 or t.type =6 or t.type =10)");
    if (form.getStatus() != null && form.getStatus() == 0) {
      sb.append(" and t.status = 0");
    } else {
      sb.append(" and t.status <> 2");
    }
    page.setTotalCount(
        (Long) this.createQuery(count + sb.toString()).setParameter("psnId", form.getPsnId()).uniqueResult());
    sb.append(" order by t.createDate desc,t.id");
    return this.createQuery(sb.toString()).setParameter("psnId", form.getPsnId()).setFirstResult(page.getFirst() - 1)
        .setMaxResults(page.getPageSize()).list();
  };

  /**
   * 根据消息类型获取列表
   * 
   * @param form
   */
  public List<MsgRelation> getMidMsgListByType(MsgShowForm form) {
    String count = "select count(1) ";
    Page<MsgShowInfo> page = form.getPage();
    StringBuilder sb = new StringBuilder();
    sb.append(" from MsgRelation t where t.receiverId=:psnId and t.type in (0)");
    if (form.getStatus() != null && form.getStatus() == 0) {
      sb.append(" and t.status = 0");
    } else {
      sb.append(" and t.status <> 2");
    }
    page.setTotalCount(
        (Long) this.createQuery(count + sb.toString()).setParameter("psnId", form.getPsnId()).uniqueResult());
    sb.append(" order by t.createDate desc,t.id");

    return this.createQuery(sb.toString()).setParameter("psnId", form.getPsnId()).setFirstResult(page.getFirst() - 1)
        .setMaxResults(page.getPageSize()).list();
  };

  /**
   * 获取全文请求的站内信
   * 
   * @param form
   */
  @SuppressWarnings("unchecked")
  public List<MsgRelation> getMsgListByFulltextRequest(MsgShowForm form) {
    Page<MsgShowInfo> page = form.getPage();
    StringBuilder sql = new StringBuilder();
    // sb.append(" from MsgRelation t where t.receiverId=:psnId and t.type
    // =11 and t.dealStatus= 0 ");
    // sb.append(" order by t.createDate desc,t.id");
    String selectCount = "SELECT count(1) ";
    String selectAll = "SELECT t.* ";
    sql.append("FROM v_msg_relation t ");
    sql.append("JOIN (");
    sql.append("SELECT e.content_id, ");
    sql.append("substr(e.CONTENT, instr(e.CONTENT, '\"pubTitleZh\":')+14, ");
    sql.append(
        "instr(e.CONTENT, ',', instr(e.CONTENT, '\"pubTitleZh\":')+14, 1)-instr(e.CONTENT, '\"pubTitleZh\":')-15) AS pub_title_zh, ");
    sql.append("substr(e.CONTENT, instr(e.CONTENT, '\"pubTitleEn\":')+14, ");
    sql.append(
        "instr(e.CONTENT, ',', instr(e.CONTENT, '\"pubTitleEn\":')+14, 1)- instr(e.CONTENT, '\"pubTitleEn\":')-15) AS pub_title_en ");
    sql.append("from v_msg_content e) c on c.content_id = t.content_id ");
    sql.append("JOIN (");
    sql.append("SELECT p.psn_id, CASE WHEN p.NAME IS NOT NULL THEN p.NAME ELSE '' END AS zh_name, ");
    sql.append("CASE WHEN p.ename IS NOT NULL THEN p.ename  ELSE trim(p.first_name||' '||p.last_name) END AS en_name ");
    sql.append("FROM person p) n ON n.psn_id = t.sender_id ");
    sql.append("WHERE t.receiver_id = :psnId AND t.TYPE =11 AND t.deal_status= 0 ");
    boolean flag = StringUtils.isNotBlank(form.getSearchKey());
    if (flag) {
      sql.append("AND ( ");
      sql.append("instr(lower(n.zh_name), :searchkey) > 0");
      sql.append("OR instr(lower(n.en_name), :searchkey) > 0");
      sql.append("OR instr(lower(c.pub_title_zh), :searchkey) > 0");
      sql.append("OR instr(lower(c.pub_title_en), :searchkey) > 0");
      sql.append(") ");
    }
    SQLQuery countSQLQuery = getSession().createSQLQuery(selectCount + sql.toString());
    countSQLQuery.setParameter("psnId", form.getPsnId());
    if (flag) {
      countSQLQuery.setParameter("searchkey", form.getSearchKey().toLowerCase());
    }
    BigDecimal count = (BigDecimal) countSQLQuery.uniqueResult();
    page.setTotalCount(count.longValue());

    sql.append("order by t.create_Date desc, t.id");
    SQLQuery selectSQLQuery = getSession().createSQLQuery(selectAll + sql.toString());
    selectSQLQuery.setParameter("psnId", form.getPsnId());
    if (flag) {
      selectSQLQuery.setParameter("searchkey", form.getSearchKey().toLowerCase());
    }
    selectSQLQuery.addEntity(MsgRelation.class);
    return (List<MsgRelation>) selectSQLQuery.setFirstResult(page.getFirst() - 1).setMaxResults(page.getPageSize())
        .list();
  };

  /**
   * 获取未读消息
   * 
   * @param form
   */
  public Map<String, String> countUnreadMsg(MsgShowForm form) {
    Map<String, String> countMap = new HashMap<String, String>();

    // 未读 统计三种类型
    // 站内信
    String hql = "select count(t.id) from MsgRelation t where t.receiverId=:psnId and t.type="
        + MsgConstants.MSG_TYPE_SMATE_INSIDE_LETTER + " and t.status=0 "
        + " and exists (select 1 from Person  p where p.personId = t.senderId)";
    // 全文请求
    String hql1 = "select count(t.id) from MsgRelation t join t.sender where t.receiverId=:psnId and t.type="
        + MsgConstants.MSG_TYPE_PUBFULLTEXT_REQUEST + " and t.dealStatus=0"
        + " and exists (select 1 from Person  p where p.personId = t.senderId)";

    // 其他所有消息
    String hql2 = "select count(t.id) from MsgRelation t where t.receiverId=:psnId and "
        + " (t.type=0 or t.type=4 or t.type=5 or t.type=6 or t.type=10 ) " + " and t.status=0"
        + " and exists (select 1 from Person  p where p.personId = t.senderId)";
    // 已读统计三种类型
    // 站内信
    String redhql = "select count(t.id) from MsgRelation t where t.receiverId=:psnId and t.type="
        + MsgConstants.MSG_TYPE_SMATE_INSIDE_LETTER + " and t.status=1";
    // 全文请求
    String redhql1 = "select count(t.id) from MsgRelation t where t.receiverId=:psnId and t.type="
        + MsgConstants.MSG_TYPE_PUBFULLTEXT_REQUEST + " and t.dealStatus=1";
    // 其他所有消息
    String redhql2 = "select count(t.id) from MsgRelation t where t.receiverId=:psnId and "
        + " (t.type=0 or t.type=4 or t.type=5 or t.type=6 or t.type=10 ) " + " and t.status=1";
    //
    Long count = (Long) super.createQuery(hql).setParameter("psnId", form.getPsnId()).uniqueResult();
    Long count1 = (Long) super.createQuery(hql1).setParameter("psnId", form.getPsnId()).uniqueResult();
    Long count2 = (Long) super.createQuery(hql2).setParameter("psnId", form.getPsnId()).uniqueResult();

    Long redcount = (Long) super.createQuery(redhql).setParameter("psnId", form.getPsnId()).uniqueResult();
    Long redcount1 = (Long) super.createQuery(redhql1).setParameter("psnId", form.getPsnId()).uniqueResult();
    Long redcount2 = (Long) super.createQuery(redhql2).setParameter("psnId", form.getPsnId()).uniqueResult();
    countMap.put(MsgConstants.MSG_TYPE_SMATE_INSIDE_LETTER, count.toString());
    countMap.put(MsgConstants.MSG_TYPE_PUBFULLTEXT_REQUEST, count1.toString());
    countMap.put("other", count2.toString());
    countMap.put("red7", redcount.toString());
    countMap.put("red11", redcount1.toString());
    countMap.put("redother", redcount2.toString());
    return countMap;

  }

  /**
   * 获取未读消息
   * 
   * @param form
   */
  public Map<String, String> countReadMsg(MsgShowForm form) {
    Map<String, String> countMap = new HashMap<String, String>();
    // 已读统计三种类型
    // 站内信
    String redhql = "select count(t.id) from MsgRelation t where t.receiverId=:psnId and t.type="
        + MsgConstants.MSG_TYPE_SMATE_INSIDE_LETTER + " and t.status=1";
    // 全文请求
    String redhql1 = "select count(t.id) from MsgRelation t where t.receiverId=:psnId and t.type="
        + MsgConstants.MSG_TYPE_PUBFULLTEXT_REQUEST + " and t.dealStatus=1";
    // 其他所有消息
    String redhql2 = "select count(t.id) from MsgRelation t where t.receiverId=:psnId and "
        + " (t.type=0 or t.type=4 or t.type=5 or t.type=6 or t.type=10 ) " + " and t.status=1";
    Long redcount = (Long) super.createQuery(redhql).setParameter("psnId", form.getPsnId()).uniqueResult();
    Long redcount1 = (Long) super.createQuery(redhql1).setParameter("psnId", form.getPsnId()).uniqueResult();
    Long redcount2 = (Long) super.createQuery(redhql2).setParameter("psnId", form.getPsnId()).uniqueResult();
    countMap.put("red7", redcount.toString());
    countMap.put("red11", redcount1.toString());
    countMap.put("redother", redcount2.toString());
    return countMap;
  }

  /**
   * 删除消息
   * 
   * @param id
   */
  public void delChatMsg(Long id, Long senderId) {
    String hql = "update MsgRelation t set t.status = 2 where t.senderId=:senderId and t.id=:id";
    this.createQuery(hql).setParameter("id", id).setParameter("senderId", senderId).executeUpdate();
  }

  /**
   * 删除消息
   * 
   * @param id
   */
  public void delMsg(Long id, Long receiverId) {
    String hql = "update MsgRelation t set t.status = 2 where t.receiverId=:receiverId and t.id=:id";
    this.createQuery(hql).setParameter("id", id).setParameter("receiverId", receiverId).executeUpdate();
  }

  /**
   * 全部标记为已读消息
   * 
   * @param id
   */
  public void setReadAllMsg(Long psnId) {
    String hql =
        "update MsgRelation t set t.status = 1 where t.type <> 7 and t.type <> 11 and t.status=0 and t.receiverId=:psnId";
    this.createQuery(hql).setParameter("psnId", psnId).executeUpdate();
  }

  /**
   * 全部标记为已读消息
   * 
   * @param id
   */
  public void setReadchatMsg(Long senderId, Long psnId) {
    String hql =
        "update MsgRelation t set t.status = 1 where t.type=7 and t.status=0 and t.receiverId=:psnId and t.senderId=:senderId";
    this.createQuery(hql).setParameter("psnId", psnId).setParameter("senderId", senderId).executeUpdate();
  }

  /**
   * 全部标记为已读消息
   * 
   * @param id
   */
  public void setReadMsg(Long id, Long receiverId) {
    String hql = "update MsgRelation t set t.status = 1 where t.receiverId=:receiverId and t.id=:id";
    this.createQuery(hql).setParameter("id", id).setParameter("receiverId", receiverId).executeUpdate();
  }

  /**
   * 获取与某人的未读消息数量
   * 
   * @param senderId
   * @param psnId
   * @return
   */
  public Long getChatMsgCount(Long senderId, Long psnId) {
    String hql =
        "select count(1) from MsgRelation t where t.status=0 and t.type=7 and t.senderId=:senderId and t.receiverId=:psnId ";
    return (Long) this.createQuery(hql).setParameter("senderId", senderId).setParameter("psnId", psnId).uniqueResult();
  }

  /**
   * 获取与某人的已读消息数量
   * 
   * @param senderId
   * @param psnId
   * @return
   */
  public Long getReadChatMsgCount(Long senderId, Long psnId) {
    String hql =
        "select count(1) from MsgRelation t where t.status!=2 and t.type=7 and ((t.senderId=:senderId and t.receiverId=:psnId) or (t.senderId=:psnId and t.receiverId=:senderId)) ";
    return (Long) this.createQuery(hql).setParameter("senderId", senderId).setParameter("psnId", psnId).uniqueResult();
  }

  public Long getNewestContentId(Long senderId, Long receiverId) {
    String hql = "select t.contentId from MsgRelation t where t.type=7 and t.status <> 2 and "
        + "(( t.senderId=:senderId and t.receiverId=:receiverId) or "
        + "( t.senderId=:receiverId and t.receiverId=:senderId)) order by t.createDate desc, id desc ";
    List<Long> list =
        this.createQuery(hql).setParameter("senderId", senderId).setParameter("receiverId", receiverId).list();
    if (list != null && list.size() > 0) {
      return list.get(0);
    } else {
      return null;
    }
  }

  /**
   * 移动端-标记消息为已读
   * 
   * @param form
   */
  public int mobileSetMsg(MobileMsgShowForm form) {
    String hql =
        "update MsgRelation t set t.status = 1 where t.id=:msgId and t.receiverId=:receiverId and t.type=7 and t.status=0";
    return super.createQuery(hql).setParameter("msgId", form.getMsgRelationId())
        .setParameter("receiverId", form.getPsnId()).executeUpdate();
  }

  /**
   * 移动端-获取用户站内信未读消息
   * 
   * @param psnId
   * @return
   */
  public Long mobileGetUnreadMsg(Long psnId) {
    String hql = "select count(t.id) from MsgRelation t where t.receiverId=:psnId and t.type="
        + MsgConstants.MSG_TYPE_SMATE_INSIDE_LETTER + " and t.status=0";
    return (Long) super.createQuery(hql).setParameter("psnId", psnId).uniqueResult();
  }

  /**
   * 移动端全文请求 app
   * 
   * @param form
   * @return
   */
  public List<MsgRelation> getMobileMsgListByFulltextReq(MobileMsgShowForm form) {
    Page<MsgShowInfo> page = form.getPage();
    StringBuilder sb = new StringBuilder();
    sb.append(" from MsgRelation t where t.receiverId=:psnId  and  t.type =11   and t.dealStatus= 0  ");
    sb.append(" order by t.createDate desc,t.id");
    return this.createQuery(sb.toString()).setParameter("psnId", form.getPsnId()).list();
  }

  /**
   * 移动端根据消息类型获取列表
   * 
   * @param form
   */
  public List<MsgRelation> getMobileMsgListByType(MobileMsgShowForm form) {
    String count = "select count(1) ";
    Page<MsgShowInfo> page = form.getPage();
    StringBuilder sb = new StringBuilder();
    sb.append(
        " from MsgRelation t where t.receiverId=:psnId and (t.type =0 or t.type =4 or t.type =5 or t.type =6 or t.type =10)");
    if (form.getStatus() != null && form.getStatus() == 0) {
      sb.append(" and t.status = 0");
    } else {
      sb.append(" and t.status <> 2");
    }
    page.setTotalCount(
        (Long) this.createQuery(count + sb.toString()).setParameter("psnId", form.getPsnId()).uniqueResult());
    sb.append(" order by t.createDate desc,t.id");

    return this.createQuery(sb.toString()).setParameter("psnId", form.getPsnId()).setFirstResult(page.getFirst() - 1)
        .setMaxResults(page.getPageSize()).list();
  };

  public List<MsgRelation> getMsgListByChatForSearch(Long chatPsnId, Long psnId, Date date, String searchKey,
      Page page) {
    String hql = "from MsgRelation t where ((t.receiverId=:psnId and t.senderId=:chatPsnId ) "
        + " or (t.senderId=:psnId and t.receiverId=:chatPsnId ) ) and t.type=7 and t.status <> 2 and t.createDate>= trunc(add_months(sysdate,-3))";
    List<MsgRelation> msgRelationList = null;
    Query query = this.createQuery(hql).setParameter("chatPsnId", chatPsnId).setParameter("psnId", psnId);
    msgRelationList = query.list();
    if (msgRelationList != null && msgRelationList.size() > 0) {
      Collections.reverse(msgRelationList);
      return msgRelationList;
    } else {
      return null;
    }
  }

  /**
   * 更新消息状态
   * 
   * @param id
   */
  public void updateMsgByContentId(Long contentId, Integer dealStatus) {
    String hql =
        "update MsgRelation t set t.dealStatus =:dealStatus   , t.dealDate =:dealDate where t.contentId=:contentId";
    this.createQuery(hql).setParameter("contentId", contentId).setParameter("dealStatus", dealStatus)
        .setParameter("dealDate", new Date()).executeUpdate();
  }

  /**
   * 获取3条站内信消息类型列表，来发邮件
   * 
   * @param form
   * @return
   */
  @SuppressWarnings("unchecked")
  public List<MsgRelation> getMsgListByInsideMsgEmail(MsgShowForm form) {
    String hql = "from MsgRelation t where ((t.receiverId=:psnId and t.senderId=:chatPsnId ) "
        + " or (t.senderId=:psnId and t.receiverId=:chatPsnId ) ) and t.type=7 and t.status <> 2 "
        + " order by t.createDate desc,t.id desc";
    List<MsgRelation> msgRelationList = null;
    Query query =
        this.createQuery(hql).setParameter("chatPsnId", form.getChatPsnId()).setParameter("psnId", form.getPsnId());
    msgRelationList = query.setFirstResult(0).setMaxResults(4).list();
    if (msgRelationList != null && msgRelationList.size() > 0) {
      return msgRelationList;
    } else {
      return null;
    }
  }

  /**
   * 当天是否发送邮件
   * 
   * @param sender
   * @param receiver
   * @return
   */
  public Boolean hasSendEmail(Long psnId, Long chatPsnId) {
    /**
     * SCM-17191 原本逻辑： ( (t.receiverId=:psnId and t.senderId=:chatPsnId ) or (t.senderId=:psnId and
     * t.receiverId=:chatPsnId ) ) A B 只要有一人收到站内信的邮件，另一个就不能收到站内信邮件 单向 改动逻辑：A 收到 B 站内信邮件，B 也可以收到 A 站内信邮件
     * 双向
     */
    String hql = " from  MsgRelation t where t.senderId=:psnId and t.receiverId=:chatPsnId "
        + " and  t.createDate>trunc(sysdate)  and t.sendEmail = 1 ";
    List list = this.createQuery(hql).setParameter("psnId", psnId).setParameter("chatPsnId", chatPsnId).list();
    if (list != null && list.size() > 0) {
      return true;
    }
    return false;
  }

  /**
   * 更新当天发送邮箱状态
   * 
   * @param sender
   * @param receiver
   * @return
   */
  public Integer updateSendEmailStatus(Long msgRelationId) {
    String hql =
        "   update  MsgRelation t set  t.sendEmail=1  ,t.sendEmailDate=:sendDate   where    t.id =:msgRelationId  ";
    int update = this.createQuery(hql).setParameter("msgRelationId", msgRelationId).setParameter("sendDate", new Date())
        .executeUpdate();
    return update;
  }

}
