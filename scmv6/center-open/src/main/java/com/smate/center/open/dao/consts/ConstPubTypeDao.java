package com.smate.center.open.dao.consts;



import java.util.List;
import java.util.Locale;

import org.hibernate.Query;
import org.springframework.stereotype.Repository;

import com.smate.center.open.model.consts.ConstPubType;
import com.smate.core.base.utils.constant.DBSessionEnum;
import com.smate.core.base.utils.data.HibernateDao;



/**
 * @author yamingd 成果类型常数Dao.
 */
@Repository("constPubTypeDao")
public class ConstPubTypeDao extends HibernateDao<ConstPubType, Integer> {

  @Override
  public DBSessionEnum getDbSession() {
    return DBSessionEnum.SNS;
  }

  /**
   * 读取所有Enabled=1的类别.
   * 
   * @return List<PublicationType>
   * @throws DaoException DaoException
   */
  @SuppressWarnings("unchecked")
  public List<ConstPubType> getAllTypes(Locale locale) throws Exception {

    Query query = super.createQuery("from ConstPubType t where enabled=1 order by   t.seqNo");

    query.setCacheable(true);
    List<ConstPubType> list = query.list();

    // 赋予正确的值给name属性
    if (list != null && list.size() > 0) {
      for (ConstPubType cr : list) {

        if ("zh".equals(locale.getLanguage())) {
          cr.setName(cr.getZhName());
        } else {
          cr.setName(cr.getEnName());
        }
      }
    }
    return query.list();

  }

  public ConstPubType get(int id, Locale locale) throws Exception {

    ConstPubType constPubType = super.get(id);

    if ("zh".equals(locale.getLanguage())) {
      constPubType.setName(constPubType.getZhName());
    } else {
      constPubType.setName(constPubType.getEnName());
    }
    return constPubType;
  }



}

