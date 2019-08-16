package com.smate.center.batch.dao.rol.pub;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Repository;

import com.smate.center.batch.model.rol.pub.ConstCnCity;
import com.smate.core.base.utils.data.RolHibernateDao;


/**
 * 中国地区.
 * 
 * @author liqinghua
 * 
 */
@Repository
public class ConstCnCityDao extends RolHibernateDao<ConstCnCity, Long> {

  /**
   * 查询所在省份地区列表.
   * 
   * @param prvId
   * @return
   */
  @SuppressWarnings("unchecked")
  public List<ConstCnCity> queryByPrvId(Long prvId) {
    String hql = "from ConstCnCity where prvId = ? ";
    return super.createQuery(hql, prvId).list();
  }

  @SuppressWarnings("unchecked")
  public List<ConstCnCity> getAllCit(Long provinceId) {
    String hql = "from ConstCnCity as c where c.prvId = ? order by c.zhSeq";
    return super.createQuery(hql, provinceId).list();

  }

  @SuppressWarnings("unchecked")
  public List<ConstCnCity> getAllCityByPrvId(Long prvId) {
    String hql = "from ConstCnCity where prvId = ? order by zhSeq ";
    return super.createQuery(hql, Long.valueOf(prvId)).list();
  }

  /**
   * 获取指定ID的地区列表.
   * 
   * @param ids
   * @return
   */
  @SuppressWarnings("unchecked")
  public List<ConstCnCity> getCityByIds(List<Long> ids) {
    String hql = "from ConstCnCity t where t.cyId in(:cyIds)";
    return super.createQuery(hql).setParameterList("cyIds", ids).list();
  }

  /**
   * 完全匹配.
   * 
   * @param name
   * @return
   */
  public ConstCnCity queryByName(String name) {
    boolean isChinese = !StringUtils.isAsciiPrintable(name);
    String hql = null;
    if (isChinese) {
      hql = "from ConstCnCity t where t.zhName=?";
    } else {
      hql = "from ConstCnCity t where t.enName=?";
    }
    return super.findUnique(hql, new Object[] {name});
  }

  /**
   * 获取前几个省的省会.
   * 
   * @param size
   * @return
   */
  @SuppressWarnings("unchecked")
  public List<ConstCnCity> queryPrePrvFirstCity(int size) {

    String hql = "select min(cyId) from ConstCnCity t group by t.prvId order by t.prvId asc ";
    List<Long> cyIds = super.createQuery(hql).setMaxResults(size).list();
    hql = " from ConstCnCity t where t.cyId in(:cyIds) order by t.cyId asc ";
    return super.createQuery(hql).setParameterList("cyIds", cyIds).list();
  }

  /**
   * 获取ID靠前的省份地区列表.
   * 
   * @param size
   * @return
   */
  @SuppressWarnings("unchecked")
  public List<ConstCnCity> queryPrePrvCity(Long prvId, int size) {
    String hql = " from ConstCnCity t where t.prvId = ? order by t.cyId asc ";
    return super.createQuery(hql, prvId).setMaxResults(size).list();
  }

  /**
   * 批量查询地区，顺序安装ID的顺序排序.
   * 
   * @param prvIds
   * @return
   */
  @SuppressWarnings("unchecked")
  public List<ConstCnCity> getOrderByIds(List<Long> cyIds) {

    String hql = "from ConstCnCity t where t.cyId in(:cyIds)";
    List<ConstCnCity> list = super.createQuery(hql).setParameterList("cyIds", cyIds).list();
    List<ConstCnCity> orderList = new ArrayList<ConstCnCity>();
    for (Long cyId : cyIds) {
      for (int i = 0; i < list.size(); i++) {
        ConstCnCity cy = list.get(i);
        if (cy.getCyId().equals(cyId)) {
          orderList.add(cy);
          list.remove(i);
          break;
        }
      }
    }
    return orderList;
  }
}
