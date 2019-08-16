package com.smate.sie.center.task.dao;

import java.util.Date;
import java.util.List;

import org.springframework.stereotype.Repository;

import com.smate.core.base.utils.data.SieHibernateDao;
import com.smate.sie.center.task.model.ImportPdwhPub;

/**
 * 
 * @author ztt
 *
 */
@Repository
public class ImportPdwhPubDao extends SieHibernateDao<ImportPdwhPub, Long> {

  /**
   * 获取同步记录表记录
   * 
   * @return
   */
  public Date getLastTime(Long insId) {
    String hql = "from ImportPdwhPub where insId=?";
    ImportPdwhPub importPdwhPub = (ImportPdwhPub) super.createQuery(hql, insId).uniqueResult();
    if (null != importPdwhPub) {
      return importPdwhPub.getUpdateTime();
    }
    return null;
  }

  /**
   * 
   * @param id
   */
  public void delete(long id) {
    String hql = "delete from ImportPdwhPub t where t.insId = ? ";
    super.createQuery(hql, id).executeUpdate();
    this.getSession().flush();

  }

  @SuppressWarnings("unchecked")
  public List<ImportPdwhPub> getImportPdwhPubList(Integer size) {
    String hql = "from ImportPdwhPub where status = 1";
    return super.createQuery(hql).setMaxResults(size).list();
  }

  public void updateStatus() {
    String hql = "update ImportPdwhPub t set t.status = 1 ";
    super.createQuery(hql).executeUpdate();
    this.getSession().flush();

  }

  public Long getInsCountByStatus() {
    String hql = "select count(insId) from ImportPdwhPub t where t.status = 1 ";
    return findUnique(hql);
  }

}
