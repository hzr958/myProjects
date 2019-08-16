package com.smate.center.batch.dao.pdwh.prj;


import java.util.List;

import org.springframework.stereotype.Repository;

import com.smate.center.batch.model.pdwh.prj.NsfcKwsTfCotf;
import com.smate.core.base.utils.data.PdwhHibernateDao;

@Repository
public class NsfcKwDisciplineDao extends PdwhHibernateDao<NsfcKwsTfCotf, Long> {

    /*
     * 获取科研之友词典（由nsfc项目关键词，学科主任维护关键词，以及项目相关成果扩展关键词组成）
     */
    @SuppressWarnings("unchecked")
    public List<String> getScmKwDic() {
        String sql = "select distinct(t.kw_str) from scm_base_kw t";
        return (List<String>) this.getSession().createSQLQuery(sql).list();
    }


    /*
     * public List<String> getScmKwDic() { String sql = "select t.kw_str from NSFC_KW_YTF_UNIQUE t";
     * return (List<String>) this.getSession().createSQLQuery(sql).list(); }
     */

}
