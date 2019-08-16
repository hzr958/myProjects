package com.smate.sie.center.task.dao;

import java.util.List;

import org.hibernate.Query;
import org.springframework.stereotype.Repository;

import com.smate.core.base.utils.data.SieHibernateDao;
import com.smate.sie.center.task.model.SiePdwhInsAddrConstRefresh;

/**
 * 基准库标准单位地址信息常量数据导入dao
 * 
 * @author YEXINGYUAN
 * @date 2018年7月17日
 */
@Repository
public class SiePdwhInsAddrConstRefreshDao extends SieHibernateDao<SiePdwhInsAddrConstRefresh, Long> {

  /**
   * 获取需要统计的记录
   * 
   * @param maxSize
   * @return
   */
  @SuppressWarnings("unchecked")
  public List<SiePdwhInsAddrConstRefresh> loadNeedCountUnitId(int maxSize) {
    String hql = "from SiePdwhInsAddrConstRefresh t where t.refreshStatus=0";
    Query queryResult = super.createQuery(hql);
    queryResult.setMaxResults(maxSize);
    return queryResult.list();
  }

  public void updateStatus() {
    super.createQuery("update SiePdwhInsAddrConstRefresh t set t.refreshStatus = 0").executeUpdate();
  }

  public Long countNeedCountData() {
    String hql = "select count(refreshStatus) from SiePdwhInsAddrConstRefresh where refreshStatus=0";
    return findUnique(hql);
  }

  public int updateDoneStatus(SiePdwhInsAddrConstRefresh bean) {
    return super.createQuery(
        "update SiePdwhInsAddrConstRefresh t set t.refreshStatus =1 where t.pk.insId=? and t.pk.insName=? ",
        bean.getPk().getInsId(), bean.getPk().getInsName()).executeUpdate();
  }

  public void deleteByInsId(Long mergeid) {
    String hql = "delete from SiePdwhInsAddrConstRefresh t where t.pk.insId= ? ";
    super.createQuery(hql, mergeid).executeUpdate();
  }

}
