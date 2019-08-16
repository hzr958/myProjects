package com.smate.center.task.dao.group;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.smate.center.task.model.grp.GrpPubs;
import com.smate.core.base.utils.data.SnsHibernateDao;

/**
 * 
 * @author zjh 群组成果
 *
 */
@Repository
public class GrpPubsDao extends SnsHibernateDao<GrpPubs, Long> {

  public Long getCountGroupPub(Long groupId) {
    String hql = "select count(t.pubId) from GrpPubs t where t.grpId = :groupId";
    return (Long) super.createQuery(hql).setParameter("groupId", groupId).uniqueResult();

  }

  @SuppressWarnings("unchecked")
  public List<Long> getGroupPubIds(Long grpId, int size) {
    String hql = "select t.pubId from GrpPubs t where t.grpId = :groupId";
    return super.createQuery(hql).setParameter("groupId", grpId).setMaxResults(size).list();
  }

  @SuppressWarnings("unchecked")
  public List<Long> findGroupPubIds(Long grpId) {
    String hql = "select t.pubId from GrpPubs t where t.grpId = :groupId and t.status=0 ";
    return super.createQuery(hql).setParameter("groupId", grpId).list();
  }

  /**
   * 根据pubId获取GrpPubs
   * 
   * @param pubId
   * @return
   */
  @SuppressWarnings("unchecked")
  public List<GrpPubs> findGrpPubs(Long pubId) {
    String sql = "from GrpPubs t where t.pubId=:pubId";
    return super.createQuery(sql).setParameter("pubId", pubId).list();
  }

  /**
   * 获取今天增加成果的群组ID
   * 
   * @return
   */
  public List<Long> getUploadPubGrpId() {

    String hql =
        "select     distinct  t.grpId  from GrpPubs t where t.createDate>trunc(sysdate) and t.createDate<trunc(sysdate+1)  and t.status = 0 "
            + "  and   not exists (select   e.groupId from  EmailGroupPubPsn  e where e.createDate >  trunc(sysdate) and e.createDate<trunc(sysdate+1)  and t.grpId = e.groupId   ) ";

    return super.createQuery(hql).list();
  };

  /**
   * 获取该群组，当天上传的第一个群组成果
   * 
   * @param grpId
   * @return
   */
  public GrpPubs getTodayUplaodGrpPubsByGrpId(Long grpId) {
    String hql =
        " from GrpPubs t where    t.grpId =:grpId and  t.createDate>trunc(sysdate) and t.createDate<trunc(sysdate+1)   and t.status = 0  order by  t.createDate asc ";
    List list = this.createQuery(hql).setParameter("grpId", grpId).list();
    if (list != null && list.size() > 0) {
      return (GrpPubs) list.get(0);
    }
    return null;
  }

  @SuppressWarnings("unchecked")
  public Integer getStatus(Long pubId) {
    String hql = "select t.status from GrpPubs t where t.pubId =:pubId and t.status = 0";
    List<Integer> staList = super.createQuery(hql).setParameter("pubId", pubId).list();
    if (staList != null && staList.size() > 0) {
      return staList.get(0);
    }
    return null;
  }
}
