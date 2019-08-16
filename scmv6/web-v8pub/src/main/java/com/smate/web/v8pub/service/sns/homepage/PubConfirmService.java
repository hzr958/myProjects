package com.smate.web.v8pub.service.sns.homepage;

import com.smate.core.base.utils.exception.PubException;
import com.smate.web.v8pub.exception.ServiceException;
import com.smate.web.v8pub.vo.PubListVO;

/**
 * 成果认领服务
 * 
 * @author aijiangbin
 * @date 2018年8月9日
 */
public interface PubConfirmService {

  /**
   * 获取未认领的成果
   */
  public void queryPubComfirm(PubListVO pubListVO);

  /**
   * 确认成果认领（界面中：是我的成果） 1.更新成果认领表状态 2.将基准库成果导入个人库中
   * 
   * @param pubId
   */
  public String affirmPubComfirm(String des3PdwhPubId, String des3PsnId) throws ServiceException;

  /**
   * 更新成果认领结果
   * 
   * @param pdwhPubId
   * @param result
   * @throws ServiceException
   */
  public void updateConfirmResult(Long pdwhPubId, Long psnId, Long snsPubId, Integer result) throws ServiceException;

  /**
   * 忽略成果认领
   * 
   * @param des3PdwhPubId
   * @param des3PsnId
   * @return
   */
  public String ignorePubComfirm(String des3PdwhPubId, String des3PsnId) throws ServiceException;

  /**
   * 获取成果认领数
   * 
   * @param Long psnId
   * @throws PubException
   */
  public Long getPubConfirmCount(Long psnId) throws ServiceException;

  /**
   * 检查是否有全文推荐
   * 
   * @param pubListVO
   * @return
   */
  public boolean checkPubfulltextConfirm(PubListVO pubListVO);
}
