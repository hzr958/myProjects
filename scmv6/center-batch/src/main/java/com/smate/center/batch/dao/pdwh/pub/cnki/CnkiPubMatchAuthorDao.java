package com.smate.center.batch.dao.pdwh.pub.cnki;

import java.util.List;

import org.apache.commons.collections4.CollectionUtils;
import org.hibernate.Query;
import org.springframework.stereotype.Repository;

import com.smate.center.batch.model.pdwh.pub.cnki.CnkiPubMatchAuthor;
import com.smate.core.base.utils.data.PdwhHibernateDao;

/**
 * CNKI成果拆分作者匹配表数据库操作类.
 * 
 * @author mjg
 * 
 */
@Repository
public class CnkiPubMatchAuthorDao extends PdwhHibernateDao<CnkiPubMatchAuthor, Long> {

  /**
   * 获取用户名称获取匹配的成果ID记录.
   * 
   * @param pubId
   * @return
   */
  @SuppressWarnings("unchecked")
  public List<CnkiPubMatchAuthor> getCnkiPubMatchAuthorList(Long pubId, String name) {
    String hql =
        "select new CnkiPubMatchAuthor(pubId,seqNo,authorPos) from CnkiPubMatchAuthor t where t.pubId=? and t.name=? ";
    Query query = createQuery(hql, pubId, name);
    return query.list();
  }

  /**
   * 获取作者名称匹配的作者.
   * 
   * @param psnZhNameList
   * @return
   */
  @SuppressWarnings("unchecked")
  public List<Long> getMatchAuthorPubList(List<String> psnZhNameList) {
    // 取匹配的成果.
    String hql = "select pubId from CnkiPubMatchAuthor t where t.name in (:name) ";
    List<Long> authorNameList = super.createQuery(hql).setParameterList("name", psnZhNameList).list();
    return authorNameList;
  }

  /**
   * 获取成果的作者列表.
   * 
   * @param pubId
   * @return
   */
  @SuppressWarnings("unchecked")
  public List<CnkiPubMatchAuthor> getMatchedAuthorList(Long pubId) {
    String hql = "from CnkiPubMatchAuthor t where t.pubId=? ";
    Query query = createQuery(hql, pubId);
    return query.list();
  }

  /**
   * 获取成果作者记录.
   * 
   * @param pubId
   * @param name
   * @return
   */
  public CnkiPubMatchAuthor getCnkiPubMatchAuthor(Long pubId, String name) {
    String hql = "from CnkiPubMatchAuthor t where t.pubId=? and t.name=? ";
    List<CnkiPubMatchAuthor> authorList = super.find(hql, pubId, name);
    if (CollectionUtils.isNotEmpty(authorList)) {
      return authorList.get(0);
    }
    return null;
  }

  /**
   * 保存CNKI成果作者信息.
   * 
   * @param author
   */
  public void saveCnkiPubMatchAuthor(CnkiPubMatchAuthor author) {
    CnkiPubMatchAuthor mauthor = this.getCnkiPubMatchAuthor(author.getPubId(), author.getName());
    if (mauthor != null) {
      mauthor.setAddr(author.getAddr());
      mauthor.setAuthorPos(author.getAuthorPos());
      mauthor.setSeqNo(author.getSeqNo());
      super.getSession().update(mauthor);
    } else {
      super.save(author);
    }
  }
}
