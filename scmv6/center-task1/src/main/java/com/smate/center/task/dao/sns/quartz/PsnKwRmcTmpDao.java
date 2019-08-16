package com.smate.center.task.dao.sns.quartz;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.smate.center.task.model.sns.quartz.PsnKwRmcTmp;
import com.smate.core.base.utils.data.SnsHibernateDao;

@Repository
public class PsnKwRmcTmpDao extends SnsHibernateDao<PsnKwRmcTmp, Long> {

  public void addNewZhToPubKwEptTmp(Long psnId) {
    StringBuffer sql = new StringBuffer();
    sql.append(
        "insert into  psn_kw_rmc_tmp(id, psn_id, keyword_txt,type,zt_tf,prj_tf,pub_tf,au_seq,au_pos,hxj,grade1,grade2,grade3,score) ");
    sql.append(
        "select  SEQ_PSN_KW_EPT_TMP.nextval,t3.*  from(select psn_id,zh_kw_txt keyword_txt,2 type,0 zt_tf,0 prj_tf,sum(t.weight ) pub_tf,min(t.au_seq) au_seq,max(t.au_pos) au_pos,max(t.hxj) hxj,0 grade1,0 grade2,0 grade3,0 score");
    sql.append(" from psn_kw_pub t where t.publish_year >= 2007 and t.psn_id=? and t.au_seq > 0 and t.zh_kw_len > 2 ");
    sql.append("group by t.psn_id, t.zh_kw_txt) t3");
    super.update(sql.toString(), new Object[] {psnId});
  }

  public void updateNewPubZhkwByPsnId(Long psnId) {
    String hql =
        "update PsnKwRmcTmp t set t.keyWord=(select t1.zhKw from PsnKwPub t1 where t.psnId=t1.psnId and t.keyWordTxt=t1.zhKwTxt and rownum=1) where t.psnId=:psnId and t.keyWord is null";
    super.createQuery(hql).setParameter("psnId", psnId).executeUpdate();

  }

  public void addNewEnToPubKwEptTmp(Long psnId) {
    StringBuffer sql = new StringBuffer();
    sql.append(
        "insert into  psn_kw_rmc_tmp(id, psn_id, keyword_txt,type,zt_tf,prj_tf,pub_tf,au_seq,au_pos,hxj,grade1,grade2,grade3,score) ");
    sql.append(
        "select  SEQ_PSN_KW_EPT_TMP.nextval,t3.* from(select psn_id,en_kw_txt keyword_txt,1 type,0 zt_tf,0 prj_tf,sum(t.weight ) pub_tf,min(t.au_seq) au_seq,max(t.au_pos) au_pos ,max(t.hxj) hxj,0 grade1,0 grade2,0 grade3,0 score ");
    sql.append("from psn_kw_pub t where t.publish_year >= 2007 and t.psn_id=?  and t.au_seq > 0 and t.EN_KW_LEN > 1 ");
    sql.append(
        " and not exists(select 1 from psn_kw_rmc_tmp t2 where t.psn_id = t.psn_id and t.en_kw_txt = t2.keyword_txt) group by t.psn_id, t.en_kw_txt)t3");
    super.update(sql.toString(), new Object[] {psnId});
  }

  public void updateNewPubEnkwByPsnId(Long psnId) {
    String hql =
        "update PsnKwRmcTmp t set t.keyWord=(select t1.enKw from PsnKwPub t1 where t.psnId=t1.psnId and t.keyWordTxt=t1.enKwTxt and rownum=1) where t.psnId=:psnId and t.keyWord is null";
    super.createQuery(hql).setParameter("psnId", psnId).executeUpdate();

  }

  public void addZhPrjToPubKwEptTmp(Long psnId) {
    StringBuffer sql = new StringBuffer();
    sql.append(
        "insert into  psn_kw_rmc_tmp(id, psn_id, keyword_txt,type,zt_tf,prj_tf,pub_tf,au_seq,au_pos,hxj,grade1,grade2,grade3,score) ");
    sql.append(
        "select  SEQ_PSN_KW_EPT_TMP.nextval,t3.*  from(select psn_id,zh_kw_txt keyword_txt,2 type,0 zt_tf,count(1) prj_tf,0 pub_tf,0 au_seq,0 au_pos,0 hxj,0 grade1,0 grade2,0 grade3,0 score");
    sql.append(
        " from psn_kw_nsfcprj t where t.prj_year >= 2007 and t.psn_id=?  and t.zh_kw_len > 2  and not exists(select 1 from psn_kw_rmc_tmp t2 where t.psn_id = t.psn_id and t.zh_kw_txt = t2.keyword_txt)");
    sql.append("group by t.psn_id, t.zh_kw_txt) t3");
    super.update(sql.toString(), new Object[] {psnId});
  }

