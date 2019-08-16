package com.smate.center.batch.dao.sns.pub;

import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Repository;

import com.smate.center.batch.model.sns.pub.ConstDisciplineKey;
import com.smate.core.base.utils.constant.ServiceConstants;
import com.smate.core.base.utils.data.HibernateDao;
import com.smate.core.base.utils.data.SnsHibernateDao;


/**
 * 学科关键字.
 * 
 * @author liqinghua
 * 
 */
@Repository(value = "constDisciplineKeyDao")
public class ConstDisciplineKeyDao extends SnsHibernateDao<ConstDisciplineKey, Long> {

  /**
   * 关键字匹配关键字列表.
   * 
   * @param keyWords
   * @return
   */
  @SuppressWarnings("unchecked")
  public List<ConstDisciplineKey> findAutoCdKey(String keyWords, String exclude, int size) {

    if (StringUtils.isNotBlank(exclude) && exclude.matches(ServiceConstants.IDPATTERN)) {
      return super.createQuery(
          "from ConstDisciplineKey where keyWords like ? and id not in(" + exclude + ") order by impactFactor desc ",
          "%" + keyWords + "%").setMaxResults(size).list();
    } else {
      return super.createQuery("from ConstDisciplineKey where keyWords like ? order by impactFactor desc",
          "%" + keyWords + "%").setMaxResults(size).list();
    }

  }

  /**
   * 获取关键字.
   * 
   * @param keyWords
   * @return
   */
  public ConstDisciplineKey findCdKeyByKeyWords(String keyWords) {

    return super.findUnique("from ConstDisciplineKey where keyWords = ? ", keyWords);
  }
}
