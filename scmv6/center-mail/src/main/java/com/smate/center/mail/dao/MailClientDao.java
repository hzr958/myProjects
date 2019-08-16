package com.smate.center.mail.dao;

import java.util.List;
import java.util.Random;

import org.apache.commons.collections4.CollectionUtils;
import org.springframework.stereotype.Repository;

import com.smate.center.mail.model.MailClient;
import com.smate.core.base.utils.data.SnsHibernateDao;

/**
 * 
 * @author tsz
 *
 */
@Repository
public class MailClientDao extends SnsHibernateDao<MailClient, Long> {

  /**
   * 获取 模板优先的客户端
   * 
   * @return
   */
  @SuppressWarnings("unchecked")
  public List<MailClient> getMailClientByTemplateCode(String code) {

    String hql = "from MailClient t where t.status=0 and t.priorTemplate like :code ";
    return super.createQuery(hql).setParameter("code", "%:" + code + ":%").list();

  }

  public int getMailClientWaitTimeByName(String clientName) {
    String hql = "select t.waitTime from MailClient t where t.clientName=:clientName";
    return (int) super.createQuery(hql).setParameter("clientName", clientName).uniqueResult();
  }

  /**
   * 获取 发送账号 优先的客户端
   * 
   * @return
   */
  @SuppressWarnings("unchecked")
  public List<MailClient> getMailClientByAccount(String account) {
    String hql = "from MailClient t where t.status=0 and t.priorSenderAccount like :account";
    return super.createQuery(hql).setParameter("account", "%:" + account + ":%").list();

  }

  /**
   * 获取 接收邮件类型 优先的客户端
   * 
   * @return
   */
  @SuppressWarnings("unchecked")
  public List<MailClient> getMailClientByEmailType(String emailType) {

    String hql = "from MailClient t where t.status=0 and t.priorEmail like :emailType ";
    return super.createQuery(hql).setParameter("emailType", "%:" + emailType + ":%").list();

  }

  /**
   * 获取 没有配置优先级的可用客户端
   * 
   * @return
   */
  @SuppressWarnings("unchecked")
  public List<MailClient> getMailClientByNoPrior() {
    String hql =
        "from MailClient t where t.status=0 and t.priorEmail is null and t.priorSenderAccount is null and t.priorTemplate is null ";
    return super.createQuery(hql).list();

  }

  /**
   * 获取 可用客户端
   * 
   * @return
   */
  @SuppressWarnings("unchecked")
  public List<MailClient> getAvailableMailClient() {
    String hql = "from MailClient t where t.status=0 ";
    return super.createQuery(hql).list();
  }

  /**
   * 获取 不 可用客户端
   * 
   * @return
   */
  @SuppressWarnings("unchecked")
  public List<MailClient> getUnavailableMailClient() {
    String hql = "from MailClient t where t.status=1 ";
    return super.createQuery(hql).list();

  }

  /**
   * 获取 低优先级的等待时间
   * 
   * @return
   */
  @SuppressWarnings("unchecked")
  public int getMailClientWaitTime() {
    // 各个客户端都有配置时间 随机取一个
    String hql = "select t.waitTime from MailClient t where t.status=0";
    List<Integer> list = super.createQuery(hql).list();
    Random ran = new Random();
    int ranIndex = ran.nextInt(list.size());
    int result = 1;
    if (CollectionUtils.isNotEmpty(list)) {
      Integer preR = list.get(ranIndex);
      if (preR != null) {
        result = preR;
      }
    }
    return result;
  }
}
