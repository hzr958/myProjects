package com.smate.web.psn.dao.autocomplete;

import java.util.Date;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.hibernate.Query;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Repository;

import com.smate.web.psn.exception.DaoException;
import com.smate.web.psn.model.autocomplete.AcAwardCategory;

/**
 * 奖励类别自动提示.
 * 
 * @author liqinghua
 * 
 */
@Repository
public class AcAwardCategoryDao extends AutoCompleteDao<AcAwardCategory> {

  /**
   * 获取智能匹配奖励类别自动提示列表，返回最多size条记录.
   * 
   * @param startWith
   * @return
   * @throws Exception
   */
  @SuppressWarnings("unchecked")
  public List<AcAwardCategory> getAcAwardCategory(String startWith, int size) throws DaoException {
    if (StringUtils.isBlank(startWith)) {
      return super.createQuery("from AcAwardCategory t order by t.seqNo ").setMaxResults(size).list();
    } else {
      String hql = "";
      startWith = this.getQuery(startWith);
      if ("en_US".equals(LocaleContextHolder.getLocale().toString())) {
        hql = "from AcAwardCategory t where lower(t.queryEn) like ? order by t.queryEn ";
      } else {
        hql = "from AcAwardCategory t where  t.query like ? order by t.query ";
      }
      Query query = super.createQuery(hql, "%" + startWith + "%");
      query.setMaxResults(size);
      return query.list();
    }
  }

  /**
   * 保存奖励类别自动提示.
   * 
   * @param obj
   * @throws DaoException
   */
  public void saveAcAwardCategory(AcAwardCategory obj) throws DaoException {

    super.save(obj);
  }

  /**
   * 保存奖励类别自动提示.
   * 
   * @param obj
   * @throws DaoException
   */
  public void saveAcAwardCategory(String name) throws DaoException {

    if (StringUtils.isBlank(name)) {
      return;
    }
    name = StringUtils.substring(name, 0, 50);

    // 判断是否已经存在该提示信息
    String query = super.getQuery(name);
    query = StringUtils.substring(query, 0, 50);
    if (super.isExistQuery(query)) {
      return;
    }
    AcAwardCategory obj = new AcAwardCategory();
    obj.setName(name);
    obj.setCreateAt(new Date());
    obj.setQuery(query);
    super.save(obj);
  }
}
