package com.smate.center.batch.dao.rol.pub;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.smate.center.batch.model.rol.pub.PsnPmCoemailRol;
import com.smate.core.base.utils.data.RolHibernateDao;

/**
 * 用户确认的成果合作者email信息.
 * 
 * @author liqinghua
 * 
 */
@Repository
public class PsnPmCoemailRolDao extends RolHibernateDao<PsnPmCoemailRol, Long> {

  /**
   * 根据人员ID删除记录.
   * 
   * @param psnId
   */
  public void deleteByPsnId(Long psnId) {
    String hql = "delete from PsnPmCoemailRol where psnId=:psnId";
    super.createQuery(hql).setParameter("psnId", psnId).executeUpdate();
  }

  /**
   * 根据人员获取记录.
   * 
   * @param psnId
   * @return
   * @throws DaoException
   */
  @SuppressWarnings("unchecked")
  public List<PsnPmCoemailRol> getPsnPmCoemailList(Long psnId) {

    String ql = "from PsnPmCoemailRol where psnId = ?";
    return super.createQuery(ql, psnId).list();
  }
}
