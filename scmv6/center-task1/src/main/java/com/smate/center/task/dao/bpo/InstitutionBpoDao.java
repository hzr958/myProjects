package com.smate.center.task.dao.bpo;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.smate.center.task.exception.DaoException;
import com.smate.center.task.model.bpo.InstitutionBpo;
import com.smate.core.base.utils.data.BpoHibernateDao;

/**
 * 
 * bpo系统 机构dao
 * 
 * @author hd
 *
 */
@Repository
public class InstitutionBpoDao extends BpoHibernateDao<InstitutionBpo, Long> {
  /**
   * 通过orgCode和数据来源获取单位
   * 
   * @param orgCode
   * @param dataFrom
   * @return
   * @throws DaoException
   */
  public InstitutionBpo findInsByCode(Integer orgCode, Long dataFrom) throws DaoException {
    String hql = "from InstitutionBpo t where t.isisOrgCode = ? and t.dataFrom=?";
    List<InstitutionBpo> list = super.find(hql, orgCode, dataFrom);
    if (list.size() > 0) {
      return list.get(0);
    }
    return null;
  }

  /**
   * 通过单位中文名获取单位
   * 
   * @param name
   * @return
   * @throws DaoException
   */
  public InstitutionBpo findInsByName(String name) throws DaoException {
    String hql = "from InstitutionBpo t where t.zhName = ? ";
    List<InstitutionBpo> list = super.find(hql, name);
    if (list.size() > 0) {
      return list.get(0);
    }
    return null;
  }

  public Long findNewInsId() throws DaoException {
    String sql = "select seq_institution.nextval from dual";
    return super.queryForLong(sql);
  }

  /**
   * 更新单位信息
   * 
   * @param objects
   * @throws DaoException
   */
  public void updateIns(Object[] objects) throws DaoException {
    String sql = "update InstitutionBpo t set t.tel=? ,t.regionId=?,t.zhAddress=?,t.url=? where t.id=?";
    super.createQuery(sql, objects).executeUpdate();
  }
}
