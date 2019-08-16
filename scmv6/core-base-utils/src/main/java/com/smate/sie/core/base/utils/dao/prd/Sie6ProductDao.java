package com.smate.sie.core.base.utils.dao.prd;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.smate.core.base.utils.data.SieHibernateDao;
import com.smate.sie.core.base.utils.model.prd.Sie6Product;

@Repository
public class Sie6ProductDao extends SieHibernateDao<Sie6Product, Long> {

  /**
   * 通过unitId查找
   * 
   * @param unitId
   * @return
   */
  @SuppressWarnings("unchecked")
  public List<Sie6Product> getByInsIdAndUnitId(Long insId, Long unitId) {
    String hql = "from Sie6Product t where t.insId = ? and t.unitId = ?";
    return super.createQuery(hql, insId, unitId).list();
  }

  /**
   * 通过insId查找
   * 
   * @param unitId
   * @return
   */
  @SuppressWarnings("unchecked")
  public List<Sie6Product> getListByInsId(Long insId) {
    String hql = "from Sie6Product t where t.insId = ?";
    return super.createQuery(hql, insId).list();
  }

  /**
   * 单位产品统计， 产品是物理删除，所以不用做有效记录过滤
   * 
   * @param insId
   * @return
   */
  public Long getProductTotalNumByInsId(Long insId) {
    String hql = "select count(id) from Sie6Product where insId = ? ";
    return super.findUnique(hql, insId);
  }

  /**
   * 人员拥有产品数
   * 
   * @param insId
   * @return
   */
  public Long getPdCount(Long psnId) {
    String hql = "select count(id) from Sie6Product where psnId = ?";
    return super.findUnique(hql, psnId);
  }

}
