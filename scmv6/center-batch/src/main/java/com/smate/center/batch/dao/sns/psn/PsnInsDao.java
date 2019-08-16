package com.smate.center.batch.dao.sns.psn;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.smate.center.batch.exception.pub.DaoException;
import com.smate.center.batch.model.sns.pub.PsnInsSns;
import com.smate.core.base.utils.constant.ServiceConstants;
import com.smate.core.base.utils.data.SnsHibernateDao;
import com.smate.core.base.utils.model.security.PsnInsPk;

/**
 * 数据层接口.
 * 
 * @author oyh
 * 
 */
@Repository
public class PsnInsDao extends SnsHibernateDao<PsnInsSns, PsnInsPk> {

  public List<Long> findInsIdsByPsnId(Long psnId) {

    String queryStr = "select t.pk.insId  from PsnInsSns t where t.pk.psnId=? and t.status = 1 ";
    return this.createQuery(queryStr, new Object[] {psnId}).list();

  }

  public List<Long> findInsIdsByPsnId(Long psnId, List<Long> excludeInsId) {
    Map<String, Object> params = new HashMap<String, Object>();
    params.put("psnId", psnId);
    params.put("excludeInsId", excludeInsId);
    String queryStr =
        "select t.pk.insId  from PsnInsSns t where t.pk.psnId=:psnId and t.pk.insId not in (:excludeInsId) and t.status = 1 ";
    return this.createQuery(queryStr, params).list();

  }

  public List<Long> findAllowSubmitInsIdsByPsnId(Long psnId) {

    String queryStr = "select t.pk.insId  from PsnInsSns t where t.pk.psnId=? and allowSubmitPub = 1 and t.status = 1 ";
    return this.createQuery(queryStr, new Object[] {psnId}).list();

  }

  public void updatePsnIns(Long fromPsnId, Long toPsnId, Long insId) throws DaoException {
    this.createQuery("update PsnInsSns t  set t.pk.psnId=? where t.pk.psnId=? and t.pk.insId=?",
        new Object[] {toPsnId, fromPsnId, insId}).executeUpdate();
  }

  /**
   * 删除人员与单位的关系.
   * 
   * @param psnId
   * @param insId
   */
  public void deletePsnIns(Long psnId, Long insId) {
    String hql = "update PsnInsSns t set t.status = 9 where t.pk.psnId = ? and t.pk.insId = ?";
    super.createQuery(hql, psnId, insId).executeUpdate();
  }

  /**
   * 查询个人的有效的单位关系.
   * 
   * @return
   */
  public List<Object[]> findCanSubmitInsByPsnId(Long psnId) {
    // String sql =
    // "select t.pk.insId,t1.zhName,t1.enName,t1.status from PsnInsSns t,Institution t1 where
    // t.pk.insId=t1.id and t1.status=2 and t.allowSubmitPub=1 and t.pk.psnId=?";
    // 不判断institution status=2的状态
    String sql =
        "select t.pk.insId,t1.zhName,t1.enName,t1.status from PsnInsSns t,Institution t1  where t.pk.insId=t1.id and t.status = 1  and t.allowSubmitPub=1 and t.pk.psnId=?";
    org.hibernate.Query query = super.createQuery(sql, new Object[] {psnId});
    return query.list();
  }

  /**
   * 查找人员与单位关系.
   * 
   * @param psnId
   * @param insId
   * @return
   */
  public PsnInsSns findPsnInsSns(Long psnId, Long insId) {
    String hql = "from PsnInsSns t where t.pk.psnId = ? and t.pk.insId = ?";
    return super.findUnique(hql, psnId, insId);
  }

  /**
   * 查找个人加入单位.
   * 
   * @param psnId
   * @return
   */
  public List<PsnInsSns> findPsnInsSnsList(Long psnId) {
    String hql = "from PsnInsSns t where t.pk.psnId=? and (t.status=? or t.status=?)";
    return this.createQuery(hql, new Object[] {psnId, 0, 1}).list();
  }

  public List<Long> getPsnInsSnsInsIds(Long psnId) throws DaoException {
    String hql = " select t.pk.insId from PsnInsSns t where t.pk.psnId=? ";
    return this.createQuery(hql, psnId).list();
  }

  /**
   * 查找个人加入的科研在线单位ID.
   * 
   * @param psnId
   * @return
   */
  public List<Long> getPsnJoinIns(Long psnId) {

    String hql =
        "select t.pk.insId from PsnInsSns t where t.pk.psnId= :psnId and t.pk.insId not in(:nsfcInsId) and t.status = 1";
    return super.createQuery(hql).setParameter("psnId", psnId)
        .setParameterList("nsfcInsId", ServiceConstants.NSFC_INS_IDS).list();
  }
}
