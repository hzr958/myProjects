package com.smate.center.batch.service.pub;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.smate.center.batch.dao.sns.prj.PublicationQueryDao;
import com.smate.center.batch.dao.sns.pub.PublicationDao;
import com.smate.center.batch.exception.pub.DaoException;
import com.smate.center.batch.exception.pub.ServiceException;
import com.smate.center.batch.model.sns.pub.ConstPubType;
import com.smate.center.batch.model.sns.pub.Publication;
import com.smate.center.batch.service.psn.PsnStatisticsService;
import com.smate.center.batch.util.pub.BasicRmtSrvModuleConstants;
import com.smate.core.base.psn.model.PsnStatistics;
import com.smate.core.base.utils.model.Page;
import com.smate.core.base.utils.security.SecurityUtils;

/**
 * @author yamingd 我的成果管理查询专用服务
 */
@Service("myPublicationQueryService")
@Transactional(rollbackFor = Exception.class)
@Lazy(false)
public class MyPublicationQueryServiceImpl implements MyPublicationQueryService, Serializable {

  /**
   * 
   */
  private static final long serialVersionUID = -3600233693942921006L;

  protected final Logger logger = LoggerFactory.getLogger(getClass());

  @Autowired
  private PublicationDao publicationDao;
  @Autowired
  private PublicationQueryDao publicationQueryDao;
  @Autowired
  private PsnStatisticsService psnStatisticsService;
  @Autowired
  private ConstPubTypeService publicationTypeService;
  @Autowired
  private PublicationWrapService publicationWrapService;

  @SuppressWarnings({"unchecked", "rawtypes"})
  @Override
  public List<Map> findRecordMap(int articleType) {
    List<Map> listMap = new ArrayList<Map>();
    List<String> pubLists = new ArrayList<String>();
    pubLists.add("ei");
    pubLists.add("sci");
    pubLists.add("istp");
    pubLists.add("ssci");
    for (String pubList : pubLists) {
      Map map = new HashMap();
      int count = this.publicationDao.getPubListNum(pubList, articleType, SecurityUtils.getCurrentUserId());
      map.put("code", pubList);
      map.put("count", count);
      listMap.add(map);
    }
    return listMap;
  }

  @SuppressWarnings({"unchecked", "rawtypes"})
  @Override
  public Map queryYearsMap(int articleType) {
    Map map = new HashMap();
    List<Map> yearsMap = this.publicationQueryDao.getMyPubYearGroup(SecurityUtils.getCurrentUserId(), articleType);
    map.put("list", yearsMap);
    map.put("notClassifiedCount", this.publicationDao.getNoPubYearNum(articleType, SecurityUtils.getCurrentUserId()));
    return map;
  }

  @Override
  public PsnStatistics getPsnStatistics(Long psnId) {
    psnId = psnId == null ? SecurityUtils.getCurrentUserId() : psnId;
    return psnStatisticsService.getPsnStatistics(psnId);
  }

  @Override
  public List<Publication> findPubsForNsfc(List<Long> pubIds) throws ServiceException {
    try {
      logger.debug("queryOutput : dbquery start at: {}", new Date());
      Page<Publication> page = new Page<Publication>();
      page.setResult(this.publicationQueryDao.findPubsForNsfc(pubIds));
      this.wrapQueryResultTypeName(page.getResult());
      this.wrapPopulateData(page, false, false);
      return page.getResult();

    } catch (Exception e) {
      logger.error(String.format("queryOutput查询成果错误。pubIds=%s", pubIds), e);
      throw new ServiceException(e);
    }
  }

  /**
   * 加载查询结果集的类别名称.
   * 
   * @param page
   * @throws ServiceException
   */
  @Override
  public void wrapQueryResultTypeName(Publication pe) throws ServiceException {

    ConstPubType type = publicationTypeService.get(pe.getTypeId());
    pe.setTypeName(type.getName());
  }

  /**
   * 加载查询结果集的类别名称.
   * 
   * @param page
   * @throws ServiceException
   */
  @Override
  public void wrapQueryResultTypeName(List<Publication> result) throws ServiceException {

    if (result != null && result.size() > 0) {
      for (Publication pe : result) {
        this.wrapQueryResultTypeName(pe);
      }
    }
  }

  @Override
  public void wrapPopulateData(Page<Publication> page, boolean isFillErrorField, boolean isViewUploadFulltext)
      throws ServiceException {
    List<Publication> outputs = page.getResult();
    if (outputs.size() == 0) {
      return;
    }
    for (int i = 0; i < outputs.size(); i++) {
      Publication item = outputs.get(i);
      if (item.getNodeId() == null) {
        item.setNodeId(BasicRmtSrvModuleConstants.SNS_MODULE_ID.intValue());
      }
      this.wrapPopulateDataItems(item, isFillErrorField, isViewUploadFulltext);
    }
  }

  @Override
  public void wrapPopulateDataItems(Publication item, boolean isFillErrorField, boolean isViewUploadFulltext)
      throws ServiceException {
    this.publicationWrapService.wrapPopulateDataItems(item, isFillErrorField, isViewUploadFulltext, null, null);
  }

  @Override
  public List<Publication> queryOutput(Long psnId, List<Long> pubIds) throws ServiceException {

    int articleType = 1;
    try {
      logger.debug("queryOutput : dbquery start at: {}", new Date());
      Page<Publication> page = new Page<Publication>();
      page.setResult(this.publicationQueryDao.searchPublication(psnId, articleType, pubIds));
      this.wrapQueryResultTypeName(page.getResult());
      this.wrapPopulateData(page, false, false);
      return page.getResult();

    } catch (DaoException e) {
      logger.error(String.format("queryOutput查询成果错误。psnId=%s,args=%s", psnId, pubIds), e);
      throw new ServiceException(e);
    } catch (ServiceException e) {
      logger.error(String.format("queryOutput查询成果错误。psnId=%s,args=%s", psnId, pubIds), e);
      throw new ServiceException(e);
    }
  }

  @Override
  public Integer getTotalCiteTimes() throws ServiceException {
    Long psnId = SecurityUtils.getCurrentUserId();
    return this.getTotalCiteTimes(psnId);
  }

  @Override
  public Integer getTotalCiteTimes(Long psnId) throws ServiceException {
    PsnStatistics psnStatistics = getPsnStatistics(psnId);
    return psnStatistics.getCitedSum();
  }

  @Override
  public Integer getHindex(Long psnId) throws ServiceException {
    PsnStatistics psnStatistics = getPsnStatistics(psnId);
    return psnStatistics.getHindex();
  }

}
