package com.smate.web.dyn.dao.dynamic.group;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.smate.core.base.utils.common.CommonUtils;
import com.smate.core.base.utils.data.SnsHibernateDao;
import com.smate.web.dyn.form.dynamic.group.GroupDynShowForm;
import com.smate.web.dyn.model.dynamic.group.GroupDynamicMsg;

/**
 * 群组动态 内容 信息 dao
 * 
 * @author tsz
 *
 */
@Repository
public class GroupDynamicMsgDao extends SnsHibernateDao<GroupDynamicMsg, Long> {

  public boolean isAdmin(Long psnId, Long grpId) {
    String sql =
        "select 1 from v_grp_member t where t.psn_id=:psnId and t.grp_id=:grpId and (t.grp_role='1' or t.grp_role='2') ";
    List list = this.getSession().createSQLQuery(sql).setParameter("psnId", psnId).setParameter("grpId", grpId).list();
    if (list != null && list.size() > 0) {
      return true;
    } else {
      return false;
    }
  }

  /**
   * 获取群组动态列表
   * 
   * @param from
   * @return
   * 
   * 
   */
  @SuppressWarnings("unchecked")
  public List<GroupDynamicMsg> getGroupDyn(GroupDynShowForm form) {
    String hql = "from GroupDynamicMsg g where g.status=0 and g.groupId=:groupId ";
    String orderBy = " order by g.createDate desc ";
    String countHql = "select count(*) ";
    if (form.getShowNew() == 1) {
      hql +=
          " and g.dynType in('GRP_SHAREPDWHPUB','GRP_SHARE','GRP_SHAREPUB','GRP_SHAREFILE','GRP_ADDPUB','GRP_ADDFILE','GRP_PUBLISHDYN'  ,'GRP_ADDWORK' ,'GRP_ADDCOURSE', 'GRP_SHAREFUND','GRP_SHAREPRJ', 'GRP_SHAREAGENCY', 'GRP_SHARENEWS') ";
    }
    Long count =
        (Long) super.createQuery(countHql + hql.toString()).setParameter("groupId", form.getGroupId()).uniqueResult();
    form.getPage().setTotalCount(count);
    if (count.equals(0L)) {
      return null;
    }
    if (form.getMaxResults() != null && form.getMaxResults() != 0) {
      return super.createQuery(hql.toString() + orderBy).setParameter("groupId", form.getGroupId())
          .setFirstResult(form.getFirstResult()).setMaxResults(form.getMaxResults()).list();
    } else {
      return super.createQuery(hql.toString() + orderBy).setParameter("groupId", form.getGroupId())
          .setFirstResult(form.getFirstResult()).list();
    }
  }

  /**
   * 创建动态主键Id
   * 
   * @return Long
   */
  public Long createDynId() {
    String sql = "select SEQ_V_GROUP_DYNAMIC_MSG.nextval from dual";
    return super.queryForLong(sql);
  }

  public void delGrpDyn(Long dynId, Long grpId, Long userId) {
    String hql =
        "update GroupDynamicMsg t set t.status=99 where  t.groupId=:grpId and t.producer=:psnId and t.dynId=:dynId";
    this.createQuery(hql).setParameter("dynId", dynId).setParameter("grpId", grpId).setParameter("psnId", userId)
        .executeUpdate();
  }


  /**
   * 获取群组动态列表
   * 
   * @param from
   * @return
   * 
   * 
   */
  @SuppressWarnings("unchecked")
  public GroupDynamicMsg getGroupDynDetails(Long grpId, Long dynId, Integer showNew) {
    String hql = "from GroupDynamicMsg g where g.status=0 and g.groupId=:groupId and g.dynId=:dynId";
    if (CommonUtils.compareIntegerValue(showNew, 1)) {
      hql +=
          " and g.dynType in('GRP_SHAREPDWHPUB','GRP_SHARE','GRP_SHAREPUB','GRP_SHAREFILE','GRP_ADDPUB','GRP_ADDFILE','GRP_PUBLISHDYN'  ,'GRP_ADDWORK' ,'GRP_ADDCOURSE', 'GRP_SHAREFUND','GRP_SHAREPRJ', 'GRP_SHAREAGENCY') ";
    }
    return (GroupDynamicMsg) super.createQuery(hql.toString()).setParameter("groupId", grpId)
        .setParameter("dynId", dynId).uniqueResult();
  }

}
