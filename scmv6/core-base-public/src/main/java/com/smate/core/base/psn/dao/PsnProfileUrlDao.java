package com.smate.core.base.psn.dao;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.smate.core.base.psn.model.PsnProfileUrl;
import com.smate.core.base.utils.data.SnsHibernateDao;

/**
 * 个人主页URL DAO.
 * 
 * @author zhuangyanming
 * 
 */
@Repository
public class PsnProfileUrlDao extends SnsHibernateDao<PsnProfileUrl, Long> {

  /**
   * 获取需要初始化短地址的人员Id
   * 
   * @param size
   * @return
   */
  @SuppressWarnings("unchecked")
  public List<Long> getNeedInitPsnId(Integer index, Integer batchSize) {
    String hql = "select t.psnId from  PsnProfileUrl t where t.psnIndexUrl is null";
    return super.createQuery(hql).setFirstResult(batchSize * (index - 1)).setMaxResults(batchSize).list();
  }

  /**
   * 查询总记录数
   * 
   * @return
   */
  public Long getCount() {
    String hql = "select count(t.psnId) from PsnProfileUrl t";
    return (Long) super.createQuery(hql).uniqueResult();

  }

  /**
   * 判断是否需要插入数据
   * 
   * @return
   */
  public boolean isNeedInsertData() {
    String sql = "select  count(1) from Person t1 where t1.personId not in(select t.psnId from PsnProfileUrl t)";
    Long count = (Long) super.createQuery(sql).uniqueResult();
    if (count > 0L) {
      return true;
    } else {
      return false;
    }

  }

  /**
   * 插入需要 初始化的个人短地址数据，person有PsnProfileUrl中没有的
   */
  public void insertInitData() {
    String sql =
        "insert into psn_profile_url t (select  t1.psn_id,NULL,null,null from person t1 where t1.psn_id not in(select t.psn_id from psn_profile_url t))";
    super.update(sql);
  }

  /**
   * 通过URL查找.
   * 
   * @param url
   * @return
   */
  public PsnProfileUrl find(String url) {
    String sql = "from PsnProfileUrl where url = ? ";
    return super.findUnique(sql, url);
  }

  /**
   * 通过PSN_ID查找.
   * 
   * @param psnId
   * @return
   */
  public PsnProfileUrl find(Long psnId) {
    String sql = "from PsnProfileUrl where psnId = ?";
    return super.findUnique(sql, psnId);
  }

  /**
   * URL是否被占用.
   * 
   * @param url
   * @return
   */
  public boolean isUsed(String url) {
    String hql = "select count(psnId) from PsnProfileUrl where url = ? ";
    Long count = super.findUnique(hql, url);
    if (count > 0) {
      return true;
    }
    return false;
  }

  /**
   * 批量查找人员短地址
   * 
   * @param psnIds
   * @return
   */
  public List<PsnProfileUrl> findPsnShortUrls(List<Long> psnIds) {
    String hql = "select new PsnProfileUrl(psnIndexUrl, psnId) from PsnProfileUrl t where t.psnId in (:psnIds)";
    return super.createQuery(hql).setParameterList("psnIds", psnIds).list();
  }
}
