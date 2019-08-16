package com.smate.center.merge.dao.person;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.smate.center.merge.model.sns.person.PersonEmail;
import com.smate.core.base.utils.data.SnsHibernateDao;

/**
 * 人员邮件数据接口.
 * 
 * @author zx
 */
@Repository
public class PersonEmailDao extends SnsHibernateDao<PersonEmail, Long> {

  public List<PersonEmail> findListByPersonId(Long personId) throws Exception {

    return this.find("from PersonEmail where psnId = ?", new Object[] {personId});
  }
}
