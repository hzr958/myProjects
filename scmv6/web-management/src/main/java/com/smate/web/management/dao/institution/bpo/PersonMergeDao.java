package com.smate.web.management.dao.institution.bpo;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.smate.core.base.utils.data.BpoHibernateDao;
import com.smate.web.management.model.analysis.DaoException;
import com.smate.web.management.model.institution.bpo.PersonMerge;

/**
 * 人员合并.
 * 
 * @author liangguokeng
 * 
 */
@Repository
public class PersonMergeDao extends BpoHibernateDao<PersonMerge, Long> {

  /**
   * 更据被合并ID 查找记录
   * 
   * @param delPsnId
   * @return
   * @throws DaoException
   */
  @SuppressWarnings("unchecked")
  public List<PersonMerge> findMergeByDelPsnId(Long delPsnId) throws DaoException {

    String hql = "from PersonMerge t where t.delPsnId=?";
    return super.createQuery(hql, delPsnId).list();
  }

  /**
   * 查找大于当前2个合并人员的psnId的集合
   * 
   * @param psnId
   * @param size
   * @return
   * @throws DaoException
   */
  @SuppressWarnings("unchecked")
  public List<PersonMerge> findMergeList(Long savePsnId, Long delPsnId, Integer size) throws DaoException {
    return super.createQuery("from PersonMerge t where t.savePsnId>? and t.delPsnId>? and t.status=0 order by t.id asc",
        savePsnId, delPsnId).setMaxResults(size).list();
  }

  /**
   * 
   * @param savePsnId
   * @param delPsnId
   * @return
   * @throws DaoException
   */
  public PersonMerge findPersonMerge(Long savePsnId, Long delPsnId) throws DaoException {
    String hql = "from PersonMerge t where t.savePsnId= :savePsnId and t.delPsnId= :delPsnId";
    return (PersonMerge) super.createQuery(hql).setParameter("savePsnId", savePsnId).setParameter("delPsnId", delPsnId)
        .uniqueResult();
  }

  @SuppressWarnings("unchecked")
  public List<PersonMerge> findMergeDynamicList(Long savePsnId, Long delPsnId, Integer size) throws DaoException {
    return super.createQuery(
        "from PersonMerge t where t.savePsnId>? and t.delPsnId>? and t.status=1 and t.statusExt=0 order by t.id asc",
        savePsnId, delPsnId).setMaxResults(size).list();
  }

  /**
   * 保存需合并的人员记录.
   * 
   * @param mergePsn
   * @throws DaoException
   */
  public void savePersonMerge(PersonMerge mergePsn) throws DaoException {
    if (mergePsn != null) {
      // 如果已经保存合并记录，则直接返回.
      PersonMerge tempMerge = this.findPersonMerge(mergePsn.getSavePsnId(), mergePsn.getDelPsnId());
      if (tempMerge != null) {
        return;
      }
      super.save(mergePsn);
    }
  }
}
