package com.smate.web.management.dao.mail;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Repository;

import com.smate.core.base.utils.data.SnsHibernateDao;
import com.smate.core.base.utils.model.Page;
import com.smate.web.management.model.mail.MailSender;

/**
 * 邮件发送记录表dao
 * 
 * @author tsz
 *
 */
@Repository
public class MailSenderDao extends SnsHibernateDao<MailSender, Long> {

  public List<MailSender> findSenderList(MailSender form, Page<MailSender> page) {
    String count = "select count(1) ";
    String hql = "from MailSender t where 1=1 ";
    String order = "order by t.createDate desc";
    StringBuffer sb = new StringBuffer();
    List<Object> params = new ArrayList<Object>();
    if (form.getAccount() != null && !form.getAccount().equals("")) {
      sb.append(" and t.account=? ");
      params.add(form.getAccount());
    }
    if (form.getStatus() != null) {
      sb.append(" and t.status=? ");
      params.add(form.getStatus());
    }
    Long totalCount = super.findUnique(count + hql + sb.toString(), params.toArray());
    page.setTotalCount(totalCount);
    List<MailSender> senderList = super.createQuery(hql + sb.toString() + order, params.toArray())
        .setFirstResult(page.getFirst() - 1).setMaxResults(page.getPageSize()).list();
    page.setResult(senderList);
    return senderList;
  }

  @SuppressWarnings("unchecked")
  public MailSender getSendCount() {
    String hql =
        "select new MailSender(sum(t.maxMailCount),sum(t.todayMailCount),(sum(t.maxMailCount)-sum(t.todayMailCount))) from MailSender t where t.status!=1";
    return (MailSender) super.createQuery(hql).uniqueResult();
  }
}
