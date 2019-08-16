package com.smate.center.mail.dao;

import java.util.Date;
import java.util.List;

import org.apache.commons.collections4.CollectionUtils;
import org.springframework.stereotype.Repository;

import com.smate.center.mail.model.MailSender;
import com.smate.core.base.utils.data.SnsHibernateDao;

/**
 * 邮件发送账号 dao
 * 
 * @author tsz
 *
 */
@Repository
public class MailSenderDao extends SnsHibernateDao<MailSender, Long> {

  /**
   * 获取 管理员发送邮件
   * 
   * @return
   */
  public MailSender getMonitorMailSender() {
    String hql = "from MailSender t where t.status=88";
    List<MailSender> list = super.createQuery(hql).list();
    if (CollectionUtils.isEmpty(list)) {
      list = getAvailableSenderAccount();
    }
    if (CollectionUtils.isNotEmpty(list)) {
      return list.get(0);
    } else {
      return null;
    }
  }

  /**
   * 获取配置了 客户端 优先的可用发送账号
   * 
   * @return
   */
  @SuppressWarnings("unchecked")
  public List<MailSender> getMailSenderByEmailType(String emailType) {
    String hql =
        "from MailSender t where t.status=0 and t.priorEmail like :emailType and t.maxMailCount>todayMailCount";
    return super.createQuery(hql).setParameter("emailType", "%:" + emailType + ":%").list();
  }

  /**
   * 获取配置了 模板 优先的可用发送账号
   * 
   * @return
   */
  @SuppressWarnings("unchecked")
  public List<MailSender> getMailSenderByTemplate(String templateCode) {
    String hql =
        "from MailSender t where t.status=0 and t.priorTemplateCode like :templateCode and t.maxMailCount>todayMailCount";
    return super.createQuery(hql).setParameter("templateCode", "%:" + templateCode + ":%").list();
  }

  /**
   * 获取配置了 客户端 优先的可用发送账号
   * 
   * @return
   */
  @SuppressWarnings("unchecked")
  public List<MailSender> getMailSenderByClient(String clinetName) {

    String hql =
        "from MailSender t where t.status=0 and t.priorClient like :clinetName and t.maxMailCount>todayMailCount";
    return super.createQuery(hql).setParameter("clinetName", "%:" + clinetName + ":%").list();
  }

  /**
   * 获取 没有配置优先的发送账号
   * 
   * @return
   */
  @SuppressWarnings("unchecked")
  public List<MailSender> getMailSenderByNoprior() {
    String hql =
        "from MailSender t where t.status=0 and t.priorEmail is null and t.priorClient is null and t.priorTemplateCode is null and t.maxMailCount>todayMailCount";
    return super.createQuery(hql).list();
  }

  /**
   * 根据账号 获取 发送者
   * 
   * @param account
   * @return
   */
  public MailSender getSender(String account) {
    String hql = "from MailSender t where  t.account=:account";
    return (MailSender) super.createQuery(hql).setParameter("account", account).uniqueResult();

  }

  /**
   * 获取 所有可用发送账号
   * 
   * @return
   */
  @SuppressWarnings("unchecked")
  public List<MailSender> getAvailableSenderAccount() {
    String hql = "from MailSender t where t.status=0";
    return super.createQuery(hql).list();
  }

  /**
   * 初始化 已经发送统计
   */
  public void initTodayMailCount() {
    List<MailSender> list = super.getAll();
    if (CollectionUtils.isNotEmpty(list)) {
      for (MailSender mailSender : list) {
        if (mailSender.getStatus() != 88 && mailSender.getStatus() == 9) {
          mailSender.setStatus(0);
          mailSender.setMsg("每天初始化状态以及统计数");
        }
        mailSender.setUpdateDate(new Date());
        mailSender.setTodayMailCount(0);
        super.save(mailSender);
      }
    }

  }

  public List<MailSender> findAll() {
    String hql = "select new MailSender(t.id,t.account,t.password,t.host) from MailSender t ";
    return super.createQuery(hql).list();
  }

  /**
   * 获取 管理员发送账号
   * 
   * @return
   */
  @SuppressWarnings("unchecked")
  public List<MailSender> getManagerMailSender() {
    String hql = "from MailSender t where t.status=88";
    return super.createQuery(hql).list();
  }
}
