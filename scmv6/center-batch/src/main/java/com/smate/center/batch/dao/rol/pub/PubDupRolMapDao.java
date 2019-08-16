package com.smate.center.batch.dao.rol.pub;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.hibernate.Query;
import org.springframework.stereotype.Repository;
import org.springframework.web.util.HtmlUtils;

import com.smate.center.batch.constant.PublicationRolStatusEnum;
import com.smate.center.batch.exception.pub.DaoException;
import com.smate.center.batch.model.rol.pub.PublicationRol;
import com.smate.center.batch.model.rol.pub.PublicationRolQueryForm;
import com.smate.core.base.utils.constant.ScmRolRoleConstants;
import com.smate.core.base.utils.data.RolHibernateDao;
import com.smate.core.base.utils.model.Page;
import com.smate.core.base.utils.security.SecurityUtils;

/**
 * 成果查重结果存储DAO.
 * 
 * @author yamingd
 * 
 */
@Repository
public class PubDupRolMapDao extends RolHibernateDao<PublicationRol, Long> {

  /**
   * 使用oracle序列生成组别ID.
   * 
   * @return Long
   * @throws DaoException
   */
  public Long getNextGroupId() throws DaoException {

    BigDecimal groupId =
        (BigDecimal) super.getSession().createSQLQuery("select SEQ_PUB_DUP_GROUP.nextval from dual").uniqueResult();
    return groupId.longValue();
  }

  /**
   * 保存查重结果到组(groupId).
   * 
   * @param pub 成果
   * @param insId 单位ID
   * @param groupId 组ID
   * @throws DaoException
   */
  public void saveDupResult(long pubId, Long groupId) throws DaoException {
    String hql = null;
    if (groupId == null) {
      hql = "update PublicationRol set dupGroupId=null where id=?";
      super.createQuery(hql, pubId).executeUpdate();
    } else {
      hql = "update PublicationRol set dupGroupId=? where id=?";
      super.createQuery(hql, groupId, pubId).executeUpdate();
    }

  }

  /**
   * 将成果加入分组.
   * 
   * @param pubId
   * @param groupId
   */
  public void addPubToGroup(List<Long> pubId, Long groupId) {

    if (CollectionUtils.isEmpty(pubId)) {
      return;
    }
    String hql = "update PublicationRol set dupGroupId=:groupId where id in(:ids)";
    super.createQuery(hql).setParameter("groupId", groupId).setParameterList("ids", pubId).executeUpdate();
  }

  /**
   * 将多个成果分组加入另一个分组.
   * 
   * @param pubId
   * @param groupId
   */
  public void addPubGroupToGroup(List<Long> groupIds, Long groupId) {

    if (CollectionUtils.isEmpty(groupIds)) {
      return;
    }
    String hql = "update PublicationRol set dupGroupId=:groupId where dupGroupId in(:groupIds)";
    super.createQuery(hql).setParameter("groupId", groupId).setParameterList("groupIds", groupIds).executeUpdate();
  }

  /**
   * 查询主体，查询条件拼接.
   * 
   * @param insId 单位ID
   * @param query 查询条件.
   * @param page 返回结果集合.
   * @throws DaoException
   */
  @SuppressWarnings({"unchecked", "rawtypes"})
  public void searchRolPub(long insId, PublicationRolQueryForm query, Page<PublicationRol> page) throws DaoException {

    // 1.先查出组别
    // 参数
    List<Object> params = new ArrayList<Object>();
    // 构造通用查询条件
    String commonCondition = buildCommonCondition(insId, query, params);

    StringBuilder countGroupSql = new StringBuilder();
    countGroupSql.append("select count(dup_group_id ) from ");
    countGroupSql.append("(select t.dup_group_id ");
    countGroupSql.append(commonCondition);
    countGroupSql.append(" group by t.dup_group_id having count(t.dup_group_id) > 1 )");

    // 总组别数
    Long totalGroup = super.queryForLong(countGroupSql.toString(), params.toArray());
    page.setTotalCount(totalGroup);
    // 无分组，则不进入下一步
    if (totalGroup == 0) {
      return;
    }

    // 查询组别
    StringBuilder listGroupSql = new StringBuilder();
    listGroupSql.append("select dup_group_id from (");
    listGroupSql.append("select dup_group_id,rownum rn from(");
    listGroupSql.append("select t.dup_group_id ").append(commonCondition);
    listGroupSql.append(" group by t.dup_group_id having count(t.dup_group_id) > 1 order by dup_group_id desc ");
    listGroupSql.append(")");
    listGroupSql.append(" ) where rn >= ? and rn < ? ");
    List<Object> listGroupParams = new ArrayList<Object>();
    listGroupParams.addAll(params);
    listGroupParams.add(page.getFirst());
    listGroupParams.add(page.getFirst() + page.getPageSize());
    List<Map> list = super.queryForList(listGroupSql.toString(), listGroupParams.toArray());

    List<Long> dupList = new ArrayList<Long>();
    for (Map map : list) {
      BigDecimal dupId = (BigDecimal) map.get("DUP_GROUP_ID");
      dupList.add(dupId.longValue());
    }

    // 2.遍历组别的成果
    StringBuilder listPubSql = new StringBuilder();
    listPubSql.append("select * ");
    listPubSql.append(commonCondition);
    listPubSql.append(" and dup_group_id in(").append(StringUtils.join(dupList, ",")).append(")");
    listPubSql.append(" order by dup_group_id ");
    if (StringUtils.isNotBlank(page.getOrder())) {
      listPubSql.append(page.getOrder());
    } else {
      listPubSql.append("desc");
    }
    List<Map> listPub = super.queryForList(listPubSql.toString(), params.toArray());
    List<PublicationRol> pubList = this.covertMapToPub(listPub);
    page.setResult(pubList);
  }

