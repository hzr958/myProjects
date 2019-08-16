package com.smate.sie.core.base.utils.dao.statistics;

import java.util.Date;
import java.util.List;

import org.hibernate.Query;
import org.springframework.stereotype.Repository;

import com.smate.core.base.utils.data.SieHibernateDao;
import com.smate.core.base.utils.model.Page;
import com.smate.sie.core.base.utils.model.statistics.SieBhIndexRead;

/**
 * 单位首页查看记录表 Dao
 * 
 * @author hd
 *
 */

@Repository
public class SieBhIndexReadDao extends SieHibernateDao<SieBhIndexRead, Long> {

  /**
   * 批量获取记录
   * 
   * @param insId
   * @param page
   * @return
   */
  @SuppressWarnings("unchecked")
  public Page<SieBhIndexRead> findByDate(Long insId, Date beginTime, Date endTime, Page<SieBhIndexRead> page) {
    StringBuilder hql = new StringBuilder();
    hql.append("from SieBhIndexRead where  insId=? and creDate>=? and creDate<?");
    Object[] objects = new Object[] {insId, beginTime, endTime};
    Query q = createQuery(hql.toString(), objects);
    if (page.isAutoCount()) {
      long totalCount = countHqlResult(hql.toString(), objects);
      page.setTotalCount(totalCount);
    }
    setPageParameter(q, page);
    List<SieBhIndexRead> result = q.list();
    page.setResult(result);
    return page;
  }


}
