package com.smate.center.batch.dao.sns.pub;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang3.StringUtils;
import org.hibernate.transform.Transformers;
import org.springframework.stereotype.Repository;

import com.smate.center.batch.model.sns.pub.ProjectDataFiveYear;
import com.smate.core.base.utils.data.SnsHibernateDao;

@Repository
public class ProjectDataFiveYearDao extends SnsHibernateDao<ProjectDataFiveYear, Long> {
  /**
   * new ProjectDataFiveYear(id,applicationCode,zhKeywords,enKeywords)
   * 
   * @param size
   * @param id
   * @return
   */
  @SuppressWarnings("unchecked")
  public List<ProjectDataFiveYear> getProjectDataList(Integer size, Long id) {
    // String hql = "select new
    // ProjectDataFiveYear(id,applicationCode,zhKeywords,enKeywords,approveCode)
    // from ProjectDataFiveYear t where t.id > :id order by t.id ";
    String hql =
        "select new ProjectDataFiveYear(id, applicationCode, zhKeywords, approveCode, zhTitle, zhAbstract) from ProjectDataFiveYear t where t.id > :id order by t.id ";
    return createQuery(hql).setParameter("id", id).setMaxResults(size).list();
  }

  public Long getTotalCount() {
    String hql = "select count(1) from ProjectDataFiveYear";
    return (Long) super.createQuery(hql).uniqueResult();
  }

  @SuppressWarnings("unchecked")
  public List<ProjectDataFiveYear> getProjectDataList(Integer size, Long id, String category) {
    // String hql = "select new
    // ProjectDataFiveYear(id,applicationCode,zhKeywords,enKeywords,approveCode)
    // from ProjectDataFiveYear t where t.id > :id order by t.id ";
    String hql =
        "select new ProjectDataFiveYear(id, applicationCode, zhKeywords, approveCode, zhTitle, zhAbstract) from ProjectDataFiveYear t where t.applicationCode =:category and t.id > :id order by t.id ";
    return createQuery(hql).setParameter("category", category).setParameter("id", id).setMaxResults(size).list();
  }

