package com.smate.web.psn.dao.autocomplete;

import java.util.Date;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Repository;

import com.smate.web.psn.exception.DaoException;
import com.smate.web.psn.model.autocomplete.AcThesisOrg;

/**
 * 颁发单位自动提示.
 * 
 * @author liqinghua
 * 
 */
@Repository
public class AcThesisOrgDao extends AutoCompleteDao<AcThesisOrg> {

  /**
   * 获取智能匹配颁发单位自动提示列表，返回最多size条记录.
   * 
   * @param startWith
   * @return
   * @throws Exception
   */
  public List<AcThesisOrg> getAcThesisOrg(String startWith, int size) throws DaoException {

    return super.getAcEntiy(startWith, size);
  }

  /**
   * 保存颁发单位自动提示.
   * 
   * @param obj
   * @throws DaoException
   */
  public void saveAcThesisOrg(AcThesisOrg obj) throws DaoException {

    super.save(obj);
  }

  /**
   * 保存颁发单位自动提示.
   * 
   * @param name
   * @throws DaoException
   */
  public void saveAcThesisOrg(String name) throws DaoException {

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

    AcThesisOrg obj = new AcThesisOrg();
    obj.setName(name);
    obj.setCreateAt(new Date());
    obj.setQuery(query);
    super.save(obj);
  }
}
