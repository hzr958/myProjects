package com.smate.center.open.dao.grp;

import java.util.List;

import org.hibernate.Query;
import org.springframework.stereotype.Repository;

import com.smate.center.open.model.grp.GrpMember;
import com.smate.core.base.utils.data.SnsHibernateDao;

/**
 * 群组成员关系Dao类
 * 
 * @author zzx
 *
 */
@Repository
public class GrpMemberDao extends SnsHibernateDao<GrpMember, Long> {
  /**
   * 查询群组 管理员 -or isAccept is null )
   * 
   * @param grpId
   * @return
   * @throws Exception
   */
  @SuppressWarnings("unchecked")
  public List<GrpMember> findFiveGrpMemberByGrpId(Long grpId) {
    String hql =
        "from GrpMember where grpId=:grpId and status=01     and  grpRole in ('1' ,'2') order by grpRole asc ,createDate asc";
    Query query = createQuery(hql).setParameter("grpId", grpId).setMaxResults(5);
    return query.list();
  }

}