  /**
   * 将SQL查询的MAP数据转换成publication对象的数据.
   * 
   * @param listPub
   * @return
   */
  @SuppressWarnings("rawtypes")
  public List<PublicationRol> covertMapToPub(List<Map> listPub) {

    List<PublicationRol> pubList = new ArrayList<PublicationRol>();
    for (Map map : listPub) {
      PublicationRol pubRol = new PublicationRol();
      Long pubId = Long.valueOf(map.get("PUB_ID").toString());
      Integer fulltextNodeId =
          map.get("FULLTEXT_NODEID") == null ? null : Integer.valueOf(map.get("FULLTEXT_NODEID").toString());
      Long insId = map.get("INS_ID") == null ? null : Long.valueOf(map.get("INS_ID").toString());
      Integer status = map.get("STATUS") == null ? null : Integer.valueOf(map.get("STATUS").toString());
      String authorNames = map.get("AUTHOR_NAMES") == null ? null : (String) map.get("AUTHOR_NAMES");
      String citedList = map.get("CITED_LIST") == null ? null : (String) map.get("CITED_LIST");
      String fulltextFileid = map.get("FULLTEXT_FILEID") == null ? null : (String) map.get("FULLTEXT_FILEID");
      String fulltextUrl = map.get("FULLTEXT_URL") == null ? null : (String) map.get("FULLTEXT_URL");
      String briefDesc = map.get("BRIEF_DESC") == null ? null : (String) map.get("BRIEF_DESC");
      String citedUrl = map.get("CITED_URL") == null ? null : (String) map.get("CITED_URL");
      pubRol.setId(pubId);
      pubRol.setFulltextNodeId(fulltextNodeId);
      pubRol.setInsId(insId);
      pubRol.setStatus(status);
      pubRol.setAuthorNames(authorNames);
      pubRol.setCitedList(citedList);
      pubRol.setFulltextFileid(fulltextFileid);
      pubRol.setFulltextUrl(fulltextUrl);
      pubRol.setBriefDesc(briefDesc);
      pubRol.setCitedUrl(citedUrl);
      Integer kpiValid = map.get("KPI_VALID") == null ? null : Integer.valueOf(map.get("KPI_VALID").toString());
      Long firstAuthorPsnId =
          map.get("FIRST_AUTHOR_PSNID") == null ? null : Long.valueOf(map.get("FIRST_AUTHOR_PSNID").toString());
      Long snsPubId = map.get("SNS_PUB_ID") == null ? null : Long.valueOf(map.get("SNS_PUB_ID").toString());
      Integer authorState =
          map.get("AUTHOR_STATE") == null ? null : Integer.valueOf(map.get("AUTHOR_STATE").toString());
      Integer articleType =
          map.get("ARTICLE_TYPE") == null ? null : Integer.valueOf(map.get("ARTICLE_TYPE").toString());
      Integer sourceDbId = map.get("SOURCE_DB_ID") == null ? null : Integer.valueOf(map.get("SOURCE_DB_ID").toString());
      Integer typeId = map.get("PUB_TYPE") == null ? null : Integer.valueOf(map.get("PUB_TYPE").toString());
      Integer publishYear =
          map.get("PUBLISH_YEAR") == null ? null : Integer.valueOf(map.get("PUBLISH_YEAR").toString());
      pubRol.setKpiValid(kpiValid);
      pubRol.setFirstAuthorPsnId(firstAuthorPsnId);
      pubRol.setSnsPubId(snsPubId);
      pubRol.setAuthorState(authorState);
      pubRol.setArticleType(articleType);
      pubRol.setSourceDbId(sourceDbId);
      pubRol.setTypeId(typeId);
      pubRol.setPublishYear(publishYear);

      Integer publishMonth =
          map.get("PUBLISH_MONTH") == null ? null : Integer.valueOf(map.get("PUBLISH_MONTH").toString());
      Integer publishDay = map.get("PUBLISH_DAY") == null ? null : Integer.valueOf(map.get("PUBLISH_DAY").toString());
      Long createPsnId = map.get("CREATE_PSN_ID") == null ? null : Long.valueOf(map.get("CREATE_PSN_ID").toString());
      Integer versionNo = map.get("VERSION_NO") == null ? null : Integer.valueOf(map.get("VERSION_NO").toString());
      Integer regionId = map.get("REGION_ID") == null ? null : Integer.valueOf(map.get("REGION_ID").toString());
      String impactFactors = map.get("IMPACT_FACTORS") == null ? null : (String) map.get("IMPACT_FACTORS");
      Long jid = map.get("JID") == null ? null : Long.valueOf(map.get("JID").toString());
      Integer recordFrom = map.get("RECORD_FROM") == null ? null : Integer.valueOf(map.get("RECORD_FROM").toString());
      pubRol.setPublishMonth(publishMonth);
      pubRol.setPublishDay(publishDay);
      pubRol.setCreatePsnId(createPsnId);
      pubRol.setVersionNo(versionNo);
      pubRol.setRegionId(regionId);
      pubRol.setImpactFactors(impactFactors);
      pubRol.setJid(jid);
      pubRol.setRecordFrom(recordFrom);
      Integer dataValidate = map.get("IS_VALID") == null ? null : Integer.valueOf(map.get("IS_VALID").toString());
      Integer citedTimes = map.get("CITED_TIMES") == null ? null : Integer.valueOf(map.get("CITED_TIMES").toString());
      Date citedDate = map.get("CITED_DATE") == null ? null : (Date) map.get("CITED_DATE");
      String zhTitle = map.get("ZH_TITLE") == null ? null : (String) map.get("ZH_TITLE");
      String enTitle = map.get("EN_TITLE") == null ? null : (String) map.get("EN_TITLE");
      Integer zhTitleHash =
          map.get("ZH_TITLE_HASH") == null ? null : Integer.valueOf(map.get("ZH_TITLE_HASH").toString());
      Integer enTitleHash =
          map.get("EN_TITLE_HASH") == null ? null : Integer.valueOf(map.get("EN_TITLE_HASH").toString());
      Integer fingerPrint =
          map.get("FINGER_PRINT") == null ? null : Integer.valueOf(map.get("FINGER_PRINT").toString());
      Date updateDate = map.get("UPDATE_DATE") == null ? null : (Date) map.get("UPDATE_DATE");
      Long updatePsnId = map.get("UPDATE_PSN_ID") == null ? null : Long.valueOf(map.get("UPDATE_PSN_ID").toString());
      pubRol.setDataValidate(dataValidate);
      pubRol.setCitedTimes(citedTimes);
      pubRol.setCitedDate(citedDate);
      pubRol.setZhTitle(zhTitle);
      pubRol.setEnTitle(enTitle);
      pubRol.setZhTitleHash(zhTitleHash);
      pubRol.setEnTitleHash(enTitleHash);
      pubRol.setFingerPrint(fingerPrint);
      pubRol.setUpdateDate(updateDate);
      pubRol.setUpdatePsnId(updatePsnId);
      String startPage = map.get("START_PAGE") == null ? null : (String) map.get("START_PAGE");
      String endPage = map.get("END_PAGE") == null ? null : (String) map.get("END_PAGE");
      String isbn = map.get("ISBN") == null ? null : (String) map.get("ISBN");
      String volume = map.get("VOLUME") == null ? null : (String) map.get("VOLUME");
      String issue = map.get("ISSUE") == null ? null : (String) map.get("ISSUE");
      String doi = map.get("DOI") == null ? null : (String) map.get("DOI");
      Long dupGroupId = map.get("DUP_GROUP_ID") == null ? null : Long.valueOf(map.get("DUP_GROUP_ID").toString());
      String isiId = map.get("ISI_ID") == null ? null : (String) map.get("ISI_ID");
      String fulltextExt = map.get("FULLTEXT_FILEEXT") == null ? null : (String) map.get("FULLTEXT_FILEEXT");
      Integer confirm =
          map.get("CONFIRM_RESULT") == null ? null : Integer.valueOf(map.get("CONFIRM_RESULT").toString());
      Integer isOpen = map.get("IS_OPEN") == null ? null : Integer.valueOf(map.get("IS_OPEN").toString());
      String zhTitleText = map.get("ZH_TITLE_TEXT") == null ? null : (String) map.get("ZH_TITLE_TEXT");
      String enTitleText = map.get("EN_TITLE_TEXT") == null ? null : (String) map.get("EN_TITLE_TEXT");
      pubRol.setStartPage(startPage);
      pubRol.setEndPage(endPage);
      pubRol.setIsbn(isbn);
      pubRol.setVolume(volume);
      pubRol.setIssue(issue);
      pubRol.setDoi(doi);
      pubRol.setDupGroupId(dupGroupId);
      pubRol.setIsiId(isiId);
      pubRol.setFulltextExt(fulltextExt);
      pubRol.setConfirm(confirm);
      pubRol.setIsOpen(isOpen);
      pubRol.setZhTitleText(zhTitleText);
      pubRol.setEnTitleText(enTitleText);
      pubList.add(pubRol);
    }
    return pubList;
  }

