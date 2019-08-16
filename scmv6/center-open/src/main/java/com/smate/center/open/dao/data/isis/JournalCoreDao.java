package com.smate.center.open.dao.data.isis;



import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.hibernate.transform.Transformers;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.smate.center.open.isis.model.data.isis.JournalCore;
import com.smate.core.base.utils.data.PdwhHibernateDao;


/**
 * 
 * @author hp
 * 
 */
@Repository
@Transactional(propagation = Propagation.REQUIRES_NEW, rollbackFor = Exception.class)
public class JournalCoreDao extends PdwhHibernateDao<JournalCore, Long> {
  @SuppressWarnings("unchecked")
  public Map<Long, Long> getHxj(Set<Long> jidSet) {
    Map<Long, Long> xhjMap = new HashMap<Long, Long>();
    if (jidSet.size() == 0)
      return xhjMap;
    String sql =
        "select j.jid,bjc.core_flag from  base_journal_core bjc ,journal j where bjc.jnl_id=j.match_base_jnl_id  and j.jid in (:jids)";
    List<Map<String, Object>> jidtempMap = this.getSession().createSQLQuery(sql).setParameterList("jids", jidSet)
        .setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP).list();
    for (Map<String, Object> tempMap : jidtempMap) {
      xhjMap.put(Long.valueOf(tempMap.get("JID").toString()), Long.valueOf(tempMap.get("CORE_FLAG").toString()));
    }
    return xhjMap;
  }
}
