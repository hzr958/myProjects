package com.smate.web.psn.dao.pub;

import java.util.ArrayList;
import java.util.List;

import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;

import com.smate.core.base.utils.data.SnsHibernateDao;
import com.smate.core.base.utils.exception.SysServiceException;
import com.smate.web.psn.model.pub.InsRefDb;


/**
 * 
 * @author fanzhiqiang
 * 
 */
@Repository
public class InsRefDbDao extends SnsHibernateDao<InsRefDb, Long> {

  /**
   * 获取指定单位可用的文献库.
   * 
   * @param insId指定单位id
   * 
   * @return 返回可用单位列表
   */
  public List<InsRefDb> getDbByIns(List<Long> insIdList) throws SysServiceException {
    List<Criterion> criterionList = new ArrayList<Criterion>();
    criterionList.add(Restrictions.in("insRefDbId.insId", insIdList.toArray()));
    // criterionList.add(Restrictions.eq("enabled", 1));
    return find(criterionList.toArray(new Criterion[criterionList.size()]));
  }

}
