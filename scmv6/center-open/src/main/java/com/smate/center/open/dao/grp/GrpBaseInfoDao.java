package com.smate.center.open.dao.grp;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.hibernate.transform.Transformers;
import org.springframework.stereotype.Repository;

import com.smate.center.open.model.grp.GrpBaseinfo;
import com.smate.core.base.utils.data.SnsHibernateDao;

/**
 * 群组基础信息dao
 * 
 * @author AiJiangBin
 *
 */

@Repository
public class GrpBaseInfoDao extends SnsHibernateDao<GrpBaseinfo, Long> {

  /**
   * 互联互通 我的项目群组
   * 
   * @param psnId
   * @return
   */
  public List<HashMap<String, Object>> findMyProjectGrp(Long psnId) {

    StringBuffer sql = new StringBuffer();
    /*
     * sql.append(" select   gb.grp_id,  gb.grp_name ,gb.grp_auatars ");
     * sql.append(" from    v_grp_member gm  , v_grp_baseinfo gb "); sql.
     * append(" where    gm.psn_id =?   and gm.status  = 01  and gm.grp_role = 1   and  gb.status = 01   and gb.grp_category  = 11 "
     * ); sql.
     * append("  and gm.grp_id = gb.grp_id order by gm.top_date desc nulls last , gm.last_visit_date desc nulls last "
     * );
     */
    sql.append("SELECT gb.grp_id, gb.grp_name, gi.grp_index_url, gb.grp_auatars ");
    sql.append("FROM v_grp_baseinfo gb ");
    sql.append("JOIN v_grp_index_url gi ON gb.grp_id = gi.grp_id ");
    sql.append("WHERE gb.owner_psn_id = ? AND gb.grp_category = 11 AND gb.status = 01 ");
    sql.append("order by gb.update_date desc nulls last, gb.create_date desc nulls last");
    Session session = this.getSession();
    List<Object> params = new ArrayList<Object>();
    params.add(psnId);
    Object[] objects = params.toArray();

    SQLQuery sqlQuery =
        (SQLQuery) session.createSQLQuery(sql.toString()).setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);
    if (objects != null) {
      sqlQuery.setParameters(objects, this.findTypes(objects));
    }
    return sqlQuery.list();

  }

  /**
   * 通过序列获取grpId
   * 
   * @return
   */
  public Long getGrpIdBySeq() {
    String sql = "select SEQ_V_GRP_BASEINFO.nextval from dual";
    Object object = this.getSession().createSQLQuery(sql).uniqueResult();
    if (object != null) {
      return Long.parseLong(object.toString());
    } else {
      return null;
    }
  }
}
