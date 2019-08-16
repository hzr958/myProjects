package com.smate.web.v8pub.dao.pdwh;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringEscapeUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Repository;

import com.smate.core.base.pub.enums.PubPdwhStatusEnum;
import com.smate.core.base.utils.data.PdwhHibernateDao;
import com.smate.web.v8pub.po.PubPO;
import com.smate.web.v8pub.po.pdwh.PubPdwhPO;
import com.smate.web.v8pub.service.query.PubQueryDTO;

/**
 * @author houchuanjie
 * @date 2018/06/01 17:44
 */
@Repository
public class PubPdwhDAO extends PdwhHibernateDao<PubPdwhPO, Long> {

  @SuppressWarnings("unchecked")
  public void findByIds(PubQueryDTO pubQueryDTO) {
    StringBuilder hql = new StringBuilder();
    if (pubQueryDTO.getPubIds() != null && pubQueryDTO.getPubIds().size() > 0) {
      String pubIds = "";
      for (Long pubId : pubQueryDTO.getPubIds()) {
        pubIds = pubIds + pubId + ",";
      }
      pubIds = pubIds.substring(0, pubIds.length() - 1);
      hql.append("from PubPdwhPO p where p.pubId in (" + pubIds + ") and p.status = 0 ");

      if ("pubId".equals(pubQueryDTO.getOrderBy())) {
        hql.append(" order by instr('" + pubIds + "',p.pubId)");
      }
      List<PubPO> list = super.createQuery(hql.toString()).list();
      if (CollectionUtils.isNotEmpty(list)) {
        if (pubQueryDTO.isAll == 1) {
          pubQueryDTO.setPubList(list);
        } else {
          List<PubPO> pubs = new ArrayList<PubPO>();
          pubs.add(list.get(0));
          pubQueryDTO.setPubList(pubs);
        }
      }

    }
  }

  @SuppressWarnings("unchecked")
  public void findByIdsforSie(PubQueryDTO pubQueryDTO) {
    StringBuilder hql = new StringBuilder();
    if (pubQueryDTO.getPubIds() != null && pubQueryDTO.getPubIds().size() > 0) {
      String pubIds = "";
      for (Long pubId : pubQueryDTO.getPubIds()) {
        pubIds = pubIds + pubId + ",";
      }
      pubIds = pubIds.substring(0, pubIds.length() - 1);
      hql.append("from PubPdwhPO p where p.pubId in (" + pubIds + ") and p.status = 0 ");
      if (StringUtils.isNotBlank(pubQueryDTO.getPubType())) {
        hql.append(" and p.pubType in (" + pubQueryDTO.getPubType() + " ) ");
      }
      if ("pubId".equals(pubQueryDTO.getOrderBy())) {
        hql.append(" order by instr('" + pubIds + "',p.pubId)");
      }
      List<PubPO> list = super.createQuery(hql.toString()).list();
      if (CollectionUtils.isNotEmpty(list)) {
        pubQueryDTO.setTotalCount(Long.parseLong(Integer.toString(list.size())));
      } else {
        pubQueryDTO.setTotalCount(0L);
      }
      pubQueryDTO.setPubList(list);


    }
  }



  /**
   * 查找 所有成果 ，包括删除的
   * 
   * @param pubQueryDTO
   */
  public void findAllByIds(PubQueryDTO pubQueryDTO) {
    StringBuilder hql = new StringBuilder();
    if (pubQueryDTO.getPubIds() != null && pubQueryDTO.getPubIds().size() > 0) {
      String pubIds = "";
      for (Long pubId : pubQueryDTO.getPubIds()) {
        pubIds = pubIds + pubId + ",";
      }
      pubIds = pubIds.substring(0, pubIds.length() - 1);
      hql.append("from PubPdwhPO p where p.pubId in (" + pubIds + ")  ");

      if ("pubId".equals(pubQueryDTO.getOrderBy())) {
        hql.append(" order by instr('" + pubIds + "',p.pubId)");
      }
      List<PubPO> list = super.createQuery(hql.toString()).list();
      if (CollectionUtils.isNotEmpty(list)) {
        if (pubQueryDTO.isAll == 1) {
          pubQueryDTO.setPubList(list);
        } else {
          List<PubPO> pubs = new ArrayList<PubPO>();
          pubs.add(list.get(0));
          pubQueryDTO.setPubList(pubs);
        }
      }

    }
  }

  @SuppressWarnings("unchecked")
  public List<PubPdwhPO> getCollectedPubs(PubQueryDTO pubQueryDTO, List<Long> pubIdList) {
    StringBuilder hql = new StringBuilder();
    List<Object> params = new ArrayList<Object>();
    hql.append("from PubPdwhPO t ");
    String string = pubIdList.toString();
    String substring = string.substring(1, string.length() - 1);
    hql.append(" where t.pubId in (" + substring + ") and t.status = 0 ");
    // 构建检索条件
    buildSearchKey(pubQueryDTO.getSearchKey(), hql, params);
    // 发表年份
    if (StringUtils.isNotEmpty(pubQueryDTO.getPublishYear())) {
      hql.append(" and t.publishYear in (" + pubQueryDTO.getPublishYear() + ") ");
    }
    // 成果类型
    if (StringUtils.isNotEmpty(pubQueryDTO.getPubType())) {
      hql.append(" and t.pubType in(" + pubQueryDTO.getPubType() + ") ");
    }
    // 收录类别
    buildSituationSql(pubQueryDTO, hql);

    return super.createQuery(hql.toString(), params.toArray()).list();
  }

