package com.smate.center.batch.dao.rol.pub;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Set;

import org.springframework.stereotype.Repository;

import com.smate.center.batch.model.rol.pub.PubAssignSpsAuthor;
import com.smate.core.base.utils.data.RolHibernateDao;
import com.smate.core.base.utils.string.ServiceUtil;

/**
 * scopus成果作者名称DAO.
 * 
 * @author liqinghua
 * 
 */
@Repository
public class PubAssignSpsAuthorDao extends RolHibernateDao<PubAssignSpsAuthor, Long> {

  /**
   * 判断成果是否已经拆分过作者.
   * 
   * @param pubId
   * @return
   */
  public boolean isExistsPubAuthor(Long pubId) {

    String hql = "select count(id) from PubAssignSpsAuthor where pubId = ? ";
    Long count = super.findUnique(hql, pubId);
    if (count > 0) {
      return true;
    }
    return false;
  }

  /**
   * 获取单位用户名称简写匹配上成果作者的人员ID列表.
   * 
   * @param pubId
   * @param insId
   * @return
   */
  public List<Object[]> getSpsPrefixNameMatchPubAuthor(Long pubId, Long insId) {

    StringBuilder hql = new StringBuilder();
    hql.append(
        "select pa.seqNo,rp.pk.psnId,pa.insId,pa.name,pa.type from PubAssignSpsAuthor pa,PsnPmIsiName pp,RolPsnIns rp ");
    // 机构ID为空(=2)或者机构ID匹配上，作者名称匹配上
    // hql.append(" where (pa.insId = ? or pa.insId = 2) and ((pa.prefixName = pp.name and pp.type = 2)
    // or (pp.name = pa.name))");
    hql.append(" where (pa.insId = ? or pa.insId = 2) and (pp.name = pa.name and pp.type in (1,5))");
    // 是否在职
    hql.append(" and rp.isIns = 0 and rp.pk.insId = ? and rp.pk.psnId = pp.psnId and pa.pubId = ?  and rp.status = 1 ");
    return super.find(hql.toString(), insId, insId, pubId);
  }

  /**
   * 获取单位用户名称简写匹配上成果作者的人员ID列表.
   * 
   * @param pubId
   * @param insId
   * @return
   */
  @SuppressWarnings("unchecked")
  public List<Object[]> getSpsInitNameMatchPubAuthor(Long pubId, Long insId, Set<Long> psnIds) {

    StringBuilder hql = new StringBuilder();

    hql.append("select distinct pa.seqNo,pp.psnId from PubAssignSpsAuthor pa,PsnPmIsiName pp ");
    // 机构ID为空(=2)或者机构ID匹配上，只匹配first name两个字母以上的，作者名称匹配上
    hql.append(" where (pa.insId = :insId or pa.insId = 2) and pa.type = 1 and pa.name = pp.name  and pp.type = 5  ");
    hql.append(" and pa.pubId = :pubId and pp.psnId in(:psnIds) ");
    Collection<Collection<Long>> container = ServiceUtil.splitList(psnIds, 80);
    List<Object[]> listResult = new ArrayList<Object[]>();
    for (Collection<Long> item : container) {
      listResult.addAll(super.createQuery(hql.toString()).setParameter("insId", insId).setParameter("pubId", pubId)
          .setParameterList("psnIds", item).list());
    }
    return listResult;
  }

  /**
   * 获取单位用户全称匹配上成果作者的人员ID列表.
   * 
   * @param pubId
   * @param psnIds
   * @param insId
   * 
   * @return
   */
  @SuppressWarnings("unchecked")
  public List<Object[]> getSpsFullNameMatchPubAuthor(Long pubId, Set<Long> psnIds, Long insId) {

    // 机构ID为空(=2)或者机构ID匹配上，只匹配first name两个字母以上的，作者名称匹配上
    String hql =
        "select distinct pp.psnId,pa.seqNo from PubAssignSpsAuthor pa,PsnPmIsiName pp where pa.pubId = :pubId and pa.type = 1 and pp.psnId in(:psnIds) and pp.type = 1 and pa.name = pp.name and (pa.insId = :insId or pa.insId = 2)";
    Collection<Collection<Long>> container = ServiceUtil.splitList(psnIds, 80);
    List<Object[]> listResult = new ArrayList<Object[]>();
    for (Collection<Long> item : container) {
      listResult.addAll(super.createQuery(hql).setParameter("pubId", pubId).setParameterList("psnIds", item)
          .setParameter("insId", insId).list());
    }
    return listResult;
  }

  /**
   * 获取单位用户合作者匹配上成果作者的人员ID列表.
   * 
   * @param pubId
   * @param psnIds
   * @return
   */
  @SuppressWarnings("unchecked")
  public List<Object[]> getSpsConMatchPubAuthor(Long pubId, Set<Long> psnIds) {
    String hql =
        "select pp.psnId,count(pp.psnId) from PubAssignSpsAuthor pa ,PsnPmSpsConame pp where pa.pubId =:pubId and pp.psnId in(:psnIds) and pp.name = pa.name group by pp.psnId ";
    Collection<Collection<Long>> container = ServiceUtil.splitList(psnIds, 80);
    List<Object[]> listResult = new ArrayList<Object[]>();
    for (Collection<Long> item : container) {
      listResult.addAll(super.createQuery(hql).setParameter("pubId", pubId).setParameterList("psnIds", item).list());
    }
    return listResult;
  }

  /**
   * 获取单位用户合作者匹配上成果作者的人员ID列表.
   * 
   * @param pubId
   * @param psnIds
   * @return
   */
  @SuppressWarnings("unchecked")
  public List<Object[]> getSpsConMatchPubAuthorPreName(Long pubId, Set<Long> psnIds) {
    String hql =
        "select pp.psnId,count(pp.psnId) from PubAssignSpsAuthor pa ,PsnPmSpsCoPreName pp where pa.pubId =:pubId and pp.psnId in(:psnIds) and pa.prefixName = pp.preName group by pp.psnId ";
    Collection<Collection<Long>> container = ServiceUtil.splitList(psnIds, 80);
    List<Object[]> listResult = new ArrayList<Object[]>();
    for (Collection<Long> item : container) {
      listResult.addAll(super.createQuery(hql).setParameter("pubId", pubId).setParameterList("psnIds", item).list());
    }
    return listResult;
  }

  /**
   * 获取匹配上单位人员的scopus成果列表.
   * 
   * @param pubId
   * @param insId
   * @return
   */
  @SuppressWarnings("unchecked")
  public List<Object[]> getSpsPubAuthorMatchPsnPrefixName(Long psnId, Long insId) {

    StringBuilder hql = new StringBuilder();
    hql.append(
        "select distinct pa.pubId,pa.seqNo,pa.insId,pa.name,pa.type from PubAssignSpsAuthor pa,PsnPmIsiName pp ");
    hql.append(" where pa.pubInsId = :insId and pp.psnId = :psnId ");
    // 机构ID为空(=2)或者机构ID匹配上，作者名称前缀匹配上
    hql.append(
        " and (pa.insId = :insId or pa.insId =2 )  and ((pa.prefixName = pp.name and pp.type = 2) or  (pp.name = pa.name)) ");
    return super.createQuery(hql.toString()).setParameter("insId", insId).setParameter("psnId", psnId).list();
  }

  /**
   * 获取成果作者列表.
   * 
   * @param pubId
   * @return
   */
  public List<PubAssignSpsAuthor> getPubAuthor(Long pubId) {

    String hql = "from PubAssignSpsAuthor where pubId = ?  ";
    return super.find(hql, pubId);
  }
}
