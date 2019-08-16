package com.smate.web.v8pub.service.pdwh;

import java.util.List;

import com.smate.web.v8pub.exception.ServiceException;
import com.smate.web.v8pub.po.pdwh.PdwhPubMemberPO;

/**
 * 基准库成果成员服务接口
 * 
 * @author aijiangbin
 * @date 2018年6月1日
 */
public interface PdwhPubMemberService {

  public List<PdwhPubMemberPO> findByPubId(Long pubId) throws ServiceException;

  /**
   * 通过pdwhPubId和psnId获取基准库成果作者对象
   * 
   * @param pdwhPubId 基准库成果id
   * @param psnId 作者的psnId
   * @return
   * @throws ServiceException
   */
  PdwhPubMemberPO getByPubIdAndPsnId(Long pdwhPubId, Long psnId) throws ServiceException;

  /**
   * 保存或更新基准库成果成员对象
   * 
   * @param pdwhpubMemberPO
   * @throws ServiceException
   */
  void saveMember(PdwhPubMemberPO pdwhpubMemberPO) throws ServiceException;

  /**
   * 删除所有的成果成员
   * 
   * @param pdwhpubMemberPO
   * @throws ServiceException
   */
  void deleteAllMember(Long pdwhPubId) throws ServiceException;

  public List<PdwhPubMemberPO> findPosMemberByPubId(Long pubId);
}
