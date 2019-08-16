package com.smate.web.v8pub.service.sns;

import com.smate.core.base.pub.vo.PubDetailVO;
import com.smate.web.v8pub.dom.sns.PubSnsDetailDOM;
import com.smate.web.v8pub.exception.ServiceException;
import com.smate.web.v8pub.service.BaseService;
import com.smate.web.v8pub.vo.PubCommentVO;

/**
 * 个人成果详情服务接口
 * 
 * @author houchuanjie
 * @date 2018/05/31 17:03
 */
public interface PubSnsDetailService extends BaseService<Long, PubSnsDetailDOM> {
  /**
   * 获取个人库成果评论列表
   * 
   * @param pubCommentVO
   * @param page
   * @throws ServiceException
   */
  public void getPubComment(PubCommentVO pubCommentVO) throws ServiceException;

  /**
   * 通过pubId主键 获取对象
   * 
   * @param object
   * @return
   * @throws ServiceException
   */
  public PubSnsDetailDOM getByPubId(Long pubId) throws ServiceException;

  public Long getCommentNumber(Long pubId) throws ServiceException;

  /**
   * 获取成果拥有者信息
   * 
   * @param pubDetailVO
   * @throws ServiceException
   */
  public void getPubOwnerInfo(PubDetailVO pubDetailVO) throws ServiceException;

  /**
   * 获取当前登录用户头像
   * 
   * @param pubDetailVO
   * @throws ServiceException
   */
  void getPsnAvatars(PubDetailVO pubDetailVO) throws ServiceException;

  /**
   * 拆分成果关键词
   * 
   * @param keywords
   * @return
   * @throws ServiceException
   */
  public String splitPubKeywords(String keywords) throws ServiceException;

  /**
   * 检测权限
   * 
   * @param pubDetailVO
   * @throws ServiceException
   */
  public void checkPubAuthority(PubDetailVO pubDetailVO) throws ServiceException;

  /**
   * 构建作者信息
   * 
   * @param pubId
   * @return
   * @throws ServiceException
   */
  String buildPubAuthorNames(Long pubId) throws ServiceException;

  /**
   * 查看是否是分享的 隐私成果
   * 
   * @param des3relationid
   * @return
   * @throws ServiceException
   */
  public boolean sharePubView(String des3relationid, Long pubId, Long pubOwnerPsnId, Long psnId)
      throws ServiceException;

  /**
   * 获取成果拥有者id
   * 
   * @param pubId
   * @return
   */
  public Long getPubOwnerPsnId(Long pubId);

  /**
   * 查看个人是否有隐私成果
   * 
   * @param psnId
   * @return
   */
  public boolean getPsnHasPrivatePub(Long psnId);

  /**
   * 构建成果单位信息
   * 
   * @param pubId
   * @return
   * @throws ServiceException
   */
  public String getPubInsName(Long pubId) throws ServiceException;

  /**
   * 更改成果权限
   * 
   * @param pubId
   * @return
   * @throws ServiceException
   */
  public String updatePubPermission(Long psnId, Long pubId) throws ServiceException;
}
