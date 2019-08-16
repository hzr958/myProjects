package com.smate.center.batch.dao.rol.pub;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.smate.center.batch.model.rol.pub.PsnPmCnkiConame;
import com.smate.core.base.utils.data.RolHibernateDao;

/**
 * 用户确认Cnki成果合作者.
 * 
 * @author liqinghua
 * 
 */
@Repository
public class PsnPmCnkiConameDao extends RolHibernateDao<PsnPmCnkiConame, Long> {

  /**
   * 判断用户确认的合作者是否存在.
   * 
   * @param name
   * @param psnId
   * @return
   */
  public PsnPmCnkiConame getCnkiConame(String name, Long psnId) {

    String hql = "from PsnPmCnkiConame where name = ? and psnId = ? ";
    @SuppressWarnings("unchecked")
    List<PsnPmCnkiConame> list = super.createQuery(hql, name, psnId).list();
    if (list != null && list.size() > 0) {
      return list.get(0);
    }
    return null;
  }

  /**
   * 根据人员获取记录.
   * 
   * @param psnId
   * @return
   * @throws DaoException
   */
  @SuppressWarnings("unchecked")
  public List<PsnPmCnkiConame> getPsnPmCnkiConameList(Long psnId) {

    String ql = "from PsnPmCnkiConame where psnId = ?";
    return super.createQuery(ql, psnId).list();
  }

  /**
   * 根据人员ID删除记录.
   * 
   * @param psnId
   */
  public void deleteByPsnId(Long psnId) {
    String hql = "delete from PsnPmCnkiConame where psnId=:psnId";
    super.createQuery(hql).setParameter("psnId", psnId).executeUpdate();
  }
}
