package com.smate.web.psn.dao.fund;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

import com.smate.core.base.utils.data.RcmdHibernateDao;
import com.smate.core.base.utils.string.StringUtils;
import com.smate.web.psn.model.fund.ConstFundCategory;

/*
 * @author zjh 基金constFundCategoryDao
 */
@Repository
public class ConstFundCategoryDao extends RcmdHibernateDao<ConstFundCategory, Long> {
  protected Logger logger = LoggerFactory.getLogger(getClass());

  /**
   * 查找基金名称
   * 
   * @param fundId
   * @param locale
   * @return
   */
  public String getFundNameByLocale(Long fundId, String locale) {
    String hql = "select new ConstFundCategory(nameZh,nameEn) from ConstFundCategory t where t.id=:fundId";
    ConstFundCategory fund = (ConstFundCategory) super.createQuery(hql).setParameter("fundId", fundId).uniqueResult();
    if (fund != null) {
      return "zh_CN".equals(locale) ? StringUtils.defaultIfEmpty(fund.getNameZh(), fund.getNameEn())
          : StringUtils.defaultIfEmpty(fund.getNameEn(), fund.getNameZh());
    }
    return "";
  }

}
