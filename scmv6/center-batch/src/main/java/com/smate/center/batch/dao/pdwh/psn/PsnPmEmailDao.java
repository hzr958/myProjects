package com.smate.center.batch.dao.pdwh.psn;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.smate.center.batch.model.pdwh.psn.PsnPmEmail;
import com.smate.core.base.utils.data.PdwhHibernateDao;


/**
 * 成果匹配-用户邮件表数据库操作类.
 * 
 * @author mjg
 * 
 */
@Repository
public class PsnPmEmailDao extends PdwhHibernateDao<PsnPmEmail, Long> {

  /**
   * 获取用户的邮件列表.
   * 
   * @param psnId
   * @return
   */
  public List<String> getEmailList(Long psnId) {
    String hql = "select email from PsnPmEmail t where t.psnId=? ";
    return super.find(hql, psnId);
  }

  public void deleteAllByPsnId(Long psnId) {
    String hql = "delete from PsnPmEmail t where t.psnId=? ";
    super.createQuery(hql, new Object[] {psnId}).executeUpdate();
  }

  public PsnPmEmail findByPsnAndEmail(Long psnId, String email) {
    String hql = "from PsnPmEmail t where t.psnId=? and t.email=?";
    return super.findUnique(hql, psnId, email);
  }

  public void deleteNotExists(Long psnId, List<String> emails) {
    String hql = "delete from PsnPmEmail t where t.psnId=:psnId and t.email not in(:emails)";
    super.createQuery(hql).setParameter("psnId", psnId).setParameterList("emails", emails).executeUpdate();
  }
}
