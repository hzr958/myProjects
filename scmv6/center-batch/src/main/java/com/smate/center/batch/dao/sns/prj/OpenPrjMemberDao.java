package com.smate.center.batch.dao.sns.prj;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.smate.center.batch.model.sns.prj.OpenPrjMember;
import com.smate.core.base.utils.data.SnsHibernateDao;

/**
 * 第三方项目成员DAO
 * 
 * @author LXZ
 * 
 * @since 6.0.1
 * @version 6.0.1
 * @return
 */
@Repository
public class OpenPrjMemberDao extends SnsHibernateDao<OpenPrjMember, Long> {

  /**
   * 根据Open项目id获取对应成员集合
   * 
   * @author LXZ
   * 
   * @since 6.0.1
   * @version 6.0.1
   * @param id
   * @return
   */
  public List<OpenPrjMember> getMembersByPrjId(Long id) {
    String hql = "from OpenPrjMember where openPrjId=? order by seqNo asc ";
    return super.createQuery(hql, id).list();
  }

}
