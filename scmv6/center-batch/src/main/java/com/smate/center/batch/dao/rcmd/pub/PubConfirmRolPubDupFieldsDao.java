package com.smate.center.batch.dao.rcmd.pub;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Repository;
import org.springframework.util.Assert;

import com.smate.center.batch.exception.pub.DaoException;
import com.smate.center.batch.model.rcmd.pub.PubConfirmRolPubDupFields;
import com.smate.core.base.utils.data.RcmdHibernateDao;



/**
 * rol成果确认用于查重的信息.
 * 
 * @author liqinghua
 * 
 */
@Repository
public class PubConfirmRolPubDupFieldsDao extends RcmdHibernateDao<PubConfirmRolPubDupFields, Long> {


  public void deleteById(Long rolPubId) throws DaoException {
    String sql = "delete from PubConfirmRolPubDupFields t where t.rolPubId=?";
    this.createQuery(sql, new Object[] {rolPubId}).executeUpdate();
  }

  /**
   * 查找isi_id重复的成果.
   * 
   * @param isiHash
   * @param psnId
   * @return
   */
  @SuppressWarnings("unchecked")
  public List<Long> findDupByIsiId(Long isiHash, Long psnId) {

    String hql = "select t.id from PublicationConfirm t where t.psnId = ? and t.confirmResult = 0 and exists("
        + "select t1.rolPubId from PubConfirmRolPubDupFields t1 where t.rolPubId = t1.rolPubId and t1.isiHash = ? )";
    return super.createQuery(hql, psnId, isiHash).list();
  }

  /**
   * 查找ei_id重复的成果.
   * 
   * @param isiHash
   * @param psnId
   * @return
   */
  @SuppressWarnings("unchecked")
  public List<Long> findDupByEiId(Long eiHash, Long psnId) {

    String hql = "select t.id from PublicationConfirm t where t.psnId = ? and t.confirmResult = 0 and exists("
        + "select t1.rolPubId from PubConfirmRolPubDupFields t1 where t.rolPubId = t1.rolPubId and t1.eiHash = ? )";
    return super.createQuery(hql, psnId, eiHash).list();
  }

  /**
   * 查找sps_id重复的成果.
   * 
   * @param isiHash
   * @param psnId
   * @return
   */
  @SuppressWarnings("unchecked")
  public List<Long> findDupBySpsId(Long spsHash, Long psnId) {

    String hql = "select t.id from PublicationConfirm t where t.psnId = ? and t.confirmResult = 0 and exists("
        + "select t1.rolPubId from PubConfirmRolPubDupFields t1 where t.rolPubId = t1.rolPubId and t1.spsHash = ? )";
    return super.createQuery(hql, psnId, spsHash).list();
  }

  /**
   * 通过DOI查重.
   * 
   * @param doiHash
   * @param pubYear
   * @param ownerId
   * @return
   */
  @SuppressWarnings("unchecked")
  public List<Long> findDupByDoi(Long doiHash, Integer pubYear, Long psnId) {

    if (doiHash == null || pubYear == null) {
      return null;
    }
    String hql = "select t.id from PublicationConfirm t where t.psnId = ? and t.confirmResult = 0 and exists("
        + "select t1.rolPubId from PubConfirmRolPubDupFields t1 where t.rolPubId = t1.rolPubId and t1.pubYear = ? and t1.doiHash = ? )";
    return super.createQuery(hql, psnId, pubYear, doiHash).list();
  }

