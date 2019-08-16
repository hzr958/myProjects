package com.smate.center.task.dao.sns.quartz;

import org.springframework.stereotype.Repository;

import com.smate.center.task.model.sns.pub.KeywordsDicComWord;
import com.smate.core.base.utils.data.SnsHibernateDao;

/**
 * 通用关键词，主要是用于英文通用关键词过滤.
 * 
 * @author lqh
 * 
 */
@Repository
public class KeywordsDicComWordDao extends SnsHibernateDao<KeywordsDicComWord, Long> {

}
