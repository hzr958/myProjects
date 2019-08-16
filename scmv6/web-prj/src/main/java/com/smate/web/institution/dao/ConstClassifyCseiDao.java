package com.smate.web.institution.dao;

import com.smate.core.base.utils.data.SnsHibernateDao;
import com.smate.web.institution.model.ConstClassifyCsei;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 新兴产业代码
 * 
 * @author ajb
 * 
 */
@Repository(value = "constClassifyCseiDao")
public class ConstClassifyCseiDao extends SnsHibernateDao<ConstClassifyCsei, Long> {

  /**
   * 获取一级的 常量
   * @return
   */
  public  List<ConstClassifyCsei> getFirstLev(){
    String hql =  "from   ConstClassifyCsei t where t.superCode  is null order by t.code ";
    return this.createQuery(hql).list();
  }

  /**
   * 获取一级的 常量
   * @return
   */
  public  List<ConstClassifyCsei> getSecondLev(String superCode){
    String hql =  "from   ConstClassifyCsei t where t.superCode   =:superCode order by t.code ";
    return this.createQuery(hql).setParameter("superCode",superCode).list();
  }

}
