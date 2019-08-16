package com.smate.web.v8pub.dao.resume;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.smate.core.base.utils.data.SnsHibernateDao;
import com.smate.web.v8pub.vo.sns.newresume.PersonalResumePub;


/**
 * 我的简历与科研成果关系表Dao.
 * 
 * yanmingzhuang
 */
@Repository
public class PersonalResumePubDao extends SnsHibernateDao<PersonalResumePub, Long> {


  /**
   * 查找简历中包含的成果ID.
   * 
   * @param resumeId
   * @return
   * @throws DaoException
   */
  @SuppressWarnings("unchecked")
  public List<PersonalResumePub> findResumePubIdsByList(Long resumeId) {
    String hql = "from  PersonalResumePub t where t.resumeId=:resumeId order by seqNo";
    return super.createQuery(hql).setParameter("resumeId", resumeId).list();
  }

}
