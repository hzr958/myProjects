package com.smate.center.task.dao.sns.quartz;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.smate.center.task.model.sns.quartz.PsnKwEptTmp;
import com.smate.core.base.utils.data.SnsHibernateDao;

/**
 * 人员关键词临时表dao
 * 
 * @author zjh
 *
 */
@Repository
public class PsnKwEptTmpDao extends SnsHibernateDao<PsnKwEptTmp, Long> {
  @Transactional(rollbackFor = Exception.class, propagation = Propagation.REQUIRES_NEW)
  public void deleteFromPsnKwEptTmp() {
    String sql = "truncate table PSN_KW_EPT_TMP";
    super.update(sql);
  }

  public void addNewZhToPubKwEptTmp(Long psnId) {
    StringBuffer sql = new StringBuffer();
    sql.append("insert into  PSN_KW_EPT_TMP(id, psn_id, keyword_txt,pt1_num, pt2_num, pt3_num, prj_num, score) ");
    sql.append("select  SEQ_PSN_KW_EPT_TMP.nextval ,psn_id,zh_kw_txt,0,0,0,0,0 from ");
    sql.append(
        "(select t.psn_id,t.zh_kw_txt from psn_kw_pub t where t.psn_id=?  and t.hxj=1 and ((t.au_seq between 1 and 3) or t.au_pos =1) and t.zh_kw_len>0");
    sql.append("group by t.psn_id, t.zh_kw_txt)");
    super.update(sql.toString(), new Object[] {psnId});
  }

  public void updateNewPubZhkwByPsnId(Long psnId) {
    String hql =
        "update PsnKwEptTmp t set t.keyWord=(select t1.zhKw from  PsnKwPub t1 where t.psnId=t1.psnId and t.keyWordTxt=t1.zhKwTxt and rownum=1) where t.psnId=:psnId and t.keyWord is null";
    super.createQuery(hql).setParameter("psnId", psnId).executeUpdate();

  }

  public void addNewEnToPubKwEptTmp(Long psnId) {
    StringBuffer sql = new StringBuffer();
    sql.append("insert into  PSN_KW_EPT_TMP(id, psn_id, keyword_txt,pt1_num, pt2_num, pt3_num, prj_num, score) ");
    sql.append("select  SEQ_PSN_KW_EPT_TMP.nextval ,psn_id,en_kw_txt,0,0,0,0,0 from ");
    sql.append(
        "(select t.psn_id,t.en_kw_txt from psn_kw_pub t where t.psn_id=?  and t.hxj=1 and ((t.au_seq between 1 and 3) or t.au_pos =1) and t.en_kw_len>0");
    sql.append("group by t.psn_id, t.en_kw_txt)");
    super.update(sql.toString(), new Object[] {psnId});
  }

  public void updateNewPubEnkwByPsnId(Long psnId) {
    String hql =
        "update PsnKwEptTmp t set t.keyWord=(select t1.enKw from  PsnKwPub t1 where t.psnId=t1.psnId and t.keyWordTxt=t1.enKwTxt and rownum=1) where t.psnId=:psnId and t.keyWord is null";
    super.createQuery(hql).setParameter("psnId", psnId).executeUpdate();

  }

  public void insertNewPubZhkw(Long psnId) {
    StringBuffer sql = new StringBuffer();
    sql.append("insert into  PSN_KW_EPT_TMP(id, psn_id, keyword_txt,pt1_num, pt2_num, pt3_num, prj_num, score)");
    sql.append("select  SEQ_PSN_KW_EPT_TMP.nextval ,psn_id,'',0,0,0,0,0 from");
    sql.append("(select t.psn_id from PUB_KEYWORDS t where t.psn_id=? group by t.psn_id)");
    super.update(sql.toString(), new Object[] {psnId});
  }

  public void updatePubZhkwByPsnId(Long psnId, String zhKw, String keyWordTxt, Long pubId) {
    String hql =
        "update PsnKwEptTmp t set t.keyWord=:zhKw,t.keyWordTxt=:keyWordTxt,t.pubId=:pubId where t.psnId=:psnId and t.keyWord is null";
    super.createQuery(hql).setParameter("zhKw", zhKw).setParameter("keyWordTxt", keyWordTxt)
        .setParameter("pubId", pubId).setParameter("psnId", psnId);

  }

  public void insertNewPubEnkw(Long psnId) {
    StringBuffer sql = new StringBuffer();
    sql.append("insert into PSN_KW_EPT_TMP(id, psn_id, keyword_txt,pt1_num, pt2_num, pt3_num, prj_num, score)");
    sql.append("select SEQ_PSN_KW_EPT_TMP.nextval,psn_id,'',0,0,0,0,0 from (");
    sql.append("select t.psn_id from PUB_KEYWORDS t");
    sql.append("where t.psn_id=?");
    sql.append(" group by t.psn_id)");
    String strSql = sql.toString();
    super.update(strSql, new Object[] {psnId});
  }

