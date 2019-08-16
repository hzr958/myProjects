package com.smate.web.v8pub.service.pdwh;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.smate.web.v8pub.dao.pdwh.PdwhPubMemberDAO;
import com.smate.web.v8pub.exception.ServiceException;
import com.smate.web.v8pub.po.pdwh.PdwhPubMemberPO;

/**
 * 基准库成果成员服务实现类
 * 
 * @author aijiangbin
 * @date 2018年6月1日
 */
@Service
@Transactional(rollbackFor = Exception.class)
public class PdwhPubMemberServiceImpl implements PdwhPubMemberService {
  private Logger logger = LoggerFactory.getLogger(getClass());

  @Autowired
  private PdwhPubMemberDAO pdwhPubMemberDAO;
  @Autowired
  private PdwhMemberInsNameService pdwhMemberInsNameService;

  @Override
  public List<PdwhPubMemberPO> findByPubId(Long pubId) throws ServiceException {
    try {
      List<PdwhPubMemberPO> list = pdwhPubMemberDAO.findByPubId(pubId);
      return list;
    } catch (Exception e) {
      logger.error("基准库成果成员服务类：通过成果id 查找所有的成果成员异常", e);
      throw new ServiceException(e);
    }
  }

  @Override
  public PdwhPubMemberPO getByPubIdAndPsnId(Long pdwhPubId, Long psnId) throws ServiceException {
    try {
      PdwhPubMemberPO pdwhPubMemberPO = pdwhPubMemberDAO.getByPubIdAndPsnId(pdwhPubId, psnId);
      return pdwhPubMemberPO;
    } catch (Exception e) {
      logger.error("基准库成果成员服务类：通过pdwhPubId和psnId 获取成果成员对象异常 pdwhPubId={},psnId={}", new Object[] {pdwhPubId, psnId});
      throw new ServiceException(e);
    }
  }

  @Override
  public void saveMember(PdwhPubMemberPO pdwhpubMemberPO) throws ServiceException {
    try {
      pdwhPubMemberDAO.save(pdwhpubMemberPO);
    } catch (Exception e) {
      logger.error("基准库成果成员服务类：保存或更新成果成员对象异常 PdwhpubMemberPO={}", pdwhpubMemberPO);
      throw new ServiceException(e);
    }
  }

  @Override
  public void deleteAllMember(Long pdwhPubId) throws ServiceException {
    try {
      // 先删除成果作者单位信息数据
      pdwhMemberInsNameService.deleteAll(pdwhPubId);
      pdwhPubMemberDAO.deleteAllMember(pdwhPubId);
    } catch (Exception e) {
      logger.error("基准库成果成员服务类：删除全部成果成员对象异常 PdwhpubMemberPO={}", pdwhPubId);
      throw new ServiceException(e);
    }
  }

  @Override
  public List<PdwhPubMemberPO> findPosMemberByPubId(Long pdwhPubId) throws ServiceException {
    try {
      List<PdwhPubMemberPO> list = pdwhPubMemberDAO.findPosMemberByPubId(pdwhPubId);
      return list;
    } catch (Exception e) {
      logger.error("基准库成果成员服务类：通过成果id查找通讯成员 pdwhPubId:", pdwhPubId);
      throw new ServiceException(e);
    }


  }
}
