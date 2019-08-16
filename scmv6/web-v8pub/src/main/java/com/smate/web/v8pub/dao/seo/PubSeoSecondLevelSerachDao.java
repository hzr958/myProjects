package com.smate.web.v8pub.dao.seo;

import java.util.List;

import org.hibernate.Query;
import org.springframework.stereotype.Repository;

import com.smate.core.base.utils.data.SnsHibernateDao;
import com.smate.web.v8pub.po.seo.PubIndexSecondLevel;

/**
 * 首页论文seo Dao服务
 * 
 * @author tsz
 * 
 */

@Repository
public class PubSeoSecondLevelSerachDao extends SnsHibernateDao<PubIndexSecondLevel, Long> {



  public List<String> getSecondLevelList(String code, Integer secondGroup) {
    if ("other".equals(code)) {
      code = "0";
    }
    String hql =
        "SELECT t.secondLabel from PubIndexSecondLevel t where t.firstGroup=? and t.secondGroup=? order by  t.thirdGroup";
    // 记录数
    // 查询数据实体
    Query queryResult = super.createQuery(hql, code, secondGroup);
    return queryResult.list();
  }

}
