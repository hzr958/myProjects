package com.smate.center.batch.dao.pdwh.pub;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections4.CollectionUtils;
import org.springframework.stereotype.Repository;

import com.smate.center.batch.model.pdwh.pub.PdwhMemberInsNamePO;
import com.smate.core.base.utils.data.PdwhHibernateDao;

@Repository
public class PdwhMemberInsNameDAO extends PdwhHibernateDao<PdwhMemberInsNamePO, Long> {

  public void saveMemberInsData(Long id, String addr, Long insId) {
    String hql = "update PdwhMemberInsNamePO t set t.insName = :addr ,t.insId = :insId where t.id = :id";
    super.createQuery(hql).setParameter("addr", addr).setParameter("insId", insId).setParameter("id", id)
        .executeUpdate();
  }

  @SuppressWarnings("unchecked")
  public List<Map<String, Object>> getPubMemberInfoList(Long pubId) {
    String sql = "select f.id,f.member_Id,p.pdwh_pub_Id ,f.dept,p.name "
        + "from v_pub_pdwh_member_insname f left join v_pub_pdwh_member p on p.id=f.member_Id"
        + " where p.pdwh_pub_Id = :pdwhPubId and exists (select 1 from v_pub_pdwh pp where pp.status = 0 and pp.pub_Id = p.pdwh_pub_Id) "
        + " order by p.seq_no asc nulls last ";
    List<Object[]> objList = super.getSession().createSQLQuery(sql).setParameter("pdwhPubId", pubId).list();
    if (CollectionUtils.isEmpty(objList))
      return null;
    List<Map<String, Object>> listMap = new ArrayList<Map<String, Object>>();
    for (Object[] objects : objList) {
      Map<String, Object> map = new HashMap<String, Object>();
      map.put("id", Long.valueOf(String.valueOf(objects[0])));
      map.put("memberId", Long.valueOf(String.valueOf(objects[1])));
      map.put("pdwhPubId", String.valueOf(objects[2]));
      map.put("dept", String.valueOf(objects[3]));
      map.put("name", String.valueOf(objects[4]));
      listMap.add(map);
    }
    return listMap;
  }

  public List<Long> getMatchIns(Long memberId) {
    String hql =
        "select distinct(p.insId) from PdwhMemberInsNamePO p  where p.memberId = :memberId  and p.insId is not null ";
    return super.createQuery(hql).setParameter("memberId", memberId).list();
  }
}
