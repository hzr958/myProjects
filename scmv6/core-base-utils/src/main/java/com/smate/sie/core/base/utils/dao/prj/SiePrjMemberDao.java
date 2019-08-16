package com.smate.sie.core.base.utils.dao.prj;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.smate.core.base.utils.data.SieHibernateDao;
import com.smate.sie.core.base.utils.model.prj.SiePrjMember;

/**
 * 项目成员 Dao
 * 
 * @author hd
 *
 */
@Repository
public class SiePrjMemberDao extends SieHibernateDao<SiePrjMember, Long> {

  /**
   * 人员成果数统计
   * 
   * @param insId
   * @return
   */
  public Long getConfirmPrjTotalNumByPsnId(Long psnId) {
    String hql =
        "select count(t.id) from SiePrjMember t where t.psnId = ? and exists (select 1 from SieProject d where d.id = t.prjId and d.status <> '1')";
    return super.findUnique(hql, psnId);
  }

  @SuppressWarnings("unchecked")
  public List<SiePrjMember> getListByInsId(Long insId) {
    String hql = " from SiePrjMember t where t.insId= ? order by id ";
    return super.createQuery(hql, insId).list();
  }

  @SuppressWarnings("unchecked")
  public List<SiePrjMember> getMembersByPrjId(long prjId) {
    return super.createQuery("from SiePrjMember t where t.prjId= ? order by t.seqNo ", prjId).list();
  }

  @SuppressWarnings("unchecked")
  public List<SiePrjMember> getMainMemberByPrjId(long prjId) {
    return super.createQuery("from SiePrjMember t where t.prjId = ? and t.notifyAuthor = 1 order by t.seqNo ", prjId)
        .list();
  }
}
