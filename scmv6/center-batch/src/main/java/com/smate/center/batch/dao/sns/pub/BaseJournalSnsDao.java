package com.smate.center.batch.dao.sns.pub;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.hibernate.Query;
import org.springframework.stereotype.Repository;

import com.smate.center.batch.exception.pub.DaoException;
import com.smate.center.batch.model.pdwh.pub.AcJournal;
import com.smate.center.batch.model.sns.pub.BaseJournalSns;
import com.smate.core.base.utils.data.PdwhHibernateDao;


/**
 * sns冗余基础期刊数据dao
 * 
 * @author tsz
 * 
 */
@Repository
public class BaseJournalSnsDao extends PdwhHibernateDao<BaseJournalSns, Long> {

  @SuppressWarnings("unchecked")
  public List<BaseJournalSns> findBaseJournalSns(List<Long> ids) {
    String hql = "from BaseJournalSns where jouId in(:ids)";
    return super.createQuery(hql).setParameterList("ids", ids.toArray()).list();
  }

  /**
   * 新加期刊 匹配基础期刊
   * 
   * @param jname
   * @param issn
   * @return
   */
  public Long snsJnlMatchBaseJnlId(String jname, String issn) {
    String hql = "select jouId from BaseJournalSns  where (titleEn=? or titleXx=?) and (pissn=?)";
    return findUnique(hql, jname, jname, issn);
  }

  /**
   * 获取期刊自动提示内容
   * 
   * @param startWith
   * @param size
   * @return
   * @throws DaoException
   */
  @SuppressWarnings("unchecked")
  public List<AcJournal> getAcJournal(String startWith, int size) throws DaoException {
    boolean isEnglish = StringUtils.isAsciiPrintable(startWith);
    String hql = null;
    // 判断是否是非英文,查询本人数据()
    if (isEnglish) {
      hql = "from BaseJournalSns t where t.lowerTitleEn like ? order by t.pissn";
    } else {
      hql = "from BaseJournalSns t where t.lowerTitleXx like ? order by t.pissn";
    }
    Query query = super.createQuery(hql, new Object[] {startWith.trim().toLowerCase() + "%"});
    query.setMaxResults(size);
    List<AcJournal> newList = new ArrayList<AcJournal>();
    List<BaseJournalSns> list = query.list();
    // 赋予正确的值给name属性
    if (CollectionUtils.isNotEmpty(list)) {
      for (BaseJournalSns cr : list) {
        AcJournal acjnl = new AcJournal();
        acjnl.setCode(cr.getJouId());
        if (StringUtils.isNotBlank(cr.getPissn()))
          acjnl.setIssn(cr.getPissn());
        if (isEnglish) {
          acjnl.setName(cr.getTitleEn());
        } else {
          acjnl.setName(cr.getTitleXx());
        }
        newList.add(acjnl);
      }
    }
    return newList;
  }
}
