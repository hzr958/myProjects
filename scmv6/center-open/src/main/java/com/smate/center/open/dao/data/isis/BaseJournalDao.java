package com.smate.center.open.dao.data.isis;


import com.smate.center.open.isis.model.data.isis.BaseJournal;
import com.smate.center.open.model.pdwh.jnl.BaseJournal2;
import com.smate.core.base.utils.data.PdwhHibernateDao;
import com.smate.core.base.utils.string.JnlFormateUtils;
import org.apache.commons.collections4.CollectionUtils;
import org.hibernate.Query;
import org.hibernate.transform.Transformers;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.*;


/**
 * pwdh BaseJournal dao
 * 
 * @author hp
 * 
 */
@Repository
public class BaseJournalDao extends PdwhHibernateDao<BaseJournal, Long> {
  @SuppressWarnings ("unchecked") public Map<Long, String> getRomeoColour(Set<Long> jidSet) {
    Map<Long, String> jidColorMap = new HashMap<Long, String>();
    if (jidSet.size() == 0) {
      return jidColorMap;
    }
    super.sessionFactory.getCurrentSession();
    String sql =
        "select j.jid jid,bj.romeo_colour romeo_colour from base_journal bj,journal j where bj.jnl_id=j.match_base_jnl_id and j.jid in (:jids) and bj.romeo_colour  is not null";
    List<Map<String, Object>> jidtempMap = this.getSession().createSQLQuery(sql).setParameterList("jids", jidSet)
        .setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP).list();
    for (Map<String, Object> tempMap : jidtempMap) {
      jidColorMap.put(Long.valueOf(tempMap.get("JID").toString()), tempMap.get("ROMEO_COLOUR").toString());
    }
    return jidColorMap;
  }

  @SuppressWarnings ("unchecked") public List<Long> getIsInJournalCategory(Set<Long> jidSet) {
    List<Long> jidList = new ArrayList<Long>();
    if (jidSet.size() == 0) {
      return jidList;
    }
    String sql =
        "select j.jid jid  from base_journal_category bjc,journal j where bjc.jnl_id=j.match_base_jnl_id and j.jid in (:jids)"
            + "  and dbid in (16,17) ";
    List<BigDecimal> jidtempList = this.getSession().createSQLQuery(sql).setParameterList("jids", jidSet).list();
    List<Long> newList = null;
    if (!CollectionUtils.isEmpty(jidtempList)) {
      newList = new ArrayList<Long>();
      for (BigDecimal jid : jidtempList) {
        newList.add(jid.longValue());
      }
    }
    return newList;
  }


  public Long snsJnlMatchBaseJnlId(String jname, String issn) {
    String hql = "select jnlId from BaseJournal  where (lower(titleEn)=? or lower(titleXx)=?) and (pissn=? or eissn=?)";
    return findUnique(hql, jname, jname, issn, issn);
  }

  public BaseJournal2 getBaseJournal(Long jid) {
    String hql = " from BaseJournal2 t where t.jouId=:jid  ";
    BaseJournal2 result = (BaseJournal2) this.createQuery(hql).setParameter("jid", jid).uniqueResult();
    return result;
  }

  public List getBaseJournalTitle(Long jid) {
    String hql = "select t.TITLE_XX , t.TITLE_XX_ALIAS , t.TITLE_EN ,t.TITLE_EN_ALIAS "
        + "t.TITLE_ABBR ,t.TITLE_ABBR_ALIAS from BASE_JOURNAL_TITLE  t where t.JNL_ID=:jid";
    List list = this.getSession().createSQLQuery(hql).setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP)
        .setParameter("jid", jid).list();
    return list;
  }

  public Boolean matchJournalNameByJid(String jname, Long jid) {

    String jnameFmt = JnlFormateUtils.getStrAlias(jname);
    // instr(:jname,t.TITLE_ABBR)>0 or instr(t.TITLE_ABBR ,:jname)>0
    String hql = "select b.JNL_ID from BASE_JOURNAL b,BASE_JOURNAL_TITLE  t "
        + "where t.JNL_ID =:jid and  ( instr(:jname,lower(b.TITLE_EN))>0 or instr(lower(b.TITLE_EN),:jname)>0 or instr(:jname,lower(b.TITLE_XX))>0 or instr(lower(b.TITLE_XX),:jname)>0"
        + " or instr(:jname,lower(t.TITLE_ABBR))>0 or instr(lower(t.TITLE_ABBR) ,:jname)>0  or instr(:jname,lower(t.TITLE_ABBR_ALIAS))>0 or instr(lower(t.TITLE_ABBR_ALIAS) ,:jname)>0 "
        + " or instr(:jname,lower(t.TITLE_EN))>0 or instr(lower(t.TITLE_EN) ,:jname)>0 or instr(:jname,lower(t.TITLE_EN_ALIAS))>0 or instr(lower(t.TITLE_EN_ALIAS) ,:jname)>0"
        + " or instr(:jnameFmt,lower(t.TITLE_XX_ALIAS))>0 or instr(lower(t.TITLE_XX_ALIAS) ,:jnameFmt)>0 or instr(:jname,lower(t.TITLE_XX))>0 or instr(lower(t.TITLE_XX) ,:jname)>0  "
        + " or instr(:jname,lower(t.TITLE_ABBR))>0 or instr(lower(t.TITLE_ABBR) ,:jname)>0 )  and t.JNL_ID=b.JNL_ID ";
    List list = this.getSession().createSQLQuery(hql).setParameter("jname", jname).setParameter("jnameFmt", jnameFmt)
        .setParameter("jid", jid).list();
    if (list != null && list.size() > 0) {
      return true;
    }
    return false;
  }

  /**
   * 获取期刊自动提示内容
   *
   * @param startWith
   * @param size
   * @return
   */
  @SuppressWarnings ("unchecked") public List<BaseJournal2> findBaseJournal(String startWith, int size) {
    String hql = null;
    // 判断是否是非英文,查询本人数据()
    hql = "from BaseJournal2 t where lower( t.titleEn) like ? or lower(t.titleXx) like ? order by t.pissn";
    Query query = super.createQuery(hql, new Object[] {startWith.trim().toLowerCase() + "%",startWith.trim().toLowerCase() + "%"});
    query.setMaxResults(size);
    List<BaseJournal2> list = query.list();
    return list;
  }
}
