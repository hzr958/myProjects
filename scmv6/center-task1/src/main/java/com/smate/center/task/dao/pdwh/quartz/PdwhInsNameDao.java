package com.smate.center.task.dao.pdwh.quartz;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.smate.center.task.model.pdwh.pub.PdwhInsName;
import com.smate.core.base.utils.data.PdwhHibernateDao;

/**
 * 基准库机构名dao
 * 
 * @author zjh
 *
 */
@Repository
public class PdwhInsNameDao extends PdwhHibernateDao<PdwhInsName, Long> {

  @SuppressWarnings("unchecked")
  public List<PdwhInsName> getPdwhInsName(Long insId) {
    String hql =
        "select new PdwhInsName(id, insId, insName, nameLength) from PdwhInsName where insId =:insId order by nameLength desc";
    return super.createQuery(hql).setParameter("insId", insId).list();
  }

  /**
   * 通过insName查其他的别名
   * 
   * @param insName
   * @return
   */
  @SuppressWarnings("unchecked")
  public List<String> getSimilarInsName(String insName) {
    String hql =
        "select insName from  PdwhInsName where insId in(select insId from PdwhInsName where insName =:insName )";
    return super.createQuery(hql).setParameter("insName", insName).list();
  }

}
