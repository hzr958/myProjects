package com.smate.center.batch.dao.pdwh.psn;

import java.util.Collection;
import java.util.List;

import org.hibernate.Query;
import org.springframework.stereotype.Repository;

import com.smate.center.batch.model.pdwh.psn.PsnPmCoEnName;
import com.smate.core.base.utils.data.PdwhHibernateDao;
import com.smate.core.base.utils.string.ServiceUtil;


/**
 * 成果匹配-用户合作者英文姓名表数据库操作类.
 * 
 * @author mjg
 * 
 */
@Repository
public class PsnPmCoEnNameDao extends PdwhHibernateDao<PsnPmCoEnName, Long> {

  /**
   * 获取用户的合作者英文名列表.
   * 
   * @param psnId
   * @return
   */
  public List<PsnPmCoEnName> getCoEnNameList(Long psnId) {
    String hql = "from PsnPmCoEnName t where t.psnId=? ";
    return super.find(hql, psnId);
  }

  public PsnPmCoEnName findPsnPmCoEnName(Long psnId, String name) {
    String hql = "from PsnPmCoEnName t where t.psnId=? and t.coName=?";
    return super.findUnique(hql, psnId, name);
  }

  public void deleteAllByPsnId(Long psnId) {
    String hql = "delete from PsnPmCoEnName t where t.psnId=:psnId";
    super.createQuery(hql).setParameter("psnId", psnId).executeUpdate();
  }

  public void deleteNotExists(Long psnId, List<String> names) {
    Collection<Collection<String>> container = ServiceUtil.splitStrList(names, 80);

    StringBuilder hql = new StringBuilder("delete from PsnPmCoEnName t where t.psnId=:psnId ");
    for (int i = 0; i < container.size(); i++) {
      hql.append(" and t.coName not in(:coName").append(i).append(")");
    }

    Query query = super.createQuery(hql.toString()).setParameter("psnId", psnId);

    int j = 0;
    for (Collection<String> c : container) {
      query.setParameterList("coName" + j, c);
      j++;
    }
    query.executeUpdate();
  }
}
