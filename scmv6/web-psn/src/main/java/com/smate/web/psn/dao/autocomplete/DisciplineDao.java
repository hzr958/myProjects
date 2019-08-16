package com.smate.web.psn.dao.autocomplete;

import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.hibernate.transform.Transformers;
import org.springframework.stereotype.Repository;

import com.smate.core.base.utils.data.SnsHibernateDao;
import com.smate.web.psn.exception.DaoException;
import com.smate.web.psn.model.autocomplete.Discipline;

@Repository
public class DisciplineDao extends SnsHibernateDao<Discipline, Long> {
  /**
   * 根据输入的信息获取研究领域
   * 
   * @param startWith
   * @param maxSize
   * @return
   * @throws DaoException
   */
  public List<Map<Long, String>> getDisciplineLists(String startWith, int maxSize) throws Exception {
    boolean isEnglish = StringUtils.isAsciiPrintable(startWith);
    String hql1 = "";
    String hql2 = "";
    if (isEnglish) {
      startWith = startWith.trim().toUpperCase() + "%";
      hql1 =
          "select distinct topCategoryId as code,topCategoryEnName as name from Discipline where topCategoryEnName like :startWith";
      hql2 =
          "select distinct secondCategoryId as code,secondCategoryEnName as name from Discipline where secondCategoryEnName like :startWith";

    } else {
      startWith = startWith.trim() + "%";
      hql1 =
          "select distinct topCategoryId as code,topCategoryZhName as name from Discipline where topCategoryZhName like :startWith";
      hql2 =
          "select distinct secondCategoryId as code,secondCategoryZhName as name from Discipline where secondCategoryZhName like :startWith";
    }

    List<Map<Long, String>> topCategoryList = super.createQuery(hql1).setParameter("startWith", startWith)
        .setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP).setMaxResults(maxSize).list();
    List<Map<Long, String>> SecondCategoryList = super.createQuery(hql2).setParameter("startWith", startWith)
        .setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP).setMaxResults(maxSize).list();
    if (topCategoryList != null && topCategoryList.size() > 0) {
      if (SecondCategoryList != null && SecondCategoryList.size() > 0) {
        topCategoryList.addAll(SecondCategoryList);
      }
      return topCategoryList;
    } else {
      return SecondCategoryList;
    }
  }

}

