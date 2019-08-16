package com.smate.center.task.single.service.pub;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.smate.center.task.dao.sns.quartz.PublicationDao;
import com.smate.center.task.exception.ServiceException;
import com.smate.center.task.single.constants.PsnCnfConst;
import com.smate.center.task.single.service.person.PsnStatisticsService;
import com.smate.core.base.psn.model.PsnStatistics;
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
  private PsnStatisticsService psnStatisticsService;
  @Autowired
  private PublicationDao publicationDao;

  @Override
  public PsnStatistics getPsnStatistics(Long psnId) {
    psnId = psnId == null ? SecurityUtils.getCurrentUserId() : psnId;
    return psnStatisticsService.getPsnStatistics(psnId);
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

  @Override
  public Long getOpenPub(Long psnId) throws ServiceException {
    List<Integer> permissions = new ArrayList<Integer>();
    permissions.add(PsnCnfConst.ALLOWS);// 默认公开
    try {
      // 查询关联权限表的公开成果数
      Long pubCount1 = this.publicationDao.queryPsnPublicPubCount(psnId, null, null, permissions, null);
      // 获取因其它导入方式在权限表没有记录的成果数，这些成果默认为公开
      Long pubCount2 = this.publicationDao.getPsnNotExistsResumePubCount(psnId);

      return (pubCount1 + pubCount2);
    } catch (Exception e) {
      logger.error(String.format("查询用户psnId=${1}的公开成果数出现异常：", psnId), e);
      throw new ServiceException(e);
    }
  }

}
