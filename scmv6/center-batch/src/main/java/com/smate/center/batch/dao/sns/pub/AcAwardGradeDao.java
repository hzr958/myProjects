package com.smate.center.batch.dao.sns.pub;

import java.util.Date;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Repository;

import com.smate.center.batch.exception.pub.DaoException;
import com.smate.center.batch.model.sns.pub.AcAwardGrade;

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
  public List<AcAwardGrade> getAcAwardGrade(String startStr, int size) throws DaoException {

    return super.getAcEntiy(startStr, size);
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
