package com.smate.center.mail.connector.dao;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Repository;

import com.smate.center.mail.connector.model.MailOriginalData;
import com.smate.center.mail.connector.model.SearchMailInfo;
import com.smate.core.base.utils.constant.DBSessionEnum;
import com.smate.core.base.utils.data.HibernateDao;
import com.smate.core.base.utils.model.Page;

/**
 * 邮件原始数据Dao
 * 
 * @author zzx
 *
 */
@Repository
public class MailOriginalDataDao extends HibernateDao<MailOriginalData, Long> {
  @Override
  public DBSessionEnum getDbSession() {
    return DBSessionEnum.SNS;
  }

  public void searchMailOriginalDataList(SearchMailInfo info, Page<MailOriginalData> page) {
    String statusCount = "select new Map(count(*) as count,t.status as status ) ";
    String statusGroupBy = " and t.status<>1 group by t.status";
    String sendStatusCount = "select new Map(count(*) as count,t.sendStatus as sendStatus ) ";
    String sendStatusGroupBy = " and t.sendStatus is not null group by t.sendStatus";

    String count = "select count(1) ";
    String hql = "from MailOriginalData t where 1=1 ";
    String order = "";
    StringBuffer sb = new StringBuffer();
    List<Object> params = new ArrayList<Object>();
    if (info.getStatus() != null) {
      sb.append(" and t.status=? ");
      params.add(info.getStatus());
    }
    if (info.getSendStatus() != null) {
      sb.append(" and t.status=1 and t.sendStatus=? ");
      params.add(info.getSendStatus());
    }
    if (StringUtils.isNotBlank(info.getTemplateCodes())) {
      sb.append(" and t.mailTemplateCode in ");
      sb.append("(" + info.getTemplateCodes() + ") ");
    }
    if (StringUtils.isNotBlank(info.getReceiver())) {
      sb.append(" and t.receiver like ? ");
      params.add("%" + info.getReceiver() + "%");
    }
    if (StringUtils.isNotBlank(info.getSender())) {
      sb.append(" and exists (select 1 from MailRecord t1 where t.mailId=t1.mailId and t1.sender like ?) ");
      params.add("%" + info.getSender() + "%");
    }
    if (info.getReceiverPsnId() != null) {
      sb.append(" and t.receiverPsnId=? ");
      params.add(info.getReceiverPsnId());
    }
    if (info.getSenderPsnId() != null) {
      sb.append(" and t.senderPsnId=? ");
      params.add(info.getSenderPsnId());
    }
    if (info.getSenderDateStart() != null && info.getSenderDateEnd() != null) {
      sb.append(" and t.updateDate>? and t.updateDate<? ");
      params.add(info.getSenderDateStart());
      params.add(info.getSenderDateEnd());
    }
    if (info.getSenderDateStart() != null && info.getSenderDateEnd() == null) {
      sb.append(" and t.updateDate>? ");
      params.add(info.getSenderDateStart());
    }
    if (info.getSenderDateStart() == null && info.getSenderDateEnd() != null) {
      sb.append(" and t.updateDate<? ");
      params.add(info.getSenderDateEnd());
    }

    if ("updateDate".equals(info.getOrderBy())) {
      order = " order by t.updateDate desc,t.mailId desc ";
    } else {
      order = " order by t.mailId desc ";
    }
    Long totalCount = super.findUnique(count + hql + sb.toString(), params.toArray());
    page.setTotalCount(totalCount);
    List<Map<String, Object>> statusList =
        super.createQuery(statusCount + hql + sb.toString() + statusGroupBy, params.toArray()).list();
    List<Map<String, Object>> sendStatusList =
        super.createQuery(sendStatusCount + hql + sb.toString() + sendStatusGroupBy, params.toArray()).list();
    info.setStatusList(statusList);
    info.setSendStatusList(sendStatusList);
    List<MailOriginalData> resultList = super.createQuery(hql + sb.toString() + order, params.toArray())
        .setFirstResult(page.getFirst() - 1).setMaxResults(page.getPageSize()).list();
    page.setResult(resultList);
  }

