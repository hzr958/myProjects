package com.smate.web.v8pub.dao.autocomplete;

import java.util.Date;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Repository;
import com.smate.core.base.psn.dao.autocomplete.AutoCompleteDao;
import com.smate.web.v8pub.po.autocomplete.AcAwardIssueIns;

/**
 * 颁奖机构.
 * 
 */
@Repository
public class AcAwardIssueInsDao extends AutoCompleteDao<AcAwardIssueIns> {

  /**
   * 获取智能匹配颁奖机构列表，返回最多length条记录.
   * 
   * @param startWith
   * @return
   * @throws Exception
   */
  public List<AcAwardIssueIns> getAcAwardIssueIns(String startWith, int size) {

    return super.getAcEntiy(startWith, size);
  }

  /**
   * 保存颁奖机构自动提示.
   * 
   * @param obj
   * @throws DaoException
   */
  public void saveAcAwardIssueIns(AcAwardIssueIns obj) {

    super.save(obj);
  }

  /**
   * 保存颁奖机构自动提示.
   * 
   * @param name
   * @throws DaoException
   */
  public void saveAcAwardIssueIns(String name) {

    if (StringUtils.isBlank(name)) {
      return;
    }
    name = StringUtils.substring(name, 0, 100);
    // 判断是否已经存在该提示信息
    String query = super.getQuery(name);
    query = StringUtils.substring(query, 0, 100);
    if (super.isExistQuery(query)) {
      return;
    }

    AcAwardIssueIns obj = new AcAwardIssueIns();
    obj.setName(name);
    obj.setCreateAt(new Date());
    obj.setQuery(query);
    super.save(obj);
  }

  @SuppressWarnings("rawtypes")
  public AcAwardIssueIns getByName(String name) {
    if (StringUtils.isBlank(name)) {
      return null;
    }
    name = StringUtils.substring(name, 0, 100);
    // 判断是否已经存在该提示信息
    String query = super.getQuery(name);
    query = StringUtils.substring(query, 0, 100);
    String hql = "from AcAwardIssueIns t where t.query =:query";
    List list = this.createQuery(hql).setParameter("query", query).list();
    if (list != null && list.size() > 0) {
      return (AcAwardIssueIns) list.get(0);
    }
    return null;
  }
}
