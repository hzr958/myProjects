package com.smate.center.batch.dao.rol.pub;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Set;

import org.springframework.stereotype.Repository;

import com.smate.center.batch.model.rol.pub.PubAssignCnkiAuthor;
import com.smate.core.base.utils.data.RolHibernateDao;
import com.smate.core.base.utils.string.ServiceUtil;

/**
 * CNKI成果作者名称DAO.
 * 
 * @author liqinghua
 * 
 */
@Repository
public class PubAssignCnkiAuthorDao extends RolHibernateDao<PubAssignCnkiAuthor, Long> {

  /**
   * 判断成果是否已经拆分过作者.
   * 
   * @param pubId
   * @return
   */
  public boolean isExistsPubAuthor(Long pubId) {

    String hql = "select count(id) from PubAssignCnkiAuthor where pubId = ? ";
    Long count = super.findUnique(hql, pubId);
    if (count > 0) {
      return true;
    }
    return false;
  }

  /**
   * 获取单位用户名称匹配上成果作者的人员ID列表.
   * 
   * @param pubId
   * @param insId
   * @return
   */
  public List<Object[]> getCnkiNameMatchPubAuthor(Long pubId, Long insId) {

    StringBuilder hql = new StringBuilder();
    hql.append(
        "select distinct rp.pk.psnId,pa.seqNo,pa.insId from PubAssignCnkiAuthor pa,PsnPmCnkiName pp,RolPsnIns rp ");
    // 用户必须在职
    hql.append(
        " where rp.pk.insId = ? and rp.pk.psnId = pp.psnId and pa.pubId = ?  and rp.status = 1 and rp.isIns = 0 ");
    // insid=2表示单位为空
    hql.append(" and pp.name = pa.name and (pa.insId = ? or pa.insId =2 )");
    return super.find(hql.toString(), insId, pubId, insId);
  }

  /**
   * 获取匹配上单位人员的CNKI成果列表.
   * 
   * @param pubId
   * @param insId
   * @return
   */
  @SuppressWarnings("unchecked")
  public List<Object[]> getCnkiPubAuthorMatchPsn(Long psnId, Long insId) {

    StringBuilder hql = new StringBuilder();
    hql.append("select distinct pa.pubId,pa.seqNo,pa.insId from PubAssignCnkiAuthor pa,PsnPmCnkiName pp ");
    hql.append(" where pa.pubInsId = :insId and pp.psnId = :psnId ");
    // 名称匹配
    hql.append(" and pp.name = pa.name and (pa.insId = :insId or pa.insId =2)");

    return super.createQuery(hql.toString()).setParameter("insId", insId).setParameter("psnId", psnId).list();
  }

  /**
   * 获取单位用户合作者匹配上成果作者的人员ID列表.
   * 
   * @param pubId
   * @param psnIds
   * @return
   */
  @SuppressWarnings("unchecked")
  public List<Object[]> getCnkiConMatchPubAuthor(Long pubId, Set<Long> psnIds) {
    String hql =
        "select pp.psnId,count(pp.psnId) from PubAssignCnkiAuthor pa ,PsnPmCnkiConame pp where pa.pubId =:pubId and pp.psnId in(:psnIds) and pp.name = pa.name group by pp.psnId ";
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
  public List<PubAssignCnkiAuthor> getPubAuthor(Long pubId) {

    String hql = "from PubAssignCnkiAuthor where pubId = ?  ";
    return super.find(hql, pubId);
  }
}
