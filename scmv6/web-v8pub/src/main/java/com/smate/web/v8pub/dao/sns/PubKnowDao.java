package com.smate.web.v8pub.dao.sns;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.smate.core.base.utils.data.SnsHibernateDao;
import com.smate.web.v8pub.po.sns.PubKnow;

/**
 * 查询拥有相同成果（合作者）用
 * 
 * @author wsn
 * @date 2018年8月24日
 */
@Repository
public class PubKnowDao extends SnsHibernateDao<PubKnow, Long> {

  /**
   * 查找拥有相同成果的人员ID
   * 
   * @param pubType
   * @param zhTitleHash
   * @param enTitleHash
   * @param psnIds
   * @return
   */
  @SuppressWarnings("unchecked")
  public List<Long> findSamePubByPsn(int pubType, Long titleHash, List<Long> psnIds) {
    titleHash = titleHash == null ? 0 : titleHash;
    String hql =
        "select distinct psnId from PubKnow where articleType=:type and (zhTitleHash=:zhHash or enTitleHash=:enHash) and isPubAuthors=1 and psnId not in(:ids) and status in(0,2,3,4,5) and rownum<=1000";
    return createQuery(hql).setParameter("type", pubType).setParameter("zhHash", titleHash)
        .setParameter("enHash", titleHash).setParameterList("ids", psnIds).list();
  }
}
