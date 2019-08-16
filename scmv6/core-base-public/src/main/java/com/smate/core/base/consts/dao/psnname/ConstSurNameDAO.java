package com.smate.core.base.consts.dao.psnname;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.smate.core.base.consts.model.psnname.ConstSurNameDTO;
import com.smate.core.base.utils.data.SnsHibernateDao;


/**
 * 复姓DAO.
 * 
 * @author zjh
 * 
 */
@Repository
public class ConstSurNameDAO extends SnsHibernateDao<ConstSurNameDTO, Long> {
  /**
   * 判断常量类别是否已经存在.
   * 
   * @param id
   * @return
   * @throws DaoException
   */

  @SuppressWarnings("unchecked")
  public List<ConstSurNameDTO> findAllSurName() {
    String hql = "from ConstSurNameDTO t order by t.name";

    return super.createQuery(hql).list();
  }

}