  /**
   * 通过期刊指纹查找期刊重复.
   * 
   * @param jaFingerPrint
   * @param jpFingerPrint
   * @param pubYear
   * @param psnId
   * @return
   */
  @SuppressWarnings("unchecked")
  public List<Long> findDupByIssueAu(Long zhTitleHash, Long enTitleHash, Long issueHash, Long auNameHash,
      Long jFingerPrint, Integer pubType, Integer pubYear, Long psnId) {
    Assert.notNull(psnId, "psnId不能为空");
    Assert.notNull(pubType, "pubType不能为空");
    if ((zhTitleHash == null && enTitleHash == null) || (issueHash == null && auNameHash == null)
        || jFingerPrint == null) {
      return null;
    }
    zhTitleHash = zhTitleHash == null ? enTitleHash : zhTitleHash;
    enTitleHash = enTitleHash == null ? zhTitleHash : enTitleHash;
    List<Object> param = new ArrayList<Object>();
    StringBuilder sb = new StringBuilder();
    sb.append("select t.id from PublicationConfirm t where t.psnId = ? and t.confirmResult = 0 and exists(");
    param.add(psnId);
    sb.append(
        "select t1.rolPubId from PubConfirmRolPubDupFields t1 where  t.rolPubId = t1.rolPubId and t1.pubType = ? and (t1.zhTitleHash = ? or t1.enTitleHash = ? ) ");
    param.add(pubType);
    // 标题
    param.add(zhTitleHash);
    param.add(enTitleHash);
    // 两个都不为空
    if (issueHash != null && auNameHash != null) {
      sb.append(" and (t1.issueHash = ? or t1.auNameHash = ? ) ");
      param.add(issueHash);
      param.add(auNameHash);
    } else if (issueHash != null) {
      sb.append(" and t1.issueHash = ? ");
      param.add(issueHash);
    } else if (auNameHash != null) {
      sb.append(" and t1.auNameHash = ? ");
      param.add(auNameHash);
    }
    sb.append(" and t1.jfingerPrint = ? ");
    param.add(jFingerPrint);
    if (pubYear != null) {
      sb.append(" and t1.pubYear = ?  ");
      param.add(pubYear);
    }
    sb.append(")");
    return super.createQuery(sb.toString(), param.toArray()).list();
  }

  /**
   * 通过期刊指纹查找期刊重复.
   * 
   * @param jaFingerPrint
   * @param jpFingerPrint
   * @param pubYear
   * @param psnId
   * @return
   */
  @SuppressWarnings("unchecked")
  public List<Long> findDupByJFingerPrint(Long jaFingerPrint, Long jpFingerPrint, Integer pubType, Integer pubYear,
      Long psnId) {
    Assert.notNull(psnId, "psnId不能为空");
    Assert.notNull(pubType, "pubType不能为空");

    if ((jaFingerPrint == null && jpFingerPrint == null) || pubYear == null) {
      return null;
    }
    List<Object> param = new ArrayList<Object>();
    StringBuilder sb = new StringBuilder();
    sb.append("select t.id from PublicationConfirm t where t.psnId = ? and t.confirmResult = 0 and exists(");
    param.add(psnId);
    sb.append("select t1.rolPubId from PubConfirmRolPubDupFields t1 where t.rolPubId = t1.rolPubId ");
    if (jaFingerPrint != null && jpFingerPrint != null) {
      sb.append(" and (t1.jaFingerPrint = ? or t1.jpFingerPrint = ? )");
      param.add(jaFingerPrint);
      param.add(jpFingerPrint);
    } else if (jaFingerPrint != null) {
      sb.append(" and t1.jaFingerPrint = ? ");
      param.add(jaFingerPrint);
    } else if (jpFingerPrint != null) {
      sb.append(" and t1.jpFingerPrint = ? ");
      param.add(jpFingerPrint);
    }
    sb.append(" and t1.pubType = ? ");
    param.add(pubType);
    sb.append(" and t1.pubYear = ? ");
    param.add(pubYear);
    sb.append(")");
    return super.createQuery(sb.toString(), param.toArray()).list();
  }

