package com.smate.center.batch.dao.rol.pub;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.smate.center.batch.model.rol.pub.PubAssignEmail;
import com.smate.core.base.utils.data.RolHibernateDao;

/**
 * 成果指派EMAIL Dao.
 * 
 * @author liqinghua
 * 
 */
@Repository
public class PubAssignEmailDao extends RolHibernateDao<PubAssignEmail, Long> {

  /**
   * 判断作者EMAIL是否已经存在.
   * 
   * @param pubId
   * @param email
   * @return
   */
  public boolean isExists(Long pubId, String email) {
    String hql = "select count(id) from PubAssingEmail where pubId = ? and email = ? ";
    Long count = super.findUnique(hql, pubId, email);
    if (count > 0) {
      return true;
    }
    return false;
  }

  /**
   * 
   * 获取单位用户email匹配上成果email的人员ID列表.
   * 
   * @param pubId
   * @param insId
   * @return
   */
  public List<Object[]> getEmailMatchPubEmail(Long pubId, Long insId) {
    // String hql =
    // "select distinct rp.pk.psnId,pe.seqNo,pe.insId from PubAssignEmail pe,PsnPmEmail pp,RolPsnIns rp
    // where rp.pk.insId = ? and rp.pk.psnId = pp.psnId and rp.status = 1 and pe.pubId = ? and pe.email
    // = pp.email ";
    // 用户必须在职,作者机构ID为空(=2)
    String hql =
        "select pe.seqNo,rp.pk.psnId,pe.insId from PubAssignEmail pe,PsnPmEmailRol pp,RolPsnIns rp where rp.pk.insId = ? and rp.pk.psnId = pp.psnId  and rp.status = 1 and rp.isIns = 0 and pe.pubId = ? and pe.email = pp.email and (pe.insId = ? or pe.insId =2 )";
    return super.find(hql, insId, pubId, insId);
  }

  /**
   * 获取人员匹配上ISI成果作者email的成果列表.
   * 
   * @param psnId
   * @param insId
   * @return
   */
  @SuppressWarnings("unchecked")
  public List<Object[]> getPsnEmailMatchIsiPubEmail(Long psnId, Long insId) {
    StringBuilder hql = new StringBuilder();
    hql.append("select pe.pubId,pe.seqNo,pe.insId from PubAssignEmail pe,PsnPmEmail pp ");
    // 单位，PSN_ID过滤
    hql.append(" where  pp.psnId  =:psnId and pe.pubInsId = :insId  ");
    // email过滤
    hql.append("  and (pe.insId = :insId or pe.insId = 2 ) and pe.email = pp.email  ");
    return super.createQuery(hql.toString()).setParameter("insId", insId).setParameter("psnId", psnId).list();
  }

  /**
   * 获取不重复的用户合作者email信息.
   * 
   * @param psnId
   * @param pubId
   * @return
   */
  @SuppressWarnings("unchecked")
  public List<String> getNotExistsCoEmail(Long psnId, Long pubId) {

    StringBuilder hql = new StringBuilder();
    hql.append("select email from PubAssignEmail t where ");
    hql.append(" not exists(select t1.id from PsnPmEmail t1 where t1.psnId = ? and t.email = t1.email ) ");
    hql.append(" and not exists(select t2.id from PsnPmCoemail t2 where t2.psnId = ? and t.email = t2.email) ");
    hql.append(" and t.pubId = ? ");

    return super.createQuery(hql.toString(), psnId, psnId, pubId).list();
  }
}
