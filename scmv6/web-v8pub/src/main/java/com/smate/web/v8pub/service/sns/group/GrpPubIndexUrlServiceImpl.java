package com.smate.web.v8pub.service.sns.group;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.smate.web.v8pub.dao.sns.group.GrpPubIndexUrlDAO;
import com.smate.web.v8pub.exception.ServiceException;
import com.smate.web.v8pub.po.sns.group.GrpPubIndexUrlPO;

/**
 * 群组成果短地址实现
 * 
 * @author YJ
 *
 *         2018年9月28日
 */
@Service(value = "grpPubIndexUrlService")
@Transactional(rollbackFor = Exception.class)
public class GrpPubIndexUrlServiceImpl implements GrpPubIndexUrlService {

  private Logger logger = LoggerFactory.getLogger(getClass());
  @Autowired
  private GrpPubIndexUrlDAO grpPubIndexUrlDAO;

  @Override
  public void saveOrUpdate(GrpPubIndexUrlPO grpPubIndexUrlPO) throws ServiceException {
    try {
      grpPubIndexUrlDAO.saveOrUpdate(grpPubIndexUrlPO);
    } catch (Exception e) {
      logger.error("保存群组成果短地址表出错！", e);
      throw new ServiceException(e);
    }
  }

  @Override
  public GrpPubIndexUrlPO get(Long pubId, Long grpId) throws ServiceException {
    try {
      return grpPubIndexUrlDAO.findByGrpIdAndPubId(grpId, pubId);
    } catch (Exception e) {
      logger.error("根据pubId和grpId获取群组成果短地址记录出错！pubId={},grpId={}", pubId, grpId, e);
      throw new ServiceException(e);
    }
  }

}
