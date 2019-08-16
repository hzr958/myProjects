package com.smate.web.psn.dao.resume;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.smate.core.base.utils.data.SnsHibernateDao;
import com.smate.web.psn.model.resume.ResumePubSimple;

/**
 * 个人成果表dao
 * 
 * @author lhd
 *
 */
@Repository
public class ResumePubSimpleDao extends SnsHibernateDao<ResumePubSimple, Long> {
  /**
   * 获取当前人所有成果
   * 
   * @param pubId
   * @return
   */
  @SuppressWarnings("unchecked")
  public List<ResumePubSimple> getPubs(Long psnId) {
    String hql = "from ResumePubSimple t where t.ownerPsnId=:psnId"
        + " and t.articleType=1 and t.status = 0 and t.simpleStatus in(0,1,99)"
        + " order by t.createDate desc,t.pubId desc";
    return super.createQuery(hql).setParameter("psnId", psnId).list();
  }

  /**
   * 查找成果拥有者(排除自己)
   * 
   * @param pubIds
   * @return
   */
  @SuppressWarnings("unchecked")
  public List<Long> getPubOwnerPsnIds(List<Long> pubIds, Long psnId) {
    String hql =
        "select distinct t.ownerPsnId from ResumePubSimple t where t.pubId in(:pubIds) and t.ownerPsnId != :psnId";
    return super.createQuery(hql).setParameterList("pubIds", pubIds).setParameter("psnId", psnId).list();
  }
}
