package com.smate.center.batch.dao.pdwh.pub.isi;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang.ObjectUtils;
import org.springframework.stereotype.Repository;

import com.smate.center.batch.exception.pub.DaoException;
import com.smate.center.batch.model.pdwh.pub.isi.IsiPubPsn;
import com.smate.core.base.utils.data.PdwhHibernateDao;

/**
 * ISI的成果人员关系表(对科研之友成果关系进行冗余)的数据库操作类.
 * 
 * @author mjg
 * 
 */
@Repository
public class IsiPubPsnDao extends PdwhHibernateDao<IsiPubPsn, Long> {

  /**
   * 获取人员已确认或已拒绝的成果ID列表.
   * 
   * @param psnId
   * @return
   */
  public List<IsiPubPsn> getPubIdByPsn(Long psnId) {
    String hql = "select new IsiPubPsn(pubId,result) from IsiPubPsn t where t.result in (1,2) and t.psnId=? ";
    return super.find(hql, psnId);
  }

  /**
   * 获取所有成果人员关系列表.
   * 
   * @param psnId
   * @return
   */
  public List<IsiPubPsn> getAllByPsnId(Long psnId) {
    String hql = "from IsiPubPsn t where t.psnId=? ";
    return super.find(hql, psnId);
  }

  @SuppressWarnings("unchecked")
  public List<Long> findIsiCnkiPsnIdByPubIds(List<Long> isiPubIds, List<Long> cnkiPubIds, Integer result, Integer size)
      throws DaoException {
    String sql;
    List<Object> list;
    if (CollectionUtils.isNotEmpty(isiPubIds) && CollectionUtils.isNotEmpty(cnkiPubIds)) {
      sql =
          "select psn_id from (select t.psn_id,t.score from isi_pub_psn t where t.pub_id in (:isiPubIds) and t.result=(:iresult) and t.score > 0 "
              + "union all "
              + "select c.psn_id,c.score from cnki_pub_psn c where c.pub_id in (:cnkiPubIds) and c.result=(:cresult) and c.score > 0) "
              + "group by psn_id order by max(score) desc ";
      list = super.getSession().createSQLQuery(sql).setParameterList("isiPubIds", isiPubIds)
          .setParameter("iresult", result).setParameterList("cnkiPubIds", cnkiPubIds).setParameter("cresult", result)
          .setMaxResults(size).list();

    } else if (CollectionUtils.isNotEmpty(isiPubIds) && CollectionUtils.isEmpty(cnkiPubIds)) {
      sql =
          "select t.psn_id from isi_pub_psn t where t.pub_id in (:isiPubIds) and t.result=(:result) order by score desc";
      list = super.getSession().createSQLQuery(sql).setParameterList("isiPubIds", isiPubIds)
          .setParameter("result", result).setMaxResults(size).list();
    } else if (CollectionUtils.isEmpty(isiPubIds) && CollectionUtils.isNotEmpty(cnkiPubIds)) {
      sql =
          "select c.psn_id from cnki_pub_psn c where c.pub_id in (:cnkiPubIds) and c.result=(:result) order by score desc";
      list = super.getSession().createSQLQuery(sql).setParameterList("cnkiPubIds", cnkiPubIds)
          .setParameter("result", result).setMaxResults(size).list();
    } else {
      return null;
    }
    if (CollectionUtils.isNotEmpty(list)) {
      List<Long> ids = new ArrayList<Long>();
      for (Object o : list) {
        ids.add(Long.parseLong(ObjectUtils.toString(o)));
      }
      return ids;
    } else {
      return null;
    }

  }

  /**
   * 
   * @param psnId
   */
  public void deleteByPsnId(Long psnId) {
    String hql = "delete IsiPubPsn t where t.psnId=?";
    super.createQuery(hql, new Object[] {psnId}).executeUpdate();
  }

  public IsiPubPsn getIsiPubPsn(Long psnId, Long pubId) throws DaoException {
    return super.findUnique("from IsiPubPsn t where t.pubId=? and t.psnId=?", pubId, psnId);
  }
}
