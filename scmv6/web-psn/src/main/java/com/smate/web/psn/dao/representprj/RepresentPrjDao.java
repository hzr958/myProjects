package com.smate.web.psn.dao.representprj;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.smate.core.base.utils.data.SnsHibernateDao;
import com.smate.web.psn.model.representprj.RepresentPrj;
import com.smate.web.psn.model.representprj.RepresentPrjPk;

/**
 * 人员代表性项目DAO
 *
 * @author wsn
 * @createTime 2017年3月14日 下午8:25:55
 *
 */
@Repository
public class RepresentPrjDao extends SnsHibernateDao<RepresentPrj, RepresentPrjPk> {

  /**
   * 查询人员代表性项目
   * 
   * @param psnId
   * @param status
   * @return
   */
  public List<RepresentPrj> findPsnRepresentPubList(Long psnId, Integer status) {
    String hql = "from RepresentPrj t where t.representPrjPk.psnId = :psnId and t.status = :status";
    return super.createQuery(hql).setParameter("psnId", psnId).setParameter("status", status).list();
  }

  /**
   * 获取人员代表性项目ID
   * 
   * @param psnId
   * @param status
   * @return
   */
  public List<Long> findPsnRepresentPrjIds(Long psnId, Integer status, Long cnfId, Integer anyUser) {
    String hql =
        "select t.representPrjPk.prjId from RepresentPrj t where t.representPrjPk.psnId = :psnId and t.status = :status and exists (select 1 from PsnConfigPrj pcp where pcp.id.cnfId = :cnfId and pcp.anyUser > :anyUser and pcp.id.prjId = t.representPrjPk.prjId) order by t.seqNo asc";
    return super.createQuery(hql).setParameter("psnId", psnId).setParameter("status", status)
        .setParameter("cnfId", cnfId).setParameter("anyUser", anyUser).list();
  }

  /**
   * 查找人员代表性想
   * 
   * @param psnId
   * @param pubId
   * @return
   */
  public RepresentPrj findPsnRepresentPrj(Long psnId, Long prjId) {
    String hql = " from RepresentPrj t where t.representPrjPk.psnId = :psnId and t.representPrjPk.prjId = :prjId";
    return (RepresentPrj) super.createQuery(hql).setParameter("psnId", psnId).setParameter("prjId", prjId)
        .uniqueResult();
  }

  /**
   * 更新代表性项目状态
   * 
   * @param psnId
   * @param status
   */
  public void updatePsnRepresentPrjStatus(Long psnId, Integer status) {
    String hql = "update RepresentPrj t set t.status = :status where t.representPrjPk.psnId = :psnId";
    super.createQuery(hql).setParameter("status", status).setParameter("psnId", psnId).executeUpdate();
  }
}
