package com.smate.web.psn.dao.autocomplete;

import java.util.Date;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Repository;

import com.smate.web.psn.exception.DaoException;
import com.smate.web.psn.model.autocomplete.AcPatentOrg;

/**
 * 发证单位自动提示dao.
 * 
 * @author liqinghua
 * 
 */
@Repository
public class AcPatentOrgDao extends AutoCompleteDao<AcPatentOrg> {

  /**
   * 获取智能匹配发证单位自动提示列表，返回最多size条记录.
   * 
   * @param startWith
   * @return
   * @throws Exception
   */
  public List<AcPatentOrg> getAcPatentOrg(String startWith, int size) throws DaoException {

    return super.getAcEntiy(startWith, size);
  }

  /**
   * 保存发证单位自动提示.
   * 
   * @param obj
   * @throws DaoException
   */
  public void saveAcPatentOrg(AcPatentOrg obj) throws DaoException {

    super.save(obj);
  }

  /**
   * 保存发证单位自动提示.
   * 
   * @param name
   * @throws DaoException
   */
  public void saveAcPatentOrg(String name) throws DaoException {

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
    AcPatentOrg obj = new AcPatentOrg();
    obj.setName(name);
    obj.setCreateAt(new Date());
    obj.setQuery(query);
    super.save(obj);
  }
}
