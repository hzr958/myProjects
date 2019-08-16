package com.smate.center.batch.dao.pdwh.pub;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.hibernate.transform.Transformers;
import org.springframework.stereotype.Repository;

import com.smate.center.batch.model.pdwh.pub.KGPubCooperation;
import com.smate.core.base.utils.data.PdwhHibernateDao;

@Repository
public class KGPubCooperationDao extends PdwhHibernateDao<KGPubCooperation, Long> {
  /**
   * 获取近10年人员负责nsfc项目id
   * 
   */
  public List<String> getPsnNsfcPrjToHandleList(List<Long> psnIds) {
    String sql = "select t.nsfc_prj_id from nsfc_prj_10year t where t.psn_code in (:psnId) order by t.nsfc_prj_id";
    return (List<String>) this.getSession().createSQLQuery(sql).setParameterList("psnId", psnIds).list();
  }

  public Integer getPsnCategoriesByNsfcPrjToHandleList(List<Long> psnIds) {
    String sql =
        "select max(t.pub_num_5year) from nsfc_psn_pubnum_average t where t.nsfc_category in (select distinct(substr(p.discode,0,1)) from nsfc_prj_10year p where p.psn_code in (:psnId))";
    return this.getSession().createSQLQuery(sql).setParameterList("psnId", psnIds).uniqueResult() == null ? 0
        : ((BigDecimal) this.getSession().createSQLQuery(sql).setParameterList("psnId", psnIds).uniqueResult())
            .intValue();
  }

  public List<String> getPsnNsfcPrjToHandleList10years(List<Long> psnIds) {
    String sql =
        "select t.approve_num from nsfc_prp_10year t where t.nsfc_type = 1 and t.prp_nsfc_psn_id in (:psnId) order by t.approve_num";
    return (List<String>) this.getSession().createSQLQuery(sql).setParameterList("psnId", psnIds).list();
  }

  /**
   * 按项目前discode（截取前三位）获取近10年项目
   * 
   **/
  public List<BigDecimal> getNsfcPrjToHandleListByCategory(String category) {
    String sql =
        "select t.nsfc_prj_id from nsfc_prj_10year t where substr(t.discode,0,3)=:category order by t.nsfc_prj_id asc";
    return (List<BigDecimal>) this.getSession().createSQLQuery(sql).setParameter("category", category).list();
  }

  public List<Long> getPsnIsisId(Long psnId) {
    String sql = "select t.psn_code from nsfc_psn_info t where t.open_id =:psnId order by t.psn_code asc";
    List<BigDecimal> psnIsisIds =
        (List<BigDecimal>) this.getSession().createSQLQuery(sql).setParameter("psnId", psnId).list();
    List<Long> psnIds = new ArrayList<Long>();
    if (psnIsisIds != null || psnIsisIds.size() > 0) {
      for (BigDecimal psnIsId : psnIsisIds) {
        psnIds.add(psnIsId.longValue());
      }
    }
    return psnIds;
  }

  public String getPsnIsisName(Long psnId) {
    String sql = "select t.name from nsfc_psn_info_1 t where t.open_id =:psnId order by t.psn_code asc";
    List<String> psnIsisNames =
        (List<String>) this.getSession().createSQLQuery(sql).setParameter("psnId", psnId).list();
    if (psnIsisNames != null || psnIsisNames.size() > 0) {
      return psnIsisNames.get(0);
    }
    return "";
  }

