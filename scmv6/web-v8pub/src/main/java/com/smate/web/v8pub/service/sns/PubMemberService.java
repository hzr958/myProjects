package com.smate.web.v8pub.service.sns;

import java.util.List;

import com.smate.core.base.pub.model.PubMemberPO;
import com.smate.web.v8pub.exception.ServiceException;

/**
 * 成果成员服务接口
 * 
 * @author aijiangbin
 * @date 2018年6月1日
 */
public interface PubMemberService {

  /**
   * 保存作者
   * 
   * @param pubMemberPO
   * @throws ServiceException
   */
  public void saveMember(PubMemberPO pubMemberPO) throws ServiceException;

  /**
   * 删除所有的成果作者
   * 
   * @param pubId
   * @throws ServiceException
   */
  public void deleteAllMember(Long pubId) throws ServiceException;

  public void deleteById(Long id) throws ServiceException;

  public List<PubMemberPO> findByPubId(Long pubId) throws ServiceException;

  /**
   * 根据pubId和psnId获取成果成员对象
   * 
   * @param pubId
   * @param psnId
   * @return
   * @throws ServiceException
   */
  PubMemberPO getByPubIdAndPsnId(Long pubId, Long psnId) throws ServiceException;


}
