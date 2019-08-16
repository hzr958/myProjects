package com.smate.center.batch.dao.pdwh.psn;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.smate.center.batch.model.pdwh.psn.PsnPmZhName;
import com.smate.core.base.utils.data.PdwhHibernateDao;

/**
 * 成果匹配-用户中文姓名表数据库操作类.
 * 
 * @author mjg
 * 
 */
@Repository
public class PsnPmZhNameDao extends PdwhHibernateDao<PsnPmZhName, Long> {

  /**
   * 获取用户的合作者中文名列表.
   * 
   * @param psnId
   * @return
   */
  public List<PsnPmZhName> getPsnPmZhNameList(Long psnId) {
    String hql = "from PsnPmZhName t where t.psnId=? ";
    return super.find(hql, psnId);
  }
}
