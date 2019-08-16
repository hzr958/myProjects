package com.smate.center.open.dao.prj;

import org.springframework.stereotype.Repository;

import com.smate.center.open.model.prj.OpenPrjMember;
import com.smate.core.base.utils.constant.DBSessionEnum;
import com.smate.core.base.utils.data.HibernateDao;

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
public class OpenPrjMemberDao extends HibernateDao<OpenPrjMember, Long> {
  @Override
  public DBSessionEnum getDbSession() {
    return DBSessionEnum.SNS;
  }
}
