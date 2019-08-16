package com.smate.core.base.psn.dao.psncnf;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.smate.core.base.psn.model.psncnf.PsnConfig;
import com.smate.core.base.utils.data.SnsHibernateDao;

/**
 * 个人配置：主表.
 * 
 * @author zhuangyanming
 * 
 */
@Repository
public class PsnConfigDao extends SnsHibernateDao<PsnConfig, Long> {

  public PsnConfig getByPsn(Long psnId) {
    return super.findUnique("from PsnConfig t where t.psnId = ?", new Object[] {psnId});
  }

  @SuppressWarnings("unchecked")
  public List<PsnConfig> getList() {
    return super.createQuery(" from PsnConfig where status=0 order by cnfId").setMaxResults(500).list();
  }

  public Long getPsnConfId(Long psnId) {
    return (Long) super.createQuery("select t.cnfId from PsnConfig t where t.psnId = :psnId")
        .setParameter("psnId", psnId).uniqueResult();
  }

  /**
   * 查询人员公开成果数
   * 
   * @param psnId
   * @return
   */
  public Long findOpenPubSum(Long psnId) {
    String sql =
        "select count(*) from v_pub_simple t, (select pcp.pub_id from Psn_Config pc,Psn_Config_Pub pcp where pc.cnf_Id=pcp.cnf_Id and pc.status=1 and pcp.any_User=7 and pc.psn_Id=? order by pcp.pub_id desc) p where t.pub_id = p.pub_id  and t.status = 0 and t.simple_status in (0, 1) and t.article_type=1 and t.owner_psn_id = ?";
    return super.queryForLong(sql, new Object[] {psnId, psnId});
  }

  /**
   * 查询人员所有成果数
   * 
   * @param psnId
   * @return
   */
  public Long findTotalPubSum(Long psnId) {
    String sql =
        "select count(*) from v_pub_simple t, (select pcp.pub_id from Psn_Config pc,Psn_Config_Pub pcp where pc.cnf_Id=pcp.cnf_Id and pc.status=1  and pc.psn_Id=? order by pcp.pub_id desc) p where t.pub_id = p.pub_id  and t.status = 0 and t.simple_status in (0, 1, 99) and t.article_type=1 and t.owner_psn_id = ?";
    return super.queryForLong(sql, new Object[] {psnId, psnId});
  }

}