  /**
   * 通过会议名称查找会议重复.
   * 
   * @param jaFingerPrint
   * @param jpFingerPrint
   * @param pubYear
   * @param psnId
   * @return
   */
  @SuppressWarnings("unchecked")
  public List<Long> findDupByConf(Long confnHash, Long zhTitleHash, Long enTitleHash, Integer pubType, Long psnId) {

    Assert.notNull(psnId, "psnId不能为空");
    Assert.notNull(pubType, "pubType不能为空");
    if (confnHash == null || (zhTitleHash == null && enTitleHash == null)) {
      return null;
    }
    List<Object> param = new ArrayList<Object>();
    zhTitleHash = zhTitleHash == null ? enTitleHash : zhTitleHash;
    enTitleHash = enTitleHash == null ? zhTitleHash : enTitleHash;
    StringBuilder sb = new StringBuilder();
    sb.append("select t.id from PublicationConfirm t where t.psnId = ? and t.confirmResult = 0 and exists(");
    param.add(psnId);
    sb.append(
        "select t1.rolPubId from PubConfirmRolPubDupFields t1 where t.rolPubId = t1.rolPubId and (t1.zhTitleHash = ? or t1.enTitleHash = ? ) and t1.pubType = ? and  t1.confnHash = ? ");
    param.add(zhTitleHash);
    param.add(enTitleHash);
    param.add(pubType);
    param.add(confnHash);
    sb.append(")");
    return super.createQuery(sb.toString(), param.toArray()).list();
  }

  /**
   * 通过会议指纹查找会议重复.
   * 
   * @param jaFingerPrint
   * @param jpFingerPrint
   * @param pubYear
   * @param psnId
   * @return
   */
  @SuppressWarnings("unchecked")
  public List<Long> findDupByCFingerPrint(Long cpFingerPrint, Long caFingerPrint, Long zhTitleHash, Long enTitleHash,
      Integer pubType, Integer pubYear, Long psnId) {
    Assert.notNull(psnId, "psnId不能为空");
    Assert.notNull(pubType, "pubType不能为空");
    if ((cpFingerPrint == null && caFingerPrint == null) || pubYear == null
        || (zhTitleHash == null && enTitleHash == null)) {
      return null;
    }
    List<Object> param = new ArrayList<Object>();
    zhTitleHash = zhTitleHash == null ? enTitleHash : zhTitleHash;
    enTitleHash = enTitleHash == null ? zhTitleHash : enTitleHash;
    StringBuilder sb = new StringBuilder();
    sb.append("select t.id from PublicationConfirm t where t.psnId = ? and t.confirmResult = 0 and exists(");
    param.add(psnId);
    sb.append(
        "select t1.rolPubId from PubConfirmRolPubDupFields t1 where t.rolPubId = t1.rolPubId and (t1.zhTitleHash = ? or t1.enTitleHash = ? ) and t1.pubType = ? and t1.pubYear = ? ");
    param.add(zhTitleHash);
    param.add(enTitleHash);
    param.add(pubType);
    param.add(pubYear);
    // 会议指纹
    if (cpFingerPrint != null && caFingerPrint != null) {
      sb.append(" and ( t1.cpFingerPrint = ? or t1.caFingerPrint = ? ) ");
      param.add(cpFingerPrint);
      param.add(caFingerPrint);
    } else if (cpFingerPrint != null) {
      sb.append(" and t1.cpFingerPrint = ? ");
      param.add(cpFingerPrint);
    } else if (caFingerPrint != null) {
      sb.append(" and t1.caFingerPrint = ? ");
      param.add(caFingerPrint);
    }
    sb.append(")");
    return super.createQuery(sb.toString(), param.toArray()).list();
  }

  /**
   * 通过会议指纹查找会议重复.
   * 
   * @param jaFingerPrint
   * @param jpFingerPrint
   * @param pubYear
   * @param psnId
   * @return
   */
  @SuppressWarnings("unchecked")
  public List<Long> findDupByCFingerPrint(Long cvaFingerPrint, Long cvpFingerPrint, Integer pubType, Integer pubYear,
      Long psnId) {
    Assert.notNull(psnId, "psnId不能为空");
    Assert.notNull(pubType, "pubType不能为空");
    if ((cvaFingerPrint == null && cvpFingerPrint == null) || pubYear == null) {
      return null;
    }
    List<Object> param = new ArrayList<Object>();
    StringBuilder sb = new StringBuilder();
    sb.append("select t.id from PublicationConfirm t where t.psnId = ? and t.confirmResult = 0 and exists(");
    param.add(psnId);
    sb.append(
        "select t1.rolPubId from PubConfirmRolPubDupFields t1 where t.rolPubId = t1.rolPubId and t1.pubType = ? and t1.pubYear = ? ");
    param.add(pubType);
    param.add(pubYear);
    // 会议指纹
    if (cvaFingerPrint != null && cvpFingerPrint != null) {
      sb.append(" and ( t1.cvaFingerPrint = ? or t1.cvpFingerPrint = ? ) ");
      param.add(cvaFingerPrint);
      param.add(cvpFingerPrint);
    } else if (cvaFingerPrint != null) {
      sb.append(" and t1.cvaFingerPrint = ? ");
      param.add(cvaFingerPrint);
    } else if (cvpFingerPrint != null) {
      sb.append(" and t1.cvpFingerPrint = ? ");
      param.add(cvpFingerPrint);
    }
    sb.append(")");
    return super.createQuery(sb.toString(), param.toArray()).list();
  }

