package com.smate.center.batch.dao.rol.pub;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Set;

import org.springframework.stereotype.Repository;

import com.smate.center.batch.model.rol.pub.PubAssignPubMedAuthor;
import com.smate.core.base.utils.data.RolHibernateDao;
import com.smate.core.base.utils.string.ServiceUtil;

/**
 * 成果作者名称DAO.
 * 
 * @author liqinghua
 * 
 */
@Repository
public class PubAssignPubMedAuthorDao extends RolHibernateDao<PubAssignPubMedAuthor, Long> {

  /**
   * 判断成果是否已经拆分过作者.
   * 
   * @param pubId
   * @return
   */
  public boolean isExistsPubAuthor(Long pubId) {

    String hql = "select count(id) from PubAssignPubMedAuthor where pubId = ? ";
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
  public List<Object[]> getPrefixNameMatchPubAuthor(Long pubId, Long insId) {

    StringBuilder hql = new StringBuilder();
    hql.append(
        "select pa.seqNo,rp.pk.psnId,pa.insId,pa.initName,pa.fullName from PubAssignPubMedAuthor pa,PsnPmIsiName pp,RolPsnIns rp ");
    // 机构ID为空(=2)或者机构ID匹配上，作者名称匹配上
    hql.append(" where (pa.insId = ? or pa.insId = 2) and pa.prefixName = pp.name  and pp.type = 2 ");
    // 是否在职
    hql.append(" and rp.isIns = 0 and rp.pk.insId = ? and rp.pk.psnId = pp.psnId and pa.pubId = ?  and rp.status = 1 ");
    return super.find(hql.toString(), insId, insId, pubId);
  }

  /**
   * 获取单位用户名称匹配上成果作者简称的人员ID列表.
   * 
   * @param pubId
   * @param insId
   * @return
   */
  public List<Object[]> getPubMedNameMatchPubAuthor(Long pubId, Long insId) {

    StringBuilder hql = new StringBuilder();
    hql.append(
        "select pa.seqNo,rp.pk.psnId,pa.insId,pa.initName,pa.fullName from PubAssignPubMedAuthor pa,PsnPmIsiName pp,RolPsnIns rp ");
    // 机构ID为空(=2)或者机构ID匹配上，作者名称匹配上
    hql.append(" where (pa.insId = ? or pa.insId = 2) and pp.name = pa.initName ");
    // 是否在职
    hql.append(" and rp.isIns = 0 and rp.pk.insId = ? and rp.pk.psnId = pp.psnId and pa.pubId = ?  and rp.status = 1 ");
    return super.find(hql.toString(), insId, insId, pubId);
  }

  /**
   * 获取单位用户名称匹配上成果作者全称的人员ID列表.
   * 
   * @param pubId
   * @param insId
   * @return
   */
  public List<Object[]> getPubMedNameFullMatchPubAuthor(Long pubId, Long insId) {

    StringBuilder hql = new StringBuilder();
    hql.append(
        "select pa.seqNo,rp.pk.psnId,pa.insId,pa.initName,pa.fullName from PubAssignPubMedAuthor pa,PsnPmIsiName pp,RolPsnIns rp ");
    // 机构ID为空(=2)或者机构ID匹配上，作者名称匹配上
    hql.append(" where (pa.insId = ? or pa.insId = 2) and pp.name = pa.fullName ");
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
  public List<Object[]> getPubMedInitNameMatchPubAuthor(Long pubId, Long insId, Set<Long> psnIds) {

    StringBuilder hql = new StringBuilder();

    hql.append("select distinct pa.seqNo,pp.psnId from PubAssignPubMedAuthor pa,PsnPmIsiName pp ");
    // 机构ID为空(=2)或者机构ID匹配上，作者名称匹配上
    // hql.append(" where (pa.insId = :insId or pa.insId = 2) and pa.initName = pp.name and pp.type
    // in(3,5) ");类型3不再匹配
    hql.append(" where (pa.insId = :insId or pa.insId = 2) and pa.initName = pp.name  and pp.type =5  ");
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
   * 获取匹配上单位人员简称的isi成果列表.
   * 
   * @param pubId
   * @param insId
   * @return
   */
  @SuppressWarnings("unchecked")
  public List<Object[]> getPubMedPubAuthorMatchPsnPrefixName(Long psnId, Long insId) {

    StringBuilder hql = new StringBuilder();
    hql.append(
        "select pa.pubId,pa.seqNo,pa.insId,pa.initName,pa.fullName from PubAssignPubMedAuthor pa,PsnPmIsiName pp ");
    // 机构ID为空(=2)或者机构ID匹配上，作者名称前缀匹配上
    hql.append("where  pp.psnId  =:psnId and pa.pubInsId = :insId ");
    hql.append("and (pa.insId = :insId or pa.insId =2 )  and pa.prefixName = pp.name and pp.type = 2");
    return super.createQuery(hql.toString()).setParameter("insId", insId).setParameter("psnId", psnId).list();
  }

  /**
   * 获取匹配上单位人员名称的isi成果列表.
   * 
   * @param pubId
   * @param insId
   * @return
   */
  @SuppressWarnings("unchecked")
  public List<Object[]> getPubMedPubAuthorMatchPsnName(Long psnId, Long insId) {

    StringBuilder hql = new StringBuilder();
    hql.append(
        "select pa.pubId,pa.seqNo,pa.insId,pa.initName,pa.fullName from PubAssignPubMedAuthor pa,PsnPmIsiName pp ");
    hql.append(" where  pp.psnId  =:psnId and pa.pubInsId = :insId ");
    // 机构ID为空(=2)或者机构ID匹配上，作者名称前缀匹配上
    hql.append(" and (pa.insId = :insId or pa.insId =2 )  and pp.name = pa.initName ");
    return super.createQuery(hql.toString()).setParameter("insId", insId).setParameter("psnId", psnId).list();
  }

  /**
   * 获取匹配上单位人员全称的isi成果列表.
   * 
   * @param pubId
   * @param insId
   * @return
   */
  @SuppressWarnings("unchecked")
  public List<Object[]> getPubMedPubAuthorMatchPsnFullName(Long psnId, Long insId) {

    StringBuilder hql = new StringBuilder();
    hql.append(
        "select pa.pubId,pa.seqNo,pa.insId,pa.initName,pa.fullName from PubAssignPubMedAuthor pa,PsnPmIsiName pp ");
    hql.append(" where  pp.psnId  =:psnId and pa.pubInsId = :insId ");
    // 机构ID为空(=2)或者机构ID匹配上，作者名称前缀匹配上
    hql.append(" and (pa.insId = :insId or pa.insId =2 )  and pp.name = pa.fullName ");
    return super.createQuery(hql.toString()).setParameter("insId", insId).setParameter("psnId", psnId).list();
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
  public List<Object[]> getPubMedFullNameMatchPubAuthor(Long pubId, Set<Long> psnIds, Long insId) {

    String hql =
        "select distinct pp.psnId,pa.seqNo from PubAssignPubMedAuthor pa,PsnPmIsiName pp where pa.pubId = :pubId and pp.psnId in(:psnIds) and (pp.type = 1 or pp.type = 4) and pa.fullName = pp.name and (pa.insId = :insId or pa.insId = 2)";
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
  public List<Object[]> getPubMedConMatchPubAuthor(Long pubId, Set<Long> psnIds) {
    String hql =
        "select pp.psnId,count(pp.psnId) from PubAssignPubMedAuthor pa ,PsnPmIsiConame pp where pa.pubId =:pubId and pp.psnId in(:psnIds) and (pp.initName = pa.initName or pp.fullName = pa.fullName) group by pp.psnId ";
    Collection<Collection<Long>> container = ServiceUtil.splitList(psnIds, 80);
    List<Object[]> listResult = new ArrayList<Object[]>();
    for (Collection<Long> item : container) {
      listResult.addAll(super.createQuery(hql).setParameter("pubId", pubId).setParameterList("psnIds", item).list());
    }
    return listResult;
  }

  /**
   * 获取单位用户合作者EMAIL匹配上成果作者的人员ID列表.
   * 
   * @param pubId
   * @param psnIds
   * @return
   */
  @SuppressWarnings("unchecked")
  public List<Object[]> getPubMedCoeMatchPubAuthor(Long pubId, Set<Long> psnIds) {
    String hql =
        "select pp.psnId,count(pp.psnId) from PubAssignPubMedEmail pa ,PsnPmCoemailRol pp where pa.pubId =:pubId and pp.psnId in(:psnIds) and pp.email = pa.email group by pp.psnId ";
    Collection<Collection<Long>> container = ServiceUtil.splitList(psnIds, 80);
    List<Object[]> listResult = new ArrayList<Object[]>();
    for (Collection<Long> item : container) {
      listResult.addAll(super.createQuery(hql).setParameter("pubId", pubId).setParameterList("psnIds", item).list());
    }
    return listResult;
  }

  /**
   * 获取成果作者列表.
   * 
   * @param pubId
   * @return
   */
  public List<PubAssignPubMedAuthor> getPubAuthor(Long pubId) {

    String hql = "from PubAssignPubMedAuthor where pubId = ?  ";
    return super.find(hql, pubId);
  }
}
