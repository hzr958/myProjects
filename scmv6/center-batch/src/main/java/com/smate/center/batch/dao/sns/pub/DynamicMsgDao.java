package com.smate.center.batch.dao.sns.pub;

import java.util.List;

import org.hibernate.Query;
import org.springframework.stereotype.Repository;

import com.smate.center.batch.model.dynamic.DynamicMsg;
import com.smate.core.base.utils.data.SnsHibernateDao;

/**
 * 动态信息表dao
 * 
 * @author zk
 *
 */
@Repository
public class DynamicMsgDao extends SnsHibernateDao<DynamicMsg, Long> {


  /**
   * 获取动态列表的动态id
   * 
   * @param psnId
   * @param batchSize
   * @param firstResult
   * @return
   */
  @SuppressWarnings("unchecked")
  public List<Long> getDynListByPsnId(Long psnId, Integer batchSize, Integer pageNo) {

    String hql = "select  dm.dynId  from  DynamicMsg  dm  where "
        + " dm.delstatus = 0    and dm.producer =:psnId     order by dm.dynId desc ";
    Query query = this.createQuery(hql).setParameter("psnId", psnId).setMaxResults(batchSize)
        .setFirstResult((pageNo - 1) * batchSize);
    return query.list();
  }

  /**
   * 获取当前人产生的动态列表的总数
   * 
   * @param psnId
   * @return
   */
  @SuppressWarnings("unchecked")
  public Integer getDynListForPsnCount(Long psnId) {

    String countSql = "select count(1)    from v_dynamic_msg dm   where"
        + " dm.del_status = 0    and dm.producer=?      order by dm.dyn_Id desc";
    Integer count = super.queryForInt(countSql, new Object[] {psnId});
    if (count == null) {
      return 0;
    }
    return count;
  }


}
