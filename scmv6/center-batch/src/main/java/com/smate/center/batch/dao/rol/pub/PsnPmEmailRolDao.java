package com.smate.center.batch.dao.rol.pub;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.smate.center.batch.model.rol.pub.PsnPmEmailRol;
import com.smate.core.base.utils.data.RolHibernateDao;

/**
 * 用户确认email.
 * 
 * @author liqinghua
 * 
 */
@Repository
public class PsnPmEmailRolDao extends RolHibernateDao<PsnPmEmailRol, Long> {

  /**
   * 获取用户确认过的email.
   * 
   * @param email
   * @param psnId
   * @return
   */
  public PsnPmEmailRol getPsnPmEmail(String email, Long psnId) {

    String hql = "from PsnPmEmailRol where email = ? and psnId = ? ";
    List<PsnPmEmailRol> list = super.find(hql, email, psnId);
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

    String hql = "delete from PsnPmEmailRol where email = ? and psnId = ?";
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
  public List<PsnPmEmailRol> getPsnPmEmailList(Long psnId) {

    String ql = "from PsnPmEmailRol where psnId = ?";
    return super.createQuery(ql, psnId).list();
  }

  /**
   * 根据人员ID删除记录.
   * 
   * @param psnId
   */
  public void deleteByPsnId(Long psnId) {
    String hql = "delete from PsnPmEmailRol where psnId=:psnId";
    super.createQuery(hql).setParameter("psnId", psnId).executeUpdate();
  }
}
