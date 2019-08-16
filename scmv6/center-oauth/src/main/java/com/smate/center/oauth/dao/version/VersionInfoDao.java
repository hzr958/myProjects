package com.smate.center.oauth.dao.version;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.smate.center.oauth.model.mobile.version.AppVersionVo;
import com.smate.center.oauth.model.mobile.version.VersionInfoBean;
import com.smate.core.base.utils.data.SnsHibernateDao;
import com.smate.core.base.utils.model.Page;

/**
 * app版本信息DAO
 * 
 * @author wsn
 * @date Jan 3, 2019
 */
@Repository
public class VersionInfoDao extends SnsHibernateDao<VersionInfoBean, Long> {

  /**
   * 查找某类app最新版本信息
   * 
   * @param appType
   * @return
   */
  public VersionInfoBean findLastAppVersionInfoByType(String appType) {
    String hql =
        "from VersionInfoBean t where t.versionCode = (select max(v.versionCode) from VersionInfoBean v where v.appType=:appType and v.status = 1) and t.appType=:appType and t.status = 1)";
    return (VersionInfoBean) super.createQuery(hql).setParameter("appType", appType).uniqueResult();
  }

  /**
   * 返回某种app所有的版本信息
   * 
   * @param appType
   * @return
   */
  @SuppressWarnings("unchecked")
  public List<VersionInfoBean> findAllAppVersionInfo(AppVersionVo vo) {
    String hql = "from VersionInfoBean v where v.appType=:appType and v.status = 1 order by v.versionCode desc";
    String countHql = "select count(1) from VersionInfoBean v where v.appType=:appType and v.status = 1 ";
    Long count = (Long) super.createQuery(countHql).setParameter("appType", vo.getAppType()).uniqueResult();
    Page page = vo.getPage();
    page.setTotalCount(count);
    vo.setPage(page);
    return super.createQuery(hql).setParameter("appType", vo.getAppType()).setFirstResult(page.getFirst() - 1)
        .setMaxResults(page.getPageSize()).list();
  }

  /**
   * 删除版本信息
   * 
   * @param appType
   * @param id
   * @return
   */
  public int deleteAppVersionInfo(String appType, Long id) {
    String hql = "update VersionInfoBean v set v.status = 0 where v.id = :id and v.appType = :appType";
    return super.createQuery(hql).setParameter("id", id).setParameter("appType", appType).executeUpdate();
  }


  /**
   * 根据ID获取版本信息
   * 
   * @param appType
   * @param id
   * @return
   */
  public VersionInfoBean findVersionInfoById(String appType, Long id) {
    String hql = " from VersionInfoBean v where v.id = :id and v.appType = :appType";
    return (VersionInfoBean) super.createQuery(hql).setParameter("id", id).setParameter("appType", appType)
        .uniqueResult();
  }
}
