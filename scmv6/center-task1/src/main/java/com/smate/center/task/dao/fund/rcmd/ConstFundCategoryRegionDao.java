package com.smate.center.task.dao.fund.rcmd;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.smate.center.task.model.fund.rcmd.ConstFundCategoryRegion;
import com.smate.core.base.utils.data.RcmdHibernateDao;

/**
 * @author cwli
 * 
 */
@Repository
public class ConstFundCategoryRegionDao extends RcmdHibernateDao<ConstFundCategoryRegion, Long> {

  public void deleteFundRegionByCategoryId(Long categoryId) {
    String hql = "delete from ConstFundCategoryRegion t where t.categoryId=?";
    super.createQuery(hql, categoryId).executeUpdate();
  }

  public List<ConstFundCategoryRegion> findFundRegionByCategoryId(Long categoryId) {
    String hql = "from ConstFundCategoryRegion t where t.categoryId=?";
    return find(hql, categoryId);
  }

  public List<ConstFundCategoryRegion> getRegionLeft(Long insId, Long psnId) {
    String hql =
        "select distinct new ConstFundCategoryRegion(t.regId,t.viewName) from ConstFundCategoryRegion t,ConstFundCategory t1,InsFundSearch t2 where t.categoryId=t1.id and t1.id=t2.categoryId and t2.insId=? and t2.psnId=? and t.regId<>0 order by nlssort(t.viewName,'NLS_SORT=SCHINESE_PINYIN_M')";
    return super.find(hql, insId, psnId);
  }

  public List<Long> findFundRegionId(Long categoryId) {
    String hql = "select t.regId from ConstFundCategoryRegion t where t.categoryId=?";
    return find(hql, categoryId);
  }
}
