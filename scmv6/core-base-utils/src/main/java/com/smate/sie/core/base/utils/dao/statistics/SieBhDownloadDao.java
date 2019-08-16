package com.smate.sie.core.base.utils.dao.statistics;

import java.util.Date;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.hibernate.Query;
import org.springframework.stereotype.Repository;

import com.smate.core.base.utils.data.SieHibernateDao;
import com.smate.core.base.utils.model.Page;
import com.smate.sie.core.base.utils.model.statistics.SieBhDownload;

/**
 * 全文下载数
 * 
 * @author ztg
 *
 */
@Repository
public class SieBhDownloadDao extends SieHibernateDao<SieBhDownload, Long> {

  /**
   * 批量获取记录
   * 
   * @param insId
   * @param page
   * @return
   */
  @SuppressWarnings("unchecked")
  public Page<SieBhDownload> findByTypeAndKeyIds(Long keyId, String types, Date beginTime, Date endTime,
      Page<SieBhDownload> page) {
    StringBuilder hql = new StringBuilder();
    hql.append("from SieBhDownload where keyId =? and  creDate>=? and creDate<?");
    if (StringUtils.isNotBlank(types)) {
      hql.append(" and type in(" + types + ") ");
    }
    Object[] objects = new Object[] {keyId, beginTime, endTime};
    Query q = createQuery(hql.toString(), objects);
    if (page.isAutoCount()) {
      long totalCount = countHqlResult(hql.toString(), objects);
      page.setTotalCount(totalCount);
    }
    setPageParameter(q, page);
    List<SieBhDownload> result = q.list();
    page.setResult(result);
    return page;
  }
}
