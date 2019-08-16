package com.smate.web.management.dao.mail;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Repository;

import com.smate.core.base.utils.data.SnsHibernateDao;
import com.smate.core.base.utils.model.Page;
import com.smate.web.management.model.mail.MailBlacklist;

/**
 * 黑名单dao
 * 
 * @author tsz
 *
 */
@Repository
public class MailBlacklistDao extends SnsHibernateDao<MailBlacklist, Long> {

  public List<MailBlacklist> findBlackList(MailBlacklist form, Page<MailBlacklist> page) {
    String count = "select count(1) ";
    String hql = "from MailBlacklist t where 1=1 ";
    String order = "order by t.updateDate desc";
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
    if (StringUtils.isNotBlank(String.valueOf(form.getType()))) {
      sb.append(" and t.type=? ");
      params.add(form.getType());
    }
    Long totalCount = super.findUnique(count + hql + sb.toString(), params.toArray());
    page.setTotalCount(totalCount);
    List<MailBlacklist> blacklist = super.createQuery(hql + sb.toString() + order, params.toArray())
        .setFirstResult(page.getFirst() - 1).setMaxResults(page.getPageSize()).list();
    page.setResult(blacklist);
    return blacklist;
  }
}
