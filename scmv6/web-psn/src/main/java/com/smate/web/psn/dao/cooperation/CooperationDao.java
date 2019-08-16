package com.smate.web.psn.dao.cooperation;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Repository;

import com.smate.core.base.utils.data.SnsHibernateDao;
import com.smate.core.base.utils.model.security.Person;
import com.smate.web.psn.model.cooperation.PsnKnowCopartner;

/**
 * 
 * 查询合作者
 * 
 * @author zx
 *
 */
@Repository
public class CooperationDao extends SnsHibernateDao<PsnKnowCopartner, Long> {
  private final static String COPARTNER_STRANGER = "stranger";
  private final static String COPARTNER_FRIEND = "friend";



  /**
   * 根据人名查询在合作者或好友中的人
   *
   * @param page
   * @param psnId
   * @return
   */
  @SuppressWarnings("unchecked")
  public List<Person> searchPsnCooperator(String searchKey, List<Long> excludePsnIds, Long psnId, Integer size) {
    StringBuilder hql = new StringBuilder();
    // 查询条件
    List<Object> params = new ArrayList<Object>();
    hql.append("from Person t where  (instr(upper(t.name),?) > 0  or instr(upper(t.ename),?) > 0) and (exists("
        + "select 1 from PsnKnowCopartner a where a.cptPsnId=t.personId and a.psnId=" + psnId + ") or exists("
        + "select 1 from Friend b where b.friendPsnId = t.personId and b.psnId=" + psnId + "))");
    params.add(StringUtils.upperCase(searchKey));
    params.add(StringUtils.upperCase(searchKey));
    // 排除成果id的判断
    if (excludePsnIds != null && excludePsnIds.size() > 0) {
      String psnIds = "";
      for (Long p : excludePsnIds) {
        psnIds = psnIds + p + ",";
      }
      psnIds = psnIds.substring(0, psnIds.length() - 1);
      hql.append("  and t.personId not in ( " + psnIds + " ) ");
    }
    hql.append(" order by t.personId desc");
    return super.createQuery(hql.toString(), params.toArray()).setMaxResults(size).list();
  }

}
