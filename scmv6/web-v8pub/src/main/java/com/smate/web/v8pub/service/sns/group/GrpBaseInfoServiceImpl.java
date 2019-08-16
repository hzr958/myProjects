package com.smate.web.v8pub.service.sns.group;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.smate.web.v8pub.dao.sns.group.GrpBaseInfoDAO;
import com.smate.web.v8pub.exception.ServiceException;
import com.smate.web.v8pub.po.sns.group.GrpBaseinfo;

@Service(value = "grpBaseInfoServiceImpl")
@Transactional(rollbackFor = Exception.class)
public class GrpBaseInfoServiceImpl implements GrpBaseInfoService {

  private Logger logger = LoggerFactory.getLogger(getClass());

  @Autowired
  private GrpBaseInfoDAO grpBaseInfoDAO;

  @Override
  public String getProjectNo(Long grpId) throws ServiceException {
    try {
      return grpBaseInfoDAO.getProjectNo(grpId);
    } catch (Exception e) {
      logger.error("群组基础信息服务：获取群组编号出错！grpId={}", grpId);
      throw new ServiceException(e);
    }
  }

  @Override
  public GrpBaseinfo getByGrpId(Long grpId) throws ServiceException {
    try {
      return grpBaseInfoDAO.get(grpId);
    } catch (Exception e) {
      logger.error("群组基础信息服务：获取群组基础信息出错！grpId={}", grpId);
      throw new ServiceException(e);
    }
  }

}
