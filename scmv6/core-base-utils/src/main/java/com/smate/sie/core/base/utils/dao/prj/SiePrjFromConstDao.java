package com.smate.sie.core.base.utils.dao.prj;

import org.springframework.stereotype.Repository;

import com.smate.core.base.utils.data.SieHibernateDao;
import com.smate.sie.core.base.utils.model.prj.SiePrjFromConst;

/**
 * 
 * @author yxs
 * @descript 项目来源dao
 */
@Repository
public class SiePrjFromConstDao extends SieHibernateDao<SiePrjFromConst, Long> {

  public SiePrjFromConst getByName(String fromName) {
    return (SiePrjFromConst) super.createQuery("from PrjFromConst t where lower(t.zhName) = ? or lower(t.enName)=?",
        new Object[] {fromName, fromName}).uniqueResult();
  }

}
