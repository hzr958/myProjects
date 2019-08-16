package com.smate.web.psn.dao.rol;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.smate.core.base.utils.data.RolHibernateDao;
import com.smate.web.psn.exception.DaoException;
import com.smate.web.psn.model.rol.PsnPmEmail;

/**
 * 用户确认email.
 * 
 * @author liqinghua
 */
@Repository
public class PsnPmEmailDao extends RolHibernateDao<PsnPmEmail, Long> {

  /**
   * 获取用户确认过的email.
   * 
   * @param email
   * @param psnId
   * @return
   */
  public PsnPmEmail getPsnPmEmail(String email, Long psnId) {

    String hql = "from PsnPmEmail where email = ? and psnId = ? ";
    List<PsnPmEmail> list = super.find(hql, email, psnId);
    if (list != null && list.size() > 0) {
      return list.get(0);
    }
    return null;
  }

  /**
   * 删除用户邮件.
   * 
   * @param email
   * @param psnId
   */
  public void removePsnPmEmail(String email, Long psnId) {

    String hql = "delete from PsnPmEmail where email = ? and psnId = ?";
    super.createQuery(hql, email, psnId).executeUpdate();
  }

  /**
   * 根据人员获取记录.
   * 
   * @param psnId
   * @return
   * @throws DaoException
   */
  @SuppressWarnings("unchecked")
  public List<PsnPmEmail> getPsnPmEmailList(Long psnId) {

    String ql = "from PsnPmEmail where psnId = ?";
    return super.createQuery(ql, psnId).list();
  }

  /**
   * 根据人员ID删除记录.
   * 
   * @param psnId
   */
  public void deleteByPsnId(Long psnId) {
    String hql = "delete from PsnPmEmail where psnId=:psnId";
    super.createQuery(hql).setParameter("psnId", psnId).executeUpdate();
  }
}
