package com.smate.center.batch.dao.sns.pub;

import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Repository;

import com.smate.center.batch.model.sns.pub.PublicationRefcJournal;
import com.smate.core.base.utils.data.SnsHibernateDao;

/**
 * 参考文献期刊数据(用于阅读的期刊统计).
 * 
 * @author liqinghua
 * 
 */
@Repository
public class PublicationRefcJournalDao extends SnsHibernateDao<PublicationRefcJournal, Long> {

  /**
   * 保存成果期刊信息.
   * 
   * @param pubId
   * @param psnId
   * @param issn
   * @param jname
   */
  public void savePubJournal(Long pubId, Long psnId, String issn, String jname, Long jnlId, Integer pubYear) {
    PublicationRefcJournal pj = super.get(pubId);
    issn = StringUtils.substring(issn, 0, 100);
    jname = StringUtils.substring(jname, 0, 300);
    if (pj == null) {
      pj = new PublicationRefcJournal(pubId, psnId, issn, jname, jnlId, pubYear);
    } else {
      pj.setPsnId(psnId);
      pj.setIssn(issn);
      pj.setIssnTxt(StringUtils.lowerCase(issn));
      pj.setJname(jname);
      pj.setJnlId(jnlId);
      pj.setPubYear(pubYear);
    }
    super.save(pj);
  }

  /**
   * 删除成果期刊信息.
   * 
   * @param pubId
   */
  public void delPubJournal(Long pubId) {
    String hql = "delete from PublicationRefcJournal where pubId = ? ";
    super.createQuery(hql, pubId).executeUpdate();
  }



}
