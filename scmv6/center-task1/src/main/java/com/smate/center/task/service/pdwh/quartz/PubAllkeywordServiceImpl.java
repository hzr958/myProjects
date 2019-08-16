package com.smate.center.task.service.pdwh.quartz;

import com.smate.center.task.dao.pdwh.quartz.PubAllkeywordDao;
import com.smate.center.task.exception.ServiceException;
import com.smate.center.task.model.pdwh.quartz.PubAllkeyword;
import com.smate.core.base.utils.data.XmlUtil;
import com.smate.core.base.utils.pubHash.PubHashUtils;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Calendar;
import java.util.List;
import java.util.Set;

/**
 * 关键词拆分服务
 * 
 * @author warrior
 * 
 */
@Service("pubAllkeywordService")
@Transactional(rollbackFor = Exception.class)
public class PubAllkeywordServiceImpl implements PubAllkeywordService {

  protected final Logger logger = LoggerFactory.getLogger(getClass());

  @Autowired
  private PubAllkeywordDupService pubAllkeywordDupService;
  @Autowired
  private PubAllkeywordDao pubAllkeyworddao;

  @Override
  public void savePubAllKeyword(Long pubAllId, String keywords, Integer type, Integer pubYear) throws ServiceException {
    if (StringUtils.isBlank(keywords)) {
      return;
    }
    Set<String> kws = XmlUtil.splitKeywords(keywords);
    if (CollectionUtils.isEmpty(kws)) {
      return;
    }
    for (String keyword : kws) {
      keyword = StringUtils.substring(keyword, 0, 200);
      Long keywordHash = PubHashUtils.getKeywordHash(keyword);
      this.pubAllkeyworddao.save(new PubAllkeyword(pubAllId, keyword, keywordHash, type, pubYear));
      pubAllkeywordDupService.updateTf(keywordHash);
    }

  }

  @Override
  public List<PubAllkeyword> findPubAllByKwHash(List<Long> kwHashList) throws ServiceException {
    try {
      Calendar cal = Calendar.getInstance();
      int year = cal.get(Calendar.YEAR) - 3;
      // 匹配近3年的基准文献
      return pubAllkeyworddao.findPubAllIds(kwHashList, year, 500);
    } catch (Exception e) {
      logger.error("人员关键词匹配基准文献关键词出错", e);
    }
    return null;
  }

  @Override
  public List<String> getPubAllKwsById(Long puballId) throws ServiceException {
    return pubAllkeyworddao.getPubAllKwsById(puballId);
  }
}
