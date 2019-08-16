package com.smate.web.v8pub.service.sns;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.smate.core.base.pub.model.PubMemberPO;
import com.smate.web.v8pub.dao.sns.PubMemberDAO;
import com.smate.web.v8pub.exception.ServiceException;

/**
 * 成果成员服务实现类
 * 
 * @author aijiangbin
 * @date 2018年6月1日
 */
@Service
@Transactional(rollbackFor = Exception.class)
public class PubMemberServiceImpl implements PubMemberService {
  private Logger logger = LoggerFactory.getLogger(getClass());

  @Autowired
  public PubMemberDAO pubMemberDAO;

  @Override
  public void saveMember(PubMemberPO pubMemberPO) throws ServiceException {
    try {
      pubMemberDAO.saveOrUpdate(pubMemberPO);
    } catch (Exception e) {
      logger.error("成果成员服务类：保存or更新成果成员异常,pubId" + pubMemberPO.getPubId(), e);
      throw new ServiceException(e);
    }

  }

  @Override
  public void deleteById(Long id) throws ServiceException {
    try {
      pubMemberDAO.delete(id);
    } catch (Exception e) {
      logger.error("成果成员服务类：通过id删除成果成员异常,id=" + id, e);
      throw new ServiceException(e);
    }

  }

  @Override
  public List<PubMemberPO> findByPubId(Long pubId) throws ServiceException {
    try {
      List<PubMemberPO> list = pubMemberDAO.findByPubId(pubId);
      return list;
    } catch (Exception e) {
      logger.error("成果成员服务类：通过成果id 查找所有的成果成员异常", e);
      throw new ServiceException(e);
    }
  }

  @Override
  public PubMemberPO getByPubIdAndPsnId(Long pubId, Long psnId) throws ServiceException {
    try {
      PubMemberPO pubMemberPO = pubMemberDAO.getByPubIdAndPsnId(pubId, psnId);
      return pubMemberPO;
    } catch (Exception e) {
      logger.error("成果成员服务类：通过psnId,pubId 查找成果成员对象异常，psnId={},pubId={}", new Object[] {psnId, pubId}, e);
      throw new ServiceException(e);
    }
  }

  @Override
  public void deleteAllMember(Long pubId) throws ServiceException {
    try {
      pubMemberDAO.deleteAllMember(pubId);
    } catch (Exception e) {
      logger.error("成果成员服务类：删除所有的成果成员异常,pubId=" + pubId, e);
      throw new ServiceException(e);
    }
  }

}
