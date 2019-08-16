package com.smate.center.batch.dao.rol.pub;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import org.apache.commons.lang.StringUtils;
import org.hibernate.Query;
import org.springframework.stereotype.Repository;

import com.smate.center.batch.model.rol.pub.ConstCnProvince;
import com.smate.core.base.utils.data.RolHibernateDao;


/**
 * 中国省.
 * 
 * @author liqinghua
 * 
 */
@Repository
public class ConstCnProvinceDao extends RolHibernateDao<ConstCnProvince, Long> {

  /**
   * 查找指定ID的省份列表.
   * 
   * @param ids
   * @return
   */
  @SuppressWarnings("unchecked")
  public List<ConstCnProvince> queryForIds(List<Long> ids) {

    String hql = "from ConstCnProvince where prvId in (:prvIds)";
    return super.createQuery(hql).setParameterList("prvIds", ids).list();
  }

  /**
   * 完全匹配.
   * 
   * @param name
   * @return
   */
  public ConstCnProvince queryByName(String name) {
    boolean isChinese = !StringUtils.isAsciiPrintable(name);
    String hql = null;
    if (isChinese) {
      hql = "from ConstCnProvince t where t.zhName=?";
    } else {
      hql = "from ConstCnProvince t where t.enName=?";
    }
    return super.findUnique(hql, new Object[] {name});
  }

  /**
   * 获取所有的省份.
   * 
   * @param locale
   * @return
   */
  public List<ConstCnProvince> getAllProvince(Locale locale) {
    //
    String hql = "from ConstCnProvince order by  " + locale.getLanguage() + "Seq";
    Query query = createQuery(hql);
    query.setCacheable(true);
    List<ConstCnProvince> list = query.list();

    return list;
  }

  /**
   * 获取前几个省份.
   * 
   * @param size
   * @return
   */
  @SuppressWarnings("unchecked")
  public List<ConstCnProvince> queryPreSeqPrv(int size) {
    String hql = "from ConstCnProvince t order by zhSeq asc ";
    return super.createQuery(hql).setMaxResults(size).list();
  }

  /**
   * 批量查询省份，顺序安装ID的顺序排序.
   * 
   * @param prvIds
   * @return
   */
  @SuppressWarnings("unchecked")
  public List<ConstCnProvince> getOrderByIds(List<Long> prvIds) {

    String hql = "from ConstCnProvince t where t.prvId in(:prvIds)";
    List<ConstCnProvince> list = super.createQuery(hql).setParameterList("prvIds", prvIds).list();
    List<ConstCnProvince> orderList = new ArrayList<ConstCnProvince>();
    for (Long prvId : prvIds) {
      for (int i = 0; i < list.size(); i++) {
        ConstCnProvince prv = list.get(i);
        if (prv.getPrvId().equals(prvId)) {
          orderList.add(prv);
          list.remove(i);
          break;
        }
      }
    }
    return orderList;
  }
}