  private void buildSituationSql(PubQueryDTO pubQueryDTO, StringBuilder hql) {
    if (StringUtils.isNotBlank(pubQueryDTO.getIncludeType())) {
      String includeType = Stream.of(pubQueryDTO.getIncludeType().split(","))
          .map(item -> "'" + item.trim().toUpperCase() + "'").collect(Collectors.joining(","));
      includeType = includeType.replaceAll("(?i)'scie?'", "'SCIE','SCI'");// scie和sci都有

      hql.append(" and  exists (select 1 from PdwhPubSituationPO ps ");

      String notIncludeType = "'EI','SCI','SSCI','ISTP','CSSCI','PKU'";
      if (includeType.contains("other")) {
        hql.append(" where ps.pdwhPubId = t.pubId  and (ps.libraryName in ( " + includeType
            + " ) or ps.libraryName not in ( " + notIncludeType + " ) )  and ps.sitStatus = 1)");
      } else {
        hql.append(
            " where ps.pdwhPubId = t.pubId  and ps.libraryName in ( " + includeType + " )  and ps.sitStatus = 1)");
      }
    }

  }

  /**
   * 构建检索条件
   * 
   * @param form
   * @param hql
   * @param params
   */
  private void buildSearchKey(String searchKey, StringBuilder hql, List<Object> params) {
    if (StringUtils.isNotEmpty(searchKey)) {
      searchKey = StringEscapeUtils.unescapeHtml4(searchKey);
      searchKey = searchKey.toUpperCase().trim();
      hql.append(" and ");
      hql.append(" ( ");
      hql.append(" instr(upper(t.title),?)>0");// 中文标题
      params.add(searchKey);
      hql.append(" or instr(upper(t.briefDesc),?)>0");// 来源
      params.add(searchKey);
      hql.append(" or instr(upper(t.authorNames),?)>0");// 作者
      params.add(searchKey);
      hql.append(" ) ");
    }
  }

  public List<PubPdwhPO> getCollectedPubs(List<Long> pubPdwhIds) {
    String hql = "from PubPdwhPO p where p.pubId in (:ids) and p.status = 0 ";
    return this.createQuery(hql).setParameterList("ids", pubPdwhIds).list();
  }

  public Long getPubCount(List<Long> pubPdwhIds) {
    String hql = "select count(pubId) from PubPdwhPO p where p.pubId in (:ids) and p.status = 0 ";
    return (Long) this.createQuery(hql).setParameterList("ids", pubPdwhIds).uniqueResult();
  }

  /**
   * 校验基准库成果是否删除
   * 
   * @param pubId
   * @return
   */
  public boolean checkPdwhIsDel(Long pubId, PubPdwhStatusEnum status) {
    String HQL = "select count(1) from PubPdwhPO t where t.pubId = :pubId and t.status = :status";
    Long count =
        (Long) getSession().createQuery(HQL).setParameter("pubId", pubId).setParameter("status", status).uniqueResult();
    return count != null && count > 0;
  }

  public boolean pubExists(Long pdwhPubId) {
    String hql = "from PubPdwhPO p where p.pubId = :id and p.status = 0 ";
    PubPdwhPO pubPdwh = (PubPdwhPO) this.createQuery(hql).setParameter("id", pdwhPubId).uniqueResult();
    if (pubPdwh != null) {
      return true;
    }
    return false;

  }

  @SuppressWarnings("unchecked")
  public List<Long> findExistsPubIds(List<Long> dupPubIds) {
    String hql = "select p.pubId from PubPdwhPO p where p.pubId in(:ids) and p.status = 0 ";
    return this.createQuery(hql).setParameterList("ids", dupPubIds).list();
  }

  /**
   * 更新基准库成果状态
   * 
   * @param pubId
   */
  public void updateStatus(Long pubId, PubPdwhStatusEnum status) {
    String hql = "update PubPdwhPO t set t.status = :status,t.gmtModified=:gmtModified where t.pubId= :pubId";
    super.createQuery(hql).setParameter("status", status).setParameter("pubId", pubId)
        .setParameter("gmtModified", new Date()).executeUpdate();

  }

  @SuppressWarnings("unchecked")
  public List<PubPdwhPO> getPrjPubs(PubQueryDTO pubQueryDTO, List<Long> pubIdList) {
    StringBuilder hql = new StringBuilder();
    List<Object> params = new ArrayList<Object>();
    hql.append("from PubPdwhPO t ");
    String string = pubIdList.toString();
    String substring = string.substring(1, string.length() - 1);
    hql.append(" where t.pubId in (" + substring + ") and t.status = 0 ");
    // 构建检索条件
    buildSearchKey(pubQueryDTO.getSearchKey(), hql, params);
    // 成果类型
    if (StringUtils.isNotEmpty(pubQueryDTO.getPubType())) {
      hql.append(" and t.pubType in(" + pubQueryDTO.getPubType() + ") ");
    }
    return super.createQuery(hql.toString(), params.toArray()).list();
  }
}
