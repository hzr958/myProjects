package com.smate.web.group.dao.grp.keywords;

import org.springframework.stereotype.Repository;

import com.smate.core.base.utils.data.SnsHibernateDao;
import com.smate.web.group.model.grp.keywords.KeywordsDicComWord;

/**
 * 通用关键词，主要是用于英文通用关键词过滤.
 * 
 * @author lqh
 * 
 */
@Repository
public class KeywordsDicComWordDao extends SnsHibernateDao<KeywordsDicComWord, Long> {

}
