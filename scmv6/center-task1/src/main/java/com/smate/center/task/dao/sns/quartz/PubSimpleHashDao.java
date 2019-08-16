package com.smate.center.task.dao.sns.quartz;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.smate.center.task.model.sns.quartz.PubSimpleHash;
import com.smate.core.base.utils.data.SnsHibernateDao;

/**
 * V_PUB_SIMPLE_HASH表实体Dao
 * 
 * @author lxz
 * 
 */
@Repository
public class PubSimpleHashDao extends SnsHibernateDao<PubSimpleHash, Long> {

  @SuppressWarnings("unchecked")
  public List<Long> getNeedInitHash(Integer size) {
    String hql = "select t.pubId from PubSimpleHash t where t.enHashCode is null and t.zhHashCode is null";
    return super.createQuery(hql).setMaxResults(size).list();

  }

  public void updateTitleHashById(Long pubId, String zhHash, String enHash) {
    String hql = "update PubSimpleHash set enHashCode=:enHash,zhHashCode=:zhHash where pubId=:pubId";
    super.createQuery(hql).setParameter("enHash", enHash).setParameter("zhHash", zhHash).setParameter("pubId", pubId)
        .executeUpdate();
  }

  /**
   * 
   * @param pubId
   * @return
   */
  public PubSimpleHash getHashByPubId(Long pubId) {

    String hql = "from PubSimpleHash where pubId=:pubId";

    return (PubSimpleHash) super.createQuery(hql).setParameter("pubId", pubId).uniqueResult();

  }

  /**
   * 通过标题和psnId获取重复的成果id列表
   * 
   * @param ownerPsnId
   * @param enHash
   * @param zhHash
   * @return
   */
  @SuppressWarnings("unchecked")
  public List<Long> getDupPubSimpleIds(Long ownerPsnId, String enHash, String zhHash) {
    String hql =
        "select ph.pubId from PubSimpleHash ph ,PubSimple ps where ps.ownerPsnId = :psnId and ph.pubId=ps.pubId and (ph.enHashCode = :enHash  or ph.zhHashCode = :zhHash)";
    return super.createQuery(hql).setParameter("psnId", ownerPsnId).setParameter("enHash", enHash)
        .setParameter("zhHash", zhHash).list();
  }

  /**
   * 通过标题和psnId获取重复的成果id列表只查没有删除的成果
   * 
   * @param ownerPsnId
   * @param enHash
   * @param zhHash
   * @return
   */
  @SuppressWarnings("unchecked")
  public List<Long> getDupPubIds(Long ownerPsnId, String enHash, String zhHash) {
    String hql =
        "select ph.pubId from PubSimpleHash ph ,PubSimple ps where ps.status=0 and ps.articleType=1 and ps.ownerPsnId = :psnId and ph.pubId=ps.pubId and (ph.enHashCode = :enHash  or ph.zhHashCode = :zhHash)";
    return super.createQuery(hql).setParameter("psnId", ownerPsnId).setParameter("enHash", enHash)
        .setParameter("zhHash", zhHash).list();
  }

  /**
   * 通过中文标题和psnId获取重复的成果id列表
   * 
   * @param ownerPsnId
   * @param zhHash
   * @return
   */
  @SuppressWarnings("unchecked")
  public List<Long> getDupPubIdsByZhTitle(Long ownerPsnId, String zhHash) {
    String hql =
        "select ph.pubId from PubSimpleHash ph ,PubSimple ps where ps.status=0 and ps.articleType=1 and ps.ownerPsnId = :psnId and ph.pubId=ps.pubId and (ph.zhHashCode = :zhHash or ph.enHashCode = :zhHash)";
    return super.createQuery(hql).setParameter("psnId", ownerPsnId).setParameter("zhHash", zhHash).list();
  }

