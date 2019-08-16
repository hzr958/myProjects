package com.smate.web.management.dao.analysis.sns;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Repository;

import com.smate.core.base.utils.data.SnsHibernateDao;
import com.smate.web.management.model.analysis.DaoException;
import com.smate.web.management.model.analysis.sns.PsnDisciplineSuper;

/**
 * 个人研究领域父二级DAO.
 * 
 * @author chenxiangrong
 * 
 */
@Repository
public class PsnDisciplineSuperDao extends SnsHibernateDao<PsnDisciplineSuper, Long> {

  public void deleteDiscByPsn(Long psnId) throws DaoException {
    String hql = "delete from PsnDisciplineSuper t where t.psnId=?";
    super.createQuery(hql, psnId).executeUpdate();
  }

  public void deleteDiscByPsn(Long psnId, Long discId) throws DaoException {
    String hql = "delete from PsnDisciplineSuper t where t.psnId=? and ( t.discId=? or t.superDiscId =?)";
    super.createQuery(hql, psnId, discId, discId).executeUpdate();
  }

  public List getDiscIdByPsns(List<Long> psnIdList) throws DaoException {
    StringBuffer hql = new StringBuffer();
    hql.append("select t1.id,t1.zh_name,t1.en_name,t1.disc_code,t1.super_id,t2.total from const_discipline t1,");
    hql.append("(select t.disc_id,count(1) as total from psn_discipline_super t where t.psn_id in(");
    List<Object> params = new ArrayList<Object>();
    for (int i = 0, size = psnIdList.size(); i < size - 1; i++) {
      hql.append("?,");
      params.add(psnIdList.get(i));
    }
    hql.append("?)");
    params.add(psnIdList.get(psnIdList.size() - 1));
    hql.append(" group by t.disc_id) t2");
    hql.append(" where t1.id=t2.disc_id");
    return super.queryForList(hql.toString(), params.toArray());
  }

  @SuppressWarnings("unchecked")
  public List<PsnDisciplineSuper> getPsnDisciplineSuper(Long psnId) throws DaoException {
    return super.createQuery("from PsnDisciplineSuper t where t.psnId=? ", psnId).list();
  }

  public PsnDisciplineSuper getDiscByPsnDis(Long psnId, Long discId) throws DaoException {
    String hql = "from PsnDisciplineSuper t where t.psnId=? and t.discId=?";
    return super.findUnique(hql, new Object[] {psnId, discId});
  }

  public int getHasDiscCount(List<Long> psnIdList) throws DaoException {
    String hql = "select count(distinct t.psnId) from PsnDisciplineSuper t where t.psnId in(:psnIdList)";
    Long count = (Long) super.createQuery(hql).setParameterList("psnIdList", psnIdList).uniqueResult();
    return count.intValue();
  }

  public int getDiscByPsn(Long psnId) throws DaoException {
    String hql = "select count(t.id) from PsnDisciplineSuper t where t.psnId=?";
    Long count = (Long) super.createQuery(hql, psnId).uniqueResult();
    return count.intValue();
  }

  /**
   * 更新关键词psnId(人员合并用)
   * 
   * @author zk
   */
  public void updateDiscPsnId(Long fromPsnId, Long toPsnId, Long id) throws DaoException {
    super.createQuery("update PsnDisciplineSuper set psnId=? where id=?", toPsnId, id).executeUpdate();
  }
}