  public Map<String, Object> getNsfcPrjInfo(String nsfcId) {
    String sql =
        "select t.nsfc_id,t.zh_title,t.en_title,t.application_code_1,t.zh_keywords,t.en_keywords,t.psn_nsfc_code,t.psn_name from nsfc_prj_all_5year t where t.nsfc_id =:nsfcId";
    return (Map<String, Object>) this.getSession().createSQLQuery(sql)
        .setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP).setParameter("nsfcId", nsfcId).uniqueResult();
  }

  public Map<String, Object> getNsfcPrjInfo(Long nsfcId) {
    String sql =
        "select t.nsfc_prj_id,t.zh_title,t.en_title,t.discode,t.zh_keywords,t.en_keywords from nsfc_prj_10year t where t.nsfc_prj_id =:nsfcId";
    return (Map<String, Object>) this.getSession().createSQLQuery(sql)
        .setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP).setParameter("nsfcId", nsfcId).uniqueResult();
  }

  public Map<String, Object> getNsfcPrjInfo10Years(String nsfcId) {
    String sql =
        "select t.approve_num,t.zh_title,t.en_title,t.discode,t.zh_keywords,t.en_keywords from nsfc_prp_10year t where t.approve_num =:nsfcId";
    return (Map<String, Object>) this.getSession().createSQLQuery(sql)
        .setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP).setParameter("nsfcId", nsfcId).uniqueResult();
  }

  // prpId 为nsfc_prp_10year的主键id
  public void insertIntoSubSetsFromPrp(String kws, Integer size, Long prpId, Long hashValue) {
    String sql = "insert into nsfc_prp_kw_10year_subset values (seq_base_category.nextval,?,?,?,?)";
    super.update(sql, new Object[] {prpId, kws, size, hashValue});
  }

  // prpId 为nsfc_prp_10year的主键id
  public void insertIntoSubSetsFinalRsFromPrp(String kws, Integer size, String category, Long hashValue, Integer cotf) {
    String sql = "insert into nsfc_prp_kw_10year_subset_all values (seq_base_category.nextval,?,?,?,?,?)";
    super.update(sql, new Object[] {category, kws, size, hashValue, cotf});
  }

  public void insertIntoPrpKws(String kws, Integer size, Long prpId) {
    String sql = "insert into nsfc_prp_kw_10year values (?,?,?)";
    super.update(sql, new Object[] {prpId, kws, size});
  }
  /*
   * public Long getKwPk() { String sql = "select seq_base_category.nextval from dual"; BigDecimal id
   * = (BigDecimal) this.getSession().createSQLQuery(sql).uniqueResult(); return id == null ? 0L :
   * id.longValue(); }
   */

  public void insertIntoPsnRrjTitleKw(Long psnOpenId, String prjId, Integer prjLanguage, String kwStr, Integer tf) {
    String sql = "insert into NSFC_PSN_PROJECT_TITLE_KW values(seq_base_category.nextval,?,?,?,?,?)";
    super.update(sql, new Object[] {psnOpenId, prjId, prjLanguage, kwStr, tf});
  }

  public void insertIntoPsnRrjSplitKw(Long psnOpenId, String prjId, Integer prjLanguage, String kwStr) {
    String sql = "insert into NSFC_PSN_PROJECT_SPLIT_KW values(seq_base_category.nextval,?,?,?,?)";
    super.update(sql, new Object[] {psnOpenId, prjId, prjLanguage, kwStr});
  }

  public void insertIntoPsnRrjKw(Long psnOpenId, String prjId, Integer prjLanguage, String kwStr) {
    String sql = "insert into NSFC_PSN_PROJECT_KW values(seq_base_category.nextval,?,?,?,?)";
    super.update(sql, new Object[] {psnOpenId, prjId, prjLanguage, kwStr});
  }

  public void insertIntoPsnRrjKwAll(Long psnOpenId, Integer prjLanguage, String kwStr, Integer tf) {
    String sql = "insert into NSFC_PSN_PROJECT_KW_ALL values(seq_base_category.nextval,?,?,?,?)";
    super.update(sql, new Object[] {psnOpenId, prjLanguage, kwStr, tf});
  }

  public void insertIntoPsnPubTitleKw(Long psnOpenId, Long pubId, Integer prjLanguage, String kwStr, Double tf) {
    String sql = "insert into NSFC_PSN_PUB_TITLE_KW values(seq_base_category.nextval,?,?,?,?,?)";
    super.update(sql, new Object[] {psnOpenId, pubId, prjLanguage, kwStr, tf});
  }

  public void insertIntoPsnPubKeywordsKw(Long psnOpenId, Long pubId, Integer prjLanguage, String kwStr) {
    String sql = "insert into NSFC_PSN_PUB_SPLIT_KW values(seq_base_category.nextval,?,?,?,?)";
    super.update(sql, new Object[] {psnOpenId, pubId, prjLanguage, kwStr});
  }

  public void insertIntoPsnPubKeywordsKw(Long psnOpenId, Long pubId, Integer prjLanguage, String kwStr,
      Integer authorNum) {
    String sql = "insert into NSFC_PSN_PUB_SPLIT_KW values(seq_base_category.nextval,?,?,?,?,?)";
    super.update(sql, new Object[] {psnOpenId, pubId, prjLanguage, kwStr, authorNum});
  }

  public void insertIntoPsnPubKw(Long psnOpenId, Long pubId, Integer prjLanguage, String kwStr, double tf) {
    String sql = "insert into NSFC_PSN_PUB_KW values(seq_base_category.nextval,?,?,?,?,?)";
    super.update(sql, new Object[] {psnOpenId, pubId, prjLanguage, kwStr, tf});
  }

  public void insertIntoPsnPubKwAll(Long psnOpenId, Integer prjLanguage, String kwStr, double tf) {
    String sql = "insert into NSFC_PSN_PUB_KW_ALL values(seq_base_category.nextval,?,?,?,?)";
    super.update(sql, new Object[] {psnOpenId, prjLanguage, kwStr, tf});
  }

  public void insertIntoPsnPubKwAllCotf(Long psnOpenId, Integer prjLanguage, String kwStr, double tf) {
    String sql = "insert into NSFC_PSN_PUB_KW_ALL_COTF values(seq_base_category.nextval,?,?,?,?)";
    super.update(sql, new Object[] {psnOpenId, prjLanguage, kwStr, tf});
  }

  public void deletePsnPrjPubKw(Long psnId) {
    String sql11 = "select count(1) from NSFC_PSN_PROJECT_KW_ALL t where t.psn_open_id =:psnId";
    BigDecimal psnPrjCount =
        (BigDecimal) this.getSession().createSQLQuery(sql11).setParameter("psnId", psnId).uniqueResult();
    if (psnPrjCount != null && psnPrjCount.intValue() > 0) {
      String sql = "delete NSFC_PSN_PROJECT_TITLE_KW t where t.psn_open_id =:psnId";
      this.getSession().createSQLQuery(sql).setParameter("psnId", psnId).executeUpdate();
      String sql5 = "delete NSFC_PSN_PROJECT_SPLIT_KW t where t.psn_open_id =:psnId";
      this.getSession().createSQLQuery(sql5).setParameter("psnId", psnId).executeUpdate();
      String sql6 = "delete NSFC_PSN_PROJECT_KW t where t.psn_open_id =:psnId";
      this.getSession().createSQLQuery(sql6).setParameter("psnId", psnId).executeUpdate();
      String sql2 = "delete NSFC_PSN_PROJECT_KW_ALL t where t.psn_open_id =:psnId";
      this.getSession().createSQLQuery(sql2).setParameter("psnId", psnId).executeUpdate();
    }
    String sql10 = "select count(1) from NSFC_PSN_PUB_KW_ALL t where t.psn_open_id =:psnId";
    BigDecimal psnPubCount =
        (BigDecimal) this.getSession().createSQLQuery(sql10).setParameter("psnId", psnId).uniqueResult();
    if (psnPubCount != null && psnPubCount.intValue() > 0) {
      String sql1 = "delete NSFC_PSN_PUB_KW t where t.psn_open_id =:psnId";
      this.getSession().createSQLQuery(sql1).setParameter("psnId", psnId).executeUpdate();
      String sql3 = "delete NSFC_PSN_PUB_KW_ALL t where t.psn_open_id =:psnId";
      this.getSession().createSQLQuery(sql3).setParameter("psnId", psnId).executeUpdate();
      String sql4 = "delete NSFC_PSN_PUB_KW_ALL_COTF t where t.psn_open_id =:psnId";
      this.getSession().createSQLQuery(sql4).setParameter("psnId", psnId).executeUpdate();
      String sql7 = "delete NSFC_PSN_PUB_TITLE_KW t where t.psn_open_id =:psnId";
      this.getSession().createSQLQuery(sql7).setParameter("psnId", psnId).executeUpdate();
      String sql8 = "delete NSFC_PSN_PUB_SPLIT_KW t where t.psn_open_id =:psnId";
      this.getSession().createSQLQuery(sql8).setParameter("psnId", psnId).executeUpdate();
      String sql9 = "delete NSFC_PSN_PUB_KW_ALL_COTF t where t.psn_open_id =:psnId";
      this.getSession().createSQLQuery(sql9).setParameter("psnId", psnId).executeUpdate();
    }
  }

  public void deletePsnPdwhPubKws(Long psnId) {
    String sql10 = "select count(1) from NSFC_PSN_PUB_KW_ALL t where t.psn_open_id =:psnId";
    BigDecimal psnPubCount =
        (BigDecimal) this.getSession().createSQLQuery(sql10).setParameter("psnId", psnId).uniqueResult();
    if (psnPubCount != null && psnPubCount.intValue() > 0) {
      String sql1 = "delete NSFC_PSN_PUB_KW t where t.psn_open_id =:psnId";
      this.getSession().createSQLQuery(sql1).setParameter("psnId", psnId).executeUpdate();
      String sql3 = "delete NSFC_PSN_PUB_KW_ALL t where t.psn_open_id =:psnId";
      this.getSession().createSQLQuery(sql3).setParameter("psnId", psnId).executeUpdate();
      String sql8 = "delete NSFC_PSN_PUB_SPLIT_KW t where t.psn_open_id =:psnId";
      this.getSession().createSQLQuery(sql8).setParameter("psnId", psnId).executeUpdate();
    }
  }

  public void insertIntoTitle(Long prjId, String prjYear, Integer prjLanguage, String kwStr, Integer tf,
      String discode) {
    String sql = "insert into nsfc_project_kw_title values(seq_base_category.nextval,?,?,?,?,?,?)";
    super.update(sql, new Object[] {prjId, prjYear, prjLanguage, kwStr, tf, discode});
  }

  public void insertIntoTitleAndKws(Long prjId, String prjYear, Integer prjLanguage, String kwStr, Integer tf,
      String discode) {
    String sql = "insert into nsfc_project_kw_title_keywords values(seq_base_category.nextval,?,?,?,?,?,?)";
    super.update(sql, new Object[] {prjId, prjYear, prjLanguage, kwStr, tf, discode});
  }

  public void insertIntoAbsTf(Long prjId, String prjYear, Integer prjLanguage, String kwStr, Integer tf,
      String discode) {
    String sql = "insert into nsfc_project_kw_abs values(seq_base_category.nextval,?,?,?,?,?,?)";
    super.update(sql, new Object[] {prjId, prjYear, prjLanguage, kwStr, tf, discode});
  }

  public void insertIntoAbsCotf(Long prjId, String prjYear, Integer prjLanguage, String kwStr, Integer cotf,
      String discode) {
    String sql = "insert into nsfc_project_kw_abs_cotf values(seq_base_category.nextval,?,?,?,?,?,?)";
    super.update(sql, new Object[] {prjId, prjYear, prjLanguage, kwStr, cotf, discode});
  }

  public void insertIntoAbsCotfDetails(Long prjId, String prjYear, Integer prjLanguage, String kwStr,
      String kwStrSecond, Integer cotf, String discode) {
    String sql = "insert into nsfc_project_kw_abs_cotf_de values(seq_base_category.nextval,?,?,?,?,?,?,?)";
    super.update(sql, new Object[] {prjId, prjYear, prjLanguage, kwStr, kwStrSecond, cotf, discode});
  }

  public void insertIntoKwCoExistsTitleAbs(Long prjId, String prjYear, Integer prjLanguage, String kwStr,
      String discode) {
    String sql = "insert into nsfc_project_kw_title_abs values(seq_base_category.nextval,?,?,?,?,?)";
    super.update(sql, new Object[] {prjId, prjYear, prjLanguage, kwStr, discode});
  }

  public void deleteNsfPrjKw(Long prjId) {
    String sql = "delete nsfc_project_kw_title t where t.prj_id =:prjId";
    this.getSession().createSQLQuery(sql).setParameter("prjId", prjId).executeUpdate();
    String sql5 = "delete nsfc_project_kw_title_keywords t where t.prj_id =:prjId";
    this.getSession().createSQLQuery(sql5).setParameter("prjId", prjId).executeUpdate();
    String sql6 = "delete nsfc_project_kw_abs t where t.prj_id =:prjId";
    this.getSession().createSQLQuery(sql6).setParameter("prjId", prjId).executeUpdate();
    String sql1 = "delete nsfc_project_kw_abs_cotf t where t.prj_id =:prjId";
    this.getSession().createSQLQuery(sql1).setParameter("prjId", prjId).executeUpdate();
    String sql2 = "delete nsfc_project_kw_abs_cotf_de t where t.prj_id =:prjId";
    this.getSession().createSQLQuery(sql2).setParameter("prjId", prjId).executeUpdate();
    String sql3 = "delete nsfc_project_kw_title_abs t where t.prj_id =:prjId";
    this.getSession().createSQLQuery(sql3).setParameter("prjId", prjId).executeUpdate();
  }

  public void deleteGaoXiaoPrjKw(Long prjId) {
    String sql10 = "select count(1) from nsfc_project_kw_title_keywords t where t.prj_id =:prjId";
    BigDecimal psnPubCount =
        (BigDecimal) this.getSession().createSQLQuery(sql10).setParameter("prjId", prjId).uniqueResult();
    if (psnPubCount != null && psnPubCount.intValue() > 0) {
      String sql5 = "delete nsfc_project_kw_title_keywords t where t.prj_id =:prjId";
      this.getSession().createSQLQuery(sql5).setParameter("prjId", prjId).executeUpdate();
    }

  }

  public List<Map<String, Object>> getPsnPdwhPubTfSumByOpenId(Long openId) {
    String sql = "select t.language,t.kw_str,t.tf from nsfc_psn_pub_kw_all t where t.psn_open_id=:openId";
    return (List<Map<String, Object>>) this.getSession().createSQLQuery(sql).setParameter("openId", openId)
        .setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP).list();
  }

  public List<Map<String, Object>> getPsnPdwhPubTfSumByOpenId(Long openId, Integer lang) {
    String sql =
        "select t.language,t.kw_str,t.tf from nsfc_psn_pub_kw_all t where t.psn_open_id=:openId and t.language=:lang";
    return (List<Map<String, Object>>) this.getSession().createSQLQuery(sql).setParameter("openId", openId)
        .setParameter("lang", lang).setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP).list();
  }

  public void insertIntoPsnScore(Long psnOpenId, Integer prjLanguage, String kwStr, Long tf) {
    String sql = "insert into NSFC_PSN_KWS_ISIS values(seq_base_category.nextval,?,?,?,?)";
    super.update(sql, new Object[] {psnOpenId, prjLanguage, kwStr, tf});
  }

  public void insertIntoPsnScoreAll(Long psnOpenId, Integer prjLanguage, String kwStr, Long tf) {
    String sql = "insert into NSFC_PSN_KWS_ISIS_ALL values(seq_base_category.nextval,?,?,?,?)";
    super.update(sql, new Object[] {psnOpenId, prjLanguage, kwStr, tf});
  }

  public void insertIntoPsnScoreAll(Long psnOpenId, Integer prjLanguage, String kwStr, Double tf) {
    String sql = "insert into NSFC_PSN_KWS_ISIS_ALL values(seq_base_category.nextval,?,?,?,?)";
    super.update(sql, new Object[] {psnOpenId, prjLanguage, kwStr, tf});
  }

  public void deletePsnScore(Long psnId, Integer language) {
    String sql11 = "select count(1) from NSFC_PSN_KWS_ISIS t where t.psn_open_id =:psnId and t.language=:language";
    BigDecimal psnPrjCount = (BigDecimal) this.getSession().createSQLQuery(sql11).setParameter("psnId", psnId)
        .setParameter("language", language).uniqueResult();
    if (psnPrjCount != null && psnPrjCount.intValue() > 0) {
      String sql = "delete NSFC_PSN_KWS_ISIS t where t.psn_open_id =:psnId and t.language=:language";
      this.getSession().createSQLQuery(sql).setParameter("psnId", psnId).setParameter("language", language)
          .executeUpdate();
    }
    String sql12 = "select count(1) from NSFC_PSN_KWS_ISIS_ALL t where t.psn_open_id =:psnId and t.language=:language";
    BigDecimal psnPrjCount1 = (BigDecimal) this.getSession().createSQLQuery(sql12).setParameter("psnId", psnId)
        .setParameter("language", language).uniqueResult();
    if (psnPrjCount1 != null && psnPrjCount1.intValue() > 0) {
      String sql = "delete NSFC_PSN_KWS_ISIS_ALL t where t.psn_open_id =:psnId and t.language=:language";
      this.getSession().createSQLQuery(sql).setParameter("psnId", psnId).setParameter("language", language)
          .executeUpdate();
    }
  }

  // 不限制语言
  public List<String> getKwByTfExcludePubKw(Set<String> titleKws, Set<String> absKws) {
    String sql =
        "select p.kw2 from (select t2.kw_str as kw2,t1.kw_str as kw1 from nsfc_prp_kw t1 inner join nsfc_prp_kw t2 on t1.prp_id=t2.prp_id where t1.kw_str in (:titleKws) and t2.kw_str in (:absKws) and t1.kw_str!=t2.kw_str group by t2.kw_str,t1.kw_str having count(1)>1) p group by p.kw2 order by count(1) desc, length(p.kw2) desc";
    return (List<String>) this.getSession().createSQLQuery(sql).setParameterList("titleKws", titleKws)
        .setParameterList("absKws", absKws).list();
  }

  // 不限制语言
  public List<Map<String, Object>> getKwByCoTfCoutExcludePubKw(Set<String> titleKws, Set<String> absKws) {
    String sql =
        "select t3.kw2, count(1) as counts from (select t2.kw_str as kw2, t1.kw_str as kw1 from nsfc_prp_kw t1 inner join nsfc_prp_kw t2 on t1.prp_id=t2.prp_id where t1.kw_str in (:titleKws) and t2.kw_str in (:absKws) and t1.kw_str!=t2.kw_str group by t2.kw_str,t1.kw_str having count(1)>1) t3 group by t3.kw2 order by count(1) desc, length(t3.kw2) desc";
    return (List<Map<String, Object>>) this.getSession().createSQLQuery(sql)
        .setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP).setParameterList("titleKws", titleKws)
        .setParameterList("absKws", absKws).list();
  }

  public List<String> getKwByTfExcludePubKw(Set<String> titleKws, Set<String> absKws, Integer language) {
    String sql =
        "select p.kw2 from (select t2.kw_str as kw2,t1.kw_str as kw1 from nsfc_prp_kw t1 inner join nsfc_prp_kw t2 on t1.prp_id=t2.prp_id where t1.kw_str in (:titleKws) and t2.kw_str in (:absKws) and t1.kw_str!=t2.kw_str and  t1.language =:language and t2.language =:language group by t2.kw_str,t1.kw_str having count(1)>1) p group by p.kw2 order by count(1) desc, length(p.kw2) desc";
    return (List<String>) this.getSession().createSQLQuery(sql).setParameterList("titleKws", titleKws)
        .setParameterList("absKws", absKws).setParameter("language", language).list();
  }

  // language=1外文；language=2中文
  public List<Map<String, Object>> getKwByCoTfCoutExcludePubKw(Set<String> titleKws, Set<String> absKws,
      Integer language) {
    String sql =
        "select t3.kw2, count(1) as counts from (select t2.kw_str as kw2, t1.kw_str as kw1 from nsfc_prp_kw t1 inner join nsfc_prp_kw t2 on t1.prp_id=t2.prp_id where t1.kw_str in (:titleKws) and t2.kw_str in (:absKws) and t1.kw_str!=t2.kw_str and t1.language =:language and t2.language =:language group by t2.kw_str,t1.kw_str having count(1)>1) t3 group by t3.kw2 order by count(1) desc, length(t3.kw2) desc";
    return (List<Map<String, Object>>) this.getSession().createSQLQuery(sql)
        .setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP).setParameterList("titleKws", titleKws)
        .setParameterList("absKws", absKws).setParameter("language", language).list();
  }

  public List<String> getKwByTfExcludePubKw(List<String> titleKws, List<String> absKws, Integer language) {
    String sql =
        "select p.kw2 from (select t2.kw_str as kw2,t1.kw_str as kw1 from nsfc_prp_kw t1 inner join nsfc_prp_kw t2 on t1.prp_id=t2.prp_id where t1.kw_str in (:titleKws) and t2.kw_str in (:absKws) and t1.kw_str!=t2.kw_str and  t1.language =:language and t2.language =:language group by t2.kw_str,t1.kw_str having count(1)>1) p group by p.kw2 order by count(1) desc, length(p.kw2) desc";
    return (List<String>) this.getSession().createSQLQuery(sql).setParameterList("titleKws", titleKws)
        .setParameterList("absKws", absKws).setParameter("language", language).list();
  }

  // language=1外文；language=2中文
  public List<Map<String, Object>> getKwByCoTfCoutExcludePubKw(List<String> titleKws, List<String> absKws,
      Integer language) {
    String sql =
        "select t3.kw2, count(1) as counts from (select t2.kw_str as kw2, t1.kw_str as kw1 from nsfc_prp_kw t1 inner join nsfc_prp_kw t2 on t1.prp_id=t2.prp_id where t1.kw_str in (:titleKws) and t2.kw_str in (:absKws) and t1.kw_str!=t2.kw_str and t1.language =:language and t2.language =:language group by t2.kw_str,t1.kw_str having count(1)>1) t3 group by t3.kw2 order by count(1) desc, length(t3.kw2) desc";
    return (List<Map<String, Object>>) this.getSession().createSQLQuery(sql)
        .setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP).setParameterList("titleKws", titleKws)
        .setParameterList("absKws", absKws).setParameter("language", language).list();
  }

  public List<String> getKwByTfExcludePubKwByCategory(Set<String> titleKws, Set<String> absKws, Integer language,
      Set<String> categories) {
    String sql =
        "select p.kw2 from (select t2.kw_str as kw2,t1.kw_str as kw1, substr(t1.nsfc_category,0,3) from nsfc_prp_kw t1 inner join nsfc_prp_kw t2 on t1.prp_id=t2.prp_id where t1.kw_str in (:titleKws) and t2.kw_str in (:absKws) and t1.kw_str!=t2.kw_str and  t1.language =:language and t2.language =:language and substr(t1.nsfc_category,0,3) in (:categories) group by t2.kw_str,t1.kw_str, substr(t1.nsfc_category,0,3) having count(1)>1) p group by p.kw2 order by count(1) desc, length(p.kw2) desc";
    return (List<String>) this.getSession().createSQLQuery(sql).setParameterList("titleKws", titleKws)
        .setParameterList("absKws", absKws).setParameter("language", language)
        .setParameterList("categories", categories).list();
  }

  // language=1外文；language=2中文
  public List<Map<String, Object>> getKwByCoTfCoutExcludePubKwByCategory(Set<String> titleKws, Set<String> absKws,
      Integer language, Set<String> categories) {
    String sql =
        "select t3.kw2, count(1) as counts from (select t2.kw_str as kw2, t1.kw_str as kw1, substr(t1.nsfc_category,0,3) from nsfc_prp_kw t1 inner join nsfc_prp_kw t2 on t1.prp_id=t2.prp_id where t1.kw_str in (:titleKws) and t2.kw_str in (:absKws) and t1.kw_str!=t2.kw_str and t1.language =:language and t2.language =:language and substr(t1.nsfc_category,0,3) in (:categories) group by t2.kw_str,t1.kw_str, substr(t1.nsfc_category,0,3) having count(1)>1) t3 group by t3.kw2 order by count(1) desc, length(t3.kw2) desc";
    return (List<Map<String, Object>>) this.getSession().createSQLQuery(sql)
        .setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP).setParameterList("titleKws", titleKws)
        .setParameterList("absKws", absKws).setParameter("language", language)
        .setParameterList("categories", categories).list();
  }

  public Map<String, Object> getNsfcPrjInfoForGaoXiao(Long nsfcId) {
    String sql =
        "select t.prp_code,t.prj_xml,t.zh_title,t.key_words from tmp_prj_classify t  where t.prp_code =:nsfcId";
    return (Map<String, Object>) this.getSession().createSQLQuery(sql)
        .setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP).setParameter("nsfcId", nsfcId).uniqueResult();
  }
}
