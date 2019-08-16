package com.smate.center.batch.dao.sns.psn;

import org.springframework.stereotype.Repository;

import com.smate.center.batch.model.sns.pub.PsnJournalRefc;
import com.smate.core.base.utils.data.SnsHibernateDao;

/**
 * 人员参考文献期刊数据(用于阅读的期刊统计).
 * 
 * @author liqinghua
 * 
 */
@Repository
public class PsnJournalRefcDao extends SnsHibernateDao<PsnJournalRefc, Long> {

  /**
   * 更新人员最新收藏文献期刊.
   * 
   * @param psnId
   */
  public void updatePsnJournalRefc(Long psnId) {

    // hql insert select子查询 有问题，使用SQL
    String delSql =
        "delete from PSN_JOURNAL_REFC t where not exists(select 1 from PUB_REFC_JOURNAL t1 where t.psn_id = t1.psn_id and t1.issn_txt = t.issn_txt) and t.psn_id = ? ";
    super.update(delSql, new Object[] {psnId});
    String insertSql =
        "insert into PSN_JOURNAL_REFC(id,psn_id,issn,issn_txt) select SEQ_PSN_JOURNAL_REFC.nextval,psn_id,issn,issn_txt from(select distinct t.psn_id,t.issn,t.issn_txt from PUB_REFC_JOURNAL t "
            + " where not exists(select 1 from PSN_JOURNAL_REFC t1 where t.psn_id = t1.psn_id and t1.issn_txt = t.issn_txt) and t.issn_txt is not null and t.psn_id = ?) ";
    super.update(insertSql, new Object[] {psnId});
  }

  public int getPsnJnlByRefc(Long psnId, String issn) {
    String hql = "select count(id) from PsnJournalRefc where psnId=? and issnTxt=?";
    return ((Long) super.createQuery(hql, psnId, issn.toLowerCase()).list().get(0)).intValue();
  }

}
