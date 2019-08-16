package com.smate.core.base.utils.dao.security;

import java.util.Date;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections4.CollectionUtils;
import org.springframework.stereotype.Repository;
import com.smate.core.base.utils.data.CasHibernateDao;
import com.smate.core.base.utils.model.cas.security.SysUserLoginLog;

/**
 * 持久化用户登录信息
 * 
 * @author tsz
 * 
 */
@Repository
public class SysUserLoginLogDao extends CasHibernateDao<SysUserLoginLog, Long> {
  /**
   * 根据psnId查找最近的登陆IP
   * 
   * @param psnId
   * @return
   * @throws DaoException
   */
  public String findLoginIpByPsnId(Long psnId) throws Exception {
    String hql = "from SysUserLoginLog t where t.psnId = ? order by t.loginTime desc";
    List<SysUserLoginLog> result = super.createQuery(hql, psnId).list();
    if (result != null && result.size() > 0) {
      return result.get(0).getLoginIp();
    } else {
      return null;
    }
  }

  @SuppressWarnings("unchecked")
  public List<SysUserLoginLog> getSysUserLoginLog(Long psnId) throws Exception {
    return super.createQuery("from SysUserLoginLog t where t.psnId = ?", psnId).list();
  }

  /**
   * 根据psnId查找上一次登录时间
   * 
   * @param psnId
   * @return
   * @throws DaoException
   */
  @SuppressWarnings("unchecked")
  public Date findLastTimeByPsnId(Long psnId) throws Exception {
    String hql = "from SysUserLoginLog t where t.psnId = ? order by t.loginTime desc";
    List<SysUserLoginLog> result = super.createQuery(hql, psnId).list();
    if (result != null && result.size() > 1) {
      return result.get(1).getLoginTime();
    } else {
      return null;
    }
  }

  @SuppressWarnings("unchecked")
  public List<Map<String, Object>> getLoginCntByDay(List<Long> ids) {
    List<Map<String, Object>> resultList = null;
    if (CollectionUtils.isNotEmpty(ids)) {
      StringBuffer sql = new StringBuffer();
      sql.append(
          "select new Map(trunc(t.loginTime) as loginTime,count(*) as cnt)from SysUserLoginLog t where trunc(t.loginTime)=trunc(sysdate-1) ");
      if (ids.size() >= 1000) {
        String sqlConditions = super.getSqlStrByList(ids, 800, "psnId");
        sql.append(sqlConditions);
        sql.append("  group by trunc(t.loginTime) ");
        resultList = super.createQuery(sql.toString()).list();
      } else {
        resultList = super.createQuery(sql.append(" and t.psnId in (:ids) group by trunc(t.loginTime)").toString())
            .setParameterList("ids", ids).list();
      }
    }
    return resultList;
  }

  /**
   * 根据psnId查找上的，登录日志
   * 暂时排除 类型为11
   *
   * @param psnId
   * @return
   */
  @SuppressWarnings("unchecked")
  public SysUserLoginLog findLastLog(Long psnId)  {
    String hql = "from SysUserLoginLog t where t.psnId = ? and t.loginType != 11 order by t.loginTime desc";
    List<SysUserLoginLog> result = super.createQuery(hql, psnId).setMaxResults(1).list();
    if (result != null && result.size() > 0) {
      return result.get(0);
    } else {
      return null;
    }
  }

}
