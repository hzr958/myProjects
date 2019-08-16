package com.smate.center.task.dao.rol.quartz;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.smate.center.task.exception.DaoException;
import com.smate.center.task.model.rol.quartz.InstitutionRol;
import com.smate.core.base.utils.data.RolHibernateDao;

@Repository
public class InstitutionRolDao extends RolHibernateDao<InstitutionRol, Long> {

  public InstitutionRol findById(Long insId) {
    return super.findUniqueBy("id", insId);
  }

  /**
   * 通过单位中文名获取单位
   * 
   * @param name
   * @return
   * @throws DaoException
   */
  public InstitutionRol findInsByName(String name) throws DaoException {
    String hql = "from InstitutionRol t where t.zhName = ? ";
    List<InstitutionRol> list = super.find(hql, name);
    if (list.size() > 0) {
      return list.get(0);
    }
    return null;
  }

  /**
   * 更新单位信息
   * 
   * @param objects
   * @throws DaoException
   */
  public void updateIns(Object[] objects) throws DaoException {
    String sql = "update InstitutionRol t set t.tel=? ,t.regionId=?,t.zhAddress=?,t.url=? where t.id=? ";
    super.createQuery(sql, objects).executeUpdate();
  }

  /**
   * 批量获取ROL机构信息------同步信息用
   * 
   * @param size
   * @param lastInsId
   * @return
   */
  @SuppressWarnings("unchecked")
  public List<InstitutionRol> getInstitutionRolBySize(Integer size, Long firstInsId, Long endInsId) {
    String hql = " from InstitutionRol t where t.id >:firstInsId and t.id<=:endInsId order by t.id asc";
    return super.createQuery(hql).setParameter("firstInsId", firstInsId).setParameter("endInsId", endInsId)
        .setMaxResults(size).list();
  }
}
