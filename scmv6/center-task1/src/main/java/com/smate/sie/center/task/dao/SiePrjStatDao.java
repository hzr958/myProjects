package com.smate.sie.center.task.dao;

import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.springframework.stereotype.Repository;

import com.smate.core.base.utils.data.SieHibernateDao;
import com.smate.sie.center.task.model.SiePrjStat;

/**
 * 
 * @author ztg
 *
 */
@Repository
public class SiePrjStatDao extends SieHibernateDao<SiePrjStat, Long> {
  @SuppressWarnings("unchecked")
  public List<SiePrjStat> getsByPrjIds(List<Long> prjIds) {
    if (CollectionUtils.isEmpty(prjIds))
      return null;
    List<SiePrjStat> prjStats =
        super.createQuery("from SiePrjStat t where t.prjId in(:prjId)").setParameterList("prjId", prjIds).list();
    return prjStats;
  }

  @SuppressWarnings("unchecked")
  public SiePrjStat getByPrjId(Long prjId) {
    if (prjId == null)
      return null;
    List<SiePrjStat> prjStats = super.createQuery("from SiePrjStat t where t.prjId=?", new Object[] {prjId}).list();
    if (prjStats != null && prjStats.size() > 0) {
      return prjStats.get(0);
    }
    return null;
  }

  /**
   * 获取prjId指定的项目的点赞统计数
   * 
   * @param prjId
   * @param fieldName
   * @return
   */
  public Long getPrjAwardNum(Long prjId, String fieldName) {
    StringBuilder hql = new StringBuilder();
    hql.append("select t.");
    hql.append(fieldName);
    hql.append(" from SiePrjStat t where t.prjId=?");
    return findUnique(hql.toString(), prjId);
  }

  /**
   * 获取ID最大值
   */
  public Long getMaxId() {
    String hql = "";
    return findUnique(hql);
  }

}
