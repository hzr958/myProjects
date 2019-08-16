package com.smate.center.batch.dao.sns.pub;

import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Repository;

import com.smate.center.batch.model.sns.pub.PublicationJournal;
import com.smate.core.base.utils.data.SnsHibernateDao;

/**
 * 成果期刊信息.
 * 
 * @author liqinghua
 * 
 */
@Repository
public class PublicationJournalDao extends SnsHibernateDao<PublicationJournal, Long> {

  /**
   * 保存成果期刊信息.
   * 
   * @param pubId
   * @param psnId
   * @param issn
   * @param jname
   */
  public void savePubJournal(Long pubId, Long psnId, String issn, String jname, Long jnlId, Integer pubYear) {
    PublicationJournal pj = super.get(pubId);
    issn = StringUtils.substring(issn, 0, 100);
    jname = StringUtils.substring(jname, 0, 300);
    if (pj == null) {
      pj = new PublicationJournal(pubId, psnId, issn, jname, jnlId, pubYear);
    } else {
      pj.setPsnId(psnId);
      pj.setIssn(issn);
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
    String hql = "delete from PublicationJournal where pubId = ? ";
    super.createQuery(hql, pubId).executeUpdate();
  }

  /**
   * 获取成果期刊ISSN信息.
   * 
   * @param pubId
   * @return
   */
  public String getPubJIssn(Long pubId) {

    String hql = "select issn from PublicationJournal t where t.pubId = ? ";
    return super.findUnique(hql, pubId);
  }

  /**
   * 获取匹配上人员的期刊ISSN列表（第1、2、3作者或通信作者）.
   * 
   * @param psnId
   * @return
   */
  public List<Object[]> getPsnOwnerPubIssn(Long psnId) {

    String hql =
        "select lower(t.issn),count(t.pubId) from PublicationJournal t,PubOwnerMatch t1 where t.issn is not null "
            + "and t.pubId = t1.pubId and t1.auSeq > 0 and (t1.auSeq <= 3 or t1.auPos = 1) and t.pubYear >= 2007 and t.psnId = ? "
            + " group by lower(t.issn) ";
    return super.createQuery(hql, psnId).list();
  }
}
