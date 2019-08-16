package com.smate.center.batch.dao.pdwh.pub;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.hibernate.transform.Transformers;
import org.springframework.stereotype.Repository;

import com.smate.center.batch.model.pdwh.pub.JournalNoSeq;
import com.smate.core.base.utils.data.PdwhHibernateDao;


/**
 * 期刊没有序列dao 供sns发送消息同步用
 * 
 * @author tsz
 * 
 */
@Repository
public class JournalNoSeqDao extends PdwhHibernateDao<JournalNoSeq, Long> {

  @SuppressWarnings("unchecked")
  public Map<Long, String> getRomeoColour(Set<Long> jidSet) {
    Map<Long, String> jidMap = new HashMap<Long, String>();
    if (jidSet.size() == 0)
      return jidMap;
    String sql =
        "select bj.jnl_id,bj.romeo_colour from  base_journal bj where bj.jnl_id in (:jids) and bj.romeo_colour  is not null";
    List<Map<String, Object>> jidtempMap = this.getSession().createSQLQuery(sql).setParameterList("jids", jidSet)
        .setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP).list();
    for (Map<String, Object> tempMap : jidtempMap) {
      jidMap.put(Long.valueOf(tempMap.get("JNL_ID").toString()), tempMap.get("ROMEO_COLOUR").toString());
    }
    return jidMap;
  }
}
