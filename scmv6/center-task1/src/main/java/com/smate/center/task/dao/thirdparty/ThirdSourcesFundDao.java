package com.smate.center.task.dao.thirdparty;


import com.smate.center.task.model.thirdparty.ThirdSourcesFund;
import com.smate.core.base.utils.data.SnsHibernateDao;
import org.springframework.stereotype.Repository;



/**
 * 第三方，基金机会Dao
 */
@Repository
public class ThirdSourcesFundDao extends SnsHibernateDao<ThirdSourcesFund, Long> {

  /**
   * 创建主键Id
   *
   * @return Long
   */
  public Long createId() {
    String sql = "select SEQ_v_third_sources_fund.nextval from dual";
    return super.queryForLong(sql);
  }

  public ThirdSourcesFund find(String fundNumber , String fundingAgency){
    String hql ="from ThirdSourcesFund t where t.fundNumber=:fundNumber and t.fundingAgency=:fundingAgency";
    return (ThirdSourcesFund)this.createQuery(hql).setParameter("fundingAgency",fundingAgency).
        setParameter("fundNumber",fundNumber).uniqueResult();
  }

}
