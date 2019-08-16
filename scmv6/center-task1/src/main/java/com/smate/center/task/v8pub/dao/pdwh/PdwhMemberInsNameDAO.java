package com.smate.center.task.v8pub.dao.pdwh;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections4.CollectionUtils;
import org.springframework.stereotype.Repository;

import com.smate.center.task.v8pub.pdwh.po.PdwhMemberInsNamePO;
import com.smate.core.base.utils.data.PdwhHibernateDao;

@Repository
public class PdwhMemberInsNameDAO extends PdwhHibernateDao<PdwhMemberInsNamePO, Long> {

  /**
   * 删除记录
   * 
   * @param pdwhPubId
   */
  public void deleteAll(Long pdwhPubId) {
    String hql =
        "delete from PdwhMemberInsNamePO t where t.memberId in(select p.id from PdwhPubMemberPO p where p.pdwhPubId =:pdwhPubId)";
    this.createQuery(hql).setParameter("pdwhPubId", pdwhPubId).executeUpdate();
  }

  public PdwhMemberInsNamePO findMemberByMemberId(Long memberId) {
    String hql = "from PdwhMemberInsNamePO t where t.memberId =:memberId " + " order by t.id";
    List<PdwhMemberInsNamePO> list = this.createQuery(hql).setParameter("memberId", memberId).list();
    if (!list.isEmpty() && list.size() > 0) {
      return list.get(0);
    }
    return null;
  }

  /**
   * 根据pdwh的memberId获取单位信息列表
   * 
   * @param memberId
   * @return
   */
  @SuppressWarnings("unchecked")
  public List<PdwhMemberInsNamePO> findListByMemberId(Long memberId) {
    String hql = "from PdwhMemberInsNamePO t where t.memberId =:memberId and t.dept is not null " + " order by t.id";
    List<PdwhMemberInsNamePO> list = this.createQuery(hql).setParameter("memberId", memberId).list();
    if (!list.isEmpty() && list.size() > 0) {
      return list;
    }
    return null;
  }

  @SuppressWarnings("unchecked")
  public List<Map<String, Object>> getPubMemberInsIdList(Long pubId, String name) {
    String sql =
        "select  p.id,f.ins_id,f.ins_name from v_pub_pdwh_member p left join v_pub_pdwh_member_insname f on p.id=f.member_Id"
            + " where p.pdwh_pub_Id = :pdwhPubId and lower(p.name) = :name and exists (select 1 from v_pub_pdwh pp where pp.status = 0 and pp.pub_Id = p.pdwh_pub_Id)";
    List<Object[]> objList =
        super.getSession().createSQLQuery(sql).setParameter("pdwhPubId", pubId).setParameter("name", name).list();
    if (CollectionUtils.isEmpty(objList))
      return null;
    List<Map<String, Object>> insInfoList = new ArrayList<Map<String, Object>>();
    for (Object[] objects : objList) {
      Map<String, Object> map = new HashMap<String, Object>();
      map.put("memberId", Long.valueOf(String.valueOf(objects[0])));
      if (objects[1] != null) {
        map.put("insId", String.valueOf(objects[1]));
      } else {
        map.put("insId", "");
      }
      if (objects[2] != null) {
        map.put("insName", Long.valueOf(String.valueOf(objects[1])));
      } else {
        map.put("insName", "");
      }
      insInfoList.add(map);
    }
    return insInfoList;
  }
}
