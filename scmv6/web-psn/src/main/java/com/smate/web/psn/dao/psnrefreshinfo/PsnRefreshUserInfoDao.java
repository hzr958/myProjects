package com.smate.web.psn.dao.psnrefreshinfo;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.smate.core.base.utils.data.SnsHibernateDao;
import com.smate.web.psn.model.psninfo.PsnRefreshUserInfo;

/**
 * 人员信息冗余刷新.
 * 
 * @author liqinghua
 * 
 */
@Repository
public class PsnRefreshUserInfoDao extends SnsHibernateDao<PsnRefreshUserInfo, Long> {

  /**
   * 加载需要刷新的人员列表.
   * 
   * @return
   */
  @SuppressWarnings("unchecked")
  public List<PsnRefreshUserInfo> loadRefreshPsn() {

    String hql = "from PsnRefreshUserInfo t where t.status = 0 ";
    return super.createQuery(hql).setMaxResults(10).list();
  }

  /**
   * 删除人员刷新纪录.
   * 
   * @param psnId
   */
  public void delRefreshPsn(Long psnId) {

    String hql = "delete from PsnRefreshUserInfo t where t.psnId = ? ";
    super.createQuery(hql, psnId).executeUpdate();
  }

  /**
   * 标记刷新人员信息失败.
   * 
   * @param psnId
   */
  public void mkRefreshPsnError(Long psnId) {

    String hql = "update PsnRefreshUserInfo t set t.status = 9 where t.psnId = ? ";
    super.createQuery(hql, psnId).executeUpdate();
  }

  /**
   * 判断name中是否有多音字
   * 
   * @param name
   * @return
   */
  public boolean isDuoyz(String name) {
    String sql = "select count(t.id) from CONST_DUOYZ t where t.zi in(:vals)";

    long count = Long.valueOf(super.getSession().createSQLQuery(sql)
        .setParameterList("vals", new Object[] {name.toCharArray()}).uniqueResult().toString());

    if (count > 0) {
      return true;
    }
    return false;
  }

  public List<String> findSyncPubJournal(Long psnId) {
    String sql =
        "select distinct t.issn from pub_journal t where exists (select 1 from pub_owner_match t1 where t.psn_id = ? and t.pub_id = t1.pub_id and t1.au_seq > 0) AND t.issn is not null";

    List<Map<String, Object>> temp = super.queryForList(sql, new Object[] {psnId});
    List<String> list = new ArrayList<String>();
    if (temp != null && temp.size() > 0) {
      for (Map<String, Object> map : temp) {
        list.add((String) map.get("ISSN"));
      }
    }
    return list;
  }
}
