package com.smate.center.batch.dao.sns.pub;

import java.util.List;
import org.codehaus.plexus.util.StringUtils;
import org.springframework.stereotype.Repository;

import com.smate.center.batch.model.sns.pub.PubDuplicatePO;
import com.smate.core.base.utils.data.SnsHibernateDao;
import com.smate.core.base.utils.number.NumberUtils;

/**
 * 个人库成果查重记录Dao
 * 
 * @author YJ
 *
 *         2018年5月31日
 */
@Repository
public class PubDuplicateDAO extends SnsHibernateDao<PubDuplicatePO, Long> {

  /**
   * 通过hashTP和hashTPP进行个人成果查重
   * 
   * @param hashTP
   * @param hashTPP
   * @return
   */
  @SuppressWarnings("unchecked")
  public List<Long> getPsnPubDuplicate(String hashTP, String hashTPP) {
    if (StringUtils.isEmpty(hashTP)) {
      return null;
    }
    String hql =
        "select t.pubId from PubDuplicatePO t where t.hashTP=:hashTP or t.hashTPP=:hashTPP order by t.pubId asc";
    return this.createQuery(hql).setParameter("hashTP", hashTP).setParameter("hashTPP", hashTPP).list();
  }

  /**
   * 通过hashDoi进行查重
   * 
   * @param hashDoi
   * @return
   */
  @SuppressWarnings("unchecked")
  public List<Long> dupByDoi(Long hashDoi) {
    if (NumberUtils.isNullOrZero(hashDoi)) {
      return null;
    }
    String hql = "select t.pubId from PubDuplicatePO t "
        + "where t.hashDoi=:hashDoi or t.hashCnkiDoi=:hashDoi order by t.pubId asc";
    return super.createQuery(hql).setParameter("hashDoi", String.valueOf(hashDoi)).list();
  }

  /**
   * 通过hashSourceId进行查重
   * 
   * @param hashSourceId
   * @return
   */
  @SuppressWarnings("unchecked")
  public List<Long> dupBySourceId(Long hashSourceId) {
    if (NumberUtils.isNullOrZero(hashSourceId)) {
      return null;
    }
    String hql = "select t.pubId from PubDuplicatePO t "
        + "where t.hashIsiSourceId=:hashSourceId or t.hashEiSourceId=:hashSourceId order by t.pubId asc";
    return super.createQuery(hql).setParameter("hashSourceId", String.valueOf(hashSourceId)).list();
  }

  /**
   * 通过专利信息的hashApplicationNo和hashPublicationOpenNo进行查重
   * 
   * @param hashApplicationNo
   * @param hashPublicationOpenNo
   * @return
   */
  @SuppressWarnings("unchecked")
  public List<Long> dupByPatentInfo(Long hashApplicationNo, Long hashPublicationOpenNo) {
    if (NumberUtils.isNullOrZero(hashApplicationNo) && NumberUtils.isNullOrZero(hashPublicationOpenNo)) {
      return null;
    }
    String hql = "select t.pubId from PubDuplicatePO t "
        + "where (t.hashApplicationNo=:hashApplicationNo and t.hashApplicationNo != 0) "
        + "or ( t.hashPublicationOpenNo=:hashPublicationOpenNo and t.hashPublicationOpenNo !=0) "
        + "order by t.pubId asc";
    return super.createQuery(hql).setParameter("hashApplicationNo", String.valueOf(hashApplicationNo))
        .setParameter("hashPublicationOpenNo", String.valueOf(hashPublicationOpenNo)).list();
  }

  /**
   * 通过hashTP和hashTPP进行查重
   * 
   * @param hashTP
   * @param hashTPP
   * @return
   */
  @SuppressWarnings("unchecked")
  public List<Long> dupByPubInfo(String hashTP, String hashTPP) {
    if (StringUtils.isEmpty(hashTP)) {
      return null;
    }
    String hql =
        "select t.pubId from PubDuplicatePO t " + "where t.hashTP=:hashTP or t.hashTPP=:hashTPP order by t.pubId asc";
    return super.createQuery(hql).setParameter("hashTP", hashTP).setParameter("hashTPP", hashTPP).list();
  }


}
