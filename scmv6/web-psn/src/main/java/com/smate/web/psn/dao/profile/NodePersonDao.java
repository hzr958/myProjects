package com.smate.web.psn.dao.profile;

import org.springframework.stereotype.Repository;

import com.smate.core.base.psn.model.NodePerson;
import com.smate.core.base.utils.data.SnsHibernateDao;

/**
 * 人员基本信息数据接口.
 * 
 * @author cwli
 * 
 */
@Repository
public class NodePersonDao extends SnsHibernateDao<NodePerson, Long> {

  /**
   * 获取人员地区信息
   * 
   * @param psnId
   * @return
   */
  public Long getPsnRegionId(Long psnId) {
    String hql = "select regionId from NodePerson where psnId=?";
    return findUnique(hql, psnId);
  }
}
