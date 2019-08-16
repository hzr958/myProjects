package com.smate.sie.center.task.dao.tmp;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.smate.core.base.utils.data.SieHibernateDao;
import com.smate.sie.center.task.model.tmp.SieTaskInsBaiDuGetAddr;

@Repository
public class SieTaskInsBaiDuGetAddrDao extends SieHibernateDao<SieTaskInsBaiDuGetAddr, Long> {

  /**
   * 获取需要处理的数据
   * 
   * @param size
   * @param sourceType
   * @return
   */
  @SuppressWarnings("unchecked")
  public List<Long> batchGetData(Integer size) {
    String hql = "select id from SieTaskInsBaiDuGetAddr where status=0 ";

    return super.createQuery(hql).setMaxResults(size).list();
  }

  public void updateStatusById(Long id, Integer status) {
    String hql = "update SieTaskInsBaiDuGetAddr set status=:status where id=:id";
    super.createQuery(hql).setParameter("id", id).setParameter("status", status).executeUpdate();
  }

  /**
   * 获取没有hash的数据
   * 
   * @param batchSize
   * @return
   * @author ztg
   * @date 2018年3月24日
   */
  public List<Long> getNullHashAddr(Integer batchSize) {
    String hql = "select id from SieTaskInsBaiDuGetAddr where tmpInsNameHash is null";
    return super.createQuery(hql).list();
  }

}