  /**
   * 构造查重合并通用条件.
   * 
   * @param insId
   * @param query
   * @param hql
   * @param params
   */
  private String buildCommonCondition(long insId, PublicationRolQueryForm query, List<Object> params) {
    StringBuilder sql = new StringBuilder();
    sql.append(
        " from publication t where t.dup_group_id is not null and t.ins_id = ? and t.article_type = ? and t.status = ? ");
    params.add(insId);
    params.add(1);
    params.add(PublicationRolStatusEnum.APPROVED);

    // StringBuilder hql = new StringBuilder();
    // hql.append(" where t.dupGroupId is not null and t.insId = ? and t.articleType=? ");
    // params.add(insId);
    // params.add(1);
    // hql.append(" and t.status = ? ");
    // params.add(PublicationRolStatusEnum.APPROVED);

    // 如果是简单查询
    if (StringUtils.isNotBlank(query.getSimpleSearchContent())) {
      sql.append(
          " and (t.zh_title_text like ? or t.en_title_text like ? or t.author_names like ? or t.brief_desc like ? or t.brief_desc_en like ? ");
      sql.append(
          " or exists (select b.type_id from const_pub_type b where t.pub_type = b.type_id and b.zh_name like ? or b.en_name like ?)) ");
      params.add("%" + query.getSimpleSearchContent().toLowerCase() + "%");
      params.add("%" + query.getSimpleSearchContent().toLowerCase() + "%");
      params.add("%" + query.getSimpleSearchContent().toLowerCase() + "%");
      params.add("%" + query.getSimpleSearchContent().toLowerCase() + "%");
      params.add("%" + query.getSimpleSearchContent().toLowerCase() + "%");
      params.add("%" + query.getSimpleSearchContent().toLowerCase() + "%");
      params.add("%" + query.getSimpleSearchContent().toLowerCase() + "%");
    }

    if (query.getFromYear() != null && query.getFromYear() > 0) {
      // 调换年度，月份
      if (query.getToYear() != null && query.getToYear() > 0) {
        int tmp = query.getToYear();
        if (tmp < query.getFromYear()) {
          query.setToYear(query.getFromYear());
          query.setFromYear(tmp);
          int tmpM = query.getFromMonth();
          query.setFromMonth(query.getToMonth());
          query.setToMonth(tmpM);
        }
      }
    }

    if (query.getFromYear() != null && query.getFromYear() > 0) {
      // hql.append(" and t.publishYear>=? ");
      sql.append(" and t.publish_year >= ? ");
      params.add(query.getFromYear());
    }
    if (query.getToYear() != null && query.getToYear() > 0) {
      // hql.append(" and t.publishYear<=? ");
      sql.append(" and t.publish_year <= ? ");
      params.add(query.getToYear());
    }

    if (query.getQueryYear() != null && query.getQueryYear() > 0) {
      // hql.append(" and t.publishYear =? ");
      sql.append(" and t.publish_year = ? ");
      params.add(query.getQueryYear());
    }

    if (query.getFromMonth() != null && query.getFromMonth() > 0) {
      // hql.append(" and t.publishMonth>=? ");
      sql.append(" and t.publish_month >= ? ");
      params.add(query.getFromMonth());
    }
    if (query.getToMonth() != null && query.getToMonth() > 0) {
      // hql.append(" and t.publishMonth<=? ");
      sql.append(" and t.publish_month <= ? ");
      params.add(query.getToMonth());
    }

    if (query.getTypeId() != null && query.getTypeId() > 0) {
      // hql.append(" and t.typeId = ?");
      sql.append(" and t.pub_type = ? ");
      params.add(query.getTypeId());
    }
    Integer roleId = SecurityUtils.getCurrentUserRoleId();
    // 部门管理员
    if (ScmRolRoleConstants.UNIT_RO.equals(roleId) || ScmRolRoleConstants.UNIT_CONTACT.equals(roleId)) {
      // 初始化部门ID
      if (query.getDeptId() == null) {
        query.setDeptId(SecurityUtils.getCurrentUnitId());
      }
    }
    if (query.getDeptId() != null && query.getPsnId() == null) {
      // hql.append(" and ( ");
      sql.append(" and ( ");
      // hql.append(" t.id in (select pubId from PubPsnRol where (unitId = ? or parentUnitId=?) and insId
      // = ?)");
      sql.append(
          " exists(select 1 from pub_psn t1 where (t1.unit_id = ? or t1.parent_unit_id = ? ) and t1.confirm_result in(0,1) and t1.ins_id = ? and t1.pub_id = t.pub_id) ");
      params.add(query.getDeptId());
      params.add(query.getDeptId());
      params.add(insId);

      // 部门管理员录入的成果，也算是这个部门的
      if ((ScmRolRoleConstants.UNIT_RO.equals(roleId) || ScmRolRoleConstants.UNIT_CONTACT.equals(roleId))
          && query.getDeptId().equals(SecurityUtils.getCurrentUnitId())) {
        // hql.append(" or exists(select pubId from PubUnitOwner where (unitId = ? or parentUnitId=?) and
        // pubId = t.id) ");
        sql.append(
            " or exists(select 1 from pub_unit_owner t2 where (t2.unit_id = ? or t2.parent_unit_id = ? ) and t2.ins_id = ? and t2.pub_id = t.pub_id ) ");
        params.add(query.getDeptId());
        params.add(query.getDeptId());
        params.add(insId);
      }
      sql.append(" )");
    }
    if (query.getPsnId() != null && query.getPsnId() > 0) {
      // hql.append(" and exists (select t2.pubId from PubPsnRol t2 where t2.psnId = ? and t2.insId = ?
      // and t2.pubId = t.id )");
      sql.append(
          " and exists(select 1 from pub_psn t3 where t3.confirm_result in(0,1) and t3.psn_id = ? and t3.ins_id = ? and t3.pub_id = t.pub_id) ");
      params.add(query.getPsnId());
      params.add(insId);
    } else if (StringUtils.isNotBlank(query.getAuthorIds())) {
      // hql.append(" and exists (select t2.pubId from PubPsnRol t2 where t2.psnId in ("
      // + query.getAuthorIds()+
      // ") and insId = ? and t2.pubId = t.id )");
      sql.append("and exists(select 1 from pub_psn t3 where t3.confirm_result in(0,1) and t3.psn_id in(")
          .append(query.getAuthorIds()).append(") and t3.ins_id = ? and t3.pub_id = t.pub_id)");
      params.add(insId);
    }

    if (query.getPubTitle() != null) {
      String title = query.getPubTitle().trim().toLowerCase();
      if (!"".equals(title)) {
        String htmlPubTitle = HtmlUtils.htmlEscape(query.getPubTitle().trim().toLowerCase());
        // hql.append(" and (t.zhTitleText like ? or t.enTitleText like ?) ");
        sql.append(" and (t.zh_title_text like ? or t.en_title_text like ? ) ");
        params.add("%" + htmlPubTitle + "%");
        params.add("%" + htmlPubTitle + "%");
      }
    }

    if (StringUtils.isNotBlank(query.getPubLang())) {
      String lang = query.getPubLang().trim().toLowerCase();
      if ("en".equalsIgnoreCase(lang)) {
        // hql.append(" and t.zhTitle is null ");
        sql.append(" and t.en_title_text is not null ");
      } else {
        // hql.append(" and t.zhTitle is not null ");
        sql.append(" and t.zh_title_text is not null ");
      }
    }

    // 引用情况
    if ((query.getListEi() != null && query.getListEi() == 1)
        || (query.getListIstp() != null && query.getListIstp() == 1)
        || (query.getListSci() != null && query.getListSci() == 1)
        || (query.getListSsci() != null && query.getListSsci() == 1)) {

      // hql.append(" and exists(select t1.id from PublicationList t1 where 1=1 ");
      sql.append(" and exists(select 1 from pub_list t4 where (");
      boolean flagOr = false;
      if (query.getListEi() != null && query.getListEi() == 1) {
        if (flagOr) {
          sql.append(" or ");
        }
        // hql.append(" t1.listEi = 1 ");
        sql.append(" t4.list_ei = 1 ");
        flagOr = true;
      }
      if (query.getListIstp() != null && query.getListIstp() == 1) {
        if (flagOr) {
          sql.append(" or ");
        }
        // hql.append(" t1.listIstp = 1 ");
        sql.append(" t4.list_istp = 1 ");
        flagOr = true;
      }
      if (query.getListSci() != null && query.getListSci() == 1) {
        if (flagOr) {
          sql.append(" or ");
        }
        // hql.append(" and t1.listSci = 1 ");
        sql.append(" t4.list_sci = 1 ");
        flagOr = true;
      }
      if (query.getListSsci() != null && query.getListSsci() == 1) {
        if (flagOr) {
          sql.append(" or ");
        }
        // hql.append(" and t1.listSsci = 1 ");
        sql.append(" t4.list_ssci = 1 ");
        flagOr = true;
      }
      sql.append(") and t4.pub_id = t.pub_id)");
    }
    if (StringUtils.isNotBlank(query.getOnlyFirstAuthor())) {
      // 如果第一作者是本单位的，但是查询的用户(研究人员条件)不是第一作者，也是可以查询出来的
      // hql.append(" and t.firstAuthorPsnId is not null ");
      sql.append("  and t.first_author_psnid is not null ");
    }
    return sql.toString();
  }

