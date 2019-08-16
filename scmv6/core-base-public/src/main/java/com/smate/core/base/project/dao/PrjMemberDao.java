package com.smate.core.base.project.dao;

import com.smate.core.base.project.model.PrjMember;
import com.smate.core.base.utils.data.SnsHibernateDao;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * 项目成员DAO
 * 
 * @author houchuanjie
 * @date 2018年3月22日 下午5:53:36
 */
@Repository
public class PrjMemberDao extends SnsHibernateDao<PrjMember, Long> {
  /**
   * 删除项目成员
   *
   * @author houchuanjie
   * @date 2018年3月22日 下午5:56:56
   * @param pmId
   * @return 返回删除的成员实体，如果没有对应成员，则返回null
   */
  public PrjMember removeById(Long pmId) {
    PrjMember prjMember = get(pmId);
    Optional.ofNullable(prjMember).ifPresent(pm -> delete(pm));
    return prjMember;
  }

  public PrjMember findOneByPrjId(Long prjId) {
    String hql = "from PrjMember t where t.prjId=:prjId";
    List<PrjMember> list = this.createQuery(hql).setParameter("prjId", prjId).list();
    if (list != null && list.size() > 0) {
      return list.get(0);
    }
    return null;
  }
  public PrjMember findOneByPrjId(Long prjId , int seqNo) {
    String hql = "from PrjMember t where t.prjId=:prjId and t.seqNo =:seqNo";
    List<PrjMember> list = this.createQuery(hql).setParameter("prjId", prjId).setParameter("seqNo",seqNo).list();
    if (list != null && list.size() > 0) {
      return list.get(0);
    }
    return null;
  }
}