  /**
   * 通过英文标题和psnId获取重复的成果id列表
   * 
   * @param ownerPsnId
   * @param enHash
   * @return
   */
  @SuppressWarnings("unchecked")
  public List<Long> getDupPubIdsByEnTitle(Long ownerPsnId, String enHash) {
    String hql =
        "select ph.pubId from PubSimpleHash ph ,PubSimple ps where ps.status=0 and ps.articleType=1 and ps.ownerPsnId = :psnId and ph.pubId=ps.pubId and (ph.enHashCode = :enHash or ph.zhHashCode = :enHash)";
    return super.createQuery(hql).setParameter("psnId", ownerPsnId).setParameter("enHash", enHash).list();
  }

  /**
   * 简单查重
   *
   * @author LXZ
   * 
   * @since 6.0.1
   * @version 6.0.1
   * @return
   */
  public boolean pubSimpleDupCheck(Long pubId, String enHashCode, String zhHashCode) {
    Map<String, Object> param1 = new HashMap<String, Object>();
    Map<String, Object> param2 = new HashMap<String, Object>();
    Long count1 = 0L;
    Long count2 = 0L;
    if (enHashCode != null && !("".equals(enHashCode))) {
      String hql1 = " select count(1) from PubSimpleHash t1 where t1.enHashCode= :enHashCode ";
      param1.put("enHashCode", enHashCode);
      if (pubId != null) {
        hql1 += " and t1.pubId!=:pubId ";
        param1.put("pubId", pubId);
      }
      count1 = super.findUnique(hql1, param1);
    }
    String hql2 = " select count(1) from PubSimpleHash t1 where t1.zhHashCode=:zhHashCode ";
    param2.put("zhHashCode", zhHashCode);
    if (pubId != null) {
      hql2 += " and t1.pubId!=:pubId ";
      param2.put("pubId", pubId);
    }

    count2 = super.findUnique(hql2, param2);
    if (count1 + count2 > 0) {
      return true;
    } else {
      return false;
    }
  }

  /**
   * 删除
   *
   * @author LXZ
   * 
   * @since 6.0.1
   * @version 6.0.1
   * @param pubId
   */
  public void deleteData(Long pubId) {
    String hql = "delete from PubSimpleHash where pubId=?";
    super.createQuery(hql, pubId).executeUpdate();
  }

  public List<Long> getSimpleDupPubIds(Long pubId, String enHashCode, String zhHashCode) {
    List<Long> dupPubIds = new ArrayList<Long>();
    List<Long> dupPubIdsEn = new ArrayList<Long>();
    List<Long> dupPubIdsZh = new ArrayList<Long>();

    Map<String, Object> param1 = new HashMap<String, Object>();
    Map<String, Object> param2 = new HashMap<String, Object>();

    if (enHashCode != null && "".equals(enHashCode)) {
      String hql1 = " select t1.pubId from PubSimpleHash t1 where t1.enHashCode= :enHashCode ";
      param1.put("enHashCode", enHashCode);
      if (pubId != null) {
        hql1 += " and t1.pubId!=:pubId ";
        param1.put("pubId", pubId);
      }
      dupPubIdsEn = super.createQuery(hql1, param1).list();
    }
    String hql2 = " select t1.pubId from PubSimpleHash t1 where t1.zhHashCode=:zhHashCode ";
    param2.put("zhHashCode", zhHashCode);
    if (pubId != null) {
      hql2 += " and t1.pubId!=:pubId ";
      param2.put("pubId", pubId);
    }

    dupPubIdsZh = super.createQuery(hql2, param2).list();

    dupPubIds.addAll(dupPubIdsZh);
    dupPubIds.addAll(dupPubIdsEn);

    return dupPubIds;
  }

  // ==================================================================
  /**
   * 根据 中英文标题+类型 生成的哈希值
   * 
   * @param ownerPsnId
   * @param zhHash
   * @param enHash
   * @return
   */
  @SuppressWarnings("unchecked")
  public List<Long> getDupPubByTitleType(Long ownerPsnId, String zhHash, String enHash) {
    String hql =
        "select ph.pubId from PubSimpleHash ph, PubSimple ps where ps.status=0 and ps.articleType=1 and ps.ownerPsnId = :psnId and ph.pubId=ps.pubId and (ph.tpHashZh = :zhHash or ph.tpHashEn = :zhHash or ph.tpHashEn = :enHash or ph.tpHashZh = :enHash)";
    return super.createQuery(hql).setParameter("psnId", ownerPsnId).setParameter("zhHash", zhHash)
        .setParameter("enHash", enHash).list();
  }

