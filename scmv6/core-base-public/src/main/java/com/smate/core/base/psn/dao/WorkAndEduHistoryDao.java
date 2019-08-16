package com.smate.core.base.psn.dao;

import java.util.List;
import java.util.Map;

import org.hibernate.transform.Transformers;
import org.springframework.stereotype.Repository;

import com.smate.core.base.psn.model.WorkHistory;
import com.smate.core.base.utils.data.SnsHibernateDao;

@Repository
public class WorkAndEduHistoryDao extends SnsHibernateDao<WorkHistory, Long> {

  @SuppressWarnings("unchecked")
  public List<Map<String, Object>> findWorkAndEduHistoryByPsnId(Long psnId) {
    String sql = "select tt.ins_name, tt.from_year,tt.to_year,tt.to_month, tt.is_primary, tt.is_active from ( "
        + " select w.work_id,w.psn_id, w.ins_name,w.from_year,w.to_year,w.to_month, w.is_primary, w.is_active from psn_work_history w where w.psn_id=:psnId"
        + " union select e.edu_id,e.psn_id,e.ins_name,e.from_year,e.to_year,e.to_month, e.is_primary, e.is_active from psn_edu_history e where e.psn_id=:psnId ) tt "
        + " order by tt.to_year desc,tt.to_month desc ,nlssort(tt.ins_name,'NLS_SORT = SCHINESE_PINYIN_M')";
    return super.getSession().createSQLQuery(sql).setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP)
        .setParameter("psnId", psnId).list();
  }
}
