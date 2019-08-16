package com.smate.center.task.dao.tmp;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.smate.center.task.model.tmp.TmpInsBdAddr;
import com.smate.core.base.utils.data.PdwhHibernateDao;

@Repository
public class TmpInsBdAddrDao extends PdwhHibernateDao<TmpInsBdAddr, Long> {

  /**
   * 获取需要处理的数据
   * 
   * @param size
   * @param sourceType
   * @return
   */
  @SuppressWarnings("unchecked")
  public List<Long> batchGetData(Integer size) {
    String hql = "select id from TmpInsBdAddr where status=0 ";

    return super.createQuery(hql).setMaxResults(size).list();
  }

  public void updateStatusById(Long id, Integer status) {
    String hql = "update TmpInsBdAddr set status=:status where id=:id";
    super.createQuery(hql).setParameter("id", id).setParameter("status", status).executeUpdate();
  }

  /**
   * 获取没有hash的数据
   * 
   * @param batchSize
   * @return
   * @author LIJUN
   * @date 2018年3月24日
   */
  public List<Long> getNullHashAddr(Integer batchSize) {
    String hql = "select id from TmpInsBdAddr where tmpInsNameHash is null";
    return super.createQuery(hql).list();
  }

}