  /**
   * 列出组别的成果列表.
   * 
   * @param insId 单位ID
   * @param groupId 组别ID
   * @param query 查询条件.
   * @param page 返回结果集合.
   * @throws DaoException
   */
  @SuppressWarnings("unchecked")
  public void searchGroupPub(long insId, long groupId, List<Long> pubIds, Page<PublicationRol> page)
      throws DaoException {

    String pubHql =
        " from PublicationRol t where t.insId = :insId and t.articleType=:articleType and t.status = :status and t.dupGroupId = :dupGroupId and t.id in(:ids) ";
    Query pubResult = super.createQuery(pubHql);
    pubResult.setParameter("insId", insId);
    pubResult.setParameter("articleType", 1);
    pubResult.setParameter("status", PublicationRolStatusEnum.APPROVED);
    pubResult.setParameter("dupGroupId", groupId);
    pubResult.setParameterList("ids", pubIds);

    page.setResult(pubResult.list());
    page.setTotalCount(page.getResult().size());
  }

  /**
   * 列出自定义合并的成果列表.
   * 
   * @param insId 单位ID
   * @param groupId 组别ID
   * @param query 查询条件.
   * @param page 返回结果集合.
   * @throws DaoException
   */
  @SuppressWarnings("unchecked")
  public void searchGroupPubByManually(long insId, String customPubIds, Page<PublicationRol> page) throws DaoException {
    List<Object> params = new ArrayList<Object>();
    String pubHql =
        "select new PublicationRol(t.id,t.typeId,t.publishYear,t.impactFactors,t.jid,t.citedTimes,t.citedDate)";
    pubHql += " from PublicationRol t where t.insId = ? and t.articleType=? and t.status = ? and t.id in ("
        + customPubIds + ")";
    params.add(insId);
    params.add(1);
    params.add(PublicationRolStatusEnum.APPROVED);
    Query pubResult = super.createQuery(pubHql, params.toArray());
    page.setResult(pubResult.list());
    page.setTotalCount(page.getResult().size());
  }
}
