package com.smate.web.psn.dao.autocomplete;

import java.util.List;

import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.hibernate.transform.Transformers;
import org.springframework.stereotype.Repository;

import com.smate.web.psn.model.autocomplete.AcDisciplineKey;

/**
 * 自己填写的关键词提示相关学科领域.
 * 
 * @author cwli
 * 
 */
@Repository
public class AcMyDisciplineKeyDao extends AutoCompleteDao<AcDisciplineKey> {

  @SuppressWarnings("unchecked")
  public List getAcMyDisKeyAutoDis(Long psnId, String startStr, int size) {
    String sql =
        "select ct.id,ct.zh_name,ct.en_name,ct.disc_code from psn_discipline t,psn_discipline_key t2,const_discipline ct where t.dis_id=ct.id and t.psndis_id=t2.psndis_id and t.psn_id=? and t2.key_words like ?";
    Object[] objects = new Object[] {psnId, "%" + startStr + "%"};
    Session session = this.getSession();
    SQLQuery sqlQuery = (SQLQuery) session.createSQLQuery(sql).setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);
    if (objects != null) {
      sqlQuery.setParameters(objects, super.findTypes(objects));
    }
    sqlQuery.setMaxResults(size);
    return sqlQuery.list();
  }
}
