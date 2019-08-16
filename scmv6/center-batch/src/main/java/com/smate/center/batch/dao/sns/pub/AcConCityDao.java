package com.smate.center.batch.dao.sns.pub;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Repository;

import com.smate.center.batch.exception.pub.DaoException;
import com.smate.center.batch.model.sns.pub.AcConCity;

/**
 * 城市智能匹配.
 * 
 * @author chenxiangrong
 * 
 */
@Repository
public class AcConCityDao extends AutoCompleteDao<AcConCity> {
  /**
   * 省份匹配列表.
   * 
   * @param startWith
   * @param prvId TODO
   * @param size
   * @return
   * @throws DaoException
   */
  @SuppressWarnings("unchecked")
  public List<AcConCity> getAcConstCity(String startWith, Long prvId, int size) throws DaoException {

    startWith = this.getQuery(startWith);
    boolean isChinese = !StringUtils.isAsciiPrintable(startWith);
    StringBuffer hql = new StringBuffer();
    List params = new ArrayList();
    hql.append("from AcConCity t where");
    if (isChinese) {
      hql.append(" lower(t.cnName) like ?");

    } else {
      hql.append(" lower(t.enName) like ?");
    }
    params.add("%" + startWith + "%");

    if (prvId != null) {
      hql.append(" and t.prvId = ? ");
      params.add(prvId);
    }
    return super.createQuery(hql.toString(), params.toArray()).setMaxResults(size).list();
  }

  /**
   * 完全匹配.
   * 
   * @param name
   * @return
   * @throws DaoException
   */
  public AcConCity getAcConCityObj(String name) {
    String hql = null;
    hql = "from AcConCity t where t.name=?";
    return super.findUnique(hql, new Object[] {name});
  }
}
