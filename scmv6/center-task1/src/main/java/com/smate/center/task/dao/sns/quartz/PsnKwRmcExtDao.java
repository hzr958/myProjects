package com.smate.center.task.dao.sns.quartz;

import org.springframework.stereotype.Repository;

import com.smate.center.task.exception.DaoException;
import com.smate.center.task.model.sns.quartz.PsnKwRmcExt;
import com.smate.core.base.utils.data.SnsHibernateDao;

@Repository
public class PsnKwRmcExtDao extends SnsHibernateDao<PsnKwRmcExt, Long> {
  public void deleteBypsnId(Long psnId) {
    String sql = "delete from psn_kw_rmc_ext where psn_Id=?";
    super.update(sql, new Object[] {psnId});
  }

  public void saveNewFromRmc(Long psnId) throws DaoException {
    StringBuffer str = new StringBuffer();
    str.append("insert into psn_kw_rmc_ext (id, rmc_id, psn_id, keyword, kw_txt, type,kw_gid)");
    str.append(
        " select seq_psn_kw_rmc.nextval,t.id,t.psn_id,t1.zh_keyword,t1.zh_kw_txt,1,t.kw_gid from psn_kw_rmc t,keywords_entran_zh t1");
    str.append(" where t.psn_id = ?  and t.type = 1 and t.keyword_txt = t1.en_kw_txt");
    str.append(
        " and not exists(select 1 from psn_kw_rmc t2 where t2.keyword_txt = t1.zh_kw_txt and t2.psn_id = t.psn_id)");
    super.update(str.toString(), new Object[] {psnId});
  }

  public void saveNewFromRmcEn(Long psnId) throws DaoException {
    StringBuffer str = new StringBuffer();
    str.append("insert into psn_kw_rmc_ext (id, rmc_id, psn_id, keyword, kw_txt, type,kw_gid)");
    str.append(
        " select seq_psn_kw_rmc.nextval,t.id,t.psn_id,t1.zh_keyword,t1.en_kw_txt,1,t.kw_gid from psn_kw_rmc t,keywords_zhtran_en t1 ");
    str.append("where t.psn_id = ?  and t.type = 2 and t.keyword_txt = t1.zh_kw_txt");
    str.append(
        " and not exists(select 1 from psn_kw_rmc t2 where t2.keyword_txt = t1.en_kw_txt and t2.psn_id = t.psn_id)");
    super.update(str.toString(), new Object[] {psnId});
  }

  public void saveNewFromKwSyn(Long psnId) throws DaoException {
    StringBuffer sb = new StringBuffer();
    sb.append("insert into psn_kw_rmc_ext (id, rmc_id, psn_id, keyword, kw_txt, type,kw_gid)");
    sb.append(
        " select seq_psn_kw_rmc.nextval,t.id,t.psn_id,t1.syn_keyword,t1.syn_kwtxt,2,t.kw_gid from psn_kw_rmc t,KEYWORDS_SYNONYM t1");
    sb.append(" where t.keyword_txt = t1.zh_kwtxt and t.psn_id = ?");
    sb.append(
        " and not exists(select 1 from psn_kw_rmc t2 where t2.keyword_txt = t1.syn_kwtxt and t2.psn_id = t.psn_id)");
    super.update(sb.toString(), new Object[] {psnId});
  }
}
