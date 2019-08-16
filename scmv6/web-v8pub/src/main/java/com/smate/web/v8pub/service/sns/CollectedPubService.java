package com.smate.web.v8pub.service.sns;

import java.util.Map;

import com.smate.core.base.pub.enums.PubDbEnum;
import com.smate.web.v8pub.exception.ServiceException;
import com.smate.web.v8pub.po.sns.CollectedPub;
import com.smate.web.v8pub.vo.PubListVO;
import com.smate.web.v8pub.vo.sns.PubOperateVO;

/**
 * 收藏论文服务
 * 
 * @author wcw
 *
 */
public interface CollectedPubService {

  /**
   * 收藏论文
   */
  public String savePub(CollectedPub pub) throws ServiceException;

  /**
   * 删除论文
   * 
   * @param pub
   */
  public boolean delPub(CollectedPub pub) throws ServiceException;

  /**
   * 收藏或删除论成果
   * 
   * @param pub
   */
  public Map<String, String> dealCollectedPub(PubOperateVO pubOperateVO) throws ServiceException;

  /**
   * 判断成果是否存在
   * 
   * @param pub
   * @return
   * @throws ServiceException
   */
  public boolean isDelPub(CollectedPub pub) throws ServiceException;

  public void getShowPubList(PubListVO pubListVO);

  /**
   * 查看人员对某条成果的收藏状态
   * 
   * @param psnId
   * @param pubId
   * @param pubDB
   * @return
   * @throws ServiceException
   */
  boolean hasCollectedPub(Long psnId, Long pubId, PubDbEnum pubDB) throws ServiceException;
}
