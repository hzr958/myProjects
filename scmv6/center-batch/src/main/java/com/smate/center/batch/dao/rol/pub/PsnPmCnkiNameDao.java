package com.smate.center.batch.dao.rol.pub;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.smate.center.batch.model.rol.pub.PsnPmCnkiName;
import com.smate.core.base.utils.data.RolHibernateDao;

/**
 * 
 * 用户Cnki名称列表.
 * 
 * @author liqinghua
 * 
 */
@Repository
public class PsnPmCnkiNameDao extends RolHibernateDao<PsnPmCnkiName, Long> {

  /**
   * 判断用户确认的用户名称是否存在.
   * 
   * @param name
   * @param psnId
   * @return
   */
  public boolean isAddtNameExists(String name, Long psnId) {

    String hql = "select count(id) from PsnPmCnkiName where name = ? and psnId = ? ";
    Long count = super.findUnique(hql, name, psnId);
    if (count > 0) {
      return true;
    } else {
      return false;
    }
  }

  /**
   * 保存用户中文名.
   * 
   * @param initName
   * @param psnId
   */
  public void saveUserZhName(String zhName, Long psnId) {

    String hql = "select count(id) from PsnPmCnkiName where name = ? and psnId = ? and type = 1";
    Long count = super.findUnique(hql, zhName, psnId);
    if (count > 0) {
      return;
    }
    super.createQuery("delete from PsnPmCnkiName where psnId = ? and type = 1", psnId).executeUpdate();
    PsnPmCnkiName name = new PsnPmCnkiName(zhName, psnId, 1);
    super.save(name);
  }

  /**
   * 删除用户别名，除前缀外.
   * 
   * @param name
   * @param psnId
   */
  public void removeUserName(String name, Long psnId) {
    super.createQuery("delete from PsnPmCnkiName where name = ? and psnId = ? ", name, psnId).executeUpdate();
  }

  /**
   * 根据人员获取记录.
   * 
   * @param psnId
   * @return
   * @throws DaoException
   */
  @SuppressWarnings("unchecked")
  public List<PsnPmCnkiName> getPsnPmCnkiNameList(Long psnId) {

    String ql = "from PsnPmCnkiName where psnId = ?";
    return super.createQuery(ql, psnId).list();
  }

  /**
   * 根据人员ID删除记录.
   * 
   * @param psnId
   */
  public void deleteByPsnId(Long psnId) {
    String hql = "delete from PsnPmCnkiName where psnId=:psnId";
    super.createQuery(hql).setParameter("psnId", psnId).executeUpdate();
  }
}