  /**
   * 专利查重.
   * 
   * @param patentHash
   * @param pubType
   * @param ownerId
   * @return
   */
  @SuppressWarnings("unchecked")
  public List<Long> findDupByPatent(Long patentHash, Integer pubType, Long psnId) {

    Assert.notNull(psnId, "psnId不能为空");
    Assert.notNull(pubType, "pubType不能为空");
    if (patentHash == null) {
      return null;
    }
    StringBuilder sb = new StringBuilder();
    sb.append("select t.id from PublicationConfirm t where t.psnId = ? and t.confirmResult = 0 and exists(");
    sb.append("select t1.rolPubId from PubConfirmRolPubDupFields t1 where t1.patentHash = ? and t1.pubType = ? ");
    sb.append(")");
    return super.createQuery(sb.toString(), psnId, patentHash, pubType).list();
  }

  /**
   * 根据成果id和ownerId查找
   * 
   * @param pubId
   * @param psnId
   * @return
   */
  public PubConfirmRolPubDupFields getByPubPsnId(Long pubId, Long psnId) {
    StringBuilder sb = new StringBuilder();
    sb.append("select t from PubConfirmRolPubDupFields t where t.rolPubId=? and t.ownerId=?");
    return super.findUnique(sb.toString(), pubId, psnId);
  }

  /**
   * 根据成果id和ownerId删除
   * 
   * @param rolPubId
   * @param psnId
   * @throws DaoException
   */
  public void deleteByPubPsnId(Long rolPubId, Long psnId) throws DaoException {
    String sql = "delete from PubConfirmRolPubDupFields t where t.rolPubId=? and t.ownerId=?";
    this.createQuery(sql, new Object[] {rolPubId, psnId}).executeUpdate();
  }

  /**
   * 根据成果Long zhTitleHashCode, Long enTitleHashCode, Integer pubType, Integer pubYear, Long psnId查找
   * 
   * @param Long zhTitleHashCode, Long enTitleHashCode, Integer pubType, Integer pubYear, Long psnId
   * @return
   */
  @SuppressWarnings("unchecked")
  public List<Long> getDupPubConfirmNew(Long zhTitleHashCode, Long enTitleHashCode, Integer pubType, Integer pubYear,
      Long psnId) {

    if ((zhTitleHashCode == null && enTitleHashCode == null) || pubYear == null) {
      return null;
    }


    List<Object> param = new ArrayList<Object>();
    StringBuilder sb = new StringBuilder();
    sb.append(
        "select distinct(t.rolPubId) from PublicationConfirm t where t.psnId = ? and t.confirmResult = 0 and exists (");
    param.add(psnId);
    sb.append(
        "select 1 from PubConfirmRolPubDupFields t1 where t.rolPubId = t1.rolPubId and (t1.zhTitleHash = ? or t1.enTitleHash = ? ) and t1.pubType = ? and t1.pubYear = ? )");
    param.add(zhTitleHashCode == null ? 1L : zhTitleHashCode);
    param.add(enTitleHashCode == null ? 1L : enTitleHashCode);
    param.add(pubType);
    param.add(pubYear);

    return super.createQuery(sb.toString(), param.toArray()).list();
  }

}
