package com.smate.web.v8pub.dao.sns.group;

import com.smate.core.base.utils.data.SnsHibernateDao;
import com.smate.web.v8pub.po.sns.group.GrpPubRcmd;
import org.hibernate.Query;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

/**
 * 群组成果推荐实体
 * 
 * @author tsz
 *
 */
@Repository
public class GrpPubRcmdDao extends SnsHibernateDao<GrpPubRcmd, Long> {


  /**
   * 查询所有的 ，open 使用， 不要添加状态
   * @param grpId
   * @param pageNo
   * @param pageSize
   * @param lastGetPubDate
   * @return
   */
  public List<GrpPubRcmd> getAllGrpPubRcmd(Long grpId, int pageNo, int pageSize, Date lastGetPubDate) {
    String hql = "from GrpPubRcmd t where t.grpId=:grpId  ";
    if (lastGetPubDate != null) {
      hql += " and  ( t.createDate >=:lastGetPubDate or t.updateDate >=:lastGetPubDate )";
    }
    String order = " order by t.createDate asc , t.id asc";
    Query query = super.createQuery(hql + order).setParameter("grpId", grpId).setFirstResult((pageNo - 1) * pageSize)
        .setMaxResults(pageSize);;
    if (lastGetPubDate != null) {
      query.setParameter("lastGetPubDate", lastGetPubDate);
    }
    Object obj = query.list();
    if (obj != null) {
      return (List<GrpPubRcmd>) obj;
    }
    return null;
  }

  /**
   * 查询所有的  open 接口使用， 不要添加状态
   * @param grpId
   * @param lastGetPubDate
   * @return
   */
  public Long getAllGrpPubRcmdCount(Long grpId, Date lastGetPubDate) {
    String hql = "select count(1) from GrpPubRcmd t where t.grpId=:grpId";
    if (lastGetPubDate != null) {
      hql += " and  ( t.createDate >=:lastGetPubDate or t.updateDate >=:lastGetPubDate)";
    }
    Query query = super.createQuery(hql).setParameter("grpId", grpId);

    if (lastGetPubDate != null) {
      query.setParameter("lastGetPubDate", lastGetPubDate);
    }
    Object obj = query.uniqueResult();
    if (obj != null) {
      return (Long) obj;
    }
    return null;
  }

  /**
   * 根据群组id 成果id查询 成果推荐记录
   * 
   * @param pubId
   * @param grpId
   * @return
   * @throws Exception
   */
  public GrpPubRcmd getGrpPubRcmd(Long pubId, Long grpId) throws Exception {
    String hql = "from GrpPubRcmd t where t.grpId=:grpId and t.pubId=:pubId and t.status=0";
    Object obj = super.createQuery(hql).setMaxResults(1).setParameter("grpId", grpId).setParameter("pubId", pubId)
        .uniqueResult();
    if (obj != null) {
      return (GrpPubRcmd) obj;
    }
    return null;
  }

  /**
   * 删除群组成果推荐记录 status=8 基准库成果已删除
   * 
   * @param pubId
   */
  public void deleteByPubId(Long pubId) {
    String hql = "update GrpPubRcmd t set t.status = 8,t.updateDate=:updateDate where t.pubId= :pubId";
    super.createQuery(hql).setParameter("pubId", pubId).setParameter("updateDate", new Date()).executeUpdate();
  }
}
