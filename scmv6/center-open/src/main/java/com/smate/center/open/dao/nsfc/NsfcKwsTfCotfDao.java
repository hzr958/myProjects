package com.smate.center.open.dao.nsfc;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.TreeSet;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.hibernate.transform.Transformers;
import org.springframework.stereotype.Repository;

import com.smate.center.open.model.nsfc.NsfcKwsTfCotf;
import com.smate.core.base.utils.data.PdwhHibernateDao;

@Repository
public class NsfcKwsTfCotfDao extends PdwhHibernateDao<NsfcKwsTfCotf, Long> {

    public Long getCounts(String kw, String category, Integer length) {
        String hql =
                "select sum(t.counts) from NsfcKwsTfCotf t where t.kwFirst =:kw or t.kwSecond =:kw and t.discode =:category and t.size =:length";
        return (Long) super.createQuery(hql).setParameter("kw", kw).setParameter("category", category)
                .setParameter("length", length).uniqueResult();
    }

    public List<BigDecimal> getToHandleList(Integer size) {
        String sql = "select t.prj_id from nsfc_keywords_tf_cotf_prj t where t.status = 0";
        return (List<BigDecimal>) this.getSession().createSQLQuery(sql).setMaxResults(size).list();
    }

    public void updateStatus(Long prjId, Integer status) {
        String sql = "update nsfc_keywords_tf_cotf_prj t set t.status =:status where t.prj_id =:prjId";
        this.getSession().createSQLQuery(sql).setParameter("status", status).setParameter("prjId", prjId)
                .executeUpdate();
    }

    /**
     * pdwh成果，用于计算关键词
     * 
     */
    public List<BigDecimal> getPdwhPubToHandleList(Integer size) {
        String sql = "select t.pub_id from knowledgegraph_pub_pdwh_init t where t.status = 0 order by t.pub_id asc";
        return (List<BigDecimal>) this.getSession().createSQLQuery(sql).setMaxResults(size).list();
    }

    /**
     * pdwh成果，用于计算关键词
     * 
     */
    public List<BigDecimal> getNsfcPrjToHandleList(Integer size) {
        String sql = "select t.pub_id from knowledgegraph_prj_pdwh_init t where t.status = 0 order by t.pub_id asc";
        return (List<BigDecimal>) this.getSession().createSQLQuery(sql).setMaxResults(size).list();
    }

    public List<String> getNsfcCategoryToHandleList(Integer size) {
        String sql = "select t.nsfc_category from nsfc_kw_cotf_init t where t.status = 0 order by t.nsfc_category";
        return (List<String>) this.getSession().createSQLQuery(sql).setMaxResults(size).list();
    }

    public List<String> getNsfcCategoryToHandleList(Integer status, Integer size) {
        String sql = "select t.nsfc_category from nsfc_kw_cotf_init t where t.status =:status order by t.nsfc_category";
        return (List<String>) this.getSession().createSQLQuery(sql).setParameter("status", status).setMaxResults(size)
                .list();
    }

    /**
     * psn_id，用于计算关键词
     * 
     */
    public List<BigDecimal> getPsnToHandleList(Integer size) {
        String sql = "select t.psn_id from knowledgegraph_psn_init t where t.status = 20 order by t.psn_id asc";
        return (List<BigDecimal>) this.getSession().createSQLQuery(sql).setMaxResults(size).list();
    }

    public List<BigDecimal> getPsnToHandleList(Integer size, Integer status) {
        String sql = "select t.psn_id from knowledgegraph_psn_init t where t.status =:status order by t.psn_id asc";
        return (List<BigDecimal>) this.getSession().createSQLQuery(sql).setParameter("status", status)
                .setMaxResults(size).list();
    }

    /**
     * pdwh成果
     * 
     */
    public void updatePdwhPubStatus(Long pubId, Integer status) {
        String sql = "update knowledgegraph_pub_pdwh_init t set t.status =:status where t.pub_id =:pubId";
        this.getSession().createSQLQuery(sql).setParameter("status", status).setParameter("pubId", pubId)
                .executeUpdate();
    }

    public void updateNsfcPrjStatus(Long pubId, Integer status) {
        String sql = "update knowledgegraph_prj_pdwh_init t set t.status =:status where t.pub_id =:pubId";
        this.getSession().createSQLQuery(sql).setParameter("status", status).setParameter("pubId", pubId)
                .executeUpdate();
    }

    public void updateNsfcCategoryStatus(String cateogry, Integer status) {
        String sql = "update nsfc_kw_cotf_init t set t.status =:status where t.nsfc_category =:cateogry";
        this.getSession().createSQLQuery(sql).setParameter("status", status).setParameter("cateogry", cateogry)
                .executeUpdate();
    }

    /**
     * pdwh成果
     * 
     */
    public void updatePsnStatus(Long pubId, Integer status) {
        String sql = "update knowledgegraph_psn_init t set t.status =:status where t.psn_id =:pubId";
        this.getSession().createSQLQuery(sql).setParameter("status", status).setParameter("pubId", pubId)
                .executeUpdate();
    }

    public List<String> getNsfcCategoryByStatus(Integer status) {
        String sql =
                "select t.nsfc_category from nsfc_kw_cotf_init t where t.status =:status order by t.nsfc_category asc";
        return this.getSession().createSQLQuery(sql).setParameter("status", status).list();
    }

    // language = 1英文；language=0中文
    public List<String> getBaseNsfcKwsByCategory(String category, Integer language) {
        String sql =
                "select distinct(t.keyword) from basetrait_dis_keywords t where t.discode =:category and t.keyword_flag =:language";
        return this.getSession().createSQLQuery(sql).setParameter("category", category)
                .setParameter("language", language).list();
    }

