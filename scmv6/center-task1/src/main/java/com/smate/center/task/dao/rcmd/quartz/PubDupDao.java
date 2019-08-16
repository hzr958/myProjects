package com.smate.center.task.dao.rcmd.quartz;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Repository;
import org.springframework.util.Assert;

import com.smate.center.task.model.rcmd.quartz.PubDupFields;
import com.smate.core.base.utils.data.RcmdHibernateDao;

/**
 * 成果查重Dao.
 * 
 * @author liqinghua
 * 
 */
@Repository
public class PubDupDao extends RcmdHibernateDao<PubDupFields, Long> {

  public List<PubDupFields> findDupByIsiId(Long isiIdHash, Long ownerId, Integer status) {
    String hql =
        "select new PubDupFields(pubId, sourceDbId)  from PubDupFields where isiHash = ? and ownerId = ? and status = ? and articleType = 1 ";
    return super.createQuery(hql, isiIdHash, ownerId, status).list();
  }

  /**
   * 查找sps_id重复的成果.
   * 
   * @param isiHash
   * @param ownerId
   * @return
   */
  @SuppressWarnings("unchecked")
  public List<PubDupFields> findDupBySpsId(Long spsIdHash, Long ownerId, Integer status) {

    String hql =
        "select new PubDupFields(pubId, sourceDbId) from PubDupFields where spsHash = ? and ownerId = ? and status = ? and articleType = 1 ";
    return super.createQuery(hql, spsIdHash, ownerId, status).list();
  }