  public void updateNewPrjZhkwByPsnId(Long psnId) {
    String hql =
        "update PsnKwRmcTmp t set t.keyWord=(select t1.zhKw from PsnKwNsfcprj t1 where t.psnId=t1.psnId and t.keyWordTxt=t1.zhKwTxt and rownum=1) where t.psnId=:psnId and t.keyWord is null";
    super.createQuery(hql).setParameter("psnId", psnId).executeUpdate();

  }

  public void addEnPrjToPubKwEptTmp(Long psnId) {
    StringBuffer sql = new StringBuffer();
    sql.append(
        "insert into  psn_kw_rmc_tmp(id, psn_id, keyword_txt,type,zt_tf,prj_tf,pub_tf,au_seq,au_pos,hxj,grade1,grade2,grade3,score) ");
    sql.append(
        "select  SEQ_PSN_KW_EPT_TMP.nextval,t3.*  from(select psn_id,en_kw_txt keyword_txt,1 type,0 zt_tf,count(1) prj_tf,0 pub_tf,0 au_seq,0 au_pos,0 hxj,0 grade1,0 grade2,0 grade3,0 score");
    sql.append(
        " from psn_kw_nsfcprj t where t.prj_year >= 2007 and t.psn_id=?  and t.en_kw_len > 1  and not exists(select 1 from psn_kw_rmc_tmp t2 where t.psn_id = t.psn_id and t.en_kw_txt = t2.keyword_txt)");
    sql.append("group by t.psn_id, t.en_kw_txt) t3");
    super.update(sql.toString(), new Object[] {psnId});
  }

  public void updateNewPrjEnkwByPsnId(Long psnId) {
    String hql =
        "update PsnKwRmcTmp t set t.keyWord=(select t1.enKw from PsnKwNsfcprj t1 where t.psnId=t1.psnId and t.keyWordTxt=t1.enKwTxt and rownum=1) where t.psnId=:psnId and t.keyWord is null";
    super.createQuery(hql).setParameter("psnId", psnId).executeUpdate();

  }

  public void addZhZtToPubKwEptTmp(Long psnId) {
    StringBuffer sql = new StringBuffer();
    sql.append(
        "insert into  psn_kw_rmc_tmp(id, psn_id, keyword_txt,type,zt_tf,prj_tf,pub_tf,au_seq,au_pos,hxj,grade1,grade2,grade3,score) ");
    sql.append(
        "select  SEQ_PSN_KW_EPT_TMP.nextval,t3.*  from(select psn_id,zh_kw_txt keyword_txt,2 type,1 zt_tf,0 prj_tf,0 pub_tf,0 au_seq,0 au_pos,0 hxj,0 grade1,0 grade2,0 grade3,0 score");
    sql.append(
        " from psn_kw_zt t where  t.psn_id=?  and t.zh_kw_len > 2  and not exists(select 1 from psn_kw_rmc_tmp t2 where t.psn_id = t.psn_id and t.zh_kw_txt = t2.keyword_txt)");
    sql.append("group by t.psn_id, t.ZH_KW_TXT) t3");
    super.update(sql.toString(), new Object[] {psnId});
  }

  public void updateNewZtZhkwByPsnId(Long psnId) {
    String hql =
        "update PsnKwRmcTmp t set t.keyWord=(select t1.zhKw from PsnKwZt t1 where t.psnId=t1.psnId and t.keyWordTxt=t1.zhKwTxt and rownum=1) where t.psnId=:psnId and t.keyWord is null";
    super.createQuery(hql).setParameter("psnId", psnId).executeUpdate();

  }

  public void addEnZtToPubKwEptTmp(Long psnId) {
    StringBuffer sql = new StringBuffer();
    sql.append(
        "insert into  psn_kw_rmc_tmp(id, psn_id, keyword_txt,type,zt_tf,prj_tf,pub_tf,au_seq,au_pos,hxj,grade1,grade2,grade3,score) ");
    sql.append(
        "select  SEQ_PSN_KW_EPT_TMP.nextval,t3.*  from(select psn_id,en_kw_txt keyword_txt,1 type,1 zt_tf,0 prj_tf,0 pub_tf,0 au_seq,0 au_pos,0 hxj,0 grade1,0 grade2,0 grade3,0 score");
    sql.append(
        " from psn_kw_zt t where  t.psn_id=?  and t.en_kw_len > 1  and not exists(select 1 from psn_kw_rmc_tmp t2 where t.psn_id = t.psn_id and t.en_kw_txt = t2.keyword_txt)");
    sql.append("group by t.psn_id, t.EN_KW_TXT) t3");
    super.update(sql.toString(), new Object[] {psnId});
  }

  public void updateNewZtEnkwByPsnId(Long psnId) {
    String hql =
        "update PsnKwRmcTmp t set t.keyWord=(select t1.enKw from PsnKwZt t1 where t.psnId=t1.psnId and t.keyWordTxt=t1.enKwTxt and rownum=1) where t.psnId=:psnId and t.keyWord is null";
    super.createQuery(hql).setParameter("psnId", psnId).executeUpdate();

  }