    // 单个学科关键词会超过1000个
    public List<String> getRelatedPubKws(String category, List<String> nsfcKws, Integer language) {
        if (nsfcKws == null) {
            return null;
        }
        if (nsfcKws.size() <= 1000) {
            String sql =
                    "select distinct(t.kw_str) from nsfc_pub_kw t where t.pub_id in (select distinct (p.pub_id) from nsfc_pub_kw p where p.kw_str in (:nsfcKws) and substr(p.nsfc_category, 0, 3) =:category) and t.language =:language";
            return this.getSession().createSQLQuery(sql).setParameterList("nsfcKws", nsfcKws)
                    .setParameter("category", category).setParameter("language", language).list();
        } else {
            Integer counts = nsfcKws.size() / 1000;
            Map<String, Object> parameterMap = new HashMap<String, Object>();
            StringBuilder sb = new StringBuilder();
            sb.append(
                    "select distinct(t.kw_str) from nsfc_pub_kw t where t.pub_id in (select distinct (p.pub_id) from nsfc_pub_kw p where (");
            for (Integer i = 0; i <= counts; i++) {
                Integer start = 1000 * i;
                Integer end = nsfcKws.size();
                sb.append(" p.kw_str in (:nsfcKws");
                sb.append(i);
                sb.append(") ");
                if (i < counts) {
                    end = 1000 * (i + 1);
                    sb.append("or");
                }
                ArrayList<String> kws = new ArrayList<String>(nsfcKws.subList(start, end));
                parameterMap.put("nsfcKws" + i, kws);
            }
            parameterMap.put("category", category);
            parameterMap.put("language", language);
            sb.append(
                    ") and substr(p.nsfc_category, 0, 3) =:category) and t.language =:language and substr(t.nsfc_category, 0, 3) =:category");
            return this.getSession().createSQLQuery(sb.toString()).setProperties(parameterMap).list();
        }
    }

    /**
     * SCM基准关键词--依据NSFC管理成果计算单个词的tf
     * 
     * 
     */
    public Integer getPubKwTf(String category, String kw) {
        String sql = "";
        if (!StringUtils.isEmpty(category)) {
            sql = "select count(distinct(t.pub_id)) from nsfc_pub_kw t where t.kw_str =:kw and substr(t.nsfc_category,0,3)=:category";
            return ((BigDecimal) (this.getSession().createSQLQuery(sql).setParameter("kw", kw)
                    .setParameter("category", category).uniqueResult())).intValue();
        } else {
            sql = "select count(distinct(t.pub_id)) from nsfc_pub_kw t where t.kw_str =:kw";
            return ((BigDecimal) (this.getSession().createSQLQuery(sql).setParameter("kw", kw).uniqueResult()))
                    .intValue();
        }
    }


    /**
     * SCM基准关键词--依据NSFC管理成果计算单个词的cotf（没有cotf》1的限制）
     * 
     * 验证关键词： select p.kw2,count(1) from (select t2.kw_str as kw2,t1.kw_str as kw1 from nsfc_pub_kw t1
     * inner join nsfc_pub_kw t2 on t1.pub_id=t2.pub_id where t1.kw_str in (select distinct(t.keyword)
     * from basetrait_dis_keywords t where t.discode = 'F06' and t.keyword_flag=0) and t2.kw_str ='智能规划'
     * and t1.kw_str!=t2.kw_str and t1.language =0 and t2.language =0 and
     * substr(t1.nsfc_category,0,3)=:category and substr(t2.nsfc_category,0,3)=:category group by
     * t2.kw_str,t1.kw_str having count(1)>1) p group by p.kw2;
     * 
     */
    public Integer getPubKwCotf(String category, List<String> categoryNsfcKws, String kw, Integer language) {
        String sql =
                "select count(1) from (select t2.kw_str as kw2,t1.kw_str as kw1 from nsfc_pub_kw t1 inner join nsfc_pub_kw t2 on t1.pub_id=t2.pub_id where t1.kw_str in (select distinct(t.keyword) from basetrait_dis_keywords t where t.discode =:category and t.keyword_flag =:language) and t2.kw_str =:kw and t1.kw_str!=t2.kw_str and  t1.language =:language and t2.language =:language and substr(t1.nsfc_category,0,3)=:category and substr(t2.nsfc_category,0,3)=:category group by t2.kw_str,t1.kw_str having count(1)>1) p group by p.kw2";
        BigDecimal counts = ((BigDecimal) this.getSession().createSQLQuery(sql).setParameter("kw", kw)
                .setParameter("language", language).setParameter("category", category).uniqueResult());
        return counts == null ? 0 : counts.intValue();
    }

    public void saveScmPubKwsInfo(String category, String kw, Integer tf, Integer cotf, Integer language) {
        String sql = "insert into nsfc_base_kw values(seq_base_category.nextval, ?, ?, ?, ?, ?, ?)";
        super.update(sql, new Object[] {language, kw, category, tf, cotf, kw.length()});
    }

    public void deleteScmPubKwsInfo(String category, Integer language) {
        String sql = "select count(1) from nsfc_base_kw t where t.nsfc_category =:category and t.language =:language";
        BigDecimal pubKwCount = (BigDecimal) this.getSession().createSQLQuery(sql).setParameter("category", category)
                .setParameter("language", language).uniqueResult();
        if (pubKwCount != null && pubKwCount.intValue() > 0) {
            String sql8 = "delete nsfc_base_kw t where t.nsfc_category =:category and t.language =:language";
            this.getSession().createSQLQuery(sql8).setParameter("category", category).setParameter("language", language)
                    .executeUpdate();
        }
    }