  /**
   * 查找ei_id重复的成果.
   * 
   * @param isiHash
   * @param ownerId
   * @return
   */
  public List<PubDupFields> findDupByEiId(Long eiIdHash, Long ownerId, Integer status) {
    String hql =
        "select new PubDupFields(pubId, sourceDbId) from PubDupFields where eiHash = ? and ownerId = ? and status = ? and articleType = 1 ";
    return super.createQuery(hql, eiIdHash, ownerId, status).list();
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
  public List<PubDupFields> findDupByDoi(Long doiHash, Integer pubYear, Long ownerId, Integer status) {
    if (doiHash == null || pubYear == null) {
      return null;
    }
    String hql =
        "select new PubDupFields(pubId, sourceDbId) from PubDupFields where doiHash = ? and pubYear = ?  and ownerId = ? and status = ? and articleType = 1 ";
    return super.createQuery(hql, doiHash, pubYear, ownerId, status).list();
  }

  /**
   * 通过期刊指纹查找期刊重复.
   * 
   * @param jaFingerPrint
   * @param jpFingerPrint
   * @param pubYear
   * @param ownerId
   * @return
   */
  @SuppressWarnings("unchecked")
  public List<PubDupFields> findDupByIssueAu(Long zhTitleHash, Long enTitleHash, Long issueHash, Long auNameHash,
      Long jFingerPrint, Integer pubType, Integer pubYear, Long ownerId, Integer stauts) {
    Assert.notNull(ownerId, "ownerId不能为空");
    Assert.notNull(pubType, "pubType不能为空");
    if ((zhTitleHash == null && enTitleHash == null) || (issueHash == null && auNameHash == null)
        || jFingerPrint == null) {
      return null;
    }
    zhTitleHash = zhTitleHash == null ? enTitleHash : zhTitleHash;
    enTitleHash = enTitleHash == null ? zhTitleHash : enTitleHash;
    List<Object> param = new ArrayList<Object>();
    StringBuilder sb = new StringBuilder();
    sb.append(
        "select new PubDupFields(pubId, sourceDbId) from PubDupFields where status = ?  and pubType = ?  and articleType = 1 and (zhTitleHash = ? or enTitleHash = ? ) ");
    param.add(stauts);
    param.add(pubType);
    // 标题
    param.add(zhTitleHash);
    param.add(enTitleHash);
    // 两个都不为空
    if (issueHash != null && auNameHash != null) {
      sb.append(" and (issueHash = ? or auNameHash = ? ) ");
      param.add(issueHash);
      param.add(auNameHash);
    } else if (issueHash != null) {
      sb.append(" and issueHash = ? ");
      param.add(issueHash);
    } else if (auNameHash != null) {
      sb.append(" and auNameHash = ? ");
      param.add(auNameHash);
    }
    sb.append(" and jFingerPrint = ? ");
    param.add(jFingerPrint);
    if (pubYear != null) {
      sb.append(" and pubYear = ?  ");
      param.add(pubYear);
    }
    sb.append(" and ownerId = ?  ");
    param.add(ownerId);
    return super.createQuery(sb.toString(), param.toArray()).list();
  }

  /**
   * 通过期刊指纹查找期刊重复.
   * 
   * @param jaFingerPrint
   * @param jpFingerPrint
   * @param pubYear
   * @param ownerId
   * @return
   */
  @SuppressWarnings("unchecked")
  public List<PubDupFields> findDupByJFingerPrint(Long jaFingerPrint, Long jpFingerPrint, Integer pubType,
      Integer pubYear, Long ownerId, Integer status) {
    Assert.notNull(ownerId, "ownerId不能为空");
    Assert.notNull(pubType, "pubType不能为空");

    if ((jaFingerPrint == null && jpFingerPrint == null) || pubYear == null) {
      return null;
    }
    List<Object> param = new ArrayList<Object>();
    StringBuilder sb = new StringBuilder();
    sb.append("select new PubDupFields(pubId, sourceDbId) from PubDupFields where status = ?   and articleType = 1 ");
    param.add(status);
    if (jaFingerPrint != null && jpFingerPrint != null) {
      sb.append(" and (jaFingerPrint = ? or jpFingerPrint = ? )");
      param.add(jaFingerPrint);
      param.add(jpFingerPrint);
    } else if (jaFingerPrint != null) {
      sb.append(" and jaFingerPrint = ? ");
      param.add(jaFingerPrint);
    } else if (jpFingerPrint != null) {
      sb.append(" and jpFingerPrint = ? ");
      param.add(jpFingerPrint);
    }
    sb.append(" and pubType = ? ");
    param.add(pubType);
    sb.append(" and pubYear = ? ");
    param.add(pubYear);
    sb.append(" and ownerId = ?  ");
    param.add(ownerId);
    return super.createQuery(sb.toString(), param.toArray()).list();
  }

  /**
   * 通过会议名称查找会议重复.
   * 
   * @param jaFingerPrint
   * @param jpFingerPrint
   * @param pubYear
   * @param ownerId
   * @return
   */
  @SuppressWarnings("unchecked")
  public List<PubDupFields> findDupByConf(Long confnHash, Long zhTitleHash, Long enTitleHash, Integer pubType,
      Long ownerId, Integer status) {

    Assert.notNull(ownerId, "ownerId不能为空");
    Assert.notNull(pubType, "pubType不能为空");
    if (confnHash == null || (zhTitleHash == null && enTitleHash == null)) {
      return null;
    }
    List<Object> param = new ArrayList<Object>();
    zhTitleHash = zhTitleHash == null ? enTitleHash : zhTitleHash;
    enTitleHash = enTitleHash == null ? zhTitleHash : enTitleHash;
    StringBuilder sb = new StringBuilder();
    sb.append(
        "select new PubDupFields(pubId, sourceDbId) from PubDupFields where (zhTitleHash = ? or enTitleHash = ? ) and pubType = ? and  confnHash = ?  and ownerId = ? and status = ? and articleType = 1 ");
    param.add(zhTitleHash);
    param.add(enTitleHash);
    param.add(pubType);
    param.add(confnHash);
    param.add(ownerId);
    param.add(status);
    return super.createQuery(sb.toString(), param.toArray()).list();
  }

  /**
   * 通过会议指纹查找会议重复.
   * 
   * @param jaFingerPrint
   * @param jpFingerPrint
   * @param pubYear
   * @param ownerId
   * @return
   */
  @SuppressWarnings("unchecked")
  public List<PubDupFields> findDupByCFingerPrint(Long cpFingerPrint, Long caFingerPrint, Long zhTitleHash,
      Long enTitleHash, Integer pubType, Integer pubYear, Long ownerId, Integer status) {
    Assert.notNull(ownerId, "ownerId不能为空");
    Assert.notNull(pubType, "pubType不能为空");
    if ((cpFingerPrint == null && caFingerPrint == null) || pubYear == null
        || (zhTitleHash == null && enTitleHash == null)) {
      return null;
    }
    List<Object> param = new ArrayList<Object>();
    zhTitleHash = zhTitleHash == null ? enTitleHash : zhTitleHash;
    enTitleHash = enTitleHash == null ? zhTitleHash : enTitleHash;
    StringBuilder sb = new StringBuilder();
    sb.append(
        "select new PubDupFields(pubId, sourceDbId) from PubDupFields where (zhTitleHash = ? or enTitleHash = ? ) and pubType = ? and pubYear = ? and ownerId = ? and status = ? and articleType = 1 ");
    param.add(zhTitleHash);
    param.add(enTitleHash);
    param.add(pubType);
    param.add(pubYear);
    param.add(ownerId);
    param.add(status);
    // 会议指纹
    if (cpFingerPrint != null && caFingerPrint != null) {
      sb.append(" and ( cpFingerPrint = ? or caFingerPrint = ? ) ");
      param.add(cpFingerPrint);
      param.add(caFingerPrint);
    } else if (cpFingerPrint != null) {
      sb.append(" and cpFingerPrint = ? ");
      param.add(cpFingerPrint);
    } else if (caFingerPrint != null) {
      sb.append(" and caFingerPrint = ? ");
      param.add(caFingerPrint);
    }
    return super.createQuery(sb.toString(), param.toArray()).list();
  }

  /**
   * 通过会议指纹查找会议重复.
   * 
   * @param jaFingerPrint
   * @param jpFingerPrint
   * @param pubYear
   * @param ownerId
   * @return
   */
  @SuppressWarnings("unchecked")
  public List<PubDupFields> findDupByCFingerPrint(Long cvaFingerPrint, Long cvpFingerPrint, Integer pubType,
      Integer pubYear, Long ownerId, Integer status) {
    Assert.notNull(ownerId, "ownerId不能为空");
    Assert.notNull(pubType, "pubType不能为空");
    if ((cvaFingerPrint == null && cvpFingerPrint == null) || pubYear == null) {
      return null;
    }
    List<Object> param = new ArrayList<Object>();
    StringBuilder sb = new StringBuilder();
    sb.append(
        "select new PubDupFields(pubId, sourceDbId) from PubDupFields where pubType = ? and pubYear = ? and ownerId = ? and status = ? and articleType = 1 ");
    param.add(pubType);
    param.add(pubYear);
    param.add(ownerId);
    param.add(status);
    // 会议指纹
    if (cvaFingerPrint != null && cvpFingerPrint != null) {
      sb.append(" and ( cvaFingerPrint = ? or cvpFingerPrint = ? ) ");
      param.add(cvaFingerPrint);
      param.add(cvpFingerPrint);
    } else if (cvaFingerPrint != null) {
      sb.append(" and cvaFingerPrint = ? ");
      param.add(cvaFingerPrint);
    } else if (cvpFingerPrint != null) {
      sb.append(" and cvpFingerPrint = ? ");
      param.add(cvpFingerPrint);
    }
    return super.createQuery(sb.toString(), param.toArray()).list();
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
  public List<PubDupFields> findDupByPatent(Long patentHash, Long patentOpenHash, Integer pubType, Long ownerId,
      Integer status) {

    Assert.notNull(ownerId, "ownerId不能为空");
    Assert.notNull(pubType, "pubType不能为空");
    List<Object> params = new ArrayList<Object>();
    String hql =
        "select new PubDupFields(pubId, sourceDbId) from PubDupFields where pubType = ? and ownerId = ? and status = ? and articleType = 1 ";
    params.add(pubType);
    params.add(ownerId);
    params.add(status);
    if (patentHash != null && patentOpenHash != null) {
      hql += " and (patentHash = ? or patentOpenNoHash = ? )";
      params.add(patentHash);
      params.add(patentOpenHash);
    } else if (patentHash != null) {
      hql += " and patentHash = ? ";
      params.add(patentHash);
    } else if (patentOpenHash != null) {
      hql += " and patentOpenNoHash = ? ";
      params.add(patentOpenHash);
    } else {
      return null;
    }

    return super.createQuery(hql, params.toArray()).list();
  }

  /**
   * 通过标题查找重复成果.
   * 
   * @param jaFingerPrint
   * @param jpFingerPrint
   * @param pubYear
   * @param ownerId
   * @return
   */
  @SuppressWarnings("unchecked")
  public List<PubDupFields> findDupByTitle(Long zhTitleHash, Long enTitleHash, Integer pubYear, Integer pubType,
      Long ownerId, Integer status) {

    if ((zhTitleHash == null && enTitleHash == null) || ownerId == null) {
      return null;
    }
    ArrayList<Object> param = new ArrayList<Object>();
    zhTitleHash = zhTitleHash == null ? enTitleHash : zhTitleHash;
    enTitleHash = enTitleHash == null ? zhTitleHash : enTitleHash;
    StringBuilder sb = new StringBuilder();
    sb.append(
        "select new PubDupFields(pubId, sourceDbId) from PubDupFields where (zhTitleHash = ? or enTitleHash = ? ) and pubType = ?  and ownerId = ? and status = ?  and articleType = 1 ");
    param.add(zhTitleHash);
    param.add(enTitleHash);
    param.add(pubType);
    param.add(ownerId);
    param.add(status);
    if (pubYear != null) {
      sb.append(" and pubYear = ? ");
      param.add(pubYear);
    }
    return super.createQuery(sb.toString(), param.toArray()).list();
  }

}
