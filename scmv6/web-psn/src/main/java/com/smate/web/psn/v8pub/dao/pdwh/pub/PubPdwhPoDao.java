package com.smate.web.psn.v8pub.dao.pdwh.pub;

import java.util.List;
import java.util.Map;

import org.apache.commons.collections4.CollectionUtils;
import org.springframework.stereotype.Repository;

import com.smate.core.base.utils.data.PdwhHibernateDao;
import com.smate.web.psn.v8pub.model.pdwh.pub.PubPdwhPo;

/**
 * @author houchuanjie
 * @date 2018/06/01 17:44
 */
@Repository
public class PubPdwhPoDao extends PdwhHibernateDao<PubPdwhPo, Long> {

  public List<PubPdwhPo> getCollectedPubs(List<Long> pubPdwhIds) {
    String hql = "from PubPdwhPo p where p.pubId in (:ids) ";
    return this.createQuery(hql).setParameterList("ids", pubPdwhIds).list();
  }

  @SuppressWarnings("unchecked")
  public List<Map<String, Object>> findPubCite(List<Long> pdwhPubIds) {
    if (CollectionUtils.isEmpty(pdwhPubIds)) {
      return null;
    }
    String hql =
        "select new Map(t.publishYear as pubYear,count(t.pubId) as count) from PubPdwhPo t,PdwhCitedRelation t2 "
            + " where t.pubId = t2.pdwhCitedPubId and t2.pdwdPubId in(:pdwhPubIds) group by t.publishYear order by t.publishYear";
    List<Map<String, Object>> list = super.createQuery(hql).setParameterList("pdwhPubIds", pdwhPubIds).list();
    return list;
  }

}
