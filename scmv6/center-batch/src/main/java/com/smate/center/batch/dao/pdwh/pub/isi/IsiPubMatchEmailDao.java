package com.smate.center.batch.dao.pdwh.pub.isi;

import java.util.List;

import org.apache.commons.collections4.CollectionUtils;
import org.springframework.stereotype.Repository;

import com.smate.center.batch.model.pdwh.pub.isi.IsiPubMatchEmail;
import com.smate.core.base.utils.data.PdwhHibernateDao;

/**
 * ISI成果匹配表的邮件表数据库操作类.
 * 
 * @author mjg
 * 
 */
@Repository
public class IsiPubMatchEmailDao extends PdwhHibernateDao<IsiPubMatchEmail, Long> {

  /**
   * 根据邮件获取成果ID列表.
   * 
   * @param email
   * @return
   */
  public List<Long> getPubIdListByEmail(String email) {
    String hql = "select pubId from IsiPubMatchEmail t where t.email=? ";
    return super.find(hql, email);
  }

  /**
   * 根据成果ID获取成果作者的邮件和用户序号列表.
   * 
   * @param pubId
   * @return
   */
  public List<IsiPubMatchEmail> getEmailListByPubId(Long pubId) {
    String hql = "select new IsiPubMatchEmail(email,seqNo) from IsiPubMatchEmail t where t.pubId=? ";
    return super.find(hql, pubId);
  }

  /**
   * 获取成果邮件记录.
   * 
   * @param pubId
   * @param email
   * @return
   */
  public IsiPubMatchEmail getIsiPubMatchEmail(Long pubId, String email) {
    String hql = "from IsiPubMatchEmail t where t.pubId=? and t.email=? ";
    List<IsiPubMatchEmail> emailList = super.find(hql, pubId, email);
    if (CollectionUtils.isNotEmpty(emailList)) {
      return emailList.get(0);
    }
    return null;
  }

  @SuppressWarnings("unchecked")
  public List<String> findEmailListByPubIds(List<Long> pubIds) {
    String sql = "select t.email from isi_pub_match_email t where t.pub_id in(:pubIds)";
    return super.getSession().createSQLQuery(sql).setParameterList("pubIds", pubIds).list();
  }

  /**
   * 保存成果邮件记录.
   * 
   * @param email
   */
  public void savePubMatchEmail(IsiPubMatchEmail email) {
    IsiPubMatchEmail memail = this.getIsiPubMatchEmail(email.getPubId(), email.getEmail());
    if (memail != null) {
      memail.setSeqNo(email.getSeqNo());
      super.getSession().update(memail);
    } else {
      super.save(email);
    }
  }
}
