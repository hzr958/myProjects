package com.smate.web.v8pub.dao.pdwh;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.smate.core.base.utils.data.PdwhHibernateDao;
import com.smate.web.v8pub.po.pdwh.CategoryHightechIndustry;

/**
 * 高新产业分类
 * 
 * @author YJ
 *
 *         2019年5月23日
 */

@Repository
public class CategoryHightechIndustryDAO extends PdwhHibernateDao<CategoryHightechIndustry, String> {

  /**
   * 查询行业一级分类
   * 
   * @return
   */
  @SuppressWarnings("unchecked")
  public List<CategoryHightechIndustry> findAllIndustry() {
    String hql =
        "SELECT new CategoryHightechIndustry(t.code,t.name,t.ename,t.parentCode,t.codeLevel,(SELECT count(1) FROM CategoryHightechIndustry t1 where t1.parentCode = t.code ) as isEnd) FROM CategoryHightechIndustry t";
    return this.createQuery(hql).list();
  }

  /**
   * 根据code获取行业信息
   * 
   * @param codeArray
   * @return
   */
  @SuppressWarnings("unchecked")
  public List<CategoryHightechIndustry> findIndustryInfo(String[] codeArray) {
    if (codeArray == null || codeArray.length == 0) {
      return null;
    }
    String hql = "from CategoryHightechIndustry t where t.code in(:codes)";
    return this.createQuery(hql).setParameterList("codes", codeArray).list();
  }

}
