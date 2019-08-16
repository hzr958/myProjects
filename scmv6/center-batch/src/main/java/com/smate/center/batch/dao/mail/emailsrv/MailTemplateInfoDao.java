package com.smate.center.batch.dao.mail.emailsrv;

import java.util.List;

import org.hibernate.NonUniqueResultException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

import com.smate.center.batch.exception.pub.DaoException;
import com.smate.center.batch.model.emailsimplify.MailPromoteInfo;
import com.smate.center.batch.model.mail.emailsrv.MailTemplateInfo;
import com.smate.core.base.utils.data.EmailSrvHibernateDao;
import com.smate.core.base.utils.model.Page;

/**
 * 模板信息表DAO
 * 
 * @author zk
 * 
 */
@Repository
public class MailTemplateInfoDao extends EmailSrvHibernateDao<MailTemplateInfo, Long> {

  protected Logger logger = LoggerFactory.getLogger(getClass());

  public Integer findTemplateCodeByName(String templateName) throws DaoException {
    StringBuilder builder = new StringBuilder("select t.tempCode from MailTemplateInfo t where t.name=?");
    try {
      Integer tempCode = (Integer) super.createQuery(builder.toString(), templateName).uniqueResult();
      if (tempCode != null) {
        return tempCode;
      }
      return 0;
    } catch (NonUniqueResultException e) {
      logger.error("模板信息表中有重名的模板", e);
      throw new DaoException("模板信息表中有重名的模板", e);
    }
  }

  public MailTemplateInfo findTemplateByName(String templateName) throws DaoException {
    StringBuilder builder = new StringBuilder("from MailTemplateInfo t where t.name=?");
    return (MailTemplateInfo) super.createQuery(builder.toString(), templateName).uniqueResult();
  }

  public Integer findPriorCodeByName(String templateName) throws DaoException {
    StringBuilder builder = new StringBuilder("select t.priorCode from MailTemplateInfo t where t.name=?");
    try {
      Integer priorCode = (Integer) super.createQuery(builder.toString(), templateName).uniqueResult();
      if (priorCode != null) {
        return priorCode;
      }
      return 0;
    } catch (NonUniqueResultException e) {
      logger.error("模板信息表中有重名的模板," + templateName, e);
      throw new DaoException("模板信息表中有重名的模板," + templateName, e);
    }
  }

  public Long getMailTempCountForPromote() throws DaoException {
    String hql = "from MailTemplateInfo t where t.folderName='promote' ";
    return super.countHqlResult(hql);
  }

  @SuppressWarnings("unchecked")
  public List<Integer> getMailTempCodeForPromote(Page<MailPromoteInfo> page) throws DaoException {
    String hql =
        "select t.tempCode from MailTemplateInfo t where t.folderName='promote' and t.status=1 order by t.tempCode desc ";
    return super.createQuery(hql).setFirstResult(page.getFirst() - 1).setMaxResults(page.getPageSize()).list();
  }

  public MailTemplateInfo getMailTempInfoForPromote(Integer tempId) throws DaoException {
    String hql = "select new MailTemplateInfo(t.tempCode,t.name,t.subject) from MailTemplateInfo t where t.tempCode=? ";
    return (MailTemplateInfo) super.createQuery(hql, tempId).uniqueResult();
  }

  @SuppressWarnings("unchecked")
  public List<MailTemplateInfo> getMailTempInfoForPromoteList() throws DaoException {
    String hql =
        "select new MailTemplateInfo(tempCode,name,subject) from MailTemplateInfo t where t.folderName='promote' ";
    return super.createQuery(hql).list();
  }

  @SuppressWarnings("unchecked")
  public List<String> findTemplateName(String ids) throws DaoException {
    StringBuilder builder = new StringBuilder("select t.name from MailTemplateInfo t where t.tempCode in(" + ids + ")");
    return super.createQuery(builder.toString()).list();
  }
}
