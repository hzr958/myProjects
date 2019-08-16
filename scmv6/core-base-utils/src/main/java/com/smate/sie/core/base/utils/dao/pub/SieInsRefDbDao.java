package com.smate.sie.core.base.utils.dao.pub;

import java.util.ArrayList;
import java.util.List;

import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;

import com.smate.core.base.utils.data.SieHibernateDao;
import com.smate.core.base.utils.exception.SysServiceException;
import com.smate.sie.core.base.utils.model.pub.SieInsRefDb;

/**
 * 
 * @author jszhou
 *
 */
@Repository
public class SieInsRefDbDao extends SieHibernateDao<SieInsRefDb, Long> {

  /**
   * 获取指定单位可用的文献库.
   * 
   * @param insId指定单位id
   * 
   * @return 返回可用单位列表
   */
  public List<SieInsRefDb> getDbByIns(List<Long> insIdList) throws SysServiceException {
    List<Criterion> criterionList = new ArrayList<Criterion>();
    criterionList.add(Restrictions.in("insRefDbId.insId", insIdList.toArray()));
    return find(criterionList.toArray(new Criterion[criterionList.size()]));
  }

  public boolean deleteByInsId(Long insid) {
    boolean flag = true;
    String sql = "delete from SieInsRefDb where insRefDbId.insId = ? ";
    int i = super.createQuery(sql, insid).executeUpdate();
    if (i < 1) {
      flag = false;
    }
    return flag;
  }

}
