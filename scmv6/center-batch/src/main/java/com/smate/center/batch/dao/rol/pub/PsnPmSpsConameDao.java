package com.smate.center.batch.dao.rol.pub;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Repository;

import com.smate.center.batch.model.rol.pub.PsnPmSpsCoPreName;
import com.smate.center.batch.model.rol.pub.PsnPmSpsConame;
import com.smate.core.base.utils.data.RolHibernateDao;

/**
 * scopus用户确认成果合作者.
 * 
 * @author liqinghua
 * 
 */
@Repository
public class PsnPmSpsConameDao extends RolHibernateDao<PsnPmSpsConame, Long> {

  /**
   * 是否存在合作者.
   * 
   * @param initName
   * @param fullName
   * @param psnId
   * @return
   */
  public boolean isExitConame(String name, Long psnId) {
    StringBuilder sb = new StringBuilder("select count(id) from PsnPmSpsConame where name = ? and psnId = ? ");
    List<Object> p = new ArrayList<Object>();
    p.add(name);
    p.add(psnId);
    Long count = super.findUnique(sb.toString(), p.toArray());
    return count > 0;
  }

  /**
   * 是否存在合作者.
   * 
   * @param initName
   * @param fullName
   * @param psnId
   * @return
   */
  public boolean isExitCoPreName(String preName, Long psnId) {
    StringBuilder sb = new StringBuilder("select count(id) from PsnPmSpsCoPreName where preName = ? and psnId = ? ");
    Long count = super.findUnique(sb.toString(), preName, psnId);
    return count > 0;
  }

  /**
   * 保存合作者前缀.
   * 
   * @param preName
   */
  public void savePsnPmSpsCoPreName(PsnPmSpsCoPreName preName) {
    super.getSession().save(preName);
  }

  /**
   * 根据人员获取记录.
   * 
   * @param psnId
   * @return
   * @throws DaoException
   */
  @SuppressWarnings("unchecked")
  public List<PsnPmSpsConame> getPsnPmSpsConameList(Long psnId) {

    String ql = "from PsnPmSpsConame where psnId = ?";
    return super.createQuery(ql, psnId).list();
  }

  /**
   * 根据人员获取记录.
   * 
   * @param psnId
   * @return
   * @throws DaoException
   */
  @SuppressWarnings("unchecked")
  public List<PsnPmSpsCoPreName> getPsnPmSpsCoPreNameList(Long psnId) {

    String ql = "from PsnPmSpsCoPreName where psnId = ?";
    return super.createQuery(ql, psnId).list();
  }

  /**
   * 根据人员ID删除记录.
   * 
   * @param psnId
   */
  public void deleteByPsnId(Long psnId) {
    String hql = "delete from PsnPmSpsConame where psnId=:psnId";
    super.createQuery(hql).setParameter("psnId", psnId).executeUpdate();
  }
}
