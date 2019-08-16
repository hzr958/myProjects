package com.smate.center.batch.dao.pdwh.pub.isi;

import java.util.List;

import org.apache.commons.collections4.CollectionUtils;
import org.hibernate.Query;
import org.springframework.stereotype.Repository;

import com.smate.center.batch.model.pdwh.pub.isi.IsiPubMatchAuthor;
import com.smate.core.base.utils.data.PdwhHibernateDao;

/**
 * ISI成果拆分作者匹配表数据库操作类.
 * 
 * @author mjg
 * 
 */
@Repository
public class IsiPubMatchAuthorDao extends PdwhHibernateDao<IsiPubMatchAuthor, Long> {

  /**
   * 获取成果的作者列表.
   * 
   * @param pubId
   * @return
   */
  @SuppressWarnings("unchecked")
  public List<IsiPubMatchAuthor> getIsiPubMatchAuthorList(Long pubId) {
    String hql = "from IsiPubMatchAuthor t where t.pubId=? ";
    Query query = createQuery(hql, pubId);
    return query.list();
  }

  /**
   * 获取成果作者记录.
   * 
   * @param pubId
   * @param initName
   * @return
   */
  public IsiPubMatchAuthor getIsiPubMatchAuthor(Long pubId, String initName) {
    String hql = "from IsiPubMatchAuthor t where t.pubId=? and t.initName=? ";
    List<IsiPubMatchAuthor> authorList = super.find(hql, pubId, initName);
    if (CollectionUtils.isNotEmpty(authorList)) {
      return authorList.get(0);
    } else {
      return null;
    }
  }

  /**
   * 获取作者名称匹配的作者.
   * 
   * @param psnEnNameList
   * @return
   */
  @SuppressWarnings("unchecked")
  public List<Long> getMatchAuthorPubList(List<String> psnEnNameList) {
    // 取简称匹配的成果.
    String hql = "select pubId from IsiPubMatchAuthor t where t.initName in (:initName) ";
    List<Long> authorNameList = super.createQuery(hql).setParameterList("initName", psnEnNameList).list();
    // 取全称匹配的成果.
    hql = "select pubId from IsiPubMatchAuthor t where t.fullName in (:fullName) ";
    List<Long> fNameList = super.createQuery(hql).setParameterList("fullName", psnEnNameList).list();
    if (CollectionUtils.isNotEmpty(fNameList)) {
      authorNameList.addAll(fNameList);
    }
    return authorNameList;
  }

  /**
   * 保存成果作者记录.
   * 
   * @param author
   */
  public void savePubMatchAuthor(IsiPubMatchAuthor author) {
    IsiPubMatchAuthor mauthor = this.getIsiPubMatchAuthor(author.getPubId(), author.getInitName());
    if (mauthor != null) {
      mauthor.setAddr(author.getAddr());
      mauthor.setAuthorPos(author.getAuthorPos());
      mauthor.setInitName(author.getInitName());
      mauthor.setFullName(author.getFullName());
      mauthor.setPrefixName(author.getPrefixName());
      mauthor.setSeqNo(author.getSeqNo());
      super.getSession().update(mauthor);
    } else {
      super.save(author);
    }
  }
}
