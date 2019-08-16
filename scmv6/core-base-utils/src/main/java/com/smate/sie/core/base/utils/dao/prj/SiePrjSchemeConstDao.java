package com.smate.sie.core.base.utils.dao.prj;

import java.util.List;
import java.util.Map;

import org.hibernate.transform.Transformers;
import org.springframework.stereotype.Repository;
import org.springframework.util.CollectionUtils;

import com.smate.core.base.utils.data.SieHibernateDao;
import com.smate.sie.core.base.utils.model.prj.SiePrjSchemeConst;

/**
 * 
 * @author yxs
 * @descript 项目类别dao
 */
@Repository
public class SiePrjSchemeConstDao extends SieHibernateDao<SiePrjSchemeConst, Long> {
  /**
   * 
   *
   * @descript 查询类别
   */
  public SiePrjSchemeConst getByNameAndFromId(String fromName, Long prjFromId) {
    return (SiePrjSchemeConst) super.createQuery(
        "from PrjSchemeConst t where (lower(t.zhName) = ? or lower(t.enName)=?) and t.prjFromId=?",
        new Object[] {fromName, fromName, prjFromId}).uniqueResult();
  }

  @SuppressWarnings("rawtypes")
  public SiePrjSchemeConst getByName(String fromName) {
    List list = super.createQuery("from PrjSchemeConst t where (lower(t.zhName) = ? or lower(t.enName)=?)",
        new Object[] {fromName, fromName}).list();
    if (!CollectionUtils.isEmpty(list)) {
      return (SiePrjSchemeConst) list.get(0);
    }
    return null;
  }

  @SuppressWarnings("unchecked")
  public List<Map<String, Object>> getPrjSchemeConstList() {
    return super.createQuery("select new Map(t.zhName as itemName,t.id as itemId) from PrjSchemeConst t").list();
  }

  @SuppressWarnings({"unchecked", "rawtypes"})
  public List<Map> getSchemeAndFrom() {
    String sql =
        "select t.SCHEME_ID,t.SCHEME_NAME,t2.ID,t2.NAME_ZH from CONST_PRJ_SCHEME t,CONST_PRJ_FROM t2 where t.PRJ_FROM_ID=t2.ID";
    return super.getSession().createSQLQuery(sql).setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP).list();
  }

}
