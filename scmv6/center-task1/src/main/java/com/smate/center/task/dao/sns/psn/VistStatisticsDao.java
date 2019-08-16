package com.smate.center.task.dao.sns.psn;

import java.util.List;

import org.hibernate.criterion.CriteriaSpecification;
import org.springframework.stereotype.Repository;

import com.smate.center.task.exception.DaoException;
import com.smate.center.task.model.sns.psn.VistStatistics;
import com.smate.core.base.utils.data.SnsHibernateDao;

/**
 * 
 * 访问他人主页
 * 
 * @author zx
 *
 */

@Repository
public class VistStatisticsDao extends SnsHibernateDao<VistStatistics, Long> {
  @SuppressWarnings("unchecked")
  public List<VistStatistics> getVistStatisIds(Long starId, int size) throws DaoException {
    String sql = "select new VistStatistics(t.psnId, t.ip, t.id) from VistStatistics t where t.id>:starId";
    return super.createQuery(sql).setParameter("starId", starId).setMaxResults(size).list();
  }

  public void updateRegionId(Long id, Long regionId) throws DaoException {
    String sql = "update VistStatistics set provinceRegionId=:regionId where id=:id";
    super.createQuery(sql).setParameter("regionId", regionId).setParameter("id", id).executeUpdate();
  }

  /**
   * 遍历查询上周有阅读数据的人员
   */
  @SuppressWarnings("rawtypes")
  public List findVistPsn(Integer size) throws DaoException {
    String hql =
        "select  sum(r.count) as count,r.vistPsnId as psnId from VistStatistics r where trunc(r.createDate)>=trunc(sysdate-7)  group by r.vistPsnId having count(r.vistPsnId)>=1";
    return super.createQuery(hql).setResultTransformer(CriteriaSpecification.ALIAS_TO_ENTITY_MAP).setMaxResults(500)
        .setFirstResult(500 * size).list();
  }

  /**
   * 查找访问记录
   * 
   * @param psnId
   * @param vistPsnId
   * @param actionKey
   * @param actionType
   * @param formateDate
   * @return
   * @throws DaoException
   */
  @SuppressWarnings("unchecked")
  public VistStatistics findVistRecord(Long psnId, Long vistPsnId, Long actionKey, Integer actionType, Long formateDate,
      String ip, Long regionId) throws DaoException {
    String hql =
        "from VistStatistics t where t.psnId=:psnId and t.vistPsnId=:vistPsnId and t.actionKey =:actionKey and t.actionType =:actionType and t.formateDate =:formateDate and t.ip =:ip and t.provinceRegionId =:regionId";
    List<VistStatistics> list = super.createQuery(hql).setParameter("psnId", psnId).setParameter("vistPsnId", vistPsnId)
        .setParameter("actionKey", actionKey).setParameter("actionType", actionType)
        .setParameter("formateDate", formateDate).setParameter("ip", ip).setParameter("regionId", regionId).list();
    if (list != null && list.size() > 0) {
      return list.get(0);
    } else {
      return null;
    }
  }

}
