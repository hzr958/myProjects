package com.smate.web.management.dao.mail;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Repository;

import com.smate.core.base.utils.data.SnsHibernateDao;
import com.smate.core.base.utils.model.Page;
import com.smate.web.management.model.mail.MailWhitelist;

/**
 * 白名单dao
 * 
 * @author tsz
 *
 */
@Repository
public class MailWhitelistDao extends SnsHibernateDao<MailWhitelist, Long> {

  public List<MailWhitelist> findWhiteList(MailWhitelist form, Page<MailWhitelist> page) {
    String count = "select count(1) ";
    String hql = "from MailWhitelist t where 1=1 ";
    String order = "order by t.id desc";
    StringBuffer sb = new StringBuffer();
    List<Object> params = new ArrayList<Object>();
    if (form.getEmail() != null && !form.getEmail().equals("")) {
      sb.append(" and t.email=? ");
      params.add(form.getEmail());
    }
    if (StringUtils.isNotBlank(String.valueOf(form.getStatus()))) {
      sb.append(" and t.status=? ");
      params.add(form.getStatus());
    }
    Long totalCount = super.findUnique(count + hql + sb.toString(), params.toArray());
    page.setTotalCount(totalCount);
    List<MailWhitelist> whitelist = super.createQuery(hql + sb.toString() + order, params.toArray())
        .setFirstResult(page.getFirst() - 1).setMaxResults(page.getPageSize()).list();
    page.setResult(whitelist);
    return whitelist;
  }

}