  public List<Map<String, Object>> getKwsInfo(Set<String> kwSet) {
    String sql =
        "select t1.kw_str,t1.tf,t1.kw_length,t1.kw_type from (select t.kw_str,max(t.tf)over(partition by t.kw_str) as tf,t.kw_length,max(t.type)over(partition by t.kw_str) as kw_type from nsfc_project_kw_test_isi_all t where t.kw_str in (:kwSet)) t1 group by t1.kw_str,t1.tf,t1.kw_length,t1.kw_type";
    return (List<Map<String, Object>>) this.getSession().createSQLQuery(sql)
        .setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP).setParameterList("kwSet", kwSet).list();
  }

  // 加入学科限制，用于项目提取关键词
  public List<Map<String, Object>> getKwsInfo(Set<String> kwSet, String category) {
    String sql =
        "select t1.kw_str,t1.tf,t1.kw_length,t1.kw_type from (select t.kw_str,max(t.tf)over(partition by t.kw_str) as tf,t.kw_length,max(t.type)over(partition by t.kw_str) as kw_type from nsfc_project_kw_test_isi_all t where t.kw_str in (:kwSet) and t.nsfc_category =:category) t1 group by t1.kw_str,t1.tf,t1.kw_length,t1.kw_type";
    return (List<Map<String, Object>>) this.getSession().createSQLQuery(sql)
        .setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP).setParameterList("kwSet", kwSet)
        .setParameter("category", category).list();
  }

  public void deleteTitleKw(Long prjId) {
    String sql = "delete nsfc_project_kw_title t where t.prj_id =:prjId";
    this.getSession().createSQLQuery(sql).setParameter("prjId", prjId).executeUpdate();
  }

  public void deleteTitleKwNew(Long prjId) {
    String sql = "delete nsfc_project_kw_title_1 t where t.prj_id =:prjId";
    this.getSession().createSQLQuery(sql).setParameter("prjId", prjId).executeUpdate();
  }

  public void deleteAbsKw(Long prjId) {
    String sql = "delete nsfc_project_kw_abs t where t.prj_id =:prjId";
    this.getSession().createSQLQuery(sql).setParameter("prjId", prjId).executeUpdate();
  }

  public void deleteAbsKwNew(Long prjId) {
    String sql = "delete nsfc_project_kw_abs_new t where t.prj_id =:prjId";
    this.getSession().createSQLQuery(sql).setParameter("prjId", prjId).executeUpdate();
  }

  public Long getMaxId() {
    String sql = "select max(t.id) from nsfc_project_kw_title t";
    BigDecimal id = (BigDecimal) this.getSession().createSQLQuery(sql).uniqueResult();
    String sql_1 = "select max(t.id) from nsfc_project_kw_abs t";
    BigDecimal absId = (BigDecimal) this.getSession().createSQLQuery(sql_1).uniqueResult();
    String sql_2 = "select max(t.id) from nsfc_project_kw_abs_new t";
    BigDecimal absId1 = (BigDecimal) this.getSession().createSQLQuery(sql_2).uniqueResult();
    String sql_3 = "select max(t.id) from nsfc_project_kw_title_1 t";
    BigDecimal id_1 = (BigDecimal) this.getSession().createSQLQuery(sql_2).uniqueResult();
    Long titleId = id == null ? 0L : id.longValue();
    Long newabsId = absId == null ? 0L : absId.longValue();
    Long newabsId1 = absId1 == null ? 0L : absId.longValue();
    Long newid_1 = id_1 == null ? 0L : id_1.longValue();

    Long[] myList = {titleId, newabsId, newabsId1, newid_1};
    Long num = 0L;
    for (int i = 0; i < myList.length; i++) {
      num = (myList[i] < num ? num : myList[i]);// 三元运算符
    }
    return num;
  }

  public void insertIntoTitleKw(Long pkId, Long prjId, String prjYear, Integer prjLanguage, String kwStr,
      String discode) {
    /*
     * String sql =
     * "insert into nsfc_project_kw_title() values(:pkId, :prjId, :prjYear, :prjLanguage, :kwStr, :discode)"
     * ; this.getSession().createQuery(sql).setParameter("pkId", pkId).setParameter("prjId", prjId)
     * .setParameter("prjYear", prjYear).setParameter("prjLanguage", prjLanguage).setParameter("kwStr",
     * kwStr) .setParameter("discode", discode).executeUpdate();
     */
    String sql = "insert into nsfc_project_kw_title values(?,?,?,?,?,?)";
    super.update(sql, new Object[] {pkId, prjId, prjYear, prjLanguage, kwStr, discode});
  }

  public void insertIntoTitleKwNew(Long pkId, Long prjId, String prjYear, Integer prjLanguage, String kwStr,
      String discode) {
    /*
     * String sql =
     * "insert into nsfc_project_kw_title() values(:pkId, :prjId, :prjYear, :prjLanguage, :kwStr, :discode)"
     * ; this.getSession().createQuery(sql).setParameter("pkId", pkId).setParameter("prjId", prjId)
     * .setParameter("prjYear", prjYear).setParameter("prjLanguage", prjLanguage).setParameter("kwStr",
     * kwStr) .setParameter("discode", discode).executeUpdate();
     */
    String sql = "insert into nsfc_project_kw_title_1 values(?,?,?,?,?,?)";
    super.update(sql, new Object[] {pkId, prjId, prjYear, prjLanguage, kwStr, discode});
  }

  public void insertIntoAbsKw(Long pkId, Long prjId, String prjYear, Integer prjLanguage, String kwStr, String discode,
      Double tf) {
    /*
     * String sql =
     * "insert into nsfc_project_kw_abs  values (:pkId, :prjId, :prjYear, :prjLanguage, :kwStr, :discode)"
     * ; this.getSession().createQuery(sql).setParameter("pkId", pkId).setParameter("prjId", prjId)
     * .setParameter("prjYear", prjYear).setParameter("prjLanguage", prjLanguage).setParameter("kwStr",
     * kwStr) .setParameter("discode", discode).executeUpdate();
     */
    String sql = "insert into nsfc_project_kw_abs values(?,?,?,?,?,?,?)";
    super.update(sql, new Object[] {pkId, prjId, prjYear, prjLanguage, kwStr, discode, tf});
  }

  public void insertIntoAbsKwNew(Long pkId, Long prjId, String prjYear, Integer prjLanguage, String kwStr,
      String discode, Integer cotf) {
    /*
     * String sql =
     * "insert into nsfc_project_kw_abs_new  values (:pkId, :prjId, :prjYear, :prjLanguage, :kwStr, :discode)"
     * ; this.getSession().createQuery(sql).setParameter("pkId", pkId).setParameter("prjId", prjId)
     * .setParameter("prjYear", prjYear).setParameter("prjLanguage", prjLanguage).setParameter("kwStr",
     * kwStr) .setParameter("discode", discode).executeUpdate();
     */
    String sql = "insert into nsfc_project_kw_abs_new values(?,?,?,?,?,?,?)";
    super.update(sql, new Object[] {pkId, prjId, prjYear, prjLanguage, kwStr, discode, cotf});
  }



  public List<String> getKwByTf(List<String> titleKws, List<String> absKws, String discode) {
    int length = StringUtils.isEmpty(discode) ? 7 : discode.length();
    String sql =
        "select t2.kw_str from nsfc_project_kw_all t1 inner join nsfc_project_kw_all t2 on t1.prj_id=t2.prj_id where t1.kw_str in (:titleKws) and t2.kw_str in (:absKws) and t1.kw_str!=t2.kw_str and substr(t1.nsfc_category,1,:length)=:discode group by t2.kw_str order by count(1) desc, length(t2.kw_str) desc";
    return (List<String>) this.getSession().createSQLQuery(sql).setParameterList("titleKws", titleKws)
        .setParameterList("absKws", absKws).setParameter("length", length).setParameter("discode", discode).list();
  }

  public List<Map<String, Object>> getKwByCoTfCout(List<String> titleKws, List<String> absKws, String discode) {
    int length = StringUtils.isEmpty(discode) ? 7 : discode.length();
    String sql =
        "select t3.kw_str, t3.counts from (select t2.kw_str, count(1) as counts from nsfc_project_kw_all t1 inner join nsfc_project_kw_all t2 on t1.prj_id=t2.prj_id where t1.kw_str in (:titleKws) and t2.kw_str in (:absKws) and t1.kw_str!=t2.kw_str and substr(t1.nsfc_category,1,:length)=:discode group by t2.kw_str) t3 order by t3.counts desc, length(t3.kw_str) desc";
    return (List<Map<String, Object>>) this.getSession().createSQLQuery(sql)
        .setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP).setParameterList("titleKws", titleKws)
        .setParameterList("absKws", absKws).setParameter("length", length).setParameter("discode", discode).list();
  }

  public Map<String, Object> getCoTfByKw(String titleKw, String absKw, String discode) {
    int length = StringUtils.isEmpty(discode) ? 7 : discode.length();
    String sql =
        "select t3.kw_str, t3.kw_str_sec,t3.counts from (select t2.kw_str, t1.kw_str as kw_str_sec, count(1) as counts from nsfc_project_kw_all t1 inner join nsfc_project_kw_all t2 on t1.prj_id=t2.prj_id where t1.kw_str =:titleKw and t2.kw_str =:absKw and t1.kw_str!=t2.kw_str and substr(t1.nsfc_category,1,:length)=:discode group by t2.kw_str,t1.kw_str) t3 order by t3.counts desc, length(t3.kw_str) desc";
    return (Map<String, Object>) this.getSession().createSQLQuery(sql)
        .setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP).setParameter("titleKw", titleKw)
        .setParameter("absKw", absKw).setParameter("length", length).setParameter("discode", discode).uniqueResult();
  }

  public List<String> getKwByTf(Set<String> titleKws, Set<String> absKws, Integer language) {
    String sql =
        "select t2.kw_str from nsfc_project_kw_all t1 inner join nsfc_project_kw_all t2 on t1.prj_id=t2.prj_id where t1.kw_str in (:titleKws) and t2.kw_str in (:absKws) and t1.kw_str!=t2.kw_str and  t1.language =:language and t2.language =:language group by t2.kw_str order by count(1) desc, length(t2.kw_str) desc";
    return (List<String>) this.getSession().createSQLQuery(sql).setParameterList("titleKws", titleKws)
        .setParameterList("absKws", absKws).setParameter("language", language).list();
  }

  // language=1外文；language=2中文
  public List<Map<String, Object>> getKwByCoTfCout(Set<String> titleKws, Set<String> absKws, Integer language) {
    String sql =
        "select t3.kw_str, t3.counts from (select t2.kw_str, count(1) as counts from nsfc_project_kw_all t1 inner join nsfc_project_kw_all t2 on t1.prj_id=t2.prj_id where t1.kw_str in (:titleKws) and t2.kw_str in (:absKws) and t1.kw_str!=t2.kw_str and t1.language =:language and t2.language =:language group by t2.kw_str) t3 order by t3.counts desc, length(t3.kw_str) desc";
    return (List<Map<String, Object>>) this.getSession().createSQLQuery(sql)
        .setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP).setParameterList("titleKws", titleKws)
        .setParameterList("absKws", absKws).setParameter("language", language).list();
  }

  // language=1外文；language=2中文
  public List<BigDecimal> getPdwhPubListByPsnId(Long psnId) {
    String sql =
        "select distinct(t.pdwh_pub_id) from v_pub_pdwh_sns_relation t where exists(select 1 from v_pub_sns p where p.pub_id = t.sns_pub_id and p.create_psn_id =:psnId and p.status=0 and p.pub_type in(3,4))";
    return (List<BigDecimal>) this.getSession().createSQLQuery(sql).setParameter("psnId", psnId).list();
  }

  // 需要限制论文获取数量
  public List<BigDecimal> getPdwhPubListByPsnId(Long psnId, Integer pubYear, Integer size) {
    String sql =
        "select distinct(t.pdwh_pub_id) from v_pub_pdwh_sns_relation t where exists(select 1 from v_pub_sns p where p.pub_id = t.sns_pub_id and p.create_psn_id =:psnId and p.status=0 and p.pub_type in(3,4) and p.publish_year>:pubYear) order by t.pdwh_pub_id desc";
    if (size == null || size == 0) {
      return (List<BigDecimal>) this.getSession().createSQLQuery(sql).setParameter("psnId", psnId)
          .setParameter("pubYear", pubYear).list();
    } else {
      return (List<BigDecimal>) this.getSession().createSQLQuery(sql).setParameter("psnId", psnId)
          .setParameter("pubYear", pubYear).setMaxResults(size).list();
    }
  }

  public List<BigDecimal> getPdwhPubListByPsnId(Long psnId, Integer size) {
    String sql =
        "select distinct(t.pdwh_pub_id) from v_pub_pdwh_sns_relation t where exists(select 1 from v_pub_sns p where p.pub_id = t.sns_pub_id and p.create_psn_id =:psnId and p.status=0 and p.pub_type in(3,4)) order by t.pdwh_pub_id desc";
    if (size == null || size == 0) {
      return (List<BigDecimal>) this.getSession().createSQLQuery(sql).setParameter("psnId", psnId).list();
    } else {
      return (List<BigDecimal>) this.getSession().createSQLQuery(sql).setParameter("psnId", psnId).setMaxResults(size)
          .list();
    }
  }

  public Long getPdwhPubNumByPsnId(Long psnId, Integer pubYear) {
    String sql =
        "select count(distinct(t.pdwh_pub_id)) from v_pub_pdwh_sns_relation t where exists(select 1 from v_pub_sns p where p.pub_id = t.sns_pub_id and p.create_psn_id =:psnId and p.status=0 and p.pub_type in(3,4) and p.publish_year>:pubYear)";
    return this.getSession().createSQLQuery(sql).setParameter("psnId", psnId).setParameter("pubYear", pubYear)
        .uniqueResult() == null ? 0L
            : ((BigDecimal) this.getSession().createSQLQuery(sql).setParameter("psnId", psnId)
                .setParameter("pubYear", pubYear).uniqueResult()).longValue();
  }

  public List<BigDecimal> getSnsPubListByPsnId(Long psnId) {
    String sql =
        "select p.pub_id from v3sns.v_pub_sns p where p.create_psn_id =:psnId and p.status=0 and p.pub_type in(3,4)";
    return (List<BigDecimal>) this.getSession().createSQLQuery(sql).setParameter("psnId", psnId).list();
  }

  public List<String> getKwByTfExcludePubKw(Set<String> titleKws, Set<String> absKws, Integer language) {
    String sql =
        "select p.kw2 from (select t2.kw_str as kw2,t1.kw_str as kw1 from nsfc_project_kw t1 inner join nsfc_project_kw t2 on t1.prj_id=t2.prj_id where t1.kw_str in (:titleKws) and t2.kw_str in (:absKws) and t1.kw_str!=t2.kw_str and  t1.language =:language and t2.language =:language group by t2.kw_str,t1.kw_str) p group by p.kw2 order by count(1) desc, length(p.kw2) desc";
    return (List<String>) this.getSession().createSQLQuery(sql).setParameterList("titleKws", titleKws)
        .setParameterList("absKws", absKws).setParameter("language", language).list();
  }

  // language=1外文；language=2中文
  public List<Map<String, Object>> getKwByCoTfCoutExcludePubKw(Set<String> titleKws, Set<String> absKws,
      Integer language) {
    String sql =
        "select t3.kw2, count(1) as counts from (select t2.kw_str as kw2, t1.kw_str as kw1 from nsfc_project_kw t1 inner join nsfc_project_kw t2 on t1.prj_id=t2.prj_id where t1.kw_str in (:titleKws) and t2.kw_str in (:absKws) and t1.kw_str!=t2.kw_str and t1.language =:language and t2.language =:language group by t2.kw_str,t1.kw_str) t3 group by t3.kw2 order by count(1) desc, length(t3.kw2) desc";
    return (List<Map<String, Object>>) this.getSession().createSQLQuery(sql)
        .setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP).setParameterList("titleKws", titleKws)
        .setParameterList("absKws", absKws).setParameter("language", language).list();
  }

  public Long getPdwhPubId(Long snsPubId) {
    String sql =
        "select t.pdwh_pub_id from v3sns.v_pub_pdwh_sns_relation t where t.sns_pub_id=:snsPubId order by t.pdwh_pub_id desc";
    List<BigDecimal> rsList = new ArrayList<BigDecimal>();
    rsList = this.getSession().createSQLQuery(sql).setParameter("snsPubId", snsPubId).list();
    return rsList.get(0) == null ? 0L : rsList.get(0).longValue();
  }
}