  public List<MailOriginalData> findListByLevel(int size, String level) {
    String hql = "from MailOriginalData t where t.priorLevel=:level and t.status=0 order by t.mailId asc";
    return super.createQuery(hql).setParameter("level", level).setMaxResults(size).list();
  }

  public boolean isExistsMailId(Long mailId) {
    String hql = "select count(1) from MailOriginalData t where t.status=0 and t.mailId=:mailId";
    Long count = super.findUnique(hql, mailId);
    if (count == 1) {
      return true;
    }
    return false;
  }

  public List<MailOriginalData> findByTempNameAndReceiver(String receiver, Integer mailTemplateCode) {
    String hql =
        "from MailOriginalData t where t.mailTemplateCode=:mailTemplateCode and t.receiver=:receiver and t.status not in(0,4) order by t.createDate desc,t.mailId desc";
    return super.createQuery(hql).setParameter("mailTemplateCode", mailTemplateCode).setParameter("receiver", receiver)
        .setMaxResults(1).list();
  }

  public List<MailOriginalData> findListByDate(int day, int size) {
    // String hql = "from MailOriginalData t where t.sendStatus in (2,3) and t.createDate <
    // sysdate-:day";
    String hql = "from MailOriginalData t where t.updateDate < sysdate-:day";// 将一个月前的全部移到历史表
    return super.createQuery(hql).setParameter("day", day).setMaxResults(size).list();
  }

  public boolean existOriginalList(int day) {
    String hql = "select count(1) from MailOriginalData t where t.updateDate < sysdate-:day";
    Long count = (Long) super.createQuery(hql).setParameter("day", day).uniqueResult();
    if (count != null && count != 0) {
      return true;
    }
    return false;
  }

  public List<Object[]> findStatusCount() {
    String sql = "select t.status,count(t.mail_id) from V_MAIL_ORIGINAL_DATA t where "
        + "t.STATUS <> 1 and t.create_date >trunc(sysdate-1) and t.create_date <trunc(sysdate) group by t.status";
    return super.getSession().createSQLQuery(sql).list();
  }

  public List<Object[]> findSendStatusCount() {
    String sql = "select t.send_status,count(t.mail_id) from V_MAIL_ORIGINAL_DATA t where "
        + "t.status = 1 and t.update_date >trunc(sysdate-1) and t.update_date <trunc(sysdate) group by t.send_status";
    return super.getSession().createSQLQuery(sql).list();
  }

  public Integer findCount() {
    String hql =
        "select count(1) from MailOriginalData t where t.createDate >trunc(sysdate-1) and t.createDate <trunc(sysdate) ";
    Object result = super.createQuery(hql).uniqueResult();
    if (result != null) {
      return Integer.parseInt(result.toString());
    }
    return 0;
  }

  /**
   * 一个小时内按人员分组查询发送的邮件数量
   * 
   * @return
   */
  public List<Object[]> findCheck1() {
    String sql = "select m.count,m.psn_id,m.template_code from ("
        + "select count(1) as count,t.sender_psn_id as psn_id,t.mail_template_code as template_code from V_MAIL_ORIGINAL_DATA t "
        + "where t.sender_psn_id<>0 and t.create_date>sysdate-1/24 " + "group by t.sender_psn_id,t.mail_template_code"
        + ") m where m.count>=20";
    return super.getSession().createSQLQuery(sql).list();
  }

  /**
   * 一个小时内按接收邮箱分组查询接收的邮件数量
   * 
   * @return
   */
  public List<Object[]> findCheck2() {
    String sql = "select m.count,m.receiver,m.template_code from ("
        + "select count(1) as count,t.receiver as receiver,t.mail_template_code as template_code from V_MAIL_ORIGINAL_DATA t "
        + "where t.create_date>sysdate-1/24 " + "group by t.receiver,t.mail_template_code " + ") m where m.count>=20";
    return super.getSession().createSQLQuery(sql).list();
  }

}
