package com.smate.web.v8pub.service.pdwh;

import java.util.List;

import com.smate.core.base.pub.vo.PubDetailVO;
import com.smate.web.v8pub.dom.pdwh.PubPdwhDetailDOM;
import com.smate.web.v8pub.exception.ServiceException;
import com.smate.web.v8pub.service.BaseService;
import com.smate.web.v8pub.vo.PubCommentVO;

/**
 * 基准库成果详情服务接口
 * 
 * @author houchuanjie
 * @date 2018/05/31 17:03
 */
public interface PubPdwhDetailService extends BaseService<Long, PubPdwhDetailDOM> {
  /**
   * 获取基准库成果评论内容
   * 
   * @param pubCommentVO
   * @param page
   * @throws ServiceException
   */
  public void getPdwhComment(PubCommentVO pubCommentVO) throws ServiceException;

  /**
   * 通过对象id获取成果详情
   * 
   * @param string
   * @return
   */
  public PubPdwhDetailDOM getByPubId(Long pubId);

  /**
   * 获取基准库的成果评论数
   * 
   * @param pubId
   * @return
   * @throws ServiceException
   */

  public Long getPdwhCommentNumber(Long pubId) throws ServiceException;

  /**
   * 构建基准库作者信息
   * 
   * @param pubDetailVO
   * @throws ServiceException
   */
  public void buildPdwhPsnInfo(PubDetailVO pubDetailVO) throws ServiceException;

  /**
   * 拆分成果关键词
   * 
   * @param keywords
   * @return
   * @throws ServiceException
   */
  public String splitPubKeywords(String keywords) throws ServiceException;

  /**
   * 构建成果单位信息
   * 
   * @param pubId
   * @return
   * @throws ServiceException
   */
  public String getPubInsName(Long pubId) throws ServiceException;

  /**
   * 构建成果单位信息
   * 
   * @param pubId @return @throws
   */
  public List<String> getInsDetailsById(Long pubId);

  /**
   * 获取用户所有可能变位名称（包含英文）
   * 
   * @param psnId @return @throws
   */
  public List<String> getAllUserName(Long psnId);

  /**
   * 构建基准库评论信息
   * 
   * @param pubId
   * @param pubComments
   */
  public void buildPdwhComment(Long pubId, PubCommentVO pubCommentVO, List<PubCommentVO> pubComments)
      throws ServiceException;

  /**
   * 根据type 获取用户所有可能变位名称（包含英文）
   * 
   * @param psnId type @return @throws
   */
  public List<String> getUserNameByPsnIdByType(Long psnId, Integer type);
}
