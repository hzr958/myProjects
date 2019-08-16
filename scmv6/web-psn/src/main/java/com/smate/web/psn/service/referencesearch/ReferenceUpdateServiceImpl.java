package com.smate.web.psn.service.referencesearch;

import java.util.List;

import org.apache.commons.lang.ObjectUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.smate.web.psn.exception.ServiceException;
import com.smate.web.psn.model.consts.PublicationArticleType;
import com.smate.web.psn.model.pub.ConstRefDbFrom;
import com.smate.web.psn.service.profile.WorkHistoryService;

/**
 * 更新引用服务---------其他成果相关操作请去pub项目
 * 
 * @author WSN
 *
 */
@Service("referenceUpdateService")
@Transactional(rollbackFor = Exception.class)
public class ReferenceUpdateServiceImpl implements ReferenceUpdateService {

  private final Logger logger = LoggerFactory.getLogger(getClass());
  @Autowired
  private WorkHistoryService workHistoryService;
  @Autowired
  private ReferenceSearchService referenceSearchService;

  @Override
  public String getDBurl() throws ServiceException {
    String dburl = "";
    try {
      String articleType = ObjectUtils.toString(PublicationArticleType.OUTPUT);
      List<Long> insIdList = workHistoryService.findWorkByPsnId();
      List<ConstRefDbFrom> dbList = referenceSearchService.getDbList(articleType, insIdList);
      dburl = referenceSearchService.getDbUrl(dbList);
    } catch (Exception e) {
      logger.error("getDBurl error", e);
    }
    return dburl;
  }

}
