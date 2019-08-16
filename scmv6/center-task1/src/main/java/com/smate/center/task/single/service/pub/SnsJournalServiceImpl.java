package com.smate.center.task.single.service.pub;

import com.smate.center.task.dao.sns.psn.PsnJnlPsnRecScoreDao;
import com.smate.center.task.exception.ServiceException;
import com.smate.core.base.utils.pubHash.PubHashUtils;
import org.apache.commons.collections4.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

/**
 * 期刊入库原则：2010/12/08 <br/>
 * 所有用户公用一个期刊库，不再按用户区分.<br/>
 * 查找journal的逻辑 按jname和jissn的组合查询
 * 
 * @author yamingd
 * 
 */
@Service("snsJournalService")
@Transactional(rollbackFor = Exception.class)
public class SnsJournalServiceImpl implements SnsJournalService {

  private final Logger logger = LoggerFactory.getLogger(getClass());
  @Autowired
  private PsnJnlPsnRecScoreDao psnJnlPsnRecScoreDao;

  @Override
  public List<Long> getPnsIdKeywordHashList(Long psnId) throws ServiceException {
    try {
      List<String> kwList = psnJnlPsnRecScoreDao.getPsnKeyWordsByPsnId(psnId);
      if (CollectionUtils.isEmpty(kwList))
        return null;
      List<Long> hashList = new ArrayList<Long>(kwList.size());
      for (String keyword : kwList) {
        Long hashCode = PubHashUtils.getKeywordHash(keyword);
        hashList.add(hashCode);
      }
      return hashList;
    } catch (Exception e) {
      logger.error("获取psnId：{}关键词出错", psnId, e);
    }
    return null;
  }

}
