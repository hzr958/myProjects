package com.smate.center.batch.dao.sns.institution;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.collections4.CollectionUtils;
import org.springframework.stereotype.Repository;

import com.smate.center.batch.exception.pub.DaoException;
import com.smate.center.batch.model.sns.pub.Institution;
import com.smate.core.base.utils.data.SnsHibernateDao;



/**
 * 单位数据层接口.
 * 
 * @author tsz
 * 
 */
@Repository
public class InstitutionDao extends SnsHibernateDao<Institution, Long> {

  /**
   * 通过单位名获取对应的单位记录.
   * 
   * @param zhName
   * @param enName
   * @return List<Institution>
   * @throws DaoException
   */
  public List<Institution> getInsListByName(String zhName, String enName, Long natureType) {
    List<Institution> result = null;
    String hql = "from Institution where zhName=? or enName=?";
    List<Institution> list = super.find(hql, zhName, enName);
    if (CollectionUtils.isNotEmpty(list)) {
      result = new ArrayList<Institution>();
      if (natureType != null) {
        for (Institution ins : list) {
          if (ins.getNature().longValue() == natureType.longValue()) {
            result.add(ins);
            continue;
          }
        }
      }
    }
    return result;

  }

  /**
   * 通过单位编号取得单位实体.
   * 
   * @param id
   * @return Institution
   * @throws DaoException
   */
  public Institution findById(Long id) throws DaoException {

    return super.findUniqueBy("id", id);

  }

  public Institution findByName(String name) throws DaoException {
    String hql = "from Institution where enName = ? or zhName = ? ";
    List<Institution> list = super.find(hql, name, name);
    if (list.size() > 0) {
      return list.get(0);
    }
    return null;
  }



  public Long getRegionIdByInsId(Long insId) {
    String hql = "select regionId from Institution where id = :insId";
    return (Long) super.createQuery(hql).setParameter("insId", insId).uniqueResult();
  }
}
