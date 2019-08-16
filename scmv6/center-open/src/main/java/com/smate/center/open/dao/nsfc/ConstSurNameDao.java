package com.smate.center.open.dao.nsfc;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.smate.center.open.model.nsfc.ConstSurName;
import com.smate.core.base.utils.constant.DBSessionEnum;
import com.smate.core.base.utils.data.HibernateDao;


/**
 * 复姓DAO.
 * 
 * @author zjh
 * 
 */
@Repository
public class ConstSurNameDao extends HibernateDao<ConstSurName, Long> {
  /**
   * 判断常量类别是否已经存在.
   * 
   * @param id
   * @return
   * @throws DaoException
   */

  @SuppressWarnings("unchecked")
  public List<ConstSurName> findAllSurName() {
    String hql = "from ConstSurName t order by t.name";

    return super.createQuery(hql).list();
  }

  @Override
  public DBSessionEnum getDbSession() {
    // TODO Auto-generated method stub
    return DBSessionEnum.SNS;
  }

}
