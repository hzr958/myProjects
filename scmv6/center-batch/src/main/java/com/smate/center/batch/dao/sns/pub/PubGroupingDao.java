package com.smate.center.batch.dao.sns.pub;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Repository;

import com.smate.center.batch.model.sns.pub.PubGrouping;
import com.smate.center.batch.model.sns.pub.PublicationPdwh;
import com.smate.core.base.utils.data.SnsHibernateDao;

/**
 * 成果分组 dao类.
 * 
 * @author tsz
 * 
 */
@Repository
public class PubGroupingDao extends SnsHibernateDao<PubGrouping, Long> {

  /**
   * 根据成果id查找分组内容
   * 
   * @param pubId
   * @return
   */
  public PubGrouping findById(Long pubId) {
    String hql = "from PubGrouping t where t.pubId=?";
    return super.findUnique(hql, pubId);
  }

  /**
   * 查找成果分组id
   * 
   * @param pubId
   * @return
   */
  public Long findGroupIdById(Long pubId) {
    String hql = "select t.groupId from PubGrouping t where t.pubId=?";
    return super.findUnique(hql, pubId);
  }

  /**
   * 根据分组id 获取相关成果
   * 
   * @param groupId
   * @return
   */
  public List<Long> getSnsPubIdsByGroupId(Long groupId) {
    String hql = "select t.pubId from PubGrouping t where t.groupId=?";
    return super.createQuery(hql, groupId).list();
  }

  /**
   * 根据pubid 获取分组id
   */
  public List<Long> getGroupIdByPubIds(List<Long> pubIds) {
    String hql = "select t.groupId from PubGrouping t where t.pubId in(:pubIds)";
    List<Long> resutList = new ArrayList<Long>();
    int start = 0;
    int end = 0;
    int pubIdsSize = pubIds.size();
    int count = pubIdsSize / 1000 + 1;
    for (int i = 0; i < count; i++) {
      start = i * 1000;
      end = (i + 1) * 1000;
      if (pubIdsSize < end) {
        end = pubIdsSize;
      }
      resutList.addAll(super.createQuery(hql).setParameterList("pubIds", pubIds.subList(start, end)).list());
      if (resutList != null && resutList.size() > 0) {
        break;
      }
    }

    return resutList;
  }

  /**
   * 获取新分组id
   */
  /**
   * 获取机构别名ID.
   * 
   * @return
   */
  public Long getNewGroupingId() {
    BigDecimal groupId =
        (BigDecimal) super.getSession().createSQLQuery("select seq_grouping_ids.nextval from dual").uniqueResult();
    return groupId.longValue();
  }

  /**
   * 获取初始化成果分组数据
   * 
   * @param maxsize
   * @return
   */
  public List<PublicationPdwh> getInitGroupingList(int start, int maxsize) {
    // String hql =
    // "from PublicationPdwh t where not exists(select 1 from PubGrouping t1 where t1.pubId=t.pubId)";
    String hql1 = "from PublicationPdwh t order by t.pubId";

    return super.createQuery(hql1).setFirstResult(start).setMaxResults(maxsize).list();

  }

  /**
   * 统计其他版本相关成果数.
   * 
   * @author xys
   * @param groupId
   * @param pubId
   * @param psnId
   * @return
   */
  public Long countSnsOtherRelPub(Long groupId, Long pubId, Long psnId) {
    String hql = "select count(t.id) from PubGrouping t where t.groupId=? and t.pubId<>? and t.psnId<>?";
    return super.findUnique(hql, groupId, pubId, psnId);
  }

  /**
   * 删除分组记录 根据pubId
   */
  public void delGroupindByPubId(Long pubId) {
    String hql = "delete from  PubGrouping t where t.pubId=?";
    super.createQuery(hql, pubId).executeUpdate();
  }

}
