package com.smate.center.batch.dao.rol.pub;

import java.util.List;
import java.util.Map;

import org.apache.commons.collections4.CollectionUtils;
import org.hibernate.transform.Transformers;
import org.springframework.stereotype.Repository;

import com.smate.center.batch.exception.pub.DaoException;
import com.smate.center.batch.model.rol.pub.SiePsnStatistics;
import com.smate.core.base.utils.data.RolHibernateDao;

/**
 * rol同步sns的PsnStatistics
 * 
 * @author zyx
 * 
 */
@Repository
public class SiePsnStatisticsDao extends RolHibernateDao<SiePsnStatistics, Long> {
  public SiePsnStatistics findByPsnId(Long psnId) {
    String hql = "from SiePsnStatistics t where t.psnId = ?";
    return (SiePsnStatistics) super.createQuery(hql, new Object[] {psnId}).uniqueResult();
  }

  /**
   * 获取被赞次数最高的两个人的统计数
   * 
   * @author zk
   * @return
   * @throws DaoException
   */
  @SuppressWarnings({"unchecked", "rawtypes"})
  @Deprecated
  public List<Map> getMostLikesOfPsnStatistics(Long insId) throws DaoException {
    String hql =
        "select ps.pubAwardSum as LIKES,ps.citedSum as CITEDS,ps.pubSum as PUBS,pi.avatars as AVATARS,pi.zhName as NAME,pi.firstName as FIRSTNAME,pi.lastName as LASTNAME,pi.pk.psnId as PSNID,pi.pk.insId as INSID,pi.unitId as UNITID,pi.position as POSITION,pi.posId as POSID "
            + "from SiePsnStatistics ps,RolPsnIns pi "
            + "where pi.pk.psnId = ps.psnId and pi.pk.insId=? and pi.isIns=0 and pi.status=1 and pi.isActive=1 and pi.isPublic=1 order by pi.orderByLevel desc, ps.pubAwardSum desc,ps.citedSum desc,ps.pubSum desc";
    return super.createQuery(hql, insId).setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP).setMaxResults(2).list();
  }

  @SuppressWarnings({"unchecked", "rawtypes"})
  public List<Map> getMostLikesOfPsn(Long insId) throws DaoException {
    String hql =
        "select pi.avatars as AVATARS,pi.zhName as NAME,pi.firstName as FIRSTNAME,pi.lastName as LASTNAME,pi.pk.psnId as PSNID,pi.pk.insId as INSID,pi.unitId as UNITID,pi.position as POSITION,pi.posId as POSID "
            + "from RolPsnIns pi where pi.pk.insId=? and pi.isIns=0 and pi.status=1 and pi.isPublic=1";
    return super.createQuery(hql, insId).setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP).setMaxResults(2).list();
  }

  @SuppressWarnings({"rawtypes", "unchecked"})
  public Map getMostLikesOfPsnStatisticsByPsnId(Long psnId) throws DaoException {
    String hql =
        "select ps.pubAwardSum as LIKES,ps.citedSum as CITEDS,ps.pubSum as PUBS from SiePsnStatistics ps where ps.psnId=?";
    List<Map> list =
        super.createQuery(hql, psnId).setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP).setMaxResults(2).list();
    return CollectionUtils.isEmpty(list) ? null : list.get(0);
  }

  @SuppressWarnings({"unchecked", "rawtypes"})
  @Deprecated
  public List<Map> getMostLikesOfPsnStatisticsByIndex(Long insId) throws DaoException {
    String hql =
        "select ps.pubAwardSum as LIKES,ps.citedSum as CITEDS,ps.pubSum as PUBS,pi.avatars as AVATARS,pi.zhName as NAME,pi.firstName as FIRSTNAME,pi.lastName as LASTNAME,pi.pk.psnId as PSNID,pi.pk.insId as INSID,pi.unitId as UNITID,pi.position as POSITION,pi.posId as POSID,iip.id as INDEXID "
            + "from SiePsnStatistics ps,RolPsnIns pi, InsIndexPsn iip "
            + " where pi.pk.psnId = ps.psnId and pi.pk.insId=iip.insId and pi.pk.psnId=iip.psnId and pi.pk.insId=? order by iip.id asc";
    return super.createQuery(hql, insId).setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP).list();
  }

  @SuppressWarnings({"unchecked", "rawtypes"})
  public List<Map> getMostLikesOfPsnStatisticsByIndexNew(Long insId) throws DaoException {
    String hql =
        "select pi.avatars as AVATARS,pi.zhName as NAME,pi.firstName as FIRSTNAME,pi.lastName as LASTNAME,pi.pk.psnId as PSNID,pi.pk.insId as INSID,pi.unitId as UNITID,pi.position as POSITION,pi.posId as POSID,iip.id as INDEXID "
            + "from RolPsnIns pi, InsIndexPsn iip "
            + " where pi.pk.insId=iip.insId and pi.pk.psnId=iip.psnId and pi.pk.insId=? order by iip.id asc";
    return super.createQuery(hql, insId).setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP).list();
  }
}
