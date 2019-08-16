package com.smate.web.management.dao.mail;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Repository;

import com.smate.core.base.utils.data.SnsHibernateDao;
import com.smate.core.base.utils.model.Page;
import com.smate.web.management.model.mail.MailClient;

/**
 * 
 * @author tsz
 *
 */
@Repository
public class MailClientDao extends SnsHibernateDao<MailClient, Long> {
  public List<MailClient> findClientList(MailClient form, Page<MailClient> page) {
    String count = "select count(1) ";
    String hql = "from MailClient t where 1=1 ";
    String order = "order by t.id desc";
    StringBuffer sb = new StringBuffer();
    List<Object> params = new ArrayList<Object>();
    if (form.getClientName() != null && !form.getClientName().equals("")) {
      sb.append(" and t.clientName=? ");
      params.add(form.getClientName());
    }
    if (form.getStatus() != null) {
      sb.append(" and t.status=? ");
      params.add(form.getStatus());
    }
    Long totalCount = super.findUnique(count + hql + sb.toString(), params.toArray());
    page.setTotalCount(totalCount);
    List<MailClient> clientList = super.createQuery(hql + sb.toString() + order, params.toArray())
        .setFirstResult(page.getFirst() - 1).setMaxResults(page.getPageSize()).list();
    page.setResult(clientList);
    return clientList;
  }

}