  public void updatePubEnkwByPsnId(Long psnId, String enKw, String keyWordTxt, Long pubId) {
    String hql =
        "update PsnKwEptTmp t set t.keyWord=:enKw,t.keyWordTxt=:keyWordTxt,t.pubId=:pubId  where t.psnId=:psnId and t.keyWord is null";
    super.createQuery(hql).setParameter("enKw", enKw).setParameter("keyWordTxt", keyWordTxt)
        .setParameter("pubId", pubId).setParameter("psnId", psnId).executeUpdate();
  }

  public void insertNewPrjZhkw(Long psnId) {
    StringBuffer sql = new StringBuffer();
    sql.append(" insert into PSN_KW_EPT_TMP (id, psn_id, keyword_txt,pt1_num, pt2_num, pt3_num, prj_num, score)");
    sql.append("select SEQ_PSN_KW_EPT_TMP.nextval,psn_id,zh_kw_txt,0,0,0,0,0 from (");
    sql.append(" select t.psn_id,t.zh_kw_txt  from psn_kw_nsfcprj t where t.psn_id=? and t.zh_kw_len>0 ");
    sql.append(" group by t.psn_id,t.zh_kw_txt)");
    super.update(sql.toString(), new Object[] {psnId});
  }

  public void updatePrjZhkwByPsnId(Long psnId) {
    String hql =
        "update PsnKwEptTmp t set t.keyWord=(select t1.zhKw from PsnKwNsfcprj t1 where t.psnId=t1.psnId and t.keyWordTxt=t1.zhKwTxt and rownum=1) where t.psnId=:psnId and t.keyWord is null";
    super.createQuery(hql).setParameter("psnId", psnId).executeUpdate();
  }

  public void insertNewPrjEnkw(Long psnId) {
    StringBuffer sql = new StringBuffer();
    sql.append(" insert into PSN_KW_EPT_TMP (id, psn_id, keyword_txt,pt1_num, pt2_num, pt3_num, prj_num, score)");
    sql.append("select SEQ_PSN_KW_EPT_TMP.nextval,psn_id,en_kw_txt,0,0,0,0,0 from (");
    sql.append(" select t.psn_id, t.en_kw_txt from psn_kw_nsfcprj t where t.psn_id=? ");
    sql.append("and t.en_kw_len>0  group by t.psn_id, t.en_kw_txt)");
    super.update(sql.toString(), new Object[] {psnId});
  }

  public void updatePrjEnkwByPsnId(Long psnId) {
    String hql =
        "update PsnKwEptTmp t set t.keyWord=(select t1.enKw from PsnKwNsfcprj t1 where t.psnId=t1.psnId and t.keyWordTxt=t1.enKwTxt and rownum=1) where t.psnId=:psnId and t.keyWord is null";
    super.createQuery(hql).setParameter("psnId", psnId);
  }

  public List<Object[]> getKeyWordsByPsnId(Long psnId) {
    String hql = "select distinct t.id,t.keyWordTxt from PsnKwEptTmp t where t.psnId=:psnId";
    List<Object[]> listResult = new ArrayList<Object[]>();
    listResult.addAll(super.createQuery(hql).setParameter("psnId", psnId).list());
    return listResult;
  }

  public void updateScore(Long pt1Num, Long pt2Num, Long pt3Num, Long prjNum, double score, Long kId, Long kwId) {
    String hql =
        "update PsnKwEptTmp set pt1Num=:pt1Num,pt2Num=:pt2Num,pt3Num=:pt3Num,prjNum=:prjNum,score=:score,kId=:kId where id=:kwId";
    super.createQuery(hql).setParameter("pt1Num", pt1Num).setParameter("pt2Num", pt2Num).setParameter("pt3Num", pt3Num)
        .setParameter("prjNum", prjNum).setParameter("score", score).setParameter("kId", kId).setParameter("kwId", kwId)
        .executeUpdate();
  }

  public List<PsnKwEptTmp> getPsnKwByPsnId(Long psnId) {
    String hql =
        "select new PsnKwEptTmp(id,psnId,keyWord,keyWordTxt,pt1Num,pt2Num, pt3Num,prjNum,kId,score) from PsnKwEptTmp t where t.psnId=:psnId and t.score>0 order by t.score desc";
    return super.createQuery(hql).setParameter("psnId", psnId).setMaxResults(50).list();

  }

  public void deleteAll() {
    String hql = "delete from PsnKwEptTmp";
    super.createQuery(hql).executeUpdate();

  }

  public void updatePsnKwEptTemp(Long psnId, int sourceType) {
    String hql = "update PsnKwEptTmp t set t.sourceType=:sourceType where t.psnId=:psnId and t.sourceType is null";
    super.createQuery(hql).setParameter("sourceType", sourceType).setParameter("psnId", psnId).executeUpdate();

  }

  public Long getPubKeyWordCount(int sourceType, String keywordTxt, Long psnId) {
    String hql =
        "select count(distinct t.pubId) from PsnKwEptTmp t where t.keyWordTxt=:keywordTxt and t.sourceType=:sourceType and t.psnId=:psnId";
    return (Long) super.createQuery(hql).setParameter("keywordTxt", keywordTxt).setParameter("sourceType", sourceType)
        .setParameter("psnId", psnId).uniqueResult();
  }

}
