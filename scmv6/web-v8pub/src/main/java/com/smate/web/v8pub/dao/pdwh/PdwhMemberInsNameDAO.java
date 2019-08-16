package com.smate.web.v8pub.dao.pdwh;

import org.springframework.stereotype.Repository;

import com.smate.core.base.utils.data.PdwhHibernateDao;
import com.smate.web.v8pub.po.pdwh.PdwhMemberInsNamePO;

@Repository
public class PdwhMemberInsNameDAO extends PdwhHibernateDao<PdwhMemberInsNamePO, Long> {

  /**
   * 删除指定成果id的成果作者单位信息
   * 
   * @param pdwhPubId
   */
  public void deleteAll(Long pdwhPubId) {
    String hql =
        "delete from PdwhMemberInsNamePO t where exists(select 1 from PdwhPubMemberPO p where p.id = t.memberId and p.pdwhPubId=?) ";
    this.createQuery(hql, new Object[] {pdwhPubId}).executeUpdate();
  }

}
