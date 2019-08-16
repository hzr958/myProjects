package com.smate.web.management.dao.other.fund;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.smate.core.base.utils.constant.DBSessionEnum;
import com.smate.core.base.utils.data.HibernateDao;
import com.smate.web.management.model.other.fund.ConstFundCategoryDis;

/**
 * @author cwli
 * 
 */
@Repository
public class ConstFundCategoryDisDao extends HibernateDao<ConstFundCategoryDis, Long> {

  public void deleteFundDisByCategoryId(Long categoryId) {
    String hql = "delete from ConstFundCategoryDis t where t.categoryId=?";
    super.createQuery(hql, categoryId).executeUpdate();
  }

  public List<ConstFundCategoryDis> findFundDisByCategoryId(Long categoryId) {
    String hql = "from ConstFundCategoryDis t where t.categoryId=?";
    return find(hql, categoryId);
  }

  @SuppressWarnings("unchecked")
  public List<Long> getFundDiscByFundId(List<Long> fundIdList) {
    String hql = "select disId  from ConstFundCategoryDis where categoryId in (:fundIds)";
    return super.createQuery(hql).setParameterList("fundIds", fundIdList).list();
  }

  /**
   * 获取基金的学科代码ID.
   * 
   * @param fundId
   * @return
   */
  @SuppressWarnings("unchecked")
  public List<Long> getFundDiscIdList(Long fundId) {
    String hql = "select disId  from ConstFundCategoryDis where categoryId =? ";
    return super.createQuery(hql, fundId).list();
  }

  @Override
  public DBSessionEnum getDbSession() {
    return DBSessionEnum.RCMD;
  }
}
