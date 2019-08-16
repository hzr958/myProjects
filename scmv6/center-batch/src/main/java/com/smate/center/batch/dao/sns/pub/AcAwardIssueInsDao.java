package com.smate.center.batch.dao.sns.pub;

import java.util.Date;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Repository;

import com.smate.center.batch.exception.pub.DaoException;
import com.smate.center.batch.model.sns.pub.AcAwardIssueIns;

/**
 * 颁奖机构.
 * 
 * @author liqinghua
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
  public List<AcAwardIssueIns> getAcAwardIssueIns(String startWith, int size) throws DaoException {

    return super.getAcEntiy(startWith, size);
  }

  /**
   * 保存颁奖机构自动提示.
   * 
   * @param obj
   * @throws DaoException
   */
  public void saveAcAwardIssueIns(AcAwardIssueIns obj) throws DaoException {

    super.save(obj);
  }

  /**
   * 保存颁奖机构自动提示.
   * 
   * @param name
   * @throws DaoException
   */
  public void saveAcAwardIssueIns(String name) throws DaoException {

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
}
