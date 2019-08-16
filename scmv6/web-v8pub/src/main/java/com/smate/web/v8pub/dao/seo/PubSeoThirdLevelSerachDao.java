package com.smate.web.v8pub.dao.seo;

import org.hibernate.Query;
import org.springframework.stereotype.Repository;

import com.smate.core.base.utils.data.SnsHibernateDao;
import com.smate.core.base.utils.model.Page;
import com.smate.web.v8pub.po.seo.PubIndexThirdLevel;

@Repository
public class PubSeoThirdLevelSerachDao extends SnsHibernateDao<PubIndexThirdLevel, Long> {

  public void getThirdLevel2(String code, Integer secondGroup, Integer thirdGroup, Page<PubIndexThirdLevel> pages) {
    String hql =
        "from PubIndexThirdLevel t where t.firstLetter=? and t.secondGroup=? and t.thirdGroup=? order by t.orderMark,t.pubId";
    String countHql = "select count(t.pubId) ";
    // 记录数
    Long totalCount = (Long) super.createQuery(countHql + hql, code, secondGroup, thirdGroup).uniqueResult();
    pages.setTotalCount(totalCount);
    // 查询数据实体
    Query queryResult = super.createQuery(hql, code, secondGroup, thirdGroup);
    pages.setResult(queryResult.list());
  }


}
