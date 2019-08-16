package com.smate.center.batch.dao.pdwh.psn;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.smate.center.batch.model.pdwh.psn.PsnPmCoZhName;
import com.smate.core.base.utils.data.PdwhHibernateDao;

/**
 * 成果匹配-用户合作者中文姓名表数据库操作类.
 * 
 * @author mjg
 * 
 */
@Repository
public class PsnPmCoZhNameDao extends PdwhHibernateDao<PsnPmCoZhName, Long> {

  /**
   * 获取用户的合作者中文名列表.
   * 
   * @param psnId
   * @return
   */
  public List<PsnPmCoZhName> getCoZhNameList(Long psnId) {
    String hql = "from PsnPmCoZhName t where t.psnId=? ";
    return super.find(hql, psnId);
  }

  public PsnPmCoZhName findPsnPmCoZhName(Long psnId, String name) {
    String hql = "from PsnPmCoZhName t where t.psnId=? and t.coName=?";
    return super.findUnique(hql, psnId, name);
  }

  public void deleteNotExists(Long psnId, List<String> names) {
    String hql = "delete from PsnPmCoZhName t where t.psnId=:psnId and t.coName not in(:coName)";
    super.createQuery(hql).setParameter("psnId", psnId).setParameterList("coName", names).executeUpdate();
  }

  public void deleteAllByPsnId(Long psnId) {
    String hql = "delete from PsnPmCoZhName t where t.psnId=:psnId";
    super.createQuery(hql).setParameter("psnId", psnId).executeUpdate();
  }
}
