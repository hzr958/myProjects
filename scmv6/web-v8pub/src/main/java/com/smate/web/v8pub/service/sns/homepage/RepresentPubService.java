package com.smate.web.v8pub.service.sns.homepage;

import com.smate.web.v8pub.exception.ServiceException;
import com.smate.web.v8pub.vo.PubListVO;
import com.smate.web.v8pub.vo.RepresentPubVO;

/**
 * 代表成果service
 * 
 * @author aijiangbin
 * @date 2018年8月9日
 */
public interface RepresentPubService {

  /**
   * 查找公开的成果列表
   */
  public void findPsnOpenPubList(PubListVO pubListVO);

  /**
   * 查找个人代表成果
   */
  public void findPsnRepresentPub(RepresentPubVO representPubVO) throws Exception;

  /**
   * 保存人员代表性成果
   * 
   * @param psnId
   * @param pubIds
   * @return
   */
  public void savePsnRepresentPub(Long psnId, String encodePubIds);

  /**
   * 删除指定psnId和pubId的人员代表性成果
   * 
   * @throws ServiceException
   */
  public void deleteByPsnIdAndPubId(Long pubId, Long psnId) throws ServiceException;

}
