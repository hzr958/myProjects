package com.smate.sie.core.base.utils.dao.ins;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.smate.core.base.utils.data.SieHibernateDao;
import com.smate.sie.core.base.utils.model.ins.SieInsAlias;
import com.smate.sie.core.base.utils.model.ins.SieMergeInsAliasBak;

/**
 * 检索式表
 * 
 * @author 叶星源
 * 
 */
@Repository
public class SieMergeInsAliasBakDao extends SieHibernateDao<SieMergeInsAliasBak, Long> {

  @SuppressWarnings("unchecked")
  public List<SieInsAlias> getListByInsId(Long insId) {
    String hql = " from SieMergeInsAliasBak t where t.insAliasId.insId= ? order by t.insAliasId.insId ";
    return super.createQuery(hql, insId).list();
  }

}
