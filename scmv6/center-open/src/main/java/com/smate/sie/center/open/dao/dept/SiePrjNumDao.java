package com.smate.sie.center.open.dao.dept;


import org.springframework.stereotype.Repository;

import com.smate.core.base.utils.data.RolHibernateDao;
import com.smate.sie.center.open.model.dept.InsProject;

@Repository
public class SiePrjNumDao extends RolHibernateDao<InsProject, Long> {


  public Long getNums(Long insId) {
    // 单位项目数
    Long projectNum = super.findUnique("select count(*) from InsProject p where p.insId= ? ", insId);

    return projectNum;

  }
}