  /**
   * 根据 中文标题+类型 生成的哈希值
   * 
   * @param ownerPsnId
   * @param zhHash
   * @return
   */
  @SuppressWarnings("unchecked")
  public List<Long> getDupPubByZhTitleType(Long ownerPsnId, String zhHash) {
    String hql =
        "select ph.pubId from PubSimpleHash ph, PubSimple ps where ps.status=0 and ps.articleType=1 and ps.ownerPsnId = :psnId and ph.pubId=ps.pubId and (ph.tpHashZh = :zhHash or ph.tpHashEn = :zhHash)";
    return super.createQuery(hql).setParameter("psnId", ownerPsnId).setParameter("zhHash", zhHash).list();
  }

  /**
   * 根据 英文标题+类型 生成的哈希值
   * 
   * @param ownerPsnId
   * @param enHash
   * @return
   */
  @SuppressWarnings("unchecked")
  public List<Long> getDupPubByEnTitleType(Long ownerPsnId, String enHash) {
    String hql =
        "select ph.pubId from PubSimpleHash ph, PubSimple ps where ps.status=0 and ps.articleType=1 and ps.ownerPsnId = :psnId and ph.pubId=ps.pubId and (ph.tpHashEn = :enHash or ph.tpHashZh = :enHash)";
    return super.createQuery(hql).setParameter("psnId", ownerPsnId).setParameter("enHash", enHash).list();
  }

  /**
   * 根据 中英文标题+类型+年份 生成的哈希值
   * 
   * @param ownerPsnId
   * @param zhHash
   * @param enHash
   * @return
   */
  @SuppressWarnings("unchecked")
  public List<Long> getDupPubByTitleTypeYear(Long ownerPsnId, String zhHash, String enHash) {
    String hql =
        "select ph.pubId from PubSimpleHash ph, PubSimple ps where ps.status=0 and ps.articleType=1 and ps.ownerPsnId = :psnId and ph.pubId=ps.pubId and (ph.zhHashCode = :zhHash or ph.enHashCode = :zhHash or ph.enHashCode = :enHash or ph.zhHashCode = :enHash)";
    return super.createQuery(hql).setParameter("psnId", ownerPsnId).setParameter("zhHash", zhHash)
        .setParameter("enHash", enHash).list();
  }

  /**
   * 根据 中文标题+类型+年份 生成的哈希值
   * 
   * @param ownerPsnId
   * @param zhHash
   * @return
   */
  @SuppressWarnings("unchecked")
  public List<Long> getDupPubByZhTitleTypeYear(Long ownerPsnId, String zhHash) {
    String hql =
        "select ph.pubId from PubSimpleHash ph, PubSimple ps where ps.status=0 and ps.articleType=1 and ps.ownerPsnId = :psnId and ph.pubId=ps.pubId and (ph.zhHashCode = :zhHash or ph.enHashCode = :zhHash)";
    return super.createQuery(hql).setParameter("psnId", ownerPsnId).setParameter("zhHash", zhHash).list();
  }

  /**
   * 根据 英文标题+类型+年份 生成的哈希值
   * 
   * @param ownerPsnId
   * @param enHash
   * @return
   */
  @SuppressWarnings("unchecked")
  public List<Long> getDupPubByEnTitleTypeYear(Long ownerPsnId, String enHash) {
    String hql =
        "select ph.pubId from PubSimpleHash ph, PubSimple ps where ps.status=0 and ps.articleType=1 and ps.ownerPsnId = :psnId and ph.pubId=ps.pubId and (ph.enHashCode = :enHash or ph.zhHashCode = :enHash)";
    return super.createQuery(hql).setParameter("psnId", ownerPsnId).setParameter("enHash", enHash).list();
  }

}
