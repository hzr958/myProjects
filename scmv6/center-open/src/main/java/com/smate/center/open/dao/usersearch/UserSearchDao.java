package com.smate.center.open.dao.usersearch;

import org.springframework.stereotype.Repository;
import com.smate.center.open.model.usersearch.UserSearch;
import com.smate.core.base.consts.model.Institution;
import com.smate.core.base.utils.data.SnsHibernateDao;

@Repository
public class UserSearchDao extends SnsHibernateDao<UserSearch, Long> {

  /**
   * 更新保存用户检索表的单位记录.
   * 
   * @param psnId
   * @param ins
   */
  public void updateUserIns(Long psnId, Institution ins) {
    UserSearch userSearch = super.findUniqueBy("psnId", psnId);
    if (userSearch != null) {
      if (ins != null) {
        userSearch.setInsNameZh(ins.getZhName());
        userSearch.setInsNameEn(ins.getEnName());
        userSearch.setInsNameAbbr(ins.getAbbreviation());
        userSearch.setInsId(ins.getId());
      } else {
        userSearch.setInsNameZh(null);
        userSearch.setInsNameEn(null);
        userSearch.setInsNameAbbr(null);
        userSearch.setInsId(null);
      }
      userSearch.setIndexFlag(0);
      super.getSession().update(userSearch);
    }
  }
}
