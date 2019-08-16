package com.smate.center.batch.dao.sns.psn;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.smate.center.batch.model.sns.pub.PsnKwPub;
import com.smate.core.base.utils.data.SnsHibernateDao;

/**
 * 人员成果关键词详情表.
 * 
 * @author liqinghua
 * 
 */
@Repository
public class PsnKwPubDao extends SnsHibernateDao<PsnKwPub, Long> {

  @SuppressWarnings("unchecked")
  public List<PsnKwPub> getPsnKwPub(Long psnId) {

    String hql = "from PsnKwPub t where t.psnId = ?";
    return super.createQuery(hql, psnId).list();
  }

  /**
   * 删除人员成果关键词.
   * 
   * @param pubId
   */
  public void delPsnKwPub(Long pubId) {

    String hql = "delete from PsnKwPub t where t.pubId = ? ";
    super.createQuery(hql, pubId).executeUpdate();
  }

  /**
   * 查询人员成果关键词
   * 
   * @param psnId
   * @return
   */
  public List<String> findKeywordsByPsnId(Long psnId) {
    String sql = "select distinct t.keyword from psn_kw_pub t where t.pub_id in "
        + "(select a.pub_id from publication a where a.owner_psn_id = ? and a.status <> 1 and exists "
        + "(select 1 from pub_owner_match t where psn_id = ? and a.pub_id = t.pub_id and t.au_seq > 0))"
        + " and t.psn_id = ? and t.au_seq > 0";

    List<Map<String, Object>> temp = super.queryForList(sql, new Object[] {psnId, psnId, psnId});
    List<String> list = new ArrayList<String>();
    if (temp != null && temp.size() > 0) {
      for (Map<String, Object> map : temp) {
        list.add((String) map.get("KEYWORD"));
      }
    }

    return list;
  }
}
