package com.smate.center.task.dao.journal;

import java.util.List;
import java.util.Map;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Repository;

import com.smate.center.task.model.pdwh.pub.BaseJournal;
import com.smate.core.base.utils.data.PdwhHibernateDao;

@Repository
public class BaseJournalDao extends PdwhHibernateDao<BaseJournal, Long> {

  public Long findRegionId(Long jnlId) {
    String hql = "select t.regionId from BaseJournal t where t.jnlId=:jnlId";
    return (Long) this.createQuery(hql).setParameter("jnlId", jnlId).uniqueResult();

  }


  /**
   * 查询base_journal表中与base_jouranal_title表中标题或者pissn不一致的数据
   * 
   * @return
   */
  @SuppressWarnings("unchecked")
  public List<Map<String, Object>> findTitlePissnNotSame() {
    String sql = "select  ba.jnl_id\r\n" + "  from base_journal ba,\r\n"
        + "       (select row_number() over(partition by t.jnl_id order by t.jou_title_id desc) rn,\r\n"
        + "               t.title_en,\r\n" + "               t.jnl_id,\r\n" + "               t.title_xx,\r\n"
        + "               t.pissn\r\n" + "          from base_journal_title t) temp\r\n" + " where temp.rn = 1\r\n"
        + "   and temp.jnl_id = ba.jnl_id\r\n"
        + "   and (ba.title_en <> temp.title_en or ba.title_xx <> temp.title_xx or ba.pissn<>temp.pissn)";
    return super.queryForList(sql);
  }

  /**
   * 使用中英文标题和pissn进行查询，看是否存在重复的数据
   * 
   * @param titleEn
   * @param titleXx
   * @param pissn
   * @return
   */
  public boolean isDuplicateBaseJournal(String titleEn, String titleXx, String pissn) {
    titleEn = StringUtils.isBlank(titleEn) ? "0" : titleEn;
    titleXx = StringUtils.isBlank(titleXx) ? "0" : titleXx;
    String hql = "from BaseJournal where nvl(titleEn,'0')=? and nvl(titleXx,'0')=? and pissn=?";
    List<?> list = super.createQuery(hql, titleEn, titleXx, pissn).list();
    if (CollectionUtils.isNotEmpty(list)) {
      return true;
    }
    return false;
  }
}
