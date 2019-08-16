package com.smate.sie.center.task.dao;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.smate.core.base.utils.data.SieHibernateDao;
import com.smate.sie.center.task.model.SieDataPubXml2JsonRefresh;

/**
 * 成果专利Xml数据转换为Json数据刷新表Dao层
 * 
 * @author lijianming
 * 
 */
@Repository
public class SieDataPubXml2JsonRefreshDao extends SieHibernateDao<SieDataPubXml2JsonRefresh, Long> {

    @SuppressWarnings("unchecked")
    public List<SieDataPubXml2JsonRefresh> queryNeedRefresh(int maxSize) {
        return super.createQuery("from SieDataPubXml2JsonRefresh t where t.status=0").setMaxResults(maxSize).list();
    }
}
