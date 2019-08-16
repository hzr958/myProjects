package com.smate.center.batch.dao.sns.pub;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.smate.center.batch.model.sns.pub.PubSimpleHash;
import com.smate.core.base.utils.data.SnsHibernateDao;

/**
 * V_PUB_SIMPLE_HASH表实体Dao
 * 
 * @author lxz
 * 
 */
@Repository
public class PubSimpleHashDao extends SnsHibernateDao<PubSimpleHash, Long> {


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
}
