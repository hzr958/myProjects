package com.smate.core.base.utils.dao.consts;

import java.util.ArrayList;
import java.util.List;

import org.hibernate.Query;
import org.springframework.stereotype.Repository;

import com.smate.core.base.utils.constant.SieConstPatType;
import com.smate.core.base.utils.data.SieHibernateDao;

/**
 * 专利类型表
 * 
 * @author hd
 *
 */
@Repository
public class SieConstPatTypeDao extends SieHibernateDao<SieConstPatType, Integer> {

  @SuppressWarnings("unchecked")
  public List<SieConstPatType> getAllTypes() {

    return super.createQuery("from SieConstPatType t where t.enable=1 order by t.seqNo ").list();
  }

  /**
   * 取得某类别的代码列表.
   * 
   * @param category
   * @return
   */
  @SuppressWarnings("unchecked")
  public SieConstPatType findPatentTypeByName(String name) {
    List<Object> params = new ArrayList<Object>();
    String hql = "from SieConstPatType t where t.zhName = ?";
    params.add(name);
    List<SieConstPatType> items = null;
    Query query = createQuery(hql, params.toArray());
    query.setCacheable(true);
    items = query.list();
    if (items != null && items.size() > 0) {
      return items.get(0);
    } else {
      return null;
    }

  }

}
