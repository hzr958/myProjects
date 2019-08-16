package com.smate.sie.core.base.utils.dao.prj;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.smate.core.base.utils.data.SieHibernateDao;
import com.smate.sie.core.base.utils.model.prj.SiePrjDiscipline;

@Repository
public class SiePrjDisciplineDao extends SieHibernateDao<SiePrjDiscipline, Long> {

  /**
   * 查询项目学科
   * 
   * @param prjId
   * @return
   */
  public List<SiePrjDiscipline> getByPrjId(Long prjId) {
    return (List<SiePrjDiscipline>) super.createQuery("from SiePrjDiscipline t where t.prjId = ?", prjId).list();
  }

  public void deleteByprjId(Long prjId) {
    String hql = "delete from SiePrjDiscipline where prjId = ? ";
    super.createQuery(hql, prjId).executeUpdate();
  }

  public SiePrjDiscipline getDisciplineByName(String name) {
    String hql = "from SiePrjDiscipline where zhName = :name";
    return (SiePrjDiscipline) super.createQuery(hql).setParameter("name", name).uniqueResult();
  }

}
