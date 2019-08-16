package com.smate.sie.core.base.utils.dao.ins;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.smate.core.base.utils.data.SieHibernateDao;
import com.smate.sie.core.base.utils.model.ins.SieInsAlias;

/**
 * 检索式表
 * 
 * @author 叶星源
 * 
 */
@Repository
public class SieInsAliasDao extends SieHibernateDao<SieInsAlias, Long> {

  @SuppressWarnings("unchecked")
  public List<SieInsAlias> getListByInsId(Long insId) {
    String hql = " from SieInsAlias t where t.insAliasId.insId= ? order by t.insAliasId.insId ";
    return super.createQuery(hql, insId).list();
  }

  public void deleteByInsId(Long mergeid) {
    String sql = "delete from INS_ALIAS t where t.ins_id= :insId";
    super.getSession().createSQLQuery(sql).setParameter("insId", mergeid).executeUpdate();
  }

}
