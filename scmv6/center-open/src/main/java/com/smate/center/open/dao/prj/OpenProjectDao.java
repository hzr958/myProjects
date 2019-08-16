package com.smate.center.open.dao.prj;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.smate.center.open.model.prj.OpenProject;
import com.smate.core.base.utils.constant.DBSessionEnum;
import com.smate.core.base.utils.data.HibernateDao;

/**
 * 第三方项目DAO
 * 
 * @author LXZ
 * 
 * @since 6.0.1
 * @version 6.0.1
 * @return
 */
@Repository
public class OpenProjectDao extends HibernateDao<OpenProject, Long> {

  @Override
  public DBSessionEnum getDbSession() {
    return DBSessionEnum.SNS;
  }

  public List<OpenProject> queryByObjId(String objId, String token) {
    String hql = "from OpenProject where (taskStatus=2 or taskStatus=0) and objId=? and token=? ";
    List<OpenProject> list = super.createQuery(hql, objId, token).list();
    return list;
  }

}
