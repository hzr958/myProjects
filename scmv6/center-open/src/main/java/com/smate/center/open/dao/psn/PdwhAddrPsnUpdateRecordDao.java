package com.smate.center.open.dao.psn;

import org.springframework.stereotype.Repository;

import com.smate.center.open.model.psn.PdwhAddrPsnUpdateRecord;
import com.smate.core.base.utils.data.PdwhHibernateDao;

@Repository
public class PdwhAddrPsnUpdateRecordDao extends PdwhHibernateDao<PdwhAddrPsnUpdateRecord, Long> {

  /**
   * 删除人员更新记录
   * 
   * @param psnId
   * @author LIJUN
   * @date 2018年4月4日
   */
  public void deleteRecordByPsnId(Long psnId) {

    String hql = "delete PdwhAddrPsnUpdateRecord where type=2 and taskId=:psnId";
    super.createQuery(hql).setParameter("psnId", psnId).executeUpdate();
  }

}
