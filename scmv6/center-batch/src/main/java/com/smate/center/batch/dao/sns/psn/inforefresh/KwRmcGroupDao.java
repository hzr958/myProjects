package com.smate.center.batch.dao.sns.psn.inforefresh;

import java.util.Collection;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Repository;

import com.smate.center.batch.model.sns.psn.KwRmcGroup;
import com.smate.core.base.utils.data.SnsHibernateDao;

/**
 * @author lichangwen
 */
@Repository
public class KwRmcGroupDao extends SnsHibernateDao<KwRmcGroup, Long> {

  @SuppressWarnings("unchecked")
  public List<Long> getKwRmcGids(Collection<String> kws) {
    String hql = "select gid from KwRmcGroup where kwTxt in(:kws)";
    return super.createQuery(hql).setParameterList("kws", kws).list();
  }

  /**
   * 通知kwTxt获取分组id
   * 
   * @param kwTxt
   * @return
   */
  public Long getKwRmcGid(String kwTxt) {

    if (StringUtils.isBlank(kwTxt)) {
      return null;
    }
    String hql = "select gid from KwRmcGroup where kwTxt = ?";
    return (Long) super.createQuery(hql, kwTxt).setMaxResults(1).uniqueResult();
  }

  /**
   * 通过gid获取kwTxt
   * 
   * @param gid
   * @return
   */
  @SuppressWarnings("unchecked")
  public List<String> getkwRmcGroupKwTxt(Long gid) {

    String hql = "select kwTxt from KwRmcGroup where gid=?";
    return super.createQuery(hql, gid).list();
  }
}
