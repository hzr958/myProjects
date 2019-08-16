package com.smate.center.task.dao.sns.quartz;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections4.CollectionUtils;
import org.springframework.stereotype.Repository;

import com.smate.center.task.model.grp.GrpBaseinfo;
import com.smate.core.base.utils.data.SnsHibernateDao;

@Repository
public class GrpBaseInfoDao extends SnsHibernateDao<GrpBaseinfo, Long> {

  @SuppressWarnings("unchecked")
  /**
   * 分页获取群组Id
   * 
   * @param index
   * @param batchSize
   * @return
   */
  public List<Long> getNeedInitGrpId(Integer index, Integer batchSize) {
    String hql = "select t.grpId from GrpBaseinfo t";
    return this.createQuery(hql).setFirstResult(batchSize * (index - 1)).setMaxResults(batchSize).list();

  }

  // 在GroupBaseInfo对应表中查找groupname
  @SuppressWarnings("unchecked")
  public List<GrpBaseinfo> findGroup(Long groupId) {
    String hql = "select new GrpBaseinfo(grpId,grpName,grpNo) from GrpBaseinfo g where g.grpId=:groupId ";
    return super.createQuery(hql).setParameter("groupId", groupId).list();
  }

  public Integer findGrpCatacory(Long grpId) {
    String hql = "select   g.grpCategory  from GrpBaseinfo g where g.grpId=:grpId ";
    return (Integer) super.createQuery(hql).setParameter("grpId", grpId).uniqueResult();
  }

  @SuppressWarnings("unchecked")
  public List<GrpBaseinfo> getNeedSendMailJzData(Long grpId, Integer size) {
    String hql =
        "select new GrpBaseinfo(grpId,grpName,ownerPsnId) from GrpBaseinfo g where Exists (Select 1 From JzbgsjModified f where g.projectNo=f.fundInfo and f.scmPsnId=g.createPsnId) "
            + "and to_char(g.createDate,'yyyyMMdd')>='20181029' And g.status=01 and g.grpCategory=11 and g.grpId > :grpId order by g.grpId";
    return super.createQuery(hql).setParameter("grpId", grpId).setMaxResults(size).list();
  }

  @SuppressWarnings("unchecked")
  public List<GrpBaseinfo> getNeedSendMailJtData(Long grpId, Integer size) {
    String hql =
        "select new GrpBaseinfo(grpId,grpName,ownerPsnId) from GrpBaseinfo g where Exists (Select 1 From JtbgsjModified f where g.projectNo=f.fundInfo and f.scmPsnId=g.createPsnId) "
            + "and to_char(g.createDate,'yyyyMMdd')>='20181025' And g.status=01 and g.grpCategory=11 and g.grpId > :grpId order by g.grpId";
    return super.createQuery(hql).setParameter("grpId", grpId).setMaxResults(size).list();
  }

  @SuppressWarnings("unchecked")
  public List<GrpBaseinfo> getInstrestGroup() {
    String hql =
        "select new GrpBaseinfo(grpId,grpName,ownerPsnId) from GrpBaseinfo g where g.status='01' and g.grpCategory=12 ";
    return super.createQuery(hql).list();
  }

  @SuppressWarnings("unchecked")
  public List<Map<String, String>> getInstrestGrpInfo() {
    String sql =
        "select t.grp_id,f.first_category_id,f.second_category_id,f.nsfc_category_id,f.keywords  from v_grp_baseinfo  t inner join v_grp_kw_disc f on t.grp_id=f.grp_id where t.grp_category=12 and t.status='01' order by t.grp_id";
    List<Object[]> objList = super.getSession().createSQLQuery(sql).list();
    if (CollectionUtils.isEmpty(objList))
      return null;
    List<Map<String, String>> listMap = new ArrayList<Map<String, String>>();
    for (Object[] objects : objList) {
      Map<String, String> map = new HashMap<String, String>();
      map.put("grpId", String.valueOf(objects[0]));
      if (objects[1] != null) {
        map.put("firstCatId", String.valueOf(objects[1]));
      }
      if (objects[2] != null) {
        map.put("secondCatId", String.valueOf(objects[2]));
      }
      if (objects[3] != null) {
        map.put("nsfcCatId", String.valueOf(objects[3]));
      }
      if (objects[4] != null) {
        map.put("keywords", String.valueOf(objects[4]));
      }
      listMap.add(map);
    }
    return listMap;
  }

  @SuppressWarnings("unchecked")
  public List<GrpBaseinfo> getNeedRcmdGroup(Long lastGrpId, Integer size) {
    String hql =
        " select new GrpBaseinfo(grpId,grpCategory) from GrpBaseinfo  g where g.grpId > :lastGrpId and g.status='01' order by g.grpId ";
    return super.createQuery(hql).setParameter("lastGrpId", lastGrpId).setMaxResults(size).list();
  }

  public GrpBaseinfo getGrpNameAndAuatars(Long grpId) {
    String hql = " select new GrpBaseinfo(grpName,grpAuatars) from GrpBaseinfo where grpId = :grpId and status='01'";
    return (GrpBaseinfo) super.createQuery(hql).setParameter("grpId", grpId).uniqueResult();
  }


}
