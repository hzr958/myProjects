package com.smate.web.psn.dao.autocomplete;

import java.util.Date;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.hibernate.Query;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Repository;

import com.smate.web.psn.exception.DaoException;
import com.smate.web.psn.model.autocomplete.AcAwardGrade;


/**
 * 奖励等级.
 * 
 * @author liqinghua
 * 
 */
@Repository
public class AcAwardGradeDao extends AutoCompleteDao<AcAwardGrade> {

  /**
   * 获取智能匹配奖励等级列表，返回最多size条记录.
   * 
   * @param 需要检索的字符串
   * @return 检索结果列表
   * @throws Exception
   */
  @SuppressWarnings("unchecked")
  public List<AcAwardGrade> getAcAwardGrade(String startStr, int size) throws DaoException {
    if (StringUtils.isBlank(startStr)) {
      return super.createQuery("from AcAwardGrade t order by t.seqNo ").setMaxResults(size).list();
    } else {
      String hql = "";
      startStr = this.getQuery(startStr);
      if ("en_US".equals(LocaleContextHolder.getLocale().toString())) {
        hql = "from AcAwardGrade t where lower(t.queryEn) like ? order by t.queryEn ";
      } else {
        hql = "from AcAwardGrade t where t.query like ? order by t.query";
      }
      Query query = super.createQuery(hql, "%" + startStr + "%");
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
  public void saveAcAwardGrade(AcAwardGrade obj) throws DaoException {

    super.save(obj);
  }

  /**
   * 保存奖励类别自动提示.
   * 
   * @param name
   * @throws DaoException
   */
  public void saveAcAwardGrade(String name) throws DaoException {
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

    AcAwardGrade obj = new AcAwardGrade();
    obj.setName(name);
    obj.setCreateAt(new Date());
    obj.setQuery(query);
    super.save(obj);
  }
}
