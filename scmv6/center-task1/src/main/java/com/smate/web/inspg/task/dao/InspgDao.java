package com.smate.web.inspg.task.dao;

import java.util.List;

import com.smate.core.base.utils.data.SnsHibernateDao;
import com.smate.core.base.utils.exception.RcmdTaskException;
import com.smate.web.inspg.task.model.Inspg;

/**
 * 机构主页主体实体类Dao for task
 * 
 * @author hzr
 * 
 * @since 6.0.1
 * @version 6.0.1
 */
public class InspgDao extends SnsHibernateDao<Inspg, Long> {


  /**
   * 查询50条推荐数据，不包括自己已经加入的
   * 
   * @param psnId
   */
  @SuppressWarnings("unchecked")
  public List<Inspg> getInspgByCreatedTime(Long psnId) throws RcmdTaskException {

    String hql =
        "from Inspg t where t.status =1 and not exists (select 1 from InspgMembers t1 where t1.id =? and t.Id = t1.inspgId) order by t.createTime desc";

    return super.createQuery(hql, psnId).setMaxResults(50).list();
  }

}
