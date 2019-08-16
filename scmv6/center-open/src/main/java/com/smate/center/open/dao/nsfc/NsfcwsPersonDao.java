package com.smate.center.open.dao.nsfc;



import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Repository;

import com.smate.center.open.model.nsfc.NsfcwsPerson;
import com.smate.core.base.utils.constant.DBSessionEnum;
import com.smate.core.base.utils.data.HibernateDao;



/**
 * @author ajb
 * 
 */
@Repository
public class NsfcwsPersonDao extends HibernateDao<NsfcwsPerson, Long> {
  @Override
  public DBSessionEnum getDbSession() {
    return DBSessionEnum.SNS;
  }

  /**
   * 通过邮箱查找人员.
   * 
   * @param email
   * @return
   * @throws DaoException
   */
  @SuppressWarnings("unchecked")
  public List<NsfcwsPerson> queryNsfcwsPerson(String psnName, String inputInsName, String email) throws Exception {
    StringBuffer hql = new StringBuffer("from NsfcwsPerson t where");
    List<String> params = new ArrayList<String>();
    if (StringUtils.isNotBlank(psnName) && StringUtils.isNotBlank(inputInsName) && StringUtils.isNotBlank(email)) {
      hql.append(" (t.name=? and t.insName like ?) or t.email = ? ");
      params.add(psnName);
      params.add(inputInsName);
      params.add(email);
    } else if (StringUtils.isNotBlank(psnName) && StringUtils.isNotBlank(inputInsName)) {
      params.add(inputInsName);
      params.add(psnName);
      hql.append(" t.name=? and t.insName like ?");
    } else if (StringUtils.isNotBlank(email)) {
      params.add(email);
      hql.append(" t.email = ? ");
    }
    hql.append(" order by t.id desc");
    return super.createQuery(hql.toString(), params.toArray()).list();
  }

}
