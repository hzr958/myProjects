package com.smate.center.batch.dao.sns.pub;

import java.util.Date;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Repository;

import com.smate.center.batch.exception.pub.DaoException;
import com.smate.center.batch.model.sns.pub.AcConfOrganizer;

/**
 * 会议组织者自动提示.
 * 
 * @author liqinghua
 * 
 */
@Repository(value = "acConfOrganizerDao")
public class AcConfOrganizerDao extends AutoCompleteDao<AcConfOrganizer> {

  /**
   * 获取智能匹配会议组织者自动提示列表，返回最多size条记录.
   * 
   * @param startWith
   * @return
   * @throws Exception
   */
  public List<AcConfOrganizer> getAcConfOrganizer(String startWith, int size) throws DaoException {

    return super.getAcEntiy(startWith, size);
  }

  /**
   * 保存会议组织者自动提示.
   * 
   * @param obj
   * @throws DaoException
   */
  public void saveAcConfOrganizer(AcConfOrganizer obj) throws DaoException {

    super.save(obj);
  }

  /**
   * 保存会议组织者自动提示.
   * 
   * @param name
   * @throws DaoException
   */
  public void saveAcConfOrganizer(String name) throws DaoException {
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
    AcConfOrganizer obj = new AcConfOrganizer();
    obj.setName(name);
    obj.setCreateAt(new Date());
    obj.setQuery(query);
    super.save(obj);
  }
}
