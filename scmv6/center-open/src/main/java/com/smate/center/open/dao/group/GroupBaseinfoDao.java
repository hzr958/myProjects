package com.smate.center.open.dao.group;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.hibernate.transform.Transformers;
import org.springframework.stereotype.Repository;

import com.smate.center.open.model.group.GroupBaseInfo;
import com.smate.core.base.utils.data.SnsHibernateDao;

/**
 * 群组基础信息表DAO类.
 * 
 * @author mjg
 * 
 */
@Repository
public class GroupBaseinfoDao extends SnsHibernateDao<GroupBaseInfo, Long> {


  /**
   * 互联互通 群组信息
   * 
   * @param groupId
   * @return
   */
  public GroupBaseInfo findInterconnectionGroupBaseInfo(Long groupId) {
    String hql =
        "select  new GroupBaseInfo(t.groupId   , t.groupName , t.groupImgUrl  ,t.createDate) from GroupBaseInfo t where t.groupId=:groupId ";
    GroupBaseInfo groupBaseInfo = (GroupBaseInfo) this.createQuery(hql).setParameter("groupId", groupId).uniqueResult();
    return groupBaseInfo;
  }

  /**
   * 互联互通 我的项目群组
   * 
   * @param groupId
   * @return
   */
  public List<HashMap<String, Object>> findMyProjectGroup(Long psnId) {
    String resultSql = "select t.group_id,  t.group_name  ";
    StringBuffer sql = new StringBuffer();

    sql.append("from GROUP_BASEINFO t,GROUP_INVITE_PSN p ");
    sql.append("where t.group_id=p.group_id and t.group_category in (11) ");
    sql.append("and exists (select status from GROUP_FILTER f where f.group_id=t.group_id and f.status='01') ");
    sql.append("and p.psn_id=? and p.status='01' and p.is_accept=1  ");

    sql.append("and p.group_role in ('1') ");
    String orderSql = "order by t.last_visit_date desc nulls last,t.group_id desc ";

    String s = resultSql + sql + orderSql;
    Session session = this.getSession();
    List<Object> params = new ArrayList<Object>();
    params.add(psnId);
    Object[] objects = params.toArray();

    SQLQuery sqlQuery = (SQLQuery) session.createSQLQuery(s).setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);
    if (objects != null) {
      sqlQuery.setParameters(objects, this.findTypes(objects));
    }
    return sqlQuery.list();


  }

  /**
   * 获取群组基本信息.
   * 
   * @author lhd
   * @param groupId
   * @return
   */
  public GroupBaseInfo getGroupBaseInfo(Long groupId) {
    String hql = "from GroupBaseInfo t where t.groupId=? and rownum=1 ";
    Object obj = super.createQuery(hql, groupId).uniqueResult();
    if (obj != null) {
      return (GroupBaseInfo) obj;
    }
    return null;
  }

  /**
   * 获取群组序列ID.
   * 
   * @author lhd
   */
  public Long findNewGroupId() {
    BigDecimal groupId =
        (BigDecimal) super.getSession().createSQLQuery("select SEQ_GROUP_PSN.nextval from dual").uniqueResult();
    return groupId.longValue();
  }

  /**
   * 获取群组编号
   * 
   * @author lhd
   * @return
   */
  public Long creatGroupNo() {
    BigDecimal groupNo =
        (BigDecimal) super.getSession().createSQLQuery("select SEQ_GROUP_NO.nextval from dual").uniqueResult();
    return groupNo.longValue();
  }

  /**
   * 保存群组基本信息.
   * 
   * @author lhd
   * @param groupBaseInfo
   */
  public void saveBaseInfo(GroupBaseInfo groupBaseInfo) {
    if (groupBaseInfo != null) {
      if (groupBaseInfo.getLastVisitDate() == null) {
        groupBaseInfo.setLastVisitDate(groupBaseInfo.getCreateDate());
      }
      if (groupBaseInfo.getId() != null) {
        super.getSession().update(groupBaseInfo);
      } else {
        super.save(groupBaseInfo);
      }
    }
  }


}
