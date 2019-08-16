package com.smate.center.batch.dao.sns.pub;

import java.util.Date;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Repository;

import com.smate.center.batch.exception.pub.DaoException;
import com.smate.center.batch.model.sns.pub.AcPublisher;

/**
 * 出版社自动提示.
 * 
 * @author liqinghua
 * 
 */
@Repository
public class AcPublisherDao extends AutoCompleteDao<AcPublisher> {

  /**
   * 获取智能匹配出版社自动提示列表，返回最多size条记录.
   * 
   * @param startWith
   * @return
   * @throws Exception
   */
  public List<AcPublisher> getAcPublisher(String startWith, int size) throws DaoException {

    return super.getAcEntiy(startWith, size);
  }

  /**
   * 保存出版社自动提示.
   * 
   * @param obj
   * @throws DaoException
   */
  public void saveAcPublisher(AcPublisher obj) throws DaoException {

    super.save(obj);
  }

  /**
   * 保存出版社自动提示.
   * 
   * @param obj
   * @throws DaoException
   */
  public void saveAcPublisher(String name) throws DaoException {

    if (StringUtils.isBlank(name)) {
      return;
    }

    name = StringUtils.substring(name, 0, 200);
    // 判断是否已经存在该提示信息
    String query = super.getQuery(name);
    query = StringUtils.substring(query, 0, 50);
    if (super.isExistQuery(query)) {
      return;
    }
    AcPublisher obj = new AcPublisher();
    obj.setName(name);
    obj.setCreateAt(new Date());
    obj.setQuery(query);
    super.save(obj);
  }
}
