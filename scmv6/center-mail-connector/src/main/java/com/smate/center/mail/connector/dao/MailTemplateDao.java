package com.smate.center.mail.connector.dao;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Repository;

import com.smate.center.mail.connector.model.MailTemplate;
import com.smate.core.base.utils.data.SnsHibernateDao;
import com.smate.core.base.utils.model.Page;

/**
 * 模版Dao
 * 
 * @author zzx
 *
 */
@Repository
public class MailTemplateDao extends SnsHibernateDao<MailTemplate, Integer> {
  /**
   * 通过邮件模版查找邮件标识
   * 
   * @param tempName
   * @return
   */
  public List<Integer> findCodeByTempName(String tempName) {
    String hql = "select t.templateCode from MailTemplate t where t.templateName like :tempName";
    return this.createQuery(hql).setParameter("tempName", tempName + "%").list();

  };

  public String findPriorByTempCode(Integer tempCode) {
    String hql = "select t.priorLevel from MailTemplate t where t.templateCode = ?";
    return ObjectUtils.toString(this.createQuery(hql, tempCode).uniqueResult());
  };

  public List<MailTemplate> findTemplateList(MailTemplate form, Page<MailTemplate> page, String subject) {
    String count = "select count(1) ";
    String hql = "from MailTemplate t where 1=1 ";
    String order = "order by t.createDate desc";
    StringBuffer sb = new StringBuffer();
    List<Object> params = new ArrayList<Object>();
    if (form.getTemplateCode() != null) {
      sb.append(" and t.templateCode=? ");
      params.add(form.getTemplateCode());
    }

    if (StringUtils.isNotBlank(form.getTemplateName())) {
      sb.append(" and t.templateName=? ");
      params.add(form.getTemplateName());
    }

    if (StringUtils.isNotBlank(subject)) {
      sb.append(" and (t.subject_zh like ? or t.subject_en like ? )");
      params.add("%" + subject + "%");
      params.add("%" + subject + "%");
    }

    if (form.getStatus() != null) {
      sb.append(" and t.status=? ");
      params.add(form.getStatus());
    }

    if (form.getLimitStatus() != null) {
      sb.append(" and t.limitStatus=? ");
      params.add(form.getLimitStatus());
    }

    Long totalCount = super.findUnique(count + hql + sb.toString(), params.toArray());
    page.setTotalCount(totalCount);
    List<MailTemplate> templateList = super.createQuery(hql + sb.toString() + order, params.toArray())
        .setFirstResult(page.getFirst() - 1).setMaxResults(page.getPageSize()).list();
    page.setResult(templateList);
    return templateList;
  }

}
