package com.smate.center.batch.dao.sns.pub;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections4.CollectionUtils;
import org.springframework.stereotype.Repository;

import com.smate.center.batch.exception.pub.DaoException;
import com.smate.core.base.psn.model.profile.PsnDisciplineKey;
import com.smate.core.base.utils.data.SnsHibernateDao;

/**
 * 
 * @author liqinghua
 * 
 */
@Repository
public class PsnDisciplineKeyDao extends SnsHibernateDao<PsnDisciplineKey, Long> {

  /**
   * 通过个人专长ID获取专长关键字列表.
   * 
   * @param pdId
   * @return
   */
  @SuppressWarnings("unchecked")
  public List<PsnDisciplineKey> findKeyByPdId(Long pdId) {

    return super.createQuery("from PsnDisciplineKey t where t.pdId = ? ", pdId).list();
  }

  public Boolean exitsPsnDisKey(Long psnId) {
    long count = super.countHqlResult(" from PsnDisciplineKey t where t.psnId = ? and t.status=1", psnId);
    if (count == 0) {
      return false;
    } else {
      return true;
    }
  }

  /**
   * 删除某人的个人专长关键字数据.[直接删除会丢失认同信息]
   * 
   * @param psnId
   */
  @Deprecated
  public void removeByPsnId(Long psnId) {
    super.createQuery("delete from PsnDisciplineKey where psnId = ? )", psnId).executeUpdate();
    // super.createQuery(
    // "delete from PsnDisciplineKey where pdId in (select id from
    // PsnDiscipline where psnId = ? )",
    // psnId)
    // .executeUpdate();
  }

  /**
   * 查询某人的关键词数据.
   * 
   * @param psnId
   */
  @SuppressWarnings("unchecked")
  public List<PsnDisciplineKey> findByPsnId(Long psnId) {
    // return super.createQuery(
    // "from PsnDisciplineKey where pdId in (select id from PsnDiscipline
    // where psnId = ? )",
    // psnId).list();
    return super.createQuery("from PsnDisciplineKey t where t.psnId = ? and t.status=1", psnId).list();
  }

  /**
   * 保存某人的个人专长关键字数据.
   * 
   * @param keyId
   * @param keyWords
   * @param pdId
   * @return
   */
  @Deprecated
  public PsnDisciplineKey savePsnDisciplineKey(Long keyId, String keyWords, Long pdId, Integer lanType) {
    PsnDisciplineKey key = new PsnDisciplineKey(keyId, keyWords, pdId, lanType);
    this.save(key);
    return key;
  }

  /**
   * 保存个人关键字数据.
   * 
   * @param keyWords
   * @param pdId
   */
  public Integer saveKeys(String keyWords, Long psnId, Integer permission) {

    String hql = "from PsnDisciplineKey t where t.psnId = ? and t.keyWords = ?";
    PsnDisciplineKey pdk = (PsnDisciplineKey) super.createQuery(hql, psnId, keyWords).uniqueResult();

    if (pdk == null) {
      pdk = new PsnDisciplineKey(keyWords, psnId, 1);
    } else {
      pdk.setStatus(1);
    }
    this.save(pdk);
    return 1;
  }

  /**
   * 获取指定学科的所有关键词.
   * 
   * @param psnId
   * @return
   */
  public List<PsnDisciplineKey> findPsnDisciplineKey(Long disId) {
    String hql = "from PsnDisciplineKey where pdId=?";
    return find(hql, disId);
  }

  /**
   * 更新相关关键词状态.
   * 
   * @param id
   * @throws DaoException
   */
  public void updatePsnDiscKeyRelatedStatus(Long id) throws DaoException {
    String hql = "update PsnDisciplineKey t set t.refreshFlag=? where t.id=?";
    super.createQuery(hql, new Object[] {1, id}).executeUpdate();
  }

  /**
   * 更新关键词psnId(人员合并用)
   * 
   * @author zk
   */
  public void updateDiscPsnId(Long fromPsnId, Long toPsnId) throws DaoException {
    super.createQuery("update PsnDisciplineKey set psnId=? where psnId=? ", toPsnId, fromPsnId).executeUpdate();
  }

  /**
   * 查找psnId的有效关键词
   * 
   * @param psnId
   * @return
   */
  @SuppressWarnings("unchecked")
  public List<Long> findPsnDiscKeyId(Long psnId, Integer status) {

    String hql = "select t.id from PsnDisciplineKey t where t.psnId=? and t.status=?";
    return super.createQuery(hql, psnId, status).list();

  }

  public List<PsnDisciplineKey> findPsnDisciplineKey(Long psnId, List<Long> idList, Integer status) {
    StringBuilder hql = null;
    Map<String, Object> values = new HashMap<String, Object>();
    values.put("psnId", psnId);

    if (CollectionUtils.isNotEmpty(idList)) {
      hql = new StringBuilder("from PsnDisciplineKey t where t.psnId = :psnId and t.id in(:idList) and t.status=1");
      values.put("idList", idList);
    } else {
      hql = new StringBuilder("from PsnDisciplineKey t where t.psnId = :psnId and t.status=1");
    }

    values.put("status", status);
    hql.append(" and t.status=:status order by t.id");

    return super.find(hql.toString(), values);
  }

  public void delKeywordBypsnId(Long psnId) {

    String hql = "delete from PsnDisciplineKey t WHERE t.psnId = ? and t.status=1";
    super.createQuery(hql, psnId).executeUpdate();
  }

  public void updateKwStatusByPsnId(Long psnId, Integer status) {
    String hql = "update PsnDisciplineKey t set t.status= ? where t.psnId=? ";
    super.createQuery(hql, status, psnId).executeUpdate();
  }

  public PsnDisciplineKey findPsnDiscKey(String keyWords, Long psnId) {
    StringBuilder hql = new StringBuilder("from PsnDisciplineKey t where t.psnId = ? and t.keyWords=? and t.status=1");
    return super.findUnique(hql.toString(), psnId, keyWords);
  }

  /**
   * 得到有ra串研究领域的人员id
   * 
   * @param psnId
   * @param ra
   * @return
   */
  @SuppressWarnings("unchecked")
  public List<Long> getPsnIdBysameRa(List<Long> psnId, List<String> raList) {
    String hql =
        "select t.psnId from PsnDisciplineKey t where t.psnId in (:psnId) and t.status=1 and t.keyWords in (:ra)";
    return super.createQuery(hql).setParameterList("psnId", psnId).setParameterList("ra", raList).list();
  }

  @SuppressWarnings("unchecked")
  public List<String> getPsnKeywords(Long psnId) {
    return super.createQuery("select t.keyWords from PsnDisciplineKey t where t.psnId = ? and t.status=1", psnId)
        .list();
  }
}
