package com.smate.center.batch.dao.pdwh.psn;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.smate.center.batch.model.pdwh.psn.PsnPmEnName;
import com.smate.core.base.utils.data.PdwhHibernateDao;

/**
 * 成果匹配-用户英文姓名表数据库操作类.
 * 
 * @author mjg
 * 
 */
@Repository
public class PsnPmEnNameDao extends PdwhHibernateDao<PsnPmEnName, Long> {

  /**
   * 获取用户成果匹配英文名记录.
   * 
   * @param psnId
   * @return
   */
  public List<PsnPmEnName> getPsnPmEnNameList(Long psnId) {
    String hql = "from PsnPmEnName t where t.psnId=? ";
    return super.find(hql, psnId);
  }

  public void deleteAllByPsnId(Long psnId) {
    String hql = "delete from PsnPmEnName t where t.psnId=? ";
    super.createQuery(hql, new Object[] {psnId}).executeUpdate();
  }

  public PsnPmEnName findPsnPmEnName(Long psnId, String name, Integer type) {
    String hql = "from PsnPmEnName t where t.psnId=? and t.enName=? and t.type=?";
    return super.findUnique(hql, psnId, name, type);
  }

  public void deleteNotExists(Long psnId, List<String> names, Integer type) {
    String hql = "delete from PsnPmEnName t where t.psnId=:psnId and t.enName not in(:enName) and t.type=:type";
    super.createQuery(hql).setParameter("psnId", psnId).setParameter("type", type).setParameterList("enName", names)
        .executeUpdate();
  }
}
