package com.smate.web.v8pub.service.pdwh;

import java.util.Date;
import java.util.List;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.smate.core.base.pub.po.dao.PubPdwhSnsRelationDAO;
import com.smate.core.base.utils.security.Des3Utils;
import com.smate.web.v8pub.dao.pdwh.PdwhPubShareDAO;
import com.smate.web.v8pub.exception.ServiceException;
import com.smate.web.v8pub.po.pdwh.PdwhPubSharePO;
import com.smate.web.v8pub.service.sns.PubShareService;
import com.smate.web.v8pub.vo.pdwh.PdwhPubOperateVO;
import com.smate.web.v8pub.vo.sns.PubOperateVO;

/**
 * 基准库成果分享服务实现类
 * 
 * @author aijiangbin
 * @date 2018年6月1日
 */
@Service
@Transactional(rollbackFor = Exception.class)
public class PdwhPubShareServiceImpl implements PdwhPubShareService {
  private Logger logger = LoggerFactory.getLogger(getClass());

  @Autowired
  private PdwhPubShareDAO pdwhPubShareDAO;
  @Autowired
  private PdwhPubStatisticsService newPdwhPubStatisticsService;
  @Autowired
  private PubPdwhSnsRelationDAO pubPdwhSnsRelationDAO;
  @Autowired
  private PubShareService pubShareService;

  @Override
  public void save(PdwhPubSharePO pubShare) throws ServiceException {
    try {
      pdwhPubShareDAO.save(pubShare);
    } catch (Exception e) {
      logger.error("基准库成果分享服务：添加分享记录异常,pubId" + pubShare.getPdwhPubId(), e);
      throw new ServiceException(e);
    }

  }

  /**
   * 基准库成果 分享回调
   */
  @Override
  public void pdwhShareOpt(PdwhPubOperateVO pdwhPubOperateVO) throws ServiceException {
    Long pdwhPubId = pdwhPubOperateVO.getPdwhPubId();
    try {
      pdwhShare(pdwhPubOperateVO, pdwhPubId);
      // 个人库数据同步
      sysToSnsShare(pdwhPubOperateVO);
    } catch (Exception e) {
      logger.error("成果分享回调异常,pdwhPubId=" + pdwhPubId, e);
      throw new ServiceException(e);
    }

  }

  @Override
  public void pdwhShare(PdwhPubOperateVO pdwhPubOperateVO, Long pdwhPubId) throws ServiceException {
    if (StringUtils.isNotBlank(pdwhPubOperateVO.getSharePsnGroupIds())) {
      String[] sharePsnGroupIds = pdwhPubOperateVO.getSharePsnGroupIds().split(",");
      if (sharePsnGroupIds.length > 0) {
        for (String sharePsnGroupId : sharePsnGroupIds) {
          pdwhPubOperateVO.setSharePsnGroupId(Long.parseLong(Des3Utils.decodeFromDes3(sharePsnGroupId)));
          // 往成果分享表插入记录
          insertPdwhShare(pdwhPubOperateVO);
          // 更新成果统计表 分享数
          newPdwhPubStatisticsService.updateShareStatistics(pdwhPubId);
        }
      } else {// 分享到群组、动态等 插入一条分享记录
        insertPdwhShare(pdwhPubOperateVO);
        newPdwhPubStatisticsService.updateShareStatistics(pdwhPubId);
      }
    } else {
      insertPdwhShare(pdwhPubOperateVO);
      newPdwhPubStatisticsService.updateShareStatistics(pdwhPubId);
    }
  }

  public void sysToSnsShare(PdwhPubOperateVO pdwhPubOperateVO) throws Exception {
    List<Long> snsPubIds = pubPdwhSnsRelationDAO.getSnsOpenPubIds(pdwhPubOperateVO.getPdwhPubId(), 0L);
    PubOperateVO pubOperateVO = new PubOperateVO();
    pubOperateVO.setPsnId(pdwhPubOperateVO.getPsnId());
    pubOperateVO.setSharePsnGroupIds(pdwhPubOperateVO.getSharePsnGroupIds());
    pubOperateVO.setComment(pdwhPubOperateVO.getComment());
    pubOperateVO.setPlatform(pdwhPubOperateVO.getPlatform());
    if (CollectionUtils.isNotEmpty(snsPubIds)) {
      for (Long snsPubId : snsPubIds) {
        pubShareService.updateSnsShareStatistics(pubOperateVO, snsPubId);// 个人库数据同步
      }
    }
  }

  private void insertPdwhShare(PdwhPubOperateVO pdwhPubOperateVO) {
    try {
      PdwhPubSharePO pdwhPubShare = new PdwhPubSharePO();
      if (StringUtils.isNotBlank(String.valueOf(pdwhPubOperateVO.getSharePsnGroupId()))) {
        pdwhPubShare.setSharePsnGroupId(pdwhPubOperateVO.getSharePsnGroupId());
      }
      pdwhPubShare.setPdwhPubId(pdwhPubOperateVO.getPdwhPubId());
      pdwhPubShare.setPsnId(pdwhPubOperateVO.getPsnId());
      pdwhPubShare.setComment(pdwhPubOperateVO.getComment());
      pdwhPubShare.setPlatform(pdwhPubOperateVO.getPlatform());
      pdwhPubShare.setStatus(0);// 状态 0=正常 ; 9=删除
      pdwhPubShare.setGmtCreate(new Date());
      pdwhPubShare.setGmtModified(new Date());
      save(pdwhPubShare);
    } catch (Exception e) {
      logger.error("基准库成果分享插入异常,pdwhPubId=" + pdwhPubOperateVO.getPdwhPubId(), e);
    }
  }

}