    public List<Map<String, Object>> getPubIdStr(Integer size, Long status) {
        String sql =
                "select t.pub_seq_id, substr(t.nsfc_category,0,3) as category, title from nsfc_prj_pub_info t where t.status =:status order by t.pub_seq_id asc";
        return this.getSession().createSQLQuery(sql).setParameter("status", status).setMaxResults(size)
                .setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP).list();
    }

    public List<String> getPubKws(Long pubId) {
        String sql = "select distinct(t.pub_keyword) from v_pub_pdwh_keywords t where t.pdwh_pub_id =:pubId";
        return this.getSession().createSQLQuery(sql).setParameter("pubId", pubId).list();
    }

    public List<String> hasPubKwsRecords(Long pubId, String category) {
        String sql = "select t.kw_str from nsfc_pub_kw t where t.pub_id =:pubId and t.nsfc_category =:category";
        return this.getSession().createSQLQuery(sql).setParameter("pubId", pubId).setParameter("category", category)
                .list();
    }

    public void deletehasPubKwsRecords(Long pubId, String category) {
        String sql8 = "delete nsfc_pub_kw t where t.nsfc_category =:category and t.pub_id =:pubId";
        this.getSession().createSQLQuery(sql8).setParameter("category", category).setParameter("pubId", pubId)
                .executeUpdate();
    }

    public void saveScmPubKws(Long pubId, Integer language, String kw, String category) {
        String sql = "insert into nsfc_pub_kw values(?, ?, ?, ?)";
        super.update(sql, new Object[] {pubId, language, kw, category});
    }

    public void updateNsfcPrjPubStatus(Long pubId, Integer status) {
        String sql = "update nsfc_prj_pub_info t set t.status =:status where t.pub_seq_id =:pubId";
        this.getSession().createSQLQuery(sql).setParameter("status", status).setParameter("pubId", pubId)
                .executeUpdate();
    }

    public void updateSubsetsStatus(Long pubId, Integer type, Integer status, Long size) {
        String sql =
                "update pdwh_subset_init t set t.status =:status, t.subset_num =:size where t.pid =:pubId and t.ptype =:type";
        this.getSession().createSQLQuery(sql).setParameter("status", status).setParameter("size", size)
                .setParameter("pubId", pubId).setParameter("type", type).executeUpdate();
    }

    public List<BigDecimal> getSubsetsToHandleList(Integer size, Integer type) {
        String sql = "select t.pid from pdwh_subset_init t where t.status = 0 and t.ptype =:type order by t.pid asc";
        return (List<BigDecimal>) this.getSession().createSQLQuery(sql).setParameter("type", type).setMaxResults(size)
                .list();
    }

    /**
     * pdwh成果，用于计算关键词
     * 
     */
    public List<BigDecimal> getJiangxiToHandleList() {
        String sql = "select t.kw_id from jiangxi_ins_kg_kwtf_tmp t where t.status = 0";
        return (List<BigDecimal>) this.getSession().createSQLQuery(sql).list();
    }

    public Map<String, Object> getJiangxiPrjkw(Long prjId) {
        String sql = "select t.* from jiangxi_ins_kg_kwtf_tmp t where t.kw_id =:prjId";
        return (Map<String, Object>) this.getSession().createSQLQuery(sql).setParameter("prjId", prjId)
                .setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP).uniqueResult();
    }

    public void updateJiangxiPrjkw(Long kwId, String insId, String insName, String kw, Integer count, Integer status) {
        String sql = "insert into jiangxi_ins_kg_kwtf_tmp1 values(?, ?, ?, ?, ?, ?)";
        super.update(sql, new Object[] {kwId, insId, insName, kw, count, status});
    }

    public void updateJiangxiPrjkwStatus(Long pubId, Integer status) {
        String sql = "update jiangxi_ins_kg_kwtf_tmp t set t.status =:status where t.kw_id =:pubId";
        this.getSession().createSQLQuery(sql).setParameter("status", status).setParameter("pubId", pubId)
                .executeUpdate();
    }

    /*
     * 使用知识库来过滤没有关系的关键词(cotf>1 && kw_num =2)
     * 
     */
    public List<Object> getGroupedKws(String category, TreeSet<String> nsfcKws) {
        if (nsfcKws == null || nsfcKws.size() <= 0) {
            return null;
        }
        if (nsfcKws.size() <= 1000) {
            if (StringUtils.isNotBlank(category)) {
                String sql =
                        "select p.prp_kw_extract, p.cotf from (select t.prp_kw_extract,sum(t.cotf) over (partition by t.prp_kw_extract) as cotf from nsfc_prp_kw_10year_subset_all t where t.prp_kw_extract is not null and t.cotf>1 and t.kw_size=2 and t.prp_kw_extract  in (:nsfcKws) and substr(p.nsfc_category, 0, 3) =:category) p group by p.prp_kw_extract, p.cotf order by p.cotf desc";
                return this.getSession().createSQLQuery(sql).setParameterList("nsfcKws", nsfcKws)
                        .setParameter("category", category).list();
            } else {
                String sql =
                        "select p.prp_kw_extract, p.cotf from (select t.prp_kw_extract,sum(t.cotf) over (partition by t.prp_kw_extract) as cotf from nsfc_prp_kw_10year_subset_all t where t.prp_kw_extract is not null and t.cotf>1 and t.kw_size=2 and t.prp_kw_extract  in (:nsfcKws)) p group by p.prp_kw_extract, p.cotf order by p.cotf desc";
                return this.getSession().createSQLQuery(sql).setParameterList("nsfcKws", nsfcKws).list();
            }
        } else {
            List<String> nsfcKwList = Arrays.asList(nsfcKws.toArray(new String[nsfcKws.size()]));
            Integer counts = nsfcKws.size() / 1000;
            Map<String, Object> parameterMap = new HashMap<String, Object>();
            StringBuilder sb = new StringBuilder();
            sb.append(
                    "select p.prp_kw_extract, p.cotf from (select t.prp_kw_extract,sum(t.cotf) over (partition by t.prp_kw_extract) as cotf from nsfc_prp_kw_10year_subset_all t  where t.prp_kw_extract is not null and t.cotf>1 and t.kw_size=2 and (");
            for (Integer i = 0; i <= counts; i++) {
                Integer start = 1000 * i;
                Integer end = nsfcKws.size();
                sb.append(" t.prp_kw_extract in (:nsfcKws");
                sb.append(i);
                sb.append(") ");
                if (i < counts) {
                    end = 1000 * (i + 1);
                    sb.append("or");
                }
                ArrayList<String> kws = new ArrayList<String>(nsfcKwList.subList(start, end));
                parameterMap.put("nsfcKws" + i, kws);
            }
            sb.append(")");
            if (StringUtils.isNotBlank(category)) {
                parameterMap.put("category", category);
                sb.append(" and t.nsfc_cateogry =:category)");
            }
            sb.append(") p group by p.prp_kw_extract, p.cotf order by p.cotf desc");
            return this.getSession().createSQLQuery(sb.toString()).setProperties(parameterMap).list();
        }
    }

    /**
     * pdwh成果，用于计算关键词
     * 
     */
    public List<String> getGrpKwsList() {
        String sql =
                "select distinct(t.prp_kw_extract) from nsfc_prp_kw_10year_subset_all t where t.kw_size = 1 and t.cotf>3 and t.nsfc_cateogry in ('F06','G01','H02','H01') and t.prp_kw_extract is not null";
        return (List<String>) this.getSession().createSQLQuery(sql).list();
    }

    public List<String> getPsnKwsInfo(Long psnId) {
        String sql =
                "select t.keywords_nsfc from  NSFC_PROJECT_KEYWORDS_20181008 t where substr(t.discode,0,3) = 'G01' and t.keywords_nsfc is not null";
        return (List<String>) this.getSession().createSQLQuery(sql).setMaxResults(1000).list();
    }

    public List<String> getPrjCodeInfo(Long psnId) {
        String sql =
                "select t.prj_code from  NSFC_PROJECT_KEYWORDS_20181008 t where substr(t.discode,0,3) = 'TMP' and t.keywords_nsfc is not null order by t.prj_code desc";
        return (List<String>) this.getSession().createSQLQuery(sql).setMaxResults(200).list();
    }

    public List<String> getPrjExtractKwsInfo() {
        /*
         * String sql =p.zh_title||t.keywords_nsfc
         * "select t.keywords_nsfc from  NSFC_PROJECT_KEYWORDS_20181008 t where exists(select 1 from PROJECT_DATA_FIVE_YEAR_ZLL p where p.approve_code = t.prj_code) and substr(t.discode,0,3) = 'G01' and t.keywords_nsfc is not null order by t.prj_code desc"
         * ;
         */
        String sql =
                "select t.keywords_nsfc from  NSFC_PROJECT_KEYWORDS_20181008 t inner join PROJECT_DATA_FIVE_YEAR_ZLL p on t.prj_code =p.approve_code where substr(t.discode,0,3) = 'TMP' and t.keywords_nsfc is not null order by t.prj_code desc";

        return (List<String>) this.getSession().createSQLQuery(sql).setMaxResults(200).list();
    }

    public String getPrjKwInfoByCode(String prjCode) {
        // String sql =
        // "select t.keywords_nsfc from NSFC_PROJECT_KEYWORDS_20181008 t where exists(select 1 from
        // PROJECT_DATA_FIVE_YEAR_ZLL p where p.approve_code = t.prj_code) and substr(t.discode,0,3) = 'G01'
        // and t.keywords_nsfc is not null and t.prj_code =:prjCode";
        String sql =
                "select p.zh_title||t.keywords_nsfc from  NSFC_PROJECT_KEYWORDS_20181008 t inner join PROJECT_DATA_FIVE_YEAR_ZLL p on t.prj_code =p.approve_code where substr(t.discode,0,3) = 'G01' and t.keywords_nsfc is not null order by t.prj_code desc";
        return (String) this.getSession().createSQLQuery(sql).setParameter("prjCode", prjCode).uniqueResult();
    }

    public void deleteGroupRs() {
        String sql8 = "delete nsfc_prj_clustering_test";
        this.getSession().createSQLQuery(sql8).executeUpdate();
    }

    public void insertIntoGroupRs(String prjId, Integer groupId) {
        String sql = "insert into nsfc_prj_clustering_test values (?,?)";
        super.update(sql, new Object[] {prjId, groupId});
    }

    public List<Map<String, Object>> getNsfcPrpInfoByDiscLast10Year(String discode) {
        // TODO 加入时间限制，目前不用
        String sql = "";
        if (discode.length() == 3) {
            sql = "select t.nsfc_prp_id,t.zh_keywords as zh_kws, t.en_keywords as en_kws, t.prp_year as year from nsfc_prp_10year t where substr(t.discode,0,3) =:discode and t.approve_num is null group by t.nsfc_prp_id,t.zh_keywords,t.en_keywords,t.prp_year";
        } else if (discode.length() == 5) {
            sql = "select t.nsfc_prp_id,t.zh_keywords as zh_kws, t.en_keywords as en_kws, t.prp_year as year from nsfc_prp_10year t where substr(t.discode,0,5) =:discode and t.approve_num is null group by t.nsfc_prp_id,t.zh_keywords,t.en_keywords,t.prp_year";
        } else if (discode.length() == 7) {
            sql = "select t.nsfc_prp_id,t.zh_keywords as zh_kws, t.en_keywords as en_kws, t.prp_year as year from nsfc_prp_10year t where t.discode =:discode and t.approve_num is null group by t.nsfc_prp_id,t.zh_keywords,t.en_keywords,t.prp_year";
        }
        return this.getSession().createSQLQuery(sql).setParameter("discode", discode)
                .setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP).list();
    }

    public List<Map<String, Object>> getNsfcPrjInfoByDiscLast10Year(String discode) {
        // TODO 加入时间限制，目前不用
        String sql = "";
        if (discode.length() == 3) {
            sql = "select t.nsfc_prp_id,t.zh_keywords as zh_kws, t.en_keywords as en_kws, t.prp_year as year from nsfc_prp_10year t where substr(t.discode,0,3) =:discode and t.approve_num is not null group by t.nsfc_prp_id,t.zh_keywords,t.en_keywords,t.prp_year";
        } else if (discode.length() == 5) {
            sql = "select t.nsfc_prp_id,t.zh_keywords as zh_kws, t.en_keywords as en_kws, t.prp_year as year from nsfc_prp_10year t where substr(t.discode,0,5) =:discode and t.approve_num is not null group by t.nsfc_prp_id,t.zh_keywords,t.en_keywords,t.prp_year";
        } else if (discode.length() == 7) {
            sql = "select t.nsfc_prp_id,t.zh_keywords as zh_kws, t.en_keywords as en_kws, t.prp_year as year from nsfc_prp_10year t where t.discode =:discode and t.approve_num is not null group by t.nsfc_prp_id,t.zh_keywords,t.en_keywords,t.prp_year";
        }
        return this.getSession().createSQLQuery(sql).setParameter("discode", discode)
                .setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP).list();
    }

    public List<String> getNsfcPrjApproveNumByDiscLast10Year(String discode) {
        // TODO 加入时间限制，目前不用
        String sql = "";
        if (discode.length() == 3) {
            sql = "select distinct(t.approve_num) as approve_num from nsfc_prp_10year t where substr(t.discode,0,3) =:discode and t.approve_num is not null";
        } else if (discode.length() == 5) {
            sql = "select distinct(t.approve_num) as approve_num from nsfc_prp_10year t where substr(t.discode,0,5) =:discode and t.approve_num is not null";
        } else if (discode.length() == 7) {
            sql = "select distinct(t.approve_num) as approve_num from nsfc_prp_10year t where t.discode =:discode and t.approve_num is not null";
        }
        return this.getSession().createSQLQuery(sql).setParameter("discode", discode).list();
    }

    // 学科主任维护关键词
    public List<String> getNsfcKw(String discode) {
        // TODO 加入时间限制，目前不用
        String sql = "";
        if (discode.length() == 3) {
            sql = "select distinct(t.keyword)  from nsfc_keywords_discipline t where substr(t.nsfc_application_code,0,3) =:discode";
        } else if (discode.length() == 5) {
            sql = "select distinct(t.keyword) from nsfc_keywords_discipline t where substr(t.nsfc_application_code,0,5) =:discode";
        } else if (discode.length() == 7) {
            sql = "select distinct(t.keyword) from nsfc_keywords_discipline t where t.nsfc_application_code =:discode";
        }
        return this.getSession().createSQLQuery(sql).setParameter("discode", discode).list();
    }

    public List<BigDecimal> getPubInfoByApproveNum(List<String> approveNumList) {
        if (approveNumList == null || approveNumList.size() <= 0) {
            return null;
        }
        if (approveNumList.size() <= 1000) {
            String sql =
                    "select distinct(t.pdwh_pub_id) from v_pub_pdwh_fund_info t where t.fund_no in (:approveNumList)";
            return this.getSession().createSQLQuery(sql).setParameterList("approveNumList", approveNumList).list();
        } else {
            Integer counts = approveNumList.size() / 1000;
            Map<String, Object> parameterMap = new HashMap<String, Object>();
            StringBuilder sb = new StringBuilder();
            sb.append("select distinct(t.pdwh_pub_id) from v_pub_pdwh_fund_info t where ");
            for (Integer i = 0; i <= counts; i++) {
                Integer start = 1000 * i;
                Integer end = approveNumList.size();
                sb.append(" t.fund_no in (:approveNums");
                sb.append(i);
                sb.append(") ");
                if (i < counts) {
                    end = 1000 * (i + 1);
                    sb.append("or");
                }
                ArrayList<String> kws = new ArrayList<String>(approveNumList.subList(start, end));
                parameterMap.put("approveNums" + i, kws);
            }
            return this.getSession().createSQLQuery(sb.toString()).setProperties(parameterMap).list();
        }
    }

    public Object getPubYear(Long pubId) {
        String sql = "select t.publish_year from v_pub_pdwh t where t.pub_id =:pubId";
        return this.getSession().createSQLQuery(sql).setParameter("pubId", pubId).uniqueResult();
    }

    public Long getNsfcKwPk() {
        String sql = "select seq_nsfc_kw_ytf.nextval from dual";
        return ((BigDecimal) this.getSession().createSQLQuery(sql).uniqueResult()).longValue();
    }

    /**
     * language:1英文，0中文；year：9999标记为没有年份
     * 
     */
    public void insertIntoNsfcwtf(Long kwId, String discode, String kw, Integer language, Integer year, Double wtf) {
        String sql = "insert into NSFC_KW_WTF values (seq_nsfc_kw_wtf.nextval,?,?,?,?,?,?)";
        super.update(sql, new Object[] {kwId, discode, kw, language, year, wtf});
    }

    /**
     * language:1英文，0中文；type：1学科主任维护，0其他
     * 
     */
    public void insertIntoNsfcYtf(Long kwId, String discode, String kw, Integer language, Double ytf, Integer type) {
        String sql = "insert into NSFC_KW_YTF values (?,?,?,?,?,?)";
        super.update(sql, new Object[] {kwId, discode, kw, language, ytf, type});
    }

    public void insertIntoNsfcCotfLength2(String discode, String kwsStr, String kw1, String kw2, Integer cotf,
            Integer language) {
        String sql = "insert into nsfc_kw_cotf_two values (seq_nsfc_kw_wtf.nextval, ?,?,?,?,?,?)";
        super.update(sql, new Object[] {discode, kwsStr, kw1, kw2, cotf, language});
    }

    public void insertIntoNsfcCotfLength3(String discode, String kwsStr, String kw1, String kw2, String kw3,
            Integer cotf, Integer language) {
        String sql = "insert into nsfc_kw_cotf_three values (seq_nsfc_kw_wtf.nextval, ?,?,?,?,?,?,?)";
        super.update(sql, new Object[] {discode, kwsStr, kw1, kw2, kw3, cotf, language});
    }

    public void insertIntoNsfcCotfLength4(String discode, String kwsStr, String kw1, String kw2, String kw3, String kw4,
            Integer cotf, Integer language) {
        String sql = "insert into nsfc_kw_cotf_four values (seq_nsfc_kw_wtf.nextval, ?,?,?,?,?,?,?,?)";
        super.update(sql, new Object[] {discode, kwsStr, kw1, kw2, kw3, kw4, cotf, language});
    }

    public void insertIntoNsfcCotfLength5(String discode, String kwsStr, String kw1, String kw2, String kw3, String kw4,
            String kw5, Integer cotf, Integer language) {
        String sql = "insert into nsfc_kw_cotf_five values (seq_nsfc_kw_wtf.nextval, ?,?,?,?,?,?,?,?,?)";
        super.update(sql, new Object[] {discode, kwsStr, kw1, kw2, kw3, kw4, kw5, cotf, language});
    }

    public Map<String, Object> getNsfcPrpInfo10Years(Long prpId) {
        String sql =
                "select t.approve_num,t.zh_title,t.en_title,t.discode,t.zh_keywords,t.en_keywords from nsfc_prp_10year t where t.id =:prpId";
        return (Map<String, Object>) this.getSession().createSQLQuery(sql)
                .setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP).setParameter("prpId", prpId).uniqueResult();
    }

    public String getPubNsfcCategory(Long pubId) {
        String sql =
                "select t.nsfc_category_id from pub_category_nsfc_by_journal t where t.pub_id = :pubId and rownum=1 order by t.id asc";
        return (String) this.getSession().createSQLQuery(sql).setParameter("pubId", pubId).uniqueResult();
    }

    public List<Map<String, Object>> getKwsCotfSizeTwo(List<String> subsetList, String discode, Integer language) {
        if (subsetList == null || subsetList.size() <= 0) {
            return null;
        }
        if (subsetList.size() <= 1000) {
            String sql = "select t.discode,t.kwstr,t.cotf from nsfc_kw_cotf_two t where t.kwstr in (:subsetList)";
            return this.getSession().createSQLQuery(sql).setParameterList("subsetList", subsetList).list();
        } else {
            Integer counts = subsetList.size() / 1000;
            Map<String, Object> parameterMap = new HashMap<String, Object>();
            StringBuilder sb = new StringBuilder();
            sb.append("select t.discode,t.kwstr,t.cotf from nsfc_kw_cotf_two t where (");
            for (Integer i = 0; i <= counts; i++) {
                Integer start = 1000 * i;
                Integer end = subsetList.size();
                sb.append(" t.kwstr in (:subsetList");
                sb.append(i);
                sb.append(") ");
                if (i < counts) {
                    end = 1000 * (i + 1);
                    sb.append("or");
                }
                ArrayList<String> kws = new ArrayList<String>(subsetList.subList(start, end));
                parameterMap.put("subsetList" + i, kws);
            }
            sb.append(")");
            if (StringUtils.isNotBlank(discode)) {
                if (discode.length() == 1) {
                    parameterMap.put("discode", discode);
                    sb.append(" and substr(t.discode,0,1) =:discode");
                } else {
                    parameterMap.put("discode", discode);
                    sb.append(" and t.discode =:discode");
                }
            }
            if (language != null) {
                parameterMap.put("language", language);
                sb.append(" and t.language =:language");
            }
            return this.getSession().createSQLQuery(sb.toString()).setProperties(parameterMap)
                    .setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP).list();
        }
    }

    public List<Map<String, Object>> getKwsCotfSizeThree(List<String> subsetList, String discode, Integer language) {
        if (subsetList == null || subsetList.size() <= 0) {
            return null;
        }
        if (subsetList.size() <= 1000) {
            String sql = "select t.discode,t.kwstr,t.cotf from nsfc_kw_cotf_three t where t.kwstr in (:subsetList)";
            return this.getSession().createSQLQuery(sql).setParameterList("subsetList", subsetList).list();
        } else {
            Integer counts = subsetList.size() / 1000;
            Map<String, Object> parameterMap = new HashMap<String, Object>();
            StringBuilder sb = new StringBuilder();
            sb.append("select t.discode,t.kwstr,t.cotf from nsfc_kw_cotf_three t where (");
            for (Integer i = 0; i <= counts; i++) {
                Integer start = 1000 * i;
                Integer end = subsetList.size();
                sb.append(" t.kwstr in (:subsetList");
                sb.append(i);
                sb.append(") ");
                if (i < counts) {
                    end = 1000 * (i + 1);
                    sb.append("or");
                }
                ArrayList<String> kws = new ArrayList<String>(subsetList.subList(start, end));
                parameterMap.put("subsetList" + i, kws);
            }
            sb.append(")");
            if (StringUtils.isNotBlank(discode)) {
                if (discode.length() == 1) {
                    parameterMap.put("discode", discode);
                    sb.append(" and substr(t.discode,0,1) =:discode");
                } else {
                    parameterMap.put("discode", discode);
                    sb.append(" and t.discode =:discode");
                }
            }
            if (language != null) {
                parameterMap.put("language", language);
                sb.append(" and t.language =:language");
            }
            return this.getSession().createSQLQuery(sb.toString()).setProperties(parameterMap)
                    .setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP).list();
        }
    }

    public List<Map<String, Object>> getKwsCotfSizeFour(List<String> subsetList, String discode, Integer language) {
        if (subsetList == null || subsetList.size() <= 0) {
            return null;
        }
        if (subsetList.size() <= 1000) {
            String sql = "select t.discode,t.kwstr,t.cotf from nsfc_kw_cotf_four t where t.kwstr in (:subsetList)";
            return this.getSession().createSQLQuery(sql).setParameterList("subsetList", subsetList).list();
        } else {
            Integer counts = subsetList.size() / 1000;
            Map<String, Object> parameterMap = new HashMap<String, Object>();
            StringBuilder sb = new StringBuilder();
            sb.append("select t.discode,t.kwstr,t.cotf from nsfc_kw_cotf_four t where (");
            for (Integer i = 0; i <= counts; i++) {
                Integer start = 1000 * i;
                Integer end = subsetList.size();
                sb.append(" t.kwstr in (:subsetList");
                sb.append(i);
                sb.append(") ");
                if (i < counts) {
                    end = 1000 * (i + 1);
                    sb.append("or");
                }
                ArrayList<String> kws = new ArrayList<String>(subsetList.subList(start, end));
                parameterMap.put("subsetList" + i, kws);
            }
            sb.append(")");
            if (StringUtils.isNotBlank(discode)) {
                if (discode.length() == 1) {
                    parameterMap.put("discode", discode);
                    sb.append(" and substr(t.discode,0,1) =:discode");
                } else {
                    parameterMap.put("discode", discode);
                    sb.append(" and t.discode =:discode");
                }
            }
            if (language != null) {
                parameterMap.put("language", language);
                sb.append(" and t.language =:language");
            }
            return this.getSession().createSQLQuery(sb.toString()).setProperties(parameterMap)
                    .setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP).list();
        }
    }

    public List<Map<String, Object>> getKwsCotfSizeFive(List<String> subsetList, String discode, Integer language) {
        if (subsetList == null || subsetList.size() <= 0) {
            return null;
        }
        if (subsetList.size() <= 1000) {
            String sql = "select t.discode,t.kwstr,t.cotf from nsfc_kw_cotf_five t where t.kwstr in (:subsetList)";
            return this.getSession().createSQLQuery(sql).setParameterList("subsetList", subsetList).list();
        } else {
            Integer counts = subsetList.size() / 1000;
            Map<String, Object> parameterMap = new HashMap<String, Object>();
            StringBuilder sb = new StringBuilder();
            sb.append("select t.discode,t.kwstr,t.cotf from nsfc_kw_cotf_five t where (");
            for (Integer i = 0; i <= counts; i++) {
                Integer start = 1000 * i;
                Integer end = subsetList.size();
                sb.append(" t.kwstr in (:subsetList");
                sb.append(i);
                sb.append(") ");
                if (i < counts) {
                    end = 1000 * (i + 1);
                    sb.append("or");
                }
                ArrayList<String> kws = new ArrayList<String>(subsetList.subList(start, end));
                parameterMap.put("subsetList" + i, kws);
            }
            sb.append(")");
            if (StringUtils.isNotBlank(discode)) {
                if (discode.length() == 1) {
                    parameterMap.put("discode", discode);
                    sb.append(" and substr(t.discode,0,1) =:discode");
                } else {
                    parameterMap.put("discode", discode);
                    sb.append(" and t.discode =:discode");
                }
            }
            if (language != null) {
                parameterMap.put("language", language);
                sb.append(" and t.language =:language");
            }
            return this.getSession().createSQLQuery(sb.toString()).setProperties(parameterMap)
                    .setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP).list();
        }
    }

    public List<String> getRelatedKwsBasedOnCotf(List<String> firstKws, List<String> secondKws, String nsfcCategory) {
        if (firstKws == null || firstKws.size() == 0 || secondKws == null || secondKws.size() == 0) {
            return null;
        }
        String sql = "";
        if (StringUtils.isNotBlank(nsfcCategory)) {
            sql = "select distinct(t.kw_2nd) from nsfc_kw_cotf_two t where t.kw_1st in (:firstKws) and t.kw_2nd in (:secondKws) and t.discode = :nsfcCategory";
            return this.getSession().createSQLQuery(sql).setParameterList("firstKws", firstKws)
                    .setParameterList("secondKws", secondKws).setParameter("nsfcCategory", nsfcCategory).list();
        } else {
            sql = "select distinct(t.kw_2nd) from nsfc_kw_cotf_two t where t.kw_1st in (:firstKws) and t.kw_2nd in (:secondKws)";
            return this.getSession().createSQLQuery(sql).setParameterList("firstKws", firstKws)
                    .setParameterList("secondKws", secondKws).list();
        }
    }

    public HashSet<String> getRelatedKwsBasedOnCotf(HashSet<String> kwsSet, List<String> secondKws,
            String nsfcCategory) {
        if (kwsSet == null || kwsSet.size() == 0) {
            return null;
        }
        String sql = "";
        HashSet<String> strs = new HashSet<String>();
        List<String> list1 = new ArrayList<String>();
        List<String> list2 = new ArrayList<String>();
        if (StringUtils.isNotBlank(nsfcCategory)) {
            sql = "select distinct(t.kw_2nd) from smart_kw_cotf_two t where t.kwstr in (:kwsSet) and t.kw_2nd in (:secondKws) and t.discode = :nsfcCategory";
            list1 = this.getSession().createSQLQuery(sql).setParameterList("kwsSet", kwsSet)
                    .setParameterList("secondKws", secondKws).setParameter("nsfcCategory", nsfcCategory).list();
            sql = "select distinct(t.kw_1st) from smart_kw_cotf_two t where t.kwstr in (:kwsSet) and t.kw_1st in (:secondKws) and t.discode = :nsfcCategory";
            list2 = this.getSession().createSQLQuery(sql).setParameterList("kwsSet", kwsSet)
                    .setParameterList("secondKws", secondKws).setParameter("nsfcCategory", nsfcCategory).list();
        } else {
            sql = "select distinct(t.kw_2nd) from smart_kw_cotf_two t where t.kwstr in (:kwsSet) and t.kw_2nd in (:secondKws)";
            list1 = this.getSession().createSQLQuery(sql).setParameterList("kwsSet", kwsSet)
                    .setParameterList("secondKws", secondKws).list();
            sql = "select distinct(t.kw_1st) from smart_kw_cotf_two t where t.kwstr in (:kwsSet) and t.kw_1st in (:secondKws)";
            list2 = this.getSession().createSQLQuery(sql).setParameterList("kwsSet", kwsSet)
                    .setParameterList("secondKws", secondKws).list();
        }
        if (CollectionUtils.isNotEmpty(list1)) {
            for (String str : list1) {
                strs.add(str);
            }
        }
        if (CollectionUtils.isNotEmpty(list2)) {
            for (String str : list2) {
                strs.add(str);
            }
        }
        return strs;
    }

    public List<Map<String, Object>> getKwsCotfSizeTwoWithoutDiscode(List<String> subsetList, Integer language) {
        if (subsetList == null || subsetList.size() <= 0) {
            return null;
        }
        if (subsetList.size() <= 1000) {
            String sql = "select t.kwstr,t.cotf from smart_kwcotf_two_nodisc t where t.kwstr in (:subsetList)";
            return this.getSession().createSQLQuery(sql).setParameterList("subsetList", subsetList).list();
        } else {
            Integer counts = subsetList.size() / 1000;
            Map<String, Object> parameterMap = new HashMap<String, Object>();
            StringBuilder sb = new StringBuilder();
            sb.append("select t.kwstr,t.cotf from smart_kwcotf_two_nodisc t where (");
            for (Integer i = 0; i <= counts; i++) {
                Integer start = 1000 * i;
                Integer end = subsetList.size();
                sb.append(" t.kwstr in (:subsetList");
                sb.append(i);
                sb.append(") ");
                if (i < counts) {
                    end = 1000 * (i + 1);
                    sb.append("or");
                }
                ArrayList<String> kws = new ArrayList<String>(subsetList.subList(start, end));
                parameterMap.put("subsetList" + i, kws);
            }
            sb.append(")");
            if (language != null) {
                parameterMap.put("language", language);
                sb.append(" and t.kwstr_lang =:language");
            }
            return this.getSession().createSQLQuery(sb.toString()).setProperties(parameterMap)
                    .setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP).list();
        }
    }

    public List<Map<String, Object>> getKwsCotfSizeThreeWithoutDiscode(List<String> subsetList, Integer language) {
        if (subsetList == null || subsetList.size() <= 0) {
            return null;
        }
        if (subsetList.size() <= 1000) {
            String sql = "select t.kwstr,t.cotf from smart_kwcotf_three_nodisc t where t.kwstr in (:subsetList)";
            return this.getSession().createSQLQuery(sql).setParameterList("subsetList", subsetList).list();
        } else {
            Integer counts = subsetList.size() / 1000;
            Map<String, Object> parameterMap = new HashMap<String, Object>();
            StringBuilder sb = new StringBuilder();
            sb.append("select t.kwstr,t.cotf from smart_kwcotf_three_nodisc t where (");
            for (Integer i = 0; i <= counts; i++) {
                Integer start = 1000 * i;
                Integer end = subsetList.size();
                sb.append(" t.kwstr in (:subsetList");
                sb.append(i);
                sb.append(") ");
                if (i < counts) {
                    end = 1000 * (i + 1);
                    sb.append("or");
                }
                ArrayList<String> kws = new ArrayList<String>(subsetList.subList(start, end));
                parameterMap.put("subsetList" + i, kws);
            }
            sb.append(")");
            if (language != null) {
                parameterMap.put("language", language);
                sb.append(" and t.kwstr_lang =:language");
            }
            return this.getSession().createSQLQuery(sb.toString()).setProperties(parameterMap)
                    .setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP).list();
        }
    }

    public List<Map<String, Object>> getKwsCotfSizeFourWithoutDiscode(List<String> subsetList, Integer language) {
        if (subsetList == null || subsetList.size() <= 0) {
            return null;
        }
        if (subsetList.size() <= 1000) {
            String sql = "select t.kwstr,t.cotf from smart_kwcotf_four_nodisc t where t.kwstr in (:subsetList)";
            return this.getSession().createSQLQuery(sql).setParameterList("subsetList", subsetList).list();
        } else {
            Integer counts = subsetList.size() / 1000;
            Map<String, Object> parameterMap = new HashMap<String, Object>();
            StringBuilder sb = new StringBuilder();
            sb.append("select t.kwstr,t.cotf from smart_kwcotf_four_nodisc t where (");
            for (Integer i = 0; i <= counts; i++) {
                Integer start = 1000 * i;
                Integer end = subsetList.size();
                sb.append(" t.kwstr in (:subsetList");
                sb.append(i);
                sb.append(") ");
                if (i < counts) {
                    end = 1000 * (i + 1);
                    sb.append("or");
                }
                ArrayList<String> kws = new ArrayList<String>(subsetList.subList(start, end));
                parameterMap.put("subsetList" + i, kws);
            }
            sb.append(")");
            if (language != null) {
                parameterMap.put("language", language);
                sb.append(" and t.kwstr_lang =:language");
            }
            return this.getSession().createSQLQuery(sb.toString()).setProperties(parameterMap)
                    .setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP).list();
        }
    }

    public List<Map<String, Object>> getKwsCotfSizeFiveWithoutDiscode(List<String> subsetList, Integer language) {
        if (subsetList == null || subsetList.size() <= 0) {
            return null;
        }
        if (subsetList.size() <= 1000) {
            String sql = "select t.kwstr,t.cotf from smart_kwcotf_five_nodisc t where t.kwstr in (:subsetList)";
            return this.getSession().createSQLQuery(sql).setParameterList("subsetList", subsetList).list();
        } else {
            Integer counts = subsetList.size() / 1000;
            Map<String, Object> parameterMap = new HashMap<String, Object>();
            StringBuilder sb = new StringBuilder();
            sb.append("select t.kwstr,t.cotf from smart_kwcotf_five_nodisc t where (");
            for (Integer i = 0; i <= counts; i++) {
                Integer start = 1000 * i;
                Integer end = subsetList.size();
                sb.append(" t.kwstr in (:subsetList");
                sb.append(i);
                sb.append(") ");
                if (i < counts) {
                    end = 1000 * (i + 1);
                    sb.append("or");
                }
                ArrayList<String> kws = new ArrayList<String>(subsetList.subList(start, end));
                parameterMap.put("subsetList" + i, kws);
            }
            sb.append(")");
            if (language != null) {
                parameterMap.put("language", language);
                sb.append(" and t.kwstr_lang =:language");
            }
            return this.getSession().createSQLQuery(sb.toString()).setProperties(parameterMap)
                    .setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP).list();
        }
    }
}
