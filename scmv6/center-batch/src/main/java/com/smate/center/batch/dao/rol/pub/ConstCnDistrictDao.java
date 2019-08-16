package com.smate.center.batch.dao.rol.pub;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Repository;

import com.smate.center.batch.model.rol.pub.ConstCnDistrict;
import com.smate.core.base.utils.data.RolHibernateDao;

/**
 * 中国城市的区，比如深圳市南山区.
 * 
 * @author liqinghua
 * 
 */
@Repository
public class ConstCnDistrictDao extends RolHibernateDao<ConstCnDistrict, Long> {

  @SuppressWarnings("unchecked")
  public List<ConstCnDistrict> queryByCyId(Long cyId) {
    String hql = "from ConstCnDistrict where cyId = ? ";
    return super.createQuery(hql, cyId).list();
  }

  @SuppressWarnings("unchecked")
  public List<ConstCnDistrict> getAllDistrictByCyId(Long cyId) {
    String hql = "from ConstCnDistrict as c where c.cyId = ? order by c.zhSeq";
    return super.createQuery(hql, cyId).list();

  }

  @SuppressWarnings("unchecked")
  public List<ConstCnDistrict> getAllDistrictByPrvId(Long prvId) {
    String hql = "from ConstCnDistrict as c where c.prvId = ? order by c.zhSeq";
    return super.createQuery(hql, prvId).list();

  }

  /**
   * 获取指定ID的地区列表.
   * 
   * @param ids
   * @return
   */
  @SuppressWarnings("unchecked")
  public List<ConstCnDistrict> getCityByIds(List<Long> disIds) {
    String hql = "from ConstCnDistrict t where t.disId in(:disIds)";
    return super.createQuery(hql).setParameterList("disIds", disIds).list();
  }

  /**
   * 完全匹配.
   * 
   * @param name
   * @return
   */
  public ConstCnDistrict queryByName(String name) {
    boolean isChinese = !StringUtils.isAsciiPrintable(name);
    String hql = null;
    if (isChinese) {
      hql = "from ConstCnDistrict t where t.zhName=?";
    } else {
      hql = "from ConstCnDistrict t where t.enName=?";
    }
    return super.findUnique(hql, new Object[] {name});
  }

  /**
   * 获取ID靠前的市管辖区列表.
   * 
   * @param size
   * @return
   */
  @SuppressWarnings("unchecked")
  public List<ConstCnDistrict> queryPrePrvDis(Long cyId, int size) {
    String hql = " from ConstCnDistrict t where t.cyId = ? order by t.disId asc ";
    return super.createQuery(hql, cyId).setMaxResults(size).list();
  }

  /**
   * 批量查询市管辖区，顺序按照ID的顺序排序.
   * 
   * @param prvIds
   * @return
   */
  @SuppressWarnings("unchecked")
  public List<ConstCnDistrict> getOrderByIds(List<Long> disIds) {

    String hql = "from ConstCnDistrict t where t.disId in(:disIds)";
    List<ConstCnDistrict> list = super.createQuery(hql).setParameterList("disIds", disIds).list();
    List<ConstCnDistrict> orderList = new ArrayList<ConstCnDistrict>();
    for (Long disId : disIds) {
      for (int i = 0; i < list.size(); i++) {
        ConstCnDistrict cy = list.get(i);
        if (cy.getCyId().equals(disId)) {
          orderList.add(cy);
          list.remove(i);
          break;
        }
      }
    }
    return orderList;
  }
}