  @SuppressWarnings("unchecked")
  public List<PsnKwRmcTmp> getPsnKwEptTemp(Long psnId) {
    String hql = "select new PsnKwRmcTmp(id,keyWordTxt) from PsnKwRmcTmp where psnId=:psnId and wordNum is null";
    return super.createQuery(hql).setParameter("psnId", psnId).list();

  }

  public void updatePsnKwEptTemp(Long id, int wordNum) {
    String hql = "update PsnKwRmcTmp set wordNum=:wordNum where id=:id";
    super.createQuery(hql).setParameter("wordNum", wordNum).setParameter("id", id).executeUpdate();
  }

  public void deleteAll() {
    String sql = "truncate table psn_kw_rmc_tmp";
    super.update(sql);
  }

  // 补充项目关键词个数
  public void updatePrjKwNum() {
    StringBuffer str = new StringBuffer();
    str.append("update PsnKwRmcTmp t set t.prjTf=");
    str.append("(select count(1) from PsnKwNsfcprj t1 where t.psnId=t1.psnId and ");
    str.append("(t.keyWordTxt = t1.zhKwTxt or t.keyWordTxt = t1.enKwTxt))");
    str.append("where t.prjTf is null or t.prjTf=0");
    super.createQuery(str.toString()).executeUpdate();
  }

  public boolean getPsnKwRmcTmp(Long psnId, String txt) {
    String hql = "select count(1) from PsnKwRmcTmp t where t.psnId=:psnId and t.keyWordTxt=:keyWordTxt";
    Long count =
        (Long) super.createQuery(hql).setParameter("psnId", psnId).setParameter("keyWordTxt", txt).uniqueResult();
    if (count > 0) {
      return true;
    } else {
      return false;
    }

  }

  // 补充自填关键词标记
  public void updateZtKwNum() {
    StringBuffer str = new StringBuffer();
    str.append("update PsnKwRmcTmp t set t.ztTf=1 ");
    str.append(
        "where exists(select 1 from PsnKwZt t1 where t.psnId=t1.psnId and (t.keyWordTxt=t1.zhKwTxt or t.keyWordTxt=t1.enKwTxt)) ");
    str.append("and  t.ztTf is null or t.ztTf=0");
    super.createQuery(str.toString()).executeUpdate();
  }

  public void updateGrade1() {
    String hql =
        "update PsnKwRmcTmp t set t.grade1=1 where (t.ztTf>0 or prjTf>0) or (t.auSeq in(1,2,3) or t.auPos=1) and t.hxj=1";
    super.createQuery(hql).executeUpdate();
  }

  public void updateGrade2() {
    String hql = "update PsnKwRmcTmp t set t.grade2=1 where t.grade1=1 or (t.auSeq in(1,2,3) or t.auPos=1) ";
    super.createQuery(hql).executeUpdate();
  }

  public void updateGrade3() {
    String hql = "update PsnKwRmcTmp t set t.grade2=1 where t.grade1=1 or (t.auSeq in(1,2,3) or t.auPos=1) ";
    super.createQuery(hql).executeUpdate();
  }

  public void updateScore(Long psnId) {
    String sql =
        "update psn_kw_rmc_tmp t set t.score = (t.zt_tf + ln(1+t.prj_tf) + log(10,1+ 3 * t.pub_tf)) where t.psn_id =?";
    super.update(sql, new Object[] {psnId});
  }

  public void updateScoreType2(Long psnId) {
    String sql =
        " update psn_kw_rmc_tmp t set t.score = t.score * 0.618 where t.type = 2 and t.word_num <= 3 and t.psn_id = ?";
    super.update(sql, new Object[] {psnId});

  }

  public void updateScoreType1(Long psnId) {
    String sql =
        " update psn_kw_rmc_tmp t set t.score = t.score * 0.618 where t.type = 1 and t.word_num <= 1 and t.psn_id = ?";
    super.update(sql, new Object[] {psnId});

  }

  public List<PsnKwRmcTmp> queryZhKw(Long psnId) {
    String hql =
        "from PsnKwRmcTmp t where t.psnId=:psnId and t.type=2 order by t.grade1 desc,t.grade2 desc,t.grade3 desc,t.score desc";
    return super.createQuery(hql).setParameter("psnId", psnId).setMaxResults(20).list();

  }

  public List<PsnKwRmcTmp> queryEnKw(Long psnId) {
    String hql =
        "from PsnKwRmcTmp t where t.psnId=:psnId and t.type=1 order by t.grade1 desc,t.grade2 desc,t.grade3 desc,t.score desc";
    return super.createQuery(hql).setParameter("psnId", psnId).setMaxResults(20).list();

  }


}
