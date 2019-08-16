package com.smate.center.task.dao.sns.quartz;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.collections4.CollectionUtils;
import org.springframework.stereotype.Repository;

import com.smate.center.task.exception.DaoException;
import com.smate.center.task.model.sns.quartz.Institution;
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
   * 通过单位名获取单位Id.
   * 
   * @param zhName
   * @param enName
   * @return Long @
   */
  public Long getInsIdByName(String zhName, String enName) {
    String hql = "select id from Institution where zhName=? or enName=?";
    List<Long> list = super.find(hql, zhName, enName);
    if (list.size() > 0) {
      return list.get(0).longValue();
    }
    return null;
  }

  public Institution findByName(String name) {
    String hql = "from Institution where enName = ? or zhName = ? ";
    List<Institution> list = super.find(hql, name, name);
    if (list.size() > 0) {
      return list.get(0);
    }
    return null;
  }

  /**
   * 通过单位编号取得单位实体.
   * 
   * @param id
   * @return Institution
   * @throws DaoException
   */
  public Institution findById(Long id) {

    return super.findUniqueBy("id", id);
  }

  /**
   * 获取单位中英文名称
   *
   * @param id
   * @return
   */
  public Institution findInsName(Long id) {
    String hql = "select new Institution( i.id,i.zhName,i.enName) from Institution i where i.id=:insId";
    return (Institution) super.createQuery(hql).setParameter("insId", id).uniqueResult();
  }

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
   * 更新单位信息
   *
   * @param objects
   * @throws DaoException
   */
  public void updateIns(Object[] objects) throws DaoException {
    String sql = "update Institution t set t.tel=? ,t.regionId=?,t.zhAddress=?,t.url=? where t.id=? ";
    super.createQuery(sql, objects).executeUpdate();
  }

  /**
   * 通过单位中文名获取单位
   *
   * @param name
   * @return
   * @throws DaoException
   */
  public Institution findInsByName(String name) throws DaoException {
    String hql = "from Institution t where t.zhName = ? ";
    List<Institution> list = super.find(hql, name);
    if (list.size() > 0) {
      return list.get(0);
    }
    return null;
  }

  public Long getInsNatureByInsId(Long insId) {
    String hql = "select t.nature from Institution t where id=:insId";
    return (Long) super.createQuery(hql).setParameter("insId", insId).uniqueResult();
  }

  /**
   * 查找单位地区编码
   * 
   * @param insId
   * @return
   */
  public Long findInsRegionId(Long insId) {
    String hql = "select t.regionId from Institution t where t.id = :insId";
    return (Long) super.createQuery(hql).setParameter("insId", insId).uniqueResult();
  }

}
