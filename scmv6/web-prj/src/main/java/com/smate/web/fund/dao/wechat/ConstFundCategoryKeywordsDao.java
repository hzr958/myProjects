package com.smate.web.fund.dao.wechat;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.smate.core.base.utils.data.RcmdHibernateDao;
import com.smate.web.fund.model.common.ConstFundCategoryKeywords;


@Repository
public class ConstFundCategoryKeywordsDao extends RcmdHibernateDao<ConstFundCategoryKeywords, Long> {

  // 根据关键词匹配对应的基金id
  @SuppressWarnings("unchecked")
  public List<Long> findFundIdBySearchKey(String searchKey) {
    String hql =
        "select distinct t.categoryId from ConstFundCategoryKeywords t where instr(lower(t.keyword),lower(:searchKey))>0";
    return super.createQuery(hql).setString("searchKey", searchKey).list();
  }
}
