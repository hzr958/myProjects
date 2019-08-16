package com.smate.center.task.dao.tmp;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.smate.center.task.model.fund.rcmd.ConstFundAgency;
import com.smate.core.base.utils.data.RcmdHibernateDao;

@Repository
public class FundAgencyAddrDao extends RcmdHibernateDao<ConstFundAgency, Long> {

  /**
   * 查询地区,所有的ID
   */
  @SuppressWarnings("unchecked")
  public List<ConstFundAgency> batchGetData() {
    String hql = "from ConstFundAgency";
    return super.createQuery(hql).list();
  }

  /**
   * 更新地区 地址字段
   */
  public void updateAddress(String address, String enAddress, Long id) {
    String hql = "update ConstFundAgency set address =:address,enAddress =:enAddress where id=:id";
    super.createQuery(hql).setParameter("address", address).setParameter("enAddress", enAddress).setParameter("id", id)
        .executeUpdate();
  }

}
