package com.smate.center.task.dao.bpo;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.smate.core.base.utils.constant.ConstDictionary;
import com.smate.core.base.utils.constant.ConstDictionaryKey;
import com.smate.core.base.utils.data.BpoHibernateDao;

/**
 * 
 * @author hd
 *
 */
@Repository
public class ConstDictionaryBpoDao extends BpoHibernateDao<ConstDictionary, ConstDictionaryKey> {

  /**
   * @param category
   * @param code
   * @return
   * @throws DaoException
   */
  public ConstDictionary findConstByCategoryAndName(String category, String name) {

    List<ConstDictionary> list =
        super.find("from ConstDictionary where key.category =  ? and zhCnName = ?", category, name);
    if (list != null && list.size() > 0) {
      return list.get(0);
    }
    return null;
  }

}
