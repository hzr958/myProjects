package com.smate.center.mail.dao;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Repository;

import com.smate.center.mail.model.MailRecord;
import com.smate.core.base.utils.data.SnsHibernateDao;

/**
 * 邮件发送记录表dao
 * 
 * @author tsz
 *
 */
@Repository
public class MailRecordDao extends SnsHibernateDao<MailRecord, Long> {

  /**
   * 获取 待回收的 已分配发送账号 以及客户端记录
   * 
   * @param size
   * @param recoveryTime 回收时间 s (超过分配时间多少 s 就回收)
   * @return
   */
  public List<MailRecord> getToBeRecovery(int size, int recoveryTime) {

    Calendar cal = Calendar.getInstance();
    cal.add(Calendar.SECOND, -recoveryTime);
    Date date = cal.getTime();
    String hql =
        "From MailRecord t where (t.status=1 or t.status=10) and t.sender is not null and t.distributeDate<=:targetDate ";
    return super.createQuery(hql).setTimestamp("targetDate", date).setMaxResults(size).list();

  }

  /**
   * 根据优先级 获取 邮件
   * 
   * @param size
   * @param level
   * @return
   */
  @SuppressWarnings("unchecked")
  public List<MailRecord> getToBeAllocatedMail(int size, String level, int status, String client) {

    if (StringUtils.isNotBlank(client)) {
      return super.createQuery(
          "from MailRecord t where t.priorLevel=:level and t.status=:status and t.mailClient=:client order by t.mailId asc")
              .setParameter("status", status).setParameter("level", level).setParameter("client", client)
              .setMaxResults(size).list();
    } else {
      return super.createQuery("from MailRecord t where t.priorLevel=:level and t.status=:status order by t.mailId asc")
          .setParameter("status", status).setParameter("level", level).setMaxResults(size).list();
    }

  }

  public List<MailRecord> findListByDate(int day, int size) {
    // String hql = "from MailRecord t where t.status in (2,3) and t.createDate < sysdate-:day";
    String hql = "from MailRecord t where t.createDate < sysdate-:day";// 将一个月前的全部移到历史表
    return super.createQuery(hql).setParameter("day", day).setMaxResults(size).list();
  }

  public boolean existRecordList(int day) {
    String hql = "select count(1) from MailRecord t where t.createDate < sysdate-:day";
    Long count = (Long) super.createQuery(hql).setParameter("day", day).uniqueResult();
    if (count != null && count != 0) {
      return true;
    }
    return false;
  }

}
