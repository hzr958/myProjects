package com.smate.web.group.dao.group.psn;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections4.CollectionUtils;
import org.springframework.stereotype.Repository;

import com.smate.core.base.psn.model.profile.PsnDisciplineKey;
import com.smate.core.base.utils.data.SnsHibernateDao;

/**
 * 
 * @author liqinghua
 * 
 */
@Repository
public class PsnDisciplineKeyDao extends SnsHibernateDao<PsnDisciplineKey, Long> {

  public Integer saveKeys(String keyWords, Long psnId, Integer permission) {

    String hql = "from PsnDisciplineKey t where t.psnId = ? and t.keyWords = ?";
    PsnDisciplineKey pdk = (PsnDisciplineKey) super.createQuery(hql, psnId, keyWords).uniqueResult();

    if (pdk == null) {
      pdk = new PsnDisciplineKey(keyWords, psnId, 1);
    } else {
      pdk.setStatus(1);
    }
    this.save(pdk);
    return 1;
  }

  public void updateKwStatusByPsnId(Long psnId, Integer status) {
    String hql = "update PsnDisciplineKey t set t.status= ? where t.psnId=? ";
    super.createQuery(hql, status, psnId).executeUpdate();
  }

  /**
   * 查找psnId的有效关键词
   * 
   * @param psnId
   * @return
   */
  @SuppressWarnings("unchecked")
  public List<Long> findPsnDiscKeyId(Long psnId, Integer status) {

    String hql = "select t.id from PsnDisciplineKey t where t.psnId=? and t.status=?";
    return super.createQuery(hql, psnId, status).list();

  }

  public List<PsnDisciplineKey> findPsnDisciplineKey(Long psnId, List<Long> idList, Integer status) {
    StringBuilder hql = null;
    Map<String, Object> values = new HashMap<String, Object>();
    values.put("psnId", psnId);

    if (CollectionUtils.isNotEmpty(idList)) {
      hql = new StringBuilder("from PsnDisciplineKey t where t.psnId = :psnId and t.id in(:idList) and t.status=1");
      values.put("idList", idList);
    } else {
      hql = new StringBuilder("from PsnDisciplineKey t where t.psnId = :psnId and t.status=1");
    }

    values.put("status", status);
    hql.append(" and t.status=:status order by t.id");

    return super.find(hql.toString(), values);
  }

  /**
   * 查找人员关键词List
   * 
   * @param psnId
   * @param status
   * @return
   */
  public List<PsnDisciplineKey> findPsnDisciplineKeyByPsnId(Long psnId, Integer status) {
    String hql =
        "select new PsnDisciplineKey(id, keyWords, psnId) from PsnDisciplineKey t where t.psnId=:psnId and t.status=:status";
    return super.createQuery(hql).setParameter("psnId", psnId).setParameter("status", status).list();
  }
}
