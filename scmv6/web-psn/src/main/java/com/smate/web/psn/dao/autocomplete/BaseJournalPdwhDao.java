package com.smate.web.psn.dao.autocomplete;

import com.smate.core.base.utils.data.PdwhHibernateDao;
import com.smate.web.psn.exception.DaoException;
import com.smate.web.psn.model.autocomplete.AcJournal;
import com.smate.web.psn.model.pdwh.pub.BaseJournalPdwh;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.hibernate.Query;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;

/**
 * pdwh 基础期刊表
 */
@Repository
public class BaseJournalPdwhDao extends PdwhHibernateDao<BaseJournalPdwh, Long> {

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
            hql = "from BaseJournalPdwh t where lower( t.titleEn) like ? order by t.pissn";
        } else {
            hql = "from BaseJournalPdwh t where lower(t.titleXx) like ? order by t.pissn";
        }
        Query query = super.createQuery(hql, new Object[] { startWith.trim().toLowerCase() + "%" });
        query.setMaxResults(size);
        List<AcJournal> newList = new ArrayList<AcJournal>();
        List<BaseJournalPdwh> list = query.list();
        // 赋予正确的值给name属性
        if (CollectionUtils.isNotEmpty(list)) {
            for (BaseJournalPdwh cr : list) {
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
