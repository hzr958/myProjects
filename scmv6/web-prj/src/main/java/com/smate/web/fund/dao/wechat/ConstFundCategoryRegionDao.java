package com.smate.web.fund.dao.wechat;

import java.util.List;

import org.apache.commons.lang3.math.NumberUtils;
import org.springframework.stereotype.Repository;

import com.smate.core.base.utils.data.RcmdHibernateDao;
import com.smate.core.base.utils.string.ServiceUtil;
import com.smate.web.fund.model.common.ConstFundCategoryRegion;
import com.smate.web.prj.form.wechat.FundWeChatForm;

/**
 * 基金类型地区常量Dao
 * 
 * @author zk
 *
 */
@Repository
public class ConstFundCategoryRegionDao extends RcmdHibernateDao<ConstFundCategoryRegion, Long> {
  /*
   * 查询fundId对应的所在领域
   */
  @SuppressWarnings("unchecked")
  public String queryFundRegion(FundWeChatForm form) throws Exception {
    String hql = "select viewName from ConstFundCategoryRegion where  fundCategoryId=:fundCategoryId";
    List<Object> list = super.createQuery(hql)
        .setParameter("fundCategoryId", NumberUtils.toLong(ServiceUtil.decodeFromDes3(form.getDes3FundId()))).list();
    String viewName = "";
    for (int i = 0; i < list.size(); i++) {
      viewName = viewName + list.get(i);
      if (list.size() > i + 1) {
        viewName = viewName + ",";
      }
    }
    return viewName;
  }

  /**
   * 根据fundCategoryId查询地区
   * 
   * @param fundCategoryId
   * @return
   */
  @SuppressWarnings("unchecked")
  public List<ConstFundCategoryRegion> queryViewName(Long fundCategoryId) {
    String hql = "from ConstFundCategoryRegion where fundCategoryId=:fundCategoryId";
    return super.createQuery(hql).setParameter("fundCategoryId", fundCategoryId).list();
  }

  /**
   * 根据地区id获取对应基金
   * 
   * @param fundCategoryId
   * @return
   */
  @SuppressWarnings("unchecked")
  public List<Long> queryfundIdByregionIds(List<Long> regionIds) {
    String hql = "select distinct c.fundCategoryId from ConstFundCategoryRegion c where c.regId in(:regionIds)";
    return super.createQuery(hql).setParameterList("regionIds", regionIds).list();
  }
}
