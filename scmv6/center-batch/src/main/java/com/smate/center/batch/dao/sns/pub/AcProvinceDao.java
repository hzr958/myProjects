package com.smate.center.batch.dao.sns.pub;

import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.hibernate.Query;
import org.springframework.stereotype.Repository;

import com.smate.center.batch.exception.pub.DaoException;
import com.smate.center.batch.model.sns.pub.AcProvince;

/**
 * 省份智能匹配.
 * 
 * @author chenxiangrong
 * 
 */
@Repository
public class AcProvinceDao extends AutoCompleteDao<AcProvince> {

  /**
   * 省份匹配列表.
   * 
   * @param startWith
   * @param size
   * @return
   * @throws DaoException
   */
  @SuppressWarnings("unchecked")
  public List<AcProvince> getAcProvince(String startWith, int size) throws DaoException {
    Query query = null;

    startWith = this.getQuery(startWith);
    boolean isChinese = !StringUtils.isAsciiPrintable(startWith);
    if (isChinese) {
      query = super.createQuery("from AcProvince t where  lower(t.cnName) like ? order by t.cnName ",
          "%" + startWith + "%");
    } else {
      query = super.createQuery("from AcProvince t where  lower(t.enName) like ? order by t.enName ",
          "%" + startWith + "%");
    }

    query.setMaxResults(size);
    return query.list();
  }

  /**
   * 完全匹配.
   * 
   * @param name
   * @return
   * @throws DaoException
   */
  public AcProvince getAcProvinceObj(String name) {
    String hql = null;
    hql = "from AcProvince t where t.name=?";
    return super.findUnique(hql, new Object[] {name});
  }

}
