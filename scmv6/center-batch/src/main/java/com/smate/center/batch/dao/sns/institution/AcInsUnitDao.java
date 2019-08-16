package com.smate.center.batch.dao.sns.institution;

import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.hibernate.Query;
import org.hibernate.transform.Transformers;
import org.springframework.stereotype.Repository;

import com.smate.center.batch.exception.pub.DaoException;
import com.smate.center.batch.model.sns.pub.AcInsUnit;
import com.smate.core.base.utils.data.SnsHibernateDao;

/**
 * 单位院系自动提示DAO.
 * 
 * @author zym
 * 
 */
@Repository(value = "acInsUnitDao")
public class AcInsUnitDao extends SnsHibernateDao<AcInsUnit, Long> {

  /**
   * 获取单位院系列表，只读size条记录.
   * 
   * @param startWith
   * @param insName
   * @param size
   * @return
   * @throws DaoException
   */
  @SuppressWarnings("unchecked")
  public List<AcInsUnit> getAcInsUnit(String startWith, String insName, int size) throws DaoException {

    String hql = null;

    hql = "from AcInsUnit t where lower(t.insName)=? and lower(t.searchName) like ? order by t.insName,t.seqNo";

    Query query =
        super.createQuery(hql, new Object[] {insName.trim().toLowerCase(), "%" + startWith.trim().toLowerCase() + "%"});
    query.setMaxResults(size);

    List<AcInsUnit> list = query.list();

    return list;
  }

  /**
   * 获取单位院系列表
   * 
   * @param insName
   * @param searchName
   * @return
   * @throws DaoException
   */
  @SuppressWarnings("unchecked")
  public List<AcInsUnit> getAcInsUnit(String insName, String searchName) throws DaoException {
    if (StringUtils.isNotBlank(insName)) {
      insName = insName.toLowerCase();
    }
    if (StringUtils.isNotBlank(searchName)) {
      searchName = searchName.toLowerCase();
    }
    String hql = "from AcInsUnit t where lower(t.insName)=? and lower(t.searchName) like ? order by t.seqNo,sId";
    return super.createQuery(hql, insName, searchName + "%").list();
  }

  /**
   * 获取单位下院系中英文
   * 
   * @param unitList
   * @param insName
   * @return
   * @throws DaoException
   */
  @SuppressWarnings({"unchecked", "rawtypes"})
  public List<Map> getConstInsUnit(List<Long> unitList, String insName) throws DaoException {
    String sql =
        "select c.zh_name as ZH_NAME ,c.en_name as EN_NAME,c.unit_id as UNIT_ID from const_ins_unit c where c.unit_id in (:units) and c.ins_name=:insName and c.unit_type=1 order by c.SEQ_NO";
    return super.getSession().createSQLQuery(sql).setParameterList("units", unitList).setParameter("insName", insName)
        .setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP).list();
  }
}
