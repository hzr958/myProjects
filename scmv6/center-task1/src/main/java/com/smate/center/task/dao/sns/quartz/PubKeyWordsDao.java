package com.smate.center.task.dao.sns.quartz;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.smate.center.task.model.sns.quartz.PubKeyWords;
import com.smate.core.base.utils.data.SnsHibernateDao;

@Repository
public class PubKeyWordsDao extends SnsHibernateDao<PubKeyWords, Long> {
  public List<PubKeyWords> getPubKeyWord(Long psnId) {
    String hql = "select new PubKeyWords(p.id,p.pubId,p.keyword,p.keywordTxt) from PubKeyWords p where p.psnId=:psnId";
    return super.createQuery(hql).setParameter("psnId", psnId).list();
  }

  public void updateSourceTypeById(Long id, int soruceType) {
    String hql = "update PubKeyWords t set t.sourceType=:sourceType where t.id=:id";
    super.createQuery(hql).setParameter("sourceType", soruceType).setParameter("id", id).executeUpdate();
  }

  public int getPubkeyWordCount(int sourceType, String keywordTxt, Long psnId) {
    String hql =
        "select count(1) from PubKeyWords t where t.keywordTxt=:keywordTxt and t.sourceType=:sourceType and t.hxj=1 and ((t.auSeq between 1 and 3)or t.auPos =1) and t.psnId=:psnId";
    return (int) super.createQuery(hql).setParameter("keywordTxt", keywordTxt).setParameter("sourceType", sourceType)
        .setParameter("psnId", psnId).uniqueResult();

  }

  public List<PubKeyWords> getkeyWords(Long psnId) {
    String hql =
        "select new PubKeyWords(p.id,p.pubId,p.psnId,p.keyword,p.keywordTxt) from PubKeyWords p where p.psnId=:psnId";
    return super.createQuery(hql).setParameter("psnId", psnId).list();
  }

  public List<PubKeyWords> listByPubId(Long pubId) {
    String hql = "from PubKeyWords t where t.pubId=:pubId";
    return this.createQuery(hql).setParameter("pubId", pubId).list();
  }

}
