package com.smate.core.base.project.dao;

import com.smate.core.base.exception.DAOException;
import com.smate.core.base.project.model.ProjectExpenditure;
import com.smate.core.base.utils.data.SnsHibernateDao;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 项目经费dao
 * 
 * @author YJ
 *
 *         2019年8月3日
 */
@Repository
public class ProjectExpenditureDao extends SnsHibernateDao<ProjectExpenditure, Long> {


  @SuppressWarnings("unchecked")
  public List<ProjectExpenditure> listByPrjId(Long prjId) throws DAOException {
    String hql = "from ProjectExpenditure t where t.status = 0 and t.prjId =:prjId order by t.seqNo asc";
    return this.createQuery(hql).setParameter("prjId", prjId).list();
  };

  public void delete(Long prjId){
    String hql = "delete from  ProjectExpenditure t where t.prjId=:prjId" ;
    super.createQuery(hql).setParameter("prjId", prjId).executeUpdate();
  }
}
