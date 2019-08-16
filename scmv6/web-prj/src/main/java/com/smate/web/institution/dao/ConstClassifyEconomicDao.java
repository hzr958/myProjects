package com.smate.web.institution.dao;

import com.smate.core.base.utils.data.SnsHibernateDao;
import com.smate.web.institution.model.ConstClassifyEconomic;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 国民经济行业代码
 * 
 * @author Scy
 * 
 */
@Repository(value = "constClassifyEconomicDao")
public class ConstClassifyEconomicDao extends SnsHibernateDao<ConstClassifyEconomic, Long> {

  /**
   * 获取一级的 常量
   * @return
   */
  public  List<ConstClassifyEconomic> getFirstLev(){
    String hql =  "from   ConstClassifyEconomic t where t.superCode  is null order by t.code ";
    return this.createQuery(hql).list();
  }

  /**
   * 获取一级的 常量
   * @return
   */
  public  List<ConstClassifyEconomic> getSecondLev(String superCode){
    String hql =  "from   ConstClassifyEconomic t where t.superCode   =:superCode order by t.code ";
    return this.createQuery(hql).setParameter("superCode",superCode).list();
  }

}
