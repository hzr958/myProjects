package com.smate.center.open.dao.prj;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.smate.center.open.model.prj.ThirdPrjInfo;
import com.smate.core.base.utils.data.SnsHibernateDao;

@Repository
public class ThirdPrjInfoDao extends SnsHibernateDao<ThirdPrjInfo, Long> {

  public ThirdPrjInfo getThirdPrjInfoByGroupCode(String groupCode) {
    String hql = "from  ThirdPrjInfo  t where t.groupCode=:groupCode";

    List<ThirdPrjInfo> list = this.createQuery(hql).setParameter("groupCode", groupCode).list();
    if (list != null && list.size() > 0) {
      return (ThirdPrjInfo) list.get(0);
    }
    return null;
  }

}
