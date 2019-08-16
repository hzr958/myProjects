package com.smate.web.psn.dao.autocomplete;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.hibernate.Query;
import org.springframework.stereotype.Repository;

import com.smate.core.base.utils.data.SnsHibernateDao;
import com.smate.web.psn.model.autocomplete.AcJournal;
import com.smate.web.psn.model.pub.JournalPO;

@Repository
public class JournalDAO extends SnsHibernateDao<JournalPO, Long> {

  @SuppressWarnings({"unchecked", "rawtypes"})
  public List<AcJournal> queryJournal(String startWith, int size, Long psnId) {
    boolean isEnglish = StringUtils.isAsciiPrintable(startWith);
    String hql = null;
    // 判断是否是非英文,查询本人数据()
    if (isEnglish) {
      hql =
          "from JournalPO t where lower(t.enName) like ? and t.addPsnId=? and t.matchBaseJnlId is null order by t.id desc";
    } else {
      hql =
          "from JournalPO t where lower(t.zhName) like ? and t.addPsnId=? and t.matchBaseJnlId is null order by t.id desc";
    }
    Query query = super.createQuery(hql, new Object[] {startWith.trim().toLowerCase() + "%", psnId});
    query.setMaxResults(size);
    List<AcJournal> newList = new ArrayList<AcJournal>();
    List<JournalPO> list = query.list();
    // 赋予正确的值给name属性
    if (CollectionUtils.isNotEmpty(list)) {
      for (JournalPO cr : list) {
        AcJournal acjnl = new AcJournal();
        acjnl.setCode(cr.getId());
        if (StringUtils.isNotBlank(cr.getIssn()))
          acjnl.setIssn(cr.getIssn());
        if (isEnglish) {
          acjnl.setName(cr.getEnName());
        } else {
          acjnl.setName(cr.getZhName());
        }
        newList.add(acjnl);
      }
    }
    return newList;
  }
}
