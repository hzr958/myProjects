package com.smate.core.base.pub.dao.pdwh;

import java.util.Date;
import java.util.List;

import org.springframework.stereotype.Repository;

import com.smate.core.base.pub.model.pdwh.PdwhPubKeywordDictionary;
import com.smate.core.base.utils.data.PdwhHibernateDao;

/**
 * 基准库成果关键词字典dao
 * 
 * @author aijiangbin
 *
 */
@Repository
public class PdwhPubKeywordDictionaryDao extends PdwhHibernateDao<PdwhPubKeywordDictionary, Long> {

  /**
   * 当前关键词是否存在
   * 
   * @param keyword
   * @return
   */
  public Boolean isExistByKeywordHashCode(String keywordHashCode) {
    String hql = "from PdwhPubKeywordDictionary t where  t.keywordHashCode =:keywordHashCode";
    List list = this.createQuery(hql).setParameter("keywordHashCode", keywordHashCode).list();
    if (list != null && list.size() > 0) {
      return true;
    }
    return false;
  }

  // 得到需要翻译的关键词
  public List<PdwhPubKeywordDictionary> getNeedTranslateKeywords(Integer maxSize) {
    String hql = "from PdwhPubKeywordDictionary t where t.status=0";
    List list = this.createQuery(hql).setMaxResults(maxSize).list();
    if (list != null && list.size() > 0) {
      return list;
    }
    return null;
  }

  // 得到需要翻译的关键词
  public List<PdwhPubKeywordDictionary> getKeywordsByPageNo(Integer pageNo, Integer maxSize) {
    String hql = "from PdwhPubKeywordDictionary t  where t.tempStatus=2 ";
    List list = this.createQuery(hql).setFirstResult((pageNo - 1) * maxSize).setMaxResults(maxSize).list();
    if (list != null && list.size() > 0) {
      return list;
    }
    return null;
  }

  public void translateKeywords(Long id, Integer language, String keywordGg, String keywordBd, String keywordTx) {
    String hql = " update PdwhPubKeywordDictionary t  ";

    if (language == 1) {
      hql += " set  t.zhKeywordGg =:keywordGg ,t.zhKeywordBd=:keywordBd,t.zhKeywordTx =:keywordTx ";
    } else {
      hql += " set  t.enKeywordGg =:keywordGg ,t.enKeywordBd=:keywordBd,t.enKeywordTx =:keywordTx ";
    }
    hql += " , t.updateDate=:updateDate  , t.status=1";
    hql += " where t.id=:id";
    this.createQuery(hql).setParameter("id", id).setParameter("keywordGg", keywordGg)
        .setParameter("keywordBd", keywordBd).setParameter("keywordTx", keywordTx)
        .setParameter("updateDate", new Date()).executeUpdate();

  }



}
